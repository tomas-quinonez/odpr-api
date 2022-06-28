package edu.odpr.odprapi.repositories;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.stereotype.Repository;

@Repository
public class OWLOntologyRepository {
    
    private ArrayList<OWLOntology> repository;
    private OWLOntologyManager manager;

    public OWLOntologyRepository() {
        this.repository = new ArrayList<OWLOntology>();
        this.manager = OWLManager.createOWLOntologyManager();
    }

    public void addOWLOntology(File patternFile) {
        OWLOntology o;
        try {
            o = manager.loadOntologyFromOntologyDocument(patternFile);
            this.repository.add(o);
        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<OWLOntology> getOWLOntologies() {
        return this.repository;
    }
}
