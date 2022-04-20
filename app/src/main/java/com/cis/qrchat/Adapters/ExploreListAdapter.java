package com.cis.qrchat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.Chat_data;
import com.cis.qrchat.Model.Explore_data;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.FeedActivity;
import com.cis.qrchat.Views.home.ProfileSubActivities.MyLifeshots;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRPayAccount;
import com.cis.qrchat.localData.SharedPrefsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class ExploreListAdapter extends RecyclerView.Adapter<ExploreListAdapter.MyViewHolder> {
    private Context context;
    private List<Explore_data> exploreList;

    private AdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description,time;
        public ImageView thumbnail, rightarrow;
        CardView exploreCardView;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textView);
            thumbnail = view.findViewById(R.id.imageView);
            rightarrow = view.findViewById(R.id.rightarrow);
            exploreCardView = view.findViewById(R.id.exploreCardView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(exploreList.get(getAdapterPosition()));
                }
            });
        }
    }


    public ExploreListAdapter(Context context, List<Explore_data> exploreList, AdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.exploreList = exploreList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(context, "ar");
            holder.rightarrow.setRotation(360);
        }
        else
            updateResources(context, "en-US");


        final Explore_data contact = exploreList.get(position);
        holder.name.setText(contact.getName());
        Picasso.with(context )
                .load(contact.getImages())
                .error(contact.getImages())
                .placeholder(contact.getImages())
                .into(holder.thumbnail);

        holder.exploreCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.getAdapterPosition() == 0){

                    Intent intent = new Intent(context, MyLifeshots.class);
                    context.startActivity(intent);
                }
            }
        });



//        Glide.with(context)
//                .load(contact.getImages())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return exploreList.size();
    }

    public interface AdapterListener {
        void onContactSelected(Explore_data contact);
    }
}
