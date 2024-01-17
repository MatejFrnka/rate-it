package it.rate.webapp.services;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import it.rate.webapp.exceptions.api.ApiServiceUnavailableException;
import it.rate.webapp.models.Interest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GoogleImageService implements ImageService {

  @Value("${UPLOAD_DIRECTORY}")
  private String UPLOAD_DIRECTORY;

  private final Drive driveService;


  @Override
  public String saveImage(MultipartFile image) throws IOException {

    FileContent mediaContent;
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

    Path tempFilePath = Files.createTempFile("temp_" + currentUser, null);
    image.transferTo(tempFilePath);
    mediaContent = new FileContent("image/*", tempFilePath.toFile());

    String fileName = currentUser + "_" + UUID.randomUUID();

    File fileMeta = new File();
    fileMeta.setName(fileName);
    fileMeta.setParents(List.of(UPLOAD_DIRECTORY));

    try {
      return driveService.files().create(fileMeta, mediaContent).execute().getId();
    } catch (IOException e) {
      throw new ApiServiceUnavailableException("Could not save image to the server");
    }
  }

  public String changeInterestImage(Interest interest, MultipartFile file) {

    String newImageId;

    try {
      newImageId = saveImage(file);
    } catch (IOException | ApiServiceUnavailableException e) {
      throw new ApiServiceUnavailableException("Could not save image to the server");
    }

    if (interest.getImageName() == null || interest.getImageName().isEmpty()) {
      return newImageId;
    } else {
      deleteById(interest.getImageName());
    }

    return newImageId;
  }

  public void deleteById(String imageId) {
    try {
      driveService.files().delete(imageId).execute();
    } catch (IOException e) {
      throw new ApiServiceUnavailableException("Could not delete image from server");
    }
  }

  public byte[] getImageById(String imageId) {

    try (InputStream imageStream = driveService.files().get(imageId).executeMediaAsInputStream()) {
      return imageStream.readAllBytes();
    } catch (IOException e) {
      throw new ApiServiceUnavailableException("Could not retrieve image from server");
    }
  }

  @Override
  public List<String> getImages(String imageId) {
    return null;
  }
}
