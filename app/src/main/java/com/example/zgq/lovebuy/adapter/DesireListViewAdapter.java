package com.example.zgq.lovebuy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.data.Constants;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37902 on 2016/2/20.
 */
public class DesireListViewAdapter extends ArrayAdapter{
    private Context context;
    private List<MyConsumDesire> list;
    public DesireListViewAdapter(Context context, int resource) {
        super(context, resource);
    }
    public DesireListViewAdapter(Context context,List<MyConsumDesire> list){
        super(context,0);
        this.context = context;
        this.list = list;
    }

    public DesireListViewAdapter(Context context, int resource, Context context1) {
        super(context, resource);
        context = context1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.listitem_activity_home_fragment_desire_list_desire,null);
        }
        TextView detail = (TextView)convertView.findViewById(R.id.tv_desire_detail);
        TextView date = (TextView)convertView.findViewById(R.id.tv_desire_date);
        TextView status = (TextView)convertView.findViewById(R.id.tv_desire_status);
        TextView identity = (TextView)convertView.findViewById(R.id.tv_identity);
        if (!(list.isEmpty())) {
            if (list.get(position).getUser().equals(Constants.getUserName())) {
                identity.setBackgroundColor(context.getResources().getColor(R.color.cyan_identity));
            } else {
                identity.setBackgroundColor(context.getResources().getColor(R.color.red_identity));
            }
            if (list.get(position).getStatus() == 0) {
                status.setBackground(context.getResources().getDrawable(R.drawable.ic_heart_gray));
            } else {
                status.setBackground(context.getResources().getDrawable(R.drawable.ic_heart_red));
            }
            detail.setText(list.get(position).getDetail());
            String s = list.get(position).getDate();
            date.setText(s.substring(0, 4) + "." + s.substring(4, 6) + "." + s.substring(6, 8));
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    public void changeList(ArrayList list){
        this.list = list;
    }
}
