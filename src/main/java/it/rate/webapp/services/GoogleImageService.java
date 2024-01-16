package it.rate.webapp.services;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import it.rate.webapp.exceptions.api.ApiServiceUnavailableException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GoogleImageService implements ImageService {

  @Value("${UPLOAD_DIRECTORY}")
  private String UPLOAD_DIRECTORY;

  private Drive driveService;

  public GoogleImageService(Drive driveService) {
    this.driveService = driveService;
  }

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
      throw new ApiServiceUnavailableException("Could save image to the server");
    }
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
