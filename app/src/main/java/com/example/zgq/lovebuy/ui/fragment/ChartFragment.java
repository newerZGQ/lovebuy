package com.example.zgq.lovebuy.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.data.ConsumContentProvider;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.ui.myUI.MyRatingBar;
import com.example.zgq.lovebuy.util.DBOperateTools;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private Calendar calendar;
    private String currentMonth;
    private HashMap<String, Double> pieChartDataHashMap;
    private Uri uri;
    private boolean pieChartDataIsEmpty;

    private OnFragmentInteractionListener mListener;
    private Button lastMonthBtn;
    private Button nextMonthBtn;
    private TextView monthNumberTv;
    private TextView noConsumBackgroundTv;
    private TextView thisYearTv;
    private TextView maxLableTv;
    private TextView maxLableNumberTv;
    private MyRatingBar happinessRatingBar;

    private PieChart pieChart;
    private ArrayList<Entry> yVals1 = new ArrayList<Entry>();
    private ArrayList<String> xVals = new ArrayList<String>();
    private PieDataSet dataSet;
    private PieData data;
    private float monthHappiness = 0;
    private double[] maxNumbers = new double[6];
    private int numberOfLablesToShow;
    private Typeface tf;
    private boolean dataIsEmpty;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChartFragment() {
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
        initDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home_fragment_chart, container, false);
        initView(view);
        setViewContent();
        return view;
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
    public void onResume() {
        super.onResume();
        onDataChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initDate() {
        numberOfLablesToShow = 6;
        uri = Uri.parse(ConsumContentProvider.URI + "/consum");
        calendar = Calendar.getInstance();
        getStringMonthDate();
    }

    private void initView(View view) {
        maxLableTv = (TextView) view.findViewById(R.id.tv_max_lable);
        maxLableNumberTv = (TextView) view.findViewById(R.id.tv_lable_month_consum);
        thisYearTv = (TextView) view.findViewById(R.id.tv_year_text);
        lastMonthBtn = (Button) view.findViewById(R.id.btn_tab_last_month);
        nextMonthBtn = (Button) view.findViewById(R.id.btn_tab_next_month);
        monthNumberTv = (TextView) view.findViewById(R.id.tv_text_month_number);
        noConsumBackgroundTv = (TextView) view.findViewById(R.id.no_consum_background);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        happinessRatingBar = (MyRatingBar) view.findViewById(R.id.month_happiness_ratingbar);
        initPieChart();
    }
    private void initPieChart(){
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
        pieChart.setCenterTextTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf"));
        pieChart.setCenterText(getString(R.string.consum_distribution));
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(38f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
    }
    private void setViewContent() {
        setYearAndMonthText();
        lastMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalenderTolastMonth();
                setYearAndMonthText();
                onDataChanged();
            }
        });
        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendarToNextMonth();
                setYearAndMonthText();
                onDataChanged();
            }
        });
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                setMaxLablePart(h.getXIndex());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        onDataChanged();
    }
    private void setMaxLablePart(int h){
        if (data.getYValCount() == 0){
            maxLableNumberTv.setText("0");
            maxLableTv.setText(getString(R.string.no_records));
        }else {
            maxLableNumberTv.setText("" + dataSet.getEntryForIndex(h).getVal());
            maxLableTv.setText(data.getXVals().get(h));
        }
    }
    private void setMonthHappinessPart(float stars){
        happinessRatingBar.setStar(stars);
    }
    private void setCalenderTolastMonth() {
        calendar.add(Calendar.MONTH, -1);
        getStringMonthDate();
    }

    private void setCalendarToNextMonth() {
        calendar.add(Calendar.MONTH, +1);
        getStringMonthDate();
    }

    private void setYearAndMonthText() {
        thisYearTv.setText("" + calendar.get(Calendar.YEAR));
        monthNumberTv.setText("" + (calendar.get(Calendar.MONTH) + 1));
    }

    private void getStringMonthDate() {
        currentMonth = "" + calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month >= 0 && month <= 8) {
            currentMonth = currentMonth + "0" + (month + 1);
            return;
        } else if (month == 9) {
            currentMonth = currentMonth + (month + 1);
            return;
        } else {
            currentMonth = currentMonth + month;
        }
    }

    private void onDataChanged() {
        changePieChartToNewData();
    }

    private void changePieChartToNewData() {
        new GetPieChartData().execute((Void) null);
    }

    private void invalidatePieChart() {
        if (data.getYValCount() == 0){
            pieChart.setCenterText(getString(R.string.no_records));
        }else {
            pieChart.setCenterText(getString(R.string.consum_distribution));
        }
        pieChart.setData(data);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.highlightValues(null);
        Legend l = pieChart.getLegend();
        l.getColors();
        l.setEnabled(false);
        pieChart.invalidate();
    }

    private class GetPieChartData extends AsyncTask {
        private ArrayList<Consum> myMonthConsums;
        @Override
        protected Object doInBackground(Object[] params) {
            initPieDataData();
            initMonthHappiness();
            pieChartDataHashMap = new HashMap<String, Double>();
            myMonthConsums = (ArrayList) DBOperateTools.getMonthConsum(context, currentMonth, uri);
            if (myMonthConsums == null) {
                dataIsEmpty = true;
                return null;
            }
            int monthConsumsNumber = 0;
            for (int i = 0; i < myMonthConsums.size(); i++) {
                Consum c = myMonthConsums.get(i);
                monthHappiness += c.getHappiness();
                if (c.getProperty() == Consum.ISCOST) {
                    monthConsumsNumber++;
                    if (!pieChartDataHashMap.containsKey(c.getLable())) {
                        pieChartDataHashMap.put(c.getLable(), c.getNumber());
                    } else {
                        double sum = pieChartDataHashMap.get(c.getLable()) + c.getNumber();
                        pieChartDataHashMap.put(c.getLable(), sum);
                    }
                }
            }
            if (monthConsumsNumber!=0)
            monthHappiness = (float)Math.ceil(monthHappiness/monthConsumsNumber);
            ArrayList<Map.Entry<String,Double>> pieDataList = sortHashMap(pieChartDataHashMap);
            int N = pieDataList.size();
            double othersSum = 0;
            int j = 0;
            for (int i = N-1;i>=0;i--){
                if (i>(N-numberOfLablesToShow)){
                    yVals1.add(new Entry(((float)(double)pieDataList.get(i).getValue()), j++));
                    xVals.add(pieDataList.get(i).getKey().toString());
                    continue;
                }else{
                    othersSum += (double)pieDataList.get(i).getValue();
                    continue;
                }
            }
            if (othersSum!=0) {
                yVals1.add(new Entry((float) othersSum, j));
                xVals.add(getString(R.string.other_part_of_consum));
            }
            dataSet = new PieDataSet(yVals1, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);
            data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(tf);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (dataIsEmpty){
                pieChart.setCenterText(getString(R.string.no_consum));
            }else{
                pieChart.setCenterText(getString(R.string.consum_distribution));
            }
            invalidatePieChart();
            setMaxLablePart(0);
            setMonthHappinessPart(monthHappiness);
        }

        private void initPieDataData() {
            pieChartDataHashMap = null;
            yVals1 = new ArrayList<Entry>();
            xVals = new ArrayList<String>();
        }
        private void initMonthHappiness(){
            monthHappiness = 0;
        }
        private ArrayList<Map.Entry<String,Double>> sortHashMap(HashMap map){
            ArrayList<Map.Entry<String, Double>> infoIds = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Double>>() {
                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            return infoIds;
        }
    }



    //横向BarChart 暂时不使用
//    private class GetHorizontalBarChartData extends AsyncTask {
//        private ArrayList<Consum>[] myConsumsListArray;
//        private int daysNumberOfMonth;
//        private ArrayList<Consum> myMonthConsums;
//        @Override
//        protected Object doInBackground(Object[] params) {
//            initHorizontalBarChartData();
//            daysNumberOfMonth = DateTools.getMonthLastDay(Integer.parseInt(currentMonth.substring(0, 4)), Integer.parseInt(currentMonth.substring(4, 6)));
//            myConsumsListArray = new ArrayList[daysNumberOfMonth];
//            myMonthConsums = (ArrayList) DBOperateTools.getMonthConsum(context, currentMonth, uri);
//            if (myMonthConsums.isEmpty()){
//                horizontalBarChartDataIsEmpty = true;
//                return null;
//            }
//            for (int i = 0; i < daysNumberOfMonth; i++) {
//                myConsumsListArray[i] = new ArrayList<>();
//            }
//            for (int i = 0; i < myMonthConsums.size(); i++) {
//                Consum c = myMonthConsums.get(i);
//                int whichDay = Integer.parseInt(myMonthConsums.get(i).getDate().substring(6, 8));
//                myConsumsListArray[whichDay - 1].add(c);
//            }
//            for (int j = myConsumsListArray.length-1; j >=0; j--) {
//                if (!myConsumsListArray[j].isEmpty()) {
//                    horizontalBarChartXVals.add("" + (j+1) + "号");
//                    horizontalBarChartYVals.add(new BarEntry((float) getDayConsumFromConsumList(myConsumsListArray[j]).getDayConsum(), j));
//                }
////                } else horizontalBarChartYVals.add(new BarEntry(0, j));
//            }
//            BarDataSet set1 = new BarDataSet(horizontalBarChartYVals, "");
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//            barData = new BarData(horizontalBarChartXVals, dataSets);
//            barData.setValueTextSize(10f);
//            horizontalBarChartDataIsEmpty = false;
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            if (horizontalBarChartDataIsEmpty){
//                horizontalBarChart.setVisibility(View.INVISIBLE);
//            }else {
//                horizontalBarChart.setVisibility(View.VISIBLE);
//            }
//        }
//
//        private void initHorizontalBarChartData() {
//            myMonthConsums= new ArrayList<>();
//            horizontalBarChartXVals = new ArrayList<>();
//            horizontalBarChartYVals = new ArrayList<BarEntry>();
//        }
//
//        private DayConsum getDayConsumFromConsumList(ArrayList<Consum> list) {
//            double dayconsum = 0, dayEarning = 0;
//            String date = list.get(0).getDate();
//            for (Consum c : list) {
//                if (c.getProperty() == Consum.ISCOST) {
//                    dayconsum += c.getNumber();
//                }
//                if (c.getProperty() == Consum.ISEARNING) {
//                    dayEarning += c.getNumber();
//                }
//            }
//            return new DayConsum(dayconsum, dayEarning, date);
//        }
//    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onChartFragmentInteraction(Uri uri);
    }

}
