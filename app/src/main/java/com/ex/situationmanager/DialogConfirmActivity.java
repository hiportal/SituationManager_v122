package com.ex.situationmanager;



import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogConfirmActivity extends Activity implements OnClickListener{

	private final String TAG = "DialogConfirmActivity";
	TextView txtContent;
	ImageView btn_yes, btn_no;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		//주변 검정으로 변하지않게
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//다이얼로그 테두리 제거
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		setContentView(R.layout.confirm);
		String userType = Common.nullCheck(Configuration.User.getUser_type());
		
		Intent i = getIntent();
		
		txtContent = (TextView) findViewById(R.id.txtContent);
		btn_yes = (ImageView) findViewById(R.id.btn_yes);
		btn_no = (ImageView) findViewById(R.id.btn_no);
		
		txtContent.setText(i.getStringExtra("info"));
		
		btn_yes.setOnClickListener(this);
		btn_no.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {

		case R.id.btn_yes:
			setResult(RESULT_OK, i);
			finish();
			break;
		case R.id.btn_no:
			setResult(RESULT_CANCELED, i);
			finish();
			break;
		default:
			break;
		}
	}
}
