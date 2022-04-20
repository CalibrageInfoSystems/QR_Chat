package com.cis.qrchat.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class AddContactsAdapter extends RecyclerView.Adapter<AddContactsAdapter.MyViewHolder> {
    private Context context;
    private List<Getcontactlist.ListResult> contactList;
    private List<Getcontactlist.ListResult> contactListFiltered;
    private AddContactsAdapterListner listener;
    private ArrayList<String> useridArr = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, time;
        public ImageView thumbnail;
        LinearLayout mainLyt;
        CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            thumbnail = view.findViewById(R.id.thumbnail);
            time = view.findViewById(R.id.time);
            mainLyt = view.findViewById(R.id.mainLyt);
            checkBox = view.findViewById(R.id.checkbox);


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


    public AddContactsAdapter(Context context, List<Getcontactlist.ListResult> contactList, AddContactsAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public AddContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contactitem, parent, false);


        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else
            updateResources(context, "en-US");

        return new AddContactsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AddContactsAdapter.MyViewHolder holder, final int position) {
        final Getcontactlist.ListResult contact = contactListFiltered.get(position);
        holder.name.setText(contact.getNickName());
        holder.description.setText(contact.getContactNumber());

        Glide.with(context)
                .load(contact.getUserImage())
                .error(R.drawable.ic_user)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);

        holder.mainLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.checkBox.setVisibility(View.VISIBLE);
            }
        });

        if (holder.checkBox.isChecked()){

            Log.d("ContactNamechecked",contact.getNickName() + "");
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    contact.setSelected(true);
                    contactListFiltered.set(position,contact);
                }

            }
        });

//        holder.mainLyt.setOnI (new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//
//                holder.mainLyt.setBackgroundColor(R.color.appColor);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

   public  List<Getcontactlist.ListResult> getselectedContacts()
   {
       List<Getcontactlist.ListResult> selectedContacts = new ArrayList<>();

       for (Getcontactlist.ListResult item  :contactList) {
           if(!selectedContacts.contains(item) && item.isSelected())
           {
               selectedContacts.add(item);
           }
       }


       return  selectedContacts;
   }

    public interface AddContactsAdapterListner {
        void onContactSelected(Getcontactlist.ListResult contact);
    }
}
