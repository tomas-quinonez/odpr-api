package edu.odpr.odprapi.exceptions;

import java.io.IOException;

public class FileFormatException extends IOException{
    
    public FileFormatException(String message) {
        super("FileFormatException - " + message);
    }
}
