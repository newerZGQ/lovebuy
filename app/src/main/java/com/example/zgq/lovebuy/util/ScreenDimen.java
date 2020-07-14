package com.example.zgq.lovebuy.util;

/**
 * Created by 37902 on 2016/1/6.
 */
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by 37902 on 2015/12/21.
 */
public class ScreenDimen {
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
}
