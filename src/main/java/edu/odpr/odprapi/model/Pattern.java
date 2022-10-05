package edu.odpr.odprapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "pattern", schema="patterns")
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name= "xml_file", columnDefinition = "xml")
    @org.hibernate.annotations.Type(type="edu.odpr.odprapi.model.SQLXMLType")
    private String xmlFile;
    //private ArrayList<String> classNames;
    //private int classLength;
    
    public Pattern() {}

    public Pattern(String name, String xmlFile) {
        this.name = name;
        this.xmlFile = xmlFile;
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

    public String getXmlFile() {
        return this.xmlFile;
    }

    public void setXmlFile(String xmlFile) {
        this.xmlFile = xmlFile;
    }
    
}
