package edu.odpr.odprapi.utils.XMLManager;

import org.w3c.dom.Element;

public class OPsXMLManager extends XMLManager{
    
    public OPsXMLManager() {
        super();
    }

    public void addIsOPSatisfiableQuery(String objectPropertyName) {
        Element isOPSatEl = doc.createElement("IsObjectPropertySatisfiable");
        isOPSatEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element OPEl = doc.createElement("owl:ObjectProperty");
        OPEl.setAttribute("IRI", objectPropertyName);

        isOPSatEl.appendChild(OPEl);
        this.rootElement.appendChild(isOPSatEl);
    }
}
