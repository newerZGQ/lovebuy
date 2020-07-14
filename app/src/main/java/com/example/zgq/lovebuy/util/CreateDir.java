package com.example.zgq.lovebuy.util;

/**
 * Created by 37902 on 2016/1/6.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by 37902 on 2015/10/24.
 */
public class CreateDir {
    private static File happyBuy;
    private static File photo;
    private static File month;
    public static void createDir(){
        happyBuy = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/happybuy");
        if (!happyBuy.exists()){
            happyBuy.mkdir();
            Log.d("CreateDir","success");
        }

        photo = new File(happyBuy,"/photo");
        if (!photo.exists()){
            photo.mkdir();
        }

        month = new File(photo,"/" + DateTools.getDate(DateTools.SIMPLE_TIME));
        if (!month.exists()) {
            month.mkdir();
        }
    }
    public static void testSDCard(Context context) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            new AlertDialog.Builder(context).setTitle("未检测到SD卡").show();
        } else {
            createDir();
        }
    }
}
