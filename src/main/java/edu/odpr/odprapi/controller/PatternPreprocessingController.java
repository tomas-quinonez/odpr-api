package edu.odpr.odprapi.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import edu.odpr.odprapi.exceptions.AlreadyExistsException;
import edu.odpr.odprapi.exceptions.FileFormatException;
import edu.odpr.odprapi.exceptions.PatternNotFoundException;
import edu.odpr.odprapi.model.Pattern;
import edu.odpr.odprapi.services.PatternPreprocessingService;
import edu.odpr.odprapi.services.PatternStorageService;
import edu.odpr.odprapi.utils.ResponseHandler;
import static edu.odpr.odprapi.utils.Utils.validateJSON;

@RestController
public class PatternPreprocessingController {

    @Autowired
    private PatternStorageService patternStorageService;
    @Autowired
    private PatternPreprocessingService patternPreprocessingService;

    @GetMapping("/getallpatterns")
    public List<Pattern> getPatterns() {
        return patternStorageService.getAllPatterns();
    }

    @GetMapping("getpattern/{patternName}")
    public Object getPattern(@PathVariable String patternName) {
        try {
            Pattern p = patternStorageService.getPattern(patternName);
            if (p == null) {
                throw new PatternNotFoundException("There is no pattern with name: "+patternName);
            }
        } catch (PatternNotFoundException e) {
            String errorReason = HttpStatus.BAD_REQUEST.getReasonPhrase();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, errorReason);
        }
        
        return patternStorageService.getPattern(patternName);
    }

    @DeleteMapping("/deletepattern/{patternName}")
    public int deletepattern(@PathVariable String patternName) {
        return patternStorageService.deletePattern(patternName);
    }

    @PostMapping("/savepattern/{patternName}")
    public ResponseEntity<Object> savePattern(@PathVariable String patternName,
            @RequestParam(name = "pattern", required = true) MultipartFile file,
            @RequestParam(name = "synonyms", required = false) String synonyms) {

        Pattern p;
        JSONObject jo;
        JSONParser parser = new JSONParser();

        try {
            if (!file.getContentType().equalsIgnoreCase("application/rdf+xml")) {
                throw new FileFormatException("The file must be in OWL format");
            }
            if (patternStorageService.getPattern(patternName) != null) {
                throw new AlreadyExistsException("A pattern already exists with name: " + patternName);
            }
            validateJSON(synonyms, "src/main/resources/synonymsSchema.json");
            jo = (JSONObject) parser.parse(synonyms);
            p = patternPreprocessingService.processPattern(file.getInputStream());

        } catch (FileNotFoundException | OWLOntologyCreationException e) {
            String errorReason = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, errorReason);
        } catch (JSONException | ParseException | AlreadyExistsException | FileFormatException e) {
            String errorReason = HttpStatus.BAD_REQUEST.getReasonPhrase();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, errorReason);
        } catch (IOException e) {
            String errorReason = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, errorReason);
        } catch (ValidationException e) {
            StringBuilder stringBuilder = new StringBuilder();
            e.getCausingExceptions().stream()
                    .map(ValidationException::getMessage)
                    .forEach(stringBuilder::append);
            String errorReason = HttpStatus.BAD_REQUEST.getReasonPhrase();
            return ResponseHandler.generateResponse(stringBuilder.toString(), HttpStatus.BAD_REQUEST, errorReason);
        }
        p.setName(patternName);
        p.setSynonyms(jo.toJSONString());
        patternStorageService.savePattern(p);

        return ResponseHandler.generateResponse("Pattern successfully saved", HttpStatus.CREATED, "No error");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public @ResponseBody ResponseEntity<Object> handleMissingServletRequestPartException(Exception  exception, HttpServletResponse response) {
        String errorReason = HttpStatus.BAD_REQUEST.getReasonPhrase();
        return ResponseHandler.generateResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, errorReason);
    }
}