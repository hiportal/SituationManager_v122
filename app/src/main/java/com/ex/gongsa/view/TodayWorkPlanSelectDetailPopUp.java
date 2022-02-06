package com.ex.gongsa.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Configuration;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.ex.gongsa.Configuration.SERVER_URL;


public class TodayWorkPlanSelectDetailPopUp extends BaseActivity implements View.OnClickListener {
    //수정중_2020.08

    Intent intent;

    TextView btn_shoulderSafeFacilityRemoveStart;
    TextView btn_roadSafeFacilityInstallStart;
    TextView btn_todayWorkStart;
    TextView btn_safeFacilityRemoveStart;
    TextView btn_todayWorkEnd;

    JSONObject jsonObject = null;
    String mtnofCd = "";

    String rprqCrcmSeq = "";
    String userInfoJsonObj = "";
    JSONArray workStatList = null;

    Context context;
    ProgressDialog progressDialog;



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //주변 검정으로 변하지않게
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //다이얼로그 테두리 제거
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.z_today_work_plan_content);

        intent = getIntent();

        rprqCrcmSeq = intent.getStringExtra("rprqCrcmSeq");
        userInfoJsonObj = intent.getStringExtra("userInfoJsonObj");
        mtnofCd = intent.getStringExtra("mtnofCd");

        btn_shoulderSafeFacilityRemoveStart = (TextView) findViewById(R.id.btn_shoulderSafeFacilityRemoveStart);
        btn_roadSafeFacilityInstallStart = (TextView) findViewById(R.id.btn_roadSafeFacilityInstallStart);
        btn_todayWorkStart = (TextView) findViewById(R.id.btn_todayWorkStart);
        btn_safeFacilityRemoveStart = (TextView) findViewById(R.id.btn_safeFacilityRemoveStart);
        btn_todayWorkEnd = (TextView) findViewById(R.id.btn_todayWorkEnd);


        btn_shoulderSafeFacilityRemoveStart.setOnClickListener(this);
        btn_roadSafeFacilityInstallStart.setOnClickListener(this);
        btn_todayWorkStart.setOnClickListener(this);
        btn_safeFacilityRemoveStart.setOnClickListener(this);
        btn_todayWorkEnd.setOnClickListener(this);



        try {
            JSONObject compJsonObject = null;
            String cmpValue = null;

            workStatList = new JSONArray(intent.getStringExtra("workStatList"));

            Log.i("workStatList = ", workStatList.toString());

            for (int i = 0; i < workStatList.length(); i++) {
                cmpValue = "";
                compJsonObject = workStatList.getJSONObject(i);
                cmpValue = compJsonObject.getString("acdtActnInputCd");

                System.out.println("cmpValue ============= " + cmpValue);

                if ("29".equals(cmpValue)) {
                    this.btn_shoulderSafeFacilityRemoveStart.setEnabled(false);
                } else if ("30".equals(cmpValue)) {
                    this.btn_roadSafeFacilityInstallStart.setEnabled(false);
                } else if ("31".equals(cmpValue)) {
                    this.btn_todayWorkStart.setEnabled(false);
                } else if ("32".equals(cmpValue)) {
                    this.btn_safeFacilityRemoveStart.setEnabled(false);
                } else if ("33".equals(cmpValue)) {
                    this.btn_todayWorkEnd.setEnabled(false);
                    this.btn_shoulderSafeFacilityRemoveStart.setEnabled(false);
                    this.btn_roadSafeFacilityInstallStart.setEnabled(false);
                    this.btn_todayWorkStart.setEnabled(false);
                    this.btn_safeFacilityRemoveStart.setEnabled(false);
                }
            }

        } catch (NullPointerException | JSONException e){
            Log.i("popup투데이","Exception");
        }



    }

int btnclick = 0;
    @Override
    public void onClick(View v) {

        JSONArray jsonArr = null;
        JSONObject jsonObj = null;
        JSONObject jsonStateobj = null;
        switch (v.getId()) {

            case R.id.btn_shoulderSafeFacilityRemoveStart :
                btnclick = 29;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("rprqCrcmSeq", rprqCrcmSeq);
                    jsonObj.put("acdtActnInputCd", btnclick);
                    jsonObj.put("acdtActnCtnt", "갓길 안전시설 설치 시작하였습니다.");
                    jsonObj.put("mtnofCd", mtnofCd);
                }catch(Exception e){
                    e.printStackTrace();
                }


                new Action(Configuration.JEGUBUN_JAKUPBOGOGUBUN_INSERT,"post", SERVER_URL + "/TodayWorkPlan/ingWork.do", jsonObj.toString(), this, intent).execute("");
                progressDialog = ProgressDialog.show(this, "", "로딩중...", true);
//                todayWorkEndPopUp.setText("갓길 안전시설 설치 시작하였습니다.");
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.setView(layout);
//                toast.show();

                break;

            case R.id.btn_roadSafeFacilityInstallStart :
                btnclick = 30;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("rprqCrcmSeq", rprqCrcmSeq);
                    jsonObj.put("acdtActnInputCd", btnclick);
                    jsonObj.put("acdtActnCtnt", "본선 안전시설 설치 시작하였습니다.");
                    jsonObj.put("mtnofCd", mtnofCd);
                }catch(Exception e){
                    e.printStackTrace();
                }
                new Action(Configuration.JEGUBUN_JAKUPBOGOGUBUN_INSERT,"post", SERVER_URL + "/TodayWorkPlan/ingWork.do", jsonObj.toString(), this, intent).execute("");
                progressDialog = ProgressDialog.show(this, "", "로딩중...", true);
//                todayWorkEndPopUp.setText("본선 안전시설 설치 시작하였습니다.");
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.setView(layout);
//                toast.show();
                break;

            case R.id.btn_todayWorkStart :
                btnclick = 31;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("rprqCrcmSeq", rprqCrcmSeq);
                    jsonObj.put("acdtActnInputCd", btnclick);
                    jsonObj.put("acdtActnCtnt", "작업을 시작하였습니다.");
                    jsonObj.put("mtnofCd", mtnofCd);
                }catch(Exception e){
                    e.printStackTrace();
                }
                progressDialog = ProgressDialog.show(this, "", "로딩중...", true);
                new Action(Configuration.JEGUBUN_JAKUPBOGOGUBUN_INSERT,"post", SERVER_URL + "/TodayWorkPlan/ingWork.do", jsonObj.toString(), this, intent).execute("");
//                todayWorkEndPopUp.setText("작업을 시작하였습니다.");
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.setView(layout);
//                toast.show();
                break;

            case R.id.btn_safeFacilityRemoveStart:
                btnclick = 32;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("rprqCrcmSeq", rprqCrcmSeq);
                    jsonObj.put("acdtActnInputCd", btnclick);
                    jsonObj.put("acdtActnCtnt", "안전시설 철거 시작하였습니다.");
                    jsonObj.put("mtnofCd", mtnofCd);


                }catch(Exception e){
                    e.printStackTrace();
                }
                progressDialog = ProgressDialog.show(this, "", "로딩중...", true);
                new Action(Configuration.JEGUBUN_JAKUPBOGOGUBUN_INSERT,"post", SERVER_URL + "/TodayWorkPlan/ingWork.do", jsonObj.toString(), this, intent).execute("");

                break;

            case R.id.btn_todayWorkEnd :
                //btnclick = 33;
//                todayWorkEndPopUp.setText("작업 종료하였습니다.");
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.setView(layout);
//                toast.show();
//                //               try {

//                    jsonObj = new JSONObject();
//                    jsonObj.put("rprqCrcmSeq", rprqCrcmSeq);
//                    jsonObj.put("acdtActnInputCd", btnclick);
//                    jsonObj.put("acdtActnCtnt", "안전시설 철거 시작하였습니다.");
//                    jsonObj.put("mtnofCd", mtnofCd);
//                    jsonStateobj = new JSONObject(intent.getStringExtra("jsonObj"));

                try {
                    jsonObject = new JSONObject(intent.getStringExtra("TodayWorkListJsonValue"));
//                        jsonObj = new JSONObject();
//                    jsonObj.put("rprqCrcmSeq", rprqCrcmSeq);
//                    //jsonObj.put("acdtActnInputCd", btnclick);
//                    jsonObj.put("acdtActnCtnt", "안전시설 철거 시작하였습니다.");
//                    jsonObj.put("mtnofCd", mtnofCd);
                    new CustomDialog(this, R.layout.z_send_alert_dialog, R.id.end_work_Li,jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                new CustomDialog(this, R.layout.z_send_alert_dialog, R.id.end_work_Li);


//                try {
//                    if (userInfoJsonObj.get("TEL_NO").toString().equals(jsonObject.getString("rprqInfmTelno")) ||
//                            userInfoJsonObj.get("SMS_GRP_NM").toString().equals(jsonObject.getString("cstrCrprNm"))) {
//                        if (jsonObject.get("prosStatCd").toString().equals("S") && jsonObject.get("rprqCrcmSeq") != null) {
//                            jsonObject = new JSONObject(intent.getStringExtra("TodayWorkListJsonValue"));
//                            Log.i("파람확인", jsonObject.toString());
//                            new CustomDialog(this, R.layout.z_send_alert_dialog, R.id.end_work_Li, jsonObject);
//                            /*                   Toast.makeText(this,jsonObject.get("rprqCrcmSeq").toString(),Toast.LENGTH_SHORT).show();*/
//                        } else {
//                            Toast.makeText(getApplicationContext(), "작업완료 처리는 승인상태에서만 가능합니다.", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(this, "등록하신 작업은 작업 완료 대상 작업이 아닙니다.", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException |NullPointerException e) {
//                    Log.e("에러","에러발생");
//                }catch(Exception e) {
//                    Log.e("에러","에러발생");
//                }

                break;


        }
        }

    @Override
    public String onActionPost(String primitive, String data) {

        Log.d("onActiopostinsert", "TodayWorkPlanSele onActiopostinsert" + data);
        Log.d("onActiopostinsert", "TodayWorkPlanSele onActiopostinsert2" + Configuration.JEGUBUN_JAKUPBOGOGUBUN_INSERT.toString());

        if (data.equals("1")) {
            if (primitive.equals(Configuration.JEGUBUN_JAKUPBOGOGUBUN_INSERT)) {
                progressDialog.dismiss();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.z_today_work_plan_content_popup, null);
                TextView todayWorkEndPopUp = (TextView) layout.findViewById(R.id.todayWorkEndPopUp);
                Toast toast = new Toast(getApplicationContext());
                Log.d("onActiopostinsert", "288");

                if (btnclick == 29) {
                    this.btn_shoulderSafeFacilityRemoveStart.setEnabled(false);
                    todayWorkEndPopUp.setText("갓길 안전시설 설치 시작하였습니다.");
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (btnclick == 30) {
                    this.btn_roadSafeFacilityInstallStart.setEnabled(false);
                    todayWorkEndPopUp.setText("본선 안전시설 설치 시작하였습니다.");
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (btnclick == 31) {
                    this.btn_todayWorkStart.setEnabled(false);
                    todayWorkEndPopUp.setText("작업을 시작하였습니다.");
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (btnclick == 32) {
                    this.btn_safeFacilityRemoveStart.setEnabled(false);
                    todayWorkEndPopUp.setText("안전시설 철거 시작하였습니다.");
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        }else if ("0".equals(data)) {
            progressDialog.dismiss();
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.z_today_work_plan_content_popup, null);
            TextView todayWorkEndPopUp = (TextView) layout.findViewById(R.id.todayWorkEndPopUp);
            Toast toast = new Toast(getApplicationContext());
            if (btnclick == 29) {
                this.btn_shoulderSafeFacilityRemoveStart.setEnabled(false);
                todayWorkEndPopUp.setText("갓길 안전시설 설치에 실패하였습니다.");
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            } else if (btnclick == 30) {
                this.btn_roadSafeFacilityInstallStart.setEnabled(false);
                todayWorkEndPopUp.setText("본선 안전시설 설치에 실패하였습니다.");
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            } else if (btnclick == 31) {
                this.btn_todayWorkStart.setEnabled(false);
                todayWorkEndPopUp.setText("작업 시작에 실패하였습니다.");
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            } else if (btnclick == 32) {
                this.btn_safeFacilityRemoveStart.setEnabled(false);
                todayWorkEndPopUp.setText("안전시설 철거에 실패하였습니다.");
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        }


        return null;
    }


}