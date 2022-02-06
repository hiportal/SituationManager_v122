//package com.ex.gongsa.view;
//
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.InputType;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.ex.gongsa.Common;
//import com.ex.situationmanager.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.net.URLDecoder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static com.ex.gongsa.Common.nullCheck;
//import static com.ex.gongsa.Configuration.SERVER_URL;
//import static com.ex.gongsa.view.AdapterSpinnerUtil.maxCharo;
//
////작업계획등록
//public class WorkPlanRegisterActivity_bak extends BaseActivity implements OnClickListener {
//
//    final String TAG = "WorkPlanRegisterActivity";
//    ImageView gongsaPrev;//화면 좌측 상단 뒤로가기 이미지뷰
//    ImageView startDateImageView;//시작 날짜 이미지
//    ImageView endDateImageView;
//
//    Intent intent;//인텐트(메인화면으로부터 사용자 정보를 받아왔다)
//
//    Spinner bonbuSpinner;//숨겨진 본부 스피너
//    Spinner jisaSpinner;//지사 스피너
//    Spinner nosunSpinner;//노선 스피너
//    Spinner direnctionSpinner;//방향 스피너
//    Spinner gamdokSpinner;//감독원 스피너
//    Spinner inwonJangbiSpinner;
//    Spinner startHourSpinner;//시작 시간 스피너
//    Spinner endHourSpinner;//종료 시간 스피너
//    Spinner startMinuteSpinner;
//    Spinner endMinuteSpinner;//종로 분 스피너
//    Spinner workTypeSpinner;
//    Spinner charoSpinner;
//
//    TextView bonbuTextView;//본부 스피너 위 TextView
//    TextView jisaTextView;//지사 텍스트뷰
//    TextView noSunTextView;//노선 텍스트뷰
//    TextView directionTextView;//방향 텍스트뷰
//    TextView roadlimitTextView;//차로제한 텍스트뷰
//    TextView gamdokwonTextView;//감독원 텍스트뷰
//    TextView inwonJangbiTextView;//인원 장비 텍스트뷰(우측)
//    TextView inwonJangbiResultTextView;//인원 장비 최종 결과값 텍스트뷰(좌측)
//    TextView startDateTextView;//시작 시간 텍스트뷰
//    TextView endDateTextView;//종료 날짜 텍스트뷰
//    TextView startHourTextView;//시작 시간 텍스트뷰
//    TextView startMinuteTextView;//시작 분 텍스트뷰
//    TextView endHourTextView;//종료 시간 텍스트뷰
//    TextView endMinuteTextView;//종료 분 텍스트뷰
//    TextView workTypeTextView;//작업유형 텍스트뷰
//
//    EditText ET_gongsaContent;
//    EditText ET_startGongsaGugan;
//    EditText ET_endGongsaGugan;
//    EditText ET_gosundaeNum;
//
//    Button bonbuButton;//본부 버튼
//    Button jisaButton;//지사 버튼
//    Button nosunButton;//노선 버튼
//    Button directionButton;//방향 버튼
//    Button buttoninwonjangbi;//작업유형 버튼
//
//    LinearLayout ll_bonbuSpinner;//본부 레이아웃
//    LinearLayout li_jisaSpinner;//지사 레이아수
//    LinearLayout li_registerBtn;//등록버튼
//    LinearLayout li_ResetBtn;
//    //초기화용 리스트
//
//    List<String> jisaList;//지사리스트
//    List<String> noSunList;//노선리스트
//    List<String> gamdocList;//감독리스트
//    List<String> directionList;//방향 리스트
//    List<String> roadLimit;//차로제한 리스트
//    List<String> bonbuList;//본부리스트
//    List<String> totalroadLimit;//차로제한 리스트에 대한 최종 결과값
//    List<String> inwonJangbiList;
//    List<String> jangbiList;//장비 리스트
//    List<String> startHourList;//공사 시작 시간 리스트
//    List<String> endHourList;//공사 종료 시간 리스트
//    List<String> startMinuteList;//시작 분 셋팅
//    List<String> endMinuteList;//종료 분 셋팅
//    List<String> workTypeList;//작업유형 리스트
//    List<Map<String, String>> workTypeMapList;
//    List<String> charoList;
//
//    //결과 코드값을 담고있기위한 맵
//    public static Map<String, String> codeMap;//서버로 전송시키는 각각의 파라미터를 셋팅하는 Map
//
//
//    Map<String, Map<String, String>> itemKeyValueMap;
//    Map<String, String> bonbuMap;//본부에 대한 키밸류를 담기위한 map
//    Map<String, String> jisaMap;
//    Map<String, String> nosunMap;
//    Map<String, String> workTypeMap;
//
//    Map<String, String> directionMap; //방향에 대한 코드를 담기위한 Map
//    Map<String, Object[]> viewCollection;
//
//    JSONObject jsonObject;
//    JSONArray jsonArray;
//    JSONObject savedUserInfo;//MainActivity 에서 전달받은 사용자 정보 저장소
//
//
//    //---
//
//    static Double startLimitGugan = 0.0;
//    static Double endLimitGugan = 0.0;
//    static Double startLimitEtValue = 0.0;
//    static Double endLimitEtValue = 0.0;
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        intent = getIntent();
//        setContentView(R.layout.z_work_plan_register);
//        Log.i("intentResult", intent.getStringExtra("userInfo"));
//        try {
//            savedUserInfo = new JSONObject(intent.getStringExtra("userInfo"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        codeMap = new HashMap<String, String>();//
//        directionMap = new HashMap<String, String>();//
//        viewCollection = new HashMap<String, Object[]>();
//        bonbuMap = new HashMap<String, String>();
//        jisaMap = new HashMap<String, String>();
//        nosunMap = new HashMap<String, String>();
//
//
//        itemKeyValueMap = new HashMap<String, Map<String, String>>();
//
//        bonbuList = new ArrayList<String>();//본부리스트
//        jisaList = new ArrayList<String>();//지사 리스트
//        noSunList = new ArrayList<String>();//노선 리스트
//        directionList = new ArrayList<String>();//방향 리스트
//        gamdocList = new ArrayList<String>();//감독원 리스ㅡㅌ
//        roadLimit = new ArrayList<String>();
//        totalroadLimit = new ArrayList<String>();
//        inwonJangbiList = new ArrayList<String>();
//        jangbiList = new ArrayList<String>();
//        startHourList = new ArrayList<String>();//공사 시작 시간 리스트
//        startMinuteList = new ArrayList<String>();//공사 시작 분 리스트
//        endHourList = new ArrayList<String>();//종료 시간 설정
//        endMinuteList = new ArrayList<String>();//종료 분 리스트
//        workTypeList = new ArrayList<String>();
//        workTypeMapList = new ArrayList<Map<String, String>>();
//        charoList = new ArrayList<String>();
//
//        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
//        startDateImageView = (ImageView) findViewById(R.id.startDateImageView);
//        endDateImageView = (ImageView) findViewById(R.id.endDateImageView);
//
//        bonbuSpinner = (Spinner) findViewById(R.id.bonbuSpinner);
//        jisaSpinner = (Spinner) findViewById(R.id.jisaSpinner);
//        nosunSpinner = (Spinner) findViewById(R.id.nosunSpinner);
//        direnctionSpinner = (Spinner) findViewById(R.id.direnctionSpinner);
//        gamdokSpinner = (Spinner) findViewById(R.id.gamdokSpinner);
//        inwonJangbiSpinner = (Spinner) findViewById(R.id.inwonJangbiSpinner);
//        startHourSpinner = (Spinner) findViewById(R.id.startHourSpinner);
//        endHourSpinner = (Spinner) findViewById(R.id.endHourSpinner);
//        startMinuteSpinner = (Spinner) findViewById(R.id.startMinuteSpinner);
//        endMinuteSpinner = (Spinner) findViewById(R.id.endMinuteSpinner);
//        workTypeSpinner = (Spinner) findViewById(R.id.workTypeSpinner);
//        charoSpinner = (Spinner) findViewById(R.id.charoSpinner);
//
//        ET_gongsaContent = (EditText) findViewById(R.id.ET_gongsaContent);
//        ET_startGongsaGugan = (EditText) findViewById(R.id.ET_startGongsaGugan);
//        ET_endGongsaGugan = (EditText) findViewById(R.id.ET_endGongsaGugan);
//        ET_gosundaeNum = (EditText) findViewById(R.id.ET_gosundaeNum);
//
//        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);
//        jisaTextView = (TextView) findViewById(R.id.jisaTextView);
//        noSunTextView = (TextView) findViewById(R.id.noSunTextView);
//        directionTextView = (TextView) findViewById(R.id.directionTextView);
//        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);
//        gamdokwonTextView = (TextView) findViewById(R.id.gamdokwonTextView);
//        inwonJangbiTextView = (TextView) findViewById(R.id.inwonJangbiTextView);
//        inwonJangbiResultTextView = (TextView) findViewById(R.id.inwonJangbiResultTextView);
//        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
//        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
//        startHourTextView = (TextView) findViewById(R.id.startHourTextView);
//        startMinuteTextView = (TextView) findViewById(R.id.startMinuteTextView);
//        endHourTextView = (TextView) findViewById(R.id.endHourTextView);
//        endMinuteTextView = (TextView) findViewById(R.id.endMinuteTextView);
//        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView); //작업유형 텍스트뷰
//
//        bonbuButton = (Button) findViewById(R.id.bonbuButton);
//        jisaButton = (Button) findViewById(R.id.jisaButton);
//        nosunButton = (Button) findViewById(R.id.nosunButton);
//        directionButton = (Button) findViewById(R.id.directionButton);
//
//        ll_bonbuSpinner = (LinearLayout) findViewById(R.id.ll_bonbuSpinner);
//        li_jisaSpinner = (LinearLayout) findViewById(R.id.li_jisaSpinner);
//        li_registerBtn = (LinearLayout) findViewById(R.id.li_registerBtn);
//        li_ResetBtn = (LinearLayout) findViewById(R.id.li_ResetBtn);
//
//        bonbuTextView.setOnClickListener(this);
//        jisaTextView.setOnClickListener(this);
//        gongsaPrev.setOnClickListener(this);
//        bonbuButton.setOnClickListener(this);
//        ll_bonbuSpinner.setOnClickListener(this);
//        li_jisaSpinner.setOnClickListener(this);
//        li_registerBtn.setOnClickListener(this);
//        noSunTextView.setOnClickListener(this);
//        nosunButton.setOnClickListener(this);
//        jisaButton.setOnClickListener(this);
//        directionTextView.setOnClickListener(this);
//        directionButton.setOnClickListener(this);
//        roadlimitTextView.setOnClickListener(this);
//        gamdokwonTextView.setOnClickListener(this);
//        inwonJangbiTextView.setOnClickListener(this);
//        li_ResetBtn.setOnClickListener(this);
//        startHourTextView.setOnClickListener(this);
//        endHourTextView.setOnClickListener(this);//종료 시간 텍스트뷰
//        startMinuteTextView.setOnClickListener(this);//시작 분 셋팅
//        endMinuteTextView.setOnClickListener(this);
//        startDateImageView.setOnClickListener(this);//시작 날짜 이미지
//        endDateImageView.setOnClickListener(this);//종료 날짜 이미지
//        workTypeTextView.setOnClickListener(this);
//
//
//        try {
//
//            codeMap.put("HDQR_CD", savedUserInfo.get("HDQR_CD").toString());//본부 키값 기본 셋팅
//            codeMap.put("MTNOF_CD", savedUserInfo.get("MTNOF_CD").toString());//지사 키값 기본 셋팅
//            codeMap.put("HDQR_NM", savedUserInfo.get("HDQR_NM").toString());
//            codeMap.put("MTNOF_NM", savedUserInfo.get("MTNOF_NM").toString());
//            codeMap.put("ROUTE_CD", "");//노선 키값 기본 셋팅
//            codeMap.put("ROUTE_DRCT_CD", "");//방향 키값 기본 셋팅
//            codeMap.put("CRGW_LIST", "");//노선 키값 기본 셋팅
//            // codeMap.put("RMRK_CTNT","");//비고내용 -->고순대 접수번호
//            codeMap.put("CNSTN_CTNT", "");//공사내용
//            codeMap.put("TOT_CRGW_CNT", "");//총차로수
//            //codeMap.put("SECT_CD","");//구간코드
//            //codeMap.put("EDPNT_SECT_CD","");//종점 구간코드
//            codeMap.put("CSTR_CRPR_PRCH_TELNO", savedUserInfo.get("TEL_NO").toString());
//            codeMap.put("GONSUDAE_NO", "");
//            codeMap.put("LIMT_PLAN_DATES", "");
//            codeMap.put("MVBL_BLC_YN", "N");//이동차단여부
//            codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "");//
//            codeMap.put("MTNOF_PRCH_EMNO", "");//
//
//            Log.i("사용자 아이디", savedUserInfo.get("userId").toString());
//            codeMap.put("FSTTM_RGSR_ID", savedUserInfo.get("userId").toString());
//            codeMap.put("LSTTM_MODFR_ID", savedUserInfo.get("userId").toString());
//            codeMap.put("USER_EMNO", "");
//            codeMap.put("minMtnofStpntDstnc", "");
//            codeMap.put("maxMtnofEdpntDstnc", "");
///*            codeMap.put("blcCtnt","");//차단내용
//            codeMap.put("cmmnCdNm","");//장비이름*/
//            codeMap.put("cmmnCd", "");
//            codeMap.put("humanCnt", "");//
//            codeMap.put("janbiNm", "");//장비이름
//            codeMap.put("janbiCd", "");//장비코드
//            codeMap.put("roadLimitResult","");
//            codeMap.put("CSTR_CRPR_RCRD_CTNT",savedUserInfo.get("PSNM").toString());//소속 시공사
//
//            //방향에 대한 키밸류 셋팅팅
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //본부
//        try {
//            String bonbuJsonResult = nullCheck(new Action("get", SERVER_URL+"/WorkPlan/bonbuItem.do", null,this).execute().get());
//            Log.i("bonbuJsonResult", bonbuJsonResult);
//            bonbuMap.put(savedUserInfo.get("HDQR_NM").toString(), savedUserInfo.get("HDQR_CD").toString());
//            bonbuList.add(savedUserInfo.get("HDQR_NM").toString());
//
//            codeMap.put("HDQR_CD", savedUserInfo.get("HDQR_CD").toString());//
//
//            jsonArray = new JSONArray(bonbuJsonResult);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                jsonObject = jsonArray.getJSONObject(i);
//                if (!savedUserInfo.get("HDQR_CD").toString().equals(jsonObject.get("HDQR_CD").toString())) {
//                    bonbuMap.put(jsonObject.get("HDQR_NM").toString(), jsonObject.get("HDQR_CD").toString());//첫번째 맵
//                    bonbuList.add(jsonObject.get("HDQR_NM").toString());
//                }
//            }
//
//
//            itemKeyValueMap.put("bonbuList", bonbuMap);//두번째 맵
//            // resultCodeMap= AdapterSpinnerUtil(this, R.layout.z_spinner_item, bonbuSpinner, bonbuList, codeMap);//코드맵
//            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, bonbuSpinner, bonbuList/*, codeMap*/);
//
//            // bonbuSpinner.setSelection(0);
//            bonbuTextView.setText(Common.nullCheck(bonbuSpinner.getSelectedItem().toString()));
//            viewCollection.put("bonbuView", new Object[]{bonbuSpinner, bonbuTextView, bonbuList});
//            Log.i("onCreate", "onCreate");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //지사
//        try {
//            String jisaJsonResult = nullCheck(new Action("get", SERVER_URL+"/WorkPlan/jisaItem.do", codeMap.get("HDQR_CD"),this).execute().get());
//            jisaMap.put(savedUserInfo.get("MTNOF_NM").toString(), savedUserInfo.get("MTNOF_CD").toString());
//            jisaList.add(savedUserInfo.get("MTNOF_NM").toString());
//
//            codeMap.put("MTNOF_CD", savedUserInfo.get("MTNOF_CD").toString());//
//
//            jsonArray = new JSONArray(jisaJsonResult);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                jsonObject = jsonArray.getJSONObject(i);
//                if (!savedUserInfo.get("MTNOF_CD").toString().equals(jsonObject.get("MTNOF_CD").toString())) {
//                    jisaMap.put(jsonObject.get("MTNOF_NM").toString(), jsonObject.get("MTNOF_CD").toString());//첫번째 맵
//                    jisaList.add(jsonObject.get("MTNOF_NM").toString());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        itemKeyValueMap.put("jisaList", jisaMap);
//        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, jisaSpinner, jisaList/*, codeMap*/);
//        jisaTextView.setText(jisaSpinner.getSelectedItem().toString());
//        viewCollection.put("jisaView", new Object[]{jisaSpinner, jisaTextView, jisaList});
//
//
//        //노선
//        try {
//            jsonObject = new JSONObject();
//            jsonObject.accumulate("HDQR_CD", codeMap.get("HDQR_CD"));
//            jsonObject.accumulate("MTNOF_CD", codeMap.get("MTNOF_CD"));
//            jsonObject.accumulate("MTNOF_NM", jisaTextView.getText());
//            String nosunJsonResult = nullCheck(new Action("get", SERVER_URL+"/WorkPlan/nosunItem.do", jsonObject.toString(),this).execute().get());
//            // Log.i("nosunJsonResult", nosunJsonResult);
//
//            jsonArray = new JSONArray(nosunJsonResult);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                jsonObject = jsonArray.getJSONObject(i);
//                nosunMap.put(jsonObject.get("USE_ROTNM").toString(), jsonObject.get("ROUTE_CD").toString());
//                noSunList.add(jsonObject.get("USE_ROTNM").toString());
//            }
//            codeMap.put("ROUTE_CD", jsonArray.getJSONObject(0).get("ROUTE_CD").toString());//노선 키값 기본 셋팅
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        itemKeyValueMap.put("nosunList", nosunMap);
//
//        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, nosunSpinner, noSunList/*, codeMap*/);
//        nosunSpinner.setSelection(0);
//        noSunTextView.setText(nosunSpinner.getSelectedItem().toString());
//        viewCollection.put("nosunView", new Object[]{nosunSpinner, noSunTextView, noSunList});
//
//        //방향
//
//
//
//        try {
//            JSONObject jsonObject = new JSONObject(codeMap);
//
//            String directionresult =new Action("get",SERVER_URL+"/WorkPlan/getDirectionCityNm.do",jsonObject.toString(),this).execute("").get();
//            Log.i("방향 조회결과","----------------");
//            Log.i("방향 조회결과",URLDecoder.decode(directionresult,"UTF-8"));Log.i("방향 조회결과","----------------");Log.i("방향 조회결과","----------------");
//
//
//
//            Log.i("방향 조회결과","----------------");
//            Log.i("방향 조회결과","----------------");
//            Log.i("방향 조회결과","----------------");
//            Log.i("방향 조회결과","----------------");
//            Log.i("방향 조회결과","----------------");
//            directionMap.put("시점", "S");
//            directionMap.put("양방향", "O");
//            directionMap.put("종점", "E");
//            jsonArray = new JSONArray(directionresult);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                jsonObject = jsonArray.getJSONObject(i);
//                Map<String,String> nosunMap = new HashMap<>();
//                nosunMap.put("directionCityNm",jsonObject.get("directionCityNm").toString());
//                nosunMap.put("directionCityCd",jsonObject.get("directionCityCd").toString());
//              /*  nosunMap.put(jsonObject.get("USE_ROTNM").toString(), jsonObject.get("ROUTE_CD").toString());
//                noSunList.add(jsonObject.get("USE_ROTNM").toString());*/
//                directionList.add(nosunMap.get("directionCityNm").toString());;
//             /*   directionList.add("양방향");
//                directionList.add("시점");*/
//
//            }
//
//
//            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, direnctionSpinner, directionList/*, codeMap*/);
//            direnctionSpinner.setSelection(0);
//            directionTextView.setText(direnctionSpinner.getSelectedItem().toString());
//            codeMap.put("ROUTE_DRCT_CD", directionMap.get(directionTextView.getText()));
//            viewCollection.put("direction", new Object[]{direnctionSpinner, directionTextView, directionTextView});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //차로제한
//        try {
//
//
//            String charoJsonResult = nullCheck(new Action("get", SERVER_URL+"/WorkPlan/charo.do", codeMap.get("ROUTE_CD"),this).execute().get());
//            Log.i("charoJsonResult", charoJsonResult);
//            if (Integer.parseInt(charoJsonResult) <= 5) {
//                maxCharo = Integer.parseInt(charoJsonResult);
//                roadlimitTextView.setText("총 " + charoJsonResult + "차로 중 0차로 차단 ");
//                codeMap.put("TOT_CRGW_CNT", charoJsonResult);
//            } else {
//                maxCharo = 5;
//                roadlimitTextView.setText("총 " + 5 + "차로 중 0차로 차단 ");
//                codeMap.put("TOT_CRGW_CNT", "5");
//            }
////            for(int i = 0; i < maxCharo;i++){
////                ;
////                charoList.add(new Integer(i+1)+"차로");
////            }
////           charoList.add("진입램프");
////           charoList.add("진출램프");
////           charoList.add("교량하부");
////           charoList.add("법면");
////           charoList.add("회차로");
////           charoList.add("이동차단");
////           charoList.add("갓길");
////           new AdapterSpinnerUtil(this, R.layout.z_spinner_item, charoSpinner, charoList);//*, codeMap*//*);
//            viewCollection.put("charo", new Object[]{charoSpinner, roadlimitTextView, null});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //감독원
//        try {
//            jsonObject = new JSONObject();
//            jsonObject.accumulate("HDQR_CD", codeMap.get("HDQR_CD"));
//            jsonObject.accumulate("MTNOF_CD", codeMap.get("MTNOF_CD"));
//            jsonObject.accumulate("MTNOF_NM", jisaTextView.getText());
//            String gamdkJsonResult = nullCheck(new Action("get", SERVER_URL+"/WorkPlan/gamdokwon.do", jsonObject.toString(),this).execute().get());
//            Log.i("gamdkJsonResult", gamdkJsonResult);
//            //사번 받아오기
//
//            jsonArray = new JSONArray(gamdkJsonResult);
//            Log.i("감독원 이름", jsonArray.getJSONObject(0).toString());
//            for (int i = 0; i < jsonArray.length(); i++) {
//                jsonObject = jsonArray.getJSONObject(i);
//                gamdocList.add(jsonObject.get("EMNM").toString());
//            }
//
//            codeMap.put("MTNOF_PRCH_EMNO", jsonArray.getJSONObject(0).get("USER_EMNO").toString());//지사 담당자 사원번호 현재는 전화번호로 대체하여 테스트...
//            //  codeMap.put("ROUTE_DRCT_CD", jsonArray.getJSONObject(0).get("ROUTE_CD").toString());//노선 키값 기본 셋팅
//            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, gamdokSpinner, gamdocList/*, codeMap*/);
//            gamdokSpinner.setSelection(0);
//            gamdokwonTextView.setText(gamdokSpinner.getSelectedItem().toString());
//            viewCollection.put("gamdokwon", new Object[]{gamdokSpinner, gamdokwonTextView, gamdocList});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        //인원 및 장비
//        inwonJangbiList.add("선택");
//        inwonJangbiList.add("작업원");
//        inwonJangbiList.add("사인카");
//        inwonJangbiList.add("작업차");
//        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, inwonJangbiSpinner, inwonJangbiList/*, codeMap*/);
//        inwonJangbiSpinner.setSelection(0);
//        inwonJangbiTextView.setText(inwonJangbiSpinner.getSelectedItem().toString());
//        viewCollection.put("inwon", new Object[]{inwonJangbiSpinner, inwonJangbiTextView, inwonJangbiList, inwonJangbiResultTextView, jangbiList});
//        Log.i("결과 코드맵", codeMap.toString());
//
//
////-------------------------------------------------------------------------------------------------------------------------------------
//        endDateTextView.setText(calStr()[0]);
//
//        startMinuteTextView.setText(calStr()[2]);
//        endHourTextView.setText(calStr()[1]);
//        endMinuteTextView.setText(calStr()[2]);
//
//        //시작 날짜 셋팅
//        startDateTextView.setText(calStr()[0]);
//
//        //시작 시간
//        startHourList.add(calStr()[1]);
//        startHourTextView.setText(calStr()[1]);//최초 현재 시간 설정
//        for (int i = 0; i <= 23; i++) {
//            startHourList.add(Integer.toString(i));
//        }
//        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startHourSpinner, startHourList);
//        viewCollection.put("startHour", new Object[]{startHourSpinner, startHourTextView, startHourList});
//        //종료 날짜 셋팅
//        endHourList.add(calStr()[1]);
//        endHourTextView.setText(calStr()[1]);
//        for (int i = 0; i <= 23; i++) {
//            endHourList.add(Integer.toString(i));
//        }
//        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endHourSpinner, endHourList);
//        viewCollection.put("endHour", new Object[]{endHourSpinner, endHourTextView, endHourList});
//        //시작 분 셋팅
//        startMinuteTextView.setText(calStr()[2]);
//        startMinuteList.add(calStr()[2]);
//        for (int i = 0; i <= 59; i++) {
//            startMinuteList.add(Integer.toString(i));
//        }
//        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startMinuteSpinner, startMinuteList);
//        viewCollection.put("startMinute", new Object[]{startMinuteSpinner, startMinuteTextView, startMinuteList});
//        //종료 분 셋팅
//        endMinuteTextView.setText(calStr()[2]);
//        endMinuteList.add(calStr()[2]);
//        for (int i = 0; i <= 59; i++) {
//            endMinuteList.add(Integer.toString(i));
//        }
//        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endMinuteSpinner, endMinuteList);
//        viewCollection.put("endMinute", new Object[]{endMinuteSpinner, endMinuteTextView, endMinuteList});
//
//
//        //작업유형
//        try {
//            String workTypeJsonResult = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL+"/WorkPlan/workType.do", null,this).execute().get()), "UTF-8");
//
//            Log.e("workTypeJsonResult", workTypeJsonResult);
//            JSONArray workTypeJsonArray = new JSONArray(workTypeJsonResult);
//
//            for (int i = 0; i < workTypeJsonArray.length(); i++) {
//                jsonObject = workTypeJsonArray.getJSONObject(i);
//                workTypeMap = new HashMap<String, String>();
//                workTypeMap.put("cmmnCd", jsonObject.get("cmmnCd").toString());
//                workTypeMap.put("cmmnCdNm", jsonObject.get("cmmnCdNm").toString());
//
//                workTypeMapList.add(workTypeMap);
//                workTypeList.add(jsonObject.get("cmmnCdNm").toString());
//
//
//            }
//            codeMap.put("cmmnCd", workTypeMapList.get(0).get("cmmnCd"));
//            codeMap.put("cmmnCdNm", workTypeMapList.get(0).get("cmmnCdNm"));
//            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeSpinner, workTypeList);
//            workTypeSpinner.setSelection(0);
//            workTypeTextView.setText(workTypeSpinner.getSelectedItem().toString());
//            viewCollection.put("workType", new Object[]{workTypeSpinner, workTypeTextView, workTypeMapList});
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        //구간
//        try {
//            jsonObject = new JSONObject(codeMap);
//            String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL+"/WorkPlan/guganIjung.do", jsonObject.toString(),this).execute().get()), "UTF-8");
//            Log.v("guganMinMaxValue", guganMinMaxValue);
//            jsonObject = new JSONObject(guganMinMaxValue);
//            ET_startGongsaGugan.setText(jsonObject.get("minMtnofStpntDstnc").toString());
//            ET_endGongsaGugan.setText(jsonObject.get("maxMtnofEdpntDstnc").toString());
//
//            startLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("minMtnofStpntDstnc").toString()));
//            endLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("maxMtnofEdpntDstnc").toString()));
//            startLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_startGongsaGugan.getText().toString()));
//            endLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_endGongsaGugan.getText().toString()));
//
//
//            viewCollection.put("gugan", new Object[]{ET_startGongsaGugan, ET_endGongsaGugan});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //-4276546]
//
//
//        ET_startGongsaGugan.setTextColor(-4276546);
//        ET_endGongsaGugan.setTextColor(-4276546);
//        ET_gongsaContent.setTextColor(-4276546);
//        ET_gosundaeNum.setTextColor(-4276546);
//        ET_gosundaeNum.setInputType(InputType.TYPE_CLASS_NUMBER);
//        //  ET_gosundaeNum.setFilters(new InputFilter[5]);
//
//
//    }//onCreate
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("onResult map===========", codeMap.toString());
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        switch (v.getId()) {
//            case R.id.gongsaPrev:
//                codeMap=null;
//                finish();
//                break;
//            case R.id.bonbuTextView:  //본부 클릭시 작동
///*            case R.id.ll_bonbuSpinner: //본부 클릭시 작동
//            case R.id.bonbuButton:     //본부 클릭시 작동*/
//                bonbuSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, bonbuSpinner, bonbuList,/* codeMap,*/itemKeyValueMap, viewCollection);
//                Log.i("본부버튼", codeMap.toString());
//                break;
//            case R.id.jisaTextView:
//                jisaSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, jisaSpinner, jisaList,/* codeMap,*/itemKeyValueMap, viewCollection);
//                Log.i("지사버튼", codeMap.toString());
//                break;
//            case R.id.noSunTextView:
//                nosunSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, nosunSpinner, noSunList, itemKeyValueMap, viewCollection);
//                Log.i("노선버튼", codeMap.toString());
//
//                break;
//            case R.id.directionTextView:
//                direnctionSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, direnctionSpinner, directionList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                codeMap.put("ROUTE_DRCT_CD", directionMap.get(directionTextView.getText()));
//                break;
//            /*   case R.id.roadlimitTextView://차로제한
//             *//*maxCharoSelected();수정*//*
//                break;*/
//            case R.id.gamdokwonTextView:
//                gamdokSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, gamdokSpinner, gamdocList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                Log.i("감독원버튼", codeMap.toString());
//                break;
//
//            case R.id.inwonJangbiTextView:
//                inwonJangbiSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, inwonJangbiSpinner, inwonJangbiList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                break;
//            case R.id.li_ResetBtn:
//                Intent intent = getIntent();
//                overridePendingTransition(0, 0);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(intent);
//                break;
//            case R.id.startHourTextView:
//                startHourSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startHourSpinner, startHourList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                break;
//            case R.id.endHourTextView:
//                endHourSpinner.performClick();
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endHourSpinner, endHourList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                break;
//            case R.id.startMinuteTextView:
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startMinuteSpinner, startMinuteList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                startMinuteSpinner.performClick();
//                break;
//            case R.id.endMinuteTextView:
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endMinuteSpinner, endMinuteList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                endMinuteSpinner.performClick();
//                break;
//            case R.id.startDateImageView:
//                datePickerMethod(startDateTextView);
//                break;
//            case R.id.endDateImageView:
//                datePickerMethod(endDateTextView);
//                break;
//            case R.id.li_registerBtn:
//                startLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_startGongsaGugan.getText().toString()));
//                endLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_endGongsaGugan.getText().toString()));
//
//                try {
//                    if ((Boolean) checkingValue(startLimitGugan, endLimitGugan, startLimitEtValue, endLimitEtValue) == false) {
//                        //System.out.println("시작구간은 "+ ((Boolean)checkingValue((Double)(Object)codeMap.get("minMtnofStpntDstnc")+" 보다 크거나 같고 " + ((Double)(Object)ET_startGongsaGugan.getText().toString())+ " 보다 작거나 같아야합니다.");
//                        Toast.makeText(getApplicationContext(), "공사구간은" + startLimitGugan + " 사이에서 " + endLimitGugan + "에 위치해야 합니다.", Toast.LENGTH_LONG).show();
//                    } else if (ET_gosundaeNum.getText().equals(null) || ET_gosundaeNum.getText().toString().length() == 0) {
//                        Toast.makeText(getApplicationContext(), "고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
//                    } else if (ET_gosundaeNum.getText().length() != 15) {
//                        Toast.makeText(getApplicationContext(), "고순대 번호 15자리를 모두 입력해주세요", Toast.LENGTH_LONG).show();
//                    } else if (inwonJangbiResultTextView.getText().equals(null) || inwonJangbiResultTextView.getText().toString().length() == 0) {
//                        Toast.makeText(getApplicationContext(), "공사 인원과 사용 장비를 입력해주세요.", Toast.LENGTH_LONG).show();
//                    } else if (calCompareCheck(startDateTextView.getText().toString(), startHourTextView.getText().toString(), startMinuteTextView.getText().toString(), endDateTextView.getText().toString(), endHourTextView.getText().toString(), endMinuteTextView.getText().toString()) == false) {
//                        Toast.makeText(getApplicationContext(), "끝난시간은 시작시간보다 늦은 시간에 있어야 합니다.", Toast.LENGTH_LONG).show();
//                    } else if (codeMap.get("roadLimitResult").equals(null) || codeMap.get("roadLimitResult").equals("")) {
//                        Toast.makeText(getApplicationContext(), "차로 제한을 선택하여주세요.", Toast.LENGTH_LONG).show();
//                    } else {
//                        codeMap.put("CNSTN_CTNT", ET_gongsaContent.getText().toString());//공사내용
//                        //codeMap.put("SECT_CD",ET_startGongsaGugan.getText().toString());//구간코드
//                        // codeMap.put("EDPNT_SECT_CD",ET_endGongsaGugan.getText().toString());//종료구간*/
//                        codeMap.put("GOSUNDAE_NO", gosunDaeNoParsing(ET_gosundaeNum.getText().toString()));
//
//                        codeMap.put("LIMT_PLAN_DATES", getCurDate());
//                        codeMap.put("minMtnofStpntDstnc", ET_startGongsaGugan.getText().toString());
//                        codeMap.put("maxMtnofEdpntDstnc", ET_endGongsaGugan.getText().toString());
//                        codeMap.put("HDQR_NM", bonbuTextView.getText().toString());
//                        codeMap.put("gongsaContent", "모바일 사용자가 보낸 작업계획:" + ET_gongsaContent.getText().toString());
//                        codeMap.put("startWorkTime", startDateTextView.getText().toString().replaceAll("/", "") + dateParsing(startHourTextView.getText().toString()) + dateParsing(startMinuteTextView.getText().toString()));
//                        codeMap.put("endWorkTime", endDateTextView.getText().toString().replace("/", "") + dateParsing(endHourTextView.getText().toString()) + dateParsing(endMinuteTextView.getText().toString()));
//                        codeMap.put("inwonJangbi", inwonJangbiResultTextView.getText().toString());
//                        Log.i("등록버튼", WorkPlanRegisterActivity_bak.codeMap.toString());
//
//                        String parameter = jsonParameterParsing(WorkPlanRegisterActivity_bak.codeMap);
//                        jsonObject = new JSONObject(codeMap);
//                        JSONObject jsonNameValue = new JSONObject();
//                        jsonNameValue.put("bonbu", bonbuTextView.getText().toString());
//                        jsonNameValue.put("jisa", jisaTextView.getText().toString());
//                        jsonNameValue.put("nosun", noSunTextView.getText().toString());
//                        jsonNameValue.put("direction", directionTextView.getText().toString());
//                        jsonNameValue.put("gamdokTv", gamdokwonTextView.getText().toString());
//                        jsonNameValue.put("gongsaContent", ET_gongsaContent.getText().toString());
//                        jsonNameValue.put("roadlimit", roadlimitTextView.getText().toString());
//                        jsonNameValue.put("startTime", startDateTextView.getText().toString() + " " + startHourTextView.getText().toString() + ":" + startMinuteTextView.getText().toString());
//                        jsonNameValue.put("endTime", endDateTextView.getText().toString() + " " + endHourTextView.getText().toString() + ":" + endMinuteTextView.getText().toString());
//                        jsonNameValue.put("startGugan", ET_startGongsaGugan.getText().toString());
//                        jsonNameValue.put("endGugan", ET_endGongsaGugan.getText().toString());
//                        jsonNameValue.put("inwonjangbi", inwonJangbiResultTextView.getText().toString());
//                        jsonNameValue.put("gosundaeNum", gosunDaeNoParsing(ET_gosundaeNum.getText().toString()));
//                        jsonNameValue.put("workType", workTypeTextView.getText().toString());
//                        //  Log.e("체크",codeMap.get("blcCtnt").toString());
//                        new CustomDialog(WorkPlanRegisterActivity_bak.this, R.layout.z_custom_dialog, R.id.sendApprovalRequest, jsonNameValue, jsonObject);
//                        // new Action("get","http://192.168.1.9:8080/WorkPlan/register.do",jsonObject.toString()).execute();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.workTypeTextView:
//                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeSpinner, workTypeList, /*codeMap,*/itemKeyValueMap, viewCollection);
//                workTypeSpinner.performClick();
//                break;
//            case R.id.roadlimitTextView:
//                //   new AdapterSpinnerUtil(this, R.layout.z_spinner_item, charoSpinner, charoList, /*codeMap,*/itemKeyValueMap,viewCollection);
//                maxCharoSelected();
//                /* charoSpinner.performClick();*/
//                break;
//
//
//        }
//    }
//
//    @Override
//    public String onActionPost(String primitive, String date) {
//        return date;
//    }
//
//
//    public boolean calCompareCheck(String firdate, String firhour, String firminute, String date, String hour, String minute) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        Date d = sdf.parse(firdate + " " + firhour + ":" + firminute);
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        Date d1 = sdf1.parse(date + " " + hour + ":" + minute);
//        Log.i("d", d.toString());
//        Log.i("d1", d1.toString());
//        if (d.before(d1) == false) {
//            return false;
//        } else {
//            return true;
//        }
//
//    }
//
//
//    public void maxCharoSelected() {
//        if (roadlimitTextView.getText().length() > 0 && (maxCharo != null && maxCharo != 0)) {
//
//            // final String[] items = new String[]{"1차로", "2차로", "3차로", "4차로", "5차로", "6차로", "이동 차단", "갓길", "진입램프", "진출램프", "진출램프"};
//            final String chaDanContent = "";
//            final String[] items = new String[maxCharo + 7];
//            for (int i = 0; i < maxCharo; i++) {
//                //     items[0]=Integer.toString(i+1)+"차로";
//                items[i] = i + 1 + "차로";
//            }
//            items[maxCharo] = "진입램프";
//            items[maxCharo + 1] = "진출램프";
//            items[maxCharo + 2] = "교량하부";
//            items[maxCharo + 3] = "법면";
//            items[maxCharo + 4] = "회차로";
//            items[maxCharo + 5] = "이동차단";
//            items[maxCharo + 6] = "갓길";
//
//            final StringBuffer sb = new StringBuffer();
//            final boolean[] itemsBool = new boolean[maxCharo + 7];
//            roadLimit.clear();
//            totalroadLimit.clear();
//            AlertDialog.Builder dialog = new AlertDialog.Builder(WorkPlanRegisterActivity_bak.this);
//
//            dialog.setTitle("차단 차로를 선택하여주세요")
//                    .setMultiChoiceItems(
//                            items
//
//                            , itemsBool
//                            , new DialogInterface.OnMultiChoiceClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                                    //
//
//                                    if (isChecked) {
//                                        //이동차단 여부 체크
//
//
//                                        if (!items[which].contains("진입램프") && !items[which].contains("진출램프") && !items[which].contains("교량하부") && !items[which].contains("법면") && !items[which].contains("회차로") && !items[which].contains("이동차단") && !items[which].contains("갓길")) {
//                                            roadLimit.add(items[which]);
//                                        }
//                                        totalroadLimit.add(items[which]);
//                                    } else {
//
//                                        roadLimit.remove(items[which]);
//                                        totalroadLimit.remove(items[which]);
//                                    }
//                                }
//                            }
//                    ).setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String roadListResult = "";
//
//
//                    Map<String, String> loadLimitMap = new HashMap<String, String>();
//                    loadLimitMap.put("1차로", "01");
//                    loadLimitMap.put("2차로", "02");
//                    loadLimitMap.put("3차로", "03");
//                    loadLimitMap.put("4차로", "04");
//                    loadLimitMap.put("5차로", "05");
//                    loadLimitMap.put("진입램프", "07");
//                    loadLimitMap.put("진출램프", "08");
//                    loadLimitMap.put("교량하부", "09");
//                    loadLimitMap.put("법면", "10");
//                    loadLimitMap.put("회차로", "11");
//                    loadLimitMap.put("이동차단", "20");
//                    loadLimitMap.put("갓길", "30");
//                /*    if(loadLimitMap.get("이동차단")!=null){
//                        codeMap.put("MVBL_BLC_YN","Y");
//                    }*/
//               /*      if (!items[which].contains("이동차단")) {
//                        codeMap.put("MVBL_BLC_YN", "N");//이동차단여부
//                     } else {
//                        codeMap.put("MVBL_BLC_YN", "Y");//이동차단여부
//                     }*/
//                    for (int i = 0; i < totalroadLimit.size(); i++) {
//                        if (i != totalroadLimit.size() - 1) {
//                            roadListResult += loadLimitMap.get(totalroadLimit.get(i)) + ",";
//                        } else {
//                            roadListResult += loadLimitMap.get(totalroadLimit.get(i));
//                        }
//
//                        if(totalroadLimit.get(i).equals("이동차단")){
//                            codeMap.put("MVBL_BLC_YN", "Y");//이동차단여
//                        }
//                    }
//                    codeMap.put("roadLimitResult", roadListResult);
//
//
//                    //TRFC_LIMT_CTGW_CLSS_CD
//                    Log.i("로드 Resulst", roadListResult);
//                    jsonArray = new JSONArray();
//                    //sb.append(", "+loadLimitMap.get(items[which]));
//                    Log.i("sb 확인", sb.toString());
//              /*      try {
//                        for (int k = 0; k < roadLimit.size(); k++) {
//                            jsonObject = new JSONObject();
//                            jsonArray.put(k, jsonObject.put("TRFC_LIMT_CRGW_CLSS_CD", loadLimitMap.get(totalroadLimit.get(k))));
//                        }
//                        codeMap.put("blcCtnt", jsonArray.toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//*/
//
//                    roadlimitTextView.clearComposingText();
//                    roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + roadLimit.size() + "차로 차단");
//                }
//            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            dialog.create();
//            dialog.show();
//
//        }
//    }
//
//
//    /**
//     * 최초 접속시 달력의 시간을 익일로 초기화 시켜주는 메소드
//     */
//    final public String[] calStr() {
//        final Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, 1);
//        int month = cal.get(Calendar.MONTH) + 1;
//        String montthStr = Integer.toString(month);
//        if (montthStr.length() == 1) montthStr = "0" + montthStr;
//        String dateStr = Integer.toString(cal.get(Calendar.DATE));
//        if (dateStr.length() == 1) dateStr = "0" + dateStr;
//        String yearMon = cal.get(Calendar.YEAR) + "/" + montthStr + "/" + dateStr;
//        String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
//        String min = Integer.toString(cal.get(Calendar.MINUTE));
//        String[] calArray = {"", "", ""};
//        if (hour.length() == 1) hour = "0" + hour;
//        if (min.length() == 1) min = "0" + min;
//        calArray[0] = yearMon;
//        calArray[1] = hour;
//        calArray[2] = min;
//        return calArray;
//    }
//
//    public void datePickerMethod(final TextView text) {
//        Log.i("datePickerMethod", "요기");
//        Calendar cal = Calendar.getInstance();
//        DatePickerDialog datePicker = new DatePickerDialog(WorkPlanRegisterActivity_bak.this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
//
//                try {
//                    /*String yearMonth = String.format("%d/%d/%d", year, month + 1, date);*/
//                    String yearResult = Integer.toString(year);
//                    String monthResult = Integer.toString(month + 1);
//                    String dateResult = Integer.toString(date);
//                    if (monthResult.length() == 1)
//                        monthResult = "0" + monthResult;
//
//                    if (dateResult.length() == 1)
//                        dateResult = "0" + dateResult;
//
//                    // String yearMonth = String.format("%d/%d/%d", year, monthResult , dateResult);
//                    String yearMonth = year + "/" + monthResult + "/" + dateResult;
//                    text.clearComposingText();
//                    text.setText(yearMonth);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//        datePicker.show();
//    }
//
//    public String jsonParameterParsing(Map<String, String> codeMap) {
//        Set set = codeMap.keySet();
//        Iterator iterator = set.iterator();
//        try {
//
//            JSONObject jsonObject = new JSONObject();
//            while (iterator.hasNext()) {
//                String key = (String) iterator.next();
//                jsonObject.accumulate(key, codeMap.get(key));
//            }
//
//            return jsonObject.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String getCurDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date curTime = new Date();
//        return sdf.format(curTime);
//    }
//
//
//    /**
//     * 사용자가 입력한 공사구간이 지정된 공사구간 안에 위치하는지를 체크하는 메소드(공사 구간 Validation 체크)
//     *
//     * @param oriStartGugan : 초기 셋팅된 시작구간
//     * @param oriEndGugan : 초기 셋팅된 종료구간
//     * @param startGuganEtInputValue : 사용자가 입력한 시작구간
//     * @param endGuganEtInputValue   : 사용자가 입력한 종료구간
//     * @return
//     */
//    public boolean checkingValue(double oriStartGugan, double oriEndGugan, double startGuganEtInputValue, double endGuganEtInputValue) {
//        try {
//            if ((startGuganEtInputValue >= oriStartGugan && startGuganEtInputValue <= oriEndGugan) && (endGuganEtInputValue >= oriStartGugan) && (endGuganEtInputValue <= oriEndGugan)) {
//                if (startGuganEtInputValue <= endGuganEtInputValue) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 고속도로 순찰대 번호 파싱
//     * @param gosundaNo
//     * @return
//     */
//    public static String gosunDaeNoParsing(String gosundaNo) {
//        try {
//            gosundaNo = gosundaNo.substring(0, 4) + "-" + gosundaNo.substring(4, 8) + "-" + gosundaNo.substring(8, gosundaNo.length());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        /*while(gosundaNo.length())*/
//
//        return gosundaNo;
//    }
//
//    public String dateParsing(String parsing) {
//        if (parsing.length() == 1)
//            parsing = "0" + parsing;
//        return parsing;
//    }
//
//}
