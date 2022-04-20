package com.cis.qrchat.Chat.Groupchat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.qrchat.BuildConfig;
import com.cis.qrchat.Chat.call.CallService;
import com.cis.qrchat.Chat.utils.PrefUtils;
import com.cis.qrchat.Chat.utils.PrefUtilsCall;
import com.cis.qrchat.Model.GetUserinfo;
import com.cis.qrchat.Model.Searchobject;
import com.cis.qrchat.R;
import com.cis.qrchat.Views.home.HadiatMoney;
import com.cis.qrchat.Views.home.SendMoney;
import com.cis.qrchat.common.CommonMethods;
import com.cis.qrchat.service.ApiService;
import com.cis.qrchat.service.ServiceFactory;
import com.cocosw.bottomsheet.BottomSheet;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.BaseMessageParams;
import com.sendbird.android.FileMessage;
import com.sendbird.android.FileMessageParams;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.ReactionEvent;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;


import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import dmax.dialog.SpotsDialog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.webrtc.ContextUtils.getApplicationContext;

public class GroupChatFragment extends Fragment {

    private static final String LOG_TAG = GroupChatFragment.class.getSimpleName();

    private static final int CHANNEL_LIST_LIMIT = 30;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHAT";
    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;

    private static final String STATE_CHANNEL_URL = "STATE_CHANNEL_URL";
    private static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    static final String EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL";
    private int GALLERY = 1, CAMERA = 2;
    private InputMethodManager mIMM;
    private static final String IMAGE_DIRECTORY = "/QRChat_Profile";
    private RelativeLayout mRootLayout;
    private RecyclerView mRecyclerView;
    private GroupChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private EditText mMessageEditText;
    private Button mMessageSendButton;
    private ImageButton mUploadFileButton;
    private View mCurrentEventLayout;
    private TextView mCurrentEventText;

    private GroupChannel mChannel;
    private String mChannelUrl;
    private PreviousMessageListQuery mPrevMessageListQuery;

    private boolean mIsTyping;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private int mCurrentState = STATE_NORMAL;
    private BaseMessage mEditingMessage = null;
    String   mCurrentPhotoPath;
    private String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
    };
    String imageFilePath;

    public static final int PICK_IMAGE_CODE = 100;
    public static final int DS_PHOTO_EDITOR_REQUEST_CODE = 200;

    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1000;

    public static final String OUTPUT_PHOTO_DIRECTORY = "ds_photo_editor_sample";
    String toPersonId;
    private File photoFile;
    private Uri photoURI;

    /**
     * To create an instance of this fragment, a Channel URL should be required.
     */


    public static GroupChatFragment newInstance(@NonNull String channelUrl) {
        GroupChatFragment fragment = new GroupChatFragment();

        Bundle args = new Bundle();
        args.putString( "GROUP_CHANNEL_URL", channelUrl);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.Custom)
                .build();
        if (savedInstanceState != null) {
            // Get channel URL from saved state.
            mChannelUrl = savedInstanceState.getString(STATE_CHANNEL_URL);
        } else {
            // Get channel URL from GroupChannelListFragment.
            mChannelUrl = getArguments().getString( "GROUP_CHANNEL_URL");
        }

        Log.d(LOG_TAG, mChannelUrl);

        mChatAdapter = new GroupChatAdapter(getActivity());
        setUpChatListAdapter();

        // Load messages from cache.
        mChatAdapter.load(mChannelUrl);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_chat, container, false);

        setRetainInstance(true);

        mRootLayout = (RelativeLayout) rootView.findViewById(R.id.layout_group_chat_root);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_group_chat);

        mCurrentEventLayout = rootView.findViewById(R.id.layout_group_chat_current_event);
        mCurrentEventText = (TextView) rootView.findViewById(R.id.text_group_chat_current_event);

        mMessageEditText = (EditText) rootView.findViewById(R.id.edittext_group_chat_message);
        mMessageSendButton = (Button) rootView.findViewById(R.id.button_group_chat_send);
        mUploadFileButton = (ImageButton) rootView.findViewById(R.id.button_group_chat_upload);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mMessageSendButton.setEnabled(true);
                } else {
                    mMessageSendButton.setEnabled(false);
                }
            }
        });

        mMessageSendButton.setEnabled(false);
        mMessageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_EDIT) {
                    String userInput = mMessageEditText.getText().toString();
                    if (userInput.length() > 0) {
                        if (mEditingMessage != null) {
                            editMessage(mEditingMessage, userInput);
                        }
                    }
                    setState(STATE_NORMAL, null, -1);
                } else {
                    String userInput = mMessageEditText.getText().toString();
                    if (userInput.length() > 0) {
                        sendUserMessage(userInput);
                        mMessageEditText.setText("");
                    }
                }


            }
        });

        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           showMessageTypeDialog();




            }
        });

        mIsTyping = false;
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mIsTyping) {
                    setTypingStatus(true);
                }

                if (s.length() == 0) {
                    setTypingStatus(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setUpRecyclerView();
        setHasOptionsMenu(true);

        return rootView;
    }
    private  void ClearChat()
    {
        mChannel.resetMyHistory(new GroupChannel.GroupChannelResetMyHistoryHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    // Handle error.
                }
// Todo

            }
        });
    }

    private void showMessageTypeDialog() {
  new BottomSheet.Builder(getContext(), R.style.BottomSheet_Dialog)
                .grid()// <-- important part
                .sheet(R.menu.menu_bottom_sheet)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case R.id.camera:
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // Ensure that there's a camera activity to handle the intent
                                if (takePictureIntent.resolveActivity( getActivity().getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        // Error occurred while creating the File
                                        return;
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = null;
                                        try {
                                            photoURI = FileProvider.getUriForFile(getContext(),
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    createImageFile());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(takePictureIntent, CAMERA);
                                    }

                                }

                                break;


                            case R.id.gallery:
                                requestMedia();
                                break;
                            case R.id.voicecall:
                                 makeVoiceCall();
                                break;
                            case R.id.videocall:
                                makeVodeoCall();
                                break;
                            case R.id.tranfer:
                                if(mChannel.getMembers().size() >1)
                                {
                                    String Id= null;
                                    for (Member m :mChannel.getMembers())
                                    {
                                        if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(getActivity())))
                                        {
                                            Id = m.getUserId();
                                            gettransactiondetails(Id);

                                        }
                                    }
//
                                }

                                break;

                            case R.id.action_hadiatmoney:
                                if(mChannel.getMembers().size() >1)
                                {
                                    String Id= null;
                                    for (Member m :mChannel.getMembers())
                                    {
                                        if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(getActivity())))
                                        {
                                            Id = m.getUserId();
                                            gettransactiondetailss(Id);

                                        }
                                    }
//
                                }

                                break;
                        }




                    }
                }).show();
//
    }

    private void gettransactiondetailss(String id) {

        //  mdilogue.show();
        JsonObject object = addsearchObject(id);
        ApiService service = ServiceFactory.createRetrofitService(getContext(), ApiService.class);
        mSubscription = service.getuseinfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserinfo>() {
                    @Override
                    public void onCompleted() {

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
                    }

                    @Override
                    public void onNext(GetUserinfo getUserinfo) {

                        if (HttpsURLConnection.HTTP_OK == 200) {
                            Intent transactions = new Intent(getContext(), HadiatMoney.class);
//                            transactions.putExtra("contactnumber",getUserinfo.getResult().get(0).getPhoneNumber());
//                            transactions.putExtra("name",getUserinfo.getResult().get(0).getFullName());
//                            transactions.putExtra("Amount","");
//                            transactions.putExtra("WalletID",getUserinfo.getResult().get(0).getWalletId());
                            transactions.putExtra("groupChannelUrl",mChannel.getUrl());
                            transactions.putExtra("Userid",getUserinfo.getResult().get(0).getId());
                            startActivity(transactions);

                            //  Picasso.with(getContext()).load(profileresponse.getQrContactImage()).error(R.drawable.ic_user).into(myImage);


                        }else{


                        }
                    }
                });}




    private void gettransactiondetails(String id) {

      //  mdilogue.show();
        JsonObject object = addsearchObject(id);
        ApiService service = ServiceFactory.createRetrofitService(getContext(), ApiService.class);
        mSubscription = service.getuseinfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserinfo>() {
                    @Override
                    public void onCompleted() {

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
                    }

                    @Override
                    public void onNext(GetUserinfo getUserinfo) {

                        if (HttpsURLConnection.HTTP_OK == 200) {
                            Intent transactions = new Intent(getContext(), SendMoney.class);
                            transactions.putExtra("contactnumber",getUserinfo.getResult().get(0).getPhoneNumber());
                            transactions.putExtra("name",getUserinfo.getResult().get(0).getFullName());
                            transactions.putExtra("Amount","");
                            transactions.putExtra("WalletID",getUserinfo.getResult().get(0).getWalletId());
                            transactions.putExtra("channelUrl",mChannel.getUrl());
                            transactions.putExtra("Userid",getUserinfo.getResult().get(0).getId());
                            startActivity(transactions);

                            //  Picasso.with(getContext()).load(profileresponse.getQrContactImage()).error(R.drawable.ic_user).into(myImage);


                        }else{


                        }
                    }
                });}

    private JsonObject addsearchObject(String id) {
        Searchobject requestModel = new Searchobject();
        requestModel.setSearchValue(id);

        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents


        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        Log.d("MAHESH","=====> Analysis ==> imagepath:"+mCurrentPhotoPath);


        return image;
    }
    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ;
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir     /* directory */
        );
        return image;
    }
    private void verifyStoragePermissionsAndPerformOperation(int requestPermissionCode) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_CODE);
        } else {
            // Check if we have storage permission
            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // Request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, requestPermissionCode);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_CODE);
            }
        }
    }
    private void makeVoiceCall() {
//        Log.d(LOG_TAG,"===> analysis <=====> voice call Caller Id:"+mChannel.getMembers().get(1).getUserId());
        if(mChannel.getMembers().size() >1)
        {
            String Id= null;
            for (Member m :mChannel.getMembers())
            {
                if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(getContext())))
                {
                    Id = m.getUserId();
                }
            }
            if(!android.text.TextUtils.isEmpty(Id))
            {
                CallService.dial(getContext(),Id,false);
            }else{
                Toast.makeText(getContext(), "Unable to call", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Unable to call", Toast.LENGTH_SHORT).show();
        }
    }
    private void makeVodeoCall() {

        if(mChannel.getMembers().size() >1)
        {
            String Id= null;
            for (Member m :mChannel.getMembers())
            {
                if(!m.getUserId().equalsIgnoreCase(PrefUtilsCall.getUserId(getContext())))
                {

                    Id = m.getUserId();
                }
            }
            if(!android.text.TextUtils.isEmpty(Id))
            {
                Log.d(LOG_TAG,"===> analysis <=====> voice call Caller Id:"+Id);
                CallService.dial(getContext(),Id,true);
            }else{
                Toast.makeText(getContext(), "Unable to call", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getContext(), "Unable to call", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean permissons() {

        Boolean ispermissiongranted = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !CommonMethods.areAllPermissionsAllowed(getContext(), PERMISSIONS_REQUIRED)) {

            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_REQUIRED, CommonMethods.PERMISSION_CODE);


        } else {
            try {
//                showPictureDialog();
            } catch (Exception e) {
                e.getMessage();
            }
            ispermissiongranted = true;
        }

        return ispermissiongranted;
    }

    private File createImageFile1() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile1();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.cis.qrchat.provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    private void refresh() {
        if (mChannel == null) {
            GroupChannel.getChannel(mChannelUrl, new GroupChannel.GroupChannelGetHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                    mChannel = groupChannel;
                    mChatAdapter.setChannel(mChannel);
                    mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            mChatAdapter.markAllMessagesAsRead();
                        }
                    });
                    updateActionBarTitle();
                }
            });
        } else {
            mChannel.refresh(new GroupChannel.GroupChannelRefreshHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                    mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            mChatAdapter.markAllMessagesAsRead();
                        }
                    });
                    updateActionBarTitle();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
// Todo update it

        ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID,getContext(), new ConnectionManager.ConnectionManagementHandler() {
            @Override
            public void onConnected(boolean reconnect) {
                refresh();
            }
        });

        mChatAdapter.setContext(getActivity()); // Glide bug fix (java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity)

        // Gets channel from URL user requested

        Log.d(LOG_TAG, mChannelUrl);

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.markAllMessagesAsRead();
                    // Add new message to view
                    mChatAdapter.addFirst(baseMessage);
                }
            }

            @Override
            public void onMessageDeleted(BaseChannel baseChannel, long msgId) {
                super.onMessageDeleted(baseChannel, msgId);
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.delete(msgId);
                }
            }

            @Override
            public void onMessageUpdated(BaseChannel channel, BaseMessage message) {
                super.onMessageUpdated(channel, message);
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.update(message);
                }
            }

            @Override
            public void onReadReceiptUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTypingStatusUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    List<Member> typingUsers = channel.getTypingMembers();
                    displayTyping(typingUsers);
                }
            }

            @Override
            public void onDeliveryReceiptUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onPause() {
        setTypingStatus(false);

        ConnectionManager.removeConnectionManagementHandler(CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Save messages to cache.
        mChatAdapter.save();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_CHANNEL_URL, mChannelUrl);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_group_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_group_channel_invite) {
//            Intent intent = new Intent(getActivity(), InviteMemberActivity.class);
//            intent.putExtra(EXTRA_CHANNEL_URL, mChannelUrl);
//            startActivity(intent);
//            return true;
//        } else if (id == R.id.action_group_channel_view_members) {
//            Intent intent = new Intent(getActivity(), MemberListActivity.class);
//            intent.putExtra(EXTRA_CHANNEL_URL, mChannelUrl);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_CODE);
        }
        else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setMessage("The app needs this permission to edit photos on your device.");
            builder.setPositiveButton("Update Permission",  new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    verifyStoragePermissionsAndPerformOperation(REQUEST_EXTERNAL_STORAGE_CODE);
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set this as true to restore background connection management.
        SendBird.setAutoBackgroundDetection(true);

        if (requestCode == INTENT_REQUEST_CHOOSE_MEDIA  && resultCode == Activity.RESULT_OK) {
            // If user has successfully chosen the image, show a dialog to confirm upload.


//            Log.d("CAMERA :",extras.get("data"));
            if (data == null) {
                Log.d(LOG_TAG, "data is null!");
                return;
            }
            Log.d(LOG_TAG,"File path :"+data.getData().toString()+"");
            if(data.getData().toString().contains("/image"))
            {
                Intent dsPhotoEditorIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(data.getData());

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, OUTPUT_PHOTO_DIRECTORY);
                startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQUEST_CODE);
            }else{
                sendFileWithThumbnail(data.getData());
            }

        }
       if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {



                   if (!android.text.TextUtils.isEmpty(mCurrentPhotoPath)) {
                       Intent dsPhotoEditorIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);
                       dsPhotoEditorIntent.setData(Uri.parse(mCurrentPhotoPath));

                       dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, OUTPUT_PHOTO_DIRECTORY);
                       startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQUEST_CODE);
                   }else{
                       Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                   }




       }
        if (resultCode ==  Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_CODE:

                    Uri inputImageUri = data.getData();
                    if (inputImageUri != null) {
                        Intent dsPhotoEditorIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);
                        dsPhotoEditorIntent.setData(inputImageUri);

                        // This is optional. By providing an output directory, the edited photo
                        // will be saved in the specified folder on your device's external storage;
                        // If this is omitted, the edited photo will be saved to a folder
                        // named "DS_Photo_Editor" by default.
                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, OUTPUT_PHOTO_DIRECTORY);

                        // You can also hide some tools you don't need as below
//                        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_ORIENTATION};
//                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                        startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQUEST_CODE);
                    } else {
                        Toast.makeText(getActivity(), "Please select an image from the Gallery", Toast.LENGTH_LONG).show();
                    }
                    break;
                case DS_PHOTO_EDITOR_REQUEST_CODE:
                    Uri outputUri = data.getData();
                    sendFileWithThumbnail(outputUri);
                //    imageView.setImageURI(outputUri);
                    Toast.makeText(getActivity(), "Photo saved in " + OUTPUT_PHOTO_DIRECTORY + " folder.", Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }


    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mChatAdapter.getItemCount() - 1) {
                    mChatAdapter.loadPreviousMessages(CHANNEL_LIST_LIMIT, null);
                }
            }
        });
    }

    private void setUpChatListAdapter() {
        mChatAdapter.setItemClickListener(new GroupChatAdapter.OnItemClickListener() {
            @Override
            public void onUserMessageItemClick(UserMessage message) {
                // Restore failed message and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                if (message.getCustomType().equals(GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE)) {
                    try {
                        UrlPreviewInfo info = new UrlPreviewInfo(message.getData());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getUrl()));
                        startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFileMessageItemClick(FileMessage message) {
                // Load media chooser and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                onFileMessageClicked(message);
            }
        });

        mChatAdapter.setItemLongClickListener(new GroupChatAdapter.OnItemLongClickListener() {
            @Override
            public void onUserMessageItemLongClick(UserMessage message, int position) {
                if (message.getSender().getUserId().equals(PrefUtilsCall.getUserId(getContext()))) {

                    longclickpopup(message, position);

                  //  showMessageOptionsDialog(message, position);
                }
            }

            @Override
            public void onFileMessageItemLongClick(FileMessage message) {
            }

            @Override
            public void onAdminMessageItemLongClick(AdminMessage message) {
            }
        });
    }
    private void longclickpopup(final BaseMessage message, final int position) {
        LinearLayout croplinear,deletelenear;
        final Dialog dialog = new Dialog(getContext(), R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_popup);
        dialog.show();
        croplinear = dialog.findViewById(R.id.croplinear);
        deletelenear =  dialog.findViewById(R.id.deletelenear);
        croplinear.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                setState(STATE_EDIT, message, position);
                dialog.dismiss();
            }
        });
        deletelenear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMessage(message);
                Toast.makeText(getContext(), "Deleted Message", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }
//    private void showMessageOptionsDialog(final BaseMessage message, final int position) {
//        String[] options = new String[] { "Edit message", "Delete message" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                  setState(STATE_EDIT, message, position);
//                    String emojiKey = "smile";
//
//// The BASE_MESSAGE below indicates a BaseMessage object to add a reaction to.
//
//                } else if (which == 1) {
//                    deleteMessage(message);
//                }
//            }
//        });
//        builder.create().show();
//    }

    private void setState(int state, BaseMessage editingMessage, final int position) {
        switch (state) {
            case STATE_NORMAL:
                mCurrentState = STATE_NORMAL;
                mEditingMessage = null;

                mUploadFileButton.setVisibility(View.VISIBLE);
                mMessageSendButton.setText("SEND");
                mMessageEditText.setText("");
                break;

            case STATE_EDIT:
                mCurrentState = STATE_EDIT;
                mEditingMessage = editingMessage;

                mUploadFileButton.setVisibility(View.GONE);
                mMessageSendButton.setText("SAVE");
                String messageString = ((UserMessage)editingMessage).getMessage();
                if (messageString == null) {
                    messageString = "";
                }
                mMessageEditText.setText(messageString);
                if (messageString.length() > 0) {
                    mMessageEditText.setSelection(0, messageString.length());
                }

                mMessageEditText.requestFocus();
                mMessageEditText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIMM.showSoftInput(mMessageEditText, 0);

                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.scrollToPosition(position);
                            }
                        }, 500);
                    }
                }, 100);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((GroupChannelActivity)context).setOnBackPressedListener(new GroupChannelActivity.onBackPressedListener() {
            @Override
            public boolean onBack() {
                if (mCurrentState == STATE_EDIT) {
                    setState(STATE_NORMAL, null, -1);
                    return true;
                }

                mIMM.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
                return false;
            }
        });
    }

    private void retryFailedMessage(final BaseMessage message) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Retry?")
                .setPositiveButton(R.string.resend_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (message instanceof UserMessage) {
                                String userInput = ((UserMessage) message).getMessage();
                                sendUserMessage(userInput);
                            } else if (message instanceof FileMessage) {
                                Uri uri = mChatAdapter.getTempFileMessageUri(message);
                                sendFileWithThumbnail(uri);
                            }
                            mChatAdapter.removeFailedMessage(message);
                        }
                    }
                })
                .setNegativeButton(R.string.delete_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            mChatAdapter.removeFailedMessage(message);
                        }
                    }
                }).show();
    }

    /**
     * Display which users are typing.
     * If more than two users are currently typing, this will state that "multiple users" are typing.
     *
     * @param typingUsers The list of currently typing users.
     */
    private void displayTyping(List<Member> typingUsers) {

        if (typingUsers.size() > 0) {
            mCurrentEventLayout.setVisibility(View.VISIBLE);
            String string;

            if (typingUsers.size() == 1) {
                string = String.format(getString(R.string.user_typing), typingUsers.get(0).getNickname());
            } else if (typingUsers.size() == 2) {
                string = String.format(getString(R.string.two_users_typing), typingUsers.get(0).getNickname(), typingUsers.get(1).getNickname());
            } else {
                string = getString(R.string.users_typing);
            }
            mCurrentEventText.setText(string);
        } else {
            mCurrentEventLayout.setVisibility(View.GONE);
        }
    }

    private void requestMedia() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            Intent intent = new Intent();

            intent.setType("*/*");

            intent.setAction(Intent.ACTION_GET_CONTENT);

            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), INTENT_REQUEST_CHOOSE_MEDIA);

            // Set this as false to maintain connection
            // even when an external Activity is started.
            SendBird.setAutoBackgroundDetection(false);
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mRootLayout, "Storage access permissions are required to upload/download files.",
                    Snackbar.LENGTH_LONG)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void onFileMessageClicked(FileMessage message) {
        String type = message.getType().toLowerCase();
        if (type.startsWith("image")) {
            Intent i = new Intent(getActivity(), PhotoViewerActivity.class);
            i.putExtra("url", message.getUrl());
            i.putExtra("type", message.getType());
            startActivity(i);
        } else if (type.startsWith("video")) {
            Intent intent = new Intent(getActivity(), MediaPlayerActivity.class);
            intent.putExtra("url", message.getUrl());
            startActivity(intent);
        } else {
            showDownloadConfirmDialog(message);
        }
    }

    private void showDownloadConfirmDialog(final FileMessage message) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Download file?")
                    .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                FileUtils.downloadFile(getActivity(), message.getUrl(), message.getName());
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();
        }

    }

    private void updateActionBarTitle() {
        String title = "";

        if(mChannel != null) {
            title = TextUtils.getGroupChannelTitle(mChannel);
        }

        // Set action bar title to name of channel
        if (getActivity() != null) {
            ((GroupChannelActivity) getActivity()).setActionBarTitle(title);
        }
    }

    private void sendUserMessageWithUrl(final String text, String url) {
        if (mChannel == null) {
            return;
        }

        new WebUtils.UrlPreviewAsyncTask() {
            @Override
            protected void onPostExecute(UrlPreviewInfo info) {
                if (mChannel == null) {
                    return;
                }

                UserMessage tempUserMessage = null;
                BaseChannel.SendUserMessageHandler handler = new BaseChannel.SendUserMessageHandler() {
                    @Override
                    public void onSent(UserMessage userMessage, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            Log.e(LOG_TAG, e.toString());
                            if (getActivity() != null) {
                                Toast.makeText(
                                        getActivity(),
                                        "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                            mChatAdapter.markMessageFailed(userMessage);
                            return;
                        }

                        // Update a sent message to RecyclerView
                        mChatAdapter.markMessageSent(userMessage);
                    }
                };

                try {
                    // Sending a message with URL preview information and custom type.
                    String jsonString = info.toJsonString();
                    tempUserMessage = mChannel.sendUserMessage(text, jsonString, GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE, handler);
                } catch (Exception e) {
                    // Sending a message without URL preview information.
                    tempUserMessage = mChannel.sendUserMessage(text, handler);
                }


                // Display a user message to RecyclerView
                mChatAdapter.addFirst(tempUserMessage);
            }
        }.execute(url);
    }

    private void sendUserMessage(String text) {
        if (mChannel == null) {
            return;
        }
        Log.e("===========>",text);

        List<String> urls = WebUtils.extractUrls(text);
        Log.e("===========>",urls+"");
        Log.e("===========>",urls.size()+"");
        if (urls.size() > 0) {
            sendUserMessageWithUrl(text, urls.get(0));
            return;
        }

        UserMessage tempUserMessage = mChannel.sendUserMessage(text, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Log.e(LOG_TAG, e.toString());
                    if (getActivity() != null) {
                        Toast.makeText(
                                getActivity(),
                                "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                    mChatAdapter.markMessageFailed(userMessage);
                    return;
                }

                // Update a sent message to RecyclerView
                mChatAdapter.markMessageSent(userMessage);
            }
        });

        // Display a user message to RecyclerView
        mChatAdapter.addFirst(tempUserMessage);
    }

    /**
     * Notify other users whether the current user is typing.
     *
     * @param typing Whether the user is currently typing.
     */
    private void setTypingStatus(boolean typing) {
        if (mChannel == null) {
            return;
        }

        if (typing) {
            mIsTyping = true;
            mChannel.startTyping();
        } else {
            mIsTyping = false;
            mChannel.endTyping();
        }
    }


    private String SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            return  file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  file.getAbsolutePath();
    }
    private void sendFileWithThumbnail(Uri uri) {
        if (mChannel == null) {
            return;
        }

        // Specify two dimensions of thumbnails to generate
        List<FileMessage.ThumbnailSize> thumbnailSizes = new ArrayList<>();
        thumbnailSizes.add(new FileMessage.ThumbnailSize(240, 240));
        thumbnailSizes.add(new FileMessage.ThumbnailSize(320, 320));

        Hashtable<String, Object> info = FileUtils.getFileInfo(getActivity(), uri);
       Log.d("CAMERA","sendFileWithThumbnail:"+uri);
        if (info == null || info.isEmpty()) {
            Toast.makeText(getActivity(), "Extracting file information failed.", Toast.LENGTH_LONG).show();
            return;
        }

        final String name;
        if (info.containsKey("name")) {
            name = (String) info.get("name");
        } else {
            name = "Sendbird File";
        }
        final String path = (String) info.get("path");
        final File file = new File(path);
        final String mime = (String) info.get("mime");
        final int size = (Integer) info.get("size");

        if (path == null || path.equals("")) {
            Toast.makeText(getActivity(), "File must be located in local storage.", Toast.LENGTH_LONG).show();
        } else {
            BaseChannel.SendFileMessageHandler fileMessageHandler = new BaseChannel.SendFileMessageHandler() {
                @Override
                public void onSent(FileMessage fileMessage, SendBirdException e) {
                    if (e != null) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        mChatAdapter.markMessageFailed(fileMessage);
                        return;
                    }

                    mChatAdapter.markMessageSent(fileMessage);
                }
            };

            // Send image with thumbnails in the specified dimensions
            FileMessage tempFileMessage = mChannel.sendFileMessage(file, name, mime, size, "", null, thumbnailSizes, fileMessageHandler);

            mChatAdapter.addTempFileMessageInfo(tempFileMessage, uri);
            mChatAdapter.addFirst(tempFileMessage);
        }
    }

    private void editMessage(final BaseMessage message, String editedMessage) {
        if (mChannel == null) {
            return;
        }

        mChannel.updateUserMessage(message.getMessageId(), editedMessage, null, null, new BaseChannel.UpdateUserMessageHandler() {
            @Override
            public void onUpdated(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        mChatAdapter.markAllMessagesAsRead();
                    }
                });
            }
        });
    }

    /**
     * Deletes a message within the channel.
     * Note that users can only delete messages sent by oneself.
     *
     * @param message The message to delete.
     */
    private void deleteMessage(final BaseMessage message) {
        if (mChannel == null) {
            return;
        }

        mChannel.deleteMessage(message, new BaseChannel.DeleteMessageHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        mChatAdapter.markAllMessagesAsRead();
                    }
                });
            }
        });
    }
}
