package com.ex.situationmanager;

import android.app.Activity;


import android.app.AlertDialog;
import android.app.Dialog;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;


import android.support.v4.widget.DrawerLayout;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ex.situationmanager.dto.DirectionVo;
import com.ex.situationmanager.dto.Patrol;
import com.ex.situationmanager.dto.TunnelNameVo;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.Image;

import android.view.View.OnClickListener;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ex.situationmanager.InnerEmployCCTVFragment.curCcctvOnOff;
import static com.ex.situationmanager.TowMainActivity.listTimerDelay;
import static com.ex.situationmanager.service.SituationService.nowUPT_CARTIME;

public class InnerEmployActivity extends BaseActivity implements OnClickListener {


    public static FragmentManager fragmentmanager;
    private InnerEmployCCTVFragment innerMpleMain;

    private FragmentTransaction transaction;

    TextView cctv_tv;
    TextView situattion_select_tv;
    TextView innerPicture;
    TextView detailTitle, detailNo, detailState, detailTime, detailJeopbo,
            detailType, detailJochi, detailContent;

    ImageView btnUserInfo;
    ImageView menu;
    Button testBtn;

    static String jisaCd = "";

    Spinner spin_Bonbu;
    Spinner spin_jisa;
    ArrayList<Map<String, String>> bonbuList = null;
    ArrayList<Map<String, String>> jisaList = null;
    Map<String, String> bonbuMap;
    Map<String, String> jisaMap;
    Map<String, List<Map<String, String>>> jisaListMapByBonbyCd = null;

    ArrayList<Patrol> sortItemList = new ArrayList<Patrol>();
    ArrayList<Patrol> itemList;
    ArrayList<Patrol> pastitemList;
    ArrayList<String> listb;
    ArrayList<String> jisaSpinList;

    //여기서부터 복사 시작
    int currentPage = 1;
    int position = 0;

    Intent intent;


    ListView listView;
    int height = 0;
    // ImageView patrolStart, patrolStartIng, patrolStop, patrolPicture,patrolRegist;
    //ImageView btnPatClear;
    boolean displayFlag = false;

    final String TAG = "InnerEmployActivity";
    Dialog dialogGallery;
    // ImageView btnPatJochiReg;
    //목록 통신 동기화를 위함.
    boolean listFlag = true;
    LinearLayout llbottomBtnDefault, llbottomBtnPhone;
    //  ImageView btnPatJochiReg;
    boolean initDataFlag = false;
    Spinner spinSort;
    String spinSorting = "N";
    // CheckBox patrolOK;
    TextView patrolOkTxt;
    TextView situattion_insert_tv;
    private Uri mThumbUri;

    int selectedPosition = -1;


    InnerEmploySituationSelectFragment news;
    // *********************************Slide Menu
    // ************************************
    // 변수선언
    String[] navItems = {"전체", "내부직원", "순찰원", "견인원", "대국민"};
    ExpandableListView lvNavList;
    FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    // *********************************Slide Menu
    // ************************************
    static String currentFragment = "";
    View selectedView = null;

    String selectedreg_type = "";
    boolean ProgressFlag = false;
    ProgressDialog progressDialog;
    ArrayAdapter<String> jisaAdapter;


    //cctv
    String start_cctv_url = "";
    String end_cctv_url = "";


    //
    InnerEmployCCTVFragment cctv = null;
    Bundle ds;
    InnerEmployeeSituationInsert insert;
    public static boolean isLanding = false;
    public static boolean updateDataCheck = false;
    int selectedBonbuSpinPosition = 0;
    int selectedJisaSpinPosition = 0;
    static boolean isSelection = false;

    public static boolean onResumeProtocol = false;

    //
    SharedPreferences sh;
    String userJisaStr;
    List<String> userCarNameList_ = null;
    JSONArray userJisaList = null;
    JSONObject userJisaJsonObj = null;


    //
    String userInfo;

    JSONArray jsonArray;
    List<String> innerEmployeeList;
    List<String> sunchalJsonList;

    //2020.12 터널화재
    TextView inner_download;
    TextView tunnelTitle;
    Dialog dialogPDFdown;
    String tunnelName;
    List<TunnelNameVo> items = new ArrayList<TunnelNameVo>();
    String tunnelImp = "";


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.inner_employee_main);
        //VIEW_TAG = VIEW_INNEREMPLOYEE;

        //세로고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //   btnPatJochiReg = (ImageView) findViewById(R.id.btnPatJochiReg);
        btnUserInfo = (ImageView) findViewById(R.id.btnUserInfo);
        menu = (ImageView) findViewById(R.id.menu);
        innerPicture = (TextView) findViewById(R.id.innerPicture);
        cctv_tv = (TextView) findViewById(R.id.cctv_tv);//하단 CCTV 버튼
        situattion_select_tv = (TextView) findViewById(R.id.situattion_select_tv);
        situattion_insert_tv = (TextView) findViewById(R.id.situattion_insert_tv);
        //20201125_터널화재
        inner_download = (TextView) findViewById(R.id.inner_download);
        inner_download.setOnClickListener(this);
        inner_download.setVisibility(View.GONE);
        tunnelImp = SituationService.conf.User.getTunnelImp();


        menu.setOnClickListener(this);
        cctv_tv.setOnClickListener(this);
        situattion_select_tv.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        innerPicture.setOnClickListener(this);
        situattion_insert_tv.setOnClickListener(this);
        // USER_TYPE =USER_TYPE_INNEREMPLOYEE;


        spin_Bonbu = (Spinner) findViewById(R.id.spin_Bonbu);
        spin_jisa = (Spinner) findViewById(R.id.spin_jisa);
        jisaSpinList = new ArrayList<String>();
        jisaSpinList.add("지사선택");
        jisaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jisaSpinList);
        spin_jisa.setAdapter(jisaAdapter);


       /* spin_Bonbu.setOnItemSelectedListener(this);
        spin_jisa.setOnClickListener(this);*/

        TowMainActivity.stopTimer_list();
        //여기서부터 복사시작
        ProgressFlag = true;
        // ProgressFlag = true;
        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);
        //  btnPatJochiReg.setOnClickListener(this);
        pastitemList = new ArrayList<Patrol>();

        //현재 사용 자정보
        try {

            JSONObject savedUserInfoJob = new JSONObject();

            savedUserInfoJob.put("user_type", SituationService.conf.User.getUser_type());
            savedUserInfoJob.put("bscode", SituationService.conf.User.getBscode_list().get(0));
            savedUserInfoJob.put("bsname", SituationService.conf.User.getBsname_list().get(0));
            savedUserInfoJob.put("patcar_id", SituationService.conf.User.getPatcar_id());
            savedUserInfoJob.put("car_nm", SituationService.conf.User.getCar_nm());
            savedUserInfoJob.put("cur_page", TAG);


            SharedPreferences savedUserInfo = getApplication().getSharedPreferences("savedUserInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = savedUserInfo.edit();
            editor.putString("savedUserInfo", savedUserInfoJob.toString());
            editor.commit();
        } catch (JSONException e) {
            Log.e("예외", "예외발생");
        } catch (Exception e) {
            Log.e("예외", "예외발생");
        }

        // *********************************Slide Menu
        lvNavList = (ExpandableListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);
        List<String> objects = new ArrayList<String>();
        for (int i = 0; i < navItems.length; i++) {
            objects.add(navItems[i]);
        }

        sh = getSharedPreferences("userAllJisaList", MODE_PRIVATE);
        userInfo = sh.getString("userAllInfo", "");


        innerEmployeeList = new ArrayList<String>();
        sunchalJsonList = new ArrayList<String>();
        Log.i("userdsdfsdf", userInfo);
        try {
            jsonArray = new JSONArray(userInfo);
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.i("dfsdf", jsonArray.getJSONObject(i).toString());
                innerEmployeeList.add(jsonArray.getJSONObject(i).toString());
                if (jsonArray.getJSONObject(i).get("user_type").toString().equals("0001")) {
                    sunchalJsonList.add(jsonArray.getJSONObject(i).toString());
                }
            }
        } catch (NullPointerException e) {
            Log.e("에러", "예외");
        } catch (IndexOutOfBoundsException id) {
            Log.e("에러", "예외");
        } catch (JSONException jsonException) {
            Log.e("에러", "예외");
        } catch (Exception jsonException) {
            Log.e("에러", "예외");
        }


        lvNavList = (ExpandableListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);
        for (int i = 0; i < navItems.length; i++) {
            objects.add(navItems[i]);
        }
        ArrayList<ExpandableInnerListAdapter.ItemInner> dataList = new ArrayList<ExpandableInnerListAdapter.ItemInner>();

        ExpandableInnerListAdapter.ItemInner group1 = new ExpandableInnerListAdapter().new ItemInner(navItems[0]);//상황공유
        dataList.add(group1);
        ExpandableInnerListAdapter.ItemInner group2 = new ExpandableInnerListAdapter().new ItemInner(navItems[1]);//
        group2.child.addAll(innerEmployeeList);
        dataList.add(group2);
        ExpandableInnerListAdapter.ItemInner group3 = new ExpandableInnerListAdapter().new ItemInner(navItems[2]);//tnsckf
        group3.child.addAll(sunchalJsonList);
        dataList.add(group3);
        ExpandableInnerListAdapter.ItemInner group4 = new ExpandableInnerListAdapter().new ItemInner(navItems[3]);//견인
        group4.child.addAll(sunchalJsonList);
        dataList.add(group4);
        ExpandableInnerListAdapter.ItemInner group5 = new ExpandableInnerListAdapter().new ItemInner(navItems[4]);//견인
        group5.child.addAll(sunchalJsonList);
        dataList.add(group5);

        ExpandableInnerListAdapter apapter1 = new ExpandableInnerListAdapter(this, R.layout.slide_row, R.layout.group_row, dataList);

        //

        lvNavList.setAdapter(apapter1);
        lvNavList.setOnGroupClickListener(apapter1);
        lvNavList.setOnChildClickListener(apapter1);


        //  SlideAdapter adapter = new SlideAdapter(this, objects);
        // lvNavList.setAdapter(new ArrayAdapter<String>(this,
        // android.R.layout.simple_list_item_1, navItems));
        // lvNavList.setAdapter(adapter);

        lvNavList.setOnItemClickListener(new DrawerItemClickListener());
        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
        dtToggle = new ActionBarDrawerToggle(InnerEmployActivity.this, dlDrawer,
                R.drawable.ic_launcher, R.string.app_name,
                R.string.desc_list_item_icon) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("", "SlideMenu onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("", "SlideMenu onDrawerOpened");
            }
        };
        dlDrawer.setDrawerListener(dtToggle);

        // getActionBar().setDisplayHomeAsUpEnabled(true);
        dlDrawer.closeDrawer(lvNavList);
        jisaList = new ArrayList<Map<String, String>>();
        // *********************************Slide Menu

        USER_TYPE = USER_TYPE_PATROL;
        currentPage = 1;
        listView = (ListView) findViewById(R.id.patListView);
        height = listView.getHeight() / 4;

        patrolOkTxt = (TextView) findViewById(R.id.patrolOkTxt);


        llbottomBtnDefault = (LinearLayout) findViewById(R.id.llbottomBtnDefault);
        llbottomBtnPhone = (LinearLayout) findViewById(R.id.llbottomBtnPhone);

        detailTitle = (TextView) findViewById(R.id.detailTitle);
        detailNo = (TextView) findViewById(R.id.detailNo);
        detailState = (TextView) findViewById(R.id.detailState);
        detailTime = (TextView) findViewById(R.id.detailTime);
        detailJeopbo = (TextView) findViewById(R.id.detailJeopbo);
        detailType = (TextView) findViewById(R.id.detailType);
        detailJochi = (TextView) findViewById(R.id.detailJochi);
        detailContent = (TextView) findViewById(R.id.detailContent);

        spinSort = (Spinner) findViewById(R.id.spinSort);

        bonbuList = new ArrayList<Map<String, String>>();
        sortSpinnerAdapter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 화면 켜짐 유지
        currentFragment = TAG;
        //TowMainActivity.stopTimer_list();

        //setStartBtn();


        /*Parameters params = new Parameters(ONECLICK_GETPATROLTELNO_SELECT);*/

        // Toast.makeText(this,"이름 : " + Configuration.User.getBscode_list(),Toast.LENGTH_SHORT);
        Log.println(Log.ASSERT, TAG, "InnerImployActivity new Action 직전:");
        Log.println(Log.ASSERT, TAG, "InnerImployActivity onCreate 지사코드 리스트" + Configuration.User.getBscode_list().get(0).toString());

        //  startTimer_Inner_list();
        //   new Action(ONECLICK_PATROLRCEPTINFO_SELECT, params).execute();
        //   new Action(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT, params).execute();
        jisaListMapByBonbyCd = new HashMap<String, List<Map<String, String>>>();

        listb = new ArrayList<String>();
       /* if(null == progressDialog){
            Log.println(Log.ASSERT,TAG,"상황목sdfsdf록 로딩중");
            //Log.println(Log.ASSERT,TAG,"상황목록 로딩중");
            //progressDialog = ProgressDialog.show(InnerEmployActivity.this, "", "상황목록 로딩중...", true);
            progressDialog = ProgressDialog.show(InnerEmployActivity.this, "", "상황목sdfsdf록 로딩중", true);
            initDataFlag=true;
        }*/

        Parameters params = new Parameters(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT);
        params.put("car_id", Configuration.User.getPatcar_id());

        if (null != Configuration.User.getBscode_list()) {
            if (Configuration.User.getBscode_list().size() > 0) {
                params.put("bscode", Configuration.User.getBscode_list().get(0));

            }
        }


        params.put("user_type", Configuration.User.getUser_type());
        //params.put("bscode", "N00074");

       /* if(params.get("bscode").equals("N00002")){
            params.put("bscode","N01797");
        }
        Log.println(Log.ASSERT,TAG,"-----------------------");
        Log.println(Log.ASSERT,TAG,params.get("bscode"));
        Log.println(Log.ASSERT,TAG,params.get("car_id"));
        Log.println(Log.ASSERT,TAG,params.get("bscode"));
        Log.println(Log.ASSERT,TAG,"-----------------------");*/
        for (int i = 0; i < Configuration.User.getBsname_list().size(); i++) {
            Log.i("지사 이름", "순번" + i + ",지사이름:" + Configuration.User.getBsname_list().get(i));
        }
        Log.println(Log.ASSERT, TAG, "onCreate - startTimer_Inner_list_전");
        Log.println(Log.ASSERT, TAG, "onCreate - startTimer_Inner_list_전");
        transactionCommit();
        try {
            Fragment f1 = fragmentmanager.findFragmentByTag("cctv");
            //if(!f1.isHidden()){
            //  Toast.makeText(this,"히든쿤",Toast.LENGTH_LONG).show();
            //}else{
            //   Toast.makeText(this,"히11111든쿤",Toast.LENGTH_LONG).show();
            // }
            // if(curCcctvOnOff==false){
            new Action(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT, params).execute();
            //  }

            if (!chkGpsService(this)) {
                AlertDialog.Builder gsDialog = new AlertDialog.Builder(InnerEmployActivity.this);
                gsDialog.setTitle("스마트 공사관리");
                gsDialog.setMessage("GPS가 켜져있지 않습니다.\n지금 켜시겠습니까?");
                gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // GPS설정 화면으로 이동
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(intent, 2000);
                    }

                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
                return;
            }


        } catch (NullPointerException e) {
            Log.e("에러", "익셉션");
        } catch (Exception e) {
            Log.e("에러", "익셉션");
        }

        //2020.12 터널화재

        // startTimer_Inner_list(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT);
        // btnPatJochiReg.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {

    }

    public class ExpandableInnerListAdapter extends BaseExpandableListAdapter implements OnClickListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener/*, ExpandableListView.OnItemClickListener*/ {
        private Context context;
        private int groupLayout = 0;
        private int childLayout = 0;
        private ArrayList<ExpandableInnerListAdapter.ItemInner> dataList;
        private LayoutInflater inflater;
        DrawerLayout drawerLayout;
        JSONObject jsonObject;

        public ExpandableInnerListAdapter(Context context, int groupLayout, int childLayout, ArrayList<ExpandableInnerListAdapter.ItemInner> dataList) {
            super();
            this.dataList = dataList;
            this.groupLayout = groupLayout;
            this.childLayout = childLayout;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  this.drawerLayout = drawerLayout;
        }

        public ExpandableInnerListAdapter() {
            super();
        }

        @Override
        public int getGroupCount() {
            return dataList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return dataList.get(i).child.size();
        }

        @Override
        public Object getGroup(int i) {
            return dataList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return dataList.get(i).child.get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            Log.i("호출 갯수", i + "getGroupView");
            //View view = convertView;
            Log.d("", "getview position " + position);
            if (view == null) {
                view = inflater.inflate(groupLayout, viewGroup, false);
            }
            //최창유 주석

            LinearLayout llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            LinearLayout tllSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            if (i == 0) {

                tllSlide.setBackgroundResource(R.drawable.tit_trans);
                tllSlide.setTag("전체");


            } else if (i == 1) {

                llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                llSlide.setTag("내부");
            } else if (i == 2) {
                llSlide.setBackgroundResource(R.drawable.item_slide_01);
                llSlide.setTag("순찰");
            } else if (i == 3) {
                llSlide.setBackgroundResource(R.drawable.item_slide_02);
                llSlide.setTag("견인");
            } else if (i == 4) {
                llSlide.setBackgroundResource(R.drawable.item_slide_03);
                llSlide.setTag("대민");
            }


            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

            Log.i("호출 갯수", i + "getChildView");


            if (view == null) {
                view = inflater.inflate(R.layout.group_row, viewGroup, false);
            }

            TextView llSlide = (TextView) view.findViewById(R.id.IISlideTextView);
            TextView IISlideTextView2 = (TextView) view.findViewById(R.id.IISlideTextView2);
            try {
                jsonObject = new JSONObject(dataList.get(i).child.get(i1));
                llSlide.setText("[" + jsonObject.get("bsname").toString() + "]");
                //    llSlide.setOnClickListener(this);//car_nm
                IISlideTextView2.setText("*" + jsonObject.get("car_nm").toString());
                /*  view.setOnClickListener(this);*/


            } catch (JSONException e) {
                Log.e("에러", "제이슨 익센셥");
            } catch (Exception e) {
                Log.e("에러", "제이슨 익센셥");
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), ((TextView) v).getText().toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
            Log.i("클릭된i와L", "i:" + i + "l:" + l);
            if (i == 0) {
                dlDrawer.closeDrawers();
            } else if (i == 1 && dataList.get(1).child.size() == 1) {//내부직원 직원 리스트가 1명 이면 그대로 이동
                if (dataList.get(1).child.size() == 0) {
                    Toast.makeText(InnerEmployActivity.this, "순찰 및 내부직원 사용자그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                try {
                    JSONObject job = new JSONObject(dataList.get(1).child.get(0));
                    SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                    SituationService.conf.User.getBscode_list().clear();
                    SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                    SituationService.conf.User.getBsname_list().clear();
                    SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                    SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                    SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                } catch (JSONException e) {
                    Log.e("에러", "예외");
                } catch (Exception e) {
                    Log.e("에러", "예외");
                }


                VIEW_TAG = VIEW_INNEREMPLOYEE;
                Intent i_employee = new Intent(InnerEmployActivity.this, InnerEmployActivity.class);
                startActivity(i_employee);
                finish();
                stopGPS();
                stopTimer(InnerEmployActivity.this);
                return true;
            } else if (i == 2/* && dataList.get(2).child.size() == 1*/) {//순찰 리스트가 1개이면 그대로 이동
                if (dataList.get(2).child.size() == 0) {
                    Toast.makeText(InnerEmployActivity.this, "순찰로 등록된 사용자 그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (dataList.get(2).child.size() == 1) {
                    try {
                        JSONObject job = new JSONObject(dataList.get(2).child.get(0));
                        SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                        SituationService.conf.User.getBscode_list().clear();
                        ;
                        SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                        SituationService.conf.User.getBsname_list().clear();
                        SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                        SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                        SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    } catch (Exception e) {
                        Log.e("에러", "예외");
                    }

                    Intent i_patrol = new Intent(InnerEmployActivity.this, PatrolMainActivity.class);
                    startActivity(i_patrol);
                    finish();
                    stopGPS();
                    stopTimer(InnerEmployActivity.this);
                    return true;
                } else {
                    return false;
                }

            } else if (i == 3 /*&& dataList.get(2).child.size() == 1*/) {//견인 리스트가 1개이면 그대로 이동
                if (dataList.get(2).child.size() == 0) {
                    Toast.makeText(InnerEmployActivity.this, "순찰로 등록된 사용자 그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (dataList.get(2).child.size() == 1) {
                    try {
                        JSONObject job = new JSONObject(dataList.get(2).child.get(0));
                        SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                        SituationService.conf.User.getBscode_list().clear();
                        ;
                        SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                        SituationService.conf.User.getBsname_list().clear();
                        SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                        SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                        SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    } catch (Exception e) {
                        Log.e("에러", "예외");
                    }

                    Intent i_tow = new Intent(InnerEmployActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(InnerEmployActivity.this);
                    return true;
                } else {
                    return false;
                }

            } else if (i == 4 /*&& dataList.get(2).child.size() == 1*/) {//순찰 리스트가 1개이면 그대로 이동
                if (dataList.get(2).child.size() == 0) {
                    Toast.makeText(InnerEmployActivity.this, "순찰로 등록된 사용자 그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (dataList.get(2).child.size() == 1) {
                    try {
                        JSONObject job = new JSONObject(dataList.get(2).child.get(0));
                        SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                        SituationService.conf.User.getBscode_list().clear();
                        ;
                        SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                        SituationService.conf.User.getBsname_list().clear();
                        SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                        SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                        SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    } catch (Exception e) {
                        Log.e("에러", "예외");
                    }

                    Intent i_citizen = new Intent(InnerEmployActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(InnerEmployActivity.this);
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

            //    Toast.makeText(getApplicationContext(), dataList.get(i).child.get(i1).toString(), Toast.LENGTH_SHORT).show();
            switch (i) {
                case 1:
                    try {
                        JSONObject job = new JSONObject(dataList.get(i).child.get(i1));
                        SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                        SituationService.conf.User.getBscode_list().clear();
                        ;
                        SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                        SituationService.conf.User.getBsname_list().clear();
                        SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                        SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                        SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                        VIEW_TAG = VIEW_INNEREMPLOYEE;
                        Intent i_employee = new Intent(InnerEmployActivity.this, InnerEmployActivity.class);
                        startActivity(i_employee);
                        finish();
                        stopGPS();
                        stopTimer(InnerEmployActivity.this);
                    } catch (JSONException j) {
                        Log.e("에러", "예외");
                    } catch (NullPointerException ne) {
                        Log.e("에러", "예외");
                    } catch (Exception ne) {
                        Log.e("에러", "예외");
                    }
                    break;
                // 순찰원
                case 2:

                    try {
                        JSONObject job = new JSONObject(dataList.get(i).child.get(i1));

                        if (job.get("user_type").toString().equals("0001")) {
                            SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                            SituationService.conf.User.getBscode_list().clear();
                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                            SituationService.conf.User.getBsname_list().clear();
                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                            SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                            SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                            VIEW_TAG = VIEW_PATROL;
                            Intent i_patrol = new Intent(InnerEmployActivity.this, PatrolMainActivity.class);
                            startActivity(i_patrol);
                            finish();
                            stopGPS();
                            stopTimer(InnerEmployActivity.this);
                        } else {
                            Toast.makeText(getApplicationContext(), "권한이 없는 사용자입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException j) {
                        Log.e("에러", "예외");
                    } catch (NullPointerException ne) {
                        Log.e("에러", "예외");
                    } catch (Exception ne) {
                        Log.e("에러", "예외");
                    }


                    break;
                // 견인원
                case 3:
                    try {
                        JSONObject job = new JSONObject(dataList.get(i).child.get(i1));
                        SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                        SituationService.conf.User.getBscode_list().clear();
                        SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                        SituationService.conf.User.getBsname_list().clear();
                        SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                        SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                        SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                        VIEW_TAG = VIEW_TOW;
                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    } catch (Exception e) {
                        Log.e("에러", "예외");
                    }

                    Intent i_tow = new Intent(InnerEmployActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(InnerEmployActivity.this);
                    break;
                // 대국민
                case 4:
                    try {
                        JSONObject job = new JSONObject(dataList.get(i).child.get(i1));
                        SituationService.conf.User.setUser_type(Common.nullCheck(job.getString("user_type")));
                        SituationService.conf.User.getBscode_list().clear();
                        SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                        SituationService.conf.User.getBsname_list().clear();
                        SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                        SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                        SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                        VIEW_TAG = VIEW_TOW;
                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    } catch (Exception e) {
                        Log.e("에러", "예외");
                    }
                    Intent i_citizen = new Intent(InnerEmployActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(InnerEmployActivity.this);
                    break;
                default:
                    break;
            }

            return false;
        }


        //--
        public class ItemInner {
            public ArrayList<String> child;
            public String groupName;

            public ItemInner(String name) {
                groupName = name;
                child = new ArrayList<String>();
            }
        }
    }


    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
        Log.println(Log.ASSERT, TAG, "Inner임플로이 엑션 포스트onActionPost");

        try {
            Log.println(Log.ASSERT, TAG, "onPostExecute 옴옴ㅇ모");
            // listFlag = true;
             /*   if(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT.equals(primitive)){
                    initDataFlag=false;
                }*/
            Log.println(Log.ASSERT, TAG, "progressDialog dismiss() 1104줄");
            Log.println(Log.ASSERT, TAG, "primitive 확인:" + primitive);
            Log.println(Log.ASSERT, TAG, "result 확인");
            Log.println(Log.ASSERT, TAG, "result 확인");
            Log.println(Log.ASSERT, TAG, "result 확인");
            Log.println(Log.ASSERT, TAG, "result 확인");
            Log.println(Log.ASSERT, TAG, "result 확인");
            Log.println(Log.ASSERT, TAG, "result 확인");


//                Log.println(Log.ASSERT,TAG,result.toString());
            // 2020.12 터널pdf다운로드 url
            if (primitive.equals(ONECLICK_TUNNEL_DOWNLOAD)) {

                String filedownUrl = "";
                String gubun = "";
                String compress = "";
                String filename = "";

                try {
                    //JSONObject obj = new JSONObject(baseResponse);
                    System.out.println("baseResponse : " + baseResponse);

                    JSONArray objArr = new JSONArray(baseResponse);

                    System.out.println("objArr" + objArr.toString());


           /*         for (int i = 0; i < objArr.length(); i++) {
                        JSONObject obj = (JSONObject) objArr.get(i);
                        System.out.println("obj 1= " + obj.toString());
                        boolean isURL = obj.has("filedownUrl");
                        System.out.println("isURL = " + isURL);


                        if( isURL == true &&  i == 0 ){

                            System.out.println("filedownUrl = " + obj.getString("filedownUrl"));
                            filedownUrl = obj.getString("filedownUrl");
                            reGubun1 = obj.getString("gubun");
                            compress1 = obj.getString("compress");
                            filename1 = URLDecoder.decode(obj.getString("filename1"));

                            filedownUrl = filedownUrl.replace("\\","");

                            System.out.println("filename 1 = " + filename1);

                           jsonObject1.put("filedownUrl",filedownUrl);
                           jsonObject1.put("compress1",compress1);
                           jsonObject1.put("filename1",filename1);


                        }
                        if(isURL == true &&  i == 1){
                            System.out.println("filedownUrl1 = " + obj.getString("filedownUrl"));
                            filedownUrl = obj.getString("filedownUrl");
                            reGubun2 = obj.getString("gubun");
                            compress2 = obj.getString("compress");
                            filename2 = URLDecoder.decode(obj.getString("filename2"));

                            filedownUrl = filedownUrl.replace("\\","");


                            jsonObject2.put("filedownUrl",filedownUrl);
                            jsonObject2.put("compress2",compress1);
                            jsonObject2.put("filename2",filename1);

                        }
                        if(isURL == true &&  i == 2){
                            System.out.println("filedownUrl1 = " + obj.getString("filedownUrl"));
                            filedownUrl = obj.getString("filedownUrl");
                            reGubun3 = obj.getString("gubun");
                            compress3 = obj.getString("compress");
                            filename3 = URLDecoder.decode(obj.getString("filename3"));

                            filedonwURL3 = filedonwURL.replace("\\","");


                            jsonObject3.put("filedownUrl",filedownUrl);
                            jsonObject3.put("compress3",compress3);
                            jsonObject3.put("filename3",filename3);

                        }
                        if(isURL == true &&  i == 3){

                            System.out.println("filedownUrl1 = " + obj.getString("filedownUrl"));
                            filedownUrl = obj.getString("filedownUrl");
                            reGubun4 = obj.getString("gubun");
                            compress4 = obj.getString("compress");
                            filename4 = URLDecoder.decode(obj.getString("filename4"));

                            filedonwURL4 = filedonwURL.replace("\\","");

                            jsonObject4.put("filedownUrl",filedownUrl);
                            jsonObject4.put("compress4",compress1);
                            jsonObject4.put("filename4",filename1);

                        }
                        jsonArr.put(jsonObject1);
                        jsonArr.put(jsonObject2);
                        jsonArr.put(jsonObject3);
                        jsonArr.put(jsonObject4);
                    }*/

                    showTunnelDialog(objArr);


                } catch (Exception e11) {
                    e11.printStackTrace();
                }
            } else {
                //progressDialog.dismiss();
                String rtnResultCode = Common.nullCheck(result.get("result"));//               if ("1000".equals(Common.nullCheck(result.get("result")))) {
                Log.println(Log.ASSERT, TAG, "rtnResultCode: 이거 왜안뜨냐");
                Log.println(Log.ASSERT, TAG, "rtnResultCode: " + rtnResultCode);
                Log.println(Log.ASSERT, TAG, "rtnResultCode: " + Common.nullCheck(result.get("rowno")));
                pastItem = new Patrol();
                if ("1000".equals(rtnResultCode)) {
                    nowUPT_CARTIME = Common.nullCheck(result.get("update_cartime"));

                    nowUPT_RPTID = Common.nullCheck(result.get("update_rpt_id"));
                    Log.d("", "isplay  nowUPT_RPTID = " + nowUPT_RPTID + ":" + nowUPT_CARTIME);
                    pastItem.setRpt_id(nowUPT_RPTID);
                    pastItem.setUpdate_cartime(nowUPT_CARTIME);
                    //ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
                    if (primitive.equals(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT) || primitive.equals(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT) || primitive.equals(GET_JUBBO_LIST_SELECT)) {

                        /*  if (true) {*/
                        Log.d("", "list of resultCode = " + rtnResultCode);
                        result.setList("entity");
                        Patrol item = null;
                        itemList = new ArrayList<Patrol>();

                        Map<String, String> startCountMap = new HashMap<String, String>();

                        for (int i = 0; i < result.size(); i++) {

                            String rpt_id = Common.nullCheck(result.get(i, "rpt_id"));
                            String bscode = Common.nullCheck(result.get(i, "bscode"));
                            String reg_date = Common.nullCheck(result.get(i, "reg_date"));
                            String reg_time = Common.nullCheck(result.get(i, "reg_time"));
                            String reg_type = Common.nullCheck(result.get(i, "reg_type"));
                            String reg_info = Common.nullCheck(result.get(i, "reg_info"));
                            String reg_data = Common.nullCheck(result.get(i, "reg_data"));
                            String inp_val = Common.nullCheck(result.get(i, "inp_val"));
                            String local_way = Common.nullCheck(result.get(i, "local_way"));
                            String start_km = Common.nullCheck(result.get(i, "start_km"));
                            String lane_num = Common.nullCheck(result.get(i, "lane_num"));
                            String psn_tel_no = Common.nullCheck(result.get(i, "psn_tel_no"));
                            String reg_tel_no = Common.nullCheck(result.get(i, "reg_tel_no"));
                            String r_result = Common.nullCheck(result.get(i, "r_result"));
                            String local_nm = Common.nullCheck(result.get(i, "local_nm"));
                            String direction_name = Common.nullCheck(result.get(i, "direction_name"));
                            String startcount = Common.nullCheck(result.get(i, "startcount"));
                            String end_yn = Common.nullCheck(result.get(i, "end_yn"));
                            String etc = Common.nullCheck(result.get(i, "etc"));
                            String end_time = Common.nullCheck(result.get(i, "end_time"));
                            String use_yn = Common.nullCheck(result.get(i, "use_yn"));
                            String modegubun = Common.nullCheck(result.get(i, "modegubun"));

                            //
                            String road_id = Common.nullCheck(result.get(i, "road_id"));
                            String route_no = Common.nullCheck(result.get(i, "route_no"));
                            String end_km = Common.nullCheck(result.get(i, "end_km"));
                            String roadNo = Common.nullCheck(result.get(i, "roadNo"));
                            String acdnt_id = Common.nullCheck(result.get(i, "acdnt_id"));
                            String stpnt_ic_fclts_intg_id = Common.nullCheck(result.get(i, "stpnt_ic_fclts_intg_id"));


//------------------------------acdnt_id

                            item = new Patrol();
                            item.setRoad_id(road_id);
                            item.setRoute_no(route_no);
                            item.setEnd_km(end_km);
                            item.setRpt_id(rpt_id);
                            item.setBscode(bscode);
                            item.setReg_date(reg_date);
                            item.setReg_time(reg_time);
                            item.setReg_type(reg_type);
                            item.setReg_info(reg_info);
                            item.setReg_data(reg_data);
                            item.setInp_val(inp_val);
                            item.setLocal_way(local_way);
                            item.setStart_km(start_km);
                            item.setLane_num(lane_num);
                            item.setPsn_tel_no(psn_tel_no);
                            item.setReg_tel_no(reg_tel_no);
                            item.setR_result(r_result);
                            item.setLocal_nm(local_nm);
                            item.setDirection_name(direction_name);
                            item.setStartcount(startcount);
                            item.setEnd_yn(end_yn);
                            item.setEtc(etc);
                            item.setEnd_time(end_time);
                            item.setUse_yn(use_yn);
                            item.setModegubun(modegubun);
                            item.setRoadNo(roadNo);
                            item.setAcdnt_id(acdnt_id);

                            //2020.12 터널화재
                            item.setStpnt_ic_fclts_intg_id(stpnt_ic_fclts_intg_id);
                            //item.setStpnt_ic_fclts_intg_id("F2200003420");

                            selectedPosition = -1;
                            Log.println(Log.ASSERT, TAG, "ROadNo 확인::" + item.getRoadNo());
                            Log.println(Log.ASSERT, TAG, "End kilometer 확인::" + item.getEnd_km());
//------------------------------
                            startCountMap.put(rpt_id, startcount);

                            if (nowUPT_RPTID.equals(rpt_id)) {
                                nowREG_DATE = Common.nullCheck(item.getReg_date());
                            }


                            if ("Y".equals(use_yn)) {

                                if (rpt_id.equals(nowUPT_RPTID)) {
                                    pastItem.setEnd_time(end_time);
                                    pastItem.setEnd_yn(end_yn);
                                    pastItem.setUse_yn(use_yn);
                                    pastItem.setReg_type(reg_type);
                                }
                                itemList.add(item);
                            }

                        }//for

                        Log.println(Log.ASSERT, TAG, "1323");
                        //bonbuList=new ArrayList<Map<String,String>>();


                        // jisaMap = new HashMap<String,String>();
                        if (primitive.equals(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT)) {

                            result.setList("bonbuEntity");
                            Log.println(Log.ASSERT, TAG, "본부 엔티티 사이즈:" + result.size());

                            for (int i = 0; i < result.size(); i++) {
                                Log.println(Log.ASSERT, TAG, Integer.toString(i));
                                bonbuMap = new HashMap<String, String>();
                                bonbuMap.put("bonbuNm", result.getChild(i).get("bonbuNm"));
                                bonbuMap.put("bonbuCd", result.getChild(i).get("bonbuCd"));
                                //Log.println(Log.ASSERT,TAG,"본부 네임:"+result.getChild(i).get("bonbuNm"));
                                //Log.println(Log.ASSERT,TAG,"본부 코드:"+result.getChild(i).get("bonbuCd"));
                                //Log.println(Log.ASSERT,TAG,"지사 코드:"+result.getChild(i).get("jisaCd"));
                                // Log.println(Log.ASSERT,TAG,"지사 네임:"+result.getChild(i).get("jisaNm"));
                                bonbuList.add(bonbuMap);

                            }
                            setBonbuAdapter();
                        }

                        Log.println(Log.ASSERT, TAG, bonbuList.size() + "");


                        displayGallery();

                    } else if (GET_JISA_LIST.equals(primitive)) {
                        result.setList("jisaEntity");
                        Log.println(Log.ASSERT, TAG, "1397번째줄");
                        jisaList.clear();
                        for (int i = 0; i < result.size(); i++) {
                            Log.println(Log.ASSERT, TAG, result.getChild(i).get("jisaCd"));
                            Log.println(Log.ASSERT, TAG, result.getChild(i).get("jisaNm"));
                            jisaMap = new HashMap<String, String>();
                            jisaMap.put("jisaCd", result.getChild(i).get("jisaCd"));
                            jisaMap.put("jisaNm", result.getChild(i).get("jisaNm"));
                            jisaList.add(jisaMap);
                        }

                        setJisaAdapter();

                    } else if (GET_CCTV_URL_LIST.equals(primitive)) {
                        start_cctv_url = result.get("start_cctv");

                        end_cctv_url = result.get("end_cctv");
                        ;
                        Log.println(Log.ASSERT, TAG, "start_cctv_url : = " + start_cctv_url);
                        Log.println(Log.ASSERT, TAG, "start_cctv_url : = " + end_cctv_url);
                        startCCtvActivty(start_cctv_url, end_cctv_url, sortItemList.get(selectedPosition));
                    } else if (ONECLICK_GET_TUNNELNAME.equals(primitive)) {
                        //2020.12 터널명
                        Log.i("ONECLICK_GET_TUNNELNAME", "onActionPost 1");

                        tunnelName = Common.nullCheck(result.get("Tunl_nm"));
                        String fclts_intg_id = Common.nullCheck(result.get("Fclts_intg_id"));

                        System.out.println("TunnelName = " + tunnelName);

                        TunnelNameVo itemTN = null;
                        itemTN = new TunnelNameVo();

                        itemTN.setTunnelName(tunnelName);
                        itemTN.setFclts_intg_id(fclts_intg_id);

                        items.add(itemTN);

                        Log.i("items @@@@@@@@@", items.get(0).getTunnelName().toString());

                        //tunnelTitle.setText(tunnelName);
                        //getTunnelDonwUrl();
                        System.out.println("getTunnDownloadUrl before");
                        getTunnelDownloadUrl(tunnelName);

                    }
                } else if ("1001".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_PATROLRCEPTINFO_SELECT)) {
                        Log.d("", "list of resultCode = " + rtnResultCode);
                        nowUPT_CARTIME = Common.nullCheck(result.get("date"));
                    }
                    Log.println(Log.ASSERT, TAG, "result코드 1001");
                }
            }
        } catch (XmlPullParserException a) {
            Log.e("", "Error occured ActionList onPostExecute XmlPullParserException");
        } finally {//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
            /* if(ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)){*/
            if (ONECLICK_EMPLOYEE_GET_INITDATA_SELECT.equals(primitive) || ONECLICK_EMPLOYEE_RECEPTINFO_SELECT.equals(primitive)) {
                if (progressDialog != null) {
                    listFlag = true;
                    Log.println(Log.ASSERT, TAG, "progressDialog dismiss() 1231줄");
                    progressDialog.dismiss();
                }
                ProgressFlag = false;
                if (updateDataCheck == true) {
                    updateDataCheck = false;
                }
            }
            if (ONECLICK_TUNNEL_DOWNLOAD.equals(primitive)) {
                if (progressDialog != null) {
                    listFlag = true;
                    Log.println(Log.ASSERT, TAG, "progressDialog dismiss() 1231줄");
                    progressDialog.dismiss();
                }
            }
        }

    }


    @Override
    public void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.println(Log.ASSERT, TAG, "onPause");
        db.close();
        db.init();

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (updateDataCheck != false) {
            updatedJupboSelected();
        }


        contextActivity = InnerEmployActivity.this;

        VIEW_TAG = VIEW_PATROL;
        db.close();
        db.init();
//		stopGPS();
//		startMyGps();
//		stopTimer(this);
//		startTimer();
        // stopTimer_list();

        Log.println(Log.ASSERT, TAG, "startTimer_Inner_list 바로전");
        Fragment f1 = fragmentmanager.findFragmentByTag("cctv");
        if (f1 == null) {
            startTimer_Inner_list(GET_JUBBO_LIST_SELECT);//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
            //startTimer_Inner_list(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT);
            Patrol pastItem = new Patrol();
            Patrol ppastItem = new Patrol();

            // startTimer_list();
            db.deleteRptId();
        }
        displayFlag = true;


        if ("Y".equals(SituationService.patrolOKFlag)) {
            //   patrolOK.setChecked(true);
        } else {
            //    patrolOK.setChecked(false);
        }


        // setStartBtn();
//		displayGallery();
    }


    //슬라이드 메뉴 해보기
    /**
     * 순찰,견인 목록 조회 타이머 5초단위 실행
     */
    static Timer listTimer = new Timer();
    static Handler listHandler = new Handler();

    public static void stopTimer_list() {
        if (null != listTimer) {
            listTimer.cancel();
        }
    }

    public void startCCtvActivty(String start_cctv_url, String end_cctv_url, Patrol patrol) {
        isLanding = true;
        try {
            transactionCommit();
            Log.println(Log.ASSERT, TAG, "startCCtvActivty");
            //Toast.makeText(this,"눌림",Toast.LENGTH_SHORT).show();
            //InnerEmployeeCCTV cctv =InnerEmployActivity().InnerEmployeeCCTV ;
            //transaction.replace(R.id.frag_container,cctv);
            //   cctv = new InnerEmployCCTVFragment();
            cctv = InnerEmployCCTVFragment.getInstance();
            ds = new Bundle();
            ds.putString("start_cctv_url", start_cctv_url);
            ds.putString("end_cctv_url", end_cctv_url);
            ds.putString("detailTitle", detailTitle.getText().toString());
            ds.putString("detailJeopbo", detailJeopbo.getText().toString());
            ds.putString("detailType", detailType.getText().toString());
            ds.putString("detailTime", detailTime.getText().toString());
            ds.putString("detailState", detailState.getText().toString());
            ds.putString("detailContent", detailContent.getText().toString());
            ds.putString("detailJochi", detailJochi.getText().toString());

            //
            ds.putString("orientation", "portrait");

            cctv.setArguments(ds);
            transaction.replace(R.id.fl_activity_main_container, cctv, "cctv");

            Log.i("startCCtv", ds.toString());
            //transaction.commitAllowingStateLoss();
            transaction.commit();
        } catch (NullPointerException e) {
            Log.e("에러", "NullPointerException");
        } catch (Exception e) {
            Log.e("에러", "NullPointerException");
        }
    }

    public void setPDFDownBtn() {


    }

    //주석
    @Override
    public void onClick(View view) {
        super.onClick(view);
        //최창유 주석
        if (Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE) || Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
            switch (view.getId()) {
                case R.id.cctv_tv:
                    if (selectedPosition == -1) {
                        Toast.makeText(this, "접보를 선택하여 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
                        Patrol past = sortItemList.get(selectedPosition);
                        if (past.getStart_km() == null || past.getEnd_km() == null || past.getStart_km() == "" || past.getEnd_km() == "") {
                            Toast.makeText(this, "해당 접보는 CCTV화면을 조회 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {


                            Log.println(Log.ASSERT, TAG, "선택된 접보확인");
                            Log.println(Log.ASSERT, TAG, sortItemList.get(selectedPosition).toString());

                            getCCTVURL(past, GET_CCTV_URL_LIST);
                        }
                    }
                    break;
                case R.id.testBtn:
                    // Toast.makeText(this,"1눌림",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.situattion_select_tv:
                    if (selectedPosition == -1) {
                        //          if (false) {*//*
                        Toast.makeText(this, "접보를 선택하여 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Patrol past = sortItemList.get(selectedPosition);
                        Log.println(Log.ASSERT, TAG, "선택된 상황");

                        Log.println(Log.ASSERT, TAG, sortItemList.get(selectedPosition).toString());
                        //     if (past.getAcdnt_id() != null && past.getAcdnt_id().length() != 0) {*//*
                        if (past.getAcdnt_id() != null && past.getAcdnt_id().length() != 0) {
                            transactionCommit();
                            news = new InnerEmploySituationSelectFragment();
                            Bundle d = new Bundle();
                            d.putString("rpt_id", past.getRpt_id());
                            d.putString("acdnt_id", past.getAcdnt_id());

                            news.setArguments(d);
                            transaction.replace(R.id.fl_activity_main_container, news, "news");

                            //transaction.commitAllowingStateLoss();
                            transaction.commit();
                        } else {
                            Toast.makeText(this, "해당 접보는 상황조회 대상 접보가 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
                case R.id.situattion_insert_tv:
                    try {
                        Patrol past = sortItemList.get(selectedPosition);
                        if (selectedPosition == -1) {
                            Toast.makeText(this, "접보를 선택하여 주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (past.getAcdnt_id() != null && past.getAcdnt_id().length() != 0) {
                                transactionCommit();
                                insert = new InnerEmployeeSituationInsert();
                                Bundle d = new Bundle();
                                //Patrol past = sortItemList.get(selectedPosition);
                                d.putString("rpt_id", past.getRpt_id());
                                d.putString("acdnt_id", past.getAcdnt_id());//
                                d.putString("detailTitle", detailTitle.getText().toString());
                                d.putString("detailJeopbo", detailJeopbo.getText().toString());
                                d.putString("detailType", detailType.getText().toString());
                                d.putString("detailTime", detailTime.getText().toString());
                                d.putString("detailState", detailState.getText().toString());
                                d.putString("detailContent", detailContent.getText().toString());
                                d.putString("detailJochi", detailJochi.getText().toString());
                                d.putString("r_result", sortItemList.get(selectedPosition).getR_result());
                                d.putString("rpt_id", sortItemList.get(selectedPosition).getRpt_id());
                                d.putString("rprq_ctnt", sortItemList.get(selectedPosition).getReg_data());
                                //params.put("bscode", Configuration.User.getBscode_list().get(0));
                                d.putString("bscode", Configuration.User.getBscode_list().get(0));
                                d.putString("bscodeName", Configuration.User.getBsname_list().get(0));
                                insert.setArguments(d);
                                transaction.replace(R.id.fl_activity_main_container, insert, "insert");

                                //transaction.commitAllowingStateLoss();
                                transaction.commit();
                            } else {
                                Toast.makeText(this, "해당 접보는 상황등록 대상 접보가 아닙니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (NullPointerException ne) {
                        Toast.makeText(this, "선택된 접보가 없습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("예외", "예외발생");

                    } catch (Exception e) {
                        Toast.makeText(this, "선택된 접보가 없습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("예외", "예외발생");
                    }

                    break;
                case R.id.btnUserInfo:
                    Log.println(Log.ASSERT, TAG, "플래그값 변경 onClick");
                    //  listFlag = false;
                    //progressDialog=null;
                    //   Toast.makeText(this, "테스트", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menu:
                    dlDrawer.openDrawer(Gravity.LEFT);

                    break;
                case R.id.innerPicture:
                    dialogGallery = new Dialog(InnerEmployActivity.this, R.style.FullHeightDialog);

                    dialogGallery.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogGallery.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialogGallery.setContentView(R.layout.cust_dialog_patrol);
                    dialogGallery.setCancelable(true);

                    LinearLayout layoutMain = (LinearLayout) dialogGallery.findViewById(R.id.llMediaMain);
                    layoutMain.setContentDescription("" + detailTitle.getText());

                    TextView tvTitleStartup = (TextView) dialogGallery.findViewById(R.id.tvTitleStartup);
                    tvTitleStartup.setText("" + detailTitle.getText());

                    // 이미지 Gallery
                    ImageView buttonGallery = (ImageView) dialogGallery.findViewById(R.id.goCamera);
                    buttonGallery.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            runCamera();
                            dialogGallery.dismiss();
                        }
                    });

                    // 앨범 Gallery
                    ImageView buttonGallery1 = (ImageView) dialogGallery.findViewById(R.id.goAlbum);
                    buttonGallery1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(InnerEmployActivity.this, AndroidCustomGalleryActivity.class);
                            startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

                            dialogGallery.dismiss();
                        }
                    });
                    // 앨범보기 이동.
                    ImageView buttonGallery2 = (ImageView) dialogGallery.findViewById(R.id.goPreview);
                    buttonGallery2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setType("image/*");
                            startActivity(i);

                        }
                    });
                    // 닫기 버튼
                    ImageView buttonGallery3 = (ImageView) dialogGallery.findViewById(R.id.ibtnClose);
                    buttonGallery3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogGallery.dismiss();
                        }
                    });

                    dialogGallery.show();
                    break;

                //201125_터널화재
                //2020.12 터널pdf

                case R.id.inner_download:

                    try {
                        Patrol past = sortItemList.get(selectedPosition);
                        if (selectedPosition == -1) {
                            Toast.makeText(this, "접보를 선택하여 주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            getTunnelName(past, ONECLICK_GET_TUNNELNAME);
                        }
                    } catch (NullPointerException e) {
                        Toast.makeText(this, "접보를 선택하여 주세요.", Toast.LENGTH_SHORT).show();
                        Log.e("예외", "예외발생");

                    } catch (Exception e) {
                        Toast.makeText(this, "접보를 선택하여 주세요.", Toast.LENGTH_SHORT).show();
                        Log.e("예외", "예외발생");
                    }

                    break;
            }
            return;
        }
    }

    // 2020.12 터널명
    public void getTunnelName(Patrol patrol, String primitive) {

/*
            Patrol past = sortItemList.get(selectedPosition);
            getTunnelName(past, ONECLICK_GET_TUNNELNAME);*/
        if (true) {

            Log.i("ONECLICK_GET_TUNNELNAME", "getTunnelName");
            Parameters params = new Parameters(primitive);
            params.put("stpnt_ic_fclts_intg_id", patrol.getStpnt_ic_fclts_intg_id());
            Log.i("ONECLICK_GET_TUNNELNAME", patrol.getStpnt_ic_fclts_intg_id().toString());
            new Action(primitive, params).execute("");
        }
    }

    // 2020.12 터널PDF 다운로드 url가져오기
    public void getTunnelDownloadUrl(String tunlnm) {
        try {
            System.out.println("getTunnelDownloadUrl");
            Parameters params = new Parameters("ONECLICK_TUNNEL_DOWNLOAD");
//            params.put("tunlnm", URLEncoder.encode("소래터널", "euc-kr"));
            // byte[] encryptBytes = seed.encrypt(tunlnm, szKey);
            byte[] encryptBytes = seed.encrypt(tunlnm, szKey);
            String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes)));

            params.put("tunlnm", encString);
            params.put("gubun", "1");

            new Action(ONECLICK_TUNNEL_DOWNLOAD, params).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //2020.12 터널 pdf 다운로드 getTunnelDownloadUrl url 수집후 다이얼로드 띄워주
    public void showTunnelDialog(JSONArray jsonArr) throws JSONException {


        JSONArray objArr = jsonArr;

        Log.i("ONECLICK_GET_TUNNELNAME", "button");

        System.out.println("Download PDF!!!!!!!!!!!!!!!!!");

        dialogPDFdown = new Dialog(InnerEmployActivity.this, R.style.FullHeightDialog);

        //dialogPDFdown = new Dialog(InnerEmployActivity.this);
        dialogPDFdown.setContentView(R.layout.cust_dialog_pdfdown);
        tunnelTitle = (TextView) dialogPDFdown.findViewById(R.id.tunnelTitle);
        tunnelTitle.setText(tunnelName);
        LinearLayout layoutTitle = (LinearLayout) dialogPDFdown.findViewById(R.id.pdfDownload);
        layoutTitle.setContentDescription(("" + tunnelTitle.getText()));

        ImageView btOutline = (ImageView) dialogPDFdown.findViewById(R.id.pdf_ouline);
        ImageView btContacts = dialogPDFdown.findViewById(R.id.pdf_contacts);
        ImageView btFloorplan = (ImageView) dialogPDFdown.findViewById(R.id.pdf_floorplan);
        ImageView btOther = (ImageView) dialogPDFdown.findViewById(R.id.pdf_other);
        ImageView btCancle = (ImageView) dialogPDFdown.findViewById(R.id.pdf_cancle);


        for (int i = 0; i < objArr.length(); i++) {

            if (i == 0) {//개요도
                if (((JSONObject) objArr.get(i)).has("filedownUrl") == false) {
                    btOutline.setEnabled(false);
                    btOutline.setAlpha(.5f);
                } else {
                    final String url = ((JSONObject) objArr.get(i)).getString("filedownUrl");
                    final String filename = URLDecoder.decode(((JSONObject) objArr.get(i)).getString("filename"));
                    final String compress = ((JSONObject) objArr.get(i)).getString("compress");
                    btOutline.setEnabled(true);
                    btOutline.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String fileKind = "개요도";
                            fileDownload(url, filename, compress, fileKind);
                        }
                    });
                }
            }


            if (i == 1) {//비상연락망
                if (((JSONObject) objArr.get(i)).has("filedownUrl") == false) {
                    btContacts.setEnabled(false);
                    btContacts.setAlpha(.5f);
                } else {
                    final String url = ((JSONObject) objArr.get(i)).getString("filedownUrl");
                    final String filename = URLDecoder.decode(((JSONObject) objArr.get(i)).getString("filename"));
                    final String compress = ((JSONObject) objArr.get(i)).getString("compress");
                    btContacts.setEnabled(true);
                    btContacts.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String fileKind = "도면";
                            fileDownload(url, filename, compress, fileKind);
                        }
                    });
                }
            }

            if (i == 2) {//도면
                if (((JSONObject) objArr.get(i)).has("filedownUrl") == false) {
                    btFloorplan.setAlpha(.5f);
                    btFloorplan.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), tunnelName + "의 도면이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    final String url = ((JSONObject) objArr.get(i)).getString("filedownUrl");
                    final String filename = URLDecoder.decode(((JSONObject) objArr.get(i)).getString("filename"));
                    final String compress = ((JSONObject) objArr.get(i)).getString("compress");
                    btFloorplan.setEnabled(true);
                    btFloorplan.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("TUNNEL_DOWNLOAD", "button contacts");
                            String fileKind = "도면";
                            fileDownload(url, filename, compress, fileKind);
                        }
                    });
                }
            }

            if (i == 3) {//기타자료
                if (((JSONObject) objArr.get(i)).has("filedownUrl") == false) {
                    btOther.setAlpha(.5f);
                    btOther.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), tunnelName + "의 기타자료가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    final String url = ((JSONObject) objArr.get(i)).getString("filedownUrl");
                    final String filename = URLDecoder.decode(((JSONObject) objArr.get(i)).getString("filename"));
                    final String compress = ((JSONObject) objArr.get(i)).getString("compress");
                    btOther.setEnabled(true);
                    btOther.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("TUNNEL_DOWNLOAD", "button pdf_other");
                            String fileKind = "기타자료";
                            fileDownload(url, filename, compress, fileKind);
                        }
                    });
                }
            }

        }
        //취소
        btCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPDFdown.dismiss();
            }
        });
        dialogPDFdown.show();
    }


    //2020.12 터널화재다운pdf 파일다운로드
    ProgressDialog downlodingState;


    public void fileDownload(String url, String filename, String compress, String fileKind) {

        System.out.println("fileDownload" + url);
        String File_Name = "";

        if ("Y".equals(compress)) {
            File_Name = tunnelName + "_" + fileKind + ".zip";
        } else {
            File_Name = filename;
        }

        String fileURL = url; // URL
        String Save_Path;
        String Save_folder = "/situationdown";

        System.out.println("File_Name = " + File_Name);

        DownloadThread dThread;
        // 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.

        String ext = Environment.getExternalStorageDirectory().toString();
        Save_Path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + Save_folder;
        File dir = new File(Save_Path);
        dir.mkdir();
        downlodingState = ProgressDialog.show(InnerEmployActivity.this, "", "다운로드 중...", true);
        dThread = new DownloadThread(fileURL,
                Save_Path + "/" + File_Name);
        dThread.start();

    }

    // 다운로드 쓰레드로 돌림..
    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL imgurl;
            int Read;
            String fileName;
            try {

                imgurl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
                byte[] tmpByte = new byte[1024];
                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file);

                for (; ; ) {
                    Read = is.read(tmpByte);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }

                is.close();
                fos.close();
                conn.disconnect();


            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);

        }

    }


    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.i("0000 handle 00000", "handle");
            downlodingState.dismiss();
            Toast.makeText(getApplicationContext(), "다운로드가 완료되었습니다.", Toast.LENGTH_LONG).show();

        }

    };


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    public void runCamera() {
        Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity2.class);
        startActivityForResult(cameraIntent, Configuration.IMAGE_CAPTURE);
    }

    public void transactionCommit() {
        fragmentmanager = getFragmentManager();
        transaction = fragmentmanager.beginTransaction();
    }

    //좌측 슬라이드 메뉴
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(
            android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("",
                "SlideMenu flContainer click btn position onOptionItemSelected");
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void detailClear() {
        rpt_bscode = "";
        rpt_reg_type = "";
        rpt_bhPkCode = "";

        detailNo.setText("");
        // 상세화면 뿌려주기
        detailTitle.setText("");
        detailNo.setText("번호 : ");
        detailState.setText("상태  : ");
        detailTime.setText("시간 : ");
        detailJeopbo.setText("접보 : ");
        detailType.setText("유형 : ");
        detailJochi.setText("");
        detailContent.setText("");
    }


    public void displayGallery() {
        //setBonbuAdapter();
        //20201125_터널화재

        Log.i("", TAG + " displayGallery() Call start");

        detailClear();
        if (null != itemList) {
            sortItemList.clear();

            for (int i = 0; i < itemList.size(); i++) {
                if (spinSorting.equals(itemList.get(i).getEnd_yn())) {
                    sortItemList.add(itemList.get(i));
                } else if (spinSorting.equals("")) {
                    sortItemList.add(itemList.get(i));
                }

            }


            //최창유
            Log.println(Log.ASSERT, TAG, "sortItemList 목록 확인");
            Log.println(Log.ASSERT, TAG, "sortItemList 목록 갯수" + sortItemList.size());
            for (int i = 0; i < sortItemList.size(); i++) {
                Log.println(Log.ASSERT, TAG, "sortItemList 목록 갯수" + sortItemList.get(i).toString());
            }


            final InnnerEmployeeListAdapter adapter = new InnnerEmployeeListAdapter(this, sortItemList);
            //progressDialog=null;
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    Log.println(Log.ASSERT, TAG, "537번째 줄--onItemClickListener");

                    Patrol item = sortItemList.get(position);

                    Log.println(Log.ASSERT, TAG, "패트롤 아이템 전부확인:" + item.toString());
                    Log.println(Log.ASSERT, TAG, "패트롤 아이템 확인:" + item.getBscode());
                    Log.println(Log.ASSERT, TAG, "패트롤 아이템 확인:" + item.getLocal_nm());
                    if (Common.nullCheck(item.getReg_type()).contains("7")) {
                        //  btnPatJochiReg.setVisibility(View.VISIBLE);
                    }

                    //2020.12 터널화재
                    if (tunnelImp.equals("01")) {
                        System.out.println("Download Possible");
                        if (Common.nullCheck(item.getReg_type()).contains("0013")) {
                            inner_download.setVisibility(View.VISIBLE);
                        } else {
                            inner_download.setVisibility(View.INVISIBLE);
                        }
                    } else {
                    }

             /*       if (Common.nullCheck(item.getReg_type()).contains("0001")) {
                        inner_download.setVisibility(View.VISIBLE);
                    } else {
                        inner_download.setVisibility(View.INVISIBLE);
                    }*/

                    //확인 내용 내부 DB 저장.(알람용)
                    db.insertRptId(item.getRpt_id(), item.getReg_date());
                    Log.println(Log.ASSERT, TAG, "563번째줄 if 전");
                    // 상세확인 서버 전송.
                    if ("Y".equals(Common.getPrefString(InnerEmployActivity.this, "start_yn"))) {
                        Log.println(Log.ASSERT, TAG, "565번째 줄 if------------");
                        AlertDialog.Builder ad = new AlertDialog.Builder(InnerEmployActivity.this);
                        ad.setMessage("").setTitle("확인").setMessage("* 출동중 다른 제보내용을 \n선택할 수 없습니다. \n 처리완료 후 이용하여 주시기 바랍니다.").setCancelable(false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        ad.show();

                        v.setSelected(false);
                        v.findViewById(R.id.rowPatrolLayout).setSelected(false);
                        v.setActivated(false);
                        v.findViewById(R.id.rowPatrolLayout).setActivated(false);

                        if (null != selectedView) {
                            selectedView.setSelected(true);
                            selectedView.findViewById(R.id.rowPatrolLayout).setSelected(true);
                        }

                        Log.println(Log.ASSERT, TAG, "아직 if임 else는 안ㄱ마");
                        // return;
                    } else {

                        Log.println(Log.ASSERT, TAG, "else임");
                        adapter.setItemImage(position);
                        selectedPosition = position;
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            listView.getChildAt(i).setSelected(false);
                            listView.getChildAt(i).findViewById(R.id.rowPatrolLayout).setSelected(false);
                        }

                        v.findViewById(R.id.rowPatrolLayout).setSelected(false);
                        if (null != selectedView) {
                            selectedView.setSelected(false);
                            selectedView.findViewById(R.id.rowPatrolLayout).setSelected(false);
                        }
                        v.setSelected(true);
                        v.findViewById(R.id.rowPatrolLayout).setSelected(true);
                    }
                }
            });
            // testNo=listView.getSelectedItemPosition();
            //setStartBtn();
            if (null != progressDialog) {
                Log.println(Log.ASSERT, TAG, "progress Dialog dismiss() 바로전 594줄");
                progressDialog.dismiss();
                // progressDialog=null;
            }

        }
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getRpt_id().equals(SituationService.selectedRpt_id)) {
                    listView.setSelection(i);
                    break;
                }
                if (i == itemList.size() - 1) {
                    SituationService.selectedRpt_id = "";
                }
            }

        }
       /* if(null != progressDialog){
            Log.println(Log.ASSERT,TAG,"progress Dialog dismiss() 바로전 611줄");
            progressDialog.dismiss();
        }*/
        Log.i("", TAG + " displayGallery() Call end");
        Log.println(Log.ASSERT, TAG, "displayGallery() Call end");
    }//displayGallary


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
                switch (position) {
                    // 순찰원

                    case 1:
                        Intent i_employee = new Intent(InnerEmployActivity.this, InnerEmployActivity.class);
                        startActivity(i_employee);
                        finish();
                        stopGPS();
                        stopTimer(InnerEmployActivity.this);
                        break;
                    // 순찰원
                    case 2:
                        Intent i_patrol = new Intent(InnerEmployActivity.this, PatrolMainActivity.class);
                        startActivity(i_patrol);
                        finish();
                        stopGPS();
                        stopTimer(InnerEmployActivity.this);
                        break;
                    // 견인원
                    case 3:
                        Intent i_tow = new Intent(InnerEmployActivity.this,
                                TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
                        stopGPS();
                        stopTimer(InnerEmployActivity.this);
                        break;

                    // 대국민
                    case 4:
                        Intent i_citizen = new Intent(InnerEmployActivity.this,
                                MainActivity.class);
                        startActivity(i_citizen);
                        finish();
                        stopGPS();
                        stopTimer(InnerEmployActivity.this);
                        break;

                    default:
                        break;
                }
            } else {
                Toast.makeText(InnerEmployActivity.this, "사용 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            dlDrawer.closeDrawer(lvNavList);
        }

    }

    public class SlideAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private LayoutInflater mInflater;

        public SlideAdapter(Context context, List<String> objects) {
            super(context, 0, objects);

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;

        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LinearLayout llSlide;
            View view = convertView;

            Log.d("", "getview position " + position);
            if (convertView == null) {
                view = mInflater.inflate(R.layout.slide_row, null);
            }
            llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            if (position == 0) {
                llSlide.setBackgroundResource(R.drawable.tit_trans);
            } else if (position == 1) {
                llSlide.setBackgroundResource(R.drawable.inner_item_slide_01);
            } else if (position == 2) {
                llSlide.setBackgroundResource(R.drawable.item_slide_01);
            } else if (position == 3) {
                llSlide.setBackgroundResource(R.drawable.item_slide_02);
            } else if (position == 4) {
                llSlide.setBackgroundResource(R.drawable.item_slide_03);
            }

            return view;
        }
    }

    /*   private void setPatrolOkSet() {
           if (SituationService.patrolOKFlag.equals("Y")) {
               patrolOK.setChecked(false);
               SituationService.patrolOKFlag = "N";
           } else {
               patrolOK.setChecked(true);
               SituationService.patrolOKFlag = "Y";
           }
       }
   */
    public void sortSpinnerAdapter() {
        List<String> list = new ArrayList<String>();// 상태
        list.add("전체");
        list.add("조치완료");
        list.add("처리중");

        // 노선 spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSort.setAdapter(adapter);

        spinSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch (position) {
                    case 0:
                        spinSorting = "";
                        displayGallery();
                        break;
                    case 1:
                        spinSorting = "Y";
                        displayGallery();
                        break;
                    case 2:
                        spinSorting = "N";
                        displayGallery();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinSorting = "";
            }
        });
    }

  /*  public synchronized void setStartBtn() {

        patrolStartIng.setVisibility(View.VISIBLE);
        patrolStart.setVisibility(View.VISIBLE);
        btnPatClear.setVisibility(View.VISIBLE);

        if ("Y".equals(Common.getPrefString(InnerEmployActivity.this, "start_yn"))){
            patrolStart.setVisibility(View.GONE);
            btnPatClear.setVisibility(View.INVISIBLE);
        }else{
            patrolStartIng.setVisibility(View.GONE);
        }

    }*/


    public void startTimer_Inner_list(final String primitive) {
        Log.println(Log.ASSERT, TAG, "startTimer_Inner_list() 들어가자마자 바로");
        Log.println(Log.ASSERT, TAG, "리슽 플래그값 확인");
        Log.println(Log.ASSERT, TAG, Boolean.toString(listFlag));
        TimerTask timertask = new TimerTask() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (listFlag == false) {
                        Log.println(Log.ASSERT, TAG, "return ");
                        return;
                    }
                    Log.println(Log.ASSERT, TAG, "startTimer_Inner_list fi 전 772");
                    String rtnStr = db.fetchBBBsCodeSelected();
                    /*   if(initDataFlag==true){*/
                   /* if(primitive.equals(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT) ){
                        Log.println(Log.ASSERT,TAG,"if 785줄");
                        if(Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)||Configuration.User.getUser_type().equals(USER_TYPE_PATROL)){

                            Parameters params = new Parameters(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT);
                            params.put("sms_group", SituationService.conf.User.getGroup_id());
                            if(Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0 ){

                                params.put("bscode", Configuration.User.getBscode_list().get(0).toString());
                                try {
                                    nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                    nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                params.put("update_cartime", nowUPT_CARTIME);
                                params.put("reg_date", nowREG_DATE);
                                String car_id = "";
                                for (int i = 0; i < Configuration.User.getCrdns_id_list().size(); i++) {
                                    if(i == 0){
                                        car_id = Configuration.User.getCrdns_id_list().get(i);
                                    }else{
                                        car_id += "|"+Configuration.User.getCrdns_id_list().get(i);
                                    }
                                }
                                params.put("car_id", car_id);
                            }

                          *//*  if(Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)){
                                if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                                    params.put("bscode", Configuration.User.getBscode_list().get(0));
                                }
                            }*//*
                            //  listFlag = false;
                            Log.println(Log.ASSERT,TAG,"쓰레드 이푸 직전:");
                            //new ActionInnerList(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT, params).execute("");
                            //   listFlag=false;
                            new ActionInnerList(primitive, params).execute("");
                        }else{
                            Log.println(Log.ASSERT,TAG,"else");
                            Log.d("",TAG+ "time check at Timer" + nowUPT_CARTIME+"  |||  "+SituationService.nowUPT_CARTIME);
                            Log.d("","testtesttesttest 1 ");
                            if(!SituationService.nowUPT_CARTIME.equals(nowUPT_CARTIME)){
                                Log.d("","811번째 줄 ");
//								01-19 09:33:21.034: D/(21042): TowMainActivitytime check at Timer20180119072446  |||  20180119072446
//								if(null != itemList){
                                itemList = SituationService.itemList;
                                nowUPT_CARTIME = SituationService.nowUPT_CARTIME;
//								nowUpperRPTID = SituationService.nowUpperRPTID;
                                nowREG_DATE = SituationService.nowREG_DATE;
                                pastItem = SituationService.pastItem;
                                Log.i("",TAG+ "startTimer_list() -> displayGallery() Call start");
                                displayGallery();
                                Log.i("",TAG+ "startTimer_list() -> displayGallery() Call end");

                                Log.d("","testtesttesttest 2 ");
//								runOnUiThread(new Runnable() {
//									public void run() {
//										displayGallery();
//									}
//								});

                                Log.d("","testtesttesttest 3 ");
                                if(progressDialog != null){
                                    Log.println(Log.ASSERT,TAG,"progressDialog dismiss() 842줄");
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                    ProgressFlag = false;
                                }
//								}

                            }//if
                        }
                        Log.d("","testtesttesttest 4 ");
                        if(ProgressFlag == true){
                            ProgressFlag = false;

                        }else{
                            if(null != progressDialog ){
                                Log.println(Log.ASSERT,TAG,"progressDialog dismiss() 856줄");
                                progressDialog.dismiss();
                                progressDialog = null;
                                // ProgressFlag = false;
                                initDataFlag=false;
                                ProgressFlag=true;
                                listFlag=false;
                            }
                        }
                        Log.d("","testtesttesttest 5 ");

                    }
                    else{
                     *//*   Log.println(Log.ASSERT,TAG,"else 872줄");
                        if(Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)) {

                            Parameters params = new Parameters(primitive);
                            params.put("sms_group", SituationService.conf.User.getGroup_id());
                            if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                             //   params.put("bscode", rtnStr);
                                try {
                                    nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                    nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                params.put("update_cartime", nowUPT_CARTIME);
                                params.put("reg_date", nowREG_DATE);
                                String car_id = "";
                                for (int i = 0; i < Configuration.User.getCrdns_id_list().size(); i++) {
                                    if (i == 0) {
                                        car_id = Configuration.User.getCrdns_id_list().get(i);
                                    } else {
                                        car_id += "|" + Configuration.User.getCrdns_id_list().get(i);
                                    }
                                }
                                params.put("car_id", car_id);
                            }

                            if (Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
                                if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                                    params.put("bscode", Configuration.User.getBscode_list().get(0));
                                }
                            }
                            listFlag = false;
                            Log.println(Log.ASSERT, TAG, "init");
                            //new ActionInnerList(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT, params).execute("");*//*

                        //----------
                        Log.println(Log.ASSERT,TAG,"else 920줄");
                        if(Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)){

                            // Parameters params = new Parameters(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT);
                            Parameters params = new Parameters(primitive);
                            params.put("sms_group", SituationService.conf.User.getGroup_id());
                            if(Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0 ){

                                params.put("bscode", Configuration.User.getBscode_list().get(0).toString());
                                try {
                                    nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                    nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                params.put("update_cartime", nowUPT_CARTIME);
                                params.put("reg_date", nowREG_DATE);
                                String car_id = "";
                                for (int i = 0; i < Configuration.User.getCrdns_id_list().size(); i++) {
                                    if(i == 0){
                                        car_id = Configuration.User.getCrdns_id_list().get(i);
                                    }else{
                                        car_id += "|"+Configuration.User.getCrdns_id_list().get(i);
                                    }
                                }
                                params.put("car_id", car_id);
                            }

                          *//*  if(Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)){
                                if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                                    params.put("bscode", Configuration.User.getBscode_list().get(0));
                                }
                            }*//*
                            listFlag = false;
                            Log.println(Log.ASSERT,TAG,"else");
                            //new ActionInnerList(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT, params).execute("");
                            new ActionInnerList(primitive, params).execute("");
                        }//if
                    }//if-else*/
                }
            };


            @Override
            public void run() {
                //   Log.println(Log.ASSERT, TAG, "쓰레드 run메소드");
                listHandler.post(runnable);
            }
        };
        listTimer = new Timer();
        listTimer.schedule(timertask, 10000, 15000);// 100MS 뒤시작, 1초 간격으로 호출.

    }//startTimer_Inner_list


    // 업데이트 시간 비교
    public String beforeUPT_RPTID = "";
    public String beforeUPT_CARTIME = "";
    public String beforeUse_yn = "";
    public String beforeEnd_yn = "";
    public String nowUPT_RPTID = "";
    public String nowUPT_CARTIME = "";
    public String nowUse_yn = "";
    public String nowEnd_yn = "";
    //	public String nowUpperRPTID = "";
    public String nowREG_DATE = "";

    public Map<String, String> lastStartCountMap = new HashMap<String, String>();
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void getCCTVURL(Patrol patrol, String primitive) {

        if (true) {
            Log.println(Log.ASSERT, TAG, "if 785줄");
            Parameters params = new Parameters(primitive);

            //최창유 주석
            // if(Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)||Configuration.User.getUser_type().equals(USER_TYPE_PATROL)){
            if (true) {
                Log.println(Log.ASSERT, TAG, "1073 if 안에");


                params.put("roadNo", patrol.getRoadNo());
                params.put("start_km", patrol.getStart_km());
                params.put("end_km", patrol.getEnd_km());

                Log.i("getCCTVURL", params.toString());
                          /*  if(Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)){
                                if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                                    params.put("bscode", Configuration.User.getBscode_list().get(0));
                                }
                            }*/
                //  listFlag = false;
                Log.println(Log.ASSERT, TAG, "쓰레드 이푸 직전:");

                //new ActionInnerList(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT, params).execute("");
                //   listFlag=false;
                new Action(primitive, params).execute("");
            }
        }


    }//method getCCTVURL


    // 2020.12 터널pdf다운로드
    public void getTunnelDonwUrl(TunnelNameVo vo, String primitive) {
        System.out.println("getTunnlDonwURL");
        if (true) {

            Parameters params = new Parameters(primitive);


            System.out.println("tunnul name : " + tunnelName);
            params.put("tunlnm", vo.getTunnelName());
            params.put("gubun", vo.getGubun());
            System.out.println(params.toString());

            new Action(primitive, params).execute("");

        }
    }

    public class ActionInnerList extends AsyncTask<String, Void, XMLData> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
        ProgressDialog progressDialog;

        String primitive = "";
        Parameters params = null;

        @Override
        protected void onPreExecute() {//ONECLICK_PATROLRCEPTINFO_SELECT
            Log.println(Log.ASSERT, TAG, "ActionList");//ONECLICK_EMPLOYEE_RECEPTINFO_SELECT
            if (ProgressFlag == true && ONECLICK_EMPLOYEE_RECEPTINFO_SELECT.equals(primitive)) {
                progressDialog = ProgressDialog.show(InnerEmployActivity.this, "", "로딩중...", true);
                ProgressFlag = false;
            }
            super.onPreExecute();
        }

        // primitive 에 따라 URL을 구분짓는다.
        public ActionInnerList(String primitive, Parameters params) {
            this.primitive = primitive;
            this.params = params;
            Log.println(Log.ASSERT, "ActionInnerList", "생성자");
            Log.println(Log.ASSERT, "ActionInnerList", "primitive:" + primitive);
        }


        @Override
        protected XMLData doInBackground(String... arg0) {
            Log.println(Log.ASSERT, "ActionInnerList", "doInBackground");
            Log.println(Log.ASSERT, "ActionInnerList", "primitive 확인:" + primitive);
            HttpURLConnection conn = null;
            XMLData xmlData = null;

            OutputStream os = null;
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            try {
                // test code
                StringBuffer body = new StringBuffer();
                if (ONECLICK_CARGPS_INSERT.equals(primitive)) {
                    Log.println(Log.ASSERT, "ActionInnerList", "1");
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                } else if (ONECLICK_GETUSERINFO_SELECT.equals(primitive)) {
                    Log.println(Log.ASSERT, "ActionInnerList", "2");
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                } else if (ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
                    Log.println(Log.ASSERT, "ActionInnerList", "3");
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;//ONECLICK_EMPLOYEE_RECEPTINFO_SELECT
                } else if (ONECLICK_EMPLOYEE_RECEPTINFO_SELECT.equals(primitive)) {
                    Log.println(Log.ASSERT, "ActionInnerList", "3");
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;
                } else if (ONECLICK_EMPLOYEE_GET_INITDATA_SELECT.equals(primitive)) {//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
                    Log.println(Log.ASSERT, "ActionInnerList", "1001번째줄에서 확인한 primitive:" + primitive);
                    Log.println(Log.ASSERT, "ActionInnerList", "4");
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;
                } else if (GET_JISA_LIST.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;
                } else if (GET_JUBBO_LIST_SELECT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;//GET_CCTV_URL_LIST
                } else if (GET_CCTV_URL_LIST.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;//GET_CCTV_URL_LIST
                } else if (ONECLICK_GET_TUNNELNAME.equals(primitive)) {
                    // 2020.12 터널명
                    Log.i("ONECLICK_GET_TUNNELNAME", "doInBackground");
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;//ONECLICK_GET_TUNNELNAME
                } else if (ONECLICK_TUNNEL_DOWNLOAD.equals(primitive)) {
                    // 2020.12 터널명
                    Log.i("TUNNEL_DONWLOAD", "doInBackground");
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;//ONECLICK_TUNNEL_DONWLOAD
                }
                URL url = new URL(new String(Common.nullTrim(body.toString())
                        .getBytes("EUC-KR"), "Cp1252"));
                // URL url = new URL(new String(body.toString()));

                Log.i(TAG, "URL : = " + body.toString());
                Log.println(Log.ASSERT, TAG, "최창유 ListSize");

                Log.println(Log.ASSERT, TAG, "URL : = " + body.toString());


                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setRequestProperty("Cache-Control", "no-cache");
                // conn.setDoOutput(true);
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                Log.d(TAG, TAG + " ACTION responsecode  " + responseCode + "----" + conn.getResponseMessage());
                Log.println(Log.ASSERT, "ActionInnerList", " ACTION responsecode  " + responseCode + "----" + conn.getResponseMessage());
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;

                    while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    String response = new String(byteData, "euc-kr");
                    System.out.println("First Response Data = " + response);


                    Log.println(Log.ASSERT, TAG, "response 확인");
                    Log.println(Log.ASSERT, TAG, response);

                    // String response = new String(byteData);
                    // Log.d("","responseData  = " + response);
                    if (response == null || response.equals("")) {
                        Log.e("Response is NULL!! ", TAG + "Response is NULL!!");
                    }
                    Map<String, List<String>> headers = conn.getHeaderFields();
                    Iterator<String> it = headers.keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        List<String> values = headers.get(key);
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < values.size(); i++) {
                            sb.append(";" + values.get(i));
                        }
                    }
                    try {
                        xmlData = new XMLData(response);
                        String ret = xmlData.get("result");
                        if ("1000".equals(ret)) {
                            if (ret == null) {
                                // throw new IOException();
                            } else {
                                String retMsg = xmlData.get("resultMessage");
                                // throw new IOException();
                            }
                        }
                    } catch (Exception e) {
                        listFlag = true;
                        ProgressFlag = false;
                    }
                }

            } catch (IOException e) {
                listFlag = true;
                ProgressFlag = false;
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                    }
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }

                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                    }
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
            return xmlData;
        }


        @Override
        protected void onPostExecute(XMLData result) {
            try {
                Log.println(Log.ASSERT, TAG, "onPostExecute 옴옴ㅇ모");
                // listFlag = true;
             /*   if(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT.equals(primitive)){
                    initDataFlag=false;
                }*/
                Log.println(Log.ASSERT, TAG, "progressDialog dismiss() 1104줄");
                Log.println(Log.ASSERT, TAG, "primitive 확인:" + primitive);
                Log.println(Log.ASSERT, TAG, "result 확인");
                Log.println(Log.ASSERT, TAG, "result 확인");
                Log.println(Log.ASSERT, TAG, "result 확인");
                Log.println(Log.ASSERT, TAG, "result 확인");
                Log.println(Log.ASSERT, TAG, "result 확인");
                Log.println(Log.ASSERT, TAG, "result 확인");


//                Log.println(Log.ASSERT,TAG,result.toString());


                //progressDialog.dismiss();
                String rtnResultCode = Common.nullCheck(result.get("result"));//               if ("1000".equals(Common.nullCheck(result.get("result")))) {
                Log.println(Log.ASSERT, TAG, "rtnResultCode: 이거 왜안뜨냐");
                Log.println(Log.ASSERT, TAG, "rtnResultCode: " + rtnResultCode);
                Log.println(Log.ASSERT, TAG, "rtnResultCode: " + Common.nullCheck(result.get("rowno")));
                pastItem = new Patrol();
                if ("1000".equals(rtnResultCode)) {
                    nowUPT_CARTIME = Common.nullCheck(result.get("update_cartime"));

                    nowUPT_RPTID = Common.nullCheck(result.get("update_rpt_id"));
                    Log.d("", "isplay  nowUPT_RPTID = " + nowUPT_RPTID + ":" + nowUPT_CARTIME);
                    pastItem.setRpt_id(nowUPT_RPTID);
                    pastItem.setUpdate_cartime(nowUPT_CARTIME);
                    //ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
                    if (primitive.equals(ONECLICK_EMPLOYEE_RECEPTINFO_SELECT) || primitive.equals(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT) || primitive.equals(GET_JUBBO_LIST_SELECT)) {
                        Log.println(Log.ASSERT, TAG, "1102줄 ㅡㅅ ㅡ");
                        /*  if (true) {*/
                        Log.d("", "list of resultCode = " + rtnResultCode);
                        result.setList("entity");
                        Patrol item = null;
                        itemList = new ArrayList<Patrol>();

                        Map<String, String> startCountMap = new HashMap<String, String>();

                        for (int i = 0; i < result.size(); i++) {

                            String rpt_id = Common.nullCheck(result.get(i, "rpt_id"));
                            String bscode = Common.nullCheck(result.get(i, "bscode"));
                            String reg_date = Common.nullCheck(result.get(i, "reg_date"));
                            String reg_time = Common.nullCheck(result.get(i, "reg_time"));
                            String reg_type = Common.nullCheck(result.get(i, "reg_type"));
                            String reg_info = Common.nullCheck(result.get(i, "reg_info"));
                            String reg_data = Common.nullCheck(result.get(i, "reg_data"));
                            String inp_val = Common.nullCheck(result.get(i, "inp_val"));
                            String local_way = Common.nullCheck(result.get(i, "local_way"));
                            String start_km = Common.nullCheck(result.get(i, "start_km"));
                            String lane_num = Common.nullCheck(result.get(i, "lane_num"));
                            String psn_tel_no = Common.nullCheck(result.get(i, "psn_tel_no"));
                            String reg_tel_no = Common.nullCheck(result.get(i, "reg_tel_no"));
                            String r_result = Common.nullCheck(result.get(i, "r_result"));
                            String local_nm = Common.nullCheck(result.get(i, "local_nm"));
                            String direction_name = Common.nullCheck(result.get(i, "direction_name"));
                            String startcount = Common.nullCheck(result.get(i, "startcount"));
                            String end_yn = Common.nullCheck(result.get(i, "end_yn"));
                            String etc = Common.nullCheck(result.get(i, "etc"));
                            String end_time = Common.nullCheck(result.get(i, "end_time"));
                            String use_yn = Common.nullCheck(result.get(i, "use_yn"));
                            String modegubun = Common.nullCheck(result.get(i, "modegubun"));

                            //
                            String road_id = Common.nullCheck(result.get(i, "road_id"));
                            String route_no = Common.nullCheck(result.get(i, "route_no"));
                            String end_km = Common.nullCheck(result.get(i, "end_km"));
                            String roadNo = Common.nullCheck(result.get(i, "roadNo"));
                            String acdnt_id = Common.nullCheck(result.get(i, "acdnt_id"));

//------------------------------acdnt_id

                            item = new Patrol();
                            item.setRoad_id(road_id);
                            item.setRoute_no(route_no);
                            item.setEnd_km(end_km);
                            item.setRpt_id(rpt_id);
                            item.setBscode(bscode);
                            item.setReg_date(reg_date);
                            item.setReg_time(reg_time);
                            item.setReg_type(reg_type);
                            item.setReg_info(reg_info);
                            item.setReg_data(reg_data);
                            item.setInp_val(inp_val);
                            item.setLocal_way(local_way);
                            item.setStart_km(start_km);
                            item.setLane_num(lane_num);
                            item.setPsn_tel_no(psn_tel_no);
                            item.setReg_tel_no(reg_tel_no);
                            item.setR_result(r_result);
                            item.setLocal_nm(local_nm);
                            item.setDirection_name(direction_name);
                            item.setStartcount(startcount);
                            item.setEnd_yn(end_yn);
                            item.setEtc(etc);
                            item.setEnd_time(end_time);
                            item.setUse_yn(use_yn);
                            item.setModegubun(modegubun);
                            item.setRoadNo(roadNo);
                            item.setAcdnt_id(acdnt_id);
                            selectedPosition = -1;
                            Log.println(Log.ASSERT, TAG, "ROadNo 확인::" + item.getRoadNo());
                            Log.println(Log.ASSERT, TAG, "End kilometer 확인::" + item.getEnd_km());
//------------------------------
                            startCountMap.put(rpt_id, startcount);

                            if (nowUPT_RPTID.equals(rpt_id)) {
                                nowREG_DATE = Common.nullCheck(item.getReg_date());
                            }


                            if ("Y".equals(use_yn)) {

                                if (rpt_id.equals(nowUPT_RPTID)) {
                                    pastItem.setEnd_time(end_time);
                                    pastItem.setEnd_yn(end_yn);
                                    pastItem.setUse_yn(use_yn);
                                    pastItem.setReg_type(reg_type);
                                }
                                itemList.add(item);
                            }

                        }//for
                        Log.println(Log.ASSERT, TAG, "1323");
                        //bonbuList=new ArrayList<Map<String,String>>();


                        // jisaMap = new HashMap<String,String>();
                        if (primitive.equals(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT)) {

                            result.setList("bonbuEntity");
                            Log.println(Log.ASSERT, TAG, "본부 엔티티 사이즈:" + result.size());

                            for (int i = 0; i < result.size(); i++) {
                                Log.println(Log.ASSERT, TAG, Integer.toString(i));
                                bonbuMap = new HashMap<String, String>();
                                bonbuMap.put("bonbuNm", result.getChild(i).get("bonbuNm"));
                                bonbuMap.put("bonbuCd", result.getChild(i).get("bonbuCd"));
                                //Log.println(Log.ASSERT,TAG,"본부 네임:"+result.getChild(i).get("bonbuNm"));
                                //Log.println(Log.ASSERT,TAG,"본부 코드:"+result.getChild(i).get("bonbuCd"));
                                //Log.println(Log.ASSERT,TAG,"지사 코드:"+result.getChild(i).get("jisaCd"));
                                // Log.println(Log.ASSERT,TAG,"지사 네임:"+result.getChild(i).get("jisaNm"));
                                bonbuList.add(bonbuMap);

                            }
                        }

                        Log.println(Log.ASSERT, TAG, bonbuList.size() + "");
                        setBonbuAdapter();
                        displayGallery();

                    } else if (GET_JISA_LIST.equals(primitive)) {
                        result.setList("jisaEntity");
                        Log.println(Log.ASSERT, TAG, "1397번째줄");
                        jisaList.clear();
                        for (int i = 0; i < result.size(); i++) {
                            Log.println(Log.ASSERT, TAG, result.getChild(i).get("jisaCd"));
                            Log.println(Log.ASSERT, TAG, result.getChild(i).get("jisaNm"));
                            jisaMap = new HashMap<String, String>();
                            jisaMap.put("jisaCd", result.getChild(i).get("jisaCd"));
                            jisaMap.put("jisaNm", result.getChild(i).get("jisaNm"));
                            jisaList.add(jisaMap);
                        }

                        setJisaAdapter();
                    } else if (GET_CCTV_URL_LIST.equals(primitive)) {
                        start_cctv_url = result.get("start_cctv");

                        end_cctv_url = result.get("end_cctv");
                        ;
                        Log.println(Log.ASSERT, TAG, "start_cctv_url : = " + start_cctv_url);
                        Log.println(Log.ASSERT, TAG, "start_cctv_url : = " + end_cctv_url);
                        startCCtvActivty(start_cctv_url, end_cctv_url, sortItemList.get(selectedPosition));
                    } else if (ONECLICK_GET_TUNNELNAME.equals(primitive)) {
                        Log.i("ONECLICK_GET_TUNNELNAME", "onPostExecute");
                        tunnelName = result.get("tunl_nm");

                    }

                } else if ("1001".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_PATROLRCEPTINFO_SELECT)) {
                        Log.d("", "list of resultCode = " + rtnResultCode);
                        nowUPT_CARTIME = Common.nullCheck(result.get("date"));
                    } else if (GET_CCTV_URL_LIST.equals(primitive)) {
                        Log.println(Log.ASSERT, TAG, "cctv_통신실패" + primitive);
                    }
                    Log.println(Log.ASSERT, TAG, "result코드 1001");
                }
            } catch (XmlPullParserException e) {
                /*e.printStackTrace();*/
                Log.e("", "Error occured ActionList onPostExecute:XmlPullParserException");
            } finally {//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
                /* if(ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)){*/
                if (ONECLICK_EMPLOYEE_GET_INITDATA_SELECT.equals(primitive) || ONECLICK_EMPLOYEE_RECEPTINFO_SELECT.equals(primitive) || ONECLICK_GET_TUNNELNAME.equals(primitive)) {
                    if (progressDialog != null) {
                        listFlag = true;
                        Log.println(Log.ASSERT, TAG, "progressDialog dismiss() 1231줄");
                        Log.e("예상", "오");
                        progressDialog.dismiss();
                    }
                    ProgressFlag = false;
                }
            }

        }
    }


    String curRpt_id = "";
    String pastRpt_id = "";
    int isPlayPosition = -1;
    boolean toastRunning = false;

    boolean playFlag = true;
    Patrol ppastItem = new Patrol();
    Patrol pastItem = new Patrol();

    public String getLaneName(String code) {
        if ("0001".equals(code))
            return "1차로";
        if ("0002".equals(code))
            return "2차로";
        if ("0003".equals(code))
            return "3차로";
        if ("0004".equals(code))
            return "4차로";
        if ("0005".equals(code))
            return "5차로";
        return "";
    }

    public class InnnerEmployeeListAdapter extends ArrayAdapter<Patrol> {

        private Context mContext;
        private LayoutInflater mInflater;

        public InnnerEmployeeListAdapter(Context context, List<Patrol> objects) {
            super(context, 0, objects);
            Log.println(Log.ASSERT, TAG, "InnnerEmployeeListAdapter");
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;

            if (objects.size() == 0) {
                if (toastRunning == true) {
                    Toast.makeText(getApplicationContext(), "현재 접수된 상황이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void setItemImage(int position) {
            Patrol item = this.getItem(position); // ArrayAdapter

            //순찰차인경우 gps 전송을 하는 로직 (내부직원은 이걸 막아야댐)
            /*
                2019/12/02 최창유

             */
            /*if("N".equals( item.getEnd_yn() )){
                sendConfirm();//순찰자가 내용을 확인했다는 정보 전송.
            }*/

            SituationService.selectedRpt_id = item.getRpt_id();
            Log.d("", "itemclick = " + SituationService.selectedRpt_id);

            SituationService.rpt_bscode = item.getBscode();
            SituationService.rpt_reg_type = item.getReg_type();

            detailNo.setText(item.getLocal_way());
            if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                detailTitle.setText("상황명: " + TRAINSTR + item.getInp_val());
            } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                detailTitle.setText("상황명: " + PRACTICESTR + item.getInp_val());
            } else {
                detailTitle.setText("상황명: " + item.getInp_val());
            }
            detailNo.setText("번호 : " + (sortItemList.size() - position));
            if (item.getEnd_yn().equals("Y")) {
                detailState.setText("상태  : 조치완료");
            } else {
                detailState.setText("상태  : 처리중");
            }
            try {
                Calendar calendar = Calendar.getInstance();
                Date nowDate = transFormat.parse(item.getReg_date());
                calendar.setTime(nowDate);
                String year = String.format("%04d", calendar.get(Calendar.YEAR));
                String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
                String day = String.format("%02d", (calendar.get(Calendar.DAY_OF_MONTH)));
                String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
                String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
                detailTime.setText("시간 : " + year
                        + "-" + month
                        + "-" + day
                        + " " + hour
                        + ":" + minute
                );
//				date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) +":"+String.format("%02d", c.get(Calendar.MINUTE));
            } catch (ParseException e) {
                detailTime.setText("시간 :");
            }

            detailJeopbo.setText("접보 : " + item.getReg_info());
            detailType.setText("유형 : " + getReg_TypeName(item.getReg_type()));

            String detailText = "";

            detailText = "[" + Common.nullCheck(item.getDirection_name()).replace("||", "]") + "방향 " + item.getStart_km() + "km " + getLaneName(item.getLane_num()) + "\n" + item.getReg_data();

            if (!"".equals(Common.nullCheck(item.getPsn_tel_no()))) {
                detailText += "\n당사자 : " + item.getPsn_tel_no();
            }
            if (!"".equals(Common.nullCheck(item.getReg_tel_no()))) {
                detailText += "\n제보자 : " + item.getReg_tel_no();
            }
            Log.d("", "detailContent settext now " + detailText);
            if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                detailContent.setText(TRAINSTR + detailText);
            } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                detailContent.setText(PRACTICESTR + detailText);
            } else {
                detailContent.setText(detailText);
            }

            // 업체 전화번호버튼
            if (Common.nullCheck(item.getEtc()).length() >= 10) {
                //saupPhone.setVisibility(View.VISIBLE);
            } else {
                // saupPhone.setVisibility(View.GONE);
            }
            if (Common.nullCheck(item.getPsn_tel_no()).length() >= 10) {
                //  psnPhone.setVisibility(View.VISIBLE);
            } else {
                // psnPhone.setVisibility(View.GONE);
            }

            // 고객 전화번호버튼
            if (Common.nullCheck(item.getReg_tel_no()).length() >= 10) {
                // customPhone.setVisibility(View.VISIBLE);
            } else {
                //   customPhone.setVisibility(View.GONE);
            }

        /*    psnPhoneCallInfo = "" + item.getPsn_tel_no();
            saupPhoneCallInfo = "" + item.getEtc();
            customPhoneInfo = "" + item.getReg_tel_no();*/
            detailJochi.setText("" + item.getR_result());
            selectedreg_type = item.getReg_type();
            setRegVisible(item.getReg_type());


        }


        public View getView(int position, View convertView, ViewGroup parent) {

            TextView row_patrol_no;
            TextView row_patrol_time;
            TextView row_patrol_type;
            TextView row_patrol_content;
            TextView row_patrol_startcnt;
            LinearLayout rowPatrolLayout;
            LinearLayout rowPatrolLayouthome;

            View view = convertView;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.patrol_list_row, null);
            }

            final Patrol item = this.getItem(position);
            if (item != null) {
                rowPatrolLayouthome = (LinearLayout) view.findViewById(R.id.rowPatrolLayouthome);
                rowPatrolLayout = (LinearLayout) view.findViewById(R.id.rowPatrolLayout);
                row_patrol_no = (TextView) view.findViewById(R.id.row_patrol_no);
                row_patrol_time = (TextView) view.findViewById(R.id.row_patrol_time);
                row_patrol_type = (TextView) view.findViewById(R.id.row_patrol_type);
                row_patrol_content = (TextView) view.findViewById(R.id.row_patrol_content);
                row_patrol_startcnt = (TextView) view.findViewById(R.id.row_patrol_startcnt);

                row_patrol_no.setText("" + (sortItemList.size() - position));
                row_patrol_time.setText("" + item.getReg_time());
                row_patrol_type.setText("" + getReg_TypeName(item.getReg_type()));

                if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                    row_patrol_content.setText(TRAINSTR + item.getReg_data());
                } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                    row_patrol_content.setText(PRACTICESTR + item.getReg_data());
                } else {
                    row_patrol_content.setText("" + item.getReg_data());
                }
                row_patrol_startcnt.setText(Common.nullZeroCheck("" + item.getStartcount()));

                if (pastItem.getRpt_id().equals(item.getRpt_id())) {
                    row_patrol_no.setTextColor(Color.parseColor("#ff0000ff"));
                    row_patrol_time.setTextColor(Color.parseColor("#ff0000ff"));
                    row_patrol_type.setTextColor(Color.parseColor("#ff0000ff"));
                    row_patrol_content.setTextColor(Color.parseColor("#ff0000ff"));
                    row_patrol_startcnt.setTextColor(Color.parseColor("#ff0000ff"));
                } else {
                    row_patrol_no.setTextColor(Color.parseColor("#000000"));
                    row_patrol_time.setTextColor(Color.parseColor("#000000"));
                    row_patrol_type.setTextColor(Color.parseColor("#000000"));
                    row_patrol_content.setTextColor(Color.parseColor("#000000"));
                    row_patrol_startcnt.setTextColor(Color.parseColor("#000000"));
                }

                if (SituationService.selectedRpt_id.equals(item.getRpt_id())) {
                    rowPatrolLayout.setSelected(true);
                    this.setItemImage(position);
                } else {
                    rowPatrolLayout.setSelected(false);
                }

                if (item.getEnd_yn().equals("Y")) {
                    rowPatrolLayouthome.setBackgroundColor(Color.parseColor("#ffffff"));
                } else if (item.getEnd_yn().equals("N")) {
                    rowPatrolLayouthome.setBackgroundColor(Color.parseColor("#FDCA49"));
                }

                row_patrol_startcnt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!"".equals(Common.nullZeroCheck("" + item.getStartcount()))) {
                            Intent i = new Intent(getApplicationContext(), DialogActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("rpt_id", item.getRpt_id());
                            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
                            try {
                                pi.send();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

            }
            return view;
        }
    }

    public void setBonbuAdapter() {
        //ArrayList<String> listb = bonbuList
        listb.clear();
        listb.add("본부선택");
        for (int i = 0; i < bonbuList.size(); i++) {
            listb.add(bonbuList.get(i).get("bonbuNm"));
        }
        ArrayAdapter<String> bonbuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listb);
        spin_Bonbu.setAdapter(bonbuAdapter);
        //Toast.makeText(this,"선택된 스피너 위치"+spin_Bonbu.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
        //   spin_Bonbu.setSelection(selectedBonbuSpinPosition);
        spin_Bonbu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        break;
                    default:
                        Parameters params = new Parameters(GET_JISA_LIST);
                        params.put("sms_group", SituationService.conf.User.getGroup_id());
                        if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {

                            params.put("bscode", Configuration.User.getBscode_list().get(0).toString());
                            try {
                                nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //params.put("update_cartime", nowUPT_CARTIME);
                            //params.put("reg_date", nowREG_DATE);
                            String car_id = "";
                            for (int m = 0; m < Configuration.User.getCrdns_id_list().size(); m++) {
                                if (i == 0) {
                                    car_id = Configuration.User.getCrdns_id_list().get(m);
                                } else {
                                    car_id += "|" + Configuration.User.getCrdns_id_list().get(m);
                                }
                            }
                            selectedBonbuSpinPosition = i;
                            params.put("car_id", car_id);
                        }


//                        Toast.makeText(view.getContext(),bonbuList.get(i).get("bonbuCd"),Toast.LENGTH_SHORT).show();
                        //    Parameters parameters = new Parameters(GET_JISA_LIST);
                        if (i != 0) {
                           /* if(null == progressDialog){

                                Log.println(Log.ASSERT,TAG,"상황목록 로딩중");
                                progressDialog = ProgressDialog.show(InnerEmployActivity.this, "", "상황목록 로딩중...", true);
                                //ProgressFlag = true;
                                listFlag=true;
                                initDataFlag=true;
                            }*/
                            params.put("bonbuCd", bonbuList.get(i - 1).get("bonbuCd"));
                            new Action(GET_JISA_LIST, params).execute("");
                        }
                        isSelection = true;

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //접보 업데이트가 완료되면 새로 완료된 접보도 조회하기 위한 메소드 추가
    public void updatedJupboSelected() {
        Parameters params = new Parameters(GET_JUBBO_LIST_SELECT);
        params.put("sms_group", SituationService.conf.User.getGroup_id());
        if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {

            params.put("bscode", Configuration.User.getBscode_list().get(0).toString());
            try {
                nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
            } catch (NullPointerException e) {
                Log.e("에러", "NullPointerException");
            }
            //params.put("update_cartime", nowUPT_CARTIME);
            //params.put("reg_date", nowREG_DATE);
            String car_id = "";
            for (int m = 0; m < Configuration.User.getCrdns_id_list().size(); m++) {
                if ((spin_jisa.getSelectedItemPosition()) == 0) {
                    car_id = Configuration.User.getCrdns_id_list().get(m);
                } else {
                    car_id += "|" + Configuration.User.getCrdns_id_list().get(m);
                }
            }
            params.put("car_id", car_id);
            if (spin_jisa.getSelectedItemPosition() != 0) {

                Log.println(Log.ASSERT, TAG, "InnerImployActivity new Action 직전:");
                params.put("jisaCd", jisaList.get(spin_jisa.getSelectedItemPosition() - 1).get("jisaCd"));

                new Action(GET_JUBBO_LIST_SELECT, params).execute("");
            }

        }
    }


    public void setJisaAdapter() {
        // isSelection =true;
        jisaSpinList.clear();
        //
        jisaSpinList.add("지사선택");
        for (int i = 0; i < jisaList.size(); i++) {
            jisaSpinList.add(jisaList.get(i).get("jisaNm"));
        }
        jisaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jisaSpinList);

        spin_jisa.setAdapter(jisaAdapter);
        // spin_jisa.setSelection(selectedJisaSpinPosition);
        isSelection = true;
        spin_jisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(adapterView.getContext(),new Boolean(isSelection).toString(),Toast.LENGTH_SHORT).show();
                Parameters params = new Parameters(GET_JUBBO_LIST_SELECT);
                params.put("sms_group", SituationService.conf.User.getGroup_id());
                if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {

                    params.put("bscode", Configuration.User.getBscode_list().get(0).toString());
                    try {
                        nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                        nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                    } catch (NullPointerException e) {
                        Log.i("에러", "NullPointerException");
                    }
                    //params.put("update_cartime", nowUPT_CARTIME);
                    //params.put("reg_date", nowREG_DATE);
                    String car_id = "";
                    for (int m = 0; m < Configuration.User.getCrdns_id_list().size(); m++) {
                        if (i == 0) {
                            car_id = Configuration.User.getCrdns_id_list().get(m);
                        } else {
                            car_id += "|" + Configuration.User.getCrdns_id_list().get(m);
                        }
                    }

                    selectedJisaSpinPosition = i;
                    params.put("car_id", car_id);
                    isSelection = true;
                    if (i != 0) {

                        Log.println(Log.ASSERT, TAG, "InnerImployActivity new Action 직전:");
                        params.put("jisaCd", jisaList.get(i - 1).get("jisaCd"));
                        new Action(GET_JUBBO_LIST_SELECT, params).execute("");
                    }
                }
            }//end method onItemSelected()

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }//end method onNothingSelected
        });//end spin_jisa.setOnItemSelectedListener
    }//end method setJisaAdapter()

    public String getReg_TypeName(String code) {

        if ("0001".equals(code))
            return "기타";
        if ("0002".equals(code))
            return "차단작업";
        if ("0003".equals(code))
            return "잡물";
        if ("0004".equals(code))
            return "고장차량";
        if ("0005".equals(code))
            return "사고발생";
        if ("0006".equals(code))
            return "지정체";
        if ("0007".equals(code))
            return "긴급견인";
        if ("0008".equals(code))
            return "동물";
        if ("0009".equals(code))
            return "노면(시설물)파손";
        if ("0010".equals(code))
            return "불량차량";
        if ("0011".equals(code))
            return "도로진입제한";
        if ("0012".equals(code))
            return "재난발생";
        if ("0013".equals(code))
            return "터널화재";

        return "";
    }

    public void setRegVisible(String regType) {
        // 사고발생 0005
        // patrolStop.setVisibility(View.GONE);
        //patrolRegist.setVisibility(View.GONE);
        if (regType.equals("0005") || regType.equals("0012") || regType.equals("0013")) {
            Log.d(TAG, TAG + " setRegVisible regtype = 0005");
            //  patrolRegist.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, TAG + " setRegVisible regtype = else");
            // patrolStop.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Log.i("imaimagelistgelist", resultCode + "");
        Parameters params = new Parameters(ONECLICK_FILE_SEND);
        switch (requestCode) {

       /*     case Configuration.PATROL_REG_RETURN:
                Log.d("", "onActivityResult PATROL_REG_RETURN");
                if ("Y".equals(Common.getPrefString(InnerEmployActivity.this, "start_yn"))) {
                    setStartBtn();
                } else {
                    setStartBtn();
                }
                break;
*/
            case Configuration.IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    // 파일 전송.
                    mUriSet = data.getData();
                    params.put("rpt_id", SituationService.selectedRpt_id);
                    params.put("nscode", ns_code);
                    params.put("bhcode", bhCode);
                    params.put("ijung", currentIjung);
                    if (Configuration.User.getBscode_list() != null) {
                        if (Configuration.User.getBscode_list().size() > 0) {
                            params.put("bscode", Configuration.User.getBscode_list().get(0));
                        }
                    }
                    params.put("patcar_id", Configuration.User.getPatcar_id());
                    //   Toast.makeText(this,"여기",Toast.LENGTH_SHORT).show();
                    //executeJob(params, InnerEmployActivity.this);
                }
                break;

            case Configuration.FILE_TYPE_IMAGE:
                // --------------------------------------------------------------------------------------------
                // #region 앨범 > 사진 버튼 클릭 후 리턴
                if (resultCode == RESULT_OK) {
                    // 파일 전송.
                    params.put("rpt_id", SituationService.selectedRpt_id);
                    params.put("nscode", ns_code);
                    params.put("nsname", ns_name);
                    params.put("bhcode", bhCode);
                    params.put("bhname", banghyang);
                    params.put("ijung", currentIjung);

                    if (Configuration.User.getBscode_list() != null) {
                        if (Configuration.User.getBscode_list().size() > 0) {
                            params.put("bscode", Configuration.User.getBscode_list().get(0));
                        }
                    }
                    params.put("patcar_id", Configuration.User.getPatcar_id());
                    //Toast.makeText(this, "조기", Toast.LENGTH_SHORT).show();
                    //202007_수정됨.
                    executeJob(params, InnerEmployActivity.this);

                }// end if

                // #endregion
                // --------------------------------------------------------------------------------------------
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stopTimer_list();
       /* if (!Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)) {
            stopTimer_list();
        }*/
    }

    //AudioManager am = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //
        switch (keyCode) {

            case KeyEvent.KEYCODE_BACK://종료
                if (currentFragment.equals("InnerEmployActivity")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(this);
                    ad.setMessage("").setTitle("종료").setMessage("* 앱 종료시 GPS데이타를 전송하지 않습니다. \n종료 하시겠습니까?").setCancelable(false).setPositiveButton("종료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(InnerEmployActivity.this, SituationService.class);
                            if ("Y".equals(Common.getPrefString(InnerEmployActivity.this, "start_yn"))) {
                                sendStop(true);
                            }
                            stopService(i);
                            finish();
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
                    ad.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    ad.show();
                } else {
                    if (currentFragment.equals("InnerEmployCCTVFragment")) {
                        try {
                            transactionCommit();
                            if (getFragmentManager() != null) {

                                if (cctv == null) {
                                    //        Toast.makeText(this,"cctv 널이랜다",Toast.LENGTH_LONG).show();
                                    //  cctv = InnerEmployCCTVFragment.getInstance();
                                    intent = getIntent();
                                    overridePendingTransition(0, 0);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    //  finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                } else {
                                    getFragmentManager().beginTransaction().remove(cctv).commit();
                                    getFragmentManager().popBackStack();
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                }
                                 /*   getFragmentManager().beginTransaction().remove(cctv).commit();
                                    getFragmentManager().popBackStack();*/
                                  /*  getFragmentManager().beginTransaction().remove(1).commit();
                                    getFragmentManager().popBackStack();*/

                            }

                            isLanding = false;
                            currentFragment = TAG;
                        } catch (NullPointerException e) {
                            Log.e("에러", "NullPointerException");
                        }

                        return true;
                    } else if (currentFragment.equals("InnerEmploySituationSelectFragment")) {


                        getFragmentManager().beginTransaction().remove(news).commit();
                        getFragmentManager().popBackStack();
                        // news.situation_webview=null;
                        if (news.situation_webview != null) {
                            news.situation_webview.destroy();
                        }
                        isLanding = true;
                        currentFragment = TAG;
                        return true;

                    } else if (currentFragment.equals("InnerEmployeeSituationInsert")) {
                        getFragmentManager().beginTransaction().remove(insert).commit();
                        getFragmentManager().popBackStack();
                        // news.situation_webview=null;
                        /*    if(news.situation_webview!=null){
                                news.situation_webview.destroy();
                            }*/
                        currentFragment = TAG;
                        return true;

                    } else if (currentFragment.equals("InnerEmployCCTVFragment_LAND")) {
                        getFragmentManager().beginTransaction().remove(insert).commit();
                        getFragmentManager().popBackStack();
                        isLanding = true;
                        return true;
                    } else if (currentFragment.equals("InnerEmployCCTVFragment")) {
                        getFragmentManager().beginTransaction().remove(insert).commit();
                        getFragmentManager().popBackStack();
                        isLanding = true;
                        return true;
                    }
                }

                break;
            default:
                break;
        }


        return super.onKeyDown(keyCode, event);
    }
}
