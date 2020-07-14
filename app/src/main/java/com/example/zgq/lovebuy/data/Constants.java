package com.example.zgq.lovebuy.data;

import cn.bmob.v3.BmobUser;

/**
 * Created by 37902 on 2016/2/20.
 */
public class Constants {
    public static String BmobKey = "fbccebaefecd545a0cd6b08d784af61c";
    public static String userName;
    public static String associateUserName;

    public Constants() {
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Constants.userName = userName;
    }

    public static String getAssociateUserName() {
        return associateUserName;
    }

    public static void setAssociateUserName(String associateUserName) {
        Constants.associateUserName = associateUserName;
    }
}
