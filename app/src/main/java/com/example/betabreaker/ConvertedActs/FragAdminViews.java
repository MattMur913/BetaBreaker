package com.example.betabreaker.ConvertedActs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Frags.FragAddRoute;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentAdminViewsBinding;

import java.util.ArrayList;
import java.util.List;

public class FragAdminViews extends Fragment {

    private List<ClsCentre> centreList = new ArrayList<>();
    private FragmentAdminViewsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminViewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String centreID = preferences.getString("adminOf", "");
        final Button btnAddRoute = binding.addRoute;

        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager(); // Use getChildFragmentManager() here
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragAddRoute fragment = new FragAddRoute();
                Bundle bundle = new Bundle();
                bundle.putSerializable("centre", centreID);
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                btnAddRoute.setVisibility(View.GONE);
            }
        });
    }
}
