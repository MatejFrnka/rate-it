package it.rate.webapp.controllers;

import it.rate.webapp.services.GoogleImageService;
import it.rate.webapp.services.PlaceService;
import java.io.IOException;
import java.nio.file.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController {
  
  private final GoogleImageService googleImageService;
  private final PlaceService placeService;

  
  @PostMapping("interests/{interestId}/places/{placeId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.ratePlace(#placeId))")
  public String uploadPlaceImage(
          @RequestParam("picture") MultipartFile file,
          @PathVariable Long interestId,
          @PathVariable Long placeId,
          Model model) {

    try {
      placeService.addImage(placeId, googleImageService.savePlaceImage(file, placeId));
    } catch (IOException e) {
      model.addAttribute("message", e.getMessage());
      return "error/page";
    }

    return "redirect:/interests/" + interestId + "/places/" + placeId;
  }
}
