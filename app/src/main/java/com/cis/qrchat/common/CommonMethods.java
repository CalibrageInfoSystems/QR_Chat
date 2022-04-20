package com.cis.qrchat.common;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class CommonMethods {

    public static final int PERMISSION_CODE = 100;


    public static boolean areAllPermissionsAllowed(final Context context, final String[] permissions) {
        boolean isAllPermissionsGranted = false;
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                isAllPermissionsGranted = true;
            }
        }
        return isAllPermissionsGranted;
    }

    public static void requestPermission(final FragmentActivity context, final String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(context, new String[]{permission}, PERMISSION_CODE);
    }
}
