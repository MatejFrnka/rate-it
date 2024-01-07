package it.rate.webapp.controllers;

import java.io.IOException;
import java.nio.file.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController {

  @PostMapping("interests/{interestId}/places/{placeId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.ratePlace(#placeId))")
  public String uploadPlaceImage(
      @RequestParam("picture") MultipartFile file,
      @PathVariable Long interestId,
      @PathVariable Long placeId) {

    // Testing purposes
    String testUploadDirectory =
        "C:\\Users\\Bened\\OneDrive\\Plocha\\testImageFolder\\" + file.getOriginalFilename();

    try {
      Path pathToDirectory = Paths.get(testUploadDirectory);
      file.transferTo(pathToDirectory);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return "redirect:/interests/" + interestId + "/places/" + placeId;
  }
}
