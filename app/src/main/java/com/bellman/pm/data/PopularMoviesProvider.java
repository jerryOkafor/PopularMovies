package com.bellman.pm.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PopularMoviesProvider extends ContentProvider {
    private static final String TAG = PopularMoviesProvider.class.getSimpleName();
    private PopularMoviesHelper mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        //build the uri matcher using the no match code
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //add code for each of the uri
        uriMatcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MoviesEntry.TABLE_MOVIES, MOVIES);
        uriMatcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.MoviesEntry.TABLE_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    //constants for the uri matcher
    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 102;


    public PopularMoviesProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case MOVIES:
                numDeleted = db.delete(
                        PopularMoviesContract.MoviesEntry.TABLE_MOVIES, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        PopularMoviesContract.MoviesEntry.TABLE_MOVIES + "'");
                break;
            case MOVIE_WITH_ID:
                numDeleted = db.delete(PopularMoviesContract.MoviesEntry.TABLE_MOVIES,
                        PopularMoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        PopularMoviesContract.MoviesEntry.TABLE_MOVIES + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES: {
                return PopularMoviesContract.MoviesEntry.CONTENT_DIR_TYPE;
            }
            case MOVIE_WITH_ID: {
                return PopularMoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;

            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri retUri;
        switch (match) {
            case MOVIES: {

                long _id = db.insert(PopularMoviesContract.MoviesEntry.TABLE_MOVIES, null, values);
                if (_id > 0) {
                    retUri = PopularMoviesContract.MoviesEntry.buildMoviesUri(_id);

                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.MoviesEntry.TABLE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case MOVIE_WITH_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.MoviesEntry.TABLE_MOVIES,
                        projection,
                        PopularMoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES: {
                db.beginTransaction();
                int retCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(PopularMoviesContract.MoviesEntry.TABLE_MOVIES, null, value);
                        if (_id != -1) {
                            retCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();

                }
                getContext().getContentResolver().notifyChange(uri, null);
                return retCount;

            }
            default: {
                return super.bulkInsert(uri, values);

            }
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;
        if (values == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                numUpdated = db.update(PopularMoviesContract.MoviesEntry.TABLE_MOVIES,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_ID: {
                numUpdated = db.update(PopularMoviesContract.MoviesEntry.TABLE_MOVIES,
                        values,
                        PopularMoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
