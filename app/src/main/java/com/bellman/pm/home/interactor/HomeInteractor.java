package com.bellman.pm.home.interactor;

import android.util.Log;

import com.bellman.pm.ServiceGenerator;
import com.bellman.pm.home.HomePresenter;
import com.bellman.pm.home.model.Movie;
import com.bellman.pm.home.model.MovieContainer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

public class HomeInteractor {

    private static final String TAG = HomeInteractor.class.getSimpleName();
    private final MovieService service;
    private final HomePresenter mPresenter;

    public HomeInteractor(HomePresenter presenter)
    {
        //create a simple REST adapter which points to themoviedb's Api

        service = ServiceGenerator.createService(MovieService.class);
        mPresenter = presenter;

    }

    public void getMovies(String sortOrder)
    {
        service.getMovies(sortOrder).enqueue(new Callback<MovieContainer>() {
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
