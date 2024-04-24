package com.example.betabreaker.BurgerToppings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.betabreaker.ConvertedActs.FragDisplayCentres;
import com.example.betabreaker.R;
import com.example.betabreaker.databinding.FragmentCheeseBinding;

public class Cheese extends Fragment {
    private FragmentCheeseBinding binding;
    private FragDisplayCentres displayCentresFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheeseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize FragDisplayCentres fragment
        displayCentresFragment = new FragDisplayCentres();

        // Add FragDisplayCentres fragment to the layout
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_container, displayCentresFragment)
                .commit();

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Additional logic if needed
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
