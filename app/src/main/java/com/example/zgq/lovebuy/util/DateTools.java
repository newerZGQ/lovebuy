package com.example.zgq.lovebuy.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 37902 on 2015/12/30.
 */
public class DateTools {
    public static final boolean SIMPLE_TIME = true;
    public static final boolean DETAIL_TIME = false;
    private static SimpleDateFormat format;
    public static String getDate(boolean flag) {

        if (flag == true) {
            format = new SimpleDateFormat("yyyyMM");

        }
        if (flag == false) {
            format = new SimpleDateFormat("yyyyMMddHH:mm:ss");

        }
        return format.format(new Date());
    }
    public static int getYear(){
        format = new SimpleDateFormat("yyyy");
        return Integer.parseInt(format.format(new Date()));
    }
    public static int getMonth(){
        format = new SimpleDateFormat("MM");
        return Integer.parseInt(format.format(new Date()));
    }
    public static int getDay(){
        format = new SimpleDateFormat("dd");
        return Integer.parseInt(format.format(new Date()));
    }
    public static int getMonthLastDay(int year, int month)
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    public static Date getLastMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }
    public static Date getNextMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,+1);
        return calendar.getTime();
    }
}
