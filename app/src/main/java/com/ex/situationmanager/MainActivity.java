package com.ex.situationmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.ex.situationmanager.R;
import com.ex.situationmanager.BaseActivity.Action;
import com.ex.situationmanager.BaseActivity.DoComplecatedJob;
import com.ex.situationmanager.encrypt.SeedCipher;
import com.ex.situationmanager.multiphoto.MyGalleryPicker;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.DBAdapter;
import com.ex.situationmanager.util.Image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnClickListener {

    private String TAG = "MainActivity";
    Common common = null;
    DBAdapter db = new DBAdapter();
    TextView etLocation;
    ImageView goCamera, goAlbum, goPreview;
    ImageView imLocation;

    EditText delayEdit;

    private List<Image> tImageList;
    ImageView capture1, capture2, capture3;
    ImageView btnUserInfo;

    ImageView btnRoadKill, btnLoadFault, btnRoadEtc, btnFacility, btnAccident, btnCarBroken;

    ImageView btnAgree;

    Dialog dialogGallery;

    ImageView btnJaeboCall;

    //1-?????? ??????, 2-????????????
    private String fileType = "1";

    private final static int LOC_CHANGE = 5;
    private final static int IMAGE_CAPTURE = 1;
    private final static int VIDEO_CAPTURE = 2;
    private final static int CONFIRM = 3;
    private final static int GPS_INTENT = 6;
    private final static int FILE_TYPE_IMAGE = 8;
    private final static int FILE_TYPE_VIDEO = 9;
    private Uri mUriSet;
    private Uri mPhotoUri;

    private String regType = "";

    CheckBox chkAgree;

    boolean sendGpsFlag = false;

    static boolean saveMainValueFlag = true;
    String main_nscode = "";
    String main_nsname = "";
    String main_bhcode = "";
    String main_banghyang = "";
    String main_ijung = "";


    //2015-09-15 gps?????? ?????? ?????? ???????????? test
    //???????????? ????????? path
//	http://180.148.182.64:8080/emp_sf/service.pe?primitive=COMMON_TTMCODE_UPLOADPROC
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    //*********************************Slide Menu  ************************************
    //????????????
    String[] navItems = {"??????", "????????????", "?????????", "?????????", "?????????"};

    //String[] navItems = {"??????", "?????????", "?????????", "?????????"};
    ExpandableListView lvNavList;
    FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    //*********************************Slide Menu  ************************************

    SeedCipher seed;
    ImageView menu;

    //????????????
    /*ViewGroup gongsaManagement;*/

    String userInfo;

    JSONArray jsonArray;
    List<String> innerEmployeeList;
    List<String> sunchalJsonList;

    SharedPreferences sh;
    String userJisaStr;
    List<String> userCarNameList_ = null;
    JSONArray userJisaList = null;
    JSONObject userJisaJsonObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextActivity = MainActivity.this;
        Log.d(TAG, TAG + " onCreate Call!!");
        super.onCreate(savedInstanceState);
        saveMainValueFlag = true;


        if (Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN) || Common.nullCheck(Configuration.User.getUser_type()).equals("")) {
            setContentView(R.layout.main_for_customer);
        } else {
            setContentView(R.layout.main);

            List<String> objects = new ArrayList<String>();
            for (int i = 0; i < navItems.length; i++) {
                objects.add(navItems[i]);
            }

            //?????? ?????? ?????????
            try{

                JSONObject savedUserInfoJob = new JSONObject();
                savedUserInfoJob.put("user_type",SituationService.conf.User.getUser_type());
                savedUserInfoJob.put("bscode",SituationService.conf.User.getBscode_list().get(0));
                savedUserInfoJob.put("bsname",SituationService.conf.User.getBsname_list().get(0));
                savedUserInfoJob.put("patcar_id",SituationService.conf.User.getPatcar_id());
                savedUserInfoJob.put("car_nm",SituationService.conf.User.getCar_nm());
                savedUserInfoJob.put("cur_page",TAG);

                SharedPreferences savedUserInfo = getApplication().getSharedPreferences("savedUserInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = savedUserInfo.edit();
                editor.putString("savedUserInfo",savedUserInfoJob.toString());
                editor.commit();
            }catch (JSONException e){
                Log.e("??????","????????????");
            }



            lvNavList = (ExpandableListView) findViewById(R.id.lv_activity_main_nav_list);
            sh = getSharedPreferences("userAllJisaList", MODE_PRIVATE);
            userInfo = sh.getString("userAllInfo", "");


            innerEmployeeList = new ArrayList<String>();
            sunchalJsonList = new ArrayList<String>();
            Log.i("-","-----------------------------");
            Log.i("userInfo", userInfo);
            Log.i("-","-----------------------------");
            try {
                jsonArray = new JSONArray(userInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ;
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
                Log.e("??????", "??????");
            } catch (IndexOutOfBoundsException id) {
                Log.e("??????", "??????");
                id.printStackTrace();
            } catch (JSONException jsonException) {
                Log.e("??????", "??????");
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
                ExpandableListAdapter.Item group4 = new ExpandableListAdapter().new Item(navItems[1]);//??????
                dataList.add(group4);
                ExpandableListAdapter.Item group5 = new ExpandableListAdapter().new Item(navItems[2]);//??????
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
                ExpandableListAdapter.Item group4 = new ExpandableListAdapter().new Item(navItems[3]);//??????
                group4.child.addAll(sunchalJsonList);
                dataList.add(group4);
                ExpandableListAdapter.Item group5 = new ExpandableListAdapter().new Item(navItems[4]);//??????
                group5.child.addAll(sunchalJsonList);
                dataList.add(group5);
            }
            //
            ExpandableListAdapter apapter1 = new ExpandableListAdapter(this, R.layout.slide_row, R.layout.group_row, dataList);
            lvNavList.setAdapter(apapter1);
            lvNavList.setOnGroupClickListener(apapter1);
            lvNavList.setOnChildClickListener(apapter1);


            //*********************************Slide Menu  ************************************

            flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);
            SlideAdapter adapter = new SlideAdapter(this, objects);
//			lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));




            /**/    //		lvNavList.setAdapter(adapter);
            lvNavList.setOnItemClickListener(new DrawerItemClickListener());
            dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
            dtToggle = new ActionBarDrawerToggle(MainActivity.this, dlDrawer, R.drawable.ic_launcher, R.string.app_name, R.string.desc_list_item_icon) {
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
            dlDrawer.closeDrawer(lvNavList);
            //*********************************Slide Menu  ************************************
        }
        seed = new SeedCipher();

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        etLocation = (TextView) findViewById(R.id.etLocation);
        goCamera = (ImageView) findViewById(R.id.goCamera);
        goAlbum = (ImageView) findViewById(R.id.goAlbum);
        goPreview = (ImageView) findViewById(R.id.goPreview);
        imLocation = (ImageView) findViewById(R.id.imLocation);
        btnUserInfo = (ImageView) findViewById(R.id.btnUserInfo);
        btnAgree = (ImageView) findViewById(R.id.btnAgree);
        btnJaeboCall = (ImageView) findViewById(R.id.btnJaeboCall);

        goCamera.setOnClickListener(this);
        goPreview.setOnClickListener(this);
        goAlbum.setOnClickListener(this);
        imLocation.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        btnAgree.setOnClickListener(this);
        btnJaeboCall.setOnClickListener(this);

        btnRoadKill = (ImageView) findViewById(R.id.btnRoadKill);
        btnLoadFault = (ImageView) findViewById(R.id.btnLoadFault);
        btnRoadEtc = (ImageView) findViewById(R.id.btnRoadEtc);
        btnFacility = (ImageView) findViewById(R.id.btnFacility);
        btnAccident = (ImageView) findViewById(R.id.btnAccident);
        btnCarBroken = (ImageView) findViewById(R.id.btnCarBroken);

        btnRoadKill.setOnClickListener(this);
        btnLoadFault.setOnClickListener(this);
        btnRoadEtc.setOnClickListener(this);
        btnFacility.setOnClickListener(this);
        btnAccident.setOnClickListener(this);
        btnCarBroken.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//?????? ?????? ??????


        shared = this.getSharedPreferences("com.ex.situationmanager", this.MODE_PRIVATE);
        editor = shared.edit();

        chkAgree = (CheckBox) findViewById(R.id.chkAgree);
        chkAgree.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPreference(isChecked);
            }
        });
        etLocation.setText("GPS ?????????...");

        if (!chkGpsService(this)) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(MainActivity.this);
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


            if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL) || Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)) {
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.slide_row, null);
                }
                llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
                //????????? ??????
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

			/*	if(position == 0){
					llSlide.setBackgroundResource(R.drawable.tit_trans);
				}else if(position == 1){
					llSlide.setBackgroundResource(R.drawable.item_slide_01);
				}else if(position == 2){
					llSlide.setBackgroundResource(R.drawable.item_slide_02);
				}else if(position == 3){
					llSlide.setBackgroundResource(R.drawable.item_slide_03);
				}*/
            } else if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.slide_row, null);
                }
                llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
                if (position == 0) {
                    llSlide.setBackgroundResource(R.drawable.tit_trans);
                } else if (position == 1) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_02);
                } else if (position == 2) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_03);
                }
            }
            return view;
        }
    }

    //*********************************Slide Menu  ************************************
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (null != dtToggle) {
            dtToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null != dtToggle) {
            dtToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("", "SlideMenu flContainer click btn position onOptionItemSelected");
        if (null != dtToggle) {
            if (dtToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

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
                    tllSlide.setTag("??????");
                } else if (i == 1) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_02);
                    llSlide.setTag("??????");
                } else if (i == 2) {
                    llSlide.setBackgroundResource(R.drawable.item_slide_03);
                    llSlide.setTag("??????");
                }

            } else {
			/*	llSlide = (LinearLayout) view.findViewById(R.id.llSlide);
				if(position == 0){
					llSlide.setBackgroundResource(R.drawable.tit_trans);
				}else if(position == 1){
					llSlide.setBackgroundResource(R.drawable.inner_item_slide_01);
				}else if(position == 2){
					llSlide.setBackgroundResource(R.drawable.item_slide_01);
				}else if(position == 3){
					llSlide.setBackgroundResource(R.drawable.item_slide_02);
				}else if(position == 4){
					llSlide.setBackgroundResource(R.drawable.item_slide_03);
				}*/
                if (i == 0) {

                    tllSlide.setBackgroundResource(R.drawable.tit_trans);
                    tllSlide.setTag("??????");
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


            }

    /*        if (i == 0) {

                tllSlide.setBackgroundResource(R.drawable.tit_trans);
                tllSlide.setTag("??????");
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
            }*/


            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

            Log.i("?????? ??????", i + "getChildView");


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
                Log.e("Error", "JSON Exception");
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
            if (i == 0) {
                dlDrawer.closeDrawers();
            } else if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
                switch (i) {
                    // ?????????
                    case 1:
                        Intent i_tow = new Intent(MainActivity.this, TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
//						stopGPS();
//						stopTimer(TowMainActivity.this);
                        break;

                    // ?????????
                    case 2:
                        Intent i_citizen = new Intent(MainActivity.this, MainActivity.class);
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
                if (i == 1 && dataList.get(0).child.size() == 1) {//???????????? ?????? ???????????? 1??? ?????? ????????? ??????
                    VIEW_TAG = VIEW_INNEREMPLOYEE;
                    Intent i_employee = new Intent(MainActivity.this, InnerEmployActivity.class);
                    startActivity(i_employee);
                    finish();
                    stopGPS();
                    stopTimer(MainActivity.this);
                    return true;
                } else if (i == 2 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
                    Intent i_patrol = new Intent(MainActivity.this, PatrolMainActivity.class);
                    startActivity(i_patrol);
                    finish();
                    stopGPS();
                    stopTimer(MainActivity.this);
                    return true;
                } else if (i == 3 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
                    Intent i_tow = new Intent(MainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(MainActivity.this);
                    return true;
                } else if (i == 4 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
                    Intent i_citizen = new Intent(MainActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(MainActivity.this);
                    return true;
                }
            }


            /*else if (i == 1 && dataList.get(0).child.size() == 1) {//???????????? ?????? ???????????? 1??? ?????? ????????? ??????
                VIEW_TAG = VIEW_INNEREMPLOYEE;
                Intent i_employee = new Intent(TowMainActivity.this, InnerEmployActivity.class);
                startActivity(i_employee);
                finish();
                stopGPS();
                stopTimer(TowMainActivity.this);
                return true;
            } else if (i == 2 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
                Intent i_patrol = new Intent(TowMainActivity.this, PatrolMainActivity.class);
                startActivity(i_patrol);
                finish();
                stopGPS();
                stopTimer(TowMainActivity.this);
                return true;
            } else if (i == 3 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
                Intent i_tow = new Intent(TowMainActivity.this,
                        TowMainActivity.class);
                startActivity(i_tow);
                finish();
                stopGPS();
                stopTimer(TowMainActivity.this);
                return true;
            } else if (i == 4 && dataList.get(2).child.size() == 1) {//?????? ???????????? 1????????? ????????? ??????
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
                        Intent i_employee = new Intent(MainActivity.this, InnerEmployActivity.class);
                        startActivity(i_employee);
                        finish();
                        stopGPS();
                        stopTimer(MainActivity.this);
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
                        VIEW_TAG = VIEW_INNEREMPLOYEE;
                        Intent i_patrol = new Intent(MainActivity.this, PatrolMainActivity.class);
                        startActivity(i_patrol);
                        finish();
                        stopGPS();
                        stopTimer(MainActivity.this);
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

                    } catch (JSONException e) {
                        Log.e("??????","????????????");
                    }catch (Exception e){
                        Log.e("??????","????????????");
                    }
                    Intent i_tow = new Intent(MainActivity.this,
                            TowMainActivity.class);
                    startActivity(i_tow);
                    finish();
                    stopGPS();
                    stopTimer(MainActivity.this);
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

                    } catch (JSONException e) {
                        Log.e("??????","????????????");
                    } catch(Exception e){
                        Log.e("??????","????????????");
                    }
                    Intent i_citizen = new Intent(MainActivity.this,
                            MainActivity.class);
                    startActivity(i_citizen);
                    finish();
                    stopGPS();
                    stopTimer(MainActivity.this);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //????????? ??????
            if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL) || Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)) {
                switch (position) {
                    case 1:
                        Intent i_employee = new Intent(MainActivity.this, InnerEmployActivity.class);
                        startActivity(i_employee);
                        finish();
                        stopGPS();
                        stopTimer(MainActivity.this);
                        break;
                    // ?????????
                    case 2:
                        Intent i_patrol = new Intent(MainActivity.this, PatrolMainActivity.class);
                        startActivity(i_patrol);
                        finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
                        break;
                    // ?????????
                    case 3:
                        Intent i_tow = new Intent(MainActivity.this, TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
                        break;

                    // ?????????
                    case 4:
                        Intent i_citizen = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(i_citizen);
                        finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
                        break;

                    default:
                        break;
                }

		/*	if(Configuration.User.getUser_type().equals(USER_TYPE_PATROL)){
				switch (position) {
					case 1:

						Intent i_patrol = new Intent(MainActivity.this, PatrolMainActivity.class);
						startActivity(i_patrol);
						finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
						break;
					// ?????????
					case 2:
						Intent i_tow = new Intent(MainActivity.this, TowMainActivity.class);
						startActivity(i_tow);
						finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
						break;

					// ?????????
					case 3:
						Intent i_citizen = new Intent(MainActivity.this, MainActivity.class);
						startActivity(i_citizen);
						finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
						break;

					default:
						break;
				}*/
            } else if (Configuration.User.getUser_type().equals(USER_TYPE_TOW)) {
                switch (position) {
                    // ?????????
                    case 1:
                        Intent i_tow = new Intent(MainActivity.this, TowMainActivity.class);
                        startActivity(i_tow);
                        finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
                        break;

                    // ?????????
                    case 2:
                        Intent i_citizen = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(i_citizen);
                        finish();
//					stopGPS();
//					stopTimer(MainActivity.this);
                        break;

                    default:
                        break;
                }
            }

            dlDrawer.closeDrawer(lvNavList);
        }

    }

    //*********************************Slide Menu  ************************************
    private void setPreference(boolean flag) {
        editor.putBoolean("chkAgree", flag);
        editor.commit();
    }

    private boolean getPreference() {
        return shared.getBoolean("chkAgree", false);
    }

    @Override
    protected void onResume() {
        Log.println(Log.ASSERT, TAG, "onResume");
        Log.d(TAG, TAG + " onResume Call!!");
        contextActivity = MainActivity.this;
        super.onResume();
        db.close();
        db.init();

        stopTimerView();
        startTimerView();
        chkAgree.setChecked(getPreference());
    }

    private void setImageThumdnail() {
        if (null != tImageList) {
            tImageList.clear();
        }
        tImageList = ReadSDCard();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 6;
        options.inPurgeable = true;
        options.inDither = true;


        if (null != tImageList) {
            for (int i = 0; i < tImageList.size(); i++) {
                //???????????? ?????? ??????
                if (tImageList.get(i).getFileType().toString().trim().equals("mp4")) {
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(Configuration.directoryName + "/" + tImageList.get(i).getFileName()
                            , MediaStore.Video.Thumbnails.MICRO_KIND);
//					    Bitmap bit = BitmapFactory.decodeFile(FILE_DIR+items.get(position).getFileName(), options);
                    Drawable drawable = new BitmapDrawable(bMap);
                    capture1.setImageBitmap(null);
                    capture1.setBackgroundDrawable(drawable);
                    capture1.setImageResource(R.drawable.img_movie01);
                } else {
                    Bitmap bit = BitmapFactory.decodeFile(Configuration.directoryName + "/" + tImageList.get(i).getFileName(), options);
                    Drawable drawable = new BitmapDrawable(bit);
                    capture1.setImageBitmap(null);
                    capture1.setBackgroundDrawable(drawable);
                }
            }
        }
    }


    /**
     * ??????,?????? ?????? ?????? ?????????
     * 5????????? ??????
     */
    static Timer viewTimer = new Timer();
    Handler viewHandler = new Handler();

    public void startTimerView() {

        TimerTask timertask = new TimerTask() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (saveMainValueFlag == true) {
                        if ("".equals(Common.nullCheck(SituationService.ns_code))) {
                            etLocation.setText("???????????? ?????? ???????????? ????????????.");
                        } else {
                            if (Common.nullCheck(main_banghyang).contains("??????")) {
                                etLocation.setText(SituationService.ns_name + " " + SituationService.banghyang + " " + SituationService.currentIjung + " km");
                            } else {
                                etLocation.setText(SituationService.ns_name + " " + SituationService.banghyang + " ?????? " + SituationService.currentIjung + " km");
                            }
                        }
                        main_nscode = SituationService.ns_code;
                        main_ijung = SituationService.currentIjung;
                        main_nsname = SituationService.ns_name;
                        main_banghyang = SituationService.banghyang;
                        main_bhcode = SituationService.bhCode;

                    } else {

                    }

                }
            };


            @Override
            public void run() {
                viewHandler.post(runnable);

            }
        };
        viewTimer = new Timer();
        viewTimer.schedule(timertask, 1000, 2000);// 100MS ?????????, 1??? ???????????? ??????.
    }

    public static void stopTimerView() {
        if (null != viewTimer) {
            viewTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, TAG + " onPause Call!!");
        super.onPause();
        stopTimerView();
    }

    @Override
    protected void onDestroy() {
        Log.println(Log.ASSERT, TAG, TAG + " onDestroy Call!!");
        super.onDestroy();
    }


    /*
     * primitive ?????? URL??????
     * result ????????? ?????????.
     */
    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
        if (null == e) {
            if (null != result) {
                try {
                    if (primitive.equals(ONECLICK_CARGPS_INSERT)) {

                        Log.i(TAG, TAG + "onActionPost PRIMITIVE_GPSINFO_INSERT = " + ONECLICK_CARGPS_INSERT);

                    } else if (primitive.equals(ONECLICK_CITIZENRCEPIT_INSERT)) {
                        if (0 < ReadSdCardSize()) {
                            Toast.makeText(MainActivity.this, "????????? ?????????????????????. ????????? ???????????????.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                        }

                        byte[] encryptBytes = seed.encrypt(SituationService.conf.USER_PHONE_NUMBER, szKey);
                        String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes)));

                        Parameters params = new Parameters();
                        params.put("hp_no", SituationService.conf.USER_PHONE_NUMBER);
                        params.put("rpt_id", Common.nullCheck(result.get("rpt_id")));
                        params.put("reg_type", regType);
                        params.put("nscode", SituationService.ns_code);
                        params.put("nsname", SituationService.ns_name);
                        params.put("bhcode", SituationService.bhCode);
                        params.put("bhname", SituationService.banghyang);
                        params.put("ijung", SituationService.currentIjung);
                        if (Configuration.User.getBscode_list() != null) {
                            if (Configuration.User.getBscode_list().size() > 0) {
                                params.put("bscode", Configuration.User.getBscode_list().get(0));
                            } else {
                                params.put("bscode", "");
                            }
                        } else {
                            params.put("bscode", "");
                        }

                        if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
                            params.put("patcar_id", Configuration.User.getPatcar_id());
                        }
                        executeJob(params, MainActivity.this);
                        stopTimer(contextActivity);
                    }

                } catch (XmlPullParserException e2) {
                    Log.e("??????","??????");
                } catch (Exception e2) {
                    Log.e("??????","??????");
                }
            }
        } else {
            Log.e("", /*e.toString()*/"Exception ??????");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.menu:
                if (!Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
                    DrawerLayout dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
                    dlDrawer.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.btnAgree:
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setMessage("").setTitle("???????????? ?????? ??????").setMessage("* ???????????? ?????????????? ??????\n1) ??????????????? ??????\n???????????? ??????????????? ??????????????? ???????????????.\n2) ????????? ???????????????, ??? ????????? ????????? ????????? ???????????? ???????????? ????????????.").setCancelable(false).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                ad.show();

                break;
            case R.id.btnJaeboCall:
                Intent custom = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1588-2504"));
                startActivity(custom);
                break;
            case R.id.goCamera:

//	        private String mFileimageRoute = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/situationmanager/";
//			Intent intent = new Intent();
//	        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//	        intent.putExtra( MediaStore.EXTRA_OUTPUT, tempPictuePath ) ;
//	        startActivityForResult(intent, Configuration.IMAGE_CAPTURE);	

                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                cameraIntent.putExtra("MainType", "0003");
                startActivity(cameraIntent);

                break;
            case R.id.imLocation:
                Intent locIntent = new Intent(getApplicationContext(), LocChangeActivity.class);
                locIntent.putExtra("ns_code", main_nscode);
                locIntent.putExtra("ns_name", main_nsname);
                locIntent.putExtra("bhCode", main_bhcode);
                locIntent.putExtra("banghyang", main_banghyang);
                locIntent.putExtra("currentIjung", main_ijung);
                startActivityForResult(locIntent, LOC_CHANGE);
                break;
            case R.id.goAlbum:


                Intent i = new Intent(MainActivity.this, AndroidCustomGalleryActivity.class);
                i.putExtra("MainType", "0003");
                startActivityForResult(i, Configuration.FILE_TYPE_IMAGE);



                break;
            case R.id.btnUserInfo:
                Intent info = new Intent(getApplicationContext(), DialogUserInfoActivity.class);
                info.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pi_user = PendingIntent.getActivity(getApplicationContext(), 0, info, PendingIntent.FLAG_ONE_SHOT);
                try {
                    pi_user.send();
                } catch (PendingIntent.CanceledException ee) {
                    Log.e("??????","??????");
                } catch (Exception e) {
                    Log.e("??????","??????");
                }
                break;

            case R.id.goPreview:
                Intent intent = new Intent(MainActivity.this, ImageListActivity.class);
                startActivity(intent);
                break;

            case R.id.btnRoadKill://?????????
                sendCitizenData("0008", "?????????");
                break;
            case R.id.btnLoadFault://????????????
                sendCitizenData("0010", "????????????");
                break;
            case R.id.btnRoadEtc://????????????
                sendCitizenData("0003", "????????????");
                break;
            case R.id.btnFacility://????????? ??????
                sendCitizenData("0009", "????????? ??????");
                break;
            case R.id.btnAccident://????????????
                sendCitizenData("0005", "????????????");
                break;
            case R.id.btnCarBroken://????????????/??????
                sendCitizenData("0004", "????????????");
                break;


            //????????????
	/*		case R.id.gongsaManagement:

					intent = new Intent(MainActivity.this,com.ex.gongsa.view.MainActivity.class);
					intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				break;*/
            default:
                break;

        }
    }

    public void  sendCitizenData(String regType, String content) {
        if (main_nscode.equals("")) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("").setTitle("??????").setMessage("* ???????????? ?????? ????????? ??? ????????????.").setCancelable(false).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            ad.show();
        } else if (getPreference() == false) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage("").setTitle("??????").setMessage("* ?????????????????? ????????? ?????? ???????????????.").setCancelable(false).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            ad.show();
        } else {
            this.regType = regType;
            Intent info = new Intent(getApplicationContext(), DialogConfirmActivity.class);
            info.putExtra("info", main_nsname + " " + main_banghyang + " " + main_ijung + "km \n" + "'" + content + "'\n" + "???????????? ????????????\n ?????? ???????????????????");
            System.out.println("main_nsname : " + main_nsname);
            System.out.println("main_banghyang : " + main_banghyang);
            System.out.println("main_ijung : " + main_ijung);
            startActivityForResult(info, CONFIRM);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("", TAG + " onActivityResult requestCode = " + requestCode);
        Log.d("", TAG + " onActivityResult resultCode = " + resultCode + ":" + RESULT_OK);

        String cameraPic = "??????, ????????? ???????????? 10MB ????????? ?????? ???????????????.";
        String albumPic = "??????, ????????? ???????????? 10MB ????????? ?????? ???????????????.";
        //????????????
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CONFIRM:
                    Log.d("", TAG + "ONECLICK_CITIZENRCEPIT_INSERT  onActivityResult CONFIRM ");
                    Parameters params = new Parameters(ONECLICK_CITIZENRCEPIT_INSERT);
                    //byte[] encryptBytes = seed.encrypt(Common.nullCheck(Configuration.USER_PHONE_NUMBER).replace("-", ""), szKey);
                    //2021.08 ????????? ????????? ??????
                    byte[] encryptBytes = seed.encrypt(Common.nullCheck(Configuration.USER_PHONE_NUMBER), szKey);
                    String encString = new String(Base64.encodeBase64(encryptBytes));
                    params.put("hp_no", encString);
//				params.put("hp_no", Common.nullCheck(Configuration.USER_PHONE_NUMBER).replace("-", ""));
                    params.put("reg_type", regType);
                    params.put("rpt_id", "");//???????????? rpt_id??? ????????? ?????????.
                    params.put("car_way", main_nscode);
                    params.put("car_direction", main_bhcode);
                    params.put("car_ijung", main_ijung);

                    //2021.08 ????????? ?????????????????????
                    //System.out.println("----------------------------- ???????????? ????????? : " + params.toString());

                    // ????????? ????????????
                    new Action(ONECLICK_CITIZENRCEPIT_INSERT, params).execute();

                    break;
                case LOC_CHANGE:
                    saveMainValueFlag = false;

                    main_nscode = data.getStringExtra("ns_code");
                    main_nsname = data.getStringExtra("ns_name");
                    main_bhcode = data.getStringExtra("bhCode");
                    main_banghyang = data.getStringExtra("banghyang");
                    main_ijung = data.getStringExtra("currentIjung");
                    Log.d("", "onActivityResult LOC_CHANGE = here" + main_bhcode);
                    if ("".equals(Common.nullCheck(main_ijung))) {
                        etLocation.setText("???????????? ?????? ???????????? ????????????.");
                    }
                    if (Common.nullCheck(main_banghyang).contains("??????")) {
                        etLocation.setText(main_nsname + " " + main_banghyang + " " + main_ijung + " km");
                    } else {
                        etLocation.setText(main_nsname + " " + main_banghyang + " ?????? " + main_ijung + " km");
                    }

                    break;

                case FILE_TYPE_IMAGE:
                    //--------------------------------------------------------------------------------------------
                    // #region ?????? > ?????? ?????? ?????? ??? ??????

                    if (resultCode == RESULT_OK) {

//    	    		Log.d("file", "Heap Size : "+Long.toString(Debug.getNativeHeapAllocatedSize()));
//    	    		
//    	    		mUriSet = data.getData();
//    	    		File tempfile = new File(getRealPathFromURI(mUriSet)); 
//    	    		double mbTemp = ReadSDCardMB()+getFileSizeMB(tempfile.length()); 
//        			if(mbTemp < 10.0){
//        				if (common == null) {
//    						common = new Common(getApplicationContext());
//    					}
//        				common.cameraPicRequestSelect(MainActivity.this, data);
//        			}
//    	    		
//				    String[] arrayOfString = mUriSet.toString().split("/");
//				    int iUriId = Integer.parseInt(arrayOfString[arrayOfString.length - 1]);
//
//				    Log.d("file", "mUriSet : "+mUriSet.toString());
//				    Log.d("file", "mUriSet : "+getRealPathFromURI(mUriSet));
//				    
//				    BitmapFactory.Options options = new BitmapFactory.Options();
//				    options.inSampleSize = 6;
//				    options.inPurgeable = true; 
//					options.inDither = true;

                        fileType = "2";    //????????? ?????? ??????
                    }//end if

                    // #endregion
                    //--------------------------------------------------------------------------------------------
                    break;
                case FILE_TYPE_VIDEO:
                    //--------------------------------------------------------------------------------------------
                    // #region ?????? > ????????? ?????? ?????? ??? ??????

                    if (resultCode == RESULT_OK) {

                        mUriSet = null;

                        if (data == null) {
                            mUriSet = mPhotoUri;
                        } else {
                            mUriSet = data.getData();
                            /////////////////////////////////////////////////
                            File tempfile = new File(getRealPathFromURI(mUriSet));
                            double mbTemp = ReadSDCardMB() + getFileSizeMB(tempfile.length());
                            Log.d("", "FILE_TYPE_VIDEO getfile = " + mbTemp);
                            if (mbTemp < 10.0) {
                                common.cameraVicRequestSelect(MainActivity.this, data);
                            }

                        }

                        Log.d("file", "mUriSet : " + getRealPathFromURI(mUriSet));

                        Log.d("VideoCapture", "=====" + mUriSet.getPath().toString());
                        Log.d("VideoCapture", "=====" + mUriSet.getEncodedPath().toString());

                        String[] proj = {MediaStore.Video.Media.DATA};
                        Cursor cursor = managedQuery(mUriSet, proj, null, null, null);
                        int index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

                        cursor.moveToFirst();

                        String path = cursor.getString(index);
                        Log.d("ImageCapture", "=====" + path);
                        File temp = new File(path);
                        path = Configuration.directoryName + "/" + temp.getName();

                        Log.d("ImageCapture", "=====" + path);

                        fileType = "2"; //????????? ?????? ??????
                    }//end if

                    // #endregion
                    //--------------------------------------------------------------------------------------------
                    break;

                default:
                    break;
            }

        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == LOC_CHANGE) {
                saveMainValueFlag = true;
                Log.d("", TAG + " onActivityResult == " + resultCode + ":" + requestCode);
            }
        }
    }

    //onActivityResult ??????????????? ??????????????? ????????? ????????????
    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {
        if (null != etLocation) {
            if ("".equals(Common.nullCheck(SituationService.ns_code))) {
                if (saveMainValueFlag == true) {
                    Log.d("", TAG + " setActivityViewEdit type1");
                    etLocation.setText("???????????? ?????? ???????????? ????????????.");
                    main_nscode = SituationService.ns_code;
                    main_nsname = SituationService.ns_name;
                    main_banghyang = SituationService.banghyang;
                    main_ijung = "";
                    main_bhcode = SituationService.bhCode;
                }
            } else {
                if (saveMainValueFlag == true) {
                    Log.d("", TAG + " setActivityViewEdit type2");
                    etLocation.setText(SituationService.ns_name + " " + SituationService.banghyang + " ?????? " + SituationService.currentIjung + " km");
                    main_nscode = SituationService.ns_code;
                    main_nsname = SituationService.ns_name;
                    main_banghyang = SituationService.banghyang;
                    main_ijung = SituationService.currentIjung;
                    main_bhcode = SituationService.bhCode;
                }
            }
        }

    }

    AudioManager am = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("", TAG + " onkeydown " + keyCode + ":" + KeyEvent.KEYCODE_BACK);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP://?????? ???
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                Log.d("", "volume1 = " + curVol);
                if (curVol < maxVol) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, curVol, AudioManager.STREAM_MUSIC);
                }*/

                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN://?????? ??????
                /*setVolumeControlStream(AudioManager.STREAM_MUSIC);
                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.d("", "volume1 = " + currVol);
                if (currVol > 0) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, currVol, AudioManager.STREAM_MUSIC);
                }
                Log.d("", "volume2 = " + currVol);*/
                break;
            case KeyEvent.KEYCODE_BACK://??????
                if (!Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(this);
                    ad.setMessage("").setTitle("??????").setMessage("* ??? ????????? GPS???????????? ???????????? ???????????? \n?????? ???????????????????.").setCancelable(false).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(MainActivity.this, SituationService.class);
                            if ("Y".equals(Common.getPrefString(MainActivity.this, "start_yn"))) {
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
                } else {
                    Intent i = new Intent(MainActivity.this, SituationService.class);
                    stopService(i);
                    finish();
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
