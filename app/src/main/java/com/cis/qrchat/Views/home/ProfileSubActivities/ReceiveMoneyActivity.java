package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class ReceiveMoneyActivity extends AppCompatActivity {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_money);

        settoolbar();
    }

    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrmoney));
        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left_rtl);

            }

            else {
                updateResources(this, "en-US");
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