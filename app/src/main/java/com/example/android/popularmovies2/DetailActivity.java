package com.example.android.popularmovies2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies2.Adapters.ReviewsAdapter;
import com.example.android.popularmovies2.Adapters.TrailersAdapter;
import com.example.android.popularmovies2.Interfaces.ReviewItemClickListener;
import com.example.android.popularmovies2.Interfaces.TrailerItemClickListener;
import com.example.android.popularmovies2.Models.Movie;
import com.example.android.popularmovies2.Models.Review;
import com.example.android.popularmovies2.Models.Video;
import com.example.android.popularmovies2.Network.MoviesLoaderCallbacks;
import com.example.android.popularmovies2.Utils.JsonUtils;
import com.example.android.popularmovies2.Utils.NetworkUtility;
import com.example.android.popularmovies2.data.PopularMoviesContract;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements MoviesLoaderCallbacks.MoviesLoaderListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String INTENT_KEY = "movie_detail";
    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final int ID_DETAIL_LOADER = 353;

    private static final int ID_DETAIL_REVIEWS_LOADER = 351;
    private static final int ID_DETAIL_TRAILERS_LOADER = 352;


    public static final String[] MOVIE_DETAIL_PROJECTION = {
            PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE
    };


    @BindView(R.id.iv_poster)
    ImageView mImageViewPoster;

    @BindView(R.id.tv_overview)
    TextView mTextViewOverview;

    @BindView(R.id.tv_release_date)
    TextView mTextViewReleaseDate;

    @BindView(R.id.tv_user_rating)
    TextView mTextViewRating;

    @BindView(R.id.tv_title)
    TextView mTextViewTitle;

    @BindView(R.id.recyclerViewTrailers)
    RecyclerView mRecyclerViewTrailers;

    @BindView(R.id.recyclerViewReviews)
    RecyclerView mRecyclerViewReviews;

    @BindView(R.id.favorite)
    ImageView mFavorite;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private RecyclerView.Adapter mRecycleReviewAdapter;
    private RecyclerView.Adapter mRecycleTrailerAdapter;
    private Movie movie;
    private Uri mUri;
    private List<Review> reviewsList;
    private List<Video> trailersList;
    private Boolean isFavoriteClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        loadMovieDetails();
    }

    private void loadMovieDetails() {
        Intent intentCalled = getIntent();
        if (intentCalled != null) {
            if (intentCalled.hasExtra(INTENT_KEY)) {
                movie = intentCalled.getParcelableExtra(INTENT_KEY);
                Log.d("hello","hhee");

                String imageURL = "http://image.tmdb.org/t/p/w185"+movie.getPosterPath();
                Picasso.with(this).load(imageURL).into(mImageViewPoster);

                mTextViewTitle.setText(movie.getTitle());
                mTextViewReleaseDate.setText(movie.getReleaseDate());
                mTextViewRating.setText(getString(R.string.vote_average, movie.getVoteAverage()));
                mTextViewOverview.setText(movie.getOverview());

                setupRecyclerViews();
                getTrailersFor(movie.getId());
                getReviewsFor(movie.getId());

                mUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI;
                mUri = mUri.buildUpon().appendPath(Integer.toString(movie.getId())).build();

                getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
            }
        }
    }

    private void setupRecyclerViews() {
        RecyclerView.LayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        mRecyclerViewReviews.setLayoutManager(layoutManagerReviews);

        RecyclerView.LayoutManager layoutManagerTrailers = new LinearLayoutManager(this);
        mRecyclerViewTrailers.setLayoutManager(layoutManagerTrailers);
    }


    private void getTrailersFor(Integer movieID) {
        String endPoint = getString(R.string.trailers_end_point_path);
        URL trailersUrl = NetworkUtility.buildTrailersUrl(movieID, endPoint);
        MoviesLoaderCallbacks moviesLoaderCallbacks = new MoviesLoaderCallbacks(this, trailersUrl, endPoint, this);
        getSupportLoaderManager().initLoader(ID_DETAIL_TRAILERS_LOADER, null, moviesLoaderCallbacks);
    }


    private void loadTrailers() {
        if (trailersList == null) {
            return;
        }

        mRecycleTrailerAdapter = new TrailersAdapter(this, trailersList, new TrailerItemClickListener() {
            @Override
            public void onItemClick(Video video) {
                Log.d("Video Clicked", video.getName());
                String videoUrl = VIDEO_URL + video.getKey();
                Intent openIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(openIntent);
            }
        });
        mRecyclerViewTrailers.setAdapter(mRecycleTrailerAdapter);

    }

    private void loadReviews() {
        if (trailersList == null) {
            return;
        }

        mRecycleReviewAdapter = new ReviewsAdapter(this, reviewsList, new ReviewItemClickListener() {
            @Override
            public void onItemClick(Review review) {
                Log.d("Video Clicked", review.getAuthor());
            }
        });

        mRecyclerViewReviews.setAdapter(mRecycleReviewAdapter);
    }


    private void getReviewsFor(Integer movieID) {
        String endPoint = getString(R.string.reviews_end_point_path);
        URL reviewsUrl = NetworkUtility.buildReviewsUrl(movieID, endPoint);
        MoviesLoaderCallbacks moviesLoaderCallbacks = new MoviesLoaderCallbacks(this, reviewsUrl, endPoint, this);
        getSupportLoaderManager().initLoader(ID_DETAIL_REVIEWS_LOADER, null, moviesLoaderCallbacks);
    }


    public void favoriteClick(View view) {

        if (movie == null) {
            return;
        }

        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());



        if (!isFavoriteClicked) {
            Uri insertedUri = contentResolver.insert(mUri, contentValues);
            if(insertedUri != null) {
                Toast.makeText(getBaseContext(), insertedUri.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            int deletedID = contentResolver.delete(mUri, null, null);
            if(deletedID != 0) {
                Toast.makeText(getBaseContext(), Integer.toString(deletedID), Toast.LENGTH_LONG).show();
            }
        }

        isFavoriteClicked = !isFavoriteClicked;
        changeFavoriteImage();

    }

    private void changeFavoriteImage() {
        int favoriteResource;

        if (isFavoriteClicked) {
            favoriteResource = android.R.drawable.btn_star_big_on;
        } else {
            favoriteResource = android.R.drawable.btn_star_big_off;
        }

        mFavorite.setImageResource(favoriteResource);
    }


    // MoviesLoaderCallbacks.MoviesLoaderListener Methods
    @Override
    public void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(String jsonString, String endPointFor) {
        mProgressBar.setVisibility(View.INVISIBLE);
        try {
            if (endPointFor.equals(getString(R.string.reviews_end_point_path))) {
                reviewsList = JsonUtils.getReviewsFromJSONString(jsonString);
                Log.d("Detail Activity:",reviewsList.toString());
                Log.d("Detail Activity:", endPointFor);
                loadReviews();
            } else if (endPointFor.equals(getString(R.string.trailers_end_point_path))) {
                trailersList = JsonUtils.getVideosFromJSONString(jsonString);
                Log.d("Detail Activity:",trailersList.toString());
                Log.d("Detail Activity:", endPointFor);
                loadTrailers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null ,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        // If it is moved to first then it is favorite
        isFavoriteClicked = true;
        changeFavoriteImage();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
