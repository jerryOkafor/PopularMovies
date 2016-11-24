package com.bellman.pm.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bellman.pm.R;
import com.bellman.pm.SingleFragmentActivity;

public class MovieDetailActivity extends SingleFragmentActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        //get the Bundle Extra from the Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return MovieDetailFragment.newInstance(bundle);

        } else {
            Log.e(TAG, "Can not load a null movie bundle");
            finish();
        }
        return null;
    }

}
