package com.cis.qrchat.Views.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.cis.qrchat.Adapters.TransactionHistoryAdapter;
import com.cis.qrchat.Model.GetPassbookDetails;
import com.cis.qrchat.Model.GetPassbookObject;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import lib.kingja.switchbutton.SwitchMultiButton;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransactionsActivity extends AppCompatActivity {
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TransactionHistoryAdapter transactionListAdapter;
    private List<GetPassbookDetails.ListResult> transactionList = new ArrayList<>();
    SwitchMultiButton sw_paymentMode;
    List<String> datelist = new ArrayList<String>();
LinearLayout text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        intview();
        setviews();
        settoolbar();
    }

    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.transactionshistory));

        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2) {
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
//                Intent intent = new Intent(SendMoney.this, HomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    private void intview() {

        sw_paymentMode = (SwitchMultiButton) findViewById(R.id.sw_paymentMode);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        recyclerView = findViewById(R.id.recyclerView);
        text = findViewById(R.id.text);

    }

    private void setviews() {

        sw_paymentMode.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {

                if (position == 0  )  {
                  //  transactionListAdapter.clearAllDataa();
                    gettransactiondetails();

                } else if (position == 1) {

                    gettransactiondetailss(22);

                } else if (position == 2) {

                    gettransactiondetailss(21);
                }


            }


        });

        sw_paymentMode.setSelectedTab(0);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
      //  recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());






    }
    private void gettransactiondetailss(int i) {
        transactionList.clear();
        mdilogue.show();
        JsonObject object = passbookobjectt(i);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postpassbookdetails(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPassbookDetails>() {

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
                        Log.e("============", ((HttpException) e).code()+"");
                        mdilogue.dismiss();
                        transactionListAdapter.clearAllDataa();
                        //showDialog(RegisterActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(GetPassbookDetails getTransactionsDetails) {


                        if(HttpsURLConnection.HTTP_OK == 200) {
                            if(getTransactionsDetails.getListResult()!=null){
//
                                for(int k =0; k <getTransactionsDetails.getListResult().size();k++)
                                {
                                    transactionList =getTransactionsDetails.getListResult();
//                                    Log.d("Analysis============>",datelist.get(k)+"");
//                                 //   noRecords.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    text.setVisibility(View.GONE);
                                    transactionListAdapter = new TransactionHistoryAdapter(TransactionsActivity.this, transactionList);
                                    recyclerView.setAdapter(transactionListAdapter);



                                }
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                text.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
    private void gettransactiondetails() {
        transactionList.clear();
        mdilogue.show();
        JsonObject object = passbookobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postpassbookdetails(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPassbookDetails>() {

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
                        Log.e("============", ((HttpException) e).code()+"");
                        mdilogue.dismiss();
                        transactionListAdapter.clearAllDataa();
                        //showDialog(RegisterActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(GetPassbookDetails getTransactionsDetails) {


                        if(HttpsURLConnection.HTTP_OK == 200) {
                            if(getTransactionsDetails.getListResult()!=null){
//
                                for(int k =0; k <getTransactionsDetails.getListResult().size();k++)
                                {
                                    transactionList =getTransactionsDetails.getListResult();
//                                    Log.d("Analysis============>",datelist.get(k)+"");
//                                 //   noRecords.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    text.setVisibility(View.GONE);

                                    transactionListAdapter = new TransactionHistoryAdapter(TransactionsActivity.this, getTransactionsDetails.getListResult());
                                    recyclerView.setAdapter(transactionListAdapter);



                                }
                            } else{
                                recyclerView.setVisibility(View.GONE);
                                text.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }



    private JsonObject passbookobject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        String wallet_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("WalletId");


        Log.e("============>", User_id);


        GetPassbookObject requestModel = new GetPassbookObject();
        requestModel.setTransactionTypeId(null);
        requestModel.setWalletId(wallet_id);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
    private JsonObject passbookobjectt(int i) {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        String wallet_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("WalletId");


        Log.e("============>", User_id);


        GetPassbookObject requestModel = new GetPassbookObject();
        requestModel.setTransactionTypeId(i);
        requestModel.setWalletId(wallet_id);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
}