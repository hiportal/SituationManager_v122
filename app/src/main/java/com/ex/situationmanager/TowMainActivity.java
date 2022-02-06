package com.ex.situationmanager;

import java.io.ByteArrayOutputStream;
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

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ex.situationmanager.dto.Patrol;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

@SuppressLint("NewApi")
public class TowMainActivity extends BaseActivity {
    int position = 0;
    ArrayList<Patrol> itemList;
    ArrayList<Patrol> pastitemList;

    TextView detailTowTitle, detailTowJisa, detailTowTime, detailTowType, detailTowContent;

    ListView listView;

    ImageView towStart, towStartIng, towStop;
    ImageView towPicture, btnTowClear;
    ImageView btnSelectJisa;
    ImageView btnCustomerPhone;
    ImageView btnGoMap;
    ImageView btnGoDispatch;
    ImageView btnUserInfo;
    ImageView btnTowJochiReg;                        //통합DB 개발

    TextView btnJisaPhone;

    LinearLayout llbottomBtnDefault, llbottomBtnPhone;
    ImageView towPhone, towPhoneClose;
    TextView towpatPhone1, towpatPhone2, towpatPhone3, towpatPhone4, towpatPhone5;

    String TAG = "TowMainActivity";
    //하단 통화버튼 고객
    String customerPhone = "";
    //하단 통화버튼 순찰차
    String patrolPhone = "";
    String rptId;

    private Uri mUriSet;

    //*********************************Slide Menu  ************************************
    //변수선언
    //최창유 주석
    String[] navItems = {"전체", "내부직원", "순찰원", "견인원", "대국민"};
    //String[] navItems = {"전체","순찰원", "견인원", "대국민"};

    //String[] navItems = {"전체","순찰원", "견인원", "대국민"};
    String[] navItems_tow = {"전체", "견인원", "대국민"};
    ExpandableListView lvNavList;
    FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    //*********************************Slide Menu  ************************************
    ImageView menu;
    View selectedView = null;
    int selectedPosition = -1;

    boolean pastRpt_idIsSave = false;
    boolean ProgressFlag = false;
    ProgressDialog progressDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tow_main);
        ProgressFlag = true;
        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.towListView);

        towStart = (ImageView) findViewById(R.id.towStart);
        towStartIng = (ImageView) findViewById(R.id.towStartIng);
        towStop = (ImageView) findViewById(R.id.towStop);
        towPicture = (ImageView) findViewById(R.id.towPicture);
        btnTowClear = (ImageView) findViewById(R.id.btnTowClear);
        btnSelectJisa = (ImageView) findViewById(R.id.btnSelectJisa);
        towPhone = (ImageView) findViewById(R.id.towPhone);
        btnCustomerPhone = (ImageView) findViewById(R.id.btnCustomerPhone);
        btnJisaPhone = (TextView) findViewById(R.id.btnJisaPhone);
        btnUserInfo = (ImageView) findViewById(R.id.btnUserInfo);
        btnTowJochiReg = (ImageView) findViewById(R.id.btnTowJochiReg);

        detailTowTitle = (TextView) findViewById(R.id.detailTowTitle);
        detailTowJisa = (TextView) findViewById(R.id.detailTowJisa);
        detailTowTime = (TextView) findViewById(R.id.detailTowTime);
        detailTowType = (TextView) findViewById(R.id.detailTowType);
        detailTowContent = (TextView) findViewById(R.id.detailTowContent);

        llbottomBtnDefault = (LinearLayout) findViewById(R.id.llbottomBtnDefault);
        llbottomBtnPhone = (LinearLayout) findViewById(R.id.llbottomBtnPhone);

        towPhoneClose = (ImageView) findViewById(R.id.towPhoneClose);
        towpatPhone1 = (TextView) findViewById(R.id.towpatPhone1);
        towpatPhone2 = (TextView) findViewById(R.id.towpatPhone2);
        towpatPhone3 = (TextView) findViewById(R.id.towpatPhone3);
        towpatPhone4 = (TextView) findViewById(R.id.towpatPhone4);
        towpatPhone5 = (TextView) findViewById(R.id.towpatPhone5);
        btnGoMap = (ImageView) findViewById(R.id.btnGoMap);
        btnGoDispatch = (ImageView) findViewById(R.id.btnGoDispatch);

        towStart.setOnClickListener(this);
        towStartIng.setOnClickListener(this);
        towStop.setOnClickListener(this);
        towPicture.setOnClickListener(this);
        btnTowClear.setOnClickListener(this);
        btnSelectJisa.setOnClickListener(this);
        towPhone.setOnClickListener(this);
        btnCustomerPhone.setOnClickListener(this);
        btnJisaPhone.setOnClickListener(this);
        towPhoneClose.setOnClickListener(this);
        btnGoMap.setOnClickListener(this);
        btnGoDispatch.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        btnTowJochiReg.setOnClickListener(this);

        btnJisaPhone.setText(TowJeppBoJisaName);
        btnJisaPhone.setVisibility(View.GONE);
        towPhone.setVisibility(View.GONE);
        btnCustomerPhone.setVisibility(View.GONE);

        if (Configuration.User.getTel_no().length() >= 10) {
            btnJisaPhone.setVisibility(View.VISIBLE);
        } else {
            btnJisaPhone.setVisibility(View.GONE);
        }
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
            Log.e("에러","예외");
        }


        //*********************************Slide Menu  ************************************
        List<String> objects = new ArrayList<String>();
        if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
            navItems = navItems_tow;

            for (int i = 0; i < navItems.length; i++) {
                objects.add(navItems[i]);
            }

        } else {
            for (int i = 0; i < navItems.length; i++) {
                objects.add(navItems[i]);
            }
        }

        lvNavList = (ExpandableListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);

        sh = getSharedPreferences("userAllJisaList", MODE_PRIVATE);
        userInfo = sh.getString("userAllInfo", "");

        innerEmployeeList = new ArrayList<String>();
        sunchalJsonList = new ArrayList<String>();
        Log.i("userdsdfsdf", userInfo);
        try {
            jsonArray = new JSONArray(userInfo);
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.i("dfsdf", i + "");

                /*innerEmployeeList.add(jsonArray.getJSONObject(i).get("bsname") + ":" + jsonArray.getJSONObject(i).get("car_nm"));*/
                innerEmployeeList.add(jsonArray.getJSONObject(i).toString());
                if (jsonArray.getJSONObject(i).get("user_type").toString().equals("0001")) {
                    // sunchalJsonList.add(jsonArray.getJSONObject(i).get("bsname") + ":" + jsonArray.getJSONObject(i).get("car_nm"));
                    sunchalJsonList.add(jsonArray.getJSONObject(i).toString());
                }
             /*   Log.i("dfsdf",i+"");
                String userInfoString = jsonArray.getJSONObject(i).get("bsname") + " " + jsonArray.getJSONObject(i).get("car_nm");
                list_adapter.add(userInfoString);*/
            }
            //spinner_user_JisaList.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list_adapter));
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
    /*    List<String> objects = new ArrayList<String>();
        for (int i = 0; i < navItems.length; i++) {
            objects.add(navItems[i]);
        }*/
        ArrayList<ExpandableListAdapter.Item> dataList = new ArrayList<ExpandableListAdapter.Item>();
        if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {


            ExpandableListAdapter.Item group1 = new ExpandableListAdapter().new Item(navItems[0]);//
            dataList.add(group1);
            ExpandableListAdapter.Item group4 = new ExpandableListAdapter().new Item(navItems[1]);//견인
            dataList.add(group4);
            ExpandableListAdapter.Item group5 = new ExpandableListAdapter().new Item(navItems[2]);//견인
            dataList.add(group5);


        } else {
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
        }
        //
        ExpandableListAdapter apapter1 = new ExpandableListAdapter(this, R.layout.slide_row, R.layout.group_row, dataList);
        lvNavList.setAdapter(apapter1);
        lvNavList.setOnGroupClickListener(apapter1);
        lvNavList.setOnChildClickListener(apapter1);

        SlideAdapter adapter = new SlideAdapter(this, objects);
        //  lvNavList.setAdapter(adapter);
        //    lvNavList.setOnItemClickListener(new DrawerItemClickListener());
        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
        dtToggle = new ActionBarDrawerToggle(TowMainActivity.this, dlDrawer, R.drawable.ic_launcher, R.string.app_name, R.string.desc_list_item_icon) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dlDrawer.setDrawerListener(dtToggle);

//				getActionBar().setDisplayHomeAsUpEnabled(true);
        dlDrawer.closeDrawer(lvNavList);

        //*********************************Slide Menu  ************************************

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PatrolMainActivity.stopTimer_list();


        Parameters params = new Parameters(ONECLICK_GETPATROLTELNO_SELECT);//ONECLICK_TOWRCEPTINFO_SELECT

        if (null != Configuration.User.getBscode_list()) {
            if (Configuration.User.getBscode_list().size() > 0) {
                params.put("bscode", Configuration.User.getBscode_list().get(0));
            }
        }
        params.put("user_type", Configuration.User.getUser_type());
        params.put("tel_no", Configuration.User.getHp_no());
        new Action(ONECLICK_GETPATROLTELNO_SELECT, params).execute();//ONECLICK_TOWRCEPTINFO_SELECT--


        btnTowJochiReg.setVisibility(View.INVISIBLE);
//		detailTowContent.setTextSize(detailTowContent.getTextSize()*0.6f);

        if (!chkGpsService(this)) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(TowMainActivity.this);
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
    }

    //*********************************Slide Menu  ************************************
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("", "SlideMenu flContainer click btn position onOptionItemSelected");
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //최창유 주석
            if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL) || Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)) {
                switch (position) {
                    case 1:
                        Intent i_employee = new Intent(TowMainActivity.this, InnerEmployActivity.class);
                        startActivity(i_employee);
                        finish();
                        stopGPS();
                        stopTimer(TowMainActivity.this);
                        break;
                    // 순찰원
                    case 2:
                        Intent i_patrol = new Intent(TowMainActivity.this, PatrolMainActivity.class);
                        startActivity(i_patrol);
                        finish();

//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;
                    // 견인원
                    case 3:
                        Intent i_tow = new Intent(TowMainActivity.this, TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;

                    // 대국민
                    case 4:
                        Intent i_citizen = new Intent(TowMainActivity.this, MainActivity.class);
                        startActivity(i_citizen);
                        finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;

                    default:
                        break;
                }

				/*	switch (position) {
						case 1:

							Intent i_patrol = new Intent(TowMainActivity.this, PatrolMainActivity.class);
							startActivity(i_patrol);
							finish();

//						stopGPS();
//						stopTimer(TowMainActivity.this);
							break;
						// 견인원
						case 2:
							Intent i_tow = new Intent(TowMainActivity.this, TowMainActivity.class);
							startActivity(i_tow);
							finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
							break;

						// 대국민
						case 3:
							Intent i_citizen = new Intent(TowMainActivity.this, MainActivity.class);
							startActivity(i_citizen);
							finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
							break;

						default:
							break;
					}*/
            } else if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
                switch (position) {
                    // 견인원
                    case 1:
                        Intent i_tow = new Intent(TowMainActivity.this, TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;

                    // 대국민
                    case 2:
                        Intent i_citizen = new Intent(TowMainActivity.this, MainActivity.class);
                        startActivity(i_citizen);
                        finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;

                    default:
                        break;
                }
            }


            dlDrawer.closeDrawer(lvNavList);
        }

    }
    //*********************************Slide Menu  ************************************

    @Override
    protected void onResume() {
        super.onResume();
        if (null == progressDialog) {
            progressDialog = ProgressDialog.show(TowMainActivity.this, "", "로딩중...", true);
        }

        if ("".equals(btnJisaPhone.getText())) {
            btnJisaPhone.setText(BaseActivity.TowJeppBoJisaName);
        }
        contextActivity = TowMainActivity.this;
        VIEW_TAG = VIEW_TOW;
        db.close();
        db.init();
//		stopGPS();
//		startMyGps();
//		stopTimer(this);
//		startTimer();
        stopTimer_list();
        startTimer_list();

        pastItem = new Patrol();
        ppastItem = new Patrol();

        setStartBtn();
        boolean displayFlag = true;
    }

    boolean displayFlag = false;
    ArrayList<Patrol> sortItemList = new ArrayList<Patrol>();

    public void displayGallery() {
        Log.i("TMA 535", TAG + " displayGallery() Call start");
        detailClear();

        if (null != sortItemList) {
            sortItemList.clear();

            if (itemList != null) {
                for (int i = 0; i < itemList.size(); i++) {
                    sortItemList.add(itemList.get(i));
                }
            }

            final TowListAdapter adapter = new TowListAdapter(this, sortItemList);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    Patrol item = sortItemList.get(position);
                    if (Common.nullCheck(item.getReg_type()).contains("7")) {
                        btnTowJochiReg.setVisibility(View.VISIBLE);
                    }

                    //확인 내용 내부 DB 저장.(알람용)
                    db.insertRptId(item.getRpt_id(), item.getReg_date());

                    if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(TowMainActivity.this);
                        ad.setMessage("").setTitle("확인").setMessage("* 출동중 다른 제보내용을 \n선택할 수 없습니다. \n 처리완료 후 이용하여 주시기 바랍니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ad.show();

                        v.setSelected(false);
                        v.findViewById(R.id.rowTowLayout).setSelected(false);
                        v.setActivated(false);
                        v.findViewById(R.id.rowTowLayout).setActivated(false);

                        if (null != selectedView) {
                            selectedView.setSelected(true);
                            selectedView.findViewById(R.id.rowPatrolLayout).setSelected(true);
                        }
//						return;					
                    } else {
                        adapter.setItemImage(position);
                        selectedPosition = position;
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            listView.getChildAt(i).setSelected(false);
                            listView.getChildAt(i).findViewById(R.id.rowTowLayout).setSelected(false);
                        }

                        v.findViewById(R.id.rowTowLayout).setSelected(false);
                        if (null != selectedView) {
                            selectedView.setSelected(false);
                            selectedView.findViewById(R.id.rowTowLayout).setSelected(false);
                        }
                        v.setSelected(true);
                        v.findViewById(R.id.rowTowLayout).setSelected(true);
                    }
                }
            });
            if (sortItemList != null) {
                for (int i = 0; i < sortItemList.size(); i++) {
                    if (sortItemList.get(i).getRpt_id().equals(SituationService.selectedRpt_id)) {
                        listView.setSelection(i);
                        break;
                    }

                    if (i == sortItemList.size() - 1) {
                        SituationService.selectedRpt_id = "";
                    }
                }
            }
        }

        Log.i("TWA 609", TAG + " displayGallery() Call end");
    }

    //*********************************Slide Menu  ************************************
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


            if (convertView == null) {
                view = mInflater.inflate(R.layout.slide_row, null);
            }

            Log.d("", TAG + " SlideAdapter getview " + Configuration.User.getUser_type());
            if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {

                llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
                if (position == 0) {
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                } else if (position == 1) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_02);
                } else if (position == 2) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_03);
                }
            } else {
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


            }

            return view;
        }
    }
    //*********************************Slide Menu  ************************************


    public class TowListAdapter extends ArrayAdapter<Patrol> {

        private Context mContext;
        private LayoutInflater mInflater;

        public TowListAdapter(Context context, List<Patrol> objects) {
            super(context, 0, objects);

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;

            if (objects.size() == 0) {
                if (toastRunning == true) {
                    Toast.makeText(getApplicationContext(), "현재 접수된 상황이 없습니다.", Toast.LENGTH_SHORT).show();
                    Log.i("TOWMAIN 682","현재접수된상황이없습니다.");
                }
            }
            refreshAdapter();
        }

        public void refreshAdapter() {
            notifyDataSetChanged();
        }

        public void setItemImage(int position) {

            Patrol item = this.getItem(position);        // ArrayAdapter

            if ("N".equals(item.getEnd_yn())) {
                if (!Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
                    sendConfirm();//순찰자가 내용을 확인했다는 정보 전송.
                }
            }

            SituationService.selectedRpt_id = item.getRpt_id();
            SituationService.rpt_bscode = item.getBscode();
            SituationService.rpt_reg_type = item.getReg_type();
            SituationService.rpt_endtime = item.getEnd_time();

            //상세화면 뿌려주기
            if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                detailTowTitle.setText("상황명: " + TRAINSTR + item.getInp_val());
            } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                detailTowTitle.setText("상황명: " + PRACTICESTR + item.getInp_val());
            } else {
                detailTowTitle.setText("상황명: " + item.getInp_val());
            }
            detailTowJisa.setText("접보 : " + item.getLocal_nm());
            try {
                Calendar calendar = Calendar.getInstance();
                Date nowDate = transFormat.parse(item.getReg_date());
                calendar.setTime(nowDate);
                String year = String.format("%04d", calendar.get(Calendar.YEAR));
                String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
                String day = String.format("%02d", (calendar.get(Calendar.DAY_OF_MONTH)));
                String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
                String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
                detailTowTime.setText("시간 : " + year
                        + "-" + month
                        + "-" + day
                        + " " + hour
                        + ":" + minute
                );
//				date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) +":"+String.format("%02d", c.get(Calendar.MINUTE));
            } catch (NullPointerException e) {
                detailTowTime.setText("시간 :");
            } catch (ParseException ee) {
                detailTowTime.setText("시간 :");
            }

            detailTowType.setText("유형 : " + getReg_TypeName(item.getReg_type()));
            String detailText = "[" + Common.nullCheck(item.getDirection_name()).replace("||", "]") + "방향 " +
                    item.getStart_km() + "km " + getLaneName(item.getLane_num()) + "\n" + item.getReg_data();

            if (!"".equals(Common.nullCheck(item.getPsn_tel_no()))) {
                detailText = detailText + "\n당사자 : " + item.getPsn_tel_no();
            }

            if (!"".equals(Common.nullCheck(item.getReg_tel_no()))) {
                detailText = detailText + "\n제보자 : " + item.getReg_tel_no();
            }

            if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                detailTowContent.setText(TRAINSTR + detailText);
            } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                detailTowContent.setText(PRACTICESTR + detailText);
            } else {
                detailTowContent.setText(detailText);
            }


            // 재보자 전화번호버튼
            if (Common.nullCheck(item.getReg_tel_no()).length() >= 10) {
                btnCustomerPhone.setVisibility(View.VISIBLE);
            } else {
                btnCustomerPhone.setVisibility(View.GONE);
            }

            customerPhone = item.getReg_tel_no();
            patrolPhone = item.getPsn_tel_no();
            SituationService.rpt_latitude = item.getRpt_la();
            SituationService.rpt_longitude = item.getRpt_lo();
            setTowStopVisibility(item.getReg_type());

        }


        public View getView(int position, View convertView, ViewGroup parent) {

            TextView rowTowJisa;
            TextView rowTowTime;
            TextView rowTowType;
            TextView rowTowContent;
            TextView rowTowStat;
            LinearLayout rowTowLayout;

            View view = convertView;


            if (convertView == null) {
                view = mInflater.inflate(R.layout.tow_list_row, null);
            }
            final Patrol item = this.getItem(position);
            if (item != null) {
                rowTowJisa = (TextView) view.findViewById(R.id.rowTowJisa);
                rowTowTime = (TextView) view.findViewById(R.id.rowTowTime);
                rowTowType = (TextView) view.findViewById(R.id.rowTowType);
                rowTowContent = (TextView) view.findViewById(R.id.rowTowContent);
                rowTowStat = (TextView) view.findViewById(R.id.rowTowStat);
                rowTowLayout = (LinearLayout) view.findViewById(R.id.rowTowLayout);

                rowTowJisa.setText(Common.nullCheck(item.getLocal_nm()).replace("지사", ""));
                rowTowTime.setText(item.getReg_time());
                rowTowType.setText(getReg_TypeName(item.getReg_type()));

                if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                    rowTowContent.setText(TRAINSTR + item.getReg_data());
                } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                    rowTowContent.setText(PRACTICESTR + item.getReg_data());
                } else {
                    rowTowContent.setText(item.getReg_data());
                }

                rowTowStat.setText(Common.nullZeroCheck(item.getStartcount()));

                if (pastItem.getRpt_id().equals(item.getRpt_id())) {
                    rowTowJisa.setTextColor(Color.parseColor("#ff0000ff"));
                    rowTowTime.setTextColor(Color.parseColor("#ff0000ff"));
                    rowTowType.setTextColor(Color.parseColor("#ff0000ff"));
                    rowTowContent.setTextColor(Color.parseColor("#ff0000ff"));
                    rowTowStat.setTextColor(Color.parseColor("#ff0000ff"));
                } else {
                    rowTowJisa.setTextColor(Color.parseColor("#000000"));
                    rowTowTime.setTextColor(Color.parseColor("#000000"));
                    rowTowType.setTextColor(Color.parseColor("#000000"));
                    rowTowContent.setTextColor(Color.parseColor("#000000"));
                    rowTowStat.setTextColor(Color.parseColor("#000000"));
                }
                if (SituationService.selectedRpt_id.equals(item.getRpt_id())) {
                    rowTowLayout.setSelected(true);
                    setItemImage(position);
                } else {
                    rowTowLayout.setSelected(false);
                }

                rptId = item.getRpt_id();

                /*rowTowStat.setOnClickListener(new OnClickListener() {
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
                                Log.e("에러", "예외");
                            }
                        }

                    }
                });*/

            }
            return view;
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
            Log.d("", "getview position " + position);
            if (view == null) {
                view = inflater.inflate(groupLayout, viewGroup, false);
            }
            //최창유 주석

            LinearLayout llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            LinearLayout tllSlide = (LinearLayout) view.findViewById(R.id.llSlide);

            if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {

				/*llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
				if(position == 0){
					llSlide.setBackgroundResource(R.drawable.tit_trans);
				}else if(position == 1){
					llSlide.setBackgroundResource(R.drawable.item_slide_02);
				}else if(position == 2){
					llSlide.setBackgroundResource(R.drawable.item_slide_03);
				}*/
                if (i == 0) {

                    tllSlide.setBackgroundResource(R.drawable.tit_trans);
                    tllSlide.setTag("전체");
                } else if (i == 1) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_02);
                    llSlide.setTag("견인");
                } else if (i == 2) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_03);
                    llSlide.setTag("대민");
                }

            } else {

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
            } else if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
                switch (i) {
                    // 견인원
                    case 1:
                        Intent i_tow = new Intent(TowMainActivity.this, TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;

                    // 대국민
                    case 2:
                        Intent i_citizen = new Intent(TowMainActivity.this, MainActivity.class);
                        startActivity(i_citizen);
                        finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;

                    default:
                        break;

                }
                return true;
            } else if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
                if (i == 1 && dataList.get(0).child.size() == 1) {//내부직원 직원 리스트가 1명 이면 그대로 이동
                    VIEW_TAG = VIEW_INNEREMPLOYEE;
                    Intent i_employee = new Intent(TowMainActivity.this, InnerEmployActivity.class);
                    startActivity(i_employee);
                    finish();
                    stopGPS();
                    stopTimer(TowMainActivity.this);
                    return true;
                } else if (i == 2 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                    Intent i_patrol = new Intent(TowMainActivity.this, PatrolMainActivity.class);
                    startActivity(i_patrol);
                    finish();
                    stopGPS();
                    stopTimer(TowMainActivity.this);
                    return true;
                } else if (i == 3 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                    Intent i_tow = new Intent(TowMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(TowMainActivity.this);
                    return true;
                } else if (i == 4 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                    Intent i_citizen = new Intent(TowMainActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(TowMainActivity.this);
                    return true;
                } else {
                    return false;
                }
            }


            /*else if (i == 1 && dataList.get(0).child.size() == 1) {//내부직원 직원 리스트가 1명 이면 그대로 이동
                VIEW_TAG = VIEW_INNEREMPLOYEE;
                Intent i_employee = new Intent(TowMainActivity.this, InnerEmployActivity.class);
                startActivity(i_employee);
                finish();
                stopGPS();
                stopTimer(TowMainActivity.this);
                return true;
            } else if (i == 2 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                Intent i_patrol = new Intent(TowMainActivity.this, PatrolMainActivity.class);
                startActivity(i_patrol);
                finish();
                stopGPS();
                stopTimer(TowMainActivity.this);
                return true;
            } else if (i == 3 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                Intent i_tow = new Intent(TowMainActivity.this,
                        TowMainActivity.class);
                startActivity(i_tow);
                finish();
                stopGPS();
                stopTimer(TowMainActivity.this);
                return true;
            } else if (i == 4 && dataList.get(2).child.size() == 1) {//순찰 리스트가 1개이면 그대로 이동
                Intent i_citizen = new Intent(TowMainActivity.this,
                        MainActivity.class);
                startActivity(i_citizen);
                finish();
                stopGPS();
                stopTimer(TowMainActivity.this);
                return true;
            }*/

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

                        SituationService.conf.User.getBscode_list().add(Common.nullCheck(job.getString("bscode")));
                        SituationService.conf.User.getBsname_list().clear();
                        SituationService.conf.User.getBsname_list().add(Common.nullCheck(job.getString("bsname")));
                        SituationService.conf.User.setPatcar_id(Common.nullCheck(job.getString("patcar_id")));
                        SituationService.conf.User.setCar_nm(Common.nullCheck(job.getString("car_nm")));
                        VIEW_TAG = VIEW_INNEREMPLOYEE;
                        Intent i_employee = new Intent(TowMainActivity.this, InnerEmployActivity.class);
                        startActivity(i_employee);
                        finish();
                        stopGPS();
                        stopTimer(TowMainActivity.this);
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
                        VIEW_TAG = VIEW_INNEREMPLOYEE;
                        Intent i_patrol = new Intent(TowMainActivity.this, PatrolMainActivity.class);
                        startActivity(i_patrol);
                        finish();
                        stopGPS();
                        stopTimer(TowMainActivity.this);
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
                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    }
                    Intent i_tow = new Intent(TowMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(TowMainActivity.this);
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
                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    }
                    Intent i_citizen = new Intent(TowMainActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(TowMainActivity.this);
                    break;
                default:
                    break;
            }

            return false;
        }

        public class Item {
            public ArrayList<String> child;
            public String groupName;

            public Item(String name) {
                groupName = name;
                child = new ArrayList<String>();
            }
        }
    }

    //견인완료 권한 관리 비지블 관리
    public void setTowStopVisibility(String reg_type) {
        if (reg_type.equals("0007")) {//긴급견인
            towStop.setVisibility(View.VISIBLE);
        } else {
            towStop.setVisibility(View.GONE);
        }
    }

    //등록 명 가져오기
    public String getReg_TypeName(String code) {


        if ("0001".equals(code)) return "기타";
        if ("0002".equals(code)) return "차단작업";
        if ("0003".equals(code)) return "잡물";
        if ("0004".equals(code)) return "고장차량";
        if ("0005".equals(code)) return "사고발생";
        if ("0006".equals(code)) return "지정체";
        if ("0007".equals(code)) return "긴급견인";
        if ("0008".equals(code)) return "동물";
        if ("0009".equals(code)) return "노면(시설물)파손";
        if ("0010".equals(code)) return "불량차량";
        if ("0011".equals(code)) return "도로진입제한";
        if ("0012".equals(code)) return "재난발생";
        if ("0013".equals(code)) return "터널화재";
        return "";
    }

    public String getLaneName(String code) {
        if ("0001".equals(code)) return "1차로";
        if ("0002".equals(code)) return "2차로";
        if ("0003".equals(code)) return "3차로";
        if ("0004".equals(code)) return "4차로";
        if ("0005".equals(code)) return "5차로";
        return "";
    }

    public String getResultName(String code) {
        if ("0001".equals(code)) return "접보";
        if ("0002".equals(code)) return "순찰차확인";
        if ("0003".equals(code)) return "기타";
        if ("0004".equals(code)) return "본부전달완료";
        if ("0005".equals(code)) return "완료";
        return "";
    }

    //상세화면 초기화.
    public void detailClear() {
//		
        rpt_bscode = "";
        rpt_reg_type = "";
        rpt_bhPkCode = "";

        detailTowTitle.setText("상황명 : ");
        detailTowJisa.setText("접보 : ");
        detailTowTime.setText("시간 : ");
        detailTowType.setText("유형 : ");
        detailTowContent.setText("");

    }

    public boolean refreshList = true;

    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
        try {
            if (e == null) {
                if (null != result) {
                    Log.i("result", result.toString());
                    String rtnResultCode = result.get("result");
                    if ("1000".equals(rtnResultCode)) {
                        if (primitive.equals(ONECLICK_WORKCOMPLETE_UPDATE)) {
                            displayGallery();
                            setStartBtn();
                        } else if (primitive.equals(ONECLICK_GETPATROLTELNO_SELECT)) {
                            result.setList("entity");
                            if (result.size() > 0) {
                                towPhone.setVisibility(View.VISIBLE);

                                for (int i = 0; i < result.size(); i++) {
                                    String car_nm = result.get(i, "car_nm");
                                    final String tel_no = result.get(i, "tel_no");

                                    if (Configuration.User.getHp_no().equals(tel_no)) {
                                        return;
                                    }
                                    if (tel_no.startsWith("012")) {
                                        return;
                                    }

                                    if (i == 0) {
                                        towpatPhone1.setVisibility(View.VISIBLE);
                                        towpatPhone1.setText(car_nm);
                                        towpatPhone1.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent towpatPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(towpatPhone1Intent);

                                            }
                                        });
                                    } else if (i == 1) {
                                        towpatPhone2.setVisibility(View.VISIBLE);
                                        towpatPhone2.setText(car_nm);
                                        towpatPhone2.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent towpatPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(towpatPhone1Intent);

                                            }
                                        });
                                    } else if (i == 2) {
                                        towpatPhone3.setVisibility(View.VISIBLE);
                                        towpatPhone3.setText(car_nm);
                                        towpatPhone3.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent towpatPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(towpatPhone1Intent);

                                            }
                                        });
                                    } else if (i == 3) {
                                        towpatPhone4.setVisibility(View.VISIBLE);
                                        towpatPhone4.setText(car_nm);
                                        towpatPhone4.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent towpatPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(towpatPhone1Intent);

                                            }
                                        });
                                    } else if (i == 4) {
                                        towpatPhone5.setVisibility(View.VISIBLE);
                                        towpatPhone3.setText(car_nm);
                                        towpatPhone3.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent towpatPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                                startActivity(towpatPhone1Intent);

                                            }
                                        });
                                    }

                                }//for
                            } else {
                                towPhone.setVisibility(View.GONE);
                            }
                        } else if (primitive.equals(ONECLICK_RCVJISALIST_UPDATE)) {
                            Log.i(TAG, TAG + " onpostexecute ONECLICK_RCVJISALIST_UPDATE");
                        }

                    } else if ("9999".equals(rtnResultCode)) {
                        if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                            if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
                                Patrol item = new Patrol();
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

                                mp3Start(rtnResultCode, item);
                                setStartBtn();
                                beforeUPT_CARTIME = "";
                                displayFlag = true;
                            }

                        }
                    } else if ("9998".equals(rtnResultCode)) {
                        if (primitive.equals(ONECLICK_CARGPS_INSERT)) {

                            if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
                                SituationService.start_yn = "N";
                                Common.setPrefString(TowMainActivity.this, "start_yn", "N");
                                SituationService.selectedRpt_id = "";

                                Patrol item = new Patrol();
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

                                mp3Start(rtnResultCode, item);
                                setStartBtn();
                                beforeUPT_CARTIME = "";
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e2) {
            Log.e("에러", "예외");
        } catch (XmlPullParserException e3) {
            Log.e("에러", "예외 ");
        }

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

        pastItem = new Patrol();
        ppastItem = new Patrol();
        stopTimer_list();
        startTimer_list();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
            stopTimer_list();
        }

    }

    //출동, 출동중 버튼 visible 관리 ( 모두지우기 버튼 추가)
    public synchronized void setStartBtn() {
        towStartIng.setVisibility(View.VISIBLE);
        towStart.setVisibility(View.VISIBLE);
        btnTowClear.setVisibility(View.VISIBLE);
        if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
            towStart.setVisibility(View.GONE);
            btnTowClear.setVisibility(View.INVISIBLE);
        } else {
            towStartIng.setVisibility(View.GONE);
            btnTowClear.setVisibility(View.VISIBLE);
        }
    }

    //순찰차전화버튼 클릭시 visible 관리
    public synchronized void setPhoneBtnVisible(boolean phone) {
        llbottomBtnPhone.setVisibility(View.VISIBLE);
        llbottomBtnDefault.setVisibility(View.VISIBLE);
        if (phone == true) {
            llbottomBtnDefault.setVisibility(View.GONE);
        } else {
            llbottomBtnPhone.setVisibility(View.GONE);
        }
    }

    Dialog dialogGallery;

    @Override
    public void onClick(View v) {
        super.onClick(v);

        // 12 견인 목록
        if (!Configuration.User.getUser_type().equals(USER_TYPE_TOW)
                && v.getId() != R.id.menu
                && v.getId() != R.id.btnUserInfo) {
            Toast.makeText(this, "견인 계정이 아닙니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.btnTowJochiReg:
                if ("".equals(common.nullCheck(SituationService.selectedRpt_id))) {
                    Toast.makeText(TowMainActivity.this, "상황 선택후 이용해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent jochiIntent = new Intent(TowMainActivity.this, DialogJochi.class);
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
            case R.id.btnGoMap:
                String url = "http://map.naver.com/?slat=" + latitude + "&slng=" + longitude + "&sText=내위치&elat=" + rpt_latitude + "&elng=" + rpt_longitude + "&eText=고객위치&mapMode=1";
                Log.d(TAG, TAG + "  gomap url = " + url);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;
            case R.id.btnGoDispatch:
                if (rptId != null) {
                    Intent in = new Intent(getApplicationContext(), DialogActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("rpt_id", rptId);
                    PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0, in, PendingIntent.FLAG_ONE_SHOT);
                    try {
                        intent.send();
                    } catch (PendingIntent.CanceledException e) {
                        Log.e("에러", "예외");
                    }
                } else {
                    Toast.makeText(TowMainActivity.this, "접보를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnJisaPhone:
                Intent jisa = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Configuration.User.getTel_no()));
                startActivity(jisa);
                break;
            case R.id.btnCustomerPhone:
                Intent custom = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customerPhone));
                startActivity(custom);
                break;
            case R.id.towPhone:
                setPhoneBtnVisible(true);
                break;
            case R.id.towPhoneClose:
                setPhoneBtnVisible(false);
                break;
            case R.id.towStartIng:
                AlertDialog.Builder ading = new AlertDialog.Builder(TowMainActivity.this);
                ading.setMessage("").setTitle("확인").setMessage("* 출동중 입니다. \n취소 하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SituationService.actionStop(TowMainActivity.this);
                        setStartBtn();
                        sendStop(true);
                    }
                });
                ading.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ading.show();
                break;
            case R.id.towStart:
                if (null == itemList) {
                    SituationService.selectedRpt_id = "";
                } else if (itemList.size() < 1) {
                    SituationService.selectedRpt_id = "";
                }
                if ("".equals(Common.nullCheck(SituationService.selectedRpt_id))) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(TowMainActivity.this);
                    ad.setMessage("").setTitle("확인").setMessage("* 상황 목록 선택후 출동하실 수 있습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    ad.show();
                    return;
                } else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(TowMainActivity.this);
                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    ad.setMessage("").setTitle("확인").setMessage(detailTowTitle.getText() + "\n* 출동 하시겠습니까?.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //통합DB 개발로 인한 주석처리 배포시 주석 풀기
                            SituationService.actionStart(TowMainActivity.this);
                            setStartBtn();

                            //통합DB 개발
                            Intent directionIntent = new Intent(TowMainActivity.this, DialogDirection.class);
                            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, directionIntent, PendingIntent.FLAG_ONE_SHOT);
                            try {
                                pi.send();
                            } catch (PendingIntent.CanceledException ee) {
                                //ee.printStackTrace();
                                Log.e("에러", "예외");
                            }
                        }
                    });
                    ad.show();
                }

                break;

            case R.id.towStop:
                if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
                    AlertDialog.Builder adStop = new AlertDialog.Builder(TowMainActivity.this);
                    adStop.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    adStop.setMessage("").setTitle("확인").setMessage(detailTowTitle.getText() + "\n* 견인 완료 하시겠습니까?.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SituationService.actionStop(TowMainActivity.this);
                            setStartBtn();
                            displayGallery();
                            sendStop(false);
                        }
                    });
                    adStop.show();
                } else {
                    AlertDialog.Builder adStop = new AlertDialog.Builder(TowMainActivity.this);
                    adStop.setMessage("").setTitle("확인").setMessage("출동중 견인완료 하실 수 있습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    adStop.show();
                }
                SituationService.move_direct = "";
                break;

            case R.id.btnTowClear:
                rptId = null;

                if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
                    AlertDialog.Builder abdclear = new AlertDialog.Builder(TowMainActivity.this);
                    abdclear.setMessage("").setTitle("확인").setMessage("* 출동중 삭제 하실 수 없습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    abdclear.show();
                } else {
                    detailClear();
                    SituationService.selectedRpt_id = "";
                    for (int i = 0; i < listView.getChildCount(); i++) {
                        listView.getChildAt(i).setSelected(false);
                        listView.getChildAt(i).findViewById(R.id.rowTowLayout).setSelected(false);
                        listView.getChildAt(i).setActivated(false);
                        listView.getChildAt(i).findViewById(R.id.rowTowLayout).setActivated(false);
                    }
                }

                break;
            case R.id.btnSelectJisa:
                if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
                    AlertDialog.Builder abdclear = new AlertDialog.Builder(TowMainActivity.this);
                    abdclear.setMessage("").setTitle("확인").setMessage("* 출동중 선택하실 수 없습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    abdclear.show();
                } else {

                    Intent i = new Intent(getApplicationContext(), DialogTowSelectJisaActivity.class);
                    startActivityForResult(i, Configuration.SELECT_JISA);
                }

                break;
            case R.id.btnUserInfo:
                break;
            case R.id.towPicture:

                dialogGallery = new Dialog(TowMainActivity.this, R.style.FullHeightDialog);

                dialogGallery.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogGallery.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogGallery.setContentView(R.layout.cust_dialog_patrol);
                dialogGallery.setCancelable(true);

                LinearLayout layoutMain = (LinearLayout) dialogGallery.findViewById(R.id.llMediaMain);
                layoutMain.setContentDescription("" + detailTowTitle.getText());

                TextView tvTitleStartup = (TextView) dialogGallery.findViewById(R.id.tvTitleStartup);
                tvTitleStartup.setText("" + detailTowTitle.getText());


                //이미지 Gallery
                ImageView buttonGallery = (ImageView) dialogGallery.findViewById(R.id.goCamera);
                buttonGallery.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runCamera();
                        dialogGallery.dismiss();
                    }
                });

                //앨범 Gallery
                ImageView buttonGallery1 = (ImageView) dialogGallery.findViewById(R.id.goAlbum);
                buttonGallery1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(TowMainActivity.this, AndroidCustomGalleryActivity.class);
                        startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

                        dialogGallery.dismiss();
                    }
                });
                ImageView buttonGallery2 = (ImageView) dialogGallery.findViewById(R.id.goPreview);
                buttonGallery2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setType("image/*");
                        startActivity(i);

                    }
                });
                //닫기 버튼
                ImageView buttonGallery3 = (ImageView) dialogGallery.findViewById(R.id.ibtnClose);
                buttonGallery3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogGallery.dismiss();
                    }
                });

                dialogGallery.show();
                break;
            default:
                break;
        }
    }

    public void runCamera() {
        Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity2.class);
        startActivityForResult(cameraIntent, Configuration.IMAGE_CAPTURE);
    }

    /**
     * 순찰,견인 목록 조회 타이머
     * 5초단위 실행
     */
    static Timer listTimer = new Timer();
    Handler listHandler = new Handler();
    static int listTimerDelay = 15000;//default 5초

    //목록 통신 동기화를 위함.
    boolean listFlag = true;

    public void startTimer_list() {
        Log.i("TOWMAIN START_TIMER","1823");
        TimerTask timertask = new TimerTask() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (listFlag == false) {
                        return;
                    }
                    Log.i("TOWMAIN START_TIMER","1832");
                    String rtnStr = db.fetchBBBsCodeSelected();
                    Log.i("TOWMAIN now 1834 = ", nowUPT_CARTIME);
                    if (VIEW_TOW.equals(VIEW_TAG)) {
                        Log.i("TOWMAIN START_TIMER", "1835");  // 수정

                        // if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)){

                        Parameters params = new Parameters(ONECLICK_TOWRCEPTINFO_SELECT);
                        // params.put("sms_group", SituationService.conf.User.getGroup_id());
                        Log.i("TOWMAIN START_TIMER", "1839");


                        if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                            params.put("bscode", rtnStr);
                            try {
                                Log.i("TOWMAIN nowUPT1 = ", nowUPT_CARTIME);
                                nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                                Log.i("TOWMAIN START nowUPT = ", nowUPT_CARTIME);
                                Log.i("TOWMAIN START nowREG = ", nowREG_DATE);
                                Log.i("TOWMAIN START_TIMER", "1845");
                            } catch (NullPointerException e) {
                                /*e.printStackTrace();*/
                                Log.e("에러", "예외");
                            }
                            //params.put("update_cartime", nowUPT_CARTIME);
                            //params.put("reg_date", nowREG_DATE);
                            String car_id = "";
                            for (int i = 0; i < Configuration.User.getCrdns_id_list().size(); i++) {
                                if (i == 0) {
                                    car_id = Configuration.User.getCrdns_id_list().get(i);
                                    Log.i("TOWMAIN START_TIMER", "1856");
                                } else {
                                    car_id += "|" + Configuration.User.getCrdns_id_list().get(i);
                                    Log.i("TOWMAIN START_TIMER", "1859");
                                }
                            }
                            params.put("car_id", car_id);

                            byte[] hp_noByte = seed.encrypt(Configuration.USER_PHONE_NUMBER + "", szKey);
                            String enchp_noByte = seed.renameSpecificChar(new String(Base64.encodeBase64(hp_noByte)));
                            params.put("hp_no", enchp_noByte);
                        }

                        new ActionList(ONECLICK_TOWRCEPTINFO_SELECT, params).execute("");
                        // 0904수정중
                        if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
                            Log.i("TOWMAIN START_TIMER", "1867");
                            if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                                params.put("bscode", Configuration.User.getBscode_list().get(0));
                                Log.i("TOWMAIN START_TIMER", "1870");
                            }
                            listFlag = false;
//                        new ActionList(ONECLICK_TOWRCEPTINFO_SELECT, params).execute("");

//                        listFlag = false;
//                        new ActionList(ONECLICK_TOWRCEPTINFO_SELECT, params).execute("");
                        Log.i("TOWMAIN START_TIMER", "1875");


                    } else {
                            Log.d("", TAG + "time check at Timer" + nowUPT_CARTIME + "  |||  " + SituationService.nowUPT_CARTIME);
                            Log.i("TOWMAIN START_TIMER","1878");
                            Log.i("TOWMAIN situaion : ",SituationService.nowUPT_CARTIME);
                            Log.i("TOWMAIN START now : ",nowUPT_CARTIME);

                            if (!SituationService.nowUPT_CARTIME.equals(nowUPT_CARTIME)) {
                                Log.i("TOWMAIN START_TIMER","1880");
//								01-19 09:33:21.034: D/(21042): TowMainActivitytime check at Timer20180119072446  |||  20180119072446
//								if(null != itemList){
                                itemList = SituationService.itemList;
                                nowUPT_CARTIME = SituationService.nowUPT_CARTIME;
//								nowUpperRPTID = SituationService.nowUpperRPTID;
                                nowREG_DATE = SituationService.nowREG_DATE;
                                pastItem = SituationService.pastItem;
                                Log.i("TOWMAIN START_TIMER","1888");
                                Log.i("", TAG + "startTimer_list() -> displayGallery() Call start");
                                displayGallery();
                                Log.i("", TAG + "startTimer_list() -> displayGallery() Call end");
                                Log.i("TOWMAIN START_TIMER","1892");
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                    ProgressFlag = false;
                                    Log.i("TOWMAIN START_TIMER","1897");
                                }
                                Log.i("TOWMAIN START_TIMER","1899");
                            }//if
                            Log.i("TOWMAIN START_TIMER","1901");
                    }
                        Log.i("TOWMAIN START_TIMER","1903");
                        if (ProgressFlag == true) {
                            ProgressFlag = false;
                        } else {
                            if (null != progressDialog) {
                                progressDialog.dismiss();
                                progressDialog = null;
                                ProgressFlag = false;
                            }
                        }
                        Log.i("TOWMAIN START_TIMER","1913");
                    }
                }
            };


            @Override
            public void run() {
                listHandler.post(runnable);
            }
        };
        listTimer = new Timer();
        listTimer.schedule(timertask, 1000, listTimerDelay);// 100MS 뒤시작, 1초 간격으로 호출.

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

    //업데이트 시간 비교
    // 업데이트 시간 비교
    public String beforeUPT_RPTID = "";
    public String beforeUPT_CARTIME = "";
    public String beforeUse_yn = "";
    public String beforeEnd_yn = "";
    public String nowUPT_RPTID = "";
    public String nowUPT_CARTIME = "";
    public String nowUse_yn = "";
    public String nowEnd_yn = "";

    public String nowREG_DATE = "";

    public Map<String, String> lastStartCountMap = new HashMap<String, String>();
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public class ActionList extends AsyncTask<String, Void, XMLData> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
        ProgressDialog proDialog;

        String primitive = "";
        Parameters params = null;

        @Override
        protected void onPreExecute() {
            if (ProgressFlag && ONECLICK_TOWRCEPTINFO_SELECT.equals(primitive)) {
                proDialog = ProgressDialog.show(TowMainActivity.this, "", "로딩중...", true);
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
                } else if (ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;
                    Log.i("", TAG + " " + primitive + "synchronized start ");
                } else if (ONECLICK_TOWRCEPTINFO_SELECT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    Log.i("", TAG + " " + primitive + "synchronized start ");
                }
                URL url = new URL(new String(Common.nullTrim(body.toString()).getBytes("EUC-KR"), "Cp1252"));
                // URL url = new URL(new String(body.toString()));

                Log.i(TAG, "URL : = " + body.toString());

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
//					String response = new String(byteData);
//					Log.d("","responseData  = " + response);
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
                            } else {
                                String retMsg = xmlData.get("resultMessage");
                            }
                        }
                    } catch (NullPointerException e) {
                        listFlag = true;
                        ProgressFlag = false;
                        Log.i("", TAG + " " + primitive + "synchronized end ");
                    } catch (XmlPullParserException ee) {
                        listFlag = true;
                        ProgressFlag = false;
                        Log.i("", TAG + " " + primitive + "synchronized end ");
                    }
                }

            } catch (IOException e) {
                listFlag = true;
                ProgressFlag = false;
                Log.i("", TAG + " " + primitive + "synchronized end ");
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (Exception e) {
                    }
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }

                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {
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
                ProgressFlag = false;
                Log.i("", TAG + " " + primitive + "synchronized end ");
                setStartBtn();
                String rtnResultCode = Common.nullCheck(result.get("result"));
                Log.i("Towmain", "2138줄");

                if ("1000".equals(rtnResultCode)) {
                    pastItem = new Patrol();
                    nowUPT_CARTIME = Common.nullCheck(result.get("update_cartime"));
                    Log.i("nowUPT_CARTIME 2142" , nowUPT_CARTIME);
                    nowUPT_RPTID = Common.nullCheck(result.get("update_rpt_id"));
                    Log.d("", "1 wowza " + nowUPT_CARTIME);
                    pastItem.setRpt_id(nowUPT_RPTID);
                    pastItem.setUpdate_cartime(nowUPT_CARTIME);

                    if (primitive.equals(ONECLICK_TOWRCEPTINFO_SELECT)) {
                        Log.d(TAG, TAG + "list of resultCode = " + rtnResultCode);
                        listFlag = true;
                        result.setList("entity");
                        Log.d("", TAG + " onPostExecute size = " + result.toString());
                        Patrol item = null;
                        itemList = new ArrayList<Patrol>();

                        Map<String, String> startCountMap = new HashMap<String, String>();

                        for (int i = 0; i < result.size(); i++) {
                            String rpt_id = result.get(i, "rpt_id");
                            String bscode = result.get(i, "bscode");
                            String reg_date = result.get(i, "reg_date");
                            String reg_time = result.get(i, "reg_time");
                            String reg_type = result.get(i, "reg_type");
                            String reg_info = result.get(i, "reg_info");
                            String reg_data = result.get(i, "reg_data");
                            String inp_val = result.get(i, "inp_val");
                            String local_way = result.get(i, "local_way");
                            String start_km = result.get(i, "start_km");
                            String lane_num = result.get(i, "lane_num");
                            //개인정보 보호법으로 인하여 전화번호를 받지 않는다. (당사자, 제보자)
//						String psn_tel_no = result.get(i, "psn_tel_no");
//						String reg_tel_no = result.get(i, "reg_tel_no");
                            String r_result = result.get(i, "r_result");
                            String local_nm = result.get(i, "local_nm");
                            String direction_name = result.get(i, "direction_name");
                            String startcount = result.get(i, "startcount");
                            String rpt_lo = result.get(i, "rpt_lo");
                            String rpt_la = result.get(i, "rpt_la");
                            String end_time = result.get(i, "end_time");
                            String use_yn = result.get(i, "use_yn");
                            String end_yn = result.get(i, "end_yn");

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
                            //개인정보 보호법으로 인하여 전화번호를 받지 않는다. (당사자, 제보자)
//					    	item.setPsn_tel_no(psn_tel_no);
//						    item.setReg_tel_no(reg_tel_no);
                            item.setR_result(r_result);
                            item.setLocal_nm(local_nm);
                            item.setDirection_name(direction_name);
                            item.setStartcount(startcount);
                            item.setRpt_la(rpt_la);
                            item.setRpt_lo(rpt_lo);
                            item.setEnd_time(end_time);
                            item.setUse_yn(use_yn);
                            item.setEnd_yn(end_yn);

                            startCountMap.put(rpt_id, startcount);

                            if (nowUPT_RPTID.equals(rpt_id)) {
                                nowREG_DATE = Common.nullCheck(item.getReg_date());
                            }

                            if ("Y".equals(use_yn) && "N".equals(end_yn)) {
                                if (rpt_id.equals(nowUPT_RPTID)) {
                                    pastItem.setEnd_time(end_time);
                                    pastItem.setEnd_yn(end_yn);
                                    pastItem.setUse_yn(use_yn);
                                    pastItem.setReg_type(reg_type);
                                    pastItem.setStartcount(startcount);
                                }
                                itemList.add(item);
                            }
                        }
                        displayGallery();
//						isPlay();
                    }


                } else if ("1001".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_TOWRCEPTINFO_SELECT)) {
                        Log.d(TAG, TAG + "list of resultCode = " + rtnResultCode);
                        nowUPT_CARTIME = Common.nullCheck(result.get("date"));
                        Log.d("", "1 wowza2 " + nowUPT_CARTIME);
                    }
                }
            } catch (NullPointerException e) {
                // e.printStackTrace();
                Log.e("", "Error occured ActionList onPostExecute");
            } catch (XmlPullParserException e) {
                // e.printStackTrace();
                Log.e("", "Error occured ActionList onPostExecute");
            } finally {
                Log.e("TowMain", "Finally");
                if (ONECLICK_TOWRCEPTINFO_SELECT.equals(primitive)) {
                    if (proDialog != null) {
                        proDialog.dismiss();
                    }
                    ProgressFlag = false;
                }
            }

        }

    }

    String curRpt_id = "";
    String pastRpt_id = "";
    int isPlayPosition = -1;


    boolean playFlag = true;
    Patrol ppastItem = new Patrol();
    Patrol pastItem = new Patrol();

    //mp3플레이 여부.및 listview 재설정 여부
    public void isPlay() {

        if (displayFlag == true) {
            displayGallery();
            pastitemList = itemList;
            displayFlag = false;
        }

        Log.d("", "이것만 보면 된다 !! = " + "" + beforeUPT_CARTIME + "====" + nowUPT_CARTIME);
        if (!beforeUPT_CARTIME.equals(nowUPT_CARTIME) || "".equals(Common.nullCheck(nowUPT_CARTIME))) {//update_cartime 이 동일하지 않을 경우. 새로고침+음성
            ppastItem = pastItem;
            pastitemList = itemList;
            displayGallery();
            beforeUPT_CARTIME = nowUPT_CARTIME;
            if ("N".equals(pastItem.getEnd_yn())) {
                mp3Start(pastItem.getReg_type(), pastItem);
            }
        }
        if (!ppastItem.getRpt_id().equals(pastItem.getRpt_id())) {
            ppastItem = pastItem;
            pastitemList = itemList;
            displayGallery();
            beforeUPT_CARTIME = nowUPT_CARTIME;
            if ("N".equals(pastItem.getEnd_yn())) {
                mp3Start(pastItem.getReg_type(), pastItem);
            }
        }

        if (null != pastitemList && null != itemList) {
            Log.d("", "refreshCheck  out=  " + pastitemList.size() + ":" + itemList.size());
            if (pastitemList.size() != itemList.size()) {
                Log.d("", "refreshCheck in =  " + pastitemList.size() + ":" + itemList.size());
                displayGallery();
                pastitemList = itemList;
            }
        } else {
            pastitemList = itemList;
            displayGallery();
        }

        ppastItem = pastItem;
    }

    MediaPlayer player;

    //	TimerTask ttm;
    public void mp3Start(String code, Patrol item) {

//		Log.d("","LIST RPT_ID 비교  pastRpt_id : curRpt_id ["+pastRpt_id +"  |  "+curRpt_id +"]");
        //이전 rpt_id 첫번째 값과 현재 rpt_id 첫번째 값을 비교하여 다를 경우 음성 재생
//		if(pastRpt_id.equals(curRpt_id)){
//			playFlag = false;
//		}else{
//			playFlag = true;
//		}
//		pastRpt_id = curRpt_id;

        Log.d("", "mp3Start code = " + code);
        String name = "etc.mp3";

        if ("0001".equals(code)) {//기타접보
            name = "etc.mp3";
        } else if ("0002".equals(code)) {//차단작업접보
            name = "block.mp3";
        } else if ("0003".equals(code)) {//잡물접보
            name = "sundries.mp3";
        } else if ("0004".equals(code)) {//고장차량
            name = "car_broke.mp3";
        } else if ("0005".equals(code)) {//사고발생
            name = "accident.mp3";
        } else if ("0006".equals(code)) {//지정체
            name = "tieup.mp3";
        } else if ("0007".equals(code)) {//긴급견인
            name = "tow.mp3";
        } else if ("0008".equals(code)) {//동물
            name = "animal.mp3";
        } else if ("0009".equals(code)) {//노면(시설물)파손
            name = "facility.mp3";
        } else if ("0010".equals(code)) {//불량차량
            name = "fault_car.mp3";
        } else if ("0011".equals(code)) {//도로진입제한
            name = "entry.mp3";
        } else if ("0012".equals(code)) {//재난발생
            name = "disaster.mp3";
        } else if ("0013".equals(code)) {//터널화재
            name = "tunnelfire.mp3";
        } else if ("9999".equals(code)) {//상황실에서 상황 종료 했을경우
            Log.e("", TAG + "9999");
            SituationService.selectedRpt_id = "";
            SituationService.start_yn = "N";
            Common.setPrefString(TowMainActivity.this, "start_yn", "N");

            Calendar c = Calendar.getInstance();
            String date = "";
            try {
                Date nowDate = transFormat.parse(item.getEnd_time());
                c.setTime(nowDate);
                date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
            } catch (NullPointerException e) {
                Log.e("에러", "예외");
            } catch (ParseException ee) {
                Log.e("에러", "예외");
            }

            name = "acdntend.mp3";
            if (null != contextActivity) {
                if (contextActivity.isFinishing() == false) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(TowMainActivity.this);
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

//			try {
//				AssetFileDescriptor afd = getAssets().openFd(name);
//				player = new MediaPlayer();
//				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
//						afd.getLength());
//				afd.close();
//				player.prepare();
//				player.start();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
            return;
        } else if ("9998".equals(code)) {//긴급 견인 상황에서 다른 견인차가 견인완료 했을경우.
            Log.e("", TAG + "9998");
            SituationService.selectedRpt_id = "";
            SituationService.start_yn = "N";
            Common.setPrefString(TowMainActivity.this, "start_yn", "N");

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            String date = "";
            try {
                Date nowDate = transFormat.parse(item.getEnd_time());
                c.setTime(nowDate);
                date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
            } catch (NullPointerException e) {
                /*e.printStackTrace();*/
                Log.e("에러", "예외");
            } catch (ParseException ee) {
                Log.e("에러", "예외");
            }

            name = "towcomplete.mp3";
            if (null != contextActivity) {
                if (contextActivity.isFinishing() == false) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(TowMainActivity.this);
                    ad.setMessage("")
                            .setTitle("확인")
                            .setMessage("상황명: " + item.getInp_val() + "\n" + date + "분 견인이 완료 되었 습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                        }
                                    });
                    ad.show();
                }
            }
			
			/*try {
				AssetFileDescriptor afd = getAssets().openFd(name);
				player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				afd.close();
				player.prepare();
				player.start();

			} catch (Exception e) {
				e.printStackTrace();
			}*/

            return;
        }
		
		/*try {
			AssetFileDescriptor afd = getAssets().openFd(name);
			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			afd.close();
			player.prepare();
			player.start();
			
		} catch (Exception e) {
		    e.printStackTrace();
		}*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
        Parameters params = new Parameters(ONECLICK_FILE_SEND);
        switch (requestCode) {

            case Configuration.SELECT_JISA:
                if (resultCode == RESULT_OK) {
                    pastItem = new Patrol();
                    ppastItem = new Patrol();
                    nowUPT_CARTIME = "";
                    beforeUPT_CARTIME = "";
                    String rtnStr = db.fetchBBBsCodeSelected();
                    Parameters jisaParams = new Parameters(ONECLICK_TOWRCEPTINFO_SELECT);
                    // jisaParams.put("sms_group", SituationService.conf.User.getGroup_id());

                    if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                        jisaParams.put("bscode", rtnStr);
                    }
                    if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
                        if (Configuration.User.getBscode_list() != null && Configuration.User.getBscode_list().size() > 0) {
                            jisaParams.put("bscode", Configuration.User.getBscode_list().get(0));
                        }
                    }
                    String car_id = "";
                    for (int i = 0; i < Configuration.User.getCrdns_id_list().size(); i++) {
                        if (i == 0) {
                            car_id = Configuration.User.getCrdns_id_list().get(i);
                        } else {
                            car_id += "|" + Configuration.User.getCrdns_id_list().get(i);
                        }
                    }
                    jisaParams.put("car_id", car_id);

                    displayFlag = true;
                    new ActionList(ONECLICK_TOWRCEPTINFO_SELECT, jisaParams).execute("");
                }
                break;
            case Configuration.IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    //파일 전송.
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

                    executeJob(params, TowMainActivity.this);
                }
                break;
            case Configuration.FILE_TYPE_IMAGE:
                //--------------------------------------------------------------------------------------------
                // #region 앨범 > 사진 버튼 클릭 후 리턴

                if (resultCode == RESULT_OK) {
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

                    executeJob(params, TowMainActivity.this);
				
	    		/*mUriSet = data.getData();
	    		String fileAddName = "";
	    		if("".equals(SituationService.selectedRpt_id)){
	    			fileAddName = "_"+ns_name+"_"+banghyang+"_"+Common.nullCheck(currentIjung).replace(".", "|");
	    		}else{
	    			fileAddName = "_"+SituationService.selectedRpt_id;
	    		}
	    		
	    		Log.d("file", "Heap Size : "+Long.toString(Debug.getNativeHeapAllocatedSize()));
	    		
	    		mUriSet = data.getData();
	    		File tempfile = new File(getRealPathFromURI(mUriSet)); 
	    		double mbTemp = ReadSDCardMB()+getFileSizeMB(tempfile.length()); 
    			if(mbTemp < 10.0){
    				if(common == null){
    					common = new Common(getApplicationContext());
    				}
    				common.cameraPicRequestSelect(TowMainActivity.this, data);
    				
    			}
	    		
			    String[] arrayOfString = mUriSet.toString().split("/");
			    int iUriId = Integer.parseInt(arrayOfString[arrayOfString.length - 1]);

			    Log.d("file", "mUriSet : "+mUriSet.toString());
			    Log.d("file", "mUriSet : "+getRealPathFromURI(mUriSet));
			    
			    BitmapFactory.Options options = new BitmapFactory.Options();
			    options.inSampleSize = 6;
			    options.inPurgeable = true; 
				options.inDither = true;

				//파일 전송.
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
				
				executeJob(params, TowMainActivity.this);*/

                }//end if

                // #endregion
                //--------------------------------------------------------------------------------------------
                break;

            default:
                break;
        }
    }

    //onActivityResult 카메라관련 파일경로및 파일명 받아오기
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void showDialog(String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("").setTitle("확인").setMessage(message).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ad.show();
    }

    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {
    }

    AudioManager am = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("", "keycode " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP://볼륨 업
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                Log.d("", "volume1 = " + curVol);
                if (curVol < maxVol) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, curVol, AudioManager.STREAM_MUSIC);
                }
                Log.d("", "volume2 = " + curVol);*/
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN://볼륨 다운
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.d("", "volume1 = " + currVol);
                if (currVol > 0) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, currVol, AudioManager.STREAM_MUSIC);
                }
                Log.d("", "volume2 = " + currVol);*/
                break;
            case KeyEvent.KEYCODE_BACK://종료
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setMessage("").setTitle("종료").setMessage("* 앱 종료시 GPS데이타를 전송하지 않습니다 \n종료 하시겠습니까?.").setCancelable(false).setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(TowMainActivity.this, SituationService.class);
                        if ("Y".equals(Common.getPrefString(TowMainActivity.this, "start_yn"))) {
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
