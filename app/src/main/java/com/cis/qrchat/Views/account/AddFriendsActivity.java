package com.cis.qrchat.Views.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.SpinnerTypeArrayAdapter;
import com.cis.qrchat.Model.Addfriendrequestobject;
import com.cis.qrchat.Model.Addfriendrequestresp;
import com.cis.qrchat.Model.FindContactResp;
import com.cis.qrchat.Model.GetCardtype;
import com.cis.qrchat.Model.Invitefriends;
import com.cis.qrchat.Model.QrResponse;
import com.cis.qrchat.Model.RegistrationOTPobject;
import com.cis.qrchat.Model.RegistrationOTPresp;
import com.cis.qrchat.Model.TypeItem;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.HomeActivity;
import com.cis.qrchat.Views.home.ProfileSubActivities.AddCardDetails;
import com.cis.qrchat.Views.home.QRScannerActivity;
import com.cis.qrchat.Views.home.QRScannerMainActivity;
import com.cis.qrchat.Views.home.SendMoney;
import com.cis.qrchat.Views.home.myprofile;
import com.cis.qrchat.Views.home.sendmoney_QRScannerActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class AddFriendsActivity extends AppCompatActivity {
Toolbar toolbar;
EditText et_search,nickname;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    LinearLayout lineardata,nicknamelinear,invitelineardata;
    TextView nocontact;
    Button addfriend;
    private Dialog _dialog;
    TextView username , Mobilenumber,phone;
    String Nick_name;
    Button submitpwdBtn, cancelBtn;
    ImageView action_scan;
    String GETQRDATA;
    String  User_id;
    Button invitefriend;
    ImageButton searchbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        intviews();
        setviews();
        settoolbar();


    }

    private void intviews() {
        et_search = findViewById(R.id.et_search);
        searchbutton = findViewById(R.id.searchbutton);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        lineardata = findViewById(R.id.lineardata);
        invitelineardata = findViewById(R.id.invitelineardata);
        nicknamelinear = findViewById(R.id.nicknamelinear);
        nocontact = findViewById(R.id.nocontact);
        username = findViewById(R.id.name);
        Mobilenumber = findViewById(R.id.mobilenumber);
        addfriend = findViewById(R.id.addfriend);
        submitpwdBtn = findViewById(R.id.submit_btn);
        cancelBtn = findViewById(R.id.cancelBtn);
        nickname = findViewById(R.id.nickname);
        phone = findViewById(R.id.phone);
        action_scan = findViewById(R.id.action_scan);
        Bundle extras = getIntent().getExtras();
        GETQRDATA = "";

        if (extras != null) {
            GETQRDATA = extras.getString("QRDATA");
            Log.e("GETQRDATA===>",GETQRDATA);
            getScanQRCode(GETQRDATA);
           // et_search.setText(GETQRDATA);
        }

        invitefriend = findViewById(R.id.invitefriend);
    }

    private void getScanQRCode(String getqrdata) {


            mdilogue.show();
            ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
            mSubscription = service.getqrresponse(APIConstantURL.ScanQRCode + getqrdata)
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

                                if(qrResponse.getIsSuccess()) {
                                    invitelineardata.setVisibility(View.GONE);
                                    nocontact.setVisibility(View.GONE);
                                    lineardata.setVisibility(View.VISIBLE);
                                    username.setText(qrResponse.getResult().getFullName());
                                    Mobilenumber.setText(qrResponse.getResult().getMobileNumber());

                                    addfriend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            nicknamelinear.setVisibility(View.VISIBLE);
                                            submitpwdBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    if (TextUtils.isEmpty(nickname.getText().toString())) {

                                                        showDialog(AddFriendsActivity.this, getResources().getString(R.string.enternickname));
                                                    } else {
                                                        Nick_name = nickname.getText().toString().trim();
                                                        Add_Friend(qrResponse.getResult().getUserId(), Nick_name);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                               // mySuperIntent.putExtra("Userid",qrResponse.getResult().getUserId());


//                            Intent mySuperIntent = new Intent(sendmoney_QRScannerActivity.this, Contactlist.class);
//                            mySuperIntent.putExtra("contactnumber",qrResponse.getResult().getMobileNumber());
//                            mySuperIntent.putExtra("walletId",qrResponse.getResult().getWalletId());
//                            mySuperIntent.putExtra("contact",qrResponse.getEndUserMessage());
//
//                            startActivity(mySuperIntent);

                            }else{
                                invitelineardata.setVisibility(View.VISIBLE);
                                phone.setText(et_search.getText().toString().trim());
                                nocontact.setVisibility(View.VISIBLE);
                                lineardata.setVisibility(View.GONE);
                                nicknamelinear.setVisibility(View.GONE);
                                nickname.setText("");

                            }
                        }



                    });}


    private void setviews() {
          User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
//
//        et_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(final CharSequence s, int start, int before, int count) {
//
//                    if (s.toString().trim().length() >= 4  ) {
//
//                        FindContactdetails();
//
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//            }
//        });
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindContactdetails();
            }
        });
if(et_search.getText().toString().isEmpty()){
    invitelineardata.setVisibility(View.GONE);
    lineardata.setVisibility(View.GONE);
}
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nicknamelinear.setVisibility(View.GONE);

            }
        });

        action_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nicknamelinear.setVisibility(View.GONE);
                startActivity(new Intent(AddFriendsActivity.this, QRScannerActivity.class));
            }
        });

        invitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              invitefriends();
            }
        });

    }

    private void invitefriends() {
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getinvitefriends(APIConstantURL.InviteFriend+et_search.getText().toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Invitefriends>() {
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
                        showDialog(AddFriendsActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(Invitefriends invitefriends) {

                        if(invitefriends.getIsSuccess()){
                      Toast.makeText(AddFriendsActivity.this,invitefriends.getEndUserMessage(),Toast.LENGTH_SHORT).show();
                        }
                        else{


                        }
                    }




                });}



    private void FindContactdetails() {
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getcontact(APIConstantURL.findcontact+et_search.getText().toString()+"/"+User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<FindContactResp>() {
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
                      showDialog(AddFriendsActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(FindContactResp findContactResp) {
                      if(findContactResp.getResult()!=null){
                          invitelineardata.setVisibility(View.GONE);
                          nocontact.setVisibility(View.GONE);
                          lineardata.setVisibility(View.VISIBLE);
                          username.setText(findContactResp.getResult().getFullName());
                          Mobilenumber .setText(findContactResp.getResult().getMobileNumber());

                          addfriend.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {

                                  nicknamelinear.setVisibility(View.VISIBLE);
                                  submitpwdBtn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          if (TextUtils.isEmpty(nickname.getText().toString())){

                                              showDialog(AddFriendsActivity.this, getResources().getString(R.string.enternickname));
                                          }
                                          else{
                                              Nick_name =nickname.getText().toString().trim();
                                              Add_Friend(findContactResp.getResult().getUserId(),Nick_name);

                                          }
                                      }
                                  });
                              }
                          });

                      }
                      else{


                          invitelineardata.setVisibility(View.VISIBLE);
                          phone.setText(et_search.getText().toString().trim());
                          nocontact.setVisibility(View.VISIBLE);
                          lineardata.setVisibility(View.GONE);
                          nicknamelinear.setVisibility(View.GONE);
                          nickname.setText("");

                      }
                    }




                    });}


    private void Addfriendpopup(String contactId){

            _dialog = new Dialog(this);
            _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            _dialog.setCancelable(false);
            _dialog.setContentView(R.layout.addfriend);
            _dialog.show();

            Button submitpwdBtn, cancelBtn;
            final EditText nickname;

            submitpwdBtn = _dialog.findViewById(R.id.submit_btn);
            cancelBtn = _dialog.findViewById(R.id.cancelBtn);

        nickname = _dialog.findViewById(R.id.nickname);




            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _dialog.dismiss();
                }
            });
        }

    private void Add_Friend(String contactId, String name) {

        JsonObject object = Addfriendobject(contactId,name);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postAddfriend(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Addfriendrequestresp>() {

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
                     showDialog(AddFriendsActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(Addfriendrequestresp addfriendrequestresp) {


                        if (HttpsURLConnection.HTTP_OK == 200) {
                            Toast.makeText(AddFriendsActivity.this,addfriendrequestresp.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddFriendsActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

    }

    private JsonObject Addfriendobject(String contactId,String nick_name) {
      String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        Addfriendrequestobject requestModel = new Addfriendrequestobject();
        requestModel.setId(0);
        requestModel.setContactId(contactId);
        requestModel.setSenderId(User_id);
        requestModel.setName(nick_name);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();}



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
                Intent intent = new Intent(AddFriendsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
