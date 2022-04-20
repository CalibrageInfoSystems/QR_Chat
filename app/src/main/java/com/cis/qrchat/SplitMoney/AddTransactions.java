package com.cis.qrchat.SplitMoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Adapters.AddContactsNewAdapter;
import com.cis.qrchat.Model.AddFundRequest;
import com.cis.qrchat.Model.AddFundResponse;
import com.cis.qrchat.Model.AddGroupRequest;
import com.cis.qrchat.Model.AddGroupResponse;
import com.cis.qrchat.Model.AmountDetails;
import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.GetGroups;
import com.cis.qrchat.Model.Item;
import com.cis.qrchat.Model.SendMoneyobject;
import com.cis.qrchat.Model.TypeItem;
import com.cis.qrchat.Model.TypeItems;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.OTPActivity;
import com.cis.qrchat.Views.account.RegisterActivity;
import com.cis.qrchat.common.MultiSelectionSpinner;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
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

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class AddTransactions extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemSelectedListener, AddContactsAdapter.AddContactsAdapterListner, AddContactsNewAdapter.Add_ContactsAdapterListner {

    private Toolbar toolbar;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    ArrayList<TypeItems> _groupmembersArr;
    ArrayList<Item> contactsArr;

    private List<GetGroupMembers.ListResult> groupMembersList;
    private List<GetGroupMembers.ListResult> membersList;

    String groupName;
    String groupNumberCount;

    private Dialog _confirmationdialog;

    TextView groupNameandNumbers;
    Button splitequally, splitbyAmount;
    EditText eventname, totalamout;
    Spinner spin_groupmembers;
    private List<AddFundResponse.Result> fundResult;
    SpinnerTypeArrayAdapter _groupTypeAdapter;
    String groupId;
    Button addContactBtn, addTransactionBtn;
    private Dialog _dialog;
    ArrayList<Item> selected_Services = new ArrayList<>();
    MultiSelectionSpinner contactsspinner;
    RecyclerView rcv_selectedcontacts, rcv_finalsplitamount;
    AddContactsAdapter addContactsAdapter;
    AddContactsNewAdapter add_ContactsAdapter;
    TypeItems paidby;
    ArrayList<Item> useridArr = new ArrayList<>();
    ArrayList<Item> selectedServices = new ArrayList<>();
    ArrayList<String> participantsuserids = new ArrayList<>();
    TextView numberofparticipants;
    Double totalprice = 0.0;
    int fundId;
    String text;
    static ArrayList<AmountDetails> amountDetails = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transactions);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                groupId = null;
            } else {
                groupId = extras.getString("GroupId");
                Log.d("GroupId", groupId + "");
            }
        } else {
            groupId = (String) savedInstanceState.getSerializable("GroupId");
            Log.d("GroupIdelse", groupId + "");
        }

        init();
        setViews();
        settoolbar();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void init() {

        // spin_groupmembers = findViewById(R.id.paidBy);
        //addContactBtn = findViewById(R.id.addContactBtn);
        rcv_selectedcontacts = findViewById(R.id.rcvaddContacts);
        eventname = findViewById(R.id.eventName);
        totalamout = findViewById(R.id.totalamount);
        addTransactionBtn = findViewById(R.id.addTransactionBtn);
        numberofparticipants = findViewById(R.id.numberofparticipants);

        splitequally = findViewById(R.id.splitequally);
        splitbyAmount = findViewById(R.id.splitbyAmount);
        groupNameandNumbers = findViewById(R.id.groupNameandNumber);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
                groupNameandNumbers.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);


            } else {
                updateResources(this, "en-US");
            }

    }

    private void setViews() {

        rcv_selectedcontacts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        GetGroupMembers();
        GetUserGroups();

        //groupNameandNumbers.setText(groupName + "("+groupNumberCount+" Members");

//        addContactBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (contactsArr != null && contactsArr.size() > 0) {
//
//                    showContactsDialog(AddTransactions.this);
//
//                }else {
//
//                    showDialog(AddTransactions.this, "Create Group to Add Transaction");
//                }
//
//            }
//        });

        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text != null && !text.isEmpty()) {
                    if (validations()) {
                        CreateFund();
                    }
                } else {
                    showDialog(AddTransactions.this, getResources().getString(R.string.pleaseselectsplitmethod));
                }
            }
        });

        splitequally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationss()) {
                    splitequally.setBackgroundColor(getResources().getColor(R.color.appColor));
                    splitequally.setTextColor(getResources().getColor(R.color.white));
                    splitbyAmount.setTextColor(getResources().getColor(R.color.black));
                    splitbyAmount.setBackgroundColor(getResources().getColor(R.color.white));


                    String amount = totalamout.getText().toString();
                    Double equalamount = Double.parseDouble(amount) / contactsArr.size();
                    String roundedamount = String.format("%.2f", equalamount);

                    text = splitequally.getText().toString();

                    Log.e("============>194", text);

//                Log.d("Equal Amount", equalamount + "");
//                Log.d("Rounded Amount", roundedamount + "");
//
//                List<GetGroupMembers.ListResult> membars = new ArrayList<>();
//
//                for (GetGroupMembers.ListResult item:
//                        groupMembersList) {
//                    membars.add(new GetGroupMembers.ListResult(
//                            item.getUserId(),item.getName(),item.getNickName(),item.getPhoneNumber(),item.getQrChatId(),item.getFileName(),item.getFileExtension(),item.getFileLocation(),item.getUserImage(),Double.parseDouble(roundedamount),true));
//                }

                    String number = arabicToDecimal(roundedamount); // number = 42;
                    String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";



                    for (GetGroupMembers.ListResult item : groupMembersList) {
                        item.setSelected(true);
                        item.setEqualamount(Double.parseDouble(number));
                    }
                    addContactsAdapter = new AddContactsAdapter(AddTransactions.this, groupMembersList, AddTransactions.this);
                    rcv_selectedcontacts.setAdapter(addContactsAdapter);
                } else {

                }
            }
        });

        splitbyAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validationss()) {
                    splitbyAmount.setBackgroundColor(getResources().getColor(R.color.appColor));
                    splitbyAmount.setTextColor(getResources().getColor(R.color.white));
                    splitequally.setTextColor(getResources().getColor(R.color.black));
                    splitequally.setBackgroundColor(getResources().getColor(R.color.white));
                    text = splitbyAmount.getText().toString();

                    Log.e("============>231", text);
                    for (GetGroupMembers.ListResult item : groupMembersList) {
                        item.setSelected(false);
                        //  item.setEqualamount(0.0);
                    }
                    add_ContactsAdapter = new AddContactsNewAdapter(AddTransactions.this, groupMembersList, AddTransactions.this);
                    rcv_selectedcontacts.setAdapter(add_ContactsAdapter);

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


//    fun convertArabic(arabicStr: String): String? {
//        val chArr = arabicStr.toCharArray()
//        val sb = StringBuilder()
//        for (ch in chArr) {
//            if (Character.isDigit(ch)) {
//                sb.append(Character.getNumericValue(ch))
//            }else if (ch == '٫'){
//                sb.append(".")
//            }
//
//            else {
//                sb.append(ch)
//            }
//        }
//        return sb.toString()
//    }

    private boolean validationss() {
        if (TextUtils.isEmpty(eventname.getText().toString())) {
            showDialog(AddTransactions.this, getResources().getString(R.string.pleaseentereventName));
            return false;
        } else if (TextUtils.isEmpty(totalamout.getText().toString().trim())) {
            showDialog(AddTransactions.this, getResources().getString(R.string.pleaseentertotalAmount));
            return false;
        }
        return true;
    }

    private boolean validations() {

//        if (TextUtils.isEmpty(eventname.getText().toString())) {
//            showDialog(AddTransactions.this, "Please Enter Event Name");
//            return false;
//        } else if (TextUtils.isEmpty(totalamout.getText().toString().trim())) {
//            showDialog(AddTransactions.this, "Please Enter Total Amount");
//            return false;
//        }

        if (text.equalsIgnoreCase(getResources().getString(R.string.splitbyamount))) {


            if (amountDetails.size() == 0) {

                showDialog(AddTransactions.this, getResources().getString(R.string.pleasesplitamount));
                return false;

            } else if (!IsVallidamount()) {
                showDialog(AddTransactions.this, getResources().getString(R.string.pleasesplitamount));
                return false;
            }
        }
        if (text.equalsIgnoreCase(getResources().getString(R.string.splitequally))) {


            if (groupMembersList.size() == 0) {

                showDialog(AddTransactions.this, getResources().getString(R.string.pleasesplitamount));
                return false;

            } else if (!IsVallidsplit()) {
                showDialog(AddTransactions.this, getResources().getString(R.string.pleasesplitamount));
                return false;
            }
        }
//        else if (contactsspinner == null) {
//
//            showDialog(AddTransactions.this, "Please select atleast one user");
//            return false;
//        } else if (contactsspinner.getSelectedItems().isEmpty() || contactsspinner.getSelectedItems().size() == 0) {
//            showDialog(AddTransactions.this, "Please select atleast one user");
//            return false;
//        }
        return true;
    }

    private boolean IsVallidsplit() {
        boolean isvalid = false;
        for (int i = 0; i < groupMembersList.size(); i++) {
            if (groupMembersList.get(i).getEqualamount() > 0.0) {
                isvalid = true;
            }

        }

        return isvalid;
    }

    private boolean IsVallidamount() {
        boolean isvalid = false;
        for (int i = 0; i < amountDetails.size(); i++) {
            if (amountDetails.get(i).getAmount() > 0.0) {
                isvalid = true;
            }

        }

        return isvalid;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrwallet));
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

    private void GetGroupMembers() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>", User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getGroupMemberspage(APIConstantURL.getGroupMembers + groupId)
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


                        if (getGroupMembers.getListResult() != null) {

                            groupMembersList = getGroupMembers.getListResult();

                            groupNumberCount = getGroupMembers.getAffectedRecords() + "";

                            //  _groupmembersArr = new ArrayList<>();
                            contactsArr = new ArrayList<>();
                            //_groupmembersArr.add(new TypeItems("0", "Please Select Paid By"));

                            for (GetGroupMembers.ListResult data : getGroupMembers.getListResult()
                            ) {
                                // _groupmembersArr.add(new TypeItems(data.getUserId(), data.getName()));
                                contactsArr.add(new Item(data.getUserId(), data.getName(), false));

                            }

//                            _groupTypeAdapter = new SpinnerTypeArrayAdapter(AddTransactions.this, _groupmembersArr);
//                            _groupTypeAdapter.setDropDownViewResource(R.layout.simple_spinnerdropdown_item);
//                            spin_groupmembers.setAdapter(_groupTypeAdapter);


                        } else {

                        }

                    }
                });
    }


    public void showContactsDialog(Activity activity) {
        _dialog = new Dialog(activity);
        _dialog = new Dialog(activity);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setContentView(R.layout.contacts);
        _dialog.show();

        Button submitpeopleBtn, cancelBtn;

        submitpeopleBtn = _dialog.findViewById(R.id.addpeople_btn);
        cancelBtn = _dialog.findViewById(R.id.cancelBtn);
        contactsspinner = _dialog.findViewById(R.id.multiselectcontactspinner);
        contactsspinner.setListener(AddTransactions.this);
        contactsspinner.setItems(contactsArr);


        submitpeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _dialog.dismiss();
                if (contactsspinner.getSelectedItems() != null && contactsspinner.getSelectedItems().size() > 0) {

                } else {
                    Toast.makeText(activity, "Please select atlest one user", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialog.dismiss();
            }
        });
    }

    private void CreateFund() {
        mdilogue.show();
        JsonObject object = createFundObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.addfundpage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddFundResponse>() {

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
                        Log.e("============", ((HttpException) e).code() + "");
                        mdilogue.dismiss();
                        //showDialog(RegisterActivity.this, getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(AddFundResponse addFundResponse) {
                        if (addFundResponse.getIsSuccess()) {
                            Toast.makeText(AddTransactions.this, addFundResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                            fundId = addFundResponse.getResult().getId();
                            int amount = addFundResponse.getResult().getTotalAmount();
                            String date = addFundResponse.getResult().getUpdatedDate();
                            String transactionname = addFundResponse.getResult().getName();
                            int groupcount = addFundResponse.getResult().getAppGroupFundXrefFundId().size();

                            Intent transactionsummary = new Intent(AddTransactions.this, TransactionSummary.class);
                            transactionsummary.putExtra("PassFundId", fundId);
                            transactionsummary.putExtra("PassAmount", amount);
                            transactionsummary.putExtra("PassDate", date);
                            transactionsummary.putExtra("PassTransactionName", transactionname);
                            transactionsummary.putExtra("PassGroupId", groupId);
                            transactionsummary.putExtra("splitmethod", text);

                            startActivity(transactionsummary);
                            Log.d("FundIddddd", fundId + "");
                            Log.d("Amouuuunt", amount + "");
                            Log.d("TrannssssName", transactionname + "");
                            amountDetails.clear();
                            groupMembersList.clear();
                        } else {

                            Toast.makeText(AddTransactions.this, addFundResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    private JsonObject createFundObject() {
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        //useridArr.add(User_id);

        Log.e("============>", User_id);

        // paidby = (TypeItems) spin_groupmembers.getSelectedItem();
//        selectedServices = contactsspinner.getSelectedItems();
//
//        participantsuserids.add(User_id);
//
//        for (int i=0; i< selectedServices.size(); i++){
//
//            participantsuserids.add(selectedServices.get(i).getId());
//        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String date = arabicToDecimal(currentDate);


        List<AddFundRequest.Participant> header = new ArrayList<>();
        if (text.equalsIgnoreCase(getResources().getString(R.string.splitequally))) {
            if (groupMembersList.size() != 0 && groupMembersList != null) {


                for (int i = 0; i < groupMembersList.size(); i++) {
                    AddFundRequest.Participant request = new AddFundRequest.Participant();
                    if (groupMembersList.get(i).getEqualamount() != 0.0 || groupMembersList.get(i).getUserId().contains(User_id)) {
                        request.setParticipantId(groupMembersList.get(i).getUserId());
                        request.setShareAmount(groupMembersList.get(i).getEqualamount());
                        header.add(request);
                    }


                }
            }
        } else if (text.equalsIgnoreCase(getResources().getString(R.string.splitbyamount))) {

            if (amountDetails.size() != 0 && amountDetails != null) {
                for (int i = 0; i < amountDetails.size(); i++) {
                    AddFundRequest.Participant request = new AddFundRequest.Participant();
                    if (amountDetails.get(i).getAmount() != 0.0 || amountDetails.get(i).getId().contains(User_id)) {
                        request.setParticipantId(amountDetails.get(i).getId());
                        request.setShareAmount(amountDetails.get(i).getAmount());
                        header.add(request);
                    }
                }


            }


        }

        AddFundRequest requestModel = new AddFundRequest(header);
        requestModel.setId(null);
        requestModel.setGroupId(groupId);
        requestModel.setName(eventname.getText().toString());
        requestModel.setParticipantId(User_id);
        requestModel.setTotalAmount(Integer.parseInt(totalamout.getText().toString()));
        requestModel.setBalAmount(0);
        requestModel.setCreatedBy(User_id);
        requestModel.setCreatedDate(date);
        requestModel.setUpdatedBy(User_id);
        requestModel.setUpdatedDate(date);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    @Override
    public void selecteditems(ArrayList<Item> items) {

        Log.d("isComing", "Not Coming");


        items = contactsspinner.getSelectedItems();
        selected_Services = items;
        Log.d("SelectedItems", selected_Services.get(0).getName() + "");
    }

    @Override
    public void onitemselected(GetGroupMembers.ListResult items) {

    }

    @Override
    public void onunchecked(GetGroupMembers.ListResult items, int po) {

        Log.d("AddTransaction", "===> Analysis ==> onunchecked():" + items.getSelected() + "  && Position :" + po);
        groupMembersList.set(po, items);

//        List<GetGroupMembers.ListResult> updateditems = new ArrayList<>();
//        Toast.makeText(this, "USER CHECKED :"+items.getSelected(), Toast.LENGTH_SHORT).show();
//        for(GetGroupMembers.ListResult updateditemseach :groupMembersList)
//        {
//            if(updateditemseach.getSelected() == true)
//            {
//                updateditems.add(updateditemseach);
//            }
//        }
        int numberscount = 0;
        for (int i = 0; i < groupMembersList.size(); i++) {
            if (groupMembersList.get(i).getSelected()) {
                numberscount = numberscount + 1;
            }
            Log.d("AddTransaction", "== Analysis  == Name :" + groupMembersList.get(i).getName() + "&& Selected :" + groupMembersList.get(i).getSelected());
        }

        String amount = totalamout.getText().toString();
        Double equalamount = Double.parseDouble(amount) / numberscount;
        String roundedamount = String.format("%.2f", equalamount);

        for (int i = 0; i < groupMembersList.size(); i++) {
            if (groupMembersList.get(i).getSelected()) {
                groupMembersList.get(i).setEqualamount(equalamount);
            } else {
                groupMembersList.get(i).setEqualamount(0.0);
            }
        }
        // Show Progress dialogue some time call again refresh Recycleview
        addContactsAdapter = new AddContactsAdapter(AddTransactions.this, groupMembersList, AddTransactions.this);
        rcv_selectedcontacts.setAdapter(addContactsAdapter);
    }

    @Override
    public void onTextChanged(ArrayList<AmountDetails> Amount, int po) {
        Log.d("AddTransaction", "===> Analysis ==> onTextChanged():" + Amount.get(po).getAmount() + "  && Position :" + po);


        amountDetails = Amount;
        Double enteramount = 0.0;
        for (AmountDetails attached : Amount) {
            Double oneitem = attached.getAmount();
            enteramount = oneitem + enteramount;
            Log.d("enteramount====541", "===> Analysis ==> onTextChanged():" + enteramount);
        }
        if (enteramount > Double.parseDouble(totalamout.getText().toString().trim()))
            showDialog(AddTransactions.this, "Entering Amount is greater than Total Amount");
        Log.d("enteramount====544", "===> Analysis ==> onTextChanged():" + enteramount);
    }


    public void showFinalSplitAmountDialog(Activity activity) {
        _confirmationdialog = new Dialog(activity);
        _confirmationdialog = new Dialog(activity);
        _confirmationdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _confirmationdialog.setCancelable(false);
        _confirmationdialog.setContentView(R.layout.finalsplitamount);
        _confirmationdialog.show();

        Button submitPeopleBtn, cancelBtn;

        rcv_finalsplitamount = _confirmationdialog.findViewById(R.id.rcv_finalSplit);
        submitPeopleBtn = _confirmationdialog.findViewById(R.id.addpeople_btn);
        cancelBtn = _confirmationdialog.findViewById(R.id.cancelBtn);

        rcv_finalsplitamount.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        submitPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _confirmationdialog.dismiss();
            }
        });
    }


//    public GetGroupMembers.ListResult OnListnerCalled(){
//
//
//        return groupMembersList;
//
//    }


    private void GetUserGroups() {
        mdilogue.show();
        String User_id = SharedPrefsData.getInstance(this).getStringFromSharedPrefs("USERID");
        Log.e("============>", User_id);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getGroupspage(APIConstantURL.getGroups + User_id)
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


                        if (getGroupsresponse.getListResult() != null) {

                            for (int i = 0; i < getGroupsresponse.getListResult().size(); i++) {

                                if (getGroupsresponse.getListResult().get(i).getId().contains(groupId)) {

                                    groupNameandNumbers.setText(getGroupsresponse.getListResult().get(i).getName() + "(" + groupNumberCount + getResources().getString(R.string.members) + ")");
                                }
                            }

                        }

                    }
                });
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