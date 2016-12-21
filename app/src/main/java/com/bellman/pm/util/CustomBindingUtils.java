package com.bellman.pm.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellman.pm.R;
import com.bumptech.glide.Glide;

/**
 * Created by Potencio on 12/6/2016. @ 6:09 AM
 * For PopularMovies
 */

public class CustomBindingUtils {

    @BindingAdapter("bind:poster")
    public static void setPoster(ImageView imageView, String posterPath) {
        Glide.with(imageView.getContext())
                .load(PopularMovieUtil.buildPosterUrl(posterPath))
                .into(imageView);

    }

    @BindingAdapter("bind:releaseDate")
    public static void setReleaseDate(TextView textView, String releaseDate) {
        textView.setText(PopularMovieUtil.formatReleaseDate(releaseDate));
    }

    @BindingAdapter("bind:voteAverage")
    public static void setVoteAverage(TextView textView, double voteAverage) {
        textView.setText(String.format(textView.getContext().getString(R.string.fmt_vote_average), voteAverage));
    }
}
