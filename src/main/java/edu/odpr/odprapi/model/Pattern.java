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

    @Column(name= "class_names", columnDefinition = "xml")
    @org.hibernate.annotations.Type(type="edu.odpr.odprapi.model.SQLXMLType")
    private String classNames;

    @Column(name= "op_names", columnDefinition = "xml")
    @org.hibernate.annotations.Type(type="edu.odpr.odprapi.model.SQLXMLType")
    private String opNames;

    @Column(name= "axioms", columnDefinition = "xml")
    @org.hibernate.annotations.Type(type="edu.odpr.odprapi.model.SQLXMLType")
    private String axioms;

    @Type(type= "json")
    @Column(name= "synonyms", columnDefinition = "jsonb")
    private String synonyms;
    //private ArrayList<String> classNames;
    //private int classLength;
    
    public Pattern() {}

    public Pattern(String name, String classNames, String opNames, String axioms, String synonyms) {
        this.name = name;
        this.classNames = classNames;
        this.opNames = opNames;
        this.axioms = axioms;
        this.synonyms = synonyms;
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


    public String getClassNames() {
        return this.classNames;
    }

    public void setClassNames(String classNames) {
        this.classNames = classNames;
    }

    public String getOpNames() {
        return this.opNames;
    }

    public void setOpNames(String opNames) {
        this.opNames = opNames;
    }

    public String getAxioms() {
        return this.axioms;
    }

    public void setAxioms(String axioms) {
        this.axioms = axioms;
    }

    public String getSynonyms() {
        return this.synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }


    
}
