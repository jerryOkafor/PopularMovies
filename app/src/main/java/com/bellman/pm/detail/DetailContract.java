package com.bellman.pm.detail;

import com.bellman.pm.BasePresenter;
import com.bellman.pm.BaseView;
import com.bellman.pm.review.model.Review;
import com.bellman.pm.detail.model.Trailer;

import java.util.ArrayList;

/**
 * Created by Potencio on 12/1/2016. @ 3:04 AM
 * For PopularMovies
 */

public class DetailContract {
    interface View extends BaseView<Presenter>{
        void setReviews(ArrayList<Review> reviews);
        void setTrailers(ArrayList<Trailer> trailers);
        void showProgressDialog();
        void hideProgressDialog();
        void showMoreReviewDialog();
        boolean isMovieFavorite();
        void shareMovie();
        void insertFavourite();
    }
    interface Presenter extends BasePresenter{
        void getVideos(long movieId);
        void getReviews(long movieId);
        //return all the data when done
        void setReviews(ArrayList<Review> reviews);
        void setTrailers(ArrayList<Trailer> trailers);
        void showProgress();
        void hideProgress();

    }
}
