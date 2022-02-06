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
import android.widget.Toast;

import com.ex.gongsa.vo.KyoTongResultVo;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class WorkPlanResisterListActivity extends BaseActivity  implements View.OnClickListener {


    final String TAG = "WorkPlanListActivity";
    // TextView no,line,date;
  //  List<KyoTongResultVo> list;


    Intent intent;
    ViewGroup listItemview = null;
    // TextView no,line,date;


    //매개변수로 넣어줄 텍뷰
    TextView no, line, date, gotoMainTV;
    List<Integer> viewIdList;
    List<View> viewList;
    ImageView gongsaPrev;
    ViewGroup gotoMainBottom;

    JSONArray jsonArray;
    JSONObject jsonObject;
    JSONArray jangbiJsonArray;
    JSONObject jangbiJsonObject;
    ArrayList<String> jangbiList;
    String list;
    RecyclerView workPlanResiterRecyclerItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_work_plan_register_list_item);
        String userInfo = getIntent().getStringExtra("userInfo");
        gongsaPrev = (ImageView) findViewById(R.id.gongsaPrev);
        gotoMainBottom = (ViewGroup) findViewById(R.id.gotoMainBottom);
        gotoMainTV = (TextView) findViewById(R.id.gotoMainTV);
        // list

        //개발용 테스트 계정 전화번호
        jangbiList = new ArrayList<String>();
        jsonObject = new JSONObject();
        //첫번째 매개변수는 리스트에 표출되어질 목록의 리스트, 두번쨰 매개변수는 recycleview에 사용될 레이아웃



        gongsaPrev.setOnClickListener(this);
        gotoMainBottom.setOnClickListener(this);

        //하단 메인으로 레이아웃
//        Log.i("userInfo",userInfo);
        try{

           list = getIntent().getStringExtra("list");
            // list = new Action("get",SERVER_URL+"/WorkPlan/loadWorkPlan.do",userInfo,this).execute("").get();
            Log.i("통신결과",list);
            JSONArray jsonArray = new JSONArray(list);
            for(int i =0;i<jsonArray.length();i++){
                Log.i("어레이",jsonArray.getJSONObject(i).toString());
            }

            workPlanResiterRecyclerItem= findViewById(R.id.workPlanResiterRecyclerItem);

            WorkPlanResisterListReRecyclerItem adapter = new WorkPlanResisterListReRecyclerItem(new JSONArray(list),userInfo);
            workPlanResiterRecyclerItem.setLayoutManager(new LinearLayoutManager(this));
            workPlanResiterRecyclerItem.setAdapter(adapter);

        }catch (JSONException e){
            Log.e("에러","예외");
        }catch (Exception e){
            Log.e("에러","예외");
        }


    }

    @Override
    public String onActionPost(String primitive, String date){
        return null;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.gotoMainBottom:
                finish();
                break;
        }
    }
}
