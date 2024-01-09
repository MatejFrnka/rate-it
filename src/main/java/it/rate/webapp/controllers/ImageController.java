package it.rate.webapp.controllers;

import it.rate.webapp.services.GoogleImageService;
import java.nio.file.*;
import java.security.Principal;
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

  @PostMapping("interests/{interestId}/places/{placeId}")
  @PreAuthorize("hasAnyAuthority(@permissionService.ratePlace(#placeId))")
  public String uploadPlaceImage(
          @RequestParam("picture") MultipartFile file,
          @PathVariable Long interestId,
          @PathVariable Long placeId,
          Principal principal) {

    System.out.println(googleImageService.savePlaceImage(file, placeId));

    try {




//      File convertedFile = new File(file.getOriginalFilename());
//      file.transferTo(convertedFile);
//
//      Path smudla = Paths.get("src/main/java/it/rate/webapp/controllers/min.jpg");
//      File me = smudla.toFile();
//
//      
//
//      FileContent mediaContent = new FileContent("image/*", me);
//
//      com.google.api.services.drive.model.File uploadedFile = driveService.files().create(fileMeta, mediaContent ).execute();
//      var pepik = driveService.files().list().execute();


    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return "redirect:/interests/" + interestId + "/places/" + placeId;
  }
}
