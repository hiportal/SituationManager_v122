package com.ex.situationmanager;



import java.io.IOException;

import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogSituEnd extends BaseActivity implements OnClickListener{

	private final String TAG = "DialogSituEnd";
	TextView situend;
	ImageView btnOk;
	
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
		
		setContentView(R.layout.situend);
		
		Intent i = getIntent();
		String message = i.getStringExtra("message");

		situend = (TextView) findViewById(R.id.situend);
		situend.setText(message);
		
		btnOk = (ImageView)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			//수정중_btnok를 타면 무조건 메인으로 돌아가서 접보가 리프레쉬되면서 버튼이 바뀌는거같은데...
			// 그러면 조건을 더 걸어줘야할듯.. 이미 완료된 결과값을 받아서 리프레시되지않도록 설정할것

			//202007_원래어플은 ... 이거 잠궈놨는딩 ..
			case R.id.btnOk:
				if("Y".equals(Common.getPrefString(PatrolMainActivity.contextActivity,"getSetSelectJubboStart"))){
					break;
			}else {
					finish();
					break;
				}
			default:
				break; }
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		return super.onKeyDown(keyCode, event);
	}


	
	@Override
	protected void setActivityViewEdit(Activity activity) throws IOException {
		
	}

	@Override
	protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
		
	}
	
	
	
}
