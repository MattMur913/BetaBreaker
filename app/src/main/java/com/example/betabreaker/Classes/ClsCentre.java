package com.example.betabreaker.Classes;

public class ClsCentre {

    private String idCentre;
    private String CentreName;
    private String Address;
    private String Description;
    private String email;
    private String contactNumber;
    private String website;
    private int logoid;


    public ClsCentre(String idCentre, String centreName, String address, String description, String email, String contactNumber, String website, int logoid) {
        this.idCentre = idCentre;
        this.CentreName = centreName;
        this.Address = address;
        this.Description = description;
        this.email = email;
        this.contactNumber = contactNumber;
        this.website = website;
        this.logoid = logoid;
    }

    public String getIdCentre(){return this.idCentre;}
    public String getCentreName(){return this.CentreName;}
    public String getAddress(){return this.Address;}
    public String getDescription(){return this.Description;}
    public String getEmail(){return this.email;}
    public String getNumber(){return this.contactNumber;}
    public String getWebsite(){return this.website;}
    public int getlogo(){return this.logoid;}


    public void setIdCentre(String centreID){
        this.idCentre = centreID;
    }
    public void setCentreName(String CentreName){
        this.CentreName = CentreName;
    }
    public void setAddress(String Address){
        this.Address = Address;
    }
    public void setDescription(String Description){
        this.Description = Description;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
    }
    public void setWebsite(String website){
        this.website = website;
    }
    public void setLogoid(int logoid){
        this.logoid = logoid;
    }






}
