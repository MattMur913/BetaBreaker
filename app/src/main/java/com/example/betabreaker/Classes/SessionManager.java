package com.example.betabreaker.Classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.betabreaker.Classes.ClsUser;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String LOGIN_NAME = "LoginPref";
    private static final String KEY_IS_LOGGED = "isLoggedIn";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(LOGIN_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED, isLoggedIn);
        editor.apply();
    }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED,false);
    }

    public void loadUserDets(ClsUser user){

        editor.putString("Username", user.getUsername());
        editor.putString("ConNumber", user.getContactNumber());
        editor.putString("DOB", user.getDOB());
        editor.putString("Email", user.getEmail());
        editor.putString("Shoes", user.getShoes());

    }

}
