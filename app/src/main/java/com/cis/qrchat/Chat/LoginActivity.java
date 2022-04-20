package com.cis.qrchat.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;


import com.cis.qrchat.Chat.utils.AuthenticationUtils;
import com.cis.qrchat.Chat.utils.PrefUtils;
import com.cis.qrchat.Chat.utils.PrefUtilsCall;
import com.cis.qrchat.Chat.utils.PreferenceUtils;
import com.cis.qrchat.Chat.utils.PushUtils;
import com.cis.qrchat.Chat.utils.PushUtilsCall;
import com.cis.qrchat.MainActivity;
import com.cis.qrchat.R;
import com.sendbird.android.SendBird;
import com.sendbird.calls.AuthenticateParams;
import com.sendbird.calls.SendBirdCall;
import com.sendbird.calls.SendBirdException;
import com.sendbird.uikit.BuildConfig;
import com.sendbird.uikit.SendBirdUIKit;
import com.sendbird.uikit.log.Logger;
import com.sendbird.uikit.utils.TextUtils;
import com.sendbird.uikit.widgets.WaitingDialog;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        EditText etUserId = findViewById(R.id.etUserId);
        EditText etNickname = findViewById(R.id.etNickname);
        TextView tvVersion = findViewById(R.id.tvVersionInfo);

        etUserId.setSelectAllOnFocus(true);
        etNickname.setSelectAllOnFocus(true);

        String sdkVersion = String.format(getResources().getString(R.string.text_version_info), BuildConfig.VERSION_NAME, SendBird.getSDKVersion());
        tvVersion.setText(sdkVersion);

        findViewById(R.id.btSignIn).setOnClickListener(v -> {
            String userId = etUserId.getText().toString();
            // Remove all spaces from userID
            userId = userId.replaceAll("\\s", "");

            String userNickname = etNickname.getText().toString();
            if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(userNickname)) {
                return;
            }

            PreferenceUtils.setUserId(userId);
            PreferenceUtils.setNickname(userNickname);

            WaitingDialog.show(this);
            SendBirdUIKit.connect((user, e) -> {
                if (e != null) {
                    Logger.e(e);
                    WaitingDialog.dismiss();
                    return;
                }


                WaitingDialog.dismiss();


                // Authentication


                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }
}



