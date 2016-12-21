package com.bellman.pm.detail.interactor;

import com.bellman.pm.detail.model.ResultContainer;
import com.bellman.pm.review.model.Review;
import com.bellman.pm.detail.model.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

public interface DetailService {

    @GET("movie/{movieId}/videos")
    Call<ResultContainer<Trailer>> getVideos(@Path("movieId") long movieId);

    @GET("movie/{movieId}/reviews")
    Call<ResultContainer<Review>> getReviews(@Path("movieId") long movieId);
}
