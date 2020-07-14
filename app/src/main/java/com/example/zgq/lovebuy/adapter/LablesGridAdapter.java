package com.example.zgq.lovebuy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;

import java.util.ArrayList;

/**
 * Created by 37902 on 2016/1/23.
 */
public class LablesGridAdapter extends ArrayAdapter {
    private ArrayList<String> list;
    private Context context;
    private String lableSelected;
    private changeLableListener mListener;
//    private WatchLableListener listener;

    public LablesGridAdapter(Context context, ArrayList<String> list,changeLableListener mListener) {
        super(context, 0);
        this.list = list;
        this.context = context;
        this.mListener = mListener;
//        this.listener = listener;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.listitem_lables_grid, null);
        final TextView textView = (TextView) view.findViewById(R.id.lable_textview);
        textView.setText(list.get(position));
        if (list.get(position).equals("+")) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddLable();
                }
            });

        } else {
            textView.setFocusableInTouchMode(true);
            textView.setClickable(true);
            textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d("---->>>", "out");
                    TextView textView1 = (TextView) v;
                    if (hasFocus) {
                        if (textView1.getText() != "+")
                            lableSelected = ((TextView) v).getText().toString();
                    } else {

                    }
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onDeleteLable(((TextView)v).getText().toString());
                    return true;
                }
            });
        }
        return view;
    }

    public String getLableSelected() {
        return lableSelected;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public interface changeLableListener {
        public void onAddLable();
        public void onDeleteLable(String lable);
    }
}
