package com.example.betabreaker.Editables;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.betabreaker.Classes.ConfirmationDialog;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentEditUserBinding;

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

public class FragEditUser extends Fragment {

    private FragmentEditUserBinding binding;
    private String email;
    private String dob;
    private String pass = "empty";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView lblUsername = binding.txtDisName;
        final EditText inpEmail = binding.edEmail;
        final EditText inpDob = binding.edDob;
        final EditText inpPass = binding.edPass;
        final Button btnView =binding.rotBack;
        final CheckBox chDob = binding.txtDob;
        final CheckBox chEmail = binding.txtEmail;
        final CheckBox chPass = binding.txtPass;
        final Button btnUpdate =binding.rotUpdate;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username = preferences.getString("username","");
         email = preferences.getString("email","");
         dob = preferences.getString("dob","");
        inpEmail.setHint(email);
        inpDob.setHint(dob);
        inpPass.setHint("Password");
        lblUsername.setHint(preferences.getString("username",""));
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog.showConfirmationDialog(getContext(), "Are you sure you want update your details?", new ConfirmationDialog.ConfirmationListener() {
                    @Override
                    public void onConfirm() {
                        String updUrl = GlobalUrl.updUserUrl.replace("{username}",username);
                        JSONObject jsonObject = new JSONObject();

                        if (chDob.isChecked()) {
                            // Update date value
                            dob = inpDob.getText().toString();

                        }
                        if (chEmail.isChecked()) {
                            // Update email value
                            email = inpEmail.getText().toString();

                        }
                        if (chPass.isChecked()) {
                            // Update password value
                            pass = sha256(inpPass.getText().toString());

                        }
                        try {
                            jsonObject.put("dob", dob);
                            jsonObject.put("email", email);
                            jsonObject.put("pass", pass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String userData = jsonObject.toString();
                        OkHttpClient client = new OkHttpClient();
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), userData);

                        // Create a request
                        Request request = new Request.Builder()
                                .url(updUrl)
                                .put(body)
                                .build();
                        btnUpdate.setVisibility(View.GONE);
                        btnView.setVisibility(View.GONE);
                        binding.editUsProg.setVisibility(View.VISIBLE);
                        // Execute the request asynchronously
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                btnUpdate.setVisibility(View.VISIBLE);
                                btnView.setVisibility(View.VISIBLE);
                                binding.editUsProg.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "There has been an issue running this request please try again", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                                        NavController navController = navHostFragment.getNavController();
                                        navController.popBackStack();
                                        navController.navigate(R.id.action_refreshEditUser);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                                NavController navController = navHostFragment.getNavController();
                                navController.popBackStack();
                                navController.navigate(R.id.action_refreshEditUser);
                            }
                        });
                    }
                });
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(FragEditUser.this)
                        .navigate(R.id.go_display_user);
            }
        });
    }

    public static String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
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
}