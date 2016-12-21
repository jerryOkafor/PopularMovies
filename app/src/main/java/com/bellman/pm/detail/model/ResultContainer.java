package com.bellman.pm.detail.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Potencio on 12/1/2016. @ 3:20 AM
 * For PopularMovies
 */

public class ResultContainer<T> {

    @SerializedName("results")
    private ArrayList<T> results;

    public ResultContainer(ArrayList<T> results) {
        this.results = results;
    }

    public ArrayList<T> getResults() {
        return results;
    }
}
