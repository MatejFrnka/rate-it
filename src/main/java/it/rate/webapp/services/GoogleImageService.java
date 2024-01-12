package it.rate.webapp.services;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
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
  public String savePlaceImage(MultipartFile image, Long placeId) throws IOException {

    FileContent mediaContent;
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

    try {
      Path tempFilePath = Files.createTempFile("temp_" + currentUser, null);
      image.transferTo(tempFilePath);
      mediaContent = new FileContent("image/*", tempFilePath.toFile());
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }

    String fileName = currentUser + "_" + placeId + "_" + UUID.randomUUID();

    File fileMeta = new File();
    fileMeta.setName(fileName);
    fileMeta.setParents(List.of(UPLOAD_DIRECTORY));

    try {
      return driveService.files().create(fileMeta, mediaContent).execute().getId();
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }
  }

  public byte[] getImageById(String imageId) throws IOException {

    try {
      try(InputStream imageStream = driveService.files().get(imageId).executeMediaAsInputStream()){
        return imageStream.readAllBytes();
      }
    } catch (IOException e) {
      throw new IOException("Could not retrieve image from server");
  }
    }

  @Override
  public String saveInterestImage(MultipartFile image, Long interestId) {
    return null;
  }

  @Override
  public List<String> getImages(String imageId) {
    return null;
  }
}
