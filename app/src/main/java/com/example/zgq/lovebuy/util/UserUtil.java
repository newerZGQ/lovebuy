package com.example.zgq.lovebuy.util;

import android.content.Context;

import cn.bmob.v3.BmobUser;

/**
 * Created by 37902 on 2016/2/22.
 */
public class UserUtil {
    private Context context;
    private String userName;
    public UserUtil(Context context) {
        this.context = context;
    }
    public String getUserName(){
        return BmobUser.getCurrentUser(context).getUsername();
    }
}
