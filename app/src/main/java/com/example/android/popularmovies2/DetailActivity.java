package com.example.android.popularmovies2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies2.Adapters.ReviewsAdapter;
import com.example.android.popularmovies2.Adapters.TrailersAdapter;
import com.example.android.popularmovies2.Interfaces.ReviewItemClickListener;
import com.example.android.popularmovies2.Interfaces.TrailerItemClickListener;
import com.example.android.popularmovies2.Models.Movie;
import com.example.android.popularmovies2.Models.Review;
import com.example.android.popularmovies2.Models.Video;
import com.example.android.popularmovies2.Network.MoviesAsyncTask;
import com.example.android.popularmovies2.Utils.JsonUtils;
import com.example.android.popularmovies2.Utils.NetworkUtility;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements MoviesAsyncTask.MoviesListener{

    private static final String INTENT_KEY = "movie_detail";

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";

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

    private RecyclerView.Adapter mRecycleReviewAdapter;
    private RecyclerView.Adapter mRecycleTrailerAdapter;
    private MoviesAsyncTask moviesAsyncTask;
    private List<Review> reviewsList;
    private List<Video> trailersList;

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
                Movie movie = intentCalled.getParcelableExtra(INTENT_KEY);
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
        moviesAsyncTask =  new MoviesAsyncTask(this, endPoint);
        URL trailersUrl = NetworkUtility.buildTrailersUrl(movieID, endPoint);
        moviesAsyncTask.execute(trailersUrl);
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
        moviesAsyncTask =  new MoviesAsyncTask(this, endPoint);
        URL reviewsUrl = NetworkUtility.buildReviewsUrl(movieID, endPoint);
        moviesAsyncTask.execute(reviewsUrl);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(String jsonString, String endPointFor) {
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
}
