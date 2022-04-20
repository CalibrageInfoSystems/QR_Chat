package com.cis.qrchat.Views.splashScreen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cis.qrchat.Chat.BaseApplication;
import com.cis.qrchat.Chat.utils.AuthenticationUtils;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.LanguageActivity;
import com.cis.qrchat.Views.account.LoginActivity;
import com.cis.qrchat.Views.home.HomeActivity;
import com.cis.qrchat.common.Constants;
import com.cis.qrchat.localData.SharedPrefsData;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cis.qrchat.common.CommonUtil.updateResources;


public class SplashActivity extends AppCompatActivity {
    private ImageView logo;

   // private static int splashTimeOut=10000;
    private static int splashTimeOut=500;
    private boolean is_login;



    private String mEncodedAuthInfo;

    private TextView months_left,weeks_left,daysLeft,hrsLeft,minLeft,secLeft,endDate;
    /*Handler Declaration*/
    private Handler handler;
    /*set End Time for timer */
    private String endDateTime="2022-11-21 03:00:00";

     boolean welcome ;
     int langID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
        countDownStart();
//        if (!hasDeepLink()) {
//            autoAuthenticate();
//        }

        autoAuthenticate();
    }
    private void autoAuthenticate() {
        AuthenticationUtils.autoAuthenticate(getApplicationContext(), userId -> {
            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void run() {

                    if (langID == 2)
                        updateResources(SplashActivity.this, "ar");
                    else
                        updateResources(SplashActivity.this, "en-US");

                    if(!is_login){
                        Intent mySuperIntent = new Intent(SplashActivity.this, LanguageActivity.class);
                        startActivity(mySuperIntent);  

                        //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                        finish();
                    }else {
                        Intent mySuperIntent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(mySuperIntent);

                        //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                        finish();

                    }
                }


            }, splashTimeOut);
        });
    }
    private boolean hasDeepLink() {
        boolean result = false;

        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                String scheme = data.getScheme();
                if (scheme != null && scheme.equals("sendbird")) {
                    Log.i(BaseApplication.TAG, "[SplashActivity] deep link: " + data.toString());
                    mEncodedAuthInfo = data.getHost();
                    if (!TextUtils.isEmpty(mEncodedAuthInfo)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private void init() {
        logo = findViewById(R.id.imglogo);
        is_login = SharedPrefsData.getBool(SplashActivity.this, Constants.IS_LOGIN);
       welcome = SharedPrefsData.getBool(SplashActivity.this, Constants.WELCOME);
         langID = SharedPrefsData.getInstance(SplashActivity.this).getIntFromSharedPrefs("lang");



        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splashscreenanimation);
        logo.startAnimation(myanim);



        daysLeft = findViewById(R.id.daysLeft);
        hrsLeft = findViewById(R.id.hrsLeft);
        minLeft = findViewById(R.id.minLeft);
        secLeft = findViewById(R.id.secLeft);


    }
    public void countDownStart() {
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    // Please set date in  YYYY-MM-DD hh:mm:ss format
                    /*parse endDateTime in future date*/
                    Date futureDate = dateFormat.parse(endDateTime);
                    Date currentDate = new Date();
                    /*if current date is not comes after future date*/
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();

                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days *(24  *60 * 60  *1000);
                        long hours = diff / (60 * 60*  1000);
                        diff -= hours * (60*  60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60  *1000);
                        long seconds = diff / 1000;
                        @SuppressLint("DefaultLocale") String dayLeft = "" + String.format("%02d", days);
                        @SuppressLint("DefaultLocale") String hrLeft = "" + String.format("%02d", hours);
                        @SuppressLint("DefaultLocale") String minsLeft = "" + String.format("%02d", minutes);
                        @SuppressLint("DefaultLocale") String secondLeft = "" + String.format("%02d", seconds);
                        daysLeft.setText(dayLeft);
                        hrsLeft.setText(hrLeft);
                        minLeft.setText(minsLeft );
                        secLeft.setText(secondLeft);

                    } else {
//                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
