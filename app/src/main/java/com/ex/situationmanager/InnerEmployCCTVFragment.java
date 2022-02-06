package com.ex.situationmanager;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import android.widget.Toast;
import android.widget.VideoView;

import static com.ex.situationmanager.InnerEmploySituationSelectFragment.fragCheck;


public class InnerEmployCCTVFragment extends Fragment implements View.OnClickListener {


    public static InnerEmployCCTVFragment instance;

    public InnerEmployCCTVFragment() {

    }

    static String cur_cctv = null;

    public static final InnerEmployCCTVFragment getInstance() {
        if (instance == null) {
            instance = new InnerEmployCCTVFragment();
        }
        return instance;
    }

    private static String cur_cctv_url = null;
    ImageView btnBack;

    VideoView cctv_videoView;

    private String start_cctv_url = "";
    private String end_cctv_url = "";

    public  String getCur_cctv_url() {
        return cur_cctv_url;
    }

    public void setCur_cctv_url(String cur_cctv_url) {
        this.cur_cctv_url = cur_cctv_url;
    }


    String TAG = "InnerEmployCCTVFragment";

    String LAND_TAG = "InnerEmployCCTVFragment_LAND";

    public String getStart_cctv_url() {
        return start_cctv_url;
    }

    public void setStart_cctv_url(String start_cctv_url) {
        this.start_cctv_url = start_cctv_url;
    }

    public String getEnd_cctv_url() {
        return end_cctv_url;
    }

    public void setEnd_cctv_url(String end_cctv_url) {
        this.end_cctv_url = end_cctv_url;
    }


    //getAruments로 값 받아오기

    View view;

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    LayoutInflater inflater;
    ViewGroup container;

    public static String innerStr ="";

    public static boolean curCcctvOnOff = false;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        InnerEmployActivity.onResumeProtocol=false;
        curCcctvOnOff = true;
        InnerEmployActivity.currentFragment = "InnerEmployCCTVFragment";
        fragCheck=false;
        this.setStart_cctv_url(getArguments().get("start_cctv_url").toString());
        this.setEnd_cctv_url(getArguments().get("end_cctv_url").toString());
        Log.println(Log.ASSERT, "", getArguments().get("start_cctv_url").toString());
        Log.println(Log.ASSERT, "", getArguments().get("end_cctv_url").toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            this.inflater = inflater;
            setInflater(inflater);

            this.container = container;
            if (InnerEmployActivity.isLanding != false) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
            if (getActivity().getResources().getConfiguration().orientation == 1) {
                InnerEmployActivity.currentFragment = "InnerEmployCCTVFragment";
                view =this.inflater.inflate(R.layout.inner_employee_cctv, this.container, false);
                InnerEmployActivity.currentFragment = TAG;
                TextView detailTitle = (TextView) view.findViewById(R.id.detailTitle);
                //
                TextView detailJeopbo = (TextView) view.findViewById(R.id.detailJeopbo);
                TextView detailType = (TextView) view.findViewById(R.id.detailType);
                TextView detailTime = (TextView) view.findViewById(R.id.detailTime);
                TextView detailState = (TextView) view.findViewById(R.id.detailState);
                TextView detailContent = (TextView) view.findViewById(R.id.detailContent);
                TextView detailJochi = (TextView) view.findViewById(R.id.detailJochi);
                detailTitle.setText(getArguments().get("detailTitle").toString());
                //
                detailJeopbo.setText(getArguments().get("detailJeopbo").toString());
                detailType.setText(getArguments().get("detailType").toString());
                detailTime.setText(getArguments().get("detailTime").toString());
                detailState.setText(getArguments().get("detailState").toString());
                detailContent.setText(getArguments().get("detailContent").toString());
                detailJochi.setText(getArguments().get("detailJochi").toString());
                btnBack = (ImageView) view.findViewById(R.id.btnBack);
                //
                //

                btnBack.setOnClickListener(this);


                TextView btn = (TextView) view.findViewById(R.id.testBtn);
                TextView btn1 = (TextView) view.findViewById(R.id.testBtn1);

                cctv_videoView = (VideoView) view.findViewById(R.id.cctv_videoView);

                Log.println(Log.ASSERT, "", cctv_videoView.getHeight() + "");



                btn.setOnClickListener(this);
                btn1.setOnClickListener(this);

                cctv_videoView.setVideoURI(Uri.parse(this.getStart_cctv_url()));
                cur_cctv = this.getStart_cctv_url();
                this.setCur_cctv_url(this.getStart_cctv_url());
                cctv_videoView.start();

            } else {
                InnerEmployActivity.currentFragment = "InnerEmployCCTVFragment";

                view = this.inflater.inflate(R.layout.inner_employee_cctv_land,this.container,false);
                cctv_videoView = view.findViewById(R.id.cctv_videoView);
                cctv_videoView.setVideoURI(Uri.parse(this.getCur_cctv_url()));
                cctv_videoView.start();

            }

        } catch (ParseException e) {
            Log.e("에러","예외");
        }
        return view;
    }


    public String getLaneName(String code) {
        if ("0001".equals(code))
            return "1차로";
        if ("0002".equals(code))
            return "2차로";
        if ("0003".equals(code))
            return "3차로";
        if ("0004".equals(code))
            return "4차로";
        if ("0005".equals(code))
            return "5차로";
        return "";
    }

    public String getReg_TypeName(String code) {

        if ("0001".equals(code))
            return "기타";
        if ("0002".equals(code))
            return "차단작업";
        if ("0003".equals(code))
            return "잡물";
        if ("0004".equals(code))
            return "고장차량";
        if ("0005".equals(code))
            return "사고발생";
        if ("0006".equals(code))
            return "지정체";
        if ("0007".equals(code))
            return "긴급견인";
        if ("0008".equals(code))
            return "동물";
        if ("0009".equals(code))
            return "노면(시설물)파손";
        if ("0010".equals(code))
            return "불량차량";
        if ("0011".equals(code))
            return "도로진입제한";
        if ("0012".equals(code))
            return "재난발생";
        if ("0013".equals(code))
            return "터널화재";

        return "";
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.testBtn:
                try {
                    InnerEmployActivity.isLanding = true;
                    cctv_videoView.setVideoURI(Uri.parse(this.getStart_cctv_url()));
                    cur_cctv = this.getStart_cctv_url();
                    setCur_cctv_url(this.getStart_cctv_url());
                    cctv_videoView.start();
                } catch (ParseException e) {
                    Log.e("에러","예외");
                }

                break;
            case R.id.testBtn1:
                try {
                    InnerEmployActivity.isLanding = true;
                    cctv_videoView.setVideoURI(Uri.parse(this.getEnd_cctv_url()));
                    cur_cctv = this.getEnd_cctv_url();
                    setCur_cctv_url(this.getEnd_cctv_url());
                    cctv_videoView.start();
                } catch (ParseException e) {
                    Log.e("에러","예외");
                }
                break;
            case R.id.btnBack:
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                cctv_videoView=null;

                curCcctvOnOff = false;
                Log.println(Log.ASSERT, TAG, "CCTV Fragment에서 누른 Activity이름:" + getActivity().getClass().getName());
                Log.println(Log.ASSERT, TAG, "CCTV Fragment에서 누른 getClass getName:" + this.getClass().getName());


                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                getActivity().getFragmentManager().popBackStack();

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.println(Log.ASSERT, TAG, "onDestroy");
        Log.i("CCTV ", "onDestroy");
        Log.println(Log.ASSERT, "", "CCTV:" + "onDestroy");

    }


}
