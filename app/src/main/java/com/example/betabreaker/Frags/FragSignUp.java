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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.ActDisplayApp;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentSignUpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FragSignUp extends Fragment {

    private FragmentSignUpBinding binding;
    private Button btnLogin;
    private Button btnSign;
    private EditText edtUsername;
    private EditText edtPass;
    private EditText edtEmail;
    private EditText edtDob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogin = binding.btnLogin;
        btnSign = binding.btnSignUp;
        edtDob = binding.edDob;
        edtUsername = binding.edName;
        edtEmail = binding.edEmail;
        edtPass=binding.edPass;

        btnSign.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String updURL = GlobalUrl.getSignUp;
               // Get values from EditText fields
               String Name = edtUsername.getText().toString();
               String tempPass = edtPass.getText().toString();
               String Email = edtEmail.getText().toString();
               //need this to be a date in the format dd/mm/yyyy
               String dob = formatDob(edtDob.getText().toString());

               if (dob == null || dob.isEmpty()) {
                   Toast.makeText(getContext(), "Please enter a valid Date of Birth", Toast.LENGTH_SHORT).show();
                   return;
               }
               String pass = sha256(tempPass);


               // Create a MultipartBody.Builder to construct the request body
               MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                       .setType(MultipartBody.FORM)
                       .addFormDataPart("username", Name)
                       .addFormDataPart("hashPass", pass)
                       .addFormDataPart("email", Email)
                       .addFormDataPart("dob", dob);

               // Build the request body
               RequestBody requestBody = requestBodyBuilder.build();

               // Create a request
               Request request = new Request.Builder()
                       .url(updURL)
                       .post(requestBody)
                       .build();

               // Execute the request asynchronously
               OkHttpClient client = new OkHttpClient();
               client.newCall(request).enqueue(new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                       Log.d("SingleCentre", "Not updated");
                       e.printStackTrace();
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       if (response.isSuccessful()) {
                           String responseBody = response.body().string();
                           if (responseBody.equals("Already Exists")) {
                               Log.d("SingleCentre", "User already exists");
                           } else {
                               try {
                                   JSONObject jsonResponse = new JSONObject(responseBody);
                                   String username = jsonResponse.getString("Username").trim(); // Extracting username from JSON
                                   String email = jsonResponse.getString("Email").trim(); // Extracting email from JSON
                                   String dob = jsonResponse.getString("DoB").trim(); // Extracting date of birth from JSON

                                   Log.d("SingleCentre", "Username: " + username); // Log the username
                                   Log.d("SingleCentre", "Email: " + email); // Log the email
                                   Log.d("SingleCentre", "Date of Birth: " + dob); // Log the date of birth

                                   // Add the extracted values to SharedPreferences
                                   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString("username", username);
                                   editor.putString("email", email);
                                   editor.putString("dob", dob);
                                   //TODO Cheange thisd
                                   editor.putInt("admin", 0); // Assuming the admin value is always 0
                                   editor.putString("adminOf","" );
                                   editor.apply();

                                   Intent intent = new Intent(getActivity(), ActDisplayApp.class);
                                   startActivity(intent);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                                   Log.d("SingleCentre", "Failed to parse JSON response");
                               }
                           }
                       } else {
                           Log.d("SingleCentre", "Response unsuccessful");
                       }
                   }

               });
           }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragWelcomeScrn fragment = new FragWelcomeScrn();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
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

    // Method to format date of birth to "dd/mm/yyyy" format
    private String formatDob(String dob) {
        // Check if dob matches the format "yyyy-mm-dd"
        if (dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            // Split the date by "-"
            String[] parts = dob.split("-");
            // Reconstruct the date in "dd/mm/yyyy" format
            return parts[2] + "/" + parts[1] + "/" + parts[0];
        } else {
            // Show toast indicating incorrect DOB format
            Toast.makeText(getContext(), "Date of Birth should be in yyyy-mm-dd format", Toast.LENGTH_SHORT).show();
            // Return null to indicate incorrect format
            return null;
        }
    }

}