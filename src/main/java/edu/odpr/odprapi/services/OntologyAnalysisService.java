package edu.odpr.odprapi.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.odpr.odprapi.model.Pattern;

@Service
public class OntologyAnalysisService {

    private void writeXMLFile(String fileName, String xmlContent) {
        String tempPath = "src\\main\\java\\edu\\odpr\\odprapi\\temp\\";
        BufferedWriter writer = null;
        File file = null;
        try {
            file = new File(tempPath + fileName);
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(xmlContent);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {

            }
        }
    }

    public void analyseOntology(List<Pattern> patternList, InputStream ontologyIS) {
        try {
            writeXMLFile("UserOntology.owl", new String(ontologyIS.readAllBytes(), StandardCharsets.UTF_8));

            for (Pattern p : patternList) {
                writeXMLFile("ClassNamesQueries.xml", p.getClassNamesQueries());
                writeXMLFile("OPNamesQueries.xml", p.getOpNamesQueries());
                writeXMLFile("AxiomQueries.xml", p.getAxiomsQueries());

                
            }
        } catch (Exception e) {

        }
    }
}
