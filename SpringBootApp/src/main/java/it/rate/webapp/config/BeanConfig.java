package it.rate.webapp.config;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class BeanConfig {

  @Value("${CREDENTIALS_PATH}")
  private String path;

  @Bean
  public Drive getGoogleDriveService() throws IOException {

    FileInputStream credentialFile;

    try {
      credentialFile = new FileInputStream(path);
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("Credentials not found");
    }

    GoogleCredentials credentials;
    try {
      credentials =
          ServiceAccountCredentials.fromStream(credentialFile)
              .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/drive"));

    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }

    HttpTransport httpTransport = new NetHttpTransport();
    HttpRequestInitializer httpRequestInitializer = new HttpCredentialsAdapter(credentials);

    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    return new Drive.Builder(httpTransport, jsonFactory, httpRequestInitializer)
        .setApplicationName("RateSpot")
        .build();
  }
}
