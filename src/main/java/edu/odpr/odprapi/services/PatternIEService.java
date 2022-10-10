package edu.odpr.odprapi.services;

import java.io.File;
import java.io.FileReader;
import java.util.Set;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.annotations.SourceType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.SslConfigurationValidator;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.odpr.odprapi.model.Pattern;

@Service
public class PatternIEService {

    @Autowired
    private PatternPreprocessingService patternPreprocessingService;

    public Pattern processPattern(File f, JSONObject jo) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        XMLManager classNamesXMLManager = new XMLManager();
        XMLManager objectPropNamesXMLManager = new XMLManager();
        XMLManager axiomsXMLManager = new XMLManager();
        Pattern p = null;
        try {
            OWLOntology o = manager.loadOntologyFromOntologyDocument(f);
            IRI iri = o.getOntologyID().getOntologyIRI().get();

            Set<OWLClass> classes = o.getClassesInSignature();
            for (OWLClass owlClass: classes) {
                String className = owlClass.getIRI().getShortForm();
                classNamesXMLManager.addIsClassSatisfiableQuery(className);
            }
            Set<OWLObjectProperty> objectProperties = o.getObjectPropertiesInSignature();
            for (OWLObjectProperty owlOP: objectProperties) {
                String oPName = owlOP.getIRI().getShortForm();
                objectPropNamesXMLManager.addIsOPSatisfiableQuery(oPName);;
            }

            Set<OWLAxiom> axioms = o.getAxioms();
            for (OWLAxiom owlAxiom : axioms) {
                System.out.println(owlAxiom.getAxiomType().getName());
                Object[] classesArr = owlAxiom.getClassesInSignature().toArray();
                Object[] objectPropertiesArr = owlAxiom.getObjectPropertiesInSignature().toArray();
                String class1Name;
                String class2Name;
                String oPName;

                switch (owlAxiom.getAxiomType().getName()) {
                    case "SubClassOf":
                    Object[] classExpressionsArr = owlAxiom.getNestedClassExpressions().toArray();
                    OWLClassExpression firstExp = (OWLClassExpression) classExpressionsArr[0];
                    class1Name = ((OWLClass) classesArr[0]).getIRI().getShortForm();
                    class2Name = ((OWLClass) classesArr[1]).getIRI().getShortForm();
                    if (firstExp.getClassExpressionType().toString().equals("ObjectSomeValuesFrom")) {
                        oPName = ((OWLObjectProperty)objectPropertiesArr[0]).getIRI().getShortForm();
                        axiomsXMLManager.addSomeValuesFromQuery(class1Name, class2Name, oPName);
                        axiomsXMLManager.addOPRangeQuery(oPName, class2Name);
                    } else {
                        axiomsXMLManager.addSubClassOfQuery(class1Name, class2Name);
                    }
                    break;

                    case "ObjectPropertyRange":
                        oPName = ((OWLObjectProperty)objectPropertiesArr[0]).getIRI().getShortForm();
                        class1Name = ((OWLClass) classesArr[0]).getIRI().getShortForm();
                        axiomsXMLManager.addOPRangeQuery(oPName, class1Name);
                        break;

                    default:
                        break;
                }

            }

            //classNamesXMLManager.printXMLFile();
            //classNamesXMLManager.saveXMLInFile("src/main/java/edu/odpr/odprapi/model/class_names_queries.xml");
            //objectPropNamesXMLManager.saveXMLInFile("src/main/java/edu/odpr/odprapi/model/OP_names_queries.xml");
            //axiomsXMLManager.saveXMLInFile("src/main/java/edu/odpr/odprapi/model/axiom_queries.xml");
            System.out.println("ONTOLOGIA: "+o.getOntologyID().getOntologyIRI().get());
            String classNamesXMLString = classNamesXMLManager.getXMLString();
            String opNamesXMLString = objectPropNamesXMLManager.getXMLString();
            String axiomsXMLString = axiomsXMLManager.getXMLString();

            p = new Pattern("EL PATRON",classNamesXMLString, opNamesXMLString, axiomsXMLString, jo.toJSONString());
        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}
