package com.ex.gongsa.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class GuganOverLapListActivity extends BaseActivity  implements View.OnClickListener  {
    Intent intent;
    TextView guganOverLapMsgTv;//중첩된 구간이 없을시 표출되는 TextView
    RecyclerView workPlanResiterRecyclerItem;//중첩된 구간이 있을시 표출되는 리스트
    LinearLayout gotoMainBottom;
    String workInfo;
    String list;
    ImageView gongsaPrev;
    JSONArray jsonArray;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.z_gugan_overlap_list);
        guganOverLapMsgTv= (TextView)findViewById(R.id.guganOverLapMsgTv);

        workPlanResiterRecyclerItem = (RecyclerView)findViewById(R.id.workPlanResiterRecyclerItem);

        gotoMainBottom = (LinearLayout)findViewById(R.id.gotoMainBottom);

        gongsaPrev=(ImageView)findViewById(R.id.gongsaPrev);

        gotoMainBottom.setOnClickListener(this);
        gongsaPrev.setOnClickListener(this);
        intent= getIntent();

     //   workInfo=intent.getStringExtra("workPlanParam");
        try{
           // list = new Action("get",SERVER_URL+"/WorkPlan/guganOverLapCheck.do",workInfo,this).execute("").get();
            list = intent.getStringExtra("list");
            try{
                Log.i("리스트",list);
            }catch (NullPointerException e){
                Log.e("에러","에러발생");
            }
            if(list.length()==0){
                guganOverLapMsgTv.setVisibility(View.VISIBLE);
                workPlanResiterRecyclerItem.setVisibility(View.GONE);
            }else{

                guganOverLapMsgTv.setVisibility(View.GONE);
                workPlanResiterRecyclerItem.setVisibility(View.VISIBLE);

                jsonArray = new JSONArray(list);
                workPlanResiterRecyclerItem= findViewById(R.id.workPlanResiterRecyclerItem);

                GuganOverLapRecyclerItem adapter = new GuganOverLapRecyclerItem(jsonArray);
                workPlanResiterRecyclerItem.setLayoutManager(new LinearLayoutManager(this));
                workPlanResiterRecyclerItem.setAdapter(adapter);
            }


        }catch (NullPointerException| JSONException e){
            Log.e("에러","에러발생");
        }

    }

    public  String onActionPost(String primitive, String date){
        return null;
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.gongsaPrev:
                finish();
                break;
            case R.id.gotoMainBottom:
                finish();
                break;
        }
    }
}
