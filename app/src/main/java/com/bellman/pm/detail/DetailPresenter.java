package com.bellman.pm.detail;

import com.bellman.pm.detail.interactor.DetailInteractor;
import com.bellman.pm.review.model.Review;
import com.bellman.pm.detail.model.Trailer;

import java.util.ArrayList;

/**
 * Created by Potencio on 12/1/2016. @ 3:04 AM
 * For PopularMovies
 */

public class DetailPresenter implements DetailContract.Presenter {
    private final DetailContract.View mView;
    private final DetailInteractor mInteractor;

    public DetailPresenter(DetailContract.View view) {
        mView = view;
        mInteractor = new DetailInteractor(this);
        mView.setPresenter(this);

    }

    @Override
    public void getVideos(long movieId) {

        mInteractor.getVideos(movieId);
    }

    @Override
    public void getReviews(long movieId) {
        mInteractor.getReviews(movieId);

    }

    @Override
    public void setReviews(ArrayList<Review> reviews) {
        hideProgress();
        mView.setReviews(reviews);
    }

    @Override
    public void setTrailers(ArrayList<Trailer> trailers) {
        hideProgress();
        mView.setTrailers(trailers);

    }

    @Override
    public void showProgress() {
        mView.showProgressDialog();
    }

    @Override
    public void hideProgress() {

        mView.hideProgressDialog();
    }

    @Override
    public void start(long movieId) {
        showProgress();
        getReviews(movieId);
        getVideos(movieId);
    }
}
