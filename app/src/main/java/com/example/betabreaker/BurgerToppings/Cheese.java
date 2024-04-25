package com.example.betabreaker.BurgerToppings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragCheese, displayCentresFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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
