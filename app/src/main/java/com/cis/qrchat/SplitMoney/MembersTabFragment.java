package com.cis.qrchat.SplitMoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.GetGroups;
import com.cis.qrchat.R;
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

public class MembersTabFragment extends Fragment implements MembersTabViewAdapter.GroupMemberdapterAdapterListner {
    private MemberViewModel memberViewModel;
    private String gName; // group name
    private MembersTabViewAdapter adapter;
    private List<MemberEntity> members = new ArrayList<>(); // maintain a list of all the existing members of the group from the database
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private String groupId;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        groupId = getActivity().getIntent().getExtras().getString("GroupId");
        Log.d("ExtraItem", "GroupId=" + groupId);

    }

    static MembersTabFragment newInstance(String gName) {
        Bundle args = new Bundle();
        args.putString("group_name", gName);
        MembersTabFragment f = new MembersTabFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.members_fragment,container,false);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.Custom)
                .build();

        GetGroupMembers();

        if(getArguments() == null) {
            return view;
        }
        gName = getArguments().getString("group_name"); // get group name from bundle

        // prepare recycler view for displaying all members of the group
        recyclerView = view.findViewById(R.id.membersRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);


//        recyclerView.setHasFixedSize(true);
//        if(getActivity() != null) {
//            adapter = new MembersTabViewAdapter(gName,getActivity().getApplication(),this);
//        }
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);

        // if data in database(MemberEntity) changes, call the onChanged() below and recreate recycler view
//        memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(getActivity().getApplication(),gName)).get(MemberViewModel.class);
//        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
//            @Override
//            public void onChanged(List<MemberEntity> memberEntities) {
//                adapter.storeToList(memberEntities); // Recreate the recycler view by passing the new List<MemberEntity> to the adapter
//                members = memberEntities;
//            }
//        });


        // Implement Add new member function
        FloatingActionButton addFloating = view.findViewById(R.id.membersFragmentAdd); // floating button for add new member
        addFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an add member intent
                Intent intent = new Intent(getActivity(), AddEditMemberActivity.class);
                intent.putExtra("groupName",gName);
                intent.putExtra("requestCode",1); // using requestCode(value = 1) to identify add member intent
                if(getActivity() != null) {
                    getActivity().startActivityFromFragment(MembersTabFragment.this,intent,1);
                }
            }
        });

        // implement edit member intent
        // create new MembersTabViewAdapter.OnItemClickListener interface object and pass it as a parameter to MembersTabViewAdapter.setOnItemClickListener method
//        adapter.setOnItemClickListener(new MembersTabViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(MemberEntity member) {
//                // create an edit member intent
//                Intent intent = new Intent(getActivity(), AddEditMemberActivity.class);
//                intent.putExtra("memberName",member.name);
//                intent.putExtra("requestCode",2); // // using requestCode(value = 2) to identify edit member intent
//                intent.putExtra("avatarResource",member.mAvatar);
//                intent.putExtra("memberId",member.id);
//                intent.putExtra("groupName",gName);
//
//                if(getActivity() != null) {
//                    getActivity().startActivityFromFragment(MembersTabFragment.this,intent,2); // launch the intent
//                }
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.members_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.deleteAllMembers) {
            if(!members.isEmpty()) { // condition prevents initiating a deleteAll operation if there are no members to delete
                memberViewModel.deleteAll(gName);
                Toast.makeText(getActivity(), getResources().getString(R.string.allmembersdeleted), Toast.LENGTH_SHORT).show();
                return true;
            }
            Toast.makeText(getActivity(), getResources().getString(R.string.nothingtodelete), Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onPause() {
//        // close ActionMode if the user decides to leave the fragment while multiSelect is ON
//        if(adapter.multiSelect) {
//            adapter.actionMode.finish();
//            adapter.multiSelect = false;
//            adapter.selectedItems.clear();
//            adapter.notifyDataSetChanged();
//        }
//        super.onPause();
//    }


    private void GetGroupMembers() {
        String  User_id = SharedPrefsData.getInstance(getContext()).getStringFromSharedPrefs("USERID");
        Log.e("============>",User_id);
        ApiService service = ServiceFactory.createRetrofitService(getContext(), ApiService.class);
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

                            adapter = new MembersTabViewAdapter(getContext(), getGroupMembers.getListResult(), MembersTabFragment.this);
                            recyclerView.setAdapter(adapter);

                            for (int i = 0; i<= getGroupMembers.getListResult().size(); i++)

                            Log.d("GroupMembers", "Name==" + getGroupMembers.getListResult().get(i).getName());

                        }

                    }});}

    @Override
    public void onGroupMemberSelected(GetGroupMembers.ListResult groupmembers) {

    }
}