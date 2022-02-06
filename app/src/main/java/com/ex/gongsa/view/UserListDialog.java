package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.ex.gongsa.Configuration.SERVER_URL;

public class UserListDialog extends Dialog implements View.OnClickListener  {

    Context context;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Dialog dlg;
    RecyclerView userListRecyclerView;
    LinearLayout insertUser;
    String result ="";

    List<Map<String,String>> list =null;
    Map<String,String> map=null;

    TextView userTv= null;
    LinearLayout click_user_identify,click_user_cancel,goto_back=null;
    List<Map<String,String>> resultMap;
    Intent intent;
    String parsingPhoneNo=null;
    TextView userPhneNo,userName;
    Dialog dialog=null;
    private BaseActivity baseActivity = new BaseActivity() {
        @Override
        public String onActionPost(String primitive, String date) {

            return null;
        }
    };

    public UserListDialog(Context context,JSONObject jsonObject,Intent intent){
        super(context);
        Log.i("생성자","다이얼로그");
        this.context=context;
        this.jsonObject = jsonObject;
        this.intent=intent;
        this.dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.z_user_list);
        userTv= dlg.findViewById(R.id.userTv);
        insertUser= dlg.findViewById(R.id.insertUser);
        goto_back=dlg.findViewById(R.id.goto_back);

        userListRecyclerView = dlg.findViewById(R.id.userListRecyclerView);

        goto_back.setOnClickListener(this);
        insertUser.setOnClickListener(this);
        dlg.show();
        //getInitList();
        try {
            resultMap = getInitList();
            if(resultMap.size()!=0){
                UserListDialogItem adapter = new UserListDialogItem(resultMap,context,userListRecyclerView,userTv);
                userListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                userListRecyclerView.setAdapter(adapter);
            }

        }catch (NullPointerException e){
            Log.e("에러","에러발생");
        }catch (Exception e){
            Log.e("에러","에러발생");
        }
    }

    public List<Map<String,String>> getInitList(){
        list = new ArrayList<Map<String,String>>();
        try{

            result=  baseActivity.new Action("get",SERVER_URL+"/selectUser.do",jsonObject.toString(),context).execute("").get();
            Log.i("생성자",result);
            if(result.length()==0){
                userTv.setVisibility(View.VISIBLE);
                userListRecyclerView.setVisibility(View.GONE);
            }else{
                userTv.setVisibility(View.GONE);
                userListRecyclerView.setVisibility(View.VISIBLE);
                Log.i("생성자","통신결과 확인");
                Log.i("생성자",result);
                jsonArray =new JSONArray(result);
                for(int i = 0 ; i < jsonArray.length();i++){
                    map=new HashMap<String, String>();
                    JSONObject job1=jsonArray.getJSONObject(i);
                    map.put("HDQR_CD",job1.get("HDQR_CD").toString());//1
                    map.put("HDQR_NM",job1.get("HDQR_NM").toString());//2
                    map.put("MTNOF_CD",job1.get("MTNOF_CD").toString());//3
                    map.put("MTNOF_NM",job1.get("MTNOF_NM").toString());//4
                    map.put("SMS_GRP_NM",job1.get("SMS_GRP_NM").toString());//5
                    map.put("EMNM",job1.get("EMNM").toString());//6
                    map.put("PSNM",job1.get("PSNM").toString());//7
                    map.put("TEL_NO",job1.get("TEL_NO").toString());//8
//                map.put("USER_EMNO",jsonObject.get("USER_EMNO").toString());//9
                    map.put("USE_CLSS_CD",job1.get("USE_CLSS_CD").toString());//10
                    map.put("FSTTM_RGSR_ID",job1.get("FSTTM_RGSR_ID").toString());//11
                    map.put("SMS_ADBK_GRP_SEQ",job1.get("SMS_ADBK_GRP_SEQ").toString());//11
                    map.put("SMS_ADBK_SEQ",job1.get("SMS_ADBK_SEQ").toString());//11
                    list.add(map);
                }
                return list;
            }

        }catch (NullPointerException| JSONException | InterruptedException| ExecutionException e){
            Log.e("에러","에러발생");
        }

        return list ;
    }




    public void setUser(String insertedUser,List<Map<String,String>> list){
        userTv.setVisibility(View.GONE);
        userListRecyclerView.setVisibility(View.VISIBLE);
        Map<String,String> map= new HashMap<String,String>();
        try{
            JSONObject jsonObject = new JSONObject(insertedUser);

            map.put("HDQR_CD",jsonObject.get("HDQR_CD").toString());//1
            map.put("HDQR_NM",jsonObject.get("HDQR_NM").toString());//2
            map.put("MTNOF_CD",jsonObject.get("MTNOF_CD").toString());//3
            map.put("MTNOF_NM",jsonObject.get("MTNOF_NM").toString());//4
            map.put("SMS_GRP_NM",jsonObject.get("SMS_GRP_NM").toString());//5
            map.put("EMNM",jsonObject.get("EMNM").toString());//6
            map.put("PSNM",jsonObject.get("PSNM").toString());//7
            map.put("TEL_NO",jsonObject.get("TEL_NO").toString());//8
//                map.put("USER_EMNO",jsonObject.get("USER_EMNO").toString());//9
            map.put("USE_CLSS_CD",jsonObject.get("USE_CLSS_CD").toString());//10
            map.put("FSTTM_RGSR_ID",jsonObject.get("FSTTM_RGSR_ID").toString());//11
        }catch (NullPointerException| JSONException e){
            Log.e("에러","에러발생");
        }catch (Exception e){
            Log.e("에러","에러발생");
        }
        list.add(map);
        UserListDialogItem adapter = new UserListDialogItem(list,context,userListRecyclerView,userTv);
        userListRecyclerView.setAdapter(adapter);
    }


    public List<Map<String,String>> deleteUser(){

        return null;
    }

    public void setAdapter(){

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.insertUser:
                insertUserName(jsonObject, intent);


                break;
            case R.id.click_user_cancel:
                dialog.dismiss();
                break;
            case R.id.click_user_identify:
                try{
                    String data =baseActivity.new Action("get",SERVER_URL+"/insertUser.do",jsonObject.toString(),context).execute("").get();
                    Log.i("데이타 확인",URLDecoder.decode(data,"UTF-8"));
                    if(list.size()==0){
                        UserListDialogItem adapter = new UserListDialogItem(resultMap,context,userListRecyclerView,userTv);
                        userListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                        userListRecyclerView.setAdapter(adapter);
                    }
                    setUser(URLDecoder.decode(data,"UTF-8"), list);
                    dialog.dismiss();
                }catch (NullPointerException | InterruptedException| ExecutionException| UnsupportedEncodingException e){
                    Log.e("에러","에러발생");
                }catch (Exception e){
                    Log.e("에러","에러발생");
                }

                break;
            case R.id.goto_back:

                dlg.dismiss();
                break;
        }
    }

    public void insertUserName(final JSONObject jsonObject, final Intent intent) {


        final EditText editText = new EditText(context);
        //    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("추가하실 사용자의 이름을 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            String phoneNo = editText.getText().toString();
                          /*  String parsingPhoneNo = phoneNo.substring(0,3)+phoneNo.substring(3,7)+phoneNo.substring(7,11);*/
                            jsonObject.put("newUserName",phoneNo);
                            insertPhonNo( jsonObject,intent);
                        }catch (NullPointerException| JSONException  e){
                            Log.e("에러","에러발생");
                        }catch(Exception e){

                        }

                        //   codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    public void insertPhonNo(final JSONObject jsonObject,final  Intent intent) {


        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("추가하실 사용자의 전화번호를 입력해주세요");
        builder.setView(editText);
        editText.clearComposingText();
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            String phoneNo = editText.getText().toString();
                            try{
                                 parsingPhoneNo = phoneNo.substring(0,3)+"-"+phoneNo.substring(3,7)+"-"+phoneNo.substring(7,11);
                            }catch (ArrayIndexOutOfBoundsException|StringIndexOutOfBoundsException e){
                                 parsingPhoneNo =phoneNo;
                            }
                           // String parsingPhoneNo = phoneNo.substring(0,3)+"-"+phoneNo.substring(3,7)+"-"+phoneNo.substring(7,11);
                            jsonObject.put("newUserPhoneNo",parsingPhoneNo);
                            //insertProtocol( jsonObject);
                            openDialog(context, R.layout.z_insert_user_info, R.id.click_user_identify, jsonObject, jsonObject,intent);

                            Log.i("data123","통신후에 받아오는지 확인:"+result);
                        }catch (NullPointerException|JSONException e){
                            Log.e("에러","에러발생");
                        }

                        //   codeMap.put("humanCnt", editText.getText().toString());

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    public void insertProtocol(JSONObject jsonObject){
        try{
            result=  baseActivity.new Action("get",SERVER_URL+"/insertUser.do",jsonObject.toString(),context).execute("").get();
            Log.i("생성자",result);
        }catch (Exception e){
            Log.e("에러","에러발생");
        }
    }
  //  new CustomDialog(context, R.layout.z_insert_user_info, R.id.click_user_identify, jsonObject, jsonObject,intent).userInsertReturnData();
    public void openDialog(Context context, int layout, int id, JSONObject jsonStrValue, JSONObject jsonCodeValue ,Intent intent){
        dialog =new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        click_user_identify=(LinearLayout)dialog.findViewById(R.id.click_user_identify);
        click_user_cancel =(LinearLayout)dialog.findViewById(R.id.click_user_cancel);



        click_user_cancel.setOnClickListener(this);
        click_user_identify.setOnClickListener(this);

        userPhneNo=(TextView)dialog.findViewById(R.id.userPhneNo);
        userName=(TextView)dialog.findViewById(R.id.userName);
        try{
            userPhneNo.append(jsonCodeValue.get("newUserPhoneNo").toString());
            userName.append(jsonCodeValue.get("newUserName").toString());
        }catch (NullPointerException|JSONException e){
            Log.e("에러","에러발생");
        }
        dialog.show();
    }
}
