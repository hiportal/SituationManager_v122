package com.ex.gongsa.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//import com.ex.gongsa.utils.MyOnScrollListenner;
import com.ex.gongsa.vo.KyoTongResultVo;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class WorkPlanListActivity extends BaseActivity implements View.OnClickListener {

    final String TAG = "WorkPlanListActivity";
    // TextView no,line,date;
    List<KyoTongResultVo> list;

    Intent intent;
    ViewGroup listItemview = null;

    //매개변수로 넣어줄 텍뷰
    TextView no, line, date, gotoMainTV;
    List<Integer> viewIdList;
    List<View> viewList;
    ImageView gongsaPrev;
    ViewGroup workPlanSelect_Li, yesterDaywork_select_Li, prevWork_select_Li;

    JSONArray jsonArray;
    JSONObject jsonObject;
    JSONArray jangbiJsonArray;
    JSONObject userInfoJsonObj;
    ArrayList<String> jangbiList;
    String workJsonWorkPlanList;
    String userInfo;
    String praam;

    WorkPlanRecyclerItem adapter;
    RecyclerView recyclerview;
    int nextPage = 1;
    int curPage = 1;
    ProgressDialog dialog;

    ImageView curwork_iv, yesterDaywork_iv, prevWork_iv;
    Map<String, ImageView> imageViewVisibleArray;
    String[] imageViewIdArray = null;

    JSONArray njsonArray;
    List<JSONObject> jsonJavaList = null;

    boolean enabledProtocol = false;
    int checkCnt = 0;
    boolean isScrollabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_work_plan_list);

        no = findViewById(R.id.no);
        line = findViewById(R.id.line);
        date = findViewById(R.id.date);
        list = new ArrayList<KyoTongResultVo>();

        curwork_iv = (ImageView) findViewById(R.id.curwork_iv);
        yesterDaywork_iv = (ImageView) findViewById(R.id.yesterDaywork_iv);
        prevWork_iv = (ImageView) findViewById(R.id.prevWork_iv);

        imageViewVisibleArray = new HashMap<String, ImageView>();
        imageViewVisibleArray.put("curwork_iv", curwork_iv);
        imageViewVisibleArray.put("yesterDaywork_iv", yesterDaywork_iv);
        imageViewVisibleArray.put("prevWork_iv", prevWork_iv);

        imageViewIdArray = new String[3];
        imageViewIdArray[0] = "curwork_iv";
        imageViewIdArray[1] = "yesterDaywork_iv";
        imageViewIdArray[2] = "prevWork_iv";

        //개발용 테스트 계정 전화번호
        jangbiList = new ArrayList<String>();
        jsonObject = new JSONObject();

        try {
            workJsonWorkPlanList = URLDecoder.decode(getIntent().getStringExtra("workJsonWorkPlanList"), "UTF-8");
            userInfo = getIntent().getStringExtra("userInfo");
            userInfoJsonObj = new JSONObject(userInfo);
            jsonArray = new JSONArray(workJsonWorkPlanList);
            //최창유 주석
            jsonJavaList = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonJavaList.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException | UnsupportedEncodingException e) {
           Log.e("에러","에러발생");
            e.printStackTrace();
        }

        recyclerview = (RecyclerView) findViewById(R.id.workplanlistRecycleView);
        adapter = new WorkPlanRecyclerItem(this, jsonJavaList, userInfo, 0, recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        //최창유 주석

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {
                    ++checkCnt;
                    try {
                        if (checkCnt == 1 && isScrollabled == false) {
                            Toast.makeText(getApplicationContext(), "마지막 페이지입니다.", Toast.LENGTH_LONG).show();

                        } else if (isScrollabled == true) {
                            Toast.makeText(getApplicationContext(), "한번 더 위아래로 스크롤하시면 다음 작업계획이 조회됩니다.", Toast.LENGTH_LONG).show();
                        }
                        if (checkCnt == 1) {
                            //recyclerView.smoothScrollToPosition(lastVisibleItemPosition-((lastVisibleItemPosition/10)*8));
                        } else if (checkCnt == 2) {
                            enabledProtocol = true;
                        }
                    } catch (NullPointerException e) {
                       Log.e("에러","에러발생");
                    }
                }
                if (lastVisibleItemPosition == itemTotalCount && enabledProtocol == true) {
                    checkCnt = 0;
                    enabledProtocol = false;

                    Log.i("라스트 포지션", lastVisibleItemPosition + "");
                    try {
                        currentPageCheck();
                    } catch (NullPointerException e) {
                       Log.e("에러","에러발생");
                    }
                }
            }
        });

        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
        gongsaPrev.setOnClickListener(this);

        //하단 메인으로 레이아웃
        workPlanSelect_Li = (ViewGroup) findViewById(R.id.workPlanSelect_Li);
        workPlanSelect_Li.setOnClickListener(this);
        gotoMainTV = (TextView) findViewById(R.id.gotoMainTV);

        //최창유 주석
        yesterDaywork_select_Li = (ViewGroup) findViewById(R.id.yesterDaywork_select_Li);

        yesterDaywork_select_Li.setOnClickListener(this);
        prevWork_select_Li = (ViewGroup) findViewById(R.id.prevWork_select_Li);
        prevWork_select_Li.setOnClickListener(this);
    }


    public void currentPageCheck() {
        try {
            if (userInfoJsonObj.get("prevChk").toString().equals("prevWork")) {
                onProtocol("prevWork");
            } else if (userInfoJsonObj.get("prevChk").toString().equals("yesterDay")) {
                onProtocol("yesterDay");
            } else {
                onProtocol("today");
            }
        } catch (NullPointerException | JSONException e) {
            e.printStackTrace();
           Log.e("에러","에러발생");
        }
    }

    @Override
    public String onActionPost(String primitive, String data) {
        return null;
    }

    public void onProtocol(String value) {
        try {
            if (curPage < new Integer(jsonArray.getJSONObject(jsonArray.length() - 1).getString("lastPage"))) {
                userInfoJsonObj = new JSONObject(userInfo);
                userInfoJsonObj.put("prevChk", value);
                userInfoJsonObj.put("initCheck", "N");
                userInfoJsonObj.put("isInfiniteTest", "Y");
                userInfoJsonObj.put("totalCount", jsonArray.getJSONObject(jsonArray.length() - 1).get("totalCount").toString());

                Log.i("par12nextPage3am", "nextPage:" + nextPage);
                nextPage = Integer.parseInt(jsonArray.getJSONObject(jsonArray.length() - 1).get("nextPage").toString());
                curPage = nextPage;

                userInfoJsonObj.put("curPage", nextPage + "");
                Log.i("par123am", "jsonObject 파라미터 확인:" + userInfoJsonObj.toString());


                praam = URLDecoder.decode(new Action("post", SERVER_URL + "/WorkPlan/List.do", userInfoJsonObj.toString(), this, true, userInfo).execute().get(), "UTF-8");


                workJsonWorkPlanList = workJsonWorkPlanList.substring(0, workJsonWorkPlanList.length() - 1) + "," + praam.substring(1);
                jsonArray = new JSONArray(workJsonWorkPlanList);

                njsonArray = new JSONArray(praam);

                // jsonJavaList = new ArrayList<JSONObject>();
                for (int i = 0; i < njsonArray.length(); i++) {

                    jsonJavaList.add(njsonArray.getJSONObject(i));
                    //adapter.notifyItemInserted(jsonJavaList.size() - 1);
                    adapter.notifyItemInserted(jsonJavaList.size() );
                }


                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.i("총 파람 확인", jsonArray.get(i).toString());
                }
                Log.i("par123am", "여기에바로오는지 체크" + jsonArray.get(jsonArray.length() - 1).toString());
                nextPage = new Integer(jsonArray.getJSONObject(jsonArray.length() - 1).getString("nextPage"));
                curPage = new Integer(jsonArray.getJSONObject(jsonArray.length() - 1).getString("curPage"));
            } else {
                isScrollabled = false;
                 Toast.makeText(this, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | ArrayIndexOutOfBoundsException | InterruptedException | ExecutionException |UnsupportedEncodingException e) {
           Log.e("에러","에러발생");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.workPlanSelect_Li:
                try {
                    isScrollabled = true;
                    setImageViewVisible(imageViewVisibleArray, "curwork_iv", imageViewIdArray);
                    userInfoJsonObj = new JSONObject(userInfo);
                    userInfoJsonObj.put("prevChk", "today");
                    userInfoJsonObj.put("curPage", "1");
                    userInfoJsonObj.put("initCheck", "N");
                    userInfoJsonObj.put("isInfiniteTest", "Y");
                    new Action("post", SERVER_URL + "/WorkPlan/List.do", userInfoJsonObj.toString(), this).execute("");
                } catch (JSONException | ArrayIndexOutOfBoundsException  e) {
                   Log.e("에러","에러발생");
                }
                break;
            case R.id.yesterDaywork_select_Li:
                try {
                    isScrollabled = true;
                    setImageViewVisible(imageViewVisibleArray, "yesterDaywork_iv", imageViewIdArray);
                    userInfoJsonObj = new JSONObject(userInfo);
                    userInfoJsonObj.put("prevChk", "yesterDay");
                    userInfoJsonObj.put("curPage", "1");
                    userInfoJsonObj.put("initCheck", "N");
                    userInfoJsonObj.put("isInfiniteTest", "Y");
                    new Action("post", SERVER_URL + "/WorkPlan/List.do", userInfoJsonObj.toString(), this).execute("");
                } catch (JSONException | ArrayIndexOutOfBoundsException e) {
                   Log.e("에러","에러발생");
                }
                break;
            case R.id.prevWork_select_Li:
                try {
                    isScrollabled = true;
                    setImageViewVisible(imageViewVisibleArray, "prevWork_iv", imageViewIdArray);
                    try {
                        userInfoJsonObj = new JSONObject(userInfo);
                        userInfoJsonObj.put("prevChk", "prevWork");
                        userInfoJsonObj.put("curPage", "1");
                        userInfoJsonObj.put("initCheck", "N");
                        userInfoJsonObj.put("isInfiniteTest", "Y");
                        new Action("post", SERVER_URL + "/WorkPlan/List.do", userInfoJsonObj.toString(), this).execute("");
                    } catch (JSONException | ArrayIndexOutOfBoundsException  e) {
                       Log.e("에러","에러발생");
                    }
                } catch ( ArrayIndexOutOfBoundsException e) {
                   Log.e("에러","에러발생");
                }
                break;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        try {
            if (userInfoJsonObj.get("curPage").equals("1")) {
                workJsonWorkPlanList = URLDecoder.decode(intent.getStringExtra("workJsonWorkPlanList"), "UTF-8");

                jsonArray = new JSONArray(workJsonWorkPlanList);
                for(int i = 0 ; i < jsonArray.length();i++){
                    Log.i("ㅇㅇㅇ",jsonArray.getJSONObject(i).toString());

                }
                jsonJavaList = new ArrayList<JSONObject>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonJavaList.add(jsonArray.getJSONObject(i));
                }
                adapter = new WorkPlanRecyclerItem(this, jsonJavaList, userInfo, 0, recyclerview);
                recyclerview.setLayoutManager(new LinearLayoutManager(this));
                recyclerview.setAdapter(adapter);
                nextPage = new Integer(jsonArray.getJSONObject(0).getString("nextPage"));
                userInfoJsonObj.put("nextPage", nextPage + "");
            }

        } catch (JSONException | ArrayIndexOutOfBoundsException | UnsupportedEncodingException e) {
           Log.e("에러","에러발생");
        }
    }

    public void setImageViewVisible(Map<String, ImageView> imageViewMap, String id, String[] imageViewIdArray) {
        imageViewMap.get(id).setVisibility(View.VISIBLE);
        for (int i = 0; i < imageViewIdArray.length; i++) {
            if (!imageViewIdArray[i].equals(id)) {
                imageViewMap.get(imageViewIdArray[i]).setVisibility(View.GONE);
            }
        }
    }
}

