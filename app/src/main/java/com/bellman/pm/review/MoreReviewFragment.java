package com.bellman.pm.review;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bellman.pm.R;
import com.bellman.pm.adapter.CustomAdapter;
import com.bellman.pm.databinding.MoreReviewFragmentBinding;
import com.bellman.pm.review.model.Review;
import com.bellman.pm.detail.viewholder.ReviewHolder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreReviewFragment extends Fragment {


    private ArrayList<Review> reviews;

    public MoreReviewFragment() {
        // Required empty public constructor
    }

    public static MoreReviewFragment newInstance(Bundle bundle) {
        MoreReviewFragment fragment = new MoreReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reviews = getArguments().getParcelableArrayList(MoreReviewActivity.EXTRA_REVIEW);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MoreReviewFragmentBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.more_review_fragment, container, false);
        View rootView = mBinding.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        }
        //set the layout manager
        RecyclerView reviewRecycler = mBinding.reviewRecycler;
        reviewRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        CustomAdapter<Review, ReviewHolder> mReviewAdapter = new CustomAdapter<Review, ReviewHolder>(Review.class, ReviewHolder.class, R.layout.review_item, reviews) {

            @Override
            protected void populateViewHolder(ReviewHolder holder, int position, Review model) {
//                holder.getBinding().setReview(model);
                //do nothing for now.
                holder.getBinding().setReview(model);
            }
        };
        reviewRecycler.setAdapter(mReviewAdapter);


        return rootView;
    }

}
