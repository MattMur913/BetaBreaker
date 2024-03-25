package com.example.betabreaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Classes.ClsRoutes;
import com.example.betabreaker.Classes.GlobalUrl;
import com.example.betabreaker.databinding.FragmentSpecRouteBinding;


public class FragSpecRoute extends Fragment {

    private FragmentSpecRouteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpecRouteBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        TextView tvArea;
        TextView tvColour;
        TextView tvDate;
        TextView tvGrade;
        TextView tvSetter;
        TextView tvVotes;
        ImageView ivRoute;
        if (bundle != null) {
            ClsRoutes routes = (ClsRoutes) bundle.getSerializable("viewRoute");

            // Initialize views
            tvArea = binding.spRArea;
            tvColour = binding.spRColour;
            tvDate = binding.spRDate;
            tvGrade = binding.spRGrade;
            tvSetter = binding.spRSetter;
            tvVotes = binding.spRVotes;
            ivRoute = binding.spRClimb;

            // Display the data in the TextViews
            if (routes != null) {
                tvArea.setText(routes.getArea());
                tvColour.setText(routes.getColour());
                tvGrade.setText(routes.getGrades());
                tvSetter.setText(routes.getSetter());
                tvVotes.setText(routes.getUpvotes());
                tvDate.setText(routes.getSetDate());
                Glide.with(view.getContext()).load(GlobalUrl.imageUrl + routes.getImage()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image))
                        .into(ivRoute);
            }
            }
        }
}