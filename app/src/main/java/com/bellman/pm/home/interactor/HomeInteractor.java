package com.bellman.pm.home.interactor;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.bellman.pm.ServiceGenerator;
import com.bellman.pm.data.PopularMoviesContract;
import com.bellman.pm.home.HomePresenter;
import com.bellman.pm.home.model.Movie;
import com.bellman.pm.home.model.MovieContainer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeInteractor {

    private static final String TAG = HomeInteractor.class.getSimpleName();
    private final MovieService service;
    private final HomePresenter mPresenter;

    public HomeInteractor(HomePresenter presenter) {
        //create a simple REST adapter which points to themoviedb's Api

        service = ServiceGenerator.createService(MovieService.class);
        mPresenter = presenter;

    }

    public void getMovies(String sortOrder) {

        service.loadMoviesFromServer(sortOrder).enqueue(new Callback<MovieContainer>() {
            @Override
            public void onResponse(Call<MovieContainer> call, Response<MovieContainer> response) {
                //handle the response here
                if (response.isSuccessful())
                {
                    //get the list of the movies
                    ArrayList<Movie> movies =  response.body().getResults();
                    if (!movies.isEmpty()) {

                        mPresenter.setMovies(movies);
                    }else {
                        mPresenter.showEmptyView();
                    }

                    mPresenter.dismissProgress();
                }else {
                    //handle some other errors here
                    Log.e(TAG,"Error fetching movies: "+response.code());
                    mPresenter.dismissProgress();
                    mPresenter.showEmptyView();

                }
            }

            @Override
            public void onFailure(Call<MovieContainer> call, Throwable t) {
                Log.e(TAG,"Error fetching movies: "+t.getMessage());
                mPresenter.dismissProgress();
                mPresenter.showEmptyView();

            }
        });
    }
}
