package com.cis.qrchat.Views.account;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Chat.Groupchat.GroupChannelActivity;
import com.cis.qrchat.Chat.call.CallService;
import com.cis.qrchat.Chat.utils.PrefUtilsCall;
import com.cis.qrchat.Model.FeedImages;
import com.cis.qrchat.Model.Feed_Data;
import com.cis.qrchat.Model.GetUserdetails;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.FeedActivity;
import com.cis.qrchat.Views.home.FeedRecycleAdapter;
import com.cis.qrchat.Views.home.QRScannerMainActivity;
import com.cis.qrchat.Views.home.myprofile;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBirdException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class ViewContactdetails extends AppCompatActivity {
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    Toolbar toolbar;
    String USerid,Phonenumber;
    Integer genderid;
    ImageView userImg;
    TextView location,gendertype,qrchatid,name,email,mobilenumber,aliasName, statsus;
    LinearLayout gender_layout,email_layout,location_layout;
    RecyclerView rcv_feeds;
    FeedImagesAdapter feedImagesRecycleAdapter;
    private List<FeedImages> feedImages_List = new ArrayList<>();
    ImageView vediocall,voicecall,chats;
    GroupChannel mChannel;
    String Id;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contactdetails);
        intview();
        setviews();
        settoolbar();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void intview() {
        userImg = findViewById(R.id.userImg);
        location = findViewById(R.id.location);
        gendertype = findViewById(R.id.gendertype);
        qrchatid = findViewById(R.id.qrchatid);
        //rcv_feeds = findViewById(R.id.feeds);
        aliasName = findViewById(R.id.aliasName);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobilenumber = findViewById(R.id.mobilenumber);
        statsus = findViewById(R.id.status);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        location_layout = findViewById(R.id.location_layout);
        gender_layout = findViewById(R.id.gender_layout);
        email_layout = findViewById(R.id.email_layout);
        vediocall = findViewById(R.id.vediocall);
        voicecall = findViewById(R.id.voicecall);
        chats = findViewById(R.id.chats);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
               name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                statsus.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                qrchatid.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                aliasName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                gendertype.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                email.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                location.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            }

            else
                updateResources(this, "en-US");

    }
    private void setviews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            USerid = extras.getString("useid");
            Id = extras.getString("id");
            Phonenumber =extras.getString("Phonenumber");
            Log.e("Phonenumber==========>",Phonenumber);
            getuserdetails(USerid);
        }
        vediocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeVodeoCall();

            }
        });
        voicecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeVoiceCall();
            }
        });
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String User_id = SharedPrefsData.getInstance(ViewContactdetails.this).getStringFromSharedPrefs("USERID");
                Log.e("============>", User_id);
                List<String> userIds = new ArrayList<>();
                userIds.add(Phonenumber);
              //  userIds.add(User_id);

                Log.e("IDs==============>1",userIds.get(0) +"============"+ userIds.size());

                createGroupChannel(userIds,true );
               // createGroupChannel(USerid,true );
            }
        });

//        feedImages_List = new ArrayList<>();
//        feedImagesRecycleAdapter = new FeedImagesAdapter(ViewContactdetails.this, feedImages_List);
//
//        rcv_feeds.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        rcv_feeds.setAdapter(feedImagesRecycleAdapter);
//        fetchdata();
    }
    private void createGroupChannel(List<String> userIds, boolean distinct) {
        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                Intent intent = new Intent(ViewContactdetails.this, GroupChannelActivity.class);
                intent.putExtra("groupChannelUrl", groupChannel.getUrl());
                startActivity(intent);
            }
        });
    }
//    private void createGroupChannel(List<String> userIds, boolean distinct) {
//        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
//            @Override
//            public void onResult(GroupChannel groupChannel, SendBirdException e) {
//                if (e != null) {
//                    // Error!
//                    return;
//                }
//
//                Intent intent = new Intent(ViewContactdetails.this, GroupChannelActivity.class);
//                intent.putExtra("groupChannelUrl", groupChannel.getUrl());
//                startActivity(intent);
//            }
//        });
//    }

    private void makeVoiceCall() {

            if (!android.text.TextUtils.isEmpty(Id)) {
                CallService.dial(ViewContactdetails.this, Id, false);
            } else {
                Toast.makeText(ViewContactdetails.this, "Unable to call", Toast.LENGTH_SHORT).show();
            }
        }

        private void makeVodeoCall() {


                if(!android.text.TextUtils.isEmpty(Id))
                {

                    CallService.dial(ViewContactdetails.this,Id,true);
                }else{
                    Toast.makeText(ViewContactdetails.this, "Unable to call", Toast.LENGTH_SHORT).show();
                }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        Context myContext = new ContextThemeWrapper(this,R.style.menuStyle);
   ;
        menuInflater.inflate(R.menu.viewcontactmenu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    private void fetchdata() {

        int[] feedcovers = new int[]{
                R.drawable.pisatower,
                R.drawable.tajmahal,
                R.drawable.statue,
        };


        FeedImages a = new FeedImages(feedcovers[0]);
        feedImages_List.add(a);
        a = new FeedImages(feedcovers[1]);
        feedImages_List.add(a);
        a = new FeedImages(feedcovers[2]);
        feedImages_List.add(a);

    }

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
                                name.setText(getUserdetails.getFullName());
                                mobilenumber.setText(getUserdetails.getPhoneNumber());


//                                selected_regionid = getUserdetails.getRegionId();
                                genderid = getUserdetails.getGenderTypeId();
                                if(genderid == 12){
                                    gendertype.setText("FeMale");
                                }else if(genderid == 11){
                                    gendertype.setText("Male");
                                }
                                else{
                                    gender_layout.setVisibility(View.GONE);
                                }
//                                getgender();
                             qrchatid.setText(getUserdetails.getUserName());
                                aliasName.setText(getUserdetails.getFullName());


                                if(getUserdetails.getLocation()!=null){
                                    location_layout.setVisibility(View.VISIBLE);
                                    location.setText(getUserdetails.getLocation());
                                }else{
                                    location_layout.setVisibility(View.GONE);
                                }
                                if(getUserdetails.getEmail()!=null){
                                    email_layout.setVisibility(View.VISIBLE);
                                    email.setText(getUserdetails.getEmail());

                                }else{
                                    email_layout.setVisibility(View.GONE);
                                }
                                if(getUserdetails.getFileUrl()== null){
                                    Picasso.with(ViewContactdetails.this).load(R.drawable.ic_user).error(R.drawable.ic_user).into(userImg);
                                }else{
                                    Picasso.with(ViewContactdetails.this).load(getUserdetails.getFileUrl()).error(R.drawable.ic_user).into(userImg);}
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
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
//                Intent intent = new Intent(SendMoney.this, HomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

}
