package com.example.betabreaker.Frags;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentSpecCentreBinding;

import java.io.Serializable;
import java.util.List;

public class FragSpecCentre extends Fragment {

    private FragmentSpecCentreBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSpecCentreBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        final Button btnRoutes = binding.vewRoutes;
        Button btnFav = binding.btnFav;
        TextView txtName = binding.spCName;
        TextView txtAddress = binding.spCAddress;

        if (bundle != null) {
            ClsCentre centre = (ClsCentre) bundle.getSerializable("centre");
            if (centre != null) {
                txtAddress.setText(centre.getAddress());
                txtName.setText(centre.getCentreName());
            }

            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("favCent", centre.getIdCentre());
                    editor.apply();
                    //TODO Add a toast here
                }
            });


            btnRoutes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragDisplayRoutes fragment = new FragDisplayRoutes();
                    Bundle bundle = new Bundle();
                    if (centre != null) {
                        List<ClsRoutes> routes = centre.getRoutes();
                        fragmentTransaction.remove(FragSpecCentre.this);
                        bundle.putSerializable("routes", (Serializable) routes);
                        bundle.putSerializable("centreID", centre.getIdCentre());
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            });
        }

    }}