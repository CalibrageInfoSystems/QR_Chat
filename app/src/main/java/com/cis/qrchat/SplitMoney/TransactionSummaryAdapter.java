package com.cis.qrchat.SplitMoney;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.AmountDetails;
import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.GetTransactionsByFundId;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class TransactionSummaryAdapter extends RecyclerView.Adapter<TransactionSummaryAdapter.MyViewHolder> {

    private Context context;
    private List<GetTransactionsByFundId.ListResult> itemsList;
    private TransactionSummaryAdapterListner listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupMemberName, mobileNumber;
        ImageView memberprofileImg;
        LinearLayout mainLyt;
        TextView equalAmount;

        public MyViewHolder(View view) {
            super(view);
            groupMemberName = view.findViewById(R.id.memberName);
            mobileNumber = view.findViewById(R.id.membermobileNumber);
            memberprofileImg = view.findViewById(R.id.profileImg);
            mainLyt = view.findViewById(R.id.memberlyt);
            equalAmount = view.findViewById(R.id.memberamount);
            //cardView = view.findViewById(R.id.cardview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onitemselected(itemsList.get(getAdapterPosition()));
                    // listener.setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))
                }
            });
        }
    }


    public TransactionSummaryAdapter(Context context, List<GetTransactionsByFundId.ListResult> itemsList,TransactionSummaryAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.itemsList = itemsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactionsummary, parent, false);


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

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(context, "ar");
            holder.groupMemberName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else
            updateResources(context, "en-US");

        final GetTransactionsByFundId.ListResult item = itemsList.get(position);
        holder.groupMemberName.setText(item.getUserName());
        holder.mobileNumber.setText(item.getPhoneNumber());
        holder.equalAmount.setText(item.getAmount() + " " + context.getResources().getString(R.string.qar));



        Glide.with(context)
                .load(item.getUserImage())
                .error(R.drawable.ic_user_contactss)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.memberprofileImg);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public interface TransactionSummaryAdapterListner {
        void onitemselected(GetTransactionsByFundId.ListResult items);

    }

}
