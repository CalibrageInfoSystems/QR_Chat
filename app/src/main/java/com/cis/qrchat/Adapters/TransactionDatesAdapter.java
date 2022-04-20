package com.cis.qrchat.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.Call_data;
import com.cis.qrchat.Model.GetPassbookDetails;
import com.cis.qrchat.Model.Transactiondata;
import com.cis.qrchat.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDatesAdapter extends RecyclerView.Adapter<TransactionDatesAdapter.MyViewHolder>
{
    private Context context;
    private List<GetPassbookDetails.ListResult> transactionList;

    private List<Transactiondata> transaction_List = new ArrayList<>();
    private List<String> datelist = new ArrayList<String>();
    String datetimevaluereq;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView day, date,amount,time;
        private RecyclerView rvSubItem;

        public MyViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.day);
            date = view.findViewById(R.id.date);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
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


    public TransactionDatesAdapter(Context context, List<GetPassbookDetails.ListResult> contactList,List<String> datelist) {
        this.context = context;
        this.datelist = datelist;
        this.transactionList = contactList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_history_dates, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GetPassbookDetails.ListResult contact = transactionList.get(position);


    //   holder.day.setText(contact.getName());


        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date oneWayTripDate = input.parse(datelist.get(position));

            datetimevaluereq = output.format(oneWayTripDate);


            Log.e("===============", "======currentData======" + output.format(oneWayTripDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        holder.date.setText(datetimevaluereq);
        for (GetPassbookDetails.ListResult item:transactionList
        ) {
            Log.d("Anlysis============>", "curreent date :"+item.getCreated()+" && recv exist date Compare date :"+datelist.get(position));



//                if(item.getCreated().equalsIgnoreCase(datelist.get(position))  )
//                {
//                    transaction_List.add(new Transactiondata(item.getAmount(),item.getTransactionTypeId(),item.getName(),item.getMobileNumber(),item.getUserImage()));
//
//                    Log.d("Anlysis============>","Date Compatered added to list");
//                    //print log
//                }else
//                {
//                    Log.d("Anlysis============>","Date not compared andd not added to list");
//                    //  print  log
//                }

        }

    //    TransactionHistoryAdapter subItemAdapter = new TransactionHistoryAdapter(context,transaction_List);

//        holder.rvSubItem.setLayoutManager(layoutManager);
//        holder.rvSubItem.setAdapter(subItemAdapter);
//        holder.rvSubItem.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return datelist.size();
    }



    public interface ContactsAdapterListener {
        void onContactSelected(Call_data contact);
    }
}