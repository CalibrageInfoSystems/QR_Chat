package com.cis.qrchat.Views.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cis.qrchat.Adapters.ChatListAdapter;
import com.cis.qrchat.Model.Chat_data;
import com.cis.qrchat.Model.GetWalletBalanceResponse;
import com.cis.qrchat.Model.Profile_Data;
import com.cis.qrchat.Model.Profileresponse;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.OTPActivity;
import com.cis.qrchat.Views.account.RegisterActivity;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
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

import static android.content.Context.WINDOW_SERVICE;
import static com.cis.qrchat.common.CommonUtil.updateResources;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    public static String TAG = "ProfileFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Profile_Data> profile_List = new ArrayList<>();
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    TextView noRecords;
    RecyclerView recyclerView;
    ImageView profileimage;
    ProfileRecylerviewAdapter profileRecylerviewAdapter;
    ImageView profileImg;
    CircleImageView myImage;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView displayName, qrpayid;
    TextView editProfileBtn,qrcode;
    private Dialog _QRpopupdialog;
    ImageView Qrimage;
    String qrimage;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_profile, container, false);

    //   profileImg = view.findViewById(R.id.profileimage);

        final int langID = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(getContext(), "ar");
        }

        else
            updateResources(getContext(), "en-US");


        init(view);

        setviews();

        return view;}

    @Override
    public void onResume() {
        super.onResume();

        final int langID = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(getContext(), "ar");
        }

        else
            updateResources(getContext(), "en-US");

    }

    private void setviews() {

        getuserdata();

        String fullname = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("FULLNAME");
        String username = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("USERNAME");

        displayName.setText(fullname);
        qrpayid.setText(username);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editProfile =  new Intent(getContext(), myprofile.class);
                startActivity(editProfile);
            }
        });

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               QRpopup();
            }
        });
        //Picasso.with(getContext()).load("http://183.82.111.111/QRChatFileRepository/FileRepository///2020//12//08//QRCode/20201208084223753.png").error(R.drawable.ic_user).into(myImage);
    }

    private void QRpopup() {
        _QRpopupdialog = new Dialog(getContext());
        _QRpopupdialog = new Dialog(getContext());
        _QRpopupdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _QRpopupdialog.setCancelable(false);
        _QRpopupdialog.setContentView(R.layout.showqrcode);
        _QRpopupdialog.show();

        Button submitGroupBtn, cancelBtn;


        cancelBtn = _QRpopupdialog.findViewById(R.id.cancelBtn);
        Qrimage = _QRpopupdialog.findViewById(R.id.imageView);

        Picasso.with(getContext()).load(qrimage).error(R.drawable.ic_user).into(Qrimage);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _QRpopupdialog.dismiss();
            }
        });
    }



    private void getuserdata() {
        String User_name = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("USERNAME");
        Log.e("============>",User_name);
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(getContext(), ApiService.class);
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
if(profileresponse.getFileUrl()!=null) {
                                Picasso.with(getContext()).load(profileresponse.getFileUrl()).error(R.drawable.ic_user).into(myImage);
                            }else{
    Picasso.with(getContext()).load(R.drawable.ic_user).error(R.drawable.ic_user).into(myImage);
                            }
                             qrimage = profileresponse.getQrContactImage();
                            Picasso.with(getContext()).load(qrimage).error(R.drawable.ic_user).into(Qrimage);

                        }else{


                        }
                    }



                });}



    private void init(View view) {
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext( getContext())
                .setTheme(R.style.Custom)
                .build();

        qrpayid =  view.findViewById(R.id.qrpayId);
        displayName = view.findViewById(R.id.displayName);
        editProfileBtn = view.findViewById(R.id.editProfile);
        qrcode = view.findViewById(R.id.qrcode);
        recyclerView = (RecyclerView) view.findViewById(R.id.profileRecyclerview);
      //  profileimage = view.findViewById(R.id.profileimage);
        profile_List = new ArrayList<>();
        profileRecylerviewAdapter = new ProfileRecylerviewAdapter(getContext(), profile_List);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(profileRecylerviewAdapter);
        fetchchatdata();

         myImage = (CircleImageView) view. findViewById(R.id.imageView1);
    }





//        WindowManager manager = (WindowManager)getActivity().getSystemService(WINDOW_SERVICE);
//        Display display = manager.getDefaultDisplay();
//        Point point = new Point();
//        display.getSize(point);
//        int width = point.x;
//        int height = point.y;
//        int smallerDimension = width < height ? width : height;
//        smallerDimension = smallerDimension * 4/4;
//
//        //Encode with a QR Code image
//        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder("http://183.82.111.111/QRChatFileRepository/FileRepository///2020//12//08//QRCode/20201208084223753.png",
//                null,
//                Contents.Type.TEXT,
//                BarcodeFormat.QR_CODE.toString(),
//                smallerDimension);
//        try {
//            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
//            ImageView myImage = (ImageView) view. findViewById(R.id.imageView1);
//            myImage.setImageBitmap(bitmap);
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }



    private void fetchchatdata() {

        int[] covers = new int[]{
                R.drawable.ic_qr_code,
                R.drawable.ic_lens,
                R.drawable.ic_heart,
                R.drawable.ic_happy,
                R.drawable.ic_account,
                R.drawable.ic_settings,


        };

        Profile_Data a = new Profile_Data( covers[0], getResources().getString(R.string.qrpayaccount));
        profile_List.add(a);
        a = new Profile_Data( covers[1], getResources().getString(R.string.mylifeshots));
        profile_List.add(a);
        a = new Profile_Data( covers[2], getResources().getString(R.string.favorites));
        profile_List.add(a);
        a = new Profile_Data( covers[3], getResources().getString(R.string.stickersgallery));
        profile_List.add(a);
        a = new Profile_Data( covers[4], getResources().getString(R.string.accountsettings));
        profile_List.add(a);
        a = new Profile_Data( covers[5], getResources().getString(R.string.appsettings));
        profile_List.add(a);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        // Refresh tab data:

        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
            getuserdata();
        }
    }
}