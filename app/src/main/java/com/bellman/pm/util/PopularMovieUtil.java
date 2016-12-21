package com.bellman.pm.util;

import android.content.ContentValues;
import android.net.Uri;

import com.bellman.pm.data.PopularMoviesContract;
import com.bellman.pm.home.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PopularMovieUtil {
    public static String buildPosterUrl(String posterPath) {
        return "http://image.tmdb.org/t/p/w185/" + posterPath;
    }

    public static String formatReleaseDate(String releaseDate) {

        if (releaseDate == null) {
            return "";
        }

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        Date date = null;
        try {
            date = originalFormat.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return newFormat.format(date);
    }

    public static ContentValues buildContentValuesFromMovie(Movie movie) {
        ContentValues movieValue = new ContentValues();
        movieValue.put(PopularMoviesContract.MoviesEntry.COLUMN_ID, movie.getId());
        movieValue.put(PopularMoviesContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValue.put(PopularMoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        movieValue.put(PopularMoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        movieValue.put(PopularMoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieValue.put(PopularMoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        movieValue.put(PopularMoviesContract.MoviesEntry.COLUMN_POPULARITY, movie.getPopularity());
        return movieValue;
    }

    public static Uri buildTrailerUrl(String key) {
        return Uri.parse("http://www.youtube.com/watch?v=" + key);
    }
}
