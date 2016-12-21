package com.bellman.pm.home.interactor;

import com.bellman.pm.home.model.MovieContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;



public interface MovieService {
    @GET("movie/{sortType}")
    Call<MovieContainer> loadMoviesFromServer(@Path("sortType") String sortType);

}
