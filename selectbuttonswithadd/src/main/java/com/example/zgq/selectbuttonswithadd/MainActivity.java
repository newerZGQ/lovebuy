package com.example.zgq.selectbuttonswithadd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String[] labels = {"吃饭","衣服","住房","交通","娱乐","其他","医药","+"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout gridLayout = (GridLayout)findViewById(R.id.grid);
//        sButton.setColumnCount(4);
//        sButton.init();
        for (int i = 0; i<labels.length-1;i++){
            TextView button = new Button(this,null,0,0);
            button.setPadding(5, 35, 5, 35);
            button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setText(labels[i]);
//            button.setWidth(R.dimen.width);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(R.dimen.width,R.dimen.width);
            GridLayout.Spec rowSpec = GridLayout.spec((i+3) / 4);
            GridLayout.Spec columnSpec = GridLayout.spec((i+3)%4);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,columnSpec);
            params.width = 120;
            params.leftMargin = 30;
            params.rightMargin = 30;
            params.bottomMargin = 20;
            params.topMargin = 20;
            params.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
            gridLayout.addView(button, params);
            Log.d("----->>", "buttons");
        }
    }
}
