package com.ex.gongsa.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.BuildConfig;
import com.ex.situationmanager.IntroActivity;
import com.ex.situationmanager.R;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;

//메인화면
public class MainActivity extends BaseActivity implements View.OnClickListener {
    Intent intent;

    ViewGroup insertWork;//작업계획 등록
    ViewGroup selectWork;//작업계획 조회
    ViewGroup todayWorkInsert;//금일작업 등록
    ViewGroup todayWorkSelect;//금일작업 조회
    ViewGroup  traficStandard;//교통관리기준
    ViewGroup  traficManageMap;//교통관리도

    ImageView gongsaUserInfo;//우측 상단 사용자 메뉴
    TextView mainUserName;//메인화면 사용자 이름
    ImageView edit_main_ImageView;//좌측 중상단 메뉴

    String userInfo;//사용자 정보
    JSONArray jsonArray;//사용자 전체 소속 지사 전체가 담겨있는 초기 jsonArray
    private JSONObject jsonObject;//사용자 정보
    SharedPreferences sh = null;//사용자 정보를 저장하기 위한 SharedPrefrences

    //교통관리도 및 교통관리기준 조회를 위한 메소드
    File file = null;
    InputStream in = null;
    OutputStream out = null;
    AssetManager assetManager = null;//assets 폴더 안에 있는 교통관리기준과 교통관리도로 접근하기 위한 AssetManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_activity_main);

        edit_main_ImageView = (ImageView) findViewById(R.id.edit_main_ImageView);
        insertWork = (ViewGroup) findViewById(R.id.insertWork);
        selectWork = (ViewGroup) findViewById(R.id.selectWork);
        traficStandard = (ViewGroup) findViewById(R.id.traficStandard);
        todayWorkSelect = (ViewGroup) findViewById(R.id.todayWorkSelect);
        gongsaUserInfo = (ImageView) findViewById(R.id.gongsaUserInfo);
        mainUserName = (TextView) findViewById(R.id.mainUserName);
        traficManageMap = (ViewGroup) findViewById(R.id.traficManageMap);
        todayWorkInsert = (ViewGroup) findViewById(R.id.todayWorkInsert);

        selectWork.setOnClickListener(this);
        insertWork.setOnClickListener(this);
        todayWorkSelect.setOnClickListener(this);
        todayWorkInsert.setOnClickListener(this);
        gongsaUserInfo.setOnClickListener(this);
        traficStandard.setOnClickListener(this);
        traficManageMap.setOnClickListener(this);
        edit_main_ImageView.setOnClickListener(this);
        Log.i("현재 페이징름",getApplicationContext().getClass().toString());
        sh = getSharedPreferences("gongsaPref", MODE_PRIVATE);
        try {
            if (IntroActivity.userInfoNumber == 0) {
                userInfo = sh.getString("UserPhoneNo", "");
                try {
                    jsonArray = new JSONArray(sh.getString("UserBelongJisaAndBonbu", ""));
                } catch (JSONException e) {
                    Log.e("에러","에러발생");
                } catch(Exception e){
                    Log.e("에러","에러발생");
                }
            } else {
                userInfo = new JSONArray(sh.getString("UserBelongJisaAndBonbu", "")).get(IntroActivity.userInfoNumber).toString();
                jsonArray = new JSONArray(sh.getString("UserBelongJisaAndBonbu", ""));
            }

            jsonObject = new JSONObject(userInfo);
            jsonObject.put("userId", jsonObject.get("TEL_NO").toString());//사번이 없으면 전화번호를 아이디에 넣어주고

            //메인 화면 유저 이름 입력
            String userName = nullCheck(jsonObject.get("EMNM").toString());
            mainUserName.setText(userName);

            //단말기 버전 정보를 가져오는 메소드
            Log.i("Build.VERSION.SDK_INT", "" + Build.VERSION.SDK_INT);
        } catch (JSONException e) {
            Log.e("에러","에러발생");
        }catch (Exception e) {
            Log.e("에러","에러발생");
        }//try-catch
    }//onCreate

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insertWork:
                try {
                    //if(jsonObject.get("USE_CLSS_CD").equals("E") || jsonObject.get("USE_CLSS_CD").equals("H")){
                    if (true) {//초기 사용자 등급 USE_CLSS_CD값에 따라서 사용자가 이용가능한 메뉴의 제한이 걸려있었으나, 현재는 그 제한이 풀려서 true로 설정

                        /*
                           사용자가 신규 작업 입력 혹은 기존 작업불러오기를 선택할지에 대한 처리 로직이 담긴 메소드 호출
                           파라미터 : jsonObject
                           파라미터 정보: 사용자가 현재 사용중인 유저정보.
                         */
                        selectWorkDialoague(jsonObject, this);
                    } else {
                        Toast.makeText(getApplicationContext(), "권한이 없는 사용자입니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                } catch (Exception e) {
                    Log.e("에러","에러발생");
                }
                break;
            case R.id.selectWork:
                try {
                    /*
                       작업계획 조회기간 설정하여 조회 및 페이징 조회를 위한 초기 파라미터값.
                     */
                    jsonObject.put("prevChk", "today");
                    jsonObject.put("curPage", "1");
                    jsonObject.put("initCheck", "Y");
                    jsonObject.put("isInfiniteTest", "Y");
                   // jsonObject.put("totalCnt","100");
                    new Action("post", SERVER_URL + "/WorkPlan/List.do", jsonObject.toString(), this).execute("");
                } catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                }catch (JSONException e) {
                    Log.e("에러","에러발생");
                }catch (Exception e) {
                    Log.e("에러","에러발생");
                }
                break;
            case R.id.todayWorkInsert:
                new Action("post", SERVER_URL + "/TodayWorkPlan/todayWorkRegisterlist.do", jsonObject.toString(), this, getIntent()).execute("");
                break;
            case R.id.todayWorkSelect:
                new Action("post", SERVER_URL + "/TodayWorkPlan/List.do", jsonObject.toString(), this, intent).execute("");
                break;
            case R.id.gongsaUserInfo:
                try {
                    new CustomDialog(MainActivity.this, R.layout.z_user_info, R.id.clickOk, jsonObject);
                } catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                } catch (Exception e) {
                    Log.e("에러","에러발생");
                }
                break;
            case R.id.traficStandard:
                /*
                  SDK 버전에 따라서 파일(교통관리기준, 교통관리도) 열람하는 방식이 다르다.
                  달라지는 버전은 24버전부터이므로 사용자가 사용하는 단말기의 버전 정보에 따라서  분기처리를 하여준다
                 */
                if (Build.VERSION.SDK_INT >= 24) {
                   //SDK 레벨 - 24 이상
                   //trafficstandardVersion24Over(); (21년 이전)
                    trafficNewVersion24Over();
                } else {
                    //SDK 레벨 - 24 미만
                    //trafficstandard(); (21년 이전)
                    trafficNew();
                }
                break;
            case R.id.traficManageMap:
                /*
                  SDK 버전에 따라서 파일(교통관리기준, 교통관리도) 열람하는 방식이 다르다.
                  달라지는 버전은 24버전부터이므로 사용자가 사용하는 단말기의 버전 정보에 따라서  분기처리를 하여준다
                 */
                if (Build.VERSION.SDK_INT >= 24) {
                    //SDK 레벨 - 24 이상
                    trafficMapVersion24Over();
                } else {
                    //SDK 레벨 - 24 미만
                    trafficMap();
                }
                break;
            case R.id.edit_main_ImageView:

                /*
                   지사 변경 혹은 현장 소장 추가 제거(현장 소장의 공사관리 회원 등급:SMARTAPP)
                 */
                selectJisaOrUserSettingMenu(jsonArray, this);
                break;

        }
    }

    //작업계획 등록
    //i의 값이 0이면 새작업 등록 , i의 값이 1이면 이전 작업 등록
    public void selectWorkDialoague(final JSONObject jsonObject, final Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<String> list = new ArrayList<String>();
        list.add("새 작업 등록");//0번
        list.add("이전 작업 불러오기");//1번
        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    intent = new Intent(context, WorkPlanRegisterActivity.class);
                    intent.putExtra("userInfo", jsonObject.toString());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    try {//WorkPlanResisterListActivity
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                            }
                        });
                        intent = new Intent(context, WorkPlanResisterListActivity.class);
                        jsonObject.put("nVersion", "true");
                        Log.i("loadWorkPlan : ", jsonObject.toString());
                        new Action("get", SERVER_URL + "/WorkPlan/loadWorkPlan.do", jsonObject.toString(), context, intent).execute("");
                    } catch (NullPointerException e) {
                        Log.e("에러","에러발생");
                    }catch (JSONException e) {
                        Log.e("에러","에러발생");
                    }catch (Exception e) {
                        Log.e("에러","에러발생");
                    }
                }
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * 교통관리도 버전 24이하 전용
     */
    public void trafficMap() {
        assetManager = getAssets();
        file = new File(getFilesDir(), "(2017) 고속도로_공사장_교통관리기준(하).pdf");
        try {
            in = assetManager.open("(2017) 고속도로_공사장_교통관리기준(하).pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (NullPointerException e) {
            Log.e("에러","에러발생");
        }catch (IOException e) {
            Log.e("에러","에러발생");
        }catch (Exception e) {
            Log.e("에러","에러발생");
        }
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getFilesDir() + "/(2017) 고속도로_공사장_교통관리기준(하).pdf"), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /*
        교통관리도 버전 25이상 전용
     */
    public void trafficMapVersion24Over() {

        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "External Storage is not Available", Toast.LENGTH_SHORT).show();
        }
        File pdfDir = new File(getExternalFilesDir("").getAbsolutePath() + "/PDFs");
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }
        File file = new File(pdfDir + "/(2017) 고속도로_공사장_교통관리기준(하).pdf");

        try {
            in = assetManager.open("(2017) 고속도로_공사장_교통관리기준(하).pdf");
            out = new BufferedOutputStream(new FileOutputStream(file));
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        if (file.exists()) //Checking for the file is exist or not
        {
            Uri path = FileProvider.getUriForFile(this, "com.ex.gongsa.view.GenericFileProvider", file);


            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Intent intent1 = Intent.createChooser(objIntent, "Open PDF with..");
            try {
                startActivity(objIntent);
            } catch (ActivityNotFoundException e) {
                Log.e("에러","에러발생");
            }
        } else {
            Log.i("", "The file not exists! ");
        }
    }

    /**
     * 교통관리기준 버전 24이하 전용
     */
    public void trafficstandard() {
        assetManager = getAssets();
        file = new File(getFilesDir(), "(2017) 고속도로_공사장_교통관리기준(상).pdf");
        try {
            in = assetManager.open("(2017) 고속도로_공사장_교통관리기준(상).pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            // out = openFileOutput(file.getName(), Context.MODE_PRIVATE);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("에러","에러발생");
        } catch (Exception e){
            Log.e("에러","에러발생");
        }
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getFilesDir() + "/(2017) 고속도로_공사장_교통관리기준(상).pdf"), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 교통관리기준 버전 25이상 전용
     */
    public void trafficstandardVersion24Over() {
        AssetManager assetManager = this.getAssets();
        InputStream in = null;
        OutputStream out = null;
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "External Storage is not Available", Toast.LENGTH_SHORT).show();
        }
        File pdfDir = new File(getExternalFilesDir("").getAbsolutePath() + "/PDFs");
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }
        File file = new File(pdfDir + "/(2017) 고속도로_공사장_교통관리기준(상).pdf");

        try {
            in = assetManager.open("(2017) 고속도로_공사장_교통관리기준(상).pdf");
            out = new BufferedOutputStream(new FileOutputStream(file));
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        } catch (Exception e){
            Log.e("tag", e.getMessage());
        }

        if (file.exists()) //Checking for the file is exist or not
        {

            Uri path = FileProvider.getUriForFile(this, "com.ex.gongsa.view.GenericFileProvider", file);
            Log.i("URIddddddddd 주소", path.getAuthority());

            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(objIntent);
            } catch (ActivityNotFoundException e) {
                Log.e("에러","에러발생");
            } catch (Exception e){
                Log.e("에러","에러발생");
            }

        } else {
            Log.i("", "The file not exists! ");
        }
    }

    public void trafficNew() {
        assetManager = getAssets();
        file = new File(getFilesDir(), "(2021) 고속도로_작업장_교통관리기준.pdf");
        try {
            in = assetManager.open("(2021) 고속도로_작업장_교통관리기준.pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            // out = openFileOutput(file.getName(), Context.MODE_PRIVATE);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("에러","에러발생");
        } catch (Exception e){
            Log.e("에러","에러발생");
        }
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getFilesDir() + "/(2017) 고속도로_공사장_교통관리기준(상).pdf"), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 교통관리기준 버전 25이상 전용
     */
    public void trafficNewVersion24Over() {
        AssetManager assetManager = this.getAssets();
        InputStream in = null;
        OutputStream out = null;
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "External Storage is not Available", Toast.LENGTH_SHORT).show();
        }
        File pdfDir = new File(getExternalFilesDir("").getAbsolutePath() + "/PDFs");
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }
        File file = new File(pdfDir + "/(2021) 고속도로_작업장_교통관리기준.pdf");

        try {
            in = assetManager.open("(2021) 고속도로_작업장_교통관리기준.pdf");
            out = new BufferedOutputStream(new FileOutputStream(file));
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        } catch (Exception e){
            Log.e("tag", e.getMessage());
        }

        if (file.exists()) //Checking for the file is exist or not
        {

            Uri path = FileProvider.getUriForFile(this, "com.ex.gongsa.view.GenericFileProvider", file);
            Log.i("URIddddddddd 주소", path.getAuthority());

            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(objIntent);
            } catch (ActivityNotFoundException e) {
                Log.e("에러","에러발생");
            } catch (Exception e){
                Log.e("에러","에러발생");
            }

        } else {
            Log.i("", "The file not exists! ");
        }
    }

    /*
       교통관리도 및 교통 -교통관리 기준을 이용하기 위한 서브 메소드
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    /**
     * 사용자 전화번호를 가져오는 메소드
     *
     * @return 사용자 전화번호
     */
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


    public void selectOtherJisaAndBonbu(final JSONArray njsonArray) {

        ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i < njsonArray.length(); i++) {
            try {
                /* list.add("본부 : "+njsonArray.getJSONObject(i).get("HDQR_NM").toString()+", 지사 : "+njsonArray.getJSONObject(i).get("MTNOF_NM").toString());*/
                list.add(njsonArray.getJSONObject(i).get("HDQR_NM").toString() + " " + njsonArray.getJSONObject(i).get("MTNOF_NM").toString());
            } catch (JSONException e) {
                Log.e("에러","에러발생");
            } catch (NullPointerException e) {
                Log.e("에러","에러발생");
            } catch (Exception e) {
                Log.e("에러","에러발생");
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("변경하실 지사를 선택해주세요.");

        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    jsonObject = njsonArray.getJSONObject(i);
                    IntroActivity.userInfoNumber = i;
                    Log.i("선택된 지사", i + "");
                    Log.i("dd", jsonObject.toString());
                    try {
                        jsonObject.put("userId", jsonObject.get("TEL_NO").toString());//사번이 없으면 전화번호를 아이디에 넣어주고
                    } catch (JSONException e) {
                        Log.e("에러","에러발생");
                        //  jsonObject.put("userId", jsonObject.get("TEL_NO").toString());//사번이 없으면 전화번호를 아이디에 넣어주고
                    } catch (Exception e) {
                        Log.e("에러","에러발생");
                        //  jsonObject.put("userId", jsonObject.get("TEL_NO").toString());//사번이 없으면 전화번호를 아이디에 넣어주고
                    }
                    /*   sh.putExtra("UserPhoneNo",jsonObject.toString());*/
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("UserPhoneNo", jsonObject.toString());
                    Log.i("유저정보", jsonObject.toString());
                    Log.println(Log.ASSERT, "유저정보1", jsonObject.toString());
                } catch (JSONException e) {
                    Log.e("에러","에러발생");
                }catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                }catch (Exception e) {
                    Log.e("에러","에러발생");
                }
            }
        });

        builder.create();
        builder.show();
    }

    public void selectJisaOrUserSettingMenu(final JSONArray njsonArray, final Context context) {
        final ArrayList<String> userMenuList = new ArrayList<String>();

        userMenuList.add("지사 변경");
        userMenuList.add("현장 소장 추가 및 제거");
        final Spinner spinner = new Spinner(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메뉴를 선택하여주세요");

        builder.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, userMenuList), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    if (userMenuList.get(i).toString().equals("지사 변경")) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                            }
                        });
                        selectOtherJisaAndBonbu(jsonArray);
                    } else {
                        if (jsonObject.get("USE_CLSS_CD").equals("E") || jsonObject.get("USE_CLSS_CD").equals("H")) {
                           /* Toast.makeText(getApplicationContext(),"사용자 추가",Toast.LENGTH_SHORT).show();
                            insertUserName(  jsonObject);*/
                            new UserListDialog(context, jsonObject, intent);
                            /*test();*/
                        } else {
                            Toast.makeText(getApplicationContext(), "권한이 없는 사용자입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (NullPointerException e) {
                    Log.e("에러","에러발생");
                }catch (JSONException e) {
                    Log.e("에러","에러발생");
                }catch (Exception e) {
                    Log.e("에러","에러발생");
                }
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public String onActionPost(String primitive, String date) {
        return "";
    }

}//end class MainActivity
