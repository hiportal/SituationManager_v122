package com.ex.gongsa.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;

public class TodayWorkPlanRegisterItem extends RecyclerView.Adapter<TodayWorkPlanRegisterItem.WorkPlanRegisterViewHolder> {

    JSONArray jsonArray;
    String userInfo;
    public class WorkPlanRegisterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView gugan, content, date, company;//no,
        JSONArray jsonArray;
        Intent intent;
        Context context;
        String userInfo;
        WorkPlanRegisterViewHolder(View itemView, Context context, JSONArray jsonArray,String userInfo) {
            super(itemView);
            this.jsonArray = jsonArray;
            this.context = context;
            this.userInfo = userInfo;
            gugan = itemView.findViewById(R.id.gugan);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            company = itemView.findViewById(R.id.company);
            itemView.setOnClickListener(this);

            Log.i("Register item : ",itemView.toString());
            Log.i("Register context : ", context.toString());
            Log.i("Register jsonArray : ", jsonArray.toString());
            Log.i("Register userInfo : ", userInfo.toString());
        }

        @Override
        public void onClick(View v) {
            try {
                intent = new Intent(v.getContext(), TodayWorkPlanRegisterDetailActivity.class);
                intent.putExtra("todayWorkPlanRegister", jsonArray.getJSONObject(getAdapterPosition()).toString());
                intent.putExtra("userInfo",userInfo);
                v.getContext().startActivity(intent);
            } catch (JSONException e) {
                Log.e("에러","에러발생");
            }
        }//onClck
    }//class

    TodayWorkPlanRegisterItem(JSONArray jsonArray,String userInfo) {
        this.jsonArray = jsonArray;
        this.userInfo = userInfo;
    }

    @Override
    public WorkPlanRegisterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("ViewHolder", "WorkPlanRegisterViewHolder ");
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.z_today_work_plan_list_item, parent, false);

        WorkPlanRegisterViewHolder viewHolder = new WorkPlanRegisterViewHolder(view, context, jsonArray,userInfo);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WorkPlanRegisterViewHolder holder, int position) {
        try {
            String drirectCity = jsonArray.getJSONObject(position).get("drctClssNm").toString();
            if(!drirectCity.equals("양방향"))drirectCity+="방향";

            holder.gugan.setText("["+jsonArray.getJSONObject(position).get("routeNm").toString()+"]"+drirectCity+" "+jsonArray.getJSONObject(position).get("strtBlcPntVal").toString()+"km"+" ~ "+jsonArray.getJSONObject(position).get("endBlcPntVal").toString()+"km");
            holder.content.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString().intern()  );
            try{
                /*  holder.date.setText("시작:" + jsonArray.getJSONObject(position).get("blcStrtDttm").toString() + "\n종료:" + jsonArray.getJSONObject(position).get("blcRevocDttm").toString());*/
                holder.date.setText("시작:" + jsonArray.getJSONObject(position).get("blcStrtDttm").toString()+" "+ jsonArray.getJSONObject(position).get("startGongsaHour").toString()+":"+ jsonArray.getJSONObject(position).get("startGongsaMinute").toString() +
                        "\n종료:" + jsonArray.getJSONObject(position).get("blcRevocDttm").toString()+" " + jsonArray.getJSONObject(position).get("endGongsaHour").toString()+":"+ jsonArray.getJSONObject(position).get("endGongsaMinute").toString() );
            }catch (JSONException|NullPointerException e){
                holder.date.setText("기간:" + "");
            }

            try{
                /*holder.company.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString()+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]" );*/
                holder.company.setText("시공사:"+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()/*+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]"*/ );
            }catch (JSONException|NullPointerException e){
                holder.company.setText("시공사:"+"");

            }

        /*    if(jsonArray.getJSONObject(position).get("userCheck").toString().equals("1")){
                holder.gugan.setBackgroundColor(Color.WHITE);
                //  holder.gugan.setBackground(R.color.item_ing);
                holder.content.setBackgroundColor(Color.WHITE);
                holder.date.setBackgroundColor(Color.WHITE);
                holder.gugan.setTextColor(Color.BLACK);
                holder.content.setTextColor(Color.BLACK);
                holder.date.setTextColor(Color.BLACK);

            }*/
        } catch (JSONException|NullPointerException e) {
            Log.e("에러","에러발생");
        }
    }


    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


}
