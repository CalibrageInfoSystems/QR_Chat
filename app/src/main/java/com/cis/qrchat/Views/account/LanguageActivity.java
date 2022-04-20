package com.cis.qrchat.Views.account;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cis.qrchat.R;
import com.cis.qrchat.Views.splashScreen.SplashActivity;
import com.cis.qrchat.localData.SharedPrefsData;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class LanguageActivity extends Activity {
    Button loginBtn,registerBtn;
TextView language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        intviews();
        setviews();
    }

    private void intviews() {
        language = findViewById(R.id.language);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
    }
    private void setviews() {
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLanguage();
            }

        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LanguageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LanguageActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }


    private void selectLanguage() {

        final Dialog dialog = new Dialog(LanguageActivity.this, R.style.DialogSlideAnim);
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


                updateResources(LanguageActivity.this, "en-US");
                SharedPrefsData.getInstance(LanguageActivity.this).updateIntValue(LanguageActivity.this, "lang", 1);
                Intent refresh = new Intent(getApplicationContext(), LanguageActivity.class);
                startActivity(refresh);
               finish();
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
                updateResources(LanguageActivity.this, "ar");
                SharedPrefsData.getInstance(LanguageActivity.this).updateIntValue(LanguageActivity.this, "lang", 2);
                Intent refresh = new Intent(getApplicationContext(), LanguageActivity.class);
                startActivity(refresh);
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
