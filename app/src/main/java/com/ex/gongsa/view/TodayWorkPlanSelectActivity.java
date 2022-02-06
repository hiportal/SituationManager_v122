package com.ex.gongsa.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.gongsa.vo.KyoTongResultVo;
import com.ex.situationmanager.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;

//금일작업 조회
//리스트
public class TodayWorkPlanSelectActivity extends BaseActivity implements View.OnClickListener {

    final String TAG = "TodayWorkPlanSelectActivity";

    Intent intent = null;
    ViewGroup gotoMain;

    ImageView gongsaPrev;
    RecyclerView todayWorkSelectRecyclerView;

    List<KyoTongResultVo> list;


    ViewGroup listItemview = null;
    // TextView no,line,date;


    //매개변수로 넣어줄 텍뷰
    TextView no, line, date, gotoMainTV;
    List<Integer> viewIdList;
    List<View> viewList;

    ViewGroup gotoMainBottom;

    JSONArray jsonArray;
    JSONObject jsonObject;
    JSONArray jangbiJsonArray;
    JSONObject jangbiJsonObject;
    ArrayList<String> jangbiList;
    String workJsonWorkPlanList;
    String userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("금일작업조회", "TodayWorkPlanSelectActivity onCreate");
        setContentView(R.layout.z_today_work_plan_list);


     //   String userInfo = getIntent().getStringExtra("userInfo");//user정보는 Json문자열로 옴
      //  Log.i("userInfo", userInfo);

        try {
           // String workJsonWorkPlanList = URLDecoder.decode(new Action("post", SERVER_URL+"/TodayWorkPlan/List.do", userInfo,this).execute("").get(), "UTF-8");
           // workJsonWorkPlanList =URLDecoder.decode( getIntent().getStringExtra("workJsonWorkPlanList"),"UTF-8");
            userInfo = getIntent().getStringExtra("userInfo");
            workJsonWorkPlanList =nullCheck(URLDecoder.decode( getIntent().getStringExtra("workJsonWorkPlanList"),"UTF-8"));
            Log.i("투데이 워크플랙 ", workJsonWorkPlanList);
            jsonArray = new JSONArray(workJsonWorkPlanList);
        } catch (JSONException e) {
            Log.e("에러","에러발생");
        }catch ( UnsupportedEncodingException e) {
            Log.e("에러","에러발생");
        }catch (Exception e) {
            Log.e("에러","에러발생");
        }

       // list = new ArrayList<KyoTongResultVo>();


        todayWorkSelectRecyclerView = (RecyclerView) findViewById(R.id.todayWorkSelectRecyclerView);

        TodayWorkPlanSelectRecyclerItem adapter = new TodayWorkPlanSelectRecyclerItem(jsonArray,userInfo);

        todayWorkSelectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todayWorkSelectRecyclerView.setAdapter(adapter);

        gotoMain = (ViewGroup) findViewById(R.id.gotoMain);
        gotoMain.setOnClickListener(this);
        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
        gongsaPrev.setOnClickListener(this);
    }

    @Override
    public String onActionPost(String primitive, String date) {
        return null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.gotoMain:
                /*intent = new Intent(TodayWorkPlanSelectActivity.this, MainActivity.class);
                startActivity(intent);*/
                finish();
            case R.id.gongsaPrev:
                finish();
                break;

        }
    }
}//end Activity
