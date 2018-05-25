package com.example.android.popularmovies2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies2.Interfaces.MovieItemClickListener;
import com.example.android.popularmovies2.Models.Movie;
import com.example.android.popularmovies2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyleAdapter extends RecyclerView.Adapter<RecyleAdapter.ViewHolder> {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    private List<Movie> movies;
    private Context mContext;
    private MovieItemClickListener movieItemClickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        public ImageView movieImageView;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public RecyleAdapter(List<Movie> movies, Context context, MovieItemClickListener movieItemClickListener) {
        this.movies = movies;
        this.mContext = context;
        this.movieItemClickListener = movieItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.movie_list_item,parent, false);
        final ViewHolder viewHolder = new ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieItemClickListener.onItemClick(movies.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String imageURL = IMAGE_BASE_URL+movie.getPosterPath();
        Picasso.with(mContext).load(imageURL).into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


}
