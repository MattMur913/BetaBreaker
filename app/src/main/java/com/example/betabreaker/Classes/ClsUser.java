package com.example.betabreaker.Classes;

public class ClsUser {
    private String username;
    private String password;
    private String DoB;
    private String FavouriteShoes;
    private String email;
    private String contactNumber;
    private int admin;
    private String adminOf;

    public String getUsername(){return this.username;}

    public String getDOB(){return this.DoB;}

    public String getShoes(){return this.FavouriteShoes;}

    public String getEmail(){return this.email;}

    public String getContactNumber(){return this.contactNumber;}

    public int getAdmin(){return this.admin;}
    public String getAdminOf(){return this.adminOf;}


    public void setPassword(String newPass){this.password = newPass;}
    public void setEmail(String email){this.email = email;}
    public void setShoes(String FavouriteShoes){this.FavouriteShoes = FavouriteShoes;}
    public void setDOB(String DoB){ this.DoB = DoB;}
    public void setNumber(String contactNum){this.contactNumber = contactNum;}
    public void setAdmin(int admin) {this.admin = admin;}
    public void setAdminOf(String adminOf) {this.adminOf = adminOf;}


}
