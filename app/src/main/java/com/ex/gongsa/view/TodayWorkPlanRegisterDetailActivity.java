package com.ex.gongsa.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.MalformedJsonException;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Preview;
import com.ex.situationmanager.R;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.util.Common;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;

public class TodayWorkPlanRegisterDetailActivity extends BaseActivity implements View.OnClickListener {

    Intent intent;

    final int REQUEST_CODE_CAMERA = 1000;
    ImageView examplePic;

    ViewGroup prevewLayout, registerLayout, updateContent;
    ImageView gongsaPrev;

    TextView gotoMainTv, bonbuTextView, jisaTextView,bonbu_jisa_Tv;
  //  TextView noSunTextView;
 //   TextView gosundatTextView;
  //  TextView startGuganTextView;
    //TextView endGuganTextView;
    TextView gongsaContent;
    TextView startDateTextView;
    TextView endDateTextView;
    TextView jackupinwonetTextView;
   // TextView directionTextView;
    TextView roadlimitTextView;
    TextView workTypeTextView;
    TextView workApprovalCheck;
    TextView gamdokTextView;
    TextView startHourTv;//공사 시작 시간
    TextView startMinuteTv;//공사 시작 분
    TextView endHourTv;//공사 종료 시간
    TextView endMinuteTv;
    TextView guganTv;

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    LinearLayout gosundae_list;

    int chaDanChaoCnt = 0;
    String chdanBangsik = "";
    String userInfo;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.zz_today_work_plan_register_detail);
        intent = getIntent();
    //    Toast.makeText(getApplicationContext(), intent.getStringExtra("todayWorkPlanRegister"), Toast.LENGTH_LONG);

        gosundae_list = (LinearLayout)findViewById(R.id.gosundae_list);

        examplePic = (ImageView) findViewById(R.id.examplePic);
        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);

        prevewLayout = (ViewGroup) findViewById(R.id.prevewLayout);
        registerLayout = (ViewGroup) findViewById(R.id.registerLayout);
        updateContent = (ViewGroup) findViewById(R.id.updateContent);

/*        registerLayout.setOnClickListener(this);*/
        updateContent.setOnClickListener(this);
        gongsaPrev.setOnClickListener(this);
        //   android:visibility="gone"
        guganTv = (TextView)findViewById(R.id.guganTv);
        bonbu_jisa_Tv = (TextView)findViewById(R.id.bonbu_jisa_Tv);
        gotoMainTv = (TextView) findViewById(R.id.gotoMainTv);
        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);
        jisaTextView = (TextView) findViewById(R.id.jisaTextView);
    //    noSunTextView = (TextView) findViewById(R.id.noSunTextView);
      //  gosundatTextView = (TextView) findViewById(R.id.gosundatTextView);
      //  startGuganTextView = (TextView) findViewById(R.id.startGuganTextView);
    //    endGuganTextView = (TextView) findViewById(R.id.endGuganTextView);
        gongsaContent = (TextView) findViewById(R.id.gongsaContent);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        jackupinwonetTextView = (TextView) findViewById(R.id.jackupinwonetTextView);
      //  directionTextView = (TextView) findViewById(R.id.directionTextView);
        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);
        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView);
        workApprovalCheck = (TextView) findViewById(R.id.workApprovalCheck);
        gamdokTextView = (TextView) findViewById(R.id.gamdokTextView);
        startHourTv = (TextView)findViewById(R.id.startHourTv);
        startMinuteTv = (TextView)findViewById(R.id.startMinuteTv);
        endHourTv =(TextView)findViewById(R.id.endHourTv);
        endMinuteTv = (TextView)findViewById(R.id.endMinuteTv) ;

        registerLayout.setOnClickListener(this);
        updateContent.setOnClickListener(this);
        gongsaPrev.setOnClickListener(this);
       // roadlimitTextView.setOnClickListener(this);

        try {


            jsonObject = new JSONObject(intent.getStringExtra("todayWorkPlanRegister"));
            userInfo = intent.getStringExtra("userInfo");
            Log.i("json파람 확인",jsonObject.toString());

            if(jsonObject.get("userCheck").equals("2")){
                new CustomDialog(TodayWorkPlanRegisterDetailActivity.this, R.layout.z_user_dialog, R.id.userCheckTextView);
            }

            String drirectCity = jsonObject.get("drctClssNm").toString();
            if(!drirectCity.equals("양방향"))drirectCity+="방향";

            bonbu_jisa_Tv.setText(nullCheck(jsonObject.get("hdqrNm").toString())+" "+nullCheck(jsonObject.get("mtnofNm").toString()));
            guganTv.setText("["+jsonObject.get("routeNm").toString()+"]"+drirectCity+" "+jsonObject.get("strtBlcPntVal").toString()+"km"+" ~ "+jsonObject.get("endBlcPntVal").toString()+"km");

            /*bonbuTextView.setText(nullCheck(jsonObject.get("hdqrNm").toString()));
            jisaTextView.setText(nullCheck(jsonObject.get("mtnofNm").toString()));*/
/*            noSunTextView.setText(nullCheck(jsonObject.get("routeNm").toString()));*/
            gongsaContent.setText(nullCheck(jsonObject.get("cnstnCtnt").toString()));


           // gosundatTextView.setText(nullCheck(jsonObject.get("gosundaeNo").toString()));
            Log.i("고순대번호 확인",nullCheck(jsonObject.get("gosundaeNo").toString()));
            try{
                String[] gosundaeList = jsonObject.get("gosundaeNo").toString().split(",");


                setGosundaeNo(gosundaeList,gosundaeList.length);

            }catch (JSONException|NullPointerException e) {
                Log.e("에러","에러발생");
                String[] gosundaeList = new String[]{""};
               // setGosundaeNo(gosundaeList, 1);
                //
            }

//            startGuganTextView.setText(nullCheck(jsonObject.get("strtBlcPntVal").toString()));
  //          endGuganTextView.setText(nullCheck(jsonObject.get("endBlcPntVal").toString()));
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
                Log.e("에러","에러발생");
                jackupinwonetTextView.setText(nullCheck(""));
            }

            gamdokTextView.setText(nullCheck(jsonObject.get("gamdokNameEMNM").toString()));
         /*   if (jsonObject.get("drctClssCd").toString().equalsIgnoreCase("E")) {
                directionTextView.setText("종점방향");
            } else if (jsonObject.get("drctClssCd").toString().equalsIgnoreCase("O")) {
                directionTextView.setText("양방향");
            } else {
                directionTextView.setText("시점방향");
            }*/
            //String directCity=nullCheck(jsonObject.get("drctClssNm").toString());
          //  if(!directCity.equals("양방향"))directCity+="방향";

         //   directionTextView.setText(directCity);
            try{
                jackupinwonetTextView.setText(nullCheck(jsonObject.get("cmmdNm").toString()));
            }catch (JSONException|NullPointerException e){
                jackupinwonetTextView.setText("");
            }

            startHourTv.setText(nullCheck(jsonObject.get("startGongsaHour").toString()));
            startMinuteTv.setText(nullCheck(jsonObject.get("startGongsaMinute").toString()));
            endHourTv.setText(nullCheck(jsonObject.get("endGongsaHour").toString()));
            endMinuteTv.setText(nullCheck(jsonObject.get("endGongsaMinute").toString()));
            String[] chdanListSplit = jsonObject.get("trfcLimtCrgwClssCd").toString().split(",");//차단 차로 각각의 값을 String[]에 담음
            Log.i("그룹바이 결과", jsonObject.get("trfcLimtCrgwClssCd").toString());
            for (int i = 0; i < chdanListSplit.length; i++) {
                //총차로와 비교한 차단된 차로의 갯수 카운팅
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

                //커스텀 다이얼로그에서 보여줄 차단 차로의 상세 내용 셋팅
                if (chdanListSplit[i].equals("01")) {
                    chdanBangsik += ", 1차로";
                } else if (chdanListSplit[i].equals("02")) {
                    chdanBangsik += ", 2차로";
                } else if (chdanListSplit[i].equals("03")) {
                    chdanBangsik += ", 3차로";
                } else if (chdanListSplit[i].equals("04")) {
                    chdanBangsik += ", 4차로";
                } else if (chdanListSplit[i].equals("05")) {
                    chdanBangsik += ", 5차로";
                } else if (chdanListSplit[i].equals("06")) {
                    chdanBangsik += ", 6차로";
                } else if (chdanListSplit[i].equals("07")) {
                    chdanBangsik += ", 진입램프";
                } else if (chdanListSplit[i].equals("08")) {
                    chdanBangsik += ", 진출램프";
                } else if (chdanListSplit[i].equals("09")) {
                    chdanBangsik += ", 교량하부";
                } else if (chdanListSplit[i].equals("10")) {
                    chdanBangsik += ", 법면";
                } else if (chdanListSplit[i].equals("11")) {
                    chdanBangsik += ", 회차로";
                } else if (chdanListSplit[i].equals("20")) {
                    chdanBangsik += ", 이동차단";
                } else if (chdanListSplit[i].equals("30")) {
                    chdanBangsik += ", 갓길";
                }

            }
            /*roadlimitTextView.setText("총 " + jsonObject.get("totCrgwCnt").toString() + "차로 중 " + chaDanChaoCnt + "차로 차단");*/
            try{
                String chadanResult = chdanBangsik.substring(1);
                if(chadanResult.length()>20){
                    roadlimitTextView.setTextSize(9.5f);
                    roadlimitTextView.setText(chdanBangsik.substring(1));
                }else{
                    roadlimitTextView.setTextSize(11f);
                    roadlimitTextView.setText(chdanBangsik.substring(1));
                }
            }catch (Exception e){
                roadlimitTextView.setText("차단 차로 없음");
            }

            workTypeTextView.setText(jsonObject.get("workType").toString());

        } catch (JSONException|NullPointerException e) {
            Log.e("에러","에러발생");
        }
    }

    public void setGosundaeNo(String[] gosundaeArray,int length){
        switch (1){
            case 1:
                //  ET_gosundaeNum.setText(Common.nullCheck(gosundaeArray[0].replaceAll("-", "")));
                addView(com.ex.gongsa.Common.nullCheck(gosundaeArray[0].replaceAll("-", "")));
                // que_et_list.add(ET_gosundaeNum);
                if(1==length)break;
            case 2:
                //que_et_list.add(ET_gosundaeNum_no1);
             /*   view_gosundae_no1.setVisibility(View.VISIBLE);
                Li_gosundae_no1.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no1.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no1.setText(Common.nullCheck(gosundaeArray[1].replaceAll("-", "")));*/
                addView(com.ex.gongsa.Common.nullCheck(gosundaeArray[1].replaceAll("-", "")));
                if(2==length)break;
            case 3:
                //que_et_list.add(ET_gosundaeNum_no2);
             /*   view_gosundae_no2.setVisibility(View.VISIBLE);
                Li_gosundae_no2.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no2.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no2.setText(Common.nullCheck(gosundaeArray[2].replaceAll("-", "")));*/
                addView(com.ex.gongsa.Common.nullCheck(gosundaeArray[2].replaceAll("-", "")));
                if(3==length)break;
            case 4:
                // que_et_list.add(ET_gosundaeNum_no3);
                /*view_gosundae_no3.setVisibility(View.VISIBLE);
                Li_gosundae_no3.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no3.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no3.setText(Common.nullCheck(gosundaeArray[3].replaceAll("-", "")));*/
                addView(com.ex.gongsa.Common.nullCheck(gosundaeArray[3].replaceAll("-", "")));
                if(4==length)break;
            case 5:
                //   que_et_list.add(ET_gosundaeNum_no4);
               /* view_gosundae_no4.setVisibility(View.VISIBLE);
                Li_gosundae_no4.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no4.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no4.setText(Common.nullCheck(gosundaeArray[4].replaceAll("-", "")));*/
                addView(com.ex.gongsa.Common.nullCheck(gosundaeArray[4].replaceAll("-", "")));
                if(5==length)break;
        }
    }

    public void addView(@Nullable String gosundae_No){

        try {
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


            li_out.addView(li);
            //  li.addView(iv);

            gosundae_list.addView(li_out);
        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }
    }


    @Override
    public String onActionPost(String primitive, String date) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerLayout:
                if(jackupinwonetTextView.getText().length()==0||jackupinwonetTextView.getText().equals("")||jackupinwonetTextView.getText().equals("\n")){
                    Toast.makeText(this, "작업인원 및 장비가 입력되지 않아 금일작업등록이 불가능합니다.", Toast.LENGTH_SHORT).show();
                }/*else if(gosundatTextView.getText().length()==0||gosundatTextView.getText().equals("")||gosundatTextView.getText().equals("\n")){
                    Toast.makeText(this, "고속도로 순찰대 번호가 입력되지 않아 금일작업등록이 불가능합니다.", Toast.LENGTH_SHORT).show();
                }*/else if(workTypeTextView.getText().length()==0||workTypeTextView.getText().equals("")/*||workTypeTextView.equals("\n")*/){
                    Toast.makeText(this, "작업유형이 입력되지 않아 금일작업 등록이 불가능합니다", Toast.LENGTH_SHORT).show();
                }else {
                    try {

                        jsonObject.put("cstrCpprPrchTelno", SituationService.conf.USER_PHONE_NUMBER);//공사관리 테이블의 등록자를 자기 자신으로 넣어줌
                       //jsonObject.put("cstrCpprPrchTelno","010-5382-8426");//공사관리 테이블의 등록자를 자기 자신으로 넣어줌
                        Log.i("체크","------------------------------------------");
                        Log.i("체크",jsonObject.toString());
                        Log.i("체크","-------------------------------------------");

                        new CustomDialog(TodayWorkPlanRegisterDetailActivity.this, R.layout.z_user_action_dialog, R.id.userconfirmedCheck,"금일작업계획을 등록하시겠습니까?", jsonObject);
                    } catch (JSONException|NullPointerException e) {
                        Log.e("에러","에러발생");
                    }
                }


                break;
            case R.id.updateContent:
                try{
                    String check = new JSONObject(userInfo).getString("USE_CLSS_CD").toString();
                    //if(check.equals("E") || check.equals("H")){
                    if(true){
                        intent = new Intent(TodayWorkPlanRegisterDetailActivity.this, TodayWorkPlanUpdateActivity_N.class);
                        intent.putExtra("todayWorkPlanRegister", jsonObject.toString());
                        intent.putExtra("userInfo",userInfo);
                        startActivity(intent);
                    }else{
                        Toast.makeText(this,"권한이 없는 사용자입니다.",Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException|NullPointerException e){
                    Log.e("에러","에러발생");
                }

                break;
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.roadlimitTextView:
                try {
                    new CustomDialog(TodayWorkPlanRegisterDetailActivity.this, R.layout.z_road_chadan_detail, R.id.JaupclickOk, chdanBangsik);
                } catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                }
                break;
        }//switch
    }//method onClick

    /**
     * 사용자 전화번호를 가져오는 메소드
     * @return 사용자 전화번호
     */
    public String getPhoneNo() {
        TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("DEBUGGING", "This will be a final Error");
        }
        Log.e("DEBUGGING", "This will be a final Error Really");
        String phoneNo[] = com.ex.situationmanager.util.Common.phoneNumCut(systemService.getLine1Number());
        String mUserTel = phoneNo[0] + "-" + phoneNo[1] + "-" + phoneNo[2];
        mUserTel = Common.nullCheck(mUserTel);
        return mUserTel;
    }
}
