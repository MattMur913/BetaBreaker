package com.example.betabreaker.ConvertedActs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Classes.ConfirmationDialog;
import com.example.betabreaker.FragDisplayUser;
import com.example.betabreaker.MainActivity;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentUserViewBinding;


public class FragUserView extends Fragment {
    private FragmentUserViewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnOut = binding.btnLogOut;

        FragDisplayUser welcomeFragment = new FragDisplayUser();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.dspFragUV, welcomeFragment);
        transaction.commit();
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog.showConfirmationDialog(getContext(), "Are you sure you want log out?", new ConfirmationDialog.ConfirmationListener() {
                    @Override
                    public void onConfirm() {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

}