package com.cis.qrchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class MembersinExpenseAdapter extends RecyclerView.Adapter<MembersinExpenseAdapter.MyViewHolder> {

    private Context context;
    private List<GetGroupMembers.ListResult> groupList;
    private List<GetGroupMembers.ListResult> groupMembersList;
    private GroupMemberExpensedapterAdapterListner listener;
    private ArrayList<String> useridArr = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupMemberName;
        CheckBox checkBox;
        LinearLayout contactslyt;

        public MyViewHolder(View view) {
            super(view);
            groupMemberName = view.findViewById(R.id.memberDetailName);
            checkBox = view.findViewById(R.id.checkbox);
            contactslyt = view.findViewById(R.id.contactlyt);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onGroupMemberExpenseSelected(groupMembersList.get(getAdapterPosition()));
                    // listener.setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))
                }
            });
        }
    }


    public MembersinExpenseAdapter(Context context, List<GetGroupMembers.ListResult> groupList, GroupMemberExpensedapterAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.groupList = groupList;
        this.groupMembersList = groupList;
    }

    @NonNull
    @Override
    public MembersinExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expensemembers, parent, false);


        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(context, "ar");

        else
            updateResources(context, "en-US");

        return new MembersinExpenseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersinExpenseAdapter.MyViewHolder holder, int position) {

        final GetGroupMembers.ListResult groupmembers = groupMembersList.get(position);
        holder.groupMemberName.setText(groupmembers.getName());

        holder.contactslyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.checkBox.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupMembersList.size();
    }

    public class MemberDetailViewHolder extends RecyclerView.ViewHolder {
        public MemberDetailViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface GroupMemberExpensedapterAdapterListner {
        void onGroupMemberExpenseSelected(GetGroupMembers.ListResult groupmembers);
    }

}
