package com.ex.gongsa.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.ex.gongsa.Configuration.SERVER_URL;


//금일작업 등록
public class TodayWorkPlanRegisterActivity extends BaseActivity implements View.OnClickListener {

    final String TAG = "TodayWorkPlanRegisterActivity";
    RecyclerView recyclerView;

    ImageView gongsaPrev;
    ViewGroup gotoMain;
    JSONArray jsonArray;
    Intent intent;
    String workJsonWorkPlanList=null;
    String userInfo ="";
    @Override
    public String onActionPost(String primitive, String date) {
   /*     workJsonWorkPlanList=date;
        Log.i("금일작업 등록 리스트",date);*/
        return null;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_today_work_plan_register);
        Log.i("TodayWorkPlanRegister", " onCreate");
        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
        gotoMain = (ViewGroup) findViewById(R.id.gotoMain);
        intent=getIntent();


        gongsaPrev.setOnClickListener(this);
        gotoMain.setOnClickListener(this);
        //String userInfo = getIntent().getStringExtra("userInfo");

    //    Log.i("userInfo", userInfo);


        try {
            workJsonWorkPlanList = URLDecoder.decode(getIntent().getStringExtra("workJsonWorkPlanList"), "UTF-8");
            userInfo = getIntent().getStringExtra("userInfo");
         //   String workJsonWorkPlanList = URLDecoder.decode(new Action("post", SERVER_URL+"/TodayWorkPlan/todayWorkRegisterlist.do", userInfo,this).execute("").get(), "UTF-8");
            Log.i("-","-----------------------------");
            Log.i("TodayWorkplan List", workJsonWorkPlanList.toString());
            Log.i("-","-----------------------------");


            if(workJsonWorkPlanList.length()!=0){

                Log.i("금일작업등록-", workJsonWorkPlanList);
                jsonArray = new JSONArray(workJsonWorkPlanList);


                TodayWorkPlanRegisterItem adapter = new TodayWorkPlanRegisterItem(jsonArray,userInfo);

                recyclerView = (RecyclerView) findViewById(R.id.todayWorkPlanListRecycleView);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);

            }

        } catch (JSONException|NullPointerException| UnsupportedEncodingException e) {
            Log.e("에러","에러발생");
        } catch (Exception e) {
            Log.e("에러","에러발생");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.gotoMain:
                finish();
                break;
        }
    }
}
