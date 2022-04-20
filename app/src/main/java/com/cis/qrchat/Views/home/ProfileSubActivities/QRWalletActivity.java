package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.savedCardAdapter;
import com.cis.qrchat.Model.AddMoneytoUserWalletRequest;
import com.cis.qrchat.Model.AddMoneytoUserWalletResponse;
import com.cis.qrchat.Model.GetCards;
import com.cis.qrchat.Model.GetWalletBalanceResponse;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class QRWalletActivity extends AppCompatActivity implements savedCardAdapter.AdapterListener {

    private Toolbar toolbar;
    Button topup;

    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private Dialog _dialogg;
    RecyclerView savedcards;
    private savedCardAdapter adapter;
    String cardid, mpinNumber;
    EditText amount;
    LinearLayout mpinlayout;
    String mpin;
    String wallet_balance;
    TextView walletBalance, nocards;
    SwipeRefreshLayout swipeLyt;
    ImageView managecardsimg, transactionsimg, privacypolicyimg, helpimg;
    private ArrayList<GetCards.ListResult> cardslist =new ArrayList<>();

    String EnterAmount ;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_wallet);

        init();
        setViews();
        getwalletBalance();
        settoolbar();
    }

    private void init() {
        topup = findViewById(R.id.topup);
        walletBalance = findViewById(R.id.walletBalance);
        swipeLyt =  findViewById(R.id.swipeLyt);

        managecardsimg = findViewById(R.id.managecardsarrow);
        transactionsimg = findViewById(R.id.transactionhistoryimgarrow);
        privacypolicyimg = findViewById(R.id.paymentprivacyarrow);
        helpimg = findViewById(R.id.helpcenterarrow);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setViews() {

        final int langID = SharedPrefsData.getInstance(QRWalletActivity.this).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(QRWalletActivity.this, "ar");
            managecardsimg.setRotation(180);
            transactionsimg.setRotation(180);
            privacypolicyimg.setRotation(180);
            helpimg.setRotation(180);


        }
        else
            updateResources(QRWalletActivity.this, "en-US");

        swipeLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getwalletBalance();
                swipeLyt.setRefreshing(false);
            }
        });

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        wallet_balance = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("walletBalance");

        Log.d("wallet_balance", wallet_balance + "");


        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEnterAmountDetails(QRWalletActivity.this);

            }
        });
    }


    public void AddMoneytoUserWallet() {
        //cardid = "";
        mdilogue.show();
        JsonObject object = AddMoneytoUserWalletObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postAddMoneytoUserWallet(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddMoneytoUserWalletResponse>() {

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
                        //showDialog(RegisterActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(AddMoneytoUserWalletResponse addMoneytoUserWalletResponse) {

                        if(HttpsURLConnection.HTTP_OK == 200) {

                            getwalletBalance();
                            cardid = "";
                        }
                    }
                });


    }

    private JsonObject AddMoneytoUserWalletObject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        String wallet_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("WalletId");

        Log.e("============>",User_id);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        AddMoneytoUserWalletRequest requestModel = new AddMoneytoUserWalletRequest();
        requestModel.setUserId(User_id);
        requestModel.setCardId(cardid);
        requestModel.setGroupId(null);
        requestModel.setId(0);
        requestModel.setWalletId(wallet_id);
        requestModel.setAmount(Integer.parseInt(amount.getText().toString()));
        requestModel.setTransactionTypeId(20);
        requestModel.setReasonTypeId(13);
        requestModel.setIsActive(true);
        requestModel.setCurrency("INR");

        return new Gson().toJsonTree(requestModel).getAsJsonObject();}


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrwallet));
        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
             //   holder.rightarrow.setRotation(180);

            } else {
                updateResources(this, "en-US");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
            }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showEnterAmountDetails(Activity activity) {
      //  cardslist.clear();

        _dialogg = new Dialog(activity);
        _dialogg = new Dialog(activity);
        _dialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialogg.setCancelable(true);
        _dialogg.setContentView(R.layout.amountdetails);
        _dialogg.show();


        Button submitAdmountDetailsBtn, cancelAdmountDetailsBtn;
        submitAdmountDetailsBtn = _dialogg.findViewById(R.id.submitAmountdetailsbtn);
        cancelAdmountDetailsBtn = _dialogg.findViewById(R.id.cancelAmountdetailsBtn);

        amount = _dialogg.findViewById(R.id.amount_et);
        savedcards =  _dialogg.findViewById(R.id.savedcards);
        nocards =  _dialogg.findViewById(R.id.nocardavailablee);
        Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
        final Runnable[] runnable = new Runnable[1];
//        Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
//        Runnable workRunnable;
//        @Override public void afterTextChanged(Editable s) {
//            handler.removeCallbacks(workRunnable);
//            workRunnable = () -> doSmth(s.toString());
//            handler.postDelayed(workRunnable, 500 /*delay*/);
//        }
//
//        private final void doSmth(String str) {
//            //
//        }

        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

        }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() <= 1  ){
                    EnterAmount = s.toString();
                    getcardslist();
                    Log.e("EnterAmount===",EnterAmount);

            }
            else if(s.length()   ==0 ){
                    showDialog(QRWalletActivity.this, getResources().getString(R.string.enterAmount));
            }}
        });
//        mpinlayout = _dialogg.findViewById(R.id.mpinlayout);
//
//        if(!TextUtils.isEmpty(cardid)){
//
//            mpinlayout.setVisibility(View.VISIBLE);
//        }else{
//            mpinlayout.setVisibility(View.GONE);
//
//        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        savedcards.setLayoutManager(mLayoutManager);
//        savedcards.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
//        savedcards.setItemAnimator(new DefaultItemAnimator());


//        submitAdmountDetailsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (TextUtils.isEmpty(amount.getText().toString())) {
//                    showDialog(QRWalletActivity.this, getResources().getString(R.string.enterAmount));
//                }else if(cardid == null || cardid.trim().equals("null") || cardid.trim()
//                        .length() <= 0){
//                    showDialog(QRWalletActivity.this, getResources().getString(R.string.pleaseselectCard));
//
//                }
//
////
////                }
//                else{
//                    AddMoneytoUserWallet();
//                  // startActivity(getIntent());
//                    _dialogg.dismiss();
//                }
//            }
//        });

        cancelAdmountDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialogg.dismiss();
            }
        });
    }


    private void getcardslist() {

        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getcards(APIConstantURL.getCard + User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetCards>() {
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
                        // showDialog(SignUpActicity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(GetCards getCards) {
                        mdilogue.dismiss();
                        if(getCards.getListResult()!=null){

                            nocards.setVisibility(View.GONE);
                            savedcards.setVisibility(View.VISIBLE);
                            adapter = new savedCardAdapter(QRWalletActivity.this, getCards.getListResult(), QRWalletActivity.this);
                            savedcards.setAdapter(adapter);
                        }else{

                            nocards.setVisibility(View.VISIBLE);
                            savedcards.setVisibility(View.GONE);
                        }

                    }



                });}

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
                            walletBalance.setText(ar + " " + getResources().getString(R.string.qar));

                            Log.d("WalletBalanceAPI", walletBalanceResponse.getResult().getWalletBalance() + "");
                            Log.d("WalletBalanceAPI", walletBalance.getText().toString() + "");

                        }else{

                            Log.d("Not Sucess", "Why Not");

                        }
                    }


                });}



    @Override
    public void onCardSelected(GetCards.ListResult contact, String s) {

        cardid = contact.getId();

        Log.e("===========cardid",cardid);

        if (TextUtils.isEmpty(amount.getText().toString())) {
            showDialog(QRWalletActivity.this, getResources().getString(R.string.enterAmount));
        }else if(cardid == null || cardid.trim().equals("null") || cardid.trim()
                .length() <= 0){
            showDialog(QRWalletActivity.this, getResources().getString(R.string.pleaseselectCard));

        }

//
//                }
        else{
            AddMoneytoUserWallet();
            // startActivity(getIntent());
            _dialogg.dismiss();
        }

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