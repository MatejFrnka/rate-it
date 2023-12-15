package it.rate.webapp.controllers;

import it.rate.webapp.models.Place;
import it.rate.webapp.services.PlaceService;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/{interestId}/places")
public class PlaceController {

  private final PlaceService placeService;

  @GetMapping("/new-place")
  public String newPlacePage(@PathVariable Long interestId, Place place, Model model) {

    model.addAttribute("place", place);
    model.addAttribute("method", "POST");
    model.addAttribute("action", "/" + interestId + "/places/new-place");
    model.addAttribute("title", "Edit page");

    return "placeForm";
  }

  @PostMapping("/new-place")
  public String createNewPlace(@PathVariable Long interestId, @ModelAttribute Place place) {

    Place createdPlace = placeService.saveNewPlace(place, interestId);

    return "redirect:/" + interestId + "/places/" + createdPlace.getId();
  }

  @GetMapping("/{placeId}")
  public String placeDetails(@PathVariable Long interestId, @PathVariable Long placeId) {
    // todo: find place by placeId, possibly no need to use interestId
    // todo: load view according to placeId
    // todo: load list of criteria
    return "place";
  }

  @PostMapping("/{placeId}")
  public String placeVote(@PathVariable Long interestId, @PathVariable Long placeId) {
    // todo: accept updated list of ratings
    // todo: save new/updated ratings
    // todo: redirect to GET of place
    return "redirect:/places/{placeId}";
  }

  @GetMapping("/{placeId}/edit")
  public String editPlacePage(
      @PathVariable Long interestId,
      @PathVariable Long placeId,
      Model model,
      Principal principal,
      HttpServletResponse response) {

    if (placeService.findById(placeId).isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      model.addAttribute("message", "This place doesn't exist");
    }

    if (!placeService.isCreator(principal.getName(), placeId)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "notAuthorized";
    }

    model.addAttribute("method", "PUT");
    model.addAttribute("action", "/" + interestId + "/places/" + placeId + "/edit");
    model.addAttribute("title", "Edit page");
    model.addAttribute("place", placeService.findById(placeId).get());

    return "placeForm";
  }

  @PutMapping("/{placeId}/edit")
  public String editPlace(@PathVariable Long interestId, @ModelAttribute Place place) {

    Place editedPlace = placeService.saveNewPlace(place, interestId);

    return "redirect:/" + interestId + "/places/" + editedPlace.getId();
  }
}
