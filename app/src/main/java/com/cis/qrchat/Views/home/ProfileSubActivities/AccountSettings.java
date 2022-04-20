package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.cis.qrchat.Chat.utils.PreferenceUtils;
import com.cis.qrchat.Chat.utils.PushUtils;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.LoginActivity;
import com.cis.qrchat.common.Constants;
import com.cis.qrchat.localData.SharedPrefsData;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.SendBirdPushHelper;
import com.sendbird.uikit.SendBirdUIKit;
import com.sendbird.uikit.widgets.WaitingDialog;

import static com.cis.qrchat.common.CommonUtil.updateResources;


public class AccountSettings extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView dialogMessage, mobileNumber, qrchatid;

    ImageView ra_phone, ra_rqchat, ra_password, ra_email, ra_facebook, ra_insta, ra_loggeddevices, ra_switchaccount;


    private Button ok_btn, cancel_btn,logout;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        intviews();
        setviews();

        ra_phone =  findViewById(R.id.phonerightarrow);
        ra_rqchat=  findViewById(R.id.qrchatrightarrow);
        ra_password=  findViewById(R.id.passwordrightarrow);
        ra_email=  findViewById(R.id.emailrightarrow);
        ra_facebook=  findViewById(R.id.facebookrightarrow);
        ra_insta=  findViewById(R.id.instarightarrow);
        ra_loggeddevices=  findViewById(R.id.loggeddevicesrightarrow);
        ra_switchaccount=  findViewById(R.id.switchaccountrightarrow);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
                ra_phone.setRotation(360);
                ra_rqchat.setRotation(360);
                ra_password.setRotation(360);
                ra_email.setRotation(360);
                ra_facebook.setRotation(360);
                ra_insta.setRotation(360);
                ra_loggeddevices.setRotation(360);
                ra_switchaccount.setRotation(360);
            }

            else
                updateResources(this, "en-US");

        settoolbar();
    }

    private void intviews() {
        logout = (Button)findViewById(R.id.logout);
        qrchatid = findViewById(R.id.qrchatidnumber);
        mobileNumber = findViewById(R.id.phonenumber);
    }
    private void setviews() {

        String phoneNumber = SharedPrefsData.getInstance(AccountSettings.this).getStringFromSharedPrefs("MOBILENUMBER");
        String qrchatId = SharedPrefsData.getInstance(AccountSettings.this).getStringFromSharedPrefs("USERNAME");


        qrchatid.setText(qrchatId);
        mobileNumber.setText(phoneNumber);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutDialog();
            }
            });
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.accountsettings));
        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
            if (langID == 2)
            {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left_rtl);

            } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void logOutDialog() {


        final Dialog dialog = new Dialog(AccountSettings.this, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_logout);
        dialogMessage = dialog.findViewById(R.id.dialogMessage);
        dialogMessage.setText(getString(R.string.alert_logout));
        cancel_btn = dialog.findViewById(R.id.cancel_btn);
        ok_btn = dialog.findViewById(R.id.ok_btn);
/**
 * @param OnClickListner
 */
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                WaitingDialog.show(AccountSettings.this);
//                PushUtils.unregisterPushHandler(new SendBirdPushHelper.OnPushRequestCompleteListener() {
//                    @Override
//                    public void onComplete(boolean isActive, String token) {
//                        SendBirdUIKit.disconnect(() -> {
//                            if (AccountSettings.this == null) {
//                                return;
//                            }
//                            WaitingDialog.dismiss();
//                            PreferenceUtils.clearAll();
//                            SharedPrefsData.getInstance(getApplicationContext()).ClearData(getApplicationContext());
//                            SharedPrefsData.putBool(AccountSettings.this, Constants.IS_LOGIN, false);
//                            Intent intent = new Intent(AccountSettings.this, LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//                        });
//                    }
//
//                    @Override
//                    public void onError(SendBirdException e) {
//                        WaitingDialog.dismiss();
//                    }
//                });
                PreferenceUtils.clearAll();
                SharedPrefsData.getInstance(getApplicationContext()).ClearData(getApplicationContext());
                SharedPrefsData.putBool(AccountSettings.this, Constants.IS_LOGIN, false);
                Intent intent = new Intent(AccountSettings.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                dialog.dismiss();
                startActivity(intent);
                finish();


            }});
////                updateResources(getApplicationContext(), "en-US");
//                SharedPrefsData.getInstance(getApplicationContext()).ClearData(getApplicationContext());
//                SharedPrefsData.putBool(AccountSettings.this, Constants.IS_LOGIN, false);
//
//                finish();



/**
 * @param OnClickListner
 */
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}