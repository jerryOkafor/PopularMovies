package com.bellman.pm.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bellman.pm.R;
import com.bellman.pm.home.interactor.HomeInteractor;
import com.bellman.pm.home.model.Movie;

import java.util.ArrayList;

/**
 * Created by Potencio on 11/20/2016.
 * <p>
 * Copyright 2016, Potencio
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    public void start() {
        mView.setRefreshing();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mSortOrder = settings.getString(mContext.getString(R.string.pref_sorting_order_key), "");
        mInteractor.getMovies(mSortOrder);

    }

    @Override
    public void dismissProgress() {
        mView.setNotRefreshing();
    }

    @Override
    public void setMovies(ArrayList<Movie> movies) {
        mView.setmMovies(movies);
    }

    @Override
    public void showEmptyView() {
        mView.showEmptyView();
    }

    @Override
    public void hideEmptyView() {

        mView.hideEmptyView();
    }

    @Override
    public void refreshMovies() {
        mView.setRefreshing();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mSortOrder = settings.getString(mContext.getString(R.string.pref_sorting_order_key), "");

        mInteractor.getMovies(mSortOrder);
    }
}
