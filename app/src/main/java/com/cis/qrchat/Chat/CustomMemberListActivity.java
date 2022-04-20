package com.cis.qrchat.Chat;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.cis.qrchat.Chat.fragments.CustomMemberListFragment;
import com.cis.qrchat.R;
import com.sendbird.uikit.activities.MemberListActivity;

import com.sendbird.uikit.fragments.MemberListFragment;

public class CustomMemberListActivity extends MemberListActivity {
    @Override
    protected MemberListFragment createMemberListFragment(@NonNull String channelUrl) {
        getSupportActionBar().hide();
        return new MemberListFragment.Builder(channelUrl, R.style.CustomUserListStyle)
                .setCustomMemberListFragment(new CustomMemberListFragment())
                .setUseHeader(true)
                .setHeaderTitle(getString(R.string.sb_text_header_member_list))
                .setUseHeaderLeftButton(true)
                .setUseHeaderRightButton(true)
                .setHeaderLeftButtonIconResId(R.drawable.icon_arrow_left)
                .setHeaderRightButtonIconResId(R.drawable.icon_add_member)
                .setHeaderLeftButtonListener(null)
                .setHeaderRightButtonListener(v -> showCustomInviteChannelActivity(channelUrl))
                .setItemClickListener(null)
                .setItemLongClickListener(null)
                .build();
    }

    private void showCustomInviteChannelActivity(String channelUrl) {
        Intent intent = CustomInviteChannelActivity.newIntentFromCustomActivity(CustomMemberListActivity.this, CustomInviteChannelActivity.class, channelUrl);
        startActivity(intent);
    }
}