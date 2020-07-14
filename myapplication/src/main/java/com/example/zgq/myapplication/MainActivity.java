package com.example.zgq.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.zgq.myapplication.TwoButton;

public class MainActivity extends AppCompatActivity {
    private TwoButton twoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twoButton = (TwoButton)findViewById(R.id.two_button);
        twoButton.setAttrs(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimary), "zhichu", "shouru");
        twoButton.init();
        TextView textView = (TextView) findViewById(R.id.text);
        Log.d("--->>",twoButton.getTitle());
        textView.setText(twoButton.getTitle());
    }
}
