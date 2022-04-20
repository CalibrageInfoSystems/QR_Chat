package com.cis.qrchat.Views.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cis.qrchat.R;
import com.cis.qrchat.SplitMoney.SplitActivity;
import com.cis.qrchat.Views.account.LoginActivity;
import com.cis.qrchat.localData.SharedPrefsData;

public class Contactus extends AppCompatActivity {

    private Toolbar toolbar;
    EditText fullname, email, message;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        settoolbar();

        fullname = findViewById(R.id.fullname);
        email  = findViewById(R.id.contactemail);
        message = findViewById(R.id.reasonforcontact);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validations()){

                    finish();
                }

            }
        });
    }

    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    private boolean validations() {

        String emaill = email.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(fullname.getText().toString())) {
            showDialog(Contactus.this, getResources().getString(R.string.pleaseenterfullname));
            return false;
        }else if (TextUtils.isEmpty(email.getText().toString().trim())) {
            showDialog(Contactus.this, getResources().getString(R.string.pleaseenteremail));
            return false;
        } else if (!emaill.matches(emailPattern)) {
            showDialog(Contactus.this, getResources().getString(R.string.pleaseentervalidemail));
            return false;
        }else if (TextUtils.isEmpty(message.getText().toString().trim())) {
            showDialog(Contactus.this, getResources().getString(R.string.pleaseenterbreifmessage));
            return false;
        }

        return true;
    }

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        final ImageView img = dialog.findViewById(R.id.img_cross);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Animatable) img.getDrawable()).start();
            }
        }, 500);
    }
}