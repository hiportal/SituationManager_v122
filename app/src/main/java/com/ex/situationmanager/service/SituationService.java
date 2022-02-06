package com.ex.situationmanager.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.ex.situationmanager.BaseActivity;
import com.ex.situationmanager.DialogSituEnd;
import com.ex.situationmanager.DialogUserInfoActivity;
import com.ex.situationmanager.InnerEmployActivity;
import com.ex.situationmanager.IntroActivity;
import com.ex.situationmanager.PatrolMainActivity;
import com.ex.situationmanager.PatrolRegActivity;
import com.ex.situationmanager.R;
import com.ex.situationmanager.TowMainActivity;
import com.ex.situationmanager.BaseActivity.Action;
import com.ex.situationmanager.dto.Patrol;
import com.ex.situationmanager.encrypt.SeedCipher;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.DBAdapter;
import com.ex.situationmanager.util.Image;

import com.ex.situationmanager.util.CustomMultiPartEntity.ProgressListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.DetailedState;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.ex.gongsa.Configuration.SERVER_URL;
//import static com.ex.situationmanager.BaseActivity.VIEW_INNEREMPLOYEE;
//import static com.ex.situationmanager.BaseActivity.VIEW_INNEREMPLOYEE;
import static com.ex.situationmanager.BaseActivity.ONECLICK_EMPLOYEE_GET_INITDATA_SELECT;
import static com.ex.situationmanager.BaseActivity.VIEW_INNEREMPLOYEE;
import static com.ex.situationmanager.IntroActivity.gpsListFlag;
import static com.ex.situationmanager.IntroActivity.isSoundAlert;


public class SituationService extends Service implements View.OnClickListener {


    View quit_Tv;

    //TODO[JSPRemark] : modified in 2019.07.03
    private final IBinder mBinder = new LocalBinder();

    //TODO[JSPRemark] : modified in 2019.07.0
    public class LocalBinder extends Binder {
        public SituationService getService() {
            return SituationService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        //TODO[JSPRemark] : modified in 2019.07.03
        return mBinder;
    }

    //TODO[JSPRemark] : modified in 2019.07.03
    public int setAppIcon() {
        return R.drawable.app_icon;
    }

    public void removeNoti() {
        stopForeground(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  Log.println(Log.ASSERT,TAG, TAG + " onStartCommand Call!!");

        //공사관리 어플을 제외하는 로직


        //TODO[JSPRemark] : modifed in 2019.07.03
        // 서비스 시작 시점에 포그라운드로 전환 된다. startService 는 IntroActivity 실행 시점에 호출된다.
        // 포그라운드로 전환 되면서 노티피케이션을 띄워 앱의 실행을 중지할 수 있다.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Notification notification;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChanel = new NotificationChannel("SITUATION_SERVICE", "원클릭", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChanel.setDescription("channel description");
            notificationChanel.enableLights(true);
            notificationChanel.setLightColor(Color.GREEN);
            notificationChanel.enableVibration(true);
            notificationChanel.setVibrationPattern(new long[]{30, 30, 30});
            notificationChanel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChanel);

            Notification.Builder mBuilder;

            PendingIntent mFinishPendingIntent;

            RemoteViews contentViews = new RemoteViews(getPackageName(), R.layout.notificationview);
            mFinishPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), IntroActivity.class)
                    .putExtra("finishAppxx", "N").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);// PendingIntent.FLAG_UPDATE_CURRENT
            PendingIntent mPendingIntent;
            mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, new Intent(getApplicationContext(), IntroActivity.class)
                    .putExtra("finishApp", "Y").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);//PendingIntent.FLAG_CANCEL_CURRENT


            mBuilder = new Notification.Builder(getBaseContext(), "SITUATION_SERVICE").setContentTitle("title")
                    .setContentText("body").setSmallIcon(setAppIcon()).setAutoCancel(true).setCustomContentView(contentViews);


            contentViews.setImageViewResource(R.id.icon, R.drawable.app_icon);
            SpannableStringBuilder stringBuilder;
            stringBuilder = new SpannableStringBuilder("고속도로 상황대응" + "실행중");
            contentViews.setTextViewText(R.id.appName, stringBuilder);

            contentViews.setOnClickPendingIntent(R.id.vpn_quit_button, mFinishPendingIntent); // 종료
            contentViews.setOnClickPendingIntent(R.id.vpn_disconnet_button, mPendingIntent); // 이동

            mBuilder.setContent(contentViews);
            notification = mBuilder.build();
//				notification = new Notification.Builder(SituationService.this, "SITUATION_SERVICE")
//						.setSmallIcon(setAppIcon())
//						.setContentTitle("title")
//						.setContentText("body")
//						.setAutoCancel(true)
//						.build();
//
//				notification.contentView = contentViews;
//				contentViews.setOnClickPendingIntent(R.id.vpn_quit_button,mFinishPendingIntent);
//				contentViews.setOnClickPendingIntent(R.id.vpn_disconnet_button,mFinishPendingIntent);
//				//contentViews.addView(R.id.vpn_disconnet_button,contentViews);
//				notification.contentView = contentViews;
            startForeground(1, notification);

//

        } else {

            Notification notification;
            PendingIntent mPendingIntent;
            mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1,
                    new Intent(getApplicationContext(), IntroActivity.class).putExtra("finishApp", "Y").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT);


            PendingIntent mFinishPendingIntent;
            mFinishPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    new Intent(getApplicationContext(), IntroActivity.class).putExtra("finishAppxx", "N").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notificationview);

            contentView.setImageViewResource(R.id.icon, R.drawable.app_icon);
            SpannableStringBuilder stringBuilder;
            stringBuilder = new SpannableStringBuilder("모바일오피스" + " - 업무망 연결됨");

            contentView.setOnClickPendingIntent(R.id.vpn_disconnet_button, mPendingIntent);
            contentView.setOnClickPendingIntent(R.id.vpn_quit_button, mFinishPendingIntent);

            notification = new Notification.Builder(SituationService.this)
                    .setSmallIcon(setAppIcon())
                    .setContentTitle("title")
                    .setContentText("body").setAutoCancel(true)
                    .build();

            notification.contentView = contentView;
            startForeground(1, notification);

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            //


        }

        //return START_REDELIVER_INTENT;

        return START_NOT_STICKY;
    }//method


    //서비스시작
    public static void actionStart(Context ctx) {
        gpsDialogFlag = true;
        Common.setPrefString(ctx, "start_yn", "Y");
        start_yn = "Y";
    }

    //서비스 종료
    public static void actionStop(Context ctx) {
        gpsDialogFlag = false;
        Common.setPrefString(ctx, "start_yn", "N");
        start_yn = "N";

    }

    //서비스 재시작
    public static void setDelaytimer(int Y, int N) {
        TIMER_DELAY_Y = Y;
        TIMER_DELAY_N = N;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vpn_quit_button:
                //android.os.Process.killProcess(android.os.Process.myPid());
                Toast.makeText(view.getContext(), "종료버튼", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String TAG = "SituationService";
    public static String VIEW_TAG = "";
    public static String VIEW_PATROL = "PatrolMainActivity";

    public static String VIEW_TOW = "TowMainActivity";

    public static ArrayList<Patrol> itemList;

    public static Configuration conf = new Configuration();

    public static boolean gpsDialogFlag = true;
    SeedCipher seed = new SeedCipher();
    public static byte[] szKey = {(byte) 0x88, (byte) 0xE3, (byte) 0x44,
            (byte) 0x8F, (byte) 0x28, (byte) 0x32, (byte) 0xFE, (byte) 0xF1,
            (byte) 0xF9, (byte) 0xF3, (byte) 0xF1, (byte) 0x37, (byte) 0xFF,
            (byte) 0xA4, (byte) 0x05, (byte) 0x29};

    // 사용자정보조회
    public static String ONECLICK_GETUSERINFO_SELECT = "ONECLICK_GETUSERINFO_SELECT";
    // gps좌표+각종정보 발송.
    public static String ONECLICK_CARGPS_INSERT = "ONECLICK_CARGPS_INSERT";
    // 순찰,견인 처리완료.
    public static String ONECLICK_WORKCOMPLETE_UPDATE = "ONECLICK_WORKCOMPLETE_UPDATE";
    // 순찰 조치 목록 조회
    public static String ONECLICK_PATROLRCEPTINFO_SELECT = "ONECLICK_PATROLRCEPTINFO_SELECT";
    //변경
    public static String ONECLICK_PATROL_RCEPTINFO_SELECT2 = "ONECLICK_PATROL_RCEPTINFO_SELECT2";
    // 순찰 조치 목록 등록
    public static String ONECLICK_ACDNTACT_INSERT = "ONECLICK_ACDNTACT_INSERT";
    // 순찰 상세화면 목록 조회.
    public static String ONECLICK_GETACDNTACTINFO_SELECT = "ONECLICK_GETACDNTACTINFO_SELECT";
    // 견인 목록 조회
    public static String ONECLICK_TOWRCEPTINFO_SELECT = "ONECLICK_TOWRCEPTINFO_SELECT";
    // 파일전송.
    public static String ONECLICK_FILE_SEND = "ONECLICK_FILE_SEND";

    // 주 접보지사 업데이트.
    public static String ONECLICK_MAINRCVBS_UPDATE = "ONECLICK_MAINRCVBS_UPDATE";
    // 접보받을 지사 목록 업데이트.
    public static String ONECLICK_RCVJISALIST_UPDATE = "ONECLICK_RCVJISALIST_UPDATE";
    // 대국민 접보 등록.
    public static String ONECLICK_CITIZENRCEPIT_INSERT = "ONECLICK_CITIZENRCEPIT_INSERT";
    // 순찰차 전화번호 조회.
    public static String ONECLICK_GETPATROLTELNO_SELECT = "ONECLICK_GETPATROLTELNO_SELECT";

    //순찰차가 내용을 확인했다는 primitive
    public static String ONECLICK_PATROLCONFRIM_UPDATE = "ONECLICK_PATROLCONFRIM_UPDATE";

    //public static String URL_SENDGPS = "http://192.168.1.24:8080/proxy.jsp"; 	//로컬테스트(최창유 로컬)
    //public static String URL_SENDGPS = "http://192.168.10.3:8080/proxy.jsp"; 	//로컬테스트(과장님 로컬 로컬)
    //--public static String URL_SENDGPS = "http://oneclickapp.ex.co.kr:5000/proxy.jsp";    // 신규운영(주석처리 최창유: 배포시 열어야댐)
    public static String URL_SENDGPS = "https://oneclickapp.ex.co.kr:9443/proxy.jsp";    // SSL 적용 운영
//	public static String URL_SENDGPS = "http://oneclickmobile.ex.co.kr:8080/proxy.jsp";	// 기존운영


    public Uri mUriSet;

    Handler fileHandler;

    Common common = null;
    DBAdapter db = new DBAdapter();

    //DialogDirect 에서 선택된 정보.
    public static String move_direct = "";//S or E

    private static final int GPS_INTENT = 6;
    // 사용자 구분
    public static String USER_TYPE_PATROL = "0001";// 순찰자
    public static String USER_TYPE_TOW = "0002";// 견인자
    public static String USER_TYPE_CITIZEN = "0003";// 대국민
    public static String USER_TYPE_INNEREMPLOYEE = "0004";// 내부직원
    //public static String USER_TYPE = USER_TYPE_PATROL;
    public static String USER_TYPE = "";
    //public static String USER_TYPE = USER_TYPE_INNEREMPLOYEE;
    //2020.12 터널화재pdf 다운로드
    public static String tunnelImp = "";


    // 주 접보지사정보
    public static String TowJeopBoJisaCode = "";
    public static String TowJeppBoJisaName = "";

    // 선택된 상황일지 정보.
    public static String selectedRpt_id = "";
    //	public static String rpt_local_way = "";
//	public static String rpt_start_km = "";
    public static String rpt_bscode = "";
    public static String rpt_bhPkCode = "";
    public static String rpt_reg_type = "";
    public static String rpt_latitude = "";
    public static String rpt_longitude = "";
    public static String rpt_endtime = "";
    public static String rpt_inp_val = "";

    public static String patrolOKFlag = "N";

    public static String start_yn = "";

    public static String ok = "";

    public static int TIMER_DELAY_Y = 1000;
    /*public static int TIMER_DELAY_N = 60000;*/

    //실험
    public static int TIMER_DELAY_N = 15000;

    public static int TIMER_DELAY_CNT = 0;


    static boolean isPlayFlag = true;


    @Override
    public void onCreate() {
        super.onCreate();

        //202007
//        if ("Y".equals(Common.getPrefString(SituationService.this, "start_yn"))) {
//            selectedRpt_id = Common.getPrefString(SituationService.this, "selectedRpt_id");
//
//        }

//        if (PatrolMainActivity.getSetSelectJubboStart == "Y") {
//            selectedRpt_id = PatrolMainActivity.setFixSelectJubbo;
//        }

        //최창유 주석
     /*   if( !Configuration.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)){
            Log.println(Log.ASSERT,TAG,"service if - onCreate");
            //Toast.makeText(getApplicationContext(),"여기서 바뀜"+"이전 유저타입:"+USER_TYPE+",새 유저타입:"+USER_TYPE_INNEREMPLOYEE,Toast.LENGTH_LONG).show();
            USER_TYPE = USER_TYPE_INNEREMPLOYEE;
        }*/

        Log.d(TAG, TAG + " onCreate call!!!");
        db.close();
        db.init();
        stopGPS();
        startMyGps();
        stopTimer(SituationService.this);
        startTimer();
        stopTimer_list();
        startTimer_list();
        ConnectivityManager cmn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cmn.getActiveNetworkInfo();


        Log.d("", "networkInfo status = " + info.getState());
        Log.d("", "networkInfo status = " + info.getType());
        Log.d("", "networkInfo status = " + info.getTypeName());
        Log.d("", "networkInfo status = " + info.getReason());
        Log.d("", "networkInfo status = " + info.getDetailedState());

        PatrolMainActivity.setFixSelectJubbo = "";

    }


    @Override
    public void onDestroy() {
//		new MobiusNotification(SituationService.this, "원클릭 사고제보", "SituationService onDestroy", "").show();
        Log.d(TAG, TAG + " onDestroy call!!!");
        Log.println(Log.ASSERT, TAG, "onDestroy");
        stopForeground(true);

        super.onDestroy();

    }


    @SuppressLint("NewApi")
    @Override
    public void onTaskRemoved(Intent rootIntent) {
//		new MobiusNotification(SituationService.this, "원클릭 사고제보", "SituationService onTaskRemoved", "").show();
        super.onTaskRemoved(rootIntent);

        //출동 취소 처리
        if ("Y".equals(Common.getPrefString(PatrolMainActivity.contextActivity, "getSetSelectJubboStart"))) {
            sendStop();
//            start_yn = "";
            //202007_
            PatrolMainActivity.setFixSelectJubbo = "";
            //PatrolMainActivity.getSetSelectJubboStart = "";
            Common.setPrefString(PatrolMainActivity.contextActivity, "getSetSelectJubboStart", "");

            Log.i("ss493,어플상태", "출동취소");
        }
        Log.println(Log.ASSERT, TAG, TAG + " onTaskRemoved call!!!");
        Log.i("ss493,어플상태", "출동취소2");
        //TODO[JSPRemark] : modified in 2019.07.03
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
            stopSelf();
            Log.i("ss493,어플상태", "출동취소3");
        }
//	    android.os.Process.killProcess(android.os.Process.myPid());
        Log.i("ss493,어플상태", "출동취소4");
//        stopTimer(SituationService.this);
        //20207_
//        start_yn = "";
//        PatrolMainActivity.setFixGetJubbo = "";
//        PatrolMainActivity.setFixSelectJubbo = "";


    }

    public void sendStop() {

        move_direct = "";//DialogDirection 에서 설정한 데이터 초기화
//202007_
        Common.setPrefString(SituationService.this, "start_yn", "");
        selectedRpt_id = "";
//		firstFlag = false;
        timerCnt = 0;
        Parameters params = new Parameters(ONECLICK_CARGPS_INSERT);

        params.put("user_type", Configuration.User.getUser_type());// 차량관리번호

        params.put("car_id", SituationService.conf.User.getPatcar_id());// 순찰차량 일련번호.
        params.put("rpt_id", SituationService.selectedRpt_id);// 상황일지

        //암호화
        byte[] encryptBytes_lo = seed.encrypt(longitude + "", szKey);
        String encString_lo = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo)));
        byte[] encryptBytes_la = seed.encrypt(latitude + "", szKey);
        String encString_la = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la)));


        byte[] encryptBytes_sp = seed.encrypt(speed + "", szKey);
        String encString_sp = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_sp)));

        params.put("car_gps_lo", encString_lo + "");// 경도
        params.put("car_gps_la", encString_la + "");// 위도
        try {
            altitude = Math.round(altitude * 100f) / 100f;
        } catch (NullPointerException | NumberFormatException e) {
            altitude = 0.0;
            Log.e("에러", "NullPointerException | NumberFormatException ");
        } finally {
            byte[] encryptBytes_hg = seed.encrypt(altitude + "", szKey);
            String encString_hg = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_hg)));
            params.put("car_gps_hg", encString_hg + "");// 고도
        }
        params.put("car_gps_sp", encString_sp + "");// 속도
        try {
            angle = Math.round(angle * 1000000000d) / 1000000000d;
        } catch (NullPointerException | NumberFormatException e) {
            Log.e("에러", "NullPointerException | NumberFormatException ");
        } finally {
            byte[] encryptBytes_ag = seed.encrypt(angle + "", szKey);
            String encString_ag = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_ag)));
            params.put("car_gps_ag", encString_ag + "");// 이동각도
        }
        params.put("start_yn", Common.getPrefString(SituationService.this, "start_yn"));// 출동여부 (y or n)
        params.put("car_way", "" + ns_code);// 차량 노선 코드
        params.put("car_direct", "");// 차량 방향 pk코드
        if (null != gonggu && !"".equals(gonggu)) {
            params.put("car_gonggu", gonggu.substring(0, 1) + "");// 공구명
        } else {
            params.put("car_gonggu", gonggu + "");// 공구명
        }
        params.put("car_sisul", sisul + "");// IC,JCT
        params.put("car_ijung", currentIjung + "");// IC,JCT,휴게소,..및 시설물
        params.put("ok", Common.getPrefString(SituationService.this, "patrolOKFlag") + "");// 이상없은 상태
        params.put("STOP", "STOP");//sendstop
        params.put("canceled", "Y");


        new Action(ONECLICK_CARGPS_INSERT, params).execute("");
    }

    /**
     * 순찰,견인 목록 조회 타이머 5초단위 실행
     */
    static Timer listTimer = new Timer();
    public static int listTimerDelay = 15000;//default 5초
    Handler listHandler = new Handler();
    //목록 통신 동기화를 위함.
    boolean listFlag = true;


    public void startTimer_list() {

        TimerTask timertask = new TimerTask() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (listFlag == false) {
                        Log.println(Log.ASSERT, TAG, "service return");
                        return;
                    }
                    Log.d("", "Delaytimer check " + TIMER_DELAY_N);
                    Log.d("", "conf.User.getUser_type() " + conf.User.getUser_type());

                    Log.println(Log.ASSERT, TAG, VIEW_TAG);
                    Log.println(Log.ASSERT, TAG, VIEW_TAG);
                    Log.println(Log.ASSERT, TAG, conf.User.getUser_type());

                    if ((conf.User.getUser_type().equals(USER_TYPE_PATROL) || (conf.User.getUser_type().equals("0004")) && VIEW_TAG.equals(VIEW_INNEREMPLOYEE))) {

                        Log.println(Log.ASSERT, TAG, "if");
                        Log.println(Log.ASSERT, TAG, "service if - startTimer_list if");
                        Parameters params = new Parameters(ONECLICK_PATROLRCEPTINFO_SELECT);
                        //Parameters params = new Parameters(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT);
                        if (conf.User.getBscode_list() != null && conf.User.getBscode_list().size() > 0) {
                            Log.println(Log.ASSERT, TAG, "두번째 if 안에");
                            params.put("bscode", conf.User.getBscode_list().get(0));//운영
//							params.put("bscode", "B0000");//테스트 test
                            params.put("sms_group", conf.User.getGroup_id());
                            Log.d("sms_group", "SituationService sms_group = " + conf.User.getGroup_id());
                            try {
                                nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                            } catch (NullPointerException e) {
                                Log.e("에러", "예외");
                            } catch (Exception e) {
                                Log.e("에러", "예외");
                            }

                            // params.put("update_cartime", nowUPT_CARTIME);
                            // params.put("reg_date", nowREG_DATE);
                            byte[] hp_noByte = seed.encrypt(conf.USER_PHONE_NUMBER + "", szKey);
                            String enchp_noByte = seed.renameSpecificChar(new String(Base64.encodeBase64(hp_noByte)));
                            params.put("hp_no", enchp_noByte);

                        }
                        Log.println(Log.ASSERT, TAG, "service 583");
                        if (listFlag == true) {
                            listFlag = false;

                            // new ActionList(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT, params).execute("");
                            Log.i("940412", "2");
                            new ActionList(ONECLICK_PATROLRCEPTINFO_SELECT, params).execute("");


                        }
                    } else if (conf.User.getUser_type().equals(USER_TYPE_TOW)) {
                        Parameters params = new Parameters(ONECLICK_TOWRCEPTINFO_SELECT);
                        // params.put("sms_group", conf.User.getGroup_id());

                        if (conf.User.getBscode_list() != null && conf.User.getBscode_list().size() > 0) {
                            String rtnStr = db.fetchBBBsCodeSelected();
                            params.put("bscode", rtnStr);
                            try {
                                nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                            } catch (NullPointerException e) {
                                Log.e("에러", "예외");
                            } catch (Exception e) {
                                Log.e("에러", "예외");
                            }

                            // params.put("update_cartime", nowUPT_CARTIME);
                            // params.put("reg_date", nowREG_DATE);
                            byte[] hp_noByte = seed.encrypt(conf.USER_PHONE_NUMBER + "", szKey);
                            String enchp_noByte = seed.renameSpecificChar(new String(Base64.encodeBase64(hp_noByte)));
                            params.put("hp_no", enchp_noByte);
                        }

                        String car_id = "";

                        for (int i = 0; i < Configuration.User.getCrdns_id_list().size(); i++) {
                                if (i == 0) {
                                car_id = Configuration.User.getCrdns_id_list().get(i);
                            } else {
                                car_id += "|" + Configuration.User.getCrdns_id_list().get(i);
                            }
                        }

                        params.put("car_id", car_id);
                        if (listFlag == true) {
                            listFlag = false;
                            Log.i("토우 전송전", "----------");


                            new ActionList(ONECLICK_TOWRCEPTINFO_SELECT, params).execute("");

                        }
                    } /* else  if (conf.User.getUser_type().equals(USER_TYPE_INNEREMPLOYEE)) {
                        Parameters params = new Parameters(ONECLICK_PATROLRCEPTINFO_SELECT);
                        if (conf.User.getBscode_list() != null && conf.User.getBscode_list().size() > 0) {
                            params.put("bscode", conf.User.getBscode_list().get(0));//운영
//							params.put("bscode", "B0000");//테스트 test
                            params.put("sms_group", conf.User.getGroup_id());
                            Log.d("sms_group", "SituationService sms_group = " + conf.User.getGroup_id());
                            try {
                                nowUPT_CARTIME = nowUPT_CARTIME.replace(" ", "").replace(":", "").replace("-", "");
                                nowREG_DATE = nowREG_DATE.replace(" ", "").replace(":", "").replace("-", "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            params.put("update_cartime", nowUPT_CARTIME);
                            params.put("reg_date", nowREG_DATE);
                            byte[] hp_noByte = seed.encrypt(conf.USER_PHONE_NUMBER + "", szKey);
                            String enchp_noByte = seed.renameSpecificChar(new String(Base64.encodeBase64(hp_noByte)));
                            params.put("hp_no", enchp_noByte);

                        }
                        if (listFlag == true) {
                            listFlag = false;
                            new ActionList(ONECLICK_PATROLRCEPTINFO_SELECT, params).execute("");


                        }
                    }*/
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

    public void stopTimer_list() {
        if (null != listTimer) {
            listTimer.cancel();
        }
    }

    // 업데이트 시간 비교
    public String beforeUPT_RPTID = "";
    public String beforeUPT_CARTIME = "";
    public String beforeUse_yn = "";
    public String beforeEnd_yn = "";
    public static String nowUPT_RPTID = "";
    public static String nowUPT_CARTIME = "";
    public String nowUse_yn = "";
    public String nowEnd_yn = "";
//		public static String nowUpperRPTID = "";

    public static String nowREG_DATE = "";

    boolean playFlag = true;
    public static Patrol ppastItem = new Patrol();
    public static Patrol pastItem = new Patrol();


    public Map<String, String> lastStartCountMap = new HashMap<String, String>();
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public class ActionList extends AsyncTask<String, Void, XMLData> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
        ProgressDialog progressDialog;

        String primitive = "";
        Parameters params = null;

        XMLData returnData = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        // primitive 에 따라 URL을 구분짓는다.
        public ActionList(String primitive, Parameters params) {
            this.primitive = primitive;
            this.params = params;
        }

        @Override
        protected XMLData doInBackground(String... arg0) {
            Log.i("최11창유", "ㅠㅅ ㅠ");
            Log.println(Log.ASSERT, TAG, "doInBackGround");
            HttpURLConnection conn = null;
            XMLData xmlData = null;

            OutputStream os = null;
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            try {
                StringBuffer body = new StringBuffer();
                if (ONECLICK_CARGPS_INSERT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT

                } else if (ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
                    //} else if (ONECLICK_EMPLOYEE_GET_INITDATA_SELECT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;
                    Log.i(";12l3jsdlfjsdlf", body.toString());

                    Log.i("", TAG + " " + primitive + "synchronized start ");
                } else if (ONECLICK_TOWRCEPTINFO_SELECT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    listFlag = false;
                    Log.i("", TAG + " " + primitive + "synchronized start ");
                }
                URL url = new URL(new String(Common.nullTrim(body.toString()).getBytes("EUC-KR"), "Cp1252"));
                // URL url = new URL(new String(body.toString()));
                Log.println(Log.ASSERT, TAG, "URL : = " + body.toString());
                if (primitive.equals("ONECLICK_PATROLRCEPTINFO_SELECT")) {
                    Log.i("12312312", "UdddddRL : = " + body.toString());
                }


                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {

                        return null;

                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {

                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {

                    }

                }};


                SSLContext sc = SSLContext.getInstance("SSL");

                sc.init(null, trustAllCerts, new SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setRequestProperty("Cache-Control", "no-cache");
                // conn.setDoOutput(true);
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
//					Log.d(TAG, TAG + " ACTION responsecode  " + responseCode+ "----" + conn.getResponseMessage());
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
                        Log.i("", TAG + " " + primitive + "synchronized end ");
                    } catch (Exception e) {
                        listFlag = true;
                        Log.i("", TAG + " " + primitive + "synchronized end ");
                    }
                }

            } catch (IOException e) {
                listFlag = true;
                Log.i("", TAG + " " + primitive + "synchronized end ");
            } catch (NoSuchAlgorithmException e) {
                Log.e("에러", "예외");
            } catch (KeyManagementException e) {
                Log.e("예외", "예외발생");
            } catch (Exception e) {
                Log.e("예외", "예외발생");
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        Log.e("에러", "예외");
                    }
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.e("에러", "예외");
                    } catch (Exception e) {
                        Log.e("에러", "예외");
                    }
                }

                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        Log.e("에러", "예외");
                    } catch (Exception e) {
                        Log.e("에러", "예외");
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

                if ("1000".equals(rtnResultCode)) {
                    Log.i("최11창유", "onPostExecute");
                    pastItem = new Patrol();
                    nowUPT_CARTIME = Common.nullCheck(result.get("update_cartime"));
                    nowUPT_RPTID = Common.nullCheck(result.get("update_rpt_id"));
                    Log.d("", "isplay  nowUPT_RPTID = " + nowUPT_RPTID + ":" + nowUPT_CARTIME);
                    pastItem.setRpt_id(nowUPT_RPTID);
                    pastItem.setUpdate_cartime(nowUPT_CARTIME);


                    //--------------

                    //------
                    if (primitive.equals(ONECLICK_PATROLRCEPTINFO_SELECT) || primitive.equals(ONECLICK_TOWRCEPTINFO_SELECT)/*||primitive.equals(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT)*/) {
                        //   Log.i("냐아아앙",rtnResultCode);
                        Log.i("최11창유", "onPostExecute--925");
                        Log.d(TAG, TAG + "list of resultCode = " + rtnResultCode);
                        Log.i("", TAG + " " + primitive + "synchronized end ");
                        result.setList("entity");
                        Patrol item = null;

                        Map<String, String> startCountMap = new HashMap<String, String>();
                        if (itemList != null) {
                            itemList.clear();
                        }

                        itemList = new ArrayList<Patrol>();
                        //Log.i("냐아아앙",result.size()+"");
                        for (int i = 0; i < result.size(); i++) {
                            //  Log.println(Log.ASSERT,TAG,"냐냐냐냐냐냐냐냐냐냐냐냐");

                       /*     Log.println(Log.ASSERT,TAG, Common.nullCheck(result.get(i,"roadNo").toString()));
                            Log.println(Log.ASSERT,TAG, Common.nullCheck(result.get(i,"start_km").toString()));
                            Log.println(Log.ASSERT,TAG, Common.nullCheck(result.get(i,"end_km").toString()));*/
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
                            String roadNo = Common.nullCheck(result.get(i, "roadNo"));
                            String end_km = Common.nullCheck(result.get(i, "end_km"));
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
                            item.setRoadNo(roadNo);
                            item.setEnd_km(end_km);
                            Log.i("냐아아앙", item.toString());
                            startCountMap.put(rpt_id, startcount);

                            //   Log.d("", "112312312312  === " + nowUPT_RPTID + ":" + rpt_id + ": endyn" + end_yn);
                            if (nowUPT_RPTID.equals(rpt_id)) {
                                nowREG_DATE = Common.nullCheck(item.getReg_date());
//									nowUpperRPTID = rpt_id;
                                pastItem.setRpt_id(rpt_id);
                                pastItem.setEnd_time(end_time);
                                pastItem.setEnd_yn(end_yn);
                                pastItem.setUse_yn(use_yn);
                                pastItem.setReg_type(reg_type);
                                Log.d("", "where are you 1");
                            } else {
                                Log.d("", "where are you 1 else");
                            }

                            if ("Y".equals(use_yn)) {

                                if (rpt_id.equals(nowUPT_RPTID)) {
                                    pastItem.setRpt_id(rpt_id);
                                    pastItem.setEnd_time(end_time);
                                    pastItem.setEnd_yn(end_yn);
                                    pastItem.setUse_yn(use_yn);
                                    pastItem.setReg_type(reg_type);
                                    Log.d("", "where are you 2");
                                }
                                itemList.add(item);
                            }
                        }


                        Log.i("최11창유", "onPostExecute--1022");
                        Log.d(TAG, TAG + "mp3Start regType  = " + pastItem.getReg_type());

                        isPlay();
                    }

                } else if ("1001".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_PATROLRCEPTINFO_SELECT) || primitive.equals(ONECLICK_TOWRCEPTINFO_SELECT)) {
                        Log.d(TAG, TAG + "list of resultCode = " + rtnResultCode);
                        Log.d(TAG, TAG + "mpeStart rptid  = " + pastItem.getRpt_id());
                        nowUPT_CARTIME = Common.nullCheck(result.get("date"));

                        if (db.selectRptId(pastItem.getRpt_id()) == false) {//확인된 데이타가 없는 경우 음성 재생
                            if ("Y".equals(pastItem.getUse_yn())) {
                                Log.d("", "SituationService mp3Start type 3");

                                mp3Start(pastItem.getReg_type(), pastItem);
                            }
                        }
                    }
                }

            } catch (XmlPullParserException | NullPointerException e) {
                e.printStackTrace();
                Log.e("에러", "예외");
            } finally {
                listFlag = true;
            }
        }
    }

    public void isPlay() {

        Log.d("", "여긴 걸리누?");
        Log.println(Log.ASSERT, "isPlay", "여긴 걸리누?");

        if (isPlayFlag == false)
            return;

        Log.println(Log.ASSERT, "isPlay", "여긴 걸리누?");
        Log.println(Log.ASSERT, "isPlay", "여긴 걸리누?");
        Log.println(Log.ASSERT, "isPlay", "여긴 걸리누?");
        Log.println(Log.ASSERT, "isPlay", "여긴 걸리누?");
        Log.println(Log.ASSERT, "isPlay", "여긴 걸리누?");

        if (isSoundAlert == false)
            return;

        Log.d("", "이것만 보면 된다 !! = " + "" + beforeUPT_CARTIME + "====" + nowUPT_CARTIME);
        Log.println(Log.ASSERT, "isPlay", "최창유:이것만 보면 된다 !! = " + "" + beforeUPT_CARTIME + "====" + nowUPT_CARTIME);
        // Toast.makeText(this.getApplicationContext(),"isPlay",Toast.LENGTH_LONG).show();

        if (!beforeUPT_CARTIME.equals(nowUPT_CARTIME) || "".equals(Common.nullCheck(nowUPT_CARTIME))) { //update_cartime 이 동일하지 않을 경우. 새로고침+음성
            ppastItem = pastItem;
            beforeUPT_CARTIME = nowUPT_CARTIME;

            if ("N".equals(pastItem.getEnd_yn())) {
                Log.d("", "SituationService mp3Start type 4");

                mp3Start(pastItem.getReg_type(), pastItem);
                return;
            }
        } else {

            if (USER_TYPE_PATROL.equals(USER_TYPE)) {
                if (db.selectRptId(pastItem.getRpt_id()) == false) {//확인된 데이타가 없는 경우 음성 재생
                    Log.d("", "SituationService mp3Start type 5");

                    mp3Start(pastItem.getReg_type(), pastItem);
                    return;
                }
            }
        }


        if (!ppastItem.getRpt_id().equals(pastItem.getRpt_id())) {
            ppastItem = pastItem;
            beforeUPT_CARTIME = nowUPT_CARTIME;

            if ("N".equals(pastItem.getEnd_yn())) {
                Log.d("", "SituationService mp3Start type 6");

                mp3Start(pastItem.getReg_type(), pastItem);
                return;
            }
        }

        if (USER_TYPE_TOW.equals(USER_TYPE)) {
            db.insertRptId(pastItem.getRpt_id(), pastItem.getReg_date());
        }

        ppastItem = pastItem;
    }

    MediaPlayer player;
    static boolean popupFlag = false;

    // TimerTask ttm;
    public void mp3Start(String code, Patrol item) {

        Log.d("", "SituationService mp3Start code = " + code + ": rpt_id=" + item.getRpt_id());
        //String name = "etc.mp3";
        String name = "";
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
            Log.e("", TAG + " ---- 9999");
//            selectedRpt_id = "";
//            start_yn = "N";
//            Common.setPrefString(SituationService.this, "start_yn", "N");
            //202007
            if ("N".equals(Common.getPrefString(PatrolMainActivity.contextActivity, "getSetSelectJubboStart"))) {
                if (popupFlag == false) {
                    Calendar c = Calendar.getInstance();
                    String date = "";
                    try {
                        Date nowDate = transFormat.parse(rpt_endtime);
                        c.setTime(nowDate);
                        date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
                    } catch (ParseException e) {
                        Log.e("에러", "예외");
                        try {
                            date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
                        } catch (NullPointerException e2) {
                            Log.e("에러", "예외");
                        }
                    }

                    LayoutInflater inflater = (LayoutInflater) SituationService.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    inflater.inflate(R.layout.userinfo, null);


                    //202007

                    Intent i = new Intent(SituationService.this, DialogSituEnd.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("message", "상황명: " + rpt_inp_val + "\n" + date + "분 상황이 종료 되었습니다.");
                    PendingIntent pi = PendingIntent.getActivity(SituationService.this, 0, i, PendingIntent.FLAG_ONE_SHOT);

                    //202007 ( pi를 위에다가 선언할지 안에다가 선언할지 해보기
                    Log.i("0909090", "확인");
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e) {
                        Log.e("에러", "예외");
                    }

                    name = "acdntend.mp3";
                    mp3SoundStatChoice(name);

                    Log.i("9999", "Situation");
                    selectedRpt_id = "";
                    start_yn = "N";
                    Common.setPrefString(PatrolMainActivity.contextActivity, "getSetSelectJubboStart", "");
                    Common.setPrefString(SituationService.this, "start_yn", "N");
                }
            }
            return;

        } else if ("9998".equals(code)) {//긴급 견인 상황에서 다른 견인차가 견인완료 했을경우.
            Log.e("", TAG + " ---- 9998");
            SituationService.selectedRpt_id = "";
            SituationService.start_yn = "N";
            Common.setPrefString(SituationService.this, "start_yn", "N");

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            String date = "";
            try {
                Date nowDate = transFormat.parse(rpt_endtime);
                c.setTime(nowDate);
                date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
            } catch (ParseException e) {
                Log.e("에러", "예외");
                try {
                    date = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE));
                } catch (NullPointerException e2) {
                    Log.e("에러", "예외");
                }
            }

            LayoutInflater inflater = (LayoutInflater) SituationService.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.userinfo, null);

            Intent i = new Intent(SituationService.this, DialogSituEnd.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("message", "상황명: " + rpt_inp_val + "\n" + date + "분 견인이 완료 되었 습니다.");
            PendingIntent pi = PendingIntent.getActivity(SituationService.this, 0, i, PendingIntent.FLAG_ONE_SHOT);
            try {
                pi.send();
            } catch (PendingIntent.CanceledException e) {
                Log.e("에러", "PendingIntent.CanceledExceptio");
            } catch (Exception e) {
                Log.e("에러", "PendingIntent.CanceledExceptio");
            }

            name = "towcomplete.mp3";
            mp3SoundStatChoice(name);

            return;
        }

        mp3SoundStatChoice(name);

    }

    static SoundPool sPool;
    int[] sound;

    //진동, 무음, 소리 상태에 따른 분기.
    private void mp3SoundStatChoice(String name) {
        Log.d("", "mp3SoundStatChoice 1");
        //기타접보 지속발생 제거. 차단작업 음성 발생 제거
        if ("etc.mp3".equals(name) || "block.mp3".equals(name)) {
            return;
        }
        if (conf.User.getUser_type().equals(USER_TYPE_TOW)) {//(견인)
            if ("0005".equals(Common.nullCheck(pastItem.getReg_type()))
                    || "0007".equals(Common.nullCheck(pastItem.getReg_type()))
                    || "0012".equals(Common.nullCheck(pastItem.getReg_type()))
                    || "0013".equals(Common.nullCheck(pastItem.getReg_type()))) {
            } else {
                return;
            }
        }
        Log.d("", "mp3SoundStatChoice 2");
        //기타접보 지속발생 제거. 차단작업 음성 발생 제거
        if ("0001".equals(Common.nullCheck(pastItem.getReg_type())) || "0002".equals(Common.nullCheck(pastItem.getReg_type()))) {
            return;
        }
        Log.d("", "mp3SoundStatChoice 3 end_yn = " + pastItem.getEnd_yn() + ": int_vap = " + pastItem.getInp_val());
        Log.d("", "mp3SoundStatChoice 3 end_yn = " + pastItem.getEnd_yn() + ": reg_data = " + pastItem.getReg_data());
        Log.d("", "mp3SoundStatChoice 3 end_yn = " + pastItem.getEnd_yn() + ": rpt_id = " + pastItem.getRpt_id());


        if (Common.nullCheck(pastItem.getEnd_yn()).equals("Y")) {
            return;
        }
        Log.d("", "mp3SoundStatChoice 4");

        try {
            AssetFileDescriptor afd = getAssets().openFd(name);


//				sPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
//
//				sound = new int[1];
//				sound[0] = sPool.load(afd, 1);
//				sPool.play(sound[0], 1, 1, 0, 0, 1);
            player = null;
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            player.prepare();
            player.start();
            Log.d("", "mp3SoundStatChoice 5");
        } catch (IOException e) {
            Log.e("에러", "예외");
            Log.d("", "mp3SoundStatChoice 6");
        }

    }

    // 서버 통신
    public class Action extends AsyncTask<String, Void, XMLData> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
        ProgressDialog progressDialog;

        String primitive = "";
        Parameters params = null;

        XMLData returnData = null;

        @Override
        protected void onPreExecute() {
            if (primitive.equals(ONECLICK_GETUSERINFO_SELECT)) {
                progressDialog = ProgressDialog.show(SituationService.this, "", "로딩중...", true);
            } else if (primitive.equals(ONECLICK_CITIZENRCEPIT_INSERT)) {
                progressDialog = ProgressDialog.show(SituationService.this, "", "전송중...", true);
            } else if (primitive.equals(ONECLICK_GETPATROLTELNO_SELECT)) {
//				progressDialog = ProgressDialog.show(SituationService.this, "", "로딩중...", true);
            }

            super.onPreExecute();
        }

        // primitive 에 따라 URL을 구분짓는다.
        public Action(String primitive, Parameters params) {
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
                StringBuffer body = new StringBuffer();
                if (ONECLICK_CARGPS_INSERT.equals(primitive)) {
                    body.append(URL_SENDGPS);
                    body.append("?");
                    body.append(params.toString());
                    gpsinsertFlag = false;

                    Log.i("", TAG + " " + primitive + "synchronized start ");
                }

//				String deUrl = body.toString();
//				String encodeurl = URLEncoder.encode(deUrl, "UTF-8");
//				deUrl = encodeurl;
//				Log.i(TAG, "deUrl : = " + deUrl);
//				
//				URL url = new URL(encodeurl);

                URL url = new URL(new String(Common.nullTrim(body.toString()).getBytes("UTF-8"), "UTF-8"));
//				URL url = new URL(new String(Common.nullTrim(body.toString()).getBytes("EUC-KR"), "Cp1252"));
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
                    // String response = new String(byteData);
                    Log.d("", "responseData  = " + response);
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
                        returnData = xmlData;
                        String ret = xmlData.get("result");
                        Log.d("", TAG + " ret check = " + ret);
                        if ("1000".equals(ret)) {
                            if (ret == null) {
                                // throw new IOException();
                            } else {
                                String retMsg = xmlData.get("resultMessage");
                                // throw new IOException();
                            }
                        } else if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                            if ("9999".equals(ret)) {
                                rpt_endtime = Common.nullCheck(xmlData.get("end_time"));
                                rpt_inp_val = Common.nullCheck(xmlData.get("inp_val"));
                                Log.d("", "SituationService mp3Start type 1");
//check_9999
                                mp3Start(ret, pastItem);
                            } else if ("9998".equals(ret)) {
                                rpt_endtime = Common.nullCheck(xmlData.get("end_time"));
                                rpt_inp_val = Common.nullCheck(xmlData.get("inp_val"));
                                Log.d("", "SituationService mp3Start type 2");

                                mp3Start(ret, pastItem);
                            } else if ("9997".equals(ret)) {
                                if ("Y".equals(Common.getPrefString(SituationService.this, "start_yn"))) {
                                    Intent i = new Intent(SituationService.this, DialogSituEnd.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.putExtra("message", "긴급견인은 차량 1대만 출동하실 수 있습니다.");
                                    PendingIntent pi = PendingIntent.getActivity(SituationService.this, 0, i, PendingIntent.FLAG_ONE_SHOT);
                                    try {
                                        pi.send();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    sendStop();
                                }

                            }
                            if ("START".equals(Common.nullCheck(params.get("STOP")))) {
                                Log.d("", TAG + "9999 onActionPost  1 primitive = " + primitive);
                                Log.d("", TAG + " setActivityViewEdit 1");
                            }
                        }
                    } catch (XmlPullParserException e) {
                        Log.e("에러", "예외");
                        if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                            gpsinsertFlag = true;
                            Log.i("", TAG + " " + primitive + "synchronized end Exception 1");
                        }
                    } catch (Exception e) {
                        Log.e("에러", "예외");
                        if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                            gpsinsertFlag = true;
                            Log.i("", TAG + " " + primitive + "synchronized end Exception 1");
                        }
                    }
                }

            } catch (IOException e) {
                Log.e("에러", "예외");
                if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                    gpsinsertFlag = true;
                    Log.i("", TAG + " " + primitive + " synchronized end IOException 1");
                }

            } catch (Exception e) {
                Log.e("에러", "예외");
                if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                    gpsinsertFlag = true;
                    Log.i("", TAG + " " + primitive + " synchronized end IOException 1");
                }

            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        Log.e("에러", "에러");
                    } catch (Exception e) {
                        Log.e("에러", "에러");
                    }
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.e("에러", "에러");
                    }
                }

                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        Log.e("에러", "에러");
                    }
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(XMLData result) {
            if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
                gpsinsertFlag = true;
                Log.i("", TAG + " ONECLICK_CARGPS_INSERT synchronized end onPostExecute");
            }
            if (primitive.equals(ONECLICK_GETUSERINFO_SELECT) || primitive.equals(ONECLICK_CITIZENRCEPIT_INSERT)) {
                progressDialog.dismiss();
            }

        }

    }

    /************************************************************************
     * 제보 파일 전송 - Progressbar, AsyncTask를 이용하여 파일 전송 - 전송완료 후 제보내용 전송 호출
     ************************************************************************/
    String sendCheckFile;
    String transData = "Y";
    com.ex.situationmanager.util.CustomMultiPartEntity multipartContent;

    public int iPercent;

    public class DoComplecatedJob extends AsyncTask<String, Integer, Long> {
        long totalSize;
        long totalSizeKB;

        // 진행 상태 Progressbar
        ProgressDialog progressDialog;
        Dialog mDialog = null;
        ProgressBar pb = null;
        ImageButton ibtnProgressCancel;

        public TextView tv;
        public TextView tvTrans;
        public TextView tvTot;
        public TextView tvPromptProgress;
        public int iProgress;
        public List<Image> mFileList;
        Parameters params;
        Activity mActivity;

        public DoComplecatedJob() {
            super();
        }

        public DoComplecatedJob(List<Image> mList, Parameters params, Activity mActivity) {
            super();
            mFileList = mList;
            this.params = params;
            this.mActivity = mActivity;
        }

        @Override
        protected void onPreExecute() {
            // --------------------------------------------------------------------------------------------
            // #region 전송화면 출력

            progressDialog = new ProgressDialog(mActivity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("파일 전송 중...");
            progressDialog.setCancelable(true);

            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "전송취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            transData = "N";
                        }
                    });

            mDialog = new Dialog(mActivity, R.style.FullHeightDialog);

            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            mDialog.setContentView(R.layout.cust_progressbar);
            mDialog.setCancelable(false);


            pb = (ProgressBar) mDialog.findViewById(R.id.pbProgress);
            tv = (TextView) mDialog.findViewById(R.id.tvPercentProgress);
            tvTrans = (TextView) mDialog.findViewById(R.id.tvProgressTrans);
            tvTot = (TextView) mDialog.findViewById(R.id.tvProgressTot);
            tvPromptProgress = (TextView) mDialog.findViewById(R.id.tvPromptProgress);
            if (!conf.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
                tvPromptProgress.setText("사진을 전송중 입니다.");
            }
            ibtnProgressCancel = (ImageButton) mDialog.findViewById(R.id.ibtnProgressCancel);


            ibtnProgressCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    transData = "N";
                }
            });

            // 화면 표시 초기화
            tv.setText("0 %");
            tvTrans.setText("0 KB");
            tvTot.setText("0 KB");

            // 진행 변수 초기화
            iProgress = 0;
            iPercent = 0;
            totalSize = 0;
            totalSizeKB = 0;
            transData = "Y";
            mDialog.show();

            // #endregion
            // --------------------------------------------------------------------------------------------
        }

        @Override
        protected Long doInBackground(String... strData) {
            // --------------------------------------------------------------------------------------------
            // #region 실 첨부파일 전송, 전송화면 프로그레스 바 업데이트 함수 호출

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext httpContext = new BasicHttpContext();
            // HttpPost httpPost = new
            // HttpPost("http://uportal.ex.co.kr:5100/pda/src_new/jaenan/APNS/upload_proc.jsp");
            HttpPost httpPost;
            Log.d("", "################### DoComplecatedJob doInBackground start");
            try {
                StringBuffer sb = new StringBuffer();
                sb.append(conf.FILE_UPLOAD_PATH);
                sb.append("?");
                sb.append(params.toString());
                Log.i("90909090909090", "90909090909090" + sb.toString());
                Log.i("", "URL = " + sb.toString());
                // URL url = new URL(new String(
                // Common.nullTrim(sb.toString()).getBytes("EUC-KR"), "8859_1"))
                // ;

                httpPost = new HttpPost(sb.toString());

                multipartContent = new com.ex.situationmanager.util.CustomMultiPartEntity(
                        new ProgressListener() {

                            int uploadPercent = 0;

                            @Override
                            public void transferred(long num) {
                                // TODO Auto-generated method stub
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // String uriSet = mUriSet.toString();
                String absolutePath = "";

                // 파일 첨부 및 전송
                Log.d("", "before send ReadSdCardSize() size = " + ReadSdCardSize());

                for (int i = 0; i < ReadSdCardSize(); i++) {
                    Image image = mFileList.get(i);
                    absolutePath = conf.directoryName + "/" + image.getFileName().toString();
                    Log.d("", "multipartContent FILENAME = " + absolutePath);
                    multipartContent.addPart("uploaded_file" + i, new FileBody(new File(absolutePath)));
                }

                totalSize = multipartContent.getContentLength();
                totalSizeKB = totalSize / 1024;
                // pb.setMax(Integer.parseInt(totalSize + ""));

                // Send it
                multipartContent.addPart("renamePrefix", new StringBody(""));
                if (!conf.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
                    multipartContent.addPart("rpt_id", new StringBody(params.get("rpt_id"), Charset.forName("UTF-8")));
                    // bscode; // 순찰, 견인
                    multipartContent.addPart("bscode", new StringBody(params.get("bscode"), Charset.forName("UTF-8")));
                    // patcar_id; // 순찰일때만
                    if (conf.User.getUser_type().equals(USER_TYPE_PATROL)) {
                        multipartContent.addPart("patcar_id", new StringBody(params.get("patcar_id"), Charset.forName("UTF-8")));
                    }
                } else {
                    multipartContent.addPart("rpt_id", new StringBody(params.get("rpt_id"), Charset.forName("UTF-8")));
                    multipartContent.addPart("bscode", new StringBody("", Charset.forName("UTF-8")));
                    multipartContent.addPart("patcar_id", new StringBody("", Charset.forName("UTF-8")));
                }
                multipartContent.addPart("nscode", new StringBody(params.get("nscode"), Charset.forName("UTF-8")));
                multipartContent.addPart("bhcode", new StringBody(params.get("bhcode"), Charset.forName("UTF-8")));
                multipartContent.addPart("ijung", new StringBody(params.get("ijung"), Charset.forName("UTF-8")));
                // user_type; // 모두 필요
                multipartContent.addPart("user_type", new StringBody(conf.User.getUser_type(), Charset.forName("UTF-8")));


                httpPost.setEntity(multipartContent);

                // multipartContent.addPart("renamePrefix", new

                // 미디어 파일 전송
                HttpResponse response = httpClient.execute(httpPost, httpContext);
                String serverResponse = EntityUtils.toString(response.getEntity());

                sendCheckFile = serverResponse;
                Log.d("", "*****************************************************");
                Log.d("", "" + sendCheckFile);
                Log.d("", "" + sendCheckFile.trim());
                Log.d("", "*****************************************************");

            } catch (IOException e) {
                Log.e("에러", "예외");
            } catch (Exception e) {
                // end try~catch
                Log.e("에러", "예외");
            }
            Log.d("", "################### DoComplecatedJob doInBackground end");
            return null;

            // #endregion
            // --------------------------------------------------------------------------------------------
        }

        @Override
        protected void onCancelled() {
            // --------------------------------------------------------------------------------------------
            // #region 전송화면에서 취소버튼 클릭

            // 중지 flag가 호출된 경우 파일 upload 중단
            multipartContent.stop();
            multipartContent = null;
            // dialogStartup.dismiss();

            Log.d("ProgressUpdate", "onCancelled : " + "onCancelled");

            // #endregion
            // --------------------------------------------------------------------------------------------
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // --------------------------------------------------------------------------------------------
            // #region 전송화면 프로그레스바 업데이트

            pb.setProgress((int) (progress[0]));

            // thread 중지
            if (transData.equals("N")) {
                cancel(true);
            }

            if (iPercent == (int) (progress[0])) {

            } else {

                iPercent = (int) (progress[0]);

                if (iProgress == 0) {
                    iProgress++;
                }// end if

                for (int i = iProgress; iProgress < iPercent; i++) {
                    iProgress++;
                }// end for

                tv.setText(iProgress + " %");

                Log.d("ProgressUpdate", "iPercent : " + iPercent);
                Log.d("ProgressUpdate", "iProgress : " + iProgress);
                Log.d("ProgressUpdate", "totalSize : " + totalSize);

                DecimalFormat df = new DecimalFormat("###,###");

                tvTrans.setText(df.format(iProgress * totalSizeKB / 100) + " KB");
                tvTot.setText(df.format(totalSizeKB) + " KB");
            }// end if

            // #endregion
            // --------------------------------------------------------------------------------------------
        }

        @Override
        protected void onPostExecute(Long result) {
            // --------------------------------------------------------------------------------------------
            // #region 첨부파일 전송 완료 후 제보내용 전송 함수 호출

            progressDialog.dismiss();
            multipartContent = null;
            mDialog.dismiss();

            Log.d("ProgressUpdate", "transData : " + transData);
            Log.d("ProgressUpdate", "transData : " + result);

            if (multipartContent != null || (iPercent < 100 && iPercent > 0)) {

                openWarnDialog(mActivity, "파일 전송에 실패했습니다.");
            } else {
                if (USER_TYPE_TOW.equals(conf.User.getUser_type()) || USER_TYPE_PATROL.equals(conf.User.getUser_type())) {
                    openWarnDialog(mActivity, "파일 전송 성공");
                } else {
                    openWarnDialog(mActivity, "파일 전송 성공\n제보가 완료 되었습니다.");
                }
            }
//			Common.DeleteDir(Common.FILE_DIR);
            /*
             * else{
             *
             * //파일 전송이 정상적으로 종료된 경우
             *
             * if(transData.equals("Y")){ fileHandler.postDelayed(new Runnable()
             * {
             *
             * @Override public void run() { fileHandler.postDelayed(this,
             * 1000); fileHandler.removeMessages(0); new
             * saveReportInfo().execute("1000"); } }, 1000);
             *
             * }//end if }//end if
             */
            // #endregion
            // --------------------------------------------------------------------------------------------
        }

    }

    double totalFileSize = 0.0;

    public void executeJob(final Parameters params, Activity mActivity) {
        if (ReadSdCardSize() > 0) {
            final DoComplecatedJob task = new DoComplecatedJob(ReadSDCard(), params, mActivity);
            task.execute("5000");

            fileHandler = new Handler();
            fileHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fileHandler.postDelayed(this, 2000);
                    if (task.getStatus() == AsyncTask.Status.FINISHED) {
                        fileHandler.removeMessages(0);
                    }// end if
                }
            }, 2000);
        }
    }

    // SituationManager 폴더내에 있는 파일갯수
    public static int ReadSdCardSize() {

        File fileDir = new File(conf.directoryName);
        if (!fileDir.exists())
            fileDir.mkdir();
        File[] files = fileDir.listFiles();

        return files.length;
    }

    // SituationManager 폴더내에 있는 파일 전체용량
    public static double ReadSDCardMB() {

        File fileDir = new File(conf.directoryName);

        if (!fileDir.exists())
            fileDir.mkdir();

        File[] files = fileDir.listFiles();
        double totalFileSize = 0;
        for (int i = 0; i < files.length; i++) {

            File file = files[i];
            double nByte = file.length();
            double mByte = 0;
            double tvMByte = 0;
            String tvTextSize = "";

            mByte = Math.floor(nByte * 100 / (1024 * 1024));
            tvTextSize = String.valueOf(mByte / 100);

            tvMByte = Math.round(mByte / 10);
            tvMByte = tvMByte / 10;
            totalFileSize = totalFileSize + tvMByte;
        }

        return totalFileSize;
    }

    // 1개의 파일용량 MB 로 표시
    public static double getFileSizeMB(double filesize) {
        double nByte = filesize;
        double mByte = 0;
        double tvMByte = 0;
        String tvTextSize = "";

        mByte = Math.floor(nByte * 100 / (1024 * 1024));
        tvTextSize = String.valueOf(mByte / 100);

        tvMByte = Math.round(mByte / 10);
        tvMByte = tvMByte / 10;
        return tvMByte;
    }

    /**
     * 다이얼 로그 박스
     *
     * @param message
     */
    Dialog dialogWarn;

    public void openWarnDialog(Context ctx, String message) {
        dialogWarn = new Dialog(ctx, R.style.FullHeightDialog);

        dialogWarn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWarn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogWarn.setContentView(R.layout.cust_dialog_warn);
        dialogWarn.setCancelable(false);

        ImageButton button = (ImageButton) dialogWarn.findViewById(R.id.ibtnMovie);

        TextView tvPrompt = (TextView) dialogWarn.findViewById(R.id.tvPrompt);
        tvPrompt.setText(message);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogWarn.dismiss();
            }
        });

        dialogWarn.show();
    }

    /************************************************************************
     * GPS
     ************************************************************************/
    // public static LocationManager mLocMgr = null;
    // public static LocationListener mLocListener = null;
    private static Handler mHandler = new Handler();
    double ppastLatitude;
    double ppastLongitude;
    double pastLatitude;
    double pastLongitude;
    double angle;
    public static double longitude = 0.0;
    public static double latitude = 0.0;

    //
    double altitude;
    double speed = 0.0;
    boolean mGpsSatStat = false;
    Location location;
    LocationManager lm;
    StringBuilder sb;
    int noOfFixes = 0;
    static boolean isGPSEnable = false;

    private double START_LAT = 0.0;
    private double START_LNG = 0.0;

    // GPS 정보
    public static String ns_code = ""; // 노선코드
    public static String ns_name = ""; // 노선명
    public static String banghyang = ""; // 방향
    public static String bhCode = "S"; // 방향코드(S,E)
    public String sisul; // 시설물명(IC/JCT, 휴게소, 졸음쉼터)
    public String[] sisulInfo = new String[3];
    public String siseolDaepyo;// ICJCT, 휴게소, 졸음쉼터 화면표출 텍스트
    public String ppastIjung = "000";
    public String pastIjung = "111";
    public static String currentIjung = ""; // 이정
    public String rampid = "";

    public String gonggu = "";
    boolean isInNoseon = true;

    private int myCnt = 0;
    public static LocationManager RelimyLocMgr;
    public static LocationListener RelimyListener;

    private int maxNsEmptyTime = 30;
    private int moveNsEmptyTime = 4;
    private int emptyCnt = 0;// 좌표정보없음 카운트 초기화
    private boolean gpsStatusYN = false;


    public int locationChangedCnt = 0;


    static LocationManager mLocMgr;
    static LocationListener mLocListener;

    public void startMyGps() {
        //--------------------------------------------------------------------------------------------
        // #region startGPS, 위치정보 표시 [ setCurrentPosition(getDBSearch(latitude, longitude)); ]

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {
                    public void run() {
                        mLocMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        mLocListener = new LocationListener() {
                            public void onProviderDisabled(String provider) {

                            }

                            public void onStatusChanged(String provider, int status, Bundle extras) {

                                switch (status) {
                                    case LocationProvider.OUT_OF_SERVICE:
                                        Log.v("GPSInfo", "Status Changed: Out of Service");
                                        latitude = START_LAT;
                                        longitude = START_LNG;

                                        break;
                                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                                        Log.v("GPSInfo", "Status Changed: Temporarily Unavailable");
                                        break;
                                    case LocationProvider.AVAILABLE:
                                        Log.v("GPSInfo", "Status Changed: Available");

                                        break;

                                }
                            }

                            public void onProviderEnabled(String provider) {
                            }

                            public void onLocationChanged(Location location) {

                                Log.d("", "locationchanged in send");
                                locationChangedCnt = 0;
                                dist = 0.0;
//								pastLatitude = latitude;
//								pastLongitude = longitude;
                                latitude = location.getLatitude();// 위도
                                longitude = location.getLongitude();// 경도
                                Log.d("", "locationChanged in=" + latitude + " : " + longitude);
                                //자릿수 소숫점 8째 자리까지
                                latitude = Common.doubleCutToString(latitude);
                                longitude = Common.doubleCutToString(longitude);

                                altitude = location.getAltitude();// 고도
                                speed = Math.round((location.getSpeed() * 3600) / 1000);// 속도

                            }
                        };

                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);        // 정확도
                        criteria.setPowerRequirement(Criteria.POWER_LOW);    // 전원 소비량
                        criteria.setAltitudeRequired(true);                // 고도, 높이 값을 얻어 올지를 결정
                        criteria.setBearingRequired(true);
                        criteria.setSpeedRequired(true);                    //속도
                        criteria.setCostAllowed(false);                    //위치 정보를 얻어 오는데 들어가는 금전적 비용

                        mLocMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (PermissionChecker.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        //기본 원클릭
                        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocListener);
                        //mLocMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocListener);


                        GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
                            public void onGpsStatusChanged(int event) {
                                if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
                                    GpsStatus status = mLocMgr.getGpsStatus(null);
                                    Iterable<GpsSatellite> sats = status.getSatellites();
                                    Iterator<GpsSatellite> iter = sats.iterator();

                                    GpsSatellite gpsS = null;

                                    boolean checkStat = false;
                                    int checkStatCnt = 0;
                                    while (iter.hasNext()) {
                                        gpsS = iter.next();

                                        if (gpsS.usedInFix()) {
                                            Log.d("GPSInfo", "Almanac : " + gpsS.hasAlmanac());
                                            Log.d("GPSInfo", "Ephemeris : " + gpsS.hasEphemeris());
                                            checkStatCnt++;
                                        }
                                    }
//				                    Log.d("GPSInfo", "checkStatCnt"+checkStatCnt);

                                    if (checkStatCnt > 3) {
                                        checkStat = true;
                                    } else {
                                        checkStat = false;
                                    }

                                    if (mGpsSatStat != checkStat) {
                                        mGpsSatStat = checkStat;

                                        try {

                                            if (mLocMgr != null) {
                                                Location l = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                                if (l != null) {
                                                }
                                            }

                                        } catch (SecurityException ex) {
                                            Log.e("에러", "예외");
                                        } catch (Exception e) {
                                            Log.e("에러", "예외");
                                        }

                                    }

                                }
                            }
                        };

                        mLocMgr.addGpsStatusListener(gpsStatusListener);
                        String provider = mLocMgr.getBestProvider(criteria, true);


                        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocListener);

                    }
                });
            }
        }).start();
        // #endregion
        //--------------------------------------------------------------------------------------------
    }

    /************************************************************************
     * GPS 중지
     ************************************************************************/
    public static void stopGPS() {
        if (mLocMgr != null)
            mLocMgr.removeUpdates(mLocListener);
    }

    Cursor dbCursor;


    /************************************************************************
     * GPS 정보에 따른 위치정보를 DB에서 조회 후 리턴 - 방향 기점, 종점 구분 방법 : 이전 좌표의 이정과 현재 이정을 비교
     * 이전이정 > 현재 이정 = 기점 (경부선: 부산) 이전이정 < 현재 이정 = 종점 (경부선: 서울) : 정지상태에서 GPS 수신하여
     * 이전이정이 없는 경우 현재 이정 기준에서 먼저 검색되는 방향을 표시 즉, 정지상태에서 비교할 이정이 없는 경우에는 정확한 방향표시
     * 불가능 함.
     *
     * @param latitude
     * @param longitude
     * @return
     ************************************************************************/
    public String getDBSearch(double latitude, double longitude) {
        // --------------------------------------------------------------------------------------------
        // #region GPS 위경도 좌표에 따른 위치(노선, 방향, 이정, 부근 IC/JCT) 정보를 리턴한다.

        Log.v("GPSInfo", "getDBSearch");

        String rtnStr = "";
        // -> 현재 위치가 있다면 현재 위치에 과거 위치값으로 대입
        // -> 현재 위치가 없다면 현재 위치에 현재 위치값으로 대입

        if (!"".equals(currentIjung) && !"-999".equals(currentIjung)) {
            ppastIjung = pastIjung;
            pastIjung = Common.nullCheck(currentIjung);
            if (!pastIjung.equals("")) {
                ppastIjung = pastIjung;
            } else {
                pastIjung = "000";
            }// end if

        } else {
            // currentIjung = "111";
        }// end if

        Log.d("GPSInfo", "latitude : " + latitude);
        Log.d("GPSInfo", "longitude : " + longitude);
        Log.d("GPSInfo", "ns_code : " + ns_code);


        try {
            // private int maxNsEmptyTime = 30;
            // private int moveNsEmptyTime = 4;
            // private int emptyCnt = 0;//좌표정보없음 카운트 초기화

            dbCursor = db.fetchRange(latitude, longitude, ns_code);

            Log.d("GPSInfo", "getDBSearchCompleted...");
            if (dbCursor.getCount() > 0) {

                // cursor.getCount(); // 총 ROW 수
                // cursor.getColumnCount() // 컬럼 개수

                // 현재 GPS정보에 노선이 2개 이상인 경우 기존 노선 정보를 확인한다.
                String rowResults = "";

                if (dbCursor.getCount() > 1) {
                    if (ns_code != null && ns_code.trim().length() > 0) {

                        Log.d("GPSInfo", "GPSInfo 결과값 2개이상 기존 노선코드 존재");
                        Log.d("GPSInfo", "ns_code : " + ns_code + "|");
                        // Log.d("GPSInfo", "dbCursor.getString(0) : " +
                        // dbCursor.getString(0) +"|" );

                        dbCursor.moveToFirst();
                        for (int i = 0; i < dbCursor.getCount(); i++) {

                            dbCursor.moveToPosition(i);

                            if (ns_code.equals(dbCursor.getString(0))) {

                                ns_code = dbCursor.getString(0);
                                currentIjung = dbCursor.getString(2);
                                ns_name = dbCursor.getString(1);
                                sisul = dbCursor.getString(4);
                                rampid = dbCursor.getString(5);

                            }// end if
                        }// end for
                    } else {

                        Log.d("GPSInfo", "GPSInfo 결과값 2개이상 기존 노선코드 없음");
                        dbCursor.moveToFirst();

                        for (int i = 0; i < dbCursor.getCount(); i++) {
                            dbCursor.moveToPosition(i);

                            ns_code = dbCursor.getString(0);
                            currentIjung = dbCursor.getString(2);
                            ns_name = dbCursor.getString(1);
                            sisul = dbCursor.getString(4);
                            rampid = dbCursor.getString(5);
                        }// end for
                    }// end if

                } else {
                    Log.d("GPSInfo",
                            "GPSInfo 결과값 1개 | count : " + dbCursor.getCount());

                    // for(int i=0; i<dbCursor.getCount(); i++){

                    dbCursor.moveToFirst();

                    ns_code = dbCursor.getString(0);
                    currentIjung = dbCursor.getString(2);
                    ns_name = dbCursor.getString(1);
                    sisul = dbCursor.getString(4);
                    rampid = dbCursor.getString(5);

                    // dbCursor.moveToNext();
                    // }

                }// end if

                Log.d("GPSInfo", "GPSInfo ns_code : " + ns_code + "|");
                Log.d("GPSInfo", "GPSInfo currentIjung : " + currentIjung + "|");
                Log.d("GPSInfo", "GPSInfo ns_name : " + ns_name + "|");

                // 방향 목록
                Cursor cursorBangHyang = db.fetchBangHyang(ns_code);

                String gjMyeong = "";
                String jjMyeong = "";
                if (cursorBangHyang.getCount() > 0) {
                    cursorBangHyang.moveToFirst();

                    gjMyeong = cursorBangHyang.getString(2);
                    jjMyeong = cursorBangHyang.getString(3);
                }// end if

                double temp0 = Double.parseDouble(ppastIjung);
                double temp1 = Double.parseDouble(pastIjung);
                double temp2 = Double.parseDouble(currentIjung);

                Log.d("GPSInfo", "GPSInfo temp0" + temp2 + ":" + temp1 + ":"
                        + temp0 + " ::::: " + ns_code);
                // Log.d("GPSInfo", "temp1" + temp1);
                // Log.d("GPSInfo", "temp2" + temp2);
                // 서울외곽순환선
                // <down>판교</down>
                // <upper>구리</upper>
                // <middle>일산</middle>
                /*if (ns_code.equals("1000")) {*/
                //공사관리 위치정보 테스트

                if (ns_code.equals("1000")) {


                    if (temp2 <= 33) {
                        if (temp2 > temp1) {// 현재이정(currentIjung)이
                            // 과거이정(pastIjung)보다
                            // 크면
                            if (temp1 >= temp0) {
                                banghyang = common.UPPER_BANGHYANG;
                                bhCode = "S";
                            }// end if
                        } else if (temp2 < temp1) {
                            if (temp1 <= temp0) {
                                banghyang = common.DOWN_BANGHYANG;
                                bhCode = "E";
                            }// end if
                        }// end if
                    } else if (temp2 > 33 && temp2 <= 70) {
                        if (temp2 > temp1) {// 현재이정(currentIjung)이
                            // 과거이정(pastIjung)보다
                            // 크면
                            if (temp1 >= temp0) {
                                banghyang = common.MIDDLE_BANGHYANG;
                                bhCode = "S";
                            }// end if
                        } else if (temp2 < temp1) {
                            if (temp1 <= temp0) {
                                banghyang = common.UPPER_BANGHYANG;
                                bhCode = "E";
                            }// end if
                        }// end if
                    } else {

                        if (temp2 > temp1) {// 현재이정(currentIjung)이
                            // 과거이정(pastIjung)보다
                            // 크면
                            if (temp1 >= temp0) {
                                banghyang = common.DOWN_BANGHYANG;
                                bhCode = "S";
                            }// end if
                        } else if (temp2 < temp1) {
                            if (temp1 <= temp0) {
                                banghyang = common.MIDDLE_BANGHYANG;
                                bhCode = "E";
                            }// end if
                        }// end if
                    }// end if
                } else {
                    Log.d("", "GPSInfo temp2-1-0 " + temp2 + " : " + temp1
                            + " : " + temp0);
                    if (temp2 > temp1) {// 현재이정(currentIjung)이 과거이정(pastIjung)보다
                        // 크면
                        if (temp1 >= temp0) {
                            banghyang = jjMyeong;
                            bhCode = "E";
                        }// end if
                    } else if (temp2 < temp1) {
                        if (temp1 <= temp0) {
                            bhCode = "S";
                            banghyang = gjMyeong;
                        }// end if
                    }
                    // else{
                    //
                    // Log.d("", "ddddddddddddddd " + bhCode);
                    // bhCode = bhCode;
                    // }
                }// end if(ns_code.equals("1000")){
                Log.d("", "GPSInfo bhcode = " + bhCode);
                rtnStr = ns_name + "|" + banghyang + "|" + currentIjung + "|" + ns_code + "|0|" + siseolDaepyo;

                Log.d("GPSInfo", "Row : " + dbCursor.getPosition() + " => " + rowResults);
                Log.d("GPSInfo", "rtnStr :  " + rtnStr);

                isInNoseon = true;
            } else {
                // 노선 내에 존재하지 않는 경우
                // 메시지 한 번만 표시
                if (isInNoseon) {
                    //Toast.makeText(getApplicationContext(),"현재 위치는 도로공사 관리노선에 해당되지 않습니다.", 5).show();
                    if ("".equals(Common.nullCheck(ns_code))) {
//						setActivityViewEdit(SituationService.this);
                        Log.d("", TAG + " setActivityViewEdit 2");
                    }
                }// end if

                Log.d("GPSInfo", "mGpsSatStat2:" + mGpsSatStat);

                if (mGpsSatStat) {
                    rtnStr = "노선외|_|-999|_|0|0";
                } else {
                    rtnStr = "노선외(위성GPS가 활성화되지 \n않아 위치정보가 부정확합니다)|_|-999|_|0|0";
                }// end if

                isInNoseon = false;
            }// end if (dbCursor.getCount() > 0) {

            // 방향코드조회
            // bhCode = db.getBanghyangCode(ns_code, banghyang);

            // 방향정보 의 code 컬럼 값.
            rpt_bhPkCode = db.fetchBanghyang_sub(ns_code, bhCode, rpt_bscode, currentIjung);

        } catch (NullPointerException e) {
            Log.e("에러", "예외");
        } finally {
            Log.d("", "GPSInfo rtnStr = " + rtnStr);
            if (dbCursor != null && !dbCursor.isClosed()) {
                dbCursor.close();
            }// end if
        }// end try~catch

        return rtnStr;

        // #endregion
        // --------------------------------------------------------------------------------------------
    }

    // 좌표수집 불가시 실행.
    static double dist = 0.0;

    public void noLocationInfo() {

        if (gpsStatusYN == false) {
            emptyCnt++;
            emptyCnt++;
            if (moveNsEmptyTime % moveNsEmptyTime == 0) {
                if (moveNsEmptyTime <= maxNsEmptyTime) {

                    Cursor cursor = db.fetchNextRange(ns_code, currentIjung, bhCode);
                    if (null != cursor) {
                        while (cursor.moveToNext()) {
                            ns_code = Common.nullCheck(cursor.getString(0));
                            ns_name = Common.nullCheck(cursor.getString(1));
                            currentIjung = Common.nullCheck(cursor.getString(2));
                            sisul = Common.nullCheck(cursor.getString(4));
                            rampid = Common.nullCheck(cursor.getString(5));
                        }
                    } else {
                        return;
                    }

                    sisul = db.fetchSisulAll(ns_code, latitude, longitude, bhCode);// 시설물전체조회


                    // //////////////////////////////////////////////////////////////////////////////////////
                    double currlo = longitude;
                    double currla = latitude;

                    double besidelo = 0.0;
                    double besidela = 0.0;
                    double nextlo = 0.0;
                    double nextla = 0.0;
                    double nnextlo = 0.0;
                    double nnextla = 0.0;
                    double resultlo = 0.0;
                    double resultla = 0.0;

                    System.out.println("Tunnel Update Locatoin cursor count = " + cursor.getCount());
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        if (i == 0) {
                            besidelo = Common.nullCheckDouble(cursor.getString(6));
                            besidela = Common.nullCheckDouble(cursor.getString(7));
                        } else if (i == 1) {
                            nextlo = Common.nullCheckDouble(cursor.getString(6));
                            nextla = Common.nullCheckDouble(cursor.getString(7));
                        } else if (i == 2) {
                            nnextlo = Common.nullCheckDouble(cursor.getString(6));
                            nnextla = Common.nullCheckDouble(cursor.getString(7));
                        }
                    }

                    double dist_curr_beside = Math.sqrt(Math.pow((currlo - besidelo), 2) + Math.pow((currla - besidela), 2));
                    double anglea = Math.atan2(currla - besidela, currlo - besidelo);
                    double angleb = Math.atan2(nextla - besidela, nextlo - besidelo);
                    double nextangleb = Math.atan2(nnextla - nextla, nnextlo - nextlo);
                    double realangle = anglea - angleb;

                    if (dist == 0.0) {
                        dist = Math.sin(realangle) * dist_curr_beside;
                    }

                    if (dist > 0) {
                        System.out.println("Tunnel Update Locatoin dist edit = " + dist);
                        resultlo = nextlo + (Math.cos(nextangleb + Math.atan2(1, 0)) * Math.abs(dist));
                        resultla = nextla + (Math.sin(nextangleb + Math.atan2(1, 0)) * Math.abs(dist));
                    } else {
                        System.out.println("Tunnel Update Locatoin dist edit = " + dist);
                        resultlo = nextlo + (Math.cos(nextangleb - Math.atan2(1, 0)) * Math.abs(dist));
                        resultla = nextla + (Math.sin(nextangleb - Math.atan2(1, 0)) * Math.abs(dist));
                    }

                    if (34 < latitude && latitude < 38.4) {
                        if (126 < longitude && longitude < 129.6) {
                            longitude = resultlo;
                            latitude = resultla;
                            //자릿수 소숫점 8째 자리까지
                            longitude = Common.doubleCutToString(longitude);
                            latitude = Common.doubleCutToString(latitude);
                        }
                    }
                    // //////////////////////////////////////////////////////////////////////////////////////

                } else {
                }
            }
        } else {
            emptyCnt = 0;
            Log.d("", TAG + " tunnel in false");
        }
    }

    /************************************************************************
     * GPS 정보에 따른 DB검색 결과 값을 각 변수 대입하고 화면에 표시한다.
     ************************************************************************/
    private void setCurrentPosition(String dbSearch) {
        // --------------------------------------------------------------------------------------------
        // #region GPS 정보에 따른 DB검색 결과 값을 각 변수 대입하고 화면에 표시한다.
        Log.d("GPSInfo", "setCurrentPosition");

        Log.d("GPSInfo", dbSearch);

        String[] GPSInfo = dbSearch.split("[|]");
        Log.d("GPSInfo", "setCurrentPosition" + GPSInfo.length);
        // GPS 정보가 정상인 경우
        if (GPSInfo.length >= 5) {

            // etLocation.setTextSize(20);

            Log.d("GPSInfo", "GPSInfo[0]" + GPSInfo[0]);
            Log.d("GPSInfo", "GPSInfo[1]" + GPSInfo[1]);
            Log.d("GPSInfo", "GPSInfo[2]" + GPSInfo[2]);

            // 관리 노선인 경우
            if (GPSInfo[0] != null && !GPSInfo[0].equals("")
                    && !GPSInfo[0].startsWith("노선외") && GPSInfo[2] != null) {

                // 이정에 소수점이 없으면 .0 추가
                if (GPSInfo[2].indexOf(".") <= -1) {
                    GPSInfo[2] = GPSInfo[2] + ".0";
                } else {
                    if (GPSInfo[2].indexOf(".") == GPSInfo[2].length() - 1) {
                        GPSInfo[2] = GPSInfo[2] + "0";
                    }// end if
                }// end if

                // 위치정보가 update 될 때마다 각 변수에 위치정보 설정함
                ns_name = GPSInfo[0]; // 노선명
                banghyang = GPSInfo[1]; // 방향
                currentIjung = GPSInfo[2]; // 이정
                ns_code = GPSInfo[3]; // 노선코드
            } else {
                ns_name = ""; // 노선명
                banghyang = ""; // 방향
                // currentIjung = "-999"; // 이정
                currentIjung = currentIjung;
                ns_code = ""; // 노선코드
                sisul = "";
            }// end if
        }// end if

//		if("".equals(Common.nullCheck(ns_code))){
//			sisulInfo = db.fetchSisulAllInfo(ns_code, latitude, longitude,bhCode);// 시설물전체조회
//			ns_code = sisulInfo[0];
//			currentIjung = sisulInfo[1];
//			sisul = sisulInfo[2];
//		}else{
//		}

        sisul = db.fetchSisulAll(ns_code, latitude, longitude, bhCode);// 시설물전체조회

        // 공구조회
        if (!"".equals(Common.nullCheck(ns_code))) {
            gonggu = db.fetchGonggu(ns_code, currentIjung);// 공구조회
        } else {// 노선 OUT
            gonggu = "";

        }

        // #endregion
        // --------------------------------------------------------------------------------------------
    }


    private int timerDelay = 1;// timerDelay 초마다 실행
    private int timerCnt = 0;// 카운트 증가. 최대 timerDelay
    static Timer timer = new Timer();
    Handler handler = new Handler();

    //좌표값을 받아올수 없는 상태에서 출동시 최초에 1회만 보냄.
    boolean firstFlag = true;
    //통신 동기화를 위함.
    boolean gpsinsertFlag = true;

    //공사관리 서버
    com.ex.gongsa.view.BaseActivity baseActivity = new com.ex.gongsa.view.BaseActivity() {
        @Override
        public String onActionPost(String primitive, String date) {
            return null;
        }
    };
    JSONObject jsonObject = null;
    JSONArray jsonArray = null;


    public String getPhoneNo() {
        TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("DEBUGGING", "This will be a final Error");
        }
        Log.e("DEBUGGING", "This will be a final Error Really");
        String phoneNo[] = com.ex.situationmanager.util.Common.phoneNumCut(systemService.getLine1Number());
        String mUserTel = phoneNo[0] + "-" + phoneNo[1] + "-" + phoneNo[2];
        mUserTel = Common.nullCheck(mUserTel);
        return mUserTel;
    }


    public void startTimer() {
        TimerTask timertask = new TimerTask() {
            Runnable runnable = new Runnable() {

                @SuppressLint("NewApi")
                @Override
                public void run() {
                    // GPS전송
                    Log.d("", TAG + " Time Check STA = " + Calendar.getInstance().getTimeInMillis());
                    //테스트 좌표
//					latitude = 36.131684;
//					longitude = 128.164336;
//					latitude = 36.130983;
//					longitude = 128.094225;
//					latitude = 36.131108;
//					longitude = 128.157442;

//					경부선 193.040
                    Log.d("", "TimerDelay = [" + timerDelay + "]:[" + timerCnt + "]");
                    if (true) {
                        timerCnt = 0;
                        locationChangedCnt++;


                        if (locationChangedCnt > 1) {
                            gpsStatusYN = false;
                        } else {
                            gpsStatusYN = true;
                        }

                        Log.d("", "Tunnel Update Locatoin locationChangedCnt= " + locationChangedCnt);

                        if (!"Y".equals(Common.getPrefString(SituationService.this, "start_yn"))) {
                            int cnt = (TIMER_DELAY_N / TIMER_DELAY_Y) - 1;
                            Log.d("", "TIMER_DELAY_CNT = " + cnt + ":" + TIMER_DELAY_CNT);
                            if (cnt >= TIMER_DELAY_CNT) {
                                TIMER_DELAY_CNT++;
                                return;
                            } else {
                                TIMER_DELAY_CNT = 0;
                            }
                        }

                        Log.d("", TAG + "ONECLICK_CARGPS_INSERT synchronized gpsinsertFlag = " + gpsinsertFlag);


                        //if(latitude != 0.0 && longitude != 0.0){


                        //	이거 사용해야도미

                        try {
                            Log.i("gps 전송중", "ㅠㅠㅠㅠㅠㅠ");
                            if (gpsListFlag == true) {
                                Log.i("gps 전송중", "true");

                                if (baseActivity.new Action(getApplicationContext()).chkGpsService()) {
                                    Log.i("gps 전송중", "-");
                                    jsonObject = new JSONObject();
                                    jsonObject.put("latitude", Double.toString(latitude));
                                    // jsonObject.put("longitude", Double.toString(longitude));

                                    byte[] encryptBytes = seed.encrypt(SituationService.conf.USER_PHONE_NUMBER, szKey);
                                    String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes)));

                                    baseActivity.new Action("get", SERVER_URL + "/TodayWorkPlan/gpsUpdater2.do" + "?latitude=" + latitude + "&longitude=" + longitude + "&telNo=" + encString, null, getApplicationContext()).execute("");
                                    // baseActivity.new Action("get",SERVER_URL+"/TodayWorkPlan/gpsUpdater.do?latitude="+latitude+"&longitude="+longitude+"&telNo="+"010-5382-8426" ,null, getApplicationContext()).execute("");
                                    Log.i("Situation 2732", "gps전송부분");
                                }
                            }

                            //Toast.makeText(getApplicationContext(),"위도:"+latitude+", 경도:"+longitude,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Log.e("에러", "예외");
                        } catch (Exception e) {
                            Log.e("에러", "예외");
                        }


                        //}


                        if ((locationChangedCnt > 1 && locationChangedCnt <= 7)) {//28초 30초전가지 진행
                            if (locationChangedCnt % 1 == 0) {
                                noLocationInfo();
                                Log.d("", "Tunnel Update Locatoin locationChangedCnt tunnel ininini= ");
                            }
                        } else {
                            if (locationChangedCnt < 30) {
                                setCurrentPosition(getDBSearch(latitude, longitude));
                            }
                        }


                        // ***************************************************************

                        Parameters params = new Parameters(ONECLICK_CARGPS_INSERT);

                        if (!"".equals(Common.nullCheck(move_direct))) {
                            params.put("move_direct", "" + move_direct);// 사용자가 입력한 방향(DialogDirect 입력값)
                        }

                        params.put("user_type", conf.User.getUser_type());// 차량관리번호
                        params.put("car_id", conf.User.getPatcar_id());// 순찰차량 일련번호.
                        if ("Y".equals(Common.getPrefString(SituationService.this, "start_yn"))) {
                            params.put("rpt_id", selectedRpt_id);// 상황일지 일련번호
                        }

                        //암호화
                        byte[] encryptBytes_lo = seed.encrypt(longitude + "", szKey);
                        String encString_lo = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo)));

                        byte[] encryptBytes_la = seed.encrypt(latitude + "", szKey);
                        String encString_la = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la)));


                        byte[] encryptBytes_sp = seed.encrypt(speed + "", szKey);
                        String encString_sp = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_sp)));

//						params.put("car_gps_lo", longitude + "");// 경도
//						params.put("car_gps_la", latitude + "");// 위도
                        params.put("car_gps_lo", encString_lo + "");// 경도
                        params.put("car_gps_la", encString_la + "");// 위도
                        params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0", szKey)))));// 이동각도


                        try {
                            altitude = Math.round(altitude * 100f) / 100f;
                        } catch (NullPointerException | NumberFormatException e) {
                            altitude = 0.0;
                            Log.e("에러", "NullPointerException | NumberFormatException");
                        } finally {
                            byte[] encryptBytes_hg = seed.encrypt(altitude + "", szKey);
                            String encString_hg = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_hg)));
                            params.put("car_gps_hg", encString_hg + "");// 고도
                        }
                        params.put("car_gps_sp", encString_sp + "");// 속도

                        params.put("start_yn", Common.getPrefString(SituationService.this, "start_yn") + "");// 출동여부 (y or n)
                        params.put("car_way", "" + ns_code);// 차량 노선 코드
                        if (!"".equals(Common.nullCheck(rampid))) {
                            params.put("car_way_ramp", (Common.nullCheck(rampid) + "").replace(" ", "").trim());// 램프 노선 코드
                        } else {
                        }

//						params.put("car_direct", rpt_bhPkCode);// 차량 방향 pk코드
                        if (conf.User.getBscode_list() != null && conf.User.getBscode_list().size() > 0) {
                            if (!"".equals(Common.nullCheck(ns_code))) {
                                params.put("car_direct", ns_code + conf.User.getBscode_list().get(0) + bhCode);// 차량 방향 pk코드
                            }
                        }

                        if (null != gonggu && !gonggu.isEmpty()) {
                            params.put("car_gonggu", gonggu.substring(0, 1) + "");// 공구명
                        } else {
                            params.put("car_gonggu", gonggu + "");// 공구명
                        }

                        params.put("car_sisul", sisul + "");// IC,JCT
                        params.put("car_ijung", currentIjung + "");// IC,JCT,휴게소,..및
                        // 시설물
                        params.put("ok", patrolOKFlag + "");// 이상없은 상태
                        params.put("nowprovider", "gps");// best provider 확인
                        params.put("STOP", "START");
                        if (conf.User.getBscode_list() != null && conf.User.getBscode_list().size() > 0) {
                            String rtnStr = db.fetchBBBsCodeSelected();
                            params.put("bscode", rtnStr);
                        }

                        // USER_TYPE_CITIZEN 대국민이 아닐 경우.0003


                        Log.d("", TAG + "user_type ) = " + conf.User.getUser_type());
                        if (USER_TYPE_TOW.equals(conf.User.getUser_type()) || USER_TYPE_PATROL.equals(conf.User.getUser_type())) {
                            if (34 < latitude && latitude < 38.4) {
                                if (126 < longitude && longitude < 129.6) {
                                    Log.d("", "locationchanged in send");

                                    try {
                                        angle = Math.atan2(longitude - pastLongitude, latitude - pastLatitude);
                                        angle = Math.round(angle * 1000000000d) / 1000000000d;
                                    } catch (Exception e) {
                                        angle = 0.0;
                                        e.printStackTrace();
                                    } finally {
                                        params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt(angle + "", szKey)))));// 이동각도
                                    }

                                    //Angle 계산후 이전좌표값 설정.
                                    pastLatitude = latitude;
                                    pastLongitude = longitude;
                                    //마지막 좌표값을 저장하기 위함. 0.0이 아닌 좌표값을 넣어줘야 상황판에서 정상 동작함.
                                    Common.setPrefString(SituationService.this, "lastla", "" + latitude);
                                    Common.setPrefString(SituationService.this, "lastlo", "" + longitude);
                                    if (gpsinsertFlag == true) {
                                        gpsinsertFlag = false;
                                        //차량용 단말기 전화번호 구분하여 좌표 전송 하지 않음
                                        if (!SituationService.conf.USER_PHONE_NUMBER.startsWith("012")) {

                                            new Action(ONECLICK_CARGPS_INSERT, params).execute("");

                                        }
                                    }
                                }
                            } else if (latitude == 0 || longitude == 0) {

                                if (Common.getPrefString(SituationService.this, "lastla").equals("")) {//마지막 저장 좌표가 없을 경우
                                    if (mLocMgr != null) {
                                        Location l = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                        if (mLocMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                            if (null != l) {
                                                byte[] encryptBytes_lo3 = seed.encrypt(l.getLongitude() + "", szKey);
                                                String encString_lo3 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo3)));
                                                byte[] encryptBytes_la3 = seed.encrypt(l.getLatitude() + "", szKey);
                                                String encString_la3 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la3)));

                                                params.put("car_gps_lo", encString_lo3 + "");// 경도
                                                params.put("car_gps_la", encString_la3 + "");// 위도
                                                params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0", szKey)))));// 이동각도
                                            }
                                        } else {
                                            params.put("car_gps_lo", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0", szKey)))));// 경도
                                            params.put("car_gps_la", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0", szKey)))));// 위도
                                            params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0", szKey)))));// 이동각도
                                        }
                                    }
                                } else {//마지막 저장 좌표가 있을 경우.
                                    byte[] encryptBytes_lo2 = seed.encrypt(Common.getPrefString(SituationService.this, "lastlo") + "", szKey);
                                    String encString_lo2 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo2)));
                                    byte[] encryptBytes_la2 = seed.encrypt(Common.getPrefString(SituationService.this, "lastla") + "", szKey);
                                    String encString_la2 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la2)));

                                    params.put("car_gps_lo", encString_lo2 + "");// 경도
                                    params.put("car_gps_la", encString_la2 + "");// 위도
                                    params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0", szKey)))));// 이동각도
                                }

                                if (gpsinsertFlag == true) {
                                    gpsinsertFlag = false;
                                    Log.i("940412", "5");
                                    Log.i("940412", "6");
                                    new Action(ONECLICK_CARGPS_INSERT, params).execute("");
                                }

                            }
                        }


                    }
                    timerCnt++;
                    // HTTP 통신
                    Log.d("", TAG + " Time Check END = " + Calendar.getInstance().getTimeInMillis());
                }
            };

            @Override
            public void run() {
                handler.post(runnable);
            }
        };

        timer = new Timer();
        timer.schedule(timertask, 1000, TIMER_DELAY_Y);// 1000MS 뒤시작
    }

    public void stopTimer(Context context) {
        if (null != timer) {
            timer.cancel();
        }
    }

    /**
     * 순찰자가 내용을 확인 했다는 정보 전송.
     */
    public void sendConfirm() {
        Parameters params = new Parameters(ONECLICK_PATROLCONFRIM_UPDATE);
        params.put("user_type", conf.User.getUser_type());// 차량관리번호
        params.put("car_id", conf.User.getPatcar_id());// 순찰차량 일련번호.
        params.put("rpt_id", selectedRpt_id);// 상황일지
        params.put("start_yn", "N");// 출동여부 (y or n)
        params.put("STOP", "CONFIRM");

        new Action(ONECLICK_CARGPS_INSERT, params).execute("");
    }


    public List<Image> ReadSDCard() {
        Image currentItem = null;
        List<Image> trnImageList = null;

        trnImageList = new ArrayList<Image>();

        File fileDir = new File(Common.FILE_DIR);

        if (!fileDir.exists())
            fileDir.mkdir();

        File[] files = fileDir.listFiles();
        totalFileSize = 0;
        for (int i = 0; i < files.length; i++) {

            currentItem = new Image();
            File file = files[i];

            String fileName[] = Common.split(file.getPath().toString(), "/");
            String fileType[] = Common.split(file.getPath().toString(), ".");

            currentItem.setFilePath(file.getPath().toString());
            currentItem.setFileName(fileName[fileName.length - 1]);
            currentItem.setFileType(fileType[fileType.length - 1]);
            // currentItem.setFileSize(file.length());

            Log.d("saveFile", "fileName : " + fileName[fileName.length - 1]);

            // 파일리스트 총 용량 받아오기 소수점 1자리, 반올림, MB 표현

            double nByte = file.length();
            double mByte = 0;
            double tvMByte = 0;
            String tvTextSize = "";

            mByte = Math.floor(nByte * 100 / (1024 * 1024));
            tvTextSize = String.valueOf(mByte / 100);

            tvMByte = Math.round(mByte / 10);
            tvMByte = tvMByte / 10;
            totalFileSize = totalFileSize + tvMByte;

            // totalFileListSize.setText(totalFileSize+"MB");
            trnImageList.add(currentItem);

        }// end for

        Log.d("", "ReadSdCard list size = " + trnImageList.size());
        return trnImageList;
    }

    /**
     * 접보지사 초기화+접보지사 재 설정.
     */
    public void settingJeopboJisa() {
        if (null != conf.User.getRcv_yn_list()) {
            // 초기화
            db.updateBBBsCodeInit();
            for (int i = 0; i < conf.User.getRcv_yn_list().size(); i++) {
                if (conf.User.getRcv_yn_list().get(i).equals("Y")) {
                    Log.d("", TAG + "items splig1  = "
                            + conf.User.getRcv_bs_list().get(i));
                    if (conf.User.getRcv_bs_list() != null) {
                        String[] items = conf.User.getRcv_bs_list().get(i)
                                .split("[|]");
                        for (int j = 0; j < items.length; j++) {
                            Log.d("", TAG + "items splig2  = " + items[j]);
                            db.updateBBBsCode(items[j], "Y");
                        }
                    }
                }
            }
        }
    }
}
