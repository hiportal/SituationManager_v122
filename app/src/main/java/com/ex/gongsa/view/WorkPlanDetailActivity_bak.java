/*
package com.ex.gongsa.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.situationmanager.R;
import com.ex.situationmanager.service.SituationBroadcast;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.ex.gongsa.Common.nullCheck;

public class WorkPlanDetailActivity_bak extends BaseActivity implements View.OnClickListener {

    Intent intent;
    ViewGroup gotoMainBottom;
    TextView gotoMainTv, bonbuTextView, jisaTextView;
    TextView noSunTextView;
    TextView gosundatTextView;
    TextView startGuganTextView;
    TextView endGuganTextView;
    TextView gongsaContent;
    TextView startDateTextView;
    TextView endDateTextView;
    TextView jackupinwonetTextView;
    TextView directionTextView;
    TextView roadlimitTextView;
    TextView workTypeTextView;
    TextView workApprovalCheck;
    TextView gamdokTextView;
    TextView startHourTv;
    TextView startMinuteTv;
    TextView endHourTv;
    TextView endMinuteTv;

    ImageView gongsaPrev;
    ViewGroup ApprovalCheck;
    SituationBroadcast broadcast;
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;


    int chaDanChaoCnt = 0;
    String chdanBangsik = "";

    @Override
    public void onCreate(Bundle savedInsataceState) {
        super.onCreate(savedInsataceState);
        setContentView(R.layout.z_work_plan_detail);

        intent = getIntent();


        Log.i("vvb", intent.getStringExtra("WorkPlanJsonValue"));


        gotoMainBottom = (ViewGroup) findViewById(R.id.gotoMainBottom);
        ApprovalCheck = (ViewGroup) findViewById(R.id.ApprovalCheck);

        gotoMainTv = (TextView) findViewById(R.id.gotoMainTv);
        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);
        jisaTextView = (TextView) findViewById(R.id.jisaTextView);
        noSunTextView = (TextView) findViewById(R.id.noSunTextView);
        gosundatTextView = (TextView) findViewById(R.id.gosundatTextView);
        startGuganTextView = (TextView) findViewById(R.id.startGuganTextView);
        endGuganTextView = (TextView) findViewById(R.id.endGuganTextView);
        gongsaContent = (TextView) findViewById(R.id.gongsaContent);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        jackupinwonetTextView = (TextView) findViewById(R.id.jackupinwonetTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);
        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView);
        workApprovalCheck = (TextView) findViewById(R.id.workApprovalCheck);
        gamdokTextView = (TextView) findViewById(R.id.gamdokTextView);
        startHourTv =(TextView)findViewById(R.id.startHourTv);
        startMinuteTv = (TextView)findViewById(R.id.startMinuteTv);
        endHourTv = (TextView)findViewById(R.id.endHourTv);
        endMinuteTv = (TextView)findViewById(R.id.endMinuteTv);

        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);

        */
/*ApprovalCheck.setOnClickListener(this);*//*

        gongsaPrev.setOnClickListener(this);
        gotoMainBottom.setOnClickListener(this);
      //  roadlimitTextView.setOnClickListener(this);
        try {

            //nullCheck( ;
            jsonObject = new JSONObject(intent.getStringExtra("WorkPlanJsonValue"));

            try {

                if (jsonObject.get("trfcLimtStatCd").toString().equals("01")) {
                    workApprovalCheck.setText("대기");
                } else if (jsonObject.get("trfcLimtStatCd").toString().equals("12")) {
                    workApprovalCheck.setText("승인");
                } else if (jsonObject.get("trfcLimtStatCd").toString().equals("13")) {
                    workApprovalCheck.setText("반려");
                } else if (jsonObject.get("trfcLimtStatCd").toString().equals("14")) {
                    workApprovalCheck.setText("대기안됨");
                } else if (jsonObject.get("trfcLimtStatCd").toString().equals("11")) {
                    workApprovalCheck.setText("기안");
                }
            }catch (Exception e){
                workApprovalCheck.setText("처리안됨");
            }
            if(jsonObject.get("userCheck").equals("2")){
                new CustomDialog(WorkPlanDetailActivity_bak.this, R.layout.z_user_dialog, R.id.userCheckTextView);
            }
            bonbuTextView.setText(nullCheck(jsonObject.get("hdqrNm").toString()));
            jisaTextView.setText(nullCheck(jsonObject.get("mtnofNm").toString()));
            noSunTextView.setText(nullCheck(jsonObject.get("routeNm").toString()));
            try{
                gongsaContent.setText(nullCheck(jsonObject.get("cnstnCtnt").toString()));
            }catch (Exception e){
                gongsaContent.setText("");
            }
            try{
                gosundatTextView.setText(nullCheck(jsonObject.get("gosundaeNo").toString()));
            }catch (Exception e){
                gosundatTextView.setText("");
            }
            startGuganTextView.setText(nullCheck(jsonObject.get("strtBlcPntVal").toString()));
            endGuganTextView.setText(nullCheck(jsonObject.get("endBlcPntVal").toString()));
            try{
                startDateTextView.setText(nullCheck(jsonObject.get("blcStrtDttm").toString()));
            }catch (Exception e){
                startDateTextView.setText("");
            }

            try{
                endDateTextView.setText(nullCheck(jsonObject.get("blcRevocDttm").toString()));
            }catch (Exception e){
                endDateTextView.setText("");
            }
            try{
                jackupinwonetTextView.setText(nullCheck(jsonObject.get("cmmdNm").toString()));
            }catch (Exception e){
                jackupinwonetTextView.setText("");
            }

            startHourTv.setText(nullCheck(jsonObject.get("startGongsaHour").toString()));
            startMinuteTv.setText(nullCheck(jsonObject.get("startGongsaMinute").toString()));
            endHourTv.setText(nullCheck(jsonObject.get("endGongsaHour").toString()));
            endMinuteTv.setText(nullCheck(jsonObject.get("endGongsaMinute").toString()));
            try {
                gamdokTextView.setText(nullCheck(jsonObject.get("gamdokNameEMNM").toString()));
            } catch (Exception e) {
                gamdokTextView.setText("");
            }
  */
/*   if (jsonObject.get("drctClssCd").toString().equalsIgnoreCase("E")) {
                directionTextView.setText("종점방향");
            } else if (jsonObject.get("drctClssCd").toString().equalsIgnoreCase("O")) {
                directionTextView.setText("양방향");
            } else {
                directionTextView.setText("시점방향");
            }*//*

            if(!jsonObject.get("drctClssNm").toString().equals("양방향")){
                directionTextView.setText(jsonObject.get("drctClssNm").toString()+"방향");
            }else{
                directionTextView.setText(jsonObject.get("drctClssNm").toString());
            }
            try{
                jackupinwonetTextView.setText(nullCheck(jsonObject.get("cmmdNm").toString()));
            }catch (Exception e){
                jackupinwonetTextView.setText("");
            }



            String[] chdanListSplit = jsonObject.get("trfcLimtCrgwClssCd").toString().split(",");
            Log.i("그룹바이 결과", jsonObject.get("trfcLimtCrgwClssCd").toString());
            for (int i = 0; i < chdanListSplit.length; i++) {
                if (chdanListSplit[i].equals("01")) {
                    chaDanChaoCnt++;
                } else if (chdanListSplit[i].equals("02")) {
                    chaDanChaoCnt++;
                } else if (chdanListSplit[i].equals("03")) {
                    chaDanChaoCnt++;
                } else if (chdanListSplit[i].equals("04")) {
                    chaDanChaoCnt++;
                } else if (chdanListSplit[i].equals("05")) {
                    chaDanChaoCnt++;
                }


                if (chdanListSplit[i].equals("01")) {
                    chdanBangsik += ",1차로";
                } else if (chdanListSplit[i].equals("02")) {
                    chdanBangsik += ",2차로";
                } else if (chdanListSplit[i].equals("03")) {
                    chdanBangsik += ",3차로";
                } else if (chdanListSplit[i].equals("04")) {
                    chdanBangsik += ",4차로";
                } else if (chdanListSplit[i].equals("05")) {
                    chdanBangsik += ",5차로";
                } else if (chdanListSplit[i].equals("07")) {
                    chdanBangsik += ",진입램프";
                } else if (chdanListSplit[i].equals("08")) {
                    chdanBangsik += ",진출램프";
                } else if (chdanListSplit[i].equals("09")) {
                    chdanBangsik += ",교량하부";
                } else if (chdanListSplit[i].equals("10")) {
                    chdanBangsik += ",법면";
                } else if (chdanListSplit[i].equals("11")) {
                    chdanBangsik += ",회차로";
                } else if (chdanListSplit[i].equals("20")) {
                    chdanBangsik += ",이동차단";
                } else if (chdanListSplit[i].equals("30")) {
                    chdanBangsik += ",갓길";
                }

            }
            roadlimitTextView.setText(chdanBangsik.substring(1));
      */
/*      if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("01")){
                roadlimitTextView.setText("1차로");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("02")){
                roadlimitTextView.setText("2차로");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("03")){
                roadlimitTextView.setText("3차로");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("04")){
                roadlimitTextView.setText("4차로");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("05")){
                roadlimitTextView.setText("5차로");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("07")){
                roadlimitTextView.setText("진입램프");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("08")){
                roadlimitTextView.setText("진출램프");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("09")){
                roadlimitTextView.setText("교량하부");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("10")){
                roadlimitTextView.setText("법면");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("11")){
                roadlimitTextView.setText("회차로");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("20")){
                roadlimitTextView.setText("이동차단");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("30")){

            }*//*


            workTypeTextView.setText(jsonObject.get("workType").toString());



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gotoMainBottom:

                intent = new Intent(WorkPlanDetailActivity_bak.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.ApprovalCheck:

                break;
            case R.id.roadlimitTextView:
                try {
                    new CustomDialog(WorkPlanDetailActivity_bak.this, R.layout.z_road_chadan_detail, R.id.JaupclickOk, chdanBangsik.substring(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

        }


    }


    @Override
    public String onActionPost(String primitive, String date) {

        return null;
    }

}
*/
