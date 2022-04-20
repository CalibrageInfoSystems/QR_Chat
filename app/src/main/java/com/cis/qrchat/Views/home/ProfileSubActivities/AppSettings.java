package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.LanguageActivity;
import com.cis.qrchat.Views.home.HomeActivity;
import com.cis.qrchat.Views.home.ProfileFragment;
import com.cis.qrchat.localData.SharedPrefsData;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class AppSettings extends AppCompatActivity {

    private Toolbar toolbar;
    ImageView ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8;
    LinearLayout selectlanguagelayout;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        ra1 =  findViewById(R.id.rightarrow1);
        ra2=  findViewById(R.id.rightarrow2);
        ra3=  findViewById(R.id.rightarrow3);
        ra4=  findViewById(R.id.rightarrow4);
        ra5=  findViewById(R.id.rightarrow5);
        ra6=  findViewById(R.id.rightarrow6);
        ra7=  findViewById(R.id.rightarrow7);
        ra8=  findViewById(R.id.rightarrow8);

        selectlanguagelayout = findViewById(R.id.selectlanguagelayout);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");

                ra1.setRotation(360);
                ra2.setRotation(360);
                ra3.setRotation(360);
                ra4.setRotation(360);
                ra5.setRotation(360);
                ra6.setRotation(360);
                ra7.setRotation(360);
                ra8.setRotation(360);
            }

            else {
                updateResources(this, "en-US");
            }

        settoolbar();

        selectlanguagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLanguage();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.appsettings));
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void selectLanguage() {

        final Dialog dialog = new Dialog(AppSettings.this, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_language);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("");

        // set the custom forgotPasswordDialog components - text, image and button
        final TextView rbEng = dialog.findViewById(R.id.rbEng);
        final TextView rbTelugu = dialog.findViewById(R.id.rbTelugu);

        View view =dialog.findViewById(R.id.view);


//        if (statecode.equalsIgnoreCase("AP")){
//
//            rbTelugu.setVisibility(View.VISIBLE);
//            rbKannada.setVisibility(View.GONE);
//            view2.setVisibility(View.GONE);
//        }
//        else{
//            rbTelugu.setVisibility(View.GONE);
//            rbKannada.setVisibility(View.VISIBLE);
//            view2.setVisibility(View.GONE);
//        }

/**
 * @param OnClickListner
 */
        rbEng.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                /**
                 * "en" is the localization code for our default English language.
                 */


                updateResources(AppSettings.this, "en-US");
                SharedPrefsData.getInstance(AppSettings.this).updateIntValue(AppSettings.this, "lang", 1);
                //startActivity(getIntent());
                Intent intent = new Intent(AppSettings.this, HomeActivity.class);
                startActivity(intent);
                dialog.dismiss();

            }
        });

/**
 * @param OnClickListner
 */
        rbTelugu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                /**
                 * "te" is the localization code for our default Telugu language.
                 */
                updateResources(AppSettings.this, "ar");
                SharedPrefsData.getInstance(AppSettings.this).updateIntValue(AppSettings.this, "lang", 2);

                Intent intent = new Intent(AppSettings.this, HomeActivity.class);
                startActivity(intent);
                //startActivity(getIntent());
                dialog.dismiss();
               // dialog.dismiss();
            }
        });

        dialog.show();
    }

//    public void triggerRebirth(Activity activity, Intent nextIntent) {
//        Intent intent = new Intent(activity, AppSettings.class);
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(KEY_RESTART_INTENT, nextIntent);
//        startActivity(intent);
//        if (activity instanceof Activity) {
//            ((Activity) activity).finish();
//        }
//
//        Runtime.getRuntime().exit(0);
//    }

    public static void doRestart(Activity c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 10;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(c.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 0, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e("TAG", "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e("TAG", "Was not able to restart application, PM null");
                }
            } else {
                Log.e("TAG", "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e("TAG", "Was not able to restart application");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}