package com.example.betabreaker.Classes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    public ViewPageAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    public void updateCurrentFragmentContent(FragmentManager fragmentManager, int viewPagerId) {
        int currentItem = getCurrentItem(fragmentManager, viewPagerId);
        if (currentItem != -1) {
            Fragment currentFragment = fragmentManager.findFragmentByTag("android:switcher:" + viewPagerId + ":" + currentItem);
            if (currentFragment != null) {
                // You can add specific conditions based on the fragment class if needed
                if (currentFragment instanceof UpdateableFragment) {
                    ((UpdateableFragment) currentFragment).updateContent();
                }
            }
        }
    }

    private int getCurrentItem(FragmentManager fragmentManager, int viewPagerId) {
        Fragment fragment = fragmentManager.findFragmentByTag("f" + viewPagerId);
        if (fragment != null && fragment instanceof FragmentPagerAdapter) {
            return ((FragmentPagerAdapter) fragment).getItemPosition(fragment);
        }
        return -1;
    }
}
