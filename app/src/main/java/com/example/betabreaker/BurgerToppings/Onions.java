package com.example.betabreaker.BurgerToppings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.betabreaker.databinding.FragmentOnionsBinding;

public class Onions extends Fragment {

    private FragmentOnionsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOnionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContainerView fragDisplay = binding.FragOnions;
        TextView txtFav = binding.textView4;
        if (isFavouriteCentreEmpty()) {
           fragDisplay.setVisibility(View.GONE);
           txtFav.setVisibility(View.VISIBLE);

        }
    }

    private boolean isFavouriteCentreEmpty() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String favouriteCentre = preferences.getString("favCent", "");
        return favouriteCentre.isEmpty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
