package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.AddedbankdetailsAdapter;
import com.cis.qrchat.Model.DeleteCard;
import com.cis.qrchat.Model.GetCards;
import com.cis.qrchat.Model.GetCardtype;
import com.cis.qrchat.R;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class ShowCards extends AppCompatActivity implements AddedbankdetailsAdapter.AdapterListener{
RecyclerView Cardslist;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private AddedbankdetailsAdapter adapter;
    TextView addnewcard, nocards;
    Toolbar toolbar;
    int langID;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");

        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_show_cards);
        intviews();
        setviews();
        settoolbar();
    }
    private void intviews() {
        Cardslist = findViewById(R.id.Cardslist);
        nocards = findViewById(R.id.nocards);
        nocards.setVisibility(View.GONE);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        addnewcard = findViewById(R.id.addnewcard);
    }
    private void setviews() {
        addnewcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivemoneyscreen =  new Intent(ShowCards.this, AddCardDetails.class);
                startActivity(receivemoneyscreen);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        Cardslist.setLayoutManager(mLayoutManager);
        Cardslist.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        Cardslist.setItemAnimator(new DefaultItemAnimator());
        getcardslist();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
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
    protected void onResume() {
        super.onResume();
        getcardslist();
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
                            Cardslist.setVisibility(View.VISIBLE);
                            nocards.setVisibility(View.GONE);
                            adapter = new AddedbankdetailsAdapter(ShowCards.this, getCards.getListResult(), ShowCards.this);
                            Cardslist.setAdapter(adapter);
                        }
                        else {
                            Cardslist.setVisibility(View.GONE);
                            nocards.setVisibility(View.VISIBLE);
                        }
                    }

                    });}

    @Override
    public void onContactSelected(GetCards.ListResult contact) {
        deleteCard(contact.getId());
    }

    private void deleteCard(String selectedItemID) {
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.deletecards(APIConstantURL.DeleteCard + selectedItemID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<DeleteCard>() {
                    @Override
                    public void onCompleted() {
                        //   mdilogue.dismiss();
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
                        //  mdilogue.cancel();
                        // showDialog(SignUpActicity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(DeleteCard deleteCard) {
                        if(deleteCard != null){
                            Toast.makeText(ShowCards.this, deleteCard.getEndUserMessage(), Toast.LENGTH_LONG).show();
                            getcardslist();
                        }


                    }});}
}
