package edu.odpr.odprapi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Utils {
    
    public static void validateJSON(String jsonString, String schemaPath) throws FileNotFoundException, ValidationException, JSONException {
        File schemaFile = new File(schemaPath);
        InputStream targetStream = new FileInputStream(schemaFile);

        JSONObject jsonSchema = new JSONObject(new JSONTokener(targetStream));

        JSONObject jsonSubject = new JSONObject(jsonString);
        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
    }
}
