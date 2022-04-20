package com.cis.qrchat.Views.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.TransactionAdapter;
import com.cis.qrchat.Model.GetTransactionsDetails;
import com.cis.qrchat.Model.GetTransactionsObject;
import com.cis.qrchat.Model.Profileresponse;
import com.cis.qrchat.Model.RequestmoneyObject;
import com.cis.qrchat.Model.RequestmoneyResp;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.Contactlist;
import com.cis.qrchat.Views.home.ProfileSubActivities.ReceiveMoneyActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReciveMainActivity extends AppCompatActivity {
Toolbar toolbar;
    Dialog _dialog;
EditText amount;
Button sendrequest;
TextView addamount;
    public static ActionBottomSheetDialog openBottomSheet;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    String base64image;
    public static LinearLayout Qrcode,statictext;
   public static ImageView qrimage;

    String image;
    public static final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive_main);
        intviews();
        setviews();
        setupToolbar();
        getuserdata();
    }

    private void intviews() {

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
         qrimage = findViewById(R.id.qrimage);
        addamount = findViewById(R.id.addamount);
        Qrcode = findViewById(R.id.Qrcode);
        statictext = findViewById(R.id.statictext);
//

    }
    private void setviews() {
        addamount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                         openBottomSheet = ActionBottomSheetDialog.newInstance();
                        openBottomSheet.show(getSupportFragmentManager(),ActionBottomSheetDialog.TAG);


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



    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.receiveMoney));
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
//                Intent intent = new Intent(SendMoney.this, HomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    private void getuserdata() {
        String User_name = SharedPrefsData.getInstance(getApplicationContext()).getStringFromSharedPrefs("USERNAME");
        Log.e("============>",User_name);
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getprofilerespose(APIConstantURL.getprofile + User_name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Profileresponse>() {
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
                    public void onNext(Profileresponse profileresponse) {
                        if (HttpsURLConnection.HTTP_OK == 200) {

                            image = profileresponse.getQrContactImage();
                            Picasso.with(getApplicationContext()).load(image).error(R.drawable.ic_user).into(qrimage);

                        }else{


                        }
                    }



                });}

    public static class ActionBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
        public static final String TAG = "ActionBottomDialog";

        EditText amount;
        private SpotsDialog mdilogue;
        private Subscription mSubscription;
        String base64image;
        public static final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
        public static ActionBottomSheetDialog newInstance() {
            return new ActionBottomSheetDialog();
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.botton_sheet_layout, container, false);
        }
        @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.findViewById(R.id.saveBtn).setOnClickListener(this);
            amount=  view.findViewById(R.id.editTextamount);
            mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                    .setContext(getActivity())
                    .setTheme(R.style.Custom)
                    .build();
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

        }
        @Override
        public void onDetach() {
            super.onDetach();
          //  mListener = null;
        }
        @Override public void onClick(View view) {
            TextView tvSelected = (TextView) view;

            if (TextUtils.isEmpty(amount.getText().toString())) {
                showDialog(getActivity(), getResources().getString(R.string.enterAmount));
            }else{
                AddRequest();}

        }





        private void AddRequest() {

            mdilogue.show();
            JsonObject object = requestobject();
            ApiService service = ServiceFactory.createRetrofitService(getActivity(), ApiService.class);
            mSubscription = service.Postrequestmoney(object)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RequestmoneyResp>() {

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
                            Log.e("============", ((HttpException) e).code() + "");
                            mdilogue.dismiss();
                            //showDialog(RegisterActivity.this, getString(R.string.server_error));
                        }

                        @Override
                        public void onNext(RequestmoneyResp requestmoneyResp) {

                            if (HttpsURLConnection.HTTP_OK == 200) {
                                if (requestmoneyResp.getIsSuccess()) {

                                    base64image = requestmoneyResp.getResult().getQrCodeImage();

                                String base64Image = base64image.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                              ReciveMainActivity.qrimage.setImageBitmap(decodedByte);

                                    ReciveMainActivity.openBottomSheet.dismiss();
                                    statictext.setVisibility(View.GONE);
                                    Qrcode.setVisibility(View.VISIBLE);

//                                    transactionListAdapter = new TransactionAdapter(SendMoney.this, getTransactionsDetails.getListResult());
//                                    recyclerView.setAdapter(transactionListAdapter);
                                }
                            }
                        }
                    });
        }

        private JsonObject requestobject() {

            String User_id = SharedPrefsData.getInstance(getActivity()).getStringFromSharedPrefs("USERID");

            String currentDate = new SimpleDateFormat(DATE_FORMAT1).format(Calendar.getInstance().getTimeInMillis());
            Log.e("============>",User_id);




            RequestmoneyObject requestModel = new RequestmoneyObject();
            requestModel.setId(null);
            requestModel.setUserId(User_id);
            requestModel.setAmount(Integer.valueOf(amount.getText().toString()));
            requestModel.setTransactionId(null);
            requestModel.setCreatedBy(User_id);
            requestModel.setCreatedDate(currentDate);
            requestModel.setUpdatedBy(User_id);
            requestModel.setUpdatedDate(currentDate);


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




    }}
