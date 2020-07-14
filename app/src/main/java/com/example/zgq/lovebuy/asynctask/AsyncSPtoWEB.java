package com.example.zgq.lovebuy.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by 37902 on 2016/2/25.
 */
public class AsyncSPtoWEB extends AsyncTask<Void,Void,Boolean> {
    private Context context;

    public AsyncSPtoWEB(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        SharedPreferences unSaveConsum = context.getSharedPreferences(context.getString(R.string.unSavedConsumCache),0);
        SharedPreferences unSaveDesire = context.getSharedPreferences(context.getString(R.string.unSavedDesireCache),0);
        SharedPreferences unDeleConsum = context.getSharedPreferences(context.getString(R.string.unDeleConsumCache),0);
        SharedPreferences unDeleDesire = context.getSharedPreferences(context.getString(R.string.unDeleDesireCache),0);
        SharedPreferences.Editor unSaveConsumEditor = unSaveConsum.edit();
        SharedPreferences.Editor unSaveDesireEditor = unSaveDesire.edit();
        Map<String, ?> unSaveConsumMap = unSaveConsum.getAll();
        for (Map.Entry<String, ?> entry : unSaveConsumMap.entrySet()) {
            Gson gson = new Gson();
            Consum consum = gson.fromJson(entry.getValue().toString(), Consum.class);
            Consum newConsum = convertConsum(consum);
            newConsum.save(context);
            unSaveConsumEditor.remove(entry.getKey()).commit();
        }
        Map<String, ?> unSaveDesireMap = unSaveDesire.getAll();
        for (Map.Entry<String, ?> entry : unSaveDesireMap.entrySet()) {
            Gson gson = new Gson();
            MyConsumDesire desire = gson.fromJson(entry.getValue().toString(), MyConsumDesire.class);
            MyConsumDesire newDesire = convertDesire(desire);
            newDesire.save(context);
            unSaveDesireEditor.remove(entry.getKey()).commit();
        }

        return true;
    }
    private Consum convertConsum(Consum consum){
        return new Consum(new Double(consum.getNumber()),consum.getLable(),consum.getDate(),new Integer(consum.getHappiness()),consum.getDetail(),new Integer(consum.getProperty()));
    }
    private MyConsumDesire convertDesire(MyConsumDesire desire){
        return new MyConsumDesire(new Double(desire.getNumber()),desire.getDetail(),desire.getDate(),new Integer(desire.getStatus()));
    }
}
