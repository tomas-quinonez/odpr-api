package edu.odpr.odprapi.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.odpr.odprapi.model.Pattern;
import edu.odpr.odprapi.services.OntologyAnalysisService;
import edu.odpr.odprapi.services.PatternStorageService;
import edu.odpr.odprapi.utils.ResponseHandler;

@RestController
public class OntologyAnalysisController {

    @Autowired
    private PatternStorageService patternStorageService;
    @Autowired
    private OntologyAnalysisService ontologyAnalysisService;

    @GetMapping("/recommendpatterns")
    public ResponseEntity<Object> recommendPatterns(
            @RequestParam(name = "ontology", required = true) MultipartFile ontology) {
                // TODO poner lo de iri base en el informe
        LinkedList<Map<String, String>> recommendationList = new LinkedList<Map<String, String>>();
        try {
            List<Pattern> patternList = patternStorageService.getAllPatterns();
            recommendationList = ontologyAnalysisService.analyseOntology(patternList, ontology.getInputStream(), new String(ontology.getBytes()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseHandler.generateRecommendationResponse(recommendationList);
    }
}
