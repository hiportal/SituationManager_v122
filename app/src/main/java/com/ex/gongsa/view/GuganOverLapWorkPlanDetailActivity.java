package com.ex.gongsa.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.R;
import com.ex.situationmanager.service.SituationBroadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ex.gongsa.Common.nullCheck;

public class GuganOverLapWorkPlanDetailActivity extends BaseActivity {


    Intent intent;

    //ViewGroup gotoMainBottom;
    TextView gotoMainTv, bonbu_jisa_Tv,guganTv;
    // TextView noSunTextView;
    TextView gosundatTextView;

    /* TextView startGuganTextView;
     TextView endGuganTextView;*/
    TextView gongsaContent;
    TextView startDateTextView;
    TextView endDateTextView;
    TextView jackupinwonetTextView;
    //  TextView directionTextView;
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

    LinearLayout callLayout;
    LinearLayout goto_list_Li;
    int chaDanChaoCnt = 0;
    String chdanBangsik = "";
    String result;

    String workerPhonNo="";
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.z_gugan_goverlap_work_detail);
        callLayout = (LinearLayout)findViewById(R.id.callLayout);
        //gotoMainBottom = (ViewGroup) findViewById(R.id.gotoMainBottom);
        // ApprovalCheck = (ViewGroup) findViewById(R.id.ApprovalCheck);

        gotoMainTv = (TextView) findViewById(R.id.gotoMainTv);
        guganTv = (TextView) findViewById(R.id.guganTv);
        bonbu_jisa_Tv = (TextView) findViewById(R.id.bonbu_jisa_Tv);
        //     noSunTextView = (TextView) findViewById(R.id.noSunTextView);
        gosundatTextView = (TextView) findViewById(R.id.gosundatTextView);
    /*    startGuganTextView = (TextView) findViewById(R.id.startGuganTextView);
        endGuganTextView = (TextView) findViewById(R.id.endGuganTextView);*/
        gongsaContent = (TextView) findViewById(R.id.gongsaContent);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        jackupinwonetTextView = (TextView) findViewById(R.id.jackupinwonetTextView);
        // directionTextView = (TextView) findViewById(R.id.directionTextView);
        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);
        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView);
        workApprovalCheck = (TextView) findViewById(R.id.workApprovalCheck);
        gamdokTextView = (TextView) findViewById(R.id.gamdokTextView);
        startHourTv =(TextView)findViewById(R.id.startHourTv);
        startMinuteTv = (TextView)findViewById(R.id.startMinuteTv);
        endHourTv = (TextView)findViewById(R.id.endHourTv);
        endMinuteTv = (TextView)findViewById(R.id.endMinuteTv);

        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);

        goto_list_Li =(LinearLayout)findViewById(R.id.goto_list_Li);
        /*ApprovalCheck.setOnClickListener(this);*/
        gongsaPrev.setOnClickListener(this);
        goto_list_Li.setOnClickListener(this);
       // gotoMainBottom.setOnClickListener(this);
        intent= getIntent();

        result = intent.getStringExtra("guganWorkDetail");

        callLayout.setOnClickListener(this);


        try {

            //nullCheck( ;
            jsonObject = new JSONObject(result);
            Log.i("resuldsfdsdf",result);
            try{
                String temp=  jsonObject.get("cstrCpprPrchTelno").toString();
                Log.i("????????????",temp);
                try{
                    String[] tempArray = temp.split("-");
                    for(int i =0 ; i<tempArray.length;i++){
                        workerPhonNo+=tempArray[i];
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    workerPhonNo=temp;
                }
            }catch (JSONException|NullPointerException e){
                workerPhonNo="";
            }

             /*   try {

                    if (jsonObject.get("trfcLimtStatCd").toString().equals("01")) {
                        workApprovalCheck.setText("??????");
                    } else if (jsonObject.get("trfcLimtStatCd").toString().equals("12")) {
                        workApprovalCheck.setText("??????");
                    } else if (jsonObject.get("trfcLimtStatCd").toString().equals("13")) {
                        workApprovalCheck.setText("??????");
                    } else if (jsonObject.get("trfcLimtStatCd").toString().equals("14")) {
                        workApprovalCheck.setText("????????????");
                    } else if (jsonObject.get("trfcLimtStatCd").toString().equals("11")) {
                        workApprovalCheck.setText("??????");
                    }
                }catch (Exception e){
                    workApprovalCheck.setText("????????????");
                }*/
         /*   if(jsonObject.get("userCheck").equals("2")){
                new CustomDialog(WorkPlanDetailActivity.this, R.layout.z_user_dialog, R.id.userCheckTextView);
            }*/



            try{

                String directCity = jsonObject.get("drctClssNm").toString();
                if(!directCity.equals("?????????")) directCity+="??????";

                guganTv.setText("[" + jsonObject.get("routeNm").toString()+"]"+directCity+" "+jsonObject.get("strtBlcPntVal").toString()+"km ~ "+jsonObject.get("endBlcPntVal").toString()+"km");
            }catch (JSONException|NullPointerException e){

                guganTv.setText("\n");


            }

            try{
                bonbu_jisa_Tv.setText(nullCheck(jsonObject.get("hdqrNm").toString())+" "+nullCheck(jsonObject.get("mtnofNm").toString()));
            }catch (JSONException|NullPointerException e){
                bonbu_jisa_Tv.setText("\n");
                Log.e("??????","??????");
            }

            //guganTv.setText("[" + jsonObject.get("routeNm").toString()+"]"+directCity+" "+jsonObject.get("strtBlcPntVal").toString()+"km ~ "+jsonObject.get("endBlcPntVal").toString()+"km");
            /* guganTv.setText("[" + jsonObject.get("routeNm").toString()+"]"+jsonObject.get("drctClssNm").toString()+" "+jsonObject.get("strtBlcPntVal").toString()+"km ~ "+jsonObject.get("endBlcPntVal").toString()+"km");*/

  /*          bonbuTextView.setText(nullCheck(jsonObject.get("hdqrNm").toString()));
            jisaTextView.setText(nullCheck(jsonObject.get("mtnofNm").toString()));*/
            //  noSunTextView.setText(nullCheck(jsonObject.get("routeNm").toString()));
            try{
                gongsaContent.setText(nullCheck(jsonObject.get("cnstnCtnt").toString()));
            }catch (JSONException|NullPointerException e){
                gongsaContent.setText("");
            }
            try{
                gosundatTextView.setText(nullCheck(jsonObject.get("gosundaeNo").toString()));
            }catch (JSONException|NullPointerException e){
                gosundatTextView.setText("");
            }
            /*   guganTv.setText("["+nullCheck(jsonObject.get("routeNm").toString())+"]"+" ");*/
             /*   startGuganTextView.setText(nullCheck(jsonObject.get("strtBlcPntVal").toString()));
                endGuganTextView.setText(nullCheck(jsonObject.get("endBlcPntVal").toString()));*/
            try{
                startDateTextView.setText(nullCheck(jsonObject.get("blcStrtDttm").toString()));
            }catch (JSONException|NullPointerException e){
                startDateTextView.setText("");
            }

            try{
                endDateTextView.setText(nullCheck(jsonObject.get("blcRevocDttm").toString()));
            }catch (JSONException|NullPointerException e){
                endDateTextView.setText("");
            }
            try{
                jackupinwonetTextView.setText(nullCheck(jsonObject.get("cmmdNm").toString()));
            }catch (JSONException|NullPointerException e){
                jackupinwonetTextView.setText("");
            }

            startHourTv.setText(nullCheck(jsonObject.get("startGongsaHour").toString()));
            startMinuteTv.setText(nullCheck(jsonObject.get("startGongsaMinute").toString()));
            endHourTv.setText(nullCheck(jsonObject.get("endGongsaHour").toString()));
            endMinuteTv.setText(nullCheck(jsonObject.get("endGongsaMinute").toString()));
            try {
                gamdokTextView.setText(nullCheck(jsonObject.get("gamdokNameEMNM").toString()));
            } catch (JSONException|NullPointerException e) {
                gamdokTextView.setText("");
            }
  /*   if (jsonObject.get("drctClssCd").toString().equalsIgnoreCase("E")) {
                directionTextView.setText("????????????");
            } else if (jsonObject.get("drctClssCd").toString().equalsIgnoreCase("O")) {
                directionTextView.setText("?????????");
            } else {
                directionTextView.setText("????????????");
            }*/
       /*     if(!jsonObject.get("drctClssNm").toString().equals("?????????")){
                directionTextView.setText(jsonObject.get("drctClssNm").toString()+"??????");
            }else{
                directionTextView.setText(jsonObject.get("drctClssNm").toString());
            }*/
            try{
                jackupinwonetTextView.setText(nullCheck(jsonObject.get("cmmdNm").toString()));
            }catch (Exception e){
                jackupinwonetTextView.setText("");
            }



            String[] chdanListSplit = jsonObject.get("trfcLimtCrgwClssCd").toString().split(",");
            Log.i("???????????? ??????", jsonObject.get("trfcLimtCrgwClssCd").toString());
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
                } else if (chdanListSplit[i].equals("06")) {
                    chaDanChaoCnt++;
                }


                if (chdanListSplit[i].equals("01")) {
                    chdanBangsik += ",1??????";
                } else if (chdanListSplit[i].equals("02")) {
                    chdanBangsik += ",2??????";
                } else if (chdanListSplit[i].equals("03")) {
                    chdanBangsik += ",3??????";
                } else if (chdanListSplit[i].equals("04")) {
                    chdanBangsik += ",4??????";
                } else if (chdanListSplit[i].equals("05")) {
                    chdanBangsik += ",5??????";
                }else if (chdanListSplit[i].equals("06")) {
                    chdanBangsik += ",6??????";
                } else if (chdanListSplit[i].equals("07")) {
                    chdanBangsik += ",????????????";
                } else if (chdanListSplit[i].equals("08")) {
                    chdanBangsik += ",????????????";
                } else if (chdanListSplit[i].equals("09")) {
                    chdanBangsik += ",????????????";
                } else if (chdanListSplit[i].equals("10")) {
                    chdanBangsik += ",??????";
                } else if (chdanListSplit[i].equals("11")) {
                    chdanBangsik += ",?????????";
                } else if (chdanListSplit[i].equals("20")) {
                    chdanBangsik += ",????????????";
                } else if (chdanListSplit[i].equals("30")) {
                    chdanBangsik += ",??????";
                }

            }

            /*   String test = ",1??????,2??????,3??????,4??????,5??????,????????????,????????????,????????????,??????,?????????,????????????,??????";*/
            // String test = ",1??????,2??????,3??????,4??????,5??????,????????????";
            //Toast.makeText(this,""+test.length(),Toast.LENGTH_SHORT).show();
            //roadlimitTextView.setText(chdanBangsik.substring(1));
            try{
                if(chdanBangsik.length()>25){
                    roadlimitTextView.setTextSize(9.5f);
                    roadlimitTextView.setText(chdanBangsik.substring(1));
                }else{
                    roadlimitTextView.setText(chdanBangsik.substring(1));
                }
            }catch (ArrayIndexOutOfBoundsException|NullPointerException e){
                roadlimitTextView.setText("?????? ?????? ??????");
            }



      /*      if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("01")){
                roadlimitTextView.setText("1??????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("02")){
                roadlimitTextView.setText("2??????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("03")){
                roadlimitTextView.setText("3??????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("04")){
                roadlimitTextView.setText("4??????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("05")){
                roadlimitTextView.setText("5??????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("07")){
                roadlimitTextView.setText("????????????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("08")){
                roadlimitTextView.setText("????????????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("09")){
                roadlimitTextView.setText("????????????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("10")){
                roadlimitTextView.setText("??????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("11")){
                roadlimitTextView.setText("?????????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("20")){
                roadlimitTextView.setText("????????????");
            }else if(jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("30")){

            }*/

            workTypeTextView.setText(jsonObject.get("workType").toString());



        } catch (Exception e) {
           // e.printStackTrace();
            Log.e("??????","????????????");
        }

    }

    public  String onActionPost(String primitive, String date){
        return null;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.callLayout:
                if(workerPhonNo.length()==0){
                    Toast.makeText(this,"???????????? ??????????????? ?????????????????? ????????????",Toast.LENGTH_SHORT).show();
                }else{
                    String tel = "tel:";
                    workerPhonNo=tel+workerPhonNo;
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(workerPhonNo)));
                }

                break;
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.goto_list_Li:
                finish();
                break;

        }
    }
}
