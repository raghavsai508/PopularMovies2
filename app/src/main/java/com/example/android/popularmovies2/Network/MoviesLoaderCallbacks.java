package com.example.android.popularmovies2.Network;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.net.URL;

public class MoviesLoaderCallbacks implements LoaderManager.LoaderCallbacks<String> {

    private Context context;
    private URL url;
    private String endPoint;
    private MoviesLoaderListener moviesLoaderListener;

    public interface MoviesLoaderListener {
        void onPreExecute();
        void onPostExecute(String jsonString, String endPointFor);
    }

    public MoviesLoaderCallbacks(Context context, URL url, String endPoint, MoviesLoaderListener moviesLoaderListener) {
        this.context = context;
        this.url = url;
        this.endPoint = endPoint;
        this.moviesLoaderListener = moviesLoaderListener;
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        moviesLoaderListener.onPreExecute();
        return new MoviesAsyncTaskLoader(context, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        moviesLoaderListener.onPostExecute(data, endPoint);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
