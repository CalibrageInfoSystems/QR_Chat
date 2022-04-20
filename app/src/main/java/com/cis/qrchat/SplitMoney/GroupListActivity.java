package com.cis.qrchat.SplitMoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Adapters.AddContactsAdapter;
import com.cis.qrchat.Model.GetGroups;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.R;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GroupListActivity extends AppCompatActivity implements GroupListActivityViewAdapter.GroupsAdapterListner {
    public static final String EXTRA_TEXT_GNAME = "com.nishantboro.splititeasy.EXTRA_TEXT_GNAME";
    private List<GroupEntity> groupNames = new ArrayList<>();
    private GroupListActivityViewAdapter adapter;
    private GroupViewModel groupViewModel;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_activity);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();


        // set toolbar
        Toolbar toolbar = findViewById(R.id.groupListToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getResources().getString(R.string.groups));

        recyclerView = findViewById(R.id.group_list_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());


        GetUserGroups();

        // prepare recycler view
//        recyclerView = findViewById(R.id.group_list_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        // second parameter below -> attach this activity as a listener to every item in the group list
//        adapter = new GroupListActivityViewAdapter(GroupListActivity.this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        // if data in database(Group) changes, call the onChanged() below
//        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
//        groupViewModel.getAllGroups().observe(this, new Observer<List<GroupEntity>>() {
//            @Override
//            public void onChanged(List<GroupEntity> groupEntities) {
//                // Recreate the recycler view by notifying adapter with the changes
//                // here groupEntities is the list of current items in the groupList
//                groupNames = groupEntities;
//                adapter.saveToList(groupEntities);
//            }
//        });
//
//        adapter.setOnItemClickListener(new GroupListActivityViewAdapter.OnGroupClickListener() {
//            @Override
//            public void onGroupClick(int position) {
//                // get group name of the item the user clicked on from groupNames array
//                String gName = groupNames.get(position).gName;
//
//                // create an intent to launch the HandleOnGroupClickActivity, pass the gName along
//                Intent intent = new Intent(GroupListActivity.this,HandleOnGroupClickActivity.class);
//                intent.putExtra(EXTRA_TEXT_GNAME,gName);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteAllGroups) {
            if(!groupNames.isEmpty()) {
                groupViewModel.deleteAll();
                Toast.makeText(this, getResources().getString(R.string.allgroupsdeleted), Toast.LENGTH_SHORT).show();
                return true;
            }
            Toast.makeText(this, getResources().getString(R.string.nothingtodelete), Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
        // finish if user clicks on back button
        finish();
        return true;
    }

//    @Override
//    public void onPause() {
//        if(adapter.multiSelect) {
//            adapter.actionMode.finish();
//            adapter.multiSelect = false;
//            adapter.selectedItems.clear();
//            adapter.notifyDataSetChanged();
//        }
//        super.onPause();
//    }

    private void GetUserGroups() {
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

                            adapter = new GroupListActivityViewAdapter(GroupListActivity.this, getGroupsresponse.getListResult(), GroupListActivity.this);
                            recyclerView.setAdapter(adapter);
                        }

                    }});}

    @Override
    public void onGroupSelected(GetGroups.ListResult group) {

    }
}
