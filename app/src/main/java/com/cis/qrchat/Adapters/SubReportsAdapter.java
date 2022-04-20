package com.cis.qrchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Model.Feed_Data;
import com.cis.qrchat.R;

import java.util.ArrayList;
import java.util.List;



public class SubReportsAdapter extends RecyclerView.Adapter<SubReportsAdapter.ViewHolder> {


    public Context mContext;
    private List<Feed_Data> feeddataList;
    int Position;
    LinearLayout linear;
    int[] feedcovers = new int[]{
            R.drawable.tajmahal,
            R.drawable.pisatower,
            R.drawable.statue,
    };
    int[] feedcover = new int[]{

            R.drawable.pisatower,
            R.drawable.statue,
    };
 public SubReportsAdapter( int[] feedcovers, Context ctx,int Position) {
        this.feedcovers = feedcovers;

        this.mContext = ctx;
        this.Position = Position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.uploading_data, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int[] feedcovers = new int[]{
                R.drawable.tajmahal,
                R.drawable.pisatower,
                R.drawable.statue,
        };



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, PlayerActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                // intent.putExtra("videoid", videoId);
//                intent.putExtra("name", "Crop Video");
//                mContext.startActivity(intent);
//            }
//        });

//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//if(position ==0 ){
//    holder.iv1.setImageResource(feedcovers[1]);
//}
        final List<String> img = new ArrayList<>();
        if (feedcovers!= null) {
            Log.e("===========>122", feedcovers + "");

            for (int i = 0; i < feedcovers.length; i++) {


                if (Position == 0) {
                    if (position == 0)

                        holder.iv1.setImageResource(feedcovers[1]);
                }

                if (Position == 1) {

                    if (position == 0)
                        holder.iv1.setImageResource(feedcovers[2]);
                    else if (position == 1)
                        holder.iv1.setImageResource(feedcovers[0]);
                }

                if (Position == 2) {
                    if (position == 0)
                        holder.iv1.setImageResource(feedcovers[1]);
                    else if (position == 1)
                        holder.iv1.setImageResource(feedcovers[2]);
                    else if (position == 2)
                        holder.iv1.setImageResource(feedcovers[0]);
                }
            }
            }


                ///  voice_layout.setVisibility(View.GONE);
             //   img.add(feeddataList.get(i).getFeedImg());

//
            }
//
            //Set MediaController  to enable play, pause, forward, etc options.

          //  holder.iv2.setImageResource(feedcovers[1]);







    @Override
    public int getItemCount() {

        if (feedcovers.length != 0){
            return feedcovers.length;}


        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {



        LinearLayout linear,voice_layout;
        ImageView iv_youtube_thumnail;
        TextView txt_name, txt_desc;
        RelativeLayout webvideo_layout2;
        VideoView videoView ;
        public   ImageView iv1, iv2, iv3, imageViewPlay;
        public ViewHolder(View itemView) {
            super(itemView);



            iv1 = (ImageView) itemView.findViewById(R.id.iv);



//            iv1.setVisibility(View.GONE);
//            iv2.setVisibility(View.GONE);
//            iv3.setVisibility(View.GONE);
//            location = itemView.findViewById(R.id.village_name);
//            req_date = itemView.findViewById(R.id.reqCreatedDate);
      //    linear = itemView.findViewById(R.id.linear);
//            reopen = itemView.findViewById(R.id.reopen);
//
//            close = itemView.findViewById(R.id.close);
            //     voice_layout = itemView.findViewById(R.id.webvideo_layout2);


        }


    }
}



