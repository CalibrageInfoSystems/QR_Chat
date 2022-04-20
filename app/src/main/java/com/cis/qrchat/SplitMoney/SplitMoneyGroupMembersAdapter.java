package com.cis.qrchat.SplitMoney;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class SplitMoneyGroupMembersAdapter extends RecyclerView.Adapter<SplitMoneyGroupMembersAdapter.MyViewHolder>  {

    private Context context;
    private List<GetGroupMembers.ListResult> groupList;
    private List<GetGroupMembers.ListResult> groupMembersList;
    private SplitMoneyGroupMemberListner listener;
    private ArrayList<String> useridArr = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupMemberName;
        ImageView memberprofileImg;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            groupMemberName = view.findViewById(R.id.memberName);
            memberprofileImg = view.findViewById(R.id.profileImg);
            cardView = view.findViewById(R.id.cardview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSplitGroupMemberSelected(groupMembersList.get(getAdapterPosition()));
                    // listener.setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))
                }
            });
        }
    }


    public SplitMoneyGroupMembersAdapter(Context context, List<GetGroupMembers.ListResult> groupList, SplitMoneyGroupMemberListner listener) {
        this.context = context;
        this.listener = listener;
        this.groupList = groupList;
        this.groupMembersList = groupList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groupmember_details, parent, false);


        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else
            updateResources(context, "en-US");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final GetGroupMembers.ListResult groupmembers = groupMembersList.get(position);
        holder.groupMemberName.setText(groupmembers.getName());

        Glide.with(context)
                .load(groupmembers.getUserImage())
               .error(R.drawable.ic_user_contactss)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.memberprofileImg);


        if ((position == 0) || (position == 4) || (position == 8)){

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lytpink));
        }
        if ((position == 1) || (position == 5) || (position == 9)){

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lytyellow));
        }
            if ((position == 2) || (position == 6) || (position == 10)){

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lytorange));
        }
        if ((position == 3) || (position == 7) || (position == 11)){

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.lytblue));
        }

        if(groupmembers.getUserImage() == null)
        {
            Log.d("USERS ADAPTER"," ==========> Applied Padding");
            holder.memberprofileImg.setPadding(5,5,5,5);
        }

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                listener.onSplitGroupMemberSelected(groupMembersList.get(position));
                return true;
            }
        });
}

    @Override
    public int getItemCount() {
        return groupMembersList.size();
    }
    public interface SplitMoneyGroupMemberListner {
        void onSplitGroupMemberSelected(GetGroupMembers.ListResult groupmembers);
    }

    public void showDialog(Context context, String msg) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.deletedialog);
        final ImageView img = dialog.findViewById(R.id.img_cross);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Button yesbTn = (Button) dialog.findViewById(R.id.btn_yes);
        Button nobTn = (Button) dialog.findViewById(R.id.btn_no);
        yesbTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        nobTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Animatable) img.getDrawable()).start();
            }
        }, 500);
    }


}
