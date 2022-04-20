package com.cis.qrchat.Views.account;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Chat.BaseApplication;
import com.cis.qrchat.Chat.fcm.MyFirebaseMessagingService;
import com.cis.qrchat.Chat.utils.PrefUtilsCall;
import com.cis.qrchat.Chat.utils.PreferenceUtils;
import com.cis.qrchat.Chat.utils.PushUtils;
import com.cis.qrchat.Chat.utils.PushUtilsCall;
import com.cis.qrchat.MainActivity;
import com.cis.qrchat.Model.LoginResponse;
import com.cis.qrchat.Model.Loginobject;
import com.cis.qrchat.Model.RegisterResponse;
import com.cis.qrchat.Model.Registerobject;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.HomeActivity;
import com.cis.qrchat.common.Constants;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sendbird.calls.AuthenticateParams;
import com.sendbird.calls.DirectCall;
import com.sendbird.calls.SendBirdCall;
import com.sendbird.calls.SendBirdException;
import com.sendbird.calls.User;
import com.sendbird.calls.handler.AuthenticateHandler;
import com.sendbird.calls.handler.SendBirdCallListener;
import com.sendbird.uikit.SendBirdUIKit;
import com.sendbird.uikit.log.Logger;
import com.sendbird.uikit.widgets.WaitingDialog;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextView signup, forgotpassword;
    Button loginBtn;
    private Dialog _dialog;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");

        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_et);
        password = findViewById(R.id.loginpassword_et);
        signup = findViewById(R.id.signup);
        loginBtn = findViewById(R.id.loginBtn);
        forgotpassword = findViewById(R.id.forgotpassword);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validations()){
                    userlogin();


                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerscreen = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerscreen);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showForgotPasswordDialog(LoginActivity.this);
            }
        });

    }

    private void userlogin() {
        mdilogue.show();
        JsonObject object = Loginobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getLoginPage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {

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
                        //Log.e("============", ((HttpException) e).code()+"");
                        mdilogue.dismiss();
                        showDialog(LoginActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {


                        if (loginResponse.getIsSuccess()) {

                            SharedPrefsData.putBool(LoginActivity.this, Constants.IS_LOGIN, true);
                            SharedPrefsData.saveCatagories(LoginActivity.this, loginResponse);
                            SharedPrefsData.getInstance(LoginActivity.this).updateStringValue(LoginActivity.this, "USERNAME", loginResponse.getResult().getUserName());
                            SharedPrefsData.getInstance(LoginActivity.this).updateStringValue(LoginActivity.this, "FULLNAME", loginResponse.getResult().getFullName());
                            SharedPrefsData.getInstance(LoginActivity.this).updateStringValue(LoginActivity.this, "MOBILENUMBER", loginResponse.getResult().getMobileNumber());
                            SharedPrefsData.getInstance(LoginActivity.this).updateStringValue(LoginActivity.this, "USERID", loginResponse.getResult().getUserId());
                            SharedPrefsData.getInstance(LoginActivity.this).updateStringValue(LoginActivity.this, "WalletId", loginResponse.getResult().getWalletId());
                            SharedPrefsData.getInstance(LoginActivity.this).updateStringValue(LoginActivity.this, "walletBalance", loginResponse.getResult().getWalletBallence() + "");

                            Log.e("created_useid==", loginResponse.getResult().getUserName() + "");

                            PreferenceUtils.setWalletId(loginResponse.getResult().getWalletId() );
                            PreferenceUtils.setFullName(loginResponse.getResult().getFullName() + "" );
                            PreferenceUtils.setMobileNumber(loginResponse.getResult().getMobileNumber() + "");
                            PreferenceUtils.setWalletBalance(loginResponse.getResult().getWalletBallence() + "");
                            PreferenceUtils.setNickname(loginResponse.getResult().getUserName());
                            PreferenceUtils.setNickname(loginResponse.getResult().getUserName());

                            PreferenceUtils.setUserId(loginResponse.getResult().getMobileNumber() );
                            PreferenceUtils.setNickname(loginResponse.getResult().getFullName());

                            PrefUtilsCall.setUserId(LoginActivity.this, loginResponse.getResult().getMobileNumber() );


                            AuthenticateParams params = new AuthenticateParams(loginResponse.getResult().getMobileNumber())
                                    .setAccessToken("9618f1912558a269adaa5304e4d517c2ddbf7b9a")
                                    .setPushToken(PrefUtilsCall.getPushToken(LoginActivity.this), false);

                            SendBirdCall.authenticate(params, new AuthenticateHandler() {
                                @Override
                                public void onResult(User user, SendBirdException e) {
                                    if (e == null) {
                                        // The user is authenticated successfully.
                                    }
                                }
                            });

                            WaitingDialog.show(LoginActivity.this);
                            SendBirdUIKit.connect((user, e) -> {
                                if (e != null) {
                                    Logger.e(e);
                                    WaitingDialog.dismiss();
                                    return;
                                }
                                WaitingDialog.dismiss();
                                PushUtils.registerPushHandler(new MyFirebaseMessagingService());
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            });

//                            Intent loginscreen = new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(loginscreen);
//                            finish();
//                        }
//                    }

                        }
                        else{
                            showDialog(LoginActivity.this, getResources().getString(R.string.invalidlogin));
                        }
                    }});}
    private JsonObject Loginobject() {
        Loginobject requestModel = new Loginobject();

        requestModel.setMobileNumber((username.getText().toString().trim()));
        requestModel.setPassword(password.getText().toString().trim());


        return new Gson().toJsonTree(requestModel).getAsJsonObject();}

    private boolean validations() {
        int length = username.getText().length();
        if (TextUtils.isEmpty(username.getText().toString())) {
            showDialog(LoginActivity.this, getResources().getString(R.string.pleaseenternumberqrid));
            return false;
        }
        else if (length < 4) {
            showDialog(LoginActivity.this, getResources().getString(R.string.minimumNumber));
            return false;
        }else if (TextUtils.isEmpty(password.getText().toString().trim())) {
            showDialog(LoginActivity.this, getResources().getString(R.string.pleaseenterpassword));
            return false;
        }

        return true;
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

    public void showForgotPasswordDialog(Activity activity) {
        _dialog = new Dialog(activity);
        _dialog = new Dialog(activity);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setContentView(R.layout.forgotpassword);
        _dialog.show();

        Button submitpwdBtn, cancelBtn;
        final EditText mobileNumber;

        submitpwdBtn = _dialog.findViewById(R.id.submitpwd_btn);
        cancelBtn = _dialog.findViewById(R.id.cancelBtn);

        mobileNumber = _dialog.findViewById(R.id.mobilenumber_et);


        submitpwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int length = mobileNumber.getText().length();

                if (TextUtils.isEmpty(mobileNumber.getText().toString().trim())) {
                    showDialog(LoginActivity.this, getResources().getString(R.string.pleaseenternumber));
                } else if (length < 4) {
                    showDialog(LoginActivity.this, getResources().getString(R.string.minimumNumber));
                } else{
                    Intent otpscreen = new Intent(LoginActivity.this, OTPActivity.class);
                    otpscreen.putExtra("FromScreen","Login");
                    otpscreen.putExtra("phonenumber",mobileNumber.getText().toString().trim());
                    startActivity(otpscreen);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialog.dismiss();
            }
        });
    }


}