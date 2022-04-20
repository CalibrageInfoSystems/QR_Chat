package com.cis.qrchat.Adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.GetPassbookDetails;
import com.cis.qrchat.Model.GetTransactions;
import com.cis.qrchat.Model.GetTransactionsDetails;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class TransactionAdapter  extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>
{
    private Context context;
    private List<GetTransactionsDetails.ListResult> contactList;
  //  private List<Getcontactlist.ListResult> contactListFiltered;
  SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
String datetimevaluereq,datetimevaluereq2;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, description, time,amount2, description2,time2;
        public ImageView thumbnail;
        LinearLayout linear,linear2;

        public MyViewHolder(View view) {
            super(view);
            amount = view.findViewById(R.id.amount);
            description = view.findViewById(R.id.description);
            amount2 = view.findViewById(R.id.amount2);
            description2 = view.findViewById(R.id.description2);
            linear = view.findViewById(R.id.linear);
            linear2 = view.findViewById(R.id.linear2);
            time = view.findViewById(R.id.time);
            time2 = view.findViewById(R.id.time2);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                //    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                    // listener.setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))
                }
            });
        }
    }


    public TransactionAdapter(Context context, List<GetTransactionsDetails.ListResult> contactList) {
        this.context = context;

        this.contactList = contactList;

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactionlist, parent, false);

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else
            updateResources(context, "en-US");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GetTransactionsDetails.ListResult contact = contactList.get(position);


        if(contact.getTransactionType().equalsIgnoreCase("Sent Money")) {

            try {
                Date oneWayTripDate = input.parse(contact.getCreated());

                datetimevaluereq = output.format(oneWayTripDate);


                Log.e("===============", "======currentData======" + output.format(oneWayTripDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
            if (langID == 2) {
                updateResources(context, "ar");
                holder.description.setText("أرسل الأموال");
            }

            else{
                updateResources(context, "en-US");
                holder.description.setText("Sent Money");
            }

            holder.linear.setVisibility(View.VISIBLE);
            holder.amount.setText(contact.getAmount() + " " + context.getResources().getString(R.string.qar));

            holder.time.setText(datetimevaluereq);
        }else{
            holder.linear.setVisibility(View.GONE);
        }
        if(contact.getTransactionType().equalsIgnoreCase("Recieved Money")) {
            try {
                Date oneWayTripDate = input.parse(contact.getCreated());

                datetimevaluereq2 = output.format(oneWayTripDate);


                Log.e("===============", "======currentData======" + output.format(oneWayTripDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
            if (langID == 2) {
                updateResources(context, "ar");
                holder.description2.setText("النقود المستلمة");
            }

            else{
                updateResources(context, "en-US");
                holder.description2.setText("Recieved Money");
            }

            holder.linear2.setVisibility(View.VISIBLE);
            holder.amount2.setText(contact.getAmount() +  " " + context.getResources().getString(R.string.qar));
            holder.time2.setText(datetimevaluereq2);
        }
        else {
            holder.linear2.setVisibility(View.GONE);
        }

//        Glide.with(context)
//                .load(contact.getUserImage())
//                .error(R.drawable.ic_user)
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public interface ContactsAdapterListener {
        void onContactSelected(Getcontactlist.ListResult contact);
    }
}