package com.bellman.pm.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Potencio on 12/18/2016. @ 1:39 PM
 * For PopularMovies
 */

public class PopularMoviesHelper extends SQLiteOpenHelper {

    private static final String TAG = PopularMoviesHelper.class.getSimpleName();

    //Database Name and Version
    private static final String DATABASE_NAME = "popular_movies.db";
    private static final int DATABASE_VERSION = 12;

    public PopularMoviesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POPULAR_MOVIE_TABLE = "CREATE TABLE " +
                PopularMoviesContract.MoviesEntry.TABLE_MOVIES +
                "(" +
                PopularMoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.MoviesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                PopularMoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                PopularMoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                PopularMoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                PopularMoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                PopularMoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                PopularMoviesContract.MoviesEntry.COLUMN_POPULARITY + " REAL NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_MOVIE_TABLE);

    }

    //delete and recreate the database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.MoviesEntry.TABLE_MOVIES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                PopularMoviesContract.MoviesEntry.TABLE_MOVIES + "'");

        // re-create database
        onCreate(sqLiteDatabase);

    }
}
