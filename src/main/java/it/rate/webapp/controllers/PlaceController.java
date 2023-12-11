package it.rate.webapp.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/{interestId}/places")
public class PlaceController {

    @GetMapping("/newplace")
    public String newPlacePage(@PathVariable Long interestId) {
        //todo: return view for new place creation
        return "todo";
    }

    @PostMapping("/newplace")
    public String createNewPlace(@PathVariable Long interestId) {
        //todo: RequestBody - place
        //todo: connect user that created new place with this place and Interest. Save to db.
        return "todo";
    }

    @GetMapping("/places/{placeId}")
    public String placeDetails(@PathVariable Long interestId, @PathVariable Long placeId) {
        //todo: find place by placeId, possibly no need to use interestId
        //todo: load view according to placeId
        //todo: load list of criteria
        return "todo";
    }

    @PostMapping("/places/{placeId}")
    public String placeVote(@PathVariable Long interestId, @PathVariable Long placeId) {
        //todo: accept updated list of ratings
        // todo: save new/updated ratings
        // todo: redirect to GET of place
        return "todo";
    }

    @GetMapping("/places/{placeId}/edit")
    public String editPlacePage(@PathVariable Long interestId, @PathVariable Long placeId, Model model) {
        //todo: return edit form
        return "todo";
    }

    @PutMapping("/places/{placeId}/edit")
    public String editPlace() {
        //todo: accept Place object, save(overwrite) with new values
        //todo: redirect to GET of edited place

        return "todo";
    }
}
