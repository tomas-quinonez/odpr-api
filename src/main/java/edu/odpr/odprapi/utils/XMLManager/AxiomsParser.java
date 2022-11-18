package edu.odpr.odprapi.utils.XMLManager;

import org.w3c.dom.Element;

public class AxiomsParser extends OWLlinkXMLParser{
    
    public AxiomsParser() {
        super();
    }

    public void addSubClassOfQuery(String subClass, String superClass) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/KB");
        Element subClassOfEl = doc.createElement("SubClassOf");
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", "replace#"+subClass);
        Element classEl2 = doc.createElement("owl:Class");
        classEl2.setAttribute("IRI", "replace#"+superClass);

        subClassOfEl.appendChild(classEl);
        subClassOfEl.appendChild(classEl2);
        isEntailedEl.appendChild(subClassOfEl);
        this.rootElement.appendChild(isEntailedEl);
    }

    public void addOPRangeQuery(String objectPropertyName, String className) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/KB");
        Element OPRangeEl = doc.createElement("owl:ObjectPropertyRange");
        Element OPEl = doc.createElement("owl:ObjectProperty");
        OPEl.setAttribute("IRI", "replace#"+objectPropertyName);
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", "replace#"+className);

        OPRangeEl.appendChild(OPEl);
        OPRangeEl.appendChild(classEl);
        isEntailedEl.appendChild(OPRangeEl);
        this.rootElement.appendChild(isEntailedEl);
    }

    public void addOPDomainQuery(String objectPropertyName, String className) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/KB");
        Element OPDomainEl = doc.createElement("owl:ObjectPropertyDomain");
        Element OPEl = doc.createElement("owl:ObjectProperty");
        OPEl.setAttribute("IRI", "replace#"+objectPropertyName);
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", "replace#"+className);

        OPDomainEl.appendChild(OPEl);
        OPDomainEl.appendChild(classEl);
        isEntailedEl.appendChild(OPDomainEl);
        this.rootElement.appendChild(isEntailedEl);
    }

    public void addSomeValuesFromQuery(String className1, String className2, String objectPropertyName) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/KB");
        Element subClassOfEl = doc.createElement("SubClassOf");
        // subClassOfEl.setPrefix("owl");
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", "replace#"+className1);
        Element someValuesEl = doc.createElement("owl:ObjectSomeValuesFrom");
        Element OPEl = doc.createElement("owl:ObjectProperty");
        OPEl.setAttribute("IRI", "replace#"+objectPropertyName);
        Element classEl2 = doc.createElement("owl:Class");
        classEl2.setAttribute("IRI", "replace#"+className2);

        someValuesEl.appendChild(OPEl);
        someValuesEl.appendChild(classEl2);
        subClassOfEl.appendChild(classEl);
        subClassOfEl.appendChild(someValuesEl);
        isEntailedEl.appendChild(subClassOfEl);
        this.rootElement.appendChild(isEntailedEl);
    }
    
}
