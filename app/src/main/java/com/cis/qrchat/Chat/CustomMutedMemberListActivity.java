package com.cis.qrchat.Chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cis.qrchat.Chat.fragments.CustomMutedMemberListFragment;
import com.cis.qrchat.R;
import com.sendbird.uikit.activities.MutedMemberListActivity;

import com.sendbird.uikit.fragments.MutedMemberListFragment;

public class CustomMutedMemberListActivity extends MutedMemberListActivity {
    @Override
    protected Fragment createMutedMemberListFragment(@NonNull String channelUrl) {
        getSupportActionBar().hide();
        return new MutedMemberListFragment.Builder(channelUrl, R.style.SendBird_Custom)
                .setCustomMutedMemberFragment(new CustomMutedMemberListFragment())
                .setUseHeader(true)
                .setUseHeaderLeftButton(true)
                .setUseHeaderRightButton(false)
                .setHeaderTitle(getString(R.string.sb_text_menu_muted_members))
                .setEmptyIcon(R.drawable.icon_muted)
                .setEmptyText(R.string.sb_text_empty_no_muted_member)
                .setHeaderLeftButtonIconResId(R.drawable.icon_arrow_left)
                .setHeaderLeftButtonListener(null)
                .setHeaderRightButtonIconResId(R.drawable.icon_plus)
                .setHeaderRightButtonListener(null)
                .setItemClickListener(null)
                .setItemLongClickListener(null)
                .setActionItemClickListener(null)
                .setMemberListAdpater(null)
                .build();
    }
}
