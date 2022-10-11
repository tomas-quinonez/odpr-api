package edu.odpr.odprapi.services;

import org.w3c.dom.Element;

public class AxiomsXMLManager extends XMLManager{
    
    public AxiomsXMLManager() {
        super();
    }

    public void addSubClassOfQuery(String subClass, String superClass) {
        Element isEntailedEl = doc.createElement("IsEntailed");
        isEntailedEl.setAttributeNS(null, "kb", "http://www.owllink.org/ont/sistema");
        Element subClassOfEl = doc.createElement("SubClassOf");
        Element classEl = doc.createElement("owl:Class");
        classEl.setAttribute("IRI", subClass);
        Element classEl2 = doc.createElement("owl:Class");
        classEl2.setAttribute("IRI", superClass);

        subClassOfEl.appendChild(classEl);
        subClassOfEl.appendChild(classEl2);
        isEntailedEl.appendChild(subClassOfEl);
        this.rootElement.appendChild(isEntailedEl);
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
    
}
