package com.bellman.pm.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bellman.pm.R;
import com.bellman.pm.detail.MovieDetailActivity;
import com.bellman.pm.home.HomeActivity;
import com.bellman.pm.home.model.Movie;
import com.bellman.pm.util.PopularMovieUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Potencio on 11/19/2016.
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

public class MoviesAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    /**
     * my custom constructor
     *
     * @param context The contxt that is used to inflate the layout file
     * @param movies  List of the movies to be displayed in GridView
     */
    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    /**
     * Provide view for my GridView
     *
     * @param position    The postion of the adapter that is requesting a view
     * @param convertView The recycled view to populate
     * @param parent      The parent view that is used to inflate the layout.
     * @return The view for the position in adapter view.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the movie object fro the corresponding position
        final Movie movieItem = getItem(position);

        //get the convertView
        if (convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item,parent,false);
        }

        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);

        //load the image using Glide
        String url = PopularMovieUtil.buildPosterUrl(movieItem.getPosterPath());
        Glide.with(getContext())
                .load(url)
                .into(moviePoster);
        return convertView;
    }
}
