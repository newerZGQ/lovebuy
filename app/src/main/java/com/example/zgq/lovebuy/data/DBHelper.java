package com.example.zgq.lovebuy.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 37902 on 2015/12/28.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static String name = "mydb.db";
    public static String consumTable = "consum";
    public static String desireTable = "desire";

    private static int version = 1;
    String CREATE_CONSUMPTION = "create table consum ("
            + "id integer primary key autoincrement, "
            + "number real, "
            + "lable text, "
            + "detail text, "
            + "date text, "
            + "happiness integer, "
            + "property integer)";
    String CREATE_DESIRE = "create table desire ("
            + "id integer primary key autoincrement, "
            + "number real, "
            + "detail text, "
            + "date text, "
            + "status integer)";
    public DBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONSUMPTION);
        db.execSQL(CREATE_DESIRE);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }
}
