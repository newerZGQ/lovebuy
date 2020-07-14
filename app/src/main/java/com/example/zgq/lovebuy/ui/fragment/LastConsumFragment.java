package com.example.zgq.lovebuy.ui.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.data.ConsumContentProvider;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.ui.activity.MyConsumActivity;
import com.example.zgq.lovebuy.ui.activity.OtherConsumActivity;
import com.example.zgq.lovebuy.util.DBOperateTools;
import com.example.zgq.lovebuy.util.DateTools;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LastConsumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LastConsumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LastConsumFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Uri uri;
    private Context context;
    private TextView monthConsumText;
    private TextView monthEarningText;
    private TextView thisMonthTextChinese;
    private TextView thisMonthTextEnglish;
    private TextView thisDayConsumText;
    private TextView thisDayEarningText;
    private TextView thisDayText;
    private TextView thisYearText;
    private TextView myLastConsum;
    private TextView otherConsum;
    private LinearLayout myLaCoLay;
    private LinearLayout otherLaCoLay;

    private ArrayList<Consum> monthConsums;
    private ArrayList<Consum> thisDayConsums;
    private Consum myLast;
    private Consum otherLast;
    private double monthConsum;
    private double monthEarning;
    private double thisDayConsum;
    private double thisDayEarning;
    private String thisDateOfMonth;
    private String thisDateOfDay;
    private double[] dayConsums = new double[31];
    private String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    //BubbleChart
    private static final int BUBBLES_NUM = 32;
    private BubbleChartView bubbleChart;
    private BubbleChartData data;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = true;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LastConsumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LastConsumFragment newInstance(String param1, String param2) {
        LastConsumFragment fragment = new LastConsumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LastConsumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = getActivity();
        uri = Uri.parse(ConsumContentProvider.URI + "/consum");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment_last_consum, container, false);
        initview(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOtherLastConsum();
        new LastConsumFragmentData().execute((Void) null);
    }


    private void initview(View view) {
        thisYearText = (TextView) view.findViewById(R.id.tv_year_text);
        myLaCoLay = (LinearLayout) view.findViewById(R.id.lv_my_laCo);
        myLaCoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyConsumActivity.class));
            }
        });
        myLaCoLay.setLongClickable(true);
        myLaCoLay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createDialog();
                return true;
            }
        });
        otherLaCoLay = (LinearLayout) view.findViewById(R.id.other_laCo_lay);
        otherLaCoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OtherConsumActivity.class));
            }
        });
        myLastConsum = (TextView) view.findViewById(R.id.my_last_consum_number);
        otherConsum = (TextView) view.findViewById(R.id.tv_other_last_consum_number);
        bubbleChart = (BubbleChartView) view.findViewById(R.id.bubble_chart);
        monthConsumText = (TextView) view.findViewById(R.id.tv_month_consum_number);
        monthEarningText = (TextView) view.findViewById(R.id.tv_month_earning_number);
        thisMonthTextChinese = (TextView) view.findViewById(R.id.tv_month_number_chinese);
        thisMonthTextEnglish = (TextView) view.findViewById(R.id.tv_month_number_english);
        thisDayEarningText = (TextView) view.findViewById(R.id.tv_day_earning_number);
        thisDayConsumText = (TextView) view.findViewById(R.id.tv_day_consum_number);
        thisDayText = (TextView) view.findViewById(R.id.tv_day_number_chinese);
    }

    private void setViewContent() {
        myLastConsum.setText("" + myLast.getNumber());
        monthConsumText.setText("" + (int) monthConsum);
        monthEarningText.setText("" + (int)monthEarning);
        thisMonthTextChinese.setText(Integer.parseInt(thisDateOfMonth.substring(4, 6)) + "月");
        thisMonthTextEnglish.setText(months[Integer.parseInt(thisDateOfMonth.substring(4, 6)) - 1]);
        thisDayConsumText.setText("" + thisDayConsum);
        thisDayEarningText.setText("" + thisDayEarning);
        thisDayText.setText("" + Integer.parseInt(thisDateOfDay.substring(6, 8)));
        thisYearText.setText(""+thisDateOfMonth.substring(0,4));
        bubbleChart.setOnValueTouchListener(new ValueTouchListener());
        bubbleChart.setZoomEnabled(false);
        generateData();
    }

    private void generateData() {
        List<BubbleValue> values = new ArrayList<BubbleValue>();
        if (dayConsums.length == 0) return;
        for (int i = 1; i <= dayConsums.length; ++i) {
            BubbleValue value = new BubbleValue(i, (float) Math.random() * 100, (float) dayConsums[i - 1]);
            value.setColor(ChartUtils.pickColor());
            value.setShape(shape);
            values.add(value);
        }
        data = new BubbleChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        Axis axisX = new Axis().setHasLines(false);
        Axis axisY = new Axis().setHasLines(false);
        data.setAxisXBottom(null);
        data.setAxisYLeft(null);
        bubbleChart.setBubbleChartData(data);
        final Viewport v = new Viewport(bubbleChart.getMaximumViewport());
        v.top = 110;
        v.bottom = -5;
        v.left = 0.1f;
        v.right = 32;
        bubbleChart.setMaximumViewport(v);
        bubbleChart.setCurrentViewport(v);
    }

    private class ValueTouchListener implements BubbleChartOnValueSelectListener {

        @Override
        public void onValueSelected(int bubbleIndex, BubbleValue value) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.my_view_custom_toast, null);
            TextView number = (TextView) layout.findViewById(R.id.toast_number);
            TextView date = (TextView) layout.findViewById(R.id.toast_date);
            number.setText("" + (int) value.getZ());
            date.setText("" + (int) value.getX() + "  |");
            Toast toast = new Toast(getActivity().getApplicationContext());
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 250);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub
        }
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.yes_to_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        Animation alphaDown = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_down);
                        alphaDown.setFillAfter(true);
                        alphaDown.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ContentResolver contentResolver = context.getContentResolver();
                                contentResolver.delete(uri, "date=" + "'" + myLast.getDate() + "'", null);
                                Animation toBig = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_to_big);
                                toBig.setFillAfter(true);
                                myLaCoLay.startAnimation(toBig);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        myLaCoLay.startAnimation(alphaDown);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
    }

    public class LastConsumFragmentData extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            initData();
            thisDateOfMonth = DateTools.getDate(true);
            thisDateOfDay = DateTools.getDate(false).substring(0, 8);
            monthConsums = (ArrayList) DBOperateTools.getMonthConsum(context, thisDateOfMonth, uri);
            if (monthConsums == null || monthConsums.size() == 0){
            }
            if (monthConsums.isEmpty()) return null;
            int dayNumberOnNow = Integer.parseInt(monthConsums.get(monthConsums.size() - 1).getDate().substring(6, 8));
            dayConsums = new double[dayNumberOnNow];
            for (int i = 0; i < monthConsums.size(); i++) {
                Consum c = monthConsums.get(i);
                if (c.getProperty() == Consum.ISCOST) {
                    monthConsum += c.getNumber();
                    int whichDay = Integer.parseInt(c.getDate().substring(6, 8));
                    dayConsums[whichDay - 1] += c.getNumber();
                    if (c.getDate().substring(0, 8).equals(thisDateOfDay)) {
                        thisDayConsum += c.getNumber();
                    }
                }
                if (c.getProperty() == Consum.ISEARNING) {
                    monthEarning += c.getNumber();
                    if (c.getDate().substring(0, 8).equals(thisDateOfDay)) {
                        thisDayEarning += c.getNumber();
                    }
                }
                myLast = monthConsums.get(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setViewContent();
        }

        private void initData() {
            myLast = new Consum();
            monthConsums = null;
            monthConsum = 0;
            monthEarning = 0;
            thisDateOfMonth = null;
            thisDayConsum = 0;
            thisDayEarning = 0;
            dayConsums = new double[0];
        }
    }
    private void getOtherLastConsum(){
        BmobQuery<Consum> query = new BmobQuery<Consum>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userName", 0);
        final String associateUser = sharedPreferences.getString("associateUserName", "");
        query.addWhereEqualTo("user", associateUser);
        query.order("-date");
        query.setLimit(1);
        query.findObjects(context, new FindListener<Consum>() {
            @Override
            public void onSuccess(List<Consum> object) {
                // TODO Auto-generated method stub
                for(int i = 0;i<object.size();i++){
                    if (i==(object.size()-1)){
                        otherConsum.setText(""+object.get(i).getNumber());
                    }
                }
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public boolean onLastConsumFragmentInteraction(boolean dataIsChanged);
    }
}
