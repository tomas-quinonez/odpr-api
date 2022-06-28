package edu.odpr.odprapi.services;

import java.io.File;

import org.springframework.stereotype.Service;

import edu.odpr.odprapi.repositories.OWLOntologyRepository;
import edu.odpr.odprapi.repositories.PatternRepository;

@Service
public class LoadOntologiesService {
    
    private File folder;
    private OWLOntologyRepository owlOntologyRepository;
    private PatternRepository patternRepository;

    public LoadOntologiesService() {
        this.folder = new File("src/main/resources/patterns");
        this.owlOntologyRepository = new OWLOntologyRepository();
        this.patternRepository = new PatternRepository();
    }

    public void loadPatternOntologies() {
        for (File patternFile: this.folder.listFiles()) {
            this.owlOntologyRepository.addOWLOntology(patternFile);
        }
    }

    public OWLOntologyRepository getOWLOntologyRepository() {
        return this.owlOntologyRepository;
    }

    public PatternRepository gPatternRepository() {
        return this.patternRepository;
    }

}
