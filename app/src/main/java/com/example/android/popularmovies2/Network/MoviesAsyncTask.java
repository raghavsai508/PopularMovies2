package com.example.android.popularmovies2.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies2.Models.Movie;
import com.example.android.popularmovies2.Utils.JsonUtils;
import com.example.android.popularmovies2.Utils.NetworkUtility;

import java.net.URL;
import java.util.List;

public class MoviesAsyncTask extends AsyncTask<String,Void,List<Movie>> {

    private MoviesListener moviesListener;


    // Call backs for Movies Async Task
    public interface MoviesListener {
        void onPreExecute();
        void onPostExecute(List<Movie> movies);
    }

    public MoviesAsyncTask(MoviesListener moviesListener) {
        this.moviesListener = moviesListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        moviesListener.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        if (params == null) {
            return null;
        }

        String sortType = params[0];
        URL moviesURL = NetworkUtility.buildUrl(sortType);

        try {
            String movieSearchResults = NetworkUtility.getResponseFromHttpUrl(moviesURL);
            Log.v("Results:",movieSearchResults);
            return JsonUtils.getMoviesFromJSONString(movieSearchResults);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        moviesListener.onPostExecute(movies);
    }
}
