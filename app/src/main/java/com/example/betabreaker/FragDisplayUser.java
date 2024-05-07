package com.example.betabreaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.betabreaker.databinding.FragmentDisplayUserBinding;

public class FragDisplayUser extends Fragment {

    private FragmentDisplayUserBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDisplayUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView lblUsername = binding.txtName2;
        final TextView lblDob = binding.txtDob2;
        final TextView lblEmail = binding.txtEmail2;
        final Button btnSign =binding.btnEdit;

        //Displays the users information
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username = preferences.getString("username","");
        String email = preferences.getString("email","");
        String dob = preferences.getString("dob","");

        lblUsername.setText(username);
        lblDob.setText(dob);
        lblEmail.setText(email);


        //Takes to the edit user page
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(FragDisplayUser.this)
                        .navigate(R.id.go_edit_user);
            }
        });


    }



}