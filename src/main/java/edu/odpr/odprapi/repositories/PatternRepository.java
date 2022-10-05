package edu.odpr.odprapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.odpr.odprapi.model.Pattern;

@Repository
public interface PatternRepository extends JpaRepository<Pattern, Integer>{
    
}
