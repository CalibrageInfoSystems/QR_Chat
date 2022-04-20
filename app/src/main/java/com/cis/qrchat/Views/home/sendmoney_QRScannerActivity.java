package com.cis.qrchat.Views.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Model.GetrequestResp;
import com.cis.qrchat.Model.Profileresponse;
import com.cis.qrchat.Model.QrResponse;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.Contactlist;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class sendmoney_QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    Toolbar toolbar;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    View bottomSheetLayout;
    TextView phonenumber;
    int langID;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.sendmoney_activity_qrscanner);
        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");

intviews();
setviews();

        setupToolbar();
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        scannerView = new ZXingScannerView(this);
        contentFrame.addView(scannerView);
    }

    private void setviews() {
        phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sendmoney_QRScannerActivity.this, GetContactlist.class));
                // Toast.makeText(getApplicationContext(), "Ok button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void showbottomDialog() {
//
//        bottomSheetLayout  = getLayoutInflater().inflate(R.layout.bottomsheet_dialog, null);
//        BottomSheetDialog   mBottomSheetDialog = new BottomSheetDialog(this);
//        mBottomSheetDialog.setContentView(bottomSheetLayout);
//        mBottomSheetDialog.show();
//        bottomSheetLayout.setBackgroundResource(R.drawable.white_bg);
//        (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBottomSheetDialog.dismiss();
//            }
//        });
//        (bottomSheetLayout.findViewById(R.id.phonenumber)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(sendmoney_QRScannerActivity.this, GetContactlist.class));
//               // Toast.makeText(getApplicationContext(), "Ok button clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }

    private void intviews() {

        phonenumber = findViewById(R.id.phonenumber);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.Sendmoney));
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



    @Override
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

   //     LoginActivity.farmerId.setText(result.getText());
  //    finish();

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

                            Intent mySuperIntent = new Intent(sendmoney_QRScannerActivity.this, SendMoney.class);
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

                            Intent mySuperIntent = new Intent(sendmoney_QRScannerActivity.this, SendMoney.class);
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
