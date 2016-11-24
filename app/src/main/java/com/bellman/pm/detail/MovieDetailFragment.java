package com.bellman.pm.detail;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellman.pm.R;
import com.bellman.pm.home.HomeActivity;
import com.bellman.pm.home.HomeFragment;
import com.bellman.pm.home.model.Movie;
import com.bellman.pm.util.PopularMovieUtil;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private Movie mMovie;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(Bundle bundle){
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
        {
            mMovie = getArguments().getParcelable(HomeActivity.EXTRA_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.movie_detail_fragment, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

        }

        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_original_title);
        ImageView posterImageView = (ImageView) rootView.findViewById(R.id.imgv_detail_poster);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        TextView tvVoteAverage = (TextView) rootView.findViewById(R.id.tv_vote_average);
        TextView tvOverview = (TextView) rootView.findViewById(R.id.tv_overview);

        //bind all the details
        tvTitle.setText(mMovie.getOriginalTitle());
        tvReleaseDate.setText(PopularMovieUtil.formatReleaseDate(mMovie.getReleaseDate()));
        tvVoteAverage.setText(String.format(getString(R.string.fmt_vote_average),mMovie.getVoteAverage()));
        tvOverview.setText(mMovie.getOverview());
        //set the title of the activity as the movie title
        if (((AppCompatActivity)getActivity()).getSupportActionBar() !=null)
        {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mMovie.getOriginalTitle());
        }

        //load the poster image using Glide
        Glide.with(getContext())
                .load(PopularMovieUtil.buildPosterUrl(mMovie.getPosterPath()))
                .into(posterImageView);

        return rootView;
    }

}
