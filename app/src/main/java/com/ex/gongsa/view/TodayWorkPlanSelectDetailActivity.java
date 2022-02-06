package com.ex.gongsa.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Common;
import com.ex.gongsa.Configuration;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;

public class TodayWorkPlanSelectDetailActivity extends BaseActivity implements View.OnClickListener {

    Intent intent;

    ViewGroup camera;
    ViewGroup registerLayout;
    ViewGroup prevewLayout;
    //수정중_202008
    ViewGroup Li_endWork;
    LinearLayout Li_update_work;

    TextView bonbuTextView;//본부이름
    TextView jisaTextView;//지사
    TextView noSunTextView;//노선
    TextView directionTextView;//방향
    TextView gamdokTextView;//감독원
    TextView gongsaGuganTextView;
    TextView gongsaContentTextView;
    TextView jackupinwonetTextView;//작업인원
    TextView gosundatTextView;//고순대번호
    TextView workTypeTextView;
    TextView workApprovalCheck;//승인여부
    TextView roadlimitTextView;
    TextView startHourTv; //공사 시작시간
    TextView startMinuteTv;//공사 시작 분
    TextView endHourTv;
    TextView endMinuteTv;
    TextView startDateTextView;
    TextView endDateTextView;
    ImageView gongsaPrev, examplePic;

    Bitmap rotateBitMap;
    String checking = null;

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;

    LinearLayout gosundae_list;

    TextView bonbuTableTextView;
    TextView jisaTableTextView;
    TextView nosunTableTextView;
    TextView gamdokTableTextView;

    int maxCharo = 0;


    String chadanListDetailList = "";
    int chadanCharo = 0;


    JSONObject userInfoJsonObj = null;
    //202008_수정중
    JSONObject jubboJsonObj = null;
    String jubboNumber = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();

        setContentView(R.layout.z_today_work_plan_detail);

        registerLayout = (ViewGroup) findViewById(R.id.registerLayout);
        prevewLayout = (ViewGroup) findViewById(R.id.prevewLayout);
        camera = (ViewGroup) findViewById(R.id.camera);
        Li_endWork = (LinearLayout) findViewById(R.id.Li_endWork);
        Li_update_work = (LinearLayout) findViewById(R.id.Li_update_work);

        examplePic = (ImageView) findViewById(R.id.examplePic);
        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);

        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);
        jisaTextView = (TextView) findViewById(R.id.jisaTextView);
        noSunTextView = (TextView) findViewById(R.id.noSunTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);//방향
        gamdokTextView = (TextView) findViewById(R.id.gamdokTextView);
        gongsaGuganTextView = (TextView) findViewById(R.id.gongsaGuganTextView);
        gongsaContentTextView = (TextView) findViewById(R.id.gongsaContentTextView);
        jackupinwonetTextView = (TextView) findViewById(R.id.jackupinwonetTextView);
        gosundatTextView = (TextView) findViewById(R.id.gosundatTextView);
        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView);
        workApprovalCheck = (TextView) findViewById(R.id.workApprovalCheck);
        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);//차로제한
        startMinuteTv = (TextView) findViewById(R.id.startMinuteTv);
        endHourTv = (TextView) findViewById(R.id.endHourTv);
        endMinuteTv = (TextView) findViewById(R.id.endMinuteTv);
        startHourTv = (TextView) findViewById(R.id.startHourTv);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        bonbuTableTextView = (TextView) findViewById(R.id.bonbuTableTextView);//이미지 태그 본부
        jisaTableTextView = (TextView) findViewById(R.id.jisaTableTextView);//이미지 태그 텍스트뷰
        nosunTableTextView = (TextView) findViewById(R.id.nosunTableTextView);//이미지 태그 노선
        gamdokTableTextView = (TextView) findViewById(R.id.gamdokTableTextView);//이미지 태그 감독원
        gosundae_list = (LinearLayout) findViewById(R.id.gosundae_list);

        gongsaPrev.setOnClickListener(this);
        camera.setOnClickListener(this);
        registerLayout.setOnClickListener(this);
        //roadlimitTextView.setOnClickListener(this);
        Li_endWork.setOnClickListener(this);
        Li_update_work.setOnClickListener(this);

        try {
            userInfoJsonObj = new JSONObject(getIntent().getStringExtra("userInfo"));
            Log.i("유저인포", userInfoJsonObj.toString());
        } catch (JSONException|NullPointerException e) {
            Log.e("에러","에러발생");
        }
        try {


            jsonObject = new JSONObject(intent.getStringExtra("TodayWorkListJsonValue"));
            Log.println(Log.ASSERT, "이거 보고 적으셈", jsonObject.toString());
            bonbuTextView.setText(nullCheck(jsonObject.get("hdqrNm").toString()));
            jisaTextView.setText(nullCheck(jsonObject.get("mtnofNm").toString()));
            noSunTextView.setText(nullCheck(jsonObject.get("routeNm").toString()));
            if (jsonObject.get("routeDrctCd").toString().equals("E")) {
                directionTextView.setText("종점방향");
            } else if (jsonObject.get("routeDrctCd").toString().equals("S")) {
                directionTextView.setText("시점방향");
            } else {
                directionTextView.setText("양방향");
            }


            try{
                try{
                    if (userInfoJsonObj.get("TEL_NO").toString().equals(jsonObject.getString("rprqInfmTelno")) ||
                            userInfoJsonObj.get("SMS_GRP_NM").toString().equals(jsonObject.getString("cstrCrprNm"))) {
                        if (jsonObject.get("prosStatCd").toString().equals("R") ){

                        } else {
                            Li_update_work.setVisibility(View.GONE);
                        }
                    } else {
                        Li_update_work.setVisibility(View.GONE);
                    }
                }catch (JSONException|NullPointerException e){
                    Log.e("에러","에러발생");
                }
            }catch (NullPointerException e){
                Log.e("에러","에러발생");
            }

            String[] lineArray = jsonObject.get("acdtLoctCtnt").toString().split("km");
            gongsaGuganTextView.setText(lineArray[0] + "km" + lineArray[1] + "km");

           try{
               gongsaContentTextView.setText(nullCheck(jsonObject.get("gongsaContent").toString()));
           }catch (JSONException|NullPointerException e){
               gongsaContentTextView.setText("");
           }catch(Exception e){
               gongsaContentTextView.setText("");
           }

            //    gongsaContentTextView.setText("");
            try{
               jackupinwonetTextView.setText(nullCheck(jsonObject.get("inwonJangbi").toString()));
            }catch (Exception e){
                try{
                    jackupinwonetTextView.setText(nullCheck(jsonObject.get("cnstnCtnt").toString()));
                }catch (JSONException|NullPointerException ee){
                    jackupinwonetTextView.setText("");
                }catch(Exception ee){
                    jackupinwonetTextView.setText("");
                }
            }

            try {
                String[] gosundaeList = jsonObject.get("gosundaeNo").toString().split(",");


                setGosundaeNo(gosundaeList, gosundaeList.length);

            } catch (JSONException|NullPointerException e) {
                Log.e("에러","에러발생");
                String[] gosundaeList = new String[]{""};
                setGosundaeNo(gosundaeList, 1);
                //  gosundatTextView.setText("");
            }
            try{
                workTypeTextView.setText(nullCheck(jsonObject.get("workTypeNm").toString()));
            }catch (JSONException|NullPointerException e){
                Log.e("Exceptioen","예외발생");
            }catch(Exception e){
                Log.e("Exceptioen","예외발생");
            }


            if (jsonObject.get("prosStatCd").toString().equals("R")) {//대기
                workApprovalCheck.setText("대기");
            } else if (jsonObject.get("prosStatCd").toString().equals("S")) {
                workApprovalCheck.setText("승인");
            } else {
                workApprovalCheck.setText("반려");
            }


            bonbuTableTextView.setText(nullCheck(jsonObject.get("hdqrNm").toString()));
            jisaTableTextView.setText(nullCheck(jsonObject.get("mtnofNm").toString()));
            nosunTableTextView.setText(nullCheck(jsonObject.get("routeNm").toString()));
            String[] charoList;
            try {
                charoList = jsonObject.get("crgwList").toString().split(",");
                for (int i = 0; i < charoList.length; i++) {
                    //20201021_법면코드수정, 코드 더 추가됨.
                    if (charoList[i].equals("01")) {
                        chadanCharo = chadanCharo + 1;
                        chadanListDetailList += ",1차로";
                    } else if (charoList[i].equals("02")) {
                        chadanCharo = chadanCharo + 1;
                        chadanListDetailList += ",2차로";
                    } else if (charoList[i].equals("03")) {
                        chadanCharo = chadanCharo + 1;
                        chadanListDetailList += ",3차로";
                    } else if (charoList[i].equals("04")) {
                        chadanCharo = chadanCharo + 1;
                        chadanListDetailList += ",4차로";
                    } else if (charoList[i].equals("05")) {
                        chadanCharo = chadanCharo + 1;
                        chadanListDetailList += ",5차로";
                    } else if (charoList[i].equals("11")) {
                        chadanListDetailList += ",6차로";
                    } else if (charoList[i].equals("06")) {
                        chadanListDetailList += ",진입램프";
                    } else if (charoList[i].equals("07")) {
                        chadanListDetailList += ",진출램프";
                    } else if (charoList[i].equals("08")) {
                        chadanListDetailList += ",교량하부";
                    } else if (charoList[i].equals("09")) {
                        chadanListDetailList += ",법면";
                    } else if (charoList[i].equals("00")) {
                        chadanListDetailList += ",갓길";
                    } else if (charoList[i].equals("12")) {
                        chadanListDetailList += ",교대차단";

                    } else if (charoList[i].equals("10")) {
                        chadanListDetailList += ",회차로";
                    } else if (charoList[i].equals("20")) {
                        chadanListDetailList += ",이동차단";
                    }
                    //if-else
                }//for
            } catch (JSONException|NullPointerException e) {
               // Log.e("에러","에러발생");
                chadanListDetailList += ",차단차로 없음";
            }



            try {
                if (chadanListDetailList.length() > 20) {
                    roadlimitTextView.setTextSize(9.5f);
                    /* roadlimitTextView.setTextSize(10.5f);*/
                    roadlimitTextView.setText(chadanListDetailList.substring(1));
                } else {
                    roadlimitTextView.setText(chadanListDetailList.substring(1));
                }
            } catch (NullPointerException e) {
                roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + "차단");
            }


            try {
                gamdokTextView.setText(nullCheck(jsonObject.get("gamdokNameEMNM").toString()));
            } catch (Exception e) {
                Log.e("에러","에러발생");
                try {
                    gamdokTextView.setText(jsonObject.get("spvsrTelno").toString());
                }catch (JSONException|NullPointerException e1){
                    Log.e("에러","에러발생");
                }
            }

            try {
                gamdokTableTextView.setText(nullCheck(jsonObject.get("gamdokNameEMNM").toString()));
            } catch (JSONException|NullPointerException e) {
                Log.e("에러","에러발생");
                try {
                    gamdokTableTextView.setText(nullCheck(jsonObject.get("spvsrTelno").toString()));
                } catch (JSONException|NullPointerException ee) {
                    Log.e("에러","에러발생");
                    gamdokTableTextView.setText("");
                }
            }


            startDateTextView.setText(nullCheck(jsonObject.get("startDate").toString()));
            endDateTextView.setText(nullCheck(jsonObject.get("endDate").toString()));
            startHourTv.setText(nullCheck(jsonObject.get("startHour").toString()));
            endHourTv.setText(nullCheck(jsonObject.get("endHour").toString()));
            startMinuteTv.setText(nullCheck(jsonObject.get("startMinute").toString()));
            endMinuteTv.setText(nullCheck(jsonObject.get("endMinute").toString()));
            try {
                jsonObject.get("fileGrpSeq").toString();
            } catch (JSONException|NullPointerException e) {
                Log.e("에러","에러발생");
                jsonObject.put("fileSeq", "1");
                Log.i("fileSeq값 확인", jsonObject.getString("fileSeq"));
            }
        } catch (JSONException|NullPointerException e) {
            Log.e("에러","에러발생");
        }
    }

    public void setGosundaeNo(String[] gosundaeArray, int length) {
        switch (1) {
            case 1:
                addView(Common.nullCheck(gosundaeArray[0].replaceAll("-", "")));
                if (1 == length) break;
            case 2:
                addView(Common.nullCheck(gosundaeArray[1].replaceAll("-", "")));
                if (2 == length) break;
            case 3:
                addView(Common.nullCheck(gosundaeArray[2].replaceAll("-", "")));
                if (3 == length) break;
            case 4:
                addView(Common.nullCheck(gosundaeArray[3].replaceAll("-", "")));
                if (4 == length) break;
            case 5:
                addView(Common.nullCheck(gosundaeArray[4].replaceAll("-", "")));
                if (5 == length) break;
        }
    }

    public void addView(@Nullable String gosundae_No) {
        Log.i("addView", "View에 들어옴");
        LinearLayout li_out = new LinearLayout(this);
        final int view_height_out = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams viewParams_out_Li = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        li_out.setLayoutParams(viewParams_out_Li);
        li_out.setOrientation(LinearLayout.VERTICAL);
        // li_out.setTag("pa_"+gosundae_list.getChildCount());
        //View 추가
        View view = new View(this);
        final int view_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());


        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, view_height, 0);
        view.setLayoutParams(viewParams);
        //gosundae_list.addView(view);
        //if(gosundae_list.getChildCount()!=0){
        Log.i("cntcnt", li_out.getChildCount() + "");
        li_out.addView(view);
        //}


        //Linear Layout 추가
        final int li_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        LinearLayout li = new LinearLayout(this);
        li.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams linear_View = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, li_height, 0);
        //li.setBackground(R.drawable.insert_line_color);
        //li.setBackground(R.drawable.insert_line_color);
        //li.setBackgroundResource(R.drawable.insert_line_color);
        li.setLayoutParams(linear_View);
        /*li.setWeightSum(322)*/
        ;
        li.setWeightSum(100);
        li.setOrientation(LinearLayout.HORIZONTAL);


        final int set_et_padding_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams et_param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 100);
        TextView tv = new TextView(this);
        tv.setLayoutParams(et_param);
        tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tv.setBackgroundResource(R.drawable.insert_line_color_left);
        tv.setPadding(set_et_padding_left, 0, 0, 0);
        tv.setTextSize(15);
        tv.setTextColor(-4276546);
        tv.setInputType(InputType.TYPE_CLASS_NUMBER);
        tv.setMaxLines(1);
        // que_et_list.add(et);
        if (gosundae_No != null) {
            tv.setText(gosundae_No);

        }
        li.addView(tv);
        tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
//        que_et_list.add(et);

 /*       View inner_view = new View(this);
        LinearLayout.LayoutParams inner_viewParams = new LinearLayout.LayoutParams(0,view_height,5);
        inner_view.setLayoutParams(inner_viewParams);
        li.addView(inner_view);*/


        //ImageView iv = new ImageView(this);
     /*   final int set_iv_padding_left = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams iv_param = new LinearLayout.LayoutParams(set_iv_padding_left, ViewGroup.LayoutParams.WRAP_CONTENT,10);
        iv.setLayoutParams(iv_param);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //iv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        //iv.s(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        iv.setBackgroundResource(R.drawable.z_gosunda_minus_item);
*/
        //iv.setBackground(this.getDrawable(R.drawable.z_gosunda_minus_item));
        //iv.setTag((String)"gosundae_"+li.getChildCount());


//        iv.setTag((String)"gosundae_"+((LinearLayout)li_out.getParent()).getChildCount());
//        iv.setId( Integer.parseInt((String)iv.getTag()));
      /*  iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup v = (ViewGroup)view.getParent();
                LinearLayout li = (LinearLayout)v.getParent();
                LinearLayout li_pa = (LinearLayout)li.getParent();
                Log.i("index","지워지는 인덱스:"+li_pa.indexOfChild(li)+"");

                *//*if(que_et_list.size()>0){

                }*//*

               // Log.i("index","리스트사이즈::"+que_et_list.size());
                //li_pa.removeView(li);
                //li_pa.removeView(li);
      *//*          if(li_pa.getChildCount()==1){
         *//**//*Toast.makeText(WorkPlanResisterLoadContentActivity.this,"고순대번호는 최소 1개 이상이 입력 되어야 합니다.",Toast.LENGTH_SHORT).show();
                    ((EditText)v.getChildAt(0)).setText("");
                    ((EditText)que_et_list.get(0)).setText("");
                    *//**//*
                    if(((EditText)v.getChildAt(0)).getText().toString().length()==0 || ((EditText)v.getChildAt(0)).getText().toString().equals("")){
                        Toast.makeText(WorkPlanResisterLoadContentActivity.this,"고순대번호는 최소 1개가 입력되어야 합니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        new CustomDialog(WorkPlanResisterLoadContentActivity.this, R.layout.zz_warn_gosundae, R.id.warn_msg,"지우시는 고순대 번호는\n"+gosunDaeNoParsing(((EditText)v.getChildAt(0)).getText().toString())+"\n입니다.", que_et_list, li_pa.indexOfChild(li),li_pa) ;
                    }


                }else{
             *//**//*       que_et_list.remove(li_pa.indexOfChild(li));
                    Toast.makeText(WorkPlanResisterLoadContentActivity.this,"지우시는 번호는 "+((EditText)v.getChildAt(0)).getText().toString()+" 입니다.",Toast.LENGTH_SHORT).show();
                    li_pa.removeViewAt(li_pa.indexOfChild(li));
*//**//*
                    new CustomDialog(WorkPlanResisterLoadContentActivity.this, R.layout.zz_warn_gosundae, R.id.warn_msg,"지우시는 고순대 번호는\n"+gosunDaeNoParsing(((EditText)v.getChildAt(0)).getText().toString())+"\n입니다.", que_et_list, li_pa.indexOfChild(li),li_pa) ;
                }
*//*


                for(int i = 0; i <que_et_list.size() ;i++){
                    Log.i("index","리스트에 담긴것들::"+que_et_list.get(i).getText().toString());
                }
                Log.i("index","------------------------------------");
            }
        });*/
        li_out.addView(li);
        //  li.addView(iv);

        gosundae_list.addView(li_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.gongsaPrev:
                try {
                    String isUpdated = getIntent().getStringExtra("isAfterUpdate");
                    if (isUpdated.equals("Y")) {
                        userInfoJsonObj.put("curPage", "1");
                        userInfoJsonObj.put("initCheck", "Y");
                        userInfoJsonObj.put("isInfiniteTest", "Y");
                        new Action("post", SERVER_URL + "/TodayWorkPlan/List.do", userInfoJsonObj.toString(), this, intent).execute("");
                    } else {
                        finish();
                    }
                } catch (JSONException | NullPointerException e) {
                    finish();
                }

                ;
                break;
            case R.id.camera:
                try {
                    /*if (userInfoJsonObj.get("TEL_NO").toString().equals(jsonObject.getString("rprqInfmTelno")) ||
                            userInfoJsonObj.get("SMS_GRP_NM").toString().equals(jsonObject.getString("cstrCrprNm"))) {

                        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                permissionCheck();
                            } else {


                                intent = new Intent(TodayWorkPlanSelectDetailActivity.this, CameraActivity.class);

                                intent.putExtra("TodayWorkListJsonValue", jsonObject.toString());
                                startActivityForResult(intent, 0);
                            }
                    } else {
                        Toast.makeText(this, "등록하신 작업은 사진촬영 대상 작업이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }*/

                    intent = new Intent(TodayWorkPlanSelectDetailActivity.this, CameraActivity.class);

                    intent.putExtra("TodayWorkListJsonValue", jsonObject.toString());
                    startActivityForResult(intent, 0);


                } catch (NullPointerException e) {
                    Log.e("에러", "에러발생");
                }

                break;
            case R.id.roadlimitTextView:

                try {
                    new CustomDialog(TodayWorkPlanSelectDetailActivity.this, R.layout.z_road_chadan_detail, R.id.JaupclickOk, chadanListDetailList);
                } catch (NullPointerException e) {
                    Log.e("에러", "에러발생");
                }
                break;
            case R.id.registerLayout://사진첨부

                System.out.println("========= gongsa photo ============");


                if (rotateBitMap == null) {
                    Toast.makeText(getApplicationContext(), "사진을 첨부해주세요", Toast.LENGTH_LONG).show();
                    Log.e("Toast Messages : ", "Toast Message : NULL ");
                    break;
                } else {
                    //Bitmap을 사진으로 저장
                    File savedFile = savePictur(rotateBitMap);
                    Log.i("saved FileName", savedFile.getName());
                    Log.i("saved FilePath", savedFile.getAbsolutePath());
                    try {
                        if (true) {

                            System.out.println("========= gongsa photo : " + jsonObject.toString());
                            new CustomDialog(TodayWorkPlanSelectDetailActivity.this, R.layout.z_user_action_dialog, R.id.userconfirmedCheck, "사진을 전송하시겠습니까?", jsonObject, savedFile);
                            break;

                        } else {
                            Toast.makeText(getApplicationContext(), "첨부가능한 사진의 갯수를 초과하여 사진을 첨부 할 수 없습니다.", Toast.LENGTH_LONG).show();
                        }
                    } catch (NullPointerException e) {
                        Log.e("에러", "에러발생");
                    }
                }
                break;
            case R.id.li_ResetBtn:

                intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                break;

            //작업완료 버튼 -> 작업보고 버튼으로 수정됨.
            case R.id.Li_endWork:

//                try {
//
////                    jubboNumber = jsonObject.getString("rprqCrcmSeq");
//                    jsonObject.put("primitive", "TEST");
//                    jubboNumber = jsonObject.toString();
//
//                    new Action(Configuration.JEGUBUN_JAKUPBOGO_SELECT, "post", SERVER_URL + "/TodayWorkPlan/WorkStatList.do", jubboNumber, this, intent).execute("");
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                try {
                    Log.i("sms_grp_nm 확인",jsonObject.getString("cstrCrprNm"));
                    if (userInfoJsonObj.get("TEL_NO").toString().equals(jsonObject.getString("rprqInfmTelno")) ||
                            userInfoJsonObj.get("SMS_GRP_NM").toString().equals(jsonObject.getString("cstrCrprNm"))) {
                        if (jsonObject.get("prosStatCd").toString().equals("S") && jsonObject.get("rprqCrcmSeq") != null) {

                           //           jsonObject.put("primitive", "TEST");
                            jubboNumber = jsonObject.toString();

                            new Action(Configuration.JEGUBUN_JAKUPBOGO_SELECT, "post", SERVER_URL + "/TodayWorkPlan/WorkStatList.do", jubboNumber, this, intent).execute("");


//                            intent = new Intent(TodayWorkPlanSelectDetailActivity.this, TodayWorkPlanSelectDetailPopUp.class);
//                            intent.putExtra("TodayWorkListJsonValue", jsonObject.toString());
//                            intent.putExtra("userInfoJsonObj", jsonObject.toString());
//                            startActivityForResult(intent, 0);

                        } else {
                            Toast.makeText(getApplicationContext(), "작업보고 승인상태에서만 가능합니다.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "등록하신 작업은 작업 보고 대상이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException|NullPointerException e) {
                    Log.e("에러","에러발생");
                }

                break;


//
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
//                } catch (JSONException|NullPointerException e) {
//                    Log.e("에러","에러발생");
//                }catch(Exception e) {
//                    Log.e("에러","에러발생");
//        }
            case R.id.Li_update_work:

                try{
                    try{
                        if (userInfoJsonObj.get("TEL_NO").toString().equals(jsonObject.getString("rprqInfmTelno")) ||
                                userInfoJsonObj.get("SMS_GRP_NM").toString().equals(jsonObject.getString("cstrCrprNm"))) {
                            if (jsonObject.get("prosStatCd").toString().equals("R") ) {
                                jsonObject = new JSONObject(intent.getStringExtra("TodayWorkListJsonValue"));
                                intent = new Intent(this,TodayWorkPlanSelectUpdate.class);
                                intent.putExtra("userInfo",userInfoJsonObj.toString());
                                intent.putExtra("TodayWorkListJsonValue",jsonObject.toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "작업 수정은 대기 상태에서만 가능합니다.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "선택하신 작업은 작업수정 대상 작업이 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException|NullPointerException e){
                        Log.e("에러","에러발생");
                    }
                }catch (NullPointerException e){
                    Log.e("에러","에러발생");
                }
                break;
        }
    }

    @Override
    public String onActionPost(String primitive, String data) {


        if(primitive.equals(Configuration.JEGUBUN_JAKUPBOGO_SELECT)){


            try {
                JSONArray arr = new JSONArray(URLDecoder.decode(data, "utf-8"));

                intent = new Intent(TodayWorkPlanSelectDetailActivity.this, TodayWorkPlanSelectDetailPopUp.class);
                intent.putExtra("rprqCrcmSeq", jsonObject.getString("rprqCrcmSeq"));
                intent.putExtra("userInfoJsonObj", userInfoJsonObj.toString());
                intent.putExtra("mtnofCd", jsonObject.getString("mtnofCd") );
                intent.putExtra("workStatList", arr.toString());
                intent.putExtra("TodayWorkListJsonValue", jsonObject.toString());



                startActivityForResult(intent, 0);
            }catch(Exception e){
                e.printStackTrace();
            }


        }
        Log.d("TodayonActionPost","TodayonActionPost data = " + data);



        return null;
    }


    public void permissionCheck() {
        String[] permisionList = new String[3];
        permisionList[0] = Manifest.permission.CAMERA;
        permisionList[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permisionList[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        final int MULTIPLE_PERMISSIONS = 100;
        ActivityCompat.requestPermissions(TodayWorkPlanSelectDetailActivity.this, permisionList, MULTIPLE_PERMISSIONS);
    }

    public File savePictur(Bitmap bitmap) {

        try {
            /*String path="/sdcard/DCIM/Camera/";*/
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Date today = new Date();

            String fileName = sdf.format(today) + ".jpg";
            //기기 내부 사진 저장경로
            String fullPath = path + "/DCIM/Camera/" + fileName;

            File file_path = new File(fullPath);
            if (!file_path.exists()) {
                file_path.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            if (file_path.exists() == true) {
                Log.e("파일", "TodayWorkPlanRegisterDetailActivity 파일 있음");
                return file_path;


            } else {
                Log.e("파일", "TodayWorkPlanRegisterDetailActivity 파일 파일없음");
                //  Toast.makeText(getApplicationContext(),"파일 없음",Toast.LENGTH_SHORT).show();
                return null;
            }

        } catch (IOException e) {
            Log.e("에러","에러발생");
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                prevewLayout.setVisibility(prevewLayout.VISIBLE);
                byte[] fileName = data.getByteArrayExtra("filename");

                //비트맵 용량 축소
               /* BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 2;*/

                //신규
                //Bitmap myBit = BitmapFactory.decodeByteArray(fileName, 0, fileName.length,opt);

                Bitmap myBit = BitmapFactory.decodeByteArray(fileName, 0, fileName.length);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                rotateBitMap = Bitmap.createBitmap(myBit, 0, 0, myBit.getWidth(), myBit.getHeight(), matrix, true);
                examplePic.setImageBitmap(rotateBitMap);
                Log.d("이미지뷰 가로", Integer.toString(examplePic.getWidth()));
                Log.d("이미지뷰 세로", Integer.toString(examplePic.getHeight()));
                Log.d("bitmap 세로", Integer.toString(rotateBitMap.getHeight()));
                Log.d("bitmap 가로", Integer.toString(rotateBitMap.getWidth()));
                checking = "ㅡㅡ";
            } catch (Exception e) {
                Log.e("에러","에러발생");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://종료
                try{
                    String isUpdated = getIntent().getStringExtra("isAfterUpdate");
                    if(isUpdated.equals("Y")){
                        new Action("post", SERVER_URL + "/TodayWorkPlan/List.do", userInfoJsonObj.toString(), this, intent).execute("");
                    }else{
                        finish();
                    }
                }catch (NullPointerException e){
                    finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
