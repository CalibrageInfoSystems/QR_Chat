package com.cis.qrchat.Views.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.cis.qrchat.Adapters.SpinnerTypeArrayAdapter;
import com.cis.qrchat.Adapters.TransactionAdapter;
import com.cis.qrchat.Adapters.savedCardAdapter;

import com.cis.qrchat.Chat.Groupchat.GroupChannelActivity;
import com.cis.qrchat.Chat.Groupchat.UrlPreviewInfo;
import com.cis.qrchat.Chat.Groupchat.WebUtils;
import com.cis.qrchat.Model.GetCards;
import com.cis.qrchat.Model.GetGender;
import com.cis.qrchat.Model.GetPassbookObject;
import com.cis.qrchat.Model.GetTransactionsDetails;
import com.cis.qrchat.Model.GetTransactionsObject;
import com.cis.qrchat.Model.GetUserdetails;
import com.cis.qrchat.Model.GetWalletBalanceResponse;
import com.cis.qrchat.Model.SendMoneyobject;
import com.cis.qrchat.Model.SendmoneyResponse;
import com.cis.qrchat.Model.TypeItem;
import com.cis.qrchat.Model.TypeItems;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.ProfileSubActivities.AddCardDetails;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRPayAccount;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRWalletActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
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

public class SendMoney extends AppCompatActivity {
    public  static  EditText editTextamount;

    Button send;
    public Subscription mSubscription;
    private SpotsDialog mdilogue;
    public static String phonenumber,Name,WalletId;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView reason;
    public static   Dialog _dialog;
    public  static  ActionBottomSheetDialog openBottomSheet;
    public static     Integer amount;
    public static   TransactionAdapter transactionListAdapter;
   public static String channelUrl,Userid;
    GroupChannel mChannel;
    TextView title,subtitle;
    CircleImageView profile;
    ImageView backImg;
    TextView no_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        intview();
        setviews();
        settoolbar();
    }

    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Name );
        toolbar.setSubtitle(phonenumber);

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

    private void intview() {
        backImg = findViewById(R.id.back);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        send = findViewById(R.id.buttonSend);
        editTextamount = findViewById(R.id.editTextamount);
        recyclerView = findViewById(R.id.recyclerView);
        no_data = findViewById(R.id.no_data);
        reason= findViewById(R.id.reason);
        title= findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        profile = findViewById(R.id.profile);
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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phonenumber = extras.getString("contactnumber");
            Name= extras.getString("name");
            WalletId= extras.getString("WalletID");
            amount  = extras.getInt("Amount");
            channelUrl  = extras.getString("channelUrl");
            Userid = extras.getString("Userid");
            getuserdetails(Userid);
            Log.e("========Userid==1634",Userid+"");
            if (amount == 0)
                editTextamount.setText("");
            else
                editTextamount.setText(amount+"");

            title.setText(Name);
            subtitle.setText(phonenumber);

        }
        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reasonpopup();
            }
        });

        gettransactiondetails();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(editTextamount.getText().toString())) {
                    showDialog(SendMoney.this, getResources().getString(R.string.enterAmount));
                }else{
                    ActionBottomSheetDialog dialog = new ActionBottomSheetDialog();
                    openBottomSheet = dialog.newInstance();
                    openBottomSheet.show(getSupportFragmentManager(),ActionBottomSheetDialog.TAG);

                    //  sendMoney();
                    //    startActivity(getIntent());

                }
            }
        });


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

                                if(getUserdetails.getFileUrl()== null){
                                    Picasso.with(SendMoney.this).load(R.drawable.ic_user).error(R.drawable.ic_user).into(profile);
                                }else{
                                    Picasso.with(SendMoney.this).load(getUserdetails.getFileUrl()).error(R.drawable.ic_user).into(profile);}
////                            Glide.with(myprofile.this)
////                                    .load(getUserdetails.getFileUrl())
////
//                                    .apply(RequestOptions.circleCropTransform())
//                                    .into(userImg);
                            }
                            else{


                            }
                        }



                    });}

    }



    private  void gettransactiondetails() {

        JsonObject object = transcationobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.posttransactiondetails(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetTransactionsDetails>() {

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
                        Log.e("============", ((HttpException) e).code()+"");

                        //showDialog(RegisterActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(GetTransactionsDetails getTransactionsDetails) {


                        if(HttpsURLConnection.HTTP_OK == 200) {
                            if(getTransactionsDetails.getListResult()!=null){

                                no_data.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                transactionListAdapter = new TransactionAdapter(SendMoney.this, getTransactionsDetails.getListResult() );
                                recyclerView.setAdapter(transactionListAdapter);
                            }
                            else{
                                no_data.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }


    public static void showDialog(Activity activity, String msg) {
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

    private void reasonpopup() {
        _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setContentView(R.layout.reasonpopup);
        _dialog.show();

//        EditText phone_number =  _dialog.findViewById(R.id.phonenumber);
//        phone_number.setText(phonenumber);
        EditText reason = _dialog.findViewById(R.id.reason);
        ((Button) _dialog.findViewById(R.id.saveBtn))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
//                        Toast.makeText(getApplicationContext(),
//                                reason.getText().toString(),Toast.LENGTH_LONG).show();

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



    private  JsonObject transcationobject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        String wallet_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("WalletId");

        Log.e("============>",User_id);

        GetTransactionsObject requestModel = new GetTransactionsObject();
        requestModel.setLoginUserWalletId(wallet_id);
        requestModel.setContactUserWalletId(WalletId);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();}










    public static class ActionBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
        public static final String TAG = "ActionBottomDialog";
        GroupChannel mChannel;
        EditText amount;
        private SpotsDialog mdilogue;
        private Subscription mSubscription;
        Spinner bankname;
        private ArrayList<TypeItems> getbank = new ArrayList<>();
        SpinnerTypeArrayAdapterr bankAdapter;
        TypeItems bankTypeItem;
        TextView walletbalance;
        RadioGroup radioGroup;
        RadioButton selectedRadioButton;
        TextView addmoney, nocard,addcard;
        Button cancel;
        public static final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
        public  ActionBottomSheetDialog newInstance() {
            return new ActionBottomSheetDialog();
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.botton_sheet_send_layout, container, false);
        }
        @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.findViewById(R.id.saveBtn).setOnClickListener(this);
            amount=  view.findViewById(R.id.editTextamount);
            mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                    .setContext(getActivity())
                    .setTheme(R.style.Custom)
                    .build();
            bankname = view.findViewById(R.id.bankname);
            walletbalance =view.findViewById(R.id.walletbalance);
            radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
            addmoney = view.findViewById(R.id.addmoney);
            nocard = view.findViewById(R.id.nocardavailable);
            cancel = view.findViewById(R.id.cancel);
            addcard = view.findViewById(R.id.addcard);

            if (mChannel == null) {
                GroupChannel.getChannel(channelUrl, new GroupChannel.GroupChannelGetHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            e.printStackTrace();
                            return;
                        }

                        mChannel = groupChannel;

                    }
                });
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    selectedRadioButton = (RadioButton) group.findViewById(checkedId);

                    if (null != selectedRadioButton && checkedId > -1) {
              //      Toast.makeText(getActivity(), selectedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                    }else{
                        showDialog(getActivity(), getResources().getString(R.string.selectradiobutton));
                    }



                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  SendMoney.openBottomSheet.dismiss();
                }
            });
            addmoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent moneyscreen = new Intent(getActivity(), QRWalletActivity.class);
                    startActivity(moneyscreen);
                    SendMoney.openBottomSheet.dismiss();
                }
            });
            addcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SendMoney.openBottomSheet.dismiss();
                    Intent moneyscreen = new Intent(getActivity(), AddCardDetails.class);
                    startActivity(moneyscreen);
                }
            });
            getwalletBalance();
            getcardslist();

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
        @SuppressLint("ResourceType")
        @Override public void onClick(View view) {
            TextView tvSelected = (TextView) view;
            selectedRadioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

            if ( selectedRadioButton.getText() != null&& radioGroup.getCheckedRadioButtonId() > -1 ) {
                if(selectedRadioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.bankaccount))) {
if(nocard.getVisibility() == View.VISIBLE){
    showDialog(getActivity(), getResources().getString(R.string.pleaeAddcard));
}else {
    sendMoney();
}

                }
              else if(selectedRadioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.wallet))) {
                    if(walletbalance.getText().toString().equalsIgnoreCase("0.00")){
                        showDialog(getActivity(), getResources().getString(R.string.addamout));
                    }else {
                        sendMoney();
                    }

                }

            }else{

                showDialog(getActivity(), getResources().getString(R.string.selectradiobutton));}

        }

        private void sendMoney() {
            mdilogue.show();
            JsonObject object = sendmoneyobject();
            ApiService service = ServiceFactory.createRetrofitService(getContext(), ApiService.class);
            mSubscription = service.postsendmoney(object)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SendmoneyResponse>() {

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
                        public void onNext(SendmoneyResponse sendmoneyResponse) {

                            if(sendmoneyResponse.getIsSuccess()) {
                                String text = "money& "+editTextamount.getText().toString();
                                Log.e("===========>567",text);
                                sendUserMessage(text);

                                enterpin(getActivity(), getResources().getString(R.string.Amount) + " "+editTextamount.getText().toString()+ " "+getResources().getString(R.string.sucess));
                                //  Toast.makeText(SendMoney.this, sendMoneytoFundResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();


//                                Log.e("====text====476",text);
//                                List<String> urls = WebUtils.extractUrls(text);
//                                Log.e("====urls====478",urls+"");.
//                                Log.e("====urls====478",urls.size()+"");
//                                if (urls.size() > 0) {
//                                    Log.e("====urls====480", urls.get(0)+"");
//                                    sendUserMessageWithUrl(text, urls.get(0));
//                                    return;
//                                }
                                SendMoney.editTextamount.setText("");


                              //  gettransactiondetails();

                            }
                            else{

                                Toast.makeText(getContext(), sendmoneyResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                            }
//                        if(HttpsURLConnection.HTTP_OK == 200) {
//                            Toast.makeText(SendMoney.this,sendmoneyResponse.getEndUserMessage(),Toast.LENGTH_LONG).show();
//                            editTextamount.setText("");
//                            gettransactiondetails();
//
//                            showSuccessDialog(SendMoney.this, sendmoneyResponse.getEndUserMessage());
//                        }
                        }
                    });
        }

        private void enterpin(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.enterpin);

            PinEntryEditText pinEntry = dialog.findViewById(R.id.mpin_entry);


//
//            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//            text.setText(msg);

            Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("===========length",pinEntry.getText().length()+"");

                    if (pinEntry.getText() != null & pinEntry.getText().toString().trim() != "" & !TextUtils.isEmpty(pinEntry.getText() ) & pinEntry.getText().length() == 4 ) {
                        dialog.dismiss();
                    showSuccessDialog(getActivity(), msg);
                }
                else{
    Toast.makeText(
            getActivity(),
            getResources().getString(R.string.pleaseenterpin), Toast.LENGTH_SHORT)
            .show();

                }
                }
            });
            dialog.show();
        }

        private void sendUserMessage(String text) {

                if (mChannel == null) {
                    return;
                }
                Log.e("===========>",text);

                List<String> urls = WebUtils.extractUrls(text);
                Log.e("===========>",urls+"");
                Log.e("===========>",urls.size()+"");
                if (urls.size() > 0) {
                    sendUserMessageWithUrl(text, urls.get(0));
                    return;
                }

                UserMessage tempUserMessage = mChannel.sendUserMessage(text, new BaseChannel.SendUserMessageHandler() {
                    @Override
                    public void onSent(UserMessage userMessage, SendBirdException e) {
                        if (e != null) {
                            // Error!

                            if (getActivity() != null) {
                                Toast.makeText(
                                        getActivity(),
                                        "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }

                            return;
                        }

                        // Update a sent message to RecyclerView

                    }
                });

                // Display a user message to RecyclerView


        }

        public void sendUserMessageWithUrl(final String text, String url) {


            new WebUtils.UrlPreviewAsyncTask() {
                @Override
                protected void onPostExecute(UrlPreviewInfo info) {
                    if ( mChannel == null) {
                        return;
                    }

                    UserMessage tempUserMessage = null;
                    BaseChannel.SendUserMessageHandler handler = new BaseChannel.SendUserMessageHandler() {
                        @Override
                        public void onSent(UserMessage userMessage, SendBirdException e) {
                            if (e != null) {
                                // Error!
//                                Log.e(LOG_TAG, e.toString());
                                if (getActivity() != null) {
                                    Toast.makeText(
                                            getActivity(),
                                            "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                                            .show();
                                }

                                return;
                            }

                            // Update a sent message to RecyclerView

                        }
                    };

                    try {
                        // Sending a message with URL preview information and custom type.
                        String jsonString = info.toJsonString();

                    } catch (Exception e) {
                        // Sending a message without URL preview information.
                        tempUserMessage = mChannel.sendUserMessage(text, handler);
                    }


                    // Display a user message to RecyclerView

                }
            }.execute(url);
        }

        public void showSuccessDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.successdialog);
            final ImageView img = dialog.findViewById(R.id.img_cross);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    SendMoney.openBottomSheet.dismiss();


                    getActivity().finish();
                }
            });
            dialog.show();
        }

        private JsonObject sendmoneyobject() {
            String User_id = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("USERID");
            String wallet_id = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("WalletId");


            Log.e("============>",User_id);



            SendMoneyobject.UserWalletHistory header = new SendMoneyobject.UserWalletHistory();
            header.setGroupId(null);
            header.setId(0);

            header.setWalletId(wallet_id);

            if(selectedRadioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.bankaccount))){
                header.setCardId(bankTypeItem.getId());

            }
            else{
                header.setCardId(null);
            }

            header.setAmount(Integer.parseInt(editTextamount.getText().toString()));
            header.setTransactionTypeId(21);
            header.setReasonTypeId(17);
            header.setIsActive(true);
            header.setComments(null);
            SendMoneyobject requestModel = new SendMoneyobject(header);
            requestModel.setUserId(User_id);

            requestModel.setRecieverUserName(phonenumber);

            return new Gson().toJsonTree(requestModel).getAsJsonObject();}


        private void getcardslist() {
            String User_id = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("USERID");
            Log.e("============>",User_id);
            mdilogue.show();
            ApiService service = ServiceFactory.createRetrofitService(getContext(), ApiService.class);
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
                                getbank = new ArrayList<>();

                                nocard.setVisibility(View.GONE);
                                addcard.setVisibility(View.GONE);

                                for (GetCards.ListResult data : getCards.getListResult()
                                ) {

                                    String number = data.getCardNumber();
                                    String mask = number.replaceAll("\\w(?=\\w{4})", "*");

                                    getbank.add(new TypeItems(data.getId(), data.getBankName() + "\n"+ mask));
                                }


                                bankAdapter = new SpinnerTypeArrayAdapterr(getContext(), getbank);
                                bankAdapter.setDropDownViewResource(R.layout.simple_spinnerdropdown_item);
                                bankname.setAdapter(bankAdapter);

                                bankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                        bankTypeItem = (TypeItems) bankname.getSelectedItem();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        // DO Nothing here
                                    }
                                });
                            }else{
                                 nocard.setVisibility(View.VISIBLE);
                                 addcard.setVisibility(View.VISIBLE);
                            }
                        }



                    });}

        private void getwalletBalance() {
            String User_id = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("USERID");
            Log.e("============>",User_id);
            mdilogue.show();
            ApiService service = ServiceFactory.createRetrofitService(getContext(), ApiService.class);
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

                                walletbalance.setText(walletBalanceResponse.getResult().getWalletBalance().toString());
//
//                                Log.d("WalletBalanceAPI", walletBalanceResponse.getResult().getWalletBalance() + "");
//                                Log.d("WalletBalanceAPI", walletBalance.getText().toString() + "");

                            }else{

                                Log.d("Not Sucess", "Why Not");

                            }
                        }


                    });}






    }}


