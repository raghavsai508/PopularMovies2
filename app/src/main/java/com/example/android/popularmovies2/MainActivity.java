package com.example.android.popularmovies2;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.android.popularmovies2.Adapters.RecyleAdapter;
import com.example.android.popularmovies2.Interfaces.MovieItemClickListener;
import com.example.android.popularmovies2.Models.Movie;
import com.example.android.popularmovies2.Network.MoviesLoaderCallbacks;
import com.example.android.popularmovies2.Utils.JsonUtils;
import com.example.android.popularmovies2.Utils.NetworkUtility;
import com.example.android.popularmovies2.data.PopularMoviesContract;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MoviesLoaderCallbacks.MoviesLoaderListener {

    //region Binder Variables
    /* Bind Views: ButterKnife */
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.spinner)
    Spinner mSpinner;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    //endregion

    //region Private Variables
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> moviesList;
    private Uri mUri;
    //endregion

    //region Private Static Variables
    private static int GRID_COLUMNS = 2;
    private static final int ID_MAIN_ACTIVITY_LOADER = 123;
    private static final int ID_MAIN_ACTIVITY_DATALOADER = 111;
    public static final int INDEX_MOVIE_ID = 0;

    private static final String SORT_TYPE_POPULAR = "popular";
    private static final String INTENT_KEY = "movie_detail";
    private static final String SORT_TYPE_TOP_RATED = "top_rated";
    private static final String SORT_TYPE_FAVORITES = "favorites";
    private static final String SORT_TYPE_DEFAULT = "Select Sort Type";

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_ID,
            PopularMoviesContract.PopularMoviesEntry.COLUMN_MOVIE_TITLE
    };
    //endregion

    //region Activity Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLayoutManager = new GridLayoutManager(this,GRID_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        mUri = PopularMoviesContract.PopularMoviesEntry.CONTENT_URI;
        getMovies();
        setupSpinner();
    }
    //endregion

    //region UI Setup methods
    private void setupSpinner() {
        final Map<String, String> map = new HashMap<>();
        map.put("Top Rated", SORT_TYPE_TOP_RATED);
        map.put("Most Popular", SORT_TYPE_POPULAR);
        map.put("My Favorites", SORT_TYPE_FAVORITES);

        Set<String> spinnerKeys = map.keySet();
        Log.v("spinner keys",spinnerKeys.toString());
        String[] spinnerArray = spinnerKeys.toArray(new String[spinnerKeys.size()+1]);
        int index = 0;
        spinnerArray[index++] = SORT_TYPE_DEFAULT;
        for(String spinnerKey: spinnerKeys) {
            spinnerArray[index++] = spinnerKey;
        }

        mSpinner = findViewById(R.id.spinner);

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                String item = parent.getItemAtPosition(position).toString();
                String mappedSpinner = map.get(item);
                Log.v("Spinner:",mappedSpinner);
                if (mappedSpinner.equals(SORT_TYPE_FAVORITES)) {
                    getSupportLoaderManager().initLoader(ID_MAIN_ACTIVITY_LOADER, null, MainActivity.this).forceLoad();
                } else {
                    URL moviesURL = NetworkUtility.buildMoviesUrl(mappedSpinner);
                    mSpinner.setVisibility(View.GONE);
                    MoviesLoaderCallbacks moviesLoaderCallbacks = new MoviesLoaderCallbacks(MainActivity.this, moviesURL, mappedSpinner, MainActivity.this);
                    getSupportLoaderManager().restartLoader(ID_MAIN_ACTIVITY_DATALOADER, null, moviesLoaderCallbacks);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinner.setVisibility(View.GONE);
            }
        });
    }

    private void showSpinner() {
        mSpinner.setVisibility(View.VISIBLE);
        mSpinner.performClick();
    }
    //endregion

    //region Movies
    private void getMovies() {
        String endPoint = getString(R.string.popular_end_point_path);
        URL moviesURL = NetworkUtility.buildMoviesUrl(endPoint);
        // By default get results for type: popular
        MoviesLoaderCallbacks moviesLoaderCallbacks = new MoviesLoaderCallbacks(this, moviesURL, endPoint, this);
        getSupportLoaderManager().initLoader(ID_MAIN_ACTIVITY_DATALOADER, null, moviesLoaderCallbacks);
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
    //endregion

    //region MoviesLoaderCallbacks.MoviesLoaderListener call back methods
    @Override
    public void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(String jsonString, String endPointFor) {
        getSupportLoaderManager().destroyLoader(ID_MAIN_ACTIVITY_DATALOADER);
        mProgressBar.setVisibility(View.INVISIBLE);
        try {
          moviesList = JsonUtils.getMoviesFromJSONString(jsonString);
          loadMovies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Cursor Loaders methods
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case ID_MAIN_ACTIVITY_LOADER:
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

        if (data.getCount() == 0) {
            return;
        }

        ArrayList<Movie> filteredMoviesArray = new ArrayList<>();

        try {
            while (data.moveToNext()) {
                final String movieId = data.getString(INDEX_MOVIE_ID);
                for (Movie movie: moviesList) {
                    if (movieId.equals(Integer.toString(movie.getId()))) {
                        filteredMoviesArray.add(movie);
                    }
                }
            }

            if (filteredMoviesArray.size() > 0) {
                moviesList = filteredMoviesArray;
                loadMovies();
            }

        } finally {
            data.close();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    //endregion

    //region Menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            showSpinner();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}
