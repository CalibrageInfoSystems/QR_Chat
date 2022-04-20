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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Model.ConfirmResetPasswordOTPRES;
import com.cis.qrchat.Model.ConfirmResetPasswordOTPobject;
import com.cis.qrchat.Model.CreateWalletresp;
import com.cis.qrchat.Model.Loginobject;
import com.cis.qrchat.Model.RegisterResponse;
import com.cis.qrchat.Model.Registerobject;
import com.cis.qrchat.Model.RegistrationOTPobject;
import com.cis.qrchat.Model.RegistrationOTPresp;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class  OTPActivity extends Activity {

    com.alimuzaffar.lib.pin.PinEntryEditText txtPinEntry;
    Button submit;
    public String otptext;
    private Dialog _dialog;
    private Dialog _dialogg;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    String screenname,OTP,phonenumber,Otp_new;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            screenname = extras.getString("FromScreen");
           // OTP = extras.getString("otp");
            phonenumber = extras.getString("phonenumber");
            Log.e("From Screen", screenname);
        }


        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 1)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
            }

        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_o_t_p);




        submit = findViewById(R.id.otp_submit);
        txtPinEntry = findViewById(R.id.txt_pin_entry);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        ResetPasswordSendOTP();
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.toString().equals("123456")) {
//                    Toast.makeText(OTPActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
//                    otptext = s.toString();
//                    Log.d("OTPTEXT", otptext + "");
//                } else if (s.length() == "123456".length()) {
//                    Toast.makeText(OTPActivity.this, getResources().getString(R.string.incorrect), Toast.LENGTH_SHORT).show();
//                    txtPinEntry.setText(null);
//                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(txtPinEntry.getText().toString())) {
                    showDialog(OTPActivity.this, getResources().getString(R.string.pleaseenterotp));
                }
                else if (txtPinEntry.getText().length() < 6) {
                    showDialog(OTPActivity.this, getResources().getString(R.string.pleaseentervalidotp));
                }
                else{

                        if (screenname.equalsIgnoreCase("Register")){

                            showPasswordDialog(OTPActivity.this);
                        }else{
                            showResetPasswordDialog(OTPActivity.this);
                        }

                }
            }
        });
    }

    private void ResetPasswordSendOTP() {
        JsonObject object = ResetPasswordobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postResetPassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegistrationOTPresp>() {

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
                    public void onNext(RegistrationOTPresp registrationOTPresp) {


                        if (HttpsURLConnection.HTTP_OK == 200) {
                            Otp_new = registrationOTPresp.getOtp();
                        }

                    }
                });
    }

    private JsonObject ResetPasswordobject() {
        RegistrationOTPobject requestModel = new RegistrationOTPobject();

        requestModel.setPhoneNumber(phonenumber);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();}



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
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public void showPasswordDialog(Activity activity) {
        _dialog = new Dialog(activity);
        _dialog = new Dialog(activity);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setContentView(R.layout.password);
            _dialog.show();

        Button submitpwdBtn, cancelBtn;
        final EditText password, confirmpassword;

        submitpwdBtn = _dialog.findViewById(R.id.submitpwd_btn);
        cancelBtn = _dialog.findViewById(R.id.cancelBtn);

        password = _dialog.findViewById(R.id.password_et);
        confirmpassword = _dialog.findViewById(R.id.confirmpassword_et);

        submitpwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(password.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.enterpassword));
                }else if (password.getText().toString().length()< 8 &&!isValidPassword(password.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.passwordValidation));
                }else if(TextUtils.isEmpty(confirmpassword.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.enterconfirmpassword));
                }
                else if (!password.getText().toString().equals(confirmpassword.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.passworddoesntmatch));

//                    Toast.makeText(OTPActivity.this, getResources().getString(R.string.passworddoesntmatch), Toast.LENGTH_SHORT).show();
                }else{

                    ConfirmResetPasswordOTP(password.getText().toString());

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

    private void ConfirmResetPasswordOTP(String password) {
        mdilogue.show();
        JsonObject object = ConfirmResetPasswordOTPobject(password);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postConfirmResetPasswordOTP(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ConfirmResetPasswordOTPRES>() {

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
                       // Log.e("============", ((HttpException) e).code()+"");
                        mdilogue.dismiss();
                        //showDialog(RegisterActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(ConfirmResetPasswordOTPRES confirmResetPasswordOTPRES) {



                        if (HttpsURLConnection.HTTP_OK == 200) {
                            if(confirmResetPasswordOTPRES.getMessage().equalsIgnoreCase("success")){
                                CreateWallet();
                            }else{
                                showDialog(OTPActivity.this, confirmResetPasswordOTPRES.getMessage());
                            }




                        }

                    }
                });
    }

    private void CreateWallet() {
        JsonObject object = ResetPasswordobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postCreateWallet(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateWalletresp>() {

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
                    public void onNext(CreateWalletresp createWalletresp) {


                        if (HttpsURLConnection.HTTP_OK == 200) {

                            Intent loginscreen = new Intent(OTPActivity.this, LoginActivity.class);
                            startActivity(loginscreen);
                            Toast.makeText(OTPActivity.this, getResources().getString(R.string.passwordsubmitted), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
    }

    private JsonObject ConfirmResetPasswordOTPobject(String password) {
        ConfirmResetPasswordOTPobject requestModel = new ConfirmResetPasswordOTPobject();

        requestModel.setNewPassword(password);
        requestModel.setOtp(Otp_new);
        requestModel.setPhonenumber(phonenumber);



        return new Gson().toJsonTree(requestModel).getAsJsonObject();}



    public void showResetPasswordDialog(Activity activity) {
        _dialogg = new Dialog(activity);
        _dialogg = new Dialog(activity);
        _dialogg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialogg.setCancelable(false);
        _dialogg.setContentView(R.layout.resetpassword);
        _dialogg.show();

        Button submitpwdBtn, cancelBtn;
        final EditText password, confirmpassword;

        submitpwdBtn = _dialogg.findViewById(R.id.submitpwd_btn);
        cancelBtn = _dialogg.findViewById(R.id.cancelBtn);

        password = _dialogg.findViewById(R.id.password_et);
        confirmpassword = _dialogg.findViewById(R.id.confirmpassword_et);

        submitpwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(password.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.enterNewpassword));
                }else if (password.getText().toString().length()< 8 &&!isValidPassword(password.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.passwordValidation));
                }else if(TextUtils.isEmpty(confirmpassword.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.enterconfirmpassword));
                }
                else if (!password.getText().toString().equals(confirmpassword.getText().toString())){

                    showDialog(OTPActivity.this, getResources().getString(R.string.passworddoesntmatch));

//                    Toast.makeText(OTPActivity.this, getResources().getString(R.string.passworddoesntmatch), Toast.LENGTH_SHORT).show();
                }else{


                    ConfirmResetPasswordOTP(password.getText().toString());
//                    Intent loginscreen = new Intent(OTPActivity.this, LoginActivity.class);
//                    startActivity(loginscreen);
//                    Toast.makeText(OTPActivity.this, getResources().getString(R.string.passwordsubmitted), Toast.LENGTH_SHORT).show();
//                    finish();

                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialogg.dismiss();
            }
        });
    }


}