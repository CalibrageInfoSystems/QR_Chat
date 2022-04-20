package com.cis.qrchat.Views.home;

import androidx.annotation.RequiresApi;
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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.cis.qrchat.Chat.Groupchat.UrlPreviewInfo;
import com.cis.qrchat.Chat.Groupchat.WebUtils;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.LoginActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.util.List;

public class HadiatMoney extends AppCompatActivity {
    EditText amount_et,wishes;

    TextView cover,amounttext;

    Button subitBtn;
    Toolbar toolbar;
    public static String channelUrl,Userid;
    GroupChannel mChannel;
    Integer Amount;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadiat_money);
        intviews();
        setviews();
        settoolbar();
    }
    private void intviews() {
        amount_et = findViewById(R.id.amount_et);
        wishes = findViewById(R.id.wishes_et);
        cover = findViewById(R.id.cover);
        amounttext = findViewById(R.id.amounttext);
        subitBtn = findViewById(R.id.subitBtn);


    }
    private void setviews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        channelUrl  = extras.getString("groupChannelUrl");}
      //  channelUrl = getIntent().getStringExtra("groupChannelUrl");
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
        cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(HadiatMoney.this, HaditCoverActivity.class);
                    startActivity(intent);

                    }


            });
        amount_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 ){
                    amounttext.setText(s + ".00" + getResources().getString(R.string.qar));
                }
                else{
                    amounttext.setText( "0.00" + getResources().getString(R.string.qar));
                }


                try {
                    Amount = Integer.valueOf(s.toString());
                }
                catch (NumberFormatException e)
                {
                    Amount = 0;
                }

            }
        });

        subitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(amount_et.getText().toString())) {
                    showDialog(HadiatMoney.this, getResources().getString(R.string.pleaseentergiftmoney));
                }
                else if(Amount  > 500){
                    showDialog(HadiatMoney.this, getResources().getString(R.string.pleaseentergiftmoneylimit));
                }else if (TextUtils.isEmpty(wishes.getText().toString())) {
                    showDialog(HadiatMoney.this, getResources().getString(R.string.pleaseenterwishes));
                }
                else{
                    String text = "money* " + wishes.getText().toString();
                    Log.e("===========>567", text);
                    sendUserMessage(text);
                    enterpin(HadiatMoney.this, amounttext.getText().toString());
                    //showSuccessDialog(HadiatMoney.this, amounttext.getText().toString());

                }

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
                if (pinEntry.getText() != null & pinEntry.getText().toString().trim() != "" & !TextUtils.isEmpty(pinEntry.getText()) & pinEntry.getText().length() == 4) {
                    dialog.dismiss();
                    showSuccessDialog(HadiatMoney.this, msg);
                }
                else{
                    Toast.makeText(
                            HadiatMoney.this,
                            getResources().getString(R.string.pleaseenterpin), Toast.LENGTH_SHORT)
                            .show();

                }
            }
        });
        dialog.show();
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

                    if (this != null) {
                        Toast.makeText(HadiatMoney.
                                this,
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
                            if (this!= null) {
                                Toast.makeText(
                                       HadiatMoney.this,
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.accountsettings));
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
                finish();
            }
        });
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
            //    SendMoney.openBottomSheet.dismiss();


              finish();
            }
        });
        dialog.show();
    }

}
