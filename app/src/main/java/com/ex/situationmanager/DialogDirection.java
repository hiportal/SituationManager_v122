package com.ex.situationmanager;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ex.situationmanager.dto.DirectionVo;
import com.ex.situationmanager.service.Parameters;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

public class DialogDirection extends BaseActivity implements OnClickListener{

	private final String TAG = "DialogDirection";
	ImageView btn_confirm, btn_cancel;
	List<DirectionVo> items = new ArrayList<DirectionVo>();
	TextView txt_directionS, txt_directionE;//시점, 종점 텍스트뷰
	CheckBox chk_directionS, chk_directionE;//시점, 종점 체크박스
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		//주변 검정으로 변하지않게
//		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//다이얼로그 테두리 제거
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		setContentView(R.layout.dialog_direction);
		
		txt_directionS = (TextView) findViewById(R.id.txt_directionS);
		txt_directionE = (TextView) findViewById(R.id.txt_directionE);

		btn_confirm = (ImageView)findViewById(R.id.btn_confirm);
		btn_cancel = (ImageView)findViewById(R.id.btn_cancel);
		
		btn_confirm.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
		setCheckBox();
		getDirectionInfo(SituationService.selectedRpt_id);
		
	}
	
	
	/** 시점 종점 CheckBox 컨트롤
	 * 
	 */
	public void setCheckBox(){
		chk_directionS = (CheckBox) findViewById(R.id.chk_directionS);
		chk_directionE = (CheckBox) findViewById(R.id.chk_directionE);
		
		chk_directionS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(true == chk_directionS.isChecked()){
					chk_directionE.setChecked(false);
				}
			}
		});
		
		chk_directionE.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(true == chk_directionE.isChecked()){
					chk_directionS.setChecked(false);
				}
				
			}
		});
		
		//default Status Setting
		chk_directionS.performClick();
	}
	
	
	/** 방향정보 가져오기
	 * @param rpt_id 사고보고서 번호
	 */
	public void getDirectionInfo(String rpt_id){
		Parameters params = new Parameters(ONECLICK_MOVE_DIRECTION_SELECT);
		params.put("rpt_id", SituationService.selectedRpt_id);
		params.put("hp_no", Configuration.User.getHp_no());
		
		new Action(ONECLICK_MOVE_DIRECTION_SELECT, params).execute();
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			sendDirectionInfo();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	/** 출동방향 선택 결과 전송
	 * 
	 */
	public void sendDirectionInfo(){
//		Parameters params = new Parameters(ONECLICK_MOVE_DIRECTION_INSERT);
//		params.put("rpt_id", SituationService.selectedRpt_id);
//		params.put("hp_no", Configuration.User.getHp_no());
		if(chk_directionS.isChecked()){
//			params.put("direction_code", "S");
			SituationService.move_direct = "S";
		}else{
//			params.put("direction_code", "E");
			SituationService.move_direct = "E";
		}
//		new Action(ONECLICK_MOVE_DIRECTION_INSERT, params).execute();
		
		finish();
		
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	
	@Override
	protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
		try {
			if (e == null) {
				if (null != result) {

					String rtnResultCode = result.get("result");
					if ("1000".equals(rtnResultCode)) {
						if (primitive.equals(ONECLICK_MOVE_DIRECTION_SELECT)) {
							result.setList("entity");
							
							items.clear();
							for (int i = 0; i < result.size(); i++) {
//								Log.d("","onactionpost direction check = " + result.get(i,"direction_name"));
								DirectionVo item = new DirectionVo();
								String direct_code = Common.nullCheck(result.get(i,"direction_code"));
								String direct_name = Common.nullCheck(result.get(i,"direction_name"));
								
								item.setDirect_code(direct_code);
								item.setDirect_name(direct_name);
								
								items.add(item);
							}
//							Log.d("","onactionpost direction check = " + items.size());
							
						}if(primitive.equals(ONECLICK_MOVE_DIRECTION_INSERT)){
							if(rtnResultCode.equals("1000")){
								Toast.makeText(DialogDirection.this, "출동방향을 전송 하였습니다.", Toast.LENGTH_SHORT).show();
							}
						}
					}else{
						Toast.makeText(DialogDirection.this, "서버 통신 실패", Toast.LENGTH_SHORT).show();
					}
				}
			}
			displayItems();
		}catch(XmlPullParserException e1){
			Log.e("에러","예외");
		}catch (NullPointerException ee){
			Log.e("에러","예외");
		}
	}
	
	/**
	 * 방향정보 셋팅하기
	 */
	public void displayItems(){
		if(null != items){
			if(items.size() > 1){
				
				for (int i = 0; i < items.size(); i++) {
//					Log.d("","direction check = "+items.get(i).getDirect_name());
					if(i == 0){
						txt_directionS.setText(items.get(i).getDirect_name()+"방향");
					}else if(i == 1){
						txt_directionE.setText(items.get(i).getDirect_name()+"방향");
					}
				}
			}
		}
	}
	
	@Override
	protected void setActivityViewEdit(Activity activity) throws IOException {

	}

}
