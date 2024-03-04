package com.example.betabreaker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.betabreaker.databinding.FragmentWelcomeScrnBinding;


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
        final TextView lblUsername = binding.txtUsername;
        final TextView lblPassword = binding.txtPassword;
        Boolean correctDetails = false;



        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (inpUsername.getText().toString() != null && inpPassword.getText().toString() != null) {

                   // correctDetails = sendUser(inpUsername,inpPassword);

                    if (correctDetails) {
                        //ClsUser logUser = getUserDetails(inpUsername, inpPassword);
                        SessionManager session = new SessionManager(requireContext());
                        session.setLogin(true);
                        //session.loadUserDets(logUser);
                        Intent intent = new Intent(requireActivity(), ActDisplayCentre.class);
                        startActivity(intent);

                    }else{
                        lblUsername.setText("Incorrect details provided");
                        inpUsername.setText("");
                        lblPassword.setText("Working");
                        inpPassword.setText("");
                    }
                } else {
                    lblUsername.setText("Cannot be empty");
                    inpUsername.setText("");
                    lblPassword.setText("Working");
                    inpPassword.setText("");

                }

            }
        });


        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ActDisplayCentre.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}