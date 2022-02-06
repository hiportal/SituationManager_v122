package com.ex.situationmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.situationmanager.dto.TowRow;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;

import org.xmlpull.v1.XmlPullParserException;

public class DialogActivity extends BaseActivity{

	private final String TAG = "DialogActivity";
	ArrayList<TowRow> itemList;
	
	
	public static String ONECLICK_TOWSTARTEDINFO_SELECT = "ONECLICK_TOWSTARTEDINFO_SELECT";
	public static String ONECLICK_TOWLOGLIST_SELECT = "ONECLICK_TOWLOGLIST_SELECT";
	
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
		
		setContentView(R.layout.popupscreen);
		
		Intent i = getIntent();
		String rpt_id = i.getStringExtra("rpt_id");
		
		Parameters param = new Parameters(ONECLICK_TOWLOGLIST_SELECT);
		param.put("rpt_id", rpt_id);
		new ActionList(ONECLICK_TOWLOGLIST_SELECT, param).execute("");
		ImageView btnBack = (ImageView) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
	
	public class TowRowAdapter extends ArrayAdapter<TowRow>{
		
		private Context mContext;
		private LayoutInflater mInflater;
		
		public TowRowAdapter(Context context, List<TowRow> objects){
			super(context, 0, objects);

			mInflater	= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mContext	= context;
			
			if(objects.size() == 0){
				Toast.makeText(getApplicationContext(), "검색된 결과가 없습니다.", Toast.LENGTH_SHORT).show();
			}
		}
		
		public void setItemImage(int position){

//			Patrol item = this.getItem(position);		// ArrayAdapter
			
		}
		
		public View getView(int position, View convertView, ViewGroup parent){

			TextView pNum;
			TextView pName;
			TextView pTime;
			TextView pDistance;
			
			View view = convertView;
			
			
			if(convertView == null){
				view = mInflater.inflate(R.layout.popupscreen_row, null);
			}
			TowRow item = this.getItem(position);
			if(item != null){
				pNum = (TextView) view.findViewById(R.id.pNum);
				pName = (TextView) view.findViewById(R.id.pName);
				pTime = (TextView) view.findViewById(R.id.pTime);
				pDistance = (TextView) view.findViewById(R.id.pDistance);
				
//				pNum.setText(item.getCar_id());
				pNum.setText(""+(position+1));
				pName.setText(item.getReg_name());
				pTime.setText(item.getReg_date());
				pDistance.setText( addZeroPoint(item.getDisk_km()) +"Km");
				
				for(int i = 0; i < Configuration.User.getCrdns_id_list().size(); i++ ) {
					if (Configuration.User.getCrdns_id_list().get(i).equals(item.getCrdns_id())) {
						pNum.setBackgroundColor(Color.parseColor("#FFDAB9"));
						pName.setBackgroundColor(Color.parseColor("#FFDAB9"));
						pTime.setBackgroundColor(Color.parseColor("#FFDAB9"));
						pDistance.setBackgroundColor(Color.parseColor("#FFDAB9"));
					}
				}
				
			}
			return view;
		}
	}
	public String addZeroPoint(String str){
		if(!"".equals(Common.nullCheck(str))){
			if(str.contains(".")){
				if(str.startsWith(".")){
					return "0"+str;
				}
			
			}
		}
		return "";
	}

	
	
	
	public class ActionList extends AsyncTask<String, Void, XMLData> {
		// --------------------------------------------------------------------------------------------
		// #region 공통코드 정보 수신
		// 진행 상태 Progressbar
		ProgressDialog progressDialog;

		String primitive = "";
		Parameters params = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = ProgressDialog.show(DialogActivity.this, "", "로딩중...", true);
		}

		// primitive 에 따라 URL을 구분짓는다.
		public ActionList(String primitive, Parameters params) {
			this.primitive = primitive;
			this.params = params;
		}

		@Override
		protected XMLData doInBackground(String... arg0) {
			HttpURLConnection conn = null;
			XMLData xmlData = null;

			OutputStream os = null;
			InputStream is = null;
			ByteArrayOutputStream baos = null;
			try {
				// test code
				
				StringBuffer body = new StringBuffer();
				body.append(URL_SENDGPS);
				body.append("?");
				body.append(params.toString());
				
				URL url = new URL(new String( Common.nullTrim(body.toString()).getBytes("EUC-KR"), "Cp1252")) ;
				// URL url = new URL(new String(body.toString()));

				Log.i(TAG, TAG+"URL : = " + body.toString());

				conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("GET");
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setRequestProperty("Cache-Control", "no-cache");
				// conn.setDoOutput(true);
				conn.setDoInput(true);


				int responseCode = conn.getResponseCode();
				Log.d(TAG, TAG + " ACTION responsecode  " + responseCode+ "----" + conn.getResponseMessage());
				if (responseCode == HttpURLConnection.HTTP_OK) {

					is = conn.getInputStream();
					baos = new ByteArrayOutputStream();
					byte[] byteBuffer = new byte[1024];
					byte[] byteData = null;
					int nLength = 0;

					while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
						baos.write(byteBuffer, 0, nLength);
					}
					byteData = baos.toByteArray();
					String response = new String(byteData, "euc-kr");
//					String response = new String(byteData);
//					Log.d("","responseData  = " + response);
					if (response == null || response.equals("")) {
						Log.e(TAG + "Response is NULL!! ", TAG+ "Response is NULL!!");
					}
					Map<String, List<String>> headers = conn.getHeaderFields();
					Iterator<String> it = headers.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						List<String> values = headers.get(key);
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < values.size(); i++) {
							sb.append(";" + values.get(i));
						}
					}
					try {
						xmlData = new XMLData(response);
						String ret = xmlData.get("result");
						if ("1000".equals(ret)) {
							if (ret == null) {
//								throw new IOException();
							} else {
								String retMsg = xmlData.get("resultMessage");
//								throw new IOException();
							}
						}
					} catch (XmlPullParserException e) {
					}
				}

			} catch (IOException e) {
				try {
				} catch (NullPointerException e2) {
				}
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
					}
				}

				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}

				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
					}
				}

				if (conn != null) {
					conn.disconnect();
				}
			}
			return xmlData;
		}

		@Override
		protected void onPostExecute(XMLData result) {
			try {
				progressDialog.dismiss();

				String rtnResultCode = result.get("result");
				if("1000".equals(rtnResultCode)){
					result.setList("entity");
					Log.d("","PatrolMainActivity onPostExecute size = "+result.size());
					TowRow item  = null;
					itemList = new ArrayList<TowRow>();
					
					for (int i = 0; i < result.size(); i++) {
						String car_id = result.get(i, "car_id");
						String reg_name = result.get(i, "reg_name");
						String reg_date = result.get(i, "reg_date");
						String disk_km = result.get(i, "disk_km");
						
						item = new TowRow();
						item.setCar_id(car_id);
						item.setReg_name(reg_name);
						item.setReg_date(reg_date);
						item.setDisk_km(disk_km);
						
						itemList.add(item);
					}
				}
				displayGallery();
				
			} catch (XmlPullParserException e) {
				// TODO: handle exception
			}
		}
	}
	
	public void displayGallery(){
		final ListView listView = (ListView) findViewById(R.id.listView);
		final TowRowAdapter adapter = new TowRowAdapter(this, itemList);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				adapter.setItemImage(position);
			}
		});
		
	}

	@Override
	protected void onActionPost(String primitive, XMLData result, Exception e)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setActivityViewEdit(Activity activity) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
}
