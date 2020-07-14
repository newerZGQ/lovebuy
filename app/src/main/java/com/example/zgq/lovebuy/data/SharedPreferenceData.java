package com.example.zgq.lovebuy.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 37902 on 2016/3/15.
 */
public class SharedPreferenceData {
    public SharedPreferenceData(){
    }
    public static void initUserSharedPreferenceData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userName", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myUserName", null);
        editor.putString("associateUserName","未与对方关联");
        editor.commit();
    }
    public static void changeSharedPreferenceDataMyUserName(Context context,String myUserName){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userName", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myUserName", myUserName);
        editor.commit();
    }
    public static void changeSharedPreferenceDataAssociateUserName(Context context,String associateUserName){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userName", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("associateUserName", associateUserName);
        editor.commit();
    }
    public static void deleteSharedPreferenceDataMyUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userName", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myUserName", null);
        editor.commit();
    }
    public static void deleteSharedPreferenceDataAssociateUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userName", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("associateUserName", "未与对方关联");
        editor.commit();
    }
    public static String getSharedPreferenceDataMyUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userName", 0);
        return sharedPreferences.getString("myUserName",null);
    }
    public static String getSharedPreferenceDataAssociateUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userName", 0);
        return sharedPreferences.getString("associateUserName",null);
    }
}
