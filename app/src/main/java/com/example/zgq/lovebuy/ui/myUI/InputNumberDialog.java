package com.example.zgq.lovebuy.ui.myUI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;

/**
 * Created by 37902 on 2016/1/23.
 */
public class InputNumberDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TableLayout tableLayout;
    private TextView numberIn;
    private String number;
    private ActoinListener listener;
    public Button cancle;
    public Button confirm;

    public InputNumberDialog(Context context) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    public InputNumberDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_view_dialog_input_number);
        initView();
    }
    private void initView(){
        tableLayout  =  (TableLayout) findViewById(R.id.input_number_table);
        numberIn = (TextView) ((TableRow)tableLayout.getChildAt(0)).getChildAt(0);
        numberIn.setHint("0");
        for (int i = 1;i<tableLayout.getChildCount()-1;i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) ((TableRow)tableLayout.getChildAt(i)).getChildAt(j);
                button.setOnClickListener(this);
            }
        }
        cancle = (Button) findViewById(R.id.cancel_action);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        confirm = (Button) findViewById(R.id.confirm_action);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onConfirm();
                cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {
        number = numberIn.getText().toString();

        if (v.getId() == R.id.button_back){
            if (number.length()>0) {
                numberIn.setText(number.subSequence(0, number.length() - 1));
            }
        }else if (v.getId() == R.id.number_point){
            if (number.length() == 0){
                numberIn.setText("0");
            }
            if (!number.contains(".")){
                numberIn.append(".");
            }
        } else {
            if (number.contains(".")) {
                int pointPosition = number.indexOf(".");
                if ((number.substring(pointPosition, number.length())).length() == 3) {
                    return;
                }
            }
            if (v.getId() == R.id.number_0 && numberIn.getText() == ""){
                return;
            }
            numberIn.append((((Button) v).getText()));
        }
        number = numberIn.getText().toString();
    }
    public String getNumber(){
        return number = numberIn.getText().toString();
    }
    public interface ActoinListener{
        public void onConfirm();
    }
    public void setListener(ActoinListener listener){
        this.listener = listener;
    }
}
