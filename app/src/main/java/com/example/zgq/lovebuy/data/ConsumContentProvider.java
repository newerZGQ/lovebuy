package com.example.zgq.lovebuy.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ConsumContentProvider extends ContentProvider {

    public final static String AUTHORITIES = "com.example.zgq.lovebuy.data.ConsumContentProvider";
    public final static String URI = "content://" + AUTHORITIES;
    private final static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CONSUM = 1;
    private static final int CONSUMS = 2;
    private DBHelper dbHelper;
    static {
        URI_MATCHER.addURI("com.example.zgq.lovebuy.data.ConsumContentProvider","consum",CONSUMS);
        URI_MATCHER.addURI("com.example.zgq.lovebuy.data.ConsumContentProvider","consum/#",CONSUM);
    }


    public ConsumContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count = 0;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        switch (flag){
            case CONSUM:
                long consumid = ContentUris.parseId(uri);
                String where_value = " consumid = "+consumid;
                if (selection!=null && !selection.equals("")){
                    where_value += selection;
                }
                count = database.delete("consum",where_value,selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case CONSUMS:
                count = database.delete("consum",selection,selectionArgs);
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
            case CONSUM:
                return "vnd.android.cursor.item/consum";
            case CONSUMS:
                return "vnd.android.cursor.dir/consums";
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
            case CONSUM:

            case CONSUMS:
                long id = database.insert(DBHelper.consumTable,null,values);

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
            case CONSUM:
                long consumid = ContentUris.parseId(uri);
                String where_value = " consumid = " + consumid;
                if (selection != null && !selection.equals("")){
                    where_value += selection;
                }
                cursor = database.query("consum",projection,where_value,selectionArgs,null,null,null);
                break;
            case CONSUMS:
                cursor = database.query("consum",projection,selection,selectionArgs,sortOrder,null,null,null);
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
            case CONSUM:
                long consumid = ContentUris.parseId(uri);
                String where_value = " consumid = " + consumid;
                if (selection != null && !selection.equals("")) {
                    where_value += selection;
                }
                count = database.update("consum", values, where_value, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case CONSUMS:
                count = database.update("consum", values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }
        return count;
    }
}
