package com.example.zgq.selectbuttonswithadd;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 37902 on 2016/1/23.
 */
public class SelectButtonsWithAdd extends GridView {
    private Context context;
    private String[] labels = {"吃饭","衣服","住房","交通","娱乐","其他","医药","+"};
    private Button[] buttons = new Button[30];
    private int column;
    public SelectButtonsWithAdd(Context context) {
        super(context);
        this.context = context;
    }

    public SelectButtonsWithAdd(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void init(){
        GridLayout layout = (GridLayout)View.inflate(context, R.layout.grid_layout, this);
//        setMinimumWidth(R.dimen.gridheight);
        setPadding(20, 20, 20, 20);

        for (int i = 0; i<labels.length-1;i++){
            TextView button = new Button(context,null,0,0);
            button.setPadding(5, 35, 5, 35);
            button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setText(labels[i]);
//            button.setWidth(R.dimen.width);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(R.dimen.width,R.dimen.width);
            GridLayout.Spec rowSpec = GridLayout.spec(i / 4);
            GridLayout.Spec columnSpec = GridLayout.spec(i%4);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,columnSpec);

//            params.setGravity(Gravity.NO_GRAVITY);
            addView(button,params);
            Log.d("----->>","buttons");
        }
        TextView textView = new TextView(context);
        textView.setText("+");
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        addView(textView);

    }

}
