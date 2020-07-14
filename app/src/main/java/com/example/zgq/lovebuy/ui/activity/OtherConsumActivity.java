package com.example.zgq.lovebuy.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.data.SharedPreferenceData;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.ui.myUI.MyRatingBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 37902 on 2016/3/17.
 */
public class OtherConsumActivity extends AppCompatActivity{
    private final static String OTHERCONSUM = "otherConusm";
    private ListView listView;
    private MyAdapter adapter;
    private ArrayList<Consum> list;
    private String associateUserName;
    private Toolbar toolbar;
    private TextView statusbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_consum);
        initData();
        initView();
        getOtherConsums();
    }
    private void initView(){
        statusbar = (TextView) findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT<21){
            statusbar.setVisibility(View.GONE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.other_recent_consums));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView =(ListView) findViewById(R.id.lVi_other_consum);
    }
    private void initData(){
        list = new ArrayList<>();
        associateUserName = SharedPreferenceData.getSharedPreferenceDataAssociateUserName(this);
    }
    private void setViewContent(){
        adapter = new MyAdapter(this,0);
        listView.setAdapter(adapter);
    }
    public class MyAdapter extends ArrayAdapter{
        @Override
        public int getCount() {
            return list.size();
        }
        public MyAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(OtherConsumActivity.this,R.layout.listitem_other_consum,null);
            }
            Consum consum = list.get(position);
            TextView property = (TextView) convertView.findViewById(R.id.tv_consum_property);
            TextView number = (TextView) convertView.findViewById(R.id.tv_consum_number);
            TextView date = (TextView) convertView.findViewById(R.id.consum_date);
            TextView lable = (TextView) convertView.findViewById(R.id.consum_lable);
            TextView detail = (TextView)convertView.findViewById(R.id.consum_detail);
            MyRatingBar ratingBar = (MyRatingBar)convertView.findViewById(R.id.other_consum_item_ratingbar);

            ratingBar.setStar((float) consum.getHappiness());
            if (consum.getProperty()==Consum.ISCOST) {
                property.setText(getString(R.string.cost));
            }else{
                property.setText(getString(R.string.earning));
            }
            number.setText("" + consum.getNumber());
            date.setText(parseDateToshow(consum.getDate()));
            lable.setText(consum.getLable());
            if (consum.getDetail() == null || consum.getDetail().equals("") || consum.getDetail().length() == 0){
                detail.setVisibility(View.GONE);
            }else {
                detail.setText("" + consum.getDetail());
            }
            return convertView;
        }
        public SpannableStringBuilder parseDateToshow(String date){
            String month = ""+Integer.parseInt(date.substring(4,6));
            String day = "" + Integer.parseInt(date.substring(6,8));
            String myDate = month+"月"+day+"日"+" "+date.substring(8,13);
            int start = 0;
            switch (myDate.length()){
                case 10:
                    start = 5;
                    break;
                case 11:
                    start = 6;
                    break;
                case 12:
                    start = 7;
                    break;
            }
            SpannableStringBuilder builder = new SpannableStringBuilder(myDate);
            builder.setSpan(new AbsoluteSizeSpan(40), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_gray)), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
    }
    private void getOtherConsums(){
        if (associateUserName == null)return;
        list = new ArrayList<>();
        BmobQuery<Consum> query = new BmobQuery<Consum>();
        query.addWhereEqualTo("user", associateUserName);
        query.order("-date");
        query.setLimit(10);
        query.findObjects(this, new FindListener<Consum>() {
            @Override
            public void onSuccess(List<Consum> object) {
                // TODO Auto-generated method stub
                for (Consum consum : object) {
                    list.add(consum);
                }
                setViewContent();
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.d("---->>","otherfaild11");
            }
        });
    }

}
