package edu.odpr.odprapi.model;

import java.util.ArrayList;

public class Pattern {
    private String patternName;
    private ArrayList<String> classNames;
    private int classLength;
    

    public Pattern(String name) {
        this.patternName = name;
        this.classNames = new ArrayList<String>();
        this.classLength = 0;
    }

    public void addClassName(String newName) {
        this.classNames.add(newName);
    }

    public ArrayList<String> getClassNames() {
        return classNames;
    }

    public String getPatternName() {
        return this.patternName;
    }

    public void setClassLength(int length) {
        this.classLength = length;
    }

    public int getClassLength() {
        return this.classLength;
    }
}
