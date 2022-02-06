package com.ex.gongsa.view;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WorkPlanRecyclerItem extends RecyclerView.Adapter<WorkPlanRecyclerItem.WorkPlanViewHolder> {

    private JSONArray jsonArray = null;
    private String userInfo;
    private int viewType = 0;
    Context context_item;
    WorkPlanViewHolder viewHolder;
    RecyclerView recyclerView;

    List<JSONObject> jsonJavaList;

    WorkPlanViewHolder viewHolderii =null;



    public class WorkPlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TextView textView1;
        Context context;
        TextView company,gugan,date,content;
        ImageView approvalCheck;
        int i = 0;
        Intent intent;
        JSONObject jsonObject;
        JSONArray jsonArray = null;
        String userInfo;
        List<JSONObject> jsonJavaList;

    /*    public WorkPlanViewHolder(View itemView, Context context, JSONArray jsonArray,String userInfo) {

            super(itemView);
            Log.i("순서",3+"");
            Log.i("WorkPlanViewHolder", " -------- Constructor");
            this.context = context;
            *//*     this.dData = dData;*//*
            this.jsonArray = jsonArray;
            approvalCheck = itemView.findViewById(R.id.approvalCheck);
            *//*gugan = itemView.findViewById(R.id.no);*//*
            gugan = itemView.findViewById(R.id.gugan);
            date = itemView.findViewById(R.id.date);
            company = itemView.findViewById(R.id.company);
            content = itemView.findViewById(R.id.content);
            this.userInfo =userInfo ;
            itemView.setOnClickListener(this);
        }//method ViewHolder*/

        public WorkPlanViewHolder(View itemView, Context context,  List<JSONObject> jsonJavaList,String userInfo) {

            super(itemView);
            Log.i("순서",3+"");
            Log.i("WorkPlanViewHolder", " -------- Constructor");
            this.context = context;
            /*     this.dData = dData;*/
            this.jsonJavaList = jsonJavaList;
            approvalCheck = itemView.findViewById(R.id.approvalCheck);
            /*gugan = itemView.findViewById(R.id.no);*/
            gugan = itemView.findViewById(R.id.gugan);
            date = itemView.findViewById(R.id.date);
            company = itemView.findViewById(R.id.company);
            content = itemView.findViewById(R.id.content);
            this.userInfo =userInfo ;
            itemView.setOnClickListener(this);
        }//method ViewHolder

        public void addList(List<JSONObject> addedjsonJavaListt){
            for(int i = 0 ; i < addedjsonJavaListt.size();i++){
                jsonJavaList.add(addedjsonJavaListt.get(i));
            }

        }


        @Override
        public void onClick(View v) {
            try {
                intent = new Intent(v.getContext(), WorkPlanDetailActivity.class);

             //   intent.putExtra("WorkPlanJsonValue", jsonArray.getJSONObject(getAdapterPosition()).toString());
                intent.putExtra("WorkPlanJsonValue", jsonJavaList.get(getAdapterPosition()).toString());
                intent.putExtra("userInfo",userInfo);
                v.getContext().startActivity(intent);
            } catch (Exception e) {
               Log.e("예외","예외발생");
            }
        }
    }//class Viewholder

    public WorkPlanRecyclerItem(){

    }
    public WorkPlanRecyclerItem(JSONArray jsonArray,String userInfo,int viewType) {

        this.jsonArray = jsonArray;
        this.userInfo = userInfo;
        this.viewType = viewType;
    }//생성자

   /* public WorkPlanRecyclerItem(JSONArray jsonArray,String userInfo,int viewType,RecyclerView recyclerView) {

        this.jsonArray = jsonArray;
        this.userInfo = userInfo;
        this.viewType = viewType;
        this.recyclerView = recyclerView;
    }

    public WorkPlanRecyclerItem(JSONArray jsonArray,String userInfo,Context context) {
        this.jsonArray = jsonArray;
        this.userInfo = userInfo;
        this.context_item = context;
    }//생성자*/

    public WorkPlanRecyclerItem( Context context_item,List<JSONObject> jsonJavaList,String userInfo,int viewType,RecyclerView recyclerView) {

        this.jsonJavaList = jsonJavaList;
        this.userInfo = userInfo;
        this.viewType = viewType;
        this.recyclerView = recyclerView;
        this.context_item = context_item;
        Log.i("추적",1+"");
    }



    public WorkPlanRecyclerItem(List<JSONObject> jsonJavaList,String userInfo,Context context) {
        this.jsonJavaList = jsonJavaList;
        this.userInfo = userInfo;
        this.context_item = context;
    }//생성자



    @Override
    public WorkPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        Log.i("dfasdfa",parent.getHeight()+"");
        //recyclerView.smoothScrollToPosition(parent.getHeight()/1105);
        Log.i("추적",2+"");
        //recyclerView.scrollToPosition(parent.getHeight()/6);
        Context context;
        if (context_item == null) {
            context = parent.getContext();
        } else {
            context = context_item;
        }
        if(viewType == 0) {
            Log.i("순서",1+"");

            Log.i("추적",3+"");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.z_work_plan_list_item, parent, false);
            WorkPlanViewHolder viewHolder = new WorkPlanViewHolder(view, context, jsonJavaList, userInfo);

            return viewHolder;
        }else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.z_work_plan_list_item, parent, false);
            WorkPlanViewHolder viewHolder = new WorkPlanViewHolder(view, context, jsonJavaList, userInfo);
//            viewHolder.addList(jsonJavaList);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(WorkPlanViewHolder holder, int position) {

        try {

            Log.i("순서",2+"");
            try{
// 승인 : 12 비승인 : 01
                if (jsonJavaList.get(position).get("trfcLimtStatCd").toString().equals("12")) {
                    holder.approvalCheck.setImageResource(R.drawable.icon_approval);
                } else {
                    holder.approvalCheck.setImageResource(R.drawable.icon_wait);
                }
                Log.i("workplanlist : ",jsonJavaList.get(position).get("trfcLimtStatCd").toString());

//                if (jsonJavaList.get(position).get("trfcLimtStatCd").toString().equals("01")) {
//                    holder.approvalCheck.setImageResource(R.drawable.icon_wait);
//                } else if (jsonJavaList.get(position).get("trfcLimtStatCd").toString().equals("12")) {
//
//                    holder.approvalCheck.setImageResource(R.drawable.icon_approval);
//                } else {
//                    Log.i("추적","4" + jsonJavaList.get(position).get("trfcLimtStatCd").toString() );
//                    holder.approvalCheck.setImageResource(R.drawable.icon_disapproval);
//                }
            }catch (Exception e){
                holder.approvalCheck.setImageResource(R.drawable.icon_wait);
            }
            try{

                /*holder.company.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString()+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]" );*/
                holder.company.setText("시공사:"+jsonJavaList.get(position).get("cstrCrprRcrdCtnt").toString()/*+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]"*/ );
            }catch (Exception e){
                holder.company.setText(""+"["+jsonJavaList.get(position).get("cstrCrprRcrdCtnt").toString()+"]");
//                Log.i("cntsnCtnt",jsonArray.getJSONObject(position).get("cnstnCtnt").toString());
                // Log.i("cstrCrprRcrdCtnt",jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]");
            }

            try{
                holder.content.setText(jsonJavaList.get(position).get("cnstnCtnt").toString());
            }catch (Exception e){
                holder.content.setText("");
            }

            try{
                String directCity = jsonJavaList.get(position).get("drctClssNm").toString();
                if(!directCity.equals("양방향")){
                    directCity+="방향";
                }
                holder.gugan.setText("[" + jsonJavaList.get(position).get("routeNm").toString()+"]"+directCity+" "+jsonJavaList.get(position).get("strtBlcPntVal").toString()+"km ~ "+jsonJavaList.get(position).get("endBlcPntVal").toString()+"km");
            }catch (Exception e
            ){
                holder.gugan.setText("\n");
            }

            // holder.gugan.setText("공사구간 : [" + jsonArray.getJSONObject(position).get("routeNm").toString()+"]");
            try{
                holder.date.setText("시작:" + jsonJavaList.get(position).get("blcStrtDttm").toString()+" "+ jsonJavaList.get(position).get("startGongsaHour").toString()+":"+jsonJavaList.get(position).get("startGongsaMinute").toString() +
                        "\n종료:" + jsonJavaList.get(position).get("blcRevocDttm").toString()+" " +jsonJavaList.get(position).get("endGongsaHour").toString()+":"+ jsonJavaList.get(position).get("endGongsaMinute").toString() );
            }catch (JSONException | NullPointerException e){
                holder.date.setText("");
            }

        } catch (JSONException | NullPointerException e) {
            Log.e("에러","예외");
        }


        /*try {
            Log.i("순서",2+"");
            try{
                if (jsonArray.getJSONObject(position).get("trfcLimtStatCd").toString().equals("01")) {
                    holder.approvalCheck.setImageResource(R.drawable.icon_wait);
                } else if (jsonArray.getJSONObject(position).get("trfcLimtStatCd").toString().equals("12")) {
                    holder.approvalCheck.setImageResource(R.drawable.icon_approval);
                } else {
                    holder.approvalCheck.setImageResource(R.drawable.icon_disapproval);
                }
            }catch (Exception e){
                holder.approvalCheck.setImageResource(R.drawable.icon_wait);
            }
            try{
                *//*holder.company.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString()+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]" );*//*
                holder.company.setText("시공사:"+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()*//*+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]"*//* );
            }catch (Exception e){
                holder.company.setText(""+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]");
//                Log.i("cntsnCtnt",jsonArray.getJSONObject(position).get("cnstnCtnt").toString());
               // Log.i("cstrCrprRcrdCtnt",jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]");
            }

            try{
                holder.content.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString());
            }catch (Exception e){
                holder.content.setText("");
            }

            try{
                String directCity = jsonArray.getJSONObject(position).get("drctClssNm").toString();
                if(!directCity.equals("양방향")){
                    directCity+="방향";
                }
                holder.gugan.setText("[" + jsonArray.getJSONObject(position).get("routeNm").toString()+"]"+directCity+" "+jsonArray.getJSONObject(position).get("strtBlcPntVal").toString()+"km ~ "+jsonArray.getJSONObject(position).get("endBlcPntVal").toString()+"km");
            }catch (Exception e
            ){
                holder.gugan.setText("\n");
            }

           // holder.gugan.setText("공사구간 : [" + jsonArray.getJSONObject(position).get("routeNm").toString()+"]");
            try{
                holder.date.setText("시작:" + jsonArray.getJSONObject(position).get("blcStrtDttm").toString()+" "+ jsonArray.getJSONObject(position).get("startGongsaHour").toString()+":"+ jsonArray.getJSONObject(position).get("startGongsaMinute").toString() +
                                     "\n종료:" + jsonArray.getJSONObject(position).get("blcRevocDttm").toString()+" " + jsonArray.getJSONObject(position).get("endGongsaHour").toString()+":"+ jsonArray.getJSONObject(position).get("endGongsaMinute").toString() );
            }catch (Exception e){
                holder.date.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    @Override
    public int getItemViewType(int position) {
        return viewType;
    }


    //추가
    public void addItem(List<JSONObject> jsonJavaList11){


       /* for(int i =getItemCount();i<(getItemCount()+jsonJavaList.size());i++ ){
            LayoutInflater inflater = (LayoutInflater) context_item.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.z_work_plan_list_item, recyclerView, false);

            for(int ii=0;ii<jsonJavaList11.size();i++){
                jsonJavaList.add(jsonJavaList11.get(ii));
            }
            WorkPlanViewHolder viewHolder = new WorkPlanViewHolder(view, context_item, jsonJavaList, userInfo);

            customBindVIewHolder(viewHolder,i, null, jsonJavaList.get(0));
        }
*/
        //return viewHolder;
    }

    @Override
    public int getItemCount() {
     /*   try {
            return jsonArray.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }*/
        if(jsonJavaList==null){
            return 0;
        }else{
            return jsonJavaList.size();
        }
    }

    public void customBindVIewHolder(WorkPlanViewHolder holder, int position, List<Object> payloads,JSONObject jsonArray){
        super.onBindViewHolder(holder, position, payloads);
        try {

            Log.i("순sdfsdfsdfs서",position+"");
            try{
                if (jsonJavaList.get(position).get("trfcLimtStatCd").toString().equals("12")) {
                    holder.approvalCheck.setImageResource(R.drawable.icon_approval);
                } else {
                    holder.approvalCheck.setImageResource(R.drawable.icon_wait);
                }

//                if (jsonJavaList.get(position).get("trfcLimtStatCd").toString().equals("01")) {
//                    holder.approvalCheck.setImageResource(R.drawable.icon_wait);
//                } else if (jsonJavaList.get(position).get("trfcLimtStatCd").toString().equals("12")) {
//                    holder.approvalCheck.setImageResource(R.drawable.icon_approval);
//                } else {
//                    holder.approvalCheck.setImageResource(R.drawable.icon_disapproval);
//                }
            }catch (NullPointerException e){
                holder.approvalCheck.setImageResource(R.drawable.icon_wait);
            }
            try{

                /*holder.company.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString()+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]" );*/
                holder.company.setText("시공사:"+jsonJavaList.get(position).get("cstrCrprRcrdCtnt").toString()/*+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]"*/ );
            }catch (NullPointerException e){
                holder.company.setText(""+"["+jsonJavaList.get(position).get("cstrCrprRcrdCtnt").toString()+"]");
//                Log.i("cntsnCtnt",jsonArray.getJSONObject(position).get("cnstnCtnt").toString());
                // Log.i("cstrCrprRcrdCtnt",jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]");
            }

            try{
                holder.content.setText(jsonJavaList.get(position).get("cnstnCtnt").toString());
            }catch (NullPointerException  | JSONException e){
                holder.content.setText("");
            }

            try{
                String directCity = jsonJavaList.get(position).get("drctClssNm").toString();
                if(!directCity.equals("양방향")){
                    directCity+="방향";
                }
                holder.gugan.setText("[" + jsonJavaList.get(position).get("routeNm").toString()+"]"+directCity+" "+jsonJavaList.get(position).get("strtBlcPntVal").toString()+"km ~ "+jsonJavaList.get(position).get("endBlcPntVal").toString()+"km");
            }catch (NullPointerException  | JSONException e ){
                holder.gugan.setText("\n");
            }

            // holder.gugan.setText("공사구간 : [" + jsonArray.getJSONObject(position).get("routeNm").toString()+"]");
            try{
                holder.date.setText("시작:" + jsonJavaList.get(position).get("blcStrtDttm").toString()+" "+ jsonJavaList.get(position).get("startGongsaHour").toString()+":"+ jsonJavaList.get(position).get("startGongsaMinute").toString() +
                        "\n종료:" + jsonJavaList.get(position).get("blcRevocDttm").toString()+" " +jsonJavaList.get(position).get("endGongsaHour").toString()+":"+ jsonJavaList.get(position).get("endGongsaMinute").toString() );
            }catch (NullPointerException | JSONException e){
                holder.date.setText("");
            }

        } catch (NullPointerException e) {
            //e.printStackTrace();
            Log.e("에러","예외 발생");
        }catch ( JSONException e) {
            //e.printStackTrace();
            Log.e("에러","예외 발생");
        }catch (Exception e){
            Log.e("에러","예외 발생");
        }
    }

    @Override
    public void onBindViewHolder(WorkPlanViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);



    }
}//class WorkPlanRecyclerItem

