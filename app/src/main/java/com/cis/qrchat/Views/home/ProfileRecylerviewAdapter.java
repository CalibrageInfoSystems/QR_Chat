package com.cis.qrchat.Views.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Adapters.ChatListAdapter;
import com.cis.qrchat.Model.Chat_data;
import com.cis.qrchat.Model.Profile_Data;
import com.cis.qrchat.R;
import com.cis.qrchat.SplitMoney.SplitMainActivity;
import com.cis.qrchat.Views.home.ProfileSubActivities.AccountSettings;
import com.cis.qrchat.Views.home.ProfileSubActivities.AppSettings;
import com.cis.qrchat.Views.home.ProfileSubActivities.MyLifeshots;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRPayAccount;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class ProfileRecylerviewAdapter extends RecyclerView.Adapter<ProfileRecylerviewAdapter.MyViewHolder> {

    private Context context;
    private List<Profile_Data> profileList;
    private List<Profile_Data> profiledataList;




    public ProfileRecylerviewAdapter(Context context, List<Profile_Data> profileList) {
        this.context = context;
        this.profileList = profileList;
        this.profiledataList = profileList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profilerecyclerviewlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(context, "ar");
            holder.rightarrow.setRotation(360);
        }
        else
            updateResources(context, "en-US");

        final Profile_Data profiledata = profiledataList.get(position);

        holder.categoryName.setText(profiledata.getCategoryName());
        holder.categoryImage.setImageResource(profiledata.getCategoryImage());


//        Glide.with(context)
//                .load(profiledata.getCategoryImage())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.categoryImage);

        holder.profilecardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.getAdapterPosition() == 0){

                    Intent intent = new Intent(context, QRPayAccount.class);
                    context.startActivity(intent);
                }

                if (holder.getAdapterPosition() == 1){

                    Intent intent = new Intent(context, MyLifeshots.class);
                    context.startActivity(intent);
                }

                if (holder.getAdapterPosition() == 4){

                    Intent intent = new Intent(context, AccountSettings.class);
                    context.startActivity(intent);
                }

                if (holder.getAdapterPosition() == 5){

                    Intent intent = new Intent(context, AppSettings.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return profiledataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage, rightarrow;
        TextView categoryName;
        CardView profilecardview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryimage);
            categoryName = itemView.findViewById(R.id.categoryname);
            profilecardview = itemView.findViewById(R.id.profilecardview);
            rightarrow = itemView.findViewById(R.id.rightarroww);
        }
    }
}
