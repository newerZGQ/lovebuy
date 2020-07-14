package com.example.zgq.lovebuy.util;

/**
 * Created by 37902 on 2016/3/5.
 */
public class FloatUtil {
    public static String floatToString(String number){
        int N = number.length();
        if (N>=2){
            if (number.charAt(N-2) == '.'&& number.charAt(N-1) == '0'){
                return number.substring(0,N-2);
            }
        }
        return number;
    }
}
