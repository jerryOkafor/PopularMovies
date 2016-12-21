package com.bellman.pm.detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bellman.pm.R;
import com.bellman.pm.adapter.CustomAdapter;
import com.bellman.pm.data.PopularMoviesContract;
import com.bellman.pm.databinding.MovieDetailFragmentBinding;
import com.bellman.pm.detail.model.Trailer;
import com.bellman.pm.detail.viewholder.TrailerHolder;
import com.bellman.pm.home.HomeActivity;
import com.bellman.pm.home.model.Movie;
import com.bellman.pm.review.MoreReviewActivity;
import com.bellman.pm.review.model.Review;
import com.bellman.pm.util.PopularMovieUtil;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment implements DetailContract.View,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private static final int MOVIE_DETAIL_LOADER = 104;
    private Movie mMovie;
    private MovieDetailFragmentBinding mBinding;
    private DetailContract.Presenter mPresenter;
    private ArrayList<Trailer> mTrailers;
    private CustomAdapter<Trailer, TrailerHolder> mTrailerAdapter;
    private ProgressDialog mProgress;
    private ArrayList<Review> mReview;
    private boolean mIsFavorite;
    private MenuItem favoriteMenuItem;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(Bundle bundle) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new DetailPresenter(this);
        mPresenter.start(mMovie.getId());
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(HomeActivity.EXTRA_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.movie_detail_fragment, container, false);
        View rootView = mBinding.getRoot();

        setHasOptionsMenu(true);

        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Please wait...");
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

        }

        mBinding.setMovie(mMovie);
        mBinding.tvMore.setOnClickListener(this);
        //set the title of the activity as the movie title
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mMovie.getOriginalTitle());
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setSubtitle(PopularMovieUtil.formatReleaseDate(mMovie.getReleaseDate()));

        }

        mTrailers = new ArrayList<>();
        RecyclerView mTrailerRecycler = mBinding.trailerRecycler;
        mTrailerRecycler.setNestedScrollingEnabled(true);
        mTrailerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mTrailerAdapter = new CustomAdapter<Trailer, TrailerHolder>(Trailer.class, TrailerHolder.class, R.layout.trailer_item, mTrailers) {
            @Override
            protected void populateViewHolder(TrailerHolder holder, int position, final Trailer model) {
                holder.getBinding().setTrailer(model);
                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, PopularMovieUtil.buildTrailerUrl(model.getKey())));
                    }
                });

            }
        };
        mTrailerRecycler.setAdapter(mTrailerAdapter);
        return rootView;
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);

    }

    @Override
    public void setReviews(ArrayList<Review> reviews) {
        mReview = reviews;
        if (reviews.size() > 0) {
            mBinding.tvReviewAuthor.setText(reviews.get(0).getAuthor());
            mBinding.tvReviewContent.setText(reviews.get(0).getContent());
        } else {
            mBinding.tvReviewAuthor.setVisibility(View.INVISIBLE);
            mBinding.tvReviewContent.setVisibility(View.INVISIBLE);
            mBinding.tvMore.setVisibility(View.INVISIBLE);
            mBinding.tvReviewsTitle.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void setTrailers(ArrayList<Trailer> trailers) {
        if (trailers.size() > 0) {
            mTrailerAdapter.addAll(trailers);
            mTrailerAdapter.notifyDataSetChanged();
        } else {
            mBinding.trailerTitleTv.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        favoriteMenuItem = menu.findItem(R.id.action_add_to_favourite);
        if (isMovieFavorite()) {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite);
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_to_favourite:
                //add the movie to the favourite list
                insertFavourite();
                break;
            case R.id.action_share:
                shareMovie();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressDialog() {
        if (!mProgress.isShowing()) {
            mProgress.show();
        }

    }

    @Override
    public void hideProgressDialog() {

        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void showMoreReviewDialog() {
        Intent reviewIntent = new Intent(getContext(), MoreReviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MoreReviewActivity.EXTRA_REVIEW, mReview);
        reviewIntent.putExtras(bundle);
        startActivity(reviewIntent);
    }

    @Override
    public boolean isMovieFavorite() {
        return mIsFavorite;
    }

    @Override
    public void shareMovie() {
        if (mTrailers.size() == 0) {
            Toast.makeText(getContext(), getString(R.string.can_not_share_movie), Toast.LENGTH_LONG).show();

        } else {
            String shareString = String.format(getString(R.string.share_movie_string_format),
                    PopularMovieUtil.buildTrailerUrl(mTrailers.get(0).getKey()));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            shareIntent.setType(getString(R.string.share_intent_type));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_popular_movies_with)));
        }
    }

    @Override
    public void insertFavourite() {
        if (!isMovieFavorite()) {
            getActivity().getContentResolver()
                    .insert(
                            PopularMoviesContract.MoviesEntry.CONTENT_URI,
                            PopularMovieUtil.buildContentValuesFromMovie(mMovie)
                    );
            favoriteMenuItem.setIcon(R.drawable.ic_favorite);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_more:
                showMoreReviewDialog();
                break;
            default:
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = PopularMoviesContract.MoviesEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(mMovie.getId())};

        return new CursorLoader(getContext(),
                PopularMoviesContract.MoviesEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            mIsFavorite = false;
        } else if (cursor.getCount() < 1) {
            mIsFavorite = false;
        } else if (cursor.moveToFirst()) {
            cursor.close();
            mIsFavorite = true;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
