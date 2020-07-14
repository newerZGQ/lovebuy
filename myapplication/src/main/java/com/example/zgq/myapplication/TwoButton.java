package com.example.zgq.myapplication;

/**
 * Created by 37902 on 2016/1/22.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by 37902 on 2016/1/22.
 */
public class TwoButton extends LinearLayout {
    private String title;
    public Button leftButton;
    public Button rightButton;
    private Context context;
    private int focusedBack = getResources().getColor(R.color.colorPrimary);
    private int unFocusedBack = getResources().getColor(R.color.colorPrimaryDark);;
    private int focusedText= getResources().getColor(R.color.colorPrimaryDark);
    private int unFocusedText = getResources().getColor(R.color.colorPrimary);;
    private String leftText = "左";
    private String rightText = "右";

    public TwoButton(Context context) {
        super(context);
        this.context = context;
    }

    public TwoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public void setAttrs(int focusedBack,int unFocusedBack,int focusedText,int unFocusedText,String leftText,String rightText){
        this.focusedBack = focusedBack;
        this.unFocusedBack = unFocusedBack;
        this.focusedText = focusedText;
        this.unFocusedText = unFocusedText;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init(){
        View.inflate(context,R.layout.two_button,this);
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
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeAttrs(v);
                title = rightText;
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
}
