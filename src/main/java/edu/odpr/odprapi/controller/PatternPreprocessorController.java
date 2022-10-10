package edu.odpr.odprapi.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.odpr.odprapi.model.Pattern;
import edu.odpr.odprapi.repositories.PatternRepository;
import edu.odpr.odprapi.services.Greeting;
import edu.odpr.odprapi.services.PatternIEService;
import edu.odpr.odprapi.services.PatternPreprocessingService;

@RestController
public class PatternPreprocessorController {
    
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PatternRepository patternRepository;
    @Autowired
    private PatternPreprocessingService patternPreprocessingService;
    @Autowired
    private PatternIEService patternIEService;


    @GetMapping("/greeting2")
    public ResponseEntity<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        //return new Greeting(counter.incrementAndGet(), String.format(template, name));
        return new ResponseEntity<Greeting>(new Greeting(counter.incrementAndGet(), String.format(template, name+"hola")),null,HttpStatus.CREATED);
    }

    @GetMapping("/patterns")
    public List<Pattern> getPatterns() {
        //return new Greeting(counter.incrementAndGet(), String.format(template, name));
        return patternRepository.findAll();
    }

    @PostMapping("/savepattern/{patternName}")
    public Pattern savePattern(@PathVariable String patternName) {
        return patternRepository.save(new Pattern(patternName, "", "", "", null)); 
    }

    @PostMapping("/savepattern2/{patternName}")
    public Pattern savePattern2(@PathVariable String patternName, @RequestParam(name = "file", required = false) MultipartFile file) {
        String xmlFile = "";
        try {
            xmlFile = new String(file.getBytes());
            System.out.println("patron: "+ xmlFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return patternPreprocessingService.savePattern(new Pattern(patternName, xmlFile, "", "", null)); 
    }

    @PostMapping("/savepattern3/{patternName}")
    public Pattern savePattern3(@PathVariable String patternName, @RequestParam(name = "file", required = false) MultipartFile file,
                                    @RequestParam(name= "synonyms", required = false) String synonyms) {
        String xmlFile = "";
        try {
            xmlFile = new String(file.getBytes());
            System.out.println("patron: "+ xmlFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File convFile = new File( file.getOriginalFilename() );
        System.out.println("INICIO: "+convFile.exists());
        FileOutputStream fos;
        try {
            //fos = new FileOutputStream( convFile );
            //fos.write( file.getBytes() );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        JSONParser parser = new JSONParser();
        JSONObject jo = null;
        try {
            jo = (JSONObject) parser.parse(synonyms);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("ANTES DEL PROCESAMIENTO");
        Pattern p = patternIEService.processPattern(convFile,jo);
        p.setName(patternName);
        return patternPreprocessingService.savePattern(p); 
    }

    /*@GetMapping("/getpatterns")
    public ResponseEntity<Pattern> uploadFile3(@RequestParam(name = "file", required = false) MultipartFile file) {
        // update return type to ResponseEntity<ResponseMessage>
        
        //FileStorageService.storeFile(file);
        //storageService.storeFile(file);
        LoadOntologiesService loadOntologyService = new LoadOntologiesService();
        loadOntologyService.loadPatternOntologies();

        OWLOntologyRepository owlOntologyRepository = loadOntologyService.getOWLOntologyRepository();
        PatternRepository patternRepository = loadOntologyService.gPatternRepository();

        NameProcessor nameProcessor = new NameProcessor(owlOntologyRepository, patternRepository);
        nameProcessor.processPatterns();

        // test pattern
        Pattern p = patternRepository.getPatterns().get(0);
        System.out.println("Nombre patron: "+p.getPatternName());
        System.out.println("Lista de nombres de clases: "+p.getClassNames().toString());
        //

        return ResponseEntity.status(HttpStatus.OK).body(p);
    }*/
}
