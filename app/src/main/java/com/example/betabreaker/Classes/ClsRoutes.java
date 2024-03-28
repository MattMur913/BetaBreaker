package com.example.betabreaker.Classes;

import java.io.Serializable;

public class ClsRoutes implements Serializable {
    private String area;
    private String colour;
    private String grades;
    private String setDate;
    private String setter;
    private int upvotes;
    private String ImageUrl;

    public ClsRoutes(String area, String colour, String grades, String setDate, String setter, int upvotes,String ImageUrl) {
        this.area = area;
        this.colour = colour;
        this.grades = grades;
        this.setDate = setDate;
        this.setter = setter;
        this.upvotes = upvotes;
        this.ImageUrl = ImageUrl;
    }

    // Getters
    public String getArea() {
        return area;
    }
    public String getImage() {
        return ImageUrl;
    }

    public String getColour() {
        return colour;
    }

    public String getGrades() {
        return grades;
    }

    public String getSetDate() {
        return setDate;
    }

    public String getSetter() {
        return setter;
    }

    public int getUpvotes() {
        return upvotes;
    }

    // Setters
    public void setArea(String area) {
        this.area = area;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }
    public void setImage(String Image) {
        this.ImageUrl = Image;
    }
}