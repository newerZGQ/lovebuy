package com.example.zgq.lovebuy.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.data.ConsumContentProvider;
import com.example.zgq.lovebuy.data.DesireContentProvider;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.consum.DayConsum;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37902 on 2016/1/12.
 */
public class DBOperateTools {
//    参数YMdate是201506这种类型的字符串
    public static List<Consum> getMonthConsum(Context context,String YMdate,Uri uri){
        List<Consum> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, "date like ?", new String[]{ YMdate + "%"}, "date");
        if (cursor != null && cursor.moveToFirst()){
            Consum consum = newConsumFromCursor(cursor);
            list.add(consum);
            while(cursor.moveToNext()) {
                consum = newConsumFromCursor(cursor);
                list.add(consum);
            }
            cursor.close();
            return list;
        }else{
            cursor.close();
            return list;
        }
    }

    public static Consum getLastConsum(Context context,Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, "date");
        if (cursor.getCount() >0 ) {
            cursor.moveToLast();
            Consum consum = newConsumFromCursor(cursor);
            return consum;
        }
        cursor.close();
        return new Consum();
    }
    public static boolean isEmptyConsumTableInDB(Context context,Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor.getCount() == 0){
            return true;
        }else{
            return false;
        }
    }
    public static Consum newCostFromCursor(Cursor cursor){
        double lastConsumNum = cursor.getDouble(cursor.getColumnIndex("number"));
        String lastConsumLab = cursor.getString(cursor.getColumnIndex("lable"));
        String lastConsumDat = cursor.getString(cursor.getColumnIndex("date"));
        int lastConsumHap = cursor.getInt(cursor.getColumnIndex("happiness"));
        String lastConsumDet = cursor.getString(cursor.getColumnIndex("detail"));
        if (lastConsumDat != "无"){
            return new Consum(lastConsumNum, lastConsumLab, lastConsumDat, lastConsumHap, lastConsumDet, 1);
        }else{
            return new Consum();
        }
    }
//
    public static Consum newConsumFromCursor(Cursor cursor){
        double  lastConsumNum = cursor.getDouble(cursor.getColumnIndex("number"));
        String    lastConsumLab = cursor.getString(cursor.getColumnIndex("lable"));
        String    lastConsumDat = cursor.getString(cursor.getColumnIndex("date"));
        int    lastConsumHap = cursor.getInt(cursor.getColumnIndex("happiness"));
        String    lastConsumDet = cursor.getString(cursor.getColumnIndex("detail"));
        int    lastConsumPro = cursor.getInt(cursor.getColumnIndex("property"));
        return new Consum(lastConsumNum, lastConsumLab, lastConsumDat, lastConsumHap, lastConsumDet, lastConsumPro);
    }
//    public static EpListViewData getEpListViewData(List<Consum> arrayList){
//
//        List<Consum> partSingles = new ArrayList<>();
//        List<DayConsum> dayConsumptions = new ArrayList<>();
//        List<List<Consum>> childLists = new ArrayList<>();
//        if (arrayList.size() == 0){
//            return new EpListViewData(childLists,dayConsumptions);
//        }
//        String dayDate = arrayList.get(0).getDate().substring(0,8);
//        for (int i= 0;i<=arrayList.size();i++){
//            if ( i == arrayList.size()){
//                double totalPrice = 0;
//                List<Consum> tmpList = new ArrayList<>();
//                for (Consum partConsum: partSingles){
//                    totalPrice += partConsum.getNumber();
//                    tmpList.add(partConsum);
//                }
////                DayConsum dayConsum = new DayConsum(totalPrice,dayDate);
////                dayConsumptions.add(dayConsum);
//                childLists.add(tmpList);
//                break;
//            }
//            Consum consum = arrayList.get(i);
//            if (consum.getDate().substring(0,8).equals(dayDate)){
//                partSingles.add(consum);
//            }else{
//                double totalPrice = 0;
//                List<Consum> tmpList = new ArrayList<>();
//                for (Consum partConsum: partSingles){
//                    totalPrice += partConsum.getNumber();
//                    tmpList.add(partConsum);
//                }
//                DayConsum dayConsum = new DayConsum(totalPrice,dayDate);
//                dayConsumptions.add(dayConsum);
//                childLists.add(tmpList);
//                dayDate = consum.getDate().substring(0,8);
//                partSingles.clear();
//                partSingles.add(consum);
//            }
//        }
//        EpListViewData epListViewData = new EpListViewData(childLists,dayConsumptions);
//        return  epListViewData;
//    }

    public static class EpListViewData{
        private List<List<Consum>> childLists;
        private List<DayConsum> parentList;

        public EpListViewData(List<List<Consum>> childLists, List<DayConsum> parentList) {
            this.childLists = childLists;
            this.parentList = parentList;
        }

        public void setChildLists(List<List<Consum>> childLists) {
            this.childLists = childLists;
        }

        public void setParentList(List<DayConsum> parentList) {
            this.parentList = parentList;
        }

        public List<List<Consum>> getChildLists() {
            return childLists;
        }

        public List<DayConsum> getParentList() {
            return parentList;
        }
    }


    public static List<MyConsumDesire> getMyDesireList(Context context,Uri uri){
        List<MyConsumDesire> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            MyConsumDesire desire = newDesireFromCursor(cursor);
            list.add(desire);
            while(cursor.moveToNext()) {
                desire = newDesireFromCursor(cursor);
                list.add(desire);
            }
        }
        cursor.close();
        return list;
    }
    public static MyConsumDesire newDesireFromCursor(Cursor cursor){
        double number = cursor.getDouble(cursor.getColumnIndex("number"));
        String detail = cursor.getString(cursor.getColumnIndex("detail"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        int status = cursor.getInt(cursor.getColumnIndex("status"));
        return new MyConsumDesire(number,detail,date,status);
    }
    public static boolean isEmptyMyDesireTableInDB(Context context,Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor.getCount() == 0){
            return true;
        }else{
            return false;
        }
    }


    public static int clearConsum(Context context,Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        int i = contentResolver.delete(uri, null, null);
        return i;
    }
    public static int clearDesire(Context context,Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        int i = contentResolver.delete(uri, null, null);
        return i;
    }


    //DB Tools
    public static boolean isEmptyDB(Context context){
        if (isEmptyConsumTableInDB(context, Uri.parse(ConsumContentProvider.URI + "/consum")) && isEmptyMyDesireTableInDB(context, Uri.parse(DesireContentProvider.URI + "/desire"))){
            return true;
        }
        else{
            return false;
        }
    }
}
