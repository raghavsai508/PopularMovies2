package com.example.android.popularmovies2.Network;

import android.os.AsyncTask;

import com.example.android.popularmovies2.Utils.NetworkUtility;

import java.net.URL;

public class MoviesAsyncTask extends AsyncTask <URL,Void,String> {

    private MoviesListener moviesListener;
    private String endPoint;


    // Call backs for Movies Async Task
    public interface MoviesListener {
        void onPreExecute();
        void onPostExecute(String jsonString, String endPointFor);
    }

    public MoviesAsyncTask(MoviesListener moviesListener, String endPoint) {
        this.moviesListener = moviesListener;
        this.endPoint = endPoint;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        moviesListener.onPreExecute();
    }

    @Override
    protected String doInBackground(URL... urls) {
        if (urls == null) {
            return null;
        }

        URL url = urls[0];
        try {
            return NetworkUtility.getResponseFromHttpUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        moviesListener.onPostExecute(data, endPoint);
    }

}
