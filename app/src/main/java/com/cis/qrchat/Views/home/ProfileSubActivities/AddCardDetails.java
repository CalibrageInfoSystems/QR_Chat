package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.cis.qrchat.Adapters.SpinnerTypeArrayAdapter;
import com.cis.qrchat.Model.Addcardrequestobject;
import com.cis.qrchat.Model.Addcardresponse;
import com.cis.qrchat.Model.GetCardtype;
import com.cis.qrchat.Model.GetGender;
import com.cis.qrchat.Model.RegisterResponse;
import com.cis.qrchat.Model.TypeItem;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.RegisterActivity;
import com.cis.qrchat.Views.home.myprofile;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;
import static java.util.Calendar.*;

public class AddCardDetails extends AppCompatActivity {
    CardForm cardForm;
    Button addcard, cancel;
    AlertDialog.Builder alertBuilder;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    Spinner cardtype;
    private ArrayList<TypeItem> getCardtype = new ArrayList<>();
    SpinnerTypeArrayAdapter cardAdapter;
    TypeItem cardTypeItem;
    EditText bankname_et,cardholdername, cardNumber, expdate, cvv;
    TextView bankName, ccardNumber,ccardName, ccexpdate;
    boolean ISDEFAULT = false;
    Toolbar toolbar;
    int langID;
    String _pickedDate;
    Calendar myCalendar = getInstance();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_card_details);
        addcard = findViewById(R.id.btnaddCard);
        cancel = findViewById(R.id.btnCancel);
        cardtype = findViewById(R.id.cardtype);
        bankname_et = findViewById(R.id.bankname_et);
        cardholdername = findViewById(R.id.cardholdername_et);
        cardNumber = findViewById(R.id.cardNumber_et);
        expdate = findViewById(R.id.expdate_et);
        cvv = findViewById(R.id.cvv_et);

        bankName = findViewById(R.id.cardname);
        ccardNumber = findViewById(R.id.cardnumber);
        ccardName = findViewById(R.id.cardholdername);
        ccexpdate = findViewById(R.id.expirydate);

        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(this, "ar");
            cardtype.setTextDirection(View.TEXT_DIRECTION_RTL);
            cardtype.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        else
            updateResources(this, "en-US");

        bankname_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                bankName.setText(s);
            }
        });


        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String cardnumber = s.toString();
                String mask = cardnumber.replaceAll("\\w(?=\\w{4})", "*");

                ccardNumber.setText(mask);
            }
        });

        cardholdername.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ccardName.setText(s);
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(YEAR, year);
                myCalendar.set(MONTH, monthOfYear);
                myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        expdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                new DatePickerDialog(AddCardDetails.this, date, myCalendar
//                        .get(YEAR), myCalendar.get(MONTH),
//                        myCalendar.get(DAY_OF_MONTH)).show();


                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddCardDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                         _pickedDate =  _month + "/" + year;
                        expdate.setText(_pickedDate);
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();


            }
        });

        expdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ccexpdate.setText(s);
            }
        });




//        cardForm.cardRequired(true)
//                .expirationRequired(true)
//                .cvvRequired(false)
//                .postalCodeRequired(false)
//                .mobileNumberRequired(false)
//                .setup(AddCardDetails.this);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        settoolbar();
        getcardtype();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()){

                    Addcard();
                }

            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/YY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String currentDate = new SimpleDateFormat("MM/YY", Locale.getDefault()).format(new Date());
        int dateDifference = (int) getDateDiff(new SimpleDateFormat("MM/YY"), currentDate, sdf.format(myCalendar.getTime()));
        System.out.println("dateDifference: " + dateDifference);
        expdate.setText(_pickedDate);
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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
                finish();
            }
        });
    }
    private void Addcard() {
        mdilogue.show();
        JsonObject object = AddcardObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postAdd(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Addcardresponse>() {

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
                    public void onNext(Addcardresponse addcardresponse) {



                        if(HttpsURLConnection.HTTP_OK == 200) {


                            if(addcardresponse.getIsSuccess()) {
                                Toast.makeText(AddCardDetails.this, getString(R.string.cardsusscess), Toast.LENGTH_LONG).show();
//                                Intent receivemoneyscreen = new Intent(AddCardDetails.this, ShowCards.class);
//                                startActivity(receivemoneyscreen);
                                 finish();

                            }

                            else{
                                showDialog(AddCardDetails.this, getString(R.string.cardnumberexist));
                            }
                        //    getRegistrationOTP();
//
                        }
                    }
                });


    }

    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }

        return new String(chars).replace("٫",".");
    }


    private JsonObject AddcardObject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String date = arabicToDecimal(currentDate);


        Addcardrequestobject requestModel = new Addcardrequestobject();
        requestModel.setId(null);
        requestModel.setUserId(User_id);
        requestModel.setCardTypeId(cardTypeItem.getId());
        requestModel.setCardName(cardholdername.getText().toString().trim());
        requestModel.setCardNumber(cardNumber.getText().toString());
        requestModel.setExpiredOn(expdate.getText().toString());
        requestModel.setBankName(bankname_et.getText().toString().trim());
        requestModel.setUpdatedDate(date);
        requestModel.setCreatedDate(date);
        requestModel.setIsActive(true);
        requestModel.setIsDefault(ISDEFAULT);


        return new Gson().toJsonTree(requestModel).getAsJsonObject();}

    private boolean validation() {
        if (cardtype.getSelectedItemPosition() == 0) {
            showDialog(AddCardDetails.this, getResources().getString(R.string.selectcardtype));
            return false;
        }
       else if (TextUtils.isEmpty(bankname_et.getText().toString())) {
            showDialog(AddCardDetails.this, getResources().getString(R.string.pleaseenterbankname));
            return false;
        } else if (TextUtils.isEmpty(cardholdername.getText().toString().trim())) {
            showDialog(AddCardDetails.this, getResources().getString(R.string.pleaseentercardholdername));
            return false;
        }
        else if (TextUtils.isEmpty(cardNumber.getText().toString().trim())) {
            showDialog(AddCardDetails.this, getResources().getString(R.string.pleaseentercardnumber));
            return false;
        }
        else if (TextUtils.isEmpty(expdate.getText().toString().trim())) {
            showDialog(AddCardDetails.this, getResources().getString(R.string.pleaseenterexpirydate));
            return false;
        }
        else if (TextUtils.isEmpty(cvv.getText().toString().trim())) {
            showDialog(AddCardDetails.this, getResources().getString(R.string.pleaseentercvv));
            return false;
        }
        else if (cvv.getText().toString().trim().length()!=3) {
            showDialog(AddCardDetails.this, getResources().getString(R.string.cvvlength));
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

    private void getcardtype() {
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getcardtype(APIConstantURL.getCardtype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetCardtype>() {
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
                    public void onNext(GetCardtype getCardtypee) {

                        if (getCardtypee.getListResult() != null) {


                            getCardtype = new ArrayList<>();
                            getCardtype.add(new TypeItem(0, "Please Select Card Type"));


                            for (GetCardtype.ListResult data : getCardtypee.getListResult()
                            ) {
                                getCardtype.add(new TypeItem(data.getId(), data.getName()));
                            }

                            cardAdapter = new SpinnerTypeArrayAdapter(AddCardDetails.this, getCardtype);
                            cardAdapter.setDropDownViewResource(R.layout.simple_spinnerdropdown_item);
                            cardtype.setAdapter(cardAdapter);
                            cardtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    cardTypeItem = (TypeItem) cardtype.getSelectedItem();




                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // DO Nothing here
                                }
                            });


                        }
                    }});}
}


