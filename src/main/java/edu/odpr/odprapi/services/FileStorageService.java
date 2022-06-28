package edu.odpr.odprapi.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  private Path root = Path.of("src/main/java/edu/odpr/odprapi/ontologies");

  public void storeFile(MultipartFile file) {
    // Path path = Path.of("src/main/resources/" + file.getOriginalFilename());
    // root.resolve(file.getOriginalFilename());
    // System.out.println(root.toString());
    try {
      Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
