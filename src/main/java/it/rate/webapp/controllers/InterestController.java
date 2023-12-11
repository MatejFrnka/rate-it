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





}
