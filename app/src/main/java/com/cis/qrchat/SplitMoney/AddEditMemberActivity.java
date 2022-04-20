package com.cis.qrchat.SplitMoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.cis.qrchat.Adapters.ContactListAdapter;
import com.cis.qrchat.Chat.Groupchat.GroupChannelActivity;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.Contactlist;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/* Note that this activity can act as a Add Member Activity or Edit Member Activity based on the intent data we receive*/
public class AddEditMemberActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddContactsAdapter.AddContactsAdapterListner  {
    private EditText editText;
    private String gName;
    private int requestCode;
    private int userId;
    private int avatarResource;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    AddContactsAdapter contactListAdapter;
    TextView noRecords;
    RecyclerView recyclerView;
    private ArrayList<String> useridArr = new ArrayList<>();




    private void saveEditMember() {

        if (null !=contactListAdapter.getselectedContacts()) {
            for(Getcontactlist.ListResult item:  contactListAdapter.getselectedContacts())
            {
                Log.d("AddeditContact","==> item :"+item.getContactId());
                useridArr.add(item.getContactId());
                Log.d("SelectedContadId", "ContactId====:"+useridArr);
            }
        }

        String name = editText.getText().toString();

        // check if the name is empty
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(getApplication(),gName)).get(MemberViewModel.class);

        if(requestCode == 1) { // 1 for Add Member Activity
            // store the name in database
            MemberEntity member = new MemberEntity(name,gName);
            member.setMAvatar(avatarResource);
            memberViewModel.insert(member);
        }

        if(requestCode == 2) { // 2 for Edit Member Activity
            MemberEntity member = new MemberEntity(name,gName);

            if(userId == -1) {
                Toast.makeText(this, "Member could not be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            /* update the database. note that update operation in memberViewModel looks for a row in MemberEntity where the value of column("Id")  = userId
               and if found, updates other columns in the row */
            member.setId(userId);
            member.setMAvatar(avatarResource);
            memberViewModel.update(member);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_activity);

        editText = findViewById(R.id.addMemberNameText);

        // get data from the intent that started this activity
        Intent intent = getIntent();
        gName = intent.getStringExtra("groupName");
        requestCode = intent.getIntExtra("requestCode",0);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.addMemberToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        // implement spinner for avatar
        final Spinner spinnerAvatar = findViewById(R.id.addMemberActivityAvatarSpinner);
        final AddEditMemberAvatarSpinnerAdapter addEditMemberAvatarSpinnerAdapter = new AddEditMemberAvatarSpinnerAdapter(this,new ArrayList<Integer>()); // adapter for spinner
        addEditMemberAvatarSpinnerAdapter.setDropDownViewResource(0);
        spinnerAvatar.setAdapter(addEditMemberAvatarSpinnerAdapter); // set the adapter to spinner
        spinnerAvatar.setOnItemSelectedListener(this);

        // populate the spinner adapter's list
        List<Integer> avatarOptions = new ArrayList<>();
        populateAvatarList(avatarOptions);
        addEditMemberAvatarSpinnerAdapter.addAll(avatarOptions);

        if(intent.hasExtra("memberId")) {
            // Only edit member intent sends "memberId" with it
            // Get data from the edit member intent that started this activity
            userId = intent.getIntExtra("memberId",-1);

            setTitle("Edit Member");

            editText.setText(intent.getStringExtra("memberName")); // set default text received from the intent

            // set default spinner item
            int spinnerAvatarPosition = addEditMemberAvatarSpinnerAdapter.getPosition(intent.getIntExtra("avatarResource",-1)); // get position of the avatar(received from intent) in spinner array
            spinnerAvatar.setSelection(spinnerAvatarPosition); // set spinner default selection
        } else {
            setTitle("Add Member to Group");
        }

        GetUserContacts();
    }

    private void populateAvatarList(List<Integer> avatarOptions) {
        avatarOptions.add(R.drawable.member);
        avatarOptions.add(R.drawable.member_female_one);
        avatarOptions.add(R.drawable.member_female_two);
        avatarOptions.add(R.drawable.member_female_three);
        avatarOptions.add(R.drawable.member_female_four);
        avatarOptions.add(R.drawable.member_female_five);
        avatarOptions.add(R.drawable.member_male_one);
        avatarOptions.add(R.drawable.member_male_two);
        avatarOptions.add(R.drawable.member_male_three);
        avatarOptions.add(R.drawable.member_male_four);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_member_action_bar_menu,menu);
        return true;
    }

    // call this method when an option in the menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // if user clicks on submit button, save/update the database
        if(item.getItemId() == R.id.addMemberToolbarMenu) {
            saveEditMember();
        }

        finish(); // if the user clicks on back button close this activity
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.addMemberActivityAvatarSpinner) { // if user clicks on an avatar
            avatarResource = Integer.parseInt(parent.getItemAtPosition(position).toString());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

                            contactListAdapter = new AddContactsAdapter(AddEditMemberActivity.this, getcontactlist.getListResult(), AddEditMemberActivity.this);
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
}
