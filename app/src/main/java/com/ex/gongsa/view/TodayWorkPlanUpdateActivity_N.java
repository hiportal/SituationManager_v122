package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Common;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;
import static com.ex.gongsa.view.AdapterSpinnerUtil.maxCharo;

public class TodayWorkPlanUpdateActivity_N  extends BaseActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    // 2021.07 금일작업등록에서 수정페이지
    Intent intent;

    Spinner bonbuSpinner;//본부 스피너
    Spinner jisaSpinner;//지사 스피너
    Spinner nosunSpinner;//노선 스피너
    Spinner direnctionSpinner;//이정 스피너
    Spinner charoSpinner;
    Spinner workTypeSpinner;//작업유형 스피너
    Spinner gamdokSpinner;
    Spinner startHourSpinner;
    Spinner startMinuteSpinner;
    Spinner endHourSpinner;
    Spinner endMinuteSpinner;
    Spinner inwonJangbiSpinner;
    Spinner workTypeCATSpinner; // 2021.07 작업유형(대분류) 스피너

    TextView bonbuTextView;//본부 텍스트 뷰
    TextView jisaTextView;//지사 텍스트 뷰
    TextView noSunTextView;//노선 텍스트뷰
    TextView directionTextView;//이정 텍스트뷰
    TextView roadlimitTextView;//차로제한
    TextView inwonJangbiResultTextView;//인원 장비 텍스트뷰
    TextView startDateTextView;
    TextView endDateTextView;
    TextView workTypeTextView;//작업유형 텍스트뷰
    TextView gamdokwonTextView;
    TextView startHourTextView;
    TextView startMinuteTextView;
    TextView endHourTextView;
    TextView endMinuteTextView;
    TextView inwonJangbiTextView;
    TextView workTypeCATTextView; // 2021.07 작업유형(대분류)스피너

    EditText ET_startGongsaGugan;//시작구간
    EditText ET_endGongsaGugan;//종료 공사구간
    EditText ET_gongsaContent;//공사내용
 //   EditText ET_gosundaeNum;//고순대번호

    ImageView startDateImageView;
    ImageView endDateImageView;
    ImageView gongsaPrev;
    ImageView plus_btn;

    JSONObject jsonObject;
    JSONObject jsonResultObject;
    JSONArray jsonArray;

    List<Map<String,String>> bonbuMapList = null;//본부맵
    List<Map<String,String>> jisaMapList = null;//지사맵
    List<Map<String,String>> nosunMapList = null;//노선맵리스트
    List<Map<String,String>> iJungMapList = null;
    List<Map<String,String>> gamdokMapList = null;
    //202009_작업유형수정중
    List<Map<String, String>> workTypeMapList = null; //작업유형 맵
    //2021.07 작업유형(대분류) 맵
    Map<String, String> workTypeCATMap;
    List<Map<String, String>> workTypeCATMapList = null;
    String[] workTypeCATNM;
    String[] workTypeCATCD;


    List<String> bonbuList = null;//본부리스트
    List<String> jisaList = null;//지사 리스트
    List<String> nusunList = null;//노선리스트
    List<String> ijungList =null;//이정 리스트
    List<String> workTypeList=null;
    List<String> gamdokList=null;
    List<String> inwonJangbiList=null;
    ArrayList<String> totalroadLimit;
    List<String> startHourList = null;
    List<String> startMinList = null;
    List<String> endHourList = null;
    List<String> endMinList = null;
    List<String> workTypeCATList; // 2021.07 작업유형(대분류) 리스트

    ArrayAdapter<String> jisaAdapter;

    LinearLayout li_registerBtn;//등록버튼
    LinearLayout li_ResetBtn;
    LinearLayout gosundae_list;

    int chaDanChaoCnt;
    String chdanBangsik="";
    int maxCharo;

    double maxGugan=0.0;
    double minGugan=0.0;
    String todayParam;
    String[] roadLimit;//차로제한 리스트

    String userCheck = "";

    String userInfo = "";
    String gamdokTvResult = "";
    String gamdokTelno="";
    String gamdokSawonNo="";

    String[] gamdokName =null;
    boolean[] gamdokItemBoo = null;
    AlertDialog.Builder gamdokdialog;
    JSONArray gamdokJsonArray;

    List<EditText> que_et_list = new ArrayList<EditText>();




    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.zz_today_work_plan_update);
        intent = getIntent();

        li_registerBtn = (LinearLayout)findViewById(R.id.li_registerBtn);
        li_ResetBtn = (LinearLayout)findViewById(R.id.li_ResetBtn);
        gosundae_list = (LinearLayout)findViewById(R.id.gosundae_list);
        bonbuSpinner = (Spinner)findViewById(R.id.bonbuSpinner);//본부 스피너
        jisaSpinner = (Spinner)findViewById(R.id.jisaSpinner);//지사 스피너
        nosunSpinner = (Spinner)findViewById(R.id.nosunSpinner);
        direnctionSpinner = (Spinner)findViewById(R.id.direnctionSpinner);
        charoSpinner = (Spinner)findViewById(R.id.charoSpinner);
        workTypeSpinner =(Spinner)findViewById(R.id.workTypeSpinner);
        gamdokSpinner =(Spinner)findViewById(R.id.gamdokSpinner);
        startHourSpinner =(Spinner)findViewById(R.id.startHourSpinner);
        startMinuteSpinner=(Spinner)findViewById(R.id.startMinuteSpinner);
        endHourSpinner=(Spinner)findViewById(R.id.endHourSpinner);
        endMinuteSpinner=(Spinner)findViewById(R.id.endMinuteSpinner);
        inwonJangbiSpinner = (Spinner)findViewById(R.id.inwonJangbiSpinner);
        //2021.07 작업유형(대분류)
        workTypeCATSpinner = (Spinner) findViewById(R.id.workTypeCATSpinner); //작업유형(대분류)

        bonbuTextView = (TextView)findViewById(R.id.bonbuTextView);//본부 텍스트뷰
        jisaTextView = (TextView)findViewById(R.id.jisaTextView);//지사 텍스트뷰
        noSunTextView = (TextView)findViewById(R.id.noSunTextView);
        directionTextView = (TextView)findViewById(R.id.directionTextView);
        roadlimitTextView=(TextView)findViewById(R.id.roadlimitTextView);
        inwonJangbiResultTextView=(TextView)findViewById(R.id.inwonJangbiResultTextView);
        startDateTextView = (TextView)findViewById(R.id.startDateTextView);
        endDateTextView = (TextView)findViewById(R.id.endDateTextView);
        workTypeTextView =(TextView)findViewById(R.id.workTypeTextView);//작업유형 텍스트뷰
        gamdokwonTextView =(TextView)findViewById(R.id.gamdokwonTextView);
        startHourTextView =(TextView)findViewById(R.id.startHourTextView);
        startMinuteTextView =(TextView) findViewById(R.id.startMinuteTextView);
        endHourTextView=(TextView)findViewById(R.id.endHourTextView);
        endMinuteTextView=(TextView)findViewById(R.id.endMinuteTextView);
        inwonJangbiTextView=(TextView)findViewById(R.id.inwonJangbiTextView);
        workTypeCATTextView = (TextView) findViewById(R.id.workTypeCATTextView); //2021.07 작업유형(대분류)


        ET_startGongsaGugan=(EditText)findViewById(R.id.ET_startGongsaGugan);
        ET_endGongsaGugan =(EditText)findViewById(R.id.ET_endGongsaGugan);
        ET_gongsaContent =(EditText)findViewById(R.id.ET_gongsaContent);
        //ET_gosundaeNum=(EditText)findViewById(R.id.ET_gosundaeNum);

        startDateImageView = (ImageView)findViewById(R.id.startDateImageView);
        endDateImageView = (ImageView)findViewById(R.id.endDateImageView);
        gongsaPrev = (ImageView)findViewById(R.id.gongsaPrev);
        plus_btn = (ImageView)findViewById(R.id.plus_btn);//고순대 추가 버튼

        bonbuMapList = new ArrayList<Map<String,String>>();
        jisaMapList = new ArrayList<Map<String,String>>();
        nosunMapList = new ArrayList<Map<String,String>>();
        iJungMapList = new ArrayList<Map<String,String>>();
        gamdokMapList = new ArrayList<Map<String,String>>();
        //202009_작업유형수정중
        workTypeMapList = new ArrayList<Map<String, String>>();
        workTypeCATMapList = new ArrayList<Map<String, String>>(); //2021.07 작업유형


        bonbuList=new ArrayList<String>();
        jisaList = new ArrayList<String>();
        nusunList = new ArrayList<String>();
        ijungList = new ArrayList<String>();
        workTypeList = new ArrayList<String>();
        gamdokList=new ArrayList<String>();
        inwonJangbiList=new ArrayList<String>();
        totalroadLimit= new ArrayList<String>();
       startHourList = new ArrayList<String>();;
       startMinList = new ArrayList<String>();;
        endHourList = new ArrayList<String>();;
       endMinList = new ArrayList<String>();;


        bonbuTextView.setOnClickListener(this);
        jisaTextView.setOnClickListener(this);
        noSunTextView.setOnClickListener(this);
        directionTextView.setOnClickListener(this);
        roadlimitTextView.setOnClickListener(this);
        workTypeTextView.setOnClickListener(this);
        gamdokwonTextView.setOnClickListener(this);
        startDateImageView.setOnClickListener(this);
        endDateImageView.setOnClickListener(this);
        startHourTextView.setOnClickListener(this);
        startMinuteTextView.setOnClickListener(this);
        endHourTextView.setOnClickListener(this);
        endMinuteTextView.setOnClickListener(this);
        inwonJangbiTextView.setOnClickListener(this);
        li_registerBtn.setOnClickListener(this);
        li_ResetBtn.setOnClickListener(this);
        gongsaPrev.setOnClickListener(this);
        plus_btn.setOnClickListener(this);//고순대 접수번호 +버튼
        workTypeCATTextView.setOnClickListener(this); // 2021.07 작업유형(대분류)

        Log.println(Log.ASSERT,"변화여부 확인", "변화여부 확인 : "+ intent.getStringExtra("todayWorkPlanRegister"));

        todayParam=intent.getStringExtra("todayWorkPlanRegister");
        userInfo=intent.getStringExtra("userInfo");

        try {

            jsonResultObject = new JSONObject(todayParam);
            try{
                userCheck =jsonResultObject.getString("USE_CLSS_CD");
            }catch (JSONException e){
                Log.e("에러","에러발생");
            }catch (Exception e){
                Log.e("에러","에러발생");
            }
            ET_startGongsaGugan.setText(jsonResultObject.get("strtBlcPntVal").toString());
            ET_endGongsaGugan.setText(jsonResultObject.get("endBlcPntVal").toString());
            ET_gongsaContent.setText(jsonResultObject.get("cnstnCtnt").toString());


            String[] gosundaeList = jsonResultObject.get("gosundaeNo").toString().split(",");



            setGosundaeNo(gosundaeList,gosundaeList.length);

            ArrayList<String> bonbuParam = new ArrayList<String>();
            bonbuParam.add("hdqrCd");
            bonbuParam.add("hdqrNm");
            setAdapterModule(bonbuSpinner,bonbuMapList,bonbuList,SERVER_URL + "/TodayWorkPlan/settingBonbu.do",todayParam,bonbuParam,"hdqrNm",bonbuTextView,"firstAction");
///----------------------------w

            ArrayList<String> jisaParam = new ArrayList<String>();
            jisaParam.add("mtnofCd");
            jisaParam.add("mtnofNm");
            setAdapterModule(jisaSpinner,jisaMapList,jisaList,SERVER_URL + "/TodayWorkPlan/settingJisa.do",todayParam,jisaParam,"mtnofNm",jisaTextView,"firstAction");
        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }catch ( JSONException e){
            Log.e("에러","에러발생");
        }catch ( Exception e){
            Log.e("에러","에러발생");
        }

        try{

            ArrayList<String> nosunParam = new ArrayList<String>();
            nosunParam.add("routeNm");
            nosunParam.add("routeCd");
            setAdapterModule(nosunSpinner,nosunMapList,nusunList,SERVER_URL + "/TodayWorkPlan/settingNosun.do",todayParam,nosunParam,"routeNm",noSunTextView,"firstAction");
        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }catch (Exception e){
            Log.e("에러","에러발생");
        }

        try{

            ArrayList<String> ijungParam = new ArrayList<String>();
            ijungParam.add("drctClssNm");
            ijungParam.add("drctClssCd");
            setAdapterModule(direnctionSpinner,iJungMapList,ijungList,SERVER_URL + "/TodayWorkPlan/settingInjung.do",todayParam,ijungParam,"drctClssNm",directionTextView,"firstAction");

            //차로제한
            jsonObject =new JSONObject(todayParam);
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
                } else if (chdanListSplit[i].equals("12")) {
                    chdanBangsik += ", 교대차단";
                }

            }
          //  roadlimitTextView.setText("총 "+jsonObject.get("totCrgwCnt")+" 중 " +chdanBangsik);
           try{
               maxCharo=Integer.parseInt(jsonObject.get("totCrgwCnt").toString());
               if(chdanBangsik.length()>20){
                   roadlimitTextView.setTextSize(9.5f);
                   /* roadlimitTextView.setTextSize(10.5f);*/
                   roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + chdanBangsik.substring(1) + " 차단");
               }else{
                   roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + chdanBangsik.substring(1) + " 차단");
               }
           }catch (NullPointerException e){
               roadlimitTextView.setText("차단 차로 없음");
           }catch (JSONException e){
               roadlimitTextView.setText("차단 차로 없음");
           }catch (StringIndexOutOfBoundsException e){
               roadlimitTextView.setText("차단 차로 없음");
           }catch (ArrayIndexOutOfBoundsException e){
               roadlimitTextView.setText("차단 차로 없음");
           }catch (Exception e){
               roadlimitTextView.setText("차단 차로 없음");
           }

            try{
                inwonJangbiResultTextView.setText(jsonObject.get("cmmdNm").toString());
            }catch (NullPointerException e){
                Log.e("에러","에러발생");
                inwonJangbiResultTextView.setText("인원 0명, 사인카 0대, 작업차 0대");
            }catch (JSONException e){
                Log.e("에러","에러발생");
                inwonJangbiResultTextView.setText("인원 0명, 사인카 0대, 작업차 0대");
            }catch (Exception e){
                Log.e("에러","에러발생");
                inwonJangbiResultTextView.setText("인원 0명, 사인카 0대, 작업차 0대");
            }


        }catch (JSONException e){
            Log.e("에러","에러발생");
        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }catch (Exception e){
            Log.e("에러","에러발생");
        }

        //2021.07 작업유형(대분류)


        try{
            workTypeCATMap = new HashMap<String,String>();
            workTypeCATMap.put("노면보수작업","021");
            workTypeCATMap.put("조경작업","022");
            workTypeCATMap.put("터널내작업","023");
            workTypeCATMap.put("교량작업","024");
            workTypeCATMap.put("시설물설치","025");
            workTypeCATMap.put("기타","020");


            workTypeCATMapList.add(workTypeCATMap);
            workTypeCATList.add("노면보수작업");
            workTypeCATList.add("조경작업");
            workTypeCATList.add("터널내작업");
            workTypeCATList.add("교량작업");
            workTypeCATList.add("시설물설치");
            workTypeCATList.add("비탈면보수");



           /* new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeCATSpinner, workTypeCATList);
            workTypeCATSpinner.setSelection(0);
            workTypeCATTextView.setText(workTypeCATSpinner.getSelectedItem().toString());*/


        }catch (Exception e){

        }

        try{
            //2021.07 작업유형(대분류) - 코드값이랑 순서가 같아야함
            workTypeCATNM = getResources().getStringArray(R.array.workTypeCategorizationName);
            workTypeCATCD = getResources().getStringArray(R.array.workTypeCategorizationCode);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workTypeCATNM);
            workTypeCATSpinner.setAdapter(adapter);
            workTypeCATSpinner.setSelection(0);

        }catch (Exception e){

        }

        try{
            //작업유형
            ArrayList<String> workTypeParam = new ArrayList<String>();
            workTypeParam.add("workType");
            //202009_작업유형
            workTypeParam.add("workTypeCd");

            //2021.07 작업유형(대분류)
            //setAdapterModule(workTypeSpinner,null,workTypeList,SERVER_URL + "/TodayWorkPlan/settingWorkType.do",todayParam,workTypeParam,"workType",workTypeTextView);
            System.out.println("------------------------------------------- workTypeParam " + workTypeParam.toString());

            //202009_작업유형
            //setAdapterModule(workTypeSpinner, null, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkType.do", todayParam, workTypeParam, "workType", workTypeTextView, false);
            System.out.println("------------------------------------ todayParam ==== " + todayParam.toString());
            JSONObject jobj = new JSONObject();
            try{
                jobj.put("workTypeCd",new JSONObject(todayParam).getString("workTypeCd"));
                jobj.put("subWorkTypeCd",new JSONObject(todayParam).getString("workTypeCd").substring(0,3));
            }catch (Exception e){

            }

            //2021.07 작업유형(대분류)

            for( Map.Entry<String,String> entry : workTypeCATMap.entrySet()){
                System.out.println("========================= entry.getValue() ============================ " + entry.getValue());
                if (entry.getValue().equals(jobj.getString("subWorkTypeCd"))){
                    System.out.println("========================= entry.getValue() ============================ " + entry.getKey());
                    workTypeCATTextView.setText(entry.getKey());
                    break;
                }else{

                }
            }

            //2021.07 작업유형_ 초기값으로 작업등록시 등록되는 값
            jsonResultObject.put("workType", new JSONObject(todayParam).getString("workType"));
            jsonResultObject.put("workTypeCd", new JSONObject(todayParam).getString("workTypeCd"));

            String obj = jobj.toString();
            System.out.println("========================= obj ============================ " + obj);
            workTypeTextView.setText(new JSONObject(todayParam).getString("workType"));
            setAdapterModule(workTypeSpinner,workTypeMapList, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkTypeCode.do",obj, workTypeParam, "workType", workTypeTextView,"firstAction");
            //2021.07 작업유형(대분류) TEST
            //setAdapterModule(workTypeSpinner,workTypeMapList, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkType.do", todayParam, workTypeParam, "workType", workTypeTextView, false);

        } catch (NullPointerException | JSONException e) {
            Log.e("에러","예외");
        }


        //--감독우너

        try{

            ArrayList<String> gamdokParam = new ArrayList<String>();
            gamdokParam.add("cstrCpprPrchTelno");
            gamdokParam.add("gamdokNameEMNM");
            gamdokParam.add("mtnofPrchEmno");
            gamdokMoule(gamdokSpinner,gamdokMapList,gamdokList,SERVER_URL + "/TodayWorkPlan/getGamdok.do",todayParam,gamdokParam,"gamdokNameEMNM",gamdokwonTextView);

        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }

        try{
            startDateTextView.setText(nullCheck(jsonResultObject.get("blcStrtDttm").toString()));
        }catch (NullPointerException|JSONException e){
            startDateTextView.setText("");
        }
        try{
            endDateTextView.setText(nullCheck(jsonResultObject.get("blcRevocDttm").toString()));
        }catch (NullPointerException|JSONException e){
            endDateTextView.setText("");
        }
        try{
            startHourTextView.setText(nullCheck(jsonResultObject.get("startGongsaHour").toString()));
            startMinuteTextView.setText(nullCheck(jsonResultObject.get("startGongsaMinute").toString()));
            endHourTextView.setText(nullCheck(jsonResultObject.get("endGongsaHour").toString()));
            endMinuteTextView.setText(nullCheck(jsonResultObject.get("endGongsaMinute").toString()));
        }catch (NullPointerException|JSONException e){
            Log.e("에러","에러발생");
        }


        ET_startGongsaGugan.setTextColor(-4276546);
        ET_endGongsaGugan.setTextColor(-4276546);
        ET_gongsaContent.setTextColor(-4276546);
/*        ET_gosundaeNum.setTextColor(-4276546);
        ET_gosundaeNum.setInputType(InputType.TYPE_CLASS_NUMBER);*/

        try{
            String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL + "/TodayWorkPlan/getGugan.do",todayParam, this).execute().get()), "UTF-8");
            jsonObject=new JSONObject(guganMinMaxValue);
            Log.e("이거 뭥미",guganMinMaxValue);
            maxGugan=Double.parseDouble(jsonObject.get("maxMtnofEdpntDstnc").toString());
            minGugan=Double.parseDouble(jsonObject.get("minMtnofStpntDstnc").toString());
        }catch (NumberFormatException|JSONException|InterruptedException| ExecutionException| UnsupportedEncodingException e){
            Log.e("에러","에러발생");
        }

        try{
            inwonJangbiList.add("선택");
            inwonJangbiList.add("작업원");
            inwonJangbiList.add("사인카");
            inwonJangbiList.add("작업차");

            inwonJangbiSpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,inwonJangbiList));
            inwonJangbiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    adapterView.setSelection(0);
                    inwonJangbiTextView.setText(adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            roadLimit = new String[13];


            startHourList.add(startHourTextView.getText().toString());
            for(int i =0; i<=24;i++){
                startHourList.add(i+"");
            }
            startHourSpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,startHourList));
            startHourSpinner.setSelection(0);



            endHourList.add(endHourTextView.getText().toString());
            for(int i =0; i<=24;i++){
                endHourList.add(i+"");
            }
            endHourSpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,endHourList));
            endHourSpinner.setSelection(0);


            startMinList.add(startMinuteTextView.getText().toString());
            for(int i =0;i<=60;i++){
                startMinList.add(i+"");
            }
            startMinuteSpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,startMinList));
            startMinuteSpinner.setSelection(0);


            endMinList.add(endMinuteTextView.getText().toString());
            for(int i = 0;i<=60;i++){
                endMinList.add(i+"");
            }
            endMinuteSpinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,endMinList));
            endMinuteSpinner.setSelection(0);

        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }
    }

    public   String onActionPost(String primitive, String date){
        return null;
    }


    public void setGosundaeNo(String[] gosundaeArray,int length){
        switch (1){
            case 1:
                //  ET_gosundaeNum.setText(Common.nullCheck(gosundaeArray[0].replaceAll("-", "")));
                addView(Common.nullCheck(gosundaeArray[0].replaceAll("-", "")));
                // que_et_list.add(ET_gosundaeNum);
                if(1==length)break;
            case 2:
                //que_et_list.add(ET_gosundaeNum_no1);
             /*   view_gosundae_no1.setVisibility(View.VISIBLE);
                Li_gosundae_no1.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no1.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no1.setText(Common.nullCheck(gosundaeArray[1].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[1].replaceAll("-", "")));
                if(2==length)break;
            case 3:
                //que_et_list.add(ET_gosundaeNum_no2);
             /*   view_gosundae_no2.setVisibility(View.VISIBLE);
                Li_gosundae_no2.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no2.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no2.setText(Common.nullCheck(gosundaeArray[2].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[2].replaceAll("-", "")));
                if(3==length)break;
            case 4:
                // que_et_list.add(ET_gosundaeNum_no3);
                /*view_gosundae_no3.setVisibility(View.VISIBLE);
                Li_gosundae_no3.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no3.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no3.setText(Common.nullCheck(gosundaeArray[3].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[3].replaceAll("-", "")));
                if(4==length)break;
            case 5:
                //   que_et_list.add(ET_gosundaeNum_no4);
               /* view_gosundae_no4.setVisibility(View.VISIBLE);
                Li_gosundae_no4.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no4.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no4.setText(Common.nullCheck(gosundaeArray[4].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[4].replaceAll("-", "")));
                if(5==length)break;
        }
    }

    public void addView(@Nullable String gosundae_No){
        Log.i("addView","View에 들어옴");
        LinearLayout li_out = new LinearLayout(this);
        final int view_height_out = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams viewParams_out_Li = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
        li_out.setLayoutParams(viewParams_out_Li);
        li_out.setOrientation(LinearLayout.VERTICAL);
        // li_out.setTag("pa_"+gosundae_list.getChildCount());
        //View 추가
        View view = new View(this);
        final int view_height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());


        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,view_height,0);
        view.setLayoutParams(viewParams);
        //gosundae_list.addView(view);
        //if(gosundae_list.getChildCount()!=0){
        Log.i("cntcnt",li_out.getChildCount()+"");
        li_out.addView(view);
        //}


        //Linear Layout 추가
        final int li_height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        LinearLayout li = new LinearLayout(this);
        li.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams linear_View = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,li_height,0);
        //li.setBackground(R.drawable.insert_line_color);
        //li.setBackground(R.drawable.insert_line_color);
        //li.setBackgroundResource(R.drawable.insert_line_color);
        li.setLayoutParams(linear_View);
        /*li.setWeightSum(322)*/;
        li.setWeightSum(100);
        li.setOrientation(LinearLayout.HORIZONTAL);



        final int set_et_padding_left = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams et_param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,85);
        EditText et = new EditText(this);
        et.setLayoutParams(et_param);
        et.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        et.setBackgroundResource(R.drawable.insert_line_color);
        et.setPadding(set_et_padding_left,0,0,0);
        et.setTextSize(15);
        et.setTextColor(-4276546);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setMaxLines(1);
        // que_et_list.add(et);
        if(gosundae_No!=null){
            et.setText(gosundae_No);

        }
        li.addView(et);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        que_et_list.add(et);

        View inner_view = new View(this);
        LinearLayout.LayoutParams inner_viewParams = new LinearLayout.LayoutParams(0,view_height,5);
        inner_view.setLayoutParams(inner_viewParams);
        li.addView(inner_view);


        ImageView iv = new ImageView(this);
        final int set_iv_padding_left = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams iv_param = new LinearLayout.LayoutParams(set_iv_padding_left, ViewGroup.LayoutParams.WRAP_CONTENT,10);
        iv.setLayoutParams(iv_param);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //iv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        //iv.s(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        iv.setBackgroundResource(R.drawable.z_gosunda_minus_item);

        //iv.setBackground(this.getDrawable(R.drawable.z_gosunda_minus_item));
        //iv.setTag((String)"gosundae_"+li.getChildCount());


//        iv.setTag((String)"gosundae_"+((LinearLayout)li_out.getParent()).getChildCount());
//        iv.setId( Integer.parseInt((String)iv.getTag()));
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup v = (ViewGroup)view.getParent();
                LinearLayout li = (LinearLayout)v.getParent();
                LinearLayout li_pa = (LinearLayout)li.getParent();
                Log.i("index","지워지는 인덱스:"+li_pa.indexOfChild(li)+"");

                /*if(que_et_list.size()>0){

                }*/

                Log.i("index","리스트사이즈::"+que_et_list.size());
                //li_pa.removeView(li);
                //li_pa.removeView(li);
                if(li_pa.getChildCount()==1){
                    /*Toast.makeText(WorkPlanResisterLoadContentActivity.this,"고순대번호는 최소 1개 이상이 입력 되어야 합니다.",Toast.LENGTH_SHORT).show();
                    ((EditText)v.getChildAt(0)).setText("");
                    ((EditText)que_et_list.get(0)).setText("");
                    */
                    if(((EditText)v.getChildAt(0)).getText().toString().length()==0 || ((EditText)v.getChildAt(0)).getText().toString().equals("")){
                        Toast.makeText(TodayWorkPlanUpdateActivity_N.this,"고순대번호는 최소 1개가 입력되어야 합니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        new CustomDialog(TodayWorkPlanUpdateActivity_N.this, R.layout.zz_warn_gosundae, R.id.warn_msg,"지우시는 고순대 번호는\n"+gosunDaeNoParsing(((EditText)v.getChildAt(0)).getText().toString())+"\n입니다.", que_et_list, li_pa.indexOfChild(li),li_pa) ;
                    }


                }else{
             /*       que_et_list.remove(li_pa.indexOfChild(li));
                    Toast.makeText(WorkPlanResisterLoadContentActivity.this,"지우시는 번호는 "+((EditText)v.getChildAt(0)).getText().toString()+" 입니다.",Toast.LENGTH_SHORT).show();
                    li_pa.removeViewAt(li_pa.indexOfChild(li));
*/
                    new CustomDialog(TodayWorkPlanUpdateActivity_N.this, R.layout.zz_warn_gosundae, R.id.warn_msg,"지우시는 고순대 번호는\n"+gosunDaeNoParsing(((EditText)v.getChildAt(0)).getText().toString())+"\n입니다.", que_et_list, li_pa.indexOfChild(li),li_pa) ;
                }



                for(int i = 0; i <que_et_list.size() ;i++){
                    Log.i("index","리스트에 담긴것들::"+que_et_list.get(i).getText().toString());
                }
                Log.i("index","------------------------------------");
            }
        });
        li_out.addView(li);
        li.addView(iv);

        gosundae_list.addView(li_out);
    }

    public static String gosunDaeNoParsing(String gosundaNo) {
        try {
            gosundaNo = gosundaNo.substring(0, 4) + "-" + gosundaNo.substring(4, 8) + "-" + gosundaNo.substring(8, gosundaNo.length());
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
            // Log.e("에러","에러발생");
        }

        /*while(gosundaNo.length())*/

        return gosundaNo;
    }

    public  void gamokAlertDialog(String selectedBonboAndJisaCd,Spinner gamdokSpinner,String url,TextView tv,String gamdokList){

        try{
            tv.setText(gamdokList);
            JSONObject job = new JSONObject(selectedBonboAndJisaCd);
            Log.i("선택된 지사 본부",job.toString());
            Log.i("선택된 지사 본부",gamdokList);
            //
            //
            //
            // "hdqrCd":"N02723", "mtnofCd":"N01046",
            String result = nullCheck(URLDecoder.decode(new Action("get", url, selectedBonboAndJisaCd, this).execute().get(), "UTF-8"));
            Log.i("선택된 지사 본부",result);

            gamdokwonTextView.clearComposingText();
            gamdokSpinner.performClick();
            gamdokJsonArray = new JSONArray(result);
            gamdokName = new String[gamdokJsonArray.length()];
            gamdokItemBoo = new boolean[gamdokJsonArray.length()];
            Log.println(Log.ASSERT,"감독원 어레이 길이",gamdokJsonArray.length()+"");
            for(int i = 0 ; i < gamdokJsonArray.length(); i++){
                Log.println(Log.ASSERT,"감독원 리스,트",gamdokJsonArray.get(i).toString()+"");
                gamdokItemBoo[i]= false;
                gamdokName[i] = gamdokJsonArray.getJSONObject(i).get("gamdokNameEMNM").toString();
            }
            tv.clearComposingText();
            tv.setText(gamdokName[0]);
            gamdokItemBoo[0]=true;

            jsonResultObject.put("gamdokNameEMNM",gamdokName[0]);
            /*   final AlertDialog.Builder*/ gamdokdialog = new AlertDialog.Builder(this);


        }catch (JSONException|InterruptedException|ExecutionException|UnsupportedEncodingException e){
            Log.e("에러","에러발생");
        }

    }

    public void gamdokMoule(Spinner spinner, List<Map<String,String>> listMap, List<String> list, String url,String param, List<String> keyArray, String gamdokInitdat, final TextView tv){
        try{


            String result = nullCheck( URLDecoder.decode(new Action("get", url,param, this).execute().get(),"UTF-8"));
            gamdokJsonArray=new JSONArray(result);
            Log.i("확인",jsonArray.toString());
            gamdokName = new String[gamdokJsonArray.length()];
            gamdokItemBoo = new boolean[gamdokJsonArray.length()];

            for(int i = 0 ; i < gamdokJsonArray.length(); i++){
                Log.println(Log.ASSERT,"감독원 리스,트",gamdokJsonArray.get(i).toString()+"");
                gamdokItemBoo[i]= false;
                gamdokName[i] = gamdokJsonArray.getJSONObject(i).get("gamdokNameEMNM").toString();
            }
            try{
                tv.setText(new JSONObject(todayParam).getString("gamdokNameEMNM"));
            }catch (JSONException e){
                tv.setText("");
            }catch (NullPointerException e){
                tv.setText("");
            }catch (Exception e){
                tv.setText("");
            }
            jsonResultObject.put("gamdokNameEMNM",new JSONObject(todayParam).getString("gamdokNameEMNM"));
      //gamdokSpinner,gamdokMapList,gamdokList,SERVER_URL + "/TodayWorkPlan/getGamdok.do",todayParam,gamdokParam,"gamdokNameEMNM",gamdokwonTextView
            gamdokdialog = new AlertDialog.Builder(this);
        }catch (JSONException  e){
            Log.e("에러","에러발생");
        }catch ( NullPointerException  e){
            Log.e("에러","에러발생");
        }catch (InterruptedException  e){
            Log.e("에러","에러발생");
        }catch ( UnsupportedEncodingException e){
            Log.e("에러","에러발생");
        }catch (Exception e){
            Log.e("에러","에러발생");
        }
    }//end setAdapter

    //2021.07 작업유형(대분류) TEST
    //public void setAdapterModule(Spinner spinner, List<Map<String,String>> listMap, List<String> list, String url,String param, List<String> keyArray, String listParam, final TextView tv){
    public void setAdapterModule(Spinner spinner, List<Map<String,String>> listMap, List<String> list, String url,String param, List<String> keyArray, String listParam, final TextView tv, String actionTime){
        try{

            if("notFirstAciton".equals(actionTime)){
                String result = nullCheck(URLDecoder.decode(new Action("get", url, param, this).execute().get(), "UTF-8"));
                jsonArray = new JSONArray(result);
                Log.i("확인", jsonArray.toString());
                //2021.07 작업유형(대분류) TEST
                for (int i = 1; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);
                    Log.i("감독ddd원", jsonObject.toString());
                    Log.println(Log.ASSERT, "감독ddd원", jsonObject.toString());
                    Map<String, String> map = new HashMap<String, String>();

                    //2021.07 작업유형(대분류) 에서 대분류 변경했을경우
                    for (int k = 0; k < keyArray.size(); k++) {
                        map.put(keyArray.get(k), jsonObject.get(keyArray.get(k)).toString());
                    }

                    System.out.println("-------------------------------- setAdapterModule map ----- + " + map.toString());
                    if (listMap != null) {
                        listMap.add(map);
                    }
                    list.add(map.get(listParam));
                }

                spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        adapterView.setSelection(0);
                        tv.setText(adapterView.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }else {
                String result = nullCheck(URLDecoder.decode(new Action("get", url, param, this).execute().get(), "UTF-8"));
                jsonArray = new JSONArray(result);
                Log.i("확인", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    Map<String, String> map = new HashMap<String, String>();
                    for (int k = 0; k < keyArray.size(); k++) {
                        map.put(keyArray.get(k), jsonObject.get(keyArray.get(k)).toString());
                    }
                    if (listMap != null) {
                        listMap.add(map);
                    }

                    list.add(map.get(listParam));
                }

                spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        adapterView.setSelection(0);
                        tv.setText(adapterView.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }catch (JSONException e){
            Log.e("에러","에러발생");
        }catch (InterruptedException e){
            Log.e("에러","에러발생");
        }catch (ExecutionException e){
            Log.e("에러","에러발생");
        }catch (UnsupportedEncodingException e){
            Log.e("에러","에러발생");
        }catch (Exception e){
            Log.e("에러","에러발생");
        }
    }//end setAdapter

    @Override
    public void onPause(){
        super.onPause();
        /*ET_gosundaeNum.onCh*/

    }

    public void setAdapterModule( Spinner spinner,  List<Map<String,String>> listMap,  List<String> list, String url, String param,  List<String> keyArray, String listParam, final TextView tv,  JSONObject job,  List<String> jobParam){
        try{
            listMap.clear();
            list.clear();
            tv.clearComposingText();

            Log.println(Log.ASSERT,"파라미터", "파라미터 : "+ param);
            String result = nullCheck( URLDecoder.decode(new Action("get", url,param, this).execute().get(),"UTF-8"));
            jsonArray=new JSONArray(result);
            Log.i("jddddsfd",jsonArray.toString());
            Log.i("확인",jsonArray.toString());
     /*       Log.println(Log.ASSERT,"통신결과", "통신결과 : "+ jsonArray.toString());*/


            for(int i = 0 ; i < jsonArray.length();i++){
                jsonObject=jsonArray.getJSONObject(i);

                Map<String,String> map = new HashMap<String,String>();
                for(int k =0;k<keyArray.size();k++){
                    map.put(keyArray.get(k),jsonObject.get(keyArray.get(k)).toString());
                    Log.d("키",keyArray.get(k));
            /*        Log.println(Log.ASSERT,"키", " : "+ keyArray.get(k));
                    Log.println(Log.ASSERT,"밸류",jsonObject.get(keyArray.get(k)).toString())     ;*/
                }
                    listMap.add(map);
                   list.add(listMap.get(i).get(listParam));

            }

            try{


                for(int k =0;k<jobParam.size();k++){

                        jsonResultObject.put(keyArray.get(k),listMap.get(0).get(keyArray.get(k)));

                }

            }catch (JSONException e){
                Log.e("에러","에러발생");
                tv.setText("");
            }catch (ArrayIndexOutOfBoundsException e){
                Log.e("에러","에러발생");
                tv.setText("");
            }catch (NullPointerException e){
                Log.e("에러","에러발생");
                tv.setText("");
            }catch (Exception e){
                Log.e("에러","에러발생");
                tv.setText("");
            }

            spinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,list));

            if(list.size()!=0){
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        adapterView.setSelection(0);
                        tv.setText(adapterView.getSelectedItem().toString());

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }else {
                Log.println(Log.ASSERT,"리스트", "0이라서 여기왔는데?");
                tv.clearComposingText();
                tv.setText("");
                for(int k =0;k<jobParam.size();k++){
                    jsonResultObject.put(keyArray.get(k),"\n");
                   /* jsonResultObject.put(keyArray.get(k),null);*/
                }
                list.clear();
                listMap.clear();
            }
            Log.println(Log.ASSERT,"-=------------------", "-=----------------------------");
        }catch (JSONException|NullPointerException e){
            Log.e("에러","에러발생");
        }catch (InterruptedException e){
            Log.e("에러","에러발생");
        }catch (ExecutionException e){
            Log.e("에러","에러발생");
        }catch (UnsupportedEncodingException e){
            Log.e("에러","에러발생");
        }
        try{///TodayWorkPlan/settingInjung.do
            //if(url.equals(SERVER_URL + "/TodayWorkPlan/settingNosun.do")){
            if(url.equals(SERVER_URL + "/TodayWorkPlan/settingInjung.do")){
                String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL + "/TodayWorkPlan/getGugan.do",param, this).execute().get()), "UTF-8");
                Log.i("구간 파람",param.toString());
                jsonObject=new JSONObject(param);
                Log.e("이거 뭥미",guganMinMaxValue);
                JSONObject initGugan = new JSONObject(todayParam);
                JSONObject JsonGuganResult = new JSONObject(guganMinMaxValue);
                if(initGugan.get("mtnofCd")!=jsonObject.get("mtnofCd")&&initGugan.get("routeCd")!=jsonObject.get("routeCd")){
                    Log.println(Log.ASSERT,"if", "ㅡㅡㅡㅡ : "+ keyArray.toString());

                    maxGugan=Double.parseDouble(JsonGuganResult.get("maxMtnofEdpntDstnc").toString());
                    minGugan=Double.parseDouble(JsonGuganResult.get("minMtnofStpntDstnc").toString());
                    ET_startGongsaGugan.setText(minGugan+"");
                    ET_endGongsaGugan.setText(maxGugan+"");
                }else{
                    Log.println(Log.ASSERT,"else", "ㅡㅡㅡㅡ : "+ keyArray.toString());

                    maxGugan=Double.parseDouble(JsonGuganResult.get("maxMtnofEdpntDstnc").toString());
                    minGugan=Double.parseDouble(JsonGuganResult.get("minMtnofStpntDstnc").toString());
                  /*  ET_startGongsaGugan.setText(initGugan+"");
                    ET_endGongsaGugan.setText(initGugan+"");*/
                    ET_startGongsaGugan.setText(initGugan.get("strtBlcPntVal").toString());
                    ET_endGongsaGugan.setText(initGugan.get("endBlcPntVal").toString());
                }



            }
        }catch (JSONException|NumberFormatException|InterruptedException|ExecutionException|UnsupportedEncodingException e){
            Log.e("에러","에러발생");
        }

    }//end setAdapter

    public void gamdokMoule( Spinner spinner,  List<Map<String,String>> listMap,  List<String> list, String url, String param,  List<String> keyArray, String listParam, final TextView tv,  JSONObject job,  List<String> jobParam){
        try{
            String result = nullCheck( URLDecoder.decode(new Action("get", url,param, this).execute().get(),"UTF-8"));
            gamdokJsonArray=new JSONArray(result);
            Log.i("확인",jsonArray.toString());
            gamdokName = new String[gamdokJsonArray.length()];
            gamdokItemBoo = new boolean[gamdokJsonArray.length()];

            for(int i = 0 ; i < gamdokJsonArray.length(); i++){
                Log.println(Log.ASSERT,"감독원 리스,트",gamdokJsonArray.get(i).toString()+"");
                gamdokItemBoo[i]= false;
                gamdokName[i] = gamdokJsonArray.getJSONObject(i).get("gamdokNameEMNM").toString();
            }

        }catch (JSONException|NullPointerException|InterruptedException|ExecutionException|UnsupportedEncodingException e){
            Log.e("에러","에러발생");
        }

    }//end setAdapter
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bonbuTextView:
                bonbuSpinner.performClick();
                bonbuSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.jisaTextView:
                jisaSpinner.performClick();
                jisaSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.noSunTextView:
                nosunSpinner.performClick();
                nosunSpinner.setOnItemSelectedListener(this);

                break;
            case R.id.directionTextView:
                direnctionSpinner.performClick();
                direnctionSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.roadlimitTextView:
                maxCharoChoice();
                break;
            case R.id.workTypeCATTextView:       //2021.07 작업유형(대분류)
                workTypeCATSpinner.performClick();
                workTypeCATSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.workTypeTextView:
                workTypeSpinner.performClick();
                workTypeSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.gamdokwonTextView:
                //gamdokSpinner.performClick();
                //gamdokSpinner.setOnItemSelectedListener(this);
                showGamdokDialog(gamdokdialog, gamdokName,gamdokItemBoo);
                break;
            case R.id.inwonJangbiTextView:
                inwonJangbiSpinner.performClick();
                inwonJangbiSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.startDateImageView:

                datePickerMethod(startDateTextView);
                break;
            case R.id.endDateImageView:

                datePickerMethod(endDateTextView);
                break;
            case R.id.li_registerBtn://checkingValue
                try {
                    if(inwonJangbiTextView.getText().equals("인원 0명, 사인카 0대, 작업차 0대")) {
                        Toast.makeText(this, "인원 장비를 입력해주세요", Toast.LENGTH_SHORT).show();
                    /*}else if(ET_gosundaeNum.getText().equals("\n")||ET_gosundaeNum.getText().equals("")){
                        Toast.makeText(this,"고속도로 순찰대 번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                    }*/

                    } else if(gosundaeNullCheck()==false){


                        }else if(directionTextView.getText().equals("\n")){

                        Toast.makeText(this,"이정이 설정되지 않아 등록이 불가능합니다.",Toast.LENGTH_SHORT).show();
                    } else if (calCompareCheck(startDateTextView.getText().toString(), startHourTextView.getText().toString(), startMinuteTextView.getText().toString(), endDateTextView.getText().toString(), endHourTextView.getText().toString(), endMinuteTextView.getText().toString()) == false) {
                        Toast.makeText(getApplicationContext(), "끝난시간은 시작시간보다 늦은 시간에 있어야 합니다.", Toast.LENGTH_LONG).show();
                    } else if((Boolean) checkingValue(minGugan, maxGugan, Double.parseDouble(ET_startGongsaGugan.getText().toString()) , Double.parseDouble(ET_endGongsaGugan.getText().toString())) == false){
                        Log.i("구간값",minGugan+","+minGugan);
                        Toast.makeText(this,"공사구간은" + minGugan + " 사이에서 " + maxGugan + "에 위치해야 합니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        intent = new Intent(TodayWorkPlanUpdateActivity_N.this, TodayWorkPlanRegisterDetailActivity.class);


                        jsonObject.put("cmmdNm",inwonJangbiResultTextView.getText().toString());
                        jsonObject.put("humanCnt"," 장비인원목록:"+inwonJangbiResultTextView.getText().toString());

                   /*     try{
                            if(((String[])ET_gosundaeNum.getText().toString().split("-")).length==1){
                                String result =    ET_gosundaeNum.getText().toString().subSequence(0, 4)+"-"+ET_gosundaeNum.getText().toString().subSequence(4, 8)+"-"+ET_gosundaeNum.getText().toString().subSequence(8, 15);
                                jsonResultObject.put("gosundaeNo",result);
                            }else{
                                jsonResultObject.put("gosundaeNo",ET_gosundaeNum.getText().toString());
                            }
                        }catch (Exception e){
                            Log.e("에러","에러발생");
                            jsonResultObject.put("gosundaeNo",ET_gosundaeNum.getText().toString());
                        }*/
                        jsonResultObject.put("gosundaeNo",gosundaTextParsing(que_et_list));

                        jsonResultObject.put("cnstnCtnt", ET_gongsaContent.getText().toString());
                        jsonResultObject.put("blcStrtDttm",startDateTextView.getText().toString());//공사 시작시간
                        jsonResultObject.put("blcRevocDttm",endDateTextView.getText().toString());//종료시간
                        jsonResultObject.put("workType",workTypeTextView.getText().toString());

                        jsonResultObject.put("startGongsaHour",startHourTextView.getText().toString());
                        jsonResultObject.put("startGongsaMinute",startMinuteTextView.getText().toString());
                        jsonResultObject.put("endGongsaHour",endHourTextView.getText().toString());
                        jsonResultObject.put("endGongsaMinute",endMinuteTextView.getText().toString());
                        jsonResultObject.put("cmmdNm",inwonJangbiResultTextView.getText().toString());
                        Log.println(Log.ASSERT,"결과 파람 확인", "결과 파람 확인 : "+ inwonJangbiResultTextView.getText().toString());
                        /*ET_startGongsaGugan.setText(minGugan+"");
                        ET_endGongsaGugan.setText(maxGugan+"");*/
                        jsonResultObject.put("strtBlcPntVal",ET_startGongsaGugan.getText().toString());
                        jsonResultObject.put("endBlcPntVal",ET_endGongsaGugan.getText().toString());
                        jsonResultObject.put("USE_CLSS_CD",userCheck);
                        intent.putExtra("todayWorkPlanRegister", jsonResultObject.toString());
                        intent.putExtra("userInfo",userInfo);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                        startActivity(intent);
                       // new CustomDialog(TodayWorkPlanUpdateActivity_N.this, R.layout.z_custom_dialog, R.id.sendApprovalRequest, jsonNameValue, jsonObject);
                    }

                } catch (ParseException|StringIndexOutOfBoundsException|JSONException e) {
                    Log.e("에러","에러발생");
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

            case R.id.startHourTextView:
                startHourSpinner.performClick();
                startHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        startHourSpinner.setSelection(i);
                        startHourTextView.setText(adapterView.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            case R.id.endHourTextView:
                endHourSpinner.performClick();
                endHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        adapterView.setSelection(i);
                        endHourTextView.setText(adapterView.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case R.id.startMinuteTextView:
                startMinuteSpinner.performClick();
                startMinuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        adapterView.setSelection(i);
                        startMinuteTextView.setText(adapterView.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case R.id.endMinuteTextView:
                endMinuteSpinner.performClick();
                endMinuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        adapterView.setSelection(i);
                        endMinuteTextView.setText(adapterView.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.plus_btn:
                //  int index =
                if(que_et_list.size()==1 &&(que_et_list.get(0).getText().toString().length()==0 ||que_et_list.get(0).getText().toString().equals(""))){
                    Toast.makeText(this,"1번째 고순대번호를 입력해주세요",Toast.LENGTH_LONG).show();
                }else if(/*que_et_list.size()!=0 &&*/ (que_et_list.get(que_et_list.size()-1).getText().toString().equals("")||que_et_list.get(que_et_list.size()-1).getText().toString().length()==0)){
                    //                  Log.i("뭐지","사이즈:"+que_et_list.size());
//                    Log.i("뭐지",(que_et_list.size()-1)+"번째, 값은:"+que_et_list.get(que_et_list.size()-1).getText().toString());
                    //int cnt=que_et_list.size()+1;
                    int cnt=que_et_list.size();
                    Toast.makeText(this,cnt+"번째 고순대번호를 입력해주세요",Toast.LENGTH_LONG).show();
                }else if(que_et_list.size()==5){
                    Toast.makeText(this,"고순대번호는 최고 5개만 입력이 가능합니다.",Toast.LENGTH_LONG).show();
                }else {
                    addView(null);
                }
                break;
        }
    }

    public boolean gosundaeNullCheck(){
        Log.i("cccc",""+"여기는 들옴");
        Log.i("cccc","사이즈::"+que_et_list.size());
        for(int i = 0 ; i <que_et_list.size();i++){
            if(que_et_list.get(i).getText().toString().length()==0 ||que_et_list.get(i).getText().toString().equals("")){
                switch (i){
                    case 0:
                        Toast.makeText(this,"첫번째 고순대 번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        return false;
                    case 1:
                        Toast.makeText(this,"두번째 고순대 번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        return false;
                    case 2:
                        Toast.makeText(this,"세번째 고순대 번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        return false;
                    case 3:
                        Toast.makeText(this,"네번째 고순대 번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        return false;
                    case 4:
                        Toast.makeText(this,"다섯번째 고순대 번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        return false;
                }
            } else if(i==que_et_list.size()-1) {
                return true;
            }

        }
        return false;
    }

    public String gosundaTextParsing(List<EditText> list){
        //String result = gosunDaeNoParsing(ET_gosundaeNum.getText().toString());
        String result="";
        if(list.size()!=0){
            for(int i =0 ;i <list.size();i++){
                if(!list.get(i).getText().equals("") && list.get(i).getText().toString().length()!=0 ){
                    result += ","+gosunDaeNoParsing(list.get(i).getText().toString());
                }
            }
        }
        return result.substring(1);
    }

    public boolean calCompareCheck(String firdate, String firhour, String firminute, String date, String hour, String minute) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date d = sdf.parse(firdate + " " + firhour + ":" + firminute);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date d1 = sdf1.parse(date + " " + hour + ":" + minute);
        Log.i("d", d.toString());
        Log.i("d1", d1.toString());
        if (d.before(d1) == false) {
            return false;
        } else {
            return true;
        }

    }

    public void showGamdokDialog(final  AlertDialog.Builder gamdokdialog,final  String[] gamdokName,boolean[] gamdokItemBoo){
        try{
            for(int i = 0 ; i < gamdokItemBoo.length;i++){
                gamdokItemBoo[i]=false;
            }
            gamdokdialog.setMultiChoiceItems(gamdokName, gamdokItemBoo, new DialogInterface.OnMultiChoiceClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if(b==true){

                        //gamdokwonTextView.append(gamdokName[i]+",");
                        gamdokTvResult+=","+gamdokName[i];
//[{"cstrCpprPrchTelno":"010-9189-2421","gamdokNameEMNM":"김광석"},{"cstrCpprPrchTelno":"010-8914-8602","gamdokNameEMNM":"정성모"},{"cstrCpprPrchTelno":"010-4945-0845","gamdokNameEMNM":"임민혁"},{"cstrCpprPrchTelno":"010-7728-9263","gamdokNameEMNM":"최운"}]
                        try{
                            gamdokTelno+=","+gamdokJsonArray.getJSONObject(i).getString("cstrCpprPrchTelno");


                            gamdokSawonNo+=","+gamdokJsonArray.getJSONObject(i).getString("mtnofPrchEmno");//USER_EMNO
                        }catch (JSONException|ArrayIndexOutOfBoundsException|StringIndexOutOfBoundsException e){
                            Log.e("에러","에러발생");
                        }

                    }

                }
            }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gamdokwonTextView.clearComposingText();
                    gamdokwonTextView.setText(gamdokTvResult.substring(1));
                /*    codeMap.put("MTNOF_PRCH_EMNO_TELNO",gamdokTelno.substring(1));
                    codeMap.put("MTNOF_PRCH_EMNO",gamdokSawonNo.substring(1));*/

                    try{
                        jsonResultObject.put("MTNOF_PRCH_EMNO_TELNO",gamdokTelno.substring(1));
                        jsonResultObject.put("mtnofPrchEmno",gamdokSawonNo.substring(1));
                        jsonResultObject.put("gamdokNameEMNM",gamdokTvResult.substring(1));
      /*                  Log.i("텔노",parameterMap.get("MTNOF_PRCH_EMNO_TELNO").toString());
                        Log.i("사번",parameterMap.get("MTNOF_PRCH_EMNO").toString());*/
                  /*      gamdokTvResult="";
                        gamdokTelno="";
                        gamdokSawonNo="";*/
                    }catch(JSONException|ArrayIndexOutOfBoundsException|StringIndexOutOfBoundsException e){
                        Log.e("에러","에러발생");
                    }
                    gamdokTvResult="";
                    gamdokTelno="";
                    gamdokSawonNo="";
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gamdokdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            dialogInterface.dismiss();
                        }
                    });
                }
            });

            gamdokdialog.create();
            gamdokdialog.show();
        }catch (ArrayIndexOutOfBoundsException|StringIndexOutOfBoundsException e){
            Log.e("에러","에러발생");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.bonbuSpinner:


            try {

                selectItemList(parent, position, bonbuTextView, R.id.bonbuSpinner);
           /*     jsonResultObject.put("hdqrCd",nJob.get("hdqrCd").toString());
                jsonResultObject.put("hdqrNm", nJob.get("hdqrNm").toString());*/
                ArrayList<String> jisaParam = new ArrayList<String>();
                jisaParam.add("mtnofCd");
                jisaParam.add("mtnofNm");
                setAdapterModule(jisaSpinner, jisaMapList, jisaList, SERVER_URL + "/TodayWorkPlan/settingJisa.do", jsonResultObject.toString(), jisaParam, "mtnofNm", jisaTextView, jsonObject, jisaParam);
                ////

                ;;
               /* jsonResultObject.put("mtnofCd",nJob.get("mtnofCd").toString());
                jsonResultObject.put("mtnofNm",nJob.get("mtnofNm").toString());*/
                ArrayList<String> nosunParam = new ArrayList<String>();
                nosunParam.add("routeNm");
                nosunParam.add("routeCd");

                setAdapterModule(nosunSpinner, nosunMapList, nusunList, SERVER_URL + "/TodayWorkPlan/settingNosun.do", jsonResultObject.toString(), nosunParam, "routeNm", noSunTextView, jsonObject, nosunParam);

                ArrayList<String> ijungParam = new ArrayList<String>();
                ijungParam.add("drctClssNm");
                ijungParam.add("drctClssCd");
                setAdapterModule(direnctionSpinner, iJungMapList, ijungList, SERVER_URL + "/TodayWorkPlan/settingInjung.do", jsonResultObject.toString(), ijungParam, "drctClssNm", directionTextView, jsonObject, ijungParam);
               /* jsonResultObject.put("cstrCpprPrchTelno",nJob.get("cstrCpprPrchTelno").toString());
                jsonResultObject.put("gamdokNameEMNM",nJob.get("gamdokNameEMNM").toString());
                jsonResultObject.put("mtnofPrchEmno",nJob.get("mtnofPrchEmno").toString());*/
                ArrayList<String> gamdokParam = new ArrayList<String>();
                gamdokParam.add("cstrCpprPrchTelno");
                gamdokParam.add("gamdokNameEMNM");
                gamdokParam.add("mtnofPrchEmno");
                gamdokMoule(gamdokSpinner, gamdokMapList, gamdokList, SERVER_URL + "/TodayWorkPlan/getGamdok.do", jsonResultObject.toString(), gamdokParam, "gamdokNameEMNM", gamdokwonTextView, jsonObject, gamdokParam);
         /*       jisaAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,jisaList);
                jisaSpinner.setAdapter(jisaAdapter);*/
            }catch (ArrayIndexOutOfBoundsException|StringIndexOutOfBoundsException|NullPointerException e){
                Log.e("에러","에러발생");
            }
                break;
            case R.id.jisaSpinner :
                selectItemList( parent, position,  jisaTextView, R.id.jisaSpinner );
                ArrayList<String> nosunParam = new ArrayList<String>();
                nosunParam.add("routeNm");
                nosunParam.add("routeCd");
                setAdapterModule(nosunSpinner,nosunMapList,nusunList,SERVER_URL + "/TodayWorkPlan/settingNosun.do",jsonResultObject.toString(),nosunParam,"routeNm",noSunTextView,jsonObject,nosunParam);

                ArrayList<String>   ijungParam = new ArrayList<String>();
                ijungParam.add("drctClssNm");
                ijungParam.add("drctClssCd");
                setAdapterModule(direnctionSpinner,iJungMapList,ijungList,SERVER_URL + "/TodayWorkPlan/settingInjung.do",jsonResultObject.toString(),ijungParam,"drctClssNm",directionTextView,jsonObject,ijungParam);

                ArrayList<String>  gamdokParam = new ArrayList<String>();
                gamdokParam.add("cstrCpprPrchTelno");
                gamdokParam.add("gamdokNameEMNM");
                gamdokParam.add("mtnofPrchEmno");
                gamdokMoule(gamdokSpinner,gamdokMapList,gamdokList,SERVER_URL + "/TodayWorkPlan/getGamdok.do",jsonResultObject.toString(),gamdokParam,"gamdokNameEMNM",gamdokwonTextView,jsonObject,gamdokParam);

                break;
            case R.id.nosunSpinner:
                selectItemList( parent,    position,  noSunTextView, R.id.nosunSpinner );

                nosunParam = new ArrayList<String>();
                nosunParam.add("routeNm");
                nosunParam.add("routeCd");
                setAdapterModule(nosunSpinner, nosunMapList, nusunList, SERVER_URL + "/TodayWorkPlan/settingNosun.do", jsonResultObject.toString(), nosunParam, "routeNm", noSunTextView, jsonObject, nosunParam);

                ijungParam = new ArrayList<String>();
                ijungParam.add("drctClssNm");
                ijungParam.add("drctClssCd");
                setAdapterModule(direnctionSpinner, iJungMapList, ijungList, SERVER_URL + "/TodayWorkPlan/settingInjung.do", jsonResultObject.toString(), ijungParam, "drctClssNm", directionTextView, jsonObject, ijungParam);
            /*    gamdokParam = new ArrayList<String>();
                gamdokParam.add("cstrCpprPrchTelno");
                gamdokParam.add("gamdokNameEMNM");
                gamdokParam.add("mtnofPrchEmno");
                setAdapterModule(gamdokSpinner,gamdokMapList,gamdokList,SERVER_URL + "/TodayWorkPlan/getGamdok.do",jsonResultObject.toString(),gamdokParam,"gamdokNameEMNM",gamdokwonTextView,jsonObject,gamdokParam);*/
               //
                break;
            case R.id.direnctionSpinner:
                selectItemList( parent,    position,  directionTextView, R.id.direnctionSpinner );
                break;
            case R.id.gamdokSpinner:
               // parent.setSelection(position);

                //selectItemList( parent,   position,  gamdokwonTextView, R.id.gamdokSpinner );
                break;
            case R.id.workTypeCATSpinner:       //2021.07 작업유형(대분류)
                System.out.println("--------------------------------------------- 가자 ");
                selectItemList(parent, position, workTypeCATTextView, R.id.workTypeCATSpinner);
                break;
            case R.id.workTypeSpinner:
            /* 2021.07 작업유형(대분류)
             parent.setSelection(position);
            *//*    workTypeTextView.clearComposingText();*//*
                workTypeTextView.setText(parent.getSelectedItem().toString());*/
                selectItemList(parent, position, workTypeTextView, R.id.workTypeSpinner);
                ArrayList<String> workTypeParam = new ArrayList<String>();
                workTypeParam.add("workType");
                workTypeParam.add("workTypeCd");
                break;
            case R.id.inwonJangbiSpinner:
                if(parent.getSelectedItem().equals("작업원")){
                    inwonEditText();
                }else if(parent.getSelectedItem().equals("사인카")){
                    signCarSetting();
                }else if(parent.getSelectedItem().equals("작업차")){
                    workCarSetting();
                }
                parent.setSelection(0);
                inwonJangbiTextView.setText(parent.getSelectedItem().toString());
                break;
        }
    }//onItemed

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void selectItemList(AdapterView<?> parent, int position, TextView tv,int SpinnerId){
      //  JSONObject job = new JSONObject();
        switch (SpinnerId){
            case R.id.bonbuSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try{
                    jsonResultObject.put("hdqrCd",bonbuMapList.get(position).get("hdqrCd").toString());
                    jsonResultObject.put("hdqrNm",bonbuMapList.get(position).get("hdqrNm").toString());
                }catch (ArrayIndexOutOfBoundsException|JSONException|NullPointerException e){
                    Log.e("에러","에러발생");
                }

                break;
            case R.id.jisaSpinner :
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try{
                    jsonResultObject.put("mtnofNm",jisaMapList.get(position).get("mtnofNm").toString());
                    jsonResultObject.put("mtnofCd",jisaMapList.get(position).get("mtnofCd").toString());
                }catch (JSONException e){
                    Log.e("에러","에러발생");
                }

                break;
            case R.id.nosunSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try{
                    jsonResultObject.put("routeNm",nosunMapList.get(position).get("routeNm").toString());
                    jsonResultObject.put("routeCd",nosunMapList.get(position).get("routeCd").toString());
                }catch (JSONException e){
                    Log.e("에러","에러발생");
                }
                break;
            case R.id.direnctionSpinner:

                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try{
                    jsonResultObject.put("drctClssNm",iJungMapList.get(position).get("drctClssNm").toString());
                    jsonResultObject.put("drctClssCd",iJungMapList.get(position).get("drctClssCd").toString());
                }catch (JSONException e){
                    Log.e("에러","에러발생");
                }
                break;
            case R.id.gamdokSpinner:

                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try{
                    jsonResultObject.put("cstrCpprPrchTelno",gamdokMapList.get(position).get("cstrCpprPrchTelno").toString());
                    jsonResultObject.put("gamdokNameEMNM",gamdokMapList.get(position).get("gamdokNameEMNM").toString());
                    jsonResultObject.put("mtnofPrchEmno",gamdokMapList.get(position).get("mtnofPrchEmno").toString());
                }catch (JSONException e){
                    Log.e("에러","에러발생");
                }
                break;

            case R.id.workTypeCATSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                final String[] workTypeCategorizationCode = getResources().getStringArray(R.array.workTypeCategorizationCode);      //2021.07 작업유형(대분류) 코드값/ 코드명 순서중요
                String workTypeCode = workTypeCategorizationCode[position].toString();
                System.out.println("-------------- -------------------- workTypeCATTextView 3 workTypeCode " + workTypeCode);
                ArrayList<String> workTypeParam1 = new ArrayList<String>();
                workTypeParam1.add("workType");
                workTypeParam1.add("workTypeCd");
                JSONObject jobj = new JSONObject();
                try{
                    jobj.put("workTypeCd",new JSONObject(todayParam).getString("workTypeCd"));
                    jobj.put("subWorkTypeCd",workTypeCode);
                }catch (Exception e){

                }
                String obj = jobj.toString();
                workTypeMapList.clear();
                workTypeList.clear();
                setAdapterModule(workTypeSpinner,workTypeMapList, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkTypeCode.do",obj, workTypeParam1, "workType", workTypeTextView, "notFirstAciton");
                break;
            //202009_작업유형
            case R.id.workTypeSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                Log.i("workType1937",workTypeMapList.toString());
                try {
                    //2021.07 작업유형_ 선택시 변경되어 최종으로 들어가는 값
                    jsonResultObject.put("workType", workTypeMapList.get(position).get("workType"));
                    jsonResultObject.put("workTypeCd", workTypeMapList.get(position).get("workTypeCd"));
                } catch (JSONException | ArrayIndexOutOfBoundsException e) {
                    Log.e("에러","예외");
                }
                break;

        }

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
                        try{
                            String[] inwonTextViewStr =inwonJangbiResultTextView.getText().toString().split(", ");
                            Log.println(Log.ASSERT,"0", inwonJangbiTextView.getText().toString()+"");
                            Log.println(Log.ASSERT,"1", inwonTextViewStr.length+"");
                            Log.println(Log.ASSERT,"2", inwonTextViewStr.length+"");

                            inwonTextViewStr[0] = "인원 " + editText.getText() + "명, ";

                            inwonJangbiResultTextView.setText(inwonTextViewStr[0] + inwonTextViewStr[1] + ", " + inwonTextViewStr[2]);
                        }catch (StringIndexOutOfBoundsException|ArrayIndexOutOfBoundsException e){
                            Log.e("에러","에러발생");
                        }

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
                        String[] inwonTextViewStr = inwonJangbiResultTextView.getText().toString().split(", ");
                        inwonTextViewStr[1] = "사인카 " + editText.getText() + "대, ";

                        inwonJangbiResultTextView.setText(inwonTextViewStr[0] + ", " + inwonTextViewStr[1] + inwonTextViewStr[2]);
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

                        String[] inwonTextViewStr =inwonJangbiResultTextView.getText().toString().split(", ");
                        inwonTextViewStr[2] = "작업차 " + editText.getText() + "대";

                        inwonJangbiResultTextView.setText(inwonTextViewStr[0] + ", " + inwonTextViewStr[1] + ", " + inwonTextViewStr[2]);
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

    public boolean checkingValue(double oriStartGugan, double oriEndGugan, double startGuganEtInputValue, double endGuganEtInputValue) {
        try {
            if ((startGuganEtInputValue >= oriStartGugan && startGuganEtInputValue <= oriEndGugan) && (endGuganEtInputValue >= oriStartGugan) && (endGuganEtInputValue <= oriEndGugan)) {
                if (true) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            Log.e("에러","에러발생");
            return false;
        }
    }

    public void datePickerMethod(final TextView text) {
        Log.i("datePickerMethod", "요기");
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(TodayWorkPlanUpdateActivity_N.this, new DatePickerDialog.OnDateSetListener() {
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
                } catch (Exception e) {
                    Log.e("에러","에러발생");
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        datePicker.show();
    }

    public void maxCharoChoice() {


        final EditText editText = new EditText(this);
        editText.setText(maxCharo+"");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("총 차로를 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;
                        try {
                            int preChao = Integer.parseInt(editText.getText().toString());
                            /*   if(preChao <=6 && preChao <= maxCharo){*/
                            if (preChao != 0) {
                                if (preChao <= 6) {
                                    maxCharo = preChao;
                                    jsonResultObject.put("totCrgwCnt", Integer.toString(maxCharo));
                                } else {
                                    maxCharo = 6;
                                    jsonResultObject.put("totCrgwCnt", "6");
                                }

                                maxCharoSelected();
                            } else {
                                Toast.makeText(TodayWorkPlanUpdateActivity_N.this, "총 차로를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                                maxCharoChoice();
                            }
                        }catch (Exception e){
                            Log.e("에러","에러발생");
                        }
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


    public void maxCharoSelected() {
        if (roadlimitTextView.getText().length() > 0 && ( maxCharo != 0)) {

            // final String[] items = new String[]{"1차로", "2차로", "3차로", "4차로", "5차로", "6차로", "이동 차단", "갓길", "진입램프", "진출램프", "진출램프"};
            final String chaDanContent = "";
            final String[] items = new String[maxCharo + 8];
            for (int i = 0; i < maxCharo; i++) {
                //     items[0]=Integer.toString(i+1)+"차로";
                items[i] = i + 1 + "차로";
            }
            items[maxCharo] = "진입램프";
            items[maxCharo + 1] = "진출램프";
            items[maxCharo + 2] = "교량하부";
            items[maxCharo + 3] = "법면";
            items[maxCharo + 4] = "회차로";
            items[maxCharo + 5] = "이동차단";
            items[maxCharo + 6] = "갓길";
            items[maxCharo + 7] = "교대차단";
            final StringBuffer sb = new StringBuffer();
            final boolean[] itemsBool = new boolean[maxCharo + 7];
            for (int i =0;i<roadLimit.length;i++){
                roadLimit[i]=null;
            }
            totalroadLimit.clear();
            AlertDialog.Builder dialog = new AlertDialog.Builder(TodayWorkPlanUpdateActivity_N.this);

            dialog.setTitle("차단 차로를 선택하여주세요")
                    .setMultiChoiceItems(
                            items

                            , itemsBool
                            , new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    //

                                    if (isChecked) {
                                        //이동차단 여부 체크


                                        //if (!items[which].contains("진입램프") && !items[which].contains("진출램프") && !items[which].contains("교량하부") && !items[which].contains("법면") && !items[which].contains("회차로") && !items[which].contains("이동차단") && !items[which].contains("갓길")) {
                                            /*roadLimit.add(items[which]);*/
                                            //roadLimit.put(which+1,items[which]);
                                        roadLimit[which] =items[which];

                                        totalroadLimit.add(items[which]);
                                    } else {

                                        // roadLimit.remove(which);
                                        roadLimit[which] =null;
                                        totalroadLimit.remove(items[which]);
                                    }
                                }
                            }
                    ).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String roadListResult = "";


                    Map<String, String> loadLimitMap = new HashMap<String, String>();
                    loadLimitMap.put("1차로", "01");
                    loadLimitMap.put("2차로", "02");
                    loadLimitMap.put("3차로", "03");
                    loadLimitMap.put("4차로", "04");
                    loadLimitMap.put("5차로", "05");
                    loadLimitMap.put("6차로", "06");
                    loadLimitMap.put("진입램프", "07");
                    loadLimitMap.put("진출램프", "08");
                    loadLimitMap.put("교량하부", "09");
                    loadLimitMap.put("법면", "10");
                    loadLimitMap.put("회차로", "11");
                    loadLimitMap.put("이동차단", "20");
                    loadLimitMap.put("갓길", "30");
                    loadLimitMap.put("교대차단", "12");
                /*    if(loadLimitMap.get("이동차단")!=null){
                        codeMap.put("MVBL_BLC_YN","Y");
                    }*/
               /*      if (!items[which].contains("이동차단")) {
                        codeMap.put("MVBL_BLC_YN", "N");//이동차단여부
                     } else {
                        codeMap.put("MVBL_BLC_YN", "Y");//이동차단여부
                     }*/
                   try{
                       for (int i = 0; i < totalroadLimit.size(); i++) {
                           if (i != totalroadLimit.size() - 1) {
                               roadListResult += loadLimitMap.get(totalroadLimit.get(i)) + ",";
                           } else {
                               roadListResult += loadLimitMap.get(totalroadLimit.get(i));
                           }

                           if (totalroadLimit.get(i).equals("이동차단")) {
                               jsonResultObject.put("MVBL_BLC_YN", "Y");//이동차단여
                           }
                       }
                       jsonResultObject.put("trfcLimtCrgwClssCd", roadListResult);

                   }catch (Exception e){
                       Log.e("에러","에러발생");
                   }


                    //TRFC_LIMT_CTGW_CLSS_CD
                    Log.i("로드 Resulst", roadListResult);
                    jsonArray = new JSONArray();
                    //sb.append(", "+loadLimitMap.get(items[which]));
                    Log.i("sb 확인", sb.toString());
                    try {
                        for (int k = 0; k < roadLimit.length; k++) {
                            jsonObject = new JSONObject();
                            jsonArray.put(k, jsonObject.put("TRFC_LIMT_CRGW_CLSS_CD", loadLimitMap.get(totalroadLimit.get(k-1))));
                        }
                        jsonResultObject.put("trfcLimtCrgwClssCd", jsonArray.toString());
                    } catch (Exception e) {
                        Log.e("에러","에러발생");
                    }

                    roadlimitTextView.clearComposingText();
                    String charhDetail = "";
                    for(int i = 0 ; i<roadLimit.length;i++){
                        if(roadLimit[i]!=null){
                            charhDetail+=","+roadLimit[i];
                        }

                    }
                    try{
                        Log.i("텍스트 길이",roadlimitTextView.length()+"");
                        /*if(charhDetail.length()>20){*/
                        if(charhDetail.length()>14){

                            roadlimitTextView.setTextSize(8.5f);
                            // roadlimitTextView.setTextSize(10.5f);
                            roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + charhDetail.substring(1) + " 차단");
                        }else{
                            roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + charhDetail.substring(1) + " 차단");
                        }
                    }catch (Exception e){
                        roadlimitTextView.setText("총 " + maxCharo + "차로 중 " +  "차단");
                    }

                    /*   roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + roadLimit.size() + "차로 차단");*/
                    // roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + charhDetail.substring(1) + "차로 차단");
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.create();
            dialog.show();

        }
    }

}
