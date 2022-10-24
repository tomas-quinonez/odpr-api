package edu.odpr.odprapi.utils.XMLManager;

import org.w3c.dom.Element;

public class ClassesXMLManager extends XMLManager{
    
    public ClassesXMLManager() {
        super();
    }

    public void addIsClassSatisfiableQuery(String className) {
        Element isClassSatEl = doc.createElement("IsClassSatisfiable");
        isClassSatEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", className);

        isClassSatEl.appendChild(classEl);
        this.rootElement.appendChild(isClassSatEl);
    }
}
