package com.example.zgq.lovebuy.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zgq.lovebuy.R;
import com.example.zgq.lovebuy.adapter.DesireListViewAdapter;
import com.example.zgq.lovebuy.data.DesireContentProvider;
import com.example.zgq.lovebuy.data.SharedPreferenceData;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;
import com.example.zgq.lovebuy.ui.activity.DetailDesireActivity;
import com.example.zgq.lovebuy.ui.myUI.CircleRefreshLayout;
import com.example.zgq.lovebuy.util.CheckNetWorkInfoUtil;
import com.example.zgq.lovebuy.util.DBOperateTools;
import com.example.zgq.lovebuy.util.SortUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DesireFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DesireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DesireFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;

    private TextView ourTabViewTv;
    private TextView myTabViewTv;
    private TextView otherTabViewTv;
    private ViewPager viewPager;

    private static int MYDESIRETAB = 1;
    private static int OTHERDESIRETAB = 2;
    private static int OURDESIRETAB = 3;

    private CircleRefreshLayout circleRefreshLayout;
    private View myDesireListViewLayout;
    private View otherDesireListViewLayout;
    private View ourDesireListViewLayout;
    private ListView myListViewInCicle;
    private ListView otherListViewInCicle;
    private ListView ourListViewInCicle;
    private ArrayList<View> viewPagerContainer = new ArrayList<>(3);

    private DesireListViewAdapter myAdapter;
    private DesireListViewAdapter otherAdapter;
    private DesireListViewAdapter ourAdapter;
    private ArrayList<MyConsumDesire> myDesireList;
    private ArrayList<MyConsumDesire> otherDesireList;
    private ArrayList<MyConsumDesire> ourDesireList;
    private Uri uri;

    private boolean dataIsChanged = false;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DesireFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DesireFragment newInstance(String param1, String param2) {
        DesireFragment fragment = new DesireFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DesireFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment_desire, container, false);
        initView(view);
        getMyDesireList();
        setMyListViewContent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncDesireData(getActivity()).execute((Void) null);
    }

    public void initView(View view){
        viewPager = (ViewPager) view.findViewById(R.id.vp_desire_listview);
        myDesireListViewLayout = LayoutInflater.from(context).inflate(R.layout.activity_home_fragment_desire_pageritem_my_desire, null);
        otherDesireListViewLayout = LayoutInflater.from(context).inflate(R.layout.activity_home_fragment_desire_pageritem_other_desire, null);
        ourDesireListViewLayout = LayoutInflater.from(context).inflate(R.layout.activity_home_fragment_desire_pageritem_our_desire, null);
        myTabViewTv = (TextView) view.findViewById(R.id.tv_tab_my_desire);
        myTabViewTv.setSelected(true);
        myTabViewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTabViewTv.setSelected(true);
                ourTabViewTv.setSelected(false);
                otherTabViewTv.setSelected(false);
                changeViewPagerItem(MYDESIRETAB);
            }
        });
        otherTabViewTv = (TextView) view.findViewById(R.id.tv_tab_other_desire);
        otherTabViewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherTabViewTv.setSelected(true);
                ourTabViewTv.setSelected(false);
                myTabViewTv.setSelected(false);
                changeViewPagerItem(OTHERDESIRETAB);
            }
        });
        ourTabViewTv = (TextView) view.findViewById(R.id.tv_tab_our_desire);
        ourTabViewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ourTabViewTv.setSelected(true);
                myTabViewTv.setSelected(false);
                otherTabViewTv.setSelected(false);
                changeViewPagerItem(OURDESIRETAB);
            }
        });
        myListViewInCicle = (ListView) myDesireListViewLayout.findViewById(R.id.lVi_my_desire);
        otherListViewInCicle = (ListView) otherDesireListViewLayout.findViewById(R.id.lVi_other_desire);
        ourListViewInCicle = (ListView) ourDesireListViewLayout.findViewById(R.id.lVi_our_desire);
        myListViewInCicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailDesireActivity.class);
                intent.putExtra("desire", myDesireList.get(position));
                startActivity(intent);
            }
        });
        initViewPager();
    }
    private void initViewPager() {
        viewPagerContainer.add(myDesireListViewLayout);
        viewPagerContainer.add(otherDesireListViewLayout);
        viewPagerContainer.add(ourDesireListViewLayout);
        viewPager.setAdapter(new PagerAdapter() {
            //number of views in viewpager
            @Override
            public int getCount() {
                return viewPagerContainer.size();
            }

            //destroy current view when sliding
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewPagerContainer.get(position));
            }

            //the generate view when sliding
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewPagerContainer.get(position));
                return viewPagerContainer.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return null;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        myTabViewTv.setSelected(true);
                        otherTabViewTv.setSelected(false);
                        ourTabViewTv.setSelected(false);
                        break;
                    case 1:
                        myTabViewTv.setSelected(false);
                        otherTabViewTv.setSelected(true);
                        ourTabViewTv.setSelected(false);
                        break;
                    case 2:
                        myTabViewTv.setSelected(false);
                        otherTabViewTv.setSelected(false);
                        ourTabViewTv.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void changeViewPagerItem(int flag) {
        switch (flag) {
            case 1:
                viewPager.setCurrentItem(0);
                break;
            case 2:
                viewPager.setCurrentItem(1);
                break;
            case 3:
                viewPager.setCurrentItem(2);
                break;
        }
    }
    public void setViewContent(){
        setMyListViewContent();
        setOtherListViewContent();
        setOurListViewContent();
    }
    public void setMyListViewContent(){
        myAdapter = new DesireListViewAdapter(getActivity(), myDesireList);
        myListViewInCicle.setAdapter(myAdapter);
    }
    public void setOtherListViewContent(){
        otherAdapter = new DesireListViewAdapter(getActivity(), otherDesireList);
        otherListViewInCicle.setAdapter(otherAdapter);
    }
    public void setOurListViewContent(){
        ourAdapter = new DesireListViewAdapter(getActivity(), ourDesireList);
        ourListViewInCicle.setAdapter(ourAdapter);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        public void onDeaireFragmentInteraction(Uri uri);
    }

    public class AsyncDesireData extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        public AsyncDesireData(Context context) {
            this.context = context;
        }
        @Override

        protected Boolean doInBackground(Void... params) {
            getMyDesireList();
            if (CheckNetWorkInfoUtil.isNetWorkAvailable(context)) {
                getOtherAndOurDesireList();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }
    private void getMyDesireList() {
        uri = Uri.parse(DesireContentProvider.URI + "/desire");
        myDesireList = (ArrayList) DBOperateTools.getMyDesireList(getActivity(), uri);
        SortUtil.sortDesire(myDesireList);
    }

    private void getOtherAndOurDesireList() {
        otherDesireList = new ArrayList<>();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userName", 0);
        final String associateUser = SharedPreferenceData.getSharedPreferenceDataAssociateUserName(context);
        if (!associateUser.equals(getString(R.string.have_not_associat_with_other))) {
            BmobQuery<MyConsumDesire> queryDesire = new BmobQuery<MyConsumDesire>();
            queryDesire.addWhereEqualTo("user", associateUser);
            queryDesire.findObjects(getActivity(), new FindListener<MyConsumDesire>() {
                @Override
                public void onSuccess(List<MyConsumDesire> object) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < object.size(); i++) {
                        Log.d("--------->>", object.get(i).getDate());
                        MyConsumDesire desire = new MyConsumDesire(new Double(object.get(i).getNumber()), object.get(i).getDetail(), object.get(i).getDate(), new Integer(object.get(i).getStatus()));
                        desire.setUser(associateUser);
                        otherDesireList.add(desire);
                    }
                    SortUtil.sortDesire(otherDesireList);
                    getOurDesireList();
                    setViewContent();
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    getOurDesireList();
                    setViewContent();
                }
            });
        }
    }

    private void getOurDesireList() {
        ourDesireList = new ArrayList<>();
        ourDesireList = SortUtil.mergeDesire(myDesireList, otherDesireList);
    }
}
