package com.example.betabreaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.Classes.MSAzureClient;
import com.example.betabreaker.Classes.ResponseCallBack;
import com.example.betabreaker.databinding.FragmentWelcomeScrnBinding;

public class FragWelcomeScrn extends Fragment implements ResponseCallBack {

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
        final TextView lblUsername = binding.txtUsername;
        final TextView lblPassword = binding.txtPassword;
        final ProgressBar vwProgress = binding.loadingProgressBar;
        final Button btnSign =binding.btnSignup;
        final Button btnLog =binding.btnLogin;

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean correctDetails = false;
                vwProgress.setVisibility(View.VISIBLE);
                btnSign.setVisibility(View.GONE);
                btnLog.setVisibility(View.GONE);
                Log.d("SHOW DB", "onClick: is running");

                if (inpUsername.getText().toString() != null && inpPassword.getText().toString() != null) {
                    Log.d("SHOW DB", "Sending to HTTP");
                    MSAzureClient httpClient = new MSAzureClient(FragWelcomeScrn.this);
                    String type = "POST";
                    String url = GlobalUrl.loginURL;
                    String body = "{ \"username\": \"" + inpUsername.getText().toString() + "\" ," +
                            " \"password\": \"" + inpPassword.getText().toString() + "\" }";
                    httpClient.execute(type, url, body);
                    Log.d("SHOW DB", "BAck from http");
                }else {
                    lblUsername.setText("Cannot be empty");
                    vwProgress.setVisibility(View.GONE);
                    btnSign.setVisibility(View.VISIBLE);
                    btnLog.setVisibility(View.VISIBLE);
                }

            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                vwProgress.setVisibility(View.VISIBLE);
                btnSign.setVisibility(View.GONE);
                btnLog.setVisibility(View.GONE);

                Intent intent = new Intent(requireActivity(), ActDisplayCentre.class);
                startActivity(intent);
                requireActivity().finish();
                //NavHostFragment.findNavController(FragWelcomeScrn.this).navigate(R.id.action_SignUpFrag);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResponseReceived(String jsonResponse) {
        // Handle the response here
        if (jsonResponse != null && jsonResponse.equals("true")) {
            Intent intent = new Intent(requireActivity(), ActDisplayCentre.class);
            startActivity(intent);
            requireActivity().finish();
        } else {
            TextView lblUsername = binding.txtUsername;
            final ProgressBar vwProgress = binding.loadingProgressBar;
            final Button btnSign =binding.btnSignup;
            final Button btnLog =binding.btnLogin;
            lblUsername.setText("Incorrect details provided");
            vwProgress.setVisibility(View.GONE);
            btnSign.setVisibility(View.VISIBLE);
            btnLog.setVisibility(View.VISIBLE);
            Log.d("SHOW DB", "onClick: incorrect details");
        }
    }
}