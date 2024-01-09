package it.rate.webapp.controllers;

import it.rate.webapp.services.GoogleImageService;
import it.rate.webapp.services.PlaceService;
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
  
  private final GoogleImageService googleImageService;
  private final PlaceService placeService;

  @PostMapping("interests/{interestId}/places/{placeId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.ratePlace(#placeId))")
  public String uploadPlaceImage(
          @RequestParam("picture") MultipartFile file,
          @PathVariable Long interestId,
          @PathVariable Long placeId) {

    placeService.addImage(placeId, googleImageService.savePlaceImage(file, placeId));
    
    return "redirect:/interests/" + interestId + "/places/" + placeId;
  }
}
