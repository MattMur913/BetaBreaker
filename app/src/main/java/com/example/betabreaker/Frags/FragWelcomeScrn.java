package com.example.betabreaker.Frags;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.ActAdminViews;
import com.example.betabreaker.ActDisplayApp;
import com.example.betabreaker.Classes.ClsUser;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.Classes.MSAzureClient;
import com.example.betabreaker.Classes.ResponseCallBack;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentWelcomeScrnBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FragWelcomeScrn extends Fragment implements ResponseCallBack {

    private FragmentWelcomeScrnBinding binding;
    //RemoveThis
    private List<ClsUser> userList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWelcomeScrnBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText inpUsername = binding.inpUsername;
        final EditText inpPassword = binding.inpPassword;
        final TextView lblUsername = binding.txtUsername;
        final TextView lblPassword = binding.txtPassword;
        final ProgressBar vwProgress = binding.loadingProgressBar;
        final Button btnSign =binding.btnSignup;
        final Button btnLog =binding.btnLogin;

       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Clear all data
        editor.apply(); // Apply changes



        checkLogged();


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean correctDetails = false;
                vwProgress.setVisibility(View.VISIBLE);
                btnSign.setVisibility(View.GONE);
                btnLog.setVisibility(View.GONE);

                if (inpUsername.getText().toString() != null && inpPassword.getText().toString() != null) {

                    String hashPass = sha256(String.valueOf(inpPassword.getText()));
                    MSAzureClient httpClient = new MSAzureClient(FragWelcomeScrn.this);
                    String type = "POST";
                    String url = GlobalUrl.loginURL;
                    String body = "{ \"username\": \"" + inpUsername.getText().toString() + "\" ," +
                            " \"password\": \"" + hashPass.toString() + "\" }";
                    httpClient.execute(type, url, body);

                }else {
                    lblUsername.setText("Cannot be empty");
                    vwProgress.setVisibility(View.GONE);
                    btnSign.setVisibility(View.VISIBLE);
                    btnLog.setVisibility(View.VISIBLE);
                }



            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vwProgress.setVisibility(View.VISIBLE);
                btnSign.setVisibility(View.GONE);
                btnLog.setVisibility(View.GONE);

                Context context = getContext();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragSignUp fragment = new FragSignUp();


                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //RemoveThis
    public void testingAdmin(String Username, String Password){
        addingHardUsers();
        Log.d("TestingAdmin", "In method ");
        //String hashPass = sha256(Password);
        String hashPass = ("password123");
        for(int i =0; i< userList.size(); i++){
            if(Username.equals(  userList.get(i).getUsername()) && hashPass.equals( userList.get(i).getPassword())){
                Log.d("TestingAdmin", "In If ");
                Context context = getContext();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", userList.get(i).getUsername()) ;
                editor.putString("shoes", userList.get(i).getShoes()) ;
                editor.putString("email", userList.get(i).getEmail()) ;
                editor.putString("contactNumber", userList.get(i).getContactNumber()) ;
                editor.putString("DoB", userList.get(i).getDOB()) ;
                editor.putInt("admin", userList.get(i).getAdmin()) ;
                editor.putString("adminOf", userList.get(i).getAdminOf()) ;
                editor.apply();

                int admin = preferences.getInt("admin", 0);
                if(admin != 0 ){
                    Log.d("TestingAdmin", "Is Admin ");
                    //GET SINGLUAR CENTRE
                    //Method this seperate
                    Intent intent = new Intent(requireActivity(), ActAdminViews.class);
                    startActivity(intent);
                    requireActivity().finish();
                }else {
                    Log.d("TestingAdmin", "Not Admin ");

                    Intent intent = new Intent(requireActivity(), ActDisplayApp.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }else {
                TextView lblUsername = binding.txtUsername;
                final ProgressBar vwProgress = binding.loadingProgressBar;
                final Button btnSign =binding.btnSignup;
                final Button btnLog =binding.btnLogin;
                lblUsername.setText("Incorrect details provided");
                vwProgress.setVisibility(View.GONE);
                btnSign.setVisibility(View.VISIBLE);
                btnLog.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addingHardUsers(){
        //String hashPass = sha256("password123");
        String hashPass = ("password123");
        /*ClsUser hardUser = new ClsUser("john_doe", hashPass,
                "1990-01-01", "Nike",
                "john@example.com", "1234567890", 1, "6");

         */
        ClsUser hardUser = new ClsUser("john_doe", hashPass,
                "1990-01-01", "Nike",
                "john@example.com", "1234567890", 1, "JTJmcHJvamVjdC1pbWFnZXMlMmY2Mzg0NjYzNjMzNzcwNzA1MDg=");


        userList.add(hardUser);
         hardUser = new ClsUser("matty", hashPass,
                "1990-01-01", "Nike",
                "john@example.com", "1234567890", 0, "");
        userList.add(hardUser);
    }

    public static String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void checkLogged(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username = preferences.getString("username", "");

        if(username.equals("") || username == null){
            Log.d("FlowChecks", "User is logged in: "+ username);
            Intent intent = new Intent(requireActivity(), ActDisplayApp.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }
    @Override
    public void onResponseReceived(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject resultSets = jsonObject.getJSONObject("ResultSets");
            JSONArray userTable = resultSets.getJSONArray("Table1");
            if (userTable.length()==1){

                JSONObject userData = userTable.getJSONObject(0);
                Context context = getContext();
                //TODO Adjust this so it isnt depreciated
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", userData.getString("Username"));
                editor.putString("shoes", userData.getString("Shoes"));
                editor.putString("email", userData.getString("Email"));
                editor.putString("contactNumber", userData.getString("ContactNumebr"));
                editor.putString("DoB", userData.getString("DoB"));
                editor.putInt("admin", userData.getInt("admin"));
                editor.putString("adminOf", userData.getString("adminOf"));
                editor.apply();

                Log.d("TestingAdmin", "Not Admin ");
                Intent intent = new Intent(requireActivity(), ActDisplayApp.class);
                startActivity(intent);
                requireActivity().finish();

            } else {
                TextView lblUsername = binding.txtUsername;
                final ProgressBar vwProgress = binding.loadingProgressBar;
                final Button btnSign = binding.btnSignup;
                final Button btnLog = binding.btnLogin;
                lblUsername.setText("Incorrect details provided");
                vwProgress.setVisibility(View.GONE);
                btnSign.setVisibility(View.VISIBLE);
                btnLog.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {

            e.printStackTrace();

        }
    }
}