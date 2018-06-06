package com.example.android.popularmovies2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies2.Interfaces.ReviewItemClickListener;
import com.example.android.popularmovies2.Models.Review;
import com.example.android.popularmovies2.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context mContext;
    private List<Review> reviewsList;
    private ReviewItemClickListener reviewItemClickListener;

    public ReviewsAdapter(Context context, List<Review> reviewsList, ReviewItemClickListener reviewItemClickListener) {
        this.mContext = context;
        this.reviewItemClickListener = reviewItemClickListener;
        this.reviewsList = reviewsList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author)
        public TextView textViewAuthor;

        @BindView(R.id.tv_content)
        public TextView textViewContent;

        @BindView(R.id.tv_url)
        public TextView textViewURL;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.review_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewItemClickListener.onItemClick(reviewsList.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
       Review review = reviewsList.get(position);

       holder.textViewAuthor.setText(review.getAuthor());
       holder.textViewContent.setText(review.getContent());
       holder.textViewURL.setText(review.getUrl());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
