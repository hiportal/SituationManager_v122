package com.ex.situationmanager;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.ex.situationmanager.BaseActivity;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Image;

import java.io.IOException;

public class PatrolCCtvActivity extends BaseActivity implements View.OnClickListener {

    String TAG = "PatrolCCtvActivity";
    private String start_cctv_url="";
    private  String end_cctv_url="";

    TextView btn = null;
    TextView btn1 =  null;
    TextView detailTitle= null;


    //
    TextView detailJeopbo= null;
    TextView detailType= null;
    TextView detailTime= null;
    TextView detailState= null;
    TextView detailContent= null;
    TextView detailJochi= null;

    ImageView btnBack=null;
    ImageView btnUserInfo=null;
    VideoView cctv_videoView = null;

    String thisVideoViewURI="";

    @Override
    public void onCreate(Bundle bunble){
        super.onCreate(bunble);
        setContentView(R.layout.patrol_cctv);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        this.setStart_cctv_url(getIntent().getStringExtra("start_cctv"));
        this.setEnd_cctv_url(getIntent().getStringExtra("end_cctv"));

         btn = (TextView)findViewById(R.id.testBtn);
         btn1 = (TextView)findViewById(R.id.testBtn1);
         detailTitle=(TextView)findViewById(R.id.detailTitle);
        //
         detailJeopbo=(TextView)findViewById(R.id.detailJeopbo);
         detailType=(TextView)findViewById(R.id.detailType);
         detailTime=(TextView)findViewById(R.id.detailTime);
         detailState=(TextView)findViewById(R.id.detailState);
         detailContent=(TextView)findViewById(R.id.detailContent);
         detailJochi=(TextView)findViewById(R.id.detailJochi);
        btnUserInfo=(ImageView)findViewById(R.id.btnUserInfo);
         btnBack=(ImageView)findViewById(R.id.btnBack);
         cctv_videoView = (VideoView)findViewById(R.id.cctv_videoView);

        detailTitle.setText(getIntent().getStringExtra(("detailTitle").toString()));
        //
        detailJeopbo.setText(getIntent().getStringExtra(("detailJeopbo").toString()));
        detailType.setText(getIntent().getStringExtra(("detailType").toString()));
        detailTime.setText(getIntent().getStringExtra(("detailTime").toString()));
        detailState.setText(getIntent().getStringExtra(("detailState").toString()));
        detailContent.setText(getIntent().getStringExtra(("detailContent").toString()));
        detailJochi.setText(getIntent().getStringExtra(("detailJochi").toString()));
        //

        btnBack.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        try{
            cctv_videoView.setVideoURI(Uri.parse(this.getStart_cctv_url()));
            thisVideoViewURI=this.getStart_cctv_url();
            cctv_videoView.start();
        }catch (ParseException e){
            //e.printStackTrace();
            Log.e("에러","예외");
        }
    }

    @Override
    public void onClick(View v){
        super.onClick(v);
        switch (v.getId()){
            case  R.id.testBtn:
                cctv_videoView.setVideoURI(Uri.parse(this.getStart_cctv_url()));
                thisVideoViewURI=this.getStart_cctv_url();
                cctv_videoView.start();
                break;

            case R.id.testBtn1:
                cctv_videoView.setVideoURI(Uri.parse(this.getEnd_cctv_url()));
                thisVideoViewURI=this.getEnd_cctv_url();
                cctv_videoView.start();
                break;
            case R.id.btnUserInfo:

                break;
            case R.id.btnBack:
                finish();
                break;

        }

    }

    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {

    }

    @Override
    public void onResume(){
        super.onResume();
        if(getResources().getConfiguration().orientation != 1){

            setContentView(R.layout.inner_employee_cctv_land);
            VideoView cc = findViewById(R.id.cctv_videoView);
            cc.setVideoURI(Uri.parse(thisVideoViewURI));
            cc.start();
        }
    }

    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {

    }


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
}
