package edu.odpr.odprapi.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/recommendpatterns")
    public ResponseEntity<Object> recommendPatterns(
            @RequestParam(name = "ontology", required = false) MultipartFile ontology) {
        try {
            /*File newFile = File.createTempFile("text", ".temp", new File("src\\main\\java\\edu\\odpr\\odprapi\\temp"));

            String[] args = { "Konclude.bat", "owllinkfile" };
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "Konclude.bat", "owllinkfile",
                    "-i", "Tesis/axiom_queries_met_prot.xml",
                    "-o", "Tesis/respuesta_met_prot.xml");
            pb.directory(new File("src\\main\\java\\edu\\odpr\\odprapi\\utils\\Konclude"));
            final StringBuffer sb = new StringBuffer();
            Process p;

            p = pb.start();
            int exitStatus = p.waitFor();
            System.out.println(exitStatus);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());*/

            List<Pattern> patternList = patternStorageService.getAllPatterns();
            /*for (Pattern p : patternList) {
                System.out.println(p.getClassNamesQueries());
                java.io.FileWriter fw = new java.io.FileWriter("src\\main\\java\\edu\\odpr\\odprapi\\temp\\ej.xml");
                fw.write(p.getClassNamesQueries());
                fw.close();
            }*/
            ontologyAnalysisService.analyseOntology(patternList, ontology.getInputStream());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseHandler.generateResponse(null, HttpStatus.CREATED, null);
    }
}
