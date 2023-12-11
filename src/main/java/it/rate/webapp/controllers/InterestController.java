package it.rate.webapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestController {

    @GetMapping("/create")
    public String createPage() {
        //todo: send empty arraylist as an attribute. For criteria.
        return "todo";
    }

    @PostMapping("/create")
    public String createNew() {
        //todo: accept model of Interest(subsite), accept list of criteria (if possible?)
        //todo: add business logic to connect criteria with new subject and save them into DB
        //todo: redirect to /{id}

        return "todo";
    }

    @GetMapping("/{id}")
    public String interestView(Model model, @PathVariable Long id) {
        //todo: find interest object by id and send it as an attribute to view
        return "todo";
    }

    @PostMapping("/{id}/vote")
    public String vote(@PathVariable Long id) {
        //todo: change vote value according to input (either delete vote, create new one or change vote value)
        return "todo";
    }

    @GetMapping("/{interestId}/newplace")
    public String newPlacePage(@PathVariable Long interestId) {
        //todo: return view for new place creation
        return "todo";
    }

    @PostMapping("/{interestId}/newplace")
    public String createNewPlace(@PathVariable Long interestId) {
        //todo: RequestBody - place
        //todo: connect user that created new place with this place and Interest. Save to db.
        return "todo";
    }

    @GetMapping("/{interestId}/places/{placeId}")
    public String placeDetails(@PathVariable Long interestId,@PathVariable Long placeId) {
        //todo: find place by placeId, possibly no need to use interestId
        //todo: load view according to placeId
        return "todo";
    }

    @GetMapping("/{interestId}/places/{placeId}/edit")
    public String editPlacePage(@PathVariable Long interestId,@PathVariable Long placeId, Model model) {
        //todo: return edit form
        return "todo";
    }

    @PutMapping("/{interestId}/places/{placeId}/edit")
    public String editPlace() {
        //todo: accept Place object, save(overwrite) with new values
        //todo: redirect to GET of edited place

        return "todo";
    }




}
