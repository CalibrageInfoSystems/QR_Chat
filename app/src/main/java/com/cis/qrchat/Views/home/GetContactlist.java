package com.cis.qrchat.Views.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import com.cis.qrchat.Adapters.ContactListAdapter;
import com.cis.qrchat.Model.Call_data;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.account.Contactlist;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetContactlist extends AppCompatActivity implements ContactListAdapter.ContactsAdapterListener {
RecyclerView contactsrecyclerView;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    TextView noRecords;
    private SearchView searchView;
    Toolbar toolbar;
    ContactListAdapter contactListAdapter;
    private List<Call_data> call_List = new ArrayList<>();
    FloatingActionButton addfloat;
    String phonenumber;
    Dialog _dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contactlist);
        intviews();
        setviews();
        settoolbar();
    }

    private void intviews() {
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        noRecords = (TextView)findViewById(R.id.no_data);

        addfloat = findViewById(R.id.addfloat);
        contactsrecyclerView = findViewById(R.id.contactsrecyclerView);
        
        
    }
    private void settoolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.contacts));
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
//                Intent intent = new Intent(GetContactlist.this, HomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
    private void setviews() {
     

        // white background notification bar
        whiteNotificationBar(contactsrecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        contactsrecyclerView.setLayoutManager(mLayoutManager);
        contactsrecyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        contactsrecyclerView.setItemAnimator(new DefaultItemAnimator());

        getContacts();
    }

    private void getContacts() {
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
                            noRecords.setVisibility(View.GONE);
                            contactsrecyclerView.setVisibility(View.VISIBLE);

                            contactListAdapter = new ContactListAdapter(GetContactlist.this, getcontactlist.getListResult(), GetContactlist.this);
                            contactsrecyclerView.setAdapter(contactListAdapter);
                        }
                        else {
                            noRecords.setVisibility(View.VISIBLE);
                            contactsrecyclerView.setVisibility(View.GONE);
                        }



                    }});}
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//       // searchView.setMaxWidth(Integer.MIN_VALUE);
//
//        // listening to search query text change
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                contactListAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                contactListAdapter.getFilter().filter(query);
//                return false;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        // close search view on back button pressed
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            return;
//        }
//        super.onBackPressed();
//    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int flags = view.getSystemUiVisibility();
//            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onContactSelected(Getcontactlist.ListResult contact) {
        Intent mySuperIntent = new Intent(GetContactlist.this, SendMoney.class);
        mySuperIntent.putExtra("contactnumber",contact.getContactNumber());
        mySuperIntent.putExtra("name",contact.getContactName());
        mySuperIntent.putExtra("Amount","");
        mySuperIntent.putExtra("WalletID",contact.getWalletId());
        mySuperIntent.putExtra("Userid",contact.getContactId());
        startActivity(mySuperIntent);
    }
}
