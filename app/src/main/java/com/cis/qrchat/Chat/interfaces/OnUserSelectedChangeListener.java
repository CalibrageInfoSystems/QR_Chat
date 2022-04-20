package com.cis.qrchat.Chat.interfaces;

import java.util.List;

public interface OnUserSelectedChangeListener {
    void onUserSelectedChange(List<String> selectedUsers, boolean selected);
}
