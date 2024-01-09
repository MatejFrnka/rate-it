package it.rate.webapp.services;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@PropertySource("classpath:application-dev.properties") //possible could be more dynamic
public class GoogleImageService implements ImageService {


  @Value("${UPLOAD_DIRECTORY}")
  private String UPLOAD_DIRECTORY;

  private Drive getGoogleDriveService() {

    FileInputStream credentialFile =
            null;
    try {
      credentialFile = new FileInputStream("src/main/resources/credentials.json");
    } catch (FileNotFoundException e) {
      System.out.println("Credentials not found");
    }

    GoogleCredentials credentials =
            null;
    try {
      credentials = ServiceAccountCredentials.fromStream(credentialFile)
          .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/drive"));

    } catch (IOException e) {
      System.out.println("Credentials cannot be created from stream");
    }

    HttpTransport httpTransport = new NetHttpTransport();
    HttpRequestInitializer httpRequestInitializer = new HttpCredentialsAdapter(credentials);
    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    return new Drive.Builder(httpTransport, jsonFactory, httpRequestInitializer)
        .setApplicationName("RateSpot")
        .build();

  }

  @Override
  public String savePlaceImage(MultipartFile image, Long placeId) {
    Drive driveService = getGoogleDriveService();
    FileContent mediaContent = null;
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

    try {
      Path tempFilePath = Files.createTempFile("temp", null);
      image.transferTo(tempFilePath);
      mediaContent = new FileContent("image/*", tempFilePath.toFile());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String fileName = currentUser + "_" + System.currentTimeMillis() + "_" + placeId;


    com.google.api.services.drive.model.File fileMeta = new com.google.api.services.drive.model.File();
    fileMeta.setName(fileName);
    fileMeta.setParents(List.of(UPLOAD_DIRECTORY));

    try {
      driveService.files().create(fileMeta, mediaContent ).execute();
    } catch (IOException e) {
      System.out.println("Could not upload");
    }

    return fileName;
  }

  @Override
  public String saveInterestImage(MultipartFile image, Long interestId) {
    return null;
  }

  @Override
  public File getImage(String imageId) {
    return null;
  }
}
