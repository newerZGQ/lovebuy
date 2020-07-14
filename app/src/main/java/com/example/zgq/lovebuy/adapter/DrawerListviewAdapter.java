package com.example.zgq.lovebuy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;

import java.util.ArrayList;

/**
 * Created by 37902 on 2016/1/27.
 */
public class DrawerListviewAdapter extends ArrayAdapter {
    private ArrayList<String> list;
    private ArrayList<Drawable> iconList = new ArrayList<>();
    private Context context;
    public DrawerListviewAdapter(Context context, ArrayList list) {
        super(context,0);
        this.list = list;
        this.context = context;
        iconList.add(context.getResources().getDrawable(R.drawable.ic_activity_home_drawer_coin));
        iconList.add(context.getResources().getDrawable(R.drawable.ic_activity_home_drawer_chart));
        iconList.add(context.getResources().getDrawable(R.drawable.ic_activity_home_drawer_desire));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.listitem_activity_home_drawer,null);
        }
        TextView text = (TextView)convertView.findViewById(R.id.tv_drawer_listview_title);
        TextView icon = (TextView)convertView.findViewById(R.id.tv_drawer_list_icon);
        icon.setBackground(iconList.get(position));
        text.setText(list.get(position));
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
}
