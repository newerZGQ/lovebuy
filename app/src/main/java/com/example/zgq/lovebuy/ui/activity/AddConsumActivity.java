package com.example.zgq.lovebuy.ui.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.adapter.LablesGridAdapter;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.asynctask.SaveObjectToSP;
import com.example.zgq.lovebuy.asynctask.SyncDataTask;
import com.example.zgq.lovebuy.ui.myUI.CostOrEaringButton;
import com.example.zgq.lovebuy.ui.myUI.InputDetailDialog;
import com.example.zgq.lovebuy.ui.myUI.InputNumberDialog;
import com.example.zgq.lovebuy.ui.myUI.MyRatingBar;
import com.example.zgq.lovebuy.util.CheckNetWorkInfoUtil;
import com.example.zgq.lovebuy.util.DateTools;
import com.example.zgq.lovebuy.util.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.ArrayList;

public class AddConsumActivity extends AppCompatActivity implements View.OnClickListener, LablesGridAdapter.changeLableListener {

    private TextView numberTv;
    private TextView numberTitleTv;
    private RelativeLayout numberRv;
    private RelativeLayout detailRv;
    private RelativeLayout dateRv;
    private Toolbar toolbar;
    private MyRatingBar ratingBar;
    private TextView detailTv;
    private TextView dateTv;
    private CostOrEaringButton ceButton;
    private GridView lablesGv;
    private TextView statusbar;

    private double number = 0;
    private String lable;
    private int happiness = -1;
    private String detail = null;
    private int property = -1;
    private String date;
    private LablesGridAdapter adapter;
    private ArrayList<String> costList;
    private ArrayList<String> earingList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consum);
        initView();
        try {
            initDate();
            setViewContent();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initDate() throws JSONException {
        date = DateTools.getDate(false);
        costList = new ArrayList<>();
        costList = SharedPreferencesUtil.getCostLablesList(this);
        costList.add(getString(R.string.action_add_consum_lable));
        property = Consum.ISCOST;
    }

    public void initView() {
        statusbar = (TextView) findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT<21){
            statusbar.setVisibility(View.GONE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dateRv = (RelativeLayout) findViewById(R.id.date_edit_layout);
        numberRv = (RelativeLayout) findViewById(R.id.rv_number);
        detailRv = (RelativeLayout) findViewById(R.id.rv_detail_edit);
        ceButton = (CostOrEaringButton) findViewById(R.id.cost_earing_buttons);
        numberTv = (TextView) findViewById(R.id.tv_price_edit);
        numberTitleTv = (TextView) findViewById(R.id.tv_number_title);
        detailTv = (TextView) findViewById(R.id.tv_detail_edit);
        lablesGv = (GridView) findViewById(R.id.gv_lables);
        ratingBar = (MyRatingBar) findViewById(R.id.ratingbar);
        dateTv = (TextView) findViewById(R.id.tv_date_edit);
    }




    public void setViewContent() throws JSONException {
        toolbar.setTitle(getResources().getString(R.string.add_consum_toolbar_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        numberTv.setText("0");
        numberTitleTv.setText(ceButton.getTitle());
        ceButton.setAttrs(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.bg_white),
                getResources().getColor(R.color.text_white),
                getResources().getColor(R.color.colorPrimary),
                getResources().getString(R.string.cost_title),
                getResources().getString(R.string.earing_title),
                new CostOrEaringButton.WatchListener() {
                    @Override
                    public void onClickLeftButton() throws JSONException {
                        numberTitleTv.setText(ceButton.getLeftText());
                        property = Consum.ISCOST;
                        changeLablesList(property);
                    }

                    @Override
                    public void onClickRightButton() throws JSONException {
                        numberTitleTv.setText(ceButton.getRightText());
                        property = Consum.ISEARNING;
                        changeLablesList(property);
                    }
                });
        adapter = new LablesGridAdapter(this, costList, this);
        lablesGv.setClickable(false);
        lablesGv.setAdapter(adapter);
        ratingBar.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(int RatingCount) {
                happiness = RatingCount;
            }
        });
        detailRv.setOnClickListener(this);
        numberRv.setOnClickListener(this);
        dateRv.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.confirm_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm:
                insertConsum();
                break;
        }
        finish();
        return true;
    }

    public boolean insertConsum() {
        date = DateTools.getDate(false);
        lable = adapter.getLableSelected();
        if (number == 0 || lable == null) {
            createDialog();
            return false;
        }
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("date", date);
        values.put("lable", lable);
        values.put("happiness", happiness);
        values.put("property", property);
        values.put("detail", detail);
        Uri uri = Uri.parse("content://com.example.zgq.lovebuy.data.ConsumContentProvider/consum");
        contentResolver.insert(uri, values);
        if (CheckNetWorkInfoUtil.isNetWorkAvailable(this)) {
            new SyncDataTask(this, new Consum(new Double(number), lable, date, new Integer(happiness), detail, new Integer(property))).doExecute();
        } else {
            new SaveObjectToSP(this, new Consum(new Double(number), lable, date, new Integer(happiness), detail, new Integer(property)), SaveObjectToSP.CONSUMSAVETASK).execute((Void) null);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_number:
                final InputNumberDialog numberDialog = new InputNumberDialog(this);
                ;
                numberDialog.setListener(new InputNumberDialog.ActoinListener() {
                    @Override
                    public void onConfirm() {
                        String s;
                        double d;
                        if ((s = numberDialog.getNumber().toString()) != "" && ((d = Double.parseDouble(s)) > 0)) {
                            number = d;
                            numberTv.setText(s);
                        }
                    }
                });
                numberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                numberDialog.show();
                break;
            case R.id.rv_detail_edit:
                final InputDetailDialog detailDialog = new InputDetailDialog(this);
                detailDialog.setOnGetDetailListener(new InputDetailDialog.OnGetDetailListener() {
                    @Override
                    public void onGetDetail() {
                        if ((detail = detailDialog.getDetail()) != null) {
                            detailTv.setText(detailDialog.getDetail());
                        }
                    }
                });
                detailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                detailDialog.show();
                break;
            case R.id.date_edit_layout:
                final int mYear = DateTools.getYear();
                final int mMonth = DateTools.getMonth() - 1;
                final int mDay = DateTools.getDay();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 10)
                            if (year == mYear && monthOfYear == mMonth && dayOfMonth == mDay) {
                            } else {
                                Log.d("" + year + "" + monthOfYear + "" + dayOfMonth, ">>>>>>>>>>>>");
                                if (monthOfYear < 10 && dayOfMonth < 10)
                                    date = "" + year + "0" + monthOfYear + "0" + dayOfMonth + date.substring(8, 12);
                                if (monthOfYear < 10 && dayOfMonth >= 10)
                                    date = "" + year + "0" + monthOfYear + "" + dayOfMonth + date.substring(8, 12);
                                if (monthOfYear >= 10 && dayOfMonth >= 10)
                                    date = "" + year + "" + monthOfYear + "" + dayOfMonth + date.substring(8, 12);
                                dateTv.setText(monthOfYear + 1 + "月" + dayOfMonth + "日");
                            }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }

    public void createDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.wrong_input)
                .show();
    }

    public void changeLablesList(int property) throws JSONException {
        if (property == Consum.ISCOST) {
            ArrayList<String> arrayList = SharedPreferencesUtil.getCostLablesList(this);
            costList.clear();
            for (String s : arrayList) {
                costList.add(s);
            }
        } else {
            ArrayList<String> arrayList = SharedPreferencesUtil.getEaringLablesList(this);
            costList.clear();
            for (String s : arrayList) {
                costList.add(s);
            }
        }
        costList.add(getString(R.string.action_add_consum_lable));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddLable() {
        final InputDetailDialog detailDialog = new InputDetailDialog(this,0,getString(R.string.less_three_chart));
        detailDialog.setOnGetDetailListener(new InputDetailDialog.OnGetDetailListener() {
            @Override
            public void onGetDetail() {
                String newLable = detailDialog.getDetail();
                if (newLable.length() > 3 || newLable.length() == 0) return;
                if (property == Consum.ISCOST) {
                    SharedPreferencesUtil.putCostLablesList(AddConsumActivity.this, newLable);
                    ArrayList<String> arrayList = SharedPreferencesUtil.getCostLablesList(AddConsumActivity.this);
                    costList.clear();
                    for (String s : arrayList) {
                        costList.add(s);
                    }
                    costList.add(getString(R.string.action_add_consum_lable));
                }
                if (property == Consum.ISEARNING) {
                    SharedPreferencesUtil.putEarningLablesList(AddConsumActivity.this, newLable);
                    ArrayList<String> arrayList = SharedPreferencesUtil.getEaringLablesList(AddConsumActivity.this);
                    costList.clear();
                    for (String s : arrayList) {
                        costList.add(s);
                    }
                    costList.add(getString(R.string.action_add_consum_lable));
                }
                adapter = new LablesGridAdapter(AddConsumActivity.this,costList,AddConsumActivity.this);
                lablesGv.setAdapter(adapter);
            }
        });
        detailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        detailDialog.show();
    }

    @Override
    public void onDeleteLable(final String lable) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.yes_to_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        if (property == Consum.ISCOST){
                            SharedPreferencesUtil.deleteCostLablesList(AddConsumActivity.this,lable);
                            ArrayList<String> arrayList = SharedPreferencesUtil.getCostLablesList(AddConsumActivity.this);
                            costList.clear();
                            for (String s : arrayList) {
                                costList.add(s);
                            }
                            costList.add(getString(R.string.action_add_consum_lable));
                        }
                        if (property == Consum.ISEARNING){
                            SharedPreferencesUtil.deleteEaringLablesList(AddConsumActivity.this,lable);
                            ArrayList<String> arrayList = SharedPreferencesUtil.getCostLablesList(AddConsumActivity.this);
                            costList.clear();
                            for (String s : arrayList) {
                                costList.add(s);
                            }
                            costList.add(getString(R.string.action_add_consum_lable));
                        }
                        adapter = new LablesGridAdapter(AddConsumActivity.this,costList,AddConsumActivity.this);
                        lablesGv.setAdapter(adapter);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
    }
}


//    public void takePhoto() {
//        //        打开输出文件流并创建jpg文件
//        pictureDate = DateTools.getDate(DateTools.DETAIL_TIME);
//        consumImageId = pictureDate + ".jpg";
//        file = new File(PathTools.getPath(DateTools.getDate(true)) + "/" + consumImageId);
//        try {
//            if (file.exists()) {
//                file.delete();
//            }
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        imageUri = Uri.fromFile(file);
//        //         打开相机,拍照结果给imageUri
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, 1);
//    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//// 检测sd是否可用
//            String sdStatus = Environment.getExternalStorageState();
//            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
//                return;
//            }
//
//            FileInputStream fis = null;
//            try {
//                fis = new FileInputStream(file);
//                bitmap = BitmapFactory.decodeStream(fis);
//                fis.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            bitmap = BitmapUtil.centerSquareScaleBitmap(bitmap, ScreenDimen.getScreenWidth(this));
//
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
