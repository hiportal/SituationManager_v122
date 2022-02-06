package com.ex.gongsa.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class UserListDialogItem extends RecyclerView.Adapter<UserListDialogItem.UserListDialogItemViewHolder>{


    List<Map<String,String>> param=null;
    Context context=null;
    RecyclerView userListRecyclerView;
    TextView userTv=null;

    private BaseActivity baseActivity = new BaseActivity() {
        @Override
        public String onActionPost(String primitive, String date) {
            //                    intent = new Intent(context, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(intent);
            return null;
        }
    };
    public class UserListDialogItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        List<Map<String,String>> param=null;
        TextView userName,userPhoneNo,delete_user_tv;
        Context context;
        RecyclerView userListRecyclerView;
        TextView userTv;
        UserListDialogItemViewHolder(View view,List<Map<String,String>> param,Context context,RecyclerView userListRecyclerView,TextView userTv){
            super(view);
            this.param = param;
            this.context=context;
            this.userListRecyclerView=userListRecyclerView;

            userName=(TextView)view.findViewById(R.id.userName);
            userPhoneNo=(TextView)view.findViewById(R.id.userPhoneNo);
            delete_user_tv=(TextView)view.findViewById(R.id.delete_user_tv);
            userName.setOnClickListener(this);
            userPhoneNo.setOnClickListener(this);
            delete_user_tv.setOnClickListener(this);
            this.userTv = userTv;

        }

        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.delete_user_tv:
                    try{
                        JSONObject job = new JSONObject(param.get(getAdapterPosition()).toString());
                        baseActivity.new Action("get",SERVER_URL+"/deleteUser.do",job.toString(),context).execute("").get();
                        param.remove(getAdapterPosition());

                        UserListDialogItem adapter = new UserListDialogItem(param,context,userListRecyclerView,userTv);
                        userListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                        userListRecyclerView.setAdapter(adapter);
                        if(param.size()==0){
                            userTv.setVisibility(View.VISIBLE);
                            userListRecyclerView.setVisibility(View.GONE);
                        }else {
                            userTv.setVisibility(View.GONE);
                            userListRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }catch (JSONException | InterruptedException | ExecutionException e){
                        Log.e("에러","에러발생");
                    }

                   // Toast.makeText(view.getContext(),"으앙",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }//class UserListDialogItemViewHolder

    UserListDialogItem( List<Map<String,String>> param,Context context,RecyclerView userListRecyclerView,TextView userTv){
        this.param = param;
        this.context=context;
        this.userListRecyclerView = userListRecyclerView;
        this.userTv = userTv;


    }

    @Override
    public UserListDialogItemViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.z_user_list_item, parent, false);
        UserListDialogItemViewHolder viewHolder = new UserListDialogItemViewHolder(view,param,context,userListRecyclerView,userTv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserListDialogItemViewHolder  holder,int position){
//        holder.testTv.setText(param.get(position).get("test").toString());

        try{
            holder.userName.setText(param.get(position).get("EMNM").toString());
            holder.userPhoneNo.setText(param.get(position).get("TEL_NO").toString());
        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }
    }

    @Override
    public int getItemCount(){
        try{
            if(param.size() ==0){
                return 0;
            }else{
                return param.size();
            }
        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }
        return 0;
    }
}
