package com.cis.qrchat.Chat.Groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.cis.qrchat.Model.AddUserstoGroupResponse;
import com.cis.qrchat.Model.GetPassbookObject;
import com.cis.qrchat.Model.GetUserdetails;
import com.cis.qrchat.Model.GetUserinfo;

import com.cis.qrchat.Chat.call.CallService;
import com.cis.qrchat.Chat.utils.PrefUtilsCall;

import com.cis.qrchat.Model.Searchobject;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.Contactlist;
import com.cis.qrchat.Views.account.ViewContactdetails;
import com.cis.qrchat.Views.home.QRScannerMainActivity;
import com.cis.qrchat.Views.home.SearchContactlist;
import com.cis.qrchat.Views.home.SendMoney;

import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBirdException;
import com.squareup.picasso.Picasso;

import static com.cis.qrchat.common.CommonUtil.updateResources;


public class GroupChannelActivity extends AppCompatActivity {
TextView title_;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
ImageView vediocall,voicecall,menuicon,backImg;
GroupChannel mChannel;
    CircleImageView profile;
    String channelUrl;
    int langID;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        setContentView(R.layout.activity_group_channel);

        intviews();
        setviews();
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
        title_ = (TextView )findViewById(R.id.title) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


         channelUrl = getIntent().getStringExtra("groupChannelUrl");

        if(channelUrl != null) {
            // If started from notification
            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        GroupChannel.getChannel(channelUrl, new GroupChannel.GroupChannelGetHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                mChannel = groupChannel;
                if(mChannel.getMembers().size() >0)
                {
                    String Id= null;
                    for (Member m :mChannel.getMembers())
                    {
                        if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(GroupChannelActivity.this)))
                        {
                            Id = m.getUserId();
                            searchphonenumber(Id);

                        }
                    }
//
                }

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void intviews() {
        menuicon = findViewById(R.id.menuicon);

        vediocall = findViewById(R.id.voicecall_btn);
        voicecall = findViewById(R.id.vediocall_btn);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        profile = findViewById(R.id.profile);
        backImg = findViewById(R.id.back);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
               backImg.setRotation(180);
            }

            else
                updateResources(this, "en-US");

    }
    private void setviews() {

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
        vediocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeVoiceCall();
            }
        });
        voicecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeVodeoCall();
            }
        });


    }

    private void searchphonenumber(String id) {
        mdilogue.show();
        JsonObject object = addsearchObject(id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getuseinfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserinfo>() {
                    @Override
                    public void onCompleted() {

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
                    public void onNext(GetUserinfo getUserinfo) {

                        if (HttpsURLConnection.HTTP_OK == 200) {
                            mdilogue.dismiss();

                            String User_id =getUserinfo.getResult().get(0).getId();
                            getuserdetails(User_id);
                            profile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent transactions = new Intent(GroupChannelActivity.this, ViewContactdetails.class);
                                    transactions.putExtra("useid",getUserinfo.getResult().get(0).getId());
                                    transactions.putExtra("Phonenumber",getUserinfo.getResult().get(0).getPhoneNumber());
                                    transactions.putExtra("id",id);
                                    startActivity(transactions);
                                }
                            });

//

                        }else{


                        }
                    }
                });}

    private void getuserdetails(String uSerid) {
        {
            mdilogue.show();
            ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
            mSubscription = service.getuserdetails(APIConstantURL.getuserdetails + uSerid)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserdetails>() {
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
                        public void onNext(GetUserdetails getUserdetails) {


                            if (HttpsURLConnection.HTTP_OK == 200) {

                                if(getUserdetails.getFileUrl()== null){
                                    Picasso.with(GroupChannelActivity.this).load(R.drawable.ic_user).error(R.drawable.ic_user).into(profile);
                                }else{
                                    Picasso.with(GroupChannelActivity.this).load(getUserdetails.getFileUrl()).error(R.drawable.ic_user).into(profile);}
//                            Glide.with(myprofile.this)
//                                    .load(getUserdetails.getFileUrl())
//
//                                    .apply(RequestOptions.circleCropTransform())
//                                    .into(userImg);
                            }else{


                            }
                        }



                    });}

    }




    private void makeVoiceCall() {
//        Log.d(LOG_TAG,"===> analysis <=====> voice call Caller Id:"+mChannel.getMembers().get(1).getUserId());
        if(mChannel.getMembers().size() >1)
        {
            String Id= null;
            for (Member m :mChannel.getMembers())
            {
                if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(GroupChannelActivity.this)))
                {
                    Id = m.getUserId();
                }
            }
            if(!android.text.TextUtils.isEmpty(Id))
            {
                CallService.dial(GroupChannelActivity.this,Id,false);
            }else{
                Toast.makeText(GroupChannelActivity.this, "Unable to call", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(GroupChannelActivity.this, "Unable to call", Toast.LENGTH_SHORT).show();
        }
    }
    private void makeVodeoCall() {

        if(mChannel.getMembers().size() >1)
        {
            String Id= null;
            for (Member m :mChannel.getMembers())
            {
                if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(GroupChannelActivity.this)))
                {

                    Id = m.getUserId();
                }
            }
            if(!android.text.TextUtils.isEmpty(Id))
            {

                CallService.dial(GroupChannelActivity.this,Id,true);
            }else{
                Toast.makeText(GroupChannelActivity.this, "Unable to call", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(GroupChannelActivity.this, "Unable to call", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopup(View view) {
        Context myContext = null;
        MenuBuilder menuBuilder =new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.chat_detail_menu_option, menuBuilder);

        if (langID == 2) {
            myContext = new ContextThemeWrapper(GroupChannelActivity.this, R.style.menuStyle);
        }else{
            myContext = new ContextThemeWrapper(GroupChannelActivity.this, R.style.menunewStyle);
        }

        MenuPopupHelper optionsMenu = new MenuPopupHelper(myContext, menuBuilder, view);

        optionsMenu.setForceShowIcon(true);
        MenuCompat.setGroupDividerEnabled(menuBuilder, true);
        // Set Item Click Listener
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_contacts:
                        if(mChannel.getMembers().size() >1)
                        {
                            String Id= null;
                            for (Member m :mChannel.getMembers())
                            {
                                if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(GroupChannelActivity.this)))
                                {
                                    Id = m.getUserId();
                                    gettransactiondetailss(Id);

                                }
                            }
//                            if(!android.text.TextUtils.isEmpty(Id))
//                            {
//                                gettransactiondetails(Id);
//
//                            }
                        }

                        return true;

//                        return true;
                    case R.id.action_search:
                        Intent contacts_list = new Intent(GroupChannelActivity.this, SearchContactlist.class);
                        startActivity(contacts_list);
                        // call My Buddies class or function here
                        return true;
                    case R.id.action_ntifications:
//                        Intent spiltmoneyscreen = new Intent(GroupChannelActivity.this, SplitActivity.class);
//                        startActivity(spiltmoneyscreen);
                        // call My Buddies class or function here
                        return true;

                    case R.id.action_transactionhistory:
                        Log.d("ChannelUrl===382",mChannel.getUrl());
                        if(mChannel.getMembers().size() >1)
                            {
                                String Id= null;
                                for (Member m :mChannel.getMembers())
                                {
                                    if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(GroupChannelActivity.this)))
                                    {
                                        Id = m.getUserId();
                                        Log.d("ChannelUrl===391",mChannel.getUrl());
                                        gettransactiondetails(Id);

                                    }
                            }
//                            if(!android.text.TextUtils.isEmpty(Id))
//                            {
//                                gettransactiondetails(Id);
//
//                            }
                        }


                        return true;
                    case R.id.action_media:
//                        Intent transactionscreen = new Intent(GroupChannelActivity.this, TransactionsActivity.class);
//                        startActivity(transactionscreen);
                        return true;
                    case R.id.action_clearchat:
                        // call My Buddies class or function here
                        return true;
                    case R.id.action_report:
//                        Intent transactionscreen = new Intent(GroupChannelActivity.this, TransactionsActivity.class);
//                        startActivity(transactionscreen);
                        return true;
              }
             return true;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {}
        });


        // Display the menu
        optionsMenu.show();

    }

    private void gettransactiondetailss(String id) {

        mdilogue.show();
        JsonObject object = addsearchObject(id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getuseinfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserinfo>() {
                    @Override
                    public void onCompleted() {

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
                    public void onNext(GetUserinfo getUserinfo) {

                        if (HttpsURLConnection.HTTP_OK == 200) {
                            mdilogue.dismiss();
                            Intent transactions = new Intent(GroupChannelActivity.this, ViewContactdetails.class);
                            transactions.putExtra("useid",getUserinfo.getResult().get(0).getId());
                            transactions.putExtra("Phonenumber",getUserinfo.getResult().get(0).getPhoneNumber());
                            transactions.putExtra("id",id);

//                            transactions.putExtra("contactnumber",getUserinfo.getResult().get(0).getPhoneNumber());
//                            transactions.putExtra("name",getUserinfo.getResult().get(0).getFullName());
//                            transactions.putExtra("Amount","");
//                            transactions.putExtra("WalletID",getUserinfo.getResult().get(0).getWalletId());
                            startActivity(transactions);
                            //  Picasso.with(getContext()).load(profileresponse.getQrContactImage()).error(R.drawable.ic_user).into(myImage);


                        }else{


                        }
                    }
                });}

    private void gettransactiondetails(String id) {

        mdilogue.show();
        JsonObject object = addsearchObject(id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getuseinfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserinfo>() {
                    @Override
                    public void onCompleted() {

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
                    public void onNext(GetUserinfo getUserinfo) {
                        Log.d("channelUrl==========514",mChannel.getUrl());
                        if (HttpsURLConnection.HTTP_OK == 200) {
                            mdilogue.dismiss();
                            Intent transactions = new Intent(GroupChannelActivity.this, SendMoney.class);

                            transactions.putExtra("contactnumber",getUserinfo.getResult().get(0).getPhoneNumber());
                            transactions.putExtra("name",getUserinfo.getResult().get(0).getFullName());
                            transactions.putExtra("Amount","");
                            transactions.putExtra("WalletID",getUserinfo.getResult().get(0).getWalletId());
                            transactions.putExtra("channelUrl",mChannel.getUrl());
                            transactions.putExtra("Userid",getUserinfo.getResult().get(0).getId());
                            startActivity(transactions);


                          //  Picasso.with(getContext()).load(profileresponse.getQrContactImage()).error(R.drawable.ic_user).into(myImage);


                        }else{


                        }
                    }
    });}

    private JsonObject addsearchObject(String id) {
        Searchobject requestModel = new Searchobject();
        requestModel.setSearchValue(id);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    interface onBackPressedListener {
        boolean onBack();
    }
    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
//        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
//            return;
//        }
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            title_.setText(title);
        }
    }
}
