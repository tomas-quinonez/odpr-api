package edu.odpr.odprapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.odpr.odprapi.model.Pattern;

public interface PatternRepository extends JpaRepository<Pattern, Integer>{
    
}
