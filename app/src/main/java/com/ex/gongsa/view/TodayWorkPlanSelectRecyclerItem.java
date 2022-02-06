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

import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;

public class TodayWorkPlanSelectRecyclerItem extends RecyclerView.Adapter<TodayWorkPlanSelectRecyclerItem.TodayWorkViewHolder> {
    private JSONArray jsonArray;
    String userInfo;
    //______________________________________________________________________________
    public class TodayWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView line, data, content,company;
        Intent intent;
        ImageView approvalCheck;
        String userInfo;
        TodayWorkViewHolder(View itemView, JSONArray jsonArray,String userInfo) {
            super(itemView);
            approvalCheck = itemView.findViewById(R.id.approvalCheck);
            line = itemView.findViewById(R.id.line);
            data = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.content);
            company = itemView.findViewById(R.id.company);
            this.userInfo = userInfo;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), TodayWorkPlanSelectDetailActivity.class);
            try {
                intent.putExtra("userInfo",userInfo);
                intent.putExtra("TodayWorkListJsonValue", jsonArray.getJSONObject(getAdapterPosition()).toString());
            } catch (JSONException|NullPointerException e) {
                Log.e("에러","에러발생");
            }
            v.getContext().startActivity(intent);
        }

    }//end class


    /**
     * 생성자
     * @param jsonArray
     */
    TodayWorkPlanSelectRecyclerItem(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        Log.i("TodayWorkPlanSelect", "Constuctor");

    }

    TodayWorkPlanSelectRecyclerItem(JSONArray jsonArray,String userInfo) {
        this.jsonArray = jsonArray;
        this.userInfo = userInfo;
        Log.i("TodayWorkPlanSelect", "Constuctor");

    }

    @Override
    public TodayWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.z_today_work_plan_list_select_item, parent, false);
        TodayWorkViewHolder viewHolder = new TodayWorkViewHolder(view, jsonArray,userInfo);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodayWorkViewHolder holder, int position) {
        try {

            String[] lineArray = jsonArray.getJSONObject(position).get("acdtLoctCtnt").toString().split("km");
            holder.line.setText(lineArray[0] + "km" + lineArray[1] + "km");
            holder.data.setText("시작:"+jsonArray.getJSONObject(position).get("startDate").toString()+" "+jsonArray.getJSONObject(position).get("startHour").toString()+":"+jsonArray.getJSONObject(position).get("startMinute").toString() +
                    "\n종료:" + jsonArray.getJSONObject(position).get("endDate")+" "+jsonArray.getJSONObject(position).get("endHour").toString()+":"+jsonArray.getJSONObject(position).get("endMinute").toString() );
            holder.content.setText(jsonArray.getJSONObject(position).get("gongsaContent").toString());
            holder.company.setText("시공사:"+jsonArray.getJSONObject(position).get("cstrCrprNm").toString());
            if ((jsonArray.getJSONObject(position).get("prosStatCd").toString().equals("R"))) {
                holder.approvalCheck.setImageResource(R.drawable.icon_wait);
            } else if ((jsonArray.getJSONObject(position).get("prosStatCd").toString().equals("S"))) {
                holder.approvalCheck.setImageResource(R.drawable.icon_approval);
            } else {
                holder.approvalCheck.setImageResource(R.drawable.icon_disapproval);
            }
        } catch (JSONException|NullPointerException e) {
            Log.e("에러","에러발생");
        }
    }


    @Override
    public int getItemCount() {

        try{
            return jsonArray.length();
        }catch (NullPointerException e){
            return 0;
        }
    }
}
