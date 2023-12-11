package it.rate.webapp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/{interestId}/admin")
public class InterestAdminController {



    @GetMapping("/edit")
    public String editPageView(Model model) {

        //todo: add model attribute(Interest) according to id
        //todo: return page customization/create template
        return "todo";
    }

    @PutMapping("/edit")
    public String editPage() {
        //todo: update Interest object according to input changes
        //todo: redirect to GET interest page
        return "todo";
    }

    @GetMapping("/users")
    public String editUsersPage() {
        //todo: load list of users related to Interest
        //todo: users template - form to invite users
        return "todo";
    }



}
