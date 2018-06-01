package com.example.android.popularmovies2.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtility {

    private static final String TAG = NetworkUtility.class.getSimpleName();

    private static final String MOVIE_SCHEME = "https";
    private static final String MOVIE_BASE_URL = "api.themoviedb.org";
    private static final String MOVIE_APPENDED_PATH = "3/movie";


    private static final String MOVIE_API_KEY = "";
    private static final String MOVIE_QUERY_API_PARAM = "api_key";


    public static URL buildMoviesUrl(String sortType) {

        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme(MOVIE_SCHEME)
                .authority(MOVIE_BASE_URL)
                .appendEncodedPath(MOVIE_APPENDED_PATH)
                .appendPath(sortType)
                .appendQueryParameter(MOVIE_QUERY_API_PARAM,MOVIE_API_KEY)
                .build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;

    }

    public static URL buildTrailersUrl(Integer movieId, String trailersEndPoint) {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme(MOVIE_SCHEME)
                .authority(MOVIE_BASE_URL)
                .appendEncodedPath(MOVIE_APPENDED_PATH)
                .appendPath(movieId.toString())
                .appendPath(trailersEndPoint)
                .appendQueryParameter(MOVIE_QUERY_API_PARAM,MOVIE_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildReviewsUrl(Integer movieId, String reviewsEndPoint) {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme(MOVIE_SCHEME)
                .authority(MOVIE_BASE_URL)
                .appendEncodedPath(MOVIE_APPENDED_PATH)
                .appendPath(movieId.toString())
                .appendPath(reviewsEndPoint)
                .appendQueryParameter(MOVIE_QUERY_API_PARAM,MOVIE_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.d(TAG, "Built URI " + url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Log.v("input stream", in.toString());

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



}
