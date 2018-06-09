package com.example.android.popularmovies2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PopularMoviesContract {

    public static final String AUTHORITY = "com.example.android.popularmovies2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class PopularMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                                                .buildUpon()
                                                .appendPath(PATH_MOVIES)
                                                .build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";

    }
}
