package edu.odpr.odprapi.services;

import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLManager {

    private Document doc = null;
    private Element rootElement = null;

    public XMLManager() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dbFactory.setNamespaceAware(true);
            this.doc = dBuilder.newDocument();

            // Element rootElement = doc.createElement("RequestMessage");
            rootElement = doc.createElementNS("http://www.owllink.org/owllink#", "RequestMessage");
            // rootElement.setAttributeNS(null, null, null);
            // doc.createElementNS("http://www.owllink.org/owllink#", "xmlns:owl");
            this.rootElement.setAttribute("xmlns:owl", "http://www.owllink.org/owllink#");
            this.rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            this.rootElement.setAttribute("xsi:schemaLocation",
                    "http://www.owllink.org/owllink# http://www.w3.org/Submission/2010/SUBM-owllink-httpxml-binding-20100701/owllink.xsd");
            // rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:owl",
            // "http://www.owllink.org/owllink#");
            // rootElement.setAttributeNS("http://www.owllink.org/owllink", "xmlns:owl",
            // "http://www.w3.org/2002/07/owl#");

            Element createKBEl = doc.createElement("CreateKB");
            createKBEl.setAttribute("kb", "http://www.owllink.org/ont/sistema");
            createKBEl.setAttribute("name", "KB 1");
            this.rootElement.appendChild(createKBEl);

            doc.appendChild(this.rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSubClassOfQuery(String subClass, String superClass) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element subClassOfEl = doc.createElement("SubClassOf");
        // subClassOfEl.setPrefix("owl");
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", subClass);
        Element classEl2 = doc.createElement("owl:Class");
        classEl2.setAttribute("IRI", superClass);

        subClassOfEl.appendChild(classEl);
        subClassOfEl.appendChild(classEl2);
        isEntailedEl.appendChild(subClassOfEl);
        this.rootElement.appendChild(isEntailedEl);
        // this.rootElement.setAttributeNS("http://www.owllink.org/ont/sistema", "kb",
        // "IsEntailed");
    }

    public void addIsClassSatisfiableQuery(String className) {
        Element isClassSatEl = doc.createElement("IsClassSatisfiable");
        isClassSatEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", className);

        isClassSatEl.appendChild(classEl);
        this.rootElement.appendChild(isClassSatEl);
    }

    public void addIsOPSatisfiableQuery(String objectPropertyName) {
        Element isOPSatEl = doc.createElement("IsObjectPropertySatisfiable");
        isOPSatEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element OPEl = doc.createElement("owl:ObjectProperty");
        OPEl.setAttribute("IRI", objectPropertyName);

        isOPSatEl.appendChild(OPEl);
        this.rootElement.appendChild(isOPSatEl);
    }

    public void addOPRangeQuery(String objectPropertyName, String className) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element OPRangeEl = doc.createElement("owl:ObjectPropertyRange");
        Element OPEl = doc.createElement("owl:ObjectProperty");
        OPEl.setAttribute("IRI", objectPropertyName);
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", className);

        OPRangeEl.appendChild(OPEl);
        OPRangeEl.appendChild(classEl);
        isEntailedEl.appendChild(OPRangeEl);
        this.rootElement.appendChild(isEntailedEl);
    }

    public void addSomeValuesFromQuery(String className1, String className2, String objectPropertyName) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element subClassOfEl = doc.createElement("SubClassOf");
        // subClassOfEl.setPrefix("owl");
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", className1);
        Element someValuesEl = doc.createElement("owl:ObjectSomeValuesFrom");
        Element OPEl = doc.createElement("owl:ObjectProperty");
        OPEl.setAttribute("IRI", objectPropertyName);
        Element classEl2 = doc.createElement("owl:Class");
        classEl2.setAttribute("IRI", className2);

        someValuesEl.appendChild(OPEl);
        someValuesEl.appendChild(classEl2);
        subClassOfEl.appendChild(classEl);
        subClassOfEl.appendChild(someValuesEl);
        isEntailedEl.appendChild(subClassOfEl);
        this.rootElement.appendChild(isEntailedEl);
    }

    public void printXMLFile() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void saveXMLInFile(String path) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getXMLString() {
        String output = "";
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return output;
    }
}
