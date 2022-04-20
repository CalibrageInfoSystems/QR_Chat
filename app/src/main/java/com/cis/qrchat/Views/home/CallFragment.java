package com.cis.qrchat.Views.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cis.qrchat.Adapters.CallListAdapter;
import com.cis.qrchat.Adapters.ChatListAdapter;
import com.cis.qrchat.Model.Call_data;
import com.cis.qrchat.Model.Chat_data;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.splashScreen.SplashActivity;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import rx.Subscription;

import static com.cis.qrchat.common.CommonUtil.updateResources;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallFragment extends Fragment implements  CallListAdapter.ContactsAdapterListener {
    public static String TAG = "CallFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    TextView noRecords;
    RecyclerView recyclerView;
    CallListAdapter callListAdapter;
    private List<Call_data> call_List = new ArrayList<>();

    public CallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallFragment newInstance(String param1, String param2) {
        CallFragment fragment = new CallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final int langID = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(getContext(), "ar");

        else
            updateResources(getContext(), "en-US");

        View view = inflater.inflate(R.layout.fragment_call, container, false);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext( getContext())
                .setTheme(R.style.Custom)
                .build();
        noRecords = (TextView) view.findViewById(R.id.no_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        call_List = new ArrayList<>();
        callListAdapter = new CallListAdapter(getContext(), call_List, this);

        // white background notification bar
       whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(callListAdapter);
        fetchcalldata();
        return view;

    }
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int flags = view.getSystemUiVisibility();
//            flags |= View.STATUS_BAR_HIDDEN;
//            view.setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void fetchcalldata() {
        int[] covers = new int[]{
                R.drawable.group_call,
                R.drawable.person_call,
                R.drawable.missedcall_icon,
                R.drawable.incoming_call,
                R.drawable.outgoing_call,


        };

        Call_data a = new Call_data( "Group call", "https://api.androidhive.info/json/images/tom_cruise.jpg","Group call","11.56 AM", covers[3]);
        call_List.add(a);
        a = new Call_data("Roja", "https://api.androidhive.info/json/images/tom.jpg","Missed Call(2)","10.30 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Mahesh", "https://api.androidhive.info/json/images/will.jpg"," Call Ended - 1:09 ","9.30 AM", covers[4]);
        call_List.add(a);
        a = new Call_data("Arun", "https://api.androidhive.info/json/images/tom.jpg","Call Ended - 5:09 )","9.29 AM", covers[4]);
        call_List.add(a);
        a = new Call_data("Anil", "https://api.androidhive.info/json/images/will.jpg","Missed Call(2)","9.20 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Likitha","https://api.androidhive.info/json/images/robert.jpg","Missed Call(2)","9.10 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Sowmya","https://api.androidhive.info/json/images/russell.jpg","Missed Call(2)","9.00 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Group Call ","https://api.androidhive.info/json/images/russell.jpg","Group call with (Roja,Arun.....)","9.00 AM", covers[3]);
        call_List.add(a);
        a = new Call_data("Roja", "https://api.androidhive.info/json/images/tom.jpg","Missed Call(2)","10.30 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Mahesh", "https://api.androidhive.info/json/images/will.jpg"," Call Ended - 1:09 ","9.30 AM", covers[4]);
        call_List.add(a);
        a = new Call_data("Arun", "https://api.androidhive.info/json/images/tom.jpg","Call Ended - 5:09 )","9.29 AM", covers[4]);
        call_List.add(a);
        a = new Call_data("Anil", "https://api.androidhive.info/json/images/will.jpg","Missed Call(2)","9.20 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Likitha","https://api.androidhive.info/json/images/robert.jpg","Missed Call(2)","9.10 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Sowmya","https://api.androidhive.info/json/images/russell.jpg","Missed Call(2)","9.00 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Group Call ","https://api.androidhive.info/json/images/russell.jpg","Group call with (Roja,Arun.....)","9.00 AM", covers[3]);
        call_List.add(a);
        a = new Call_data("Roja", "https://api.androidhive.info/json/images/tom.jpg","Missed Call(2)","10.30 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Mahesh", "https://api.androidhive.info/json/images/will.jpg"," Call Ended - 1:09 ","9.30 AM", covers[4]);
        call_List.add(a);
        a = new Call_data("Arun", "https://api.androidhive.info/json/images/tom.jpg","Call Ended - 5:09 )","9.29 AM", covers[4]);
        call_List.add(a);
        a = new Call_data("Anil", "https://api.androidhive.info/json/images/will.jpg","Missed Call(2)","9.20 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Likitha","https://api.androidhive.info/json/images/robert.jpg"," Call Ended 3.40","9.10 AM", covers[3]);
        call_List.add(a);
        a = new Call_data("Sowmya","https://api.androidhive.info/json/images/russell.jpg","Missed Call(2)","9.00 AM", covers[2]);
        call_List.add(a);
        a = new Call_data("Group Call ","https://api.androidhive.info/json/images/russell.jpg","Group call with (Roja,Arun.....)","9.00 AM", covers[3]);
        call_List.add(a);
    }

    @Override
    public void onContactSelected(Call_data contact) {
        Intent mySuperIntent = new Intent(getActivity(), ChatRoomActivity.class);
        startActivity(mySuperIntent);
    }
}