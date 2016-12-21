package com.bellman.pm.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bellman.pm.R;
import com.bellman.pm.home.interactor.HomeInteractor;
import com.bellman.pm.home.model.Movie;

import java.util.ArrayList;


public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View mView;
    private final HomeInteractor mInteractor;
    private final Context mContext;
    private String TAG = HomeInteractor.class.getSimpleName();

    public HomePresenter(HomeContract.View view, Context context) {

        mContext = context;
        mInteractor = new HomeInteractor(this);
        mView = view;
        mView.setPresenter(this);

    }

    @Override
    public void start(long movieId) {
        loadData();
    }

    @Override
    public void dismissProgress() {
        mView.hideLoadingProgress();
    }

    @Override
    public void setMovies(ArrayList<Movie> movies) {
        mView.setMovies(movies);
    }


    @Override
    public void showEmptyView() {
        mView.showEmptyView(HomeFragment.EMPTY_VIEW_TYPE_SERVER);
    }

    @Override
    public void hideEmptyView() {

        mView.hideEmptyView();
    }

    @Override
    public void refreshMovies() {
        mView.showLoadingProgress();
        loadData();
    }

    @Override
    public void loadData() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mSortOrder = settings.getString(mContext.getString(R.string.pref_sort_order_key),
                mContext.getString(R.string.pref_sorting_order_default));
        if (mSortOrder.equals(mContext.getString(R.string.sort_item_favorite))) {
            mView.loadFavoriteMovies();
        } else {
            mInteractor.getMovies(mSortOrder);
        }
    }
}
