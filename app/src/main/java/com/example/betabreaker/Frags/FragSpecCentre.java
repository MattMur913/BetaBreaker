package com.example.betabreaker.Frags;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Classes.ClsCentre;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
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
        ImageView imgView = binding.spCLogoview;
        Button btnFav = binding.btnFav;
        TextView txtName = binding.spCName;
        TextView txtWebsite = binding.spCWebsite;
        TextView txtEmail = binding.spCEmail;
        TextView txtCoNo = binding.spCNumber;
        TextView txtAddress = binding.spCAddress;

        if (bundle != null) {
            ClsCentre centre = (ClsCentre) bundle.getSerializable("centre");
            if (centre != null) {
                Glide.with(view.getContext()).load(GlobalUrl.imageUrl + centre.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(imgView);
                txtAddress.setText(centre.getAddress());
                txtName.setText(centre.getCentreName());
                txtWebsite.setText(centre.getWebsite());
                txtCoNo.setText(centre.getNumber());
                txtEmail.setText(centre.getEmail());
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
                    Bundle bundle = new Bundle();
                    List<ClsRoutes> routes = centre.getRoutes();
                    bundle.putSerializable("routes", (Serializable) routes);
                    bundle.putSerializable("centreID", centre.getIdCentre());
                    bundle.putSerializable("fragger", "");

                    NavHostFragment.findNavController(FragSpecCentre.this)
                            .navigate(R.id.go_spec_centre_to_display_route, bundle);

                }
            });
        }

    }}