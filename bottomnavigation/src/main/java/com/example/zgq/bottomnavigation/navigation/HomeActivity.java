package com.example.zgq.bottomnavigation.navigation;

/**
 * Created by 37902 on 2016/2/17.
 */

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.adapter.HomePagerAdapter;
import com.example.zgq.lovebuy.ui.fragment.ChartFragment;
import com.example.zgq.lovebuy.ui.fragment.DesireFragment;
import com.example.zgq.lovebuy.ui.fragment.LastConsumFragment;
import com.example.zgq.lovebuy.ui.fragment.WebFragment;
import com.example.zgq.lovebuy.util.CreateDir;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements LastConsumFragment.OnFragmentInteractionListener,
        ChartFragment.OnFragmentInteractionListener,
        DesireFragment.OnFragmentInteractionListener,
        WebFragment.OnFragmentInteractionListener,
        RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    private static final String TAG = "HOMEACTIVITY";

    private Toolbar toolbar;


    private ViewPager homePager;

    private HomePagerAdapter adapter;

    private FragmentManager fm;

    private ArrayList<Fragment> homeFragments;

    private RadioGroup tabMenu;
    private RadioButton tabConsum;
    private RadioButton tabChart;
    private RadioButton tabDesire;
    private RadioButton tabWeb;

    private ListView drawerListView;
    private DrawerListviewAdapter drawerAdapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Button toolbarNavigation;
    private Button toolbarActionBar;
    private TextView toolbarTitle;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setViewContent();


        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.action_settings,R.string.action_settings){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle("open");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle("close");
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);

//        toolbar.setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);



        CreateDir.testSDCard(this);
//        setTitle("微账");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_menu_white_24dp);

        homePager = (ViewPager) findViewById(R.id.home_pager);

        homeFragments = new ArrayList<Fragment>(4);
        Fragment lastConsumFragment = LastConsumFragment.newInstance("this is", "LastConsumFragment");
        Fragment chartFragment = ChartFragment.newInstance("this is", "ChartFragment");
        Fragment desireFragment = DesireFragment.newInstance("this is", "DesireFragment");
        Fragment webFragment = WebFragment.newInstance("this is", "WebFragment");
        homeFragments.add(lastConsumFragment);
        homeFragments.add(chartFragment);
        homeFragments.add(desireFragment);
        homeFragments.add(webFragment);
        fm = getSupportFragmentManager();
        adapter = new HomePagerAdapter(fm, homeFragments);

        homePager.setAdapter(adapter);
        homePager.addOnPageChangeListener(this);

        bindViews();

//                String[] date = {"2015110108:0102","2015110109:0102","2015110110:0102","2015110111:0102","2015110112:0102","2015110113:0102","2015110114:0102","2015110115:0102","2015110116:0102","2015110117:0102",
//                "2015110207:0102","2015110208:0102","2015110209:0102","2015110210:0102","2015110211:0102","2015110212:0102","2015110213:0102","2015110214:0102","2015110215:0102","2015110216:0102","2015110217:0102",
//                "2015110307:0102","2015110308:0102","2015110309:0102","2015110310:0102","2015110311:0102","2015110312:0102","2015110313:0102","2015110314:0102","2015110315:0102","2015110316:0102","2015110317:0102",
//                "2015110407:0102","2015110408:0102","2015110409:0102","2015110410:0102","2015110411:0102","2015110412:0102","2015110413:0102","2015110414:0102","2015110415:0102","2015110416:0102","2015110417:0102",
//                "2015120107:0102","2015120108:0102","2015120109:0102","2015120110:0102","2015120111:0102","2015120112:0102","2015120113:0102","2015120114:0102","2015120115:0102","2015120116:0102","2015120117:0102",
//                "2015120207:0102","2015120208:0102","2015120209:0102","2015120210:0102","2015120211:0102","2015120212:0102","2015120213:0102","2015120214:0102","2015120215:0102","2015120216:0102","2015120217:0102",
//                "2015120307:0102","2015120308:0102","2015120309:0102","2015120310:0102","2015120311:0102","2015120312:0102","2015120313:0102","2015120314:0102","2015120315:0102","2015120316:0102","2015120317:0102",
//                "2015120407:0102","2015120408:0102","2015120409:0102","2015120410:0102","2015120411:0102","2015120412:0102","2015120413:0102","2015120414:0102","2015120415:0102","2015120416:0102","2015120417:0102",
//                };
//        ContentResolver contentResolver = getContentResolver();
//        Uri uri = Uri.parse("content://com.example.zgq.lovebuy.data.ConsumContentProvider/consum");
//        for (int i = 0; i<date.length; i++) {
//            Consum singleConsumption = new Consum(i, "cloth", date[i], 5, null, null);
//            ContentValues values = new ContentValues();
//            values.put("price", singleConsumption.getPrice());
//            values.put("date", singleConsumption.getDate());
//            values.put("lable", singleConsumption.getLable());
//            values.put("happiness", singleConsumption.getHappines());
//            values.put("imageId", singleConsumption.getImageId());
//            values.put("detail", singleConsumption.getDetail());
////            Log.d("------->>",singleConsumption.getDate());
//            contentResolver.insert(uri,values);
//            Log.d("------->>", contentResolver.insert(uri, values).toString());
//        }

    }
    private void initView(){
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer_listview);
        toolbarNavigation = (Button) findViewById(R.id.navigation_icon);
        toolbarActionBar = (Button) findViewById(R.id.action_icon);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
    }
    private void setViewContent(){
        toolbarNavigation.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.app_name);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(getResources().getString(R.string.navigation_home));
        arrayList.add(getResources().getString(R.string.navigation_chart));
        arrayList.add(getResources().getString(R.string.navigation_desire));
        drawerAdapter = new DrawerListviewAdapter(this,arrayList);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        homePager.setCurrentItem(PAGE_ONE);
                        Log.d("---------->>", "fail");
                        break;
                    case 1:
                        homePager.setCurrentItem(PAGE_TWO);
                        Log.d("---------->>", "fail");
                        break;
                    case 2:
                        homePager.setCurrentItem(PAGE_THREE);
                        break;
                    case 3:
                        homePager.setCurrentItem(PAGE_FOUR);
                        break;

                }
            }
        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    private void bindViews() {

        tabMenu = (RadioGroup) findViewById(R.id.tab_menu);
        tabConsum = (RadioButton) findViewById(R.id.tab_consum);
        tabChart = (RadioButton) findViewById(R.id.tab_chart);
        tabDesire = (RadioButton) findViewById(R.id.tab_desire);
        tabWeb = (RadioButton) findViewById(R.id.tab_web);
        tabMenu.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_consum:
                homePager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.tab_chart:
                homePager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.tab_desire:
                homePager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.tab_web:
                homePager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (homePager.getCurrentItem()) {
                case PAGE_ONE:
                    tabConsum.setChecked(true);
                    break;
                case PAGE_TWO:
                    tabChart.setChecked(true);
                    break;
                case PAGE_THREE:
                    tabDesire.setChecked(true);
                    break;
                case PAGE_FOUR:
                    tabWeb.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onDeaireFragmentInteraction(Uri uri) {

    }

    @Override
    public void onWebFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLastConsumFragmentInteraction(Uri uri) {

    }

    @Override
    public void onChartFragmentInteraction(Uri uri) {

    }
}

