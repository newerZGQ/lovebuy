package com.example.zgq.lovebuy.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DesireContentProvider extends ContentProvider {
    public DesireContentProvider() {
    }

    public final static String AUTHORITIES = "com.example.zgq.lovebuy.data.DesireContentProvider";
    public final static String URI = "content://" + AUTHORITIES;
    private final static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int DESIRE = 1;
    private static final int DESIRES = 2;
    private DBHelper dbHelper;
    static {
        URI_MATCHER.addURI("com.example.zgq.lovebuy.data.DesireContentProvider","desire",DESIRES);
        URI_MATCHER.addURI("com.example.zgq.lovebuy.data.DesireContentProvider","desire/#",DESIRE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count = 0;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (flag){
            case DESIRE:
                long desireid = ContentUris.parseId(uri);
                String where_value = " consumid = "+desireid;
                if (selection!=null && !selection.equals("")){
                    where_value += selection;
                }
                count = database.delete("desire",where_value,selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case DESIRES:
                count = database.delete("desire",selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        int flag = URI_MATCHER.match(uri);
        switch (flag){
            case DESIRE:
                return "vnd.android.cursor.item/desire";
            case DESIRES:
                return "vnd.android.cursor.dir/desire";
        }
        throw new UnsupportedOperationException("not macthed");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Uri uri2 = null;
        switch (flag){
            case DESIRE:

            case DESIRES:
                long id = database.insert(DBHelper.desireTable,null,values);
                uri2 = ContentUris.withAppendedId(uri,id);
                getContext().getContentResolver().notifyChange(uri2, null);
                break;
        }
        return uri2;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor = null;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (flag){
            case DESIRE:
                long desireid = ContentUris.parseId(uri);
                String where_value = " desireid = " + desireid;
                if (selection != null && !selection.equals("")){
                    where_value += selection;
                }
                cursor = database.query("desire",projection,where_value,selectionArgs,null,null,null);
                break;
            case DESIRES:
                cursor = database.query("desire",projection,selection,selectionArgs,sortOrder,null,null,null);
                return cursor;
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int count = 0;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        switch (flag) {
            case DESIRE:
                long desireid = ContentUris.parseId(uri);
                String where_value = " desireid = " + desireid;
                if (selection != null && !selection.equals("")) {
                    where_value += selection;
                }
                count = database.update("desire", values, where_value, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case DESIRES:
                count = database.update("desire", values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }
        return count;
    }
}
