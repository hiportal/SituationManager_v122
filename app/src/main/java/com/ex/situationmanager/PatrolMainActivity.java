package com.ex.situationmanager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ex.situationmanager.BaseActivity.Action;
import com.ex.situationmanager.dto.Patrol;
import com.ex.situationmanager.multiphoto.GalleryListMain;
import com.ex.situationmanager.multiphoto.MultiPhotoSelector;
import com.ex.situationmanager.multiphoto.MyGalleryPicker;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import static com.ex.situationmanager.service.SituationService.conf;

@SuppressLint("NewApi")
public class PatrolMainActivity extends BaseActivity   {

    // int position = 0;
    ArrayList<Patrol> itemList;

    ArrayList<Patrol> pastitemList;
    Intent intent;
    TextView detailTitle, detailNo, detailState, detailTime, detailJeopbo,
            detailType, detailJochi, detailContent;
    //ListView listView;
    ListView listView;
    int height = 0;
    ImageView patrolStart, patrolStartIng, patrolStop, patrolPicture, patrolRegist;
    ImageView btnPatClear;
    ImageView btnUserInfo;
    String TAG = "PatrolMainActivity";
    Dialog dialogGallery;
    ImageView btnPatJochiReg;

    LinearLayout llbottomBtnDefault, llbottomBtnPhone;
    ImageView customPhone, patPhone, patPhoneClose;
    TextView patPhone1, patPhone2, patPhone3, patPhone4, patPhone5;
    String customPhoneInfo = "";
    ImageView saupPhone, psnPhone;
    String saupPhoneCallInfo = "";
    String psnPhoneCallInfo = "";
    Spinner spinSort;
    String spinSorting = "N";
    CheckBox patrolOK;
    TextView patrolOkTxt;

    //202007 수정중
    public static String setSelectJubbo = "";
    public static String getSetSelectJubboStart = "";
    // setSelectJubbo만 살려둬야하는지 아니면 둘다 조건으로 걸어둬야하는지 확인 한번 더 하기
    public static String setFixSelectJubbo = "";
    public static String setFixGetJubbo = "";

    private Uri mThumbUri;

    int selectedPosition = -1;

    // *********************************Slide Menu
    // ************************************
    // 변수선언 최창유 주석
    String[] navItems = {"전체", "내부직원", "순찰원", "견인원", "대국민"};

    //String[] navItems = { "전체","순찰원", "견인원", "대국민" };
    //ListView lvNavList;
    ExpandableListView lvNavList;
    ListView lvNavList2;
    FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    // *********************************Slide Menu
    // ************************************
    ImageView menu;
    View selectedView = null;

    String selectedreg_type = "";
    boolean ProgressFlag = false;
    ProgressDialog progressDialog;

    TextView cctv_btn = null;

    //슬라이드바
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

    DrawerItemClickListener drawrListener = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.println(Log.ASSERT, TAG, "PatrolMainActivity onCreate");
        if (conf.USER_PHONE_NUMBER.startsWith(Configuration.NAVIGATION_START_NUMBER)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
            setContentView(R.layout.patrol_main_land);
        } else {
            setContentView(R.layout.patrol_main);
        }

//		Intent i2 = new Intent(PatrolMainActivity.this,PatrolRegActivity.class);
//		i2.putExtra("selectedRpt_id", SituationService.selectedRpt_id);
//		i2.putExtra("customPhoneInfo", customPhoneInfo);
//		startActivityForResult(i2, Configuration.PATROL_REG_RETURN);

        ProgressFlag = true;
        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);
        pastitemList = new ArrayList<Patrol>();

        //202007
        setFixSelectJubbo = "";
        getSetSelectJubboStart = "";
//        setSelectJubbo = "";

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
           //e.printStackTrace();
            Log.e("에러","예외발생");
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("에러","예외발생");
        }


        //사용자가 속한 지사 겸직 리스트 전부 가져옴
        sh = getSharedPreferences("userAllJisaList", MODE_PRIVATE);
        userInfo = sh.getString("userAllInfo", "");


        innerEmployeeList = new ArrayList<String>();
        sunchalJsonList = new ArrayList<String>();
        Log.i("userdsdfsdf", userInfo);
        try {
            jsonArray = new JSONArray(userInfo);
            for (int i = 0; i < jsonArray.length(); i++) {
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
        }


        // *********************************Slide Menu
        lvNavList = (ExpandableListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);
        List<String> objects = new ArrayList<String>();
        for (int i = 0; i < navItems.length; i++) {
            objects.add(navItems[i]);
        }
        ArrayList<ExpandableListAdapter.Item> dataList = new ArrayList<ExpandableListAdapter.Item>();

        ExpandableListAdapter.Item group1 = new ExpandableListAdapter().new Item(navItems[0]);//
        dataList.add(group1);
        ExpandableListAdapter.Item group2 = new ExpandableListAdapter().new Item(navItems[1]);//

        group2.child.addAll(innerEmployeeList);
        dataList.add(group2);
        ExpandableListAdapter.Item group3 = new ExpandableListAdapter().new Item(navItems[2]);//tnsckf
        group3.child.addAll(sunchalJsonList);
        dataList.add(group3);
        ExpandableListAdapter.Item group4 = new ExpandableListAdapter().new Item(navItems[3]);//견인
        group4.child.addAll(sunchalJsonList);
        dataList.add(group4);
        ExpandableListAdapter.Item group5 = new ExpandableListAdapter().new Item(navItems[4]);//견인
        group5.child.addAll(sunchalJsonList);
        dataList.add(group5);

        ExpandableListAdapter apapter1 = new ExpandableListAdapter(this, R.layout.slide_row, R.layout.group_row, dataList);

        //

        lvNavList.setAdapter(apapter1);
        lvNavList.setOnGroupClickListener(apapter1);
        lvNavList.setOnChildClickListener(apapter1);
        //  drawrListener = new DrawerItemClickListener(objects, innerEmployeeList, sunchalJsonList, lvNavList, apapter1);


        //lvNavList.setOnItemClickListener((ExpandableListView.OnItemClickListener) apapter1);//draer_layout_view
        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
        //dlDrawer = (DrawerLayout) findViewById(R.id.draer_layout_view);
        Log.i("940413","4");
        dtToggle = new ActionBarDrawerToggle(PatrolMainActivity.this, dlDrawer,
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

        // *********************************Slide Menuconf.User.getUser_type()


        if (conf.User.getUser_type().equals("0001")) {
            USER_TYPE = USER_TYPE_PATROL;
        } else {
            //USER_TYPE=USER_TYPE_INNEREMPLOYEE;
        }
        listView = (ListView) findViewById(R.id.patListView);
        height = listView.getHeight() / 4;

        patrolStart = (ImageView) findViewById(R.id.patrolStart);
        patrolStartIng = (ImageView) findViewById(R.id.patrolStartIng);
        patrolStop = (ImageView) findViewById(R.id.patrolStop);
        patrolRegist = (ImageView) findViewById(R.id.patrolRegist);
        patrolPicture = (ImageView) findViewById(R.id.patrolPicture);
        btnUserInfo = (ImageView) findViewById(R.id.btnUserInfo);
        patrolOK = (CheckBox) findViewById(R.id.patrolOK);
        patrolOkTxt = (TextView) findViewById(R.id.patrolOkTxt);
        btnPatJochiReg = (ImageView) findViewById(R.id.btnPatJochiReg);

        patrolStart.setOnClickListener(this);
        patrolStartIng.setOnClickListener(this);
        patrolStop.setOnClickListener(this);
        patrolRegist.setOnClickListener(this);
        patrolPicture.setOnClickListener(this);
        btnPatClear = (ImageView) findViewById(R.id.btnPatClear);
        btnPatClear.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        btnPatJochiReg.setOnClickListener(this);

        patrolOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setPatrolOkSet();
            }
        });
        patrolOkTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                patrolOK.performClick();
            }
        });

        llbottomBtnDefault = (LinearLayout) findViewById(R.id.llbottomBtnDefault);
        llbottomBtnPhone = (LinearLayout) findViewById(R.id.llbottomBtnPhone);
        patPhone = (ImageView) findViewById(R.id.patPhone);
        patPhoneClose = (ImageView) findViewById(R.id.patPhoneClose);
        patPhone1 = (TextView) findViewById(R.id.patPhone1);
        patPhone2 = (TextView) findViewById(R.id.patPhone2);
        patPhone3 = (TextView) findViewById(R.id.patPhone3);
        patPhone4 = (TextView) findViewById(R.id.patPhone4);
        patPhone5 = (TextView) findViewById(R.id.patPhone5);

        patPhone.setOnClickListener(this);
        patPhoneClose.setOnClickListener(this);
        patPhone1.setOnClickListener(this);
        patPhone2.setOnClickListener(this);
        patPhone3.setOnClickListener(this);
        patPhone4.setOnClickListener(this);
        patPhone5.setOnClickListener(this);

        saupPhone = (ImageView) findViewById(R.id.saupPhone);
        saupPhone.setOnClickListener(this);
        psnPhone = (ImageView) findViewById(R.id.psnPhone);
        psnPhone.setOnClickListener(this);
        customPhone = (ImageView) findViewById(R.id.customPhone);
        customPhone.setOnClickListener(this);

        detailTitle = (TextView) findViewById(R.id.detailTitle);
        detailNo = (TextView) findViewById(R.id.detailNo);
        detailState = (TextView) findViewById(R.id.detailState);
        detailTime = (TextView) findViewById(R.id.detailTime);
        detailJeopbo = (TextView) findViewById(R.id.detailJeopbo);
        detailType = (TextView) findViewById(R.id.detailType);
        detailJochi = (TextView) findViewById(R.id.detailJochi);
        detailContent = (TextView) findViewById(R.id.detailContent);

        spinSort = (Spinner) findViewById(R.id.spinSort);

        patPhone.setVisibility(View.GONE);
        saupPhone.setVisibility(View.GONE);
        customPhone.setVisibility(View.GONE);

        sortSpinnerAdapter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 화면 켜짐 유지

        TowMainActivity.stopTimer_list();

        //최창유 추가
        InnerEmployActivity.stopTimer_list();



        setStartBtn();
        setPhoneBtnVisible(false);



        Parameters params = new Parameters(ONECLICK_GETPATROLTELNO_SELECT);
        params.put("car_id", Configuration.User.getPatcar_id());
        if (null != Configuration.User.getBscode_list()) {
            if (Configuration.User.getBscode_list().size() > 0) {
                params.put("bscode", Configuration.User.getBscode_list().get(0));
            }
        }

        for (int i = 0; i < Configuration.User.getBsname_list().size(); i++) {
            Log.i("지사 이름", "순번" + i + ",지사이름:" + Configuration.User.getBsname_list().get(i));
        }

        params.put("user_type", Configuration.User.getUser_type());
        params.put("tel_no", Configuration.User.getHp_no());
        Log.println(Log.ASSERT, TAG, "PatrolMainActivity에서의 new Action 직전:");
        Log.println(Log.ASSERT, TAG, Configuration.User.getUser_type());
        Log.i("940413","1");
        new Action(ONECLICK_GETPATROLTELNO_SELECT, params).execute();

        btnPatJochiReg.setVisibility(View.INVISIBLE);

//		detailContent.setTextSize(detailContent.getTextSize()*1.2f);
//		detailContent.setTextSize(detailContent.getTextSize()/1.0f);
//		detailContent.setTextSize( getResources().getDisplayMetrics().densityDpi);
//		detailContent.setTextScaleX(1.3f);
//		patrolRegist.performClick();


        //최창유 CCTV 주석
        cctv_btn = (TextView) findViewById(R.id.cctv_btn);
        cctv_btn.setOnClickListener(this);

        if (!chkGpsService(this)) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(PatrolMainActivity.this);
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

        //본부 스피너


    }//onCre

    //202007_접보선택시 다른접보선택해서 접보내용받아오는것 수정부


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

        spinSort.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    // *********************************Slide Menu
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

    private class DrawerItemClickListener implements ExpandableListView.OnItemClickListener {

        List<String> defaultList = null;
        ListView lvNavList;
        ExpandableListAdapter adapter;
        List<String> nList;
        List<String> innerI;

        DrawerItemClickListener(List<String> defaultList, List<String> innerI, List<String> nList, ListView lvNavList, ExpandableListAdapter adapter) {
            this.defaultList = defaultList;
            this.lvNavList = lvNavList;
            this.adapter = adapter;
            this.nList = nList;
            this.innerI = innerI;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            //최창유 주석


            switch (position) {
                case 1:


                    break;
                // 순찰원
                case 2:
                    // for (int i = 0; i < nList.size(); i++) {
                    //    Log.i("텟트", nList.get(i).toString());
                    // }
                    //    defaultList.addAll(1,nList);
                    //lvNavList.
                    break;
                // 견인원
                case 3:
                    Intent i_tow = new Intent(PatrolMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                // 대국민
                case 4:
                    Intent i_citizen = new Intent(PatrolMainActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                default:
                    break;
            }

          /*  switch (position) {
                case 1:
                    VIEW_TAG = VIEW_INNEREMPLOYEE;
                    Intent i_employee = new Intent(PatrolMainActivity.this, InnerEmployActivity.class);
                    startActivity(i_employee);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;
                // 순찰원
                case 2:
                    Intent i_patrol = new Intent(PatrolMainActivity.this, PatrolMainActivity.class);
                    startActivity(i_patrol);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;
                // 견인원
                case 3:
                    Intent i_tow = new Intent(PatrolMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                // 대국민
                case 4:
                    Intent i_citizen = new Intent(PatrolMainActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                default:
                    break;
            }*/


            //dlDrawer.closeDrawer(lvNavList);
        }

    }

    // *********************************Slide Menu

    @Override
    protected void onResume() {
        super.onResume();
        ProgressFlag = true;
        Log.println(Log.ASSERT, TAG, "onResume");
        if (null == progressDialog) {
            Log.println(Log.ASSERT, TAG, "로딩중");
            progressDialog = ProgressDialog.show(PatrolMainActivity.this, "", "로딩중...", true);

        }

        contextActivity = PatrolMainActivity.this;

        //202007
        //수정
        setFixSelectJubbo = setSelectJubbo;
        //202007_
        //setFixGetJubbo = getSetSelectJubboStart;
        VIEW_TAG = VIEW_PATROL;
        db.close();
        db.init();
//		stopGPS();
//		startMyGps();
//		stopTimer(this);
//		startTimer();
        stopTimer_list();

        pastItem = new Patrol();
        ppastItem = new Patrol();



        startTimer_list();
        db.deleteRptId();
        displayFlag = true;
        Log.i("onResum start ",Common.getPrefString(PatrolMainActivity.this, "start_yn").toString());
        Log.i("onResum",setFixSelectJubbo.toString()+"고정접보값");



        if ("Y".equals(SituationService.patrolOKFlag)) {
           patrolOK.setChecked(true);
        } else {
            patrolOK.setChecked(false);
        }

        Log.i("getSetSelect",getSetSelectJubboStart);

        //202007 - 통신을 타고 재 통신할때 여기를 타는데 완료버튼을 누른상태에서 버튼을 바뀌는 위치가 여기인지
//        Log.i("onResum","line648");
        if("Y".equals(Common.getPrefString(PatrolMainActivity.this,"getSetSelectJubboStart"))){
            displayGallery();
        } else {
            setStartBtn();
        }

//		displayGallery();

    }

    boolean displayFlag = false;
    private ArrayList<Patrol> sortItemList = new ArrayList<Patrol>();


    public void displayGallery() {
        Log.i("", TAG + " displayGallery() Call start");

        Log.d("920506","1");

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
            for (int i = 0; i < sortItemList.size(); i++) {
                Log.println(Log.ASSERT, TAG, "" + sortItemList.toString());
            }

            final PatrolListAdapter adapter = new PatrolListAdapter(this, sortItemList);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Patrol item = sortItemList.get(position);
                    if (Common.nullCheck(item.getReg_type()).contains("7")) {
                        btnPatJochiReg.setVisibility(View.VISIBLE);
                    }
                    //확인 내용 내부 DB 저장.(알람용)
                    db.insertRptId(item.getRpt_id(), item.getReg_date());

                    // 상세확인 서버 전송.
                    // 수정중
                    //202007
                    //if (getSetSelectJubboStart == "Y") {

                    // if (item.getRpt_id() != setSelectJubbo) {
                    //수정중
//                            Log.i("PM713", setSelectJubbo);
//                            adapter.setItemImage(position);
//                            selectedPosition = position;
//                            for (int i = 0; i < listView.getChildCount(); i++) {
//                                listView.getChildAt(i).setSelected(false);
//                                listView.getChildAt(i).findViewById(R.id.rowPatrolLayout).setSelected(false);
//                            }
//
//
//                            v.findViewById(R.id.rowPatrolLayout).setSelected(false);
//                            if (null != selectedView) {
//                                selectedView.setSelected(false);
//                                selectedView.findViewById(R.id.rowPatrolLayout).setSelected(false);
//                            }
//                            v.setSelected(true);
//                            v.findViewById(R.id.rowPatrolLayout).setSelected(true);
//

//                    if( setSelectJubbo != sortItemList.get(position).getRpt_id() || setFixSelectJubbo != sortItemList.get(position).getRpt_id()){
//                    Log.i("PM715", "다른접보선택");
//                    Log.i("PM715 다른접보선택",setFixSelectJubbo);
//                    Log.i("PM715 다른접보", sortItemList.get(position).getRpt_id());
                    //202007_
                    if("Y".equals(Common.getPrefString(PatrolMainActivity.this,"getSetSelectJubboStart"))) {
                       //수정
                        if (setFixSelectJubbo != sortItemList.get(position).getRpt_id()) {
                            Log.i("PM713", "다른접보선택");
                            Log.i("PM713 다른접보선택", setFixSelectJubbo);
                            Log.i("PM173 다른접보", sortItemList.get(position).getRpt_id());
                            patrolStartIng.setVisibility(View.GONE);
                            patrolStart.setVisibility(View.VISIBLE);

                             } else if (setFixSelectJubbo == sortItemList.get(position).getRpt_id()) {

                            Log.i("PM713", "같은접보선택");
                            patrolStartIng.setVisibility(View.VISIBLE);
                            patrolStart.setVisibility(View.GONE);

                        }
                    } else {
                        setStartBtn();
                    }

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
//                }
            });

            //setStartBtn();

            // }
            Log.d("920506","26");
            if (itemList != null) {
                Log.d("920506","27");
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getRpt_id().equals(SituationService.selectedRpt_id)) {
                        Log.d("920506","28");
                        listView.setSelection(i);
                        break;
                    }
                    if (i == itemList.size() - 1) {
                        Log.d("920506","29");
                        SituationService.selectedRpt_id = "";
                    }
                }

            }
            Log.d("920506","30");
            Log.i("", TAG + " displayGallery() Call end");
            Log.println(Log.ASSERT, TAG, "displayGallery() Call end");
        }
    }
    public class SlideAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private LayoutInflater mInflater;
        Boolean isInit = false;
        LinearLayout llSlide;

        public List<String> nList = null;

        public SlideAdapter(Context context, List<String> objects, Boolean isInit) {
            super(context, 0, objects);
            this.isInit = isInit;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;


        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("익스", "getView");
            View view = convertView;
            if (isInit) {
                Log.d("", "getview position " + position);
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.slide_row, null);
                }
                //최창유 주석

                llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
                if (position == 0) {
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                    llSlide.setTag("전체");
                } else if (position == 1) {
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("내부");
                } else if (position == 2) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_01);
                    llSlide.setTag("순찰");
                } else if (position == 3) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_02);
                    llSlide.setTag("견인");
                } else if (position == 4) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_03);
                    llSlide.setTag("대민");
                }


            } else {
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.slide_row, null);
                }

         /*       if(parent.getChildAt(position).getTag().equals("전체")){
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                    llSlide.setTag("전체");
                }else if(parent.getChildAt(position).getTag().equals("내부")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("내부");
                }else if(parent.getChildAt(position).getTag().equals("순찰")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("순찰");
                }else if(parent.getChildAt(position).getTag().equals("견인")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("견인");
                }else if(parent.getChildAt(position).getTag().equals("대민")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("대민");
                }else{
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                }
*/
                llSlide.setBackgroundResource(R.drawable.tit_trans);
                llSlide.getChildAt(0).findViewById(R.id.yesterDaywork_iv);
            /*    if (position == 0) {
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                } else if (position == 1) {
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                } else if (position == 2) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_01);
                } else if (position == 3) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_02);
                } else if (position == 4) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_03);
                }*/

            }
            return view;
        }

        public SlideAdapter setIseInit(Boolean isInit, List<String> list) {
            this.isInit = isInit;
            this.nList = nList;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return this;
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter implements OnClickListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener/*, ExpandableListView.OnItemClickListener*/ {
        private Context context;
        private int groupLayout = 0;
        private int childLayout = 0;
        private ArrayList<Item> dataList;
        private LayoutInflater inflater;
        DrawerLayout drawerLayout;
        JSONObject jsonObject;

        public ExpandableListAdapter(Context context, int groupLayout, int childLayout, ArrayList<Item> dataList) {
            super();
            this.dataList = dataList;
            this.groupLayout = groupLayout;
            this.childLayout = childLayout;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  this.drawerLayout = drawerLayout;
        }

        public ExpandableListAdapter() {
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
            if (view == null) {
                view = inflater.inflate(groupLayout, viewGroup, false);
            }
            //최창유 주석

            LinearLayout llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            LinearLayout tllSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            if (i == 0) {

                tllSlide.setBackgroundResource(R.drawable.tit_trans);
                tllSlide.setTag("전체");

           /*     tllSlide.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlDrawer.closeDrawers();
                    }
                });*/
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
            } catch (NullPointerException ee) {
                Log.e("에러", "널포인터 익셉션 익센셥");
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
            Log.i("i체크", "i:" + i + "ㅣ:" + l);
            if (i == 0) {
                dlDrawer.closeDrawers();
                return true;
            } else if (i == 1 && dataList.get(0).child.size() == 1) {//내부직원 직원 리스트가 1명 이면 그대로 이동
                VIEW_TAG = VIEW_INNEREMPLOYEE;
                Intent i_employee = new Intent(PatrolMainActivity.this, InnerEmployActivity.class);
                startActivity(i_employee);
                finish();
                stopGPS();
                stopTimer(PatrolMainActivity.this);
                return true;
            } else if (i == 2 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                Intent i_patrol = new Intent(PatrolMainActivity.this, PatrolMainActivity.class);
                startActivity(i_patrol);
                finish();
                stopGPS();
                stopTimer(PatrolMainActivity.this);
                return true;
            } else if (i == 3 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                Intent i_tow = new Intent(PatrolMainActivity.this,
                        TowMainActivity.class);
                startActivity(i_tow);
                finish();
                stopGPS();
                stopTimer(PatrolMainActivity.this);
                return true;
            } else if (i == 4 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                Intent i_citizen = new Intent(PatrolMainActivity.this,
                        MainActivity.class);
                startActivity(i_citizen);
                finish();
                stopGPS();
                stopTimer(PatrolMainActivity.this);
                return true;
            }

         /*   switch (i) {
                case 1:

                    VIEW_TAG = VIEW_INNEREMPLOYEE;
                    Intent i_employee = new Intent(PatrolMainActivity.this, InnerEmployActivity.class);
                    startActivity(i_employee);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;
                // 순찰원
                case 2:
                    Intent i_patrol = new Intent(PatrolMainActivity.this, PatrolMainActivity.class);
                    startActivity(i_patrol);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;
                // 견인원
                case 3:
                    Intent i_tow = new Intent(PatrolMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                // 대국민
                case 4:
                    Intent i_citizen = new Intent(PatrolMainActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                default:
                    break;
            }*/

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
                        Intent i_employee = new Intent(PatrolMainActivity.this, InnerEmployActivity.class);
                        startActivity(i_employee);
                        finish();
                        stopGPS();
                        stopTimer(PatrolMainActivity.this);
                    } catch (JSONException j) {
                        Log.e("에러", "예외");


                    } catch (NullPointerException ne) {
                        Log.e("에러", "예외");
                    }
                    break;
                // 순찰원
                case 2:
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
                        VIEW_TAG = VIEW_PATROL;
                        Intent i_patrol = new Intent(PatrolMainActivity.this, PatrolMainActivity.class);
                        startActivity(i_patrol);
                        finish();
                        stopGPS();
                        stopTimer(PatrolMainActivity.this);
                    } catch (JSONException j) {
                        Log.e("에러", "예외");
                    } catch (NullPointerException ne) {
                        Log.e("에러", "예외");
                    }
                    break;
                // 견인원
                case 3:
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
                        VIEW_TAG = VIEW_TOW;
                        Intent i_tow = new Intent(PatrolMainActivity.this,
                                TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
                        stopGPS();
                        stopTimer(PatrolMainActivity.this);
                    } catch (JSONException e) {
                        Log.e("에러 ", "예외");
                    }

                    break;

                // 대국민
                case 4:
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
                        Intent i_citizen = new Intent(PatrolMainActivity.this,
                                MainActivity.class);
                        startActivity(i_citizen);
                        finish();
                        stopGPS();
                        stopTimer(PatrolMainActivity.this);
                    } catch (JSONException e) {
                        Log.e("에러 ", "예외");
                    }

                    break;
                default:
                    break;
            }

            return false;
        }


        //--
        public class Item {
            public ArrayList<String> child;
            public String groupName;

            public Item(String name) {
                groupName = name;
                child = new ArrayList<String>();
            }
        }
    }


    //익스펜더블 adapter
  /*  public class SlideAdapterExpandableListView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        List<String> groupList;
        ArrayList<ArrayList<String>> childList;
        private LayoutInflater mInflater;

        public SlideAdapterExpandableListView(Context context, List<String> groupList, ArrayList<ArrayList<String>> childList) {
            super();
            this.context = context;
            this.groupList = groupList;
            this.childList = childList;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            context = context;

        }

        public SlideAdapterExpandableListView() {
            super();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void setHasStableIds(boolean hasStableIds) {
            super.setHasStableIds(hasStableIds);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return super.onFailedToRecycleView(holder);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
        }


        public class ListHeaderViewHolder extends RecyclerView.ViewHolder {

            View view;

            ListHeaderViewHolder(View view) {
                super(view);
                this.view = view;
            }
        }



    }*/

    public class PatrolListAdapter extends ArrayAdapter<Patrol> {

        private Context mContext;
        private LayoutInflater mInflater;

        public PatrolListAdapter(Context context, List<Patrol> objects) {
            super(context, 0, objects);

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
            if ("N".equals(item.getEnd_yn())) {
                sendConfirm();//순찰자가 내용을 확인했다는 정보 전송.
            }

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
            } catch (NullPointerException e) {
                detailTime.setText("시간 :");
            } catch (ParseException ee) {
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
                saupPhone.setVisibility(View.VISIBLE);
            } else {
                saupPhone.setVisibility(View.GONE);
            }
            if (Common.nullCheck(item.getPsn_tel_no()).length() >= 10) {
                psnPhone.setVisibility(View.VISIBLE);
            } else {
                psnPhone.setVisibility(View.GONE);
            }

            // 고객 전화번호버튼
            if (Common.nullCheck(item.getReg_tel_no()).length() >= 10) {
                customPhone.setVisibility(View.VISIBLE);
            } else {
                customPhone.setVisibility(View.GONE);
            }

            psnPhoneCallInfo = "" + item.getPsn_tel_no();
            saupPhoneCallInfo = "" + item.getEtc();
            customPhoneInfo = "" + item.getReg_tel_no();
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
                            } catch (PendingIntent.CanceledException e) {
                                /*  e.printStackTrace();*/
                                Log.e("에러", "예외");
                            }
                        }
                    }
                });
            }
            return view;
        }
    }

    // 수정중
    // 이상없은 버튼 관리
    private void setPatrolOkSet() {
        if (SituationService.patrolOKFlag.equals("Y")) {
            patrolOK.setChecked(false);
            SituationService.patrolOKFlag = "N";
        } else {
            patrolOK.setChecked(true);
            SituationService.patrolOKFlag = "Y";
        }
    }

    public void setRegVisible(String regType) {
        // 사고발생 0005
        patrolStop.setVisibility(View.GONE);
        patrolRegist.setVisibility(View.GONE);
        if (regType.equals("0005") || regType.equals("0012") || regType.equals("0013")) {
            Log.d(TAG, TAG + " setRegVisible regtype = 0005");
            patrolRegist.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, TAG + " setRegVisible regtype = else");
            patrolStop.setVisibility(View.VISIBLE);
        }

    }

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

    public String getResultName(String code) {
        if ("0001".equals(code))
            return "접보";
        if ("0002".equals(code))
            return "순찰차확인";
        if ("0003".equals(code))
            return "기타";
        if ("0004".equals(code))
            return "본부전달완료";
        if ("0005".equals(code))
            return "완료";
        return "";
    }

    // 상세화면 초기화.
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

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
        db.init();
//		stopGPS();
//		startMyGps();
//		stopTimer(this);
//		startTimer();
        stopTimer_list();
        startTimer_list();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stopTimer_list();

        if (!Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
            stopTimer_list();
        }
    }


    // 출동, 출동중 버튼 visible 관리 ( 선택해제 버튼 추가)
    public synchronized void setStartBtn() {

        patrolStartIng.setVisibility(View.VISIBLE);
        patrolStart.setVisibility(View.VISIBLE);
        btnPatClear.setVisibility(View.VISIBLE);

        if ("Y".equals(Common.getPrefString(PatrolMainActivity.this, "start_yn"))) {
            patrolStart.setVisibility(View.GONE);
            btnPatClear.setVisibility(View.INVISIBLE);
        } else {
            patrolStartIng.setVisibility(View.GONE);
        }

    }

    // 순찰차전화버튼 클릭시 visible 관리
    public synchronized void setPhoneBtnVisible(boolean phone) {
        llbottomBtnPhone.setVisibility(View.VISIBLE);
        llbottomBtnDefault.setVisibility(View.VISIBLE);
        if (phone == true) {
            llbottomBtnDefault.setVisibility(View.GONE);
        } else {
            llbottomBtnPhone.setVisibility(View.GONE);
        }

    }

    //202007 고정
    public void setFixItem(){

        setSelectJubbo = SituationService.selectedRpt_id;
        //202007_
        getSetSelectJubboStart = "Y";

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)/*||Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)*/) {
            switch (v.getId()) {
                case R.id.btnPatJochiReg:
                    if ("".equals(common.nullCheck(SituationService.selectedRpt_id))) {
                        Toast.makeText(PatrolMainActivity.this, "상황 선택후 이용해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent jochiIntent = new Intent(PatrolMainActivity.this, DialogJochi.class);
                    if (null != itemList) {
                        if (selectedPosition != -1) {
                            if (itemList.size() > 0) {
                                jochiIntent.putExtra("psn_tel_no", Common.nullCheck(itemList.get(selectedPosition).getPsn_tel_no()));
                            } else {
                                jochiIntent.putExtra("psn_tel_no", "");
                            }

                        }
                    }
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, jochiIntent, PendingIntent.FLAG_ONE_SHOT);
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException ee) {
                        /*ee.printStackTrace();*/
                        Log.e("에러", "예외");
                    }

                    break;
                case R.id.saupPhone:
                    Intent saupIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + saupPhoneCallInfo));
                    startActivity(saupIntent);
                    break;
                case R.id.psnPhone:
                    Intent psnIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + psnPhoneCallInfo));
                    startActivity(psnIntent);
                    break;

                case R.id.patrolStartIng:

                    Log.i("PM1738_ing2", setSelectJubbo.toString());
                    Log.i("PM1738_ing1", SituationService.selectedRpt_id);

                    if (null == itemList) {
                        SituationService.selectedRpt_id = "";
                    } else if (itemList.size() < 1) {
                        SituationService.selectedRpt_id = "";
                    }

                    if (setSelectJubbo == SituationService.selectedRpt_id || setFixSelectJubbo == SituationService.selectedRpt_id) {
                        AlertDialog.Builder ading = new AlertDialog.Builder(PatrolMainActivity.this);
                        ading.setMessage("")
                                .setTitle("확인")
                                .setMessage("* 출동중 입니다. \n취소 하시겠습니까?")
                                .setCancelable(false)
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SituationService.actionStop(PatrolMainActivity.this);
                                                setStartBtn();
                                                setSelectJubbo = "";
                                                //getSetSelectJubboStart = "";
                                                //202007_
                                                Common.setPrefString(PatrolMainActivity.this, "getSetSelectJubboStart", "");
                                                sendStop(true);
                                            }
                                        });
                        ading.setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                        ading.show();

                    } else {
                        AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                        ad.setMessage("").setTitle("확인").setMessage("출동중인 제보가 아닙니다.").setCancelable(false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        ad.show();

                    }

                    break;

                case R.id.patrolStart:
                    Log.i("PM1833_start_ssid", SituationService.selectedRpt_id);
                    Log.i("PM1833_start_sssj", setSelectJubbo.toString());
                    Log.i("PM1833_start_ssfj", setFixSelectJubbo);
                    //Toast.makeText(this,"출동",Toast.LENGTH_SHORT).show();
                    if ("".equals(Common.nullCheck(SituationService.selectedRpt_id))) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                        ad.setMessage("")
                                .setTitle("확인")
                                .setMessage("* 상황 목록 선택후 출동하실 수 있습니다.")
                                .setCancelable(false)
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "순찰차가 출동하였습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                        ad.show();
                        return;
                    }

                    if ("Y".equals(Common.getPrefString(PatrolMainActivity.this,"getSetSelectJubboStart"))) {
                        Log.i("PM1738_0", SituationService.selectedRpt_id);
                        Log.i("PM1738_9", setSelectJubbo.toString());
                        Log.i("PM1738", getSetSelectJubboStart);
                        AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                        ad.setMessage("").setTitle("확인").setMessage("* 출동중인 다른 접보가 있습니다.").setCancelable(false).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        ad.show();
                        return;
                    } else {

                        String endyn = "";
                        if (itemList != null) {
                            for (int i = 0; i < itemList.size(); i++) {
                                Patrol item = itemList.get(i);
                                endyn = item.getEnd_yn();
                                if (SituationService.selectedRpt_id.equals(item.getRpt_id())) {
                                    break;
                                }
                            }
                        }

                        //수정중_202007

                        //setSelectJubbo = sortItemList.get(selectedPosition).getRpt_id();
                        //getSetSelectJubboStart = "Y";

                        if (endyn.equals("Y")) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                            ad.setMessage("")
                                    .setTitle("확인")
                                    .setMessage("* 조치완료된 제보는 출동할 수 없습니다.")
                                    .setCancelable(false)
                                    .setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                            ad.show();
                        } else {
                            AlertDialog.Builder ad = new AlertDialog.Builder(
                                    PatrolMainActivity.this);
                            ad.setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                        }
                                    });
                            ad.setMessage("")
                                    .setTitle("확인")
                                    .setMessage(detailTitle.getText() + "\n* 출동 하시겠습니까?.")
                                    .setCancelable(false)
                                    .setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Log.i("PM1843", "출동버튼 누름");
                                                    SituationService.patrolOKFlag = "Y";
                                                    //202007
                                                    setPatrolOkSet();
                                                    SituationService.actionStart(PatrolMainActivity.this);
                                                    setStartBtn();
                                                    setFixItem();
                                                    Common.setPrefString(PatrolMainActivity.this, "getSetSelectJubboStart", "Y");
                                                    //통합DB 개발
                                                    Intent directionIntent = new Intent(PatrolMainActivity.this, DialogDirection.class);
                                                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, directionIntent, PendingIntent.FLAG_ONE_SHOT);
                                                    try {
                                                        pi.send();
                                                    } catch (PendingIntent.CanceledException ee) {
                                                        Log.e("예외", "예외");
                                                    } catch (Exception ee) {
                                                        Log.e("예외", "예외");
                                                    }
                                                }
                                            });
                            ad.show();

                        }

                    }

            break;


            case R.id.patrolStop:
                Log.i("PM1935_stop", SituationService.selectedRpt_id);
                Log.i("PM1936_stop2", setSelectJubbo.toString());
                //202007
                //수정
                 if (setSelectJubbo == SituationService.selectedRpt_id || setFixSelectJubbo == SituationService.selectedRpt_id) {
//                    if (getSetSelectJubboStart == "Y") {
                if("Y".equals(Common.getPrefString(PatrolMainActivity.this,"getSetSelectJubboStart"))){
                        AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                        ad.setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                });
                        ad.setMessage("")
                                .setTitle("확인")
                                .setMessage(detailTitle.getText() + "\n* 조치 완료 하시겠습니까?.")
                                .setCancelable(false)
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SituationService.actionStop(PatrolMainActivity.this);
                                                setStartBtn();
                                                sendStop(false);
                                                SituationService.patrolOKFlag = "Y";
                                                //202007_
                                                Common.setPrefString(PatrolMainActivity.this,"getSetSelectJubboStart","N");
                                                setPatrolOkSet();
                                            }
                                        });
                        ad.show();
                    }

                    setSelectJubbo = "";
                    SituationService.move_direct = "";

                } else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                    ad.setMessage("").setTitle("확인").setMessage("출동중인 제보가 아닙니다.").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    ad.show();

                }

                    break;

                case R.id.customPhone:
                    Intent customIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customPhoneInfo));
                    startActivity(customIntent);
                    break;

                case R.id.patPhone:
                    setPhoneBtnVisible(true);
                    break;

                case R.id.patPhoneClose:
                    setPhoneBtnVisible(false);
                    break;

                case R.id.patrolRegist:
                    //202007
                    //수정
                    //if ( setSelectJubbo.equals(Common.getPrefString(PatrolMainActivity.this,"setFixSelectJubbo"))){
                    if(setSelectJubbo == SituationService.selectedRpt_id || setFixSelectJubbo == SituationService.selectedRpt_id) {
                        Intent i = new Intent(PatrolMainActivity.this, PatrolRegActivity.class);
                        i.putExtra("selectedRpt_id", SituationService.selectedRpt_id);
                        i.putExtra("customPhoneInfo", customPhoneInfo);
                        startActivityForResult(i, Configuration.PATROL_REG_RETURN);
                    }
                    break;

                case R.id.btnUserInfo:
                    //Toast.makeText(this,"패트롤 메인",Toast.LENGTH_SHORT).show();
                    // Intent info = new Intent(getApplicationContext(),
                    // DialogUserInfoActivity.class);
                    // info.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // PendingIntent pi_user =
                    // PendingIntent.getActivity(getApplicationContext(), 0, info,
                    // PendingIntent.FLAG_ONE_SHOT );
                    // try {
                    // pi_user.send();
                    // } catch (Exception ee) {
                    // ee.printStackTrace();
                    // }
                    break;

                case R.id.patrolPicture:
                    dialogGallery = new Dialog(PatrolMainActivity.this, R.style.FullHeightDialog);

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

                            Intent i = new Intent(PatrolMainActivity.this, AndroidCustomGalleryActivity.class);
                            startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

//					Intent chooseIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//					chooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//					startActivityForResult(chooseIntent, 2);
                            //MyGalleryPicker 실행


                            //GalleryListMain 실행
//					Intent i = new Intent(PatrolMainActivity.this, GalleryListMain.class);
//					startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

//					AlertDialog.Builder patClear = new AlertDialog.Builder(PatrolMainActivity.this);
//					patClear.setMessage("")
//							.setTitle("확인")
//							.setMessage("* 테스트.")
//							.setCancelable(false)
//							.setNeutralButton("type3", new DialogInterface.OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									//type 1
//									Intent intent = new Intent();
//				                    intent.setType("image/*");
//				                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//				                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//				                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), Configuration.FILE_TYPE_IMAGE);
//								}
//							})
//							.setNegativeButton("type1", new DialogInterface.OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									  //type2
//									Intent i = new Intent(PatrolMainActivity.this, GalleryListMain.class);
//									startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);
//								}
//							})
//							.setPositiveButton("type2",
//									new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialog,int which) {
//											//type3
//											Intent i = new Intent(PatrolMainActivity.this, MyGalleryPicker.class);
//											startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);
//										}
//									});
//					
//					patClear.show();
//					


                            //앨범 1장 가져오기
//					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//					i.setType("image/*");
//					startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

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

                    //202007
                    case R.id.btnPatClear:
                        //수정
                  if (getSetSelectJubboStart == "Y" || setFixSelectJubbo == SituationService.selectedRpt_id) {
                      AlertDialog.Builder patClear = new AlertDialog.Builder(PatrolMainActivity.this);
                        patClear.setMessage("")
                                .setTitle("확인")
                                .setMessage("* 출동중 삭제 하실 수 없습니다.")
                                .setCancelable(false)
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                            }
                                        });
                        patClear.show();
                    } else {
                        SituationService.selectedRpt_id = "";
                        detailClear();
                        for (int j = 0; j < listView.getChildCount(); j++) {
                            listView.getChildAt(j).setSelected(false);
                            listView.getChildAt(j).setActivated(false);
                            listView.getChildAt(j).findViewById(R.id.rowPatrolLayout).setSelected(false);
                            listView.getChildAt(j).findViewById(R.id.rowPatrolLayout).setActivated(false);
                            customPhone.setVisibility(View.GONE);
                            saupPhone.setVisibility(View.GONE);
                        }
                    }
                    break;
                case R.id.cctv_btn:
                    if (selectedPosition == -1) {
                        Toast.makeText(this, "접보를 선택하여 주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        //	Toast.makeText(this,sortItemList.get(selectedPosition).getEnd_km()+","+sortItemList.get(selectedPosition).getStart_km()+","+sortItemList.get(selectedPosition).getRoadNo(),Toast.LENGTH_LONG).show();

                        Patrol param = new Patrol();
                        param.setEnd_km(sortItemList.get(selectedPosition).getEnd_km());
                        param.setStart_km(sortItemList.get(selectedPosition).getStart_km());
                        param.setRoadNo(sortItemList.get(selectedPosition).getRoadNo());
                        if (param.getStart_km() == null || param.getEnd_km() == null || param.getStart_km() == "" || param.getEnd_km() == "") {
                            Toast.makeText(this, "해당 접보는 CCTV화면을 조회 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            getCCTVURL(param, GET_CCTV_URL_LIST);
                        }

                    }
                    break;
                default:
                    break;
            }
        } else {
            return;
        }
    }


    public void getCCTVURL(Patrol patrol, String primitive) {
        Parameters params = new Parameters(primitive);
        if (/*Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)||*/Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
            params.put("roadNo", patrol.getRoadNo());
            params.put("start_km", patrol.getStart_km());
            params.put("end_km", patrol.getEnd_km());
            Log.i("940413","2");
            new Action(primitive, params).execute("");
        }


    }

    public void runCamera() {
        Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity2.class);
        startActivityForResult(cameraIntent, Configuration.IMAGE_CAPTURE);
    }

    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
        Log.println(Log.ASSERT, TAG, "onActionPost");
        try {
            if (result != null) {
                Log.println(Log.ASSERT, TAG, "PatrolMainActivity에서 확인한 result");
                Log.println(Log.ASSERT, TAG, "" + result.toString());
            }
        } catch (NullPointerException e1) {
            Log.e("에러","예외");
        }

        try {
            if (e == null) {
                if (null != result) {
                    String rtnResultCode = result.get("result");
                    if ("1000".equals(rtnResultCode)) {
                        if (primitive.equals(ONECLICK_WORKCOMPLETE_UPDATE)) {
                            displayGallery();
                            setStartBtn();
                        } else if (primitive.equals(ONECLICK_GETPATROLTELNO_SELECT)) {
                            result.setList("entity");
                            Log.d("", TAG + "ONECLICK_GETPATROLTELNO_SELECT size = " + result.size());
                            if (result.size() > 0) {
                                patPhone.setVisibility(View.VISIBLE);
                                for (int i = 0; i < result.size(); i++) {
                                    String car_nm = result.get(i, "car_nm");
                                    final String tel_no = result.get(i, "tel_no");

                                    if (Configuration.User.getHp_no().equals(tel_no)) {
                                        return;
                                    }
                                    if (i == 0) {
                                        patPhone1.setVisibility(View.VISIBLE);
                                        patPhone1.setText(car_nm);
                                        patPhone1.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(patPhone1Intent);

                                            }
                                        });
                                    } else if (i == 1) {
                                        patPhone2.setVisibility(View.VISIBLE);
                                        patPhone2.setText(car_nm);
                                        patPhone2.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(patPhone1Intent);

                                            }
                                        });
                                    } else if (i == 2) {
                                        patPhone3.setVisibility(View.VISIBLE);
                                        patPhone3.setText(car_nm);
                                        patPhone3.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(patPhone1Intent);

                                            }
                                        });
                                    } else if (i == 3) {
                                        patPhone4.setVisibility(View.VISIBLE);
                                        patPhone4.setText(car_nm);
                                        patPhone4.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(patPhone1Intent);

                                            }
                                        });
                                    } else if (i == 4) {
                                        patPhone5.setVisibility(View.VISIBLE);
                                        patPhone5.setText(car_nm);
                                        patPhone5.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(patPhone1Intent);

                                            }
                                        });
                                    }

                                }// for
                            } else {
                                patPhone.setVisibility(View.GONE);
                            }

                        } else if (primitive.equals(GET_CCTV_URL_LIST)) {
                            Log.println(Log.ASSERT, TAG, "else if");
                            Intent intent = new Intent(this, PatrolCCtvActivity.class);
                            intent.putExtra("start_cctv", result.get("start_cctv").toString());
                            intent.putExtra("end_cctv", result.get("end_cctv"));
                            intent.putExtra("road_no", sortItemList.get(selectedPosition).getRoadNo());
                            intent.putExtra("detailTitle", detailTitle.getText().toString());
                            intent.putExtra("detailJeopbo", detailJeopbo.getText().toString());
                            intent.putExtra("detailType", detailType.getText().toString());
                            intent.putExtra("detailTime", detailTime.getText().toString());
                            intent.putExtra("detailState", detailState.getText().toString());
                            intent.putExtra("detailContent", detailContent.getText().toString());
                            intent.putExtra("detailJochi", detailJochi.getText().toString());
                            startActivity(intent);
                        }

                    } else if ("9999".equals(rtnResultCode)) {

                        if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                            if ("Y".equals(Common.getPrefString(PatrolMainActivity.this, "start_yn"))) {
                                Patrol item = new Patrol();
                                Log.d("", "9999 value  = " + Common.nullCheck(result.get("end_time")));
//								item.setRpt_id(Common.nullCheck(result.get("rpt_id")));
//								item.setBscode(Common.nullCheck(result.get("bscode")));
//								item.setReg_date(Common.nullCheck(result.get("reg_date")));
//								item.setReg_time(Common.nullCheck(result.get("reg_time")));
//								item.setReg_type(Common.nullCheck(result.get("reg_type")));
//								item.setReg_info(Common.nullCheck(result.get("reg_info")));
//								item.setReg_data(Common.nullCheck(result.get("reg_data")));
                                item.setInp_val(Common.nullCheck(result.get("inp_val")));
//								item.setLocal_way(Common.nullCheck(result.get("local_way")));
//								item.setStart_km(Common.nullCheck(result.get("start_km")));
//								item.setLane_num(Common.nullCheck(result.get("lane_num")));
//								item.setLocal_nm(Common.nullCheck(result.get("local_nm")));
//								item.setDirection_name(Common.nullCheck(result.get("direction_name")));
//								item.setStartcount(Common.nullCheck(result.get("startcount")));
                                item.setEnd_time(Common.nullCheck(result.get("end_time")));

                                Log.i("PM2287","9999");
                                mp3Start(rtnResultCode, item);
                                setStartBtn();
                                beforeUPT_CARTIME = "";
                                displayFlag = true;
                            }
                        }
                    }
                }
            }
        } catch (XmlPullParserException e2) {
            //e2.printStackTrace();
            Log.e("에러","예외");
        }
    }


    /**
     * 순찰,견인 목록 조회 타이머 5초단위 실행
     */
    static Timer listTimer = new Timer();
    static Handler listHandler = new Handler();

    //목록 통신 동기화를 위함.
    boolean listFlag = true;
    XMLData patrolData;

    public void startTimer_list() {
        Log.println(Log.ASSERT, TAG, "startTimer_list");
        TimerTask timertask = new TimerTask() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (listFlag == false) {
                        Log.println(Log.ASSERT, TAG, "listFlas false");
                        return;
                    }

                    Log.println(Log.ASSERT, TAG, "최창유 ListSize");
                    Log.d(TAG, TAG + " starttimer actionlist regtype = " + selectedreg_type);
                    setRegVisible(selectedreg_type);
                    if (VIEW_PATROL.equals(VIEW_TAG) /*|| Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)*/) {
                        Log.println(Log.ASSERT, TAG, "if 안에");
//						Parameters params = new Parameters(ONECLICK_PATROLRCEPTINFO_SELECT);
//						if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
//							params.put("bscode", Configuration.User.getBscode_list().get(0));
//							
//							try {
//								nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
//								nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							
//							params.put("update_cartime", nowUPT_CARTIME);
//							params.put("reg_date", nowREG_DATE);
//						}

//						setPatrolList(SituationService.serviceData, ONECLICK_PATROLRCEPTINFO_SELECT);

//						if(listFlag == true){
//							listFlag = false;
//							new ActionList(ONECLICK_PATROLRCEPTINFO_SELECT, params).execute("");
//						}

                        Log.d("", TAG + "time check at Timer" + nowUPT_CARTIME + "  |||  " + SituationService.nowUPT_CARTIME);
                        if (!SituationService.nowUPT_CARTIME.equals(nowUPT_CARTIME)) {
                            /*if(true){*/
                            itemList = SituationService.itemList;
                            nowUPT_CARTIME = SituationService.nowUPT_CARTIME;
//							nowUpperRPTID = SituationService.nowUpperRPTID;
                            nowREG_DATE = SituationService.nowREG_DATE;
                            pastItem = SituationService.pastItem;


                            itemList = SituationService.itemList;




                            displayGallery();


                            Log.i("", TAG + "startTimer_list() -> displayGallery() Call end");

                            if (null != progressDialog) {
                                progressDialog.dismiss();
                                progressDialog = null;
                                ProgressFlag = false;
                            }

                        }//if
                        if (ProgressFlag == true) {
                            ProgressFlag = false;
                        } else {
                            if (null != progressDialog) {
                                progressDialog.dismiss();
                                progressDialog = null;
                                ProgressFlag = false;
                            }
                        }
                    }
                }
            };

            @Override
            public void run() {
                listHandler.post(runnable);

            }
        };
        listTimer = new Timer();
        listTimer.schedule(timertask, 1000, 15000);// 100MS 뒤시작, 1초 간격으로 호출.
    }

    public static void stopTimer_list() {
        if (null != listTimer) {
            listTimer.cancel();
        }
    }

    boolean toastRunning = false;

    @Override
    protected void onStart() {
        super.onStart();
        toastRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        toastRunning = false;
    }

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

    public class ActionList extends AsyncTask<String, Void, XMLData> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
        ProgressDialog progressDialog;

        String primitive = "";
        Parameters params = null;

        @Override
        protected void onPreExecute() {
            Log.println(Log.ASSERT, TAG, "ActionList");
            if (ProgressFlag == true && ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
                progressDialog = ProgressDialog.show(PatrolMainActivity.this, "", "로딩중...", true);
                ProgressFlag = false;
            }
            super.onPreExecute();
        }



        // primitive 에 따라 URL을 구분짓는다.
        public ActionList(String primitive, Parameters params) {
            this.primitive = primitive;
            this.params = params;
        }

        @Override
        protected XMLData doInBackground(String... arg0) {

            HttpURLConnection conn = null;
            XMLData xmlData = null;

            OutputStream os = null;
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            try {
                // test code
                StringBuffer body = new StringBuffer();
                if (ONECLICK_CARGPS_INSERT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                } else if (ONECLICK_GETUSERINFO_SELECT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                } else if (ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;
                }
                URL url = new URL(new String(Common.nullTrim(body.toString())
                        .getBytes("EUC-KR"), "Cp1252"));
                // URL url = new URL(new String(body.toString()));

                Log.i(TAG, "URL : = " + body.toString());
                Log.println(Log.ASSERT, TAG, "URL : = " + body.toString());
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
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    //202007_수정중
//                    if(getSetSelectJubboStart == "Y"){
//                        SituationService.selectedRpt_id = setFixSelectJubbo;
//                        Log.i("PM2541",setFixSelectJubbo);
//                        Log.i("PM2541",getSetSelectJubboStart);
//                    }

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
                    // String response = new String(byteData);
                    // Log.d("","responseData  = " + response);
                    if (response == null || response.equals("")) {
                        Log.e(TAG + "Response is NULL!! ", TAG + "Response is NULL!!");
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
                    } catch (XmlPullParserException e) {
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
                listFlag = true;
                String rtnResultCode = Common.nullCheck(result.get("result"));
                pastItem = new Patrol();
                Log.i("최11창유","PatrolMainActivity -- onPostExecute");
                if ("1000".equals(rtnResultCode)) {
                    nowUPT_CARTIME = Common.nullCheck(result.get("update_cartime"));

                    nowUPT_RPTID = Common.nullCheck(result.get("update_rpt_id"));
                    Log.d("", "isplay  nowUPT_RPTID = " + nowUPT_RPTID + ":" + nowUPT_CARTIME);
                    pastItem.setRpt_id(nowUPT_RPTID);
                    pastItem.setUpdate_cartime(nowUPT_CARTIME);

                    if (primitive.equals(ONECLICK_PATROLRCEPTINFO_SELECT)) {
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

                            item = new Patrol();
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

                            startCountMap.put(rpt_id, startcount);


                            if (nowUPT_RPTID.equals(rpt_id)) {
                                nowREG_DATE = Common.nullCheck(item.getReg_date());
                            }

                            if (i == 0) {
//								nowUpperRPTID = rpt_id;
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

                        }
                        //Log.println(Log.ASSERT,TAG,"ActionList");
                        //최창유 통신결과 확인
                        if (itemList != null) {
                            Log.println(Log.ASSERT, TAG, "최창유");
                            for (int i = 0; i < itemList.size(); i++) {
                                Log.println(Log.ASSERT, TAG, i + 1 + "번째 통신 결과 ");
                                Log.println(Log.ASSERT, TAG, itemList.get(i).toString());
                            }
                        }
                        displayGallery();
                    }
                } else if ("1001".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_PATROLRCEPTINFO_SELECT)) {
                        Log.d("", "list of resultCode = " + rtnResultCode);
                        nowUPT_CARTIME = Common.nullCheck(result.get("date"));
                    }
                }
            } catch (XmlPullParserException e) {
                //e.printStackTrace();
                Log.e("", "Error occured ActionList onPostExecute:예외");
            } finally {
                if (ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
                    if (progressDialog != null) {
                        listFlag = true;
                        progressDialog.dismiss();
                    }
                    ProgressFlag = false;
                }
            }//try-catch-finally

        }//onPostExcute
    }//ActionList


    String curRpt_id = "";
    String pastRpt_id = "";
    int isPlayPosition = -1;


    boolean playFlag = true;
    Patrol ppastItem = new Patrol();
    Patrol pastItem = new Patrol();

    //mp3플레이 여부.및 listview 재설정 여부
    public void isPlay() {
//		Log.d("displayGallery start");

//		if(displayFlag == true){
//			Log.d("",TAG+ " isplay type 1");
//			displayGallery();
//			displayFlag = false;
//			pastitemList = itemList;
//		}
//		Log.d("","isplay type 2 이것만 보면 된다 !! = " +""+beforeUPT_CARTIME+"===="+ nowUPT_CARTIME);
//		if(!beforeUPT_CARTIME.equals(nowUPT_CARTIME) || "".equals(Common.nullCheck(nowUPT_CARTIME))){//update_cartime 이 동일하지 않을 경우. 새로고침+음성
//			Log.d("",TAG+ " isplay type 2");
//			ppastItem = pastItem;
//			pastitemList = itemList;
//			displayGallery();
//			beforeUPT_CARTIME = nowUPT_CARTIME;
//			if("N".equals(pastItem.getEnd_yn())){
//				mp3Start(pastItem.getReg_type(), pastItem);
//			}
//			return;
//		}
//		
//		if(!ppastItem.getRpt_id().equals(pastItem.getRpt_id())){
//			Log.d("",TAG+ " isplay type 3");
//			ppastItem = pastItem;
//			pastitemList = itemList;
//			displayGallery();
//			beforeUPT_CARTIME = nowUPT_CARTIME;
//			if("N".equals(pastItem.getEnd_yn())){
//				mp3Start(pastItem.getReg_type(), pastItem);
//			}
//			return;
//		}
//		
//		if(null != pastitemList && null != itemList){
//			Log.d("","refreshCheck  out=  " + pastitemList.size()+":"+itemList.size());
//			if(pastitemList.size() != itemList.size()){
//				Log.d("",TAG+ " isplay type 4");
//				Log.d("","refreshCheck in =  " + pastitemList.size()+":"+itemList.size());
//				displayGallery();
//				pastitemList = itemList;
//			}
//		}else{
//			Log.d("",TAG+ " isplay type 5");
//			pastitemList = itemList;
//			displayGallery();
//		}
//		ppastItem = pastItem;
		
		/*Log.d("","isPlay uhuh =  ["+ppastItem.getRpt_id()+":"+ppastItem.getUse_yn()+":"+ppastItem.getUpdate_cartime()+"]  ["+pastItem.getRpt_id()+":"+pastItem.getUse_yn()+":"+pastItem.getUpdate_cartime()+"]");
		if(ppastItem.getRpt_id().equals("")){//이전 Patrol 정보가 없을 경우 새로고침 + 음성
			Log.d("","isPlay uhuh = type 1"+pastItem.getEnd_yn()+":"+pastItem.getReg_type());
			ppastItem = pastItem;
			displayGallery();
			beforeUPT_CARTIME = nowUPT_CARTIME;
			
			if("N".equals(pastItem.getEnd_yn())){
				Log.d("","isPlay uhuh = type 1-1 IN ############ ");
				mp3Start(pastItem.getReg_type(), pastItem);
			}
		}else{//이전 Patrol 정보가 있을 경우.
			if(ppastItem.getRpt_id().equals(pastItem.getRpt_id())){//과거 rpt_id와 현재 rpt_id 비교 동일할 경우
				Log.d("","isPlay uhuh = type 2-1 = " + beforeUPT_CARTIME+":"+nowUPT_CARTIME);
				ppastItem = pastItem;
				if(!beforeUPT_CARTIME.equals(nowUPT_CARTIME)){
					beforeUPT_CARTIME=nowUPT_CARTIME;
					displayGallery();
				}else {//알람 반복 울림, 확인하기 전까지(DB에 rpt_id ) 저장 전까지 알람 무한 반복.
					Log.d("","isPlay uhuh = type 2-2" +pastItem.getRpt_id());
					if( db.selectRptId(pastItem.getRpt_id()) == false ){//확인된 데이타가 없는 경우 음성 재생
						Log.d("","isPlay uhuh = type 2-2 IN" );
						ppastItem = pastItem;
						beforeUPT_CARTIME=nowUPT_CARTIME;
						if("N".equals(pastItem.getEnd_yn())){
							mp3Start(pastItem.getReg_type(), pastItem);
						}
					}
				}
				if(!ppastItem.getUpdate_cartime().equals(pastItem.getUpdate_cartime())){//update_cartime 이 동일하지 않을 경우. 새로고침+음성
					ppastItem = pastItem;
					displayGallery();
					beforeUPT_CARTIME = nowUPT_CARTIME;
					if("N".equals(pastItem.getEnd_yn())){
						mp3Start(pastItem.getReg_type(), pastItem);
					}
				}
				
			}else{//과거 rpt_id와 현재 rpt_id 비교 동일하지 않을 경우
				Log.d("","isPlay uhuh = type 2-3");
				ppastItem = pastItem;
				displayGallery();
				beforeUPT_CARTIME = nowUPT_CARTIME;
				if("N".equals(pastItem.getEnd_yn())){
					mp3Start(pastItem.getReg_type(), pastItem);
				}
			}
		}*/
//		
    }

    MediaPlayer player;

    // TimerTask ttm;
    public void mp3Start(String code, Patrol item) {

        Log.d("", "PatrolMainActivity mp3Start code = " + code);
        //String name = "etc.mp3";
        String name = "";
        //Toast.makeText(this,"메소드에 들옴",Toast.LENGTH_LONG).show();

        if ("0001".equals(code)) {// 기타접보
            name = "etc.mp3";
        } else if ("0002".equals(code)) {// 차단작업접보
            name = "block.mp3";
        } else if ("0003".equals(code)) {// 잡물접보
            name = "sundries.mp3";

        } else if ("0004".equals(code)) {// 고장차량
            name = "car_broke.mp3";
        } else if ("0005".equals(code)) {// 사고발생
            name = "accident.mp3";
        } else if ("0006".equals(code)) {// 지정체
            name = "tieup.mp3";
        } else if ("0007".equals(code)) {// 긴급견인
            name = "tow.mp3";
        } else if ("0008".equals(code)) {// 동물
            name = "animal.mp3";
        } else if ("0009".equals(code)) {// 노면(시설물)파손
            name = "facility.mp3";
        } else if ("0010".equals(code)) {// 불량차량
            name = "fault_car.mp3";
        } else if ("0011".equals(code)) {// 도로진입제한
            name = "entry.mp3";
        } else if ("9999".equals(code)) {
            SituationService.selectedRpt_id = "";
            SituationService.start_yn = "N";
            Common.setPrefString(PatrolMainActivity.this, "start_yn", "N");

            Calendar c = Calendar.getInstance();
            String date = "";
            try {
                Date nowDate = transFormat.parse(item.getEnd_time());
                c.setTime(nowDate);
                date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
            } catch (ParseException e) {
                //e.printStackTrace();
                Log.e("에러","예외");
            }

            name = "acdntend.mp3";
            if (null != contextActivity) {
                if (contextActivity.isFinishing() == false) {

                    AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                    ad.setMessage("")
                            .setTitle("확인")
                            .setMessage("상황명: " + item.getInp_val() + "\n" + date + "분 상황이 종료 되었습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    ad.show();
                }
            }
            Log.i("9999","patrol");
            SituationService.start_yn = "N";
            Common.setPrefString(PatrolMainActivity.this, "start_yn", "N");
            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Parameters params = new Parameters(ONECLICK_FILE_SEND);

        switch (requestCode) {
            //2020207 수정중  _ 사진이나 파일올리고 난 후에 버튼이 바뀌는 곳.
            case Configuration.PATROL_REG_RETURN:
                Log.d("", "onActivityResult PATROL_REG_RETURN");
                //if ("Y".equals(Common.getPrefString(PatrolMainActivity.this, "start_yn"))) {
                //202007
                if(getSetSelectJubboStart == "Y"){
                    // 수정중
                    // setStartBtn();
                } else {
                    setStartBtn();
                }
                break;

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

                    executeJob(params, PatrolMainActivity.this);
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

                    executeJob(params, PatrolMainActivity.this);

                }// end if

                // #endregion
                // --------------------------------------------------------------------------------------------
                break;

            default:
                break;
        }
    }

    // onActivityResult 카메라관련 파일경로및 파일명 받아오기
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {

    }

    AudioManager am = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:// 볼륨 업
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                if (curVol < maxVol) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, curVol, AudioManager.STREAM_MUSIC);
                }*/
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:// 볼륨 다운
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currVol > 0) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, currVol, AudioManager.STREAM_MUSIC);
                }*/
                break;
            case KeyEvent.KEYCODE_BACK://종료
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setMessage("").setTitle("종료").setMessage("* 앱 종료시 GPS데이타를 전송하지 않습니다. \n종료 하시겠습니까?").setCancelable(false).setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(PatrolMainActivity.this, SituationService.class);
                        if ("Y".equals(Common.getPrefString(PatrolMainActivity.this, "start_yn"))) {
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
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

}
