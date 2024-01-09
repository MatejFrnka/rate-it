package it.rate.webapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ImageService {
    String savePlaceImage(MultipartFile image, Long placeId);

    String saveInterestImage(MultipartFile image, Long interestId);

    File getImage(String imageId);
}
