package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Model.GetWalletBalanceResponse;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.HomeActivity;
import com.cis.qrchat.Views.home.ReciveMainActivity;
import com.cis.qrchat.Views.home.SendMoney;
import com.cis.qrchat.Views.home.TransactionsActivity;
import com.cis.qrchat.Views.home.sendmoney_QRScannerActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;

import java.io.IOException;
import java.text.DecimalFormat;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class QRPayAccount extends AppCompatActivity {

    private Toolbar toolbar;
    LinearLayout sendmoneylenear, receivelinear;
    TextView walletbalance;

    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    SwipeRefreshLayout swipeLyt;
    LinearLayout qrmoney, topup, transactions;

    int langID;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_pay_account);


        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();


        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
            } else {
                updateResources(this, "en-US");
            }

//        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
//                .setContext(this)
//                .setTheme(R.style.Custom)
//                .build();


//        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
//        if (langID == 2)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                updateResources(this, "ar");
//            } else {
//                updateResources(this, "en-US");
//            }




     sendmoneylenear = findViewById(R.id.sendmoneylenear);
     receivelinear = findViewById(R.id.receivelinear);
       walletbalance = findViewById(R.id.availablebalnce);

        swipeLyt =  findViewById(R.id.swipeLyt);

//        moneylyt = findViewById(R.id.moneylyt);
//        walletlyt = findViewById(R.id.walletlyt);
//        walletbalance = findViewById(R.id.mainwalletBalance);
        swipeLyt =  findViewById(R.id.swipeLyt);
        qrmoney = findViewById(R.id.qrmoneylinear);
        topup = findViewById(R.id.topuplinear);
        transactions = findViewById(R.id.showTransactionslinear);


        swipeLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getwalletBalance();

                swipeLyt.setRefreshing(false);
            }
        });


//        swipeLyt =  findViewById(R.id.swipeLyt);
//
//        swipeLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getwalletBalance();
//                swipeLyt.setRefreshing(false);
//            }
//        });

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moneyscreen = new Intent(QRPayAccount.this, QRWalletActivity.class);
                startActivity(moneyscreen);
            }
        });

        qrmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moneyscreen = new Intent(QRPayAccount.this, QRMoneyActivity.class);
                startActivity(moneyscreen);
            }
        });

        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moneyscreen = new Intent(QRPayAccount.this, TransactionsActivity.class);
                startActivity(moneyscreen);
            }
        });




        sendmoneylenear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moneyscreen = new Intent(QRPayAccount.this, sendmoney_QRScannerActivity.class);
                startActivity(moneyscreen);
            }
        });

        receivelinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moneyscreen = new Intent(QRPayAccount.this, ReciveMainActivity.class);
                startActivity(moneyscreen);
            }
        });
//



//        walletlyt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent walletscreen = new Intent(QRPayAccount.this, QRWalletActivity.class);
//                startActivity(walletscreen);
//            }
//        });
//


        settoolbar();
        getwalletBalance();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
              finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getwalletBalance();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrpayaccount));
        setSupportActionBar(toolbar);


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

    private void getwalletBalance() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getwalletBalance(APIConstantURL.GetWalletBalance + User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetWalletBalanceResponse>() {
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
                    public void onNext(GetWalletBalanceResponse walletBalanceResponse) {

                        Log.d("WalletBalanceSuccess", "Success");
                        Log.d("WalletBalanceAmount", walletBalanceResponse.getResult().getWalletBalance() + "");
                        Log.d("WalletBalanceStatus", walletBalanceResponse.getIsSuccess() + "");

                        if (walletBalanceResponse.getIsSuccess()){

                            String balance = walletBalanceResponse.getResult().getWalletBalance().toString();
                            Log.d("balancebalance", balance + "");
                            double d= Double.parseDouble(balance);
                            Log.d("DoubleBalance", balance + "");
                            String ar = String.format("%.2f", d);
                            walletbalance.setText(ar + " " + getResources().getString(R.string.qar));

                        }else{


                        }
                    }

                });}

//
//        settoolbar();
//        getwalletBalance();
    }

//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    private void settoolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.qrpayaccount));
//        setSupportActionBar(toolbar);
//
//
//        if (langID == 2)
//            {
//                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left_rtl);
//
//            } else {
//                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
//            }
//
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

//    private void getwalletBalance() {
//        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
//        Log.e("============>",User_id);
//        mdilogue.show();
//        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
//        mSubscription = service.getwalletBalance(APIConstantURL.GetWalletBalance + User_id)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetWalletBalanceResponse>() {
//                    @Override
//                    public void onCompleted() {
//                        mdilogue.dismiss();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof HttpException) {
//                            ((HttpException) e).code();
//                            ((HttpException) e).message();
//                            ((HttpException) e).response().errorBody();
//                            try {
//                                ((HttpException) e).response().errorBody().string();
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                            }
//                            e.printStackTrace();
//                        }
//                        mdilogue.cancel();
//                    }
//
//                    @Override
//                    public void onNext(GetWalletBalanceResponse walletBalanceResponse) {
//
//                        Log.d("WalletBalanceSuccess", "Success");
//                        Log.d("WalletBalanceAmount", walletBalanceResponse.getResult().getWalletBalance() + "");
//                        Log.d("WalletBalanceStatus", walletBalanceResponse.getIsSuccess() + "");
//
//                        if (walletBalanceResponse.getIsSuccess()){
//
//                            walletbalance.setText(walletBalanceResponse.getResult().getWalletBalance().toString());
//
//                        }else{
//
//
//                        }
//                    }
//
//
//                });}


