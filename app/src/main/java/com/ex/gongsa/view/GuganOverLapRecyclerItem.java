package com.ex.gongsa.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;

public class GuganOverLapRecyclerItem  extends RecyclerView.Adapter<GuganOverLapRecyclerItem.GuganOverLapRecyclerItemViewHolder>{

    private JSONArray jsonArray;
    public class GuganOverLapRecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        JSONArray jsonArray;
        TextView gugan, content, date,company;//no,
        Intent intent;
        String userInfo;
        View v;
        GuganOverLapRecyclerItemViewHolder(View v,JSONArray jsonArray){
            super(v);
            this.jsonArray=jsonArray;
            gugan = itemView.findViewById(R.id.gugan);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            this.userInfo =userInfo;
            company = itemView.findViewById(R.id.company);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            intent = new Intent(v.getContext(),GuganOverLapWorkPlanDetailActivity.class);
            try{

                intent.putExtra("guganWorkDetail", jsonArray.getJSONObject(getAdapterPosition()).toString());

            }catch (JSONException e){
                Log.e("에러","에러발생");
            }
            v.getContext().startActivity(intent);
        }
    }

    public GuganOverLapRecyclerItem(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    @Override
    public void onBindViewHolder(GuganOverLapRecyclerItem.GuganOverLapRecyclerItemViewHolder holder,int position){
        try{
            String drirectCity = jsonArray.getJSONObject(position).get("drctClssNm").toString();
            if(!drirectCity.equals("양방향"))drirectCity+="방향";

            holder.gugan.setText("["+jsonArray.getJSONObject(position).get("routeNm").toString()+"]"+drirectCity+" "+jsonArray.getJSONObject(position).get("strtBlcPntVal").toString()+"km"+" ~ "+jsonArray.getJSONObject(position).get("endBlcPntVal").toString()+"km");
            holder.content.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString() + " " );
            try{
                /*  holder.date.setText("시작:" + jsonArray.getJSONObject(position).get("blcStrtDttm").toString() + "\n종료:" + jsonArray.getJSONObject(position).get("blcRevocDttm").toString());*/
                holder.date.setText("시작:" + jsonArray.getJSONObject(position).get("blcStrtDttm").toString()+" "+ jsonArray.getJSONObject(position).get("startGongsaHour").toString()+":"+ jsonArray.getJSONObject(position).get("startGongsaMinute").toString() +
                        "\n종료:" + jsonArray.getJSONObject(position).get("blcRevocDttm").toString()+" " + jsonArray.getJSONObject(position).get("endGongsaHour").toString()+":"+ jsonArray.getJSONObject(position).get("endGongsaMinute").toString() );
            }catch (JSONException|NullPointerException e){
                Log.e("에러","Exception");
                holder.date.setText("기간:" + "");
            }

            try{
                /*holder.company.setText(jsonArray.getJSONObject(position).get("cnstnCtnt").toString()+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]" );*/
                holder.company.setText("시공사:"+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()/*+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]"*/ );
            }catch (JSONException|NullPointerException e){
                holder.company.setText("시공사:"+"");
//                holder.company.setText(""+"["+jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]");
//                Log.i("cntsnCtnt",jsonArray.getJSONObject(position).get("cnstnCtnt").toString());
                // Log.i("cstrCrprRcrdCtnt",jsonArray.getJSONObject(position).get("cstrCrprRcrdCtnt").toString()+"]");
            }

        }catch (JSONException|NullPointerException e){
            Log.e("에러","에러발생");
        }
    }

    @Override
    public GuganOverLapRecyclerItemViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.z_today_work_plan_list_item,parent,false);
        GuganOverLapRecyclerItemViewHolder viewHolder = new GuganOverLapRecyclerItemViewHolder(view,jsonArray);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        try{
            return jsonArray.length();
        }catch (NullPointerException e){
            Log.e("에러","Exception");
            return 0;
        }
    }
}
