package com.cis.qrchat.Views.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Model.FeedImages;
import com.cis.qrchat.Model.Feed_Data;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.FeedRecycleAdapter;

import java.util.List;


public class FeedImagesAdapter extends RecyclerView.Adapter<FeedImagesAdapter.MyViewHolder>{

    private Context context;
    private List<FeedImages> feedImages;
    private List<FeedImages> feedImagesdataList;

    public FeedImagesAdapter(Context context, List<FeedImages> feedImagesList) {
        this.context = context;
        this.feedImages = feedImagesList;
        this.feedImagesdataList = feedImagesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedimagesrecyclerviewlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final FeedImages feedImagesData = feedImagesdataList.get(position);

        holder.feedsImg.setImageResource(feedImagesData.getFeedImg());
    }

    @Override
    public int getItemCount() {
        return feedImagesdataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView feedsImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            feedsImg = itemView.findViewById(R.id.feedsImg);
        }
    }
}
