package com.cis.qrchat.Views.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.Contactlist;

public class sendmoneyMainActivity extends AppCompatActivity {
    CardView mobilenumber, qrcode;
    private int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney_main);
        intviews();
        setviews();
    }
    private void intviews() {

        mobilenumber = findViewById(R.id.mobilenumber);
        qrcode = findViewById(R.id.qrcode);
    }
    private void setviews() {
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(sendmoneyMainActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(sendmoneyMainActivity.this, sendmoney_QRScannerActivity.class));

                } else {
                    ActivityCompat.requestPermissions((sendmoneyMainActivity) sendmoneyMainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        });

        mobilenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sendmoneyMainActivity.this, GetContactlist.class));

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            startActivity(new Intent(sendmoneyMainActivity.this, sendmoney_QRScannerActivity.class));
        }
    }}