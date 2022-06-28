package edu.odpr.odprapi.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.odpr.odprapi.message.ResponseMessage;
import edu.odpr.odprapi.services.FileStorageService;
import edu.odpr.odprapi.services.Greeting;

@RestController
public class RecommenderController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    FileStorageService storageService;

    @GetMapping("/greeting")
    public ResponseEntity<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        //return new Greeting(counter.incrementAndGet(), String.format(template, name));
        return new ResponseEntity<Greeting>(new Greeting(counter.incrementAndGet(), String.format(template, name)),null,HttpStatus.CREATED);
    }

    @GetMapping("/getpatterns")
    public ResponseEntity<ResponseMessage> uploadFile3(@RequestParam(name = "file", required = false) MultipartFile file) {
        //FileStorageService.storeFile(file);
        storageService.storeFile(file);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage());
    }
}
