package com.cis.qrchat.Views.account;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.AutoCompleteUserAdapter;
import com.cis.qrchat.Adapters.SpinnerTypeArrayAdapter;
import com.cis.qrchat.Model.GetRegions;
import com.cis.qrchat.Model.Regionitems;
import com.cis.qrchat.Model.RegisterResponse;
import com.cis.qrchat.Model.Registerobject;
import com.cis.qrchat.Model.RegistrationOTPobject;
import com.cis.qrchat.Model.RegistrationOTPresp;
import com.cis.qrchat.R;
import com.cis.qrchat.common.CommonMethods;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

public class RegisterActivity extends AppCompatActivity {


    Button registerBtn;
    EditText name_et, number_et;
    AutoCompleteTextView region_et;
    CircleImageView userImg;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/QRChat_Profile";
    private String mCurrentPhotoPath = null;
    private String extension;
    Boolean isselectedCamera = false;
    Boolean isFarmerHaveImg = false;
    CheckBox termsandcontions;
    private Subscription mSubscription;
    int Region_ID;
    TextView signin;
    private SpotsDialog mdilogue;
    AutoCompleteUserAdapter regionAdapter;
    private List<Regionitems> get_region = new ArrayList<>();
    private String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
    };

    String[] arr = {"93 - Afghanistan", "213 - Algeria", "672 - Antarctica", "54 - Argentina", "61 - Australia", "880 - Bangladesh", "32 - Belgium", "975 - Bhutan",
            "55 - Brazil", "1 - Canada", "57 - Colombia", "45 - Denmark", "20 - Egypt", "33 - France", "49 - Germany", "299 - Greenland", "852 - Hong Kong",
            "91 - India", "98 - Iran", "964 - Iraq", "353 - Ireland", "39 - Italy", "81 - Japan", "254 - Kenya", "965 - Kuwait", "60 - Malaysia", "960 - Maldives",
            "52 - Mexico", "95 - Myanmar", "977 - Nepal", "31 - Netherlands", "64 - New Zealand", "850 - North Korea", "92 - Pakistan", "63 - Philippines",
            "974 - Qatar", "7 - Russia", "966 - Saudi Arabia", "65 - Singapore", "82 - South Korea", "34 - Spain", "66 - Thailand", "971 - United Arab Emirates",
            "44 - United Kingdom", "1 - United States", "84 - Vietnam", "263 - Zimbabwe"};

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");
        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_register);

        init();
        setViews();

        setupHyperlink();
    }

    private void init() {

        registerBtn = findViewById(R.id.registerBtn);
        name_et = findViewById(R.id.name_et);
        region_et = findViewById(R.id.region_et);
        number_et = findViewById(R.id.number_et);
        userImg = findViewById(R.id.userImg);
        termsandcontions = findViewById(R.id.termsandconditions);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        signin = findViewById(R.id.signin);
    }

    private void setViews() {




        getregion();


        region_et.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Region_ID = regionAdapter.getId(position);


                Log.e("===>Region_ID===", Region_ID+"");
            }
        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permissons()){
                    showPictureDialog();
                }

            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginscreen = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginscreen);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validations()) {


                    userRegisteration();

                }

            }
        });
    }

    private void getregion()
       {
           ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
            mSubscription = service.getregions(APIConstantURL.getRegion)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetRegions>() {
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
                         //   showDialog(Visit_request_Activity.this, getString(R.string.server_error));
                        }

                        @Override
                        public void onNext(GetRegions getRegions) {
                            if (getRegions.getListResult() != null) {


                                get_region = new ArrayList<>();



                                for (GetRegions.ListResult data : getRegions.getListResult()
                                ) {
                                  get_region.add(new Regionitems(data.getId(), data.getCode(), data.getName()));

                                    regionAdapter = new AutoCompleteUserAdapter(RegisterActivity.this, get_region);

                                    region_et.setThreshold(1);
                                    region_et.setAdapter(regionAdapter);


                                }
                            }

                        }
                    });

        }



    private void userRegisteration() {
        mdilogue.show();
        JsonObject object = Regesterobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postregister(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterResponse>() {

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
                    public void onNext(RegisterResponse registerResponse) {

                        if(HttpsURLConnection.HTTP_OK == 200) {
                            if(registerResponse.getMessage().equalsIgnoreCase("success")){
                                getRegistrationOTP();
                            }
                            else {
                                showDialog(RegisterActivity.this, registerResponse.getDescription());
                            }


//
                        }
                        else {
                            showDialog(RegisterActivity.this, getString(R.string.server_error));
                        }
                    }
                });

    }

    private void getRegistrationOTP() {
        JsonObject object = Regesterotpobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postregisterotp(object)
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
                            Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                            intent.putExtra("FromScreen", "Register");

                            intent.putExtra("phonenumber",number_et.getText().toString().trim());
                            startActivity(intent);
                        }

                    }
                });

    }

    private JsonObject Regesterotpobject() {
        RegistrationOTPobject requestModel = new RegistrationOTPobject();

        requestModel.setPhoneNumber(number_et.getText().toString().trim());

        return new Gson().toJsonTree(requestModel).getAsJsonObject();}

    private JsonObject Regesterobject() {

        Registerobject requestModel = new Registerobject();

        requestModel.setId(null);
        requestModel.setName(name_et.getText().toString().trim());
        requestModel.setPhoneNumber(number_et.getText().toString().trim());

        requestModel.setFileLocation(null);
        if(mCurrentPhotoPath!=null){
        requestModel.setFileName(doFileUpload(new File(mCurrentPhotoPath)));
            requestModel.setFileExtension(".jpg");}
        requestModel.setRegionId(Region_ID);


        return new Gson().toJsonTree(requestModel).getAsJsonObject();}


    private String doFileUpload(File f){


        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //String encoded = Base64.encodeToString(bytes, 0);
        String finalString =android.util.Base64.encodeToString(bytes,0);



        return finalString;

// Receiving side

    }


    private void setupHyperlink() {
        TextView hyperlink = findViewById(R.id.hyperlink);
        hyperlink.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private boolean validations() {

        int length = number_et.getText().length();
        int length2 = name_et.getText().length();

        if (TextUtils.isEmpty(name_et.getText().toString())) {
            showDialog(RegisterActivity.this, getResources().getString(R.string.pleaseentername));
            return false;
        }
        else if (length2 < 4) {
            showDialog(RegisterActivity.this, getResources().getString(R.string.minimumletters));
            return false;
        } else if (TextUtils.isEmpty(region_et.getText().toString().trim())) {
            showDialog(RegisterActivity.this, getResources().getString(R.string.pleaseenterregion));
            return false;
        }else if (TextUtils.isEmpty(number_et.getText().toString().trim())) {
            showDialog(RegisterActivity.this, getResources().getString(R.string.pleaseenternumber));
            return false;
        } else if (length < 4) {
            showDialog(RegisterActivity.this, getResources().getString(R.string.minimumNumber));
            return false;
        } else if (!termsandcontions.isChecked()) {
            showDialog(RegisterActivity.this, getResources().getString(R.string.pleaseacceptTC));
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

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(RegisterActivity.this);

        pictureDialog.setTitle(getResources().getString(R.string.selectAction));
        String[] pictureDialogItems = {
                getResources().getString(R.string.selectfromgallery),
                getResources().getString(R.string.selectfromcamera)};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);}

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == this.RESULT_CANCELED) {
                return;
            }
            if (requestCode == GALLERY) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        String path = saveImage(bitmap);
                        Log.e("path===", path);
                        //   Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                        userImg.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        //  Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

            } else if (requestCode == CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                userImg.setImageBitmap(thumbnail);
              saveImage(thumbnail);

                //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }
        }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            mCurrentPhotoPath = f.getAbsolutePath();
            extension = mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("."));
            return f.getAbsolutePath();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

//    public String saveImage(Bitmap myBitmap) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File wallpaperDirectory = new File(
//                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
//        if (!wallpaperDirectory.exists()) {
//            wallpaperDirectory.mkdirs();
//        }
//
//        try {
//            File f = new File(wallpaperDirectory, "My Image");
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            fo.write(bytes.toByteArray());
//            MediaScannerConnection.scanFile(getApplicationContext(),
//                    new String[]{f.getPath()},
//                    new String[]{"image/jpeg"}, null);
//            fo.close();
//            mCurrentPhotoPath = f.getAbsolutePath();
//            extension = mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("."));
//
//            return f.getAbsolutePath();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        return "";
//    }

    public boolean permissons() {

        Boolean ispermissiongranted = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !CommonMethods.areAllPermissionsAllowed(this, PERMISSIONS_REQUIRED)) {

            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, CommonMethods.PERMISSION_CODE);


        } else {
            try {
//                showPictureDialog();
            } catch (Exception e) {
                e.getMessage();
            }
            ispermissiongranted = true;
        }

        return ispermissiongranted;
}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonMethods.PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        showPictureDialog();
                    } catch (Exception e) {

                    }
                }
                break;
        }
    }



}