package edu.odpr.odprapi.processors;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import edu.odpr.odprapi.model.Pattern;
import edu.odpr.odprapi.repositories.OWLOntologyRepository;
import edu.odpr.odprapi.repositories.PatternRepository;

public class NameProcessor {
    
    OWLOntologyRepository OWLOntologyRepository;
    PatternRepository patternRepository;

    public NameProcessor(OWLOntologyRepository owlOntologyRepository, PatternRepository patternRepository) {
        this.OWLOntologyRepository = owlOntologyRepository;
        this.patternRepository = patternRepository;
    }
    
    public void processPatterns() {
        ArrayList<OWLOntology> OWLOntologyList = this.OWLOntologyRepository.getOWLOntologies();
        for (OWLOntology owlOntology: OWLOntologyList) {
            IRI iri = owlOntology.getOntologyID().getOntologyIRI().get();
            Pattern newPattern = new Pattern(iri.getShortForm());
            Set<OWLClass> classes = owlOntology.getClassesInSignature();
            Set<OWLObjectProperty> objectProperties = owlOntology.getObjectPropertiesInSignature();

            for (OWLObjectProperty p: objectProperties) {
                System.out.println(p.getIRI().getShortForm());
            }

            for (OWLClass owlClass : classes) {
                //System.out.println(owlClass.getIRI().getShortForm());
                newPattern.addClassName(owlClass.getIRI().getShortForm());
            }

            newPattern.setClassLength(classes.size());

            try {
                this.addSynonyms(newPattern);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.patternRepository.addPattern(newPattern);
        }
    }

    private void addSynonyms(Pattern p) throws Exception {
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/synonyms.json"));
        JSONObject jo = (JSONObject) obj;
        JSONArray jArray = (JSONArray) jo.get("patterns");

        Iterator it = jArray.iterator();
        Iterator<Map.Entry> itr1;
        //System.out.println(it.hasNext());
        boolean nameMatch = false;
        while (!nameMatch && it.hasNext()) {
            //System.out.println("hola");
            itr1 = ((Map) it.next()).entrySet().iterator();
            Map.Entry pair = itr1.next();
            if ((p.getPatternName()).equalsIgnoreCase((String) pair.getKey())) {
                nameMatch = true;
                Iterator it3 = ((JSONArray) pair.getValue()).iterator();
                while (it3.hasNext()) {
                    p.addClassName((String) it3.next());
                }
            }

        }
        //System.out.println(p.getNames().toString());
    }

}
