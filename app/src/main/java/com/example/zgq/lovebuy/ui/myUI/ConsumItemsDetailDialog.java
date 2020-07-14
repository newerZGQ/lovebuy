package com.example.zgq.lovebuy.ui.myUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.model.consum.Consum;

/**
 * Created by 37902 on 2016/3/12.
 */
public class ConsumItemsDetailDialog extends DialogFragment {
    private Consum consum;
    private View view;
    public static ConsumItemsDetailDialog newInstance(Consum consum) {
        ConsumItemsDetailDialog fragment = new ConsumItemsDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable("consum",consum);
        fragment.setArguments(args);
        return fragment;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            consum = (Consum)getArguments().getSerializable("consum");
        }else{
            consum = new Consum();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        if (savedInstanceState != null){

//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.listitem_consum_detail,null);
        TextView property = (TextView) view.findViewById(R.id.tv_consum_property);
        TextView number = (TextView) view.findViewById(R.id.tv_consum_number);
        TextView date = (TextView) view.findViewById(R.id.consum_date);
        TextView lable = (TextView) view.findViewById(R.id.consum_lable);
        TextView detail = (TextView)view.findViewById(R.id.consum_detail);
        MyRatingBar ratingBar = (MyRatingBar)view.findViewById(R.id.other_consum_item_ratingbar);
        ratingBar.setStar((float)consum.getHappiness());
        if (consum.getProperty()==Consum.ISCOST) {
            property.setText("支出");
        }else{
            property.setText("收入");
        }
        number.setText("" + consum.getNumber());
        date.setText(parseDateToshow(consum.getDate()));
        lable.setText(consum.getLable());
        detail.setText(""+consum.getDetail());
        builder.setView(view);
        return builder.create();
    }
    public SpannableStringBuilder parseDateToshow(String date){
        String month = ""+Integer.parseInt(date.substring(4,6));
        String day = "" + Integer.parseInt(date.substring(6,8));
        String myDate = month+"月"+day+"日"+" "+date.substring(8,13)+" "+date.substring(0,4);
        int start = 0;
        switch (myDate.length()){
            case 15:
                start = 5;
                break;
            case 16:
                start = 6;
                break;
            case 17:
                start = 7;
                break;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(myDate);
        builder.setSpan(new AbsoluteSizeSpan(40),start,builder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_gray)),start,builder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }
}
