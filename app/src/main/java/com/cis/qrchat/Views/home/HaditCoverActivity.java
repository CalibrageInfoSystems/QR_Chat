package com.cis.qrchat.Views.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import de.hdodenhof.circleimageview.CircleImageView;

public class HaditCoverActivity extends AppCompatActivity {
    CircleImageView imageview;
    Button open;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadit_cover);
        intviews();
        setviews();
        settoolbar();
    }

    private void intviews() {
        imageview = findViewById(R.id.imageView);
        open = findViewById(R.id.btn_open);
    }
    private void setviews() {
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();

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
}
