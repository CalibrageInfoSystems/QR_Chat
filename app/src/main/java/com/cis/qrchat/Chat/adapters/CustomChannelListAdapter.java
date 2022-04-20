package com.cis.qrchat.Chat.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.cis.qrchat.Chat.viewholders.CustomChannelViewHolder;
import com.cis.qrchat.databinding.ViewCustomChannelHolderBinding;
import com.sendbird.android.GroupChannel;
import com.sendbird.uikit.activities.adapter.ChannelListAdapter;
import com.sendbird.uikit.activities.viewholder.BaseViewHolder;


public class CustomChannelListAdapter extends ChannelListAdapter {
    @NonNull
    @Override
    public BaseViewHolder<GroupChannel> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomChannelViewHolder(ViewCustomChannelHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<GroupChannel> holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
