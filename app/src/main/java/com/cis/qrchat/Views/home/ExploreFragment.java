package com.cis.qrchat.Views.home;

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

import com.cis.qrchat.Adapters.ChatListAdapter;
import com.cis.qrchat.Adapters.ExploreListAdapter;
import com.cis.qrchat.Model.Call_data;
import com.cis.qrchat.Model.Chat_data;
import com.cis.qrchat.Model.Explore_data;
import com.cis.qrchat.R;
import com.cis.qrchat.common.MyDividerItemDecoration;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import rx.Subscription;

import static com.cis.qrchat.common.CommonUtil.updateResources;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment implements  ExploreListAdapter.AdapterListener{
    public static String TAG = "ExploreFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private List<Explore_data> explore_List = new ArrayList<>();
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    TextView noRecords;
    RecyclerView recyclerView;
    ExploreListAdapter exploreListAdapter;
    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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

        View view = inflater.inflate(R.layout.fragment_explore,
                container, false);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext( getContext())
                .setTheme(R.style.Custom)
                .build();
        noRecords = (TextView) view.findViewById(R.id.no_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        explore_List = new ArrayList<>();
        exploreListAdapter = new ExploreListAdapter(getContext(), explore_List, this);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(exploreListAdapter);
        fetchchatdata();
        return view;
    }


    private void fetchchatdata() {
        int[] covers = new int[]{
                R.drawable.ic_life_shots,
                R.drawable.ic_earth,
                R.drawable.ic_news,
                R.drawable.ic_subscription,
                R.drawable.ic_find_nearny,


        };
        Explore_data a = new Explore_data( getResources().getString(R.string.lifeshots), covers[0]);
        explore_List.add(a);
        a = new Explore_data(getResources().getString(R.string.walkinplatform) , covers[1]);
        explore_List.add(a);
        a = new Explore_data(getResources().getString(R.string.news) , covers[2]);
        explore_List.add(a);
        a = new Explore_data(getResources().getString(R.string.subscriptions) , covers[3]);
        explore_List.add(a);
        a = new Explore_data(getResources().getString(R.string.findNearby) , covers[4]);
        explore_List.add(a);
    }

    @Override
    public void onContactSelected(Explore_data contact) {

    }
}