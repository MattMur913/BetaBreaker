package com.example.betabreaker;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Editables.FragEditUser;
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username = preferences.getString("username","");
        String email = preferences.getString("email","");
        String dob = preferences.getString("DoB","");

        lblUsername.setText(username);
        lblDob.setText(dob);
        lblEmail.setText(email);

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragEditUser fragment = new FragEditUser();


                fragmentTransaction.replace(R.id.dspFragUV, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }



}