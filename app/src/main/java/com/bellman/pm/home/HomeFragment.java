package com.bellman.pm.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.bellman.pm.detail.MovieDetailActivity;
import com.bellman.pm.home.adapter.MoviesAdapter;
import com.bellman.pm.home.model.Movie;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment implements HomeContract.View, AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnChildScrollUpCallback, AdapterView.OnItemSelectedListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    ArrayList<Movie> mMovies = new ArrayList<>();
    private HomeContract.Presenter mPresenter;
    private MoviesAdapter mAdapter;
    private LinearLayout mEmptyView;
    private GridView movieGridView;
    private Spinner mSpinner;
    private SwipeRefreshLayout mSwipeToRefresh;

    public HomeFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new HomePresenter(this, getContext());
        if (savedInstanceState == null) {
            mPresenter.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(HomeActivity.EXTRA_MOVIE_LIST);
        }
        setHasOptionsMenu(true);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        //get my custom adapter here
        mAdapter = new MoviesAdapter(getContext(), mMovies);
        movieGridView = (GridView) rootView.findViewById(R.id.movie_grid);
        mSwipeToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_to_refresh);
        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_sort);

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
        mEmptyView = (LinearLayout) rootView.findViewById(R.id.no_movie_empty_view);
        movieGridView.setAdapter(mAdapter);
        movieGridView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        //ensure that we are ot setting a null value
        mPresenter = checkNotNull(presenter);

    }

    @Override
    public void setRefreshing() {
        if (!mSwipeToRefresh.isRefreshing()) {
            mSwipeToRefresh.setRefreshing(true);
        }

    }

    @Override
    public void setNotRefreshing() {
        if (mSwipeToRefresh.isRefreshing()) {
            mSwipeToRefresh.setRefreshing(false);
        }

    }

    public void setmMovies(ArrayList<Movie> movies) {

        hideEmptyView();
        //simple call the mAdapter.addAll() method here
        mMovies = movies;
        mAdapter.addAll(movies);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void showEmptyView() {
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent mDetailIntent = new Intent(getContext(), MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(HomeActivity.EXTRA_MOVIE, (Movie) adapterView.getItemAtPosition(i));
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
        mAdapter.clear();
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
        editor.putString(getString(R.string.pref_sorting_order_key), getString(position == 0 ? R.string.sort_item_top_rated : R.string.sort_item_popular));
        editor.apply();
        //call the presenter refresh method to
        //load new movies based on the user sort choice
        mAdapter.clear();
        mPresenter.refreshMovies();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing when nothing is selected

    }
}
