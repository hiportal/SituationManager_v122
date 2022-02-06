package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Common.intStrNullCheck;
import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;
import static com.ex.gongsa.view.AdapterSpinnerUtil.maxCharo;

////작업계획등록
public class WorkPlanRegisterActivity extends BaseActivity implements OnClickListener {

    final String TAG = "WorkPlanRegisterActivity";
    ImageView gongsaPrev;//화면 좌측 상단 뒤로가기 이미지뷰
    ImageView startDateImageView;//시작 날짜 이미지
    ImageView endDateImageView;
    ImageView plus_btn;
    //ImageView minus_btn;

    /*View view_gosundae_no1;
    View view_gosundae_no2;
    View view_gosundae_no3;
    View view_gosundae_no4;
*/
    Intent intent;//인텐트(메인화면으로부터 사용자 정보를 받아왔다)

    Spinner bonbuSpinner;//숨겨진 본부 스피너
    Spinner jisaSpinner;//지사 스피너
    Spinner nosunSpinner;//노선 스피너
    Spinner direnctionSpinner;//방향 스피너
    Spinner gamdokSpinner;//감독원 스피너
    Spinner inwonJangbiSpinner;
    Spinner startHourSpinner;//시작 시간 스피너
    Spinner endHourSpinner;//종료 시간 스피너
    Spinner startMinuteSpinner;
    Spinner endMinuteSpinner;//종로 분 스피너
    Spinner workTypeSpinner;
    Spinner charoSpinner;
    Spinner workTypeCATSpinner; //2021.07 작업유형(대분류)

    TextView bonbuTextView;//본부 스피너 위 TextView
    TextView jisaTextView;//지사 텍스트뷰
    TextView noSunTextView;//노선 텍스트뷰
    TextView directionTextView;//방향 텍스트뷰
    TextView roadlimitTextView;//차로제한 텍스트뷰
    TextView gamdokwonTextView;//감독원 텍스트뷰
    TextView inwonJangbiTextView;//인원 장비 텍스트뷰(우측)
    TextView inwonJangbiResultTextView;//인원 장비 최종 결과값 텍스트뷰(좌측)
    TextView startDateTextView;//시작 시간 텍스트뷰
    TextView endDateTextView;//종료 날짜 텍스트뷰
    TextView startHourTextView;//시작 시간 텍스트뷰
    TextView startMinuteTextView;//시작 분 텍스트뷰
    TextView endHourTextView;//종료 시간 텍스트뷰
    TextView endMinuteTextView;//종료 분 텍스트뷰
    TextView workTypeTextView;//작업유형 텍스트뷰
    TextView workTypeCATTextView; //2021.07 작업유형(대분류)
   /* TextView gosundatTextView_no1;
    TextView gosundatTextView_no2;*/

    EditText ET_gongsaContent;
    EditText ET_startGongsaGugan;
    EditText ET_endGongsaGugan;
   /* EditText ET_gosundaeNum;
    EditText ET_gosundaeNum_no1;
    EditText ET_gosundaeNum_no2;
    EditText ET_gosundaeNum_no3;
    EditText ET_gosundaeNum_no4;*/

    Button bonbuButton;//본부 버튼
    Button jisaButton;//지사 버튼
    Button nosunButton;//노선 버튼
    Button directionButton;//방향 버튼
    Button buttoninwonjangbi;//작업유형 버튼

    LinearLayout ll_bonbuSpinner;//본부 레이아웃
    LinearLayout li_jisaSpinner;//지사 레이아수
    LinearLayout li_registerBtn;//등록버튼
    LinearLayout li_ResetBtn;
    LinearLayout gosundae_list;

/*    LinearLayout Li_gosundae_no1;
    LinearLayout Li_gosundae_no2;
    LinearLayout Li_gosundae_no3;
    LinearLayout Li_gosundae_no4;*/
    //초기화용 리스트

    List<String> jisaList;//지사리스트
    List<String> noSunList;//노선리스트
    List<String> gamdocList;//감독리스트
    List<String> directionList;//방향 리스트
    String[] roadLimit;//차로제한 리스트
    List<String> bonbuList;//본부리스트
    ArrayList<String> totalroadLimit;//차로제한 리스트에 대한 최종 결과값
    List<String> inwonJangbiList;
    List<String> jangbiList;//장비 리스트
    List<String> startHourList;//공사 시작 시간 리스트
    List<String> endHourList;//공사 종료 시간 리스트
    List<String> startMinuteList;//시작 분 셋팅
    List<String> endMinuteList;//종료 분 셋팅
    List<String> workTypeList;//작업유형 리스트
    List<Map<String, String>> workTypeMapList;
    List<String> charoList;
    //2021.07 작업유형 대분류
    List<String> workTypeCATList; //작업유형대분류 리스트 2021.07 작업유형(대분류)
    List<Map<String,String>> workTypeCATMapList;

    //결과 코드값을 담고있기위한 맵
    public static Map<String, String> codeMap;//서버로 전송시키는 각각의 파라미터를 셋팅하는 Map


    Map<String, Map<String, String>> itemKeyValueMap;
    Map<String, String> bonbuMap;//본부에 대한 키밸류를 담기위한 map
    Map<String, String> jisaMap;
    Map<String, String> nosunMap;
    Map<String, String> workTypeMap;
    Map<String, String> workTypeCATMap;    //2021.07 작업유형(대분류)
    //2021.07 작업유형(대분류)
    String workTypeCAT = "";
    String workTypeCATCd = "";


    Map<String, String> directionMap; //방향에 대한 코드를 담기위한 Map
    Map<String, Object[]> viewCollection;

    JSONObject jsonObject;
    JSONArray jsonArray;
    JSONObject savedUserInfo;//MainActivity 에서 전달받은 사용자 정보 저장소

   /* View gosundatTextView_view_no1;
    View gosundatTextView_view_no2;*/

    String gamdokTvResult = "";
    String gamdokTelno = "";
    String gamdokSawonNo = "";
    String gamdokList = "";
    static Double startLimitGugan = 0.0;
    static Double endLimitGugan = 0.0;
    static Double startLimitEtValue = 0.0;
    static Double endLimitEtValue = 0.0;
    public static String gamdokArrayItem = "";

    List<EditText> que_et_list = new ArrayList<EditText>();

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zz_work_plan_register);

        intent = getIntent();
        Log.i("intentResult", intent.getStringExtra("userInfo"));


        //Log.i("intentResult", intent.getStringExtra("userInfo"));
        try {
            savedUserInfo = new JSONObject(intent.getStringExtra("userInfo"));

            //테스트'N01046

        } catch (JSONException e) {
            Log.e("에러", "익셉션 발생");
        } catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }

        intent = getIntent();
//    //---

        codeMap = new HashMap<String, String>();//
        directionMap = new HashMap<String, String>();//
        viewCollection = new HashMap<String, Object[]>();
        bonbuMap = new HashMap<String, String>();
        jisaMap = new HashMap<String, String>();
        nosunMap = new HashMap<String, String>();
        Map<String, String> cityMap = new HashMap<>();
        ;

        itemKeyValueMap = new HashMap<String, Map<String, String>>();

        bonbuList = new ArrayList<String>();//본부리스트
        jisaList = new ArrayList<String>();//지사 리스트
        noSunList = new ArrayList<String>();//노선 리스트
        directionList = new ArrayList<String>();//방향 리스트
        gamdocList = new ArrayList<String>();//감독원 리스ㅡㅌ
        /*        roadLimit =new String[7];*/
        roadLimit = new String[8];
        totalroadLimit = new ArrayList<String>();
        inwonJangbiList = new ArrayList<String>();
        jangbiList = new ArrayList<String>();
        startHourList = new ArrayList<String>();//공사 시작 시간 리스트
        startMinuteList = new ArrayList<String>();//공사 시작 분 리스트
        endHourList = new ArrayList<String>();//종료 시간 설정
        endMinuteList = new ArrayList<String>();//종료 분 리스트
        workTypeList = new ArrayList<String>();
        workTypeMapList = new ArrayList<Map<String, String>>();
        charoList = new ArrayList<String>();
        //2021.07 작업유형(대분류)
        workTypeCATList = new ArrayList<String>();
        workTypeCATMapList = new ArrayList<Map<String,String>>();

      /*  gosundatTextView_view_no1 = (View)findViewById(R.id.gosundatTextView_view_no1);
        gosundatTextView_view_no2 = (View)findViewById(R.id.gosundatTextView_view_no2);*/

        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
        startDateImageView = (ImageView) findViewById(R.id.startDateImageView);
        endDateImageView = (ImageView) findViewById(R.id.endDateImageView);
        plus_btn = (ImageView) findViewById(R.id.plus_btn);//고순대 추가 버튼
        //  minus_btn = (ImageView)findViewById(R.id.minus_btn); //고순대 삭제버튼

/*        view_gosundae_no1 = (View)findViewById(R.id.view_gosundae_no1);
        view_gosundae_no2 = (View)findViewById(R.id.view_gosundae_no2);
        view_gosundae_no3 = (View)findViewById(R.id.view_gosundae_no3);
        view_gosundae_no4 = (View)findViewById(R.id.view_gosundae_no4);*/

        bonbuSpinner = (Spinner) findViewById(R.id.bonbuSpinner);
        jisaSpinner = (Spinner) findViewById(R.id.jisaSpinner);
        nosunSpinner = (Spinner) findViewById(R.id.nosunSpinner);
        direnctionSpinner = (Spinner) findViewById(R.id.direnctionSpinner);
        gamdokSpinner = (Spinner) findViewById(R.id.gamdokSpinner);
        inwonJangbiSpinner = (Spinner) findViewById(R.id.inwonJangbiSpinner);
        startHourSpinner = (Spinner) findViewById(R.id.startHourSpinner);
        endHourSpinner = (Spinner) findViewById(R.id.endHourSpinner);
        startMinuteSpinner = (Spinner) findViewById(R.id.startMinuteSpinner);
        endMinuteSpinner = (Spinner) findViewById(R.id.endMinuteSpinner);
        workTypeSpinner = (Spinner) findViewById(R.id.workTypeSpinner);
        charoSpinner = (Spinner) findViewById(R.id.charoSpinner);
        workTypeCATSpinner = (Spinner) findViewById(R.id.workTypeCATSpinner);   //2021.07 작업유형(대분류)

        ET_gongsaContent = (EditText) findViewById(R.id.ET_gongsaContent);
        ET_startGongsaGugan = (EditText) findViewById(R.id.ET_startGongsaGugan);
        ET_endGongsaGugan = (EditText) findViewById(R.id.ET_endGongsaGugan);
   /*     ET_gosundaeNum = (EditText) findViewById(R.id.ET_gosundaeNum);
        ET_gosundaeNum_no1 =(EditText)findViewById(R.id.ET_gosundaeNum_no1);
        ET_gosundaeNum_no2 = (EditText)findViewById(R.id.ET_gosundaeNum_no2);
        ET_gosundaeNum_no3 =(EditText)findViewById(R.id.ET_gosundaeNum_no3);
        ET_gosundaeNum_no4 = (EditText)findViewById(R.id.ET_gosundaeNum_no4);
*/
        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);
        jisaTextView = (TextView) findViewById(R.id.jisaTextView);
        noSunTextView = (TextView) findViewById(R.id.noSunTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        roadlimitTextView = (TextView) findViewById(R.id.roadlimitTextView);
        gamdokwonTextView = (TextView) findViewById(R.id.gamdokwonTextView);
        inwonJangbiTextView = (TextView) findViewById(R.id.inwonJangbiTextView);
        inwonJangbiResultTextView = (TextView) findViewById(R.id.inwonJangbiResultTextView);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);
        startHourTextView = (TextView) findViewById(R.id.startHourTextView);
        startMinuteTextView = (TextView) findViewById(R.id.startMinuteTextView);
        endHourTextView = (TextView) findViewById(R.id.endHourTextView);
        endMinuteTextView = (TextView) findViewById(R.id.endMinuteTextView);
        workTypeTextView = (TextView) findViewById(R.id.workTypeTextView); //작업유형 텍스트뷰
        workTypeCATTextView = (TextView) findViewById(R.id.workTypeCATTextView);    // 작업유형(대분류) 텍스트뷰 2021.07 작업유형(대분류)
       /* gosundatTextView_no1 = (TextView)findViewById(R.id.gosundatTextView_no1);
        gosundatTextView_no2 = (TextView)findViewById(R.id.gosundatTextView_no2);*/

        bonbuButton = (Button) findViewById(R.id.bonbuButton);
        jisaButton = (Button) findViewById(R.id.jisaButton);
        nosunButton = (Button) findViewById(R.id.nosunButton);
        directionButton = (Button) findViewById(R.id.directionButton);

        ll_bonbuSpinner = (LinearLayout) findViewById(R.id.ll_bonbuSpinner);
        li_jisaSpinner = (LinearLayout) findViewById(R.id.li_jisaSpinner);
        li_registerBtn = (LinearLayout) findViewById(R.id.li_registerBtn);
        li_ResetBtn = (LinearLayout) findViewById(R.id.li_ResetBtn);
        gosundae_list = (LinearLayout) findViewById(R.id.gosundae_list);
/*        Li_gosundae_no1 =(LinearLayout)findViewById(R.id.Li_gosundae_no1);
        Li_gosundae_no2 = (LinearLayout)findViewById(R.id.Li_gosundae_no2);
        Li_gosundae_no3 =(LinearLayout)findViewById(R.id.Li_gosundae_no3);
        Li_gosundae_no4 = (LinearLayout)findViewById(R.id.Li_gosundae_no4);*/
        /*gosundatLi_no1 = (LinearLayout)findViewById(R.id.gosundatLi_no1);
        gosundatLi_no2 = (LinearLayout)findViewById(R.id.gosundatLi_no2);
*/
        bonbuTextView.setOnClickListener(this);
        jisaTextView.setOnClickListener(this);
        gongsaPrev.setOnClickListener(this);
        bonbuButton.setOnClickListener(this);
        ll_bonbuSpinner.setOnClickListener(this);
        li_jisaSpinner.setOnClickListener(this);
        li_registerBtn.setOnClickListener(this);
        noSunTextView.setOnClickListener(this);
        nosunButton.setOnClickListener(this);
        jisaButton.setOnClickListener(this);
        directionTextView.setOnClickListener(this);
        directionButton.setOnClickListener(this);
        roadlimitTextView.setOnClickListener(this);
        gamdokwonTextView.setOnClickListener(this);
        inwonJangbiTextView.setOnClickListener(this);
        li_ResetBtn.setOnClickListener(this);
        startHourTextView.setOnClickListener(this);
        endHourTextView.setOnClickListener(this);//종료 시간 텍스트뷰
        startMinuteTextView.setOnClickListener(this);//시작 분 셋팅
        endMinuteTextView.setOnClickListener(this);
        startDateImageView.setOnClickListener(this);//시작 날짜 이미지
        endDateImageView.setOnClickListener(this);//종료 날짜 이미지
        workTypeTextView.setOnClickListener(this);
        workTypeCATTextView.setOnClickListener(this);   //2021.07 작업유형(대분류)
        plus_btn.setOnClickListener(this);//고순대 접수번호 +버튼
        //  minus_btn.setOnClickListener(this);


        try {
            Log.println(Log.ASSERT, "작업계획 조회 유저정보", savedUserInfo.toString());
            codeMap.put("HDQR_CD", savedUserInfo.get("HDQR_CD").toString());//본부 키값 기본 셋팅
            codeMap.put("MTNOF_CD", savedUserInfo.get("MTNOF_CD").toString());//지사 키값 기본 셋팅
            codeMap.put("HDQR_NM", savedUserInfo.get("HDQR_NM").toString());
            codeMap.put("MTNOF_NM", savedUserInfo.get("MTNOF_NM").toString());
            codeMap.put("ROUTE_CD", "");//노선 키값 기본 셋팅
            codeMap.put("ROUTE_DRCT_CD", "");//방향 키값 기본 셋팅
            codeMap.put("CRGW_LIST", "");//노선 키값 기본 셋팅
            // codeMap.put("RMRK_CTNT","");//비고내용 -->고순대 접수번호
            codeMap.put("CNSTN_CTNT", "");//공사내용
            codeMap.put("TOT_CRGW_CNT", "");//총차로수
            codeMap.put("gamdokList_in_bigo", "");
            //codeMap.put("SECT_CD","");//구간코드
            //codeMap.put("EDPNT_SECT_CD","");//종점 구간코드
            codeMap.put("CSTR_CRPR_PRCH_TELNO", savedUserInfo.get("TEL_NO").toString());
            codeMap.put("GONSUDAE_NO", "");
            codeMap.put("LIMT_PLAN_DATES", "");
            codeMap.put("MVBL_BLC_YN", "Y");//이동차단여부
            codeMap.put("TRFC_LIMT_CTGW_CLSS_CD", "");//
            codeMap.put("MTNOF_PRCH_EMNO", "");//

            Log.i("사용자 아이디", savedUserInfo.get("userId").toString());
            Log.i("사용자 아이디 F", savedUserInfo.get("userId").toString());
            Log.i("사용자 아이디 L", savedUserInfo.get("userId").toString());
            codeMap.put("FSTTM_RGSR_ID", savedUserInfo.get("userId").toString());
            codeMap.put("LSTTM_MODFR_ID", savedUserInfo.get("userId").toString());
            codeMap.put("USER_EMNO", "");
            codeMap.put("minMtnofStpntDstnc", "");
            codeMap.put("maxMtnofEdpntDstnc", "");
///*            codeMap.put("blcCtnt","");//차단내용
            codeMap.put("cmmnCdNm", "");//장비이름*/
            codeMap.put("cmmnCd", "");
            codeMap.put("humanCnt", "");//
            codeMap.put("janbiNm", "");//장비이름
            codeMap.put("janbiCd", "");//장비코드
            codeMap.put("roadLimitResult", "");
            codeMap.put("CSTR_CRPR_RCRD_CTNT", savedUserInfo.get("SMS_GRP_NM").toString());//소속 시공사
            codeMap.put("ROUTE_DRCT_CD", "");
            codeMap.put("MTNOF_PRCH_EMNO_TELNO", "");
            codeMap.put("MTNOF_PRCH_EMNO", "");//지사 담당자 사원번호 현재는 전화번호로 대체하여 테스트...
            codeMap.put("ROUTE_NM", "");
            codeMap.put("NOSUN_DIRECTION", "");
            //방향에 대한 키밸류 셋팅팅

        } catch (JSONException e) {
            Log.e("에러", "익셉션 발생");
        } catch (NullPointerException e) {
            Log.e("에러", "익셉션 발생");
        } catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }

        //본부
        try {

            String bonbuJsonResult = nullCheck(new Action("get", SERVER_URL + "/WorkPlan/bonbuItem.do", null, this).execute().get());
            Log.i("bonbuJsonResult", bonbuJsonResult);
            bonbuMap.put(savedUserInfo.get("HDQR_NM").toString(), savedUserInfo.get("HDQR_CD").toString());
            bonbuList.add(savedUserInfo.get("HDQR_NM").toString());

            codeMap.put("HDQR_CD", savedUserInfo.get("HDQR_CD").toString());//

            jsonArray = new JSONArray(bonbuJsonResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (!savedUserInfo.get("HDQR_CD").toString().equals(jsonObject.get("HDQR_CD").toString())) {
                    bonbuMap.put(jsonObject.get("HDQR_NM").toString(), jsonObject.get("HDQR_CD").toString());//첫번째 맵
                    bonbuList.add(jsonObject.get("HDQR_NM").toString());
                }
            }


            itemKeyValueMap.put("bonbuList", bonbuMap);//두번째 맵
            // resultCodeMap= AdapterSpinnerUtil(this, R.layout.z_spinner_item, bonbuSpinner, bonbuList, codeMap);//코드맵
            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, bonbuSpinner, bonbuList/*, codeMap*/);

            // bonbuSpinner.setSelection(0);
            bonbuTextView.setText(Common.nullCheck(bonbuSpinner.getSelectedItem().toString()));
            viewCollection.put("bonbuView", new Object[]{bonbuSpinner, bonbuTextView, bonbuList});
            Log.i("onCreate", "onCreate");
        } catch (JSONException e) {
            Log.e("에러", "익셉션 발생");
        }catch ( NullPointerException e) {
            Log.e("에러", "익셉션 발생");
        }catch (InterruptedException  e) {
            Log.e("에러", "익셉션 발생");
        }catch (ExecutionException e) {
            Log.e("에러", "익셉션 발생");
        }catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }
        //지사
        try {
            String jisaJsonResult = nullCheck(new Action("get", SERVER_URL + "/WorkPlan/jisaItem.do", codeMap.get("HDQR_CD"), this).execute().get());
            System.out.println("------------------------------- 순서 1111 -----------------------------------------");
            jisaMap.put(savedUserInfo.get("MTNOF_NM").toString(), savedUserInfo.get("MTNOF_CD").toString());
            jisaList.add(savedUserInfo.get("MTNOF_NM").toString());
            codeMap.put("MTNOF_CD", savedUserInfo.get("MTNOF_CD").toString());//


            jsonArray = new JSONArray(jisaJsonResult);


            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                if (!savedUserInfo.get("MTNOF_CD").toString().equals(jsonObject.get("MTNOF_CD").toString())) {
                    jisaMap.put(jsonObject.get("MTNOF_NM").toString(), jsonObject.get("MTNOF_CD").toString());//첫번째 맵
                    jisaList.add(jsonObject.get("MTNOF_NM").toString());

                }
            }
        } catch (NullPointerException e) {
            Log.e("에러", "익셉션 발생");
        }catch ( JSONException  e) {
            Log.e("에러", "익셉션 발생");
        }catch ( InterruptedException  e) {
            Log.e("에러", "익셉션 발생");
        }catch (ExecutionException e) {
            Log.e("에러", "익셉션 발생");
        }catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }
        itemKeyValueMap.put("jisaList", jisaMap);
        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, jisaSpinner, jisaList/*, codeMap*/);

        jisaTextView.setText(jisaSpinner.getSelectedItem().toString());
        viewCollection.put("jisaView", new Object[]{jisaSpinner, jisaTextView, jisaList});


        //노선
        try {
            jsonObject = new JSONObject();
            jsonObject.accumulate("HDQR_CD", codeMap.get("HDQR_CD"));
            jsonObject.accumulate("MTNOF_CD", codeMap.get("MTNOF_CD"));
            jsonObject.accumulate("MTNOF_NM", jisaTextView.getText());
            String nosunJsonResult = nullCheck(new Action("get", SERVER_URL + "/WorkPlan/nosunItem.do", jsonObject.toString(), this).execute().get());
            // Log.i("nosunJsonResult", nosunJsonResult);

            jsonArray = new JSONArray(nosunJsonResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                nosunMap.put(jsonObject.get("USE_ROTNM").toString(), jsonObject.get("ROUTE_CD").toString());
                noSunList.add(jsonObject.get("USE_ROTNM").toString());
            }
            codeMap.put("ROUTE_CD", jsonArray.getJSONObject(0).get("ROUTE_CD").toString());//노선 키값 기본 셋팅
        } catch (NullPointerException e) {
            Log.e("에러", "익셉션 발생");
        }catch ( JSONException e) {
            Log.e("에러", "익셉션 발생");
        }catch ( InterruptedException e) {
            Log.e("에러", "익셉션 발생");
        }catch (ExecutionException e) {
            Log.e("에러", "익셉션 발생");
        }catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }
        itemKeyValueMap.put("nosunList", nosunMap);

        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, nosunSpinner, noSunList/*, codeMap*/);

        noSunTextView.setText(nosunSpinner.getSelectedItem().toString());
        viewCollection.put("nosunView", new Object[]{nosunSpinner, noSunTextView, noSunList});

        //방향


        try {
            JSONObject jsonObject = new JSONObject(codeMap);

            String directionresult = new Action("get", SERVER_URL + "/WorkPlan/getDirectionCityNm.do", jsonObject.toString(), this).execute("").get();

           /* directionMap.put("시점", "S");
            directionMap.put("양방향", "O");
            directionMap.put("종점", "E");*/

            jsonArray = new JSONArray(directionresult);
            Log.i("다이렉션 결과", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                cityMap.put(jsonObject.get("directionCityNm").toString(), jsonObject.get("directionCityCd").toString());

              /*  nosunMap.put(jsonObject.get("USE_ROTNM").toString(), jsonObject.get("ROUTE_CD").toString());
                noSunList.add(jsonObject.get("USE_ROTNM").toString());*/
                directionList.add(jsonObject.get("directionCityNm").toString());

                ;
             /*   directionList.add("양방향");
                directionList.add("시점");*/

            }
            itemKeyValueMap.put("directCity", cityMap);
            Log.i("itemKeyValueMap도시", itemKeyValueMap.get("directCity").toString());
            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, direnctionSpinner, directionList/*, codeMap*/);
            try {
                direnctionSpinner.setSelection(0);
              /*  directionTextView.setText(direnctionSpinner.getSelectedItem().toString());
                direnctionSpinner.setSelection(0);*/
                directionTextView.setText(direnctionSpinner.getSelectedItem().toString());
                codeMap.put("ROUTE_DRCT_CD", jsonArray.getJSONObject(0).get("directionCityCd").toString());
            } catch (NullPointerException | JSONException e) {
                directionTextView.setText("");
                codeMap.put("ROUTE_DRCT_CD", "\n");
            }

            viewCollection.put("direction", new Object[]{direnctionSpinner, directionTextView, directionList});
        }catch (NullPointerException  e) {
            Log.e("에러", "익셉션 발생");
        }catch ( JSONException  e) {
            Log.e("에러", "익셉션 발생");
        }catch ( InterruptedException  e) {
            Log.e("에러", "익셉션 발생");
        }catch (ExecutionException e) {
            Log.e("에러", "익셉션 발생");
        }catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }

        //차로제한
        try {


         /*   String charoJsonResult = nullCheck(new Action("get", SERVER_URL + "/WorkPlan/charo.do", codeMap.get("ROUTE_CD"), this).execute().get());
            Log.i("charoJsonResult", charoJsonResult);
            if (Integer.parseInt(charoJsonResult) <= 6) {
                maxCharo = Integer.parseInt(charoJsonResult);
                roadlimitTextView.setText("총 " + charoJsonResult + "차로 중 0차로 차단 ");
                codeMap.put("TOT_CRGW_CNT", charoJsonResult);
            } else {

                roadlimitTextView.setText("총 " + 6 + "차로 중 0차로 차단 ");
                codeMap.put("TOT_CRGW_CNT", "6");
            }*/
            maxCharo = 0;
            //roadlimitTextView.setText("총 " + 0 + "차로 중 0차로 차단 ");
            roadlimitTextView.setText("현재 차단차로 없음");
            codeMap.put("TOT_CRGW_CNT", "0");
//            for(int i = 0; i < maxCharo;i++){
//                ;
//                charoList.add(new Integer(i+1)+"차로");
//            }
//           charoList.add("진입램프");
//           charoList.add("진출램프");
//           charoList.add("교량하부");
//           charoList.add("법면");
//           charoList.add("회차로");
//           charoList.add("이동차단");
//           charoList.add("갓길");
//           new AdapterSpinnerUtil(this, R.layout.z_spinner_item, charoSpinner, charoList);//*, codeMap*//*);
            viewCollection.put("charo", new Object[]{charoSpinner, roadlimitTextView, null});
        } catch (NullPointerException e) {
            Log.e("에러", "익셉션 발생");
        }catch (Exception e){
            Log.e("에러", "익셉션 발생");
        }

        //감독원
        try {
            jsonObject = new JSONObject();
            jsonObject.accumulate("HDQR_CD", codeMap.get("HDQR_CD"));
            jsonObject.accumulate("MTNOF_CD", codeMap.get("MTNOF_CD"));
            jsonObject.accumulate("MTNOF_NM", jisaTextView.getText());
            String gamdkJsonResult = nullCheck(new Action("get", SERVER_URL + "/WorkPlan/gamdokwon.do", jsonObject.toString(), this).execute().get());
            gamdokArrayItem = gamdkJsonResult;
            Log.i("gamdkJsonResult", gamdkJsonResult);
            //사번 받아오기

            jsonArray = new JSONArray(gamdkJsonResult);

            Log.i("감독원 이름", jsonArray.getJSONObject(0).toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                gamdocList.add(jsonObject.get("EMNM").toString());
                Log.i("감독원 이름", jsonArray.getJSONObject(i).toString());
                /*gamdocList.add("dd");*/
            }
            try {
                codeMap.put("MTNOF_PRCH_EMNO", jsonArray.getJSONObject(0).get("USER_EMNO").toString());//지사 담당자 사원번호 현재는 전화번호로 대체하여 테스트...
            } catch (JSONException e) {
                codeMap.put("MTNOF_PRCH_EMNO", "\n");//지사 담당자 사원번호 현재는 전화번호로 대체하여 테스트...
                Log.e("에러", "익셉션 발생");
            } catch (Exception e) {
                codeMap.put("MTNOF_PRCH_EMNO", "\n");//지사 담당자 사원번호 현재는 전화번호로 대체하여 테스트...
                Log.e("에러", "익셉션 발생");
            }


            String teno = "";
            String[] tenoArray = null;
            String reusltTelNo = "";
            try {
                tenoArray = jsonArray.getJSONObject(0).get("TEL_NO").toString().split("-");
                reusltTelNo = tenoArray[0] + tenoArray[1] + tenoArray[2];
            } catch (JSONException e) {
                reusltTelNo = jsonArray.getJSONObject(0).get("TEL_NO").toString();
            }catch (Exception e) {
                Log.e("에러",TAG +"줄: 629");
                try{
                    reusltTelNo = jsonArray.getJSONObject(0).get("TEL_NO").toString();
                }catch (NullPointerException e1){
                    Log.e("에러",TAG +"줄: 633");
                    reusltTelNo = "";
                }
            }
            codeMap.put("MTNOF_PRCH_EMNO_TELNO", reusltTelNo);

            // new AdapterSpinnerUtil(this, R.layout.z_spinner_item, gamdokSpinner, gamdocList/*, codeMap*/);
            //  gamdokSpinner.setSelection(0);


            //gamdokwonTextView.setText(gamdokSpinner.getSelectedItem().toString());

            gamdokwonTextView.setText(gamdocList.get(0).toString());
            viewCollection.put("gamdokwon", new Object[]{gamdokSpinner, gamdokwonTextView, gamdocList});
        } catch (StringIndexOutOfBoundsException  e) {
            gamdokwonTextView.setText("");
            Log.e("에러", "익셉션 발생123");
        } catch ( ArrayIndexOutOfBoundsException  e) {
            gamdocList = new ArrayList<String>();
            gamdokwonTextView.setText("");
            codeMap.put("MTNOF_PRCH_EMNO_TELNO", "");
            gamdocList.add("");
            viewCollection.put("gamdokwon", new Object[]{gamdokSpinner, gamdokwonTextView, gamdocList});
            Log.e("에러", "익셉션 발생1234");
        }catch ( JSONException  e) {
            try{
                viewCollection.put("gamdokwon", new Object[]{gamdokSpinner, gamdokwonTextView, gamdocList});
                gamdokwonTextView.setText(jsonArray.getJSONObject(0).get("EMNM").toString());
            }catch (JSONException e1){
                Log.e("에러","예외");
            }

            Log.e("에러", "익셉션 발생1235");
        }catch ( InterruptedException  e) {
            gamdokwonTextView.setText("");
            Log.e("에러", "익셉션 발생12367");
        }catch ( ExecutionException e) {
            gamdokwonTextView.setText("");
            Log.e("에러", "익셉션 발생745");
        }catch ( Exception e) {
            gamdokwonTextView.setText("");
            Log.e("에러", "익셉션 발생");
        }


        //인원 및 장비
        inwonJangbiList.add("선택");
        inwonJangbiList.add("작업원");
        inwonJangbiList.add("사인카");
        inwonJangbiList.add("작업차");
        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, inwonJangbiSpinner, inwonJangbiList/*, codeMap*/);
        inwonJangbiSpinner.setSelection(0);
        inwonJangbiTextView.setText(inwonJangbiSpinner.getSelectedItem().toString());
        viewCollection.put("inwon", new Object[]{inwonJangbiSpinner, inwonJangbiTextView, inwonJangbiList, inwonJangbiResultTextView, jangbiList});
        Log.i("결과 코드맵", codeMap.toString());


//-------------------------------------------------------------------------------------------------------------------------------------
        endDateTextView.setText(calStr()[0]);

        startMinuteTextView.setText("00");
        endHourTextView.setText("18");
        endMinuteTextView.setText("00");

        //시작 날짜 셋팅
        startDateTextView.setText(calStr()[0]);

        //시작 시간
        startHourList.add("09");
        startHourTextView.setText("09");//최초 현재 시간 설정
        for (int i = 0; i <= 23; i++) {
            startHourList.add(Integer.toString(i));
        }
        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startHourSpinner, startHourList);
        viewCollection.put("startHour", new Object[]{startHourSpinner, startHourTextView, startHourList});
        //종료 날짜 셋팅
        endHourList.add("18");
        endHourTextView.setText("18");
        for (int i = 0; i <= 23; i++) {
            endHourList.add(Integer.toString(i));
        }
        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endHourSpinner, endHourList);
        viewCollection.put("endHour", new Object[]{endHourSpinner, endHourTextView, endHourList});
        //시작 분 셋팅
        startMinuteTextView.setText("00");
        startMinuteList.add("00");
        for (int i = 0; i <= 59; i++) {
            startMinuteList.add(Integer.toString(i));
        }
        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startMinuteSpinner, startMinuteList);
        viewCollection.put("startMinute", new Object[]{startMinuteSpinner, startMinuteTextView, startMinuteList});
        //종료 분 셋팅
        endMinuteTextView.setText("00");
        endMinuteList.add("00");
        for (int i = 0; i <= 59; i++) {
            endMinuteList.add(Integer.toString(i));
        }
        new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endMinuteSpinner, endMinuteList);
        viewCollection.put("endMinute", new Object[]{endMinuteSpinner, endMinuteTextView, endMinuteList});

        //2021.07 작업유형(대분류)
        try{
            workTypeCATMap = new HashMap<String,String>();
            workTypeCATMap.put("기타","020");
            workTypeCATMap.put("노면보수작업","021");
            workTypeCATMap.put("조경작업","022");
            workTypeCATMap.put("터널내작업","023");
            workTypeCATMap.put("교량작업","024");
            workTypeCATMap.put("시설물설치","025");


            workTypeCATMapList.add(workTypeCATMap);
            workTypeCATList.add("기타");
            workTypeCATList.add("노면보수작업");
            workTypeCATList.add("조경작업");
            workTypeCATList.add("터널내작업");
            workTypeCATList.add("교량작업");
            workTypeCATList.add("시설물설치");



            itemKeyValueMap.put("workTypeCATList", workTypeCATMap);
            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeCATSpinner, workTypeCATList);
            workTypeCATSpinner.setSelection(0);
            workTypeCATTextView.setText(workTypeCATSpinner.getSelectedItem().toString());
            viewCollection.put("workTypeCATView", new Object[]{workTypeCATSpinner, workTypeCATTextView, workTypeCATMapList});
            System.out.println("-------------------- 작업등록 : " + workTypeCATList.toString());


        }catch (Exception e){

        }

        //작업유형
        try {
            //String workTypeJsonResult = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL + "/WorkPlan/workType.do", null, this).execute().get()), "UTF-8");
            String workTypeJsonResult = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL + "/WorkPlan/workTypeCode.do", workTypeCATMap.get("기타"), this).execute().get()), "UTF-8");

            Log.e("workTypeJsonResult", workTypeJsonResult);
            JSONArray workTypeJsonArray = new JSONArray(workTypeJsonResult);

            System.out.println("workTypeJsonArray ============= " + workTypeJsonArray.toString());

            for (int i = 0; i < workTypeJsonArray.length(); i++) {
                jsonObject = workTypeJsonArray.getJSONObject(i);
                workTypeMap = new HashMap<String, String>();

                //2021.07 작업유형(대분류) TEST
                /*workTypeMap.put("cmmnCd", jsonObject.get("cmmnCd").toString());
                workTypeMap.put("cmmnCdNm", jsonObject.get("cmmnCdNm").toString());*/
                workTypeMap.put("cmmnCd",jsonObject.get("cmmnCd").toString() );
                workTypeMap.put("cmmnCdNm",jsonObject.get("cmmnCdNm").toString() );

                workTypeMapList.add(workTypeMap);
                workTypeList.add(jsonObject.get("cmmnCdNm").toString());

            }
            System.out.println("workTypeMapList.get(0).get(cmmnCd------------------) " + workTypeMapList.get(0).get("cmmnCd"));
            codeMap.put("cmmnCd", workTypeMapList.get(0).get("cmmnCd"));
            codeMap.put("cmmnCdNm", workTypeMapList.get(0).get("cmmnCdNm"));
            new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeSpinner, workTypeList);
            workTypeSpinner.setSelection(0);
            workTypeTextView.setText(workTypeSpinner.getSelectedItem().toString());
            viewCollection.put("workType", new Object[]{workTypeSpinner, workTypeTextView, workTypeMapList});


        } catch (JSONException  e) {
            Log.e("에러", "익셉션 발생");
        } catch ( InterruptedException   e) {
            Log.e("에러", "익셉션 발생");
        } catch ( ExecutionException  e) {
            Log.e("에러", "익셉션 발생");
        } catch (   UnsupportedEncodingException e) {
            Log.e("에러", "익셉션 발생");
        } catch (  Exception e) {
            Log.e("에러", "익셉션 발생");
        }


        //구간
        try {
            jsonObject = new JSONObject(codeMap);
            System.out.println("jsonObject ======== ============ " + jsonObject.toString());
            String guganMinMaxValue = URLDecoder.decode(Common.nullCheck(new Action("get", SERVER_URL + "/WorkPlan/guganIjung.do", jsonObject.toString(), this).execute().get()), "UTF-8");
            Log.v("guganMinMaxValue", guganMinMaxValue);
            Log.i("구간 정보", guganMinMaxValue);
            jsonObject = new JSONObject(guganMinMaxValue);
            ET_startGongsaGugan.setText(jsonObject.get("minMtnofStpntDstnc").toString());
            ET_endGongsaGugan.setText(jsonObject.get("maxMtnofEdpntDstnc").toString());

            startLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("minMtnofStpntDstnc").toString()));
            endLimitGugan = Common.doubleNullCheck(Double.parseDouble(jsonObject.get("maxMtnofEdpntDstnc").toString()));
            startLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_startGongsaGugan.getText().toString()));
            endLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_endGongsaGugan.getText().toString()));


            viewCollection.put("gugan", new Object[]{ET_startGongsaGugan, ET_endGongsaGugan});
        } catch (UnsupportedEncodingException e) {
            Log.e("에러", "익셉션 발생");
        }catch ( JSONException  e) {
            Log.e("에러", "익셉션 발생");
        }catch (ExecutionException  e) {
            Log.e("에러", "익셉션 발생");
        }catch ( InterruptedException e) {
            Log.e("에러", "익셉션 발생");
        }catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }

        ET_startGongsaGugan.setTextColor(-4276546);
        ET_endGongsaGugan.setTextColor(-4276546);
        ET_gongsaContent.setTextColor(-4276546);
  /*      ET_gosundaeNum.setTextColor(-4276546);
        ET_gosundaeNum_no1.setTextColor(-4276546);
        ET_gosundaeNum_no2.setTextColor(-4276546);
        ET_gosundaeNum_no3.setTextColor(-4276546);
        ET_gosundaeNum_no4.setTextColor(-4276546);

        ET_gosundaeNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        ET_gosundaeNum_no1.setInputType(InputType.TYPE_CLASS_NUMBER);
        ET_gosundaeNum_no2.setInputType(InputType.TYPE_CLASS_NUMBER);
        ET_gosundaeNum_no3.setInputType(InputType.TYPE_CLASS_NUMBER);
        ET_gosundaeNum_no4.setInputType(InputType.TYPE_CLASS_NUMBER);*/
        //  ET_gosundaeNum.setFilters(new InputFilter[5]);

        roadLimit = new String[14];

        addView("");
    }//onCreate


    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResult map===========", codeMap.toString());
        System.out.println("==============onResult map=========== " + codeMap.toString());
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.gongsaPrev:
                codeMap = null;
                finish();
                break;
            case R.id.bonbuTextView:  //본부 클릭시 작동
/*            case R.id.ll_bonbuSpinner: //본부 클릭시 작동
            case R.id.bonbuButton:     //본부 클릭시 작동*/
                bonbuSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, bonbuSpinner, bonbuList,/* codeMap,*/itemKeyValueMap, viewCollection);
                Log.i("본부버튼", codeMap.toString());
                break;
            case R.id.jisaTextView:
                jisaSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, jisaSpinner, jisaList,/* codeMap,*/itemKeyValueMap, viewCollection);
                Log.i("지사버튼", codeMap.toString());
                break;
            case R.id.noSunTextView:
                nosunSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, nosunSpinner, noSunList, itemKeyValueMap, viewCollection);
                Log.i("노선버튼", codeMap.toString());

                break;
            case R.id.directionTextView:
                direnctionSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, direnctionSpinner, directionList, /*codeMap,*/itemKeyValueMap, viewCollection);
                codeMap.put("ROUTE_DRCT_CD", directionMap.get(directionTextView.getText()));
                break;
            /*   case R.id.roadlimitTextView://차로제한
             *//*maxCharoSelected();수정*//*
                break;*/
            case R.id.gamdokwonTextView:

                //  new AdapterSpinnerUtil(this, R.layout.z_spinner_item, gamdokSpinner, gamdocList, /*codeMap,*/itemKeyValueMap, viewCollection);
                gamokAlertDialog(gamdokArrayItem, viewCollection, gamdokwonTextView);
                Log.i("감독원버튼", codeMap.toString());
                break;

            case R.id.inwonJangbiTextView:
                inwonJangbiSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, inwonJangbiSpinner, inwonJangbiList, /*codeMap,*/itemKeyValueMap, viewCollection);
                break;
            case R.id.li_ResetBtn:
                codeMap = null;
                intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);

                break;
            case R.id.startHourTextView:
                startHourSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startHourSpinner, startHourList, /*codeMap,*/itemKeyValueMap, viewCollection);
                break;
            case R.id.endHourTextView:
                endHourSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endHourSpinner, endHourList, /*codeMap,*/itemKeyValueMap, viewCollection);
                break;
            case R.id.startMinuteTextView:
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, startMinuteSpinner, startMinuteList, /*codeMap,*/itemKeyValueMap, viewCollection);
                startMinuteSpinner.performClick();
                break;
            case R.id.endMinuteTextView:
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, endMinuteSpinner, endMinuteList, /*codeMap,*/itemKeyValueMap, viewCollection);
                endMinuteSpinner.performClick();
                break;
            case R.id.startDateImageView:
                datePickerMethod(startDateTextView);
                break;
            case R.id.endDateImageView:
                datePickerMethod(endDateTextView);
                break;
            case R.id.li_registerBtn:

                startLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_startGongsaGugan.getText().toString()));
                endLimitEtValue = Common.doubleNullCheck(Double.parseDouble(ET_endGongsaGugan.getText().toString()));

                try {
                    if ((Boolean) checkingValue(startLimitGugan, endLimitGugan, startLimitEtValue, endLimitEtValue) == false) {
                        //System.out.println("시작구간은 "+ ((Boolean)checkingValue((Double)(Object)codeMap.get("minMtnofStpntDstnc")+" 보다 크거나 같고 " + ((Double)(Object)ET_startGongsaGugan.getText().toString())+ " 보다 작거나 같아야합니다.");
                        Toast.makeText(getApplicationContext(), "공사구간은" + startLimitGugan + " 사이에서 " + endLimitGugan + "에 위치해야 합니다.", Toast.LENGTH_LONG).show();
                    } else if (gosundaeNullCheck() == false) {


                    }/*else if ((ET_gosundaeNum.getText().equals(null) || ET_gosundaeNum.getText().toString().length() == 0) && (Li_gosundae_no1.getVisibility()==View.GONE && Li_gosundae_no2.getVisibility()==View.GONE)) {
                        Toast.makeText(getApplicationContext(), "고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                   *//*     if(Li_gosundae_no1.getVisibility()==View.GONE && Li_gosundae_no2.getVisibility()==View.GONE){
                            Log.i("mmm",1+"");
                            Toast.makeText(getApplicationContext(), "고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                        }else if(Li_gosundae_no1.getVisibility()==View.VISIBLE && Li_gosundae_no2.getVisibility()==View.GONE){
                            Log.i("mmm",2+"");
                            Toast.makeText(getApplicationContext(), "고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                        }else if(Li_gosundae_no1.getVisibility()==View.VISIBLE && Li_gosundae_no2.getVisibility()==View.VISIBLE){
                            Log.i("mmm",3+"");
                            codeMap.put("GOSUNDAE_NO", ""+gosunDaeNoParsing(ET_gosundaeNum.getText().toString())+","+gosunDaeNoParsing(ET_gosundaeNum_no1.getText().toString())+","+gosunDaeNoParsing(ET_gosundaeNum_no2.getText().toString()));
                        }*//*
                    }else if(((que_et_list.empty()==false) && que_et_list.peek().getId()==ET_gosundaeNum_no1.getId())&&(ET_gosundaeNum_no1.getText().toString().equals("") ||ET_gosundaeNum_no1.getText().toString().length() ==0  ) ){
                        Toast.makeText(getApplicationContext(), "두번째 고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    }else if( ((que_et_list.empty()==false) && que_et_list.peek().getId()==ET_gosundaeNum_no2.getId()) &&(ET_gosundaeNum_no2.getText().toString().equals("")||ET_gosundaeNum_no2.getText().toString().length() ==0 )  ){
                        Toast.makeText(getApplicationContext(), "세번째 고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    }else if( ((que_et_list.empty()==false) && que_et_list.peek().getId()==ET_gosundaeNum_no3.getId()) &&(ET_gosundaeNum_no3.getText().toString().equals("")||ET_gosundaeNum_no3.getText().toString().length() ==0 )  ){
                        Toast.makeText(getApplicationContext(), "네번째 고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    }else if( ((que_et_list.empty()==false) && que_et_list.peek().getId()==ET_gosundaeNum_no4.getId()) &&(ET_gosundaeNum_no4.getText().toString().equals("")||ET_gosundaeNum_no4.getText().toString().length() ==0 )  ){
                        Toast.makeText(getApplicationContext(), "다섯번째 고순대 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    }*/ else if (inwonJangbiResultTextView.getText().equals(null) || inwonJangbiResultTextView.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "공사 인원과 사용 장비를 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else if (calCompareCheck(startDateTextView.getText().toString(), startHourTextView.getText().toString(), startMinuteTextView.getText().toString(), endDateTextView.getText().toString(), endHourTextView.getText().toString(), endMinuteTextView.getText().toString()) == false) {
                        Toast.makeText(getApplicationContext(), "끝난시간은 시작시간보다 늦은 시간에 있어야 합니다.", Toast.LENGTH_LONG).show();
                    } /*else if (codeMap.get("roadLimitResult").equals(null) || codeMap.get("roadLimitResult").equals("")) {
                        Toast.makeText(getApplicationContext(), "차로 제한을 선택하여주세요.", Toast.LENGTH_LONG).show();
                    } */ else if (directionTextView.equals("") || directionTextView.length() == 0) {
                        Toast.makeText(getApplicationContext(), "해당 노선은 이정이 선택되지 않아서 작업계획의 등록이 불가능합니다.", Toast.LENGTH_LONG).show();
                    } else {
                        codeMap.put("CNSTN_CTNT", ET_gongsaContent.getText().toString());//공사내용
                        codeMap.put("GOSUNDAE_NO", gosundaeNoParsing(que_et_list));
                        codeMap.put("LIMT_PLAN_DATES", getCurDate());
                        codeMap.put("minMtnofStpntDstnc", ET_startGongsaGugan.getText().toString());
                        codeMap.put("maxMtnofEdpntDstnc", ET_endGongsaGugan.getText().toString());
                        codeMap.put("HDQR_NM", bonbuTextView.getText().toString());
                        //codeMap.put("gamdokList_in_bigo", bonbuTextView.getText().toString());
                        if (!roadlimitTextView.getText().toString().equals("현재 차단차로 없음")) {
                            codeMap.put("gongsaContent", roadlimitTextView.getText().toString());
                        } else {
                            codeMap.put("gongsaContent", "차단 차로 없음");
                        }

                        codeMap.put("startWorkTime", startDateTextView.getText().toString().replaceAll("/", "") + dateParsing(startHourTextView.getText().toString()) + dateParsing(startMinuteTextView.getText().toString()));
                        codeMap.put("endWorkTime", endDateTextView.getText().toString().replace("/", "") + dateParsing(endHourTextView.getText().toString()) + dateParsing(endMinuteTextView.getText().toString()));
                        codeMap.put("inwonJangbi", inwonJangbiResultTextView.getText().toString());
                        codeMap.put("nVersion", "true");
                        //
                        codeMap.put("ROUTE_NM", noSunTextView.getText().toString());
                        codeMap.put("NOSUN_DIRECTION", directionTextView.getText().toString());
                        /*      codeMap.put("roadJobClssCd","2");*/
                        Log.i("등록버튼", WorkPlanRegisterActivity.codeMap.toString());

                        String parameter = jsonParameterParsing(WorkPlanRegisterActivity.codeMap);
                        jsonObject = new JSONObject(codeMap);
                        JSONObject jsonNameValue = new JSONObject();
                        jsonNameValue.put("bonbu", bonbuTextView.getText().toString());
                        jsonNameValue.put("jisa", jisaTextView.getText().toString());
                        jsonNameValue.put("nosun", noSunTextView.getText().toString());
                        jsonNameValue.put("direction", directionTextView.getText().toString());
                        jsonNameValue.put("gamdokTv", gamdokwonTextView.getText().toString());
                        jsonNameValue.put("gongsaContent", ET_gongsaContent.getText().toString());
                        jsonNameValue.put("roadlimit", roadlimitTextView.getText().toString());
                        jsonNameValue.put("startTime", startDateTextView.getText().toString() + " " + startHourTextView.getText().toString() + ":" + startMinuteTextView.getText().toString());
                        jsonNameValue.put("endTime", endDateTextView.getText().toString() + " " + endHourTextView.getText().toString() + ":" + endMinuteTextView.getText().toString());
                        jsonNameValue.put("startGugan", ET_startGongsaGugan.getText().toString());
                        jsonNameValue.put("endGugan", ET_endGongsaGugan.getText().toString());
                        jsonNameValue.put("inwonjangbi", inwonJangbiResultTextView.getText().toString());
                        jsonNameValue.put("gosundaeNum", gosundaTextParsing(que_et_list));
                        jsonNameValue.put("workType", workTypeTextView.getText().toString());
                        Log.i("1전송 파라미터", codeMap.toString());
                        // Log.println(Log.ASSERT, "등록전 파람 확인", "변화여부 확인 : " + codeMap.toString());
                        Log.println(Log.ASSERT, "sdfsdf등록전 파람 확인", "" + URLEncoder.encode(jsonObject.toString(),"UTF-8"));

                        //2021.06 안내문구표출
                        //new CustomDialog(WorkPlanRegisterActivity.this, R.layout.z_custom_dialog, R.id.sendApprovalRequest, jsonNameValue, jsonObject, intent);
                        new CustomDialog(WorkPlanRegisterActivity.this, R.layout.z_work_plan_noti, R.id.correctBtn, jsonNameValue, jsonObject, intent);

                        // new Action("get","http://192.168.1.9:8080/WorkPlan/register.do",jsonObject.toString()).execute();

                    }
                } catch (JSONException e) {
                    Log.e("에러", "익셉션 발생");
                }catch ( NullPointerException e) {
                    Log.e("에러", "익셉션 발생");
                }catch ( ArrayIndexOutOfBoundsException  e) {
                    Log.e("에러", "익셉션 발생");
                }catch ( ParseException e) {
                    Log.e("에러", "익셉션 발생");
                }catch (Exception e) {
                    Log.e("에러", "익셉션 발생");
                }
                break;
            case R.id.workTypeCATTextView: //2021.07 작업유형(대분류)
                workTypeCATSpinner.performClick();
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeCATSpinner, workTypeCATList,itemKeyValueMap,viewCollection);
                break;

            case R.id.workTypeTextView:
                //2021.07 작업유형 대분류 TEST
                //new AdapterSpinnerUtil(this, R.layout.z_spinner_item, workTypeSpinner, workTypeList, /*codeMap,*/itemKeyValueMap, viewCollection);
                workTypeSpinner.performClick();
                break;

            case R.id.roadlimitTextView:
                new AdapterSpinnerUtil(this, R.layout.z_spinner_item, charoSpinner, charoList, /*codeMap,*/itemKeyValueMap, viewCollection);
                maxCharoChoice();

                /* charoSpinner.performClick();*/
                break;
            case R.id.plus_btn:
                //  int index =
                if (que_et_list.size() == 1 && (que_et_list.get(0).getText().toString().length() == 0 || que_et_list.get(0).getText().toString().equals(""))) {
                    Toast.makeText(this, "1번째 고순대번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (/*que_et_list.size()!=0 &&*/ (que_et_list.get(que_et_list.size() - 1).getText().toString().equals("") || que_et_list.get(que_et_list.size() - 1).getText().toString().length() == 0)) {
                    //                  Log.i("뭐지","사이즈:"+que_et_list.size());
//                    Log.i("뭐지",(que_et_list.size()-1)+"번째, 값은:"+que_et_list.get(que_et_list.size()-1).getText().toString());
                    //int cnt=que_et_list.size()+1;
                    int cnt = que_et_list.size();
                    Toast.makeText(this, cnt + "번째 고순대번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (que_et_list.size() == 5) {
                    Toast.makeText(this, "고순대번호는 최고 5개만 입력이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    addView(null);
                }


                break;

        }
    }

    @Override
    public String onActionPost(String primitive, String date) {
        return date;
    }

    public boolean gosundaeNullCheck() {
        Log.i("cccc", "" + "여기는 들옴");
        Log.i("cccc", "사이즈::" + que_et_list.size());
        for (int i = 0; i < que_et_list.size(); i++) {
            if (que_et_list.get(i).getText().toString().length() == 0 || que_et_list.get(i).getText().toString().equals("")) {
                switch (i) {
                    case 0:
                        Toast.makeText(this, "첫번째 고순대 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 1:
                        Toast.makeText(this, "두번째 고순대 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 2:
                        Toast.makeText(this, "세번째 고순대 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 3:
                        Toast.makeText(this, "네번째 고순대 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return false;
                    case 4:
                        Toast.makeText(this, "다섯번째 고순대 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return false;
                }
            } else if (i == que_et_list.size() - 1) {
                return true;
            }

        }
        return false;
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
        /*li.setWeightSum(322);*/
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
        final int inner_view_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());


        LinearLayout.LayoutParams inner_viewParams = new LinearLayout.LayoutParams(0, view_height, 5);
        inner_view.setLayoutParams(inner_viewParams);
        li.addView(inner_view);

        ImageView iv = new ImageView(this);
        final int set_iv_padding_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams iv_param = new LinearLayout.LayoutParams(set_iv_padding_left, ViewGroup.LayoutParams.WRAP_CONTENT, 10);
        iv.setLayoutParams(iv_param);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv.setBackgroundResource(R.drawable.z_gosunda_minus_item);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup v = (ViewGroup) view.getParent();
                LinearLayout li = (LinearLayout) v.getParent();
                LinearLayout li_pa = (LinearLayout) li.getParent();

                Log.i("index", "리스트사이즈::" + que_et_list.size());
                //li_pa.removeView(li);
                //li_pa.removeView(li);
                if (li_pa.getChildCount() == 1) {
                    /*Toast.makeText(WorkPlanRegisterActivity.this,"고순대번호는 최소 1개 이상이 입력 되어야 합니다.",Toast.LENGTH_SHORT).show();
                    ((EditText)v.getChildAt(0)).setText("");
                    ((EditText)que_et_list.get(0)).setText("");*/
                    if (((EditText) v.getChildAt(0)).getText().toString().length() == 0 || ((EditText) v.getChildAt(0)).getText().toString().equals("")) {
                        Toast.makeText(WorkPlanRegisterActivity.this, "고순대번호는 최소 1개가 입력되어야 합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        new CustomDialog(WorkPlanRegisterActivity.this, R.layout.zz_warn_gosundae, R.id.warn_msg, "지우시는 고순대 번호는\n" + gosunDaeNoParsing(((EditText) v.getChildAt(0)).getText().toString()) + "\n입니다.", que_et_list, li_pa.indexOfChild(li), li_pa);
                    }
                } else {
                   /* que_et_list.remove(li_pa.indexOfChild(li));
                    Toast.makeText(WorkPlanRegisterActivity.this,"지우시는 번호는 "+((EditText)v.getChildAt(0)).getText().toString()+" 입니다.",Toast.LENGTH_SHORT).show();
                    li_pa.removeViewAt(li_pa.indexOfChild(li));*/
                    new CustomDialog(WorkPlanRegisterActivity.this, R.layout.zz_warn_gosundae, R.id.warn_msg, "지우시는 고순대 번호는\n" + gosunDaeNoParsing(((EditText) v.getChildAt(0)).getText().toString()) + "\n입니다.", que_et_list, li_pa.indexOfChild(li), li_pa);

                }


                for (int i = 0; i < que_et_list.size(); i++) {
                    Log.i("index", "리스트에 담긴것들::" + que_et_list.get(i).getText().toString());
                }
                Log.i("index", "------------------------------------");
            }
        });
        li_out.addView(li);
        li.addView(iv);

        gosundae_list.addView(li_out);
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


    //


    //총 차로 입력
    public void maxCharoChoice() {


        final EditText editText = new EditText(this);
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

                        int preChao = intStrNullCheck(editText.getText().toString());
                        /*   if(preChao <=6 && preChao <= maxCharo){*/
                        if (preChao != 0 || editText.getText().toString().length() != 0) {
                            if (preChao <= 6) {
                                maxCharo = preChao;
                                codeMap.put("TOT_CRGW_CNT", Integer.toString(maxCharo));
                            } else {
                                maxCharo = 6;
                                codeMap.put("TOT_CRGW_CNT", "6");
                            }
                            maxCharoSelected();
                        } else {
                            Toast.makeText(WorkPlanRegisterActivity.this, "총 차로를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                            maxCharoChoice();
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
        if (roadlimitTextView.getText().length() > 0 && (maxCharo != null && maxCharo != 0)) {
            // roadLimit= new String[9];
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

            //최창유 주석
            items[maxCharo + 7] = "교대차단";
            final StringBuffer sb = new StringBuffer();
            final boolean[] itemsBool = new boolean[maxCharo + 8];
            for (int i = 0; i < roadLimit.length; i++) {
                roadLimit[i] = null;
            }
            totalroadLimit.clear();
            AlertDialog.Builder dialog = new AlertDialog.Builder(WorkPlanRegisterActivity.this);

            dialog.setTitle("차단 차로를 선택하여주세요")
                    .setMultiChoiceItems(
                            items
                            , itemsBool
                            , new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    //

                                    if (isChecked) {
                                        roadLimit[which] = items[which];
                                        totalroadLimit.add(items[which]);
                                    } else {

                                        roadLimit[which] = null;
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

                    for (int i = 0; i < totalroadLimit.size(); i++) {
                        if (i != totalroadLimit.size() - 1) {
                            roadListResult += loadLimitMap.get(totalroadLimit.get(i)) + ",";
                        } else {
                            roadListResult += loadLimitMap.get(totalroadLimit.get(i));
                        }
                        Log.i("교대차단", roadListResult);
                        if (totalroadLimit.get(i).equals("이동차단")) {
                            codeMap.put("MVBL_BLC_YN", "Y");//이동차단여
                        }
                    }
                    codeMap.put("roadLimitResult", roadListResult);

                    jsonArray = new JSONArray();

                    roadlimitTextView.clearComposingText();
                    String charhDetail = "";
                    for (int i = 0; i < roadLimit.length; i++) {
                        if (roadLimit[i] != null) {
                            if (i == roadLimit.length - 1) {
                                if (roadLimit[i].equals("이동차단") || roadLimit[i].equals("교대차단")) {
                                    charhDetail += ", " + roadLimit[i];
                                } else {
                                    charhDetail += ", " + roadLimit[i] + "차단";
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
                            roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + charhDetail.substring(1) + "");
                        } else {
                            roadlimitTextView.setText("총 " + maxCharo + "차로 중 " + charhDetail.substring(1) + "");
                        }

                    } catch (ArrayIndexOutOfBoundsException  e) {
                        roadlimitTextView.setText("총 " + maxCharo + "차로 중" + "" + " 차단");
                    }catch ( NullPointerException e) {
                        roadlimitTextView.setText("총 " + maxCharo + "차로 중" + "" + " 차단");
                    }catch (Exception e) {
                        roadlimitTextView.setText("총 " + maxCharo + "차로 중" + "" + " 차단");
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


    //    /**
//     * 최초 접속시 달력의 시간을 익일로 초기화 시켜주는 메소드
//     */
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

    public String gosundaeNoParsing(List<EditText> que_et_list) {
        String result = "";
        for (int i = 0; i < que_et_list.size(); i++) {
            result += "," + gosunDaeNoParsing(que_et_list.get(i).getText().toString());
        }
        return result.substring(1);
    }

    public void gamokAlertDialog(String listArray, Map<String, Object[]> viewCollection, final TextView gamdokwonTextView) {
        Log.println(Log.ASSERT, "감독원 어레이", listArray);

        try {
            final JSONArray jsonArray = new JSONArray(listArray);

            if (!jsonArray.getJSONObject(0).get("EMNM").toString().equals("조회된 감독원이 없습니다.") || !gamdokwonTextView.getText().equals("조회된 감독원이 없습니다.")) {

                gamdokwonTextView.clearComposingText();
                gamdokSpinner.performClick();
                final String[] gamdokName = new String[jsonArray.length()];
                final boolean[] gamdokItemBoo = new boolean[jsonArray.length()];
                Log.println(Log.ASSERT, "감독원 어레이 길이", listArray.length() + "");
                for (int i = 0; i < gamdokName.length; i++) {
                    gamdokItemBoo[i] = false;
                    gamdokName[i] = jsonArray.getJSONObject(i).get("EMNM").toString();
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setMultiChoiceItems(gamdokName, gamdokItemBoo, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b == true) {

                            //gamdokwonTextView.append(gamdokName[i]+",");

                            String[] itemCheck = null;

                            try {

                                if (!gamdokSawonNo.contains(jsonArray.getJSONObject(i).getString("USER_EMNO"))) {
                                    gamdokTvResult += "," + gamdokName[i];
                                    gamdokTelno += "," + jsonArray.getJSONObject(i).getString("TEL_NO").replace("-", "");


                                    gamdokSawonNo += "," + jsonArray.getJSONObject(i).getString("USER_EMNO");//USER_EMNO

                                    gamdokList += "\n" + gamdokName[i] + "(" + "" + jsonArray.getJSONObject(i).getString("TEL_NO").replace("-", "") + ")";
                                    itemCheck = gamdokSawonNo.substring(1).split(",");
                                    codeMap.put("gamdokList_in_bigo", gamdokList);
                                    Log.i("ndndnsdnf", codeMap.get("gamdokList_in_bigo"));
                                    if (itemCheck.length >= 6) {
                                        Toast.makeText(WorkPlanRegisterActivity.this, "최대 다섯명의 감독원만 등록 가능합니다.", Toast.LENGTH_SHORT).show();


                                        for (int ii = 0; ii < gamdokItemBoo.length; ii++) {
                                            gamdokItemBoo[ii] = false;
                                            gamdokTvResult = "";
                                            gamdokTelno = "";
                                            gamdokSawonNo = "";
                                            gamdokList = "";
                                            //
                                        }
                                        gamdokwonTextView.clearComposingText();
                                        gamdokwonTextView.setText("");
                                        itemCheck = null;
                                        dialogInterface.dismiss();
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException  e) {
                                Log.e("에러", "익셉션 발생");
                            } catch ( JSONException e) {
                                Log.e("에러", "익셉션 발생");
                            } catch (Exception e) {
                                Log.e("에러", "익셉션 발생");
                            }
                            // String[] itemCheck = new String[gamdokSawonNo.substring(1).split("-").length];

                            int cnt = 0;


                        } else {
                            Log.i("해제", "해제");

                            try {
                                gamdokName[i] = null;
                                gamdokTvResult.replace("," + gamdokName[i], "");
                                //gamdokTvResult+=","+gamdokName[i];
                                Log.i("해제", gamdokTvResult);
                                gamdokTelno.replace("," + jsonArray.getJSONObject(i).getString("TEL_NO").replace("-", ""), "");

                                Log.i("해제", gamdokTelno);
                                gamdokSawonNo.replace(jsonArray.getJSONObject(i).getString("USER_EMNO"), "");//USER_EMNO
                                Log.i("해제", gamdokSawonNo);
                                gamdokList.replace("\n" + gamdokName[i] + "(" + "" + jsonArray.getJSONObject(i).getString("TEL_NO").replace("-", "") + ")", "");
                                ;
                                Log.i("해제", gamdokList);
                            } catch (ArrayIndexOutOfBoundsException   e) {
                                Log.e("에러", "익셉션 발생");
                            }catch ( JSONException  e) {
                                Log.e("에러", "익셉션 발생");
                            }catch ( NullPointerException e) {
                                Log.e("에러", "익셉션 발생");
                            }catch (Exception e) {
                                Log.e("에러", "익셉션 발생");
                            }
                        }


                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (gamdokTvResult.length() != 0) {
                            gamdokwonTextView.clearComposingText();
                            gamdokwonTextView.setText(gamdokTvResult.substring(1));
                            codeMap.put("MTNOF_PRCH_EMNO_TELNO", gamdokTelno.substring(1));
                            codeMap.put("MTNOF_PRCH_EMNO", gamdokSawonNo.substring(1));
                            gamdokTvResult = "";
                            gamdokTelno = "";
                            gamdokSawonNo = "";
                        }
                    }
                });

                dialog.create();
                dialog.show();
            } else {
                Toast.makeText(this, "조회된 감독원이 없습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (NullPointerException  e) {
            Log.e("에러", "익셉션 발생");
        } catch ( ArrayIndexOutOfBoundsException  e) {
            Log.e("에러", "익셉션 발생");
        } catch ( JSONException e) {
            Log.e("에러", "익셉션 발생");
        } catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }

    }

    public void datePickerMethod(final TextView text) {
        Log.i("datePickerMethod", "요기");
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(WorkPlanRegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                } catch (ArrayIndexOutOfBoundsException  e) {
                    Log.e("에러", "익셉션 발생");
                } catch ( StringIndexOutOfBoundsException e) {
                    Log.e("에러", "익셉션 발생");
                } catch (Exception e) {
                    Log.e("에러", "익셉션 발생");
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        datePicker.show();
    }

    public String jsonParameterParsing(Map<String, String> codeMap) {
        Set set = codeMap.keySet();
        Iterator iterator = set.iterator();
        try {

            JSONObject jsonObject = new JSONObject();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                jsonObject.accumulate(key, codeMap.get(key));
            }

            return jsonObject.toString();

        } catch (JSONException e) {
            Log.e("에러", "익셉션 발생");
        }
        return null;
    }

    public String getCurDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date curTime = new Date();
        return sdf.format(curTime);
    }

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
            Log.e("에러", "익셉션 발생");
            return false;
        } catch (Exception e){
            Log.e("에러", "익셉션 발생");
            return false;
        }
    }

    //
//    /**
//     * 고속도로 순찰대 번호 파싱
//     * @param gosundaNo
//     * @return
//     */
    public String gosunDaeNoParsing(String gosundaNo) {
        try {
            gosundaNo = gosundaNo.substring(0, 4) + "-" + gosundaNo.substring(4, 8) + "-" + gosundaNo.substring(8, gosundaNo.length());
        } catch (StringIndexOutOfBoundsException  e) {
            Log.e("에러", "익셉션 발생");
        }catch ( ArrayIndexOutOfBoundsException e) {
            Log.e("에러", "익셉션 발생");
        }catch (Exception e) {
            Log.e("에러", "익셉션 발생");
        }
        return gosundaNo;
    }

    public String dateParsing(String parsing) {
        if (parsing.length() == 1)
            parsing = "0" + parsing;
        return parsing;
    }
}
