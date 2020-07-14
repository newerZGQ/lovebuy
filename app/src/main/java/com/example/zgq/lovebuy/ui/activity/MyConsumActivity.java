package com.example.zgq.lovebuy.ui.activity;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.adapter.MonthExpAdapter;
import com.example.zgq.lovebuy.data.ConsumContentProvider;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.consum.DayConsum;
import com.example.zgq.lovebuy.asynctask.SaveObjectToSP;
import com.example.zgq.lovebuy.asynctask.SyncDataTask;
import com.example.zgq.lovebuy.ui.myUI.ConsumItemsDetailDialog;
import com.example.zgq.lovebuy.util.CheckNetWorkInfoUtil;
import com.example.zgq.lovebuy.util.DBOperateTools;
import com.example.zgq.lovebuy.util.DateTools;

import java.util.ArrayList;
import java.util.Calendar;

public class MyConsumActivity extends AppCompatActivity implements MonthExpAdapter.OnConsumClickAndLongClickListener {
    private Toolbar toolbar;
    private ExpandableListView myEpdLt;
    private Button lastMonthBtn;
    private Button nextMonthBtn;
    private TextView monthNumberTv;
    private TextView noConsumBackgroundTv;
    private TextView statusbar;

    private Calendar calendar;
    private String currentMonth;
    private Uri uri;
    private ArrayList<Consum> myMonthConsums;
    private MonthExpAdapter myAdapter;
    private ArrayList<DayConsum> myDayConsumList;
    private ArrayList<Consum>[] myConsumsListArray;

    //expanableListView info
    private int scrolledX;
    private float scrolledY;
    private boolean[] expandedGroups = new boolean[31];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_consum);
        initDate();
        initView();
        setViewContent();
        getListViewData();
    }
    private void initDate(){
        uri = Uri.parse(ConsumContentProvider.URI + "/consum");
        calendar = Calendar.getInstance();
        getStringMonthDate();
    }
    private void initView(){
        statusbar = (TextView) findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT<21){
            statusbar.setVisibility(View.GONE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.my_month_consums));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myEpdLt = (ExpandableListView) findViewById(R.id.my_month_consums_expandable);
        lastMonthBtn = (Button) findViewById(R.id.btn_tab_last_month);
        nextMonthBtn = (Button) findViewById(R.id.btn_tab_next_month);
        monthNumberTv = (TextView)findViewById(R.id.tv_text_month_number);
        noConsumBackgroundTv = (TextView) findViewById(R.id.no_consum_background);
    }
    private void setViewContent() {
        setMonthNumberText();
        lastMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalenderTolastMonth();
                setMonthNumberText();
                new GetConsumActivityData().execute((Void) null);
            }
        });
        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendarToNextMonth();
                setMonthNumberText();
                new GetConsumActivityData().execute((Void) null);
            }
        });
        myEpdLt.setGroupIndicator(null);
        myEpdLt.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    View c = myEpdLt.getChildAt(0);
                    if (c == null) {
                        return;
                    }
                    int firstVisiblePosition = myEpdLt.getFirstVisiblePosition();
                    int top = c.getTop();
                    scrolledY = -top + firstVisiblePosition;
                    scrolledX = myEpdLt.getScrollX();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
    private void setBackgroundWhenDataIsEmpty(){
        if (myMonthConsums == null) noConsumBackgroundTv.setVisibility(View.VISIBLE);
        else noConsumBackgroundTv.setVisibility(View.GONE);
    }
    private void getListViewData(){
        new GetConsumActivityData().execute((Void) null);
    }
    private void setListViewContent(){
        myAdapter = new MonthExpAdapter(this, myDayConsumList, myConsumsListArray,this);
        myEpdLt.setAdapter(myAdapter);
        setExpandedGroups();
        myEpdLt.scrollTo(scrolledX, (int) scrolledY);
    }
    private void setCalenderTolastMonth(){
        calendar.add(Calendar.MONTH, -1);
        getStringMonthDate();
    }
    private void setCalendarToNextMonth(){
        calendar.add(Calendar.MONTH,+1);
        getStringMonthDate();
    }
    private void setMonthNumberText(){
        monthNumberTv.setText("" + (calendar.get(Calendar.MONTH) + 1));
    }
    private void getStringMonthDate(){
        currentMonth = ""+calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month >= 0 && month <=8){
            currentMonth = currentMonth + "0" + (month+1);
            return;
        }
        else if (month ==9){
            currentMonth = currentMonth + (month+1);
            return;
        }else{
            currentMonth = currentMonth +  month;
        }
    }

    private void getExpandedGroup(){
        for (int i = 0;i< 31;i++){
            if (myEpdLt.isGroupExpanded(i)){
                expandedGroups[i] = true;
            }
        }
    }
    private void setExpandedGroups(){
        for (int i= 0;i<myDayConsumList.size();i++){
            if (expandedGroups[i] == true){
                myEpdLt.expandGroup(i);
            }
        }
    }

    @Override
    public void onItemLongClickListener(final int groupId,final int childId,final Consum consum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_consum).setTitle(R.string.delete_consum)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getExpandedGroup();
                        ContentResolver resolver = getContentResolver();
                        resolver.delete(Uri.parse("content://com.example.zgq.lovebuy.data.ConsumContentProvider/consum"), "date = ?", new String[]{consum.getDate()});
                        getListViewData();
                        if (CheckNetWorkInfoUtil.isNetWorkAvailable(MyConsumActivity.this)) {
                            new SyncDataTask(MyConsumActivity.this, consum, SyncDataTask.DELETECONSUM ).doExecute();
                        } else {
                            new SaveObjectToSP(MyConsumActivity.this, consum, SaveObjectToSP.CONSUMSAVETASK).execute((Void) null);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
    }

    @Override
    public void onItemClickListener(Consum consum) {
        ConsumItemsDetailDialog.newInstance(consum).show(getSupportFragmentManager(),"ConsumItemsDetailDialog");
    }

    private class GetConsumActivityData extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            initMyConsumData();
            int daysNumberOfMonth = DateTools.getMonthLastDay(Integer.parseInt(currentMonth.substring(0,4)),Integer.parseInt(currentMonth.substring(4,6)));
            myDayConsumList = new ArrayList<>(daysNumberOfMonth);
            myConsumsListArray = new ArrayList[daysNumberOfMonth];
            myMonthConsums = (ArrayList) DBOperateTools.getMonthConsum(MyConsumActivity.this, currentMonth, uri);
            if (myMonthConsums == null) return null;
            for (int i = 0;i<daysNumberOfMonth;i++){
                myConsumsListArray[i] = new ArrayList<>();
            }
            for (int i = 0;i< myMonthConsums.size();i++){
                Consum c = myMonthConsums.get(i);
                int whichDay = Integer.parseInt(myMonthConsums.get(i).getDate().substring(6,8));
                myConsumsListArray[whichDay-1].add(c);
            }
            for (int j = 0;j< myConsumsListArray.length;j++){
                if (!myConsumsListArray[j].isEmpty()){
                    myDayConsumList.add(getDayConsumFromConsumList(myConsumsListArray[j]));
                }else myDayConsumList.add(null);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            setListViewContent();
            setBackgroundWhenDataIsEmpty();
        }
        private void initMyConsumData(){
            myDayConsumList = null;
            myConsumsListArray = null;
        }
    }
    private DayConsum getDayConsumFromConsumList(ArrayList<Consum> list) {
        double dayconsum = 0, dayEarning = 0;
        String date = list.get(0).getDate();
        for (Consum c : list) {
            if (c.getProperty() == Consum.ISCOST) {
                dayconsum += c.getNumber();
            }
            if (c.getProperty() == Consum.ISEARNING) {
                dayEarning += c.getNumber();
            }
        }
        return new DayConsum(dayconsum,dayEarning,date);
    }
}
