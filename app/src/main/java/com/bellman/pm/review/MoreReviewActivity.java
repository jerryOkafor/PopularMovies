package com.bellman.pm.review;

import android.support.v4.app.Fragment;

import com.bellman.pm.SingleFragmentActivity;

public class MoreReviewActivity extends SingleFragmentActivity {
    public static final String EXTRA_REVIEW = "com.bellman.pm.review.EXTRA_REVIEW";

    @Override
    protected Fragment createFragment() {
        if (getIntent().getExtras() != null) {
            return MoreReviewFragment.newInstance(getIntent().getExtras());
        } else {
            finish();
        }
        return null;
    }
}
