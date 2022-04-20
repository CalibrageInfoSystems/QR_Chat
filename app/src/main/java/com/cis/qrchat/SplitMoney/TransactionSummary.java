package com.cis.qrchat.SplitMoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cis.qrchat.Adapters.AddContactsNewAdapter;
import com.cis.qrchat.Model.GetGroupFundTransactions;
import com.cis.qrchat.Model.GetGroups;
import com.cis.qrchat.Model.GetTransactionsByFundId;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class TransactionSummary extends AppCompatActivity implements TransactionSummaryAdapter.TransactionSummaryAdapterListner {


    TextView groupName,transactionDate, transactionAmount, transactionName, groupcount, splitmode;
    RecyclerView rcv_summary;
    TransactionSummaryAdapter transactionSummaryAdapter;
    String convertDate;

    public static final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT2 = "dd-MM-yyyy";

    int amount;
    String date;
    String transname, Splitmethod;
    String grouPid;

    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    int fundId;
    private Toolbar toolbar;
    int Count;
Button btn_ok;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_summary);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fundId = extras.getInt("PassFundId");
            date = extras.getString("PassDate");
            grouPid = extras.getString("PassGroupId");
            amount = extras.getInt("PassAmount");
            transname = extras.getString("PassTransactionName");
            Splitmethod = extras.getString("splitmethod");


            Log.e("FroScreen", fundId + "");
            Log.e("FromScreenamount", amount + "");
            Log.e("FromScreentransname", transname + "");

        }



        settoolbar();
        GetTransactionByFundId();
        GetUserGroups();


        groupName = findViewById(R.id.groupNamed);
        transactionDate = findViewById(R.id.transactiondate);
        transactionAmount = findViewById(R.id.transactionAmount);
        transactionName = findViewById(R.id.transactionName);
        groupcount = findViewById(R.id.groupcount);
        splitmode = findViewById(R.id.modeofsplit);
        rcv_summary = findViewById(R.id.rcv_summary);
        btn_ok = findViewById(R.id.btn_ok);

        try {
            convertDate = formatDateFromDateString(DATE_FORMAT1, DATE_FORMAT2, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        transactionDate.setText(convertDate);
        transactionAmount.setText(amount + " " + getResources().getString(R.string.qar));
        transactionName.setText(transname + "");
        splitmode.setText(Splitmethod);


        rcv_summary.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        transactionSummaryAdapter = new GroupTransactionAdapter(TransactionSummary.this, getGroupfundTransactions.getListResult(), TransactionSummary.this);
//        rcv_summary.setAdapter(transactionSummaryAdapter);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionSummary.this, SplitActivity.class);
                startActivity(intent);
            }
        });

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
                groupName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                transactionName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);



            } else {
                updateResources(this, "en-US");
            }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrwallet));
        setSupportActionBar(toolbar);

//        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
//            if (langID == 2)
//            {
//                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left_rtl);
//
//            } else {
//                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
//            }

       // getSupportActionBar().setDisplayShowHomeEnabled(true);
    //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(TransactionSummary.this, SplitActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    private void GetTransactionByFundId() {
        mdilogue.show();
        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getTransactionByFundIdPage(APIConstantURL.getTransactionsbyFundId+fundId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetTransactionsByFundId>() {
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
                        //  showDialog(this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(GetTransactionsByFundId getTransactionsByFundId) {

                        mdilogue.dismiss();

                        groupcount.setText(getTransactionsByFundId.getAffectedRecords() + " " + getResources().getString(R.string.members));

                        transactionSummaryAdapter = new TransactionSummaryAdapter(TransactionSummary.this, getTransactionsByFundId.getListResult(), TransactionSummary.this);
                        rcv_summary.setAdapter(transactionSummaryAdapter);

                    }});}


    private void GetUserGroups() {
        mdilogue.show();
        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getGroupspage(APIConstantURL.getGroups+User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetGroups>() {
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
                        //  showDialog(this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(GetGroups getGroupsresponse) {


                        if(getGroupsresponse.getListResult()!=null){

                            for (int i =0; i<getGroupsresponse.getListResult().size(); i++ ){

                                if(getGroupsresponse.getListResult().get(i).getId().contains(grouPid)){

                                    groupName.setText(getGroupsresponse.getListResult().get(i).getName());
                                }
                            }

                        }

                    }});}

    @Override
    public void onitemselected(GetTransactionsByFundId.ListResult items) {

    }

    public static String formatDateFromDateString(String inputDateFormat, String outputDateFormat, String inputDate) throws ParseException {
        Date mParsedDate;
        String mOutputDateString;
        SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputDateFormat, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputDateFormat, java.util.Locale.getDefault());
        mParsedDate = mInputDateFormat.parse(inputDate);
        mOutputDateString = mOutputDateFormat.format(mParsedDate);
        return mOutputDateString;

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransactionSummary.this, SplitActivity.class);
        startActivity(intent);
    }
}
