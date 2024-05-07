package com.example.betabreaker.Classes;

public class ClsUser {
    private final String username;
    private String password;
    private String DoB;
    private String FavouriteShoes;
    private String email;
    private int admin;
    private String adminOf;

    public ClsUser(String username, String password, String DoB, String favouriteShoes, String email, String contactNumber, int admin, String adminOf) {
        this.username = username;
        this.password = password;
        this.DoB = DoB;
        this.FavouriteShoes = favouriteShoes;
        this.email = email;
        this.admin = admin;
        this.adminOf = adminOf;
    }

    public String getUsername(){return this.username;}

    public String getPassword(){return this.password;}

    public String getDOB(){return this.DoB;}

    public String getShoes(){return this.FavouriteShoes;}

    public String getEmail(){return this.email;}


    public int getAdmin(){return this.admin;}
    public String getAdminOf(){return this.adminOf;}


    public void setPassword(String newPass){this.password = newPass;}
    public void setEmail(String email){this.email = email;}
    public void setShoes(String FavouriteShoes){this.FavouriteShoes = FavouriteShoes;}
    public void setDOB(String DoB){ this.DoB = DoB;}
    public void setAdmin(int admin) {this.admin = admin;}
    public void setAdminOf(String adminOf) {this.adminOf = adminOf;}


}
