package it.rate.webapp.controllers;

import it.rate.webapp.services.RoleService;
import it.rate.webapp.services.UserService;
import java.io.IOException;
import java.nio.file.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping("interests/{interestId}/new-image")
    @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
    public String interestImageUploadView(@PathVariable Long interestId) {

        return "interest/imageUpload";
    }

    @PostMapping("interests/{interestId}/new-image")
    @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
    public String uploadNewInterestImage(
            @RequestParam("picture") MultipartFile file,
            @PathVariable Long interestId) {

        //So far for testing purposes
        String testUploadDirectory = "C:\\Users\\Bened\\OneDrive\\Plocha\\testImageFolder\\" + file.getOriginalFilename();

        try {
            Path pathToDirectory = Paths.get(testUploadDirectory);
            file.transferTo(pathToDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return "redirect:/interests/" + interestId;
    }
}
