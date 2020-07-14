package com.example.zgq.lovebuy.ui.myUI;

/**
 * Created by 37902 on 2016/1/22.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.zgq.lovebuy.R;

import org.json.JSONException;

/**
 * Created by 37902 on 2016/1/22.
 */
public class CostOrEaringButton extends LinearLayout {
    private String title = "支出";
    public Button leftButton;
    public Button rightButton;
    private Context context;
    private int focusedBack = getResources().getColor(R.color.colorPrimary);
    private int unFocusedBack = getResources().getColor(R.color.colorPrimaryDark);;
    private int focusedText= getResources().getColor(R.color.colorPrimaryDark);
    private int unFocusedText = getResources().getColor(R.color.colorPrimary);;
    private String leftText = "左";
    private String rightText = "右";
    public WatchListener watchListener;

    public String getLeftText() {
        return leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public CostOrEaringButton(Context context) {
        super(context);
        this.context = context;
    }

    public CostOrEaringButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public void setAttrs(int focusedBack,int unFocusedBack,int focusedText,int unFocusedText,String leftText,String rightText,WatchListener watchListener){
        this.focusedBack = focusedBack;
        this.unFocusedBack = unFocusedBack;
        this.focusedText = focusedText;
        this.unFocusedText = unFocusedText;
        this.leftText = leftText;
        this.rightText = rightText;
        this.watchListener = watchListener;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init(){
        View.inflate(context,R.layout.my_view_cost_earing_buttons,this);
        title = leftText;
        leftButton = (Button) findViewById(R.id.leftButton);
        rightButton = (Button) findViewById(R.id.rightButton);
        leftButton.setBackgroundColor(focusedBack);
        rightButton.setBackgroundColor(unFocusedBack);
        leftButton.setTextColor(focusedText);
        rightButton.setTextColor(unFocusedText);
        leftButton.setText(leftText);
        rightButton.setText(rightText);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAttrs(v);
                title = leftText;
                try {
                    watchListener.onClickLeftButton();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeAttrs(v);
                title = rightText;
                try {
                    watchListener.onClickRightButton();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public String getTitle(){
        return title;
    }
    private void changeAttrs(View view){
        if (view.getId() == R.id.leftButton){
            leftButton.setBackgroundColor(focusedBack);
            rightButton.setBackgroundColor(unFocusedBack);
            leftButton.setTextColor(focusedText);
            rightButton.setTextColor(unFocusedText);
        }
        if (view.getId() == R.id.rightButton){
            leftButton.setBackgroundColor(unFocusedBack);
            rightButton.setBackgroundColor(focusedBack);
            leftButton.setTextColor(unFocusedText);
            rightButton.setTextColor(focusedText);
        }
    }
    public interface WatchListener{
        public abstract void onClickLeftButton() throws JSONException;
        public abstract void onClickRightButton() throws JSONException;
    }
}
