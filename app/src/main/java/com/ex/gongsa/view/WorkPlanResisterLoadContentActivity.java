package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import static com.ex.gongsa.Common.intStrNullCheck;
import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;

public class WorkPlanResisterLoadContentActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Intent intent;
    Intent userInfoIntent = null;

/*    LinearLayout Li_gosundae_no1;
    LinearLayout Li_gosundae_no2;
    LinearLayout Li_gosundae_no3;
    LinearLayout Li_gosundae_no4;*/

    //2021.06 ??????????????????

    Spinner bonbuSpinner;//?????? ?????????
    Spinner jisaSpinner;//?????? ?????????
    Spinner nosunSpinner;//?????? ?????????
    Spinner direnctionSpinner;//?????? ?????????
    Spinner charoSpinner;
    Spinner workTypeSpinner;//???????????? ?????????
    Spinner gamdokSpinner;
    Spinner startHourSpinner;
    Spinner startMinuteSpinner;
    Spinner endHourSpinner;
    Spinner endMinuteSpinner;
    Spinner inwonJangbiSpinner;
    Spinner workTypeCATSpinner; // 2021.07 ????????????(?????????) ?????????

    TextView bonbuTextView;//?????? ????????? ???
    TextView jisaTextView;//?????? ????????? ???
    TextView noSunTextView;//?????? ????????????
    TextView directionTextView;//?????? ????????????
    TextView roadlimitTextView;//????????????
    TextView inwonJangbiResultTextView;//?????? ?????? ????????????
    TextView startDateTextView;
    TextView endDateTextView;
    TextView workTypeTextView;//???????????? ????????????
    TextView gamdokwonTextView;
    TextView startHourTextView;
    TextView startMinuteTextView;
    TextView endHourTextView;
    TextView endMinuteTextView;
    TextView inwonJangbiTextView;
    TextView workTypeCATTextView; // 2021.07 ????????????(?????????)?????????


 /*   View view_gosundae_no1;
    View view_gosundae_no2;
    View view_gosundae_no3;
    View view_gosundae_no4;*/

    EditText ET_startGongsaGugan;//????????????
    EditText ET_endGongsaGugan;//?????? ????????????
    EditText ET_gongsaContent;//????????????
    //EditText ET_gosundaeNum;//???????????????
    /*EditText ET_gosundaeNum_no1;
    EditText ET_gosundaeNum_no2;
    EditText ET_gosundaeNum_no3;
    EditText ET_gosundaeNum_no4;*/

    ImageView plus_btn;
    /*   ImageView minus_btn1;
       ImageView minus_btn2;
       ImageView minus_btn3;
       ImageView minus_btn4;*/
    ImageView startDateImageView;
    ImageView endDateImageView;
    ImageView gongsaPrev;

    JSONObject jsonObject;
    JSONObject jsonResultObject;
    JSONObject jobUserInfo;
    JSONArray jsonArray;

    List<Map<String, String>> bonbuMapList = null;//?????????
    List<Map<String, String>> jisaMapList = null;//?????????
    List<Map<String, String>> nosunMapList = null;//??????????????????
    List<Map<String, String>> iJungMapList = null;
    List<Map<String, String>> gamdokMapList = null;
    //202009_?????????????????????
    List<Map<String, String>> workTypeMapList = null; //???????????? ???
    //2021.07 ????????????(?????????) ???
    Map<String, String> workTypeCATMap;
    List<Map<String, String>> workTypeCATMapList = null;
    String[] workTypeCATNM;
    String[] workTypeCATCD;


    List<String> bonbuList = null;//???????????????
    List<String> jisaList = null;//?????? ?????????
    List<String> nusunList = null;//???????????????
    List<String> ijungList = null;//?????? ?????????
    List<String> workTypeList;
    List<String> workTypeCATList; // 2021.07 ????????????(?????????) ?????????


    String gamdokList = null;
    List<String> inwonJangbiList = null;
    ArrayList<String> totalroadLimit;
    List<String> startHourList = null;
    List<String> startMinList = null;
    List<String> endHourList = null;
    List<String> endMinList = null;


    ArrayAdapter<String> jisaAdapter;

    LinearLayout li_registerBtn;//????????????
    LinearLayout li_ResetBtn;
    LinearLayout gosundae_list;


    int chaDanChaoCnt;
    String chdanBangsik = "";
    int maxCharo;

    double maxGugan = 0.0;
    double minGugan = 0.0;
    String todayParam;
    String[] roadLimit;//???????????? ?????????

    Map<String, String> parameterMap = null;
    String userInfo;

    String gamdokTvResult = "";
    String gamdokTelno = "";
    String gamdokSawonNo = "";

    boolean gamdokSppinerFlag = false;

    String[] gamdokName = null;
    boolean[] gamdokItemBoo = null;
    AlertDialog.Builder gamdokdialog;
    JSONArray gamdokJsonArray;

    //Stack<EditText> que_et_list = new Stack<EditText>();
    List<EditText> que_et_list = new ArrayList<EditText>();
    String gamdokResult;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.z_work_plan_resister_load_content);
        intent = getIntent();
        //  userInfoIntent =getIntent();


        li_registerBtn = (LinearLayout) findViewById(R.id.li_registerBtn);
        li_ResetBtn = (LinearLayout) findViewById(R.id.li_ResetBtn);
        gosundae_list = (LinearLayout) findViewById(R.id.gosundae_list);
/*
        Li_gosundae_no1 =(LinearLayout)findViewById(R.id.Li_gosundae_no1);
        Li_gosundae_no2 = (LinearLayout)findViewById(R.id.Li_gosundae_no2);
        Li_gosundae_no3 =(LinearLayout)findViewById(R.id.Li_gosundae_no3);
        Li_gosundae_no4 = (LinearLayout)findViewById(R.id.Li_gosundae_no4);
*/

        bonbuSpinner = (Spinner) findViewById(R.id.bonbuSpinner);//?????? ?????????
        jisaSpinner = (Spinner) findViewById(R.id.jisaSpinner);//?????? ?????????
        nosunSpinner = (Spinner) findViewById(R.id.nosunSpinner);
        direnctionSpinner = (Spinner) findViewById(R.id.direnctionSpinner);
        charoSpinner = (Spinner) findViewById(R.id.charoSpinner);
        workTypeSpinner = (Spinner) findViewById(R.id.workTypeSpinner);
        gamdokSpinner = (Spinner) findViewById(R.id.gamdokSpinner);
        startHourSpinner = (Spinner) findViewById(R.id.startHourSpinner);
        startMinuteSpinner = (Spinner) findViewById(R.id.startMinuteSpinner);
        endHourSpinner = (Spinner) findViewById(R.id.endHourSpinner);
        endMinuteSpinner = (Spinner) findViewById(R.id.endMinuteSpinner);
        inwonJangbiSpinner = (Spinner) findViewById(R.id.inwonJangbiSpinner);
        //2021.07 ????????????(?????????)
        workTypeCATSpinner = (Spinner) findViewById(R.id.workTypeCATSpinner); //????????????(?????????)


/*        view_gosundae_no1 = (View)findViewById(R.id.view_gosundae_no1);
        view_gosundae_no2 = (View)findViewById(R.id.view_gosundae_no2);
        view_gosundae_no3 = (View)findViewById(R.id.view_gosundae_no3);
        view_gosundae_no4 = (View)findViewById(R.id.view_gosundae_no4);*/

        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);//?????? ????????????
        jisaTextView = (TextView) findViewById(R.id.jisaTextView);//?????? ????????????
        noSunTextView = (TextView) findViewById(R.id.noSunTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);
        inwonJangbiResultTextView = (TextView) findViewById(R.id.inwonJangbiResultTextView);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView);//???????????? ????????????
        gamdokwonTextView = (TextView) findViewById(R.id.gamdokwonTextView);
        startHourTextView = (TextView) findViewById(R.id.startHourTextView);
        startMinuteTextView = (TextView) findViewById(R.id.startMinuteTextView);
        endHourTextView = (TextView) findViewById(R.id.endHourTextView);
        endMinuteTextView = (TextView) findViewById(R.id.endMinuteTextView);
        inwonJangbiTextView = (TextView) findViewById(R.id.inwonJangbiTextView);
        workTypeCATTextView = (TextView) findViewById(R.id.workTypeCATTextView); //2021.07 ????????????(?????????)

        ET_startGongsaGugan = (EditText) findViewById(R.id.ET_startGongsaGugan);
        ET_endGongsaGugan = (EditText) findViewById(R.id.ET_endGongsaGugan);
        ET_gongsaContent = (EditText) findViewById(R.id.ET_gongsaContent);
        // ET_gosundaeNum=(EditText)findViewById(R.id.ET_gosundaeNum);
   /*     ET_gosundaeNum_no1 =(EditText)findViewById(R.id.ET_gosundaeNum_no1);
        ET_gosundaeNum_no2 = (EditText)findViewById(R.id.ET_gosundaeNum_no2);
        ET_gosundaeNum_no3 =(EditText)findViewById(R.id.ET_gosundaeNum_no3);
        ET_gosundaeNum_no4 = (EditText)findViewById(R.id.ET_gosundaeNum_no4);*/

        startDateImageView = (ImageView) findViewById(R.id.startDateImageView);
        endDateImageView = (ImageView) findViewById(R.id.endDateImageView);
        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
        plus_btn = (ImageView) findViewById(R.id.plus_btn);//????????? ?????? ??????
     /*   minus_btn1 = (ImageView)findViewById(R.id.minus_btn1); //????????? ????????????
        minus_btn2 = (ImageView)findViewById(R.id.minus_btn2); //????????? ????????????
        minus_btn3 = (ImageView)findViewById(R.id.minus_btn3); //????????? ????????????
        minus_btn4 = (ImageView)findViewById(R.id.minus_btn4); //????????? ????????????*/

        bonbuMapList = new ArrayList<Map<String, String>>();
        jisaMapList = new ArrayList<Map<String, String>>();
        nosunMapList = new ArrayList<Map<String, String>>();
        iJungMapList = new ArrayList<Map<String, String>>();
        gamdokMapList = new ArrayList<Map<String, String>>();
        //202009_?????????????????????
        workTypeMapList = new ArrayList<Map<String, String>>();
        workTypeCATMapList = new ArrayList<Map<String, String>>(); //2021.07 ????????????


        bonbuList = new ArrayList<String>();
        jisaList = new ArrayList<String>();
        nusunList = new ArrayList<String>();
        ijungList = new ArrayList<String>();
        workTypeList = new ArrayList<String>();
        //gamdokList=new ArrayList<String>();
        gamdokList = "";
        inwonJangbiList = new ArrayList<String>();
        totalroadLimit = new ArrayList<String>();
        startHourList = new ArrayList<String>();
        startMinList = new ArrayList<String>();
        endHourList = new ArrayList<String>();
        endMinList = new ArrayList<String>();


        gongsaPrev.setOnClickListener(this);
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
        plus_btn.setOnClickListener(this);//????????? ???????????? +??????
        workTypeCATTextView.setOnClickListener(this); // 2021.07 ????????????(?????????)
/*        minus_btn1.setOnClickListener(this);
        minus_btn2.setOnClickListener(this);
        minus_btn3.setOnClickListener(this);
        minus_btn4.setOnClickListener(this);*/
        Log.println(Log.ASSERT, "???????????? ??????", "???????????? ?????? : " + intent.getStringExtra("todayWorkPlanRegister"));

        todayParam = intent.getStringExtra("todayWorkPlanRegister");
        userInfo = intent.getStringExtra("userInfo");
        parameterMap = new HashMap<String, String>();
        parameterMap.put("gamdokList_in_bigo", "");
        try {
            jobUserInfo = new JSONObject(userInfo);

            jsonResultObject = new JSONObject(todayParam);
            Log.println(Log.ASSERT, "???????????? ??????", "???????????? ?????? : " + jsonResultObject.toString());
            ET_startGongsaGugan.setText(jsonResultObject.get("strtBlcPntVal").toString());
            //userInfo
            ET_endGongsaGugan.setText(jsonResultObject.get("endBlcPntVal").toString());
            ET_gongsaContent.setText(jsonResultObject.get("cnstnCtnt").toString());
          /*  try{
                String[] gosundaeNo = jsonResultObject.get("gosundaeNo").toString().split("-");
                ET_gosundaeNum.setText(gosundaeNo[0]+gosundaeNo[1]+gosundaeNo[2]);

            }catch (Exception e){
                ET_gosundaeNum.setText(jsonResultObject.get("gosundaeNo").toString());

            }*/
            String[] gosundaeList = jsonResultObject.get("gosundaeNo").toString().split(",");

     /*       que_et_list.add(ET_gosundaeNum_no1);
            que_et_list.add(ET_gosundaeNum_no2);
            que_et_list.add(ET_gosundaeNum_no3);
            que_et_list.add(ET_gosundaeNum_no4);*/

            setGosundaeNo(gosundaeList, gosundaeList.length);
            ArrayList<String> bonbuParam = new ArrayList<String>();
            bonbuParam.add("hdqrCd");
            bonbuParam.add("hdqrNm");
            setAdapterModule(bonbuSpinner, bonbuMapList, bonbuList, SERVER_URL + "/TodayWorkPlan/settingBonbu.do", todayParam, bonbuParam, "hdqrNm", bonbuTextView, false, "firstAciton");
///----------------------------w

            ArrayList<String> jisaParam = new ArrayList<String>();
            jisaParam.add("mtnofCd");
            jisaParam.add("mtnofNm");
            setAdapterModule(jisaSpinner, jisaMapList, jisaList, SERVER_URL + "/TodayWorkPlan/settingJisa.do", todayParam, jisaParam, "mtnofNm", jisaTextView, false, "firstAciton");
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("??????","?????? ??????");
        }

        try {

            ArrayList<String> nosunParam = new ArrayList<String>();
            nosunParam.add("routeNm");
            nosunParam.add("routeCd");
            setAdapterModule(nosunSpinner, nosunMapList, nusunList, SERVER_URL + "/TodayWorkPlan/settingNosun.do", todayParam, nosunParam, "routeNm", noSunTextView, false, "firstAciton");
        } catch (NullPointerException e) {
            Log.e("??????","??????");
        }

        try {
            jsonResultObject.put("gamdokList_in_bigo", "");
            ArrayList<String> ijungParam = new ArrayList<String>();
            ijungParam.add("drctClssNm");
            ijungParam.add("drctClssCd");
            setAdapterModule(direnctionSpinner, iJungMapList, ijungList, SERVER_URL + "/TodayWorkPlan/settingInjung.do", todayParam, ijungParam, "drctClssNm", directionTextView, false, "firstAciton");

            //????????????
            jsonObject = new JSONObject(todayParam);
            Log.println(Log.ASSERT, "todayWork ??????", jsonObject.toString());
            String[] chdanListSplit = jsonObject.get("trfcLimtCrgwClssCd").toString().split(",");//?????? ?????? ????????? ?????? String[]??? ??????
            Log.i("???????????? ??????", jsonObject.get("trfcLimtCrgwClssCd").toString());
            for (int i = 0; i < chdanListSplit.length; i++) {
                //???????????? ????????? ????????? ????????? ?????? ?????????
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

                //????????? ????????????????????? ????????? ?????? ????????? ?????? ?????? ??????
                if (chdanListSplit[i].equals("01")) {
                    chdanBangsik += ", 1??????";
                } else if (chdanListSplit[i].equals("02")) {
                    chdanBangsik += ", 2??????";
                } else if (chdanListSplit[i].equals("03")) {
                    chdanBangsik += ", 3??????";
                } else if (chdanListSplit[i].equals("04")) {
                    chdanBangsik += ", 4??????";
                } else if (chdanListSplit[i].equals("05")) {
                    chdanBangsik += ", 5??????";
                } else if (chdanListSplit[i].equals("06")) {
                    chdanBangsik += ", 6??????";
                } else if (chdanListSplit[i].equals("07")) {
                    chdanBangsik += ", ????????????";
                } else if (chdanListSplit[i].equals("08")) {
                    chdanBangsik += ", ????????????";
                } else if (chdanListSplit[i].equals("09")) {
                    chdanBangsik += ", ????????????";
                } else if (chdanListSplit[i].equals("10")) {
                    chdanBangsik += ", ??????";
                } else if (chdanListSplit[i].equals("11")) {
                    chdanBangsik += ", ?????????";
                } else if (chdanListSplit[i].equals("20")) {
                    chdanBangsik += ", ????????????";
                } else if (chdanListSplit[i].equals("30")) {
                    chdanBangsik += ", ??????";
                } else if (chdanListSplit[i].equals("12")) {
                    chdanBangsik += ", ????????????";
                }

            }
            //  roadlimitTextView.setText("??? "+jsonObject.get("totCrgwCnt")+" ??? " +chdanBangsik);
            try {
                maxCharo = Integer.parseInt(jsonObject.get("totCrgwCnt").toString());
                if (chdanBangsik.length() > 20) {
                    roadlimitTextView.setTextSize(9.5f);

                    roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + chdanBangsik.substring(1) + " ??????");
                } else {
                    roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + chdanBangsik.substring(1) + " ??????");
                }
            } catch (NullPointerException e) {
                roadlimitTextView.setText("?????? ?????? ??????");
                Log.e("??????","??????");
            } catch (Exception e) {
                roadlimitTextView.setText("?????? ?????? ??????");
                Log.e("??????","??????");
            }

            inwonJangbiResultTextView.setText(jsonObject.get("cmmdNm").toString());

        } catch (NullPointerException | JSONException e) {
            Log.e("??????","??????");
        } catch (Exception e){
            Log.e("??????","??????");
        }

        //2021.07 ????????????(?????????)


        try{
            workTypeCATMap = new HashMap<String,String>();
            workTypeCATMap.put("??????????????????","021");
            workTypeCATMap.put("????????????","022");
            workTypeCATMap.put("???????????????","023");
            workTypeCATMap.put("????????????","024");
            workTypeCATMap.put("???????????????","025");
            workTypeCATMap.put("??????","020");


            workTypeCATMapList.add(workTypeCATMap);
            workTypeCATList.add("??????????????????");
            workTypeCATList.add("????????????");
            workTypeCATList.add("???????????????");
            workTypeCATList.add("????????????");
            workTypeCATList.add("???????????????");
            workTypeCATList.add("???????????????");



           /* new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeCATSpinner, workTypeCATList);
            workTypeCATSpinner.setSelection(0);
            workTypeCATTextView.setText(workTypeCATSpinner.getSelectedItem().toString());*/


        }catch (Exception e){

        }

        try{
            //2021.07 ????????????(?????????) - ??????????????? ????????? ????????????
            workTypeCATNM = getResources().getStringArray(R.array.workTypeCategorizationName);
            workTypeCATCD = getResources().getStringArray(R.array.workTypeCategorizationCode);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workTypeCATNM);
            workTypeCATSpinner.setAdapter(adapter);
            workTypeCATSpinner.setSelection(0);

        }catch (Exception e){

        }

        try {
            ArrayList<String> workTypeParam = new ArrayList<String>();
            workTypeParam.add("workType");
            //202009_????????????
            workTypeParam.add("workTypeCd");

            System.out.println("------------------------------------------- workTypeParam " + workTypeParam.toString());

            //202009_????????????
            //setAdapterModule(workTypeSpinner, null, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkType.do", todayParam, workTypeParam, "workType", workTypeTextView, false);
            System.out.println("------------------------------------ todayParam ==== " + todayParam.toString());
            JSONObject jobj = new JSONObject();
            try{
                jobj.put("workTypeCd",new JSONObject(todayParam).getString("workTypeCd"));
                jobj.put("subWorkTypeCd",new JSONObject(todayParam).getString("workTypeCd").substring(0,3));
            }catch (Exception e){

            }

            //2021.07 ????????????(?????????)

            for( Map.Entry<String,String> entry : workTypeCATMap.entrySet()){
                System.out.println("========================= entry.getValue() ============================ " + entry.getValue());
                if (entry.getValue().equals(jobj.getString("subWorkTypeCd"))){
                    System.out.println("========================= entry.getValue() ============================ " + entry.getKey());
                    workTypeCATTextView.setText(entry.getKey());
                    break;
                }else{

                }
            }

            //2021.07 ????????????_ ??????????????? ??????????????? ???????????? ???
            jsonResultObject.put("workType", new JSONObject(todayParam).getString("workType"));
            jsonResultObject.put("workTypeCd", new JSONObject(todayParam).getString("workTypeCd"));

            String obj = jobj.toString();
            System.out.println("========================= obj ============================ " + obj);
            workTypeTextView.setText(new JSONObject(todayParam).getString("workType"));
            setAdapterModule(workTypeSpinner,workTypeMapList, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkTypeCode.do",obj, workTypeParam, "workType", workTypeTextView, false, "firstAction");
            //2021.07 ????????????(?????????) TEST
            //setAdapterModule(workTypeSpinner,workTypeMapList, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkType.do", todayParam, workTypeParam, "workType", workTypeTextView, false);

        } catch (NullPointerException | JSONException e) {
            Log.e("??????","??????");
        }

        //--????????????

        try {

            ArrayList<String> gamdokParam = new ArrayList<String>();
            gamdokParam.add("cstrCpprPrchTelno");
            gamdokParam.add("gamdokNameEMNM");
            gamdokParam.add("mtnofPrchEmno");
            Log.println(Log.ASSERT, "?????? ??????", gamdokParam.toString());
            Log.println(Log.ASSERT, "?????? ??????", todayParam.toString());
            /*gamdokwonTextView.setText();*/
            gamokAlertInitDialog(todayParam, gamdokSpinner, SERVER_URL + "/TodayWorkPlan/getGamdok.do", gamdokwonTextView, Common.nullCheck(new JSONObject(todayParam).getString("gamdokNameEMNM")), Common.nullCheck(new JSONObject(todayParam).getString("cstrCpprPrchTelno").toString().replace("-", "")), new JSONObject(todayParam).getString("mtnofPrchEmno").toString());//cstrCpprPrchTelno
            //    setAdapterModule(gamdokSpinner,gamdokMapList,gamdokList,SERVER_URL + "/TodayWorkPlan/getGamdok.do",todayParam,gamdokParam,"gamdokNameEMNM",gamdokwonTextView,false);//cstrCpprPrchTelno
        } catch (NullPointerException |JSONException e) {
            Log.e("??????","NullPointerException |JSONException ");
        }

        try {
            /*startDateTextView.setText(nullCheck(jsonResultObject.get("blcStrtDttm").toString()));*/
            startDateTextView.setText(calStr()[0]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            startDateTextView.setText("");
        }
        try {
            endDateTextView.setText(calStr()[0]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            endDateTextView.setText("");
        }
        try {
            /*startHourTextView.setText(nullCheck(jsonResultObject.get("startGongsaHour").toString()));
            startMinuteTextView.setText(nullCheck(jsonResultObject.get("startGongsaMinute").toString()));
            endHourTextView.setText(nullCheck(jsonResultObject.get("endGongsaHour").toString()));
            endMinuteTextView.setText(nullCheck(jsonResultObject.get("endGongsaMinute").toString()));
            */
            startHourTextView.setText("09");
            startMinuteTextView.setText("00");
            endHourTextView.setText("18");
            endMinuteTextView.setText("00");

        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            Log.e("??????","ArrayIndexOutOfBoundsException | NullPointerException ");
        }


        ET_startGongsaGugan.setTextColor(-4276546);
        ET_endGongsaGugan.setTextColor(-4276546);
        ET_gongsaContent.setTextColor(-4276546);
       /* ET_gosundaeNum.setTextColor(-4276546);
        ET_gosundaeNum.setInputType(InputType.TYPE_CLASS_NUMBER);*/
  /*      ET_gosundaeNum_no1.setTextColor(-4276546);
        ET_gosundaeNum_no2.setTextColor(-4276546);
        ET_gosundaeNum_no3.setTextColor(-4276546);
        ET_gosundaeNum_no4.setTextColor(-4276546);
        ET_gosundaeNum_no1.setInputType(InputType.TYPE_CLASS_NUMBER);
        ET_gosundaeNum_no2.setInputType(InputType.TYPE_CLASS_NUMBER);
        ET_gosundaeNum_no3.setInputType(InputType.TYPE_CLASS_NUMBER);
        ET_gosundaeNum_no4.setInputType(InputType.TYPE_CLASS_NUMBER);*/

        try {
            String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL + "/TodayWorkPlan/getGugan.do", todayParam, this).execute().get()), "UTF-8");
            jsonObject = new JSONObject(guganMinMaxValue);
            Log.e("?????? ??????", guganMinMaxValue);
            maxGugan = Double.parseDouble(jsonObject.get("maxMtnofEdpntDstnc").toString());
            minGugan = Double.parseDouble(jsonObject.get("minMtnofStpntDstnc").toString());
        } catch (JSONException | NullPointerException | NumberFormatException | InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            Log.e("??????","??????");;
        }


        try {
            inwonJangbiList.add("??????");
            inwonJangbiList.add("?????????");
            inwonJangbiList.add("?????????");
            inwonJangbiList.add("?????????");

            inwonJangbiSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, inwonJangbiList));
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
            roadLimit = new String[14];

            startHourList.add(startHourTextView.getText().toString());
            for (int i = 0; i <= 24; i++) {
                if (Integer.toString(i).length() == 1) {
                    startHourList.add("0" + i + "");
                } else {
                    startHourList.add(i + "");
                }
            }
            startHourSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, startHourList));
            startHourSpinner.setSelection(0);

            endHourList.add(endHourTextView.getText().toString());
            for (int i = 0; i <= 24; i++) {
                if (Integer.toString(i).length() == 1) {
                    endHourList.add("0" + i + "");
                } else {
                    endHourList.add(i + "");
                }
            }
            endHourSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, endHourList));
            endHourSpinner.setSelection(0);

            startMinList.add(startMinuteTextView.getText().toString());
            for (int i = 0; i <= 60; i++) {
                if (Integer.toString(i).length() == 1) {
                    startMinList.add("0" + i + "");
                } else {
                    startMinList.add(i + "");
                }

            }
            startMinuteSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, startMinList));
            startMinuteSpinner.setSelection(0);


            endMinList.add(endMinuteTextView.getText().toString());
            for (int i = 0; i <= 60; i++) {

                if (Integer.toString(i).length() == 1) {
                    endMinList.add("0" + i + "");
                } else {
                    endMinList.add(i + "");
                }
            }
            endMinuteSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, endMinList));
            endMinuteSpinner.setSelection(0);

        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            Log.e("??????","ArrayIndexOutOfBoundsException | NullPointerException ");
        }
    }

    final public String[] calStr() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        String montthStr = Integer.toString(month);
        if (montthStr.length() == 1) montthStr = "0" + montthStr;
        String dateStr = Integer.toString(cal.get(Calendar.DATE));
        if (dateStr.length() == 1) dateStr = "0" + dateStr;
        String yearMon = cal.get(Calendar.YEAR) + "/" + montthStr + "/" + dateStr;
       /* String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String min = Integer.toString(cal.get(Calendar.MINUTE));*/
        String hour = "09";
        String min = "18";
        String[] calArray = {"", "", ""};
        if (hour.length() == 1) hour = "0" + hour;
        if (min.length() == 1) min = "0" + min;
        calArray[0] = yearMon;
        calArray[1] = hour;
        calArray[2] = min;
        return calArray;
    }

    @Override
    public String onActionPost(String primitive, String date) {
        return null;
    }


    public void setGosundaeNo(String[] gosundaeArray, int length) {
        switch (1) {
            case 1:
                //  ET_gosundaeNum.setText(Common.nullCheck(gosundaeArray[0].replaceAll("-", "")));
                addView(Common.nullCheck(gosundaeArray[0].replaceAll("-", "")));
                // que_et_list.add(ET_gosundaeNum);
                if (1 == length) break;
            case 2:
                //que_et_list.add(ET_gosundaeNum_no1);
             /*   view_gosundae_no1.setVisibility(View.VISIBLE);
                Li_gosundae_no1.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no1.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no1.setText(Common.nullCheck(gosundaeArray[1].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[1].replaceAll("-", "")));
                if (2 == length) break;
            case 3:
                //que_et_list.add(ET_gosundaeNum_no2);
             /*   view_gosundae_no2.setVisibility(View.VISIBLE);
                Li_gosundae_no2.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no2.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no2.setText(Common.nullCheck(gosundaeArray[2].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[2].replaceAll("-", "")));
                if (3 == length) break;
            case 4:
                // que_et_list.add(ET_gosundaeNum_no3);
                /*view_gosundae_no3.setVisibility(View.VISIBLE);
                Li_gosundae_no3.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no3.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no3.setText(Common.nullCheck(gosundaeArray[3].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[3].replaceAll("-", "")));
                if (4 == length) break;
            case 5:
                //   que_et_list.add(ET_gosundaeNum_no4);
               /* view_gosundae_no4.setVisibility(View.VISIBLE);
                Li_gosundae_no4.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no4.setVisibility(View.VISIBLE);
                ET_gosundaeNum_no4.setText(Common.nullCheck(gosundaeArray[4].replaceAll("-", "")));*/
                addView(Common.nullCheck(gosundaeArray[4].replaceAll("-", "")));
                if (5 == length) break;
        }
    }


    public void gamokAlertInitDialog(String selectedBonboAndJisaCd, Spinner gamdokSpinner, String url, TextView tv, String gamdokList, String gamdokTelno, String gamdokSawonNo) {
        //this.gamdokList="";
        try {
            tv.setText(gamdokList);
            JSONObject job = new JSONObject(selectedBonboAndJisaCd);
            String[] gamdokSetParam = gamdokTelno.split(",");
            String[] gamdokNameSetparam = gamdokList.split(",");
            gamdokResult = "";
            for (int i = 0; i < gamdokSetParam.length; i++) {
                gamdokResult += "\n" + gamdokNameSetparam[i] + "(" + "" + gamdokSetParam[i] + ")";
            }

            parameterMap.put("gamdokList_in_bigo", gamdokResult);
            Log.i("????????? ?????????", parameterMap.get("gamdokList_in_bigo").toString());
            Log.i("????????? ?????? ??????", job.toString());
            Log.i("????????? ?????? ??????", gamdokList);
            //
            //
            //
            // "hdqrCd":"N02723", "mtnofCd":"N01046",
            String result = nullCheck(URLDecoder.decode(new Action("get", url, selectedBonboAndJisaCd, this).execute().get(), "UTF-8"));
            Log.i("????????? ?????? ??????", result);

            gamdokwonTextView.clearComposingText();
            gamdokSpinner.performClick();
            gamdokJsonArray = new JSONArray(result);
            gamdokName = new String[gamdokJsonArray.length()];
            gamdokItemBoo = new boolean[gamdokJsonArray.length()];
            Log.println(Log.ASSERT, "????????? ????????? ??????", gamdokJsonArray.length() + "");
            for (int i = 0; i < gamdokJsonArray.length(); i++) {
                Log.println(Log.ASSERT, "????????? ??????,???", gamdokJsonArray.get(i).toString() + "");
                gamdokItemBoo[i] = false;
                gamdokName[i] = gamdokJsonArray.getJSONObject(i).get("gamdokNameEMNM").toString();

                //


            }


            parameterMap.put("MTNOF_PRCH_EMNO_TELNO", gamdokTelno);
            parameterMap.put("MTNOF_PRCH_EMNO", gamdokSawonNo);
            Log.i("??????", gamdokTelno);
            Log.i("??????", gamdokSawonNo);
      /*      Log.i("??????11",jsonResultObject.get("MTNOF_PRCH_EMNO_TELNO").toString());
            Log.i("??????22",jsonResultObject.get("MTNOF_PRCH_EMNO").toString());*/
            /*   final AlertDialog.Builder*/
            gamdokdialog = new AlertDialog.Builder(this);
        } catch ( JSONException | NullPointerException | InterruptedException | ExecutionException | UnsupportedEncodingException e) {
             Log.e("??????"," JSONException | NullPointerException | InterruptedException | ExecutionException ");
        }

    }


    public void gamokAlertDialog(String selectedBonboAndJisaCd, Spinner gamdokSpinner, String url, TextView tv, String gamdokList) {

        try {
            tv.setText(gamdokList);
            JSONObject job = new JSONObject(selectedBonboAndJisaCd);
            Log.i("????????? ?????? ??????", job.toString());
            Log.i("????????? ?????? ??????", gamdokList);
            //
            //
            //
            // "hdqrCd":"N02723", "mtnofCd":"N01046",
            String result = nullCheck(URLDecoder.decode(new Action("get", url, selectedBonboAndJisaCd, this).execute().get(), "UTF-8"));
            Log.i("????????? ?????? ??????", result);

            gamdokwonTextView.clearComposingText();
            gamdokSpinner.performClick();
            gamdokJsonArray = new JSONArray(result);
            gamdokName = new String[gamdokJsonArray.length()];
            gamdokItemBoo = new boolean[gamdokJsonArray.length()];
            Log.println(Log.ASSERT, "????????? ????????? ??????", gamdokJsonArray.length() + "");
            for (int i = 0; i < gamdokJsonArray.length(); i++) {
                Log.println(Log.ASSERT, "????????? ??????,???asdasd", gamdokJsonArray.get(i).toString() + "");
                gamdokItemBoo[i] = false;
                gamdokName[i] = gamdokJsonArray.getJSONObject(i).get("gamdokNameEMNM").toString();
            }
            tv.clearComposingText();
            tv.setText(gamdokName[0]);
            gamdokItemBoo[0] = true;

            parameterMap.put("MTNOF_PRCH_EMNO_TELNO", gamdokTelno.substring(1));
            parameterMap.put("MTNOF_PRCH_EMNO", gamdokSawonNo.substring(1));
            /*   final AlertDialog.Builder*/
            gamdokdialog = new AlertDialog.Builder(this);


        }  catch ( JSONException | ArrayIndexOutOfBoundsException | NullPointerException | InterruptedException | ExecutionException | UnsupportedEncodingException | StringIndexOutOfBoundsException e) {
            Log.e("??????"," JSONException | NullPointerException | InterruptedException | ExecutionException ");
        }

    }

    public void showGamdokDialog(final AlertDialog.Builder gamdokdialog, final String[] gamdokName, final boolean[] gamdokItemBoo) {
        try {
            for (int i = 0; i < gamdokItemBoo.length; i++) {
                gamdokItemBoo[i] = false;
            }
            gamdokList = "";
            gamdokTelno = "";
            gamdokSawonNo = "";
            gamdokTvResult = "";
            gamdokdialog.setMultiChoiceItems(gamdokName, gamdokItemBoo, new DialogInterface.OnMultiChoiceClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    if (b == true) {

                        //gamdokwonTextView.append(gamdokName[i]+",");


                        String[] itemCheck = null;

//[{"cstrCpprPrchTelno":"010-9189-2421","gamdokNameEMNM":"?????????"},{"cstrCpprPrchTelno":"010-8914-8602","gamdokNameEMNM":"?????????"},{"cstrCpprPrchTelno":"010-4945-0845","gamdokNameEMNM":"?????????"},{"cstrCpprPrchTelno":"010-7728-9263","gamdokNameEMNM":"??????"}]
                        try {
                            if (!gamdokSawonNo.contains(gamdokJsonArray.getJSONObject(i).getString("mtnofPrchEmno"))) {
                                gamdokTvResult += "," + gamdokName[i];
                                gamdokTelno += "," + gamdokJsonArray.getJSONObject(i).getString("cstrCpprPrchTelno");


                                gamdokSawonNo += "," + gamdokJsonArray.getJSONObject(i).getString("mtnofPrchEmno");//USER_EMNO



                     /*       String[] itemCheck = gamdokSawonNo.substring(1).split("-");
                            int cnt = 0 ;
                            for (int ii = 0 ; ii < itemCheck.length;ii++){
                                cnt++;
                                if(cnt>=6){
                                    Toast.makeText(WorkPlanResisterLoadContentActivity.this,"?????? ???????????? ???????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show();
                                    gamdokItemBoo[ii]=false;
                                    ((AlertDialog)dialogInterface).getListView().setItemChecked(ii,false);
                                    dialogInterface.dismiss();
                                    gamdokTvResult="";
                                    gamdokTelno="";
                                    gamdokSawonNo="";

                                    itemCheck=null;
                                }
                            }*/

                                gamdokList += "\n" + gamdokName[i] + "(" + "" + gamdokJsonArray.getJSONObject(i).getString("cstrCpprPrchTelno").replace("-", "") + ")";
                                ;
                                parameterMap.put("gamdokList_in_bigo", gamdokList);
                                //    Toast.makeText(WorkPlanResisterLoadContentActivity.this,"??????sdf???????????????.",Toast.LENGTH_SHORT).show();
                                itemCheck = gamdokTvResult.substring(1).split(",");


                                if (itemCheck.length >= 6) {
                                    Toast.makeText(WorkPlanResisterLoadContentActivity.this, "?????? ???????????? ???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                                    //     Toast.makeText(WorkPlanResisterLoadContentActivity.this,gamdokSawonNo,Toast.LENGTH_SHORT).show();
                                    Log.i("blah", gamdokSawonNo.substring(1));//gamdokTvResult
                                    Log.i("blah", gamdokSawonNo.substring(1));//gamdokTvResult
                                    for (int ii = 0; ii < gamdokItemBoo.length; ii++) {
                                        gamdokItemBoo[ii] = false;
                                        gamdokTvResult = "";
                                        gamdokTelno = "";
                                        gamdokSawonNo = "";
                                        //
                                    }
                                    gamdokwonTextView.clearComposingText();
                                    gamdokwonTextView.setText("");
                                    itemCheck = null;
                                    dialogInterface.dismiss();
                                }
                            }
                        } catch ( JSONException | NullPointerException e) {
                            Log.e("??????"," JSONException | NullPointerException ");
                        }


                    } else {
                        Log.i("??????", "??????");

                        try {
                            gamdokName[i] = null;
                            gamdokTvResult.replace("," + gamdokName[i], "");
                            //gamdokTvResult+=","+gamdokName[i];
                            Log.i("??????", gamdokTvResult);
                            gamdokTelno.replace("," + jsonArray.getJSONObject(i).getString("TEL_NO").replace("-", ""), "");

                            Log.i("??????", gamdokTelno);
                            gamdokSawonNo.replace(jsonArray.getJSONObject(i).getString("USER_EMNO"), "");//USER_EMNO
                            Log.i("??????", gamdokSawonNo);
                            gamdokList.replace("\n" + gamdokName[i] + "(" + "" + jsonArray.getJSONObject(i).getString("TEL_NO").replace("-", "") + ")", "");
                            ;
                            Log.i("??????", gamdokList);
                        } catch ( JSONException | NullPointerException  e) {
                            Log.e("??????"," JSONException | NullPointerException ");
                        }
                    }

                }
            }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gamdokwonTextView.clearComposingText();
                    gamdokwonTextView.setText(gamdokTvResult.substring(1));
                /*    codeMap.put("MTNOF_PRCH_EMNO_TELNO",gamdokTelno.substring(1));
                    codeMap.put("MTNOF_PRCH_EMNO",gamdokSawonNo.substring(1));*/
                    Log.i("blah", gamdokSawonNo.substring(1));//gamdokTvResult
                    Log.i("blah", gamdokSawonNo.substring(1));//gamdokTvResult
                    try {
                        parameterMap.put("MTNOF_PRCH_EMNO_TELNO", gamdokTelno.replace("-", "").substring(1));
                        parameterMap.put("MTNOF_PRCH_EMNO", gamdokSawonNo.substring(1));
                        Log.i("blah", parameterMap.get("MTNOF_PRCH_EMNO_TELNO").toString());
                        Log.i("blah", parameterMap.get("MTNOF_PRCH_EMNO").toString());
                        gamdokTvResult = "";
                        gamdokTelno = "";
                        gamdokSawonNo = "";
                    } catch (NullPointerException e) {
                        Log.e("??????","??????");
                    }
                    gamdokTvResult = "";
                    gamdokTelno = "";
                    gamdokSawonNo = "";
                }
            }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
        } catch (Exception e) {
            Log.e("??????","?????? ??????");
        }
    }


    public void setAdapterModule(Spinner spinner, List<Map<String, String>> listMap, List<String> list, String url, String param, List<String> keyArray, String listParam, final TextView tv, boolean gamdokSppinerFlag, String actionTime ) {

        try {
            /* if(spinner.getId()!=R.id.gamdokSpinner) {*/
            if (true) {
                //2021.07 ????????????(?????????)
                if("notFirstAciton".equals(actionTime)){
                    String result = nullCheck(URLDecoder.decode(new Action("get", url, param, this).execute().get(), "UTF-8"));
                    jsonArray = new JSONArray(result);
                    Log.i("??????", jsonArray.toString());
                    //2021.07 ????????????(?????????) TEST
                    for (int i = 1; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);
                        Log.i("??????ddd???", jsonObject.toString());
                        Log.println(Log.ASSERT, "??????ddd???", jsonObject.toString());
                        Map<String, String> map = new HashMap<String, String>();

                        //2021.07 ????????????(?????????) ?????? ????????? ??????????????????
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
                }else{

                String result = nullCheck(URLDecoder.decode(new Action("get", url, param, this).execute().get(), "UTF-8"));
                jsonArray = new JSONArray(result);
                Log.i("??????", jsonArray.toString());
                System.out.println("-------------------------------- setAdapterModule jsonArray.length() ----- + " + jsonArray.length());
                //2021.07 ????????????(?????????) TEST
                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);
                    Log.i("??????ddd???", jsonObject.toString());
                    Log.println(Log.ASSERT, "??????ddd???", jsonObject.toString());
                    Map<String, String> map = new HashMap<String, String>();

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
            }
            }else {
                String result = nullCheck(URLDecoder.decode(new Action("get", url, param, this).execute().get(), "UTF-8"));
                final JSONArray jsonArrayInLoadContent = new JSONArray(result);

                // if(!jsonArray.getJSONObject(0).get("EMNM").toString().equals("????????? ???????????? ????????????.")||!gamdokwonTextView.getText().equals("????????? ???????????? ????????????.")){
                if (jsonArrayInLoadContent.length() != 0) {
                    gamdokwonTextView.clearComposingText();
                    gamdokSpinner.performClick();
                    final String[] gamdokName = new String[jsonArrayInLoadContent.length()];
                    final boolean[] gamdokItemBoo = new boolean[jsonArray.length()];
                    Log.println(Log.ASSERT, "????????? ????????? ??????", jsonArrayInLoadContent.length() + "");
                    for (int i = 0; i < gamdokName.length; i++) {
                        gamdokItemBoo[i] = false;
                        gamdokName[i] = jsonArrayInLoadContent.getJSONObject(i).get("gamdokNameEMNM").toString();
                    }
                    if (gamdokSppinerFlag != false) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                        dialog.setMultiChoiceItems(gamdokName, gamdokItemBoo, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b == true) {

                                    //gamdokwonTextView.append(gamdokName[i]+",");
                                    gamdokTvResult += "," + gamdokName[i];

                                    try {
                                        gamdokTelno += "," + jsonArray.getJSONObject(i).getString("cstrCpprPrchTelno");


                                        gamdokSawonNo += "," + jsonArray.getJSONObject(i).getString("USER_EMNO");//USER_EMNO
                                    } catch (JSONException e) {
                                      //  e.printStackTrace();
                                        Log.e("??????","?????? ??????");
                                    } catch(Exception e){
                                        Log.e("??????","?????? ??????");
                                    }

                                }

                            }
                        }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (gamdokTvResult.length() != 0) {
                                    tv.clearComposingText();


                                    try {
                                        jsonResultObject.put("MTNOF_PRCH_EMNO_TELNO", gamdokTelno.substring(1));
                                        jsonResultObject.put("MTNOF_PRCH_EMNO", gamdokSawonNo.substring(1));
                                        Log.i("??????", jsonResultObject.get("MTNOF_PRCH_EMNO_TELNO").toString());
                                        Log.i("??????", jsonResultObject.get("MTNOF_PRCH_EMNO").toString());
                                        gamdokTvResult = "";
                                        gamdokTelno = "";
                                        gamdokSawonNo = "";
                                    } catch (JSONException  e) {
                                        Log.e("??????","?????? ??????");
                                    }catch (NullPointerException  e) {
                                        Log.e("??????","?????? ??????");
                                    }catch (Exception e) {
                                        Log.e("??????","?????? ??????");
                                    }
                                }


                            }
                        });

                        dialog.create();
                        dialog.show();
                    }

                } else {
                    if (gamdokSppinerFlag != false) {
                        Toast.makeText(this, "????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
                    }
                }
                gamdokSppinerFlag = false;
                this.gamdokSppinerFlag = gamdokSppinerFlag;
            }
        } catch (NullPointerException | JSONException | InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("??????","??????");
        }
    }//end setAdapter

    @Override
    public void onPause() {
        super.onPause();
        /*ET_gosundaeNum.onCh*/

    }

    public void setAdapterModule(Spinner spinner, List<Map<String, String>> listMap, List<String> list, String url, String param, List<String> keyArray, String listParam, final TextView tv, JSONObject job, List<String> jobParam, boolean gamdokSppinerFlag) {

        System.out.println("------------------------------------------- here00000 ################################## ");
        try {
            /*       if(spinner.getId()!=R.id.gamdokSpinner) {*/
            if (true) {
                //2021.07 ????????????(?????????) ?????????
                listMap.clear();
                list.clear();
                tv.clearComposingText();

                Log.println(Log.ASSERT, "????????????", "???????????? : " + param);
                String result = nullCheck(URLDecoder.decode(new Action("get", url, param, this).execute().get(), "UTF-8"));
                jsonArray = new JSONArray(result);
                Log.i("jddddsfd", jsonArray.toString());
                Log.i("??????", jsonArray.toString());
                /*       Log.println(Log.ASSERT,"????????????", "???????????? : "+ jsonArray.toString());*/


                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    Map<String, String> map = new HashMap<String, String>();
                    for (int k = 0; k < keyArray.size(); k++) {
                        map.put(keyArray.get(k), jsonObject.get(keyArray.get(k)).toString());
                        Log.d("???", keyArray.get(k));
                /*        Log.println(Log.ASSERT,"???", " : "+ keyArray.get(k));
                        Log.println(Log.ASSERT,"??????",jsonObject.get(keyArray.get(k)).toString())     ;*/
                    }
                    listMap.add(map);
                    list.add(listMap.get(i).get(listParam));

                }

                try {


                    for (int k = 0; k < jobParam.size(); k++) {

                        jsonResultObject.put(keyArray.get(k), listMap.get(0).get(keyArray.get(k)));

                    }
 /*catch (NullPointerException | JSONException | InterruptedException | ExecutionException | UnsupportedEncodingException e) {
                        Log.e("??????","NullPointerException | JSONException | InterruptedException | ExecutionException");
                    }*/
                } catch (NullPointerException | JSONException   e) {
                    tv.setText("");
                }

                spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list));

                if (list.size() != 0) {
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
                } else {
                    Log.println(Log.ASSERT, "?????????", "0????????? ????????????????");
                    tv.clearComposingText();
                    tv.setText("");
                    for (int k = 0; k < jobParam.size(); k++) {
                        jsonResultObject.put(keyArray.get(k), "\n");
                        /* jsonResultObject.put(keyArray.get(k),null);*/
                    }
                    list.clear();
                    listMap.clear();
                }
                Log.println(Log.ASSERT, "-=------------------", "-=----------------------------");
            } else {
                System.out.println("------------------------------------------- here22222 ################################## ");
                String result = nullCheck(URLDecoder.decode(new Action("get", url, param, this).execute().get(), "UTF-8"));
                final JSONArray jsonArrayInLoadContent = new JSONArray(result);

                //if(!jsonArray.getJSONObject(0).get("EMNM").toString().equals("????????? ???????????? ????????????.")||!gamdokwonTextView.getText().equals("????????? ???????????? ????????????.")){
                if (jsonArrayInLoadContent.length() != 0) {
                    gamdokwonTextView.clearComposingText();
                    //    gamdokSpinner.performClick();
                    final String[] gamdokName = new String[jsonArrayInLoadContent.length()];
                    final boolean[] gamdokItemBoo = new boolean[jsonArray.length()];
                    Log.println(Log.ASSERT, "????????? ????????? ??????", jsonArrayInLoadContent.length() + "");
                    for (int i = 0; i < gamdokName.length; i++) {
                        gamdokItemBoo[i] = false;
                        gamdokName[i] = jsonArrayInLoadContent.getJSONObject(i).get("gamdokNameEMNM").toString();
                    }
                    //gamdokSppinerFlag
                    if (gamdokSppinerFlag == true) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                        dialog.setMultiChoiceItems(gamdokName, gamdokItemBoo, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b == true) {

                                    //gamdokwonTextView.append(gamdokName[i]+",");
                                    gamdokTvResult += "," + gamdokName[i];

                                    try {
                                        gamdokTelno += "," + jsonArray.getJSONObject(i).getString("cstrCpprPrchTelno");
                                        gamdokSawonNo += "," + jsonArray.getJSONObject(i).getString("USER_EMNO");//USER_EMNO
                                    } catch (NullPointerException | JSONException e) {
                                        Log.e("??????","??????");
                                    }
                                }

                            }
                        }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tv.clearComposingText();
                                try {
                                    tv.setText(gamdokTvResult.substring(1));
                                } catch (NullPointerException e) {
                                    tv.setText("");
                                }
                                try {
                                    jsonResultObject.put("MTNOF_PRCH_EMNO_TELNO", gamdokTelno.substring(1));
                                    jsonResultObject.put("MTNOF_PRCH_EMNO", gamdokSawonNo.substring(1));
                                    gamdokTvResult = "";
                                    gamdokTelno = "";
                                    gamdokSawonNo = "";
                                } catch (NullPointerException | JSONException e) {
                                    //e.printStackTrace();
                                    Log.e("??????","??????");
                                }

                            }
                        });

                        dialog.create();
                        dialog.show();
                    }

                } else {
                    //Toast.makeText(this,"????????? ???????????? ????????????.",Toast.LENGTH_LONG).show();
                    if (gamdokSppinerFlag != false) {
                        Toast.makeText(this, "????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (NullPointerException | JSONException | InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            Log.e("??????","??????");
        }
        try {///TodayWorkPlan/settingInjung.do
            //if(url.equals(SERVER_URL + "/TodayWorkPlan/settingNosun.do")){
            if (url.equals(SERVER_URL + "/TodayWorkPlan/settingInjung.do")) {
                String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL + "/TodayWorkPlan/getGugan.do", param, this).execute().get()), "UTF-8");
                Log.i("?????? ??????", param.toString());
                jsonObject = new JSONObject(param);
                Log.e("?????? ??????", guganMinMaxValue);
                JSONObject initGugan = new JSONObject(todayParam);
                JSONObject JsonGuganResult = new JSONObject(guganMinMaxValue);
                if (initGugan.get("mtnofCd") != jsonObject.get("mtnofCd") && initGugan.get("routeCd") != jsonObject.get("routeCd")) {
                    Log.println(Log.ASSERT, "if", "???????????? : " + keyArray.toString());

                    maxGugan = Double.parseDouble(JsonGuganResult.get("maxMtnofEdpntDstnc").toString());
                    minGugan = Double.parseDouble(JsonGuganResult.get("minMtnofStpntDstnc").toString());
                    ET_startGongsaGugan.setText(minGugan + "");
                    ET_endGongsaGugan.setText(maxGugan + "");
                } else {
                    Log.println(Log.ASSERT, "else", "???????????? : " + keyArray.toString());

                    maxGugan = Double.parseDouble(JsonGuganResult.get("maxMtnofEdpntDstnc").toString());
                    minGugan = Double.parseDouble(JsonGuganResult.get("minMtnofStpntDstnc").toString());
                      /*  ET_startGongsaGugan.setText(initGugan+"");
                        ET_endGongsaGugan.setText(initGugan+"");*/
                    ET_startGongsaGugan.setText(initGugan.get("strtBlcPntVal").toString());
                    ET_endGongsaGugan.setText(initGugan.get("endBlcPntVal").toString());
                }
            }

        } catch (JSONException | NullPointerException | InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            Log.e("??????","??????");
        }
    }//end setAdapter

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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
            case R.id.workTypeCATTextView:       //2021.07 ????????????(?????????)
                workTypeCATSpinner.performClick();
                workTypeCATSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.workTypeTextView:
                workTypeSpinner.performClick();
                workTypeSpinner.setOnItemSelectedListener(this);
                break;
            case R.id.gamdokwonTextView:
                gamdokSppinerFlag = true;
                showGamdokDialog(gamdokdialog, gamdokName, gamdokItemBoo);
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
            //2021.06 ???????????? TEST (?????????)
            case R.id.li_registerBtn://checkingValue

                //new CustomDialog(WorkPlanResisterLoadContentActivity.this, R.layout.z_work_plan_noti, R.id.correctBtn);

                try {
                    if (inwonJangbiTextView.getText().equals("?????? 0???, ????????? 0???, ????????? 0???")) {
                        Toast.makeText(this, "?????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }else if (gosundaeNullCheck() == false) {

                    } else if (directionTextView.getText().equals("\n")) {

                        Toast.makeText(this, "????????? ???????????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                    } else if (calCompareCheck(startDateTextView.getText().toString(), startHourTextView.getText().toString(), startMinuteTextView.getText().toString(), endDateTextView.getText().toString(), endHourTextView.getText().toString(), endMinuteTextView.getText().toString()) == false) {
                        Toast.makeText(getApplicationContext(), "??????????????? ?????????????????? ?????? ????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                    } else if ((Boolean) checkingValue(minGugan, maxGugan, Double.parseDouble(ET_startGongsaGugan.getText().toString()), Double.parseDouble(ET_endGongsaGugan.getText().toString())) == false) {
                        Log.i("?????????", minGugan + "," + minGugan);
                        Toast.makeText(this, "???????????????" + minGugan + " ???????????? " + maxGugan + "??? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
                    } else {
                        jsonObject.put("cmmdNm", inwonJangbiResultTextView.getText().toString());//?????? ????????????
                        jsonObject.put("humanCnt", " ??????????????????:" + inwonJangbiResultTextView.getText().toString());
                        jsonResultObject.put("cnstnCtnt", ET_gongsaContent.getText().toString());
                        jsonResultObject.put("blcStrtDttm", startDateTextView.getText().toString());//?????? ????????????
                        jsonResultObject.put("blcRevocDttm", endDateTextView.getText().toString());//????????????
                        jsonResultObject.put("workType", workTypeTextView.getText().toString());    //????????????(?????????)
                        System.out.println("------------------------------------ btn ------------- workType :" + workTypeTextView.getText().toString());

                        jsonResultObject.put("startGongsaHour", startHourTextView.getText().toString());
                        jsonResultObject.put("startGongsaMinute", startMinuteTextView.getText().toString());
                        jsonResultObject.put("endGongsaHour", endHourTextView.getText().toString());//
                        jsonResultObject.put("endGongsaMinute", endMinuteTextView.getText().toString());//?????? ????????????
                        jsonResultObject.put("cmmdNm", inwonJangbiResultTextView.getText().toString());//?????? ??????
                        jsonResultObject.put("strtBlcPntVal", ET_startGongsaGugan.getText().toString());
                        jsonResultObject.put("endBlcPntVal", ET_endGongsaGugan.getText().toString());
                        //   Log.println(Log.ASSERT,"?????? ?????? ??????", "?????? ?????? ?????? : "+ inwonJangbiResultTextView.getText().toString());
                        Log.println(Log.ASSERT, "?????? ?????? ??????", "?????? ?????? ?????? : " + jsonResultObject.toString());
                        //???????????? ??????


                        Log.i("ddddddddd", jsonResultObject.getString("gamdokList_in_bigo"));
                        // parameterMap.put("gamdokList_in_bigo", jsonResultObject.getString("gamdokList_in_bigo"));
                        parameterMap.put("ROUTE_DRCT_CD", jsonResultObject.get("drctClssCd").toString());
                        parameterMap.put("roadLimitResult", jsonResultObject.get("trfcLimtCrgwClssCd").toString());
                        parameterMap.put("HDQR_NM", jsonResultObject.get("hdqrNm").toString());
                        parameterMap.put("startWorkTime", startDateTextView.getText().toString().replaceAll("/", "") + dateParsing(startHourTextView.getText().toString()) + dateParsing(startMinuteTextView.getText().toString()));
                        parameterMap.put("endWorkTime", endDateTextView.getText().toString().replace("/", "") + dateParsing(endHourTextView.getText().toString()) + dateParsing(endMinuteTextView.getText().toString()));
                      /*  try{
                            parameterMap.put("MTNOF_PRCH_EMNO",jsonResultObject.get("mtnofPrchEmno").toString());
                        }catch (Exception e){
                            parameterMap.put("MTNOF_PRCH_EMNO","\n");
                        }*/

                        parameterMap.put("MVBL_BLC_YN", "Y");
                        parameterMap.put("LIMT_PLAN_DATES", getCurDate());
                        parameterMap.put("FSTTM_RGSR_ID", jsonResultObject.get("fsttmRgsrId").toString());
                        parameterMap.put("LSTTM_MODFR_ID", jsonResultObject.get("lsttmModftId").toString());
                        parameterMap.put("MTNOF_NM", jsonResultObject.get("mtnofNm").toString());
                        parameterMap.put("gongsaContent", ET_gongsaContent.getText().toString());
                        //parameterMap.put("GOSUNDAE_NO",gosundaTextParsing(que_et_list));//gosundaTextParsing(que_et_list)
                        parameterMap.put("cmmnCdNm", jsonResultObject.get("workType").toString());
                        System.out.println("------------------------------------ btn ----- cmmnCdNm workTypeNm :" + jsonResultObject.get("workType").toString());
                        parameterMap.put("HDQR_CD", jsonResultObject.get("hdqrCd").toString());
                        parameterMap.put("TOT_CRGW_CNT", jsonResultObject.get("totCrgwCnt").toString());
                        parameterMap.put("ROUTE_CD", jsonResultObject.get("routeCd").toString());
                        parameterMap.put("minMtnofStpntDstnc", jsonResultObject.get("strtBlcPntVal").toString());
                        parameterMap.put("maxMtnofEdpntDstnc", jsonResultObject.get("endBlcPntVal").toString());
                        parameterMap.put("MTNOF_CD", jsonResultObject.get("mtnofCd").toString());
                        parameterMap.put("humanCnt", "");

                        if (!roadlimitTextView.getText().toString().equals("?????? ?????? ??????")) {
                            parameterMap.put("gongsaContent", roadlimitTextView.getText().toString());
                        } else {
                            parameterMap.put("gongsaContent", "?????? ?????? ??????");
                        }

                        parameterMap.put("HDQR_NM", jsonResultObject.get("hdqrNm").toString());
                        parameterMap.put("CNSTN_CTNT", ET_gongsaContent.getText().toString());
                        parameterMap.put("TOT_CRGW_CNT", jsonResultObject.get("totCrgwCnt").toString());
                        parameterMap.put("CSTR_CRPR_RCRD_CTNT", jobUserInfo.get("SMS_GRP_NM").toString());
                        parameterMap.put("CSTR_CRPR_PRCH_TELNO", jobUserInfo.get("TEL_NO").toString());
                        parameterMap.put("inwonJangbi", inwonJangbiResultTextView.getText().toString());
                        parameterMap.put("GOSUNDAE_NO", gosundaeNoParsing(que_et_list));
                        parameterMap.put("nVersion", "true");
                        parameterMap.put("ROUTE_NM", noSunTextView.getText().toString());
                        parameterMap.put("NOSUN_DIRECTION", directionTextView.getText().toString());


                        //Log.i("workType1111111111",workTypeSpinner.getOnItemSelectedListener().toString());
                        //jsonResultObject.put(workTypeSpinner.getSelectedItem(workTypeCd).toString());
                        //2021.07 ????????????(?????????) ( ???????????? : onCreate / ??????????????? : workTypespinner ?????? ????????? ??????????????????)
                        //jsonResultObject.put("workTypeCd", workTypeMapList.get(0).get("workTypeCd").toString());
                        //System.out.println("------------------------------------ btn ------------- workType :" + workTypeMapList.);

                        parameterMap.put("cmmnCd", jsonResultObject.get("workTypeCd").toString());
                        Log.i("blah", Common.nullCheck(parameterMap.get("MTNOF_PRCH_EMNO_TELNO").toString()));
                        Log.i("blah", Common.nullCheck(parameterMap.get("MTNOF_PRCH_EMNO").toString()));

                        Log.i("?????????", parameterMap.toString());
                        intent.putExtra("todayWorkPlanRegister", jsonResultObject.toString());
                        JSONObject jsonNameValue = new JSONObject();
                        jsonNameValue.put("bonbu", bonbuTextView.getText().toString());//?????? ?????? ?????? ??????????????? ??????
                        jsonNameValue.put("jisa", jisaTextView.getText().toString());
                        jsonNameValue.put("nosun", noSunTextView.getText().toString());
                        jsonNameValue.put("direction", directionTextView.getText().toString());
                        jsonNameValue.put("gamdokTv", gamdokwonTextView.getText().toString());
                        jsonNameValue.put("gongsaContent", ET_gongsaContent.getText().toString());
                        jsonNameValue.put("gosundaeNum", gosundaTextParsing(que_et_list));
                        //jsonNameValue.put("gosundaeNum", gosunDaeNoParsing(ET_gosundaeNum.getText().toString()));
                        jsonNameValue.put("roadlimit", roadlimitTextView.getText().toString());
                        jsonNameValue.put("startTime", startDateTextView.getText().toString() + " " + startHourTextView.getText().toString() + ":" + startMinuteTextView.getText().toString());
                        jsonNameValue.put("endTime", endDateTextView.getText().toString() + " " + endHourTextView.getText().toString() + ":" + endMinuteTextView.getText().toString());
                        jsonNameValue.put("startGugan", ET_startGongsaGugan.getText().toString());
                        jsonNameValue.put("endGugan", ET_endGongsaGugan.getText().toString());
                        jsonNameValue.put("inwonjangbi", inwonJangbiResultTextView.getText().toString());

                        jsonNameValue.put("workType", workTypeTextView.getText().toString());

                        Log.i("workPlanLoad_param : ",new JSONObject(parameterMap).toString());

                        //2021.06 ??????????????????
                        //new CustomDialog(WorkPlanResisterLoadContentActivity.this, R.layout.z_custom_dialog, R.id.sendApprovalRequest, jsonNameValue, new JSONObject(parameterMap), intent);
                        new CustomDialog(WorkPlanResisterLoadContentActivity.this, R.layout.z_work_plan_noti, R.id.correctBtn, jsonNameValue, new JSONObject(parameterMap), intent);

                        //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // startActivity(intent);

                    }

                } catch (JSONException | NullPointerException | ParseException e) {
                    Log.e("??????","JSONException | NullPointerException | ParseException ");
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
            case 100:
                Toast.makeText(this, "??????????????????", Toast.LENGTH_LONG).show();
                break;
            case R.id.plus_btn:
                //  int index =
                if (que_et_list.size() == 1 && (que_et_list.get(0).getText().toString().length() == 0 || que_et_list.get(0).getText().toString().equals(""))) {
                    Toast.makeText(this, "1?????? ?????????????????? ??????????????????", Toast.LENGTH_LONG).show();
                } else if (/*que_et_list.size()!=0 &&*/ (que_et_list.get(que_et_list.size() - 1).getText().toString().equals("") || que_et_list.get(que_et_list.size() - 1).getText().toString().length() == 0)) {
                    //                  Log.i("??????","?????????:"+que_et_list.size());
//                    Log.i("??????",(que_et_list.size()-1)+"??????, ??????:"+que_et_list.get(que_et_list.size()-1).getText().toString());
                    //int cnt=que_et_list.size()+1;
                    int cnt = que_et_list.size();
                    Toast.makeText(this, cnt + "?????? ?????????????????? ??????????????????", Toast.LENGTH_LONG).show();
                } else if (que_et_list.size() == 5) {
                    Toast.makeText(this, "?????????????????? ?????? 5?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                } else {
                    addView(null);
                }
                break;

        }
    }

    public String gosundaeNoParsing(List<EditText> que_et_list) {
        String result = "";
        for (int i = 0; i < que_et_list.size(); i++) {
            result += "," + gosunDaeNoParsing(que_et_list.get(i).getText().toString());
        }
        return result.substring(1);
    }

    /* public void addView(){
         Log.i("addView","View??? ?????????");

         //View ??????
         View view = new View(this);
         final int view_height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());


         LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,view_height,0);
         view.setLayoutParams(viewParams);
         gosundae_list.addView(view);

         //Linear Layout ??????
         final int li_height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
         LinearLayout li = new LinearLayout(this);

         li.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
         LinearLayout.LayoutParams linear_View = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,li_height,0);
         //li.setBackground(R.drawable.insert_line_color);
         //li.setBackground(R.drawable.insert_line_color);
       //  li.setBackgroundResource(R.drawable.insert_line_color);
         li.setLayoutParams(linear_View);
         li.setWeightSum(322);
         li.setOrientation(LinearLayout.HORIZONTAL);



         final int set_et_padding_left = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
         LinearLayout.LayoutParams et_param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,280);
         EditText et = new EditText(this);
         et.setLayoutParams(et_param);
         et.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
         et.setBackgroundResource(R.drawable.insert_line_color);
         et.setPadding(set_et_padding_left,0,0,0);
         et.setTextSize(15);
         et.setTextColor(-4276546);
         et.setInputType(InputType.TYPE_CLASS_NUMBER);
         et.setMaxLines(1);
         et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
         que_et_list.add(et);
         li.addView(et);

         ImageView iv = new ImageView(this);
         final int set_iv_padding_left = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
         LinearLayout.LayoutParams iv_param = new LinearLayout.LayoutParams(set_iv_padding_left,15,42);
         iv.setLayoutParams(iv_param);
         iv.setScaleType(ImageView.ScaleType.FIT_XY);
         //iv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
         //iv.s(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
         iv.setBackgroundResource(R.drawable.minus_btn);
        // iv.setBackground(this.getDrawable(R.drawable.minus_btn));
         iv.setOnClickListener(this);
         iv.setId(new Integer(100));

         li.addView(iv);
         Log.i("??????????????????",li.getChildCount()+"");
         Log.i("?????????????????? LIst",gosundae_list.getChildCount()+"");
         gosundae_list.addView(li);
         Log.i("??????????????????",li.getChildCount()+"");
         Log.i("?????????????????? LIst",gosundae_list.getChildCount()+"");
         Log.i("?????????????????? LIst","----------------------------");
     }
 */
    public void addView(@Nullable String gosundae_No) {
        Log.i("addView", "View??? ?????????");
        LinearLayout li_out = new LinearLayout(this);
        final int view_height_out = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams viewParams_out_Li = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        li_out.setLayoutParams(viewParams_out_Li);
        li_out.setOrientation(LinearLayout.VERTICAL);
        // li_out.setTag("pa_"+gosundae_list.getChildCount());
        //View ??????
        View view = new View(this);
        final int view_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());


        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, view_height, 0);
        view.setLayoutParams(viewParams);
        //gosundae_list.addView(view);
        //if(gosundae_list.getChildCount()!=0){
        Log.i("cntcnt", li_out.getChildCount() + "");
        li_out.addView(view);
        //}


        //Linear Layout ??????
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
        LinearLayout.LayoutParams et_param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 85);
        EditText et = new EditText(this);
        et.setLayoutParams(et_param);
        et.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        et.setBackgroundResource(R.drawable.insert_line_color);
        et.setPadding(set_et_padding_left, 0, 0, 0);
        et.setTextSize(15);
        et.setTextColor(-4276546);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setMaxLines(1);
        // que_et_list.add(et);
        if (gosundae_No != null) {
            et.setText(gosundae_No);

        }
        li.addView(et);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        que_et_list.add(et);

        View inner_view = new View(this);
        LinearLayout.LayoutParams inner_viewParams = new LinearLayout.LayoutParams(0, view_height, 5);
        inner_view.setLayoutParams(inner_viewParams);
        li.addView(inner_view);


        ImageView iv = new ImageView(this);
        final int set_iv_padding_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams iv_param = new LinearLayout.LayoutParams(set_iv_padding_left, ViewGroup.LayoutParams.WRAP_CONTENT, 10);
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
                ViewGroup v = (ViewGroup) view.getParent();
                LinearLayout li = (LinearLayout) v.getParent();
                LinearLayout li_pa = (LinearLayout) li.getParent();
                Log.i("index", "???????????? ?????????:" + li_pa.indexOfChild(li) + "");

                /*if(que_et_list.size()>0){

                }*/

                Log.i("index", "??????????????????::" + que_et_list.size());
                //li_pa.removeView(li);
                //li_pa.removeView(li);
                if (li_pa.getChildCount() == 1) {
                    /*Toast.makeText(WorkPlanResisterLoadContentActivity.this,"?????????????????? ?????? 1??? ????????? ?????? ????????? ?????????.",Toast.LENGTH_SHORT).show();
                    ((EditText)v.getChildAt(0)).setText("");
                    ((EditText)que_et_list.get(0)).setText("");
                    */
                    if (((EditText) v.getChildAt(0)).getText().toString().length() == 0 || ((EditText) v.getChildAt(0)).getText().toString().equals("")) {
                        Toast.makeText(WorkPlanResisterLoadContentActivity.this, "?????????????????? ?????? 1?????? ??????????????? ?????????.", Toast.LENGTH_SHORT).show();
                    } else {
                        new CustomDialog(WorkPlanResisterLoadContentActivity.this, R.layout.zz_warn_gosundae, R.id.warn_msg, "???????????? ????????? ?????????\n" + gosunDaeNoParsing(((EditText) v.getChildAt(0)).getText().toString()) + "\n?????????.", que_et_list, li_pa.indexOfChild(li), li_pa);
                    }


                } else {
             /*       que_et_list.remove(li_pa.indexOfChild(li));
                    Toast.makeText(WorkPlanResisterLoadContentActivity.this,"???????????? ????????? "+((EditText)v.getChildAt(0)).getText().toString()+" ?????????.",Toast.LENGTH_SHORT).show();
                    li_pa.removeViewAt(li_pa.indexOfChild(li));
*/
                    new CustomDialog(WorkPlanResisterLoadContentActivity.this, R.layout.zz_warn_gosundae, R.id.warn_msg, "???????????? ????????? ?????????\n" + gosunDaeNoParsing(((EditText) v.getChildAt(0)).getText().toString()) + "\n?????????.", que_et_list, li_pa.indexOfChild(li), li_pa);
                }


                for (int i = 0; i < que_et_list.size(); i++) {
                    Log.i("index", "???????????? ????????????::" + que_et_list.get(i).getText().toString());
                }
                Log.i("index", "------------------------------------");
            }
        });
        li_out.addView(li);
        li.addView(iv);

        gosundae_list.addView(li_out);
    }


    public boolean gosundaeNullCheck() {
        Log.i("cccc", "" + "????????? ??????");
        Log.i("cccc", "?????????::" + que_et_list.size());
        for (int i = 0; i < que_et_list.size(); i++) {
            if (que_et_list.get(i).getText().toString().length() == 0 || que_et_list.get(i).getText().toString().equals("")) {
                switch (i) {
                    case 0:
                        Toast.makeText(this, "????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 1:
                        Toast.makeText(this, "????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 2:
                        Toast.makeText(this, "????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 3:
                        Toast.makeText(this, "????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 4:
                        Toast.makeText(this, "???????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return false;
                }
            } else if (i == que_et_list.size() - 1) {
                return true;
            }

        }
        return false;
    }

    public void delList(String value, int index) {

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


    public String dateParsing(String parsing) {
        if (parsing.length() == 1)
            parsing = "0" + parsing;
        return parsing;
    }

    public String gosundaTextParsing(List<EditText> list) {
        //String result = gosunDaeNoParsing(ET_gosundaeNum.getText().toString());
        String result = "";
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getText().equals("") && list.get(i).getText().toString().length() != 0) {
                    result += ",\n" + gosunDaeNoParsing(list.get(i).getText().toString());
                }
            }
        }
        return result.substring(1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.bonbuSpinner:


                try {

                    selectItemList(parent, position, bonbuTextView, R.id.bonbuSpinner);
           /*     jsonResultObject.put("hdqrCd",nJob.get("hdqrCd").toString());
                jsonResultObject.put("hdqrNm", nJob.get("hdqrNm").toString());*/
                    ArrayList<String> jisaParam = new ArrayList<String>();
                    jisaParam.add("mtnofCd");
                    jisaParam.add("mtnofNm");
                    setAdapterModule(jisaSpinner, jisaMapList, jisaList, SERVER_URL + "/TodayWorkPlan/settingJisa.do", jsonResultObject.toString(), jisaParam, "mtnofNm", jisaTextView, jsonObject, jisaParam, false);
                    ////

                    ;
                    ;
               /* jsonResultObject.put("mtnofCd",nJob.get("mtnofCd").toString());
                jsonResultObject.put("mtnofNm",nJob.get("mtnofNm").toString());*/
                    ArrayList<String> nosunParam = new ArrayList<String>();
                    nosunParam.add("routeNm");
                    nosunParam.add("routeCd");

                    setAdapterModule(nosunSpinner, nosunMapList, nusunList, SERVER_URL + "/TodayWorkPlan/settingNosun.do", jsonResultObject.toString(), nosunParam, "routeNm", noSunTextView, jsonObject, nosunParam, false);

                    ArrayList<String> ijungParam = new ArrayList<String>();
                    ijungParam.add("drctClssNm");
                    ijungParam.add("drctClssCd");
                    setAdapterModule(direnctionSpinner, iJungMapList, ijungList, SERVER_URL + "/TodayWorkPlan/settingInjung.do", jsonResultObject.toString(), ijungParam, "drctClssNm", directionTextView, jsonObject, ijungParam, false);
               /* jsonResultObject.put("cstrCpprPrchTelno",nJob.get("cstrCpprPrchTelno").toString());
                jsonResultObject.put("gamdokNameEMNM",nJob.get("gamdokNameEMNM").toString());
                jsonResultObject.put("mtnofPrchEmno",nJob.get("mtnofPrchEmno").toString());*/
                    ArrayList<String> gamdokParam = new ArrayList<String>();
                    gamdokParam.add("cstrCpprPrchTelno");
                    gamdokParam.add("gamdokNameEMNM");
                    gamdokParam.add("mtnofPrchEmno");//gamokAlertDialog
                    //  Toast.makeText(this,"1:"+new Boolean(gamdokSppinerFlag).toString(),Toast.LENGTH_LONG).show();
                    //  setAdapterModule(gamdokSpinner, gamdokMapList, gamdokList, SERVER_URL + "/TodayWorkPlan/getGamdok.do", jsonResultObject.toString(), gamdokParam, "gamdokNameEMNM", gamdokwonTextView, jsonObject, gamdokParam,gamdokSppinerFlag);
                    //gamokAlertDialog(gamdokSpinner, gamdokMapList, gamdokList, SERVER_URL + "/TodayWorkPlan/getGamdok.do", jsonResultObject.toString(), gamdokParam, "gamdokNameEMNM", gamdokwonTextView, jsonObject, gamdokParam);
         /*       jisaAdapter = new ArrayAdapter<String>(this,R.layout.z_spinner_item,jisaList);
                jisaSpinner.setAdapter(jisaAdapter);*/
                    // gamokAlertInitDialog( jsonResultObject.toString(),gamdokSpinner,SERVER_URL + "/TodayWorkPlan/getGamdok.do");
                    gamokAlertDialog(jsonResultObject.toString(), gamdokSpinner, SERVER_URL + "/TodayWorkPlan/getGamdok.do", gamdokwonTextView, jsonResultObject.getString("gamdokNameEMNM"));
                } catch (NullPointerException |JSONException | StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
                    Log.e("??????","??????");
                }
                break;
            case R.id.jisaSpinner:
                selectItemList(parent, position, jisaTextView, R.id.jisaSpinner);
                ArrayList<String> nosunParam = new ArrayList<String>();
                nosunParam.add("routeNm");
                nosunParam.add("routeCd");
                setAdapterModule(nosunSpinner, nosunMapList, nusunList, SERVER_URL + "/TodayWorkPlan/settingNosun.do", jsonResultObject.toString(), nosunParam, "routeNm", noSunTextView, jsonObject, nosunParam, false);

                ArrayList<String> ijungParam = new ArrayList<String>();
                ijungParam.add("drctClssNm");
                ijungParam.add("drctClssCd");
                setAdapterModule(direnctionSpinner, iJungMapList, ijungList, SERVER_URL + "/TodayWorkPlan/settingInjung.do", jsonResultObject.toString(), ijungParam, "drctClssNm", directionTextView, jsonObject, ijungParam, false);

                ArrayList<String> gamdokParam = new ArrayList<String>();
                gamdokParam.add("cstrCpprPrchTelno");
                gamdokParam.add("gamdokNameEMNM");
                gamdokParam.add("mtnofPrchEmno");
                try {
                    gamokAlertDialog(jsonResultObject.toString(), gamdokSpinner, SERVER_URL + "/TodayWorkPlan/getGamdok.do", gamdokwonTextView, jsonResultObject.getString("gamdokNameEMNM"));
                } catch (JSONException e) {
                    Log.e("??????","JSONException ");
                }

                //  Toast.makeText(this,"2:"+new Boolean(gamdokSppinerFlag).toString(),Toast.LENGTH_LONG).show();
                // gamokAlertInitDialog( todayParam,gamdokSpinner,SERVER_URL + "/TodayWorkPlan/getGamdok.do");
                // setAdapterModule(gamdokSpinner,gamdokMapList,gamdokList,SERVER_URL + "/TodayWorkPlan/getGamdok.do",jsonResultObject.toString(),gamdokParam,"gamdokNameEMNM",gamdokwonTextView,jsonObject,gamdokParam, gamdokSppinerFlag);
                //gamokAlertInitDialog( jsonResultObject.toString(),gamdokSpinner,SERVER_URL + "/TodayWorkPlan/getGamdok.do");
                break;
            case R.id.nosunSpinner:
                selectItemList(parent, position, noSunTextView, R.id.nosunSpinner);

                nosunParam = new ArrayList<String>();
                nosunParam.add("routeNm");
                nosunParam.add("routeCd");
                setAdapterModule(nosunSpinner, nosunMapList, nusunList, SERVER_URL + "/TodayWorkPlan/settingNosun.do", jsonResultObject.toString(), nosunParam, "routeNm", noSunTextView, jsonObject, nosunParam, false);

                ijungParam = new ArrayList<String>();
                ijungParam.add("drctClssNm");
                ijungParam.add("drctClssCd");
                setAdapterModule(direnctionSpinner, iJungMapList, ijungList, SERVER_URL + "/TodayWorkPlan/settingInjung.do", jsonResultObject.toString(), ijungParam, "drctClssNm", directionTextView, jsonObject, ijungParam, false);
                /*    *//*    gamdokParam = new ArrayList<String>();
                gamdokParam.add("cstrCpprPrchTelno");
                gamdokParam.add("gamdokNameEMNM");
                gamdokParam.add("mtnofPrchEmno");
                setAdapterModule(gamdokSpinner,gamdokMapList,gamdokList,SERVER_URL + "/TodayWorkPlan/getGamdok.do",jsonResultObject.toString(),gamdokParam,"gamdokNameEMNM",gamdokwonTextView,jsonObject,gamdokParam);*//*
                //*/
                break;
            case R.id.direnctionSpinner:
                selectItemList(parent, position, directionTextView, R.id.direnctionSpinner);
                break;
            case R.id.gamdokSpinner:
                // parent.setSelection(position);
                //    Toast.makeText(this,"3:"+new Boolean(gamdokSppinerFlag).toString(),Toast.LENGTH_LONG).show();
                //  selectItemList( parent,   position,  gamdokwonTextView, R.id.gamdokSpinner );
                break;
            case R.id.workTypeCATSpinner:       //2021.07 ????????????(?????????)
                System.out.println("--------------------------------------------- ?????? ");
                selectItemList(parent, position, workTypeCATTextView, R.id.workTypeCATSpinner);
                break;
            case R.id.workTypeSpinner:
                //parent.setSelection(position);
                /*    workTypeTextView.clearComposingText();*/
                //workTypeTextView.setText(parent.getSelectedItem().toString());
                System.out.println("--------------------------------------------- ??? ?????? ");
                //202009_????????????????????????
                selectItemList(parent, position, workTypeTextView, R.id.workTypeSpinner);
                Log.i("workTypeSpinner 1844", workTypeMapList.toString());
                ArrayList<String> workTypeParam = new ArrayList<String>();
                workTypeParam.add("workType");
                workTypeParam.add("workTypeCd");
                //2021.07 TEST
                //setAdapterModule(workTypeSpinner, workTypeMapList, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkType.do", jsonResultObject.toString(), workTypeParam, "workType", workTypeTextView, jsonObject, workTypeParam, false);

                break;

            case R.id.inwonJangbiSpinner:
                if (parent.getSelectedItem().equals("?????????")) {
                    inwonEditText();
                } else if (parent.getSelectedItem().equals("?????????")) {
                    signCarSetting();
                } else if (parent.getSelectedItem().equals("?????????")) {
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

    public void selectItemList(AdapterView<?> parent, int position, TextView tv, int SpinnerId) {
        //  JSONObject job = new JSONObject();
        switch (SpinnerId) {
            case R.id.bonbuSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try {
                    jsonResultObject.put("hdqrCd", bonbuMapList.get(position).get("hdqrCd").toString());
                    jsonResultObject.put("hdqrNm", bonbuMapList.get(position).get("hdqrNm").toString());
                } catch (JSONException | ArrayIndexOutOfBoundsException e) {
                    Log.e("??????","??????");
                }

                break;
            case R.id.jisaSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try {
                    jsonResultObject.put("mtnofNm", jisaMapList.get(position).get("mtnofNm").toString());
                    jsonResultObject.put("mtnofCd", jisaMapList.get(position).get("mtnofCd").toString());
                } catch (JSONException | ArrayIndexOutOfBoundsException e) {
                    Log.e("??????","??????");
                }

                break;
            case R.id.nosunSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try {
                    jsonResultObject.put("routeNm", nosunMapList.get(position).get("routeNm").toString());
                    jsonResultObject.put("routeCd", nosunMapList.get(position).get("routeCd").toString());
                } catch (JSONException | ArrayIndexOutOfBoundsException e) {
                    Log.e("??????","??????");
                }
                break;
            case R.id.direnctionSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try {
                    jsonResultObject.put("drctClssNm", iJungMapList.get(position).get("drctClssNm").toString());
                    jsonResultObject.put("drctClssCd", iJungMapList.get(position).get("drctClssCd").toString());
                } catch(JSONException | ArrayIndexOutOfBoundsException e) {
                    Log.e("??????","??????");
                }
                break;
            case R.id.gamdokSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                try {
                    jsonResultObject.put("cstrCpprPrchTelno", gamdokMapList.get(position).get("cstrCpprPrchTelno").toString());
                    jsonResultObject.put("gamdokNameEMNM", gamdokMapList.get(position).get("gamdokNameEMNM").toString());
                    jsonResultObject.put("mtnofPrchEmno", gamdokMapList.get(position).get("mtnofPrchEmno").toString());
                } catch (JSONException | ArrayIndexOutOfBoundsException e) {
                    Log.e("??????","??????");
                }
                break;
            case R.id.workTypeCATSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                final String[] workTypeCategorizationCode = getResources().getStringArray(R.array.workTypeCategorizationCode);      //2021.07 ????????????(?????????) ?????????/ ????????? ????????????
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
                setAdapterModule(workTypeSpinner,workTypeMapList, workTypeList, SERVER_URL + "/TodayWorkPlan/settingWorkTypeCode.do",obj, workTypeParam1, "workType", workTypeTextView, false, "notFirstAciton");
                break;
            //202009_????????????
            case R.id.workTypeSpinner:
                parent.setSelection(position);
                tv.clearComposingText();
                tv.setText(parent.getSelectedItem().toString());
                Log.i("workType1937",workTypeMapList.toString());
                try {
                    //2021.07 ????????????_ ????????? ???????????? ???????????? ???????????? ???
                    jsonResultObject.put("workType", workTypeMapList.get(position).get("workType"));
                    jsonResultObject.put("workTypeCd", workTypeMapList.get(position).get("workTypeCd"));
                } catch (JSONException | ArrayIndexOutOfBoundsException e) {
                    Log.e("??????","??????");
                }
                break;
        }
    }


    public void inwonEditText() {


        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("????????? ??????????????????");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String[] inwonTextViewStr = inwonJangbiResultTextView.getText().toString().split(", ");
                            Log.println(Log.ASSERT, "0", inwonJangbiTextView.getText().toString() + "");
                            Log.println(Log.ASSERT, "1", inwonTextViewStr.length + "");
                            Log.println(Log.ASSERT, "2", inwonTextViewStr.length + "");
                            if (editText.getText().length() == 0) {
                                editText.setText("0");
                            }
                            inwonTextViewStr[0] = "?????? " + editText.getText() + "???, ";

                            inwonJangbiResultTextView.setText(inwonTextViewStr[0] + inwonTextViewStr[1] + ", " + inwonTextViewStr[2]);
                        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                            Log.e("??????","??????");
                        }

                        //   codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    public String getCurDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date curTime = new Date();
        return sdf.format(curTime);
    }

    public void signCarSetting() {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("?????? ???????????? ????????? ??????????????????");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] inwonTextViewStr = inwonJangbiResultTextView.getText().toString().split(", ");
                        if (editText.getText().length() == 0) {
                            editText.setText("0");
                        }
                        inwonTextViewStr[1] = "????????? " + editText.getText() + "???, ";

                        inwonJangbiResultTextView.setText(inwonTextViewStr[0] + ", " + inwonTextViewStr[1] + inwonTextViewStr[2]);
                        //     codeMap.put("humanCnt", editText.getText().toString());
                    }
                });
        builder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }


    public static String gosunDaeNoParsing(String gosundaNo) {
        try {
            gosundaNo = gosundaNo.substring(0, 4) + "-" + gosundaNo.substring(4, 8) + "-" + gosundaNo.substring(8, gosundaNo.length());
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
            // e.printStackTrace();
            Log.e("??????","??????");
        }

        /*while(gosundaNo.length())*/

        return gosundaNo;
    }

    public void workCarSetting() {


        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("?????? ???????????? ????????? ??????????????????");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;

                        String[] inwonTextViewStr = inwonJangbiResultTextView.getText().toString().split(", ");
                        if (editText.getText().length() == 0) {
                            editText.setText("0");
                        }
                        inwonTextViewStr[2] = "????????? " + editText.getText() + "???";

                        inwonJangbiResultTextView.setText(inwonTextViewStr[0] + ", " + inwonTextViewStr[1] + ", " + inwonTextViewStr[2]);
                        // codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("??????",
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
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void datePickerMethod(final TextView text) {
        Log.i("datePickerMethod", "??????");
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(WorkPlanResisterLoadContentActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    Log.e("??????","??????");
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        datePicker.show();
    }

    public void maxCharoChoice() {


        final EditText editText = new EditText(this);
        editText.setText(maxCharo + "");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("??? ????????? ??????????????????");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            int preChao = intStrNullCheck(editText.getText().toString());
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
                                Toast.makeText(WorkPlanResisterLoadContentActivity.this, "??? ????????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                maxCharoChoice();
                            }
                        } catch (JSONException | NullPointerException e) {
                            Log.e("??????","??????");
                        }
                    }
                });
        builder.setNegativeButton("??????",
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
        if (roadlimitTextView.getText().length() > 0 && (maxCharo != 0)) {

            // final String[] items = new String[]{"1??????", "2??????", "3??????", "4??????", "5??????", "6??????", "?????? ??????", "??????", "????????????", "????????????", "????????????"};
            final String chaDanContent = "";
            final String[] items = new String[maxCharo + 8];
            for (int i = 0; i < maxCharo; i++) {
                //     items[0]=Integer.toString(i+1)+"??????";
                items[i] = i + 1 + "??????";
            }
            items[maxCharo] = "????????????";
            items[maxCharo + 1] = "????????????";
            items[maxCharo + 2] = "????????????";
            items[maxCharo + 3] = "??????";
            items[maxCharo + 4] = "?????????";
            items[maxCharo + 5] = "????????????";
            items[maxCharo + 6] = "??????";

            //????????? ??????
            items[maxCharo + 7] = "????????????";
            final StringBuffer sb = new StringBuffer();
            final boolean[] itemsBool = new boolean[maxCharo + 8];
            for (int i = 0; i < roadLimit.length; i++) {
                roadLimit[i] = null;
            }
            totalroadLimit.clear();
            AlertDialog.Builder dialog = new AlertDialog.Builder(WorkPlanResisterLoadContentActivity.this);

            dialog.setTitle("?????? ????????? ?????????????????????")
                    .setMultiChoiceItems(
                            items

                            , itemsBool
                            , new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    //

                                    if (isChecked) {
                                        //???????????? ?????? ??????


                                        //  if (!items[which].contains("????????????") && !items[which].contains("????????????") && !items[which].contains("????????????") && !items[which].contains("??????") && !items[which].contains("?????????") && !items[which].contains("????????????") && !items[which].contains("??????")) {
                                        /*roadLimit.add(items[which]);*/
                                        //roadLimit.put(which+1,items[which]);
                                        roadLimit[which] = items[which];
                                        //  }
                                        totalroadLimit.add(items[which]);
                                    } else {

                                        // roadLimit.remove(which);
                                        roadLimit[which] = null;
                                        totalroadLimit.remove(items[which]);
                                    }
                                }
                            }
                    ).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String roadListResult = "";


                    Map<String, String> loadLimitMap = new HashMap<String, String>();
                    loadLimitMap.put("1??????", "01");
                    loadLimitMap.put("2??????", "02");
                    loadLimitMap.put("3??????", "03");
                    loadLimitMap.put("4??????", "04");
                    loadLimitMap.put("5??????", "05");
                    loadLimitMap.put("6??????", "06");
                    loadLimitMap.put("????????????", "07");
                    loadLimitMap.put("????????????", "08");
                    loadLimitMap.put("????????????", "09");
                    loadLimitMap.put("??????", "10");
                    loadLimitMap.put("?????????", "11");
                    loadLimitMap.put("????????????", "20");
                    loadLimitMap.put("??????", "30");
                    loadLimitMap.put("????????????", "12");

                    try {
                        for (int i = 0; i < totalroadLimit.size(); i++) {
                            if (i != totalroadLimit.size() - 1) {
                                roadListResult += loadLimitMap.get(totalroadLimit.get(i)) + ",";
                            } else {
                                roadListResult += loadLimitMap.get(totalroadLimit.get(i));
                            }
                            Log.i("?????? ??????", roadListResult);
                            if (totalroadLimit.get(i).equals("????????????")) {
                                jsonResultObject.put("MVBL_BLC_YN", "Y");//???????????????
                            }
                        }
                        jsonResultObject.put("trfcLimtCrgwClssCd", roadListResult);

                    } catch (JSONException e) {
                        Log.e("??????","??????");
                    } catch (Exception e) {
                        Log.e("??????","??????");
                    }


                    //TRFC_LIMT_CTGW_CLSS_CD
                    Log.i("?????? Resulst", roadListResult);
                    jsonArray = new JSONArray();
                    //sb.append(", "+loadLimitMap.get(items[which]));
                    Log.i("sb ??????", sb.toString());


                    roadlimitTextView.clearComposingText();
                    String charhDetail = "";
                    for (int i = 0; i < roadLimit.length; i++) {
                        if (roadLimit[i] != null) {
                            //charhDetail+=","+roadLimit[i];
                            if (i == roadLimit.length - 1) {
                                if (roadLimit[i].equals("????????????") || roadLimit[i].equals("????????????")) {
                                    charhDetail += ", " + roadLimit[i];
                                } else {
                                    charhDetail += ", " + roadLimit[i] + "??????";
                                }
                            } else {
                                charhDetail += ", " + roadLimit[i];
                            }
                        }
                    }
                    try {
                        if (charhDetail.length() > 20) {
                            roadlimitTextView.setTextSize(9.5f);
                            /* roadlimitTextView.setTextSize(10.5f);*/
                            roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + charhDetail.substring(1) + "");
                        } else {
                            roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + charhDetail.substring(1) + "");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + "??????");
                    }catch (Exception e) {
                        roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + "??????");
                    }

                    /*   roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + roadLimit.size() + "?????? ??????");*/
                    // roadlimitTextView.setText("??? " + maxCharo + "?????? ??? " + charhDetail.substring(1) + "?????? ??????");
                }
            }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
