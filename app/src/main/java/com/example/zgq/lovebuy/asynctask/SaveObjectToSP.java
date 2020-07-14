package com.example.zgq.lovebuy.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;
import com.google.gson.Gson;

/**
 * Created by 37902 on 2016/2/24.
 */
public class SaveObjectToSP extends AsyncTask<Void,Void,Boolean> {
    private Context context;
    private Consum consum;
    private MyConsumDesire desire;
    private SharedPreferences sharedPreference;
    private int taskId;
    public static int CONSUMSAVETASK = 1;
    public static int DESIRESAVETASK = 2;
    public static int CONSUMDELETASK = 3;
    public static int DESIREDELETASK = 4;
    public static int DESIREUPDATETASK = 5;

    public SaveObjectToSP(Context context,Consum consum,int operation) {
        this.context = context;
        this.consum = consum;
        this.taskId = operation;
    }
    public SaveObjectToSP(Context context,MyConsumDesire desire,int operation){
        this.context = context;
        this.desire = desire;
        this.taskId = operation;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        if (taskId == CONSUMSAVETASK) {
            SharedPreferences sharedPreference = context.getSharedPreferences("unSavedConsumCache",0);
            SharedPreferences.Editor editor = sharedPreference.edit();
            Gson gson = new Gson();
            String s = gson.toJson(consum);
            editor.putString(consum.getDate() + "unSave", s).commit();
        }
        if (taskId == DESIRESAVETASK) {
            SharedPreferences sharedPreference = context.getSharedPreferences("unSavedDesireCache",0);
            SharedPreferences.Editor editor = sharedPreference.edit();
            Gson gson = new Gson();
            String s = gson.toJson(desire);
            editor.putString(desire.getDate()+ "unSave", s).commit();
        }
        if (taskId == CONSUMDELETASK) {
            SharedPreferences sharedPreference = context.getSharedPreferences("unDeleConsumCache",0);
            SharedPreferences.Editor editor = sharedPreference.edit();
            Gson gson = new Gson();
            String s = gson.toJson(consum);
            editor.putString(consum.getDate() + "unDele", s).commit();
        }
        if (taskId == DESIREDELETASK) {
            SharedPreferences sharedPreference = context.getSharedPreferences("unDeleDesireCache",0);
            SharedPreferences.Editor editor = sharedPreference.edit();
            Gson gson = new Gson();
            String s = gson.toJson(desire);
            editor.putString(desire.getDate()+ "unDele", s).commit();
        }
        if (taskId == DESIREUPDATETASK){
            SharedPreferences sharedPreference = context.getSharedPreferences("unUpdateDesireCache",0);
            SharedPreferences.Editor editor = sharedPreference.edit();
            Gson gson = new Gson();
            String s = gson.toJson(desire);
            editor.putString(desire.getDate()+ "unUpdate", s).commit();
        }
        return null;
    }
}
