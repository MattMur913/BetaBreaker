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
        Log.d("SingleCentre", "WelcomeScrn");
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
        Log.d("SingleCentre", "Check");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username = preferences.getString("username", "");

        if(!username.equals("") && username != null){
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
                //TODO MAke it check just like in the signup fragment
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", userData.getString("Username"));
                editor.putString("email", userData.getString("Email"));
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