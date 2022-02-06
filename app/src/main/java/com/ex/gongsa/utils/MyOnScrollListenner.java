/*
package com.ex.gongsa.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ex.gongsa.view.BaseActivity;
import com.ex.gongsa.view.WorkPlanListActivity;
import com.ex.gongsa.view.WorkPlanRecyclerItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class MyOnScrollListenner extends RecyclerView.OnScrollListener {
    BaseActivity baseActivity = new BaseActivity() {
        @Override
        public String onActionPost(String primitive, String date) {
            return null;
        }
    };
    private Context context = null;
    private int curPage = 0;
    private int lastPage = 0;
    private int nextPage = 0;
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;
    WorkPlanRecyclerItem adapter;
    RecyclerView recyclerview;
    private boolean bottomTouch = false;
    private Intent intent = null;
    private String workJsonWorkPlanList = "";
    private WorkPlanRecyclerItem item = null;
    private int totalCount = 0;
    private WorkPlanListActivity activity = null;

    public MyOnScrollListenner() {

    }

    public MyOnScrollListenner(int curPage, int lastPage, int nextPage) {
        {

        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

        if (lastVisibleItemPosition == itemTotalCount) {


            if (getCurPage() != getLastPage()) {

                try {
                    setCurPage(getNextPage());
                    Log.i("PageCheck", "현재페이지:" + getCurPage() + "," + "마지막페이지:" + getLastPage() + ",다음페이지:" + getNextPage() + ",토탈카운트:" + getTotalCount());
                    if (jsonObject.get("prevChk").toString().equals("prevWork")) {
                        onProtocol("prevWork");
                    } else if (jsonObject.get("prevChk").toString().equals("nextDay")) {
                        onProtocol("nextDay");
                    } else {
                        onProtocol("today");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "더 조회하실 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    public void setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public WorkPlanRecyclerItem getAdapter() {
        return adapter;
    }

    public void setAdapter(WorkPlanRecyclerItem adapter) {
        this.adapter = adapter;
    }

    public RecyclerView getRecyclerview() {
        return recyclerview;
    }

    public void setRecyclerview(RecyclerView recyclerview) {
        this.recyclerview = recyclerview;
    }

    public boolean getBottomTouch() {
        return this.bottomTouch;
    }

    public void setBottmTouch(boolean bottomTouch) {
        this.bottomTouch = bottomTouch;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public WorkPlanRecyclerItem getItem() {
        return item;
    }

    public void setItem(WorkPlanRecyclerItem item, JSONArray array) {
        this.item = new WorkPlanRecyclerItem(array);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getWorkJsonWorkPlanList() {
        return workJsonWorkPlanList;
    }

    public void setWorkJsonWorkPlanList(String workJsonWorkPlanList) {
        this.workJsonWorkPlanList = workJsonWorkPlanList;
    }

    public WorkPlanListActivity getActivity() {
        return activity;
    }

    public void setActivity(WorkPlanListActivity activity) {
        this.activity = activity;
    }

    public void onProtocol(String value) {
        jsonArray = getJsonArray();

        try {

            jsonObject = getJsonObject();
            jsonObject.put("prevChk", value);
            //  curPage=curPage+1;
            //userInfoJsonObj.put("curPage", jsonArray.getJSONObject(0).get("nextPage").toString()  );
            Log.println(Log.ASSERT, "작업게획", "----------------------");
            //     Log.println(Log.ASSERT,"작업게획",new Integer(curPage + 1 ) +"");
            Log.println(Log.ASSERT, "작업게획", "----------------------");
            //  userInfoJsonObj.put("totalCount",jsonArray.getJSONObject(jsonArray.length()-1).get("totalCount").toString());
            jsonObject.put("totalCount", jsonArray.getJSONObject(0).get("totalCount").toString());
            Log.i("fdfdf", jsonObject.get("prevChk").toString());
            // Toast.makeText(this,"어제 작업",Toast.LENGTH_SHORT).show();
            BaseActivity baseActivity = new BaseActivity() {
                @Override
                public String onActionPost(String primitive, String date) {
                    return null;
                }
            };
            Log.i("cudate", nextPage + "");
            jsonObject.put("curPage", getNextPage() + "");
            Log.i("par123am", jsonObject.toString());
            baseActivity.new Action("post", SERVER_URL + "/WorkPlan/List.do", jsonObject.toString(), getContext()).execute("");
            */
/*workJsonWorkPlanList = workJsonWorkPlanList.substring(0, workJsonWorkPlanList.length() - 1) + "," + URLDecoder.decode(getIntent().getStringExtra("workJsonWorkPlanList"), "UTF-8").substring(1);*//*

            //workJsonWorkPlanList = getWorkJsonWorkPlanList().substring(0, getWorkJsonWorkPlanList().length() - 1) + "," + URLDecoder.decode(getIntent().getStringExtra("workJsonWorkPlanList"), "UTF-8").substring(1);
            setWorkJsonWorkPlanList(getWorkJsonWorkPlanList().substring(0, getWorkJsonWorkPlanList().length() - 1) + "," + URLDecoder.decode(getIntent().getStringExtra("workJsonWorkPlanList"), "UTF-8").substring(1));
            //jsonArray = new JSONArray(getWorkJsonWorkPlanList());
            setJsonArray(new JSONArray(getWorkJsonWorkPlanList()));
            setAdapter(new WorkPlanRecyclerItem(getJsonArray()));
            recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerview.setAdapter(adapter);
          */
/*      for (int i = 0; i < jsonArray.length(); i++) {
                    Log.i("작업게획", jsonArray.get(i).toString());
                }*//*

            Log.println(Log.ASSERT, "작업게획", "----------------------");
            nextPage = new Integer(jsonArray.getJSONObject(0).getString("nextPage"));
            Log.i("cudate", "================123123123===========");
            Log.i("cudate", "nextPage" + nextPage);
            Log.i("cudate", "nextPage" + nextPage);
            Log.i("cudate", "===========================");
            setNextPage(nextPage);
            setCurPage(new Integer(jsonArray.getJSONObject(0).getString("curPage")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
