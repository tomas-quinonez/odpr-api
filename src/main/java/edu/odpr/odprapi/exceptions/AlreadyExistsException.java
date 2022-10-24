package edu.odpr.odprapi.exceptions;

public class AlreadyExistsException extends PatternPreprocessingException {
    
    public AlreadyExistsException(String message) {
        super("AlreadyExistsException - " + message);
    }
}
