package com.teamona.gourd.app;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by teamona on 16/04/19.
 */
//2階層
public class SwipeAdapter extends FragmentStatePagerAdapter {

    public static final int PAGE = 5;

    public SwipeAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        SwipeFragment fragment = new SwipeFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE;
    }
}
