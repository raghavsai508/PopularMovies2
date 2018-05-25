package com.example.android.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmovies2.Adapters.RecyleAdapter;
import com.example.android.popularmovies2.Interfaces.MovieItemClickListener;
import com.example.android.popularmovies2.Models.Movie;
import com.example.android.popularmovies2.Network.MoviesAsyncTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAsyncTask.MoviesListener {

    /* Bind Views: ButterKnife */
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> moviesList;
    private MoviesAsyncTask moviesAsyncTask;


    private static int GRID_COLUMNS = 2;
    private static final String SORT_TYPE_POPULAR = "popular";
    private static final String INTENT_KEY = "movie_detail";
    private static final String SORT_TYPE_TOP_RATED = "top_rated";
    private static final String SORT_TYPE_DEFAULT = "Select Sort Type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this,GRID_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);

        loadMovies();

        moviesAsyncTask =  new MoviesAsyncTask(this);

        // By default get results for type: popular
        moviesAsyncTask.execute(SORT_TYPE_POPULAR);

    }


    private void loadMovies() {
        if (moviesList == null) {
            return;
        }

        mAdapter = new RecyleAdapter(moviesList, this, new MovieItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Log.d("Movie Item", "clicked position:" + movie.toString());
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(INTENT_KEY,movie);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

    }


    /* MoviesListener call back methods */
    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(List<Movie> movies) {
        moviesList = movies;
        loadMovies();
    }
}
