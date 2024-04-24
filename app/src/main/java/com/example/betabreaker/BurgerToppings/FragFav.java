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

import com.example.betabreaker.databinding.FragmentFavsBinding;

public class FragFav extends Fragment {

    private FragmentFavsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Checks if the user has a favourite centre or not
        FragmentContainerView fragDisplay = binding.FragOnions;
        TextView txtFav = binding.textView4;
        //if there is no favourite centre it hides the fragDisplay and replaces it with a textview saying there is no favourite centre
        if (isFavouriteCentreEmpty()) {
           fragDisplay.setVisibility(View.GONE);
           txtFav.setVisibility(View.VISIBLE);

        }
    }

    private boolean isFavouriteCentreEmpty() {
        //gets the shared preference
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
