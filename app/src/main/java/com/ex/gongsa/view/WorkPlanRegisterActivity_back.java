/*
package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ex.gongsa.vo.BonbuList;
import com.ex.gongsa.vo.JangbiListVo;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ex.gongsa.Common.nullCheck;

*/
/*import static com.ex.gongsa.view.AdapterSpinnerUtil.maxCharo;*//*



public class WorkPlanRegisterActivity extends BaseActivity implements OnClickListener {

    final String TAG = "WorkPlanRegisterActivity";

//    Button testcamera=null;

    Spinner spinner1 = null;
    Button buttonspinner1 = null;
    TextView spinner1Tv ,bonbuTv,jisaTv,nosunTv,directionTv,gamdokTv=null;
    Spinner bonbuSpinner = null;
    Button buttonbonbu = null;
    List<String> bonbuData = null;
    //두번째 좌측 스피너

    Spinner jisa = null;
    Button buttonjisa = null;
    List<String> jisaData = null;

    Spinner nosun = null;
    Button buttonnosun = null;
    List<String> nosunData = null;

    Spinner direction = null;
    Button buttondirection = null;
    List<String> direcData = null;


    //차로체한 list
    List<String> road = null;
    List<String> totalroadLimit = null;
    List<String> whenGyochaSelected = null;
    TextView roadlimit = null;

    //감독관
    Spinner gamdok = null;
    Button buttongamdok = null;
    List<String> gamdokList = null;

    //인원 및 장비
    Spinner inwonjangbi = null;
    Button buttoninwonjangbi = null;
    List<String> inwonList = null;
    List<String> jangbiList = null;
    boolean[] jangbool = null;

    //시간
    TextView startdate, enddate;
    Spinner  starthour, startminute,endminute, endhour;
    List<String> starthourList,endhourList,startMinuteList,endMinuteList;
    //시간
    ImageView setstarttime, setendtime;


    //장비 및 인원
    Spinner inwonjangbisel;Button buttoninwonjangbisel;



    //하단 레이아웃
    ViewGroup reset ,registerLayout,inonwjangbilayout= null;

    //작업인원 에딕텍스트
    EditText jakup;
    TextView jackupinwonet;
    Intent intent;

    //ViewGroup
    ViewGroup sendApprovalRequest;

    ImageView gongsaPrev;







    //-----
      */
/*
       Object[0] spinner
       Object[1] TextView
       Object[2] list
     *//*

    ArrayList<Object[]> paramList =null;
    TextView starthourTv = null;
    TextView startminuteTv = null;
    TextView endhourTv = null;
    TextView endminuteTv = null;
    TextView inwonjangbiTv = null;


    EditText gongsaContent;

    EditText gosundaeNum;

    EditText startGongsaGugan,endGongsaGugan;

    JSONObject initJsonObject;
    JSONObject otherJsonObject;
    JSONArray noSunJsonArray ;

    JSONArray bonbuInitArray;
    Integer maxCharo=0;
    String jisaResult;
    boolean clickeCheck;
    String bonbuCode ="";

    Map<String,String> resultParameterMap=null;

    ///-------------
    JSONArray jsonArray;
    JSONObject jsonObj;
    List<BonbuList> BonbuVoList;
    List<String> jisaParamList;
    // List<BonbuList> BonbuVoList;
    List<String> bonbuSaveList =null;
    JSONArray jisaJsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_work_plan_register);
        try {

            //----------------------------------------------------------------------------------------------------------------------------------------------------


            ;


            //본부 위치 바꾸기
            //본부 리스트
            intent = getIntent();
            initJsonObject = new JSONObject(intent.getStringExtra("userInfo").toString());
            Log.i("dd",initJsonObject.get("HDQR_CD").toString());
            Log.i("dd",initJsonObject.get("HDQR_NM").toString());

            String bonbuJSONResult = nullCheck(new Action("get", "http://192.168.1.8:8080/WorkPlan/bonbuItem.do", null).execute().get());
            jsonArray=new JSONArray(bonbuJSONResult);

            Log.i("bonbuJSONResult",jsonArray.toString());
            BonbuVoList = new ArrayList<BonbuList>();
            for(int i = 0 ; i< jsonArray.length();i++){
                BonbuList bonbuList = new BonbuList();

                bonbuList.setHDQR_CD(jsonArray.getJSONObject(i).get("HDQR_CD").toString());
                bonbuList.setHDQR_NM(jsonArray.getJSONObject(i).get("HDQR_NM").toString());
                BonbuVoList.add(i,bonbuList);

                if(!BonbuVoList.get(0).getHDQR_CD().equals(initJsonObject.get("HDQR_CD").toString())){
                    Collections.swap(BonbuVoList,0,i);//초기 셋팅
                }
                //Collections.swap(BonbuVoList,);
            }
            bonbuSaveList=new ArrayList<String>();
            for(int i = 0;i<BonbuVoList.size();i++){
                bonbuSaveList.add(i,BonbuVoList.get(i).getHDQR_NM());
            }
            //리스트 복사


            for(int i = 0; i<BonbuVoList.size();i++){
                BonbuVoList.get(i).getHDQR_NM();
                Log.i("vo",BonbuVoList.get(i).getHDQR_NM());
                Log.i("vo",BonbuVoList.get(i).getHDQR_CD());
            }

            //여기까지가 본부셋팅끝

            jisaParamList=new ArrayList<String>();
            for(int i = 0;i<BonbuVoList.size();i++){
                jisaParamList.add(BonbuVoList.get(i).getHDQR_CD());
            }
            jisaJsonArray = new JSONArray(jisaParamList);

            String jisaJSONResult = nullCheck(new Action("get", "http://192.168.1.8:8080/WorkPlan/jisaItem.do", jisaJsonArray.toString()).execute().get());




            bonbuSpinner = (Spinner) findViewById(R.id.bonbu);
            bonbuTv = (TextView) findViewById(R.id.bonbuTv);
            buttonbonbu = findViewById(R.id.buttonbonbu);

            //
            bonbuTv.setOnClickListener(this);


            //----------------------------------------------------------------------------------------------------------------------------------------------------

           */
/* paramList = new ArrayList<Object[]>();

            BonbuVoList = new ArrayList<BonbuList>();
            BonbuList vo = new BonbuList();
            vo.setHDQR_NM(initJsonObject.get("HDQR_NM").toString());
            vo.setHDQR_CD(initJsonObject.get("HDQR_CD").toString());*//*

            ;//본부이름
            ;//본부코드

//            BonbuVoList.set(0,vo);


            //Log.i("mmmmm",initJsonObject.toString());
            //db 연결
            //new Action("WorkPlanRegisterActivity","http://210.103.95.3:8070/WorkPlan/getTheList.do").execute("");
            //  inwonjangbisel = findViewById(R.id.inwonjangbisel);


            //시간
            startdate = (TextView) findViewById(R.id.startdate);
            enddate = (TextView) findViewById(R.id.enddate);
            startdate.setText(calStr()[0]);
            enddate.setText(calStr()[0]);


            //calStr()
            road = new ArrayList<String>();
            totalroadLimit = new ArrayList<String>();
            whenGyochaSelected = new ArrayList<String>();
            //작업유형
            resultParameterMap =new HashMap<String,String>();


            //Log.i("mmmmmmmm", initJsonObject.get("HDQR_NM").toString());
            //본부
     */
/*       bonbuData = new ArrayList<String>();
            bonbuData.add( initJsonObject.get("HDQR_NM").toString());*//*


            //맵에 본부코드를 담았음
            // resultParameterMap.put("HDQR_CD", initJsonObject.get("HDQR_CD").toString());







            buttonbonbu = findViewById(R.id.buttonbonbu);
            buttonbonbu.setOnClickListener(this);
            //  new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, bonbuSpinner, bonbuData);
            //bonbuSpinner.setSelection(1);
//int resources,Spinner spinner,List<String> list
            //setInitSpinner(R.layout.z_spinner_item,bonbuSpinner,bonbuData);


            bonbuTv.setText(bonbuSpinner.getSelectedItem().toString());
          */
/*  Object[] bonbuArray = new Object[3];
            bonbuArray[0] = bonbuSpinner;
            bonbuArray[1] = bonbuTv;
            bonbuArray[2] = bonbuData;
            paramList.add(bonbuArray);//1번째*//*



            //지사
            jisaData = new ArrayList<String>();
            jisaData.add(initJsonObject.get("MTNOF_NM").toString());
            resultParameterMap.put("MTNOF_CD",initJsonObject.get("MTNOF_CD").toString());

            //jsonArray =new JSONArray(new Action("get", "http://192.168.1.8:8080/WorkPlan/jisaItem.do",initJsonObject.get("HDQR_CD").toString()).execute("").get());

            for(int i = 0 ; i<jsonArray.length();i++){
                otherJsonObject = jsonArray.getJSONObject(i);

               */
/* if(!otherJsonObject.get("MTNOF_NM").toString().equals( initJsonObject.get("MTNOF_NM").toString())){
                    jisaData.add( otherJsonObject.get("MTNOF_NM").toString());
                }*//*

            }
            jisa = (Spinner) findViewById(R.id.jisa);
            // jisa.setOnItemSelectedListener(this);
            buttonjisa = findViewById(R.id.buttonjisa);
            buttonjisa.setOnClickListener(this);
            jisaTv = (TextView) findViewById(R.id.jisaTv);
            jisaTv.setOnClickListener(this);
            //  new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, jisa, jisaData);
            //jisa.setSelection(0);
            //int resources,Spinner spinner,List<String> list


            jisaTv.setText(jisa.getSelectedItem().toString());



            //노선
            //new Action("get", "http://192.168.1.8:8080/WorkPlan/jisaItem.do",jsonObject.toString()).execute("").get();

            //Log.i("노선",new Action("get", "http://192.168.1.8:8080/WorkPlan/nosunItem.do",jsonObject.toString()).execute("").get());
            //  noSunJsonArray = new JSONArray(new Action("get", "http://192.168.1.8:8080/WorkPlan/nosunItem.do",initJsonObject.toString()).execute("").get());
            nosunData = new ArrayList<String>();//USE_ROTNM

            for(int i = 0 ; i<noSunJsonArray.length();i++){
                otherJsonObject = noSunJsonArray.getJSONObject(i);
                nosunData.add(otherJsonObject.get("USE_ROTNM").toString());
            }
            for(int i = 0 ; i< noSunJsonArray.length();i++){
                //ROUTE_CD
                if(nosunData.get(0).equals(noSunJsonArray.getJSONObject(i).get("USE_ROTNM").toString())){
                    resultParameterMap.put("ROUTE_CD",noSunJsonArray.getJSONObject(i).get("ROUTE_CD").toString());
                }
            }
            nosun = (Spinner) findViewById(R.id.nosun);
            // nosun.setOnItemSelectedListener(this);
            buttonnosun = findViewById(R.id.buttonnosun);
            buttonnosun.setOnClickListener(this);
            nosunTv = (TextView) findViewById(R.id.nosunTv);
            nosunTv.setOnClickListener(this);
            //int resources,Spinner spinner,List<String> list

            //   new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, nosun, nosunData);
            //  nosun.setSelection(0);
            nosunTv.setText(nosun.getSelectedItem().toString());

            //  new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, nosun, nosunData,paramList);

            //방향
            direcData = new ArrayList<String>();
            direcData.add("기점 방향");
            direcData.add("종점 방향");
            direcData.add("양방향");
            direction = (Spinner) findViewById(R.id.direction);
            buttondirection = findViewById(R.id.buttondirection);
            buttondirection.setOnClickListener(this);
            directionTv = (TextView) findViewById(R.id.directionTv);
            directionTv.setOnClickListener(this);
            //  new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, direction, direcData);
            //int resources,Spinner spinner,List<String> list

            directionTv.setText(direction.getSelectedItem().toString());


            //감독관
            otherJsonObject.put("MTNOF_CD",resultParameterMap.get("MTNOF_CD"));
            otherJsonObject.put("HDQR_CD",resultParameterMap.get("HDQR_CD"))   ;
            // noSunJsonArray = new JSONArray(new Action("get", "http://192.168.1.8:8080/WorkPlan/gamdokwon.do",otherJsonObject.toString()).execute("").get());
            Log.i("감독",noSunJsonArray.toString());
            gamdokList = new ArrayList<String>();
            for(int i = 0 ;i < noSunJsonArray.length();i++){
                gamdokList.add(noSunJsonArray.getJSONObject(i).get("EMNM").toString());
            }

            gamdok = (Spinner) findViewById(R.id.gamdok);

            buttongamdok = findViewById(R.id.buttongamdok);
            buttongamdok.setOnClickListener(this);

            gamdokTv = (TextView) findViewById(R.id.gamdokTv);
            gamdokTv.setOnClickListener(this);

            gamdokTv.setText(gamdok.getSelectedItem().toString());

            // new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, gamdok, gamdokList);

            //    gamdok.setSelection(0);


            roadlimit = (TextView) findViewById(R.id.roadlimit);//차로제한
            roadlimit.setOnClickListener(this);
            String maxCh= nullCheck(new Action("get", "http://192.168.1.8:8080/WorkPlan/charo.do",resultParameterMap.get("ROUTE_CD")).execute().get());

            roadlimit.setText("총 " + maxCharoCheck(maxCh) + "차로 중 0차로 차단 ");

            Object[] maxCharo = new Object[1];
            maxCharo[0] = roadlimit;
            paramList.add(maxCharo);
            //----------


            starthourList = new ArrayList<String>();
            starthour = (Spinner) findViewById(R.id.starthour);
            starthourList.add(calStr()[1]);
            starthourTv = (TextView) findViewById(R.id.starthourTv);
            starthourTv.setOnClickListener(this);
            for (int i = 0; i <= 23; i++) {
                starthourList.add(Integer.toString(i));
            }
            // new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, starthour, starthourList);
            //  starthourTv.setText(starthour.getSelectedItem().toString());


            endhourList = new ArrayList<String>();
            endhourList.add(calStr()[1]);
            for (int i = 0; i <= 23; i++) {
                endhourList.add(Integer.toString(i));
            }
            endhour = (Spinner) findViewById(R.id.endhour);
            //  new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, endhour, endhourList);

*/
/*        starthour.setText(calStr()[1]);
        endhour.setText(calStr()[1]);*//*


            startMinuteList = new ArrayList<String>();
            startMinuteList.add(calStr()[2]);
            for (int i = 0; i < 4; i++) {
                startMinuteList.add(Integer.toString(i * 15));
            }
            startminute = (Spinner) findViewById(R.id.startminute);

            startminuteTv = (TextView) findViewById(R.id.startminuteTv);
            startminuteTv.setOnClickListener(this);
            //new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, startminute, startMinuteList);

            startminuteTv.setText(startminute.getSelectedItem().toString());


            endMinuteList = new ArrayList<String>();
            endMinuteList.add(calStr()[2]);
            for (int i = 0; i < 4; i++) {
                endMinuteList.add(Integer.toString(i * 15));
            }
            endminute = (Spinner) findViewById(R.id.endminute);
            endminuteTv = (TextView) findViewById(R.id.endminuteTv);
            endminuteTv.setOnClickListener(this);
            endhourTv = (TextView) findViewById(R.id.endhourTv);
            endhourTv.setOnClickListener(this);
            // new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, endminute, endMinuteList);
            //     endhourTv.setText(endhour.getSelectedItem().toString());
            endhourTv.setText("");
            endminuteTv.setText(endminute.getSelectedItem().toString());
            //  endminute.setSelection(0);
*/
/*        startminute.setText(calStr()[2]);
        endminute.setText(calStr()[2]);*//*


            //이미지 뷰 달력 사진
            setstarttime = (ImageView) findViewById(R.id.setstarttime);
            setstarttime.setOnClickListener(this);
            setendtime = (ImageView) findViewById(R.id.setendtime);
            setendtime.setOnClickListener(this);

            //하단 레이아웃 버튼
            reset = (ViewGroup) findViewById(R.id.reset);
            reset.setOnClickListener(this);
            registerLayout = (ViewGroup) findViewById(R.id.registerLayout);
            registerLayout.setOnClickListener(this);


            //인원 및 장비
            inwonList = new ArrayList<String>();
            inwonList.add("선택");
            inwonList.add("작업원");
            inwonList.add("장비");
            inwonjangbi = (Spinner) findViewById(R.id.inwonjangbi);

//        inwonjangbi.setOnClickListener(this);
            //    inwonjangbi.setOnClickListener(this);
            buttoninwonjangbi = findViewById(R.id.buttoninwonjangbi);
            jackupinwonet = (TextView) findViewById(R.id.jackupinwonet);
            inwonjangbiTv = (TextView) findViewById(R.id.inwonjangbiTv);
            inwonjangbiTv.setOnClickListener(this);
            //buttoninwonjangbi.setOnClickListener(this);
            //int resources,Spinner spinner,List<String> list
            // setInitSpinner(R.layout.z_spinner_item,inwonjangbi,inwonList);


            //   new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, inwonjangbi, inwonList);
            //   gamdok.setSelection(0);
            inwonjangbiTv.setText(inwonjangbi.getSelectedItem().toString());
            jangbiList = new ArrayList<String>();


            //map


            gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
            gongsaPrev.setOnClickListener(this);


            //공사내용 텍스트 컬러 맞춤
            gongsaContent = (EditText) findViewById(R.id.gongsaContent);
            gongsaContent.setTextColor(-4276546);

            //고순대 접수번호  텍스트 컬러 맞춤
            gosundaeNum = (EditText) findViewById(R.id.gosundaeNum);
            gosundaeNum.setTextColor(-4276546);

            //시작구간 텍스트 컬러 맞춤
            startGongsaGugan = (EditText) findViewById(R.id.startGongsaGugan);
            startGongsaGugan.setTextColor(-4276546);

            //종료구간 텍스트 컬러맞춤
            endGongsaGugan = (EditText) findViewById(R.id.endGongsaGugan);
            endGongsaGugan.setTextColor(-4276546);
            this.clickeCheck= false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }//onCreate



    @Override
    public void onResume() {
        super.onResume();
        //     Log.i("색확인",Integer.toString(spinner1Tv.getCurrentTextColor()));
        Log.i("mm","onResume");

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.gamdokTv:
                gamdok.performClick();
                buttongamdok.performClick();
                break;
            case R.id.directionTv:
                direction.performClick();
                break;

            case R.id.buttonbonbu:
                bonbuSpinner.performClick();
                break;
            case R.id.buttonjisa:
                jisa.performClick();
                break;
            case R.id.buttonnosun:
                nosun.performClick();
                break;
            case R.id.buttondirection:
                direction.performClick();
                break;
            case R.id.buttongamdok:
                gamdok.performClick();
                break;
            case R.id.buttoninwonjangbi:
                inwonjangbi.performClick();
                break;
            case R.id.setstarttime:
                //   timePickerMethod(starthour, startminute,R.id.setstarttime);
                datePickerMethod(startdate);
                break;
            case R.id.setendtime:
                //    timePickerMethod(endhour, endminute,R.id.setendtime);
                datePickerMethod(enddate);
                break;

            case R.id.reset ://초기화
                Toast.makeText(this,"ㅡㅡ",Toast.LENGTH_SHORT).show();
                //  bonbuData.clear();
                // jisaData.clear();
                intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
                // overridePendingTransition(0,0);
                break;
            case R.id.registerLayout:
                intent = getIntent();

                // Toast.makeText(this,this.jackupinwonet.getText() , Toast.LENGTH_SHORT).show();


                */
/*        Toast.makeText(getApplicationContext(),"힣",Toast.LENGTH_LONG).show();*//*

                //    chechingData();
                */
/*CustomDialog dialog=  new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_custom_dialog);
                dialog.callFunction();*//*


                //CustomDialog dialog =new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_custom_dialog);
                //sendApprovalRequest=dialog.findViewById(R.id.sendApprovalRequest);
                //dialog.callFunction();    try{
                try{
                    if( (startdate.getText().toString()+"  "+starthourTv.getText().toString()+"시"+startminuteTv.getText().toString()+"분").equals(enddate.getText().toString()+"  "+endhourTv.getText().toString()+"시"+endminuteTv.getText().toString()+"분")){
                        new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_alert_dialog,R.id.alertOk,"공사 시간을 입력해주세요").callFunction();
                    } else if(gongsaContent.getText().toString().length()==0){
                        new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_alert_dialog,R.id.alertOk,"공사 내용을 입력해주세요").callFunction();
                    } else if(startGongsaGugan.getText().toString().length()==0 || endGongsaGugan.getText().toString().length()==0){
                        new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_alert_dialog,R.id.alertOk,"공사 구간을 입력해주세요").callFunction();
                    } else if(gosundaeNum.getText().toString().length()==0){
                        new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_alert_dialog,R.id.alertOk,"고분대 접수번호를 입력해주세요").callFunction();
                    }else if(road.size()==0){
                        new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_alert_dialog,R.id.alertOk,"제한 차로를 선택하여 주세요").callFunction();
                    } else if (jackupinwonet.getText().toString().length() ==0 ||jangbiList.size()==0) {
                        new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_alert_dialog,R.id.alertOk,"인원 및 장비를 선택해주세요").callFunction();
                    }  else if (calCompareCheck(startdate.getText().toString(),starthourTv.getText().toString(),startminuteTv.getText().toString(),enddate.getText().toString(),endhourTv.getText().toString(),endminuteTv.getText().toString())==false) {
                        new CustomDialog(WorkPlanRegisterActivity.this,R.layout.z_alert_dialog,R.id.alertOk,"공사 종료시간은 공사 시작시간보다 나중이어야합니다.").callFunction();
                    } else {

                        JSONObject obj = new JSONObject();
                        obj.accumulate("bonbu", bonbuTv.getText().toString());//json에 본부값 셋팅
                        obj.accumulate("jisa", jisaTv.getText().toString());//json에 지사값 셋팅
                        obj.accumulate("nosun", nosunTv.getText().toString());//json에 노선값셋팅
                        obj.accumulate("direction", directionTv.getText().toString());
                        obj.accumulate("roadlimit", roadlimit.getText().toString());
                        obj.accumulate("gamdokTv", gamdokTv.getText().toString());
                        obj.accumulate("gongsaContent", gongsaContent.getText().toString());
                        obj.accumulate("startTime", startdate.getText().toString() + "  " + starthourTv.getText().toString() + "시" + startminuteTv.getText().toString() + "분");
                        obj.accumulate("endTime", enddate.getText().toString() + "  " + endhourTv.getText().toString() + "시" + endminuteTv.getText().toString() + "분");
                        obj.accumulate("startGugan", startGongsaGugan.getText().toString());
                        obj.accumulate("endGugan", endGongsaGugan.getText().toString());
                        obj.accumulate("inwonjangbi", jackupinwonet.getText().toString());
                        obj.accumulate("gosundaeNum", gosundaeNum.getText().toString());
                        new CustomDialog(WorkPlanRegisterActivity.this, R.layout.z_custom_dialog, R.id.sendApprovalRequest, obj).callFunction();

                        // init(WorkPlanRegisterActivity.this,R.layout.z_custom_dialog,R.id.sendApprovalRequest);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.gongsaPrev:
                finish();
                break;
            //    case R.id.buttonspinner1:

            //   spinner1.performClick();
            // break;
            //  case R.id.spinner1Tv:
            //   spinner1.performClick();
            // ;
            //   buttonspinner1.performClick();
            //  new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, spinner1, data,paramList);


            //  break;
            case R.id.bonbuTv:
                //  Toast.makeText(this,"일단 여기 와",Toast.LENGTH_SHORT).show();
                clickeCheck= true;
                System.out.println("________________________________");
                System.out.println("________________________________");
                System.out.println("________________________________");

                System.out.println(clickeCheck);
                System.out.println("________________________________");
                System.out.println("________________________________");
                System.out.println("________________________________");
                bonbuSpinner.performClick();
                buttonbonbu.performClick();
                //  new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, bonbuSpinner, bonbuData,paramList);

                // roadlimit.clearComposingText();
                //roadlimit.setText("총"+charo+"중 0차로 선택됨");

                break;
            case R.id.jisaTv:
                buttonjisa.performClick();
                jisa.performClick();
                ;
                //new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, jisa, jisaData,paramList);

                //  roadlimit.clearComposingText();
                // roadlimit.setText("총"+charo+"중 0차로 선택됨");

                break;
            case R.id.nosunTv:
                Toast.makeText(this,"잉",Toast.LENGTH_SHORT).show();
                nosun.performClick();
                //new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, nosun, nosunData,paramList);

                // roadlimit.clearComposingText();
                // roadlimit.setText("총"+charo+"중 0차로 선택됨");

                break;

            case R.id.starthourTv:
                starthour.performClick();
                break;

            case R.id.startminuteTv:
                startminute.performClick();
                break;
            case R.id.endhourTv:
                endhour.performClick();
                break;
            case R.id.endminuteTv:
                endminute.performClick();
                break;

            case R.id.inwonjangbiTv:
                inwonjangbi.performClick();
                buttoninwonjangbi.performClick();
                break;
            case R.id.roadlimit:
                maxCharoSelected();
                break;
            */
/*   case R.id.roadlimit:

             *//*
*/
/*DialogModule(WorkPlanRegisterActivity.this,"제한 차로를 선택하여주세요",)*//*
*/
/*
              //  Toast.makeText(WorkPlanRegisterActivity.this,"ㅠㅠ",Toast.LENGTH_SHORT);
                try{
                    Log.i("roadlimit",Integer.toString(maxCharo));
                //    Toast.makeText(WorkPlanRegisterActivity.this,Integer.toString(maxCharo),Toast.LENGTH_SHORT);
                   new DialogModule(WorkPlanRegisterActivity.this,"차로를 선택하여주세요",maxCharo,"Charo");
                }catch (NullPointerException e){
                    new DialogModule(WorkPlanRegisterActivity.this,"차로를 선택하여주세요",0,"Charo");
                  //  Toast.makeText(WorkPlanRegisterActivity.this,"에러",Toast.LENGTH_SHORT);
                    *//*
*/
/*e.printStackTrace();*//*
*/
/*

                }
                break;*//*

      */
/*      case R.id.inonwjangbilayout:
               // inwonjangbi.performClick();

            break;*//*

    */
/*        case R.id.spinner1:
                new AdapterSpinnerUtil(WorkPlanRegisterActivity.this, R.layout.z_spinner_item, spinner1, data,spinner1Tv);;
                break;*//*

        */
/*    case R.id.spinner1Tv:
                spinner1.performClick();
                break;*//*



        }
    }





    public boolean chechingData(){
        if(!bonbuSpinner.getSelectedItem().toString().equals("본부")){
            Log.i("본부값",bonbuSpinner.getSelectedItem().toString());
        }

        if(!jisa.getSelectedItem().toString().equals("지사")){
            Log.i("지사값",jisa.getSelectedItem().toString());
        }
        return false;
    }

    //초기 접속시 달력을 익일 날자로 셋팅.
    final public String[] calStr() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        int month = cal.get(Calendar.MONTH) + 1;
        String yearMon = cal.get(Calendar.YEAR) + "/" + month + "/" + cal.get(Calendar.DATE);
        String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String min = Integer.toString(cal.get(Calendar.MINUTE));
        String[] calArray = {"", "", ""};
        calArray[0] = yearMon;
        calArray[1] = hour;
        calArray[2] = min;
        return calArray;
    }

    public void datePickerMethod(final TextView text) {
        Log.i("요기", "요기");
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(WorkPlanRegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                Log.i("요기", "요기요");

                //String yearMonth = String.format("%d/%d/%d", datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String yearMonth = String.format("%d/%d/%d", year, month + 1, date);
                text.clearComposingText();
                text.setText(yearMonth);

            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        datePicker.show();

    }

    public void timePickerMethod(final Spinner text, final Spinner text1,final int checking) {

        Calendar cal = Calendar.getInstance();
        TimePickerDialog timePicker = new TimePickerDialog(WorkPlanRegisterActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker datePicker, int hour, int min) {
                //   String yearMonth = String.format("%d/%d/%d",hour,min);
                //text.clearComposingText();
                //분을 화살표로 조정하면 되지만 타이핑으로 조정하면 조정이 안됨
                Log.e("아워",Integer.toString(hour));
                //  text.setText(Integer.toString(hour));
                //  text1.clearComposingText();
                Log.e("민",Integer.toString(min));

                // text1.setText(Integer.toString(min));
                try{
                    if(checking == R.id.setendtime && calCompareCheck(startdate.getText().toString(),starthour.getSelectedItem().toString(),startminute.getSelectedItem().toString(),enddate.getText().toString(),endhour.getSelectedItem().toString(),endminute.getSelectedItem().toString())==false){
                        // Toast.makeText(getApplicationContext(),"끝난시간은 시작시간보다 늦은 시간에 있어야 합니다.",Toast.LENGTH_LONG).show();
                        // timePickerMethod(endhour, endminute,checking);
                        // datePickerMethod(enddate);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        Log.e("이건 뭔미",Integer.toString(cal.get(Calendar.MINUTE)));

        timePicker.show();


    }




    public boolean calCompareCheck(String firdate,String firhour,String firminute,String date,String hour, String minute) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date d = sdf.parse(firdate+" "+firhour+":"+firminute);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date d1 = sdf1.parse(date+" "+hour+":"+minute);
        Log.i("d",d.toString());
        Log.i("d1",d1.toString());
        if(d.before(d1)==false){
            return false;
        }else{
            return true;
        }

    }

   */
/* @Override
    public  void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4){
        Log.e("여기","ㅠㅠ");
        switch (var3){
            case 0:
                Toast.makeText(this,"d",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public   void onNothingSelected(AdapterView<?> var1){

    }*//*



    final String plusCommaJangbi(ArrayList<JangbiListVo> list, String[] title){
        String result = "";
        Map<String,String> resultMap = new HashMap<String,String>();
        for(int i = 0 ; i <list.size();i++) {
            //resultMap.put(list.get(i).getCMMN_CD_NM(), list.get(i).getCMMN_CD());
        }
        for(int i =0;  i<title.length;i++) {
            if(i==0) {
                result +=resultMap.get(title[i]);;
            }else {
                result +=", "+resultMap.get(title[i]);;
            }

        }
        return result;
    }








    public String maxCharoCheck(String maxCharo){
        int charo = Integer.parseInt(maxCharo);
        if(charo <= 6){
            this.maxCharo = charo;
            return Integer.toString(charo);
        }else{
            this.maxCharo = 6;
            return "6";
        }
    }

    public void maxCharoSelected(){
        if (roadlimit.getText().length() > 0 && (maxCharo != null && maxCharo != 0)  ) {

            // final String[] items = new String[]{"1차로", "2차로", "3차로", "4차로", "5차로", "6차로", "이동 차단", "갓길", "진입램프", "진출램프", "진출램프"};
            final String[] items = new String[maxCharo + 5];
            for (int i = 0; i < maxCharo; i++) {
                //     items[0]=Integer.toString(i+1)+"차로";
                items[i] = i + 1 + "차로";
            }
            items[maxCharo] = "이동 차단";
            items[maxCharo + 1] = "갓길";
            items[maxCharo + 2] = "진입램프";
            items[maxCharo + 3] = "진출램프";
            items[maxCharo + 4] = "교차 차단";
            final boolean[] itemsBool = new boolean[maxCharo + 5];
            road.clear();
            AlertDialog.Builder dialog = new AlertDialog.Builder(WorkPlanRegisterActivity.this);

            dialog.setTitle("차단 차로를 선택하여주세요")
                    .setMultiChoiceItems(
                            items

                            , itemsBool
                            , new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    totalroadLimit.add(items[which]);
                                    if (isChecked) {
                                        if (!items[which].contains("이동 차단") && !items[which].contains("갓길") && !items[which].contains("진입램프") && !items[which].contains("진출램프") && !items[which].contains("교차 차단")) {


                                            road.add(items[which]);

                                        }//if
                                        //     jangbiList.add(items[which]);
                                    } else {
                                        road.remove(items[which]);
                                    }
                                }
                            }
                    ).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    roadlimit.clearComposingText();
                    roadlimit.setText("총 " + maxCharo + "차로 중 " + road.size() + "차로 차단");

                    // Log.i("--", Integer.toString(road.size()));

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



    @Override
    public String onActionPost(String primitive, String date) {
        Log.e("통신결과766:",date);
        return date;
    }

*/
/*
    public void jsonParsingUtil(String url, JSONObject job,String name,String code,List<String> list){
        try{
            String jsonResult = nullCheck(new Action("get", url, job.get(code).toString()).execute().get());
            JSONArray jsonArray = new JSONArray(URLDecoder.decode(jsonResult, "UTF-8"));
            list.clear();

        }catch (Exception e){
            e.printStackTrace();
        }
    }*//*





    */
/*
    *     String jisaResult = nullCheck(new Action("get", "http://192.168.1.8:8080/WorkPlan/jisaItem.do", initJsonObject.get("HDQR_CD").toString()).execute().get());
                    jsonArray = new JSONArray(URLDecoder.decode(jisaResult, "UTF-8"));
                    jisaData.clear();
                    jisaData.add(initJsonObject.get("MTNOF_NM").toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        otherJsonObject = jsonArray.getJSONObject(i);
                        //nextList.add(i,job.getString("HDQR_NM"));
                        if(!otherJsonObject.get("MTNOF_CD").toString().equals(initJsonObject.get("MTNOF_CD").toString())){
                            jisaData.add( otherJsonObject.getString("MTNOF_NM"));
                        }


                        //((List<String>) (((Object[]) nextList.get(2))[2])).add(i, job.getString("MTNOF_NM"));
                    }*//*


*/
/*        resultParameterMap.put("HDQR_CD", bonbuCode);
    String jisaResult = nullCheck(new Action("get", "http://192.168.1.8:8080/WorkPlan/jisaItem.do",bonbuCode).execute().get());
    jsonArray = new JSONArray(URLDecoder.decode(jisaResult, "UTF-8"));
                    jisaData.clear();


                    for (int i = 0; i < jsonArray.length(); i++) {
        otherJsonObject = jsonArray.getJSONObject(i);
        //nextList.add(i,job.getString("HDQR_NM"));

        jisaData.add(i, otherJsonObject.getString("MTNOF_NM"));

        //((List<String>) (((Object[]) nextList.get(2))[2])).add(i, job.getString("MTNOF_NM"));
    }
                    resultParameterMap.put("MTNOF_CD",jsonArray.getJSONObject(position).getString("MTNOF_CD"));
                    jisaTv.clearComposingText();
                    jisa.setSelection(position);
                    jisaTv.setText(jisa.getSelectedItem().toString());*//*


//}
*/
