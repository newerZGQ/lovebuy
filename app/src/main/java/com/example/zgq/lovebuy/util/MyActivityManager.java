package com.example.zgq.lovebuy.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by 37902 on 2016/3/16.
 */
public class MyActivityManager {
    public static ArrayList<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAllActivities(){
        for (int i = 0;i<activities.size();i++){
            activities.get(i).finish();
        }
    }
}
