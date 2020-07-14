package com.example.zgq.lovebuy.ui.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;
import com.example.zgq.lovebuy.asynctask.SaveObjectToSP;
import com.example.zgq.lovebuy.asynctask.SyncDataTask;
import com.example.zgq.lovebuy.util.CheckNetWorkInfoUtil;

public class DetailDesireActivity extends AppCompatActivity {
    private double number = 0;
    private String date;
    private int status = 0;
    private MyConsumDesire desire;
    private TextView priceTv;
    private TextView desireDetailTv;
    private TextView numberTitleTv;
    private TextView desireDateTv;
    private TextView desireStatusTv;
    private Toolbar toolbar;
    private TextView statusbar;
    private RelativeLayout numberLayoutRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_desire);
        desire = (MyConsumDesire) getIntent().getSerializableExtra("desire");
        initView();
        setViewContent();
    }
    private void initView(){
        statusbar = (TextView) findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT<21){
            statusbar.setVisibility(View.GONE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        priceTv = (TextView) findViewById(R.id.tv_desire_price_edit);
        desireDetailTv = (TextView) findViewById(R.id.edtTxt_desire);
        numberTitleTv = (TextView) findViewById(R.id.tv_desire_number_title);
        desireDateTv = (TextView) findViewById(R.id.tv_desire_date);
        desireStatusTv = (TextView) findViewById(R.id.tv_desire_status);
    }
    public void setViewContent(){
        toolbar.setTitle(getResources().getString(R.string.desire_detail_toolbar_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        priceTv.setText("" + desire.getNumber());
        desireDetailTv.setText(desire.getDetail());
        if (desire.getStatus() == 0){
            numberTitleTv.setText(getResources().getString(R.string.desire_detail_had_not_cost));
            desireStatusTv.setBackground(getResources().getDrawable(R.drawable.ic_heart_gray));
        }else{
            numberTitleTv.setText(getResources().getString(R.string.desire_detail_had_cost));
            desireStatusTv.setBackground(getResources().getDrawable(R.drawable.ic_heart_red));
        }
        desireDateTv.setText(desire.getDate().substring(0, 4) + "." + desire.getDate().substring(4, 6) + "." + desire.getDate().substring(6, 8));
        desireStatusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus();
            }
        });
    }
    private void changeStatus(){
        if (desire.getStatus() == 0){
            desire.setStatus(new Integer(1));
            numberTitleTv.setText(getResources().getString(R.string.desire_detail_had_cost));
            desireStatusTv.setBackground(getResources().getDrawable(R.drawable.ic_heart_red));
        }else{
            desire.setStatus(new Integer(0));
            numberTitleTv.setText(getResources().getString(R.string.desire_detail_had_not_cost));
            desireStatusTv.setBackground(getResources().getDrawable(R.drawable.ic_heart_gray));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.confirm_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.confirm:
                ContentResolver contentResolver = getContentResolver();
                Uri uri = Uri.parse("content://com.example.zgq.lovebuy.data.DesireContentProvider/desire");
                ContentValues values = new ContentValues();
                values.put("number", desire.getNumber());
                values.put("date", desire.getDate());
                values.put("detail",desire.getDetail());
                values.put("status", desire.getStatus());
                contentResolver.update(uri,values,"date=?",new String[]{desire.getDate()});
                if (CheckNetWorkInfoUtil.isNetWorkAvailable(this)){
                    new SyncDataTask(this,desire,SyncDataTask.UPDATEDESIRE).doExecute();
                }else{
                    new SaveObjectToSP(this,desire,SaveObjectToSP.DESIREUPDATETASK).execute((Void) null);
                }
                break;
        }
        finish();
        return true;
    }

}
