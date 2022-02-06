package com.ex.situationmanager;


import android.app.Activity;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.BaseActivity;
import com.ex.situationmanager.encrypt.SeedCipher;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import org.apache.commons.codec.binary.Base64;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ex.situationmanager.BaseActivity.GET_CCTV_URL_LIST;
import static com.ex.situationmanager.BaseActivity.INSERT_JUBBO_CONTENT;
import static com.ex.situationmanager.BaseActivity.ONECLICK_CARGPS_INSERT;
import static com.ex.situationmanager.BaseActivity.ONECLICK_NOTICE_SELECT;
import static com.ex.situationmanager.BaseActivity.UPDATE_JUBBO_CONTENT_INNEREMPLELOY;
import static com.ex.situationmanager.BaseActivity.URL_SENDGPS;
import static com.ex.situationmanager.BaseActivity.contextActivity;


public class InnerEmployeeSituationInsert extends Fragment implements View.OnClickListener {


    ImageView btnBack;
    TextView insert_jubbo_content;
    TextView situattion_insert_tv;
    String TAG="InnerEmployeeSituationInsert";
    ImageView btnUserInfo;
    TextView chogiwhat_tv;
    TextView detailContent;



    Fragment thisFragment = this;
    public InnerEmployeeSituationInsert(){

    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
     //   fragCheck=true;
        InnerEmployActivity.currentFragment=TAG;
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.println(Log.ASSERT,TAG,getArguments().get("rpt_id").toString());
        Log.println(Log.ASSERT,TAG,getActivity().getFragmentManager().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Toast.makeText(container.getContext(), "ㅡㅅ ㅡ", Toast.LENGTH_SHORT).show();
        InnerEmployActivity.currentFragment=TAG;


        View view = inflater.inflate(R.layout.inner_employee_situation_insert,container,false);
        insert_jubbo_content = view.findViewById(R.id.insert_jubbo_content);
        situattion_insert_tv =view.findViewById(R.id.situattion_insert_tv);
        TextView situattion_insert_tv= (TextView)view.findViewById(R.id.situattion_insert_tv);
       /* TextView detailTitle=(TextView)view.findViewById(R.id.detailTitle);*/
        //

       /* TextView detailJeopbo=(TextView)view.findViewById(R.id.detailJeopbo);*/
     /*   TextView detailType=(TextView)view.findViewById(R.id.detailType);
        TextView detailTime=(TextView)view.findViewById(R.id.detailTime);
        TextView detailState=(TextView)view.findViewById(R.id.detailState);*/
   /*     detailContent=(TextView)view.findViewById(R.id.detailContent);
        TextView detailJochi=(TextView)view.findViewById(R.id.detailJochi);*/

        EditText insert_jubbo_content = (EditText)view.findViewById(R.id.insert_jubbo_content);
        btnBack=(ImageView)view.findViewById(R.id.btnBack);
/*
        detailTitle.setText(getArguments().get("detailTitle").toString());
        //
        detailJeopbo.setText(getArguments().get("detailJeopbo").toString());
        detailType.setText(getArguments().get("detailType").toString());
        detailTime.setText(getArguments().get("detailTime").toString());
        detailState.setText(getArguments().get("detailState").toString());
        detailContent.setText(getArguments().get("detailContent").toString());
        detailJochi.setText(getArguments().get("detailJochi").toString());
*/

        btnBack.setOnClickListener(this);

         chogiwhat_tv = view.findViewById(R.id.chogiwhat_tv);
        chogiwhat_tv.setOnClickListener(this);
        situattion_insert_tv.setOnClickListener(this);

        ;
        chogiwhat_tv.setOnClickListener(this);
         btnUserInfo = view.findViewById(R.id.btnUserInfo);
        btnUserInfo.setOnClickListener(this);//          Parameters params = new Parameters(primitive);


        return view;
    }

    SeedCipher seed = new SeedCipher();
    public static byte[] szKey = {(byte) 0x88, (byte) 0xE3, (byte) 0x44,
            (byte) 0x8F, (byte) 0x28, (byte) 0x32, (byte) 0xFE, (byte) 0xF1,
            (byte) 0xF9, (byte) 0xF3, (byte) 0xF1, (byte) 0x37, (byte) 0xFF,
            (byte) 0xA4, (byte) 0x05, (byte) 0x29};

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.situattion_insert_tv:
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(insert_jubbo_content.getWindowToken(),0);
                String content = insert_jubbo_content.getText().toString();
                if(insert_jubbo_content.getText().length()!=0 || !insert_jubbo_content.getText().toString().equals("")){
                    Parameters params = new Parameters(UPDATE_JUBBO_CONTENT_INNEREMPLELOY);

                    try{
                        params.put("rpt_id",(String)getArguments().get("rpt_id"));
                        String encString = seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt(content, szKey))));

                        Log.i("ddddsldjf",getArguments().get("r_result").toString().replace(" ","\t"));
                        String oriEncContent = seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt((String)getArguments().get("r_result").toString().replace(" ","\t"), szKey))));



                        String  rpt_id = seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt((String)getArguments().get("rpt_id").toString().replace(" ","\t"), szKey))));
                        String  acdnt_id = seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt((String)getArguments().get("acdnt_id").toString().replace(" ","\t"), szKey))));
                        String  bscode = seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt((String)getArguments().get("bscode").toString().replace(" ","\t"), szKey))));
                        Log.i("ddddsldjf",oriEncContent);

                        // d.putString("bscodeName", Configuration.User.getBsname_list().get(0));
                        String  bscodeName = seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt((String)getArguments().get("bscodeName").toString().replace(" ","\t"), szKey))));
                        params.put("rpt_id",rpt_id);
                        params.put("bscodeName",bscodeName);
                        params.put("acdnt_id",acdnt_id);
                        params.put("bscode",bscode);
                        params.put("update_content",encString);
                        params.put("ori_content",oriEncContent);
                        Log.i("EncodeCheck123123",encString);
//                        EUC-KR"), "Cp1252")
                    }catch (NullPointerException e){
                        //e.printStackTrace();
                        Log.e("에러","예외");
                    }
                    new Action(UPDATE_JUBBO_CONTENT_INNEREMPLELOY,params).execute();
                   // Toast.makeText(getActivity().getApplicationContext(),params.getPrimitive(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"접보에 등록하실 내용을 입력해주세요.",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btnUserInfo:
                Log.d("InnerEmployeeSituat","InnerEmployeeSituationInsert btnUserInfo");


//		Toast.makeText(this,"눌림",Toast.LENGTH_SHORT).show();
                Intent userIntent = new Intent(getActivity().getApplicationContext(), DialogUserInfoActivity.class);
                userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, userIntent, PendingIntent.FLAG_ONE_SHOT);
                try {
                    pi.send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("에러","예외.예외");
                }
            break;
            case R.id.testBtn:
                break;
            case R.id.btnBack:
                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
               // getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                getActivity().getFragmentManager().popBackStack();
                break;

            case R.id.chogiwhat_tv:
             //   Toast.makeText(getActivity().getApplicationContext(),insert_jubbo_content.getText().toString(),Toast.LENGTH_SHORT).show();
                /*insert_jubbo_content.clearComposingText();*/
                insert_jubbo_content.setText("");
                break;
        }//switch
    }//onClick



    //*****************************************************************************************

    // 서버 통신
    public class Action extends AsyncTask<String, Void, XMLData> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
        ProgressDialog progressDialog;

        String primitive = "";
        Parameters params = null;

        XMLData returnData = null;
        Context context;


        // primitive 에 따라 URL을 구분짓는다.
        public Action(String primitive, Parameters params) {
            Log.println(Log.ASSERT,TAG,"생성자:"+primitive);
            this.primitive = primitive;
            this.params = params;

        }

        @Override
        protected void onPreExecute() {
           // progressDialog = ProgressDialog.show(context, "", "로딩중...", true);
            super.onPreExecute();
        }


        @Override
        protected XMLData doInBackground(String... arg0) {
            Log.println(Log.ASSERT,TAG,"doInBackground primitive 확인"+primitive);
            Log.println(Log.ASSERT,TAG,"doInBackground primitive 확인"+primitive);
            HttpURLConnection conn = null;
            XMLData xmlData = null;

            OutputStream os = null;
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            try {
                StringBuffer body = new StringBuffer();
//public static String URL_SENDGPS = "http://192.168.10.3:8080/proxy.jsp";
                //http://192.168.1.24:8080/test_proxy.jsp
                //운영용 파라미터 설정

                //test
                //String testUrl="http://oneclickapp.ex.co.kr:5000/test_proxy.jsp";
               // testUrl=URL_SENDGPS;
                //body.append("http://192.168.1.24:8080/test_proxy.jsp");
                //body.append(testUrl);
                body.append(URL_SENDGPS);
                body.append("?");
                body.append(params.toString());
                Log.println(Log.ASSERT,TAG,"doInBackground 바디123 확인:"+body.toString());
               // Log.i("EncodeCheck123123","http://192.168.1.24:8080/test_proxy.jsp?"+params.toString());
                //http://oneclickapp.ex.co.kr:5000/proxy.jsp
                //Log.i("EncodeCheck123123",testUrl+"?"+params.toString());
                URL url = new URL(new String(Common.nullTrim(body.toString()).getBytes("EUC-KR"), "Cp1252"));//운영용

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
                    //공지사항 인코딩이 다르다
                    if (primitive.equals(ONECLICK_NOTICE_SELECT)) {
                        response = new String(byteData, "utf-8").toString();
                    }
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
                        Log.println(Log.ASSERT,TAG," Response 확인:"+response);
                        xmlData = new XMLData(response);
                        returnData = xmlData;
                        String ret = xmlData.get("result");
                        Log.d("", TAG + " ret check = " + ret);
                        Log.println(Log.ASSERT,TAG,"result 확인: 확인:"+ret);
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }

                        if ("1000".equals(ret)) {
                            Log.println(Log.ASSERT,TAG,"업데이트 성공");
                                // throw new IOException();
                            if(InnerEmployActivity.updateDataCheck==true){
                                InnerEmployActivity.updateDataCheck=false;
                            }
                                getActivity().getFragmentManager().beginTransaction().remove(thisFragment).commit();
                               // getActivity().getSupportFragmentManager().beginTransaction().remove(thisFragment).commit();
                                getActivity().getFragmentManager().popBackStack();


                        }else{
                            Log.println(Log.ASSERT,TAG,"업데이트 실패");
                            Toast.makeText(getActivity().getApplicationContext(),"상황 등록에 실패하였습니다.",Toast.LENGTH_LONG).show();

                        }
                    } catch (XmlPullParserException e) {
                       /* e.printStackTrace();*/
                        Log.e("에러","예외");
                    }
                }

            } catch (IOException e) {
                Log.e("에러","예외");
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        Log.e("에러","예외");
                    }
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.e("에러","예외");
                    }
                }

                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        Log.e("에러","예외");
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
            Log.println(Log.ASSERT,TAG,"onPostExecute");//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
            if ( primitive.equals(UPDATE_JUBBO_CONTENT_INNEREMPLELOY) ) {//GET_CCTV_URL_LIST
                Log.println(Log.ASSERT,TAG,primitive);
            }
        }
    }
}
