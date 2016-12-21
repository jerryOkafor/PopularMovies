package com.bellman.pm.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bellman.pm.R;
import com.bellman.pm.home.model.Movie;
import com.bellman.pm.util.PopularMovieUtil;
import com.bumptech.glide.Glide;

import java.util.List;


public class MoviesAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();


    private static class ViewHolder {
        private final ImageView imageView;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }

    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the movie object fro the corresponding position
        final Movie movieItem = getItem(position);

        //get the convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        //load the image using Glide
        String url = PopularMovieUtil.buildPosterUrl(movieItem.getPosterPath());
        Glide.with(getContext())
                .load(url)
                .into(viewHolder.imageView);
        return convertView;
    }
}
