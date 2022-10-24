package edu.odpr.odprapi.exceptions;

public class PatternPreprocessingException extends RuntimeException {

    public PatternPreprocessingException(String message) {
        super("PatternPreprocessingException - " + message);
    }
}
