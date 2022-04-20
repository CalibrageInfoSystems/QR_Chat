package com.cis.qrchat.Views.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.qrchat.Chat.BaseApplication;
import com.cis.qrchat.Chat.Groupchat.GroupChannelActivity;
import com.cis.qrchat.Chat.adapters.CustomChannelListAdapter;
import com.cis.qrchat.Chat.call.CallService;
import com.cis.qrchat.Chat.call.HistoryFragment;
import com.cis.qrchat.Chat.utils.BroadcastUtils;
import com.cis.qrchat.Chat.utils.PrefUtilsCall;
import com.cis.qrchat.Chat.utils.PushUtilsCall;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.Model.Item;
import com.cis.qrchat.R;
import com.cis.qrchat.SplitMoney.SplitActivity;
import com.cis.qrchat.Views.account.AddFriendsActivity;
import com.cis.qrchat.Views.account.Contactlist;
import com.cis.qrchat.Views.home.ProfileSubActivities.QRPayAccount;
import com.cis.qrchat.common.CustomBottomNavigationView;
import com.cis.qrchat.common.MultiSelectionSpinner;
import com.cis.qrchat.localData.SharedPrefsData;
import com.cis.qrchat.service.APIConstantURL;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;

import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.MessageSearchQuery;
import com.sendbird.android.SendBirdException;
import com.sendbird.calls.AuthenticateParams;
import com.sendbird.calls.DirectCallLog;
import com.sendbird.calls.SendBirdCall;
import com.sendbird.calls.User;
import com.sendbird.calls.handler.AuthenticateHandler;
import com.sendbird.uikit.fragments.ChannelListFragment;
import com.sendbird.uikit.interfaces.OnMenuItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static com.cis.qrchat.common.CommonUtil.updateResources;

public class HomeActivity extends AppCompatActivity implements CustomBottomNavigationView.OnNavigationItemSelectedListener,MultiSelectionSpinner.OnMultipleItemSelectedListener {
    private static final String USER_EVENT_HANDLER_KEY = "USER_EVENT_HANDLER_KEY";
    Integer mSelectedItem;
    CustomBottomNavigationView customBottomNavigationView1;
    private DrawerLayout dl;
    private Toolbar toolbar;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private FrameLayout content_frame;
    private FragmentManager fragmentManager;
    private TextView toolbar_title;
    int langID;
    private Dialog _dialog;
    private int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private  MenuItem menuitem;
    private BroadcastReceiver mReceiver;
    FloatingActionButton myFab;
    EditText groupName;
    MultiSelectionSpinner userContactsSpinner;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    ArrayList<Item> usercontactsArr;
    ArrayList<Item> selecteduserContacts = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");

        else
            updateResources(this, "en-US");
        setContentView(R.layout.activity_home);
        requestMultiplePermissions();
        initviews();
        setViews();
        initSendBird();

        // For Video Calling
       registerReceiver();


       String Userid = PrefUtilsCall.getUserId(HomeActivity.this);
       String pushtoken = PrefUtilsCall.getPushToken(HomeActivity.this);
        AuthenticateParams params = new AuthenticateParams(Userid)
                .setAccessToken("9618f1912558a269adaa5304e4d517c2ddbf7b9a")
                .setPushToken(pushtoken, false);

        SendBirdCall.authenticate(params, new AuthenticateHandler() {
            @Override
            public void onResult(User user, com.sendbird.calls.SendBirdException e) {
                if (e == null) {
                    // The user is authenticated successfully.
                    PushUtilsCall.registerPushToken(getApplicationContext(), pushtoken, f -> {
                        if (f != null) {
                            Log.i(BaseApplication.TAG, "[MyFirebaseMessagingService] registerPushTokenForCurrentUser() => e: " + f.getMessage());
                        }
                    });
                }
            }
        });

        Log.d("HomeActivity","===> Analysis ==> userid :"+Userid);
        Log.d("HomeActivity","===> Analysis ==>  pushtoken :"+pushtoken);

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), getResources().getString(R.string.userPermission), Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

                    }



                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void registerReceiver() {
        Log.i(BaseApplication.TAG, "[MainActivity] registerReceiver()");

        if (mReceiver != null) {
            return;
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(BaseApplication.TAG, "[MainActivity] onReceive()");

                DirectCallLog callLog = (DirectCallLog)intent.getSerializableExtra(BroadcastUtils.INTENT_EXTRA_CALL_LOG);
                if (callLog != null) {
//                    HistoryFragment historyFragment = (HistoryFragment) mMainPagerAdapter.getItem(1);
//                    historyFragment.addLatestCallLog(callLog);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastUtils.INTENT_ACTION_ADD_CALL_LOG);
        registerReceiver(mReceiver, intentFilter);
    }
    private void unregisterReceiver() {
        Log.i(BaseApplication.TAG, "[MainActivity] unregisterReceiver()");

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
    private void initviews() {
         toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        customBottomNavigationView1 = findViewById(R.id.customBottomBar);

        customBottomNavigationView1.inflateMenu(R.menu.menu);
      //  toolbar = findViewById(R.id.toolbar);
        nv = (NavigationView) findViewById(R.id.nv);
      //  dl = (DrawerLayout) findViewById(R.id.activity_main);
 //    t = new ActionBarDrawerToggle(this, dl, R.string.app_name, R.string.app_name);
        myFab = (FloatingActionButton) findViewById(R.id.fab);


         fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, getchatFragment());
// note: there is NOT a addToBackStack call
        fragmentTransaction.commit();

//      fragmentManager = getSupportFragmentManager();
//        content_frame = (FrameLayout) findViewById(R.id.content_frame);
//        ChatFragment chatFragment = new ChatFragment();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content_frame, chatFragment, "homeTag")
//                .commit();

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

    }
    private void setViews() {


     initToolBar();
        GetUserContacts();
        customBottomNavigationView1.setOnNavigationItemSelectedListener(this);
        viewFragment(getchatFragment(),"Chatfragment");
        setToolbarTitle(getResources().getString(R.string.chats)+"        ");

    }

//    long backPressedAt = System.currentTimeMillis();
//    int backPressCount = 0;
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            final long currentTime = System.currentTimeMillis();
//            if (currentTime- backPressedAt < 888)
//            {
//                backPressCount++;
//            }
//            else
//            {
//                backPressCount = 0;
//            }
//            backPressedAt = currentTime;
//            if(backPressCount == 2)
//            {
//                return super.onKeyDown(keyCode, event);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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

                            usercontactsArr = new ArrayList<>();
                            for (Getcontactlist.ListResult data : getcontactlist.getListResult()
                            ) {
                                usercontactsArr.add(new Item(data.getContactId(),data.getContactName(),false));

                            }

                        }



                    }});}


    public void initToolBar() {



// TOdo update icon

        toolbar.setNavigationIcon(R.drawable.ic_three_dots_more_vertical);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(View v) {
                        showPopup(v);
//                        if (!dl.isDrawerOpen(Gravity.START)) dl.openDrawer(Gravity.START);
//                        else dl.closeDrawer(Gravity.END);
                    }
                }
        );


        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(HomeActivity.this, QRScannerMainActivity.class));
                }

        });
    }
//    private void showPopup(View v) {
//        Context wrapper = new ContextThemeWrapper(this, R.style.popup);
//        PopupMenu mypopupmenu = new PopupMenu(wrapper, v);
//        MenuInflater inflater = mypopupmenu.getMenuInflater();
//        inflater.inflate(R.menu.chat_menu_option, mypopupmenu.getMenu());
//        mypopupmenu.show();
//        mypopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.action_newchat:
//                        // Your code goes here
//                        break;
//
//                    case R.id.menuFrench:
//                        // Your code goes here
//                        break;
//                }
//                return false;
//            }
//        });
//    }
    private void showPopup(View view) {
        Context myContext = null;
        MenuBuilder menuBuilder =new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        if (langID == 2) {
             myContext = new ContextThemeWrapper(HomeActivity.this, R.style.menuStyle);
        }else{
            myContext = new ContextThemeWrapper(HomeActivity.this, R.style.menunewStyle);
        }
        inflater.inflate(R.menu.chat_menu_option, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(myContext, menuBuilder, view);
        optionsMenu.setForceShowIcon(true);
       // optionsMenu.setAnchorView();
        MenuCompat.setGroupDividerEnabled(menuBuilder, true);
        // Set Item Click Listener
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_newchat:
                        Intent mySuperIntent = new Intent(HomeActivity.this, Contactlist.class);

                        startActivity(mySuperIntent);
                        return true;
                    case R.id.action_Addfriend:

                        Intent intent = new Intent(HomeActivity.this, AddFriendsActivity.class);
                        startActivity(intent);
                        // finish();
                        // call Guest class or function here
                        return true;
                    case R.id.action_creategroup:

                        if (usercontactsArr != null && usercontactsArr.size() > 0) {

                            showUserContactsDialog(HomeActivity.this);
                            return true;
                        }else {

                            showDialog(HomeActivity.this, "No Contacts to Add");
                            return false;
                        }

                    case R.id.action_scan:
                        startActivity(new Intent(HomeActivity.this, QRScannerMainActivity.class));
                        return true;
                    case R.id.action_qrcare:
                        // call My Buddies class or function here
                        startActivity(new Intent(HomeActivity.this, Contactus.class));
                        return true;
                }
                return true;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

                MenuCompat.setGroupDividerEnabled(menu, true);
                menu.setGroupDividerEnabled(true);
            }
        });


        // Display the menu
        optionsMenu.show();

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



    public void showUserContactsDialog(Activity activity) {
        _dialog = new Dialog(activity);
        _dialog = new Dialog(activity);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setContentView(R.layout.addgroup);
        _dialog.show();

        Button submitGroupBtn, cancelBtn;

        submitGroupBtn = _dialog.findViewById(R.id.addgroupSubmitBtn);
        cancelBtn = _dialog.findViewById(R.id.cancelBtn);
        groupName = _dialog.findViewById(R.id.groupNamee);

        userContactsSpinner = _dialog.findViewById(R.id.peoplesspin);
        userContactsSpinner.setListener(HomeActivity.this);
        userContactsSpinner.setItems(usercontactsArr);



        submitGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _dialog.dismiss();
                selecteduserContacts = userContactsSpinner.getSelectedItems();
                List<String> userIds = new ArrayList<>();
                for(int i = 1; i < selecteduserContacts.size(); i++) {

                    Log.e("selecteduserContacts======",selecteduserContacts.get(i).getId());
                   userIds.add(selecteduserContacts.get(i).getId());}

                createGroupChannel(userIds,false );
             //   createGroupChannel()


                Log.e("selecteduserContacts======436",selecteduserContacts.size()+"");
              //  finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialog.dismiss();
            }
        });
    }



    public  void setToolbarTitle(String title){
        toolbar_title.setText(title);
    }

//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main, menu);
//         menuitem = menu.findItem(R.id.action_Addfriend);
//        menuitem.setVisible(false);
//
//        MenuItem AddColorButton = (MenuItem)findViewById(R.id.menu_insert);
//
//        boolean onOptionsItemSelected(AddColorButton) {
//                Intent intent = new  Intent(SimpleFlashLightActivity.this,
//                BlueFlashLightActivity.class);
//        startActivity(intent);
//        return true;
//        ;
//
//    }

   public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
       MenuInflater menuInflater = getMenuInflater();
       menuInflater.inflate(R.menu.menu_main, menu);
       menuitem = menu.findItem(R.id.action_Addfriend);
       menuitem.setVisible(false);
            return true;

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_Addfriend:

                    Intent intent = new Intent(HomeActivity.this, SearchContactlist.class);
                    startActivity(intent);
                  //  finish();
                    // call Guest class or function here
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

//        MenuItem searchItem = menu.findItem(R.id.action_search);

//        SearchManager searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(HomeActivity.this.getComponentName()));
//        }

    private void showMsgDirectMenuXml(MenuItem item) {
        Toast toast = Toast.makeText(this, "OK", Toast.LENGTH_LONG);
        toast.show();
    }
    private void showCustomChannelActivity(String channelUrl) {
        Log.e("===========>238",channelUrl);

        Intent intent = new Intent(HomeActivity.this, GroupChannelActivity.class);
        intent.putExtra("groupChannelUrl", channelUrl);
        startActivity(intent);



    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        View view = new View(this);
//        item.setActionView(view);
//        view.setPadding(100,0,100,0); //left , top ,right , bottom

        switch (item.getItemId()) {

            case R.id.action_chat: {
                mSelectedItem = item.getItemId();
                menuitem.setVisible(false);
                viewFragment( getchatFragment(), "Chatfragment");
                setToolbarTitle(item.getTitle().toString()+"        ");

                toolbar.setNavigationIcon(R.drawable.ic_three_dots_more_vertical);


                return true;
            }
            case R.id.action_call: {
                mSelectedItem = item.getItemId();
                viewFragment(new HistoryFragment(), CallFragment.TAG);
                setToolbarTitle("        "+item.getTitle().toString());

               menuitem.setVisible(true);
                toolbar.setNavigationIcon(null);
                return true;
            }
            case R.id.action_explore: {

              menuitem.setVisible(false);
                mSelectedItem = item.getItemId();

                viewFragment(new ExploreFragment(), ExploreFragment.TAG);
                setToolbarTitle("   "+item.getTitle().toString());
                toolbar.setNavigationIcon(null);
                return true;
            }
            case R.id.action_profile: {
            menuitem.setVisible(false);
                mSelectedItem = item.getItemId();

                viewFragment(new ProfileFragment(), ProfileFragment.TAG);
                setToolbarTitle("   "+item.getTitle().toString());
                toolbar.setNavigationIcon(null);
                return true;
            }

        }
        return false;
    }

    private void createGroupChannel(List<String> userIds, boolean distinct) {

        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }
                Log.d("GroupChannel","Channel URL :"+groupChannel.getUrl());
                Intent intent = new Intent(HomeActivity.this, GroupChannelActivity.class);
                intent.putExtra("groupChannelUrl", groupChannel.getUrl());
                startActivity(intent);
            }
        });
    }
  public Fragment  getchatFragment()
{


    return new ChannelListFragment.Builder(R.style.CustomChannelListStyle)
//            .setCustomChannelListFragment(new CustomChannelListFragment())
            .setUseHeader(false)
            .setHeaderTitle(null)
            .setUseHeaderLeftButton(true)
            .setUseHeaderRightButton(true)
            .setHeaderLeftButtonIconResId(R.drawable.icon_arrow_left)


            .setHeaderRightButtonIconResId(R.drawable.icon_create)
            .setHeaderLeftButtonListener(null)
            .setHeaderRightButtonListener(null)

            .setChannelListAdapter(new CustomChannelListAdapter())
            .setItemClickListener((view, i, channel) -> showCustomChannelActivity(channel.getUrl()))

            .setItemLongClickListener(null)
            .setGroupChannelListQuery(null)
            .build();


//    return new ChannelListFragment.Builder()
//            .setUseHeader(false)
//            .setUseHeaderLeftButton(false)
//            .build();

}
    private void viewFragment(Fragment fragment, String name){
      fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        // 1. Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if( name.equals("Chatfragment") ) {
            fragmentTransaction.addToBackStack(name);
        }
        // Commit !
        fragmentTransaction.commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if( fragmentManager.getBackStackEntryCount() <= count){
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack(ChatFragment.TAG, POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    customBottomNavigationView1.getMenu().getItem(0).setChecked(true);
                }
            }
        });
    }

//    private void viewFragment(Fragment fragment, String name) {
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.selace(R.id.content_frame, fragment);
//        // 1. Know how many fragments there are in the stack
//        final int count = fragmentManager.getBackStackEntryCount();
//        // 2. If the fragment is **not** "home type", save it to the stack
//        if (name.equals(ChatFragment.TAG)) {
//            fragmentTransaction.addToBackStack(name);
//        }
//        // Commit !
//        fragmentTransaction.commit();
//        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
//        // OnBackStackChanged callback
//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                // If the stack decreases it means I clicked the back button
//                if (fragmentManager.getBackStackEntryCount() <= count) {
//                    // pop all the fragment and remove the listener
//                    fragmentManager.popBackStack(ChatFragment.TAG, POP_BACK_STACK_INCLUSIVE);
//                    fragmentManager.removeOnBackStackChangedListener(this);
//                    // set the home button selected
//                    customBottomNavigationView1.getMenu().getItem(0).setChecked(true);
//                    menuitem.setVisible(false);
//                }
//            }
//        });
//    }



    boolean doubleBackToExitPressedOnce = false;


    @Override

    public void onBackPressed() {
        MenuItem homeItem = customBottomNavigationView1.getMenu().getItem(0);
        Log.e("===========>757",homeItem.getItemId()+"");
        if (mSelectedItem !=null && mSelectedItem != homeItem.getItemId()) {


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,  getchatFragment(),"Chatfragment" )
                    .commit();
            // Select home item
            customBottomNavigationView1.setSelectedItemId(homeItem.getItemId());
            Log.e("===========>766>",homeItem.getItemId()+"");
        }

        else {
            finishAffinity();
        }
//        else {
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//
//            this.doubleBackToExitPressedOnce = true;
//           Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce=false;
//                }
//            }, 2000);
//        }
    }

//    @Override
//    public void onBackPressed() {
//        if (onBackPressedListener != null)
//            onBackPressedListener.doBack();
//        else
//            super.onBackPressed();
//    }
//
//    @Override
//    protected void onDestroy() {
//        onBackPressedListener = null;
//        super.onDestroy();
//    }




    @Override
    protected void onResume() {
        super.onResume();
       // customBottomNavigationView1.getMenu().getItem(0).setChecked(true);

        //startActivity(getIntent());
        langID = SharedPrefsData.getInstance(this).getIntFromSharedPrefs("lang");
        if (langID == 2)
            updateResources(this, "ar");

        else
            updateResources(this, "en-US");

    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//    }

    private void initSendBird() {

    }

    @Override
    public void selecteditems(ArrayList<Item> items) {
     Log.e("=======>",items.size()+"");

    }
}
