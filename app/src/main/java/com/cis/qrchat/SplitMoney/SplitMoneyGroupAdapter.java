package com.cis.qrchat.SplitMoney;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Model.GetGroups;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class SplitMoneyGroupAdapter extends RecyclerView.Adapter<SplitMoneyGroupAdapter.MyViewHolder> {

    private Context context;
    private List<GetGroups.ListResult> groupList;
    private List<GetGroups.ListResult> groupListFiltered;
    private SplitMoneyGroupListner listener;
    private ArrayList<String> useridArr = new ArrayList<>();
    private String convertDate;
    private int selectedposition = 0;
    Double splitamount;

    public static final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT2 = "MMM yy";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName,createdDate, spentamount, splitamount, createdon;
        LinearLayout groupview;
        Button payBtn;

        public MyViewHolder(View view) {
            super(view);
            groupName = view.findViewById(R.id.groupName);
            createdDate = view.findViewById(R.id.groupcreatedDate);
            spentamount = view.findViewById(R.id.spentamount);
            splitamount = view.findViewById(R.id.tv_splitmoney);
            groupview = view.findViewById(R.id.groupview);
            createdon = view.findViewById(R.id.createdon);
            //payBtn = view.findViewById(R.id.pay);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback

                    // listener.setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))
                }
            });
        }
    }

    public SplitMoneyGroupAdapter(Context context, List<GetGroups.ListResult> groupList, SplitMoneyGroupListner listener) {
        this.context = context;
        this.listener = listener;
        this.groupList = groupList;
        this.groupListFiltered = groupList;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.splitgroupslist, parent, false);

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else {
            updateResources(context, "en-US");
        }

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(context, "ar");
            holder.createdDate.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.createdon.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }

        else {
            updateResources(context, "en-US");
        }

        final GetGroups.ListResult groups = groupListFiltered.get(position);
        holder.groupName.setText(groups.getName());

        String balance = String.valueOf(groups.getTotalAmount());
        Log.d("balancebalance", balance + "");
        double d= Double.parseDouble(balance);
        Log.d("DoubleBalance", balance + "");
        String ar = String.format("%.2f", d);
        holder.spentamount.setText(ar + " " + context.getResources().getString(R.string.qar));

        splitamount = groups.getShareAmount();

        holder.splitamount.setText((splitamount) + " " +context.getResources().getString(R.string.qar)+ " - " + context.getResources().getString(R.string.whensplitequally));

        holder.groupview.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                selectedposition = position;
                notifyDataSetChanged();
                listener.onSplitGroupSelected(groupListFiltered.get(position));

            }
        });

        try {
            convertDate = formatDateFromDateString(DATE_FORMAT1, DATE_FORMAT2, groups.getCreatedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("ConvetedDate", "is"+convertDate );

        holder.createdDate.setText(convertDate);

        if (selectedposition == position){

            holder.groupview.setBackground(context.getDrawable( R.drawable.pink));
        }else{
            holder.groupview.setBackground(context.getDrawable( R.drawable.blue));
        }


//        holder.payBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                listener.onGroupPaySelected(groupListFiltered.get(position));
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return groupListFiltered.size();
    }

    public interface SplitMoneyGroupListner {
        void onSplitGroupSelected(GetGroups.ListResult group);
        void onGroupPaySelected(GetGroups.ListResult group);

    }


    public static String formatDateFromDateString(String inputDateFormat, String outputDateFormat, String inputDate) throws ParseException {
        Date mParsedDate;
        String mOutputDateString;
        SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputDateFormat, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputDateFormat, java.util.Locale.getDefault());
        mParsedDate = mInputDateFormat.parse(inputDate);
        mOutputDateString = mOutputDateFormat.format(mParsedDate);
        return mOutputDateString;

    }

}
