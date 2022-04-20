package com.cis.qrchat.SplitMoney;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Model.GetGroupFundTransactions;
import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.GetGroupTransactions;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class GroupTransactionAdapter extends RecyclerView.Adapter<GroupTransactionAdapter.MyViewHolder> {

    private Context context;
    private List<GetGroupFundTransactions.ListResult> grouptransactionList;
    private List<GetGroupFundTransactions.ListResult> grouptransactionFilterList;
    private GroupTransactionListner listener;
    private ArrayList<String> useridArr = new ArrayList<>();

    public static final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT2 = "MMM dd";
    String convertDate, participantName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView billName,billdetails, balanceamount, ows;
        public Button payBtn;

        public MyViewHolder(View view) {
            super(view);
            billName = view.findViewById(R.id.billName);
            billdetails = view.findViewById(R.id.billdetails);
            balanceamount = view.findViewById(R.id.balanceamount);
            ows = view.findViewById(R.id.tv_ows);
            payBtn = view.findViewById(R.id.pay);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onTransactionSelected(grouptransactionFilterList.get(getAdapterPosition()));
                    // listener.setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))
                }
            });
        }
    }


    public GroupTransactionAdapter(Context context, List<GetGroupFundTransactions.ListResult> grouptransactionList,  GroupTransactionListner listener) {
        this.context = context;
        this.listener = listener;
        this.grouptransactionList = grouptransactionList;
        this.grouptransactionFilterList = grouptransactionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grouptransaction_details, parent, false);


        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else
            updateResources(context, "en-US");

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final GetGroupFundTransactions.ListResult grouptransactions = grouptransactionFilterList.get(position);
        holder.billName.setText(grouptransactions.getFundName() );
        holder.balanceamount.setText(grouptransactions.getAmount() + " " + context.getResources().getString(R.string.qar));

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(context, "ar");
            holder.ows.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.balanceamount.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        }
        else
            updateResources(context, "en-US");

        if (grouptransactions.getAmount().startsWith("+")){

            holder.payBtn.setVisibility(View.GONE);
            holder.ows.setText(context.getResources().getString(R.string.owsyou));
            holder.balanceamount.setTextColor(context.getResources().getColor(R.color.green));
        }else if(grouptransactions.getAmount().startsWith("-")){
            holder.payBtn.setVisibility(View.VISIBLE);
            holder.ows.setText(context.getResources().getString(R.string.youowns));
            holder.balanceamount.setTextColor(context.getResources().getColor(R.color.red));
        }else {

            holder.payBtn.setVisibility(View.GONE);
            holder.ows.setText("ows");
            holder.balanceamount.setTextColor(context.getResources().getColor(R.color.green));
        }

        try {
            convertDate = formatDateFromDateString(DATE_FORMAT1, DATE_FORMAT2, grouptransactions.getCreatedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        participantName = grouptransactions.getUserName();
        Log.d("ParticipantName", "is"+participantName);

        holder.billdetails.setText(convertDate+" - by " + participantName);

        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onTransactionPayFundSelected(grouptransactionFilterList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(grouptransactionFilterList != null && grouptransactionFilterList.size() > 0 )
            return grouptransactionFilterList.size();
        else
            return 0;

    }

    public interface GroupTransactionListner {
        void onTransactionSelected(GetGroupFundTransactions.ListResult groupTransactions);
        void onTransactionPayFundSelected(GetGroupFundTransactions.ListResult groupTransactions);
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
