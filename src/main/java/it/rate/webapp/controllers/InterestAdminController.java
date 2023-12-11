package it.rate.webapp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{interestId}/admin")
public class InterestAdminController {



    @GetMapping("/edit")
    public String editPage(Model model) {

        //todo: add model attribute(Interest)
        //todo: return page customization
        return "todo";
    }


}
