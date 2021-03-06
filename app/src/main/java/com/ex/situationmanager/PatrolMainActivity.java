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

    //202007 ?????????
    public static String setSelectJubbo = "";
    public static String getSetSelectJubboStart = "";
    // setSelectJubbo??? ????????????????????? ????????? ?????? ???????????? ????????????????????? ?????? ?????? ??? ??????
    public static String setFixSelectJubbo = "";
    public static String setFixGetJubbo = "";

    private Uri mThumbUri;

    int selectedPosition = -1;

    // *********************************Slide Menu
    // ************************************
    // ???????????? ????????? ??????
    String[] navItems = {"??????", "????????????", "?????????", "?????????", "?????????"};

    //String[] navItems = { "??????","?????????", "?????????", "?????????" };
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

    //???????????????
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // ????????????
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // ????????????
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

//?????? ?????? ?????????
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
            Log.e("??????","????????????");
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("??????","????????????");
        }


        //???????????? ?????? ?????? ?????? ????????? ?????? ?????????
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
            Log.e("??????", "??????");
        } catch (IndexOutOfBoundsException id) {
            Log.e("??????", "??????");
        } catch (JSONException jsonException) {
            Log.e("??????", "??????");
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
        ExpandableListAdapter.Item group4 = new ExpandableListAdapter().new Item(navItems[3]);//??????
        group4.child.addAll(sunchalJsonList);
        dataList.add(group4);
        ExpandableListAdapter.Item group5 = new ExpandableListAdapter().new Item(navItems[4]);//??????
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ?????? ?????? ??????

        TowMainActivity.stopTimer_list();

        //????????? ??????
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
            Log.i("?????? ??????", "??????" + i + ",????????????:" + Configuration.User.getBsname_list().get(i));
        }

        params.put("user_type", Configuration.User.getUser_type());
        params.put("tel_no", Configuration.User.getHp_no());
        Log.println(Log.ASSERT, TAG, "PatrolMainActivity????????? new Action ??????:");
        Log.println(Log.ASSERT, TAG, Configuration.User.getUser_type());
        Log.i("940413","1");
        new Action(ONECLICK_GETPATROLTELNO_SELECT, params).execute();

        btnPatJochiReg.setVisibility(View.INVISIBLE);

//		detailContent.setTextSize(detailContent.getTextSize()*1.2f);
//		detailContent.setTextSize(detailContent.getTextSize()/1.0f);
//		detailContent.setTextSize( getResources().getDisplayMetrics().densityDpi);
//		detailContent.setTextScaleX(1.3f);
//		patrolRegist.performClick();


        //????????? CCTV ??????
        cctv_btn = (TextView) findViewById(R.id.cctv_btn);
        cctv_btn.setOnClickListener(this);

        if (!chkGpsService(this)) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(PatrolMainActivity.this);
            gsDialog.setTitle("????????? ????????????");
            gsDialog.setMessage("GPS??? ???????????? ????????????.\n?????? ???????????????????");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS?????? ???????????? ??????
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

        //?????? ?????????


    }//onCre

    //202007_??????????????? ???????????????????????? ??????????????????????????? ?????????


    public void sortSpinnerAdapter() {
        List<String> list = new ArrayList<String>();// ??????
        list.add("??????");
        list.add("????????????");
        list.add("?????????");

        // ?????? spinner
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
            //????????? ??????


            switch (position) {
                case 1:


                    break;
                // ?????????
                case 2:
                    // for (int i = 0; i < nList.size(); i++) {
                    //    Log.i("??????", nList.get(i).toString());
                    // }
                    //    defaultList.addAll(1,nList);
                    //lvNavList.
                    break;
                // ?????????
                case 3:
                    Intent i_tow = new Intent(PatrolMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                // ?????????
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
                // ?????????
                case 2:
                    Intent i_patrol = new Intent(PatrolMainActivity.this, PatrolMainActivity.class);
                    startActivity(i_patrol);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;
                // ?????????
                case 3:
                    Intent i_tow = new Intent(PatrolMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                // ?????????
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
            Log.println(Log.ASSERT, TAG, "?????????");
            progressDialog = ProgressDialog.show(PatrolMainActivity.this, "", "?????????...", true);

        }

        contextActivity = PatrolMainActivity.this;

        //202007
        //??????
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
        Log.i("onResum",setFixSelectJubbo.toString()+"???????????????");



        if ("Y".equals(SituationService.patrolOKFlag)) {
           patrolOK.setChecked(true);
        } else {
            patrolOK.setChecked(false);
        }

        Log.i("getSetSelect",getSetSelectJubboStart);

        //202007 - ????????? ?????? ??? ???????????? ????????? ????????? ??????????????? ?????????????????? ????????? ????????? ????????? ????????????
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


            //?????????
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
                    //?????? ?????? ?????? DB ??????.(?????????)
                    db.insertRptId(item.getRpt_id(), item.getReg_date());

                    // ???????????? ?????? ??????.
                    // ?????????
                    //202007
                    //if (getSetSelectJubboStart == "Y") {

                    // if (item.getRpt_id() != setSelectJubbo) {
                    //?????????
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
//                    Log.i("PM715", "??????????????????");
//                    Log.i("PM715 ??????????????????",setFixSelectJubbo);
//                    Log.i("PM715 ????????????", sortItemList.get(position).getRpt_id());
                    //202007_
                    if("Y".equals(Common.getPrefString(PatrolMainActivity.this,"getSetSelectJubboStart"))) {
                       //??????
                        if (setFixSelectJubbo != sortItemList.get(position).getRpt_id()) {
                            Log.i("PM713", "??????????????????");
                            Log.i("PM713 ??????????????????", setFixSelectJubbo);
                            Log.i("PM173 ????????????", sortItemList.get(position).getRpt_id());
                            patrolStartIng.setVisibility(View.GONE);
                            patrolStart.setVisibility(View.VISIBLE);

                             } else if (setFixSelectJubbo == sortItemList.get(position).getRpt_id()) {

                            Log.i("PM713", "??????????????????");
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
            Log.i("??????", "getView");
            View view = convertView;
            if (isInit) {
                Log.d("", "getview position " + position);
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.slide_row, null);
                }
                //????????? ??????

                llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
                if (position == 0) {
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                    llSlide.setTag("??????");
                } else if (position == 1) {
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("??????");
                } else if (position == 2) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_01);
                    llSlide.setTag("??????");
                } else if (position == 3) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_02);
                    llSlide.setTag("??????");
                } else if (position == 4) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_03);
                    llSlide.setTag("??????");
                }


            } else {
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.slide_row, null);
                }

         /*       if(parent.getChildAt(position).getTag().equals("??????")){
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                    llSlide.setTag("??????");
                }else if(parent.getChildAt(position).getTag().equals("??????")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("??????");
                }else if(parent.getChildAt(position).getTag().equals("??????")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("??????");
                }else if(parent.getChildAt(position).getTag().equals("??????")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("??????");
                }else if(parent.getChildAt(position).getTag().equals("??????")){
                    llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                    llSlide.setTag("??????");
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
            Log.i("?????? ??????", i + "getGroupView");
            //View view = convertView;
            if (view == null) {
                view = inflater.inflate(groupLayout, viewGroup, false);
            }
            //????????? ??????

            LinearLayout llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            LinearLayout tllSlide = (LinearLayout) view.findViewById(R.id.llSlide);
            if (i == 0) {

                tllSlide.setBackgroundResource(R.drawable.tit_trans);
                tllSlide.setTag("??????");

           /*     tllSlide.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlDrawer.closeDrawers();
                    }
                });*/
            } else if (i == 1) {

                llSlide.setBackgroundResource(R.drawable.oneclick_slide);
                llSlide.setTag("??????");
            } else if (i == 2) {
                llSlide.setBackgroundResource(R.drawable.item_slide_01);
                llSlide.setTag("??????");
            } else if (i == 3) {
                llSlide.setBackgroundResource(R.drawable.item_slide_02);
                llSlide.setTag("??????");
            } else if (i == 4) {
                llSlide.setBackgroundResource(R.drawable.item_slide_03);
                llSlide.setTag("??????");
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
                Log.e("??????", "????????? ?????????");
            } catch (NullPointerException ee) {
                Log.e("??????", "???????????? ????????? ?????????");
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
            Log.i("i??????", "i:" + i + "???:" + l);
            if (i == 0) {
                dlDrawer.closeDrawers();
                return true;
            } else if (i == 1 && dataList.get(0).child.size() == 1) {//???????????? ?????? ???????????? 1??? ?????? ????????? ??????
                VIEW_TAG = VIEW_INNEREMPLOYEE;
                Intent i_employee = new Intent(PatrolMainActivity.this, InnerEmployActivity.class);
                startActivity(i_employee);
                finish();
                stopGPS();
                stopTimer(PatrolMainActivity.this);
                return true;
            } else if (i == 2 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
                Intent i_patrol = new Intent(PatrolMainActivity.this, PatrolMainActivity.class);
                startActivity(i_patrol);
                finish();
                stopGPS();
                stopTimer(PatrolMainActivity.this);
                return true;
            } else if (i == 3 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
                Intent i_tow = new Intent(PatrolMainActivity.this,
                        TowMainActivity.class);
                startActivity(i_tow);
                finish();
                stopGPS();
                stopTimer(PatrolMainActivity.this);
                return true;
            } else if (i == 4 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
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
                // ?????????
                case 2:
                    Intent i_patrol = new Intent(PatrolMainActivity.this, PatrolMainActivity.class);
                    startActivity(i_patrol);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;
                // ?????????
                case 3:
                    Intent i_tow = new Intent(PatrolMainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(PatrolMainActivity.this);
                    break;

                // ?????????
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
                        Log.e("??????", "??????");


                    } catch (NullPointerException ne) {
                        Log.e("??????", "??????");
                    }
                    break;
                // ?????????
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
                        Log.e("??????", "??????");
                    } catch (NullPointerException ne) {
                        Log.e("??????", "??????");
                    }
                    break;
                // ?????????
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
                        Log.e("?????? ", "??????");
                    }

                    break;

                // ?????????
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
                        Log.e("?????? ", "??????");
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


    //??????????????? adapter
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
                    Toast.makeText(getApplicationContext(), "?????? ????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void setItemImage(int position) {
            Patrol item = this.getItem(position); // ArrayAdapter
            if ("N".equals(item.getEnd_yn())) {
                sendConfirm();//???????????? ????????? ??????????????? ?????? ??????.
            }

            SituationService.selectedRpt_id = item.getRpt_id();
            Log.d("", "itemclick = " + SituationService.selectedRpt_id);

            SituationService.rpt_bscode = item.getBscode();
            SituationService.rpt_reg_type = item.getReg_type();

            detailNo.setText(item.getLocal_way());
            if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                detailTitle.setText("?????????: " + TRAINSTR + item.getInp_val());
            } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                detailTitle.setText("?????????: " + PRACTICESTR + item.getInp_val());
            } else {
                detailTitle.setText("?????????: " + item.getInp_val());
            }
            detailNo.setText("?????? : " + (sortItemList.size() - position));
            if (item.getEnd_yn().equals("Y")) {
                detailState.setText("??????  : ????????????");
            } else {
                detailState.setText("??????  : ?????????");
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
                detailTime.setText("?????? : " + year
                        + "-" + month
                        + "-" + day
                        + " " + hour
                        + ":" + minute
                );
//				date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) +":"+String.format("%02d", c.get(Calendar.MINUTE));
            } catch (NullPointerException e) {
                detailTime.setText("?????? :");
            } catch (ParseException ee) {
                detailTime.setText("?????? :");
            }

            detailJeopbo.setText("?????? : " + item.getReg_info());
            detailType.setText("?????? : " + getReg_TypeName(item.getReg_type()));

            String detailText = "";

            detailText = "[" + Common.nullCheck(item.getDirection_name()).replace("||", "]") + "?????? " + item.getStart_km() + "km " + getLaneName(item.getLane_num()) + "\n" + item.getReg_data();

            if (!"".equals(Common.nullCheck(item.getPsn_tel_no()))) {
                detailText += "\n????????? : " + item.getPsn_tel_no();
            }
            if (!"".equals(Common.nullCheck(item.getReg_tel_no()))) {
                detailText += "\n????????? : " + item.getReg_tel_no();
            }
            Log.d("", "detailContent settext now " + detailText);
            if (TRAIN.equals(Common.nullCheck(item.getModegubun()))) {
                detailContent.setText(TRAINSTR + detailText);
            } else if (PRACTICE.equals(Common.nullCheck(item.getModegubun()))) {
                detailContent.setText(PRACTICESTR + detailText);
            } else {
                detailContent.setText(detailText);
            }

            // ?????? ??????????????????
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

            // ?????? ??????????????????
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
                                Log.e("??????", "??????");
                            }
                        }
                    }
                });
            }
            return view;
        }
    }

    // ?????????
    // ???????????? ?????? ??????
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
        // ???????????? 0005
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
            return "??????";
        if ("0002".equals(code))
            return "????????????";
        if ("0003".equals(code))
            return "??????";
        if ("0004".equals(code))
            return "????????????";
        if ("0005".equals(code))
            return "????????????";
        if ("0006".equals(code))
            return "?????????";
        if ("0007".equals(code))
            return "????????????";
        if ("0008".equals(code))
            return "??????";
        if ("0009".equals(code))
            return "??????(?????????)??????";
        if ("0010".equals(code))
            return "????????????";
        if ("0011".equals(code))
            return "??????????????????";
        if ("0012".equals(code))
            return "????????????";
        if ("0013".equals(code))
            return "????????????";

        return "";
    }

    public String getLaneName(String code) {
        if ("0001".equals(code))
            return "1??????";
        if ("0002".equals(code))
            return "2??????";
        if ("0003".equals(code))
            return "3??????";
        if ("0004".equals(code))
            return "4??????";
        if ("0005".equals(code))
            return "5??????";
        return "";
    }

    public String getResultName(String code) {
        if ("0001".equals(code))
            return "??????";
        if ("0002".equals(code))
            return "???????????????";
        if ("0003".equals(code))
            return "??????";
        if ("0004".equals(code))
            return "??????????????????";
        if ("0005".equals(code))
            return "??????";
        return "";
    }

    // ???????????? ?????????.
    public void detailClear() {
        rpt_bscode = "";
        rpt_reg_type = "";
        rpt_bhPkCode = "";

        detailNo.setText("");
        // ???????????? ????????????
        detailTitle.setText("");
        detailNo.setText("?????? : ");
        detailState.setText("??????  : ");
        detailTime.setText("?????? : ");
        detailJeopbo.setText("?????? : ");
        detailType.setText("?????? : ");
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


    // ??????, ????????? ?????? visible ?????? ( ???????????? ?????? ??????)
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

    // ????????????????????? ????????? visible ??????
    public synchronized void setPhoneBtnVisible(boolean phone) {
        llbottomBtnPhone.setVisibility(View.VISIBLE);
        llbottomBtnDefault.setVisibility(View.VISIBLE);
        if (phone == true) {
            llbottomBtnDefault.setVisibility(View.GONE);
        } else {
            llbottomBtnPhone.setVisibility(View.GONE);
        }

    }

    //202007 ??????
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
                        Toast.makeText(PatrolMainActivity.this, "?????? ????????? ????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
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
                        Log.e("??????", "??????");
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
                                .setTitle("??????")
                                .setMessage("* ????????? ?????????. \n?????? ???????????????????")
                                .setCancelable(false)
                                .setPositiveButton("??????",
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
                        ading.setNegativeButton("??????",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                        ading.show();

                    } else {
                        AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                        ad.setMessage("").setTitle("??????").setMessage("???????????? ????????? ????????????.").setCancelable(false).setPositiveButton("??????",
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
                    //Toast.makeText(this,"??????",Toast.LENGTH_SHORT).show();
                    if ("".equals(Common.nullCheck(SituationService.selectedRpt_id))) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                        ad.setMessage("")
                                .setTitle("??????")
                                .setMessage("* ?????? ?????? ????????? ???????????? ??? ????????????.")
                                .setCancelable(false)
                                .setPositiveButton("??????",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "???????????? ?????????????????????.", Toast.LENGTH_LONG).show();
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
                        ad.setMessage("").setTitle("??????").setMessage("* ???????????? ?????? ????????? ????????????.").setCancelable(false).setPositiveButton("??????",
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

                        //?????????_202007

                        //setSelectJubbo = sortItemList.get(selectedPosition).getRpt_id();
                        //getSetSelectJubboStart = "Y";

                        if (endyn.equals("Y")) {
                            AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                            ad.setMessage("")
                                    .setTitle("??????")
                                    .setMessage("* ??????????????? ????????? ????????? ??? ????????????.")
                                    .setCancelable(false)
                                    .setPositiveButton("??????",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                            ad.show();
                        } else {
                            AlertDialog.Builder ad = new AlertDialog.Builder(
                                    PatrolMainActivity.this);
                            ad.setNegativeButton("??????",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                        }
                                    });
                            ad.setMessage("")
                                    .setTitle("??????")
                                    .setMessage(detailTitle.getText() + "\n* ?????? ???????????????????.")
                                    .setCancelable(false)
                                    .setPositiveButton("??????",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Log.i("PM1843", "???????????? ??????");
                                                    SituationService.patrolOKFlag = "Y";
                                                    //202007
                                                    setPatrolOkSet();
                                                    SituationService.actionStart(PatrolMainActivity.this);
                                                    setStartBtn();
                                                    setFixItem();
                                                    Common.setPrefString(PatrolMainActivity.this, "getSetSelectJubboStart", "Y");
                                                    //??????DB ??????
                                                    Intent directionIntent = new Intent(PatrolMainActivity.this, DialogDirection.class);
                                                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, directionIntent, PendingIntent.FLAG_ONE_SHOT);
                                                    try {
                                                        pi.send();
                                                    } catch (PendingIntent.CanceledException ee) {
                                                        Log.e("??????", "??????");
                                                    } catch (Exception ee) {
                                                        Log.e("??????", "??????");
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
                //??????
                 if (setSelectJubbo == SituationService.selectedRpt_id || setFixSelectJubbo == SituationService.selectedRpt_id) {
//                    if (getSetSelectJubboStart == "Y") {
                if("Y".equals(Common.getPrefString(PatrolMainActivity.this,"getSetSelectJubboStart"))){
                        AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                        ad.setNegativeButton("??????",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                });
                        ad.setMessage("")
                                .setTitle("??????")
                                .setMessage(detailTitle.getText() + "\n* ?????? ?????? ???????????????????.")
                                .setCancelable(false)
                                .setPositiveButton("??????",
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
                    ad.setMessage("").setTitle("??????").setMessage("???????????? ????????? ????????????.").setCancelable(false).setPositiveButton("??????",
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
                    //??????
                    //if ( setSelectJubbo.equals(Common.getPrefString(PatrolMainActivity.this,"setFixSelectJubbo"))){
                    if(setSelectJubbo == SituationService.selectedRpt_id || setFixSelectJubbo == SituationService.selectedRpt_id) {
                        Intent i = new Intent(PatrolMainActivity.this, PatrolRegActivity.class);
                        i.putExtra("selectedRpt_id", SituationService.selectedRpt_id);
                        i.putExtra("customPhoneInfo", customPhoneInfo);
                        startActivityForResult(i, Configuration.PATROL_REG_RETURN);
                    }
                    break;

                case R.id.btnUserInfo:
                    //Toast.makeText(this,"????????? ??????",Toast.LENGTH_SHORT).show();
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

                    // ????????? Gallery
                    ImageView buttonGallery = (ImageView) dialogGallery.findViewById(R.id.goCamera);
                    buttonGallery.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            runCamera();
                            dialogGallery.dismiss();
                        }
                    });

                    // ?????? Gallery
                    ImageView buttonGallery1 = (ImageView) dialogGallery.findViewById(R.id.goAlbum);
                    buttonGallery1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(PatrolMainActivity.this, AndroidCustomGalleryActivity.class);
                            startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

//					Intent chooseIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//					chooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//					startActivityForResult(chooseIntent, 2);
                            //MyGalleryPicker ??????


                            //GalleryListMain ??????
//					Intent i = new Intent(PatrolMainActivity.this, GalleryListMain.class);
//					startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

//					AlertDialog.Builder patClear = new AlertDialog.Builder(PatrolMainActivity.this);
//					patClear.setMessage("")
//							.setTitle("??????")
//							.setMessage("* ?????????.")
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


                            //?????? 1??? ????????????
//					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//					i.setType("image/*");
//					startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);

                            dialogGallery.dismiss();
                        }
                    });
                    // ???????????? ??????.
                    ImageView buttonGallery2 = (ImageView) dialogGallery.findViewById(R.id.goPreview);
                    buttonGallery2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setType("image/*");
                            startActivity(i);

                        }
                    });
                    // ?????? ??????
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
                        //??????
                  if (getSetSelectJubboStart == "Y" || setFixSelectJubbo == SituationService.selectedRpt_id) {
                      AlertDialog.Builder patClear = new AlertDialog.Builder(PatrolMainActivity.this);
                        patClear.setMessage("")
                                .setTitle("??????")
                                .setMessage("* ????????? ?????? ?????? ??? ????????????.")
                                .setCancelable(false)
                                .setPositiveButton("??????",
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
                        Toast.makeText(this, "????????? ???????????? ?????????.", Toast.LENGTH_LONG).show();
                    } else {
                        //	Toast.makeText(this,sortItemList.get(selectedPosition).getEnd_km()+","+sortItemList.get(selectedPosition).getStart_km()+","+sortItemList.get(selectedPosition).getRoadNo(),Toast.LENGTH_LONG).show();

                        Patrol param = new Patrol();
                        param.setEnd_km(sortItemList.get(selectedPosition).getEnd_km());
                        param.setStart_km(sortItemList.get(selectedPosition).getStart_km());
                        param.setRoadNo(sortItemList.get(selectedPosition).getRoadNo());
                        if (param.getStart_km() == null || param.getEnd_km() == null || param.getStart_km() == "" || param.getEnd_km() == "") {
                            Toast.makeText(this, "?????? ????????? CCTV????????? ?????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show();
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
                Log.println(Log.ASSERT, TAG, "PatrolMainActivity?????? ????????? result");
                Log.println(Log.ASSERT, TAG, "" + result.toString());
            }
        } catch (NullPointerException e1) {
            Log.e("??????","??????");
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
            Log.e("??????","??????");
        }
    }


    /**
     * ??????,?????? ?????? ?????? ????????? 5????????? ??????
     */
    static Timer listTimer = new Timer();
    static Handler listHandler = new Handler();

    //?????? ?????? ???????????? ??????.
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

                    Log.println(Log.ASSERT, TAG, "????????? ListSize");
                    Log.d(TAG, TAG + " starttimer actionlist regtype = " + selectedreg_type);
                    setRegVisible(selectedreg_type);
                    if (VIEW_PATROL.equals(VIEW_TAG) /*|| Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)*/) {
                        Log.println(Log.ASSERT, TAG, "if ??????");
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
        listTimer.schedule(timertask, 1000, 15000);// 100MS ?????????, 1??? ???????????? ??????.
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

    // ???????????? ?????? ??????
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
        // #region ???????????? ?????? ??????
        // ?????? ?????? Progressbar
        ProgressDialog progressDialog;

        String primitive = "";
        Parameters params = null;

        @Override
        protected void onPreExecute() {
            Log.println(Log.ASSERT, TAG, "ActionList");
            if (ProgressFlag == true && ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
                progressDialog = ProgressDialog.show(PatrolMainActivity.this, "", "?????????...", true);
                ProgressFlag = false;
            }
            super.onPreExecute();
        }



        // primitive ??? ?????? URL??? ???????????????.
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
                Log.println(Log.ASSERT, TAG, "????????? ListSize");

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

                    //202007_?????????
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
                Log.i("???11??????","PatrolMainActivity -- onPostExecute");
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
                        //????????? ???????????? ??????
                        if (itemList != null) {
                            Log.println(Log.ASSERT, TAG, "?????????");
                            for (int i = 0; i < itemList.size(); i++) {
                                Log.println(Log.ASSERT, TAG, i + 1 + "?????? ?????? ?????? ");
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
                Log.e("", "Error occured ActionList onPostExecute:??????");
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

    //mp3????????? ??????.??? listview ????????? ??????
    public void isPlay() {
//		Log.d("displayGallery start");

//		if(displayFlag == true){
//			Log.d("",TAG+ " isplay type 1");
//			displayGallery();
//			displayFlag = false;
//			pastitemList = itemList;
//		}
//		Log.d("","isplay type 2 ????????? ?????? ?????? !! = " +""+beforeUPT_CARTIME+"===="+ nowUPT_CARTIME);
//		if(!beforeUPT_CARTIME.equals(nowUPT_CARTIME) || "".equals(Common.nullCheck(nowUPT_CARTIME))){//update_cartime ??? ???????????? ?????? ??????. ????????????+??????
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
		if(ppastItem.getRpt_id().equals("")){//?????? Patrol ????????? ?????? ?????? ???????????? + ??????
			Log.d("","isPlay uhuh = type 1"+pastItem.getEnd_yn()+":"+pastItem.getReg_type());
			ppastItem = pastItem;
			displayGallery();
			beforeUPT_CARTIME = nowUPT_CARTIME;
			
			if("N".equals(pastItem.getEnd_yn())){
				Log.d("","isPlay uhuh = type 1-1 IN ############ ");
				mp3Start(pastItem.getReg_type(), pastItem);
			}
		}else{//?????? Patrol ????????? ?????? ??????.
			if(ppastItem.getRpt_id().equals(pastItem.getRpt_id())){//?????? rpt_id??? ?????? rpt_id ?????? ????????? ??????
				Log.d("","isPlay uhuh = type 2-1 = " + beforeUPT_CARTIME+":"+nowUPT_CARTIME);
				ppastItem = pastItem;
				if(!beforeUPT_CARTIME.equals(nowUPT_CARTIME)){
					beforeUPT_CARTIME=nowUPT_CARTIME;
					displayGallery();
				}else {//?????? ?????? ??????, ???????????? ?????????(DB??? rpt_id ) ?????? ????????? ?????? ?????? ??????.
					Log.d("","isPlay uhuh = type 2-2" +pastItem.getRpt_id());
					if( db.selectRptId(pastItem.getRpt_id()) == false ){//????????? ???????????? ?????? ?????? ?????? ??????
						Log.d("","isPlay uhuh = type 2-2 IN" );
						ppastItem = pastItem;
						beforeUPT_CARTIME=nowUPT_CARTIME;
						if("N".equals(pastItem.getEnd_yn())){
							mp3Start(pastItem.getReg_type(), pastItem);
						}
					}
				}
				if(!ppastItem.getUpdate_cartime().equals(pastItem.getUpdate_cartime())){//update_cartime ??? ???????????? ?????? ??????. ????????????+??????
					ppastItem = pastItem;
					displayGallery();
					beforeUPT_CARTIME = nowUPT_CARTIME;
					if("N".equals(pastItem.getEnd_yn())){
						mp3Start(pastItem.getReg_type(), pastItem);
					}
				}
				
			}else{//?????? rpt_id??? ?????? rpt_id ?????? ???????????? ?????? ??????
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
        //Toast.makeText(this,"???????????? ??????",Toast.LENGTH_LONG).show();

        if ("0001".equals(code)) {// ????????????
            name = "etc.mp3";
        } else if ("0002".equals(code)) {// ??????????????????
            name = "block.mp3";
        } else if ("0003".equals(code)) {// ????????????
            name = "sundries.mp3";

        } else if ("0004".equals(code)) {// ????????????
            name = "car_broke.mp3";
        } else if ("0005".equals(code)) {// ????????????
            name = "accident.mp3";
        } else if ("0006".equals(code)) {// ?????????
            name = "tieup.mp3";
        } else if ("0007".equals(code)) {// ????????????
            name = "tow.mp3";
        } else if ("0008".equals(code)) {// ??????
            name = "animal.mp3";
        } else if ("0009".equals(code)) {// ??????(?????????)??????
            name = "facility.mp3";
        } else if ("0010".equals(code)) {// ????????????
            name = "fault_car.mp3";
        } else if ("0011".equals(code)) {// ??????????????????
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
                Log.e("??????","??????");
            }

            name = "acdntend.mp3";
            if (null != contextActivity) {
                if (contextActivity.isFinishing() == false) {

                    AlertDialog.Builder ad = new AlertDialog.Builder(PatrolMainActivity.this);
                    ad.setMessage("")
                            .setTitle("??????")
                            .setMessage("?????????: " + item.getInp_val() + "\n" + date + "??? ????????? ?????? ???????????????.")
                            .setCancelable(false)
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
            //2020207 ?????????  _ ???????????? ??????????????? ??? ?????? ????????? ????????? ???.
            case Configuration.PATROL_REG_RETURN:
                Log.d("", "onActivityResult PATROL_REG_RETURN");
                //if ("Y".equals(Common.getPrefString(PatrolMainActivity.this, "start_yn"))) {
                //202007
                if(getSetSelectJubboStart == "Y"){
                    // ?????????
                    // setStartBtn();
                } else {
                    setStartBtn();
                }
                break;

            case Configuration.IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    // ?????? ??????.
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
                // #region ?????? > ?????? ?????? ?????? ??? ??????
                if (resultCode == RESULT_OK) {


                    // ?????? ??????.
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

    // onActivityResult ??????????????? ??????????????? ????????? ????????????
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
            case KeyEvent.KEYCODE_VOLUME_UP:// ?????? ???
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                if (curVol < maxVol) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, curVol, AudioManager.STREAM_MUSIC);
                }*/
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:// ?????? ??????
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currVol > 0) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, currVol, AudioManager.STREAM_MUSIC);
                }*/
                break;
            case KeyEvent.KEYCODE_BACK://??????
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setMessage("").setTitle("??????").setMessage("* ??? ????????? GPS???????????? ???????????? ????????????. \n?????? ???????????????????").setCancelable(false).setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                ad.setNegativeButton("??????",
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
