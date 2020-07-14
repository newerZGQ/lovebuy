package com.example.zgq.lovebuy.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.data.Constants;
import com.example.zgq.lovebuy.data.SharedPreferenceData;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by 37902 on 2016/3/15.
 */
public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this, Constants.BmobKey);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (isLogined()){
            SharedPreferenceData.initUserSharedPreferenceData(this);
            goToLoginActivity();
        }else{
            goToHomeActivity();
        }
    }
    private boolean isLogined(){
        BmobUser user = BmobUser.getCurrentUser(this);
        if (user == null) {
            return true;
        }else{
            return false;
        }
    }
    private void goToLoginActivity(){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
    }
    private void goToHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
