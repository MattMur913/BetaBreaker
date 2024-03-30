package com.example.betabreaker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.betabreaker.Classes.ViewPageAdapter;
import com.example.betabreaker.ConvertedActs.FragDisplayCentres;
import com.example.betabreaker.ConvertedActs.FragUserView;
import com.google.android.material.tabs.TabLayout;

public class ActDisplayApp extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_app);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragDisplayCentres(), "Tab 1");
        adapter.addFragment(new FragUserView(), "Tab 2");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}