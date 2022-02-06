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
		서비스를 포그라운드에 등록시키는 작업 진행*/

    //public static Intent jsonArrayIntent;
    static Intent jsonObjectIntent;
    //최창유 서비스를 죽기위한 boolen
    public static Boolean gpsListFlag = false;

    //공사관리 사용자 타입
    String gongsaUser_Type = "";
    JSONArray allJisaListJsonArray = null;
    JSONObject allJisaListJob = null;


    String savedUserInfoStr = null;
    JSONObject savedUserInfoJSonObj = null;

    String tunnelImp = "";

    //2021.05 디바이스 임의 개조 탐지
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

    //2021.05 디바이스 임의 개조 탐지
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

    //2021.05 디바이스 임의 개조 탐지
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
        Log.println(Log.ASSERT, TAG, "IntroActivity runTimePermission  -  통신순서 확인");
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
            Log.println(Log.ASSERT, TAG, "IntroActivity else 전  -  통신순서 확인");
            checkInitiating();
        }
    }

    //권한 리스트가 전부 허용인지 아닌지에 대한 체크용 메소드. 권한이 하나라도 비허용이면 return false;
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
                if (hasAllPermissionGranted(grantResults)) {//모든 권한이 허용이면 initiating() 호출
                    checkInitiating();

                } else {//권한이 허용이 아니면 앱 종료
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("권한 부여가 거절 되었습니다.\n 앱을종료합니다.").setPositiveButton("확인",
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

            case 3: // 12 권한 목록
                if (!hasAllPermissionGranted(grantResults)) {//모든 권한이 허용이면 initiating() 호출
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("위치 권한을 항상 허용으로 설정해주세요.\n 앱을 종료합니다.").setPositiveButton("확인",
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
                ading.setTitle("위치정보 이용 동의")
                        .setMessage(getString(R.string.location_dialog))
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
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
        Log.println(Log.ASSERT, TAG, "initiating() 시작  -  통신순서 확인");

        if (!chkGpsService(this)) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(IntroActivity.this);
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

        common = new Common(getApplicationContext());
        common.copyFile("exApp.mp4");
        common.DeleteDir(SituationService.conf.directoryName);

        //TODO[JSP Remark] :  modified 2019.07.03
        // 런타임 permission 등록


        db.init();
        db.deleteRptId();
        MainJepbo = false;

        TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);//사용자 전화번호를 가져오기위한 메소드
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
        String[] phoneNo = Common.phoneNumCut(systemService.getLine1Number());//사용자 전화번호 획득
        String mUserTel = phoneNo[0] + "-" + phoneNo[1] + "-" + phoneNo[2];//전화번호 사이에 - 추가
        mUserTel = Common.nullCheck(mUserTel);

        SituationService.conf.USER_PHONE_NUMBER = mUserTel;         // 운영용 배포용 (* 2021운영)
        // SituationService.conf.USER_PHONE_NUMBER = "010-5000-4982";
        // SituationService.conf.USER_PHONE_NUMBER = "010-7688-0352"; // 이내영 사원
        // SituationService.conf.USER_PHONE_NUMBER = "010-5382-8426"; // 권재범 대리

        // TODO[JSPRemark] : modified in 2019.07.03

        startForegroundService();


        SituationService.conf.User.setHp_no(SituationService.conf.USER_PHONE_NUMBER);

        systemService = null;//사용자 전화번호 획득후 null로 메모리 할당 해제
        phoneNo = null;
        mUserTel = null;

        //데이타 초기화..
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

        Log.println(Log.ASSERT, TAG, "initiating() new Action 직전");
        new Action(ONECLICK_NOTICE_SELECT, null).execute("");//통신 호출 ( 최초 로그인 )
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        //2021.05 디바이스 임의 개조 탐지
        checkRootingDevice();
        //stopService(intent);
        Log.println(Log.ASSERT, TAG, "onCreate  -  통신순서 확인");
        if (intent != null) {
            if (intent.getStringExtra("finishAppxx") != null) {

                //Log.println(Log.ASSERT,TAG, "finishApp : "+ intent.getStringExtra("finishApp"));
                if (intent.getStringExtra("finishAppxx").equals("N")) {
                    gpsListFlag = false;
                    //Log.println(Log.ASSERT,TAG,"finishAppxx ----N");
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (isFinishing() == true) {
                                //  Log.println(Log.ASSERT,TAG,"꺼짐");
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
                        Log.e("에러", "예외");
                    }
                }
            } else {
                try {
                    setContentView(R.layout.intro);
                    gpsListFlag = true;

                    AlertDialog.Builder ad = new AlertDialog.Builder(this);
                    ad.setMessage("").setTitle("개인정보 이용 동의").setMessage(getString(R.string.location_term)).setCancelable(false).setPositiveButton("동의", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runTimePermission();
                        }
                    });
                    ad.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Common.setPrefString(IntroActivity.this, "tow_private", "N");

                            onBackPressed();
                        }
                    });
                    ad.show();

                    //Log.println(Log.ASSERT,TAG,"--------------13");
                } catch (NullPointerException e) {
                    Log.e("에러", "익셉션");
                } catch (Exception e) {
                    Log.e("에러", "익셉션");
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
        Log.i(TAG, "루팅 여부 : " + isRootingFlag);
        return isRootingFlag;
    }

    public void getUserInfo() {//신규파람:ONECLICK_GET_USERINFO_SELECT,과거파람ONECLICK_GETUSERINFO_SELECT
        //2016-03-11 기존 사용//
        Parameters params = new Parameters(ONECLICK_GET_USERINFO_SELECT2);
        byte[] encryptBytes = seed.encrypt(SituationService.conf.USER_PHONE_NUMBER, szKey);
        String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes)));
        params.put("hp_no", encString);
        params.put("app_name", Common.PREF_SITUATIONMANAGER);
        params.put("hash_code", "2QUM0REaOZLP99cn9IVbscdwmgE=");//테스트용
        //params.remove("hash_code");
//       params.put("hash_code", seed.renameSpecificChar(getCertData(IntroActivity.this)));//운영용 배포용
        //   Log.i("getUserInfo:","getUserInfo Action 바로 전");
        Log.println(Log.ASSERT, TAG, "getUserInfo  Action execute 바로 전");
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

        try {//테스트용 황수영과장 번호를 파라미터로 넘겼음
            SeedCipher seed = new SeedCipher();
            byte[] szKey = {(byte) 0x88, (byte) 0xE3, (byte) 0x44,
                    (byte) 0x8F, (byte) 0x28, (byte) 0x32, (byte) 0xFE, (byte) 0xF1,
                    (byte) 0xF9, (byte) 0xF3, (byte) 0xF1, (byte) 0x37, (byte) 0xFF,
                    (byte) 0xA4, (byte) 0x05, (byte) 0x29};

            byte[] encryptBytes = seed.encrypt(SituationService.conf.USER_PHONE_NUMBER, szKey);
            String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes)));

            getResult = Common.nullCheck(baseActivity.new Action("get", SERVER_URL + "/gongsaManagementCheck2.do", encString, this).execute("", "", "").get());

            Log.i("getResult", getResult);
            Log.println(Log.ASSERT, TAG, "공사관리" + getResult);

            gongsaUser_Type = Common.nullCheck(new JSONArray(getResult).getJSONObject(0).get("USE_CLSS_CD").toString());

        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.println(Log.ASSERT, TAG, "익셉션:" + getResult);
        }

        return getResult;
    }

    public String getCertData(Context context) {
        String cert = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature certSignature = packageInfo.signatures[0];
            // 인증서지문을 가지고 옴('SHA1'알고리즘)
            MessageDigest msgDigest = MessageDigest.getInstance("SHA1");
            byte[] digest = msgDigest.digest();

            // Base64로 인코딩한 후 값을 반환시킴
            msgDigest.update(certSignature.toByteArray());

            cert = new String(Base64.encodeBase64(msgDigest.digest()));

            return cert;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("에러", "익셉션");
        } catch (NoSuchAlgorithmException e) {
            Log.e("에러", "익셉션");
        }
        return cert;
    }


    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
//		Log.i("프리미티트와 결과값 확인","primitive:"+primitive+", "+"result:"+result.toString());
        // Log.println(Log.ASSERT,TAG,"프리미티트와 결과값 확인"+"primitive:"+primitive+", "+"result:"+result.toString());
        if (e == null) {
            try {
                if (ONECLICK_NOTICE_SELECT.equals(Common.nullCheck(primitive)) == true) {
                    Log.i("onPostAction 메소드:", "if");
                    Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "if");

                    if (null != result) {
                        String title = Common.nullCheck(result.get("title"));
                        String content = Common.nullCheck(result.get("content"));
                        String subcontent = Common.nullCheck(result.get("subcontent"));

                        Log.i("파람 확인 title:", title);
                        Log.i("파람 확인 content:", content);
                        Log.i("파람 확인 subcontent:", subcontent);

                        if (content.length() > 0) {
                            marketDialogtype4(title, content + "\n" + subcontent);
                        } else {
                            Log.i("추적:", "else");
                            getUserInfo();
                        }
                    }

                } else if (ONECLICK_GET_USERINFO_SELECT2.equals(Common.nullCheck(primitive)) == true) {
                    // } else if (ONECLICK_GET_USERINFO_SELECT.equals(Common.nullCheck(primitive)) == true) {
                    Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "eles if");

                    if (null != result) {
                        Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "result != null");
                        String rtnResultCode = Common.nullCheck(result.get("result"));
                        serverAppVersion = Common.nullCheck(result.get("version"));
                        String hashCode = Common.nullCheck(result.get("hash_code"));
                        delay_timer_y = Common.nullCheck(result.get("delay_timer_y"));
                        delay_timer_n = Common.nullCheck(result.get("delay_timer_n"));

                        //통합 DB
                        Log.i("hashCode:", hashCode);
                        if (!"Y".equals(hashCode)) {
                            //  if (false) {
                            Toast.makeText(IntroActivity.this, "위조,변조된 앱일 수 있습니다.\n앱을 종료합니다.", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }

                        if ("1000".equals(rtnResultCode)) {
                            Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "사용자 타입 1000");

                            result.setList("entity");
                            SituationService.conf.User.setGroup_id("");
//							db.updateBBBsCodeClean();

                            for (int i = 0; i < result.size(); i++) {
                                Log.println(Log.ASSERT, TAG, "사용자 타입:" + Common.nullCheck(result.get(i, "user_type")));
                                //공통
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

                                //견인
                                if (USER_TYPE_TOW.equals(SituationService.conf.User.getUser_type())) {
                                    Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "사용자 타입(견인):" + SituationService.conf.User.getUser_type());
                                    SituationService.conf.User.getCrdns_id_list().add(Common.nullCheck(result.get(i, "crdns_id")));
                                    SituationService.conf.User.setReg_post(Common.nullCheck(result.get(i, "reg_post")));
                                    SituationService.conf.User.setReg_part(Common.nullCheck(result.get(i, "reg_part")));
                                    SituationService.conf.User.setReg_name(Common.nullCheck(result.get(i, "reg_name")));
                                    SituationService.conf.User.getRcv_yn_list().add(Common.nullCheck(result.get(i, "rcv_yn")));
                                    SituationService.conf.User.getRcv_bs_list().add(Common.nullCheck(result.get(i, "rcv_bs")));
                                    SituationService.conf.User.setTel_no(Common.nullCheck(result.get(i, "tel_no")));
                                    Log.d("IntroActivity : ", "MainJepbo list= " + result.get(i, "reg_part"));

                                    if (Common.nullCheck(result.get(i, "rcv_yn")).equals("Y")) {
                                        MainJepbo = true;//메인접보지사가 선택되어있는지 안되어있는지 판단.
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
                                    //순찰
                                } else if (USER_TYPE_PATROL.equals(SituationService.conf.User.getUser_type())) {
                                    Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "사용자 타입(순찰):" + SituationService.conf.User.getUser_type());
                                    SituationService.conf.User.setPatcar_id(Common.nullCheck(result.get(i, "patcar_id")));
                                    SituationService.conf.User.setCar_nm(Common.nullCheck(result.get(i, "car_nm")));
                                    Common.setPrefString(IntroActivity.this, "patcar_id", Common.nullCheck(result.get(i, "patcar_id")));
                                    //순찰일경우 지사코드로 사용( bscode ), 견인일 경우 팝업창에서 선택.
                                    BaseActivity.TowJeopBoJisaCode = Common.nullCheck(result.get(i, "bscode"));
                                    db.updateBBBsCode(Common.nullCheck(result.get(i, "bscode")), "Y");

                                    break;
                                }
                                //최창유 주석
                                else if (USER_TYPE_INNEREMPLOYEE.equals(SituationService.conf.User.getUser_type())) {
                                    Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "사용자 타입(내부직원):" + SituationService.conf.User.getUser_type());
                                    SituationService.conf.User.setPatcar_id(Common.nullCheck(result.get(i, "patcar_id")));
                                    SituationService.conf.User.setCar_nm(Common.nullCheck(result.get(i, "car_nm")));
                                    Common.setPrefString(IntroActivity.this, "patcar_id", Common.nullCheck(result.get(i, "patcar_id")));
                                    //순찰일경우 지사코드로 사용( bscode ), 견인일 경우 팝업창에서 선택.
                                    BaseActivity.TowJeopBoJisaCode = Common.nullCheck(result.get(i, "bscode"));
                                    db.updateBBBsCode(Common.nullCheck(result.get(i, "bscode")), "Y");

                                    //2020.12 터널화재pdf
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
                                    //팝업창.
                                    Intent i = new Intent(getApplicationContext(), DialogIntroActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
                                    try {
                                        pi.send();
                                    } catch (PendingIntent.CanceledException ee) {
                                        /*eLog.e("에러","익셉션");*/
                                        Log.e("에러", "예외");
                                    }
                                }
                            }

                            //최창유 추가.
                            try {
                                result.setList("mo");

                                if (Common.nullCheck(result.get("isAllJisaUsedEnabled")).equals("Y")) {
                                    allJisaListJsonArray = new JSONArray();

                                    allJisaListJob = null;
                                    // result.setList("allJisalist");
                                    result.setList("jisa_entity");

                                    for (int i = 0; i < result.size(); i++) {
                                        Log.i("여기 잘오냐", "냐");
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
                                Log.e("에러", "예외");
                            }
                        } else {
                            Toast.makeText(this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            nextActivity(USER_TYPE_CITIZEN, null);
                        }
                    } else {
                        Toast.makeText(this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.println(Log.ASSERT, TAG, "onPostAction 메소드:" + "사용자 타입(대민):" + SituationService.conf.User.getUser_type());
                        nextActivity(USER_TYPE_CITIZEN, null);
                    }
                }
            } catch (JSONException e2) {
                Log.e("에러", "예외");
            } catch (XmlPullParserException xme2) {
                Log.e("에러", "예외");
            }
        } else {
            Toast.makeText(this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            nextActivity(USER_TYPE_CITIZEN, e);
        }
    }


    //사용자 타입에 의한 다음 페이지 이동.

    /**
     * 버전 정보 체크후 Dialog or Next이동
     *
     * @param userType
     */
    public void nextActivity(final String userType, Exception e) {

//		double sVersion = 1.0;
        double sVersion = Common.nullCheckDouble(serverAppVersion);//서버 수신 버전
        double nVersion = Common.nullCheckDouble(Common.getAppVersionName(IntroActivity.this));//단말 보유 버전

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
        } else {//서버 버전 0.0 일 경우.
            if (serverAppVersion.equals("")) {
                Log.d("", "dialog type -1");
                marketDialogtype3();
            } else if (!serverAppVersion.equals("0.0")) {//서버에서 받은 String 형식 버전이 0.0이 아닐 경우.
                Log.d("", "dialog type 1");
                marketDialogtype2();
            } else if (serverAppVersion.equals("")) {//서버에서 받은 String 형식 버전이 없을 경우.
                Log.d("", "dialog type 2");
                if (null != e) {
                    marketDialogtype3();
                } else {
                    marketDialogtype2();
                }
            } else {//예외사항 진행.
                Log.d("", "dialog type 3");
                if (null != e) {
                    marketDialogtype3();
                }
//				marketDialogtype2();
            }
        }
    }

    //마켓 업데이트 유도 업데이트를 하지 않아도 사용 가능
    public void marketDialogType1(final String userType) {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle("앱 업데이트").setMessage("* 고속도로 상황관리 앱이 최신버전이 아닙니다. \n마켓으로 이동 하시겠습니까?").setCancelable(false).
                setPositiveButton("마켓", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goMarket();
                    }
                });
        ading.setNegativeButton("계속진행", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopGPS();
                stopTimer(IntroActivity.this);
                goNext(userType);
            }
        });
        ading.show();
    }

    //마켓 업데이트 유도 업데이트를 해야 앱 사용가능
    public void marketDialogtype2() {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle("앱 업데이트").setMessage("* 고속도로 상황관리 앱 업데이트가 필요합니다. \n마켓으로 이동 하시겠습니까?").setCancelable(false).
                setPositiveButton("마켓", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopGPS();
                        stopTimer(IntroActivity.this);
                        goMarket();
                    }
                });
        ading.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopGPS();
                stopTimer(IntroActivity.this);

                onBackPressed();
            }
        });
        ading.show();
    }

    //서버접속 오류
    public void marketDialogtype3() {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle("고속도로 상황대응").setMessage("* 서버와의 통신이 원활하지 않습니다.").setCancelable(false).
                setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
        ading.show();
    }

    //공지사항
    public void marketDialogtype4(String title, String content) {
        AlertDialog.Builder ading = new AlertDialog.Builder(IntroActivity.this);
        ading.setMessage("").setTitle(title).setMessage(content).setCancelable(false).
                setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getUserInfo();
                    }
                });
        ading.show();
    }


    //마켓 이동.
    public void goMarket() {
        Intent marketLaunch = new Intent(Intent.ACTION_VIEW);

        // 플레이스토어
        marketLaunch.setData(Uri.parse("market://details?id=com.ex.situationmanager"));

        // 원스토어
        // marketLaunch.setData(Uri.parse("onestore://common/product/0000713802"));

        startActivity(marketLaunch);
        finish();
    }


    public void maxCharoChoice(final ArrayList<String> list, final JSONArray jsonArray, final Intent jintent) {


        final Spinner spinner = new Spinner(this);
        //  spinner.setAdapter(new ArrayAdapter<String>(this,R.layout.z_spinner_item,list));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("지사를 선택해주세요.");
        // builder.setView(spinner);
        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    jintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.i("jsonArray확인", jsonArray.toString());
                    Log.i("jsonArray확인", i + "");
                    jintent.putExtra("UserPhoneNo", jsonArray.getJSONObject(i).toString());
                    jintent.putExtra("UserBelongJisaAndBonbu", jsonArray.toString());
                    //jsonArrayIntent=jintent;

                    startActivity(jintent);
                    IntroActivity.this.finish();//공사관리 분기 처리 완료
                    //	SharedPreferences.Editor editor = getSharedPreferences("userInfo",MODE_PRIVATE);
                } catch (Exception e) {
                    Log.e("에러", "익셉션");
                }


            }
        });
 /*       builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });*/
        final int result;


        builder.create();
        builder.show();
      /*  Log.i("리턴 위치",spinner.getSelectedItemPosition()+"");
		return spinner.getSelectedItemPosition();*/
    }


    //버전이 맞을경우 or 버전이 다를경우 취소버튼 눌렷을때
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


        //공사관리 분기 처리 시작지점

        String getResult = getGongsaType();

        Log.println(Log.ASSERT, TAG, "유저타입 체크:" + getResult);
        Log.println(Log.ASSERT, TAG, "유저타입 체크" + getResult);
        Log.println(Log.ASSERT, TAG, "유저타입 체크:" + userType);
        //로직
        //공사관리 사용자 결과가 null이면 원클릭으로 가고
        // 그게 아니면서 사용자가 감독원이면 다이얼로그 띄워줌
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
                    Log.e("에러", "익셉션");
                }


                Log.i("사용자정보가 있는지 확인", getUserInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    JSONArray jsonArray = new JSONArray(getUserInfo);
                    JSONObject jsonObject = new JSONObject();

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.i("jsonArray확인", jsonArray.toString());

                    startActivity(intent);
                    IntroActivity.this.finish();

                } catch (Exception e) {
                    Log.e("에러", "익셉션");
                }

                //gongsaUser_Type.equals("SMARTAPP")
                //}else if(getResult.length() != 0 && !userType.equals("")||Common.nullCheck(new JSONObject(userType).getString("USE_CLSS_CD").toString()).equals("H")){//공사관리 사용자이면서 원클릭 사용자
                //}else if(getResult.length() != 0 && !userType.equals("")){//공사관리 사용자이면서 원클릭 사용자
            } else if ((gongsaUser_Type.equals("H") && getResult.length() != 0) || (getResult.length() != 0 && !userType.equals(""))) {

                final Intent intent = new Intent(this, com.ex.gongsa.view.MainActivity.class);
                final String uType = userType;
                final String gResult = getResult;
                final Context branchContext = this;

                try {
                    oneClickBranch(uType, gResult, intent, branchContext);
                } catch (Exception e) {
                    Log.e("에러", "익셉션");
                }

            } else if ((getResult.length() == 0 && !userType.equals(""))) {//공사관리 사용자가 아니고 원클릭만 사용하는 사람

                Log.i("분기", "1067");
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
                            Log.i("savedUser", "에러");
                        } catch (JSONException e) {
                            Log.e("에러", "예외");
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

                        Log.println(Log.ASSERT, TAG, "PatrolMainActivity intent 직전 에서의 userType 확인:" + uType);
                        Intent i = new Intent(gContext, PatrolMainActivity.class);
                        startActivity(i);
                        IntroActivity.this.finish();
                    } else if (USER_TYPE_TOW.equals(uType)) {//견인사용자
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


                    } else {//대민사용자
                        isSoundAlert = false;
                        gpsListFlag = false;

                        SituationService.conf.User.setUser_type(USER_TYPE_CITIZEN);
                        Intent i = new Intent(gContext, MainActivity.class);
                        startActivity(i);
                        IntroActivity.this.finish();
                    }
                } catch (NullPointerException e) {
                    Log.e("에러", "익셉션");
                }

            }//2021.09 TEST
            /*else {//공사관리 사용자 도 아니면서 대민사용자
                //}else if((gongsaUser_Type.equals("") && userType.equals(""))){
                isSoundAlert = false;
                gpsListFlag = false;
                Log.i("분기", "1131");
                SituationService.conf.User.setUser_type(USER_TYPE_CITIZEN);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                IntroActivity.this.finish();
            }*/
            else {//공사관리 사용자 도 아니면서 대민사용자
                //}else if((gongsaUser_Type.equals("") && userType.equals(""))){
                isSoundAlert = false;
                gpsListFlag = false;
                Log.i("분기", "1131");
                SituationService.conf.User.setUser_type(USER_TYPE_CITIZEN);
                final Context gContext = this;
                final Intent i = new Intent(this, MainActivity.class);

                Common.setPrefString(IntroActivity.this, "admin_private", "Y");
                startActivity(i);
                IntroActivity.this.finish();
            }
        } catch (NullPointerException e) {
            Log.e("에러", "익셉션");
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

    //공사관리와 원클릭 분기
    public void oneClickBranch(final String userType, final String getResult, final Intent intent, final Context context) {
        Log.i("브랜치", "브랜치");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<String> list = new ArrayList<String>();
        list.add("스마트 공사관리");
        list.add("원클릭 상황공유");
        builder.setTitle("사용하실 메뉴를 선택하여 주세요.");
        builder.setCancelable(false);
        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    //dialogInterface.dismiss();
                    isSoundAlert = false;
                    Log.println(Log.ASSERT, TAG, "이푸안에");
                    Log.i("대기", "콜");

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
                        Log.e("에러", "예외");
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        JSONArray jsonArray = new JSONArray(getUserInfo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Log.i("jsonArray확인", jsonArray.toString());

                        startActivity(intent);
                        IntroActivity.this.finish();

                    } catch (JSONException e) {
                        Log.e("에러", "예외");
                    }

                } else {
                    //dialogInterface.dismiss();
                    if (Configuration.User.getUser_type().equals("0001") || Configuration.User.getUser_type().equals("0004")) {
                        try {

                            SharedPreferences sh1 = getSharedPreferences("savedUserInfo", MODE_PRIVATE);
                            String savedUser = sh1.getString("savedUserInfo", "fail");
                            Log.i("savedUser", savedUser.toString());

                            savedUserInfoStr = savedUser.toString();
                            Log.i("확이이잉", savedUserInfoStr);
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
                            Log.i("savedUser", "에러");
                        } catch (JSONException e) {
                            Log.e("에러", "예외");
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

                        Log.println(Log.ASSERT, TAG, "PatrolMainActivity intent 직전 에서의 userType 확인:" + userType);
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
