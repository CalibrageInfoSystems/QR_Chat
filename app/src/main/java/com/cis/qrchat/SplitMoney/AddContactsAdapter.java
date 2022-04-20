package com.cis.qrchat.SplitMoney;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Model.AmountDetails;
import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.Item;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class AddContactsAdapter extends RecyclerView.Adapter<AddContactsAdapter.MyViewHolder>  {

    private Context context;
    private List<GetGroupMembers.ListResult> itemsList;
    private AddContactsAdapterListner listener;
    private ArrayList<AmountDetails> amount_data = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupMemberName, mobileNumber;
        ImageView memberprofileImg;
        LinearLayout mainLyt;
        CheckBox selectcheckbox;
        EditText equalAmount;

        public MyViewHolder(View view) {
            super(view);
            groupMemberName = view.findViewById(R.id.memberName);
            mobileNumber = view.findViewById(R.id.membermobileNumber);
            memberprofileImg = view.findViewById(R.id.profileImg);
            mainLyt = view.findViewById(R.id.memberlyt);
            selectcheckbox = view.findViewById(R.id.selectcheckbox);
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


    public AddContactsAdapter(Context context, List<GetGroupMembers.ListResult> itemsList, AddContactsAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.itemsList = itemsList;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addcontactadpter, parent, false);


        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else {
            updateResources(context, "en-US");
        }

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(context, "ar");
                holder.groupMemberName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);


            } else {
                updateResources(context, "en-US");
            }

        final GetGroupMembers.ListResult item = itemsList.get(position);
        holder.groupMemberName.setText(item.getName());
        holder.mobileNumber.setText(item.getPhoneNumber());
     amount_data.add(new AmountDetails(item.getUserId(),0));
        if(null != item.getEqualamount())
        {
            holder.equalAmount.setText(item.getEqualamount().toString());
        }

//        else{
//            holder.equalAmount.setText(String.valueOf(0));
//        }


//        holder.equalAmount.addTextChangedListener(new TextChangedListener<EditText>( holder.equalAmount) {
//            @Override
//            public void onTextChanged(EditText target, Editable s) {
//                //Do stuff
//
//                amount_data.set(position,new AmountDetails(item.getUserId(),
//                        TextUtils.isEmpty( holder.equalAmount.getText()) == true ? 0 : Double.valueOf( holder.equalAmount.getText().toString())
//                ));
//
//                listener.onTextChanged(amount_data,position);
//
//
//
//            }
//        });

            if (item.getSelected() == true){

                holder.selectcheckbox.setChecked(true);
            }


            holder.selectcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    item.setSelected(b);
                    itemsList.set(position,item);
                    listener.onunchecked(itemsList.get(position), position);

//                    notifyItemChanged(position);
                }
            });



        Glide.with(context)
                .load(item.getUserImage())
                .error(R.drawable.ic_user_contactss)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.memberprofileImg);

        holder.mainLyt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                holder.selectcheckbox.setVisibility(View.VISIBLE);

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
    public interface AddContactsAdapterListner {
        void onitemselected(GetGroupMembers.ListResult items);
        void onunchecked(GetGroupMembers.ListResult items, int po);
        void onTextChanged(ArrayList<AmountDetails> Amount, int po);

    }



    public abstract static class TextChangedListener<T> implements TextWatcher {
        private T target;

        public TextChangedListener(T target) {
            this.target = target;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target, s);
        }

        public abstract void onTextChanged(T target, Editable s);
    }
}
