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
import edu.odpr.odprapi.utils.XMLManager.AxiomsParser;
import edu.odpr.odprapi.utils.XMLManager.ClassesParser;
import edu.odpr.odprapi.utils.XMLManager.OPsParser;

@Service
public class PatternPreprocessingService {

    public Pattern processPattern(InputStream inputStream) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ClassesParser classNamesXMLManager = new ClassesParser();
        OPsParser objectPropNamesXMLManager = new OPsParser();
        AxiomsParser axiomsXMLManager = new AxiomsParser();
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
            // Object[] e = owlAxiom.getNestedClassExpressions().toArray();
            String class1Name;
            String class2Name;
            String oPName;

            switch (owlAxiom.getAxiomType().getName()) {
                case "SubClassOf":
                    if (classesArr.length > 1) {
                        Object[] classExpressionsArr = owlAxiom.getNestedClassExpressions().toArray();
                        OWLClassExpression firstExp = (OWLClassExpression) classExpressionsArr[0];
                        class1Name = ((OWLClass) classesArr[0]).getIRI().getShortForm();
                        System.out.println(class1Name);
                        if (objectPropertiesArr.length > 0) {
                            System.out.println("relacion: "
                                    + ((OWLObjectProperty) objectPropertiesArr[0]).getIRI().getShortForm());
                        }
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
                    }
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
