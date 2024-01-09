package it.rate.webapp.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String savePlaceImage(MultipartFile image, Long placeId);

    String saveInterestImage(MultipartFile image, Long interestId);

    List<String> getImages(String imageId);
}
