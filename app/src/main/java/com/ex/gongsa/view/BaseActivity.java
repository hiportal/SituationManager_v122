package com.ex.gongsa.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.ex.gongsa.utils.HandlerThreadInGongsa;
import com.ex.situationmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import static com.ex.gongsa.Configuration.SERVER_URL;
import static com.ex.gongsa.view.WorkPlanRegisterActivity.codeMap;
import static java.security.AccessController.getContext;


public abstract class BaseActivity extends Activity implements OnClickListener {
    private String TAG = "BaseActivity";
    // String url ="http://localhost:8080/TestProjectWeb/index.jsp";
    /*String url ="http://192.168.10.85:8080/TestProjectWeb/index.jsp";*/
    /*String url ="http://192.168.10.85:8080/sht_webapp/index.jsp";*/
    /*  String url = "http://192.168.10.85:8080/index.jsp";//main*/
    /*  String url = "http://210.103.95.3:8070/gongsaManagement.do";//main*/
    //   String url = "http://192.168.1.116:8080/gongsaManagement.do";//개발서버

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gongsaPrev:
                finish();
        }
    }

    //서버 통신
    public class Action extends AsyncTask<String, String, String> {
        String parameter = "";
        String primitive = "";
        String jegubun = "";
        File file = null;
        String urlString = null;

        Context context;
        Intent intent;

        ProgressDialog progressDialog;
        Thread thread;
        private boolean isGPSEnabled = false;
        private boolean isScrolled = false;
        HandlerThreadInGongsa handler;
        private String userInfo;
        public Action(Context context){
            this.context = context;
        }
        public Action(String method, File file, String urlString, String parameter,Context context,Intent intent) {
            this.primitive = method;
            this.file = file;
            this.urlString = urlString;
            this.parameter = parameter;
            this.context= context;
            this.intent = intent;

            Log.i("12__3","1,,,"+urlString);
            //      Log.i("Action Contructor",file.getAbsolutePath());
            //     Log.i("Action Contructor",file.getName());
        }//end Constructor

        public Action(String primitive, String urlString, String parameter, Context context,boolean isScrolled,String userInfo) {
            this.primitive = primitive;
            this.urlString = urlString;
            this.parameter = parameter;
            this.context = context;
            this.isScrolled = isScrolled;
            this.userInfo= userInfo;

            //스크롤 기능
            if(isScrolled == true){
                handler = new HandlerThreadInGongsa("Handler",1,context,userInfo);
                handler.start();
            }

        }

        public Action(String primitive, String urlString, String parameter, Context context) {
            this.primitive = primitive;
            this.urlString = urlString;
            this.parameter = parameter;
            this.context = context;
            Log.i("12__3",urlString);
           /* if(urlString.equals(SERVER_URL + "/TodayWorkPlan/todayWorkRegisterlist.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                Log.i("12__3","생성자 이푸");
            }*/
        }



        public Action(String primitive, String urlString, String parameter, Context context,ProgressDialog progressDialog) {
            this.primitive = primitive;
            this.urlString = urlString;
            this.parameter = parameter;
            this.context = context;
            this.progressDialog = progressDialog;
            this.progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setCanceledOnTouchOutside(false);
            //Log.i("12__3","1");
            Log.i("12__4",urlString);
        /*    if(urlString.equals(SERVER_URL + "/TodayWorkPlan/todayWorkRegisterlist.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                Log.i("12__3","생성자 이푸");
            }*/
        }


        public Action(String jegubun, String primitive, String urlString, String parameter, Context context,Intent intent) {
            this.jegubun = jegubun;
            this.primitive = primitive;
            this.urlString = urlString;
            this.parameter = parameter;
            this.context = context;
            this.intent = intent;
        }

        public Action(String primitive, String urlString, String parameter, Context context,Intent intent) {
            this.primitive = primitive;
            this.urlString = urlString;
            this.parameter = parameter;
            this.context = context;
            this.intent = intent;
        }

        public Action(String primitive, String urlString, String parameter, Context context,Intent intent,String userInfo) {
            this.primitive = primitive;
            this.urlString = urlString;
            this.parameter = parameter;
            this.context = context;
            this.intent = intent;
            this.userInfo = userInfo;
        }

        public Action(String primitive, String urlString, String parameter ) {
            this.primitive = primitive;
            this.urlString = urlString;
            this.parameter = parameter;

        }




        @Override
        protected void onPreExecute() {
            //gps 상태를 체크하는 로직을 넣을것
      //      Toast.makeText(context,"이거 실행",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
           ;
         //   Log.i("12__3","2");
//            chkGpsService();

            if (!chkGpsService()) {
                AlertDialog.Builder gsDialog = new AlertDialog.Builder(context);
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

            if(urlString.equals(SERVER_URL+"/gongsaManagement.do")){
                progressDialog = ProgressDialog.show(context, "", "전송중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else if(urlString.equals(SERVER_URL+"/WorkPlan/register.do")){
                progressDialog = ProgressDialog.show(context, "", "전송중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else if(urlString.equals(SERVER_URL+"/WorkPlan/update.do")){
                progressDialog = ProgressDialog.show(context, "", "전송중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else if(urlString.equals(SERVER_URL + "/TodayWorkPlan/todayWorkupdate.do")){
                progressDialog = ProgressDialog.show(context, "", "전송중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else  if(urlString.equals(SERVER_URL+"/TodayWorkPlan/todayWorkRegisterlist.do")){
                Log.i("다이얼로그","ㅠㅠ");
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else  if(urlString.equals(SERVER_URL + "/gongsaManageMentPicture.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else  if(urlString.equals(SERVER_URL + "/WorkPlan/List.do")){
                if(progressDialog == null){
                    progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                }

            }else  if(urlString.equals(SERVER_URL + "/TodayWorkPlan/List.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else  if(urlString.equals(SERVER_URL+"/TodayWorkPlan/insertTodayWorkPlan.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                //--
         //       Log.i("12__3","다이얼로그 켜짐");
            }else  if(urlString.equals(SERVER_URL+"/WorkPlan/loadWorkPlan.do")){
         //       Log.i("12__3","else if 179");
                try{
                    progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    Log.i("타이머 들어옴","11타이머 들어옴");
                    if(progressDialog.isShowing()){
                        Log.i("타이머","보임");
                    }else{
                        Log.i("타이머","안보임");
                    }
                }catch (NullPointerException e){
                    Log.e("에러","에러발생");
                }


            }else  if(urlString.equals(SERVER_URL+"/WorkPlan/guganOverLapCheck.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                Log.i("타이머 들어옴","22타이머 들어옴");
                Log.i("context",context.getClass().toString());
                progressDialog.show();
                if(progressDialog.isShowing()){
                    Log.i("타이머","보임");
                }else{
                    Log.i("타이머","안보임");
                }
            } else  if(urlString.equals(SERVER_URL+"/selectUser.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);


            }else if(urlString.equals(SERVER_URL+"/insertUser.do")){


            }else if(urlString.equals(SERVER_URL+"/deleteUser.do")){

                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else if(urlString.equals(SERVER_URL+"/WorkPlan/update.do")){
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }else if(urlString.equals(SERVER_URL + "/TodayWorkPlan/update.do")) {
                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }
//            }else  if(urlString.equals(SERVER_URL+"/TodayWorkPlan/WorkStatList.do")){
//                //202008_수정중
//                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
//                progressDialog.setCancelable(false);
//                progressDialog.setCanceledOnTouchOutside(false);
//            }else  if(urlString.equals(SERVER_URL+"/TodayWorkPlan/ingWork.do")){
//                //202008_수정중
//                progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
//                progressDialog.setCancelable(false);
//                progressDialog.setCanceledOnTouchOutside(false);
//            }

        }

        public boolean chkGpsService() {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

/*        public Action(String parameter, String TAG) {
            Log.i("성공", parameter + "---------------Constructor");
            this.parameter = parameter;
            this.primitive = primitive;
            Toast.makeText(getApplicationContext(),primitive,Toast.LENGTH_LONG).show();;
        }*/


        @Override
        protected String doInBackground(String... args0) {

            HttpURLConnection conn = null;
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            FileInputStream fis = null;
            DataOutputStream dos = null;
            OutputStream os = null;
            PrintWriter writer;
            String boundary = "^----^";
            String LINE_FEED = "\r\n";
            String charset = "UTF-8";
            JSONObject jsonresult = null;
            byte[] buffer;
            int maxBufferSize = 5 * 1024 * 1024;
            if (primitive.equals("get")) {
                try {
                    StringBuffer body = new StringBuffer();
                    body.append(urlString);
                    if (parameter != null) {
                        body.append("?param=");
                        body.append(URLEncoder.encode(URLEncoder.encode(parameter, "utf-8"), "UTF-8"));
                    }


               /*     if(!body.toString().equals("http://180.148.182.160:5004/TodayWorkPlan/gpsUpdater.do?latitude=0.0&longitude=0.0&telNo=010-6707-2504")){
                        Log.i("bodybody",body.toString());
                        Log.i("bodybody","파라미터:"+parameter);
                    }*/

                    Log.d("URL","base 391 URL = "+body.toString());
                    URL url = new URL(body.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setRequestProperty("Accept-Charset", "UTF-8");
                    conn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    conn.connect();


                    int responseCode = conn.getResponseCode();

                    // Log.i("responseCode", "responseCode:" + Integer.toString(HttpURLConnection.HTTP_OK));
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //   Log.i("responseCode", Integer.toString(HttpURLConnection.HTTP_OK));
                        is = conn.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte[] byteBuffer = new byte[1024];
                        byte[] byteData = null;
                        int nLength = 0;

                        while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                            baos.write(byteBuffer, 0, nLength);
                        }
                        byteData = baos.toByteArray();

                        String response = URLDecoder.decode(new String(byteData, "UTF-8"), "UTF-8");

                        Log.i("response", response);
                        return response;

                    } else {
                            Log.i("실패", ":통신에 실패");//if-else
                    }

                } catch ( IOException e) {
                    Log.e("에러","get에러발생");

                }//try - catch - finally

            } else {//file전송

                try {
                    URL url = new URL(urlString);
                    Log.d("URL","URL = "+urlString+parameter);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);//multipart/form-data;
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setConnectTimeout(30000);

                    os = conn.getOutputStream();//os = OutpustStream

                    writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);

                    //Body에 데이터를 넣어줘야 할 경우 없으면 패스
                    writer.append("--" + boundary).append(LINE_FEED);
                    writer.append("Content-Disposition:form-data; name=\"param\"").append(LINE_FEED);//문자열 파라미터의 이름을 설정하고//form-data;
                    writer.append("Content-Type: text/plain; charset=" + "UTF-8").append(LINE_FEED);
                    writer.append(LINE_FEED);
                    writer.append(parameter).append(LINE_FEED);//실제 문자열을 실어준다

                    writer.flush();
                    Log.i("parameter=",parameter);

                    //파일 데이터 넣는 부분
                    if (file != null) {

                        Log.i("TYpFromFIle",URLConnection.guessContentTypeFromName(file.getName()));
                        writer.append("--" + boundary).append(LINE_FEED);
                        writer.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"").append(LINE_FEED);
                        writer.append("Content-Type:" + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
                        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                        writer.append(LINE_FEED);
                        writer.flush();

                        fis = new FileInputStream(file);

                        buffer = new byte[(int) file.length()];
                        int bytesRead = -1;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        os.flush();
                        fis.close();
                        writer.append(LINE_FEED);
                        writer.flush();
                    }//파일이 있는 경우의 체크


                    writer.append("--" + boundary + "--").append(LINE_FEED);
                    writer.close();
                    Log.i("바디 전체 출력",writer.toString());
                    Log.i("커넥션 전체 출력",conn.getResponseMessage());
                    Log.e("테스트", Integer.toString(conn.getResponseCode()));
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        //Log.i("response",response.toString());

                        return response.toString();

                    }
                } catch (ConnectException e) {
                    e.printStackTrace();
                    Log.e("에러","post 에러발생");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("에러","post 에러발생");
                } catch(Exception e){
                    Log.e("에러","post 에러발생");
                }
            }//  if(!primitive.equals("CameraActivity")){
            return null;
        }//method doInBackground


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("onPostExecute","onPostExecute result = " + s + urlString);
            try {
                // Log.i("=","=======================");
                //  Log.i("성공","ㅇㅇ");
                // Log.i("=","=======================");
                Log.i("12__3","4");

//                Log.i("onActionPost","onActionPost onPostExecute = " +s);
                if(isScrolled == true){
                    handler.interruptHandler();
                }


                if(urlString.equals(SERVER_URL+"/gongsaManagement.do")){
                    progressDialog.dismiss();

                }else if(urlString.equals(SERVER_URL+"/WorkPlan/register.do")){
                    progressDialog.dismiss();
                    if(codeMap != null){
                        codeMap=null;
                    }

                    intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    context.startActivity(intent);
                }else if(urlString.equals(SERVER_URL+"/WorkPlan/update.do")){//
                    progressDialog.dismiss();
                    Log.i("testREturn",s.toString());
                    if(s.equals("fail")){
                        Toast.makeText(context,"수정에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"수정이 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                        intent = new Intent(context, WorkPlanDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("isAfterUpdate","Y");
                        intent.putExtra("userInfo",userInfo);
                        intent.putExtra("WorkPlanJsonValue",URLDecoder.decode(s,"UTF-8"));
                        context.startActivity(intent);
                    }


                }else if(urlString.equals(SERVER_URL + "/TodayWorkPlan/update.do")){//SERVER_URL + "/TodayWorkPlan/todayWorkupdate.do"
                    progressDialog.dismiss();
                    if(s.equals("fail")){
                        Toast.makeText(context,"수정에 실패하였습니다.",Toast.LENGTH_SHORT).show();

                    }if(s.equals("prosStatCdCode")){
                        Toast.makeText(context,"금일작업 수정 도중, 금일작업 처리 상태가 변경되어 수정이 불가능합니다.",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(context,"수정이 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                        Log.i("TestREturn",URLDecoder.decode(s,"UTF-8"));
                        intent = new Intent(context, TodayWorkPlanSelectDetailActivity.class);
                       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("isAfterUpdate","Y");
                        intent.putExtra("userInfo",userInfo);
                        intent.putExtra("isUpdated","true");
                        intent.putExtra("TodayWorkListJsonValue",URLDecoder.decode(s,"UTF-8"));
                        context.startActivity(intent);
                    }


                }else  if(urlString.equals(SERVER_URL+"/TodayWorkPlan/todayWorkRegisterlist.do")){
                    Log.i("TodayWorkPlan 통신 : ","dismiss");

                    progressDialog.dismiss();

                    intent = new Intent(context, TodayWorkPlanRegisterActivity.class);
                    intent.putExtra("workJsonWorkPlanList", s);
                    Log.i("String S", URLDecoder.decode(s, "UTF-8"));
                    Log.i("String S", s);
                    intent.putExtra("userInfo",parameter);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    Log.i("TodayWorkplan595",s);

                    startActivity(intent);
                }else  if(urlString.equals(SERVER_URL+"/gongsaManageMentPicture.do")){
                    progressDialog.dismiss();
                    intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else  if(urlString.equals(SERVER_URL + "/WorkPlan/List.do")){

                    Log.i("WorkPlan/List.do1111", URLDecoder.decode(s, "UTF-8"));
                    intent = new Intent(context, WorkPlanListActivity.class);
                    try{
                    /*    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("workJsonWorkPlanList", s.toString());
                        intent.putExtra("userInfo",parameter.toString());
                        startActivity(intent);*/

                        //최창유 주석
                        JSONObject job = new JSONObject(parameter);
                        Log.i("jsonObject","123:"+job.toString());
                        if(job.get("curPage").equals("1")&&job.get("initCheck")!= null &&job.get("initCheck").toString().equals("Y")){

                            Log.i("par123am","if");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("workJsonWorkPlanList", s.toString());
                            intent.putExtra("userInfo",parameter.toString());
                            startActivity(intent);
                        }else{
                            Log.i("par123am","else");
                            intent.putExtra("workJsonWorkPlanList", s.toString());
                            intent.putExtra("userInfo",parameter.toString());

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            onNewIntent(intent);
                            setIntent(intent);

                           // startActivity(intent);


                            //setIntent(intent);
                        /*    startActivityForResult(intent,RESULT_OK);
                            setResult(RESULT_OK);
                            finish();*/
                        }

                    }catch (NullPointerException e){
                        Log.i("par123am","catch");
                        Log.e("에러","에러발생");
                        intent.putExtra("workJsonWorkPlanList", s.toString());
                        intent.putExtra("userInfo",parameter.toString());
                        //startActivity(intent);
                    }
                    if(progressDialog!= null){
                                      progressDialog.dismiss();
                    }


                }else  if(urlString.equals(SERVER_URL + "/TodayWorkPlan/List.do")){
                    progressDialog.dismiss();
                     intent = new Intent(context, TodayWorkPlanSelectActivity.class);
                    intent.putExtra("workJsonWorkPlanList",s);
                    intent.putExtra("userInfo",parameter);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    Log.i("WorkPlan/List.do", s.toString());
;
            /*    intent.putExtra("UserPhoneNo",jsonObject.toString());
                intent.putExtra("UserBelongJisaAndBonbu",jsonArray.toString());*/
                    startActivity(intent);
                }else  if(urlString.equals(SERVER_URL+"/TodayWorkPlan/insertTodayWorkPlan.do")){
                    Log.i("12__3","디스미스");
                    progressDialog.dismiss();

                }else  if(urlString.equals(SERVER_URL+"/WorkPlan/loadWorkPlan.do")){
                    Log.i("타이머 들어옴","11타이머 꺼짐");

                    intent = new Intent(context, WorkPlanResisterListActivity.class);

                    intent.putExtra("list", s);
                    intent.putExtra("userInfo", parameter);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    progressDialog.dismiss();
                }else  if(urlString.equals(SERVER_URL+"/WorkPlan/guganOverLapCheck.do")){

                    Log.i("타이머 들어옴","22타이머 꺼짐");
              //      progressDialog.dismiss();
                    intent = new Intent(context, GuganOverLapListActivity.class);
                    intent.putExtra("list",s);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    progressDialog.dismiss();
                }else  if(urlString.equals(SERVER_URL+"/selectUser.do")){
                    progressDialog.dismiss();

                }else if(urlString.equals(SERVER_URL+"/insertUser.do")){

                    progressDialog.dismiss();

                }else if(urlString.equals(SERVER_URL+"/deleteUser.do")){

                    progressDialog.dismiss();

                }/*else if(urlString.equals(SERVER_URL+"/WorkPlan/update.do")){
                    Log.i("testREturn",URLDecoder.decode(s,"UTF-8"));
                    progressDialog.dismiss();
                    if(s.equals("mtnofStatCd")){
                        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                    }if(s.equals("fail")){
                        Toast.makeText(context,"수정이 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        String userInfo = intent.getStringExtra("userInfo");
                        Log.i("dsdfsdf",URLDecoder.decode(s,"UTF-8"));
                        intent = new Intent(context,WorkPlanDetailActivity.class);
                        intent.putExtra("WorkPlanJsonValue",URLDecoder.decode(s,"UTF-8"));
                        intent.putExtra("userInfo",userInfo);
                        intent.putExtra("isUpdated","true");
                        startActivity(intent);
                    }
                }*/

            } catch (NullPointerException| JSONException|UnsupportedEncodingException e) {
                //   onActionPost(primitive, "안됨");
                Log.e("에러","에러발생");
                e.printStackTrace();
            }
            Log.d("jegubun","jegubun " + jegubun);
            if(!"".equals(jegubun)){
                onActionPost(jegubun, s);
            }else{
                onActionPost(primitive, s);
            }

        }
    }//end Class Action


    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }*/
    public abstract String onActionPost(String primitive, String data);

    //public abstract String onActionPost(String primitive, String data,String type);









}//end class BaseActivity


//--
/*  Log.i("else에서 본 파일",file.getAbsolutePath());
          Log.i("else에서 본 파일",file.getName());

          try{
          URL url = new URL("http://210.103.95.3:8070/testFile.do");

          //파일
          conn = (HttpURLConnection)url.openConnection();
          conn.setRequestMethod("POST");
          conn.setReadTimeout(1000000);
          conn.setConnectTimeout(1000000);
          conn.setDoOutput(true);
          conn.setDoInput(true);
          conn.setUseCaches(false);

          conn.setRequestProperty("ENCTYPE","multipart/form-data");//tail = LINE_END + TWOHYPEN + boundary + TWOHYPEN + LINE_END;

          conn.setRequestProperty("Content-Type","multipart/form-data; boundary="+boundary);//String boundary ="^----^";


          String delimiter = "--"+boundary+LINE_FEED;
          StringBuffer postDataBuilder = new StringBuffer();

          //파일 실어준다
          postDataBuilder.append(delimiter);//String delimiter = "--"+boundary+LINE_FEED;
          postDataBuilder.append("Content-Disposition: form-data; name=\""+"file"+"\";filename=\""+file.getName()+"\""+boundary);//String LINE_FEED ="\r\n";
          postDataBuilder.append(delimiter);



          dos = new DataOutputStream(conn.getOutputStream());
          dos.write(postDataBuilder.toString().getBytes());

          if(file.exists()==true){//파일은 무조건 있으니까 의미없음
          Log.e("파일 상태",file.getName());
          Log.e("파일 상태",Long.toString(file.length()));
          dos.writeBytes(LINE_FEED);
          fis = new FileInputStream(file);

          buffer =  new byte[maxBufferSize];
          int length = -1;

          while((length = fis.read(buffer))!= -1){
          dos.write(buffer,0,length);
          }
          Log.e("파일 상태 렝스",Integer.toString(length));
          *//*
 *//*
          //   dos.writeBytes("--"+boundary+"--"+LINE_FEED);
          // dos.writeBytes(LINE_FEED);
          fis.close();
          }else{
          Log.i("Toast","파일 널");
          }

          Log.e("전송 끝나고 잘옴","mm");
          dos.flush();
          dos.close();
          //-------------


          //

          int responseCode = conn.getResponseCode();
          Log.e("-----","-------------------------");
          Log.e("헤더스필드",conn.getHeaderFields().toString());


          Log.e("-----","-------------------------");
          Log.e("리스펀스 코드",Integer.toString(responseCode));
          if(responseCode == 200){


          String line ="";

          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          while((line = br.readLine())!= null){
          risp += line;
          }
          }
          //파일 끝끝
          }catch (Exception e){
          Log.e("에러","에러발생");
          }finally {

          }

          return  risp;*/
