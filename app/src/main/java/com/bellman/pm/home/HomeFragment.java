package com.bellman.pm.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bellman.pm.R;
import com.bellman.pm.data.PopularMoviesContract;
import com.bellman.pm.databinding.HomeFragmentBinding;
import com.bellman.pm.detail.MovieDetailActivity;
import com.bellman.pm.home.adapter.MoviesAdapter;
import com.bellman.pm.home.model.Movie;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment implements HomeContract.View, AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnChildScrollUpCallback,
        AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 101;
    public static final int EMPTY_VIEW_TYPE_SERVER = 102;
    public static final int EMPTY_VIEW_TYPE_LOCAL = 103;
    ArrayList<Movie> mMovies = new ArrayList<>();
    ArrayList<Movie> favouriteMovies = new ArrayList<>();
    private HomeContract.Presenter mPresenter;
    private MoviesAdapter mAdapter;
    private LinearLayout mEmptyView;
    private GridView movieGridView;
    private Spinner mSpinner;
    private SwipeRefreshLayout mSwipeToRefresh;
    private HomeFragmentBinding mBinding;
    private boolean mIsFavoriteSelected = false;

    public HomeFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new HomePresenter(this, getContext());
        if (savedInstanceState == null) {
            mPresenter.start(0);
        }
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);

        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(HomeActivity.EXTRA_MOVIE_LIST);
        }

        setHasOptionsMenu(true);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        //get my custom adapter here
        mAdapter = new MoviesAdapter(getContext(), mMovies);
        movieGridView = mBinding.movieGrid;
        mSwipeToRefresh = mBinding.swipeToRefresh;
        mSpinner = mBinding.spinnerSort;

        mSwipeToRefresh.setColorSchemeResources(R.color.colorAccent, R.color.settings_bg);
        mSwipeToRefresh.setOnRefreshListener(this);
        mSwipeToRefresh.setOnChildScrollUpCallback(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_entries, R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        //set the spinner selected item to be the value of what is in the
        //shared pref
        setSpinnerSelectedItem();
        mEmptyView = mBinding.noMovieEmptyView;
        movieGridView.setAdapter(mAdapter);
        movieGridView.setOnItemClickListener(this);
        View mRootView = mBinding.getRoot();
        return mRootView;
    }

    private void setSpinnerSelectedItem() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = preferences.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sorting_order_default));
        if (sortOrder.equals(getString(R.string.sort_item_top_rated))) {
            mSpinner.setSelection(0);
        } else if (sortOrder.equals(getString(R.string.sort_item_popular))) {
            mSpinner.setSelection(1);
        } else if (sortOrder.equals(getString(R.string.sort_item_favorite))) {
            mSpinner.setSelection(2);
        }
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        //ensure that we are ot setting a null value
        mPresenter = checkNotNull(presenter);

    }


    @Override
    public void showEmptyView(int type) {
        switch (type) {
            case EMPTY_VIEW_TYPE_SERVER:
                break;
            case EMPTY_VIEW_TYPE_LOCAL:
                mBinding.emptyViewTitle.setText(getString(R.string.empty_favorite));
                mBinding.emptyViewSubTitle.setText(getString(R.string.empty_favorite_sub_title));
                break;
        }
        if (mEmptyView.getVisibility() == View.INVISIBLE) {
            mEmptyView.setVisibility(View.VISIBLE);
        }

        if (movieGridView.getVisibility() == View.VISIBLE) {
            movieGridView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hideEmptyView() {
        if (mEmptyView.getVisibility() == View.VISIBLE) {
            mEmptyView.setVisibility(View.INVISIBLE);
        }
        if (movieGridView.getVisibility() == View.INVISIBLE) {
            movieGridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadFavoriteMovies() {
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);

    }

    @Override
    public void showLoadingProgress() {
        if (!mSwipeToRefresh.isRefreshing()) {
            mSwipeToRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideLoadingProgress() {
        if (mSwipeToRefresh.isRefreshing()) {
            mSwipeToRefresh.setRefreshing(false);
        }
    }

    @Override
    public void setMovies(ArrayList<Movie> movies) {
        mAdapter.clear();
        hideEmptyView();
        //simple call the mAdapter.addAll() method here
        mMovies = movies;
        mAdapter.addAll(movies);
        mAdapter.notifyDataSetChanged();
        hideLoadingProgress();

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent mDetailIntent = new Intent(getContext(), MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(HomeActivity.EXTRA_MOVIE, (Movie) adapterView.getItemAtPosition(position));
        mDetailIntent.putExtras(bundle);
        startActivity(mDetailIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(HomeActivity.EXTRA_MOVIE_LIST, mMovies);
    }


    @Override
    public void onRefresh() {
        mPresenter.refreshMovies();
    }

    @Override
    public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View childView) {
        if (Build.VERSION.SDK_INT >= 14) {
            // For ICS and above we can call canScrollVertically() to determine this
            return ViewCompat.canScrollVertically(childView, -1);
        } else {
            if (childView instanceof AbsListView) {
                // Pre-ICS we need to manually check the first visible item and the child view's top
                // value
                final AbsListView gridView = (AbsListView) childView;
                return gridView.getChildCount() > 0 &&
                        (gridView.getFirstVisiblePosition() > 0
                                || gridView.getChildAt(0).getTop() < gridView.getPaddingTop());
            } else {
                // For all other view types we just check the getScrollY() value
                return childView.getScrollY() > 0;
            }


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        //persist the users choice as the default for next visit
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        String sortOrder = "";
        if (position == 0) {
            sortOrder = getString(R.string.sort_item_top_rated);
            mIsFavoriteSelected = false;
        } else if (position == 1) {
            sortOrder = getString(R.string.sort_item_popular);
            mIsFavoriteSelected = false;
        } else if (position == 2) {
            sortOrder = getString(R.string.sort_item_favorite);
            mIsFavoriteSelected = true;
        }
        editor.putString(getString(R.string.pref_sort_order_key), sortOrder);
        editor.apply();
        //call the presenter refresh method to
        //load new movies based on the user sort choice
        mPresenter.refreshMovies();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing when nothing is selected

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                PopularMoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favouriteMovies.clear();
        if (data != null && data.getCount() > 0) {
            while (data.moveToNext()) {
                favouriteMovies.add(
                        new Movie(
                                data.getString(2),
                                data.getString(3),
                                data.getString(4),
                                data.getInt(1),
                                data.getString(5),
                                data.getDouble(6),
                                data.getDouble(7)
                        ));
                if (data.isLast() && mIsFavoriteSelected) {
                    setMovies(favouriteMovies);
                }
            }


        } else {
            showEmptyView(EMPTY_VIEW_TYPE_LOCAL);
            hideLoadingProgress();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();

    }


}
