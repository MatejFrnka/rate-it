package it.rate.webapp.Utils;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

public class FileConverter {

    public static File multipartFileToFile(MultipartFile multipartFile) {

        File file = new File(multipartFile.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (Exception e) {
      System.out.println("File conversion failed");
        }

        return file;
    }

}
