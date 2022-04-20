package com.cis.qrchat.Chat;

import androidx.annotation.NonNull;

import com.cis.qrchat.Chat.fragments.CustomInviteChannelFragment;
import com.cis.qrchat.R;
import com.sendbird.uikit.activities.InviteChannelActivity;

import com.sendbird.uikit.fragments.InviteChannelFragment;

public class CustomInviteChannelActivity extends InviteChannelActivity {
    @Override
    protected InviteChannelFragment createInviteChannelFragment(@NonNull String channelUrl) {
        getSupportActionBar().hide();
        return new InviteChannelFragment.Builder(channelUrl, R.style.CustomUserListStyle)
                .setCustomInviteChannelFragment(new CustomInviteChannelFragment())
                .setUseHeader(true)
                .setHeaderTitle(getString(R.string.sb_text_header_invite_member))
                .setUseHeaderLeftButton(true)
                .setUseHeaderRightButton(true)
                .setHeaderLeftButtonIconResId(R.drawable.icon_arrow_left)
                .setHeaderLeftButtonListener(null)
                .setUserListAdapter(null)
              // .setCustomUserListQueryHandler(BaseApplication.getCustomUserListQuery())
                .setInviteButtonText(null)
                .build();
    }
}