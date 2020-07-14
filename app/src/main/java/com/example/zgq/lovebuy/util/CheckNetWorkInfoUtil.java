package com.example.zgq.lovebuy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 37902 on 2016/2/24.
 */
public class CheckNetWorkInfoUtil {
    public CheckNetWorkInfoUtil() {
    }
    public static boolean isNetWorkAvailable(Context context){
            try
            {
                ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                    return true;
                else
                    return false;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }
}
