package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.R;
import com.ex.situationmanager.util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class TodayWorkPlanUpdate_bak extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Intent intent;

    final int REQUEST_CODE_CAMERA = 1000;
    ImageView examplePic;


    TextView gotoMainTv, bonbuTextView, jisaTextView;
    TextView noSunTextView;
    EditText gosundatTextView;
    TextView startGuganTextView;
    TextView endGuganTextView;

    TextView startDateTextView;
    TextView endDateTextView;
    TextView jackupinwonetTextView;
    TextView directionTextView;
    TextView roadlimitTextView;
    TextView workTypeTextView;
    TextView gamdokTextView;//감독원 텍스트뷰
    TextView startHourTextView;//시작시간 텍스트뷰
    TextView startMinuteTextView;//시작분 텍스트뷰
    TextView endHourTextView;//종료 시간 텍스트뷰
    TextView endMinuteTextView;//종료 분 텍스트뷰
    TextView inwonJangbiTextView;// 인원 및 장비 좌측 텍스트뷰


    ImageView startDateImageView;
    ImageView endDateImageView;
    ImageView gongsaPrev;//뒤로가기 버튼

    Spinner gamdokSpinner;//감독원 스피너
    Spinner workTypeSpinner; //작업유형 스피너
    Spinner inwonJangbiSpinner;
    Spinner startHourSpinner;
    Spinner startMinuteSpinner;
    Spinner endHourSpinner;
    Spinner endMinuteSpinner;

    List<Map<String,String>> gamdokList=null;//감독리스트
    List<Map<String,String>> worktypeList=null;//감독리스트
    List<String> gamdokSpinnerList = null;
    List<String> workTypeSpinnerList = null;//작업유형 리스트
    List<String> inwonJangbiList = null; //인원 및 장비 리스트
    List<String> jangBiList = null;
    List<String> janbiResultList = null;
    List<String> startHourList;//공사 시작 시간 리스트
    List<String> endHourList;//공사 종료 시간 리스트
    List<String> startMinuteList;//시작 분 셋팅
    List<String> endMinuteList;//종료 분 셋팅

    EditText gongsaContent;

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;

    LinearLayout li_ResetBtn, li_registerBtn;

    private JSONArray resultStringJsongArray;
    private JSONObject resultStringJsongStr;
    JSONObject gamdokJsonObj;
    JSONObject workTypeJsonObj;

    Map<String,String> gamdokMap;
    Map<String,String> workTypeMap;

    /* boolean[] itemBoolean = null;
     String[] items=null;*/
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.z_today_work_plan_update);
        intent = getIntent();
        gamdokList=new ArrayList<Map<String,String>>();//감독리스트
        workTypeSpinnerList = new ArrayList<String>();//작업유형 리스트
        inwonJangbiList = new ArrayList<String>(); //인원 및 장비 리스트
        gamdokSpinnerList = new ArrayList<String>();
        janbiResultList = new ArrayList<String>();
        jangBiList = new ArrayList<String>();
        gamdokMap = new HashMap<String,String>();
        workTypeMap = new HashMap<String,String>();
        worktypeList = new ArrayList<Map<String,String>>();
        startHourList = new ArrayList<String>();//공사 시작 시간 리스트
        startMinuteList = new ArrayList<String>();//공사 시작 분 리스트
        endHourList = new ArrayList<String>();//종료 시간 설정
        endMinuteList = new ArrayList<String>();//종료 분 리스트
        try{
            jsonObject = new JSONObject(intent.getStringExtra("todayWorkPlanRegister"));
            Log.println(Log.ASSERT,"dd", "sdfsdf : "+ jsonObject.toString());
            Log.i("제이슨 업데이트 확인",jsonObject.toString());
            String result = Common.nullCheck(new Action("get", SERVER_URL+"/TodayWorkPlan/todayWorkupdate.do", jsonObject.toString(),this).execute("", "", "").get());
            Log.v("update",result);
            resultStringJsongStr= new JSONObject(result);
            Log.v("감독",resultStringJsongStr.get("gamDokList").toString());
            Log.v("작업",resultStringJsongStr.get("workType").toString());
            Log.v("장비",resultStringJsongStr.get("jangbiResult").toString());


        }catch (JSONException  e){
            Log.e("에러","에러발생");
        }catch (InterruptedException  e){
            Log.e("에러","에러발생");
        }catch ( ExecutionException e){
            Log.e("에러","에러발생");
        }catch (Exception e){
            Log.e("에러","에러발생");
        }


        examplePic = (ImageView) findViewById(R.id.examplePic);
        li_ResetBtn = (LinearLayout) findViewById(R.id.li_ResetBtn);
        li_registerBtn = (LinearLayout) findViewById(R.id.li_registerBtn);

        //   android:visibility="gone"




        gotoMainTv = (TextView) findViewById(R.id.gotoMainTv);
        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);
        jisaTextView = (TextView) findViewById(R.id.jisaTextView);
        noSunTextView = (TextView) findViewById(R.id.noSunTextView);
        gosundatTextView = (EditText) findViewById(R.id.gosundatTextView);
        startGuganTextView = (TextView) findViewById(R.id.startGuganTextView);
        endGuganTextView = (TextView) findViewById(R.id.endGuganTextView);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        jackupinwonetTextView = (TextView) findViewById(R.id.jackupinwonetTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);
        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView);
        gamdokTextView = (TextView) findViewById(R.id.gamdokTextView);
        startHourTextView = (TextView)findViewById(R.id.startHourTextView);
        startMinuteTextView = (TextView)findViewById(R.id.startMinuteTextView);
        endHourTextView = (TextView)findViewById(R.id.endHourTextView);
        endMinuteTextView = (TextView)findViewById(R.id.endMinuteTextView);
        inwonJangbiTextView = (TextView)findViewById(R.id.inwonJangbiTextView);

        startDateImageView =(ImageView)findViewById(R.id.startDateImageView);
        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
        endDateImageView =(ImageView)findViewById(R.id.endDateImageView);

        gamdokSpinner = (Spinner)findViewById(R.id.gamdokSpinner);
        inwonJangbiSpinner = (Spinner)findViewById(R.id.inwonJangbiSpinner);
        workTypeSpinner = (Spinner)findViewById(R.id.workTypeSpinner);
        startHourSpinner = (Spinner)findViewById(R.id.startHourSpinner);
        startMinuteSpinner= (Spinner)findViewById(R.id.startMinuteSpinner);
        endHourSpinner = (Spinner)findViewById(R.id.endHourSpinner);
        endMinuteSpinner =(Spinner)findViewById(R.id.endMinuteSpinner);

        gongsaContent = (EditText) findViewById(R.id.gongsaContent);


        li_registerBtn.setOnClickListener(this);
        li_ResetBtn.setOnClickListener(this);
        startDateImageView.setOnClickListener(this);
        gongsaPrev.setOnClickListener(this);
        workTypeTextView.setOnClickListener(this);
        gamdokTextView.setOnClickListener(this);
        jackupinwonetTextView.setOnClickListener(this);
        inwonJangbiTextView.setOnClickListener(this);
        endDateImageView.setOnClickListener(this);
        startHourTextView.setOnClickListener(this);
        startMinuteTextView.setOnClickListener(this);
        endHourTextView.setOnClickListener(this);
        endMinuteTextView.setOnClickListener(this);
        try {

            bonbuTextView.setText(jsonObject.get("hdqrNm").toString());
            jisaTextView.setText(jsonObject.get("mtnofNm").toString());
            noSunTextView.setText(jsonObject.get("routeNm").toString());
            try{
                gongsaContent.setText(jsonObject.get("cnstnCtnt").toString());
            }catch (JSONException e){
                gongsaContent.setText("");
            } catch (NullPointerException e) {
                gongsaContent.setText("");
            } catch (Exception e) {
                gongsaContent.setText("");
            }

            try{
                if(((String[])jsonObject.get("gosundaeNo").toString().split("-")).length!=1){

                    String[] gosundaeValue =jsonObject.get("gosundaeNo").toString().split("-");
                    gosundatTextView.setText(gosundaeValue[0]+gosundaeValue[1]+gosundaeValue[2]);
                }
            }catch (NullPointerException e){
                gosundatTextView.setText("");
            }catch (JSONException e){
                gosundatTextView.setText("");
            }catch (Exception e){
                gosundatTextView.setText("");
            }

            startGuganTextView.setText(jsonObject.get("strtBlcPntVal").toString());
            endGuganTextView.setText(jsonObject.get("endBlcPntVal").toString());
            try{
                startDateTextView.setText(jsonObject.get("blcStrtDttm").toString());
            }catch (JSONException e){
                startDateTextView.setText("");
            }catch (NullPointerException e){
                startDateTextView.setText("");
            }catch (Exception e){
                startDateTextView.setText("");
            }
           try{
               endDateTextView.setText(jsonObject.get("blcRevocDttm").toString());
           }catch (JSONException e){
               endDateTextView.setText("");
           }catch (NullPointerException e){
               endDateTextView.setText("");
           }catch (Exception e){
               endDateTextView.setText("");
           }
            startHourTextView.setText(jsonObject.get("startGongsaHour").toString());
            startMinuteTextView.setText(jsonObject.get("startGongsaMinute").toString());
            endHourTextView.setText(jsonObject.get("endGongsaHour").toString());
            endMinuteTextView.setText(jsonObject.get("endGongsaMinute").toString());
            try {
                gamdokTextView.setText(jsonObject.get("gamdokNameEMNM").toString());
            } catch (JSONException e) {
                gamdokTextView.setText("");
            } catch (NullPointerException e) {
                gamdokTextView.setText("");
            } catch (Exception e) {
                gamdokTextView.setText("");
            }
            try{
                jackupinwonetTextView.setText(jsonObject.get("cmmdNm").toString());
            }catch (JSONException e){
                jackupinwonetTextView.setText("\n");
            }catch (NullPointerException e){
                jackupinwonetTextView.setText("\n");
            }catch (Exception e){
                jackupinwonetTextView.setText("\n");
            }
            String directCity=jsonObject.get("drctClssNm").toString();
            if(!jsonObject.get("drctClssNm").toString().equals("양방향")) directCity+="방향";

            directionTextView.setText(directCity);
        /*    } else if (jsonObject.get("drctClssCd").toString().equalsIgnoreCase("O")) {
                directionTextView.setText("양방향");
            } else {
                directionTextView.setText("시점방향");
            }*/
            try{
                jackupinwonetTextView.setText(jsonObject.get("cmmdNm").toString());
            }catch (JSONException e){

                jackupinwonetTextView.setText("인원 0명, 사인카 0대, 작업차 0대");
            }catch (NullPointerException e){

                jackupinwonetTextView.setText("인원 0명, 사인카 0대, 작업차 0대");
            }catch (Exception e){

                jackupinwonetTextView.setText("인원 0명, 사인카 0대, 작업차 0대");
            }


            if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("01")) {
                roadlimitTextView.setText("1차로");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("02")) {
                roadlimitTextView.setText("2차로");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("03")) {
                roadlimitTextView.setText("3차로");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("04")) {
                roadlimitTextView.setText("4차로");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("05")) {
                roadlimitTextView.setText("5차로");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("07")) {
                roadlimitTextView.setText("진입램프");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("08")) {
                roadlimitTextView.setText("진출램프");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("09")) {
                roadlimitTextView.setText("교량하부");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("10")) {
                roadlimitTextView.setText("법면");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("11")) {
                roadlimitTextView.setText("회차로");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("20")) {
                roadlimitTextView.setText("이동차단");
            } else if (jsonObject.get("trfcLimtCrgwClssCd").toString().equalsIgnoreCase("30")) {
                roadlimitTextView.setText("갓길");
            }

            workTypeTextView.setText(jsonObject.get("workType").toString());
         /*   if(jsonObject.get("trfcLimtStatCd").toString().equals("01")){
                workApprovalCheck.setText("대기");
            }else if(jsonObject.get("trfcLimtStatCd").toString().equals("12")) {
                workApprovalCheck.setText("승인");
            }else{
                workApprovalCheck.setText("반려");
            }*/

            int chaDanChaoCnt = 0;
            String chdanBangsik = "";
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
                }else if (chdanListSplit[i].equals("06")) {
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
            Log.i("chdanBangsik",chdanBangsik.length()+"");
            if(chdanBangsik.length()>20){
                roadlimitTextView.setTextSize(9.5f);
                roadlimitTextView.setText(chdanBangsik.substring(1));
            }else{
                roadlimitTextView.setText(chdanBangsik.substring(1));
            }




            //감독원gamdokMap

            jsonArray = new JSONArray(resultStringJsongStr.get("gamDokList").toString());
            for(int i =0;i<jsonArray.length();i++){
                gamdokJsonObj = jsonArray.getJSONObject(i);

                gamdokMap.put("HDQR_CD",gamdokJsonObj.get("HDQR_CD").toString());
                gamdokMap.put("MTNOF_CD",gamdokJsonObj.get("MTNOF_CD").toString());
                gamdokMap.put("EMNM",gamdokJsonObj.get("EMNM").toString());
                gamdokMap.put("TEL_NO",gamdokJsonObj.get("TEL_NO").toString());
                gamdokMap.put("USER_EMNO",gamdokJsonObj.get("USER_EMNO").toString());
                gamdokMap.put("mtnofPrchEmno",gamdokJsonObj.get("USER_EMNO").toString());
                gamdokList.add(gamdokMap);
                gamdokSpinnerList.add(gamdokList.get(i).get("EMNM"));
            }

      /*      if(jsonObject.get("gamdokNameEMNM").toString().equals())
                gamdokSpinnerList.add(gamdomList.get(i).get("EMNM"));   */
            for(int i=0;i<gamdokSpinnerList.size();i++){
                if(jsonObject.get("gamdokNameEMNM").toString().equals(gamdokSpinnerList.get(i))){
                    Collections.swap(gamdokSpinnerList,0,i);
                }
            }

            ArrayAdapter<String> gamdokAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,gamdokSpinnerList);
            gamdokSpinner.setOnItemSelectedListener(this);
            gamdokSpinner.setAdapter(gamdokAdapter);
            gamdokSpinner.setSelection(gamdokSpinner.getSelectedItemPosition());


            // 작업유형
            jsonArray = new JSONArray(resultStringJsongStr.get("workType").toString());
            for(int i = 0 ; i < jsonArray.length();i++){
                workTypeJsonObj = jsonArray.getJSONObject(i);
                workTypeMap.put("cmmnCd",workTypeJsonObj.get("cmmnCd").toString());
                workTypeMap.put("cmmnCdNm",workTypeJsonObj.get("cmmnCdNm").toString());
                worktypeList.add(workTypeMap);
                workTypeSpinnerList.add(worktypeList.get(i).get("cmmnCdNm"));
            }
            for(int i =0 ;i < workTypeSpinnerList.size();i++){
                if(workTypeSpinnerList.get(i).equals(workTypeTextView.getText().toString())){
                    Collections.swap(workTypeSpinnerList,0,i);
                }
            }
            ArrayAdapter<String> workTypeAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,workTypeSpinnerList);
            workTypeSpinner.setOnItemSelectedListener(this);
            workTypeSpinner.setAdapter(workTypeAdapter);
            workTypeSpinner.setSelection(0);


            //--인원 및 장비
            inwonJangbiList.add("선택");
            inwonJangbiList.add("작업원");
            inwonJangbiList.add("사인카");
            inwonJangbiList.add("작업차");
            ArrayAdapter<String> inwonJangbiAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,inwonJangbiList);
            inwonJangbiSpinner.setOnItemSelectedListener(this);
            inwonJangbiSpinner.setAdapter(inwonJangbiAdapter);
            inwonJangbiSpinner.setSelection(0);
            inwonJangbiTextView.setText(inwonJangbiSpinner.getSelectedItem().toString());

            jsonArray = new JSONArray(resultStringJsongStr.get("jangbiResult").toString());
            for(int i = 1 ; i < jsonArray.length();i++){
                jangBiList.add(jsonArray.getJSONObject(i).get("CMMN_CD_NM").toString());
            }
            //장비용 논리배열, 장비배열
         /*   itemBoolean = new boolean[jangBiList.size()];
            items = new String[jangBiList.size()];*/


/*
            startHourList = new ArrayList<String>();//공사 시작 시간 리스트
            startMinuteList = new ArrayList<String>();//공사 시작 분 리스트
            endHourList = new ArrayList<String>();//종료 시간 설정
            endMinuteList =*/

            //시간
            startHourList.add(0,startHourTextView.getText().toString());
         /*   startMinuteList.add(0,startMinuteTextView.getText().toString());
            endHourList.add(0,endHourTextView.getText().toString());
            endMinuteList.add(0,endMinuteTextView.getText().toString());*/
            for (int i = 0; i <= 23; i++) {
                startHourList.add(Integer.toString(i));
            }
            ArrayAdapter<String> startHourAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,startHourList);
            startHourSpinner.setOnItemSelectedListener(this);
            startHourSpinner.setAdapter(startHourAdapter);
            startHourSpinner.setSelection(0);

            startMinuteList.add(startMinuteTextView.getText().toString());
            for(int i = 0 ; i <= 59; i++){
                startMinuteList.add(Integer.toString(i));
            }
            ArrayAdapter<String> startMinuteAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,startMinuteList);
            startMinuteSpinner.setOnItemSelectedListener(this);
            startMinuteSpinner.setAdapter(startMinuteAdapter);
            startMinuteSpinner.setSelection(0);

            endHourList.add(0,endHourTextView.getText().toString());
            for (int i = 0; i <= 23; i++) {
                endHourList.add(Integer.toString(i));
            }
            ArrayAdapter<String> endHourAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,endHourList);
            endHourSpinner.setOnItemSelectedListener(this);
            endHourSpinner.setAdapter(endHourAdapter);
            endHourSpinner.setSelection(0);
            endMinuteList.add(endMinuteTextView.getText().toString());

            for(int i = 0 ;i<=59;i++){
                endMinuteList.add(Integer.toString(i));
            }
            ArrayAdapter<String> endMinuteAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,endMinuteList);
            endMinuteSpinner.setOnItemSelectedListener(this);
            endMinuteSpinner.setAdapter(endMinuteAdapter);
            endMinuteSpinner.setSelection(0);
        } catch (JSONException|NullPointerException e) {
            Log.e("에러","에러발생");
        }


        gongsaContent.setTextColor(-4276546);
        gongsaContent.setTextColor(-4276546);

        gosundatTextView.setTextColor(-4276546);
        gosundatTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public String onActionPost(String primitive, String date) {

        return null;
    }

    public void datePickerMethod(final TextView text) {

        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(TodayWorkPlanUpdate_bak.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                try {
                    /*String yearMonth = String.format("%d/%d/%d", year, month + 1, date);*/
                    String yearResult = Integer.toString(year);
                    String monthResult = Integer.toString(month + 1);
                    String dateResult = Integer.toString(date);
                    if (monthResult.length() == 1)
                        monthResult = "0" + monthResult;

                    if (dateResult.length() == 1)
                        dateResult = "0" + dateResult;

                    // String yearMonth = String.format("%d/%d/%d", year, monthResult , dateResult);
                    String yearMonth = year + "/" + monthResult + "/" + dateResult;
                    text.clearComposingText();
                    text.setText(yearMonth);
                } catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                }catch (Exception e) {
                    Log.e("에러","에러발생");
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        datePicker.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.gamdokSpinner:
                parent.setSelection(position);
                gamdokTextView.clearComposingText();
                gamdokTextView.setText(parent.getSelectedItem().toString());
                try{
                    jsonObject.put("gamdokNameEMNM",gamdokTextView.getText().toString());
                    jsonObject.put("mtnofPrchEmno",gamdokList.get(position).get("USER_EMNO"));
                    jsonObject.put("gamdokPhoneNo",gamdokList.get(position).get("TEL_NO"));


                }catch (JSONException|NullPointerException e){
                    Log.e("에러","에러발생");
                }

                break;
            case R.id.workTypeSpinner:
                parent.setSelection(position);
                workTypeTextView.clearComposingText();
                workTypeTextView.setText(parent.getSelectedItem().toString());
                break;
            case R.id.inwonJangbiSpinner:
                parent.setSelection(position);
                inwonJangbiTextView.clearComposingText();
                inwonJangbiTextView.setText(parent.getSelectedItem().toString());
                if(parent.getSelectedItem().toString().equals("작업원")){
                    inwonEditText();
                }else if(parent.getSelectedItem().toString().equals("사인카")){
                    signCarSetting();
                }else if(parent.getSelectedItem().toString().equals("작업차")){
                    workCarSetting();
                }
                parent.setSelection(0);
        /*        inwonJangbiTextView.clearComposingText();
                inwonJangbiTextView.setText(parent.getSelectedItem().toString());*/
                break;
            case R.id.startHourSpinner:
                parent.setSelection(position);
                startHourTextView.clearComposingText();
                startHourTextView.setText(parent.getSelectedItem().toString());
                break;
            case R.id.startMinuteTextView:
                startHourSpinner.setSelection(position);
                startHourTextView.clearComposingText();
                startHourTextView.setText(parent.getSelectedItem().toString());
                break;
            case R.id.endHourSpinner:
                endHourSpinner.setSelection(position);
                endHourTextView.clearComposingText();
                endHourTextView.setText(parent.getSelectedItem().toString());
                break;
            case R.id.endMinuteSpinner:
                endMinuteSpinner.setSelection(position);
                endMinuteTextView.clearComposingText();
                endMinuteTextView.setText(parent.getSelectedItem().toString());
                break;
        }

    }//onItemed

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void inwonEditText() {


        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("인원을 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] inwonTextViewStr = jackupinwonetTextView.getText().toString().split(", ");
                        inwonTextViewStr[0] = "인원 "+editText.getText()+"명, ";

                        jackupinwonetTextView.setText( inwonTextViewStr[0]+ inwonTextViewStr[1]+", "+ inwonTextViewStr[2]);
                        //   codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }




    public void signCarSetting() {


        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("사용 사인카의 수량을 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] inwonTextViewStr = jackupinwonetTextView.getText().toString().split(", ");
                        inwonTextViewStr[1] = "사인카 "+editText.getText()+"대, ";

                        jackupinwonetTextView.setText( inwonTextViewStr[0]+", "+ inwonTextViewStr[1]+ inwonTextViewStr[2]);
                        //     codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }


    public void workCarSetting() {


        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("인원을 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;

                        String[] inwonTextViewStr = jackupinwonetTextView.getText().toString().split(", ");
                        inwonTextViewStr[2] = "작업차 "+editText.getText()+"대";

                        jackupinwonetTextView.setText( inwonTextViewStr[0]+", "+ inwonTextViewStr[1]+", "+ inwonTextViewStr[2]);
                        // codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }



 /*   public void settingJangbi(){



        for(int i = 0 ; i < items.length;i++){
            items[i]=jangBiList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("장비를 선택해주세요.")
               .setMultiChoiceItems(items, itemBoolean, new DialogInterface.OnMultiChoiceClickListener() {

                   @Override
                   public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                       if(isChecked){
                           itemBoolean[which]=true;
                           janbiResultList.add(items[which]);
                       } else {
                           itemBoolean[which]=false;
                           for(int i  =0 ; i < janbiResultList.size();i++){
                               if(items[which].equals(janbiResultList.get(i))){
                                   janbiResultList.remove(i);
                               }
                           }
                       }
                   }
               }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] inwonArray = jackupinwonetTextView.getText().toString().split(",");
                inwonArray[2]=janbiResultList.get(0)+" 외 "+Integer.toString(janbiResultList.size()-1)+"대";
                Log.i("인원결과",inwonArray[0]);
                String inwonResult = inwonArray[0]+", "+inwonArray[1]+", "+inwonArray[2];
                jackupinwonetTextView.clearComposingText();
                jackupinwonetTextView.setText(inwonResult);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();


    }*/



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.li_registerBtn:
                try {
                    if(inwonJangbiTextView.getText().equals("인원 0명, 사인카 0대, 작업차 0대")){
                        Toast.makeText(this,"인원 장비를 입력해주세요",Toast.LENGTH_SHORT).show();
                    }else if(gosundatTextView.getText().equals("\n")||gosundatTextView.getText().equals("")){
                        Toast.makeText(this,"고속도로 순찰대 번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                    }else{
                        intent = new Intent(TodayWorkPlanUpdate_bak.this, TodayWorkPlanRegisterDetailActivity.class);
                        jsonObject.put("cnstnCtnt", gongsaContent.getText().toString());
                        jsonObject.put("blcStrtDttm",startDateTextView.getText().toString());//공사 시작시간
                        jsonObject.put("blcRevocDttm",endDateTextView.getText().toString());//종료시간
                        jsonObject.put("workType",workTypeTextView.getText().toString());
                        jsonObject.put("startGongsaHour",startHourTextView.getText().toString());
                        jsonObject.put("startGongsaMinute",startMinuteTextView.getText().toString());
                        jsonObject.put("endGongsaHour",endHourTextView.getText().toString());
                        jsonObject.put("endGongsaMinute",endMinuteTextView.getText().toString());
                        jsonObject.put("cmmdNm",jackupinwonetTextView.getText().toString());
                        jsonObject.put("humanCnt"," 장비인원목록:"+jackupinwonetTextView.getText().toString());
                        if(((String[])gosundatTextView.getText().toString().split("-")).length==1){
                            String result =    gosundatTextView.getText().toString().subSequence(0, 4)+"-"+gosundatTextView.getText().toString().subSequence(4, 8)+"-"+gosundatTextView.getText().toString().subSequence(8, 15);
                            jsonObject.put("gosundaeNo",result);
                        }else{
                            jsonObject.put("gosundaeNo",gosundatTextView.getText().toString());
                        }

                        intent.putExtra("todayWorkPlanRegister", jsonObject.toString());

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                } catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                } catch (JSONException e) {
                    Log.e("에러","에러발생");
                } catch (Exception e) {
                    Log.e("에러","에러발생");
                }

                break;

            case R.id.li_ResetBtn:
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                break;
            case R.id.gongsaPrev:
                finish();
                ;
                break;
            case R.id.gamdokTextView:
                gamdokSpinner.performClick();
                break;
            case R.id.workTypeTextView:

                workTypeSpinner.performClick();
                break;
            case R.id.inwonJangbiTextView:
                inwonJangbiSpinner.performClick();
                break;
            case R.id.startHourTextView:
                startHourSpinner.performClick();
                break;
            case R.id.startMinuteTextView:
                startHourSpinner.performClick();
                break;
            case R.id.endHourTextView:
                endHourSpinner.performClick();
                break;
            case R.id.endMinuteTextView:
                endMinuteSpinner.performClick();
                break;
            case R.id.startDateImageView:
                datePickerMethod(startDateTextView);
                break;
            case R.id.endDateImageView:
                datePickerMethod(endDateTextView);
                break;
        }//switch
    }//method onClick
}
