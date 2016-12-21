package com.bellman.pm.detail.viewholder;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bellman.pm.databinding.TrailerItemBinding;

/**
 * Created by Potencio on 12/3/2016. @ 1:05 PM
 * For PopularMovies
 */

public class TrailerHolder extends RecyclerView.ViewHolder {
    private final TrailerItemBinding mBinding;

    public TrailerHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    public TrailerItemBinding getBinding() {
        return mBinding;
    }
}
