package com.cis.qrchat.Views.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cis.qrchat.Model.GetrequestResp;
import com.cis.qrchat.Model.QrResponse;
import com.cis.qrchat.R;
import com.cis.qrchat.SplitMoney.SplitMainActivity;
import com.cis.qrchat.Views.account.AddFriendsActivity;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRMoneyActivity;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRPayAccount;
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

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    Toolbar toolbar;
    private int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_qrscanner);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        setupToolbar();

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.qrscanner));
        setSupportActionBar(toolbar);
        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2){
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

//    private void showPopup(View view) {
//
//        MenuBuilder menuBuilder =new MenuBuilder(this);
//        MenuInflater inflater = new MenuInflater(this);
//        inflater.inflate(R.menu.qr_menu_option, menuBuilder);
//        MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, view);
//        optionsMenu.setForceShowIcon(true);
//
//        // Set Item Click Listener
//        menuBuilder.setCallback(new MenuBuilder.Callback() {
//            @Override
//            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_sendmoney:
//                        Intent sendmoneyscreen = new Intent(QRScannerActivity.this, sendmoneyMainActivity.class);
//                        startActivity(sendmoneyscreen);
//                        // call My Buddies class or function here
//                        return true;
//
////                        return true;
//                    case R.id.action_recivemoney:
//                        // call Guest class or function here
//                        return true;
//                    case R.id.action_splitmoney:
//                        Intent spiltmoneyscreen = new Intent(QRScannerActivity.this, SplitMainActivity.class);
//                        startActivity(spiltmoneyscreen);
//                        // call My Buddies class or function here
//                        return true;
//
//                    case R.id.action_hadiatmoney:
//                        // call My Buddies class or function here
//                        return true;
//                    case R.id.action_transactions:
//                        // call My Buddies class or function here
//                        return true;
//                }
//                return true;
//            }
//
//            @Override
//            public void onMenuModeChange(MenuBuilder menu) {}
//        });
//
//
//        // Display the menu
//        optionsMenu.show();
//
//    }


    @Override
    public void handleResult(Result result) {
        Log.e("===========>result.getText()",result.getText()+"");


        if (!result.getText().isEmpty() && result.getText()!=null){
            Intent moneyscreen = new Intent(QRScannerActivity.this, AddFriendsActivity.class);
            moneyscreen.putExtra("QRDATA",result.getText());
            startActivity(moneyscreen);
        }

//        LoginActivity.farmerId.setText(result.getText());
        finish();


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

                            Intent mySuperIntent = new Intent(QRScannerActivity.this, SendMoney.class);
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

                            Intent mySuperIntent = new Intent(QRScannerActivity.this, SendMoney.class);
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
            startActivity(new Intent(QRScannerActivity.this, sendmoney_QRScannerActivity.class));
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
}
