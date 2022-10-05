package edu.odpr.odprapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.odpr.odprapi.model.Pattern;
import edu.odpr.odprapi.repositories.PatternRepository;

@Service
public class PatternPreprocessingService {
    
    @Autowired
    PatternRepository patternRepository;

    public Pattern savePattern(Pattern pattern) {
        return patternRepository.save(pattern);
    }
}
