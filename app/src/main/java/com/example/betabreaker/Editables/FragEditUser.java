package com.example.betabreaker.Editables;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Classes.ConfirmationDialog;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.FragDisplayUser;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentEditUserBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
         dob = preferences.getString("DoB","");

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
                            pass = inpPass.getText().toString();

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

                        // Execute the request asynchronously
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("SingleCentre", "Not updated");
                                e.printStackTrace();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("SingleCentre", "Updated");
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragDisplayUser fragment = new FragDisplayUser();


                fragmentTransaction.replace(R.id.dspFragUV, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }
}