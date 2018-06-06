package com.example.android.popularmovies2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PopularMoviesProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES, FAVORITES);
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES + "/#", FAVORITES_WITH_ID);

        return uriMatcher;
    }

    private PopularMoviesDbHelper mPopularMoviesDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mPopularMoviesDbHelper = new PopularMoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITES_WITH_ID: {
                String movieID = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieID};
                cursor = mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_NAME,
                        projection,
                        PopularMoviesContract.PopularMoviesEntry.COLUMN_WEATHER_ID + " = ?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                        );

                break;
            }
            case FAVORITES: {
                cursor = mPopularMoviesDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.PopularMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
         throw new RuntimeException("We are not implementing getType in popular movies");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAVORITES_WITH_ID:
                long id = db.insert(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mPopularMoviesDbHelper.getWritableDatabase();
        int deletedRows;

        if (selection == null) {
            selection = "1";
        }

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deletedRows = db.delete(PopularMoviesContract.PopularMoviesEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in popular movies");
    }

    @Override
    public void shutdown() {
        mPopularMoviesDbHelper.close();
        super.shutdown();
    }
}
