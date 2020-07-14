package com.example.zgq.lovebuy.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 37902 on 2016/1/26.
 */
public class SharedPreferencesUtil {



    public SharedPreferencesUtil() {
    }
    public static void initLablesSharedPf(Context context){
        SharedPreferences preferences = context.getSharedPreferences("lovebuy",Context.MODE_PRIVATE);
        if (!preferences.contains("costLables")) {
            SharedPreferences.Editor editor = preferences.edit();
            String costLables = "吃饭 衣服 住房 交通 娱乐 医药 首饰 美容 运动 人际 数码";
            editor.putString("costLables",costLables);
            editor.commit();
        }
        if(!preferences.contains("earingLables")){
            SharedPreferences.Editor editor = preferences.edit();
            String earingLables = "工资 收礼 债务 股票 买卖 捡钱 啃老";
            editor.putString("earingLables",earingLables);
            editor.commit();
            editor.commit();
        }
    }
    public static ArrayList<String> getCostLablesList(Context context) {
        ArrayList<String> list = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("lovebuy", Context.MODE_PRIVATE);
        String costLables = preferences.getString("costLables", "null");
        String[] costLablesArray = costLables.split("\\s{1,}");
        for (int i = 0;i<costLablesArray.length;i++){
            list.add(costLablesArray[i]);
        }
        return list;
    }
    public static void putCostLablesList(Context context,String lable) {
        SharedPreferences preferences = context.getSharedPreferences("lovebuy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String costLables = preferences.getString("costLables", "null");
        String[] costLablesArray = costLables.split("\\s{1,}");
        for (int i = 0;i<costLablesArray.length;i++){
            if (lable.equals(costLablesArray[i])) return;
        }
        costLables = costLables +" "+lable;
        editor.putString("costLables", costLables).apply();
    }
    public static void deleteCostLablesList(Context context,String lable) {
        SharedPreferences preferences = context.getSharedPreferences("lovebuy",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String costLables = preferences.getString("costLables", "null");
        String[] costLablesArray = costLables.split("\\s{1,}");
        String newCostLables = "";
        for (int i = 0;i<costLablesArray.length;i++){
            if (lable.equals(costLablesArray[i])) continue;
            if (i == costLablesArray.length-1){
                newCostLables += costLablesArray[i];
            }
            newCostLables +=costLablesArray[i]+ " ";
        }
        editor.putString("costLables", newCostLables).commit();
    }
    public static ArrayList<String> getEaringLablesList(Context context){
        ArrayList<String> list = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("lovebuy", Context.MODE_PRIVATE);
        String earingLables = preferences.getString("earingLables","null");
        String[] earingLablesArray = earingLables.split("\\s{1,}");
        for (int i = 0;i<earingLablesArray.length;i++){
            list.add(earingLablesArray[i]);
        }
        return list;
    }
    public static void putEarningLablesList(Context context,String lable) {
        SharedPreferences preferences = context.getSharedPreferences("lovebuy",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String earingLables = preferences.getString("earingLables", "null");
        String[] earingLablesArray = earingLables.split("\\s{1,}");
        for (int i = 0;i<earingLablesArray.length;i++){
            if (lable.equals(earingLablesArray[i])) return;
        }
        earingLables = earingLables +" "+lable;
        editor.putString("earingLables", earingLables).apply();
    }
    public static void deleteEaringLablesList(Context context,String lable){
        SharedPreferences preferences = context.getSharedPreferences("lovebuy",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String earingLables = preferences.getString("earingLables", "null");
        String[] earingLablesArray = earingLables.split("\\s{1,}");
        String newEaringLables = "";
        for (int i = 0;i<earingLablesArray.length;i++){
            if (lable.equals(earingLablesArray[i])) continue;
            if (i == earingLablesArray.length-1){
                newEaringLables += earingLablesArray[i];
            }
            newEaringLables += earingLablesArray[i] + " ";
        }
        editor.putString("earingLables", newEaringLables).apply();
    }
}
