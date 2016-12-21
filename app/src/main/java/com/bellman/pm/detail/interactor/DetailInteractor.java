package com.bellman.pm.detail.interactor;

import android.util.Log;

import com.bellman.pm.ServiceGenerator;
import com.bellman.pm.detail.DetailPresenter;
import com.bellman.pm.detail.model.ResultContainer;
import com.bellman.pm.review.model.Review;
import com.bellman.pm.detail.model.Trailer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Potencio on 12/1/2016.
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

public class DetailInteractor {
    private static final String TAG = DetailInteractor.class.getSimpleName();
    private final DetailService mService;
    private final DetailPresenter mPresenter;

    public DetailInteractor(DetailPresenter presenter) {
        mService = ServiceGenerator.createService(DetailService.class);
        mPresenter = presenter;
    }

    public void getVideos(long movieId) {
        mService.getVideos(movieId).enqueue(new Callback<ResultContainer<Trailer>>() {
            @Override
            public void onResponse(Call<ResultContainer<Trailer>> call, Response<ResultContainer<Trailer>> response) {
                mPresenter.setTrailers(response.body().getResults());

            }

            @Override
            public void onFailure(Call<ResultContainer<Trailer>> call, Throwable t) {

                Log.e(TAG, "Unable to get trailers: " + t.getMessage());

            }
        });

    }


    public void getReviews(long movieId) {
        mService.getReviews(movieId).enqueue(new Callback<ResultContainer<Review>>() {
            @Override
            public void onResponse(Call<ResultContainer<Review>> call, Response<ResultContainer<Review>> response) {
                mPresenter.setReviews(response.body().getResults());

            }

            @Override
            public void onFailure(Call<ResultContainer<Review>> call, Throwable t) {
                Log.e(TAG, "Unable to get reviews: " + t.getMessage());

            }
        });

    }
}
