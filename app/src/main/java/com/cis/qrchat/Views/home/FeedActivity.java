package com.cis.qrchat.Views.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.cis.qrchat.Model.Feed_Data;
import com.cis.qrchat.Model.Profile_Data;
import com.cis.qrchat.R;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class FeedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView rcv_feed;
    FeedRecycleAdapter feedRecycleAdapter;
    private List<Feed_Data> feed_List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        init();
        setViews();
        settoolbar();
    }

    private void init() {

        rcv_feed = findViewById(R.id.rcv_feed);

    }
    private void setViews() {

        feed_List = new ArrayList<>();
        feedRecycleAdapter = new FeedRecycleAdapter(FeedActivity.this, feed_List);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FeedActivity.this);
        rcv_feed.setLayoutManager(mLayoutManager);
        rcv_feed.setItemAnimator(new DefaultItemAnimator());
        rcv_feed.setAdapter(feedRecycleAdapter);
        fetchchatdata();

    }

        private void fetchchatdata() {

        int[] feedcovers = new int[]{
                R.drawable.pisatower,
                R.drawable.tajmahal,
                R.drawable.statue,
        };

        int[] maincovers = new int[]{
                R.drawable.ic_account,
                R.drawable.ic_user,
        };

        Feed_Data a = new Feed_Data(maincovers[0],feedcovers[0], "Mahesh", "The Leaning Tower of Pisa (Italian: torre pendente di Pisa) or simply the Tower of Pisa (torre di Pisa [ˈtorre di ˈpiːsa]) is the campanile, or freestanding bell tower, of the cathedral of the Italian city of Pisa", "Today 11:35 PM", "800", "50");
        feed_List.add(a);
        a = new Feed_Data(maincovers[1],feedcovers[1], "Arun", "An immense mausoleum of white marble, built in Agra between 1631 and 1648 by order of the Mughal emperor Shah Jahan in memory of his favourite wife, the Taj Mahal is the jewel of Muslim art in India and one of the universally admired masterpieces of the world's heritage.", "Yesterday 01:12 PM", "120", "5");
        feed_List.add(a);
        a = new Feed_Data(maincovers[0],feedcovers[2], "Roja", "The Statue of Liberty (officially named Liberty Enlightening the World and sometimes referred to as Lady Liberty) is a monument symbolising the United States", "Nov 20 04:27 PM", "632", "114");
        feed_List.add(a);

    }


    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrwallet));
        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                updateResources(this, "ar");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left_rtl);
                //   holder.rightarrow.setRotation(180);

            } else {
                updateResources(this, "en-US");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
            }
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
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

}