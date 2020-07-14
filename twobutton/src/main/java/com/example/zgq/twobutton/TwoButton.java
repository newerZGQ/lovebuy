package com.example.zgq.twobutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

/**
 * Created by 37902 on 2016/1/22.
 */
public class TwoButton extends LinearLayout {
    public Button leftButton;
    public Button rightButton;
    private Context context;
    private int focusedBack;
    private int unFocusedBack;
    private int focusedText;
    private int unFocusedText;
    private String leftText;
    private String rightText;
    public TwoButton(Context context,int focused,int unFocused,int focusedText,int unFocusedText,String leftText,String rightText) {
        super(context);
        this.context = context;
        this.focusedBack = focused;
        this.unFocusedBack = unFocused;
        this.focusedText = focusedText;
        this.unFocusedText = unFocusedText;
        this.leftText = leftText;
        this.rightText = rightText;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init(){
        View.inflate(context,R.layout.two_button,this);
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
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeAttrs(v);
            }
        });
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

}
