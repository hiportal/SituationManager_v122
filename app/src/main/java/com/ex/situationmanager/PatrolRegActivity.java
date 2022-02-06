package com.ex.situationmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.BaseActivity.Action;
import com.ex.situationmanager.PatrolMainActivity.ActionList;
import com.ex.situationmanager.dto.Jochi;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import org.xmlpull.v1.XmlPullParserException;

public class PatrolRegActivity extends BaseActivity implements OnClickListener {

    int currentPage = 1;
    int position = 0;
    ArrayList<Jochi> itemList;
    Intent intent;
    boolean sendGpsFlag = false;
    ListView listView;
    int height = 0;

    ImageView patrolPicture, patrolRegist;
    String TAG = "PatrolRegActivity";

    private String selectedRpt_id = "";
    ImageView btnReg1, btnReg2, btnReg3, btnReg4, btnReg5, btnReg6, btnReg7, btnReg8, btnReg9, btnReg10, btnReg11, btnReg12, btnReg13, btnReg14, btnReg15, btnReg16;

    ImageView menu;
    ImageView btnUserInfo;
    ImageView btnBack;

    LinearLayout llbottomBtnDefault, llbottomBtnPhone;
    ImageView customPhone, patRegPhone, patRegPhoneClose;
    TextView patRegPhone1, patRegPhone2, patRegPhone3, patRegPhone4, patRegPhone5;
    String customPhoneInfo = "";
    int focusPosition = 0;

    boolean ProgressFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SituationService.conf.USER_PHONE_NUMBER.startsWith(Configuration.NAVIGATION_START_NUMBER)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로전환
            setContentView(R.layout.patrol_reg_land);
        } else {
            setContentView(R.layout.patrol_reg);
        }


        ProgressFlag = true;
        Intent i = getIntent();
        selectedRpt_id = i.getStringExtra("selectedRpt_id");
        customPhoneInfo = i.getStringExtra("customPhoneInfo");

        Log.d(TAG, TAG + "selected item = " + selectedRpt_id);
        listView = (ListView) findViewById(R.id.jochiListView);

//		Parameters params = new Parameters(ONECLICK_GETACDNTACTINFO_SELECT);
//		params.put("rpt_id", selectedRpt_id);
//		new ActionList(ONECLICK_GETACDNTACTINFO_SELECT, params).execute("");

        btnReg1 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg1);
        btnReg2 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg2);
        btnReg3 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg3);
        btnReg4 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg4);
        btnReg5 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg5);
        btnReg6 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg6);
        btnReg7 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg7);
        btnReg8 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg8);
        btnReg9 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg9);
        btnReg10 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg10);
        btnReg11 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg11);
        btnReg12 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg12);
        btnReg13 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg13);
        btnReg14 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg14);
        btnReg15 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg15);
        btnReg16 = (ImageView) PatrolRegActivity.this.findViewById(R.id.btnReg16);

        btnReg1.setOnClickListener(PatrolRegActivity.this);
        btnReg2.setOnClickListener(PatrolRegActivity.this);
        btnReg3.setOnClickListener(PatrolRegActivity.this);
        btnReg4.setOnClickListener(PatrolRegActivity.this);
        btnReg5.setOnClickListener(PatrolRegActivity.this);
        btnReg6.setOnClickListener(PatrolRegActivity.this);
        btnReg7.setOnClickListener(PatrolRegActivity.this);
        btnReg8.setOnClickListener(PatrolRegActivity.this);
        btnReg9.setOnClickListener(PatrolRegActivity.this);
        btnReg10.setOnClickListener(PatrolRegActivity.this);
        btnReg11.setOnClickListener(PatrolRegActivity.this);
        btnReg12.setOnClickListener(PatrolRegActivity.this);
        btnReg13.setOnClickListener(PatrolRegActivity.this);
        btnReg14.setOnClickListener(PatrolRegActivity.this);
        btnReg15.setOnClickListener(PatrolRegActivity.this);
        btnReg16.setOnClickListener(PatrolRegActivity.this);

        llbottomBtnDefault = (LinearLayout) findViewById(R.id.llbottomBtnDefault);
        llbottomBtnPhone = (LinearLayout) findViewById(R.id.llbottomBtnPhone);
        patRegPhone = (ImageView) findViewById(R.id.patRegPhone);
        patRegPhoneClose = (ImageView) findViewById(R.id.patRegPhoneClose);
        customPhone = (ImageView) findViewById(R.id.customPhone);
        patRegPhone1 = (TextView) findViewById(R.id.patRegPhone1);
        patRegPhone2 = (TextView) findViewById(R.id.patRegPhone2);
        patRegPhone3 = (TextView) findViewById(R.id.patRegPhone3);
        patRegPhone4 = (TextView) findViewById(R.id.patRegPhone4);
        patRegPhone5 = (TextView) findViewById(R.id.patRegPhone5);


        patRegPhone.setOnClickListener(this);
        customPhone.setOnClickListener(this);
        patRegPhoneClose.setOnClickListener(this);
        patRegPhone1.setOnClickListener(this);
        patRegPhone2.setOnClickListener(this);
        patRegPhone3.setOnClickListener(this);
        patRegPhone4.setOnClickListener(this);
        patRegPhone5.setOnClickListener(this);

        btnUserInfo = (ImageView) findViewById(R.id.btnUserInfo);
        btnUserInfo.setOnClickListener(this);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);


        if (Common.nullCheck(customPhoneInfo).equals("")) {
            customPhone.setVisibility(View.GONE);
        }
        setPhoneBtnVisible(false);

    }


    static Timer listTimer = new Timer();
    Handler listHandler = new Handler();

    //통신 동기화를 위함.
    boolean listFlag = true;

    /**
     * 조치사항 목록 타이머
     */
    public void startTimer_list() {

        TimerTask timertask = new TimerTask() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (listFlag == false) {
                        return;
                    }

                    Parameters params = new Parameters(ONECLICK_GETACDNTACTINFO_SELECT);
                    if (null != Configuration.User.getBscode_list()) {
                        if (Configuration.User.getBscode_list().size() > 0) {
                            params.put("bscode", Configuration.User.getBscode_list().get(0));
                        }
                    }
                    params.put("rpt_id", selectedRpt_id);

                    listFlag = false;
                    new ActionList(ONECLICK_GETACDNTACTINFO_SELECT, params).execute("");
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

    //순찰차전화버튼 클릭시 visible 관리
    public void setPhoneBtnVisible(boolean phone) {
        if (phone == true) {
            llbottomBtnDefault.setVisibility(View.GONE);
            llbottomBtnPhone.setVisibility(View.VISIBLE);
        } else {
            llbottomBtnDefault.setVisibility(View.VISIBLE);
            llbottomBtnPhone.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        VIEW_TAG = VIEW_PATROL;
        startTimer_list();

        Parameters params_tel = new Parameters(ONECLICK_GETPATROLTELNO_SELECT);
        params_tel.put("car_id", Configuration.User.getPatcar_id());
        if (null != Configuration.User.getBscode_list()) {
            if (Configuration.User.getBscode_list().size() > 0) {
                params_tel.put("bscode", Configuration.User.getBscode_list().get(0));
            }
        }
        new Action(ONECLICK_GETPATROLTELNO_SELECT, params_tel).execute();

    }

    public void displayGallery() {

        final PatrolListAdapter adapter = new PatrolListAdapter(this, itemList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                adapter.setItemImage(position);
            }
        });

        setBtnVisible(itemList);
    }

    private void setBtnVisible(List<Jochi> visibleList) {
        for (int i = 0; i < visibleList.size(); i++) {
            String code = Common.nullCheck(visibleList.get(i).getReg_code());
            Log.d("", "enabled code = " + code);
            if (code.equals("03")) {
                btnReg1.setEnabled(false);
            } else if (code.equals("06")) {
                btnReg2.setEnabled(false);
            } else if (code.equals("07")) {
                btnReg3.setEnabled(false);
            } else if (code.equals("04")) {
                btnReg4.setEnabled(false);
            } else if (code.equals("09")) {
                btnReg5.setEnabled(false);
            } else if (code.equals("02")) {
                btnReg6.setEnabled(false);
            } else if (code.equals("18")) {
                btnReg7.setEnabled(false);
            } else if (code.equals("19")) {
                btnReg8.setEnabled(false);
            } else if (code.equals("20")) {
                btnReg9.setEnabled(false);
            } else if (code.equals("12")) {
                btnReg10.setEnabled(false);
            } else if (code.equals("15")) {
                btnReg11.setEnabled(false);
            } else if (code.equals("13")) {
                btnReg12.setEnabled(false);
            } else if (code.equals("16")) {
                btnReg13.setEnabled(false);
            } else if (code.equals("22")) {
                btnReg14.setEnabled(false);
            } else if (code.equals("23")) {
                btnReg15.setEnabled(false);
            } else if (code.equals("10")) {
                btnReg16.setEnabled(false);
            }

        }


    }


    public class PatrolListAdapter extends ArrayAdapter<Jochi> {

        private Context mContext;
        private LayoutInflater mInflater;

        public PatrolListAdapter(Context context, List<Jochi> objects) {
            super(context, 0, objects);

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;

            if (objects.size() == 0) {
                Toast.makeText(getApplicationContext(), "검색된 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        public void setItemImage(int position) {

            Jochi item = this.getItem(position);        // ArrayAdapter

        }

        public View getView(int position, View convertView, ViewGroup parent) {

            focusPosition = position;
            TextView row_patrol_no;
            TextView row_patrol_content;

            View view = convertView;

            Log.d("", "getview position " + position);
            if (convertView == null) {
                view = mInflater.inflate(R.layout.jochi_list_row, null);
            }

            Jochi item = this.getItem(position);
            if (item != null) {
                row_patrol_no = (TextView) view.findViewById(R.id.row_patrol_no);
                row_patrol_content = (TextView) view.findViewById(R.id.row_patrol_content);

                row_patrol_no.setText("" + item.getReg_time());
                row_patrol_content.setText("" + item.getReg_data());

            }
            return view;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer_list();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if (ProgressFlag == true) {
            return;
        }

        switch (v.getId()) {
            case R.id.btnReg1:
                acdntInsert("03", Configuration.User.getCar_nm() + "||도착||안전관리");
                break;
            case R.id.btnReg2:
                acdntInsert("06", "고속도로순찰대");
                break;
            case R.id.btnReg3:
                acdntInsert("07", "직원현장||도착");
                break;
            case R.id.btnReg4:
                acdntInsert("04", "사고처리완료");
                sendStop(false);
                SituationService.start_yn = "N";
                Common.setPrefString(PatrolRegActivity.this, "start_yn", "N");
                break;
            case R.id.btnReg5:
                acdntInsert("09", "지사||작업원||도착,||도로정비||작업");
                break;
            case R.id.btnReg6:
                acdntInsert("02", "견인차||도착,||구난작업");
                break;
            case R.id.btnReg7:
                acdntInsert("18", "대형견인차||도착,||구난작업");
                break;
            case R.id.btnReg8:
                acdntInsert("19", "견인완료");
                break;
            case R.id.btnReg9:
                acdntInsert("20", "차선통제");
                break;
            case R.id.btnReg10:
                acdntInsert("12", "119도착,||구난작업");
                break;
            case R.id.btnReg11:
                acdntInsert("15", "119헬기도착,||구난작업");
                break;
            case R.id.btnReg12:
                acdntInsert("13", "사상자후송");
                break;
            case R.id.btnReg13:
                acdntInsert("16", "사상자발생");
                break;
            case R.id.btnReg14:
                acdntInsert("22", "중장비도착,도로정비||작업");
                break;
            case R.id.btnReg15:
                acdntInsert("23", "낙하물처리");
                break;
            case R.id.btnReg16:
                acdntInsert("10", "도로정비||작업완료");
                break;

            case R.id.patRegPhone:
                setPhoneBtnVisible(true);
                break;
            case R.id.patRegPhoneClose:
                setPhoneBtnVisible(false);
                break;
            case R.id.customPhone:
                Intent customIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customPhoneInfo));
                startActivity(customIntent);
                break;
            case R.id.btnBack:
                finish();
                break;

            default:
                break;
        }

        super.onClick(v);
    }

    public void acdntInsert(String code, String data) {

        if ("Y".equals(Common.getPrefString(PatrolRegActivity.this, "start_yn"))) {
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(PatrolRegActivity.this);
            ad.setMessage("").setTitle("조치사항등록").setMessage("* 출동중 일때 이용하실 수 있습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            ad.show();
            return;
        }

        Parameters params = new Parameters(ONECLICK_ACDNTACT_INSERT);
        params.put("rpt_id", selectedRpt_id);
        params.put("reg_code", code);
        if (null != Configuration.User.getBscode_list()) {
            if (Configuration.User.getBscode_list().size() > 0) {
                params.put("bscode", Configuration.User.getBscode_list().get(0));

            }
        }

//        params.put("reg_data", data);

        new ActionList(ONECLICK_ACDNTACT_INSERT, params).execute("");
    }

    @Override
    public void onBackPressed() {
        Log.d("", TAG + "onBackPressed");
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("", TAG + "onKeyDown");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
        try {
            if (e == null) {
                if (null != result) {

                    String rtnResultCode = result.get("result");
                    if ("1000".equals(rtnResultCode)) {
                        if (primitive.equals(ONECLICK_GETPATROLTELNO_SELECT)) {
                            result.setList("entity");
                            for (int i = 0; i < result.size(); i++) {
                                String car_nm = result.get(i, "car_nm");
                                final String tel_no = result.get(i, "tel_no");

                                if (Configuration.User.getHp_no().equals(tel_no)) {
                                    return;
                                }

                                if (i == 0) {
                                    patRegPhone1.setVisibility(View.VISIBLE);
                                    patRegPhone1.setText(car_nm);
                                    patRegPhone1.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                            startActivity(patPhone1Intent);

                                        }
                                    });
                                } else if (i == 1) {
                                    patRegPhone2.setVisibility(View.VISIBLE);
                                    patRegPhone2.setText(car_nm);
                                    patRegPhone2.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                            startActivity(patPhone1Intent);

                                        }
                                    });
                                } else if (i == 2) {
                                    patRegPhone3.setVisibility(View.VISIBLE);
                                    patRegPhone3.setText(car_nm);
                                    patRegPhone3.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                            startActivity(patPhone1Intent);

                                        }
                                    });
                                } else if (i == 3) {
                                    patRegPhone4.setVisibility(View.VISIBLE);
                                    patRegPhone4.setText(car_nm);
                                    patRegPhone4.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                            startActivity(patPhone1Intent);

                                        }
                                    });
                                } else if (i == 4) {
                                    patRegPhone5.setVisibility(View.VISIBLE);
                                    patRegPhone5.setText(car_nm);
                                    patRegPhone5.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_no));
                                            startActivity(patPhone1Intent);

                                        }
                                    });
                                }
								
								/*if(car_nm.equals("순찰1호")){
									patRegPhone1.setVisibility(View.VISIBLE);
									patRegPhone1.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel_no));
											startActivity(patPhone1Intent);
											
										}
									});
								}else if(car_nm.equals("순찰2호")){
									patRegPhone2.setVisibility(View.VISIBLE);
									patRegPhone2.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel_no));
											startActivity(patPhone1Intent);
											
										}
									});
									
								}else if(car_nm.equals("순찰3호")){
									patRegPhone3.setVisibility(View.VISIBLE);
									patRegPhone3.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel_no));
											startActivity(patPhone1Intent);
											
										}
									});
								}else if(car_nm.equals("순찰4호")){
									patRegPhone4.setVisibility(View.VISIBLE);
									patRegPhone4.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel_no));
											startActivity(patPhone1Intent);
											
										}
									});
								}else if(car_nm.equals("순찰5호")){
									patRegPhone5.setVisibility(View.VISIBLE);
									patRegPhone5.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent patPhone1Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel_no));
											startActivity(patPhone1Intent);
											
										}
									});
								}//if
								*/
                            }//for

                        }

                    }
                }
            }
        } catch (XmlPullParserException e2) {
            Log.e("에러", "XmlPullParserException ");
        }


    }


    public class ActionList extends AsyncTask<String, Void, XMLData> {
        // --------------------------------------------------------------------------------------------
        // #region 공통코드 정보 수신
        // 진행 상태 Progressbar
        ProgressDialog progressDialog;

        String primitive = "";
        Parameters params = null;

        @Override
        protected void onPreExecute() {
            if (ProgressFlag == true && primitive.equals(ONECLICK_GETACDNTACTINFO_SELECT)) {
                progressDialog = ProgressDialog.show(PatrolRegActivity.this, "", "로딩중...", true);
            } else if (primitive.equals(ONECLICK_ACDNTACT_INSERT)) {
                progressDialog = ProgressDialog.show(PatrolRegActivity.this, "", "로딩중...", true);
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
                listFlag = false;
                // test code
                StringBuffer body = new StringBuffer();
                body.append(URL_SENDGPS);
                body.append("?");
                body.append(params.toString());

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
//								throw new IOException();
                            } else {
                                String retMsg = xmlData.get("resultMessage");
//								throw new IOException();
                            }
                        }
                    } catch (XmlPullParserException e) {
                    }
                }

            } catch (IOException e) {
                /*e.printStackTrace();*/
                Log.e("에러", "예외");
                listFlag = true;

            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        /*	e.printStackTrace();*/
                        Log.e("에러", "예외");
                    }
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        Log.e("에러", "예외");
                    }
                }

                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                      //  e.printStackTrace();
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
            if (primitive.equals(ONECLICK_GETACDNTACTINFO_SELECT)) {
                listFlag = true;
                if (null != progressDialog) {
                    progressDialog.dismiss();
                }
                ProgressFlag = false;
            } else if (primitive.equals(ONECLICK_ACDNTACT_INSERT)) {
                if (null != progressDialog) {
                    progressDialog.dismiss();
                }
            }
            try {
                String rtnResultCode = result.get("result");
                if ("1000".equals(rtnResultCode)) {
                    if (primitive.equals(ONECLICK_ACDNTACT_INSERT)) {
                        Log.d(TAG, "PatrolRegActivity onPostExecute ok ");
                        Parameters params = new Parameters(ONECLICK_GETACDNTACTINFO_SELECT);
                        params.put("rpt_id", selectedRpt_id);
                        new ActionList(ONECLICK_GETACDNTACTINFO_SELECT, params).execute("");

                    } else if (primitive.equals(ONECLICK_GETACDNTACTINFO_SELECT)) {
                        result.setList("entity");
                        Log.d(TAG, "PatrolRegActivity onPostExecute size = " + result.size());
                        Jochi item = null;
                        itemList = new ArrayList<Jochi>();

                        for (int i = 0; i < result.size(); i++) {
                            String reg_date = Common.nullCheck(result.get(i, "reg_date"));
                            String reg_code = Common.nullCheck(result.get(i, "reg_code"));
                            String reg_time = Common.nullCheck(result.get(i, "reg_time"));
                            String reg_data = Common.nullCheck(result.get(i, "reg_data"));

                            item = new Jochi();
                            item.setReg_code(reg_code);
                            item.setReg_date(reg_date);
                            item.setReg_time(reg_time);
                            item.setReg_data(reg_data);

                            itemList.add(item);
                        }


                        displayGallery();
                    }


                }
            } catch (NullPointerException e) {
               /* e.printStackTrace();*/
                Log.e("에러","예외");
            } catch (XmlPullParserException ee){
                Log.e("에러","예외");
            }
        }
    }

    @Override
    protected void setActivityViewEdit(Activity activity) throws IOException {
    }

}
