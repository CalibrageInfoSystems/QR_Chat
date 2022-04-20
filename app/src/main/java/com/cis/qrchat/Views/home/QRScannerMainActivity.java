package com.cis.qrchat.Views.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cis.qrchat.Model.GetrequestResp;
import com.cis.qrchat.Model.QrResponse;
import com.cis.qrchat.R;
import com.cis.qrchat.SplitMoney.SplitActivity;
import com.cis.qrchat.SplitMoney.SplitMainActivity;
import com.cis.qrchat.Views.account.Contactlist;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRMoneyActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.zxing.Result;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class QRScannerMainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    CardView qrcode;
    private int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    Toolbar toolbar;
    ZXingScannerView scannerView;
    private Subscription mSubscription;
    int langID;
    private SpotsDialog mdilogue;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");

        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_qrscanner);
        intviews();
        setviews();
        setupToolbar();
    }
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.qrscanner));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_three_dots_more_vertical);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(QRScannerMainActivity.this, HomeActivity.class);
                startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showPopup(View view) {
        Context myContext = null;
        MenuBuilder menuBuilder =new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.qr_menu_option, menuBuilder);
        if (langID == 2) {
            myContext = new ContextThemeWrapper(QRScannerMainActivity.this, R.style.menuStyle);
        }else{
            myContext = new ContextThemeWrapper(QRScannerMainActivity.this, R.style.menunewStyle);
        }

        MenuPopupHelper optionsMenu = new MenuPopupHelper(myContext, menuBuilder, view);
        optionsMenu.setForceShowIcon(true);
        optionsMenu.setGravity(Gravity.END);
        MenuCompat.setGroupDividerEnabled(menuBuilder, true);
        // Set Item Click Listener
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_sendmoney:
                        Intent qrmoneyscreen = new Intent(QRScannerMainActivity.this,sendmoney_QRScannerActivity.class);
                        startActivity(qrmoneyscreen);

                      return true;
                    case R.id.action_recivemoney:
                        Intent recivemoneyscreen = new Intent(QRScannerMainActivity.this,ReciveMainActivity.class);
                        startActivity(recivemoneyscreen);
                        // call My Buddies class or function here
                        return true;
                    case R.id.action_splitmoney:
                        Intent spiltmoneyscreen = new Intent(QRScannerMainActivity.this, SplitActivity.class);
                        startActivity(spiltmoneyscreen);
                        // call My Buddies class or function here
                        return true;

                    case R.id.action_hadiatmoney:
                        Intent mySuperIntent = new Intent(QRScannerMainActivity.this, HaditContactlist.class);

                        startActivity(mySuperIntent);
                        // call My Buddies class or function here
                        return true;
                    case R.id.action_transactions:
                        Intent transactionscreen = new Intent(QRScannerMainActivity.this,TransactionsActivity.class);
                        startActivity(transactionscreen);
                        return true;
                }
                return true;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

                MenuCompat.setGroupDividerEnabled(menuBuilder, true);

            }
        });


        // Display the menu
        optionsMenu.show();

    }
    private void intviews() {
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);
    }

    private void setviews() {

    }
    public void handleResult(Result result) {
        Log.e("===========>result.getText()",result.getText()+"");
       // Toast.makeText(this,result.getText(),Toast.LENGTH_LONG).show();

        if(result.getText().equalsIgnoreCase("https://www.kaspersky.com") ){
            finish();
            Toast.makeText(this,getResources().getString(R.string.toast),Toast.LENGTH_LONG).show();
        }
        else if(result.getText().startsWith("RM")){
            String UserID = result.getText().substring(2);
            GetRequest(UserID);
        }
        else{
            getScanQRCode(result.getText());
        }

    }


    private void GetRequest(String userID) {
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getrequestresponse(APIConstantURL.GetRequest + userID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetrequestResp>() {
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
                    public void onNext(GetrequestResp getrequestResp) {




                        if (HttpsURLConnection.HTTP_OK == 200) {

                            Log.d("============>",getrequestResp.getEndUserMessage());

                            Intent mySuperIntent = new Intent(QRScannerMainActivity.this, SendMoney.class);
                            mySuperIntent.putExtra("contactnumber",getrequestResp.getResult().getMobileNumber());
                            mySuperIntent.putExtra("name",getrequestResp.getResult().getName());
                            mySuperIntent.putExtra("WalletID",getrequestResp.getResult().getWalletId());
                            mySuperIntent.putExtra("Amount",getrequestResp.getResult().getAmount());
                            mySuperIntent.putExtra("Userid",getrequestResp.getResult().getUserId());
                            startActivity(mySuperIntent);

//                            Intent mySuperIntent = new Intent(sendmoney_QRScannerActivity.this, Contactlist.class);
//                            mySuperIntent.putExtra("contactnumber",qrResponse.getResult().getMobileNumber());
//                            mySuperIntent.putExtra("walletId",qrResponse.getResult().getWalletId());
//                            mySuperIntent.putExtra("contact",qrResponse.getEndUserMessage());
//
//                            startActivity(mySuperIntent);

                        }else{


                        }
                    }



                });}




    private void getScanQRCode(String result) {

        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getqrresponse(APIConstantURL.ScanQRCode + result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<QrResponse>() {
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
                    public void onNext(QrResponse qrResponse) {

                        if (HttpsURLConnection.HTTP_OK == 200) {

                            Log.d("============>",qrResponse.getEndUserMessage());

                            Intent mySuperIntent = new Intent(QRScannerMainActivity.this, SendMoney.class);
                            mySuperIntent.putExtra("contactnumber",qrResponse.getResult().getMobileNumber());
                            mySuperIntent.putExtra("name",qrResponse.getResult().getFullName());
                            mySuperIntent.putExtra("WalletID",qrResponse.getResult().getWalletId());
                            mySuperIntent.putExtra("Amount","");
                            mySuperIntent.putExtra("Userid",qrResponse.getResult().getUserId());
                            startActivity(mySuperIntent);

//                            Intent mySuperIntent = new Intent(sendmoney_QRScannerActivity.this, Contactlist.class);
//                            mySuperIntent.putExtra("contactnumber",qrResponse.getResult().getMobileNumber());
//                            mySuperIntent.putExtra("walletId",qrResponse.getResult().getWalletId());
//                            mySuperIntent.putExtra("contact",qrResponse.getEndUserMessage());
//
//                            startActivity(mySuperIntent);

                        }else{


                        }
                    }



                });}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            startActivity(new Intent(QRScannerMainActivity.this, sendmoney_QRScannerActivity.class));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            startActivity(new Intent(QRScannerMainActivity.this, QRScannerActivity.class));
//        }
//    }
}