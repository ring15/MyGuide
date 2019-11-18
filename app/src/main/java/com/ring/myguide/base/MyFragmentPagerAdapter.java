package com.ring.myguide.base;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by ring on 2019/11/18.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] mFragments;

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, Fragment[] fragments) {
        super(fm, behavior);
        mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
