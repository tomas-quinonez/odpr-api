package edu.odpr.odprapi.services;

import java.io.InputStream;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.stereotype.Service;

import edu.odpr.odprapi.model.Pattern;
import edu.odpr.odprapi.utils.XMLManager.AxiomsXMLManager;
import edu.odpr.odprapi.utils.XMLManager.ClassesXMLManager;
import edu.odpr.odprapi.utils.XMLManager.OPsXMLManager;

@Service
public class PatternPreprocessingService {

    public Pattern processPattern(InputStream inputStream) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ClassesXMLManager classNamesXMLManager = new ClassesXMLManager();
        OPsXMLManager objectPropNamesXMLManager = new OPsXMLManager();
        AxiomsXMLManager axiomsXMLManager = new AxiomsXMLManager();
        Pattern p = null;
        OWLOntology o = manager.loadOntologyFromOntologyDocument(inputStream);
        // IRI iri = o.getOntologyID().getOntologyIRI().get();

        Set<OWLClass> classes = o.getClassesInSignature();
        int cantClasses = classes.size();
        for (OWLClass owlClass : classes) {
            String className = owlClass.getIRI().getShortForm();
            classNamesXMLManager.addIsClassSatisfiableQuery(className);
        }
        

        Set<OWLObjectProperty> objectProperties = o.getObjectPropertiesInSignature();
        int cantOPs = objectProperties.size();
        for (OWLObjectProperty owlOP : objectProperties) {
            String oPName = owlOP.getIRI().getShortForm();
            objectPropNamesXMLManager.addIsOPSatisfiableQuery(oPName);
        }
        

        int cantAxioms = 0;
        Set<OWLAxiom> axioms = o.getAxioms();
        for (OWLAxiom owlAxiom : axioms) {
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
                        oPName = ((OWLObjectProperty) objectPropertiesArr[0]).getIRI().getShortForm();
                        axiomsXMLManager.addSomeValuesFromQuery(class1Name, class2Name, oPName);
                        axiomsXMLManager.addOPDomainQuery(oPName, class1Name);
                        axiomsXMLManager.addOPRangeQuery(oPName, class2Name);
                    } else {
                        axiomsXMLManager.addSubClassOfQuery(class1Name, class2Name);
                    }
                    cantAxioms++;
                    break;

                case "ObjectPropertyDomain":
                    oPName = ((OWLObjectProperty) objectPropertiesArr[0]).getIRI().getShortForm();
                    class1Name = ((OWLClass) classesArr[0]).getIRI().getShortForm();
                    axiomsXMLManager.addOPDomainQuery(oPName, class1Name);
                    cantAxioms++;
                    break;

                case "ObjectPropertyRange":
                    oPName = ((OWLObjectProperty) objectPropertiesArr[0]).getIRI().getShortForm();
                    class1Name = ((OWLClass) classesArr[0]).getIRI().getShortForm();
                    axiomsXMLManager.addOPRangeQuery(oPName, class1Name);
                    cantAxioms++;
                    break;

                default:
                    break;
            }

        }
        
        String classNamesXMLString = classNamesXMLManager.getXMLString();
        String opNamesXMLString = objectPropNamesXMLManager.getXMLString();
        String axiomsXMLString = axiomsXMLManager.getXMLString();
        
        p = new Pattern(classNamesXMLString, opNamesXMLString, axiomsXMLString, cantClasses, cantOPs, cantAxioms);
        return p;
    }
}
