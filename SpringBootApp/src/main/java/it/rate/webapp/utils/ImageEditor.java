package it.rate.webapp.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

@Component
public class ImageEditor {

  private final int MEGABYTE = 1024 * 1024;

  public void reduceImageSize(Path imagePath) throws IOException {

    long fileSizeBytes = Files.size(imagePath);

    double reductionRate = calculateReductionRate(fileSizeBytes);

    Thumbnails.of(imagePath.toFile())
        .outputQuality(reductionRate)
        .scale(1)
        .toFile(imagePath.toFile());
  }

  private double calculateReductionRate(long fileSizeByte) {

    if (fileSizeByte < MEGABYTE) {
      return 1.0;
    } else {
      return (double) MEGABYTE / fileSizeByte;
    }
  }
}
