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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class XMLManager {

    protected Document doc = null;
    protected Element rootElement = null;

    public XMLManager() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dbFactory.setNamespaceAware(true);
            this.doc = dBuilder.newDocument();

            rootElement = doc.createElementNS("http://www.owllink.org/owllink#", "RequestMessage");
            this.rootElement.setAttribute("xmlns:owl", "http://www.owllink.org/owllink#");
            this.rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            this.rootElement.setAttribute("xsi:schemaLocation",
                    "http://www.owllink.org/owllink# http://www.w3.org/Submission/2010/SUBM-owllink-httpxml-binding-20100701/owllink.xsd");

            Element createKBEl = doc.createElement("CreateKB");
            createKBEl.setAttribute("kb", "http://www.owllink.org/ont/sistema");
            createKBEl.setAttribute("name", "KB 1");
            this.rootElement.appendChild(createKBEl);

            doc.appendChild(this.rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
