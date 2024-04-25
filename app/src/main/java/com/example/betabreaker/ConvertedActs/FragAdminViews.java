package com.example.betabreaker.ConvertedActs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Editables.FragEditCentre;
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
         FragEditCentre displayFragment = new FragEditCentre();

        // Add FragDisplayCentres fragment to the layout
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragContent, displayFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
