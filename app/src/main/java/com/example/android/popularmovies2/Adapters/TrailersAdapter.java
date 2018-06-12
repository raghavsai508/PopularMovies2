package com.example.android.popularmovies2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies2.Interfaces.TrailerItemClickListener;
import com.example.android.popularmovies2.Models.Video;
import com.example.android.popularmovies2.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private Context mContext;
    private List<Video> videosList;
    private TrailerItemClickListener trailerItemClickListener;

    public TrailersAdapter(Context context, List<Video> videosList, TrailerItemClickListener trailerItemClickListener) {
        this.mContext = context;
        this.trailerItemClickListener = trailerItemClickListener;
        this.videosList = videosList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_trailer)
        public TextView textViewTrailer;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.trailer_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trailerItemClickListener.onItemClick(videosList.get(viewHolder.getAdapterPosition()));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String stringTrailer = "Trailer "+(position+1);
        holder.textViewTrailer.setText(stringTrailer);
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }




}
