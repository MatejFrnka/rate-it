package it.rate.webapp.services;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String savePlaceImage(MultipartFile image, Long placeId) throws IOException;

    String saveInterestImage(MultipartFile image, Long interestId);

    List<String> getImages(String imageId);
}
