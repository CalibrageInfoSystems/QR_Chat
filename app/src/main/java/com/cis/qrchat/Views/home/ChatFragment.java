package com.cis.qrchat.Views.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.cis.qrchat.Adapters.ChatListAdapter;
import com.cis.qrchat.Chat.adapters.CustomChannelListAdapter;
import com.cis.qrchat.Chat.fragments.CustomChannelListFragment;
import com.cis.qrchat.Model.Chat_data;
import com.cis.qrchat.R;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;
import com.sendbird.uikit.fragments.ChannelListFragment;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import rx.Subscription;

import static com.cis.qrchat.common.CommonUtil.updateResources;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements ChatListAdapter.ContactsAdapterListener{
    public static String TAG = "ChatFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private List<Chat_data> chat_List = new ArrayList<>();
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    TextView noRecords;
    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;



    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    public ChatFragment() {
        // Required empty public constructor
    }


    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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

    private void showCustomChannelActivity(String url) {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final int langID = SharedPrefsData.getInstance(getContext()).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(getContext(), "ar");

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            updateResources(getContext(), "en-US");
        }


        View view = inflater.inflate(R.layout.fragment_chat,
                container, false);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext( getContext())
                .setTheme(R.style.Custom)
                .build();
        noRecords = (TextView) view.findViewById(R.id.no_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        chat_List = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(getContext(), chat_List, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, 36));
         recyclerView.setAdapter(chatListAdapter);



       // fetchchatdata();
        return view;
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int flags = view.getSystemUiVisibility();
//            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            view.setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void fetchchatdata() {

        Chat_data a = new Chat_data( "QR Chat", "https://api.androidhive.info/json/images/tom_cruise.jpg","Welcome to QR  pay APP .......","11.50AM");
        chat_List.add(a);
        a = new Chat_data( "QR Care", "https://api.androidhive.info/json/images/tom.jpg","Welcome to QR chat pay APP .......","10.50AM");
        chat_List.add(a);
        a = new Chat_data( "Roja", "https://api.androidhive.info/json/images/will.jpg","Welcome to QR chat pay APP .......","8.50AM");
        chat_List.add(a);
        a = new Chat_data( "Mahesh", "https://api.androidhive.info/json/images/robert.jpg","Thankyou","8.00AM");
        chat_List.add(a);  a = new Chat_data( "Arun", "https://api.androidhive.info/json/images/kate.jpg","Nothing ....","7.50AM");
        chat_List.add(a);
        a = new Chat_data( "Anil", "https://api.androidhive.info/json/images/russell.jpg","Welcome","7.10AM");
        chat_List.add(a);
        a = new Chat_data( "QR Care", "https://api.androidhive.info/json/images/tom.jpg","Welcome to QR chat pay APP .......","10.50AM");
        chat_List.add(a);
        a = new Chat_data( "Roja", "https://api.androidhive.info/json/images/will.jpg","Welcome to QR chat pay APP .......","8.50AM");
        chat_List.add(a);
        a = new Chat_data( "Mahesh", "https://api.androidhive.info/json/images/robert.jpg","Thankyou","8.00AM");
        chat_List.add(a);  a = new Chat_data( "Arun", "https://api.androidhive.info/json/images/kate.jpg","Nothing ....","7.50AM");
        chat_List.add(a);
        a = new Chat_data( "Anil", "https://api.androidhive.info/json/images/russell.jpg","Welcome","7.10AM");
        chat_List.add(a);
        a = new Chat_data( "QR Care", "https://api.androidhive.info/json/images/tom.jpg","Welcome to QR chat pay APP .......","10.50AM");
        chat_List.add(a);
        a = new Chat_data( "Roja", "https://api.androidhive.info/json/images/will.jpg","Welcome to QR chat pay APP .......","8.50AM");
        chat_List.add(a);
        a = new Chat_data( "Mahesh", "https://api.androidhive.info/json/images/robert.jpg","Thankyou","8.00AM");
        chat_List.add(a);  a = new Chat_data( "Arun", "https://api.androidhive.info/json/images/kate.jpg","Nothing ....","7.50AM");
        chat_List.add(a);
        a = new Chat_data( "Anil", "https://api.androidhive.info/json/images/russell.jpg","Welcome","7.10AM");
        chat_List.add(a);

    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//

//        if (searchItem != null) {
//             = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//
//            queryTextListener = new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    Log.i("onQueryTextChange", newText);
//
//                    return true;
//                }
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
//
//                    return true;
//                }
//            };
//            searchView.setOnQueryTextListener(queryTextListener);
//        }
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                // Not implemented here
//                return false;
//            default:
//                break;
//        }
//        searchView.setOnQueryTextListener(queryTextListener);
//        return super.onOptionsItemSelected(item);
//    }
//

////        if (searchItem != null) {
////            searchView = (SearchView) searchItem.getActionView();
////        }
////        if (searchView != null) {
////            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
////
////            queryTextListener = new SearchView.OnQueryTextListener() {
////                @Override
////                public boolean onQueryTextChange(String newText) {
////                    Log.i("onQueryTextChange", newText);
////
////                    return true;
////                }
////                @Override
////                public boolean onQueryTextSubmit(String query) {
////                    Log.i("onQueryTextSubmit", query);
////
////                    return true;
////                }
////            };
////            searchView.setOnQueryTextListener(queryTextListener);
////        }
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onContactSelected(Chat_data contact) {
        Intent mySuperIntent = new Intent(getActivity(), ChatRoomActivity.class);
        startActivity(mySuperIntent);
    }
}