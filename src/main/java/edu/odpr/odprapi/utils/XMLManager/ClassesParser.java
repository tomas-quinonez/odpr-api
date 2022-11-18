package edu.odpr.odprapi.utils.XMLManager;

import org.w3c.dom.Element;

public class ClassesParser extends OWLlinkXMLParser{
    
    public ClassesParser() {
        super();
    }

    public void addIsClassSatisfiableQuery(String className) {
        Element isClassSatEl = doc.createElement("IsClassSatisfiable");
        isClassSatEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/KB");
        Element classEl = doc.createElement("Class");
        classEl.setAttribute("IRI", "replace#"+className);

        isClassSatEl.appendChild(classEl);
        this.rootElement.appendChild(isClassSatEl);
    }
}
