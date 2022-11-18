package edu.odpr.odprapi.utils.XMLManager;

import org.w3c.dom.Element;

public class OPsParser extends OWLlinkXMLParser{
    
    public OPsParser() {
        super();
    }

    public void addIsOPSatisfiableQuery(String objectPropertyName) {
        Element isOPSatEl = doc.createElement("IsObjectPropertySatisfiable");
        isOPSatEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/KB");
        Element OPEl = doc.createElement("ObjectProperty");
        OPEl.setAttribute("IRI", "replace#"+objectPropertyName);

        isOPSatEl.appendChild(OPEl);
        this.rootElement.appendChild(isOPSatEl);
    }
}