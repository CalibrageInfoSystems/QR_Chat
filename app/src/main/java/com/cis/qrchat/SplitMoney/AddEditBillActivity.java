package com.cis.qrchat.SplitMoney;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Adapters.AddContactsAdapter;
import com.cis.qrchat.Adapters.MembersinExpenseAdapter;
import com.cis.qrchat.Model.AddGroupRequest;
import com.cis.qrchat.Model.AddGroupResponse;
import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.Model.Item;
import com.cis.qrchat.R;
import com.cis.qrchat.common.MultiSelectionSpinner;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/* Note that this activity can act as a Add Bill Activity or Edit Bill Activity based on the intent data we receive*/
public class AddEditBillActivity extends AppCompatActivity implements AddContactsAdapter.AddContactsAdapterListner,MembersinExpenseAdapter.GroupMemberExpensedapterAdapterListner,MultiSelectionSpinner.OnMultipleItemSelectedListener  {
    private EditText editTextItem;
    private EditText editTextCost;
    private String currency;
    private String gName;
    private String paidBy;
    private int memberId;
    private int requestCode;
    private int billId;
    RecyclerView recyclerView;
    AddContactsAdapter contactListAdapter;
    MembersinExpenseAdapter adapter;
    private ArrayList<String> usernameArr = new ArrayList<>();
    private ArrayList<Item> contactsArr = new ArrayList<>();
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private String groupId;
    MultiSelectionSpinner membersspin;
    private ArrayList<Item> selectedItems;

    private void saveExpense() {
        String item = editTextItem.getText().toString();
        String cost = editTextCost.getText().toString();

        // check if the item name or cost is empty
        if (item.trim().isEmpty() || cost.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
            return;
        }

        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(getApplication(),gName)).get(BillViewModel.class);

        if(requestCode == 1) { // 1 for Add Bill Activity
            // Round up the cost of the bill to 2 decimal places
            BigDecimal decimal = new BigDecimal(cost);
            BigDecimal res = decimal.setScale(2, RoundingMode.HALF_EVEN);

            // store to database
//            Log.d("1", Integer.toString(memberId));
            billViewModel.insert(new BillEntity(memberId,item,res.toString(),gName,paidBy));
        }

        if(requestCode == 2) { // 2 for Edit Bill Activity
            BillEntity bill = new BillEntity(memberId,item,cost,gName,paidBy);
            bill.setId(billId);

            /* update the database. note that update operation in billViewModel looks for a row in BillEntity where the value of column("Id")  = billId
               and if found, updates other columns in the row */
            billViewModel.update(bill);
        }

        // updates the group currency
        GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        GroupEntity group = new GroupEntity(gName);
        group.gCurrency = currency;
        groupViewModel.update(group);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bill);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        groupId = getIntent().getExtras().getString("GroupIdd");
        Log.d("ExtraItem", "GroupId=" + groupId);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.addBillToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextItem = findViewById(R.id.addBillItemName);
        editTextCost = findViewById(R.id.addBillItemCost);
        membersspin =  findViewById(R.id.contactspinner);

        recyclerView = (RecyclerView)findViewById(R.id.participantsrecyclerview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        // makes sure the user can enter up to 2 decimal places only in item cost field
        editTextCost.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void afterTextChanged(Editable arg0) {
                String str = editTextCost.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 20, 2);

                if (!str2.equals(str)) {
                    editTextCost.setText(str2);
                    int pos = editTextCost.getText().length();
                    editTextCost.setSelection(pos);
                }
            }
        });

        // get data from the intent that started this activity
        Intent intent = getIntent();
        gName = intent.getStringExtra(GroupListActivity.EXTRA_TEXT_GNAME);
        // requestCode == 1 identifies an add bill intent and requestCode == 2 identifies an edit Bill intent
        requestCode = intent.getIntExtra("requestCode",0);
        memberId = intent.getIntExtra("billMemberId",-1);
        billId = intent.getIntExtra("billId",-1);
        currency = intent.getStringExtra("groupCurrency");


        // spinner for selecting paidBy Member
        final Spinner spinnerPaidBy = findViewById(R.id.addBillItemPaidBy);

               ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usernameArr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaidBy.setAdapter(arrayAdapter);


        membersspin.setItems(contactsArr);

//        final AllMembersSpinnerAdapter allMembersSpinnerAdapter = new AllMembersSpinnerAdapter(this,new ArrayList<MemberEntity>());
//        allMembersSpinnerAdapter.setDropDownViewResource(0);
//        spinnerPaidBy.setAdapter(allMembersSpinnerAdapter);
//        spinnerPaidBy.setOnItemSelectedListener(this);
//
//        // get all current members of the group
//        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(getApplication(),gName)).get(MemberViewModel.class);
//        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
//            @Override
//            public void onChanged(List<MemberEntity> memberEntities) {
//                allMembersSpinnerAdapter.clear();
//                allMembersSpinnerAdapter.addAll(memberEntities);
//                allMembersSpinnerAdapter.notifyDataSetChanged();
//
//                if(requestCode == 2) {
//                    MemberEntity member = new MemberEntity(paidBy,gName);
//                    member.setId(memberId);
//                    int spinnerPositionPaidBy = allMembersSpinnerAdapter.getPosition(member);
//                    spinnerPaidBy.setSelection(spinnerPositionPaidBy);
//                }
//            }
//
//        });

        if(intent.hasExtra("billId")) {
            // Only edit bill intent sends "billId" with it
            // Get data from the edit bill intent that started this activity
            setTitle("Edit expense");
            editTextItem.setText(intent.getStringExtra("billName")); // set default text received from the intent
            editTextCost.setText(intent.getStringExtra("billCost")); // set default text received from the intent
            paidBy = intent.getStringExtra("billPaidBy");
        } else {
            setTitle("Add an Expense");
        }

        GetGroupMembers();
    }


    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        StringBuilder rFinal = new StringBuilder();
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && !after){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal.toString();
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal.toString();
            }
            rFinal.append(t);
            i++;
        }return rFinal.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_bill_action_bar_menu,menu);
        return true;
    }

    // call this method when an option in the menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addBillToolbarMenu) {
            saveExpense();
        }

//        if (null !=contactListAdapter.getselectedContacts()) {
//            for(Getcontactlist.ListResult itemm:  contactListAdapter.getselectedContacts())
//            {
//                Log.d("AddeditContact","==> item :"+itemm.getContactId());
//                useridArr.add(itemm.getContactId());
//                Log.d("SelectedContadId", "ContactId====:"+useridArr);
//            }
//        }


        finish(); // if the user clicks on back button close this activity
        return true;
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        switch (parent.getId()) {
//
//            case R.id.addBillItemPaidBy:
////                Log.d("t", "selected paidBy");
//                MemberEntity member = (MemberEntity) parent.getItemAtPosition(position);
//                paidBy = member.name;
//                memberId = member.id;
//                break;
//            default:break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

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


                        if(getGroupMembers.getListResult()!=null){

                            contactsArr = new ArrayList<>();
                            contactsArr.add(new Item("Please Select Memvers partoco[ared om te bo;;,", "", false));

                                for (GetGroupMembers.ListResult data : getGroupMembers.getListResult()
                                ) {
                                    contactsArr.add(new Item(data.getUserId(),data.getName(),false));
                                }

                                membersspin.setItems(contactsArr);



                            for (int i = 0; i< getGroupMembers.getListResult().size(); i++){

                                usernameArr.add(getGroupMembers.getListResult().get(i).getName());
                            }

                            adapter = new MembersinExpenseAdapter(AddEditBillActivity.this, getGroupMembers.getListResult(), AddEditBillActivity.this);
                            recyclerView.setAdapter(adapter);

                            for (int i = 0; i<= getGroupMembers.getListResult().size(); i++)

                                Log.d("GroupMembers", "Name==" + getGroupMembers.getListResult().get(i).getName());

                        }

                    }});}


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

//                            contactListAdapter = new AddContactsAdapter(AddEditBillActivity.this, getcontactlist.getListResult(), AddEditBillActivity.this);
//                            recyclerView.setAdapter(contactListAdapter);
                        }



                    }});}

    @Override
    public void onContactSelected(Getcontactlist.ListResult contact) {


        List<String> userIds = new ArrayList<>();
        userIds.add(contact.getContactNumber());


    }


    @Override
    public void onGroupMemberExpenseSelected(GetGroupMembers.ListResult groupmembers) {

    }

    @Override
    public void selecteditems(ArrayList<Item> items) {

        selectedItems = items;

    }

//    private void CreateGroup() {
//        mdilogue.show();
//        JsonObject object = createGroupObject();
//        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
//        mSubscription = service.addgrouppage(object)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<AddGroupResponse>() {
//
//                    @Override
//                    public void onCompleted() {
//                        mdilogue.dismiss();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof HttpException) {
//                            ((HttpException) e).code();
//                            ((HttpException) e).message();
//                            ((HttpException) e).response().errorBody();
//                            try {
//                                ((HttpException) e).response().errorBody().string();
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                            }
//                            e.printStackTrace();
//                        }
//                        Log.e("============", ((HttpException) e).code()+"");
//                        mdilogue.dismiss();
//                        //showDialog(RegisterActivity.this, getString(R.string.server_error));
//                    }
//
//                    @Override
//                    public void onNext(AddGroupResponse addGroupResponse) {
//
//                        if(addGroupResponse.getIsSuccess()) {
//
//                            Toast.makeText(CreateNewGroupActivity.this, "Group Created Successfully", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                        else{
//
//                            Toast.makeText(CreateNewGroupActivity.this, addGroupResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//
//    }
//
//    private JsonObject createGroupObject() {
//        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
//        useridArr.add(User_id);
//
//        Log.e("============>",User_id);
//        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//
//        AddGroupRequest requestModel = new AddGroupRequest();
//        requestModel.setId(null);
//        requestModel.setName(editText.getText().toString());
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
}
