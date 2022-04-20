package com.cis.qrchat.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Model.DeleteCard;
import com.cis.qrchat.Model.Explore_data;
import com.cis.qrchat.Model.GetCards;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.ProfileSubActivities.AddCardDetails;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class AddedbankdetailsAdapter extends RecyclerView.Adapter<AddedbankdetailsAdapter.MyViewHolder> {
    private Context context;
    private List<GetCards.ListResult>cardslist;
    private AdapterListener listener;
String selectedItemID;
int selectedPO;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cardname, cardnumber,time, expiryDate, cardholderName;
        public ImageView thumbnail;
        Button removecard;
        LinearLayout backgroundlyt;

        public MyViewHolder(View view) {
            super(view);
            cardname = view.findViewById(R.id.cardname);
            cardnumber = view.findViewById(R.id.cardnumber);
            thumbnail = view.findViewById(R.id.thumbnail);
            expiryDate = view.findViewById(R.id.expirydate);
            cardholderName= view.findViewById(R.id.cardholdername);
            backgroundlyt = view.findViewById(R.id.backgroundlyt);
            removecard = view.findViewById(R.id.removecard);
            mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                    .setContext(context)
                    .setTheme(R.style.Custom)
                    .build();

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    listener.onContactSelected(cardslist.get(getAdapterPosition()));
//                }
//            });
        }
    }


    public AddedbankdetailsAdapter(Context context, List<GetCards.ListResult> cardsList, AdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.cardslist = cardsList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addedcards_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


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

        holder.cardholderName.setText(cardsdata.getCardName());
        holder.expiryDate.setText(cardsdata.getExpiredOn());


        String mask = number.replaceAll("\\w(?=\\w{4})", "*");

      //  textView.setText(mask);
        holder.cardnumber.setText(mask);

        holder.removecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete Specific Image

                selectedItemID = cardsdata.getId();
                selectedPO = position;
                listener.onContactSelected(cardslist.get(position));


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



    @Override
    public int getItemCount() {
        return cardslist.size();
    }

    public interface AdapterListener {
        void onContactSelected(GetCards.ListResult contact);
    }
}
