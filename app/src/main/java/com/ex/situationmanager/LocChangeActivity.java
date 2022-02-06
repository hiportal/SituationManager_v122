package com.ex.situationmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.ex.situationmanager.BaseActivity.Action;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.DBAdapter;

public class LocChangeActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener{

	private final String TAG= "LocChangeActivity";
	
	DBAdapter db = null;
	Spinner noseon_spinner, banghyang_spinner;
	EditText etIjung;
	Intent intent;
	
	ImageView btnOk, btnCancel, btnBack, btnCurPosition;
	
	static boolean saveLocValueFlag = true;
	String loc_nscode = "";
	String loc_nsname = "";
	String loc_bhcode = "";
	String loc_banghyang = "";
	String loc_ijung = "";
	
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locchange);
		
		intent = getIntent();
		loc_nscode = Common.nullCheck(intent.getStringExtra("ns_code"));
		loc_nsname = Common.nullCheck(intent.getStringExtra("ns_name"));
		loc_bhcode = Common.nullCheck(intent.getStringExtra("bhCode"));
		loc_banghyang = Common.nullCheck(intent.getStringExtra("banghyang"));
		loc_ijung = Common.nullCheck(intent.getStringExtra("currentIjung"));
		
		noseon_spinner = (Spinner) findViewById(R.id.noseon_spinner);
		banghyang_spinner = (Spinner) findViewById(R.id.banghyang_spinner);
		noseon_spinner.setOnItemSelectedListener(this);
		banghyang_spinner.setOnItemSelectedListener(this);
		
		
		btnOk = (ImageView) findViewById(R.id.btnOk);
		btnCancel = (ImageView) findViewById(R.id.btnCancel);
		btnBack = (ImageView) findViewById(R.id.btnBack);
		btnCurPosition = (ImageView) findViewById(R.id.btnCurPosition);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnCurPosition.setOnClickListener(this);
		
		
		etIjung = (EditText) findViewById(R.id.etIjung);
		if("-999".equals(loc_ijung)){
			etIjung.setText("");
		}else{
			etIjung.setText(loc_ijung);
		}
		etIjung.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		db = new DBAdapter();
		db.init();
		noseonSpinnerAdapter();
		
		if(loc_nscode == null || loc_nscode.equals("")){
			loc_nscode = "0010";
			loc_nsname = "경부선";
			loc_banghyang = "양방향";
		} 
		
	}
	

	/**
	 * 노선 스피너 아답터 구현
	 * 
	 * @param	none
	 * @return	none
	 * @throws	none
	 * @author	
	 * @since	2011-08-29
	 */
	public void noseonSpinnerAdapter(){
		List<String> list = db.selectNoseonName();		// 노선
		
		// 노선 spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		noseon_spinner.setAdapter(adapter);
		// 초기값 설정
		if(loc_nsname != null){
			noseon_spinner.setSelection(adapter.getPosition(loc_nsname));
		}
		
		
	}
	
	
	/**
	 * 방향 스피너 아답터 구현
	 * 
	 * @param	noseonName
	 * @return	none
	 * @throws	none
	 * @author	
	 * @since	2011-08-29
	 */
	@SuppressLint("NewApi")
	public void banghyangSpinnerAdapter(String noseonName){
		//List<String> list = common.selectBanghyangName(noseonName, null);		// 노선
		List<String> list = new ArrayList<String>();
		list.addAll(db.selectBanghyangName(noseonName, null));
		
		// 방향 spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		banghyang_spinner.setAdapter(adapter);
		
		// 초기값 설정
		if(intent.getStringExtra("banghyang") != null){
			banghyang_spinner.setSelection(adapter.getPosition(intent.getStringExtra("banghyang")));
		}
		
		if(list.size() <= 0){
			banghyang_spinner.setEnabled(false);
			etIjung.setEnabled(false);
			
		}else{
			banghyang_spinner.setEnabled(true);
			etIjung.setEnabled(true);
		}
		
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
		if (parent==noseon_spinner) {
			// do something with noseonSpinner
			
			((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)parent.getChildAt(0)).setTextSize(15);
			loc_nsname = noseon_spinner.getSelectedItem().toString();
			loc_nscode = db.getNoseonCode(loc_nsname);
			Log.d("","ddddddddddddddddddddd = " + loc_nscode);
			
			banghyangSpinnerAdapter(((TextView)parent.getChildAt(0)).getText().toString());
			
			
		} else if (parent==banghyang_spinner) {
			// do something with banghyangSpinner
			
			((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)parent.getChildAt(0)).setTextSize(15);
			loc_banghyang = banghyang_spinner.getSelectedItem().toString();
			loc_bhcode = db.getBanghyangCode(loc_nscode, loc_banghyang);
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCurPosition:
			saveLocValueFlag = true;
			progressDialog = ProgressDialog.show(LocChangeActivity.this, "", "위치 수집중...", true);
			
			List<String> list = db.selectNoseonName();
			
			for (int i = 0; i < list.size() ; i++) {
				if(saveLocValueFlag == true){
//					ns_name = "경부선";
//					bhCode = "O";
//					currentIjung = "123.460";
					Log.d("","ddddddddddddddd "+ns_name);
					loc_nsname = SituationService.ns_name;
					loc_bhcode = SituationService.bhCode;
					loc_ijung = SituationService.currentIjung;
					if(list.get(i).equals(loc_nsname)){
						noseon_spinner.setSelection(i);
						if(loc_bhcode.equals("S")){
							banghyang_spinner.setSelection(0);
						}else if(loc_bhcode.equals("E")){
							banghyang_spinner.setSelection(2);
						}else{
							banghyang_spinner.setSelection(1);
						}
						etIjung.setText(loc_ijung);
						break;
					}
				}
				
			}
			
			saveLocValueFlag = false;
			
			if(loc_nsname.equals("") || loc_nsname.equals("노선외")){
				noseon_spinner.setSelection(0);
				etIjung.setText(loc_ijung);
				Toast.makeText(LocChangeActivity.this, "고속도로 상에 위치하지 않습니다.", Toast.LENGTH_SHORT).show();
			}
			
//			stopGPS();
//			stopTimer(LocChangeActivity.this);
			
			if(null != progressDialog){
				progressDialog.dismiss();
			}	
			
//			stopGPS();
//			startMyGps();
//			stopTimer(this);
//			startTimer();
			break;
		case R.id.btnBack:
			Intent iBack = new Intent(LocChangeActivity.this, MainActivity.class);
			setResult(RESULT_CANCELED, iBack);
			finish();
			break;
		case R.id.btnOk:
//			stopGPS();
//			stopTimer(this);
			LaunchMainMenu1();
			break;
		case R.id.btnCancel:
			Intent iCancel = new Intent(LocChangeActivity.this, MainActivity.class);
			
			setResult(RESULT_CANCELED, iCancel);
			finish();
			break;

		default:
			break;
		}
		
	}
	
	protected void LaunchMainMenu1() {
		
		Intent i = new Intent(LocChangeActivity.this, MainActivity.class);

		String ijeong = etIjung.getText().toString();
		// 이정 validation check
		if (ijeong.trim().equals("")) {
			Common.openWarnDialog(LocChangeActivity.this, "이정을 입력하여 주십시오.");
			
		} else if (ijeong.trim().startsWith(".")) {
			Common.openWarnDialog(LocChangeActivity.this, "이정값이 형식에 맞지 않습니다.\n예)150.23");
			
		} else if (!checkIjungMax(loc_nscode, ijeong)) {
			if(db.maxIjung(loc_nscode) == null){
//				common.openWarnDialog(EditLocationActivity.this, "노선에 해당되지 않는 이정입니다.\n"+spNoSeon.getSelectedItem().toString()+" 이정 데이타가 없습니다.");
				
				if (loc_bhcode == null || loc_bhcode.equals("")) {
					loc_bhcode = "E";
				}

				// 이정에 소수점이 없으면 .0 추가
				if (ijeong.indexOf(".") <= -1) {
					ijeong = ijeong + ".0";
				} else {
					if (ijeong.indexOf(".") == ijeong.length() - 1) {
						ijeong = ijeong + "0";
					}
				}

				Log.d("",TAG+" finish = 1" );
				i.putExtra("ns_name", loc_nsname);
				i.putExtra("ns_code", loc_nscode);
				i.putExtra("ijeong", ijeong);
				i.putExtra("banghyang", loc_banghyang);
				i.putExtra("location", loc_nsname + "(" + loc_banghyang + ") "+ ijeong + "K");
				i.putExtra("bhCode", loc_bhcode);

				setResult(RESULT_OK, i);
				finish();
				
			}else{
				Common.openWarnDialog(LocChangeActivity.this, "입력된 이정값은 해당 노선의 \n최대값을 벗어납니다.\n"+noseon_spinner.getSelectedItem().toString()+" 이정 최대값 : "+db.maxIjung(loc_nscode) + " Km");
			}
			
		} else {

			if (loc_bhcode == null || loc_bhcode.equals("")) {
				loc_bhcode = "E";
			}

			// 이정에 소수점이 없으면 .0 추가
			if (ijeong.indexOf(".") <= -1) {
				ijeong = ijeong + ".0";
			} else {
				if (ijeong.indexOf(".") == ijeong.length() - 1) {
					ijeong = ijeong + "0";
				}
			}
			
			Log.d("",TAG+" finish = 2" );
			i.putExtra("ns_name", loc_nsname);
			i.putExtra("ns_code", loc_nscode);
			i.putExtra("currentIjung", ijeong);
			i.putExtra("banghyang", loc_banghyang);
			i.putExtra("location", loc_nsname + "(" + loc_banghyang + ") "+ ijeong + "K");
			i.putExtra("bhCode", loc_bhcode);

			setResult(RESULT_OK, i);
			finish();
		}
	}
	
	
	
	/** 이정 최대값 체크 함수.
	 * @param nsCode
	 * @param iJung
	 * @return 
	 */
	public boolean checkIjungMax(String nsCode ,String iJung){
		DBAdapter db = new DBAdapter();
		
		String stMaxIjung = db.maxIjung(nsCode);
		if(stMaxIjung != null){
			double dMaxIjung = Double.parseDouble(stMaxIjung);
			
			Log.d(TAG,TAG +" checkIjungMax = " +dMaxIjung+" : "+Double.parseDouble(iJung));
			
			//이정 정상 범위
			if(dMaxIjung >= Double.parseDouble(iJung)){
//				Log.d("","checkIjungMax = true");
				Cursor cursor = db.fetchTMXY2(nsCode, iJung);
				//노선 이정 중간에 비어있는 이정값 체크
				if(cursor.getCount() <= 0){
					return false;
				}
				return true;
				
			//이정 비정상 범위
			}else{
//				Log.d("","checkIjungMax = false");
				return false;	
			}
		}else{
			return false;
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		contextActivity = LocChangeActivity.this;
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	protected void setActivityViewEdit(Activity activity) throws IOException {
		Log.d("",TAG+ " setActivityViewEdit 1");
		if(null != activity){
			Log.d("",TAG+ " setActivityViewEdit 1 null X");
			if(!Common.nullCheck(loc_nsname).equals("")){
				List<String> list = db.selectNoseonName();
				
				for (int i = 0; i < list.size() ; i++) {
					if(saveLocValueFlag == true){
						ns_name = "경부선";
						bhCode = "O";
						currentIjung = "123.460";
						if(list.get(i).equals(loc_nsname)){
							noseon_spinner.setSelection(i);
							if(loc_bhcode.equals("S")){
								banghyang_spinner.setSelection(0);
							}else if(loc_bhcode.equals("E")){
								banghyang_spinner.setSelection(2);
							}else{
								banghyang_spinner.setSelection(1);
							}
							etIjung.setText(loc_ijung);
							break;
						}
					}
					
				}
				
				saveLocValueFlag = false;
				
				if(loc_nsname.equals("") || loc_nsname.equals("노선외")){
					noseon_spinner.setSelection(0);
					etIjung.setText(loc_ijung);
				}
			}else{
				noseon_spinner.setSelection(0);
			}
			
//			stopGPS();
//			stopTimer(LocChangeActivity.this);
			
			if(null != progressDialog){
				progressDialog.dismiss();
			}	
			
		}else{
			Log.d("",TAG+ " setActivityViewEdit 1 null ");
		}
		
	}


	@Override
	protected void onActionPost(String primitive, XMLData result, Exception e)
			throws IOException {
		// TODO Auto-generated method stub
		
	}
}
