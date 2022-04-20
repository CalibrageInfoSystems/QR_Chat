package com.cis.qrchat.Adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.Call_data;
import com.cis.qrchat.Model.GetPassbookDetails;
import com.cis.qrchat.Model.GetTransactionsDetails;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.cis.qrchat.SplitMoney.GroupTransactionAdapter.formatDateFromDateString;
import static com.cis.qrchat.common.CommonUtil.updateResources;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder>
{
    private Context context;
    private List<GetPassbookDetails.ListResult> transactionList;
    private List<String> datelist = new ArrayList<String>();
    String datetimevaluereq;
    public String dateStr, formatteddateStr;
    DecimalFormat dff = new DecimalFormat("##,##,##0.00");
    DecimalFormat df = new DecimalFormat("##,##,##0");
    public static final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT2 = "dd-MM-yyyy";
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description,amount,time;
        public ImageView thumbnail,callicon;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            thumbnail = view.findViewById(R.id.thumbnail);
            callicon = view.findViewById(R.id.callicon);
            amount = view.findViewById(R.id.amount);
            time = view.findViewById(R.id.time);
//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
//                }
//            });
        }
    }

//    public void updateData(List<CollectionResponceModel.CollectioDatum> viewModels) {
//        transactionList.clear();
//        transactionList.addAll(viewModels);
//        notifyDataSetChanged();
//    }
    public void clearAllDataa() {
        transactionList.clear();
        notifyDataSetChanged();
    }
    public TransactionHistoryAdapter(Context context, List<GetPassbookDetails.ListResult> contactList) {
        this.context = context;
        this.datelist = datelist;
        this.transactionList = contactList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Collections.sort(transactionList, new Comparator<GetPassbookDetails.ListResult>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            @Override
            public int compare(GetPassbookDetails.ListResult lhs,GetPassbookDetails.ListResult rhs) {
                try {
                    return f.parse(rhs.getCreated()).compareTo(f.parse(lhs.getCreated()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        dateStr = transactionList.get(position).getCreated() + "";
        try {
            formatteddateStr = formatDateFromDateString(DATE_FORMAT1, DATE_FORMAT2, dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(context, "ar");
                holder.amount.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                holder.name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }

            else
                updateResources(context, "en-US");

        final GetPassbookDetails.ListResult contact = transactionList.get(position);
        holder.name.setText(contact.getFullName());
        holder.description.setText(contact.getPhoneNumber());
        if(contact.getTransactionTypeId()== 22) {
            holder.amount.setTextColor(context.getResources().getColor(R.color.red, null));


            Double d = contact.getAmount();
            String ar = String.format("%.2f", d);
            holder.amount.setText(ar + " " + context.getResources().getString(R.string.qar));
        }else {
            holder.amount.setTextColor(context.getResources().getColor(R.color.groupChatReadReceiptMe, null));
            Double e = contact.getAmount();
            String ar = String.format("%.2f", e);
            holder.amount.setText(ar + " " + context.getResources().getString(R.string.qar));
        }

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date oneWayTripDate = input.parse(dateStr);

            datetimevaluereq = output.format(oneWayTripDate);


            Log.e("===============", "======currentData======" + output.format(oneWayTripDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.time.setText(formatteddateStr);
        Glide.with(context)
                .load(contact.getUserImage())
                .error(R.drawable.ic_user)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }



    public interface ContactsAdapterListener {
        void onContactSelected(Call_data contact);
    }
}