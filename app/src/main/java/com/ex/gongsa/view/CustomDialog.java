package com.ex.gongsa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Configuration;
import com.ex.situationmanager.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.ex.gongsa.Common.nullCheck;
import static com.ex.gongsa.Configuration.SERVER_URL;
import static com.ex.gongsa.view.WorkPlanRegisterActivity.codeMap;
//import static com.ex.gongsa.view.AdapterSpinnerUtil.codeMap;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private int layout;
    private int id;

    Intent intent;
    Dialog dlg;

    //커스텀 다이얼로그
    ViewGroup sendApprovalRequest;
    ViewGroup clickOk, alertOk, JaupclickOk;

    //2021.06 안내문구표출 TEST
    LinearLayout correctBtn;

    private String alertMsg = null;

    private TextView gongsa_result_bonbuDialog;
    private TextView gongsa_result_jisaDialog;
    private TextView gongsa_result_nosunDialog;
    private TextView gongsa_result_direnctionDialog;
    private TextView gongsa_result_roadDialog;
    private TextView gongsa_result_gamdokDialog;
    private TextView gongsa_result_gongsaContent;
    private TextView gongsa_result_startTime;
    private TextView gongsa_result_result_endTime;
    private TextView gongsa_result_startGugan;
    private TextView gongsa_result_endGugan;
    private TextView gongsa_result_Inwonjangbi;
    private TextView gongsa_result_gosundaeNum;
    private TextView gongsa_result_workType;
    private TextView alertContent;
    private TextView chdanBangSikDetail;
    private TextView userInputCheck;
    private TextView userCompany;
    private LinearLayout userCheckTextView;
    private LinearLayout end_work_Li,userconfirmedCheck;
    private LinearLayout updateContent_Li;
    private LinearLayout guganOverLapCheck_Li;
    private LinearLayout click_user_identify;
    private LinearLayout click_user_cancel;
    private LinearLayout sendApprovalRequest_in_work_plan_update;
    private LinearLayout sendApprovalRequest_in_todaywork_plan_update;
    JSONObject jsonStrValue;
    JSONObject jsonCodeValue;
    String toUserMsg;
    String userInfo;

    //202008_수정중 작업상세내용 완료 코드 추가
    int btnClick = 0;
    JSONObject jsonObj;
    ProgressDialog progressDialog;


    //메인화면 유저정보 셋팅을 위한 TestView
    TextView userPhneNo, userName, userBonbu, userJisa;
    int index;
    String data;

    //
    LinearLayout li_pa;
    LinearLayout del_ok;
    LinearLayout del_no;
    TextView warn_msg;
    List<EditText> que_et_list;
    View li;
    LinearLayout load_li;
    TextView tv;



    private BaseActivity baseActivity = new BaseActivity() {


        @Override
        public String onActionPost(String primitive, String date) {
            dlg.dismiss();

          if (btnClick == 33){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.z_today_work_plan_content_popup, (ViewGroup) TodayWorkPlanSelectDetailPopUp);
                TextView todayWorkEndPopUp = (TextView)layout.findViewById(R.id.todayWorkEndPopUp);
                Toast toast = new Toast(getContext());

                    todayWorkEndPopUp.setText("작업종료했습니다.");
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                    intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //dlg.dismiss();
                    dlg.getContext().startActivity(intent);
                    progressDialog.dismiss();
                }

               return null;

        }
    };

    File file = null;
    private Object TodayWorkPlanSelectDetailPopUp;

    public CustomDialog(Context context, int layout, int id, String toUserMsg, JSONObject jsonCodeValue,File file) {
        super(context);
        this.context = context;
        this.dlg = new Dialog(context);
        this.layout = layout;
        this.id = id;


        this.toUserMsg = toUserMsg;
        this.jsonCodeValue = jsonCodeValue;
        this.file= file;

        callFunction();
    }//생성자

    public CustomDialog(Context context, int layout, int id, String alertMsg, List<EditText> que_et_list, int index,LinearLayout li_pa) {
        super(context);
        this.context = context;
        this.dlg = new Dialog(context);
        this.layout = layout;
        this.id = id;
        this.index = index;
        this.li_pa = li_pa;
        this.alertMsg = alertMsg;
        this.que_et_list = que_et_list;

        callFunction();
    }//생성자


    public CustomDialog(Context context, int layout, int id, String toUserMsg, JSONObject jsonCodeValue ) {
        super(context);
        this.context = context;
        this.id = id;
        this.layout = layout;
        this.dlg = new Dialog(context);
        this.toUserMsg = toUserMsg;
        this.jsonCodeValue = jsonCodeValue;

    callFunction();
}//생성자

    /**
     * 생성자
     * @param context
     * @param layout
     * @param id
     * @param jsonStrValue
     * @param jsonCodeValue
     */
    public CustomDialog(Context context, int layout, int id, JSONObject jsonStrValue, JSONObject jsonCodeValue ,Intent intent) {
        super(context);
        this.context = context;
        this.id = id;
        this.layout = layout;
        this.dlg = new Dialog(context);
        this.jsonStrValue = jsonStrValue;
        this.jsonCodeValue = jsonCodeValue;
        this.intent = intent;

        callFunction();

    }//생성자

    public CustomDialog(Context context, int layout, int id, JSONObject jsonStrValue, JSONObject jsonCodeValue ,Intent intent,String userInfo) {
        super(context);
        this.context = context;
        this.id = id;
        this.layout = layout;
        this.dlg = new Dialog(context);
        this.jsonStrValue = jsonStrValue;
        this.jsonCodeValue = jsonCodeValue;
        this.intent = intent;
        this.userInfo = userInfo;
        callFunction();
    }//생성자

    /**
     * 생성자
     * @param context
     * @param layout
     * @param id
     * @param jsonStrValue
     */
    public CustomDialog(Context context, int layout, int id, JSONObject jsonStrValue ) {
        super(context);
        this.context = context;
        this.id = id;
        this.layout = layout;
        this.dlg = new Dialog(context);
        this.jsonStrValue = jsonStrValue;
        ;

        callFunction();

    }//생성자

    /**
     * 생성자
     * @param context
     * @param layout
     * @param id
     * @param alertMsg
     */
    public CustomDialog(Context context, int layout, int id, String alertMsg ) {

        super(context);
        Log.i("생성자","생성자");
        this.context = context;
        this.id = id;
        this.layout = layout;
        this.dlg = new Dialog(context);
        this.alertMsg = alertMsg;

        callFunction();

    }//생성자

    public CustomDialog(Context context, int layout, int id, String alertMsg,Stack<EditText> que_et_list ,View li,LinearLayout load_li) {
      /*  View li;
        LinearLayout load_li;*/
        super(context);
        Log.i("생성자","생성자");
        this.context = context;
        this.id = id;
        this.layout = layout;
        this.dlg = new Dialog(context);
        this.alertMsg = alertMsg;
        this.que_et_list = que_et_list;
        this.li =li;
        this.load_li = load_li;
       // this.tv = tv;
        callFunction();

    }//생성자

    public CustomDialog(Context context, int layout, int id ){
        super(context);
        this.context = context;

        this.layout = layout;
        this.dlg = new Dialog(context);
        this.id = id;
      ;
        callFunction();
    }//생성자


    /**
     * 커스텀 다이얼로그.
     *
     */
    public void callFunction()   {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(layout);
        try {

            if (dlg.findViewById(id).getId() == R.id.clickOk) {      //메인화면에서 사용자버튼 클릭시
                sendApprovalRequest = dlg.findViewById(id);
                sendApprovalRequest.setOnClickListener(this);

                userPhneNo = dlg.findViewById(R.id.userPhneNo);
                userPhneNo.append(jsonStrValue.get("TEL_NO").toString());//전화번호

                userName = dlg.findViewById(R.id.userName);
                userName.append(jsonStrValue.get("EMNM").toString());//이름

                userBonbu = dlg.findViewById(R.id.userBonbu);
                userBonbu.append(jsonStrValue.get("HDQR_NM").toString());//소속 본부


                userJisa = dlg.findViewById(R.id.userJisa);
                userJisa.append(jsonStrValue.get("MTNOF_NM").toString());//소속 지사
                userCompany = dlg.findViewById(R.id.userCompany);
                userCompany.append(jsonStrValue.get("SMS_GRP_NM").toString());
            } else if (dlg.findViewById(id).getId() == R.id.sendApprovalRequest) {//작업계획 등록 -> 등록버튼 클릭시
    /*
                clickOk = dlg.findViewById(id);
                clickOk.setOnClickListener(this);*/
                updateContent_Li = (LinearLayout)dlg.findViewById(R.id.updateContent_Li);
                guganOverLapCheck_Li=(LinearLayout)dlg.findViewById(R.id.guganOverLapCheck_Li);
                sendApprovalRequest = dlg.findViewById(id);
                sendApprovalRequest.setOnClickListener(this);
                gongsa_result_bonbuDialog = (TextView) dlg.findViewById(R.id.gongsa_result_bonbuDialog);
                gongsa_result_bonbuDialog.setText("본부 : " + jsonStrValue.get("bonbu").toString());
                gongsa_result_jisaDialog = (TextView) dlg.findViewById(R.id.gongsa_result_jisaDialog);
                gongsa_result_jisaDialog.setText("지사 : " + jsonStrValue.get("jisa").toString());
                gongsa_result_nosunDialog = (TextView) dlg.findViewById(R.id.gongsa_result_nosunDialog);
                gongsa_result_nosunDialog.setText("노선 : " + jsonStrValue.get("nosun").toString());
                gongsa_result_direnctionDialog = (TextView) dlg.findViewById(R.id.gongsa_result_direnctionDialog);
                gongsa_result_direnctionDialog.setText("방향 : " + jsonStrValue.get("direction").toString());
                gongsa_result_roadDialog = (TextView) dlg.findViewById(R.id.gongsa_result_roadDialog);

                String charo = "차로제한 : " + jsonStrValue.get("roadlimit").toString();
                for(int i = 0 ; i < charo.length();i++){

                }
                gongsa_result_roadDialog.setText("차로제한 : " + jsonStrValue.get("roadlimit").toString());
                gongsa_result_gamdokDialog = (TextView) dlg.findViewById(R.id.gongsa_result_gamdokDialog);
                gongsa_result_gamdokDialog.setText("감독원 : " + jsonStrValue.get("gamdokTv").toString());
                gongsa_result_gongsaContent = (TextView) dlg.findViewById(R.id.gongsa_result_gongsaContent);
                gongsa_result_gongsaContent.setText("공사내용 : " + jsonStrValue.get("gongsaContent").toString());
                gongsa_result_startTime = (TextView) dlg.findViewById(R.id.gongsa_result_startTime);
                gongsa_result_startTime.setText("시작시간 : " + jsonStrValue.get("startTime").toString());
                gongsa_result_result_endTime = (TextView) dlg.findViewById(R.id.gongsa_result_result_endTime);
                gongsa_result_result_endTime.setText("종료시간 : " + jsonStrValue.get("endTime").toString());
                gongsa_result_startGugan = (TextView) dlg.findViewById(R.id.gongsa_result_startGugan);
                gongsa_result_startGugan.setText("시작구간 : " + jsonStrValue.get("startGugan").toString());
                gongsa_result_endGugan = (TextView) dlg.findViewById(R.id.gongsa_result_endGugan);
                gongsa_result_endGugan.setText("종료구간 : " + jsonStrValue.get("endGugan").toString());
                gongsa_result_Inwonjangbi = (TextView) dlg.findViewById(R.id.gongsa_result_Inwonjangbi);
                gongsa_result_Inwonjangbi.setText("인원 및 장비 : " + jsonStrValue.get("inwonjangbi").toString());
                gongsa_result_gosundaeNum = (TextView) dlg.findViewById(R.id.gongsa_result_gosundaeNum);
                gongsa_result_gosundaeNum.setText("고순대번호 : " + jsonStrValue.get("gosundaeNum").toString());
                gongsa_result_workType = (TextView) dlg.findViewById(R.id.gongsa_result_workType);
                gongsa_result_workType.setText("작업유형 : " + jsonStrValue.get("workType").toString());

                updateContent_Li.setOnClickListener(this);
                guganOverLapCheck_Li.setOnClickListener(this);

            } else if (dlg.findViewById(id).getId() == R.id.sendApprovalRequest_in_work_plan_update) {//작업계획 수정
    /*             sendApprovalRequest_in_work_plan_update
                clickOk = dlg.findViewById(id);
                clickOk.setOnClickListener(this);*/
                sendApprovalRequest_in_work_plan_update = (LinearLayout)dlg.findViewById(R.id.sendApprovalRequest_in_work_plan_update);
                updateContent_Li = (LinearLayout)dlg.findViewById(R.id.updateContent_Li);
                guganOverLapCheck_Li=(LinearLayout)dlg.findViewById(R.id.guganOverLapCheck_Li);


                sendApprovalRequest_in_work_plan_update.setOnClickListener(this);
                updateContent_Li.setOnClickListener(this);
                guganOverLapCheck_Li.setOnClickListener(this);
                gongsa_result_bonbuDialog = (TextView) dlg.findViewById(R.id.gongsa_result_bonbuDialog);
                gongsa_result_bonbuDialog.setText("본부 : " + jsonStrValue.get("bonbu").toString());
                gongsa_result_jisaDialog = (TextView) dlg.findViewById(R.id.gongsa_result_jisaDialog);
                gongsa_result_jisaDialog.setText("지사 : " + jsonStrValue.get("jisa").toString());
                gongsa_result_nosunDialog = (TextView) dlg.findViewById(R.id.gongsa_result_nosunDialog);
                gongsa_result_nosunDialog.setText("노선 : " + jsonStrValue.get("nosun").toString());
                gongsa_result_direnctionDialog = (TextView) dlg.findViewById(R.id.gongsa_result_direnctionDialog);
                gongsa_result_direnctionDialog.setText("방향 : " + jsonStrValue.get("direction").toString());
                gongsa_result_roadDialog = (TextView) dlg.findViewById(R.id.gongsa_result_roadDialog);

                String charo = "차로제한 : " + jsonStrValue.get("roadlimit").toString();

                gongsa_result_roadDialog.setText("차로제한 : " + jsonStrValue.get("roadlimit").toString());
                gongsa_result_gamdokDialog = (TextView) dlg.findViewById(R.id.gongsa_result_gamdokDialog);
                gongsa_result_gamdokDialog.setText("감독원 : " + jsonStrValue.get("gamdokTv").toString());
                gongsa_result_gongsaContent = (TextView) dlg.findViewById(R.id.gongsa_result_gongsaContent);
                gongsa_result_gongsaContent.setText("공사내용 : " + jsonStrValue.get("gongsaContent").toString());
                gongsa_result_startTime = (TextView) dlg.findViewById(R.id.gongsa_result_startTime);
                gongsa_result_startTime.setText("시작시간 : " + jsonStrValue.get("startTime").toString());
                gongsa_result_result_endTime = (TextView) dlg.findViewById(R.id.gongsa_result_result_endTime);
                gongsa_result_result_endTime.setText("종료시간 : " + jsonStrValue.get("endTime").toString());
                gongsa_result_startGugan = (TextView) dlg.findViewById(R.id.gongsa_result_startGugan);
                gongsa_result_startGugan.setText("시작구간 : " + jsonStrValue.get("startGugan").toString());
                gongsa_result_endGugan = (TextView) dlg.findViewById(R.id.gongsa_result_endGugan);
                gongsa_result_endGugan.setText("종료구간 : " + jsonStrValue.get("endGugan").toString());
                gongsa_result_Inwonjangbi = (TextView) dlg.findViewById(R.id.gongsa_result_Inwonjangbi);
                gongsa_result_Inwonjangbi.setText("인원 및 장비 : " + jsonStrValue.get("inwonjangbi").toString());
                gongsa_result_gosundaeNum = (TextView) dlg.findViewById(R.id.gongsa_result_gosundaeNum);
                gongsa_result_gosundaeNum.setText("고순대번호 : " + jsonStrValue.get("gosundaeNum").toString());
                gongsa_result_workType = (TextView) dlg.findViewById(R.id.gongsa_result_workType);
                gongsa_result_workType.setText("작업유형 : " + jsonStrValue.get("workType").toString());


            } else if (dlg.findViewById(id).getId() == R.id.alertOk) {//작업계획 등록의 양식이 완전치 않을 경우 띄워주는 Dialog
                alertOk = (ViewGroup) dlg.findViewById(R.id.alertOk);
                alertContent = (TextView) dlg.findViewById(R.id.alertContent);

                alertContent.setText(alertMsg);

                alertOk.setOnClickListener(this);
            } else if (dlg.findViewById(id).getId() == R.id.JaupclickOk) {
                JaupclickOk = (ViewGroup) dlg.findViewById(id);
                JaupclickOk.setOnClickListener(this);
                Log.i("alertMsg", alertMsg);
                chdanBangSikDetail = (TextView) dlg.findViewById(R.id.chdanBangSikDetail);
                String[] chadanBasikArray = alertMsg.split(",");
                for (int i = 0; i < chadanBasikArray.length; i++) {
                    chdanBangSikDetail.append(chadanBasikArray[i] + "\n");
                }
            } else if(dlg.findViewById(id).getId() == R.id.userCheckTextView){
                userCheckTextView =(LinearLayout) dlg.findViewById(R.id.userCheckTextView);
                userCheckTextView.setOnClickListener(this);
            } else  if(dlg.findViewById(id).getId()==R.id.end_work_Li){
                end_work_Li= (LinearLayout)dlg.findViewById(R.id.end_work_Li);
                end_work_Li.setOnClickListener(this);
            } else if(dlg.findViewById(id).getId()==R.id.userconfirmedCheck){

                userconfirmedCheck = (LinearLayout)dlg.findViewById(R.id.userconfirmedCheck);
                userconfirmedCheck.setOnClickListener(this);
                userInputCheck= (TextView)dlg.findViewById(R.id.userInputCheck);
                userInputCheck.setText(toUserMsg);

            }else if(dlg.findViewById(id).getId()==R.id.warn_msg){
                del_ok = (LinearLayout)dlg.findViewById(R.id.del_ok);
                del_no = (LinearLayout)dlg.findViewById(R.id.del_no);
                warn_msg = (TextView)dlg.findViewById(R.id.warn_msg);
                Log.i("확인 텍스트",alertMsg);
                warn_msg.setText(alertMsg);
                del_ok.setOnClickListener(this);
                del_no.setOnClickListener(this);
            }else  if(dlg.findViewById(id).getId()==R.id.sendApprovalRequest_in_todaywork_plan_update){
                sendApprovalRequest_in_todaywork_plan_update = (LinearLayout)dlg.findViewById(R.id.sendApprovalRequest_in_todaywork_plan_update);
                sendApprovalRequest_in_todaywork_plan_update.setOnClickListener(this);
             //   guganOverLapCheck_Li.setOnClickListener(this);
                updateContent_Li = (LinearLayout)dlg.findViewById(R.id.updateContent_Li);
                guganOverLapCheck_Li=(LinearLayout)dlg.findViewById(R.id.guganOverLapCheck_Li);

                gongsa_result_bonbuDialog = (TextView) dlg.findViewById(R.id.gongsa_result_bonbuDialog);
                gongsa_result_bonbuDialog.setText("본부 : " + jsonStrValue.get("bonbu").toString());
                gongsa_result_jisaDialog = (TextView) dlg.findViewById(R.id.gongsa_result_jisaDialog);
                gongsa_result_jisaDialog.setText("지사 : " + jsonStrValue.get("jisa").toString());
                gongsa_result_nosunDialog = (TextView) dlg.findViewById(R.id.gongsa_result_nosunDialog);
                gongsa_result_nosunDialog.setText("노선 : " + jsonStrValue.get("nosun").toString());
                gongsa_result_direnctionDialog = (TextView) dlg.findViewById(R.id.gongsa_result_direnctionDialog);
                gongsa_result_direnctionDialog.setText("방향 : " + jsonStrValue.get("direction").toString());
                gongsa_result_roadDialog = (TextView) dlg.findViewById(R.id.gongsa_result_roadDialog);

                String charo = "차로제한 : " + jsonStrValue.get("roadlimit").toString();

                gongsa_result_roadDialog.setText("차로제한 : " + jsonStrValue.get("roadlimit").toString());
                gongsa_result_gamdokDialog = (TextView) dlg.findViewById(R.id.gongsa_result_gamdokDialog);
                gongsa_result_gamdokDialog.setText("감독원 : " + jsonStrValue.get("gamdokTv").toString());
                gongsa_result_gongsaContent = (TextView) dlg.findViewById(R.id.gongsa_result_gongsaContent);
                gongsa_result_gongsaContent.setText("공사내용 : " + jsonStrValue.get("gongsaContent").toString());
                gongsa_result_startTime = (TextView) dlg.findViewById(R.id.gongsa_result_startTime);
                gongsa_result_startTime.setText("시작시간 : " + jsonStrValue.get("startTime").toString());
                gongsa_result_result_endTime = (TextView) dlg.findViewById(R.id.gongsa_result_result_endTime);
                gongsa_result_result_endTime.setText("종료시간 : " + jsonStrValue.get("endTime").toString());
                gongsa_result_startGugan = (TextView) dlg.findViewById(R.id.gongsa_result_startGugan);
                gongsa_result_startGugan.setText("시작구간 : " + jsonStrValue.get("startGugan").toString());
                gongsa_result_endGugan = (TextView) dlg.findViewById(R.id.gongsa_result_endGugan);
                gongsa_result_endGugan.setText("종료구간 : " + jsonStrValue.get("endGugan").toString());
                gongsa_result_Inwonjangbi = (TextView) dlg.findViewById(R.id.gongsa_result_Inwonjangbi);
                gongsa_result_Inwonjangbi.setText("인원 및 장비 : " + jsonStrValue.get("inwonjangbi").toString());
                gongsa_result_gosundaeNum = (TextView) dlg.findViewById(R.id.gongsa_result_gosundaeNum);
                gongsa_result_gosundaeNum.setText("고순대번호 : " + jsonStrValue.get("gosundaeNum").toString());
                gongsa_result_workType = (TextView) dlg.findViewById(R.id.gongsa_result_workType);
                gongsa_result_workType.setText("작업유형 : " + jsonStrValue.get("workType").toString());

            }else if(dlg.findViewById(id).getId()==R.id.correctBtn){
                //2021.06 안내문구표출
                correctBtn =(LinearLayout) dlg.findViewById(R.id.correctBtn);
                correctBtn.setOnClickListener(this);
            }
        }catch (Exception e){
           Log.e("에러","에러발생");
        }
            dlg.show();
    }

    @Override
    public void onClick(View v) {//sendApprovalRequest_in_todaywork_plan_update
        switch (v.getId()) {
            case R.id.sendApprovalRequest_in_work_plan_update:
                if (dlg != null) {
                    dlg.dismiss();
                }
                Log.i("-","-----------------------------");
                Log.i("workplan_update : ",jsonCodeValue.toString());
                Log.i("-","-----------------------------");
                baseActivity.new Action("post", SERVER_URL + "/WorkPlan/update.do", jsonCodeValue.toString(), context, intent,userInfo).execute();

                break;
            case R.id.sendApprovalRequest_in_todaywork_plan_update:
                if (dlg != null) {
                    dlg.dismiss();
                }
                Log.i("-","-----------------------------");
                Log.i("Todayplan_Update : ",jsonCodeValue.toString());
                Log.i("-","-----------------------------");
                baseActivity.new Action("post", SERVER_URL + "/TodayWorkPlan/update.do", jsonCodeValue.toString(), context, intent,userInfo).execute();

                break;
            case R.id.sendApprovalRequest:

                if (dlg != null) {

                    dlg.dismiss();
                }

                Log.i("WokrPlanIN : ",SERVER_URL + "/WorkPlan/register.do");
                Log.i("-","-----------------------------");
                Log.i("WorkPlanIN",jsonCodeValue.toString());
                Log.i("-","-----------------------------");

                baseActivity.new Action("post", SERVER_URL + "/WorkPlan/register.do", jsonCodeValue.toString(), context, intent).execute();

                break;
            case R.id.guganOverLapCheck_Li:
                try {
                 /*   intent = new Intent(context, GuganOverLapListActivity.class);
                    intent.putExtra("workPlanParam", jsonCodeValue.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (dlg != null) {
                        dlg.dismiss();
                    }*/
                    if (dlg != null) {
                        dlg.dismiss();
                    }
                    Log.i("다이얼로그에서 context이름 학인",context.getClass().toString());
                    baseActivity.new Action("get", SERVER_URL + "/WorkPlan/guganOverLapCheck.do", jsonCodeValue.toString(), context, intent).execute("");
                  //  intent.putExtra("list", baseActivity.new Action("get", SERVER_URL + "/WorkPlan/guganOverLapCheck.do", jsonCodeValue.toString(), context, intent).execute("").get());

                    //dlg.dismiss();

                  //  dlg.getContext().startActivity(intent);

             /*   if(dlg!=null){
                    dlg.dismiss();
                }
                baseActivity.new Action("post",SERVER_URL+"/WorkPlan/guganOverLapCheck.do",jsonCodeValue.toString(),context,intent).execute();*/
                }catch (NullPointerException e){
                   Log.e("에러","에러발생");
                }
                break;
            case R.id.clickOk:

                dlg.dismiss();
                break;
            case R.id.alertOk:
                dlg.dismiss();
                break;
            case R.id.JaupclickOk:
                dlg.dismiss();
                break;
            case R.id.userCheckTextView:
                dlg.dismiss();
                break;

            case R.id.end_work_Li:
                //202008_수정중 완료처리시 작업보고 코드와 내용보내는곳.


                    try {

                        btnClick = 33;
                        jsonStrValue.put("acdtActnInputCd", btnClick);
                        jsonStrValue.put("acdtActnCtnt", "작업종료");
                        Log.i("-","-----------------------------");
                        Log.i("jsonStrValue", jsonStrValue.toString());
                        Log.i("-","-----------------------------");
                        progressDialog = ProgressDialog.show(getContext(), "", "로딩중...", true);
                        baseActivity.new Action("get", SERVER_URL + "/TodayWorkPlan/endWork.do", jsonStrValue.toString(), context).execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                break;

                case R.id.userconfirmedCheck:
                        if(toUserMsg.equals("사진을 전송하시겠습니까?")){
                    if(dlg!=null){
                        dlg.dismiss();
                    }

                    //2021.03 TEST
                    baseActivity.new Action("post", file, SERVER_URL + "/gongsaManageMentPicture.do", jsonCodeValue.toString(),context,intent).execute("", "", "");


                    //로컬 사진 전송
                 //   baseActivity.new Action("post", file, "http://121.190.60.214:8081" + "/gongsaManageMentPicture.do", jsonCodeValue.toString(),context,intent).execute("", "", "");


                    break;
                }else if(toUserMsg.equals("금일작업계획을 등록하시겠습니까?")){
                    if(dlg!=null){
                        dlg.dismiss();
                    }



                    Log.i("-","-----------------------------");
                    Log.i("TWPinsert : ",jsonCodeValue.toString());
                    Log.i("-","-----------------------------");
                    baseActivity.new Action("post", SERVER_URL+"/TodayWorkPlan/insertTodayWorkPlan.do", jsonCodeValue.toString(),context).execute("");
                    intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    break;
                }
            case R.id.updateContent_Li:

                dlg.dismiss();
                break;


            case R.id.click_user_identify:
                try{
                    this.data =baseActivity.new Action("get",SERVER_URL+"/insertUser.do",jsonCodeValue.toString(),context).execute("").get();
                    Log.i("data123",":      "+ URLDecoder.decode(this.data,"UTF-8"));
                  //  Intent intent=new Intent(context,MainActivity.class);
                 //   intent.putExtra("addUser",URLDecoder.decode(this.data,"UTF-8"));
                }catch (Exception e){
                   Log.e("에러","에러발생");
                }
                dlg.dismiss();
                break;
            case R.id.click_user_cancel:
                dlg.dismiss();
                break;
            case R.id.del_ok:
            /*    li.setVisibility(View.GONE);
                load_li.setVisibility(View.GONE);*/

               /* que_et_list.peek().clearComposingText();
                que_et_list.peek().setText("");
                que_et_list.pop();*/
                  //  if(index != 1){
                if(li_pa.getChildCount()!=1) {
                    que_et_list.remove(index);
                    //    Toast.makeText(context,"지우시는 번호는 "+((EditText)v.getChildAt(0)).getText().toString()+" 입니다.",Toast.LENGTH_SHORT).show();
                    li_pa.removeViewAt(index);
                } else {
                 //   Toast.makeText(context,"고순대번호는 최소 1개 이상이 입력 되어야 합니다.",Toast.LENGTH_SHORT).show();
                    //((EditText)((LinearLayout)li_pa.getChildAt(1)).getChildAt(0)).setText("");
                    que_et_list.get(0).setText("");
                }


                dlg.dismiss();
                break;
            case R.id.del_no:
                dlg.dismiss();
                break;

            //2021.06 안내문구표출
            case R.id.correctBtn:

                new CustomDialog(context,R.layout.z_custom_dialog, R.id.sendApprovalRequest, jsonStrValue, jsonCodeValue , intent);
                dlg.dismiss();

                break;

          /*  del_ok.setOnClickListener(this);
            del_no.setOnClickListener(this);*/
        }
    }//method onClick

    public String userInsertReturnData(){
    //    Log.i("data",this.data);
        return  this.data;
    }
}
