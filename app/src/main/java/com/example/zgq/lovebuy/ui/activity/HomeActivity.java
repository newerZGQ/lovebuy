package com.example.zgq.lovebuy.ui.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.adapter.DrawerListviewAdapter;
import com.example.zgq.lovebuy.data.Constants;
import com.example.zgq.lovebuy.data.SharedPreferenceData;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;
import com.example.zgq.lovebuy.model.user.MyUser;
import com.example.zgq.lovebuy.asynctask.AsyncSPtoWEB;
import com.example.zgq.lovebuy.ui.fragment.ChartFragment;
import com.example.zgq.lovebuy.ui.fragment.DesireFragment;
import com.example.zgq.lovebuy.ui.fragment.LastConsumFragment;

import com.example.zgq.lovebuy.util.CheckNetWorkInfoUtil;
import com.example.zgq.lovebuy.util.MyActivityManager;
import com.example.zgq.lovebuy.util.SharedPreferencesUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class HomeActivity extends AppCompatActivity implements LastConsumFragment.OnFragmentInteractionListener,
        ChartFragment.OnFragmentInteractionListener,
        DesireFragment.OnFragmentInteractionListener{

    private static final String TAG = "HOMEACTIVITY";

    private Toolbar toolbar;
    private TextView statusbar;
    private FragmentManager fm;

    private ArrayList<Fragment> homeFragments;
    private ArrayList<String> fragmentTitleList;
    private Uri consumUri;
    private Uri desireUri;
    private boolean dataIsChanged = false;
    private boolean isFirstTimeLogin;

    private ListView drawerLVi;
    private DrawerListviewAdapter drawerAdapter;
    private TextView configTv;

    private DrawerLayout drawerLayout;
    private LinearLayout drawerLv;
    private ActionBarDrawerToggle mDrawerToggle;

    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton floatingButtonConsum;
    private FloatingActionButton floatingButtonDesire;
    private TextView coverTv;
    private TextView myUserName;

    //indicators of three fragments
    public int fragmentIndicator;
    public static final int PAGE_HOME = 0;
    public static final int PAGE_CHART = 1;
    public static final int PAGE_DESIRE = 2;

    private LastConsumFragment lastConsumFragment;
    private ChartFragment chartFragment;
    private DesireFragment desireFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager.addActivity(this);
        if (savedInstanceState == null){
            initFragmentIndicator();
        }else{
            fragmentIndicator = savedInstanceState.getInt("fragmentIndicator");
        }
        getMyFragmentManager();
        initIsFirstTimeLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_home);
        try {
            initConstantsClass();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkUnprocessedData();
        initView();
        judgeToSetContentView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyActivityManager.removeActivity(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragmentIndicator", fragmentIndicator);
    }
    private void getMyFragmentManager(){
        fm = getSupportFragmentManager();
    }
    private void initFragmentIndicator(){
        fragmentIndicator = 0;
    }

    private void initConstantsClass() throws JSONException {
        Constants.userName = BmobUser.getCurrentUser(this).getUsername();
    }
    private void initLableSharedPreference(){
        SharedPreferencesUtil.initLablesSharedPf(this);
    }

    private boolean isFirstTimeLogin(){
        return isFirstTimeLogin;
    }
    private void initIsFirstTimeLogin(){
        isFirstTimeLogin = getIntent().getBooleanExtra("LoginFirstTime",false);
    }
    private void setNotFirstTimeLogin(){
        isFirstTimeLogin = false;
    }

    private void initDataFromWeb() {
        new InitDataFromWeb(this).execute((Void) null);
    }
    private void checkUnprocessedData(){
        if (CheckNetWorkInfoUtil.isNetWorkAvailable(this)) {
            new AsyncSPtoWEB(this).execute((Void) null);
        }
    }

    private void initView() {
        statusbar = (TextView) findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT<21){
            statusbar.setVisibility(View.GONE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLv = (LinearLayout) findViewById(R.id.lv_drawer_view);
        drawerLVi = (ListView) findViewById(R.id.lVi_left_drawer);
        configTv = (TextView) findViewById(R.id.tv_config);
        configTv.setClickable(true);
        configTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                drawerLayout.closeDrawer(drawerLv);
            }
        });
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.float_button_menu);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                coverTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                coverTv.setVisibility(View.GONE);
            }
        });
        coverTv = (TextView) findViewById(R.id.tv_home_cover);
        coverTv.setClickable(true);
        coverTv.setVisibility(View.GONE);
        floatingButtonConsum = (FloatingActionButton) findViewById(R.id.flaot_button_conusm);
        floatingButtonConsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionsMenu.collapseImmediately();
                startActivity(new Intent(HomeActivity.this, AddConsumActivity.class));
            }
        });
        floatingButtonDesire = (FloatingActionButton) findViewById(R.id.flaot_button_desire);
        floatingButtonDesire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionsMenu.collapseImmediately();
                startActivity(new Intent(HomeActivity.this, AddDesireActivity.class));
            }
        });

        myUserName = (TextView) findViewById(R.id.tv_my_user);
    }
    private void judgeToSetContentView(){
        if (isFirstTimeLogin()) {
            initDataFromWeb();
            initLableSharedPreference();
            SharedPreferenceData.initUserSharedPreferenceData(this);
            setSharedPreferencesData();
            setNotFirstTimeLogin();
        }else{
            initFragments();
            selectItem(fragmentIndicator);
        }
        setViewContent();
    }
    private void initFragments(){
        initLastConsumFragment();
        initChartFragment();
        initDesireFragment();
    }
    private void initLastConsumFragment(){
        lastConsumFragment = LastConsumFragment.newInstance("this is", "LastConsumFragment");
        FragmentTransaction transaction = fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.fragment_container, lastConsumFragment).hide(lastConsumFragment).commit();
    }
    private void initChartFragment(){
        chartFragment = ChartFragment.newInstance("this is", "ChartFragment");
        FragmentTransaction transaction = fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.fragment_container, chartFragment).hide(chartFragment).commit();
    }
    private void initDesireFragment(){
        desireFragment = DesireFragment.newInstance("this is", "DesireFragment");
        FragmentTransaction transaction = fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.fragment_container, desireFragment).hide(desireFragment).commit();
    }

    private void setViewContent() {
        fragmentTitleList = new ArrayList<>();
        fragmentTitleList.add(getResources().getString(R.string.navigation_home));
        fragmentTitleList.add(getResources().getString(R.string.navigation_chart));
        fragmentTitleList.add(getResources().getString(R.string.navigation_desire));
        drawerAdapter = new DrawerListviewAdapter(this, fragmentTitleList);
        drawerLVi.setAdapter(drawerAdapter);
        drawerLVi.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.action_settings, R.string.action_settings) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        myUserName.setText(Constants.userName);
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
        switch (item.getItemId()) {
            case R.id.sync_data:
                new AsyncSPtoWEB(this).execute((Void) null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        FragmentTransaction transaction = fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        switch (position) {
            case PAGE_HOME:
                transaction.hide(getCurrentFragment(fragmentIndicator)).show(lastConsumFragment).commit();
                toolbar.setTitle(getResources().getString(R.string.app_name));
                fragmentIndicator = PAGE_HOME;
                break;
            case PAGE_CHART:
                transaction.hide(getCurrentFragment(fragmentIndicator)).show(chartFragment).commit();
                toolbar.setTitle(getResources().getString(R.string.navigation_chart));
                fragmentIndicator = PAGE_CHART;
                break;
            case PAGE_DESIRE:
                transaction.hide(getCurrentFragment(fragmentIndicator)).show(desireFragment).commit();
                toolbar.setTitle(getResources().getString(R.string.navigation_desire));
                fragmentIndicator = PAGE_DESIRE;
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(drawerLv);
    }
    public Fragment getCurrentFragment(int fragmentIndicator){
        if (fragmentIndicator == PAGE_HOME) return lastConsumFragment;
        if (fragmentIndicator == PAGE_CHART) return chartFragment;
        if (fragmentIndicator == PAGE_DESIRE) return desireFragment;
        return null;
    }

    //if firsttime login,then sync data from web
    public class InitDataFromWeb extends AsyncTask {
        private Context context;

        public InitDataFromWeb(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            setProgressBarVisibility(true);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            BmobQuery<Consum> queryConsum = new BmobQuery<Consum>();
            String s = BmobUser.getCurrentUser(HomeActivity.this).getUsername();
            queryConsum.addWhereEqualTo("user", BmobUser.getCurrentUser(HomeActivity.this).getUsername());
            queryConsum.count(context, Consum.class, new CountListener() {
                @Override
                public void onSuccess(int i) {
                    BmobQuery<Consum> query = new BmobQuery<Consum>();
                    query.addWhereEqualTo("user", BmobUser.getCurrentUser(HomeActivity.this).getUsername());
                    query.setLimit(i);
                    query.findObjects(context, new FindListener<Consum>() {
                        @Override
                        public void onSuccess(List<Consum> object) {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < object.size(); i++) {
                                Consum consum = (Consum) object.get(i);
                                ContentResolver contentResolver = context.getContentResolver();
                                ContentValues values = new ContentValues();
                                values.put("number", consum.getNumber());
                                values.put("date", consum.getDate());
                                values.put("lable", consum.getLable());
                                values.put("happiness", consum.getHappiness());
                                values.put("property", consum.getProperty());
                                values.put("detail", consum.getDetail());
                                Uri uri = Uri.parse("content://com.example.zgq.lovebuy.data.ConsumContentProvider/consum");
                                contentResolver.insert(uri, values);
                            }
                            initLastConsumFragment();
                            initChartFragment();
                            selectItem(fragmentIndicator);
                        }

                        @Override
                        public void onError(int code, String msg) {
                            // TODO Auto-generated method stub
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            BmobQuery<MyConsumDesire> queryDesire = new BmobQuery<MyConsumDesire>();
            queryDesire.addWhereEqualTo("user", BmobUser.getCurrentUser(HomeActivity.this).getUsername());
            queryDesire.count(context, MyConsumDesire.class, new CountListener() {
                @Override
                public void onSuccess(int i) {
                    BmobQuery<MyConsumDesire> queryDesire = new BmobQuery<MyConsumDesire>();
                    queryDesire.addWhereEqualTo("user", BmobUser.getCurrentUser(HomeActivity.this).getUsername());
                    queryDesire.setLimit(i);
                    queryDesire.findObjects(context, new FindListener<MyConsumDesire>() {
                        @Override
                        public void onSuccess(List<MyConsumDesire> object) {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < object.size(); i++) {
                                MyConsumDesire desire = (MyConsumDesire) object.get(i);
                                ContentResolver contentResolver = context.getContentResolver();
                                ContentValues values = new ContentValues();
                                values.put("number", desire.getNumber());
                                values.put("date", desire.getDate());
                                values.put("detail", desire.getDetail());
                                values.put("status",desire.getStatus());
                                Uri uri = Uri.parse("content://com.example.zgq.lovebuy.data.DesireContentProvider/desire");
                                contentResolver.insert(uri, values);
                            }
                            initDesireFragment();
                        }

                        @Override
                        public void onError(int code, String msg) {
                            // TODO Auto-generated method stub
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            setProgressBarVisibility(false);
            Toast.makeText(context, getString(R.string.synchronizing_from_web), Toast.LENGTH_SHORT).show();
        }
    }
    private void setSharedPreferencesData() {

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(HomeActivity.this).getUsername());
        query.findObjects(this, new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> object) {
                String myUserName = BmobUser.getCurrentUser(HomeActivity.this).getUsername();
                String associateUserName = ((MyUser) object.get(0)).getAssociateEmail();
                SharedPreferences user = getSharedPreferences("userName", 0);
                SharedPreferences.Editor editor = user.edit();
                editor.putString("myUserName", myUserName);
                if (associateUserName != null && !associateUserName.equals("")) {
                    editor.putString("associateUserName", associateUserName);
                }
                else editor.putString("associateUserName", getString(R.string.have_not_associat_with_other));
                editor.commit();
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        return true;
    }
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), getString(R.string.more_action_down_exit_application), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else{
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class ConsumObserver extends ContentObserver {
        public ConsumObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
        }
    }
    class DesireObserver extends ContentObserver {
        public DesireObserver(Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
        }
    }

    @Override
    public void onDeaireFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onLastConsumFragmentInteraction(boolean dataIsChanged) {
        return true;
    }

    @Override
    public void onChartFragmentInteraction(Uri uri) {

    }
}
