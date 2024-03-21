package com.example.betabreaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.betabreaker.Classes.ClsCentre;

public class FragSpecCentre extends Fragment {private TextView textViewCentreId;
    private TextView textViewCentreName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spec_centre, container, false);

        // Retrieve bundle arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            ClsCentre centre = (ClsCentre) bundle.getSerializable("centre");

            // Initialize views
            textViewCentreId = view.findViewById(R.id.spCName);
            textViewCentreName = view.findViewById(R.id.spCAddress);

            // Display the data in the TextViews
            if (centre != null) {
                textViewCentreId.setText(centre.getIdCentre());
                textViewCentreName.setText(centre.getCentreName());
                // Set other TextViews accordingly
            }
        }

        return view;
    }
}
