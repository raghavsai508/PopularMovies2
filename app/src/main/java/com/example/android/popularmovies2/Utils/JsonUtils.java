package com.example.android.popularmovies2.Utils;

import com.example.android.popularmovies2.Models.Movie;
import com.example.android.popularmovies2.Models.Review;
import com.example.android.popularmovies2.Models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    public static List<Movie> getMoviesFromJSONString(String movieJSONString) throws JSONException {

        final String MOVIES_RESULTS = "results";

        final String MOVIE_VOTE_COUNT = "vote_count";
        final String MOVIE_ID = "id";
        final String MOVIE_VIDEO = "video";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_TITLE = "title";
        final String MOVIE_POPULARITY = "popularity";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_ORIGINAL_LANGUAGE = "original_language";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_GENRE_IDS = "genre_ids";
        final String MOVIE_BACKDROP_PATH = "backdrop_path";
        final String MOVIE_ADULT = "adult";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";

        /* Movie array to hold each movies data */
        ArrayList<Movie> parsedMoviesArray;

        JSONObject moviesJSON = new JSONObject(movieJSONString);
        JSONArray resultsArray = moviesJSON.getJSONArray(MOVIES_RESULTS);

        parsedMoviesArray = new ArrayList<>();

        for (int i = 0; i<resultsArray.length();i++) {
            Integer voteCount, id, voteAverage;
            Boolean video, adult;
            String title, posterPath, originalLanguage, originalTitle, backdropPath, overview, releaseDate;
            Double popularity;
            int[] genreIDs;

            JSONObject movieJSONObject = resultsArray.getJSONObject(i);
            voteCount = movieJSONObject.getInt(MOVIE_VOTE_COUNT);
            id = movieJSONObject.getInt(MOVIE_ID);
            voteAverage = movieJSONObject.getInt(MOVIE_VOTE_AVERAGE);

            video = movieJSONObject.getBoolean(MOVIE_VIDEO);
            adult = movieJSONObject.getBoolean(MOVIE_ADULT);

            title = movieJSONObject.getString(MOVIE_TITLE);
            posterPath = movieJSONObject.getString(MOVIE_POSTER_PATH);
            originalLanguage = movieJSONObject.getString(MOVIE_ORIGINAL_LANGUAGE);
            originalTitle = movieJSONObject.getString(MOVIE_ORIGINAL_TITLE);
            backdropPath = movieJSONObject.getString(MOVIE_BACKDROP_PATH);
            overview = movieJSONObject.getString(MOVIE_OVERVIEW);
            releaseDate = movieJSONObject.getString(MOVIE_RELEASE_DATE);

            popularity = movieJSONObject.getDouble(MOVIE_POPULARITY);

            JSONArray genreJSONArray = movieJSONObject.getJSONArray(MOVIE_GENRE_IDS);
            genreIDs = new int[genreJSONArray.length()];
            for (int j = 0;j<genreJSONArray.length();j++) {
                genreIDs[j] = genreJSONArray.optInt(j);
            }

            Movie movie =  new Movie(voteCount, id, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, genreIDs, backdropPath, adult, overview, releaseDate);

            parsedMoviesArray.add(movie);
        }

        return  parsedMoviesArray;
    }


    // Parse reviews JSON to return array of Reviews
    public static List<Review> getReviewsFromJSONString(String reviewJSONString) throws JSONException {
        final String REVIEW_RESULTS = "results";

        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_ID = "id";
        final String REVIEW_URL = "url";

        /* Movie array to hold each movies data */
        ArrayList<Review> parsedReviewsArray;

        JSONObject reviewsJSON = new JSONObject(reviewJSONString);
        JSONArray resultsArray = reviewsJSON.getJSONArray(REVIEW_RESULTS);

        parsedReviewsArray = new ArrayList<>();

        for (int i = 0; i<resultsArray.length();i++) {
            String author, content, id, url;
            JSONObject reviewJSONObject = resultsArray.getJSONObject(i);

            author = reviewJSONObject.getString(REVIEW_AUTHOR);
            content = reviewJSONObject.getString(REVIEW_CONTENT);
            id = reviewJSONObject.getString(REVIEW_ID);
            url = reviewJSONObject.getString(REVIEW_URL);

            Review review = new Review(author, content, id, url);
            parsedReviewsArray.add(review);
        }

        return parsedReviewsArray;
    }


    public static List<Video> getVideosFromJSONString(String videoJSONString) throws JSONException {
        final String VIDEO_RESULTS = "results";

        final String VIDEO_ID = "id";
        final String VIDEO_ISO_639_1 = "iso_639_1";
        final String VIDEO_ISO_3166_1 = "iso_3166_1";
        final String VIDEO_KEY = "key";
        final String VIDEO_NAME = "name";
        final String VIDEO_SITE = "site";
        final String VIDEO_SIZE = "size";
        final String VIDEO_TYPE = "type";

        /* Movie array to hold each movies data */
        ArrayList<Video> parsedVideosArray;

        JSONObject videosJSON = new JSONObject(videoJSONString);
        JSONArray resultsArray = videosJSON.getJSONArray(VIDEO_RESULTS);

        parsedVideosArray = new ArrayList<>();

        for (int i = 0; i<resultsArray.length();i++) {
            String id, iso_369_1, iso_3166_1, key, name, site, type;
            int size;
            JSONObject videoJSONObject = resultsArray.getJSONObject(i);

            id = videoJSONObject.getString(VIDEO_ID);
            iso_369_1 = videoJSONObject.getString(VIDEO_ISO_639_1);
            iso_3166_1 = videoJSONObject.getString(VIDEO_ISO_3166_1);
            key = videoJSONObject.getString(VIDEO_KEY);
            name = videoJSONObject.getString(VIDEO_NAME);
            site = videoJSONObject.getString(VIDEO_SITE);
            size = videoJSONObject.getInt(VIDEO_SIZE);
            type = videoJSONObject.getString(VIDEO_TYPE);

            Video video = new Video(id, iso_369_1, iso_3166_1, key, name, site, size, type);
            parsedVideosArray.add(video);
        }

        return parsedVideosArray;
    }

}
