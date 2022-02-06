package com.ex.gongsa.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Common;
import com.ex.situationmanager.DialogActivity;
import com.ex.situationmanager.InnerEmployActivity;
import com.ex.situationmanager.R;
import com.ex.situationmanager.service.SituationBroadcast;
import com.ex.situationmanager.service.SituationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Authenticator;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;
import static com.ex.situationmanager.service.SituationBroadcast.getMsg;

public class WorkPlanDetailActivity extends BaseActivity implements View.OnClickListener {

    Intent intent;
    ViewGroup gotoMainBottom;
    //최창유 주석
    ViewGroup goto_update_page_li;
    TextView gotoMainTv, bonbu_jisa_Tv, guganTv;
    // TextView noSunTextView;
    // TextView gosundatTextView;
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

    LinearLayout gosundae_list;
    int chaDanChaoCnt = 0;
    String chdanBangsik = "";
    JSONObject userInforJob = null;
    String workPlanJsonValue = null;

    //
    ProgressDialog dialog = null;
    @Override
    public void onCreate(Bundle savedInsataceState) {
        super.onCreate(savedInsataceState);
        setContentView(R.layout.zz_work_plan_detail);

        intent = getIntent();


        Log.i("vvb", intent.getStringExtra("WorkPlanJsonValue"));


         gotoMainBottom = (ViewGroup) findViewById(R.id.gotoMainBottom);
        //최창유 주석
        goto_update_page_li = (ViewGroup) findViewById(R.id.goto_update_page_li);


        gotoMainTv = (TextView) findViewById(R.id.gotoMainTv);
        guganTv = (TextView) findViewById(R.id.guganTv);
        bonbu_jisa_Tv = (TextView) findViewById(R.id.bonbu_jisa_Tv);
        gosundae_list = (LinearLayout) findViewById(R.id.gosundae_list);
        //     noSunTextView = (TextView) findViewById(R.id.noSunTextView);
        //gosundatTextView = (TextView) findViewById(R.id.gosundatTextView);
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
        startHourTv = (TextView) findViewById(R.id.startHourTv);
        startMinuteTv = (TextView) findViewById(R.id.startMinuteTv);
        endHourTv = (TextView) findViewById(R.id.endHourTv);
        endMinuteTv = (TextView) findViewById(R.id.endMinuteTv);

        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);

        /*ApprovalCheck.setOnClickListener(this);*/
        gongsaPrev.setOnClickListener(this);
          gotoMainBottom.setOnClickListener(this);
        //최창유 주석
        goto_update_page_li.setOnClickListener(this);


        Log.i("유저인포", getIntent().getStringExtra("userInfo").toString());
        try {
            userInforJob = new JSONObject(getIntent().getStringExtra("userInfo").toString());

            //nullCheck( ;
            if (intent.getStringExtra("WorkPlanJsonValue") != null) {
                jsonObject = new JSONObject(intent.getStringExtra("WorkPlanJsonValue").toString());
            } else {
                jsonObject = new JSONObject(workPlanJsonValue);
            }

            if(jsonObject.get("isUpdatable").toString().equals("true")){
                if (!jsonObject.get("userId").toString().equals("SYSTEM")) {
                    if (!jsonObject.getString("mtnofLimtStatCd").equals("01")) {
                        goto_update_page_li.setVisibility(View.GONE);
                    } else {
                        if (jsonObject.getString("userId").equals(userInforJob.getString("TEL_NO")) || jsonObject.getString("cstrCrprRcrdCtnt").equals(userInforJob.getString("SMS_GRP_NM"))) {
                        } else {
                            goto_update_page_li.setVisibility(View.GONE);
                        }
                    }
                }else{
                    goto_update_page_li.setVisibility(View.GONE);
                }
            }else{
                goto_update_page_li.setVisibility(View.GONE);
            }

            try{
                Log.i("LimtPlanDates",jsonObject.get("limtPlanDates").toString()+","+jsonObject.get("limtPlanSeq").toString());
            }catch (Exception e){
                Log.e("에러","에러발생");
            }

            if (jsonObject.get("userCheck").equals("2")) {
                new CustomDialog(WorkPlanDetailActivity.this, R.layout.z_user_dialog, R.id.userCheckTextView);
            }

            Log.println(Log.ASSERT, "작업계획 조회", jsonObject.toString());

            try {
                String directCity = jsonObject.get("drctClssNm").toString();
                if (!directCity.equals("양방향")) directCity += "방향";
                bonbu_jisa_Tv.setText(nullCheck(jsonObject.get("hdqrNm").toString()) + " " + nullCheck(jsonObject.get("mtnofNm").toString()));
                guganTv.setText("[" + jsonObject.get("routeNm").toString() + "]" + directCity + " " + jsonObject.get("strtBlcPntVal").toString() + "km ~ " + jsonObject.get("endBlcPntVal").toString() + "km");
            } catch (Exception e) {
                Log.e("에러","에러발생");
                bonbu_jisa_Tv.setText("\n");
                guganTv.setText("\n");
            }

            try {
                gongsaContent.setText(nullCheck(jsonObject.get("cnstnCtnt").toString()));
            } catch (JSONException | NullPointerException e) {
                gongsaContent.setText("");
            }
            try {
                String[] gosundaeList = jsonObject.get("gosundaeNo").toString().split(",");
                setGosundaeNo(gosundaeList, gosundaeList.length);
            } catch (JSONException | NullPointerException e) {
                Log.e("에러","에러발생");
                String[] gosundaeList = new String[]{""};
                setGosundaeNo(gosundaeList, 1);
            }

            try {
                startDateTextView.setText(nullCheck(jsonObject.get("blcStrtDttm").toString()));
            } catch (JSONException | NullPointerException e) {
                Log.e("에러","에러발생");
                startDateTextView.setText("");
            }

            try {
                endDateTextView.setText(nullCheck(jsonObject.get("blcRevocDttm").toString()));
            } catch (JSONException | NullPointerException e) {
                endDateTextView.setText("");
            }

            try {
                jackupinwonetTextView.setText(nullCheck(jsonObject.get("cmmdNm").toString()));
            } catch (JSONException | NullPointerException e) {
                jackupinwonetTextView.setText("");
            }

            startHourTv.setText(nullCheck(jsonObject.get("startGongsaHour").toString()));
            startMinuteTv.setText(nullCheck(jsonObject.get("startGongsaMinute").toString()));
            endHourTv.setText(nullCheck(jsonObject.get("endGongsaHour").toString()));
            endMinuteTv.setText(nullCheck(jsonObject.get("endGongsaMinute").toString()));
            try {
                gamdokTextView.setText(nullCheck(jsonObject.get("gamdokNameEMNM").toString()));
            } catch (JSONException | NullPointerException e) {
                //    Log.e("에러","에러발생");
                gamdokTextView.setText("");
            }

            try {
                jackupinwonetTextView.setText(nullCheck(jsonObject.get("cmmdNm").toString()));
            } catch (JSONException | NullPointerException e) {
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
                } else if (chdanListSplit[i].equals("06")) {
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
                } else if (chdanListSplit[i].equals("06")) {
                    chdanBangsik += ",6차로";
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
                } else if (chdanListSplit[i].equals("12")) {
                    chdanBangsik += ",교대차단";
                }
            }
            Log.i("workTypeTextView 280",jsonObject.get("workType").toString() );
            /*   String test = ",1차로,2차로,3차로,4차로,5차로,진입램프,진출램프,교량하부,법면,회차로,이동차단,갓길";*/
            // String test = ",1차로,2차로,3차로,4차로,5차로,진입램프";
            //Toast.makeText(this,""+test.length(),Toast.LENGTH_SHORT).show();
            //roadlimitTextView.setText(chdanBangsik.substring(1));
            try {
                if (chdanBangsik.length() > 25) {
                    roadlimitTextView.setTextSize(9.5f);
                    roadlimitTextView.setText(chdanBangsik.substring(1));
                } else {
                    roadlimitTextView.setText(chdanBangsik.substring(1));
                }
            } catch (Exception e) {
                Log.e("에러","에러발생");
                roadlimitTextView.setText("차단 차로 없음");
            }

            workTypeTextView.setText(jsonObject.get("workType").toString());
            Log.i("workTypeTextView 299",jsonObject.get("workType").toString() );

        } catch (JSONException | NullPointerException e) {
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
        Log.i("cntcnt", li_out.getChildCount() + "");
        li_out.addView(view);

        //Linear Layout 추가
        final int li_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        LinearLayout li = new LinearLayout(this);
        li.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams linear_View = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, li_height, 0);

        li.setLayoutParams(linear_View);
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
        if (gosundae_No != null) {
            tv.setText(gosundae_No);
        }
        li.addView(tv);
        tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

        li_out.addView(li);
        gosundae_list.addView(li_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gotoMainBottom:
                intent = new Intent(WorkPlanDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.gongsaPrev:
                try{
                    String isUpdated = getIntent().getStringExtra("isAfterUpdate");
                    if(isUpdated.equals("Y")){
                    //    jsonObject.put("prevChk", "today");
                        userInforJob.put("curPage", "1");
                        userInforJob.put("initCheck", "Y");
                        userInforJob.put("isInfiniteTest", "Y");
                        new Action("post", SERVER_URL + "/WorkPlan/List.do", userInforJob.toString(), this).execute("");
                    }else{
                        finish();
                    }
                }catch (JSONException | NullPointerException e){
                    finish();
                }

                break;
            case R.id.ApprovalCheck:

                break;
            case R.id.roadlimitTextView:
                try {
                    new CustomDialog(WorkPlanDetailActivity.this, R.layout.z_road_chadan_detail, R.id.JaupclickOk, chdanBangsik.substring(0));
                } catch ( NullPointerException e) {
                    Log.e("에러","에러발생");
                }
                break;

            //최창유 주석
            case R.id.goto_update_page_li:
                try {
                    //최창유 주석
                    if(jsonObject.get("isUpdatable").toString().equals("true")){
                        if (!jsonObject.get("userId").toString().equals("SYSTEM")) {
                            if (!jsonObject.getString("mtnofLimtStatCd").equals("01")) {
                                Toast.makeText(this, "지사 승인 처리상태가 대기상태인 작업만 수정이 가능합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (jsonObject.getString("userId").equals(userInforJob.getString("TEL_NO")) || jsonObject.getString("cstrCrprRcrdCtnt").equals(userInforJob.getString("SMS_GRP_NM"))) {
                                    Intent intent = new Intent(this, WorkPlanListUpdate.class);
                                    intent.putExtra("WorkPlanJsonValue", jsonObject.toString());//           jsonObject = new JSONObject(intent.getStringExtra("WorkPlanJsonValue"));
                                    intent.putExtra("userInfo", getIntent().getStringExtra("userInfo").toString());


                                    Log.i("값확인","ㅇㅇㄹㄴㅇㄹ");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivityForResult(intent,RESULT_OK);
                                    Log.i("값확인","ㅇㅇㄹddddddㄴㅇㄹ");
                                    //  dialog.dismiss();

                                } else {
                                    Toast.makeText(this, "수정 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            Toast.makeText(this,"단말기에서 등록된 작업만이 수정이 가능합니다.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "상황실에서 수정한 작업은  수정이 불가능한 작업입니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException | NullPointerException e) {
                    Log.e("에러","에러발생");
                    Toast.makeText(this, "수정이 불가능한 작업입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("asdasdasdasd", "결과 받기Dsdfsdfsdfsdf");
        if(requestCode == RESULT_OK){
           // if (resultCode == Activity.RESULT_OK){
                dialog.dismiss();
                Log.e("asdasdasdasd", "결과 받기 성공");
           // }
        }
    }

    @Override
    public String onActionPost(String primitive, String date) {
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://종료
                try{
                    String isUpdated = getIntent().getStringExtra("isAfterUpdate");
                    if(isUpdated.equals("Y")){
                        //    jsonObject.put("prevChk", "today");
                        userInforJob.put("curPage", "1");
                        userInforJob.put("initCheck", "Y");
                        userInforJob.put("isInfiniteTest", "Y");
                        new Action("post", SERVER_URL + "/WorkPlan/List.do", userInforJob.toString(), this).execute("");
                    }else{
                        finish();
                    }
                }catch (JSONException | NullPointerException e){
                    finish();
                }
            break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
