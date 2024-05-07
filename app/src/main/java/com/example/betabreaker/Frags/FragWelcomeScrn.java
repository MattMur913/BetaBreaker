package com.example.betabreaker.Frags;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.betabreaker.ActDisplayApp;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentWelcomeScrnBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragWelcomeScrn extends Fragment {

    private FragmentWelcomeScrnBinding binding;

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
        final ProgressBar vwProgress = binding.loadingProgressBar;
        final Button btnSign = binding.btnSignup;
        final Button btnLog = binding.btnLogin;

        checkLogged();

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vwProgress.setVisibility(View.VISIBLE);
                btnSign.setVisibility(View.GONE);
                btnLog.setVisibility(View.GONE);

                if (inpUsername.getText().toString() != null && inpPassword.getText().toString() != null) {
                    String hashPass = sha256(String.valueOf(inpPassword.getText()));

// Create an instance of OkHttpClient
                    OkHttpClient client = new OkHttpClient();

// Prepare the request body
                    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                    String jsonBody = "{\"username\":\"" + inpUsername.getText().toString() + "\",\"password\":\"" + hashPass + "\"}";
                    RequestBody requestBody = RequestBody.create(jsonBody, mediaType);

// Create a request
                    Request request = new Request.Builder()
                            .url(GlobalUrl.loginURL)
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "There has been an issue running this request please try again", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    JSONObject resultSets = jsonObject.getJSONObject("ResultSets");
                                    JSONArray userTable = resultSets.getJSONArray("Table1");
                                    if (userTable.length() == 1) {

                                        JSONObject userData = userTable.getJSONObject(0);
                                        Context context = getContext();
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("username", userData.getString("Username"));
                                        editor.putString("email", userData.getString("Email"));
                                        editor.putString("dob", userData.getString("DoB"));
                                        editor.putInt("admin", userData.getInt("admin"));
                                        editor.putString("adminOf", userData.getString("adminOf"));
                                        editor.putString("favCent", "");
                                        editor.apply();


                                        Intent intent = new Intent(requireActivity(), ActDisplayApp.class);
                                        startActivity(intent);
                                        requireActivity().finish();

                                    } else {
                                        final ProgressBar vwProgress = binding.loadingProgressBar;
                                        final Button btnSign = binding.btnSignup;
                                        final Button btnLog = binding.btnLogin;
                                        Toast.makeText(getContext(), "INCORRECT USERNAME OR PASSWORD PROVIDED PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();

                                        vwProgress.setVisibility(View.GONE);
                                        btnSign.setVisibility(View.VISIBLE);
                                        btnLog.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    vwProgress.setVisibility(View.GONE);
                                    btnSign.setVisibility(View.VISIBLE);
                                    btnLog.setVisibility(View.VISIBLE);

                                }
                            } else {
                                vwProgress.setVisibility(View.GONE);
                                btnSign.setVisibility(View.VISIBLE);
                                btnLog.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "USERNAME OR PASSWORD CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                    vwProgress.setVisibility(View.GONE);
                    btnSign.setVisibility(View.VISIBLE);
                    btnLog.setVisibility(View.VISIBLE);
                }


            }
        });

        //Goes to sign up app
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vwProgress.setVisibility(View.VISIBLE);
                btnSign.setVisibility(View.GONE);
                btnLog.setVisibility(View.GONE);
                NavHostFragment.findNavController(FragWelcomeScrn.this)
                        .navigate(R.id.login_to_signup);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Hashes the password
    public static String sha256(final String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //Checks if the user is already logged in
    public void checkLogged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username = preferences.getString("username", "");

        //Starts the activity of the main app
        if (!username.equals("")) {
            Intent intent = new Intent(requireActivity(), ActDisplayApp.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }
}