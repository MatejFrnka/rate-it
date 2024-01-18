package it.rate.webapp.services;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  String saveImage(MultipartFile image) throws IOException;


  List<String> getImages(String imageId);
}
