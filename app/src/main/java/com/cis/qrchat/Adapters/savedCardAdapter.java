package com.cis.qrchat.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.cis.qrchat.Model.AddMoneytoUserWalletRequest;
import com.cis.qrchat.Model.AddMoneytoUserWalletResponse;
import com.cis.qrchat.Model.GetCards;
import com.cis.qrchat.Model.GetWalletBalanceResponse;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRWalletActivity;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;
import static com.cis.qrchat.service.APIConstantURL.AddMoneytoUserWallet;

public class savedCardAdapter extends RecyclerView.Adapter<savedCardAdapter.MyViewHolder> {

    private Context context;
    private List<GetCards.ListResult> cardslist;
    private AdapterListener listener;
    String selectedItemID,QRPIN;
    int selectedPO;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private RadioButton lastCheckedRB = null;

String amount;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cardname, cardnumber,time;
        public ImageView thumbnail;
        RadioButton selectedCard;
        LinearLayout mpinlayout;
      PinEntryEditText txtmPinEntry;
        public MyViewHolder(View view) {
            super(view);
            cardname = view.findViewById(R.id.cardname);
            cardnumber = view.findViewById(R.id.cardnumber);
            thumbnail = view.findViewById(R.id.thumbnail);
            selectedCard =  view.findViewById(R.id.selectCard);
            mpinlayout = view.findViewById(R.id.mpinlayout);
            txtmPinEntry = view.findViewById(R.id.mpin_entry);

            mpinlayout.setVisibility(View.GONE);

            mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                    .setContext(context)
                    .setTheme(R.style.Custom)
                    .build();

        }
    }


    public savedCardAdapter(Context context, List<GetCards.ListResult> cardsList,AdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.cardslist = cardsList;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.savedcardsdata, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        final int langID = SharedPrefsData.getInstance(context).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(context, "ar");

        }
        else
            updateResources(context, "en-US");


        final GetCards.ListResult cardsdata = cardslist.get(position);
        if(cardsdata.getCardTypeId() == 1 )
            holder.cardname.setText(cardsdata.getBankName() + " Debit Card");
        else
            holder.cardname.setText(cardsdata.getBankName() + " Credit Card");

        String number = cardsdata.getCardNumber();
        String mask = number.replaceAll("\\w(?=\\w{4})", "*");

        //  textView.setText(mask);
        holder.cardnumber.setText(mask);

        holder.selectedCard.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (group, checkedId) -> {
            if (lastCheckedRB != null) {
                lastCheckedRB.setChecked(false);
            }
            holder.mpinlayout.setVisibility(View.GONE);

            //store the clicked radiobutton
            lastCheckedRB = holder.selectedCard;

        });
//Log.e("=========>111111111111",amount);
        holder.selectedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete Specific Image
              //  holder.mpinlayout.setVisibility(View.VISIBLE);
                selectedItemID = cardsdata.getId();
                selectedPO = position;
                enterpin(context, cardslist.get(position));

                    // Its visible



            }
        });




        if(cardsdata.getCardNumber().startsWith("4")){
            Picasso.with(context )
                    .load(R.drawable.visa)
                    .error(R.drawable.visa)
                    .placeholder(R.drawable.visa)
                    .into(holder.thumbnail);}

//        Glide.with(context)
//                .load(contact.getImages())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
    }

    private void enterpin(Context context, GetCards.ListResult listResult) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
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
                    Log.e("===========listresult",listResult.getId());
                    listener.onCardSelected(listResult, QRPIN);
                    dialog.dismiss();


                }
                else{
                    Toast.makeText(
                            context,
                            context.getResources().getString(R.string.pleaseenterpin), Toast.LENGTH_SHORT)
                            .show();

                }
            }
        });
        dialog.show();
    }




    @Override
    public int getItemCount() {
        return cardslist.size();
    }


    public interface AdapterListener {
        void onCardSelected(GetCards.ListResult contact, String s);
    }
}
