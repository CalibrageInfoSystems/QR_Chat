package com.cis.qrchat.Chat;

import androidx.annotation.NonNull;

import com.cis.qrchat.Chat.fragments.CustomModerationFragment;
import com.cis.qrchat.R;
import com.sendbird.uikit.activities.ModerationActivity;

import com.sendbird.uikit.fragments.ModerationFragment;

public class CustomModerationActivity extends ModerationActivity {
    @Override
    protected ModerationFragment createModerationFragment(@NonNull String channelUrl) {
        getSupportActionBar().hide();
        return new ModerationFragment.Builder(channelUrl, R.style.SendBird_Custom)
                .setCustomModerationFragment(new CustomModerationFragment())
                .setUseHeader(true)
                .setUseHeaderLeftButton(true)
                .setHeaderTitle(getString(R.string.sb_text_channel_settings_moderations))
                .setHeaderLeftButtonIconResId(R.drawable.icon_arrow_left)
                .setHeaderLeftButtonListener(null)
                .setOnMenuItemClickListener(null)
                .build();
    }
}
