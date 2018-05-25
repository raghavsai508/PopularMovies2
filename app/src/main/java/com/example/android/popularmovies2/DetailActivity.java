package com.example.android.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies2.Models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

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

    private static final String INTENT_KEY = "movie_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intentCalled = getIntent();
        if (intentCalled != null) {
            if (intentCalled.hasExtra(INTENT_KEY)) {
                Movie movie = intentCalled.getParcelableExtra(INTENT_KEY);
                Log.d("hello","hhee");

                String imageURL = "http://image.tmdb.org/t/p/w185"+movie.getPosterPath();
                Picasso.with(this).load(imageURL).into(mImageViewPoster);

                mTextViewTitle.setText(movie.getTitle());
                mTextViewReleaseDate.setText(movie.getReleaseDate());
                mTextViewRating.setText(movie.getVoteAverage()+"/10");
                mTextViewOverview.setText(movie.getOverview());
            }
        }
    }
}
