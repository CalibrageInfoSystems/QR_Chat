package com.cis.qrchat.Views.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Adapters.SubReportsAdapter;
import com.cis.qrchat.Model.Feed_Data;
import com.cis.qrchat.Model.Profile_Data;
import com.cis.qrchat.R;

import java.util.List;

public class FeedRecycleAdapter extends RecyclerView.Adapter<FeedRecycleAdapter.MyViewHolder>{

    private Context context;
    private List<Feed_Data> feedList;
    private List<Feed_Data> feeddataList;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public FeedRecycleAdapter(Context context, List<Feed_Data> feedList) {
        this.context = context;
        this.feedList = feedList;
        this.feeddataList = feedList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedrecyclerviewlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Feed_Data feedData = feeddataList.get(position);

        holder.mainImg.setImageResource(feedData.getMainImg());


        holder.name.setText(feedData.getAccName());
        holder.time.setText(feedData.getTimer());
        holder.desc.setText(feedData.getDesc());
        holder.likecount.setText(feedData.getLikecount());
        holder.commentcount.setText(feedData.getCommentcount());

        holder.likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.likeImg.setImageResource(R.drawable.likedimg);
            }
        });
        if(position == 0)
        {
            holder.linear.setVisibility(View.VISIBLE);
            int[] feedcovers = new int[]{
                    R.drawable.tajmahal,

            };
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.feedImg.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            SubReportsAdapter subItemAdapter = new SubReportsAdapter(feedcovers,context,position );

            holder.feedImg.setLayoutManager(layoutManager);
            holder.feedImg.setAdapter(subItemAdapter);
            holder.feedImg.setRecycledViewPool(viewPool);
        }
        if(position == 1)
        {
            holder.linear.setVisibility(View.GONE);
            int[] feedcovers = new int[]{
                    R.drawable.tajmahal,
                    R.drawable.pisatower,


            };
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.feedImg.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            SubReportsAdapter subItemAdapter = new SubReportsAdapter(feedcovers,context,position );

            holder.feedImg.setLayoutManager(layoutManager);
            holder.feedImg.setAdapter(subItemAdapter);
            holder.feedImg.setRecycledViewPool(viewPool);
        }
        if(position == 2)
        {
            holder.linear.setVisibility(View.GONE);
            int[] feedcovers = new int[]{
                    R.drawable.tajmahal,
                    R.drawable.pisatower,
                    R.drawable.statue,
            };
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.feedImg.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            SubReportsAdapter subItemAdapter = new SubReportsAdapter(feedcovers,context,position );

            holder.feedImg.setLayoutManager(layoutManager);
            holder.feedImg.setAdapter(subItemAdapter);
            holder.feedImg.setRecycledViewPool(viewPool);
        }
        if(position==3){
            holder.linear.setVisibility(View.GONE);
                holder.feedImg.setVisibility(View.GONE);

                holder.webvideo_layout2.setVisibility(View.VISIBLE);
                MediaController mediaController= new MediaController(context);
                mediaController.setAnchorView(holder.videoView);
                //Location of Media File
                Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.video);
                //Starting VideView By Setting MediaController and URI
                holder.videoView.setMediaController(mediaController);
                holder.videoView.setVideoURI(uri);
                holder.videoView.requestFocus();
                holder.videoView.start();
            }
 


        //holder.feedImg.setImageResource(feedData.getFeedImg());
    }

    @Override
    public int getItemCount() {
        return feeddataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mainImg, likeImg, commentImg;
        TextView name,time, desc, likecount, commentcount;
RecyclerView feedImg;
        RelativeLayout webvideo_layout2;
        VideoView videoView ;
        LinearLayout linear;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mainImg = itemView.findViewById(R.id.accountImg);
            feedImg = itemView.findViewById(R.id.feedImg);
            likeImg = itemView.findViewById(R.id.likeImg);
            commentImg = itemView.findViewById(R.id.commentImg);
            webvideo_layout2 =(RelativeLayout) itemView.findViewById(R.id.webvideo_layout2);
            videoView =(VideoView)itemView.findViewById(R.id.vdVw);
            name = itemView.findViewById(R.id.aname);
            time = itemView.findViewById(R.id.timeer);
            desc = itemView.findViewById(R.id.desc);
            likecount = itemView.findViewById(R.id.likeCount);
            commentcount = itemView.findViewById(R.id.commentCount);
            linear = itemView.findViewById(R.id.linear);
        }
    }
}
