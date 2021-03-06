package com.cis.qrchat.Chat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static final String PREFERENCE_KEY_USER_ID = "PREFERENCE_KEY_USER_ID";
    private static final String PREFERENCE_KEY_WALLET_ID = "PREFERENCE_KEY_WALLET_ID";

    private static final String PREFERENCE_KEY_FULL_NAME = "PREFERENCE_KEY_FULL_NAME";
    private static final String PREFERENCE_KEY_MOBILE_NUMBER = "PREFERENCE_KEY_MOBILE_NUMBER";

    private static final String PREFERENCE_KEY_WALLETBALANCE = "PREFERENCE_KEY_WALLETBALANCE";
    private static final String PREFERENCE_KEY_NICKNAME = "PREFERENCE_KEY_NICKNAME";
    private static final String PREFERENCE_KEY_PROFILE_URL = "PREFERENCE_KEY_PROFILE_URL";
    private static final String PREFERENCE_KEY_USE_DARK_THEME = "PREFERENCE_KEY_USE_DARK_THEME";
    private static final String PREFERENCE_KEY_DO_NOT_DISTURB = "PREFERENCE_KEY_DO_NOT_DISTURB";

    private static Context context;

    // Prevent instantiation
    private PreferenceUtils() {
    }

    public static void init(Context appContext) {
        context = appContext.getApplicationContext();
    }

    private static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("sendbird", Context.MODE_PRIVATE);
    }

    public static void setUserId(String userId) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_USER_ID, userId).apply();
    }

    public static String getUserId() {
        return getSharedPreferences().getString(PREFERENCE_KEY_USER_ID, "");
    }

    public static void setWalletId(String walletIdId) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_WALLET_ID, walletIdId).apply();
    }

    public static String getWalletId() {
        return getSharedPreferences().getString(PREFERENCE_KEY_WALLET_ID, "");
    }

    public static void setFullName(String fullName) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_FULL_NAME, fullName).apply();
    }

    public static String gefullName() {
        return getSharedPreferences().getString(PREFERENCE_KEY_FULL_NAME, "");
    }

    public static void setMobileNumber(String mobileNumber) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_MOBILE_NUMBER, mobileNumber).apply();
    }

    public static String getMobileNumber() {
        return getSharedPreferences().getString(PREFERENCE_KEY_MOBILE_NUMBER, "");
    }

    public static void setWalletBalance(String walletIdBalance) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_WALLETBALANCE, walletIdBalance).apply();
    }

    public static String getWalletBalance() {
        return getSharedPreferences().getString(PREFERENCE_KEY_WALLETBALANCE, "");
    }

    public static void setNickname(String nickname) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_NICKNAME, nickname).apply();
    }

    public static String getNickname() {
        return getSharedPreferences().getString(PREFERENCE_KEY_NICKNAME, "");
    }

    public static void setProfileUrl(String profileUrl) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREFERENCE_KEY_PROFILE_URL, profileUrl).apply();
    }

    public static String getProfileUrl() {
        return getSharedPreferences().getString(PREFERENCE_KEY_PROFILE_URL, "");
    }

    public static void setUseDarkTheme(boolean useDarkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(PREFERENCE_KEY_USE_DARK_THEME, useDarkTheme).apply();
    }

    public static boolean isUsingDarkTheme() {
        return getSharedPreferences().getBoolean(PREFERENCE_KEY_USE_DARK_THEME, false);
    }

    public static void setDoNotDisturb(boolean doNotDisturb) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(PREFERENCE_KEY_DO_NOT_DISTURB, doNotDisturb).apply();
    }

    public static boolean getDoNotDisturb() {
        return getSharedPreferences().getBoolean(PREFERENCE_KEY_DO_NOT_DISTURB, false);
    }

    public static void clearAll() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear().apply();
    }
}
