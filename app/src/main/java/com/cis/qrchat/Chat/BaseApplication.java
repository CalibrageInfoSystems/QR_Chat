    package com.cis.qrchat.Chat;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cis.qrchat.Chat.call.CallService;
import com.cis.qrchat.Chat.fcm.MyFirebaseMessagingService;
import com.cis.qrchat.Chat.fcm.MyFirebaseMessagingServiceCall;
import com.cis.qrchat.Chat.models.CustomUser;
import com.cis.qrchat.Chat.utils.BroadcastUtils;
import com.cis.qrchat.Chat.utils.PrefUtils;
import com.cis.qrchat.Chat.utils.PreferenceUtils;
import com.cis.qrchat.Chat.utils.PushUtils;
import com.cis.qrchat.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sendbird.android.ApplicationUserListQuery;
import com.sendbird.android.FileMessageParams;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdPushHelper;
import com.sendbird.android.User;
import com.sendbird.android.UserMessageParams;
import com.sendbird.calls.DirectCall;
import com.sendbird.calls.SendBirdCall;
import com.sendbird.calls.handler.DirectCallListener;
import com.sendbird.calls.handler.SendBirdCallListener;
import com.sendbird.uikit.SendBirdUIKit;
import com.sendbird.uikit.adapter.SendBirdUIKitAdapter;

import com.sendbird.uikit.interfaces.CustomParamsHandler;
import com.sendbird.uikit.interfaces.CustomUserListQueryHandler;
import com.sendbird.uikit.interfaces.UserInfo;
import com.sendbird.uikit.interfaces.UserListResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();
//    public static final String APP_ID = "3124F905-BEA0-4C5F-ACD6-D397E70CE233";// OLD
    //public static final String APP_ID = "028217F8-D514-4EA9-867B-6EFB70AD9E63"; // commented on 25/2/2020
   // public static final String APP_ID = "E39B1499-E633-4BDC-88CB-9208ECD2803F"; //Added & created on 25/2/2020
    public static final String APP_ID = "047D73D5-E5A8-4936-ADC2-8D2EF5DC7DD3"; //Added & created on 30/03/2020



    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(getApplicationContext());

        SendBirdUIKit.init(new SendBirdUIKitAdapter() {
            @Override
            public String getAppId() {
                return APP_ID;
            }

            @Override
            public String getAccessToken() {
                return "";
            }

            @Override
            public UserInfo getUserInfo() {
                return new UserInfo() {
                    @Override
                    public String getUserId() {
                        return PreferenceUtils.getUserId();
                    }

                    @Override
                    public String getNickname() {
                        return PreferenceUtils.getNickname();
                    }

                    @Override
                    public String getProfileUrl() {
                        return PreferenceUtils.getProfileUrl();
                    }
                };
            }
        }, this);



//        PushUtils.registerPushHandler(new MyFirebaseMessagingService());
//        PushUtils.registerPushHandler(new MyFirebaseMessagingServiceCall());

        SendBirdPushHelper.registerPushHandler(new MyFirebaseMessagingService());


        boolean useDarkTheme = PreferenceUtils.isUsingDarkTheme();
        SendBirdUIKit.setDefaultThemeMode(useDarkTheme ? SendBirdUIKit.ThemeMode.Dark : SendBirdUIKit.ThemeMode.Light);
        PushUtils.registerPushHandler(new MyFirebaseMessagingService());
        SendBirdUIKit.setLogLevel(SendBirdUIKit.LogLevel.ALL);
        SendBirdUIKit.setUseDefaultUserProfile(true);

        SendBirdUIKit.setCustomParamsHandler(new CustomParamsHandler() {
            @Override
            public void onBeforeCreateGroupChannel(@NonNull GroupChannelParams groupChannelParams) {
                // You can set GroupChannelParams globally before creating a channel.
            }

            @Override
            public void onBeforeUpdateGroupChannel(@NonNull GroupChannelParams groupChannelParams) {
                // You can set GroupChannelParams globally before updating a channel.
            }

            @Override
            public void onBeforeSendUserMessage(@NonNull UserMessageParams userMessageParams) {
                // You can set UserMessageParams globally before sending a text message.
            }

            @Override
            public void onBeforeSendFileMessage(@NonNull FileMessageParams fileMessageParams) {
                // You can set FileMessageParams globally before sending a binary file message.
            }

            @Override
            public void onBeforeUpdateUserMessage(@NonNull UserMessageParams userMessageParams) {
                // You can set UserMessageParams globally before updating a text message.
            }
        });

        SendBirdCall.init(getApplicationContext(), APP_ID);
        initSendBirdCall(APP_ID);
    }


    public static CustomUserListQueryHandler getCustomUserListQuery() {
        final ApplicationUserListQuery userListQuery = SendBird.createApplicationUserListQuery();
        return new CustomUserListQueryHandler() {
            @Override
            public void loadInitial(UserListResultHandler handler) {
                userListQuery.setLimit(5);
                userListQuery.next((list, e) -> {
                    if (e != null) {
                        return;
                    }

                    List<CustomUser> customUserList = new ArrayList<>();
                    for (User user : list) {
                        customUserList.add(new CustomUser(user));
                    }
                    handler.onResult(customUserList, null);
                });
            }

            @Override
            public void loadNext(UserListResultHandler handler) {
                userListQuery.next((list, e) -> {
                    if (e != null) {
                        return;
                    }

                    List<CustomUser> customUserList = new ArrayList<>();
                    for (User user : list) {
                        customUserList.add(new CustomUser(user));
                    }
                    handler.onResult(customUserList, null);
                });
            }

            @Override
            public boolean hasMore() {
                return userListQuery.hasNext();
            }
        };
    }
    public boolean initSendBirdCall(String appId) {
        Log.i(BaseApplication.TAG, "[BaseApplication] initSendBirdCall(appId: " + appId + ")");
        Context context = getApplicationContext();

        if (TextUtils.isEmpty(appId)) {
            appId = APP_ID;
        }

        if (SendBirdCall.init(context, appId)) {
            SendBirdCall.removeAllListeners();
            SendBirdCall.addListener(UUID.randomUUID().toString(), new SendBirdCallListener() {
                @Override
                public void onRinging(DirectCall call) {
                    int ongoingCallCount = SendBirdCall.getOngoingCallCount();
                    Log.i(BaseApplication.TAG, "[BaseApplication] onRinging() => callId: " + call.getCallId() + ", getOngoingCallCount(): " + ongoingCallCount);

                    if (ongoingCallCount >= 2) {
                        call.end();
                        return;
                    }

                    call.setListener(new DirectCallListener() {
                        @Override
                        public void onConnected(DirectCall call) {
                        }

                        @Override
                        public void onEnded(DirectCall call) {
                            int ongoingCallCount = SendBirdCall.getOngoingCallCount();
                            Log.i(BaseApplication.TAG, "[BaseApplication] onEnded() => callId: " + call.getCallId() + ", getOngoingCallCount(): " + ongoingCallCount);

                            BroadcastUtils.sendCallLogBroadcast(context, call.getCallLog());

                            if (ongoingCallCount == 0) {
                                CallService.stopService(context);
                            }
                        }
                    });

                    CallService.onRinging(context, call);
                }
            });

            SendBirdCall.Options.addDirectCallSound(SendBirdCall.SoundType.DIALING, R.raw.dialing);
            SendBirdCall.Options.addDirectCallSound(SendBirdCall.SoundType.RINGING, R.raw.ringing);
            SendBirdCall.Options.addDirectCallSound(SendBirdCall.SoundType.RECONNECTING, R.raw.reconnecting);
            SendBirdCall.Options.addDirectCallSound(SendBirdCall.SoundType.RECONNECTED, R.raw.reconnected);
            return true;
        }


        return false;
    }

}
