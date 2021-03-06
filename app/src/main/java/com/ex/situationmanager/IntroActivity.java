package com.ex.situationmanager;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.system.Os;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ex.gongsa.view.WorkPlanRegisterActivity;
import com.ex.gongsa.view.WorkPlanResisterListActivity;
import com.ex.situationmanager.dto.Userinfo;
import com.ex.situationmanager.encrypt.SeedCipher;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationBroadcast;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class IntroActivity extends BaseActivity implements OnClickListener {
    public static int userInfoNumber = 0;
    private final String TAG = "IntroActivity";
    private boolean sendGpsFlag = false;
    ArrayList<Userinfo> items;

    boolean MainJepbo = false;
    String serverAppVersion = "";

    String delay_timer_y = "";
    String delay_timer_n = "";

    public static Boolean isSoundAlert = true;
	/* TODO[JSP Remaerk] modified 2019.07.03
		???????????? ?????????????????? ??????????????? ?????? ??????*/

    //public static Intent jsonArrayIntent;
    static Intent jsonObjectIntent;
    //????????? ???????????? ???????????? boolen
    public static Boolean gpsListFlag = false;

    //???????????? ????????? ??????
    String gongsaUser_Type = "";
    JSONArray allJisaListJsonArray = null;
    JSONObject allJisaListJob = null;


    String savedUserInfoStr = null;
    JSONObject savedUserInfoJSonObj = null;

    String tunnelImp = "";

    //2021.05 ???????????? ?????? ?????? ??????
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "";
    public static final String ROOTING_PATH_1 = "/system/bin/su";
    public static final String ROOTING_PATH_2 = "/system/xbin/su";
    public static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
    public static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";
    public static final String ROOTING_PATH_5 = "/data/data/com.tegrak.lagfix";
    public static final String ROOTING_PATH_6 = "/data/data/eu.chainfire.supersu";

    public static String[] RootFilesPath = new String[]{
            ROOT_PATH + ROOTING_PATH_1,
            ROOT_PATH + ROOTING_PATH_2,
            ROOT_PATH + ROOTING_PATH_3,
            ROOT_PATH + ROOTING_PATH_4,
            ROOT_PATH + ROOTING_PATH_5,
            ROOT_PATH + ROOTING_PATH_6
    };

    //2021.05 ???????????? ?????? ?????? ??????
    public static boolean checkRootingDevice() {
        boolean isRootingFlag = false;
        try {
            Runtime.getRuntime().exec("su");
            isRootingFlag = true;
        } catch (Exception e) {
            isRootingFlag = false;
        }
        if (!isRootingFlag) {
            isRootingFlag = checkRootingFiles(RootFilesPath);
        }
        return isRootingFlag;
    }

    //2021.05 ???????????? ?????? ?????? ??????
    public static boolean checkRootingFiles(String[] filePaths) {
        boolean result = false;
        File file;
        for (String path : filePaths) {
            file = new File(path);
            if (file != null && file.exists() && file.isFile()) {
                result = true;
                break;
            } else {
                result = false;
            }
        }
        return result;
    }

    protected void startForegroundService() {
        Intent intent = new Intent(IntroActivity.this, SituationService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra("start", "start");
            startService(intent);
            Log.e("IntroActivity ---", "STARTFOREGROUNDSERVICE");
        } else {
            startService(intent);
        }
    }

    //TODO[JSP Remark] : modified 2019.07.03
    // runTimpermission()
    public void runTimePermission() {
        Log.println(Log.ASSERT, TAG, "IntroActivity runTimePermission  -  ???????????? ??????");
        final String[] permissionList;

        if (Build.VERSION.SDK_INT >= 29) {
            permissionList = new String[]{
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.CALL_PHONE};
        } else {
            permissionList = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.CALL_PHONE};
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("DEBUgging", "Delayed");
                    int count = 0;
                    for (String permission : permissionList) {

                        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(permissionList, 2);
                            count++;
                            break;
                        }
                    }

                    if (count == 0) {
                        checkInitiating();
                    }
                }
            }, 300);

        } else {
            Log.println(Log.ASSERT, TAG, "IntroActivity else ???  -  ???????????? ??????");
            checkInitiating();
        }
    }

    //?????? ???????????? ?????? ???????????? ???????????? ?????? ????????? ?????????. ????????? ???????????? ??????????????? return false;
    public boolean hasAllPermissionGranted(int[] grantResults) {
        boolean check = false;
        for (int result : grantResults) {
            //   Log.i(TAG, "result..."+result);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if (hasAllPermissionGranted(grantResults)) {//?????? ????????? ???????????? initiating() ??????
                    checkInitiating();

                } else {//????????? ????????? ????????? ??? ??????
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("?????? ????????? ?????? ???????????????.\n ?????????????????????.").setPositiveButton("??????",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    dialog.show();
                }
                break;

            case 3: // 12 ?????? ??????
                if (!hasAllPermissionGranted(grantResults)) {//?????? ????????? ???????????? initiating() ??????
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("?????? ????????? ?????? ???????????? ??????????????????.\n ?????? ???????????????.").setPositiveButton("??????",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    dialog.show();
                } else {
                    initiating();
                }
                break;
        }
    }

    public void checkInitiating() {
        if (Build.VERSION.SDK_INT >= 29) {
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
                ading.setTitle("???????????? ?????? ??????")
                        .setMessage(getString(R.string.location_dialog))
                        .setCancelable(false)
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] locationPermission = {Manifest.permission.ACCESS_BACKGROUND_LOCATION};
                                requestPermissions(locationPermission, 3);
                            }
                        })
                        .show();
            } else {
                initiating();
            }

        } else {
            initiating();
        }
    }

    public void initiating() {
        Log.println(Log.ASSERT, TAG, "initiating() ??????  -  ???????????? ??????");

        if (!chkGpsService(this)) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(IntroActivity.this);
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

        common = new Common(getApplicationContext());
        common.copyFile("exApp.mp4");
        common.DeleteDir(SituationService.conf.directoryName);

        //TODO[JSP Remark] :  modified 2019.07.03
        // ????????? permission ??????


        db.init();
        db.deleteRptId();
        MainJepbo = false;

        TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);//????????? ??????????????? ?????????????????? ?????????
        if (
                PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                        PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                        PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                        PermissionChecker.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("DEBUGGING", "This will be a final Error");
            return;
        }

        Log.e("DEBUGGING", "This will be a final Error Really");
        String[] phoneNo = Common.phoneNumCut(systemService.getLine1Number());//????????? ???????????? ??????
        String mUserTel = phoneNo[0] + "-" + phoneNo[1] + "-" + phoneNo[2];//???????????? ????????? - ??????
        mUserTel = Common.nullCheck(mUserTel);

        SituationService.conf.USER_PHONE_NUMBER = mUserTel;         // ????????? ????????? (* 2021??????)
        // SituationService.conf.USER_PHONE_NUMBER = "010-5000-4982";
        // SituationService.conf.USER_PHONE_NUMBER = "010-7688-0352"; // ????????? ??????
        // SituationService.conf.USER_PHONE_NUMBER = "010-5382-8426"; // ????????? ??????

        // TODO[JSPRemark] : modified in 2019.07.03

        startForegroundService();


        SituationService.conf.User.setHp_no(SituationService.conf.USER_PHONE_NUMBER);

        systemService = null;//????????? ???????????? ????????? null??? ????????? ?????? ??????
        phoneNo = null;
        mUserTel = null;

        //????????? ?????????..
        SituationService.conf.User.getBscode_list().clear();
        SituationService.conf.User.getBsname_list().clear();
        SituationService.conf.User.getCrdns_id_list().clear();
        SituationService.conf.User.getRcv_yn_list().clear();
        SituationService.conf.User.getRcv_bs_list().clear();

        getCertData(IntroActivity.this);

        if ("".equals(Common.getPrefString(IntroActivity.this, "jisa_init"))) {
            db.updateBBBsCodeClean();
            Common.setPrefString(IntroActivity.this, "jisa_init", "Y");
            Log.d("", TAG + " jisainit in ");
        }

        Log.println(Log.ASSERT, TAG, "initiating() new Action ??????");
        new Action(ONECLICK_NOTICE_SELECT, null).execute("");//?????? ?????? ( ?????? ????????? )
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        //2021.05 ???????????? ?????? ?????? ??????
        checkRootingDevice();
        //stopService(intent);
        Log.println(Log.ASSERT, TAG, "onCreate  -  ???????????? ??????");
        if (intent != null) {
            if (intent.getStringExtra("finishAppxx") != null) {

                //Log.println(Log.ASSERT,TAG, "finishApp : "+ intent.getStringExtra("finishApp"));
                if (intent.getStringExtra("finishAppxx").equals("N")) {
                    gpsListFlag = false;
                    //Log.println(Log.ASSERT,TAG,"finishAppxx ----N");
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (isFinishing() == true) {
                                //  Log.println(Log.ASSERT,TAG,"??????");
                            } else {
                                intent = new Intent(IntroActivity.this, SituationService.class);
                                stopService(intent);
                                this.finishAndRemoveTask();
                                finish();
                            }
                        } else {
                            finish();
                            stopService(intent);
                        }
                        ActivityCompat.finishAffinity(this);
                    } catch (NullPointerException e) {
                        Log.e("??????", "??????");
                    }
                }
            } else {
                try {
                    setContentView(R.layout.intro);
                    gpsListFlag = true;

                    AlertDialog.Builder ad = new AlertDialog.Builder(this);
                    ad.setMessage("").setTitle("???????????? ?????? ??????").setMessage(getString(R.string.location_term)).setCancelable(false).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runTimePermission();
                        }
                    });
                    ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Common.setPrefString(IntroActivity.this, "tow_private", "N");

                            onBackPressed();
                        }
                    });
                    ad.show();

                    //Log.println(Log.ASSERT,TAG,"--------------13");
                } catch (NullPointerException e) {
                    Log.e("??????", "?????????");
                } catch (Exception e) {
                    Log.e("??????", "?????????");
                }
            }
        }
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

    }

    private boolean isRooted() {
        boolean isRootingFlag = false;
        try {
            Runtime.getRuntime().exec("su");
            isRootingFlag = true;
        } catch (IOException e) {
            isRootingFlag = false;
        } catch (Exception e) {
            isRootingFlag = false;
        }
        Log.i(TAG, "?????? ?????? : " + isRootingFlag);
        return isRootingFlag;
    }

    public void getUserInfo() {//????????????:ONECLICK_GET_USERINFO_SELECT,????????????ONECLICK_GETUSERINFO_SELECT
        //2016-03-11 ?????? ??????//
        Parameters params = new Parameters(ONECLICK_GET_USERINFO_SELECT2);
        byte[] encryptBytes = seed.encrypt(SituationService.conf.USER_PHONE_NUMBER, szKey);
        String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes)));
        params.put("hp_no", encString);
        params.put("app_name", Common.PREF_SITUATIONMANAGER);
        params.put("hash_code", "2QUM0REaOZLP99cn9IVbscdwmgE=");//????????????
        //params.remove("hash_code");
//       params.put("hash_code", seed.renameSpecificChar(getCertData(IntroActivity.this)));//????????? ?????????
        //   Log.i("getUserInfo:","getUserInfo Action ?????? ???");
        Log.println(Log.ASSERT, TAG, "getUserInfo  Action execute ?????? ???");
        new Action(ONECLICK_GET_USERINFO_SELECT2, params).execute("");
    }

    public String getGongsaType() {
        com.ex.gongsa.view.BaseActivity baseActivity = new com.ex.gongsa.view.BaseActivity() {
            @Override
            public String onActionPost(String primitive, String date) {
                return null;
            }
        };

        String getResult = "";

        try {//???????????? ??????????????? ????????? ??????????????? ?????????
            SeedCipher seed = new SeedCipher();
            byte[] szKey = {(byte) 0x88, (byte) 0xE3, (byte) 0x44,
                    (byte) 0x8F, (byte) 0x28, (byte) 0x32, (byte) 0xFE, (byte) 0xF1,
                    (byte) 0xF9, (byte) 0xF3, (byte) 0xF1, (byte) 0x37, (byte) 0xFF,
                    (byte) 0xA4, (byte) 0x05, (byte) 0x29};

            byte[] encryptBytes = seed.encrypt(SituationService.conf.USER_PHONE_NUMBER, szKey);
            String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes)));

            getResult = Common.nullCheck(baseActivity.new Action("get", SERVER_URL + "/gongsaManagementCheck2.do", encString, this).execute("", "", "").get());

            Log.i("getResult", getResult);
            Log.println(Log.ASSERT, TAG, "????????????" + getResult);

            gongsaUser_Type = Common.nullCheck(new JSONArray(getResult).getJSONObject(0).get("USE_CLSS_CD").toString());

        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.println(Log.ASSERT, TAG, "?????????:" + getResult);
        }

        return getResult;
    }

    public String getCertData(Context context) {
        String cert = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature certSignature = packageInfo.signatures[0];
            // ?????????????????? ????????? ???('SHA1'????????????)
            MessageDigest msgDigest = MessageDigest.getInstance("SHA1");
            byte[] digest = msgDigest.digest();

            // Base64??? ???????????? ??? ?????? ????????????
            msgDigest.update(certSignature.toByteArray());

            cert = new String(Base64.encodeBase64(msgDigest.digest()));

            return cert;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("??????", "?????????");
        } catch (NoSuchAlgorithmException e) {
            Log.e("??????", "?????????");
        }
        return cert;
    }


    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
//		Log.i("?????????????????? ????????? ??????","primitive:"+primitive+", "+"result:"+result.toString());
        // Log.println(Log.ASSERT,TAG,"?????????????????? ????????? ??????"+"primitive:"+primitive+", "+"result:"+result.toString());
        if (e == null) {
            try {
                if (ONECLICK_NOTICE_SELECT.equals(Common.nullCheck(primitive)) == true) {
                    Log.i("onPostAction ?????????:", "if");
                    Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "if");

                    if (null != result) {
                        String title = Common.nullCheck(result.get("title"));
                        String content = Common.nullCheck(result.get("content"));
                        String subcontent = Common.nullCheck(result.get("subcontent"));

                        Log.i("?????? ?????? title:", title);
                        Log.i("?????? ?????? content:", content);
                        Log.i("?????? ?????? subcontent:", subcontent);

                        if (content.length() > 0) {
                            marketDialogtype4(title, content + "\n" + subcontent);
                        } else {
                            Log.i("??????:", "else");
                            getUserInfo();
                        }
                    }

                } else if (ONECLICK_GET_USERINFO_SELECT2.equals(Common.nullCheck(primitive)) == true) {
                    // } else if (ONECLICK_GET_USERINFO_SELECT.equals(Common.nullCheck(primitive)) == true) {
                    Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "eles if");

                    if (null != result) {
                        Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "result != null");
                        String rtnResultCode = Common.nullCheck(result.get("result"));
                        serverAppVersion = Common.nullCheck(result.get("version"));
                        String hashCode = Common.nullCheck(result.get("hash_code"));
                        delay_timer_y = Common.nullCheck(result.get("delay_timer_y"));
                        delay_timer_n = Common.nullCheck(result.get("delay_timer_n"));

                        //?????? DB
                        Log.i("hashCode:", hashCode);
                        if (!"Y".equals(hashCode)) {
                            //  if (false) {
                            Toast.makeText(IntroActivity.this, "??????,????????? ?????? ??? ????????????.\n?????? ???????????????.", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }

                        if ("1000".equals(rtnResultCode)) {
                            Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "????????? ?????? 1000");

                            result.setList("entity");
                            SituationService.conf.User.setGroup_id("");
//							db.updateBBBsCodeClean();

                            for (int i = 0; i < result.size(); i++) {
                                Log.println(Log.ASSERT, TAG, "????????? ??????:" + Common.nullCheck(result.get(i, "user_type")));
                                //??????
                                SituationService.conf.User.setUser_type(Common.nullCheck(result.get(i, "user_type")));
                                SituationService.conf.User.getBscode_list().add(Common.nullCheck(result.get(i, "bscode")));
                                SituationService.conf.User.getBsname_list().add(Common.nullCheck(result.get(i, "bsname")));


                                if (i == 0) {
                                    SituationService.conf.User.setGroup_id(SituationService.conf.User.getHp_no() + "|" + Common.nullCheck(result.get(i, "group_id")));
                                } else {
                                    SituationService.conf.User.setGroup_id(SituationService.conf.User.getGroup_id() + "|" + Common.nullCheck(result.get(i, "group_id")));
                                }

                                Common.setPrefString(IntroActivity.this, "user_type", SituationService.conf.User.getUser_type());
                                Common.setPrefString(IntroActivity.this, "bscode", SituationService.conf.User.getBscode_list().get(i));
                                Common.setPrefString(IntroActivity.this, "bsname", SituationService.conf.User.getBsname_list().get(i));

                                //??????
                                if (USER_TYPE_TOW.equals(SituationService.conf.User.getUser_type())) {
                                    Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "????????? ??????(??????):" + SituationService.conf.User.getUser_type());
                                    SituationService.conf.User.getCrdns_id_list().add(Common.nullCheck(result.get(i, "crdns_id")));
                                    SituationService.conf.User.setReg_post(Common.nullCheck(result.get(i, "reg_post")));
                                    SituationService.conf.User.setReg_part(Common.nullCheck(result.get(i, "reg_part")));
                                    SituationService.conf.User.setReg_name(Common.nullCheck(result.get(i, "reg_name")));
                                    SituationService.conf.User.getRcv_yn_list().add(Common.nullCheck(result.get(i, "rcv_yn")));
                                    SituationService.conf.User.getRcv_bs_list().add(Common.nullCheck(result.get(i, "rcv_bs")));
                                    SituationService.conf.User.setTel_no(Common.nullCheck(result.get(i, "tel_no")));
                                    Log.d("IntroActivity : ", "MainJepbo list= " + result.get(i, "reg_part"));

                                    if (Common.nullCheck(result.get(i, "rcv_yn")).equals("Y")) {
                                        MainJepbo = true;//????????????????????? ????????????????????? ?????????????????? ??????.
                                        TowJeppBoJisaName = Common.nullCheck(result.get(i, "bscode"));
                                        TowJeppBoJisaName = Common.nullCheck(result.get(i, "bsname"));
                                        SituationService.conf.User.setPatcar_id(Common.nullCheck(result.get(i, "crdns_id")));

                                        db.updateBBBsCode(Common.nullCheck(result.get(i, "bscode")), "Y");
                                        String bscodes = Common.nullCheck(result.get(i, "rcv_bs"));
                                        if (null != bscodes && !"".equals(bscodes)) {
                                            if (bscodes.contains("|")) {
                                                String[] list = bscodes.split("[|]");
                                                for (int j = 0; j < list.length; j++) {
                                                    db.updateBBBsCode(Common.nullCheck(list[j]), "Y");
                                                }
                                            }
                                        }
                                    }
                                    Log.d("", "IntroActivity onActionPost body = " + Common.nullCheck(result.get(i, "rcv_bs")));
                                    //??????
                                } else if (USER_TYPE_PATROL.equals(SituationService.conf.User.getUser_type())) {
                                    Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "????????? ??????(??????):" + SituationService.conf.User.getUser_type());
                                    SituationService.conf.User.setPatcar_id(Common.nullCheck(result.get(i, "patcar_id")));
                                    SituationService.conf.User.setCar_nm(Common.nullCheck(result.get(i, "car_nm")));
                                    Common.setPrefString(IntroActivity.this, "patcar_id", Common.nullCheck(result.get(i, "patcar_id")));
                                    //??????????????? ??????????????? ??????( bscode ), ????????? ?????? ??????????????? ??????.
                                    BaseActivity.TowJeopBoJisaCode = Common.nullCheck(result.get(i, "bscode"));
                                    db.updateBBBsCode(Common.nullCheck(result.get(i, "bscode")), "Y");

                                    break;
                                }
                                //????????? ??????
                                else if (USER_TYPE_INNEREMPLOYEE.equals(SituationService.conf.User.getUser_type())) {
                                    Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "????????? ??????(????????????):" + SituationService.conf.User.getUser_type());
                                    SituationService.conf.User.setPatcar_id(Common.nullCheck(result.get(i, "patcar_id")));
                                    SituationService.conf.User.setCar_nm(Common.nullCheck(result.get(i, "car_nm")));
                                    Common.setPrefString(IntroActivity.this, "patcar_id", Common.nullCheck(result.get(i, "patcar_id")));
                                    //??????????????? ??????????????? ??????( bscode ), ????????? ?????? ??????????????? ??????.
                                    BaseActivity.TowJeopBoJisaCode = Common.nullCheck(result.get(i, "bscode"));
                                    db.updateBBBsCode(Common.nullCheck(result.get(i, "bscode")), "Y");

                                    //2020.12 ????????????pdf
                                    tunnelImp = result.get("tunnelImp");
                                    System.out.println("tunnelImp1111 : " + tunnelImp);
                                    SituationService.conf.User.setTunnelImp(tunnelImp);

                                    break;
                                }
                            }
                            if (USER_TYPE_TOW.equals(SituationService.conf.User.getUser_type())) {
//								settingJeopboJisa();
                                if (SituationService.conf.User.getCrdns_id_list().size() > 0) {
                                    SituationService.conf.User.setPatcar_id(SituationService.conf.User.getCrdns_id_list().get(0));
                                } else {
                                    SituationService.conf.User.setPatcar_id("");
                                }
                                Log.d("", TAG + " MainJepbo = " + MainJepbo);
                                if (!MainJepbo) {
                                    //?????????.
                                    Intent i = new Intent(getApplicationContext(), DialogIntroActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
                                    try {
                                        pi.send();
                                    } catch (PendingIntent.CanceledException ee) {
                                        /*eLog.e("??????","?????????");*/
                                        Log.e("??????", "??????");
                                    }
                                }
                            }

                            //????????? ??????.
                            try {
                                result.setList("mo");

                                if (Common.nullCheck(result.get("isAllJisaUsedEnabled")).equals("Y")) {
                                    allJisaListJsonArray = new JSONArray();

                                    allJisaListJob = null;
                                    // result.setList("allJisalist");
                                    result.setList("jisa_entity");

                                    for (int i = 0; i < result.size(); i++) {
                                        Log.i("?????? ?????????", "???");
                                        allJisaListJob = new JSONObject();
                                        allJisaListJob.put("user_type", result.getChild(i).get("user_type"));
                                        allJisaListJob.put("bscode", result.getChild(i).get("bscode"));
                                        allJisaListJob.put("bsname", result.getChild(i).get("bsname"));
                                        allJisaListJob.put("tel_no", result.getChild(i).get("tel_no"));
                                        allJisaListJob.put("patcar_id", result.getChild(i).get("patcar_id"));
                                        allJisaListJob.put("car_nm", result.getChild(i).get("car_nm"));
                                        allJisaListJsonArray.put(i, allJisaListJob);
                                    }

                                    SharedPreferences sh = getApplication().getSharedPreferences("userAllJisaList", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sh.edit();

                                    Log.i("allJisaListJsonArray", allJisaListJsonArray.toString());
                                    editor.putString("userAllInfo", allJisaListJsonArray.toString());
                                    Boolean isCommited = editor.commit();

                                    Log.i("vxcv", sh.getString("userAllJisaList", ""));
                                    Log.i("vxcv", sh.getString("userAllInfo", ""));
                                    Log.i("isCommited", isCommited + "");
                                }
                                nextActivity(SituationService.conf.User.getUser_type(), null);

                            } catch (XmlPullParserException ex) {
                                Log.e("??????", "??????");
                            }
                        } else {
                            Toast.makeText(this, "???????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            nextActivity(USER_TYPE_CITIZEN, null);
                        }
                    } else {
                        Toast.makeText(this, "???????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                        Log.println(Log.ASSERT, TAG, "onPostAction ?????????:" + "????????? ??????(??????):" + SituationService.conf.User.getUser_type());
                        nextActivity(USER_TYPE_CITIZEN, null);
                    }
                }
            } catch (JSONException e2) {
                Log.e("??????", "??????");
            } catch (XmlPullParserException xme2) {
                Log.e("??????", "??????");
            }
        } else {
            Toast.makeText(this, "???????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            nextActivity(USER_TYPE_CITIZEN, e);
        }
    }


    //????????? ????????? ?????? ?????? ????????? ??????.

    /**
     * ?????? ?????? ????????? Dialog or Next??????
     *
     * @param userType
     */
    public void nextActivity(final String userType, Exception e) {

//		double sVersion = 1.0;
        double sVersion = Common.nullCheckDouble(serverAppVersion);//?????? ?????? ??????
        double nVersion = Common.nullCheckDouble(Common.getAppVersionName(IntroActivity.this));//?????? ?????? ??????

        Log.d("", TAG + " version check= [ " + sVersion + ":" + nVersion + " ]" + "(" + serverAppVersion);

        if (sVersion != 0.0) {
            if (sVersion == nVersion) {
                stopGPS();
                stopTimer(this);
                goNext(userType);
            } else if (Math.abs(sVersion - nVersion) >= 0.19) {
                marketDialogtype2();
                return;
            } else if (0 < Math.abs(sVersion - nVersion) && Math.abs(sVersion - nVersion) < 0.19) {
                marketDialogType1(userType);
                return;
            }
        } else {//?????? ?????? 0.0 ??? ??????.
            if (serverAppVersion.equals("")) {
                Log.d("", "dialog type -1");
                marketDialogtype3();
            } else if (!serverAppVersion.equals("0.0")) {//???????????? ?????? String ?????? ????????? 0.0??? ?????? ??????.
                Log.d("", "dialog type 1");
                marketDialogtype2();
            } else if (serverAppVersion.equals("")) {//???????????? ?????? String ?????? ????????? ?????? ??????.
                Log.d("", "dialog type 2");
                if (null != e) {
                    marketDialogtype3();
                } else {
                    marketDialogtype2();
                }
            } else {//???????????? ??????.
                Log.d("", "dialog type 3");
                if (null != e) {
                    marketDialogtype3();
                }
//				marketDialogtype2();
            }
        }
    }

    //?????? ???????????? ?????? ??????????????? ?????? ????????? ?????? ??????
    public void marketDialogType1(final String userType) {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle("??? ????????????").setMessage("* ???????????? ???????????? ?????? ??????????????? ????????????. \n???????????? ?????? ???????????????????").setCancelable(false).
                setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goMarket();
                    }
                });
        ading.setNegativeButton("????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopGPS();
                stopTimer(IntroActivity.this);
                goNext(userType);
            }
        });
        ading.show();
    }

    //?????? ???????????? ?????? ??????????????? ?????? ??? ????????????
    public void marketDialogtype2() {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle("??? ????????????").setMessage("* ???????????? ???????????? ??? ??????????????? ???????????????. \n???????????? ?????? ???????????????????").setCancelable(false).
                setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopGPS();
                        stopTimer(IntroActivity.this);
                        goMarket();
                    }
                });
        ading.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopGPS();
                stopTimer(IntroActivity.this);

                onBackPressed();
            }
        });
        ading.show();
    }

    //???????????? ??????
    public void marketDialogtype3() {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle("???????????? ????????????").setMessage("* ???????????? ????????? ???????????? ????????????.").setCancelable(false).
                setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
        ading.show();
    }

    //????????????
    public void marketDialogtype4(String title, String content) {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle(title).setMessage(content).setCancelable(false).
                setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getUserInfo();
                    }
                });
        ading.show();
    }


    //?????? ??????.
    public void goMarket() {
        Intent marketLaunch = new Intent(Intent.ACTION_VIEW);

        // ??????????????????
        marketLaunch.setData(Uri.parse("market://details?id=com.ex.situationmanager"));

        // ????????????
        // marketLaunch.setData(Uri.parse("onestore://common/product/0000713802"));

        startActivity(marketLaunch);
        finish();
    }


    public void maxCharoChoice(final ArrayList<String> list, final JSONArray jsonArray, final Intent jintent) {


        final Spinner spinner = new Spinner(this);
        //  spinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,list));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("????????? ??????????????????.");
        // builder.setView(spinner);
        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    jintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.i("jsonArray??????", jsonArray.toString());
                    Log.i("jsonArray??????", i + "");
                    jintent.putExtra("UserPhoneNo", jsonArray.getJSONObject(i).toString());
                    jintent.putExtra("UserBelongJisaAndBonbu", jsonArray.toString());
                    //jsonArrayIntent=jintent;

                    startActivity(jintent);
                    IntroActivity.this.finish();//???????????? ?????? ?????? ??????
                    //	SharedPreferences.Editor editor = getSharedPreferences("userInfo",MODE_PRIVATE);
                } catch (Exception e) {
                    Log.e("??????", "?????????");
                }


            }
        });
 /*       builder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });*/
        final int result;


        builder.create();
        builder.show();
      /*  Log.i("?????? ??????",spinner.getSelectedItemPosition()+"");
		return spinner.getSelectedItemPosition();*/
    }


    //????????? ???????????? or ????????? ???????????? ???????????? ????????????
    public void goNext(String userType) {

        if (false == isServiceRunning("com.ex.situationmanager.service.SituationService")) {
            Intent serviceIntent = new Intent(IntroActivity.this, SituationService.class);
			stopService(serviceIntent);


            if (USER_TYPE_TOW.equals(userType)) {
                SituationService.listTimerDelay = 20000;
                SituationService.setDelaytimer(Common.nullCheckDelayY(delay_timer_y), Common.nullCheckDelayY(delay_timer_n));
            } else if (USER_TYPE_PATROL.equals(userType)) {
                SituationService.setDelaytimer(Common.nullCheckDelayY(delay_timer_y), Common.nullCheckDelayY(delay_timer_n));
            } else {
                SituationService.setDelaytimer(10000, 10000);
            }
            //TODO[JSPRemark] : modified in 2019.07.03
            startForegroundService();
        }


        //???????????? ?????? ?????? ????????????

        String getResult = getGongsaType();

        Log.println(Log.ASSERT, TAG, "???????????? ??????:" + getResult);
        Log.println(Log.ASSERT, TAG, "???????????? ??????" + getResult);
        Log.println(Log.ASSERT, TAG, "???????????? ??????:" + userType);
        //??????
        //???????????? ????????? ????????? null?????? ??????????????? ??????
        // ?????? ???????????? ???????????? ??????????????? ??????????????? ?????????
        Log.i("user123123Type", userType);
        //gongsaUser_Type
        try {//Common.nullCheck(new JSONObject(userType).getString("USE_CLSS_CD").toString()).equals("H")
            //if(getResult.length() != 0   && userType.equals("")){
            if ((gongsaUser_Type.equals("E") || gongsaUser_Type.equals("SMARTAPP")) && userType.equals("")) {
                isSoundAlert = false;
                Log.println(Log.ASSERT, TAG, "");

                final String getUserInfo = getResult;
                final Intent intent = new Intent(this, com.ex.gongsa.view.MainActivity.class);

                try {
                    gpsListFlag = true;
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("gongsaPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    JSONArray jsonArray = new JSONArray(getUserInfo);
                    JSONObject jsonObject = new JSONObject();
                    editor.putString("UserPhoneNo", jsonArray.getJSONObject(0).toString());
                    editor.putString("UserBelongJisaAndBonbu", jsonArray.toString());

                    editor.commit();

                } catch (Exception e) {
                    Log.e("??????", "?????????");
                }


                Log.i("?????????????????? ????????? ??????", getUserInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    JSONArray jsonArray = new JSONArray(getUserInfo);
                    JSONObject jsonObject = new JSONObject();

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.i("jsonArray??????", jsonArray.toString());

                    startActivity(intent);
                    IntroActivity.this.finish();

                } catch (Exception e) {
                    Log.e("??????", "?????????");
                }

                //gongsaUser_Type.equals("SMARTAPP")
                //}else if(getResult.length() != 0 && !userType.equals("")||Common.nullCheck(new JSONObject(userType).getString("USE_CLSS_CD").toString()).equals("H")){//???????????? ?????????????????? ????????? ?????????
                //}else if(getResult.length() != 0 && !userType.equals("")){//???????????? ?????????????????? ????????? ?????????
            } else if ((gongsaUser_Type.equals("H") && getResult.length() != 0) || (getResult.length() != 0 && !userType.equals(""))) {

                final Intent intent = new Intent(this, com.ex.gongsa.view.MainActivity.class);
                final String uType = userType;
                final String gResult = getResult;
                final Context branchContext = this;

                try {
                    oneClickBranch(uType, gResult, intent, branchContext);
                } catch (Exception e) {
                    Log.e("??????", "?????????");
                }

            } else if ((getResult.length() == 0 && !userType.equals(""))) {//???????????? ???????????? ????????? ???????????? ???????????? ??????

                Log.i("??????", "1067");
                System.out.println("----------------- userType : " + userType.toString());
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                final String uType = userType;
                final Context gContext = this;

                try {

                    Log.i("savedUser", "-----------------------------------------------------");
                    if (Configuration.User.getUser_type().equals("0001") || Configuration.User.getUser_type().equals("0004")) {
                        try {

                            SharedPreferences sh1 = getSharedPreferences("savedUserInfo", MODE_PRIVATE);
                            String savedUser = sh1.getString("savedUserInfo", "fail");
                            Log.i("savedUser", savedUser.toString());

                            savedUserInfoStr = savedUser.toString();
                            if (!savedUserInfoStr.equals("fail")) {
                                savedUserInfoJSonObj = new JSONObject(savedUserInfoStr);

                                for (int ii = 0; ii < allJisaListJsonArray.length(); ii++) {
                                    if (allJisaListJsonArray.getJSONObject(ii).get("car_nm").equals(savedUserInfoJSonObj.get("car_nm")) &&
                                            allJisaListJsonArray.getJSONObject(ii).get("bscode").equals(savedUserInfoJSonObj.get("bscode")) &&
                                            allJisaListJsonArray.getJSONObject(ii).get("bsname").equals(savedUserInfoJSonObj.get("bsname")) &&
                                            allJisaListJsonArray.getJSONObject(ii).get("user_type").equals(savedUserInfoJSonObj.get("user_type"))
                                    ) {
                                        String activity_tag = savedUserInfoJSonObj.get("cur_page").toString();
                                        if (activity_tag.equals("InnerEmployActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, InnerEmployActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        } else if (activity_tag.equals("PatrolMainActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, PatrolMainActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        } else if (activity_tag.equals("TowMainActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, TowMainActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        } else if (activity_tag.equals("MainActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, MainActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        }
                                    }
                                }
                            }

                        } catch (NullPointerException dd) {
                            Log.i("savedUser", "??????");
                        } catch (JSONException e) {
                            Log.e("??????", "??????");
                        }
                    }
                    Log.i("savedUser", "-----------------------------------------------------");


                    if (USER_TYPE_INNEREMPLOYEE.equals(uType)) {
                        isSoundAlert = true;
                        gpsListFlag = false;
                        Log.i("USER_TYPE_PATROL", USER_TYPE_PATROL + "");//InnerEmployCCTVFragment
                        final Intent i = new Intent(gContext, InnerEmployActivity.class);

                        startActivity(i);
                        IntroActivity.this.finish();

                    } else if (USER_TYPE_PATROL.equals(uType)) {//
                        isSoundAlert = true;
                        gpsListFlag = false;

                        Log.println(Log.ASSERT, TAG, "PatrolMainActivity intent ?????? ????????? userType ??????:" + uType);
                        Intent i = new Intent(gContext, PatrolMainActivity.class);
                        startActivity(i);
                        IntroActivity.this.finish();
                    } else if (USER_TYPE_TOW.equals(uType)) {//???????????????
                        isSoundAlert = true;
                        gpsListFlag = false;

                        if ("Y".equals(Common.getPrefString(IntroActivity.this, "tow_private"))) {
                            final Intent i = new Intent(gContext, TowMainActivity.class);
                            startActivity(i);
                            IntroActivity.this.finish();
                        } else {
                            final Intent i = new Intent(gContext, TowMainActivity.class);

                            Common.setPrefString(IntroActivity.this, "tow_private", "Y");
                            startActivity(i);
                            IntroActivity.this.finish();
                        }


                    } else {//???????????????
                        isSoundAlert = false;
                        gpsListFlag = false;

                        SituationService.conf.User.setUser_type(USER_TYPE_CITIZEN);
                        Intent i = new Intent(gContext, MainActivity.class);
                        startActivity(i);
                        IntroActivity.this.finish();
                    }
                } catch (NullPointerException e) {
                    Log.e("??????", "?????????");
                }

            }//2021.09 TEST
            /*else {//???????????? ????????? ??? ???????????? ???????????????
                //}else if((gongsaUser_Type.equals("") && userType.equals(""))){
                isSoundAlert = false;
                gpsListFlag = false;
                Log.i("??????", "1131");
                SituationService.conf.User.setUser_type(USER_TYPE_CITIZEN);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                IntroActivity.this.finish();
            }*/
            else {//???????????? ????????? ??? ???????????? ???????????????
                //}else if((gongsaUser_Type.equals("") && userType.equals(""))){
                isSoundAlert = false;
                gpsListFlag = false;
                Log.i("??????", "1131");
                SituationService.conf.User.setUser_type(USER_TYPE_CITIZEN);
                final Context gContext = this;
                final Intent i = new Intent(this, MainActivity.class);

                Common.setPrefString(IntroActivity.this, "admin_private", "Y");
                startActivity(i);
                IntroActivity.this.finish();
            }
        } catch (NullPointerException e) {
            Log.e("??????", "?????????");
        }

    }


    public Boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {

            Log.d(TAG, TAG + " serviceName [" + serviceName + "] - [" + runningServiceInfo.service.getClassName() + "]");
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {

                Log.d(TAG, TAG + " SituationService running is already Started !!");
                return true;
            }
        }
        Log.d(TAG, TAG + " SituationService running is stoppped !!");
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {
        Log.d("", TAG + " setActivityViewEdit");

    }

    //??????????????? ????????? ??????
    public void oneClickBranch(final String userType, final String getResult, final Intent intent, final Context context) {
        Log.i("?????????", "?????????");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<String> list = new ArrayList<String>();
        list.add("????????? ????????????");
        list.add("????????? ????????????");
        builder.setTitle("???????????? ????????? ???????????? ?????????.");
        builder.setCancelable(false);
        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    //dialogInterface.dismiss();
                    isSoundAlert = false;
                    Log.println(Log.ASSERT, TAG, "????????????");
                    Log.i("??????", "???");

                    final String getUserInfo = getResult;
                    try {
                        gpsListFlag = true;
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("gongsaPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        JSONArray jsonArray = new JSONArray(getUserInfo);
                        JSONObject jsonObject = new JSONObject();
                        editor.putString("UserPhoneNo", jsonArray.getJSONObject(0).toString());
                        editor.putString("UserBelongJisaAndBonbu", jsonArray.toString());

                        editor.commit();
                    } catch (JSONException e) {
                        Log.e("??????", "??????");
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        JSONArray jsonArray = new JSONArray(getUserInfo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Log.i("jsonArray??????", jsonArray.toString());

                        startActivity(intent);
                        IntroActivity.this.finish();

                    } catch (JSONException e) {
                        Log.e("??????", "??????");
                    }

                } else {
                    //dialogInterface.dismiss();
                    if (Configuration.User.getUser_type().equals("0001") || Configuration.User.getUser_type().equals("0004")) {
                        try {

                            SharedPreferences sh1 = getSharedPreferences("savedUserInfo", MODE_PRIVATE);
                            String savedUser = sh1.getString("savedUserInfo", "fail");
                            Log.i("savedUser", savedUser.toString());

                            savedUserInfoStr = savedUser.toString();
                            Log.i("????????????", savedUserInfoStr);
                            if (!savedUserInfoStr.equals("fail")) {
                                savedUserInfoJSonObj = new JSONObject(savedUserInfoStr);

                                for (int ii = 0; ii < allJisaListJsonArray.length(); ii++) {
                                    if (allJisaListJsonArray.getJSONObject(ii).get("car_nm").equals(savedUserInfoJSonObj.get("car_nm")) &&
                                            allJisaListJsonArray.getJSONObject(ii).get("bscode").equals(savedUserInfoJSonObj.get("bscode")) &&
                                            allJisaListJsonArray.getJSONObject(ii).get("bsname").equals(savedUserInfoJSonObj.get("bsname")) &&
                                            allJisaListJsonArray.getJSONObject(ii).get("user_type").equals(savedUserInfoJSonObj.get("user_type"))
                                    ) {
                                        String activity_tag = savedUserInfoJSonObj.get("cur_page").toString();
                                        if (activity_tag.equals("InnerEmployActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, InnerEmployActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        } else if (activity_tag.equals("PatrolMainActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, PatrolMainActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        } else if (activity_tag.equals("TowMainActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, TowMainActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        } else if (activity_tag.equals("MainActivity")) {
                                            SituationService.conf.User.setUser_type(Common.nullCheck(savedUserInfoJSonObj.getString("user_type")));
                                            SituationService.conf.User.getBscode_list().clear();
                                            SituationService.conf.User.getBscode_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bscode")));
                                            SituationService.conf.User.getBsname_list().clear();
                                            SituationService.conf.User.getBsname_list().add(Common.nullCheck(savedUserInfoJSonObj.getString("bsname")));
                                            SituationService.conf.User.setPatcar_id(Common.nullCheck(savedUserInfoJSonObj.getString("patcar_id")));
                                            SituationService.conf.User.setCar_nm(Common.nullCheck(savedUserInfoJSonObj.getString("car_nm")));
                                            VIEW_TAG = VIEW_INNEREMPLOYEE;
                                            Intent i_employee = new Intent(IntroActivity.this, MainActivity.class);
                                            startActivity(i_employee);
                                            finish();
                                            stopGPS();
                                            stopTimer(IntroActivity.this);
                                            return;
                                        }
                                    }
                                }
                            }

                        } catch (NullPointerException dd) {
                            Log.i("savedUser", "??????");
                        } catch (JSONException e) {
                            Log.e("??????", "??????");
                        }
                    }
                    Log.i("savedUser", "-----------------------------------------------------");

                    if (USER_TYPE_INNEREMPLOYEE.equals(userType)) {
                        isSoundAlert = true;
                        gpsListFlag = false;
                        Log.i("USER_TYPE_PATROL", USER_TYPE_PATROL + "");//InnerEmployCCTVFragment
                        final Intent ii = new Intent(context, InnerEmployActivity.class);
                        startActivity(ii);
                        IntroActivity.this.finish();

                    } else if (USER_TYPE_PATROL.equals(userType)) {
                        isSoundAlert = true;
                        gpsListFlag = false;

                        Log.println(Log.ASSERT, TAG, "PatrolMainActivity intent ?????? ????????? userType ??????:" + userType);
                        Intent ii = new Intent(context, PatrolMainActivity.class);
                        startActivity(ii);
                        IntroActivity.this.finish();

                    } else if (USER_TYPE_TOW.equals(userType)) {
                        isSoundAlert = true;
                        gpsListFlag = false;

                        if ("Y".equals(Common.getPrefString(IntroActivity.this, "tow_private"))) {
                            final Intent ii = new Intent(context, TowMainActivity.class);
                            startActivity(ii);
                            IntroActivity.this.finish();

                        } else {
                            final Intent ii = new Intent(context, TowMainActivity.class);

                            Common.setPrefString(IntroActivity.this, "tow_private", "Y");
                            startActivity(ii);
                            IntroActivity.this.finish();
                        }
                    } else {
                        isSoundAlert = false;
                        gpsListFlag = false;
                        SituationService.conf.User.setUser_type(USER_TYPE_CITIZEN);
                        Intent ii = new Intent(context, MainActivity.class);
                        startActivity(ii);
                        IntroActivity.this.finish();
                    }
                }
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(IntroActivity.this, SituationService.class);
        stopService(i);

        finish();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
