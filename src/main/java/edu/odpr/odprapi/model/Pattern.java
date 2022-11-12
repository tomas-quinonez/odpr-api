package edu.odpr.odprapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonType;


@Entity
@Table(name = "pattern", schema="patterns")
@TypeDef(
    name = "json",
    typeClass = JsonType.class
)
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name= "class_names_queries", columnDefinition = "xml")
    @org.hibernate.annotations.Type(type="edu.odpr.odprapi.utils.SQLXMLType")
    private String classNamesQueries;

    @Column(name= "op_names_queries", columnDefinition = "xml")
    @org.hibernate.annotations.Type(type="edu.odpr.odprapi.utils.SQLXMLType")
    private String opNamesQueries;

    @Column(name= "axioms_queries", columnDefinition = "xml")
    @org.hibernate.annotations.Type(type="edu.odpr.odprapi.utils.SQLXMLType")
    private String axiomsQueries;

    @Type(type= "json")
    @Column(name= "synonyms", columnDefinition = "jsonb")
    private String synonyms;

    @Column(name = "cant_classes")
    private int cantClasses;

    @Column(name = "cant_ops")
    private int cantOPs;

    @Column(name = "cant_axioms")
    private int cantAxioms;
    
    public Pattern() {}

    public Pattern(String name, String classNamesQueries, String opNamesQueries, String axiomsQueries, String synonyms, int cantClasses, int cantOPs, int cantAxioms) {
        this.name = name;
        this.classNamesQueries = classNamesQueries;
        this.opNamesQueries = opNamesQueries;
        this.axiomsQueries = axiomsQueries;
        this.synonyms = synonyms;
        this.cantClasses = cantClasses;
        this.cantOPs = cantOPs;
        this.cantAxioms = cantAxioms;
    }

    public Pattern(String classNamesQueries, String opNamesQueries, String axiomsQueries, int cantClasses, int cantOPs, int cantAxioms) {
        this.name = "";
        this.classNamesQueries = classNamesQueries;
        this.opNamesQueries = opNamesQueries;
        this.axiomsQueries = axiomsQueries;
        this.synonyms = "";
        this.cantClasses = cantClasses;
        this.cantOPs = cantOPs;
        this.cantAxioms = cantAxioms;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getClassNamesQueries() {
        return this.classNamesQueries;
    }

    public void setClassNamesQueries(String classNamesQueries) {
        this.classNamesQueries = classNamesQueries;
    }

    public String getOpNamesQueries() {
        return this.opNamesQueries;
    }

    public void setOpNamesQueries(String opNamesQueries) {
        this.opNamesQueries = opNamesQueries;
    }

    public String getAxiomsQueries() {
        return this.axiomsQueries;
    }

    public void setAxiomsQueries(String axiomsQueries) {
        this.axiomsQueries = axiomsQueries;
    }

    public String getSynonyms() {
        return this.synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public int getCantClasses() {
        return this.cantClasses;
    }

    public void setCantClasses(int cantClasses) {
        this.cantClasses = cantClasses;
    }

    public int getCantOPs() {
        return this.cantOPs;
    }

    public void setCantOPs(int cantOPs) {
        this.cantOPs = cantOPs;
    }

    public int getCantAxioms() {
        return this.cantAxioms;
    }

    public void setCantAxioms(int cantAxioms) {
        this.cantAxioms = cantAxioms;
    }
    
}
