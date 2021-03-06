package com.cis.qrchat.Chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cis.qrchat.Chat.fragments.CustomPromoteOperatorsFragment;
import com.cis.qrchat.R;
import com.sendbird.android.GroupChannel;
import com.sendbird.uikit.activities.PromoteOperatorsActivity;

import com.sendbird.uikit.fragments.PromoteOperatorsFragment;

public class CustomPromoteOperatorsActivity extends PromoteOperatorsActivity {
    @Override
    protected Fragment createPromoteOperatorFragment(@NonNull GroupChannel channel) {
        getSupportActionBar().hide();
        return new PromoteOperatorsFragment.Builder(channel.getUrl(), R.style.SendBird_Custom)
                .setCustomPromoteOperatorFragment(new CustomPromoteOperatorsFragment())
                .setUseHeader(true)
                .setUseHeaderLeftButton(true)
                .setUseHeaderRightButton(true)
                .setHeaderTitle(getString(R.string.sb_text_header_select_members))
                .setHeaderLeftButtonIconResId(R.drawable.icon_arrow_left)
                .setHeaderLeftButtonListener(null)
                .setRightButtonText(getString(R.string.sb_text_promote_operator))
                .setUserListAdapter(null)
                .setCustomUserListQueryHandler(null)
                .build();
    }
}
