package edu.odpr.odprapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.odpr.odprapi.model.Pattern;
import edu.odpr.odprapi.repositories.PatternRepository;

@Service
@Transactional
public class PatternStorageService {
    
    @Autowired
    PatternRepository patternRepository;

    public Pattern savePattern(Pattern pattern) {
        return patternRepository.save(pattern);
    }

    public int deletePattern(String patternName) {
        return patternRepository.removeByName(patternName);
    }

    public Pattern getPattern(String patternName) {
        return patternRepository.getByName(patternName);
    }

    public List<Pattern> getAllPatterns() {
        return patternRepository.findAll();
    }
}
