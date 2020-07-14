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
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;
import com.example.zgq.lovebuy.asynctask.SaveObjectToSP;
import com.example.zgq.lovebuy.asynctask.SyncDataTask;
import com.example.zgq.lovebuy.ui.myUI.InputNumberDialog;
import com.example.zgq.lovebuy.util.CheckNetWorkInfoUtil;
import com.example.zgq.lovebuy.util.DateTools;

public class AddDesireActivity extends AppCompatActivity {
    private double number = 0;
    private String desireDetail;
    private String date;
    private int status = 0;

    private TextView priceTv;
    private EditText desireEdtTxt;
    private Toolbar toolbar;
    private RelativeLayout numberRv;
    private TextView statusbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_desire);
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
        desireEdtTxt = (EditText) findViewById(R.id.edtTxt_desire);
        numberRv = (RelativeLayout) findViewById(R.id.rv_desire_number);
    }
    public void setViewContent(){
        toolbar.setTitle(getResources().getString(R.string.add_desire_toolbar_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        priceTv.setText("0");
        numberRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputNumberDialog numberDialog = new InputNumberDialog(AddDesireActivity.this);
                numberDialog.setListener(new InputNumberDialog.ActoinListener() {
                    @Override
                    public void onConfirm() {
                        String s;
                        double d;
                        if ((s = numberDialog.getNumber().toString()) != "" && ((d = Double.parseDouble(s)) > 0)) {
                            number = d;
                            priceTv.setText(s);
                        }
                    }
                });
                numberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                numberDialog.show();
            }
        });
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
                insertDesire();
                break;
        }
        finish();
        return true;
    }
    private boolean insertDesire(){

        date = DateTools.getDate(false);
        desireDetail = desireEdtTxt.getText().toString();
        if (desireDetail == null) {
            createDialog();
            return false;
        }
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("date", date);
        values.put("detail",desireDetail);
        values.put("status", status);
        Uri uri = Uri.parse("content://com.example.zgq.lovebuy.data.DesireContentProvider/desire");
        contentResolver.insert(uri, values);
        if (CheckNetWorkInfoUtil.isNetWorkAvailable(this)) {
            new SyncDataTask(this, new MyConsumDesire(new Double(number), desireDetail, date, new Integer(status))).doExecute();
        }else{new SaveObjectToSP(this,new MyConsumDesire(new Double(number), desireDetail, date, new Integer(status)),SaveObjectToSP.DESIRESAVETASK).execute((Void) null);}
        return true;
    }
    public void createDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.wrong_input)
                .show();
    }
}
