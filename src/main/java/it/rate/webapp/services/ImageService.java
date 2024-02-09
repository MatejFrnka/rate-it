package it.rate.webapp.services;

import it.rate.webapp.models.Interest;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  String saveImage(MultipartFile image, String userEmail) throws IOException;

  byte[] getImageById(String imageId);

  void deleteById(String imageId);

  String changeInterestImage(Interest interest, MultipartFile file, String userEmail);
}
