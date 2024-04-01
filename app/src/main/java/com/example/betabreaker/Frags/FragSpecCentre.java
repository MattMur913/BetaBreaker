package com.example.betabreaker.Frags;

import android.os.Bundle;
import android.util.Log;
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

    private  FragmentSpecCentreBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        //View view = inflater.inflate(R.layout.fragment_spec_centre, container, false);
        binding = FragmentSpecCentreBinding.inflate(inflater, container, false);

        return binding.getRoot();
        // Retrieve bundle arguments
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        final Button btnRoutes = binding.vewRoutes;
        TextView textViewCentreId;
        TextView textViewCentreName;
        if (bundle != null) {
            ClsCentre centre = (ClsCentre) bundle.getSerializable("centre");
            Log.d("SingleCentre1", centre.getCentreName());
            Log.d("SingleCentre1", centre.getIdCentre());
            Log.d("SingleCentre1", centre.getAddress());
            Log.d("SingleCentre1", centre.getEmail());
            Log.d("SingleCentre1", centre.getlogo());
            Log.d("SingleCentre1", centre.getDescription());

            // Initialize views
            textViewCentreId = binding.spCName;
            textViewCentreName = binding.spCAddress;

            // Display the data in the TextViews
            if (centre != null) {
                textViewCentreId.setText(centre.getIdCentre());
                textViewCentreName.setText(centre.getCentreName());
                // Set other TextViews accordingly
            }
        }
        btnRoutes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragDisplayRoutes fragment = new FragDisplayRoutes();
                Bundle bundle = new Bundle();
                ClsCentre centre = (ClsCentre) getArguments().getSerializable("centre");
                List<ClsRoutes> routes = centre.getRoutes();
                bundle.putSerializable("routes", (Serializable) routes);
                bundle.putSerializable("centreID",centre.getIdCentre());
                Log.d("SingleCentre5", String.valueOf(centre.getIdCentre()));
                fragment.setArguments(bundle);
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.specCentreLayout);
                if (currentFragment != null) {
                    fragmentTransaction.remove(currentFragment);
                }

                fragmentTransaction.replace(R.id.specCentreLayout , fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }
}
