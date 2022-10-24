package edu.odpr.odprapi.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.jsonldjava.utils.Obj;

public class ResponseHandler {
    
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, String errorReason) {
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("timestamp", ts.toString());
        map.put("status_code", status.value());
        map.put("message", message);
        map.put("error", errorReason);

        return new ResponseEntity<Object>(map, status);
    }
}
