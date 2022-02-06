package com.ex.situationmanager;



import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.ex.situationmanager.BaseActivity.contextActivity;

public class DialogUserInfoActivity extends Activity implements OnClickListener,RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener{

	private final String TAG = "DialogUserInfoActivity";
	TextView txtUser1, txtUser2, txtUser3, txtUser4;
	RadioGroup radio_group;
	RadioButton radio_yes,radio_no;
	ImageView btnOk;
	String userType;
	//Spinner spinner_user_JisaList;
	String curContext;
	SharedPreferences sh;
	String userInfo;

	JSONArray jsonArray;
	List<String> list_adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.println(Log.ASSERT, TAG, "DiaglogeUserInfoActivity-- onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		//주변 검정으로 변하지않게
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//다이얼로그 테두리 제거
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		if(Common.nullCheck(Configuration.User.getUser_type()).equals("0001")||Common.nullCheck(Configuration.User.getUser_type()).equals("0004")){
			//배포버전
			setContentView(R.layout.zz_userinfo);

			//겸직전용 (테스트)
			//setContentView(R.layout.zz_userinfo_setting_all_jisa_list);

		    ;
			//겸직전용 테스트(테스트)
			//spinner_user_JisaList = (Spinner)findViewById(R.id.spinner_user_JisaList);

			//spinner_user_JisaList.setOnItemSelectedListener(this);
			radio_group = (RadioGroup)findViewById(R.id.radio_group);
			radio_group.setOnCheckedChangeListener(this);
			radio_yes = (RadioButton)findViewById(R.id.radio_yes);
			radio_no =(RadioButton)findViewById(R.id.radio_no);


			sh = getSharedPreferences("userAllJisaList",MODE_PRIVATE);
			userInfo=sh.getString("userAllInfo","");
			Log.i("userdsdfsdf",userInfo);
			Log.i("Reasfasdsult","옴");




		/*	list_adapter=	new ArrayList<String>();
			try{
				jsonArray = new JSONArray(userInfo);
				for(int i = 0 ; i<jsonArray.length();i++){
					;
					String userInfoString =jsonArray.getJSONObject(i).get("bsname")+" "+jsonArray.getJSONObject(i).get("car_nm");
					list_adapter.add(userInfoString);
				}
				//spinner_user_JisaList.setAdapter(new ArrayAdapter<String>(this, R.layout.z_spinner_item, list_adapter));
			}catch (Exception e){
				e.printStackTrace();
			}*/


			//userInfo = sh.getString("UserPhoneNo", "");
			Log.i("sddfsdf",userInfo);
	        if(IntroActivity.isSoundAlert == true){
				radio_group.check(R.id.radio_yes);

			}else {
				radio_group.check(R.id.radio_no);
			}
		} else {
			setContentView(R.layout.userinfo);
		}

		 userType = Common.nullCheck(Configuration.User.getUser_type());


		txtUser1 = (TextView) findViewById(R.id.txtUser1);
		txtUser2 = (TextView) findViewById(R.id.txtUser2);
		txtUser3 = (TextView) findViewById(R.id.txtUser3);
		txtUser4 = (TextView) findViewById(R.id.txtUser4);


		
		if("0001".equals(userType)){//순찰
			txtUser1.setText("이름 : " + Configuration.User.getCar_nm());
			txtUser2.setText("지사명 : " + Configuration.User.getBsname_list().get(0));
			txtUser3.setText("전화번호 : " + Configuration.User.getHp_no());
		//	txtUser4.setText("");
			Log.println(Log.ASSERT, TAG, "DiaglogeUserInfoActivity에서 확인한 내부직원 값 셋팅 지점--순찰");
		}else if("0002".equals(userType)){//견인
			txtUser1.setText("업체명 : " + Configuration.User.getReg_post() );
			txtUser2.setText("이름 : "+ Configuration.User.getReg_name());
			txtUser3.setText("전화번호 : " +Configuration.User.getHp_no());

			int position = 0;
			for (int i = 0; i < Configuration.User.getRcv_yn_list().size(); i++) {
				if(Configuration.User.getRcv_yn_list().get(i).equals("Y")){
					position = i;
				}
			}
			txtUser4.setText("주접보지사 " + Configuration.User.getBsname_list().get(position));
		}else if("0004".equals(userType)){//내부직원
			Log.println(Log.ASSERT, TAG, "DiaglogeUserInfoActivity에서 확인한 내부직원 값 셋팅 지점");
			txtUser1.setText("이름 : " + Configuration.User.getCar_nm());
			txtUser2.setText("소속 : " + Configuration.User.getBsname_list().get(0));
			txtUser3.setText("전화번호 : " + Configuration.User.getHp_no());
		//	txtUser4.setText("");
		}else{//대국민
			txtUser1.setText("전화번호 : " +Configuration.User.getHp_no());
		}
		
		btnOk = (ImageView)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:
			finish();

			//  listFlag = false;
			break;
		default:
			break;
		}
	}



	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {

		if(i == R.id.radio_yes){
			radioGroup.check( R.id.radio_yes);
			IntroActivity.isSoundAlert = true;
		}else{
			radioGroup.check( R.id.radio_no);
			IntroActivity.isSoundAlert = false;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

		try{
			SituationService.conf.User.setUser_type(Common.nullCheck(jsonArray.getJSONObject(i).getString("user_type")));
			SituationService.conf.User.getBscode_list().clear();;
			SituationService.conf.User.getBscode_list().add(Common.nullCheck(jsonArray.getJSONObject(i).getString("bscode")));
			SituationService.conf.User.getBsname_list().clear();
			SituationService.conf.User.getBsname_list().add(Common.nullCheck(jsonArray.getJSONObject(i).getString("bsname")));
			SituationService.conf.User.setPatcar_id(Common.nullCheck(jsonArray.getJSONObject(i).getString("patcar_id")));
			SituationService.conf.User.setCar_nm(Common.nullCheck(jsonArray.getJSONObject(i).getString("car_nm")));
			txtUser1.setText("이름 : " + Configuration.User.getCar_nm());
			txtUser2.setText("지사명 : " + Configuration.User.getBsname_list().get(0));
			txtUser3.setText("전화번호 : " + Configuration.User.getHp_no());
		}catch (JSONException e){
			Log.e("예외","예외발생");
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}
}
