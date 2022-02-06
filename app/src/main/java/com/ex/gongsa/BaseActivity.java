package com.ex.gongsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
/*import android.support.v4.content.ContextCompat;*/
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created date : 2017-04-06
 *
 * @author JSJ
 */

/*public class BaseActivity{

}*/
/*public abstract class BaseActivity extends Activity implements View.OnClickListener {

    private final String TAG = "BaseActivity";

    public final String CODE_SUCCESS = "1000";//통신 성공 코드
    public final String CODE_ERROR = "1001";//오류
    public final String CODE_FAIL = "9999";//통신 실패 코드

    //primitive 관리(작업 내용이 기술된 String 구분자 값)
    public final String PRIMITIVE_LOGIN = "PRIMITIVE_LOGIN";//로그인
//	public final String PRIMITIVE_** = "";

    public Common common;
    public Activity activity;

    private static boolean isMember;
    public static void setIsMember(boolean ismember)
    {
        isMember = ismember;
    }
    public boolean getIsMember()
    {
        return isMember;
    }

    //납부후에 납부내역조회 미납요금 납부 갱신을 위해 설정.
    private static boolean isNapbuComplete_MinapJohoi;
    public static void setIsNapbuComplete_MinapJohoi(boolean isComplete)
    {
        isNapbuComplete_MinapJohoi = isComplete;
    }
    public boolean getIsNapbuComplete_MinapJohoi()
    {
        return isNapbuComplete_MinapJohoi;
    }

    private static boolean isNapbuComplete_MinapResult;
    public static void setIsNapbuComplete_MinapResult(boolean isComplete)
    {
        isNapbuComplete_MinapResult = isComplete;
    }
    public boolean getIsNapbuComplete_MinapResult()
    {
        return isNapbuComplete_MinapResult;
    }

    private static boolean isNapbuComplete_CardResult;
    public static void setIsNapbuComplete_CardResult(boolean isComplete)
    {
        isNapbuComplete_CardResult = isComplete;
    }
    public boolean getIsNapbuComplete_CardResult()
    {
        return isNapbuComplete_CardResult;
    }

    public static String noticeStr = "";
    public final static int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //캡쳐방지
        *//*getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);*//*
        common = new Common();
        getMenuNotice();
    }

    @Override
    protected void onResume() {

        super.onResume();
    *//*    long nCurrentTime = SystemClock.elapsedRealtime();  //현재시간

        long nBeforeTime = common.getPrefLong(this, Configuration.SHARED_LAST_TOUCH_TIME);
        if(nBeforeTime == 0) {
            nBeforeTime = nCurrentTime;
        }
*//*
        *//**
         * 현재시간 저장
         *//*
        *//*common.setPrefLong(this, Configuration.SHARED_LAST_TOUCH_TIME, nCurrentTime);

        long nDiffTime = nCurrentTime - nBeforeTime;    //두 시간의 차이
        //재접속 시간이 20분보다 길경우 로그아웃 처리,,
         if(nDiffTime > 1000 * 60 * 20 )
            {

                CustomWarningDialog dlg = new CustomWarningDialog(this, "", "", "로그아웃", "장시간 미사용으로 앱종료 합니다.");
                dlg.setCancelable(false);
                dlg.setCanceledOnTouchOutside(false);
                dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        *//**//*
                        common.setPrefBoolean(getApplicationContext(), Configuration.SHARED_SAVE_SIXMONTH, false);
                        common.setPrefBoolean(getApplicationContext(), Configuration.SHARED_SESSION_STATUS, false);
                        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_IPIN_CI_SIXMONTH, "");
                        *//**//*
                        for (int i = 0; i < activityList.size(); i++) {
                            activityList.get(i).finish();
                        }
                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                    }
                });
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        *//**//*
                        common.setPrefBoolean(getApplicationContext(), Configuration.SHARED_SAVE_SIXMONTH, false);
                        common.setPrefBoolean(getApplicationContext(), Configuration.SHARED_SESSION_STATUS, false);
                        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_IPIN_CI_SIXMONTH, "");
                        *//**//*
                        for (int i = 0; i < activityList.size(); i++) {
                            activityList.get(i).finish();
                        }
                        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(intent);
                    }
                });
               //다이얼로그를 보여주는 show() 메소드를 주석 처리하였음:: 최창유 2019/5/7
                dlg.show();

            }*//*


        //getMenuNotice();
        *//*
        if (//자식 Activity onCreate에서 activity 채우기 필요
                activity instanceof LoginActivity ||
                        activity instanceof SplashActivity ||
                        activity instanceof JoinPersonalStep1Activity ||
                        activity instanceof JoinPersonalStep2Activity ||
                        activity instanceof JoinWebIPINActivity ||
                        activity instanceof JoinWebPhoneActivity ||
                        activity instanceof JoinPersonalStep3Activity ||
                        activity instanceof ChangePwdActivity ||
                        activity instanceof CoachMarkActivity1 ||
                        activity instanceof JoinPersonalStep4Activity
                ) {
        } else {
            if (false == isLogin()) {
                //startLoginActivity();
            }
        }
        *//*
    }

    @Override
    protected void onPause() {
        super.onPause();

        //long nCurrentTime = SystemClock.elapsedRealtime();  //현재시간
        //common.setPrefLong(this, Configuration.SHARED_LAST_TOUCH_TIME, nCurrentTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    *//* 로그인 여부 확인
     * 로그인 상태 = true
     * 로그아웃상태 = false
    * *//*
    public boolean isLogin() {
//		public static String SHARED_YEJBID = "SHARED_YEJBID";//사용자(인증서,아이핀,sms )인증 아이디
//		public static String SHARED_USER_ID= "SHARED_USER_ID";//사용자 아이디
//		public static String SHARED_USER_IPIN_CI= "SHARED_USER_IPIN_CI";//사용자 ipin ci
//		public static String SHARED_USER_NAME= "SHARED_USER_NAME";//사용자 이름
        boolean status = false;

        //Log.d("", "USER INFO ci= " + common.getPrefString(getApplicationContext(), Configuration.SHARED_USER_IPIN_CI_SIXMONTH));
        //Log.d("", "USER INFO id= " + common.getPrefString(getApplicationContext(), Configuration.SHARED_USER_ID_SIXMONTH));
        //Log.d("", "USER INFO nm= " + common.getPrefString(getApplicationContext(), Configuration.SHARED_USER_NAME_SIXMONTH));
        //Log.d("", "USER INFO yid= " + common.getPrefString(getApplicationContext(), Configuration.SHARED_YEJBID_SIXMONTH));

//        common.setPrefString(BaseActivity.this, Configuration.SHARED_YEJBID, common.getPrefString(BaseActivity.this, Configuration.SHARED_YEJBID_SIXMONTH));
//        common.setPrefString(BaseActivity.this, Configuration.SHARED_USER_ID, common.getPrefString(BaseActivity.this, Configuration.SHARED_USER_ID_SIXMONTH));
//        common.setPrefString(BaseActivity.this, Configuration.SHARED_USER_IPIN_CI, common.getPrefString(BaseActivity.this, Configuration.SHARED_USER_IPIN_CI_SIXMONTH));
//        common.setPrefString(BaseActivity.this, Configuration.SHARED_USER_NAME, common.getPrefString(BaseActivity.this, Configuration.SHARED_USER_NAME_SIXMONTH));
//        common.setPrefString(BaseActivity.this, Configuration.SHARED_USER_CERT_TYPE, common.getPrefString(BaseActivity.this, Configuration.SHARED_USER_CERT_TYPE_SIXMONTH));

        status = !(Common.nullCheck(common.getPrefString(getApplicationContext(), Configuration.SHARED_USER_IPIN_CI)).equals("")
                || Common.nullCheck(common.getPrefString(getApplicationContext(), Configuration.SHARED_USER_ID)).equals("")
                || Common.nullCheck(common.getPrefString(getApplicationContext(), Configuration.SHARED_USER_NAME)).equals(""));

        return status;
    }

    *//**
     * thkang
     * 로그아웃시 앱 내부데이터 삭제
     *//*
    public void deleteUserInfo() {
        common.setPrefString(getApplicationContext(), Configuration.SHARED_YEJBID, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_YEJBID_SIXMONTH, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_ID, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_ID_SIXMONTH, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_IPIN_CI, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_IPIN_CI_SIXMONTH, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_NAME, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_NAME_SIXMONTH, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_CERT_TYPE, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_CERT_TYPE_SIXMONTH, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_DAEPYO_CAR, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_MINAP_TOTAL_COUNT, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_MINAP_TOTAL_MONEY, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_BIZ_NO, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_BIZ_NO_SIXMONTH, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_BIZ_NAME, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_BIZ_NAME_SIXMONTH, "");

        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_TYPE, "");
        common.setPrefString(getApplicationContext(), Configuration.SHARED_USER_TYPE_SIXMONTH, "");
    }

    *//*
    //선택된 차량번호 가져오기(선택되어있던 차량번호)
    public String getSelectedCarNo() {
        return Common.nullCheck(common.getPrefString(getApplicationContext(), Configuration.SHARED_SELECTED_CAR));
    }

    //선택할 차량번호 설정하기(이전에 사용했던 차량번호 저장)
    public void setSelectedCarNo(String car_no) {
        common.setPrefString(getApplicationContext(), Configuration.SHARED_SELECTED_CAR, car_no);
    }
    *//*

    //BaseActivity 를 상속받은 공통 통신 모듈
    public class Action extends AsyncTask<String, Void, String> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
//        ProgressDialog progressDialog;
        CommonProgress progressDialog = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteOutputStream = null;

        String jsonObject = null;

        String primitive = "";
        String url = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (activity != null && activity.isDestroyed() == false && !(activity instanceof SplashActivity) && !primitive.equals(Configuration.JEGUBUN_EMERGENCY_NOTICE) ) {

                progressDialog = new CommonProgress(activity);
                progressDialog.show();

                if(activity instanceof SplashActivity ||
                        activity instanceof MinapJohoiActivity_Back ||
                        activity instanceof MinapJohoiActivity_Home ||
                        activity instanceof NapbuHistoryActivity_Back ||
                        activity instanceof NapbuHistoryActivity_Home ||
                        activity instanceof MypageActivity ||
                        activity instanceof MypageCarActivity ||
                        activity instanceof MypageHipassCardActivity ||
                        activity instanceof MyPageObuActivity_Home ||
                        activity instanceof MyPageObuActivity_Back ||
                        activity instanceof MinapCardNapbuActivity ||
                        activity instanceof MinapNapbuBankingActivity ||
                        activity instanceof AddSMSActivity ||
                        activity instanceof JoinBusinessStep2Activity ||
                        activity instanceof JoinBusinessStep3Activity ||
                        activity instanceof JoinBusinessStep4Activity ||
                        activity instanceof CardDetailActivity ||
                        activity instanceof CardReceiptActivity ||
                        activity instanceof NapbuReceiptActivity)
                {
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                }

                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        String closeStr = "";
                        progressDialog.dismiss();
                        progressDialog = null;
                        *//*
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                Log.e("", "에러");
                            }
                            closeStr = closeStr+"is";
                        }

                        if (byteOutputStream != null) {
                            try {
                                byteOutputStream.close();
                            } catch (IOException e) {
                                Log.e("", "에러");
                            }
                            closeStr = closeStr+"baos";
                        }

                        if (conn != null) {
                            conn.disconnect();
                            closeStr = closeStr+"conn";
                        }
                        *//*
                        cancel(true);//AsyncTask 종료

                        //Toast.makeText(activity, "cancelListener on "+closeStr, Toast.LENGTH_SHORT).show();

                    }
                });
//                progressDialog = ProgressDialog.show(activity, "", "로딩중...", true);
            }
        }

        // primitive 를 통해 작업 내용 구분짓는다, URL을 구분짓는다.
        public Action(String primitive, String url) {
            this.primitive = primitive;
            this.url = url;
            *//**
             * 현재시간 저장
             *//*
            long nCurrentTime = SystemClock.elapsedRealtime();  //현재시간
            common.setPrefLong(BaseActivity.this, Configuration.SHARED_LAST_TOUCH_TIME, nCurrentTime);
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                StringBuffer body = new StringBuffer();
                body.append(url);

                //안드로이드 6.0 이하 암호화 불가(encversion 파라미터 제외)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    int xxx = 0;
                    xxx = 10;
                }
                else
                {
                    if(body.toString().contains("notice_request_emergency.jsp") || body.toString().contains("notice_request.jsp")){
                        body.append("?encversion=2");
                        body.append("&package_name="+BaseActivity.this.getApplicationContext().getPackageName());
                    }
                }

                *//**
                 * 서버 요청 코드
                 * appendUrl
                 *//*
                String appendUrl = "";
                //특정 Req URL일 경우 appenUrl 제외
                if(primitive.contains(Configuration.JEGUBUN_EMERGENCY_NOTICE) == false)
                {
                    appendUrl = "&user_type="+common.getPrefString(BaseActivity.this, Configuration.SHARED_USER_TYPE) +"&biz_no="+common.getPrefString(BaseActivity.this, Configuration.SHARED_BIZ_NO)+"&mid_mgr_id="+common.getPrefString(BaseActivity.this, Configuration.SHARED_USER_ID);
                    body.append(appendUrl);
                    appendUrl = "&biz_name="+ URLEncoder.encode(common.getPrefString(BaseActivity.this, Configuration.SHARED_BIZ_NAME), "euc-kr");
                    body.append(appendUrl);
                    body.append("&package_name="+BaseActivity.this.getApplicationContext().getPackageName());
                }

                *//**
                 * 네트웍구간 암호화 공통
                 *//*
                *//*
                appendUrl = "&encversion=2";
                body.append(appendUrl);
                *//*

                URL url = new URL(Common.nullCheck(body.toString()));//운영
                Log.i(TAG, TAG + "URL : = " + body.toString());
                String strBody = body.toString();

                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                if(body.toString().contains(Configuration.JEGUBUN_RESULT_PAGE)){//미납요금 납부내역 타임아웃 180초
                    conn.setConnectTimeout(180000);
                    conn.setReadTimeout(180000);
                }
                else if(body.toString().contains(Configuration.JEGUBUN_RECEIPTNAPBU)){//미납요금 납부내역 출력 타임아웃 180초
                    //conn.setConnectTimeout(180000);
                    //conn.setReadTimeout(180000);
                }
                else if(body.toString().contains(Configuration.JEGUBUN_VERIFY_SIGN_VID_REQUEST)){

                    if(body.toString().contains("vid_msg"))
                    {
                        //Log.e(TAG, TAG + "URL : = " + body.toString());
                    }
                }
                else{

                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(15000);
                }

                conn.setRequestProperty("Cache-Control", "no-cache");
                // conn.setDoOutput(true);
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                //Log.d(TAG, TAG + " ACTION responsecode  " + responseCode + "----" + conn.getResponseMessage());
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    inputStream = conn.getInputStream();
                    byteOutputStream = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;

                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        byteOutputStream.write(byteBuffer, 0, nLength);
                    }
                    byteData = byteOutputStream.toByteArray();
                    String response = new String(byteData, "euc-kr");
                    //Log.d("", TAG + "responseData  = " + response);

                    if (response == null || response.equals("")) {
                        Log.e(TAG, TAG + "Response is NULL!!");
                    } else {
                        if (response.trim().equals("")) {
                            Log.e(TAG, TAG + "Response is NULL!!");
                        }
                    }

                    *//**
                     * thkang
                     * 불필요 코드
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
                     *//*

                    jsonObject = response;
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("", "에러");

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("", "에러");
                    }
                }

                if (byteOutputStream != null) {
                    try {
                        byteOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("", "에러");
                    }
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
            return jsonObject;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //Log.d("",TAG+"onProgressUpdate!!");
        }

        @Override
        protected void onPostExecute(String result) {
            if (activity != null && activity.isDestroyed() == false && progressDialog != null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            if (primitive.equals(JEGUBUN_MENUNOTICE)) {
                onActionPostMenuNotice(primitive, result);
            }else {
                if (activity != null && activity.isDestroyed() == false)
                {
                    onActionPost(primitive, result);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Log.d("",TAG+"onCancelled!!");
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            //Log.d("",TAG+"onCancelled String s!!");
        }
    }

    private boolean areYouMember(){
        if(isMember == false){
            Toast.makeText(activity, "회원가입후 이용해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        }
        return isMember;
    }

    *//**
     * 환불통행료 전화걸기 권한
     * @return
     *//*
    private boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);

        if (result1 != 0) {
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {

                *//**
                 * 환불통행료 전화걸기 권한
                 *//*
                if (grantResults.length == 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED ) {

                    allActivityFinish();
                    //Intent intent = new Intent(this, RefundCarActivity.class);
                    Intent intent = new Intent(this, RefundActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "권한 동의 후 이용하실 수 있습니다.", Toast.LENGTH_LONG).show();
                }
            }
            break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            *//**
             * 정보 변경
             *//*
            case R.id.btn_menu_user_info:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                allActivityFinish();
                intent = new Intent(this, UserInfoChangeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            *//**
             * 환불 통행료
             *//*
            case R.id.btn_menu_refund:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                allActivityFinish();
                intent = new Intent(this, RefundActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                *//*
                if (checkPermission())
                {
                    allActivityFinish();
                    intent = new Intent(this, RefundActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CALL_PHONE
                    }, PERMISSION_REQUEST_CODE);
                }
                *//*
                break;

            *//**
             * 로그아웃btn_top_logout
             *
             *//*
            case R.id.btn_top_logout:
            case R.id.btn_main_logout:
                //alert.cancel();
                if(!areYouMember()){
                    break;
                }
                showAlertDialog(getString(R.string.a_logout_alert), getString(R.string.a_logout), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (alert != null) {
                            alert.cancel();
                        }
                        allActivityFinish(true);

                        *//**
                         * 지문 로그인일경우 회원정보 삭제 하지 않는다.
                         *//*
                        String isFingerLogin = common.getPrefString(BaseActivity.this, Configuration.SHARED_FINGERPRING_AUTH);

                        if(isFingerLogin.equals("") || isFingerLogin.equals("false")) {
                            deleteUserInfo();
                        }

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }, getString(R.string.a_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                break;

            *//**
             * 홈버튼
             *//*
            case R.id.btn_top_home:
                if(!areYouMember()){
                    break;
                }
                //intent = new Intent(this, MainActivity.class);
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                //모든 activity 종료
                allActivityFinish();
                break;

            *//**
             * Back 버튼
             *//*
            case R.id.btn_top_back:*//*백 버튼*//*
                onBackPressed();
                break;

            *//**
             * 메뉴 팝업
             *//*
            case R.id.btn_top_menu:*//*슬라이드 메뉴버튼*//*
            case R.id.btn_main_menu:
//                getMenuNotice();
                showMenu("");
                break;

            case R.id.btn_bottom_card:*//*하단 하이패스카드*//*
            case R.id.btn_menu_hipass:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                allActivityFinish();
                intent = new Intent(this, CardHistoryActivity_Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            *//**
             * 미납요금 조회 홈버튼 화면
             *//*
            case R.id.btn_bottom_minap:*//*하단 미납요금 *//*
            case R.id.btn_minap_johoi_home: *//*미납요금의 미납요금 조회탭*//*
            case R.id.btn_menu_minap:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                allActivityFinish();
                intent = new Intent(this, MinapJohoiActivity_Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            *//**
             * 미납요금 납부내역 홈버튼 화면
             *//*
            case R.id.btn_minap_napbu_history_home:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                allActivityFinish();
                intent = new Intent(this, NapbuHistoryActivity_Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            *//**
             * 미납요금 조회 백버튼 화면
             *//*
            case R.id.btn_minap_johoi_back:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                //allActivityFinish();
                napbuBackActivityFinish();
                intent = new Intent(this, MinapJohoiActivity_Back.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            *//**
             * 미납요금 납부내역 백버튼 화면
             *//*
            case R.id.btn_minap_napbu_history_back:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                //allActivityFinish();
                napbuBackActivityFinish();
                intent = new Intent(this, NapbuHistoryActivity_Back.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            *//**
             * 약정서비스 사용내역 화면
             *//*
            case R.id.btn_contract_use:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                intent = new Intent(this, ContractUseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            *//**
             * 약정서비스 납부내역 화면
             *//*
            case R.id.btn_contract_napbu:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                intent = new Intent(this, ContractNapbuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            case R.id.btn_popup_confirm:*//*가상계좌 확인*//*
            case R.id.btn_popup_close:*//*팝업 close*//*
            case R.id.btn_popup_cancel:
            case R.id.btn_top_close:
                alert.cancel();
                break;

            case R.id.btn_bottom_nfc:
            case R.id.btn_menu_nfc:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                allActivityFinish();
                intent =  new Intent(this, NfcActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                //Toast.makeText(this, "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_menu_service:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                if (this.getClass() != AddServiceActivity.class) {
                    allActivityFinish();
                    intent = new Intent(this, AddServiceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;

            case R.id.btn_bottom_mypage: *//*하단 마이페이지*//*
            case R.id.btn_menu_mypage:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                if (this.getClass() != MypageActivity.class) {
                    allActivityFinish();
                    intent = new Intent(this, MypageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;

            *//*하이패스 단말기(OBU)*//*
            case R.id.btn_menu_obu:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                if (this.getClass() != MyPageObuActivity_Home.class) {

                    allActivityFinish();
                    intent = new Intent(this, MyPageObuActivity_Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;

            case R.id.btn_menu_help:
                if(!areYouMember()){
                    break;
                }
                if(alert != null) {
                    alert.cancel();
                }

                if (this.getClass() != UseGuideActivity.class) {

                    allActivityFinish();
                    intent = new Intent(this, UseGuideActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }

    //new Action 실행 후 결과값 받는 부분 (각각 BaseActivity 를 상속받은 Activity -> onActionPost 에서 받게 된다.)
    protected abstract void onActionPost(String primitive, String result);

    *//**
     * 2017-07-20 LSH Add
     *//*
    private static final int CUSTOM_DIALOG_MENU = 1900;
    private static final int CUSTOM_DIALOG_BASIC = 1901;

    public static final int REQUEST_CARD = 9001;
    public static final int REQUEST_BANKING = 9002;
    public static final int REQUEST_AUTO = 9003;
    public static final int REQUEST_CREDITCARD =9004;
    public static final int REQUEST_NFC = 9005;

    //삭제할 activity 관리
    public static ArrayList<Activity> activityList = new ArrayList<Activity>();

    //
    public static ArrayList<Activity> minapActivityList = new ArrayList<Activity>();

    public static ArrayList<Activity> minapBackActivityList = new ArrayList<Activity>();

    //전체 activity 삭제
    public void allActivityFinish() {
        this.allActivityFinish(false);
    }

    public void allActivityFinish(Boolean isMainFinish) {
        for (int i = 0; i < activityList.size(); i++) {
            if (isMainFinish || (!isMainFinish && activityList.get(i).getClass() != MainActivity.class)) {
                activityList.get(i).finish();
            }
        }
    }

    public void napbuBackActivityFinish(){
        for (int i = 0; i < minapBackActivityList.size(); i++) {
            minapBackActivityList.get(i).finish();
        }
    }

    public void napbuActivityFinish(){
        for (int i = 0; i < minapActivityList.size(); i++) {
            minapActivityList.get(i).finish();
        }
    }

    //Alert Util - custom dialog
    public AlertUtil alert = new AlertUtil(this);
    public AlertUtil getAlertUtil() {
        return alert;
    }
    //공지사항 처리
    public AlertUtil alert2 = new AlertUtil(this);  //공지사항 용
    public AlertUtil getAlertUtil2() {
        return alert2;
    }

    private void getMenuNotice() {
        Parameters params = new Parameters("");
        params.put("jegubun", Configuration.JEGUBUN_MENUNOTICE );
        params.put("appcd", "1" );
        params.put("logkey", "");
        new Action(JEGUBUN_MENUNOTICE, Configuration.NOTICE_JSP+"?"+params.toString()).execute();
    }

    private void onActionPostMenuNotice(String primitive, String result) {
        noticeStr = result;
//        showMenu(result);
    }

    //show menu

    *//**
     * 메인 메뉴팝업
     * @param strMenuNotice
     *//*
    private void showMenu(String strMenuNotice) {

        final View view = LayoutInflater.from(this).inflate(R.layout.activity_popup_menu, null);

        final TextView txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
        final TextView txt_version = (TextView) view.findViewById(R.id.txt_version);

        txt_user_name.setText(Html.fromHtml(String.format(getString(R.string.a_menu_user_new), common.getPrefString(this, Configuration.SHARED_USER_NAME))));
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            e.printStackTrace();
        }
        txt_version.setText("버전:" + info.versionName);

        //layout_menu_alarm.setVisibility(View.GONE);
        if (!StringUtil.isEmptyString(noticeStr)) {

            JSONArray jsonArrayMenu = null;
            try {

                jsonArrayMenu = new JSONArray(noticeStr);
                if (jsonArrayMenu.length() > 0) {

                    for (int i = 0; i < jsonArrayMenu.length(); i++) {
                        JSONObject jsonObjectMenu = jsonArrayMenu.getJSONObject(i);

                        final String strNotiTitle = jsonObjectMenu.getString("title");
                        final String strNotiContent = jsonObjectMenu.getString("content");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Button btn_top_close = view.findViewById(R.id.btn_top_close);
        btn_top_close.setOnClickListener(this);

        //ImageView btn_menu_logout = (ImageView) view.findViewById(R.id.btn_menu_logout);
        //btn_menu_logout.setOnClickListener(this);

        //final LinearLayout layout_menu_minap_johoi = (LinearLayout) view.findViewById(R.id.layout_menu_minap_johoi);
        Button btn_menu_minap = view.findViewById(R.id.btn_menu_minap);
        btn_menu_minap.setOnClickListener(this);

        Button btn_menu_hipass = view.findViewById(R.id.btn_menu_hipass);
        btn_menu_hipass.setOnClickListener(this);

        Button btn_menu_refund = view.findViewById(R.id.btn_menu_refund);
        btn_menu_refund.setOnClickListener(this);

        Button btn_menu_user_info = view.findViewById(R.id.btn_menu_user_info);
        btn_menu_user_info.setOnClickListener(this);

        Button btn_menu_nfc = view.findViewById(R.id.btn_menu_nfc);
        btn_menu_nfc.setOnClickListener(this);

        Button btn_menu_service = view.findViewById(R.id.btn_menu_service);
        btn_menu_service.setOnClickListener(this);

        Button btn_menu_mypage = view.findViewById(R.id.btn_menu_mypage);
        btn_menu_mypage.setOnClickListener(this);

        Button btn_menu_obu = view.findViewById(R.id.btn_menu_obu);
        btn_menu_obu.setOnClickListener(this);

        Button btn_menu_help = view.findViewById(R.id.btn_menu_help);
        btn_menu_help.setOnClickListener(this);

        *//**
         * 미납요금 조회
         *//*
        if (this.getClass() == MinapJohoiActivity_Home.class) {
            btn_menu_minap.setSelected(true);
        }

        *//**
         * 미납요금 납부내역
         *//*
        if (this.getClass() == NapbuHistoryActivity_Home.class) {
            btn_menu_minap.setSelected(true);
        }

        *//**
         * 카드사용내역
         *//*
        if (this.getClass() == CardHistoryActivity_Home.class) {
            btn_menu_hipass.setSelected(true);
        }

        *//**
         * 환불통행료 조회
         *//*
        if (this.getClass() == RefundActivity.class) {
            btn_menu_refund.setSelected(true);
        }

        *//**
         * 부가서비스 신청
         *//*
        if (this.getClass() == AddServiceActivity.class) {
            btn_menu_service.setSelected(true);
        }

        *//**
         * 마이페이지
         *//*
        if (this.getClass() == MypageActivity.class) {
            btn_menu_mypage.setSelected(true);
        }

        *//**
         * 정보변경
         *//*
        if (this.getClass() == UserInfoChangeActivity.class) {
            btn_menu_user_info.setSelected(true);
        }

        *//**
         * 하이패스 단말기(OBU)
         *//*
        if (this.getClass() == MyPageObuActivity_Home.class) {
            btn_menu_obu.setSelected(true);
        }

        *//**
         * 이용가이드
         *//*
        if (this.getClass() == UseGuideActivity.class) {
            btn_menu_help.setSelected(true);
        }

        *//**
         * 선불카드
         *//*
        if (this.getClass() == NfcActivity.class) {
            btn_menu_nfc.setSelected(true);
        }

        showMenuAnim(getAlertUtil(), view, true);
    }

    *//**
     * custom dialog 이용해서 Animation 메뉴 호출
     *
     * @param alert
     * @param customView
     * @param isCancelable
     *//*
    private void showMenuAnim(AlertUtil alert, View customView, boolean isCancelable) {
        this.showCustomDialog(alert, customView, isCancelable, CUSTOM_DIALOG_MENU);
    }

    *//**
     * custom dialog
     *
     * @param alert
     * @param customView
     * @param isCancelable
     *//*
    public void showCustomDialog(AlertUtil alert, View customView, boolean isCancelable) {
        this.showCustomDialog(alert, customView, isCancelable, CUSTOM_DIALOG_BASIC);
    }

    public void showCustomDialog(AlertUtil alert, View customView, boolean isCancelable, int dlgType) {
        Dialog dlg = new Dialog(this, R.style.CustomAlertNoFrame);

        if (dlgType == CUSTOM_DIALOG_MENU) {
            dlg.getWindow().setWindowAnimations(R.style.menu_animation);
        }

        dlg.setContentView(customView);
        dlg.setCancelable(isCancelable);
        dlg.setCanceledOnTouchOutside(isCancelable);
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dlg.getWindow().getAttributes());

        switch (dlgType) {
            case CUSTOM_DIALOG_MENU:    //팝업 메뉴
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.RIGHT;
                break;

            case CUSTOM_DIALOG_BASIC:     //기본 팝업
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                break;

            default:
                break;
        }

        dlg.getWindow().setAttributes(lp);
        alert.showDialog(dlg);
    }

    //로그인, 비밀번호 변경시 사용
    public void showWarningDialog(Context context, String positiveStr, String negativeStr, String title, String content){
        CustomWarningDialog dlg = new CustomWarningDialog(context, positiveStr, negativeStr, title, content);
        dlg.show();
    }

    //카드 카드등록 유의사항
    public void showWarningDialogCard(Context context, String positiveStr, String negativeStr, String title, String content){
        CustomAlertDialogCard dlg = new CustomAlertDialogCard(context, positiveStr, negativeStr, title, content);
        dlg.show();
    }

    //환불금액 메인 페이지 팝업창
    public void showWarningDialogRefund(Context context, String positiveStr, String negativeStr, String title, String content){
        CustomAlertDialogRefund dlg = new CustomAlertDialogRefund(context, positiveStr, negativeStr, title, content);
        dlg.show();
    }

    *//**
     * Android 7.0 (Nougat) DatePickerDialog spinners
     *
     * @param context
     * @param listener
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     *//*
    public static DatePickerDialog createDatePickerDialog(Context context
            , DatePickerDialog.OnDateSetListener listener, int year, int month, int dayOfMonth) {
        return createDatePickerDialog(context, listener, year, month, dayOfMonth, null, null);
    }

    public static DatePickerDialog createDatePickerDialog(Context context
            , DatePickerDialog.OnDateSetListener listener, int year, int month, int dayOfMonth, Calendar minDate, Calendar maxDate) {

        Context contextThemeWrapper = new ContextThemeWrapper(
                context,
                android.R.style.Theme_Holo_Light_Dialog
        );

        if (Build.VERSION.SDK_INT >= 24) {
            return new NougatDatePickerDialog(contextThemeWrapper, listener, year, month, dayOfMonth, minDate, maxDate);
        } else {
            DatePickerDialog datePickerDialog = new DatePickerDialog(contextThemeWrapper, listener, year, month, dayOfMonth);

            if (minDate != null) {
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            }

            if (maxDate != null) {
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            }

            return datePickerDialog;
        }
    }

    public void showAlertDialog(String message, String positiveButton, DialogInterface.OnClickListener positiveListener, String negativeButton, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
        mAlert.setMessage(message);
        mAlert.setPositiveButton(positiveButton, positiveListener);
        mAlert.setNegativeButton(negativeButton, negativeListener);
        mAlert.show();
    }

    public void showPopupDialog_Creditcard(String title, String message, String positiveButton, View.OnClickListener positiveListener, String negativeButton, View.OnClickListener negativeListener, CompoundButton.OnCheckedChangeListener chklistener) {

        final View view = LayoutInflater.from(this).inflate(R.layout.layout_popup_alert2, null);

        final LinearLayout layout_popup_top = (LinearLayout) view.findViewById(R.id.layout_popup_top);
        final ImageView btn_popup_close = (ImageView) view.findViewById(R.id.btn_popup_close);
        final TextView textview_popup_title = (TextView) view.findViewById(R.id.textview_popup_title);
        final TextView textview_popup_content = (TextView) view.findViewById(R.id.textview_popup_content);
        final LinearLayout layout_popup_check = (LinearLayout) view.findViewById(R.id.layout_popup_check);
        final CheckBox chk_edcardsave = (CheckBox) view.findViewById(R.id.chk_edcardsave);

        final TextView btn_popup_cancel = (TextView) view.findViewById(R.id.btn_popup_cancel);
        final TextView btn_popup_confirm = (TextView) view.findViewById(R.id.btn_popup_confirm);

        chk_edcardsave.setOnCheckedChangeListener(chklistener);

        if (StringUtil.isEmptyString(title)) {
            layout_popup_top.setVisibility(View.GONE);
        } else {
            textview_popup_title.setText(title);
            btn_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert2.cancel();
                }
            });
        }

        textview_popup_content.setText(message);

        if (StringUtil.isEmptyString(positiveButton)) {
            btn_popup_cancel.setVisibility(View.GONE);
        } else {
            btn_popup_cancel.setText(positiveButton);
            if (positiveListener != null) {
                btn_popup_cancel.setOnClickListener(positiveListener);
            } else {
                btn_popup_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert2.cancel();
                    }
                });
            }
        }

        if (!StringUtil.isEmptyString(negativeButton)) {
            btn_popup_confirm.setText(negativeButton);
        }
        if (negativeListener != null) {
            btn_popup_confirm.setOnClickListener(negativeListener);
        } else {
            btn_popup_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert2.cancel();
                }
            });
        }

        showCustomDialog(getAlertUtil2(), view, true);
    }

    public void showPopupDialog(String title, String message, String positiveButton, View.OnClickListener positiveListener, String negativeButton, View.OnClickListener negativeListener) {

        final View view = LayoutInflater.from(this).inflate(R.layout.layout_popup_alert, null);

        final LinearLayout layout_popup_top = (LinearLayout) view.findViewById(R.id.layout_popup_top);
        final ImageView btn_popup_close = (ImageView) view.findViewById(R.id.btn_popup_close);
        final TextView textview_popup_title = (TextView) view.findViewById(R.id.textview_popup_title);
        final TextView textview_popup_content = (TextView) view.findViewById(R.id.textview_popup_content);

        final TextView btn_popup_cancel = (TextView) view.findViewById(R.id.btn_popup_cancel);
        final TextView btn_popup_confirm = (TextView) view.findViewById(R.id.btn_popup_confirm);

        if (StringUtil.isEmptyString(title)) {
            layout_popup_top.setVisibility(View.GONE);
        } else {
            textview_popup_title.setText(title);
            btn_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert2.cancel();
                }
            });
        }

        textview_popup_content.setText(message);

        if (StringUtil.isEmptyString(positiveButton)) {
            btn_popup_cancel.setVisibility(View.GONE);
        } else {
            btn_popup_cancel.setText(positiveButton);
            if (positiveListener != null) {
                btn_popup_cancel.setOnClickListener(positiveListener);
            } else {
                btn_popup_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert2.cancel();
                    }
                });
            }
        }

        if (!StringUtil.isEmptyString(negativeButton)) {
            btn_popup_confirm.setText(negativeButton);
        }
        if (negativeListener != null) {
            btn_popup_confirm.setOnClickListener(negativeListener);
        } else {
            btn_popup_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert2.cancel();
                }
            });
        }

        showCustomDialog(getAlertUtil2(), view, true);
    }

    public void showSelectDialog(Object items, int checkedItem, DialogInterface.OnClickListener selectedListener) {
        showSelectDialog(items, checkedItem, "", selectedListener, "", null, "", null);
    }

    public void showSelectDialog(Object items, int checkedItem, String title, DialogInterface.OnClickListener selectedListener, String positiveName,
                                 DialogInterface.OnClickListener positiveListener, String negativeName, DialogInterface.OnClickListener negativeListener) {

        if (items != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setSingleChoiceItems((CharSequence[]) items, checkedItem, selectedListener).create();

            alertDialog.setTitle(title);
            alertDialog.setPositiveButton(positiveName, positiveListener);
            alertDialog.setNegativeButton(negativeName, negativeListener);

            alertDialog.setCancelable(true);

            alertDialog.show();
        }
    }

    //키패드 숨기기
    public void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && this.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}*/

