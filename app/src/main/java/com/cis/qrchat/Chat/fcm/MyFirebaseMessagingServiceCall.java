package com.cis.qrchat.Chat.fcm;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cis.qrchat.Chat.BaseApplication;
import com.cis.qrchat.Chat.utils.PrefUtils;
import com.cis.qrchat.Chat.utils.PrefUtilsCall;
import com.cis.qrchat.Chat.utils.PushUtils;
import com.cis.qrchat.Chat.utils.PushUtilsCall;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sendbird.calls.SendBirdCall;


public class MyFirebaseMessagingServiceCall extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (SendBirdCall.handleFirebaseMessageData(remoteMessage.getData())) {
            Log.i(BaseApplication.TAG, "[MyFirebaseMessagingService] onMessageReceived() => " + remoteMessage.getData().toString());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.i(BaseApplication.TAG, "[MyFirebaseMessagingService Call] onNewToken(token: " + token + ")");

        if (SendBirdCall.getCurrentUser() != null)  {
            PushUtilsCall.registerPushToken(getApplicationContext(), token, e -> {
                if (e != null) {
                    Log.i(BaseApplication.TAG, "[MyFirebaseMessagingService] registerPushTokenForCurrentUser() => e: " + e.getMessage());
                }
            });
        } else {
            PrefUtilsCall.setPushToken(getApplicationContext(), token);
        }



    }
}
