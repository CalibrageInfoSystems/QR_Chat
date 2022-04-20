package com.cis.qrchat.SplitMoney;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Adapters.AddContactsAdapter;
import com.cis.qrchat.Model.AddGroupRequest;
import com.cis.qrchat.Model.AddGroupResponse;
import com.cis.qrchat.Model.AddMoneytoUserWalletRequest;
import com.cis.qrchat.Model.AddMoneytoUserWalletResponse;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.RegisterActivity;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


public class CreateNewGroupActivity extends AppCompatActivity implements AddContactsAdapter.AddContactsAdapterListner {
    private EditText editText;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    AddContactsAdapter contactListAdapter;
    TextView noRecords;
    RecyclerView recyclerView;
    private ArrayList<String> useridArr = new ArrayList<>();

    private void saveGroup() {
       // final String name = editText.getText().toString();





//        GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
//
//        GroupEntity group = new GroupEntity(name); // create group object that needs to be inserted into the database
//        group.gCurrency = "USD-($)"; // set default currency
//
//        // if database already contains group object return and do not save
//        List<GroupEntity> groups = groupViewModel.getAllGroupsNonLiveData();
//        for(GroupEntity item:groups) {
//            if(item.gName.equals(group.gName)) {
//                Toast.makeText(this, getResources().getString(R.string.groupalreadyexists), Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//
//        // else store the group object in database
//        groupViewModel.insert(group);
//        Toast.makeText(this, getResources().getString(R.string.groupcreatedsuccessfully), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_group_activity);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.createNewGroupToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getResources().getString(R.string.createnewgroup));

        // get reference to edit text-"Enter group name"
        editText = findViewById(R.id.createNewGroupName);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        noRecords = (TextView)findViewById(R.id.no_data);
        recyclerView = (RecyclerView)findViewById(R.id.contactsrecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GetUserContacts();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // fill toolbar menu with save group item
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_group_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // save the group to database if user clicks on save group item in the toolbar
        if(item.getItemId() == R.id.createNewGroupToolbarSaveGroupItem) {

            if (null !=contactListAdapter.getselectedContacts()) {
                for(Getcontactlist.ListResult itemm:  contactListAdapter.getselectedContacts())
                {
                    Log.d("AddeditContact","==> item :"+itemm.getContactId());
                    useridArr.add(itemm.getContactId());
                    Log.d("SelectedContadId", "ContactId====:"+useridArr);
                }
            }

            if (validations()){
                CreateGroup();
            }

        }
        return true;
    }

    private void GetUserContacts() {
        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getcontactlist(APIConstantURL.GetUserContacts+User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Getcontactlist>() {
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
                        //  showDialog(this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(Getcontactlist getcontactlist) {


                        if(getcontactlist.getListResult()!=null){

                            contactListAdapter = new AddContactsAdapter(CreateNewGroupActivity.this, getcontactlist.getListResult(), CreateNewGroupActivity.this);
                            recyclerView.setAdapter(contactListAdapter);
                        }



                    }});}

    @Override
    public void onContactSelected(Getcontactlist.ListResult contact) {

        List<String> userIds = new ArrayList<>();
        userIds.add(contact.getContactNumber());
        // createGroupChannel(userIds,true );


    }

    private void createGroupChannel(List<String> userIds, boolean distinct) {
        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

            }
        });
    }

    public boolean validations(){

        if (TextUtils.isEmpty(editText.getText().toString())){

            showDialog(CreateNewGroupActivity.this, getResources().getString(R.string.entergroupname));
            return false;
        }else if (useridArr.size() < 2) {

            showDialog(CreateNewGroupActivity.this, "Select atleast 2 Contacts");

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


        private void CreateGroup() {
            mdilogue.show();
            JsonObject object = createGroupObject();
            ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
            mSubscription = service.addgrouppage(object)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddGroupResponse>() {

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
                        public void onNext(AddGroupResponse addGroupResponse) {

                            if(addGroupResponse.getIsSuccess()) {

                                Toast.makeText(CreateNewGroupActivity.this, "Group Created Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{

                                Toast.makeText(CreateNewGroupActivity.this, addGroupResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }

        private JsonObject createGroupObject() {
            String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
            useridArr.add(User_id);

            Log.e("============>",User_id);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            AddGroupRequest requestModel = new AddGroupRequest();
            requestModel.setId(null);
            requestModel.setName(editText.getText().toString());
            requestModel.setRemarks("null");
            requestModel.setIsActive(true);
            requestModel.setStatusId(0);
            requestModel.setCreatedBy(User_id);
            requestModel.setCreatedDate(currentDate);
            requestModel.setUpdatedBy(User_id);
            requestModel.setUpdatedDate(currentDate);
            requestModel.setUserIds(useridArr);


            return new Gson().toJsonTree(requestModel).getAsJsonObject();}
}


