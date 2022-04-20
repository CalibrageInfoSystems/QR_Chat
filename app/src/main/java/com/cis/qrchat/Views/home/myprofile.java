package com.cis.qrchat.Views.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cis.qrchat.Adapters.AutoCompleteUserAdapter;
import com.cis.qrchat.Adapters.SpinnerTypeArrayAdapter;
import com.cis.qrchat.Model.GetGender;
import com.cis.qrchat.Model.GetRegions;
import com.cis.qrchat.Model.GetUserdetails;
import com.cis.qrchat.Model.GetrequestResp;
import com.cis.qrchat.Model.LoginResponse;
import com.cis.qrchat.Model.Regionitems;
import com.cis.qrchat.Model.RegisterResponse;
import com.cis.qrchat.Model.Registerobject;
import com.cis.qrchat.Model.TypeItem;
import com.cis.qrchat.Model.UpdateUserobject;
import com.cis.qrchat.Model.UpdateUserres;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.LoginActivity;
import com.cis.qrchat.Views.account.RegisterActivity;
import com.cis.qrchat.common.CommonMethods;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class myprofile extends AppCompatActivity {
    Button updateBtn;
    EditText name_et, number_et,status_et,location_et,email_et;
    AutoCompleteTextView region_et;
    ImageView userImg;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/QRChat_Profile";
    private String mCurrentPhotoPath = null;
    private Subscription mSubscription;
    int Region_ID;
    String User_id,Fullname, mobilenumber,email;
    Toolbar toolbar;
    private SpotsDialog mdilogue;
    AutoCompleteUserAdapter regionAdapter;
    private List<Regionitems> get_region = new ArrayList<>();
    Spinner gendertype;
    private ArrayList<TypeItem> getgender = new ArrayList<>();
    SpinnerTypeArrayAdapter genderAdapter;
    TypeItem genderTypeItem;
    String Image_url;
Integer  selected_regionid ,genderid;
    private String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
    };
    private LoginResponse catagoriesList;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");
        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_myprofile);
        intviews();
        setviews();
        settoolbar();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void intviews() {
        name_et = findViewById(R.id.name_et);
        status_et = findViewById(R.id.status_et);
        region_et = findViewById(R.id.region_et);
        number_et = findViewById(R.id.number_et);
        location_et = findViewById(R.id.location_et);
        email_et = findViewById(R.id.email_et);
        userImg = findViewById(R.id.userImg);
        gendertype = findViewById(R.id.gendertype);
        updateBtn = findViewById(R.id.updateBtn);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2) {
            updateResources(this, "ar");
            gendertype.setTextDirection(View.TEXT_DIRECTION_RTL);
            gendertype.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        else {
            updateResources(this, "en-US");
        }

    }
    private void setviews() {
         User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
         Log.e("============>",User_id);
getuserdetails(User_id);

        catagoriesList = SharedPrefsData.getCreatedUser(myprofile.this);
        Fullname = catagoriesList.getResult().getFullName();
        mobilenumber =  catagoriesList.getResult().getMobileNumber();
        email =  catagoriesList.getResult().getEmail();
        Log.e("============>",Fullname + "======"+mobilenumber + email);
        name_et.setText(Fullname);
        number_et.setText(mobilenumber);
        if(email!=null)
            email_et.setText(email);
        else{
            email_et.setText("");
        }
        selected_regionid =catagoriesList.getResult().getRegionId();

        Log.e("==========>",selected_regionid+"");

        getregion();
        getgender();


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
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validation()){
                    UpdateUser();
                }

            }
        });


        if (!TextUtils.isEmpty(catagoriesList.getResult().getUserImage()))

//        Glide.with(this)
//                .load(catagoriesList.getResult().getUserImage())
//                .apply(RequestOptions.circleCropTransform())
//
//                .into(userImg);

        Picasso.with(this).load(catagoriesList.getResult().getUserImage()).error(R.drawable.ic_user).into(userImg);
    }

    private void getuserdetails(String user_id) {
        mdilogue.show();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getuserdetails(APIConstantURL.getuserdetails + user_id)
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
                            name_et.setText(getUserdetails.getFullName());
                            number_et.setText(getUserdetails.getPhoneNumber());

                            email_et.setText(getUserdetails.getEmail());

                            selected_regionid = getUserdetails.getRegionId();
                            genderid = getUserdetails.getGenderTypeId();
                            getgender();
             status_et.setText(getUserdetails.getStatus());
                            location_et.setText(getUserdetails.getLocation());
                            if(getUserdetails.getFileUrl()== null){
                                Picasso.with(myprofile.this).load(R.drawable.ic_user).error(R.drawable.ic_user).into(userImg);
                            }else{
                            Picasso.with(myprofile.this).load(getUserdetails.getFileUrl()).error(R.drawable.ic_user).into(userImg);
                                Image_url = getUserdetails.getFileUrl();}

//                            Glide.with(myprofile.this)
//                                    .load(getUserdetails.getFileUrl())
//
//                                    .apply(RequestOptions.circleCropTransform())
//                                    .into(userImg);
                        }else{


                        }
                    }



                });}



    private void UpdateUser() {
        mdilogue.show();
        JsonObject object =UpdateUserobject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postupdateuser(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateUserres>() {

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
                       showDialog(myprofile.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(UpdateUserres updateUserres) {
                        if(HttpsURLConnection.HTTP_OK == 200) {
                            finish();
//
//                            Toast.makeText(myprofile.this, updateUserres.getMessage(), Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(myprofile.this, HomeActivity.class);
//                            startActivity(intent);
//
                        }
                        else {
                            showDialog(myprofile.this, updateUserres.getMessage());
                        }
                    }


                });

    }

    private JsonObject UpdateUserobject() {

        UpdateUserobject requestModel = new UpdateUserobject();

        requestModel.setId(User_id);
        requestModel.setName(name_et.getText().toString().trim());
        requestModel.setPhoneNumber(number_et.getText().toString().trim());
        requestModel.setEmail(email_et.getText().toString().trim());
        requestModel.setStatus(status_et.getText().toString().trim());
        requestModel.setLocation(location_et.getText().toString().trim());
        requestModel.setGenderTypeId(genderTypeItem.getId());

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

    private boolean Validation() {
        int length = number_et.getText().length();

        if (TextUtils.isEmpty(name_et.getText().toString())) {
            showDialog(myprofile.this, getResources().getString(R.string.pleaseentername));
            return false;
        }
        else if (TextUtils.isEmpty(email_et.getText().toString().trim())) {
            showDialog(myprofile.this, getResources().getString(R.string.pleaseenteremail));
            return false;}
        else if (TextUtils.isEmpty(region_et.getText().toString().trim())) {
            showDialog(myprofile.this, getResources().getString(R.string.pleaseenterregion));
            return false;}

      else if (TextUtils.isEmpty(number_et.getText().toString().trim())) {
            showDialog(myprofile.this, getResources().getString(R.string.pleaseenternumber));
            return false;
        } else if (length < 4) {
            showDialog(myprofile.this, getResources().getString(R.string.minimumNumber));
            return false;
        }
//        else if (TextUtils.isEmpty(status_et.getText().toString().trim())) {
//            showDialog(myprofile.this, getResources().getString(R.string.pleaseenterstatus));
//            return false;
//        }
       else if (gendertype.getSelectedItemPosition() == 0) {
            showDialog(myprofile.this, getResources().getString(R.string.selectgender));
            return false;
        }
        else if (TextUtils.isEmpty(location_et.getText().toString().trim())) {
            showDialog(myprofile.this, getResources().getString(R.string.pleaseenterlocation));
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
    private void getgender() {
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getgender(APIConstantURL.getGender)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetGender>() {
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
                    public void onNext(GetGender getGender) {
       if (getGender.getListResult() != null) {


                            getgender = new ArrayList<>();
                            getgender.add(new TypeItem(0, "Please Select"));


                            for (GetGender.ListResult data : getGender.getListResult()
                            ) {
                                getgender.add(new TypeItem(data.getId(), data.getName()));
                            }

                            genderAdapter = new SpinnerTypeArrayAdapter(myprofile.this, getgender);
                            genderAdapter.setDropDownViewResource(R.layout.simple_spinnerdropdown_item);
                            gendertype.setAdapter(genderAdapter);
                            gendertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    genderTypeItem = (TypeItem) gendertype.getSelectedItem();




                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    // DO Nothing here
                                }
                            });
           for (int i = 0; i < getGender.getListResult().size(); i++) {
               if (getGender.getListResult().get(i).getId() == genderid) {
                   //Region_ID = selected_regionid;

                   gendertype.setSelection(i+1);
               }
           }

                        }
                    }});}




    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
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
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(myprofile.this);

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
      //  File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        File wallpaperDirectory   =  getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

            return f.getAbsolutePath();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
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

                                regionAdapter = new AutoCompleteUserAdapter(myprofile.this, get_region);

                                region_et.setThreshold(1);
                                region_et.setAdapter(regionAdapter);


                            }


                            for (int i = 0; i < getRegions.getListResult().size(); i++) {
                                if (getRegions.getListResult().get(i).getId() == selected_regionid) {
                                    Region_ID = selected_regionid;

                                    region_et.setText(getRegions.getListResult().get(i).getCode());
                                }
                            }
                        }

                    }
                });

    }

}
