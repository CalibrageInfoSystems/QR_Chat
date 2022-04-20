package com.cis.qrchat.Views.home.ProfileSubActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.VideoView;

import com.cis.qrchat.Model.Feed_Data;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.FeedActivity;
import com.cis.qrchat.Views.home.FeedRecycleAdapter;
import com.cis.qrchat.localData.SharedPrefsData;

import java.util.ArrayList;
import java.util.List;

import static com.cis.qrchat.common.CommonUtil.updateResources;

public class MyLifeshots extends AppCompatActivity {

    VideoView  videoView;
    private Toolbar toolbar;
    RecyclerView rcv_feed;
    ScrollView scrollView_main;
    FeedRecycleAdapter feedRecycleAdapter;
    private List<Feed_Data> feed_List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lifeshots);

        settoolbar();

        rcv_feed = findViewById(R.id.rcv_feed);
        videoView = findViewById(R.id.vdVw);
        scrollView_main = (ScrollView)findViewById(R.id.scrollView);

        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //Location of Media File
        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video);
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        scrollView_main.smoothScrollTo(0,0); //set it on top

        feed_List = new ArrayList<>();
        feedRecycleAdapter = new FeedRecycleAdapter(MyLifeshots.this, feed_List);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyLifeshots.this);
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
        a = new Feed_Data(maincovers[0],feedcovers[2], "Roja", "The Statue of Liberty (officially named Liberty Enlightening the World and sometimes referred to as Lady Liberty) is a monument symbolising the United States", "Nov 20 04:27 PM", "632", "114");
        feed_List.add(a);
    }


    private void settoolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.qrwallet));
        setSupportActionBar(toolbar);

        final int langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)

        {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left_rtl);

        }

        else {

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

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//
//        menuInflater.inflate(R.menu.feedmenu, menu);
//        MenuCompat.setGroupDividerEnabled(menu, true);
//        return true;
//    }
}