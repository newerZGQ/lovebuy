package com.example.zgq.lovebuy.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.consum.DayConsum;
import com.example.zgq.lovebuy.util.FloatUtil;

import java.util.ArrayList;

/**
 * Created by 37902 on 2016/1/11.
 */
public class MonthExpAdapter extends BaseExpandableListAdapter {
    private ArrayList<DayConsum> parentList;
    private ArrayList<Consum>[] childLists;
    private Context context;
    private Typeface tf;
    private OnConsumClickAndLongClickListener mConsumListener;
    private int[] colors = new int[]{R.color.bg_white, R.color.half_black};

    public MonthExpAdapter(Context context, ArrayList<DayConsum> parentList, ArrayList<Consum>[] childLists, OnConsumClickAndLongClickListener deleteConsum) {
        this.context = context;
        this.parentList = parentList;
        this.childLists = childLists;
        this.mConsumListener = deleteConsum;
        tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Semibold.ttf");
    }

    public MonthExpAdapter() {
        super();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return parentList.size() == 0 && childLists.length == 0;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childLists[groupPosition].size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childLists[groupPosition].get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_month_expandablelistview_parent, parent, false);
        TextView dayNumber = (TextView) view.findViewById(R.id.day_number);
        TextView dayConsum = (TextView) view.findViewById(R.id.tv_day_consum_number);
        TextView dayEarning = (TextView) view.findViewById(R.id.tv_day_earning_number);
        TextView dayConsumTitle = (TextView) view.findViewById(R.id.day_consum_title);
        TextView dayEarningTitle = (TextView) view.findViewById(R.id.day_earning_title);
        view.setBackgroundColor(context.getResources().getColor(R.color.white));
        dayNumber.setText("" + (groupPosition + 1));
        if (!(parentList.get(groupPosition) == null)) {
            DayConsum d = parentList.get(groupPosition);
            view.setBackgroundColor(context.getResources().getColor(R.color.parent_list_background));
            dayConsum.setVisibility(View.VISIBLE);
            dayEarning.setVisibility(View.VISIBLE);
            dayConsumTitle.setVisibility(View.VISIBLE);
            dayEarningTitle.setVisibility(View.VISIBLE);
            dayNumber.setTextColor(context.getResources().getColor(colors[0]));
            dayNumber.setTypeface(tf);
            dayConsum.setTextColor(context.getResources().getColor(colors[0]));
            dayEarning.setTextColor(context.getResources().getColor(colors[0]));
            dayConsumTitle.setTextColor(context.getResources().getColor(colors[0]));
            dayEarningTitle.setTextColor(context.getResources().getColor(colors[0]));
            dayConsum.setText(FloatUtil.floatToString("" + d.getDayConsum()));
            dayEarning.setText(FloatUtil.floatToString("" + d.getDayEarning()));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.bg_gray));
            dayNumber.setTextColor(context.getResources().getColor(colors[1]));
            dayNumber.setTypeface(tf);
            dayNumber.setAlpha((float) 0.1);
            dayConsum.setVisibility(View.GONE);
            dayEarning.setVisibility(View.GONE);
            dayConsumTitle.setVisibility(View.GONE);
            dayEarningTitle.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (childLists[groupPosition] == null) {
            return LayoutInflater.from(context).inflate(R.layout.listitem_month_expandablelistview_child, parent, false);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_month_expandablelistview_child, parent, false);
        }
        final Consum c = childLists[groupPosition].get(childPosition);
        TextView consumNumber = (TextView) convertView.findViewById(R.id.tv_consum_number);
        TextView consumLable = (TextView) convertView.findViewById(R.id.consum_lable);
        String number = c.getNumber().toString();
        consumNumber.setText(FloatUtil.floatToString(number));
        consumLable.setText(c.getLable());
        final int groupId = groupPosition;
        final int childId = childPosition;
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mConsumListener.onItemLongClickListener(groupId,childId,c);
                return true;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConsumListener.onItemClickListener(c);
            }
        });
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public interface OnConsumClickAndLongClickListener {
        public void onItemClickListener(Consum consum);

        public void onItemLongClickListener(int groupId,int childId,Consum consum);
    }
}
