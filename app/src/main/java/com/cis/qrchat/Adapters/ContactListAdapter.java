package com.cis.qrchat.Adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.cis.qrchat.Model.Chat_data;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class ContactListAdapter  extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Getcontactlist.ListResult> contactList =new ArrayList<>();

    private List<Getcontactlist.ListResult> contactListFiltered;
    private ContactsAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, time;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            thumbnail = view.findViewById(R.id.thumbnail);
            time = view.findViewById(R.id.time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                   // listener.setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))
                }
            });
        }
    }


    public ContactListAdapter(Context context, List<Getcontactlist.ListResult> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contactitem, parent, false);

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else
            updateResources(context, "en-US");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//
//        Collections.sort(contactList, new Comparator<Getcontactlist.ListResult>() {
//            @Override
//            public int compare(Getcontactlist.ListResult item, Getcontactlist.ListResult t1) {
//                String s1 = item.getNickName();
//                String s2 = t1.getNickName();
//                return s1.compareToIgnoreCase(s2);
//            }
//
//        });

    //  Collections.sort(contactList, String.CASE_INSENSITIVE_ORDER);
        final Getcontactlist.ListResult contact= contactListFiltered.get(position);

        Collections.sort(contactList, new Comparator<Getcontactlist.ListResult>() {
            @Override
            public int compare(Getcontactlist.ListResult item, Getcontactlist.ListResult t1) {
                String s1 = item.getNickName();
                String s2 = t1.getNickName();
                Log.e("===========>",s1.compareToIgnoreCase(s2)+"");
                return s1.compareToIgnoreCase(s2);


            }

        });
String name  =contactList. get(position).getNickName();
String phoneNumber = contactList.get(position).getContactNumber();
        holder.name.setText(name);
        holder.description.setText(phoneNumber);
        Log.d("========PhoneNumber", phoneNumber);

        Glide.with(context)
                .load(contactList.get(position).getUserImage())
                .error(R.drawable.ic_user)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                contact.setSelected(!contact.isSelected());
//                holder.itemView.setBackgroundColor(contact.isSelected() ? Color.CYAN : Color.WHITE);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Getcontactlist.ListResult> filteredList = new ArrayList<>();
                    for (Getcontactlist.ListResult row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getContactName().toLowerCase().contains(charString.toLowerCase()) || row.getContactNumber().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Getcontactlist.ListResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Getcontactlist.ListResult contact);
    }
}