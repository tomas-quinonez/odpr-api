package edu.odpr.odprapi.exceptions;

public class PatternNotFoundException extends PatternPreprocessingException {
    
    public PatternNotFoundException(String message) {
        super("PatternNotFoundException - "+message);
    } 
}
