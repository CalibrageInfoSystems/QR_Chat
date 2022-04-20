package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cis.qrchat.Model.Profileresponse;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.ReciveMainActivity;
import com.cis.qrchat.Views.home.sendmoney_QRScannerActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class QRMoneyActivity extends AppCompatActivity {

    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private Toolbar toolbar;
    Spinner bankSpinner;
    LinearLayout receivemoney,payforcreditcards, sendMoneytoBank;
    String GETQRDATA;
    ArrayList<String> arrayList = new ArrayList<>();
    int langID;
    ImageView qrmoneyImg;



    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();


        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");

        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_q_r_money);

        bankSpinner =  findViewById(R.id.spinnerbank);
        receivemoney =  findViewById(R.id.receivemoney);
        payforcreditcards = findViewById(R.id.payforCreditcard);
        sendMoneytoBank = findViewById(R.id.sendMoneytoBank);
        qrmoneyImg = findViewById(R.id.qrmoneyImg);

        getuserdata();

        arrayList.add(getResources().getString(R.string.dohabank));
        arrayList.add(getResources().getString(R.string.commercialbank));
        arrayList.add(getResources().getString(R.string.qibbank));
        arrayList.add(getResources().getString(R.string.qiibbank));
        arrayList.add(getResources().getString(R.string.qnbbank));
        arrayList.add(getResources().getString(R.string.dukhanbank));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankSpinner.setAdapter(arrayAdapter);


        sendMoneytoBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent receivemoneyscreen =  new Intent(QRMoneyActivity.this, sendmoney_QRScannerActivity.class);
                startActivity(receivemoneyscreen);

            }
        });

        receivemoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivemoneyscreen =  new Intent(QRMoneyActivity.this, ReciveMainActivity.class);
                startActivity(receivemoneyscreen);
            }
        });


        payforcreditcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivemoneyscreen =  new Intent(QRMoneyActivity.this, ShowCards.class);
                startActivity(receivemoneyscreen);
            }
        });

        settoolbar();
    }





    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrmoney));
        setSupportActionBar(toolbar);


        if (langID == 2)
        {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left_rtl);

            }

            else {

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

    private void getuserdata() {
        String User_name = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERNAME");
        Log.e("============>",User_name);
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getprofilerespose(APIConstantURL.getprofile + User_name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Profileresponse>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        mdilogue.cancel();
                    }

                    @Override
                    public void onNext(Profileresponse profileresponse) {
                        if (HttpsURLConnection.HTTP_OK == 200) {

                            Picasso.with(QRMoneyActivity.this).load(profileresponse.getQrContactImage()).error(R.drawable.ic_user).into(qrmoneyImg);


                        }else{


                        }
                    }



                });}

}