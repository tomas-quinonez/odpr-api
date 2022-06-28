package edu.odpr.odprapi.repository;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import edu.odpr.odprapi.model.Pattern;

@Repository
public class PatternRepository {
    
    private ArrayList<Pattern> repository;

    public PatternRepository() {
        this.repository = new ArrayList<>();
    }

    public void addPattern(Pattern p) {
        this.repository.add(p);
    }

    /*public Pattern getPattern(String name) {
        // implement
    }*/
}
