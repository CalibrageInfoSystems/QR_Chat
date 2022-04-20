package com.cis.qrchat.SplitMoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.AddContactsAdapter;
import com.cis.qrchat.Model.AddFundRequest;
import com.cis.qrchat.Model.AddFundResponse;
import com.cis.qrchat.Model.AddGroupRequest;
import com.cis.qrchat.Model.AddGroupResponse;
import com.cis.qrchat.Model.AddUserstoGroupReques;
import com.cis.qrchat.Model.AddUserstoGroupResponse;
import com.cis.qrchat.Model.GetGroupFundTransactions;
import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.GetGroupTransactions;
import com.cis.qrchat.Model.GetGroups;
import com.cis.qrchat.Model.GetUsers;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.Model.InactiveUserRequest;
import com.cis.qrchat.Model.InactiveUserResponse;
import com.cis.qrchat.Model.Item;
import com.cis.qrchat.Model.SendMoneytoFundRequest;
import com.cis.qrchat.Model.SendMoneytoFundResponse;
import com.cis.qrchat.Model.TypeItems;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.QRScannerMainActivity;
import com.cis.qrchat.common.MultiSelectionSpinner;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sendbird.calls.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class SplitActivity extends AppCompatActivity implements SplitMoneyGroupAdapter.SplitMoneyGroupListner,SplitMoneyGroupMembersAdapter.SplitMoneyGroupMemberListner, GroupTransactionAdapter.GroupTransactionListner, MultiSelectionSpinner.OnMultipleItemSelectedListener {

    RecyclerView rcvgroup, rcvgroupmembers, rcvgroupTransactions;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private SplitMoneyGroupAdapter adapter;
    private SplitMoneyGroupMembersAdapter groupmemberadapter;
    Button addTransaction,addGroup, addpeople;
    private String groupId, selectedgroupName, selectedTransactionPaidBy ;
    private int payId;
    private Dialog _userContactsdialog, _peopledialog, _fundpaydialog;
    MultiSelectionSpinner userContactsSpinner;
    ArrayList<Item> usercontactsArr;
    ArrayList<Item> getUsersArr;
    private ArrayList<String> useridArr = new ArrayList<>();
    EditText groupName;
    ArrayList<Item> selecteduserContacts = new ArrayList<>();
    MultiSelectionSpinner peoplespinner;
    ArrayList<Item> peoplesArr;
    SwipeRefreshLayout swipeLyt;
    ArrayList<Item> selectedusers = new ArrayList<>();
    ArrayList<String> selectedsuserids = new ArrayList<>();
    ArrayList<String> deleteUserId = new ArrayList<>();
    EditText payfundamount;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        init();
        setviews();
        settoolbar();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)

    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                Intent intent = new Intent(SplitActivity.this, QRScannerMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(SplitActivity.this, QRScannerMainActivity.class);
                startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init() {

        rcvgroup =  findViewById(R.id.rcvgroup);
        rcvgroupmembers =  findViewById(R.id.rcvgroupmembers);
        rcvgroupTransactions = findViewById(R.id.rcvgroupTransactions);
        addTransaction = findViewById(R.id.addTransaction);
        addpeople = findViewById(R.id.addpeople);
        addGroup = findViewById(R.id.addGroup);
        swipeLyt = findViewById(R.id.swiperefreshLyt);


    }
    private void setviews() {
        GetUserGroups();

        swipeLyt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLyt.setRefreshing(false);
                GetUserGroups();
                GetUserContacts();
            }
        });


        rcvgroup.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rcvgroupTransactions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvgroupmembers.setLayoutManager(new GridLayoutManager(this, 4));


//        RecyclerView.LayoutManager groupmemberlytmanager = new LinearLayoutManager(this);
//        rcvgroupmembers.setLayoutManager(groupmemberlytmanager);

        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addtransactionscreen = new Intent(SplitActivity.this, AddTransactions.class);
                addtransactionscreen.putExtra("GroupId", groupId);
                startActivity(addtransactionscreen);

            }
        });

        GetUserContacts();

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usercontactsArr != null && usercontactsArr.size() > 0) {

                    showUserContactsDialog(SplitActivity.this);
                }else {

                    showDialog(SplitActivity.this, getResources().getString(R.string.addcontactstoCreategroup));
                }

            }
        });

        addpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getUsersArr != null && getUsersArr.size() > 0) {

                    showPeopleDialog(SplitActivity.this);

                }else {

                    showDialog(SplitActivity.this, getResources().getString(R.string.nocontactstoAdd));
                }

            }
        });




    }

//    @Override
//    protected void onResume() {
//
//        super.onResume();
//        this.onCreate(null);
//    }


    @Override
    protected void onResume() {
        super.onResume();

        GetUserContacts();
         GetUserGroups();
    }

    public void showUserContactsDialog(Activity activity) {
        _userContactsdialog = new Dialog(activity);
        _userContactsdialog = new Dialog(activity);
        _userContactsdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _userContactsdialog.setCancelable(false);
        _userContactsdialog.setContentView(R.layout.addgroup);
        _userContactsdialog.show();

        Button submitGroupBtn, cancelBtn;

        submitGroupBtn = _userContactsdialog.findViewById(R.id.addgroupSubmitBtn);
        cancelBtn = _userContactsdialog.findViewById(R.id.cancelBtn);
        groupName = _userContactsdialog.findViewById(R.id.groupNamee);

        userContactsSpinner = _userContactsdialog.findViewById(R.id.peoplesspin);
        userContactsSpinner.setListener(SplitActivity.this);
        userContactsSpinner.setItems(usercontactsArr);

        submitGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(groupName.getText().toString())){

                    showDialog(SplitActivity.this, getResources().getString(R.string.pleaseentergroupname));
                }else if(userContactsSpinner.getSelectedItems().size() == 0){

                    showDialog(SplitActivity.this, getResources().getString(R.string.pleaseselectgroupmembers));

                }else{
                    _userContactsdialog.dismiss();
                    CreateGroup();


                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _userContactsdialog.dismiss();
            }
        });
    }

    public void showPeopleDialog(Activity activity) {
        _peopledialog = new Dialog(activity);
        _peopledialog = new Dialog(activity);
        _peopledialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _peopledialog.setCancelable(false);
        _peopledialog.setContentView(R.layout.contacts);
        _peopledialog.show();

        Button submitPeopleBtn, cancelBtn;

        submitPeopleBtn = _peopledialog.findViewById(R.id.addpeople_btn);
        cancelBtn = _peopledialog.findViewById(R.id.cancelBtn);
        peoplespinner = _peopledialog.findViewById(R.id.multiselectcontactspinner);
        peoplespinner.setListener(SplitActivity.this);
        peoplespinner.setItems(getUsersArr);

        submitPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(peoplespinner.getSelectedItems().size() == 0){

                    showDialog(SplitActivity.this, getResources().getString(R.string.pleaseselectpeopletoadd));

                }else{
                    _peopledialog.dismiss();
                    UpdateGroup();
                }


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _peopledialog.dismiss();
            }
        });
    }


    private void UpdateGroup() {
        mdilogue.show();
        JsonArray object = addUsersObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.addUsertoGroupPage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddUserstoGroupResponse>() {

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
                    public void onNext(AddUserstoGroupResponse addUserstoGroupResponse) {
                        Toast.makeText(SplitActivity.this, getResources().getString(R.string.usersaddedSucessfully), Toast.LENGTH_SHORT).show();
                        getUsersArr = new ArrayList<>();

                    }
                });


    }

    private JsonArray addUsersObject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        //useridArr.add(User_id);
        Log.e("============>",User_id);

        selectedsuserids.clear();

        selectedusers = peoplespinner.getSelectedItems();

        for (int i=0; i< selectedusers.size(); i++){

            selectedsuserids.add(selectedusers.get(i).getId());
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        List<AddUserstoGroupReques> userstoGroupReques =new ArrayList<>();
        for(int i=0;i <selectedusers.size();i ++)
        {
            AddUserstoGroupReques requestModel = new AddUserstoGroupReques();

            requestModel.setId(0);
            requestModel.setUserId(selectedsuserids.get(i));
            requestModel.setGroupId(groupId);
            requestModel.setIsActive(true);

            userstoGroupReques.add(requestModel);
        }






        return new Gson().toJsonTree(userstoGroupReques).getAsJsonArray();}
//    private JsonObject updateGroupObject() {
//        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
//        useridArr.add(User_id);
//
//        Log.e("============>",User_id);
//        selecteduserContacts = peoplespinner.getSelectedItems();
//
//        useridArr = new ArrayList<>();
//
//        useridArr.add(User_id);
//
//        for (int i=0; i< selecteduserContacts.size(); i++){
//
//            useridArr.add(selecteduserContacts.get(i).getId());
//        }
//
//
//        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//        AddGroupRequest requestModel = new AddGroupRequest();
//        requestModel.setId(groupId);
//        requestModel.setName(selectedgroupName);
//        requestModel.setRemarks("null");
//        requestModel.setIsActive(true);
//        requestModel.setStatusId(0);
//        requestModel.setCreatedBy(User_id);
//        requestModel.setCreatedDate(currentDate);
//        requestModel.setUpdatedBy(User_id);
//        requestModel.setUpdatedDate(currentDate);
//        requestModel.setUserIds(useridArr);
//
//
//        return new Gson().toJsonTree(requestModel).getAsJsonObject();}




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

                            GetUserGroups();

                            Toast.makeText(SplitActivity.this, getResources().getString(R.string.groupcreatedsuccessfully), Toast.LENGTH_SHORT).show();
//                            recreate();
                        }
                        else{

                            Toast.makeText(SplitActivity.this, addGroupResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private JsonObject createGroupObject() {
        useridArr.clear();
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        useridArr.add(User_id);

        Log.e("============>",User_id);
        selecteduserContacts = userContactsSpinner.getSelectedItems();

        for (int i=0; i< selecteduserContacts.size(); i++){

            useridArr.add(selecteduserContacts.get(i).getId());
        }


        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String date = arabicToDecimal(currentDate);


        AddGroupRequest requestModel = new AddGroupRequest();
        requestModel.setId(null);
        requestModel.setName(groupName.getText().toString());
        requestModel.setRemarks("null");
        requestModel.setIsActive(true);
        requestModel.setStatusId(0);
        requestModel.setCreatedBy(User_id);
        requestModel.setCreatedDate(date);
        requestModel.setUpdatedBy(User_id);
        requestModel.setUpdatedDate(date);
        requestModel.setUserIds(useridArr);


        return new Gson().toJsonTree(requestModel).getAsJsonObject();}


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

    private void GetUserContacts() {
        mdilogue.show();
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

                            usercontactsArr = new ArrayList<>();
                            for (Getcontactlist.ListResult data : getcontactlist.getListResult()
                            ) {
                                usercontactsArr.add(new Item(data.getContactId(),data.getContactName(),false));
                            }

                        }

                    }});}


    private void GetUserGroups() {
        mdilogue.show();
        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getGroupspage(APIConstantURL.getGroups+User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetGroups>() {
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
                    public void onNext(GetGroups getGroupsresponse) {

                        if(getGroupsresponse.getListResult()!=null){

                            addpeople.setVisibility(View.VISIBLE);
                            addTransaction.setVisibility(View.VISIBLE);

                            adapter = new SplitMoneyGroupAdapter(SplitActivity.this, getGroupsresponse.getListResult(), SplitActivity.this);
                            rcvgroup.setAdapter(adapter);

                            ScrollingPagerIndicator recyclerIndicator = findViewById(R.id.indicator);
                            recyclerIndicator.setDotCount(getGroupsresponse.getListResult().size());
                            recyclerIndicator.setDotColor(getResources().getColor(R.color.appColor));
                            recyclerIndicator.attachToRecyclerView(rcvgroup);
                            if(getGroupsresponse.getListResult().size() >0)
                            {
                                Log.d("Comingtothismethod", "Success");

                                groupId = getGroupsresponse.getListResult().get(0).getId();
                                GetGroupMembers();
                                GetGroupTransactions();
                                GetUsers();
                            }
                        }else {

                            addpeople.setVisibility(View.GONE);
                            addTransaction.setVisibility(View.GONE);
                        }

                    }});}

    private void GetGroupMembers() {

        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getGroupMemberspage(APIConstantURL.getGroupMembers+groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetGroupMembers>() {
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
                    public void onNext(GetGroupMembers getGroupMembers) {

                        mdilogue.dismiss();
                        if(getGroupMembers.getListResult()!=null){

                            peoplesArr = new ArrayList<>();


                            rcvgroupmembers.setVisibility(View.VISIBLE);
                            groupmemberadapter = new SplitMoneyGroupMembersAdapter(SplitActivity.this, getGroupMembers.getListResult(), SplitActivity.this);
                            rcvgroupmembers.setAdapter(groupmemberadapter);

                            for (GetGroupMembers.ListResult data : getGroupMembers.getListResult()
                            ) {
                                peoplesArr.add(new Item(data.getUserId(),data.getName(),false));

                            }

                        }else{

                            rcvgroupmembers.setVisibility(View.GONE);
                        }

                    }});}


    private void GetGroupTransactions() {
        mdilogue.show();
        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getGroupFundTransactionspage(APIConstantURL.getFundsTransactions+groupId+"/"+User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetGroupFundTransactions>() {
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
                    public void onNext(GetGroupFundTransactions getGroupfundTransactions) {

                        mdilogue.dismiss();

                        if(getGroupfundTransactions.getListResult() != null && getGroupfundTransactions.getListResult().size() > 0){
                            rcvgroupTransactions.setVisibility(View.VISIBLE);
                            GroupTransactionAdapter  grouptransactionadapter = new GroupTransactionAdapter(SplitActivity.this, getGroupfundTransactions.getListResult(), SplitActivity.this);
                            rcvgroupTransactions.setAdapter(grouptransactionadapter);

                        }else{
                            rcvgroupTransactions.setVisibility(View.GONE);
                        }

                    }});}


    private void GetUsers() {
        mdilogue.show();
        String  User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getUserspage(APIConstantURL.GetUsers+groupId+"/"+User_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUsers>() {
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
                    public void onNext(GetUsers getUsers) {
                        mdilogue.dismiss();
                        if(getUsers.getListResult() != null && getUsers.getListResult().size() > 0){
                            getUsersArr = new ArrayList<>();
                            for (GetUsers.ListResult data : getUsers.getListResult()
                            ) {
                                getUsersArr.add(new Item(data.getUserId(),data.getContactName(),false));

                            }

                        }else{
                        }

                    }});}


    @Override
    public void onSplitGroupSelected(GetGroups.ListResult group) {
        groupId = group.getId();
        selectedgroupName = group.getName();
        Log.d("GroupIddd", "is"+groupId);

        if (getUsersArr != null){

            getUsersArr.clear();

        }

        GetGroupMembers();
        GetGroupTransactions();
        GetUsers();
    }

    @Override
    public void onSplitGroupMemberSelected(GetGroupMembers.ListResult groupmembers) {

        deleteUserId.clear();

        deleteUserId.add( groupmembers.getUserId());
        Log.d("DeleteUserId", deleteUserId + "");

        showDeleteDialog(SplitActivity.this, getResources().getString(R.string.deletetheselectedUser));
    }

    private void InactiveUser() {
        mdilogue.show();
        JsonObject object = inActiveUserObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.inactiveUserPage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InactiveUserResponse>() {

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
                    public void onNext(InactiveUserResponse inactiveUserResponse) {

                        if(inactiveUserResponse.getIsSuccess()) {

                            Toast.makeText(SplitActivity.this, getResources().getString(R.string.userdeletedsuccessfully), Toast.LENGTH_SHORT).show();
                            GetGroupMembers();
                        }
                        else{

                            Toast.makeText(SplitActivity.this, inactiveUserResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private JsonObject inActiveUserObject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        useridArr.add(User_id);

        Log.e("============>",User_id);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        InactiveUserRequest requestModel = new InactiveUserRequest();
        requestModel.setGroupId(groupId);
        requestModel.setUserIds(deleteUserId);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();}

    public void showDeleteDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.deletedialog);
        final ImageView img = dialog.findViewById(R.id.img_cross);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Button yesbTn = (Button) dialog.findViewById(R.id.btn_yes);
        Button nobTn = (Button) dialog.findViewById(R.id.btn_no);
        yesbTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                InactiveUser();
            }
        });

        nobTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ((Animatable) img.getDrawable()).start();
//            }
//        }, 500);
    }


    @Override
    public void onTransactionSelected(GetGroupFundTransactions.ListResult groupTransactions) {

    }

    @Override
    public void onTransactionPayFundSelected(GetGroupFundTransactions.ListResult groupTransactions) {
                payId = groupTransactions.getFundId();
        selectedTransactionPaidBy = groupTransactions.getPhoneNumber();
        Log.d("PayID", payId + "");
        showPayDialog(SplitActivity.this);
    }

    @Override
    public void selecteditems(ArrayList<Item> items) {

    }

    @Override
    public void onGroupPaySelected(GetGroups.ListResult group) {
//        payId = group.getId();
//        Log.d("PayID", payId + "");
//        showPayDialog(SplitActivity.this);
    }

    public void showPayDialog(Activity activity) {
        _fundpaydialog = new Dialog(activity);
        _fundpaydialog = new Dialog(activity);
        _fundpaydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _fundpaydialog.setCancelable(false);
        _fundpaydialog.setContentView(R.layout.payfund);
        _fundpaydialog.show();

        Button submitPeopleBtn, cancelBtn;

        submitPeopleBtn = _fundpaydialog.findViewById(R.id.addpeople_btn);
        cancelBtn = _fundpaydialog.findViewById(R.id.cancelBtn);

        payfundamount = _fundpaydialog.findViewById(R.id.fundamount);

        submitPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(payfundamount.getText().toString())){

                    showDialog(SplitActivity.this, getResources().getString(R.string.pleaseenteramount));
                }else{
                    _fundpaydialog.dismiss();
                    SendMoneyToFund();

                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _fundpaydialog.dismiss();
            }
        });
    }

    private void SendMoneyToFund() {
        mdilogue.show();
        JsonObject object = sendMoneytoFundObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.sendMoneytoFundPage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendMoneytoFundResponse>() {

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
                    public void onNext(SendMoneytoFundResponse sendMoneytoFundResponse) {

                        if(sendMoneytoFundResponse.getIsSuccess()) {

                            showSuccessDialog(SplitActivity.this, getResources().getString(R.string.fundtransferredsuccessfully));
                        }
                        else{

                            Toast.makeText(SplitActivity.this, sendMoneytoFundResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    private JsonObject sendMoneytoFundObject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        String wallet_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("WalletId");


        Log.e("============>",User_id);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SendMoneytoFundRequest requestModel = new SendMoneytoFundRequest();
        requestModel.setUserWalletHistory(new SendMoneytoFundRequest.UserWalletHistory(0,wallet_id,Integer.parseInt(payfundamount.getText().toString()), 21, 17, groupId, payId,true));
        requestModel.setUserId(User_id);
        requestModel.setRecieverUserName(selectedTransactionPaidBy);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();}

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


}