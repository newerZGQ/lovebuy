package com.example.zgq.lovebuy.ui.myUI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zgq.lovebuy.R;

/**
 * Created by 37902 on 2016/1/24.
 */
public class InputDetailDialog extends Dialog{
    private Context context;
    private String detail;
    private String hint;
    private EditText editText;
    private Button button;
    private OnGetDetailListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public InputDetailDialog(Context context) {
        super(context);
        this.context = context;
    }

    public InputDetailDialog(Context context, int themeResId,String hint) {
        super(context, themeResId);
        this.context = context;
        this.hint= hint;
    }
    private void init(){
        setContentView(R.layout.my_view_dialog_input_detail);
        editText = (EditText) findViewById(R.id.tv_detail_edit);
        editText.setHint(hint);
        button = (Button) findViewById(R.id.confirm_detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail = editText.getText().toString();
                listener.onGetDetail();
                cancel();
            }
        });
    }
    public interface OnGetDetailListener{
        public void onGetDetail();
    }
    public void setOnGetDetailListener(OnGetDetailListener listener){
        this.listener = listener;
    }
    public String getDetail(){
        return detail;
    }
}
