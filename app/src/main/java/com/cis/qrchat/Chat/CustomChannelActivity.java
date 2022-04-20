package com.cis.qrchat.Chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cis.qrchat.Chat.adapters.CustomMessageListAdapter;
import com.cis.qrchat.Chat.consts.StringSet;
import com.cis.qrchat.Chat.fragments.CustomChannelFragment;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.CallFragment;
import com.cis.qrchat.Views.home.ChatFragment;
import com.cis.qrchat.Views.home.ExploreFragment;
import com.cis.qrchat.Views.home.ProfileFragment;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.material.snackbar.Snackbar;

import com.sendbird.android.FileMessage;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessageParams;
import com.sendbird.android.FileMessage;
import com.sendbird.android.FileMessageParams;
import com.sendbird.android.MessageMetaArray;
import com.sendbird.android.OpenChannel;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessageParams;
import com.sendbird.uikit.SendBirdUIKit;
import com.sendbird.uikit.activities.ChannelActivity;

import com.sendbird.uikit.fragments.ChannelFragment;
import com.sendbird.uikit.interfaces.CustomParamsHandler;
import com.sendbird.uikit.interfaces.OnResultHandler;
import com.sendbird.uikit.log.Logger;
import com.sendbird.uikit.model.FileInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomChannelActivity extends ChannelActivity {
    private boolean isHighlightMode = false;
    private static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    private RelativeLayout mRootLayout;
    String _channelUrl;
    @Override
    protected ChannelFragment createChannelFragment(@NonNull String channelUrl) {
        _channelUrl = channelUrl;
        final boolean useMessageGroupUI = false;
         getSupportActionBar().hide();
        setUpChatListAdapter();
        return new ChannelFragment.Builder(channelUrl, R.style.CustomMessageListStyle)
                .setCustomChannelFragment(new CustomChannelFragment())
                .setUseHeader(true)
                .setUseHeaderLeftButton(true)
                .setUseHeaderRightButton(true)
                .setUseLastSeenAt(true)
                .setUseTypingIndicator(true)
                .setHeaderLeftButtonIconResId(R.drawable.icon_arrow_left)
                .setHeaderRightButtonIconResId(R.drawable.icon_info)
                .setInputLeftButtonIconResId(R.drawable.icon_add)
                .setInputRightButtonIconResId(R.drawable.icon_send)
                .setInputHint(getString(R.string.enterAmount))
                .setHeaderLeftButtonListener(null)
                .setHeaderRightButtonListener(v -> showCustomChannelSettingsActivity(channelUrl))
                .setMessageListAdapter(new CustomMessageListAdapter(useMessageGroupUI))
                .setItemClickListener(null)
                .setItemLongClickListener(null)
                .setInputLeftButtonListener(v -> showMessageTypeDialog())
                .setMessageListParams(null)
                .setUseMessageGroupUI(useMessageGroupUI)
                .build();
    }

    private void setUpChatListAdapter() {
    }

    private void showCustomChannelSettingsActivity(String channelUrl) {
        Intent intent = CustomChannelSettingsActivity.newIntentFromCustomActivity(CustomChannelActivity.this, CustomChannelSettingsActivity.class, channelUrl);
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    private void showMessageTypeDialog() {



       
        OpenChannel.getChannel(_channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(OpenChannel openChannel, SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }
                List<String> userIDsToMention = new ArrayList<>();
                userIDsToMention.add(openChannel.getOperators().get(1).getUserId());
//                userIDsToMention.add("Julia");

                List<String> translationTargetLanguages = new ArrayList<>();
                translationTargetLanguages.add("fr");       // French
                translationTargetLanguages.add("de");       // German
                List<FileMessage.ThumbnailSize> thumbnailSizes = new ArrayList<>(); // Allowed number of thumbnail images: 3
                thumbnailSizes.add(new FileMessage.ThumbnailSize(100,100));
                thumbnailSizes.add(new FileMessage.ThumbnailSize(200,200));
File f = new File("/sdcard/DCIM/Camera/IMG_20191026_175713.jpg");
                FileMessageParams params = new FileMessageParams()
                        .setFile(f)              // Or .setFileURL(FILE_URL). You can also send a file message with a file URL.
                        .setFileName("IMG_20191026_175713")
                        .setFileSize(7)
                        .setMimeType(".jpg")
                        .setThumbnailSizes(thumbnailSizes)
                        .setCustomType("")
                        .setData("")
                        .setMentionType(BaseMessageParams.MentionType.CHANNEL)        // Either USERS or CHANNEL
                         ;     // Or .setMentionedUsers(LIST_OF_USERS_TO_MENTION)
//                        .setMetaArrays(Arrays.asList(new MessageMetaArray("itemType", Arrays.asList("tablet")), new MessageMetaArray("quality", Arrays.asList("best", "good")))
//                                .setTranslationTargetLanguages(translationTargetLanguages)
//                                .setPushNotificationDeliveryOption(PushNotificationDeliveryOption.DEFAULT);     // Either DEFAULT or SUPPRESS

                openChannel.sendFileMessage(params, new BaseChannel.SendFileMessageHandler() {
                    @Override
                    public void onSent(FileMessage fileMessage, SendBirdException e) {
                        if (e != null) {    // Error.
                            return;
                        }
                    }
                });
                // TODO: Implement what is needed with the contents of the response in the openChannel parameter.
            }
        });
        new BottomSheet.Builder(this, R.style.BottomSheet_Dialog)


                .grid()// <-- important part
                .sheet(R.menu.menu_bottom_sheet)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case R.id.camera:
                                requestMedia();
                        break;
                    }




                    }
                }).show();
//
    }


    private void requestMedia() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), INTENT_REQUEST_CHOOSE_MEDIA);

            // Set this as false to maintain connection
            // even when an external Activity is started.
            SendBird.setAutoBackgroundDetection(false);
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mRootLayout, getString(R.string.request_external_storage),
                    Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.accept_permission), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }
//    private void onFileMessageClicked(FileMessage message) {
//        String type = message.getType().toLowerCase();
//        if (type.startsWith("image")) {
//            Intent i = new Intent(getActivity(), PhotoViewerActivity.class);
//            i.putExtra("url", message.getUrl());
//            i.putExtra("type", message.getType());
//            startActivity(i);
//        } else if (type.startsWith("video")) {
//            Intent intent = new Intent(getActivity(), MediaPlayerActivity.class);
//            intent.putExtra("url", message.getUrl());
//            startActivity(intent);
//        } else {
//            showDownloadConfirmDialog(message);
//        }
//    }

    public boolean isHighlightMode() {
        return isHighlightMode;
    }
}
