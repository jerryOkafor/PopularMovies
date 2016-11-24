package com.bellman.pm;

import com.bellman.pm.home.model.Movie;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

public class ServiceGenerator {

    //get the bae url for themoviedb.org
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    //Build a custom gson to begin off at page/results
    //This is important so that we set a naming po
    private static Gson gson  = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    //get the retrofit builder
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    //build the http client that retrofit uses
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    /**
     * Creates the service class using the java interface and retrofit annotation
     *
     * @param serviceClass The class with interface and retrofit annotation
     * @return The service class
     */
    public static <S> S createService(Class<S> serviceClass) {
        //Lets add our api key query parameter to every request using interceptor
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl originalHttpUrl = originalRequest.url();

                //Build a nw HttpUrl
                HttpUrl newUrl = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key",BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .url(newUrl)
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);

            }
        });

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
