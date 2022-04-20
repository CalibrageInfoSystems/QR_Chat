package com.cis.qrchat.Views.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.CallListAdapter;
import com.cis.qrchat.Adapters.ChatListAdapter;
import com.cis.qrchat.Adapters.ContactListAdapter;
import com.cis.qrchat.Chat.CustomChannelActivity;
import com.cis.qrchat.Chat.Groupchat.GroupChannelActivity;
import com.cis.qrchat.Model.Call_data;
import com.cis.qrchat.Model.FindContactResp;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.HomeActivity;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;
import com.sendbird.uikit.activities.ChannelListActivity;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Contactlist extends AppCompatActivity implements ContactListAdapter.ContactsAdapterListener {
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    TextView noRecords;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ContactListAdapter contactListAdapter;
    private List<Call_data> call_List = new ArrayList<>();
    FloatingActionButton addfloat;
    String phonenumber;
    Dialog _dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist);
        intviews();
        setviews();
        settoolbar();
    }

    private void intviews() {
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        noRecords = (TextView)findViewById(R.id.no_data);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
      //  addfloat = findViewById(R.id.addfloat);

        // white background notification bar
     whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());




    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int flags = view.getSystemUiVisibility();
//            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            view.setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }


    private void setviews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phonenumber = extras.getString("contactnumber");

        }

//        addfloat.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(Contactlist.this, "Person Added", Toast.LENGTH_SHORT).show();
//                        AdduserPopup();
//                    }
//                });
        GetUserContacts();
    }

    private void AdduserPopup() {

        _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setContentView(R.layout.add_popup);
        _dialog.show();

    EditText phone_number =  _dialog.findViewById(R.id.nickname);
        phone_number.setText(phonenumber);
        EditText nickname = _dialog.findViewById(R.id.phonenumber);
            ((Button) _dialog.findViewById(R.id.saveBtn))
                    .setOnClickListener(new View.OnClickListener() {

                        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                        public void onClick(View arg0) {
                            Toast.makeText(getApplicationContext(),
                                    nickname.getText().toString(),Toast.LENGTH_LONG).show();

                            _dialog.dismiss();
                        }
                    });

            ((Button) _dialog.findViewById(R.id.cancelBtn))
                    .setOnClickListener(new View.OnClickListener() {

                        public void onClick(View arg0) {
                            _dialog.dismiss();
                        }
                    });
        }


    private void GetUserContacts() {
        mdilogue.show();
        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getcontactlist(APIConstantURL.GetUserContacts+User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Getcontactlist>() {
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
                     showDialog(Contactlist.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(Getcontactlist getcontactlist) {


                        if(getcontactlist.getListResult()!=null){
                            recyclerView.setVisibility(View.VISIBLE);
                            noRecords.setVisibility(View.GONE);
                            contactListAdapter = new ContactListAdapter(Contactlist.this, getcontactlist.getListResult(), Contactlist.this);
                            recyclerView.setAdapter(contactListAdapter);
                    }else{
                            noRecords.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }



                }});}

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

    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.addfriends));
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
//                Intent intent = new Intent(Contactlist.this, HomeActivity.class);
//                startActivity(intent);
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

    @Override
    public void onContactSelected(Getcontactlist.ListResult contact) {
        List<String> userIds = new ArrayList<>();
        Log.e("============>ContactNumber",contact.getContactNumber());
        userIds.add(contact.getContactNumber());
Log.e("===========>Contactids",userIds.get(0) +"============"+ userIds.size());
        createGroupChannel(userIds,true );



    }

    private void createGroupChannel(List<String> userIds, boolean distinct) {
        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    if (e != null) {
                        // Error!
                        return;
                    }

                    Intent intent = new Intent(Contactlist.this, GroupChannelActivity.class);
                    intent.putExtra("groupChannelUrl", groupChannel.getUrl());
                    startActivity(intent);
                }
            });
    }
}
