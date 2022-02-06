package com.ex.situationmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ex.situationmanager.dto.Patrol;
import com.ex.situationmanager.dto.TowRow;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

/** 주 접보지사 선택 Activity
 *
 */
public class DialogIntroActivity extends BaseActivity{

	private final String TAG = "DialogIntroActivity";
	ArrayList<TowRow> itemList;
	public static String URL_SENDGPS = "http://121.159.10.31:8080/mobileServer/proxy.jsp";
	
	Spinner jisaSpin;
	ImageView btnSave;
	int mPosition = 0;
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
		
		setContentView(R.layout.popupscreen_intro);
		db.init();
		
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseActivity.TowJeopBoJisaCode = Configuration.User.getBscode_list().get(mPosition);
				BaseActivity.TowJeppBoJisaName = Configuration.User.getBsname_list().get(mPosition);
				db.updateBBBsCode(BaseActivity.TowJeopBoJisaCode, "Y");
				Common.setPrefString(DialogIntroActivity.this, "patcar_id", Common.nullCheck(Configuration.User.getCrdns_id_list().get(mPosition)));
				Parameters params = new Parameters(ONECLICK_MAINRCVBS_UPDATE);
				params.put("crdns_id", Configuration.User.getCrdns_id_list().get(0));
				params.put("bscode", BaseActivity.TowJeopBoJisaCode);
				new Action(ONECLICK_MAINRCVBS_UPDATE, params).execute("");
				finish();
//				Intent i = new Intent(DialogIntroActivity.this, TowMainActivity.class);
//				startActivity(i);
			}
		});
		
		jisaSpin = (Spinner) findViewById(R.id.jisaSpin);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Configuration.User.getBsname_list()) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1)).setText(getItem(getCount()));
				}
				return v;
			}
			
			@Override
			public int getCount() {
//				return super.getCount() - 1;
				return super.getCount();
			}

		};
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jisaSpin = (Spinner)findViewById(R.id.jisaSpin);
		jisaSpin.setAdapter(adapter);
		jisaSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mPosition = position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
			
		});
		Log.d(TAG,TAG+" list size = "+Configuration.User.getBscode_list().size());
	}
	
	
	@Override
	protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
		if(e == null){
			try {
				String rtnResultCode = result.get("result");
				if("1000".equals(rtnResultCode)){
					if(primitive.equals(ONECLICK_MAINRCVBS_UPDATE)){
						Log.d(TAG,TAG+" connection success!!");
					}
				}
			} catch (XmlPullParserException e2) {
				Log.e("에러","예외");
			}
		}
	}


	@Override
	protected void setActivityViewEdit(Activity Activity) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	
	
}
