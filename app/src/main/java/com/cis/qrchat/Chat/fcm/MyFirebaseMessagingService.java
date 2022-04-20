/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cis.qrchat.Chat.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.cis.qrchat.Chat.consts.StringSet;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.HomeActivity;
import com.google.firebase.messaging.RemoteMessage;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.SendBirdPushHandler;
import com.sendbird.android.SendBirdPushHelper;
import com.sendbird.uikit.log.Logger;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class MyFirebaseMessagingService extends SendBirdPushHandler {

    private static final String TAG = "MyFirebaseMsgService";
    private static final AtomicReference<String> pushToken = new AtomicReference<>();

    public interface ITokenResult {
        void onPushTokenReceived(String pushToken, SendBirdException e);
    }

    @Override
    public void onNewToken(String token) {
        pushToken.set(token);
    }

    // Invoked when a notification message has been delivered to the current user's client app.
    @Override
    public void onMessageReceived(Context context, RemoteMessage remoteMessage) {
        String channelUrl = null;
        try {
            if (remoteMessage.getData().containsKey("sendbird")) {
                JSONObject sendbird = new JSONObject(remoteMessage.getData().get("sendbird"));
                JSONObject channel = (JSONObject) sendbird.get("channel");
                channelUrl = (String) channel.get("channel_url");

                // If you want to customize a notification with the received FCM message,
                // write your method like the sendNotification() below.
                sendNotification(context, remoteMessage.getData().get("message"), channelUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isUniquePushToken() {
        return false;
    }

    public static void sendNotification(Context context, String messageBody, String channelUrl) {
        // Customize your notification containing the received FCM message.
    }

    public static void getPushToken(ITokenResult listener) {
        String token = pushToken.get();
        if (!TextUtils.isEmpty(token)) {
            listener.onPushTokenReceived(token, null);
            return;
        }

        SendBirdPushHelper.getPushToken((newToken, e) -> {
            if (listener != null) {
                listener.onPushTokenReceived(newToken, e);
            }

            if (e == null) {
                pushToken.set(newToken);
            }
        });
    }
}