package it.rate.webapp.services;

import static com.google.common.io.Files.getFileExtension;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import it.rate.webapp.exceptions.api.ApiServiceUnavailableException;
import it.rate.webapp.models.Interest;
import it.rate.webapp.utils.ImageEditor;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GoogleImageService implements ImageService {

  @Value("${UPLOAD_DIRECTORY}")
  private String UPLOAD_DIRECTORY;

  private final Drive driveService;
  private final ImageEditor imageEditor;

  @Override
  public String saveImage(@NotNull MultipartFile image, String userEmail) throws IOException {

    FileContent mediaContent;

    String originalFileName = image.getOriginalFilename();

    String fileExtension = getFileExtension(originalFileName);

    if (fileExtension.isEmpty()) {
      fileExtension = ".jpg";
    }
    Path tempFilePath = Files.createTempFile("temp_" + userEmail, "." + fileExtension);
    try {

      image.transferTo(tempFilePath);
      imageEditor.reduceImageSize(tempFilePath);

      mediaContent = new FileContent("image/*", tempFilePath.toFile());

      String fileName = userEmail + "_" + UUID.randomUUID() + fileExtension;

      File fileMeta = new File();
      fileMeta.setName(fileName);
      fileMeta.setParents(List.of(UPLOAD_DIRECTORY));
      return driveService.files().create(fileMeta, mediaContent).execute().getId();
    } catch (IOException e) {
      throw new ApiServiceUnavailableException("Could not save image to the server");
    } finally {
      Files.delete(tempFilePath);
    }
  }

  @Override
  public String changeInterestImage(Interest interest, MultipartFile file, String userEmail) {

    String newImageId;

    try {
      newImageId = saveImage(file, userEmail);
    } catch (IOException | ApiServiceUnavailableException e) {
      throw new ApiServiceUnavailableException("Could not save image to the server");
    }

    return newImageId;
  }

  @Override
  public void deleteById(String imageId) {
    try {
      driveService.files().delete(imageId).execute();
    } catch (IOException e) {
      throw new ApiServiceUnavailableException("Could not delete image from server");
    }
  }

  @Override
  @Cacheable("images")
  public byte[] getImageById(String imageId) {

    try (InputStream imageStream = driveService.files().get(imageId).executeMediaAsInputStream()) {
      return imageStream.readAllBytes();
    } catch (IOException e) {
      throw new ApiServiceUnavailableException("Could not retrieve image from server");
    }
  }
}
