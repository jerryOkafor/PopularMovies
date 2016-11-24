package com.bellman.pm.home;

import android.support.v4.app.Fragment;

import com.bellman.pm.SingleFragmentActivity;

public class HomeActivity extends SingleFragmentActivity {

    public static final String EXTRA_MOVIE = "com.bellman.pm.hom.EXTRA_MOVIE";
    public static final String EXTRA_MOVIE_LIST = "com.bellman.pm.hom.EXTRA_MOVIE_LIST";


    @Override
    protected Fragment createFragment() {
        return new HomeFragment();
    }
}
