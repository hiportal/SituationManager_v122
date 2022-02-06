package com.ex.situationmanager;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import com.ex.situationmanager.dto.Userinfo;
import com.ex.situationmanager.encrypt.SeedCipher;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.DBAdapter;
import com.ex.situationmanager.util.CustomMultiPartEntity.ProgressListener;
import com.ex.situationmanager.util.Image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public abstract class BaseActivity_bak extends Activity implements OnClickListener,
		GestureDetector.OnGestureListener {

	private static String TAG = "BaseActivity_bak";
	public static String VIEW_TAG = "";
	public static String VIEW_PATROL = "PatrolMainActivity";
	public static String VIEW_TOW = "TowMainActivity";

	SeedCipher seed = new SeedCipher();
	public static byte[] szKey = { (byte) 0x88, (byte) 0xE3, (byte) 0x44,
			(byte) 0x8F, (byte) 0x28, (byte) 0x32, (byte) 0xFE, (byte) 0xF1,
			(byte) 0xF9, (byte) 0xF3, (byte) 0xF1, (byte) 0x37, (byte) 0xFF,
			(byte) 0xA4, (byte) 0x05, (byte) 0x29 };

	// 사용자정보조회
	public static String ONECLICK_GETUSERINFO_SELECT = "ONECLICK_GETUSERINFO_SELECT";
	// gps좌표+각종정보 발송.
	public static String ONECLICK_CARGPS_INSERT = "ONECLICK_CARGPS_INSERT";
	// 순찰,견인 처리완료.
	public static String ONECLICK_WORKCOMPLETE_UPDATE = "ONECLICK_WORKCOMPLETE_UPDATE";
	// 순찰 조치 목록 조회
	public static String ONECLICK_PATROLRCEPTINFO_SELECT = "ONECLICK_PATROLRCEPTINFO_SELECT";
	// 순찰 조치 목록 등록
	public static String ONECLICK_ACDNTACT_INSERT = "ONECLICK_ACDNTACT_INSERT";
	// 순찰 상세화면 목록 조회.
	public static String ONECLICK_GETACDNTACTINFO_SELECT = "ONECLICK_GETACDNTACTINFO_SELECT";
	// 견인 목록 조회
	public static String ONECLICK_TOWRCEPTINFO_SELECT = "ONECLICK_TOWRCEPTINFO_SELECT";
	// 파일전송.
	public static String ONECLICK_FILE_SEND = "ONECLICK_FILE_SEND";

	// 주 접보지사 업데이트.
	public static String ONECLICK_MAINRCVBS_UPDATE = "ONECLICK_MAINRCVBS_UPDATE";
	// 접보받을 지사 목록 업데이트.
	public static String ONECLICK_RCVJISALIST_UPDATE = "ONECLICK_RCVJISALIST_UPDATE";
	// 대국민 접보 등록.
	public static String ONECLICK_CITIZENRCEPIT_INSERT = "ONECLICK_CITIZENRCEPIT_INSERT";
	// 순찰차 전화번호 조회.
	public static String ONECLICK_GETPATROLTELNO_SELECT = "ONECLICK_GETPATROLTELNO_SELECT";

	// public static String URL_SENDGPS =
	// "http://192.168.0.53:8087/insertGPSData";//로컬

	// 개발서버 URL

	// GPS정보 전송.
	// 개발 로컬 테스트
//	public static String URL_SENDGPS = "http://192.168.0.53:8080/proxy.jsp";
	// 운영
	public static String URL_SENDGPS = "http://oneclickmobile.ex.co.kr:8080/proxy.jsp";

	public Uri mUriSet;

	Handler fileHandler;

	Common common = null;
	DBAdapter db = new DBAdapter();
	// 사용자 정보.
	// public static String user_type = "";//0001:순찰, 0002:견인, 0003:대국민
	// public static String bscode = "";//지사코드
	// public static String bsname = "";//지사명
	// public static String crdns_id = "";//자동차 아이디?
	// public static String reg_post = "";//업체명
	// public static String reg_part = "";//업무종류
	// public static String reg_name = "";//이름.
	// public static String hp_no = "";//전화번호
	// public static String reg_pnt = "";//담당지역
	// public static String car_id = "";//순찰차량 일련번호
	// public static String car_nm = "";//순찰차명

	// 사용자 구분
	public static String USER_TYPE_PATROL = "0001";// 순찰자
	public static String USER_TYPE_TOW = "0002";// 견인자
	public static String USER_TYPE_CITIZEN = "0003";// 대국민
	public static String USER_TYPE = USER_TYPE_PATROL;

	// 사용자 정보.
	public static Userinfo User = new Userinfo();
	// 주 접보지사정보
	public static String TowJeopBoJisaCode = "";
	public static String TowJeppBoJisaName = "";

	// 선택된 상황일지 정보.
	// public static String selectedRpt_id = "";
	public static String rpt_local_way = "";
	public static String rpt_start_km = "";
	public static String rpt_bscode = "";
	public static String rpt_bhPkCode = "";
	public static String rpt_reg_type = "";
	public static String rpt_latitude = "";
	public static String rpt_longitude = "";

	public static String patrolOKFlag = "N";// 이상없음 버튼 Y or N

	public static Activity contextActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// longitude = 128.278746;
		// latitude = 36.136670;

		
		// longitude = 128.268365;
		// latitude = 36.105277;

		// 2터널 36.105277, 128.268365

		// 이정
		// 128.26652662254
		// 36.10351775879

		// 위치
		// 128.266636186
		// 36.1034207957

	}


	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	final int menuGroupId = 1;
	final int menuOrderId = 1;
	final int menuUserType_patrol = 1;
	final int menuUserType_tow = 2;
	final int menuUserType_citizen = 3;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(menuGroupId, menuUserType_patrol, menuOrderId, "순찰원");
		// menu.add(menuGroupId, menuUserType_tow, menuOrderId, "견인원");
		// menu.add(menuGroupId, menuUserType_citizen, menuOrderId, "대국민");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Log.d("", "onMenuItemSelected itemId = " + item.getItemId());
		switch (item.getItemId()) {
		// 순찰원
		case menuUserType_patrol:
			Intent i_patrol = new Intent(BaseActivity_bak.this,
					PatrolMainActivity.class);
			startActivity(i_patrol);
			break;
		// 견인원
		case menuUserType_tow:
			Intent i_tow = new Intent(BaseActivity_bak.this, TowMainActivity.class);
			startActivity(i_tow);
			break;

		// 대국민
		case menuUserType_citizen:
			Intent i_citizen = new Intent(BaseActivity_bak.this, MainActivity.class);
			startActivity(i_citizen);
			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	// 서버 통신
	public class Action extends AsyncTask<String, Void, XMLData> {
		// --------------------------------------------------------------------------------------------
		// #region 공통코드 정보 수신
		// 진행 상태 Progressbar
		ProgressDialog progressDialog;

		String primitive = "";
		Parameters params = null;

		XMLData returnData = null;

		@Override
		protected void onPreExecute() {
			if (primitive.equals(ONECLICK_GETUSERINFO_SELECT)) {
				progressDialog = ProgressDialog.show(BaseActivity_bak.this, "", "", true);
			}
			super.onPreExecute();
		}

		// primitive 에 따라 URL을 구분짓는다.
		public Action(String primitive, Parameters params) {
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
				if (ONECLICK_CARGPS_INSERT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_GETUSERINFO_SELECT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_PATROLRCEPTINFO_SELECT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_WORKCOMPLETE_UPDATE.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_MAINRCVBS_UPDATE.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_RCVJISALIST_UPDATE.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_GETPATROLTELNO_SELECT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				}else if (ONECLICK_CITIZENRCEPIT_INSERT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				}

				URL url = new URL(new String(Common.nullTrim(body.toString())
						.getBytes("EUC-KR"), "Cp1252"));
				// URL url = new URL(new String(body.toString()));

				Log.i(TAG, "URL : = " + body.toString());

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
					// String response = new String(byteData);
					Log.d("", "responseData  = " + response);
					if (response == null || response.equals("")) {
						Log.e(TAG + "Response is NULL!! ", TAG
								+ "Response is NULL!!");
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
						if (ONECLICK_CARGPS_INSERT.equals(primitive)) {
							Log.d("","ddddddddddddddddd  "+response);
						}
						returnData = xmlData;
						String ret = xmlData.get("result");
						if ("1000".equals(ret)) {
							if (ret == null) {
								// throw new IOException();
							} else {
								String retMsg = xmlData.get("resultMessage");
								// throw new IOException();
							}
						} else if ("9999".equals(ret)) {
							Log.d("","ddddddddddddddddd 9999 ");
							if (primitive.equals(ONECLICK_CARGPS_INSERT)&& !params.get("STOP").equals("STOP")) {
								sendStop();
								setActivityViewEdit(contextActivity);
							}
						}
					} catch (XmlPullParserException e) {
					}
				}

			} catch (IOException e) {

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

			return null;
		}

		@Override
		protected void onPostExecute(XMLData result) {

			if (primitive.equals(ONECLICK_GETUSERINFO_SELECT)) {
				progressDialog.dismiss();
			}
			try {
				onActionPost(primitive, returnData, null);
			} catch (IOException e) {
				try {
					onActionPost(primitive, returnData, e);
				} catch (IOException e1) {
					Log.e("에러","예외");
				}
			}
		}

	}

	protected abstract void setActivityViewEdit(Activity activity) throws IOException;

	protected abstract void onActionPost(String primitive, XMLData result, Exception e) throws IOException;

	/************************************************************************
	 * 제보 파일 전송 - Progressbar, AsyncTask를 이용하여 파일 전송 - 전송완료 후 제보내용 전송 호출
	 ************************************************************************/
	String sendCheckFile;
	String transData = "Y";
	com.ex.situationmanager.util.CustomMultiPartEntity multipartContent;

	public int iPercent;

	public class DoComplecatedJob extends AsyncTask<String, Integer, Long> {
		long totalSize;
		long totalSizeKB;

		// 진행 상태 Progressbar
		ProgressDialog progressDialog;
		Dialog mDialog = null;
		ProgressBar pb = null;
		ImageButton ibtnProgressCancel;

		public TextView tv;
		public TextView tvTrans;
		public TextView tvTot;
		public int iProgress;
		public List<Image> mFileList;
		Parameters params;
		Activity mActivity;

		public DoComplecatedJob() {
			super();
		}

		public DoComplecatedJob(List<Image> mList, Parameters params, Activity mActivity) {
			super();
			mFileList = mList;
			this.params = params;
			this.mActivity = mActivity;
		}

		@Override
		protected void onPreExecute() {
			// --------------------------------------------------------------------------------------------
			// #region 전송화면 출력

			progressDialog = new ProgressDialog(mActivity);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("파일 전송 중...");
			progressDialog.setCancelable(true);

			progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "전송취소",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							transData = "N";
						}
					});

			mDialog = new Dialog(mActivity, R.style.FullHeightDialog);

			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			mDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			mDialog.setContentView(R.layout.cust_progressbar);
			mDialog.setCancelable(false);

			pb = (ProgressBar) mDialog.findViewById(R.id.pbProgress);
			tv = (TextView) mDialog.findViewById(R.id.tvPercentProgress);
			tvTrans = (TextView) mDialog.findViewById(R.id.tvProgressTrans);
			tvTot = (TextView) mDialog.findViewById(R.id.tvProgressTot);
			ibtnProgressCancel = (ImageButton) mDialog.findViewById(R.id.ibtnProgressCancel);

			ibtnProgressCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					transData = "N";
				}
			});

			// 화면 표시 초기화
			tv.setText("0 %");
			tvTrans.setText("0 KB");
			tvTot.setText("0 KB");

			// 진행 변수 초기화
			iProgress = 0;
			iPercent = 0;
			totalSize = 0;
			totalSizeKB = 0;
			transData = "Y";
			mDialog.show();

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected Long doInBackground(String... strData) {
			// --------------------------------------------------------------------------------------------
			// #region 실 첨부파일 전송, 전송화면 프로그레스 바 업데이트 함수 호출

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			// HttpPost httpPost = new
			// HttpPost("http://uportal.ex.co.kr:5100/pda/src_new/jaenan/APNS/upload_proc.jsp");
			HttpPost httpPost;
			Log.d("","################### DoComplecatedJob doInBackground start");
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(Configuration.FILE_UPLOAD_PATH);
				sb.append("?");
				sb.append(params.toString());
				// URL url = new URL(new String(
				// Common.nullTrim(sb.toString()).getBytes("EUC-KR"), "8859_1"))
				// ;

				httpPost = new HttpPost(sb.toString());

				multipartContent = new com.ex.situationmanager.util.CustomMultiPartEntity(
						new ProgressListener() {

							int uploadPercent = 0;

							@Override
							public void transferred(long num) {
								// TODO Auto-generated method stub
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});

				// String uriSet = mUriSet.toString();
				String absolutePath = "";

				// 파일 첨부 및 전송
				Log.d("", "before send ReadSdCardSize() size = "+ ReadSdCardSize());

				for (int i = 0; i < ReadSdCardSize(); i++) {
					Image image = mFileList.get(i);
					absolutePath = Configuration.directoryName + "/"+ image.getFileName().toString();
					Log.d("", "multipartContent FILENAME = " + absolutePath);
					multipartContent.addPart("uploaded_file" + i, new FileBody(new File(absolutePath)));
				}

				totalSize = multipartContent.getContentLength();
				totalSizeKB = totalSize / 1024;
				// pb.setMax(Integer.parseInt(totalSize + ""));

				// Send it
				multipartContent.addPart("renamePrefix", new StringBody(""));
				if (!User.getUser_type().equals(USER_TYPE_CITIZEN)) {
					multipartContent.addPart("rpt_id",new StringBody(params.get("rpt_id"), Charset.forName("UTF-8")));
				}
				multipartContent.addPart("nscode",new StringBody(params.get("nscode"), Charset.forName("UTF-8")));
				multipartContent.addPart("nsname",new StringBody(params.get("nsname"), Charset.forName("UTF-8")));
				multipartContent.addPart("bhcode",new StringBody(params.get("bhcode"), Charset.forName("UTF-8")));
				multipartContent.addPart("bhname",new StringBody(Common.nullCheck(params.get("bhname")),Charset.forName("UTF-8")));
				multipartContent.addPart("ijung",new StringBody(params.get("ijung"), Charset.forName("UTF-8")));

				httpPost.setEntity(multipartContent);

				// multipartContent.addPart("renamePrefix", new

				// 미디어 파일 전송
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				String serverResponse = EntityUtils.toString(response.getEntity());

				sendCheckFile = serverResponse;
				Log.d("","*****************************************************");
				Log.d("", "" + sendCheckFile);
				Log.d("", "" + sendCheckFile.trim());
				Log.d("","*****************************************************");

			} catch (IOException e) {
				Log.e("에러","예외");
			}// end try~catch
			Log.d("", "################### DoComplecatedJob doInBackground end");
			return null;

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected void onCancelled() {
			// --------------------------------------------------------------------------------------------
			// #region 전송화면에서 취소버튼 클릭

			// 중지 flag가 호출된 경우 파일 upload 중단
			multipartContent.stop();
			multipartContent = null;
			// dialogStartup.dismiss();

			Log.d("ProgressUpdate", "onCancelled : " + "onCancelled");

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// --------------------------------------------------------------------------------------------
			// #region 전송화면 프로그레스바 업데이트

			pb.setProgress((int) (progress[0]));

			// thread 중지
			if (transData.equals("N")) {
				cancel(true);
			}

			if (iPercent == (int) (progress[0])) {

			} else {

				iPercent = (int) (progress[0]);

				if (iProgress == 0) {
					iProgress++;
				}// end if

				for (int i = iProgress; iProgress < iPercent; i++) {
					iProgress++;
				}// end for

				tv.setText(iProgress + " %");

				Log.d("ProgressUpdate", "iPercent : " + iPercent);
				Log.d("ProgressUpdate", "iProgress : " + iProgress);
				Log.d("ProgressUpdate", "totalSize : " + totalSize);

				DecimalFormat df = new DecimalFormat("###,###");

				tvTrans.setText(df.format(iProgress * totalSizeKB / 100) + " KB");
				tvTot.setText(df.format(totalSizeKB) + " KB");
			}// end if

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected void onPostExecute(Long result) {
			// --------------------------------------------------------------------------------------------
			// #region 첨부파일 전송 완료 후 제보내용 전송 함수 호출

			progressDialog.dismiss();
			multipartContent = null;
			mDialog.dismiss();

			Log.d("ProgressUpdate", "transData : " + transData);
			Log.d("ProgressUpdate", "transData : " + result);

			if (multipartContent != null || (iPercent < 100 && iPercent > 0)) {

				openWarnDialog(mActivity, "파일 전송에 실패했습니다.");
			}else{
				if (USER_TYPE_TOW.equals(User.getUser_type()) || USER_TYPE_PATROL.equals(User.getUser_type())) {
					openWarnDialog(mActivity, "파일 전송 성공");
				}else{
					openWarnDialog(mActivity, "파일 전송 성공\n제보가 완료 되었습니다.");
				}
			}
			/*
			 * else{
			 * 
			 * //파일 전송이 정상적으로 종료된 경우
			 * 
			 * if(transData.equals("Y")){ fileHandler.postDelayed(new Runnable()
			 * {
			 * 
			 * @Override public void run() { fileHandler.postDelayed(this,
			 * 1000); fileHandler.removeMessages(0); new
			 * saveReportInfo().execute("1000"); } }, 1000);
			 * 
			 * }//end if }//end if
			 */
			// #endregion
			// --------------------------------------------------------------------------------------------
		}

	}

	double totalFileSize = 0.0;

	public void executeJob(final Parameters params, Activity mActivity) {
		Log.d("",TAG + "executeJob user type = "  + User.getUser_type());
		if (ReadSdCardSize() > 0) {
			final DoComplecatedJob task = new DoComplecatedJob(ReadSDCard(), params, mActivity);
			task.execute("5000");

			fileHandler = new Handler();
			fileHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					fileHandler.postDelayed(this, 2000);
					if (task.getStatus() == AsyncTask.Status.FINISHED) {
						fileHandler.removeMessages(0);
					}// end if
				}
			}, 2000);
		} 
			
		
		
	}

	// SituationManager 폴더내에 있는 파일갯수
	public static int ReadSdCardSize() {

		File fileDir = new File(Configuration.directoryName);
		if (!fileDir.exists())
			fileDir.mkdir();
		File[] files = fileDir.listFiles();

		return files.length;
	}

	// SituationManager 폴더내에 있는 파일 전체용량
	public static double ReadSDCardMB() {

		File fileDir = new File(Configuration.directoryName);

		if (!fileDir.exists())
			fileDir.mkdir();

		File[] files = fileDir.listFiles();
		double totalFileSize = 0;
		for (int i = 0; i < files.length; i++) {

			File file = files[i];
			double nByte = file.length();
			double mByte = 0;
			double tvMByte = 0;
			String tvTextSize = "";

			mByte = Math.floor(nByte * 100 / (1024 * 1024));
			tvTextSize = String.valueOf(mByte / 100);

			tvMByte = Math.round(mByte / 10);
			tvMByte = tvMByte / 10;
			totalFileSize = totalFileSize + tvMByte;
		}

		return totalFileSize;
	}

	// 1개의 파일용량 MB 로 표시
	public static double getFileSizeMB(double filesize) {
		double nByte = filesize;
		double mByte = 0;
		double tvMByte = 0;
		String tvTextSize = "";

		mByte = Math.floor(nByte * 100 / (1024 * 1024));
		tvTextSize = String.valueOf(mByte / 100);

		tvMByte = Math.round(mByte / 10);
		tvMByte = tvMByte / 10;
		return tvMByte;
	}

	/**
	 * 다이얼 로그 박스
	 * 
	 * @param message
	 */
	Dialog dialogWarn;

	public void openWarnDialog(Context ctx, String message) {
		dialogWarn = new Dialog(ctx, R.style.FullHeightDialog);

		dialogWarn.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogWarn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		dialogWarn.setContentView(R.layout.cust_dialog_warn);
		dialogWarn.setCancelable(false);

//		RelativeLayout layout = (RelativeLayout) dialogWarn.findViewById(R.id.RelativeLayout01);
		ImageButton button = (ImageButton) dialogWarn.findViewById(R.id.ibtnMovie);

		TextView tvPrompt = (TextView) dialogWarn.findViewById(R.id.tvPrompt);
		// if(message.length() > 45){
		// tvPrompt.setTextSize(14);
		// }
		tvPrompt.setText(message);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialogWarn.dismiss();
			}
		});

		dialogWarn.show();
	}

	/************************************************************************
	 * GPS
	 ************************************************************************/
	// public static LocationManager mLocMgr = null;
	// public static LocationListener mLocListener = null;
	private static Handler mHandler = new Handler();
	double ppastLatitude;
	double ppastLongitude;
	double pastLatitude;
	double pastLongitude;
	double angle;
	double longitude = 0.0;
	double latitude = 0.0;

	//
	double altitude;
	double speed = 0.0;
	boolean mGpsSatStat = false;
	Location location;
	LocationManager lm;
	StringBuilder sb;
	int noOfFixes = 0;
	static boolean isGPSEnable = false;
	boolean sendGpsFlag = false;

	private double START_LAT = 37.200349;
	private double START_LNG = 127.094467;

	// GPS 정보
	String ns_code = ""; // 노선코드
	String ns_name = "경부선"; // 노선명
	String banghyang = ""; // 방향
	String bhCode = "S"; // 방향코드(S,E)
	String sisul; // 시설물명(IC/JCT, 휴게소, 졸음쉼터)
	String siseolDaepyo;// ICJCT, 휴게소, 졸음쉼터 화면표출 텍스트
	String ppastIjung = "000";
	String pastIjung = "111";
	String currentIjung = "400"; // 이정
	String rampid = "";

	String gonggu = "";
	boolean isInNoseon = true;

	private int myCnt = 0;
	public static LocationManager RelimyLocMgr;
	public static LocationListener RelimyListener;

	private int maxNsEmptyTime = 30;
	private int moveNsEmptyTime = 4;
	private int emptyCnt = 0;// 좌표정보없음 카운트 초기화
	private boolean gpsStatusYN = false;

	public void startMyGps() {
		try{
			RelimyLocMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			RelimyListener = new SpeedoActionListener();
			RelimyLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					RelimyListener);

			RelimyLocMgr.addGpsStatusListener(gpsStatusListener);
		}catch (SecurityException e){
			Log.e("에러","예외");
		}

	}

	/************************************************************************
	 * GPS 중지
	 ************************************************************************/
	public static void stopGPS() {
		if (RelimyLocMgr != null) {
			RelimyLocMgr.removeUpdates(RelimyListener);
		}
	}

	public int locationChangedCnt = 0;

	private class SpeedoActionListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
//				Toast.makeText(BaseActivity_bak.this, location.getLatitude()+"\n"+location.getLongitude(), 500).show();
				Log.d("","locationChanged in="+latitude +" : "+longitude);
				locationChangedCnt = 0;
				dist = 0.0;
				firstFlag = true;
				pastLatitude = latitude;
				pastLongitude = longitude;
				latitude = location.getLatitude();// 위도
				longitude = location.getLongitude();// 경도
				
				//자릿수 소숫점 8째 자리까지
				latitude = Common.doubleCutToString(latitude);
				longitude = Common.doubleCutToString(longitude);
				
				angle = Math.atan2(longitude - pastLongitude, latitude- pastLatitude);
				altitude = location.getAltitude();// 고도
				speed = Math.round((location.getSpeed() * 3600) / 1000);// 속도
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.d("", "GPS Provider Disabled!!");
			gpsStatusYN = false;
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.d("", "GPS Provider Enabled!!");
			gpsStatusYN = true;
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Log.v("GPSInfo", "Status Changed: Out of Service");
				latitude = START_LAT;
				longitude = START_LNG;
				gpsStatusYN = false;
				break;

			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.v("GPSInfo", "Status Changed: Temporarily Unavailable");
				gpsStatusYN = false;
				break;

			case LocationProvider.AVAILABLE:
				Log.v("GPSInfo", "Status Chaznged: Available");
				dist = 0.0;
				gpsStatusYN = true;
				break;
			}

		}

	}

	GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
		@Override
		public void onGpsStatusChanged(int event) {

			switch (event) {
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.d("", "GPSStatus Listener = SATELLITE_STATUS");
				//GpsStatus status = RelimyLocMgr.getGpsStatus(null);
				GpsStatus status = null;
				try{
					status = RelimyLocMgr.getGpsStatus(null);
				}catch (SecurityException e){
					Log.e("에러","예외");
				}
				Iterable<GpsSatellite> sats = status.getSatellites();
				Iterator<GpsSatellite> iter = sats.iterator();

				GpsSatellite gpsS = null;

				boolean checkStat = false;
				int checkStatCnt = 0;
				while (iter.hasNext()) {
					gpsS = iter.next();

					if (gpsS.usedInFix()) {
						// Log.d("GPSInfo", "Almanac : " + gpsS.hasAlmanac());
						// Log.d("GPSInfo", "Ephemeris : " +
						// gpsS.hasEphemeris());
						checkStatCnt++;
					}
				}
				if (checkStatCnt > 3) {
					gpsStatusYN = true;
				} else {
					gpsStatusYN = false;
				}
				Log.d("GPSInfo", "checkStatCnt : " + checkStatCnt);

				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.d("", "GPSStatus Listener = GPS_EVENT_FIRST_FIX");
				break;

			default:
				break;
			}
		}

	};

	/*static LocationManager mLocMgr;
	static LocationListener mLocListener;
	
	private void startGPS(){
		//--------------------------------------------------------------------------------------------
		// #region startGPS, 위치정보 표시 [ setCurrentPosition(getDBSearch(latitude, longitude)); ]
		
		new Thread(new Runnable(){
			public void run() {
		    	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				mHandler.post(new Runnable(){
					public void run() {
				    	mLocMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);				
				    	mLocListener = new LocationListener(){
							public void onProviderDisabled(String provider) {

							}
							
							public void onStatusChanged(String provider, int status,Bundle extras) {

								switch (status) {
								case LocationProvider.OUT_OF_SERVICE:
									Log.v("GPSInfo", "Status Changed: Out of Service");
									latitude = START_LAT;
									longitude = START_LNG;

									break;
								case LocationProvider.TEMPORARILY_UNAVAILABLE:
									Log.v("GPSInfo", "Status Changed: Temporarily Unavailable");
									break;
								case LocationProvider.AVAILABLE:
									Log.v("GPSInfo", "Status Changed: Available");
									
									break;

								}								
							}
							public void onProviderEnabled(String provider) {}
							public void onLocationChanged(Location location) {

								Toast.makeText(BaseActivity_bak.this, "latitude " + location.getLatitude(), Toast.LENGTH_SHORT).show();
								sb = new StringBuilder(512);

								noOfFixes++;

								latitude = location.getLatitude();// 위도
								longitude = location.getLongitude();// 경도
								altitude = location.getAltitude();// 고도

								Log.d("process", "gps info");
								
								//sqlite 이용하여 위치 조회
								setCurrentPosition(getDBSearch(latitude, longitude));
							}
				    	};
						
				        Criteria criteria = new Criteria();
				        criteria.setAccuracy(Criteria.ACCURACY_FINE);  		// 정확도
				        criteria.setPowerRequirement(Criteria.POWER_LOW); 	// 전원 소비량
				        criteria.setAltitudeRequired(false);    			// 고도, 높이 값을 얻어 올지를 결정
				        criteria.setBearingRequired(false);
				        criteria.setSpeedRequired(false);    				//속도
				        criteria.setCostAllowed(true);      				//위치 정보를 얻어 오는데 들어가는 금전적 비용
				        
				        mLocMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				        mLocMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 100, mLocListener);
				        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 100, mLocListener);			
				        
				        GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() { 
				            public void onGpsStatusChanged(int event) { 
				                if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) { 
				                    GpsStatus status = mLocMgr.getGpsStatus(null); 
				                    Iterable<GpsSatellite> sats = status.getSatellites(); 				                    
				                    Iterator<GpsSatellite> iter = sats.iterator();
				                    
				                    GpsSatellite gpsS =  null;
				                    
				                    boolean checkStat = false;
				                    int checkStatCnt = 0;
				                    while(iter.hasNext()){
				                    	gpsS = iter.next();
				                    	
				                    	if(gpsS.usedInFix()){
				                    		Log.d("GPSInfo", "Almanac : "+gpsS.hasAlmanac());
				                    		Log.d("GPSInfo", "Ephemeris : "+gpsS.hasEphemeris());
				                    		checkStatCnt++;
				                    	}
				                    }
				                    Log.d("GPSInfo", "checkStatCnt"+checkStatCnt);
				                    
				                    if(checkStatCnt > 3){
				                    	checkStat = true;
				                    }else{
				                    	checkStat = false;
				                    }
				                    
				                    if(mGpsSatStat != checkStat){
				                    	mGpsSatStat = checkStat;
				                    	
				                    	try{
				                    		
				                    		if(mLocMgr != null){
						                    	Location l =  mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						                    	
						                    	if(l != null){
													latitude = l.getLatitude();// 위도
													longitude = l.getLongitude();// 경도
													altitude = l.getAltitude();// 고도
													Toast.makeText(BaseActivity_bak.this, "latitude = " + latitude, Toast.LENGTH_SHORT).show();
													Log.d("GPSInfo", "StatChanged...");
													
													//sqlite 이용하여 위치 조회
													setCurrentPosition(getDBSearch(latitude, longitude));
						                    	}
				                    		}
				                    		
				                    	}catch(Exception ex){
				                    		ex.printStackTrace();
				                    	}
				                    	
				                    }
				                    
				                } 
				            } 
				        } ;

				        mLocMgr.addGpsStatusListener(gpsStatusListener);
				        String provider = mLocMgr.getBestProvider(criteria, true);
				        
				        Log.d("GPSInfo", "bestProvider : "+provider);
				        
				        if(provider != null){
				        	mLocMgr.requestLocationUpdates(provider, 4000, 100, mLocListener);
				        }else{
				        	mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 100, mLocListener);
				        }
				    	
				    	if(!mLocMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){
							AlertDialog.Builder adb	= 
								new AlertDialog.Builder(BaseActivity_bak.this);
							
							adb.setCancelable(false);
							adb.setTitle("고객제보");  
							adb.setMessage("GPS가 켜져있지 않습니다.\n'무선 네트워크 사용' 으로 변경하시면 초기 정보가 부정확할 수 있습니다.\n지금 켜시겠습니까?");
							adb.setPositiveButton("예", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(i);
								}
							});
							adb.setNegativeButton("아니오", null);
							adb.show();    		
				    	}				    	
					}
		    	});		    			    	
			}
		}).start();
		
		// #endregion
		//--------------------------------------------------------------------------------------------
	}	
	
	*//************************************************************************
	 * GPS 중지
	 ************************************************************************//*	
	public static void stopCusGPS(){
		if(mLocMgr != null)
			mLocMgr.removeUpdates(mLocListener);
	}*/
	
	Cursor dbCursor;
	/************************************************************************
	 * GPS 정보에 따른 위치정보를 DB에서 조회 후 리턴 - 방향 기점, 종점 구분 방법 : 이전 좌표의 이정과 현재 이정을 비교
	 * 이전이정 > 현재 이정 = 기점 (경부선: 부산) 이전이정 < 현재 이정 = 종점 (경부선: 서울) : 정지상태에서 GPS 수신하여
	 * 이전이정이 없는 경우 현재 이정 기준에서 먼저 검색되는 방향을 표시 즉, 정지상태에서 비교할 이정이 없는 경우에는 정확한 방향표시
	 * 불가능 함.
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 ************************************************************************/
	public String getDBSearch(double latitude, double longitude) {
		// --------------------------------------------------------------------------------------------
		// #region GPS 위경도 좌표에 따른 위치(노선, 방향, 이정, 부근 IC/JCT) 정보를 리턴한다.

		Log.v("GPSInfo", "getDBSearch");

		String rtnStr = "";
		// -> 현재 위치가 있다면 현재 위치에 과거 위치값으로 대입
		// -> 현재 위치가 없다면 현재 위치에 현재 위치값으로 대입
		if (!"".equals(currentIjung) && !"-999".equals(currentIjung)) {
			ppastIjung = pastIjung;
			pastIjung = Common.nullCheck(currentIjung);
			if (!pastIjung.equals("")) {
				ppastIjung = pastIjung;
			} else {
				pastIjung = "000";
			}// end if

		} else {
			// currentIjung = "111";
		}// end if

		Log.d("GPSInfo", "latitude : " + latitude);
		Log.d("GPSInfo", "longitude : " + longitude);
		Log.d("GPSInfo", "ns_code : " + ns_code);

		try {
			// private int maxNsEmptyTime = 30;
			// private int moveNsEmptyTime = 4;
			// private int emptyCnt = 0;//좌표정보없음 카운트 초기화

			dbCursor = db.fetchRange(latitude, longitude, ns_code);

			Log.d("GPSInfo", "getDBSearchCompleted...");
			if (dbCursor.getCount() > 0) {

				// cursor.getCount(); // 총 ROW 수
				// cursor.getColumnCount() // 컬럼 개수

				// 현재 GPS정보에 노선이 2개 이상인 경우 기존 노선 정보를 확인한다.
				String rowResults = "";

				if (dbCursor.getCount() > 1) {
					if (ns_code != null && ns_code.trim().length() > 0) {

						Log.d("GPSInfo", "GPSInfo 결과값 2개이상 기존 노선코드 존재");
						Log.d("GPSInfo", "ns_code : " + ns_code + "|");
						// Log.d("GPSInfo", "dbCursor.getString(0) : " +
						// dbCursor.getString(0) +"|" );

						dbCursor.moveToFirst();
						for (int i = 0; i < dbCursor.getCount(); i++) {

							dbCursor.moveToPosition(i);

							if (ns_code.equals(dbCursor.getString(0))) {

								ns_code = dbCursor.getString(0);
								currentIjung = dbCursor.getString(2);
								ns_name = dbCursor.getString(1);
								sisul = dbCursor.getString(4);
								rampid = dbCursor.getString(5);

							}// end if
						}// end for
					} else {

						Log.d("GPSInfo", "GPSInfo 결과값 2개이상 기존 노선코드 없음");
						dbCursor.moveToFirst();

						for (int i = 0; i < dbCursor.getCount(); i++) {
							dbCursor.moveToPosition(i);

							ns_code = dbCursor.getString(0);
							currentIjung = dbCursor.getString(2);
							ns_name = dbCursor.getString(1);
							sisul = dbCursor.getString(4);
							rampid = dbCursor.getString(5);
						}// end for
					}// end if

				} else {
					Log.d("GPSInfo",
							"GPSInfo 결과값 1개 | count : " + dbCursor.getCount());

					// for(int i=0; i<dbCursor.getCount(); i++){

					dbCursor.moveToFirst();

					ns_code = dbCursor.getString(0);
					currentIjung = dbCursor.getString(2);
					ns_name = dbCursor.getString(1);
					sisul = dbCursor.getString(4);
					rampid = dbCursor.getString(5);

					// dbCursor.moveToNext();
					// }

				}// end if

				Log.d("GPSInfo", "GPSInfo ns_code : " + ns_code + "|");
				Log.d("GPSInfo", "GPSInfo currentIjung : " + currentIjung + "|");
				Log.d("GPSInfo", "GPSInfo ns_name : " + ns_name + "|");

				// 방향 목록
				Cursor cursorBangHyang = db.fetchBangHyang(ns_code);

				String gjMyeong = "";
				String jjMyeong = "";
				if (cursorBangHyang.getCount() > 0) {
					cursorBangHyang.moveToFirst();

					gjMyeong = cursorBangHyang.getString(2);
					jjMyeong = cursorBangHyang.getString(3);
				}// end if

				double temp0 = Double.parseDouble(ppastIjung);
				double temp1 = Double.parseDouble(pastIjung);
				double temp2 = Double.parseDouble(currentIjung);

				Log.d("GPSInfo", "GPSInfo temp0" + temp2 + ":" + temp1 + ":"
						+ temp0 + " ::::: " + ns_code);
				// Log.d("GPSInfo", "temp1" + temp1);
				// Log.d("GPSInfo", "temp2" + temp2);
				// 서울외곽순환선
				// <down>판교</down>
				// <upper>구리</upper>
				// <middle>일산</middle>
				if (ns_code.equals("1000")) {
					if (temp2 <= 33) {
						if (temp2 > temp1) {// 현재이정(currentIjung)이
											// 과거이정(pastIjung)보다
							// 크면
							if (temp1 >= temp0) {
								banghyang = common.UPPER_BANGHYANG;
								bhCode = "S";
							}// end if
						} else if (temp2 < temp1) {
							if (temp1 <= temp0) {
								banghyang = common.DOWN_BANGHYANG;
								bhCode = "E";
							}// end if
						}// end if
					} else if (temp2 > 33 && temp2 <= 70) {
						if (temp2 > temp1) {// 현재이정(currentIjung)이
											// 과거이정(pastIjung)보다
							// 크면
							if (temp1 >= temp0) {
								banghyang = common.MIDDLE_BANGHYANG;
								bhCode = "S";
							}// end if
						} else if (temp2 < temp1) {
							if (temp1 <= temp0) {
								banghyang = common.UPPER_BANGHYANG;
								bhCode = "E";
							}// end if
						}// end if
					} else {

						if (temp2 > temp1) {// 현재이정(currentIjung)이
											// 과거이정(pastIjung)보다
							// 크면
							if (temp1 >= temp0) {
								banghyang = common.DOWN_BANGHYANG;
								bhCode = "S";
							}// end if
						} else if (temp2 < temp1) {
							if (temp1 <= temp0) {
								banghyang = common.MIDDLE_BANGHYANG;
								bhCode = "E";
							}// end if
						}// end if
					}// end if
				} else {
					Log.d("", "GPSInfo temp2-1-0 " + temp2 + " : " + temp1
							+ " : " + temp0);
					if (temp2 > temp1) {// 현재이정(currentIjung)이 과거이정(pastIjung)보다
						// 크면
						if (temp1 >= temp0) {
							banghyang = jjMyeong;
							bhCode = "E";
						}// end if
					} else if (temp2 < temp1) {
						if (temp1 <= temp0) {
							bhCode = "S";
							banghyang = gjMyeong;
						}// end if
					}
					// else{
					//
					// Log.d("", "ddddddddddddddd " + bhCode);
					// bhCode = bhCode;
					// }
				}// end if(ns_code.equals("1000")){
				Log.d("", "GPSInfo bhcode = " + bhCode);
				rtnStr = ns_name + "|" + banghyang + "|" + currentIjung + "|"+ ns_code + "|0|" + siseolDaepyo;

				Log.d("GPSInfo", "Row : " + dbCursor.getPosition() + " => "+ rowResults);
				Log.d("GPSInfo", "rtnStr :  " + rtnStr);

				isInNoseon = true;
			} else {
				// 노선 내에 존재하지 않는 경우
				// 메시지 한 번만 표시
				if (isInNoseon) {
					Toast.makeText(getApplicationContext(),
							"현재 위치는 도로공사 관리노선에 해당되지 않습니다.", 5).show();
				}// end if

				Log.d("GPSInfo", "mGpsSatStat2:" + mGpsSatStat);

				if (mGpsSatStat) {
					rtnStr = "노선외|_|-999|_|0|0";
				} else {
					rtnStr = "노선외(위성GPS가 활성화되지 \n않아 위치정보가 부정확합니다)|_|-999|_|0|0";
				}// end if

				isInNoseon = false;
			}// end if (dbCursor.getCount() > 0) {

			// 방향코드조회
			// bhCode = db.getBanghyangCode(ns_code, banghyang);

			// 방향정보 의 code 컬럼 값.
			rpt_bhPkCode = db.fetchBanghyang_sub(ns_code, bhCode, rpt_bscode, currentIjung);

		} catch (NullPointerException e) {
			Log.e("에러","예외");
		} finally {
			Log.d("", "GPSInfo rtnStr = " + rtnStr);
			if (dbCursor != null && !dbCursor.isClosed()) {
				dbCursor.close();
			}// end if
		}// end try~catch

		return rtnStr;

		// #endregion
		// --------------------------------------------------------------------------------------------
	}

	// 좌표수집 불가시 실행.
	static double dist = 0.0;

	public void noLocationInfo() {

		if (gpsStatusYN == false) {
			emptyCnt++;
			emptyCnt++;
			if (moveNsEmptyTime % moveNsEmptyTime == 0) {
				if (moveNsEmptyTime <= maxNsEmptyTime) {

					Cursor cursor = db.fetchNextRange(ns_code, currentIjung,
							bhCode);
					Log.d("", "noLocationInfo 4 Count= " + cursor.getCount());
					while (cursor.moveToNext()) {

						ns_code = Common.nullCheck(cursor.getString(0));
						ns_name = Common.nullCheck(cursor.getString(1));
						currentIjung = Common.nullCheck(cursor.getString(2));
						sisul = Common.nullCheck(cursor.getString(4));
						rampid = Common.nullCheck(cursor.getString(5));

					}

					sisul = db.fetchSisulAll(ns_code, latitude, longitude,bhCode);// 시설물전체조회
					System.out.println("##############################################");
					System.out.println("Tunnel Update Locatoin cursor latitude = "+ latitude);
					System.out.println("Tunnel Update Locatoin cursor longitude = "+ longitude);

					// //////////////////////////////////////////////////////////////////////////////////////
					double currlo = longitude;
					double currla = latitude;

					// double besidelo = 129.0932067;
					// double besidela = 35.24657835;
					// double nextlo = 129.0939412;
					// double nextla = 35.24724875;
					// double nnextlo = 129.0946756;
					// double nnextla = 35.24791915;
					// double resultlo = 0.0;
					// double resultla = 0.0;

					double besidelo = 0.0;
					double besidela = 0.0;
					double nextlo = 0.0;
					double nextla = 0.0;
					double nnextlo = 0.0;
					double nnextla = 0.0;
					double resultlo = 0.0;
					double resultla = 0.0;

					System.out.println("Tunnel Update Locatoin cursor count = "+ cursor.getCount());
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						if (i == 0) {
							besidelo = Common.nullCheckDouble(cursor.getString(6));
							besidela = Common.nullCheckDouble(cursor.getString(7));
						} else if (i == 1) {
							nextlo = Common.nullCheckDouble(cursor.getString(6));
							nextla = Common.nullCheckDouble(cursor.getString(7));
						} else if (i == 2) {
							nnextlo = Common.nullCheckDouble(cursor.getString(6));
							nnextla = Common.nullCheckDouble(cursor.getString(7));
						}
					}
					System.out.println("Tunnel Update Locatoin cursor count be = "+ besidelo);
					System.out.println("Tunnel Update Locatoin cursor count = "+ besidela);
					System.out.println("Tunnel Update Locatoin cursor count n = "+ nextlo);
					System.out.println("Tunnel Update Locatoin cursor count = "+ nextla);
					System.out.println("Tunnel Update Locatoin cursor count nn  = "+ nnextlo);
					System.out.println("Tunnel Update Locatoin cursor count = "+ nnextla);

					// besidela = 36.10480877;
					// besidelo = 128.268051614;
					//
					// nextla = 36.1041269794;
					// nextlo = 128.267325781;
					//
					// nnextla = 36.1034207957;
					// nnextlo = 128.266636186;

					double dist_curr_beside = Math.sqrt(Math.pow((currlo - besidelo), 2)+ Math.pow((currla - besidela), 2));
					double anglea = Math.atan2(currla - besidela, currlo - besidelo);
					double angleb = Math.atan2(nextla - besidela, nextlo - besidelo);
					double nextangleb = Math.atan2(nnextla - nextla, nnextlo- nextlo);
					double realangle = anglea - angleb;

					System.out.println("Tunnel Update Locatoin dist_curr_beside= "+ dist_curr_beside);
					System.out.println("Tunnel Update Locatoin anglea = "+ anglea);
					System.out.println("Tunnel Update Locatoin angleb = "+ angleb);
					System.out.println("Tunnel Update Locatoin nextangleb = "+ nextangleb);
					System.out.println("Tunnel Update Locatoin realangle = "+ realangle);

					// if(firstFlag == true){
					// dist = Math.sin(realangle) * dist_curr_beside;
					// System.out.println("Tunnel Update Locatoin dist = "+
					// dist);
					// firstFlag = false;
					// }
					System.out.println("Tunnel Update Locatoin dist 1 = "+ dist);
					if (dist == 0.0) {
						dist = Math.sin(realangle) * dist_curr_beside;
					}
					System.out.println("Tunnel Update Locatoin dist 2 = "+ dist);

					if (dist > 0) {
						System.out.println("Tunnel Update Locatoin dist edit = "+ dist);
						resultlo = nextlo+ (Math.cos(nextangleb + Math.atan2(1, 0)) * Math.abs(dist));
						resultla = nextla+ (Math.sin(nextangleb + Math.atan2(1, 0)) * Math.abs(dist));
					} else {
						System.out.println("Tunnel Update Locatoin dist edit = "+ dist);
						resultlo = nextlo+ (Math.cos(nextangleb - Math.atan2(1, 0)) * Math.abs(dist));
						resultla = nextla+ (Math.sin(nextangleb - Math.atan2(1, 0)) * Math.abs(dist));
					}

					if (resultla < 50) {
						longitude = resultlo;
						latitude = resultla;
						
						//자릿수 소숫점 8째 자리까지
						longitude = Common.doubleCutToString(longitude);
						latitude = Common.doubleCutToString(latitude);
					}
					System.out.println("Tunnel Update Locatoin resultlo >> "+ resultlo);
					System.out.println("Tunnel Update Locatoin resultla >> "+ resultla);
					// //////////////////////////////////////////////////////////////////////////////////////

				} else {
				}
			}
		} else {
			emptyCnt = 0;
			Log.d("", TAG + " tunnel in false");
		}
	}

	/************************************************************************
	 * GPS 정보에 따른 DB검색 결과 값을 각 변수 대입하고 화면에 표시한다.
	 ************************************************************************/
	private void setCurrentPosition(String dbSearch) {
		// --------------------------------------------------------------------------------------------
		// #region GPS 정보에 따른 DB검색 결과 값을 각 변수 대입하고 화면에 표시한다.
		Log.d("GPSInfo", "setCurrentPosition");

		Log.d("GPSInfo", dbSearch);

		String[] GPSInfo = dbSearch.split("[|]");
		Log.d("GPSInfo", "setCurrentPosition" + GPSInfo.length);
		// GPS 정보가 정상인 경우
		if (GPSInfo.length >= 5) {

			// etLocation.setTextSize(20);

			Log.d("GPSInfo", "GPSInfo[0]" + GPSInfo[0]);
			Log.d("GPSInfo", "GPSInfo[1]" + GPSInfo[1]);
			Log.d("GPSInfo", "GPSInfo[2]" + GPSInfo[2]);

			// 관리 노선인 경우
			if (GPSInfo[0] != null && !GPSInfo[0].equals("")
					&& !GPSInfo[0].startsWith("노선외") && GPSInfo[2] != null) {

				// 이정에 소수점이 없으면 .0 추가
				if (GPSInfo[2].indexOf(".") <= -1) {
					GPSInfo[2] = GPSInfo[2] + ".0";
				} else {
					if (GPSInfo[2].indexOf(".") == GPSInfo[2].length() - 1) {
						GPSInfo[2] = GPSInfo[2] + "0";
					}// end if
				}// end if

				// 위치정보가 update 될 때마다 각 변수에 위치정보 설정함
				ns_name = GPSInfo[0]; // 노선명
				banghyang = GPSInfo[1]; // 방향
				currentIjung = GPSInfo[2]; // 이정
				ns_code = GPSInfo[3]; // 노선코드
			} else {
				ns_name = ""; // 노선명
				banghyang = ""; // 방향
				// currentIjung = "-999"; // 이정
				currentIjung = currentIjung;
				ns_code = ""; // 노선코드
				sisul = "";
			}// end if
		}// end if

		sisul = db.fetchSisulAll(ns_code, latitude, longitude, bhCode);// 시설물전체조회
		// 공구조회
		if (!"".equals(Common.nullCheck(ns_code))) {
			gonggu = db.fetchGonggu(ns_code, currentIjung);// 공구조회
		} else {// 노선 OUT
			gonggu = "";

		}

		// #endregion
		// --------------------------------------------------------------------------------------------
	}

	// 좌표보정 변수 start
	// private String fixBscode = "";
	// private String fixReg_Type = "";
	// private String fixLocal_nm = "";
	// private String fixDirection = "";
	// private String fixDist_km = "";
	// private String fixDist_zkm = "";
	// private String fixDept_nm = "";
	// private String fixStart_info = "";
	// private String fixUse_yn = "";
	// private String fixLocal_way = "";
	// private String fixGonggu = "";
	// private String fixLatitude = "";
	// private String fixLongitude = "";
	// private String fixCar_sisul = "";
	// private String fixAltitude = "";
	// private String fixSpeed = "";
	// private String fixAtcode = "";
	// private String fixAngle = "";
	// 좌표보정 변수 end

	public boolean DATA_SEND_STAT = true;
	public static String start_yn = "N"; // Y, N, O(출동종료안햇을 경우)
	private int timerDelay = 1;// timerDelay 초마다 실행
	private int timerCnt = 0;// 카운트 증가. 최대 timerDelay
	Timer timer = new Timer();
	Handler handler = new Handler();

	boolean firstFlag = true;

	public void startTimer() {

		TimerTask timertask = new TimerTask() {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// GPS전송
					// if (DATA_SEND_STAT == true) {
					if (timerDelay <= timerCnt) {
						timerCnt = 0;
						// ***************************************************************

						// gpsStatusYN =
						// RelimyLocMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
						// Log.d("","gpsStatusYN  = " +gpsStatusYN);
						locationChangedCnt = locationChangedCnt+1;
						
						// ###################################
						// locationChangedCnt = 2;//테스트
						// if(timer_flag){
						// latitude = 35.24919250584;
						// longitude = 129.09620643642;
						// timer_flag = false;
						// }

						if (locationChangedCnt > 1) {
							gpsStatusYN = false;
						} else {
							gpsStatusYN = true;
						}
						// Log.d("","locationChangedCnt = "
						// +locationChangedCnt);
						System.out.println("Tunnel Update Locatoin locationChangedCnt= "+ locationChangedCnt);

						// #######################################
						// locationChangedCnt = 1;//테스트

						// ##############################3
						// if (!( locationChangedCnt >1 && locationChangedCnt
						// <=30 )) {//테스트 느낌표 지우기
						if ((locationChangedCnt > 2 && locationChangedCnt <= 30)) {
							System.out.println("Tunnel Update Locatoin locationChangedCnt check 1 ");
							if (locationChangedCnt % 2 == 0) {
								// if(locationChangedCnt == 11 ||
								// locationChangedCnt == 12){
								// firstFlag = true;
								// dist = 0.0;
								// locationChangedCnt = 0;
								// longitude = 128.268424;
								// latitude = 36.105308;
								// setCurrentPosition(getDBSearch(latitude,
								// longitude));
								// }else{
								noLocationInfo();
								// }

							}
						} else {
							System.out.println("Tunnel Update Locatoin locationChangedCnt check out ");
							if (locationChangedCnt < 30) {
								setCurrentPosition(getDBSearch(latitude,longitude));
							}
						}

						// ***************************************************************

						Log.i("", TAG + " timerCalled!!!! ijung = "
								+ currentIjung);
						Log.i("", TAG + " timerCalled!!!! sisul = " + sisul);
						Parameters params = new Parameters(ONECLICK_CARGPS_INSERT);

						params.put("user_type", User.getUser_type());// 차량관리번호
						params.put("car_id", User.getPatcar_id());// 순찰차량 일련번호.
						if ("Y".equals(Common.getPrefString(contextActivity,"start_yn"))) {
							params.put("rpt_id", Common.getPrefString(contextActivity, "selectedRpt_id"));// 상황일지 일련번호
						}

						//암호화 해야함, 경도, 위도, 방향, 고도, 속도
						byte[] encryptBytes_lo = seed.encrypt(longitude+"",szKey);
						String encString_lo = new String(Base64.encodeBase64(encryptBytes_lo));
						byte[] encryptBytes_la = seed.encrypt(latitude+"",szKey);
						String encString_la = new String(Base64.encodeBase64(encryptBytes_la));
						byte[] encryptBytes_ag = seed.encrypt(angle+"",szKey);
						String encString_ag = new String(Base64.encodeBase64(encryptBytes_ag));
						byte[] encryptBytes_hg = seed.encrypt(altitude+"",szKey);
						String encString_hg = new String(Base64.encodeBase64(encryptBytes_hg));
						byte[] encryptBytes_sp = seed.encrypt(speed+"",szKey);
						String encString_sp = new String(Base64.encodeBase64(encryptBytes_sp));

						params.put("car_gps_lo", longitude + "");// 경도
						params.put("car_gps_la", latitude + "");// 위도
						try {
							altitude = Math.round(altitude * 100f) / 100f;
						} catch (NumberFormatException e) {
							Log.e("에러","예외");
							altitude = 0.0;
						} finally {
							params.put("car_gps_hg", altitude + "");// 고도
						}
						params.put("car_gps_sp", speed + "");// 속도
						try {
							angle = Math.round(angle * 1000000000d) / 1000000000d;
						} catch (NumberFormatException e) {
							Log.e("에러","예외");
							angle = 0.0;
						} finally {
							params.put("car_gps_ag", angle + "");// 이동각도
						}
						params.put("start_yn", start_yn + "");// 출동여부 (y or n)
						params.put("car_way", "" + ns_code);// 차량 노선 코드
						if (!"".equals(Common.nullCheck(rampid))) {
//							params.put("car_way_ramp", ("" + ns_name + "_"+ rampid + "Ramp_" + currentIjung).replace(" ", ""));// 램프 노선 코드
							params.put("car_way_ramp", (Common.nullCheck(rampid)+"").replace(" ", "").trim());// 램프 노선 코드
						} else {
						}

						params.put("car_direct", rpt_bhPkCode);// 차량 방향 pk코드
						if (null != gonggu && !gonggu.isEmpty()) {
							params.put("car_gonggu", gonggu.substring(0, 1)+ "");// 공구명
						} else {
							params.put("car_gonggu", gonggu + "");// 공구명
						}

						params.put("car_sisul", sisul + "");// IC,JCT
						params.put("car_ijung", currentIjung + "");// IC,JCT,휴게소,..및
																	// 시설물
						params.put("ok", patrolOKFlag + "");// 이상없은 상태

						// ############################################################################################################

						// Log.d(TAG, TAG + " nscode = " + ns_code);
						// Log.d(TAG, TAG + " nsname = " + ns_name);
						// Log.d(TAG ,TAG + " ijung = "+currentIjung);
						// Log.d(TAG ,TAG + " banghyang = "+banghyang);
						//
						// Log.d(TAG ,TAG + " latitude = "+latitude);
						// Log.d(TAG ,TAG + " longitude = "+longitude);
						// Log.d(TAG ,TAG + " altitude = "+altitude);
						// Log.d(TAG ,TAG + " speed = "+speed);
						// Log.d(TAG, TAG + " gonggu = " + gonggu);
						// Log.d(TAG, TAG + " car_sisul = " + sisul);
						// Log.d(TAG, TAG + " sendGpsFlag = " + sendGpsFlag);

						// USER_TYPE_CITIZEN 대국민이 아닐 경우.0003
						if (USER_TYPE_TOW.equals(User.getUser_type()) || USER_TYPE_PATROL.equals(User.getUser_type())) {
							if (latitude < 50) {
								new Action(ONECLICK_CARGPS_INSERT, params).execute("");
							}
						}
//						try {
//							Log.d("", TAG+ " setActivityViewEdit starttimer");
//							setActivityViewEdit(contextActivity);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
					}
					timerCnt++;
					// HTTP 통신

					// } else {
					// Log.i(TAG, TAG + "timerCalled!!!! Else");
					// }

				}
			};

			@Override
			public void run() {
				handler.post(runnable);
			}
		};
		timer = new Timer();
		timer.schedule(timertask, 1000, 2000);// 100MS 뒤시작, 1초 간격으로 호출.
	}

	public void stopTimer(Context context) {
		if (null != timer) {
			timer.cancel();
		}
	}

	@Override
	protected void onPause() {
		// stopGPS();
		// stopTimer(getApplicationContext());
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("", TAG + " onDestroy Called !!!");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.menu:
			DrawerLayout dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
			dlDrawer.openDrawer(Gravity.LEFT);
			break;
		case R.id.btnUserInfo:
			Intent userIntent = new Intent(getApplicationContext(), DialogUserInfoActivity.class);
			userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, userIntent, PendingIntent.FLAG_ONE_SHOT);
			try {
				pi.send();
			} catch (PendingIntent.CanceledException e) {
				Log.e("에러","예외");
			}
			break;

		default:
			break;
		}
	}

	public void sendStart() {
		sendGpsFlag = true;
		Common.setPrefString(contextActivity, "start_yn", "Y");
	}

	public void sendStop() {
		Common.setPrefString(contextActivity, "start_yn", "N");
		sendGpsFlag = false;
		timerCnt = 0;
		Log.i("", TAG + "timerCalled sendStop !!!!");
		Parameters params = new Parameters(ONECLICK_CARGPS_INSERT);

		params.put("user_type", User.getUser_type());// 차량관리번호

		params.put("car_id", User.getPatcar_id());// 순찰차량 일련번호.
		params.put("rpt_id",Common.getPrefString(contextActivity, "selectedRpt_id"));// 상황일지 일련번호.
		
		byte[] encryptBytes_lo = seed.encrypt(longitude+"",szKey);
		String encString_lo = new String(Base64.encodeBase64(encryptBytes_lo));
		byte[] encryptBytes_la = seed.encrypt(latitude+"",szKey);
		String encString_la = new String(Base64.encodeBase64(encryptBytes_la));
		byte[] encryptBytes_ag = seed.encrypt(angle+"",szKey);
		String encString_ag = new String(Base64.encodeBase64(encryptBytes_ag));
		byte[] encryptBytes_hg = seed.encrypt(altitude+"",szKey);
		String encString_hg = new String(Base64.encodeBase64(encryptBytes_hg));
		byte[] encryptBytes_sp = seed.encrypt(speed+"",szKey);
		String encString_sp = new String(Base64.encodeBase64(encryptBytes_sp));
		
		params.put("car_gps_lo", longitude + "");// 경도
		params.put("car_gps_la", latitude + "");// 위도
		try {
			altitude = Math.round(altitude * 100f) / 100f;
		} catch (NumberFormatException e) {
			Log.e("에러","예외");
		} finally {
			params.put("car_gps_hg", altitude + "");// 고도
		}
		params.put("car_gps_sp", speed + "");// 속도
		try {
			angle = Math.round(angle * 1000000000d) / 1000000000d;
		} catch (NumberFormatException e) {
			Log.e("에러","예외");
		} finally {
			params.put("car_gps_ag", angle + "");// 이동각도
		}
		params.put("start_yn", start_yn + "");// 출동여부 (y or n)
		params.put("car_way", "" + ns_code);// 차량 노선 코드
		params.put("car_direct", "");// 차량 방향 pk코드
		if (null != gonggu && !gonggu.isEmpty()) {
			params.put("car_gonggu", gonggu.substring(0, 1) + "");// 공구명
		} else {
			params.put("car_gonggu", gonggu + "");// 공구명
		}
		params.put("car_sisul", sisul + "");// IC,JCT
		params.put("car_ijung", currentIjung + "");// IC,JCT,휴게소,..및 시설물
		params.put("ok", patrolOKFlag + "");// 이상없은 상태

		params.put("STOP", "STOP");

		new Action(ONECLICK_CARGPS_INSERT, params).execute("");

		Parameters params_end = new Parameters(ONECLICK_WORKCOMPLETE_UPDATE);
		// if (User.getUser_type().equals("0001")) {
		params_end.put("car_id", User.getPatcar_id());
		// }
		params_end.put("user_type", User.getUser_type());
		params_end.put("rpt_id",
				Common.getPrefString(contextActivity, "selectedRpt_id"));
		params_end.put("reg_type", rpt_reg_type);

		new Action(ONECLICK_WORKCOMPLETE_UPDATE, params_end).execute("");
		try {
			setActivityViewEdit(contextActivity);
		} catch (IOException e) {
			Log.e("예외","예외발생");
		}
	}

	public List<Image> ReadSDCard() {
		Image currentItem = null;
		List<Image> trnImageList = null;

		trnImageList = new ArrayList<Image>();

		File fileDir = new File(Common.FILE_DIR);

		if (!fileDir.exists())
			fileDir.mkdir();

		File[] files = fileDir.listFiles();
		totalFileSize = 0;
		for (int i = 0; i < files.length; i++) {

			currentItem = new Image();
			File file = files[i];

			String fileName[] = Common.split(file.getPath().toString(), "/");
			String fileType[] = Common.split(file.getPath().toString(), ".");

			currentItem.setFilePath(file.getPath().toString());
			currentItem.setFileName(fileName[fileName.length - 1]);
			currentItem.setFileType(fileType[fileType.length - 1]);
			// currentItem.setFileSize(file.length());

			Log.d("saveFile", "fileName : " + fileName[fileName.length - 1]);

			// 파일리스트 총 용량 받아오기 소수점 1자리, 반올림, MB 표현

			double nByte = file.length();
			double mByte = 0;
			double tvMByte = 0;
			String tvTextSize = "";

			mByte = Math.floor(nByte * 100 / (1024 * 1024));
			tvTextSize = String.valueOf(mByte / 100);

			tvMByte = Math.round(mByte / 10);
			tvMByte = tvMByte / 10;
			totalFileSize = totalFileSize + tvMByte;

			// totalFileListSize.setText(totalFileSize+"MB");

			trnImageList.add(currentItem);

		}// end for

		// shlee 2013-11-20 추가
		// String num = String.format("%.1f" , totalFileSize);
		// totalFileListSize.setText(num+"MB");

		// Log.d("", "totalFileSize = " + totalFileSize);
		// Log.d("", "totalFileSize num = " + num);

		Log.d("", "ReadSdCard list size = " + trnImageList.size());
		return trnImageList;
	}

	/**
	 * 접보지사 초기화+접보지사 재 설정.
	 */
	public void settingJeopboJisa() {
		if (null != User.getRcv_yn_list()) {
			// 초기화
			db.updateBBBsCodeInit();
			for (int i = 0; i < User.getRcv_yn_list().size(); i++) {
				if (User.getRcv_yn_list().get(i).equals("Y")) {
					Log.d("", TAG + "items splig1  = "
							+ User.getRcv_bs_list().get(i));
					if (User.getRcv_bs_list() != null) {
						String[] items = User.getRcv_bs_list().get(i)
								.split("[|]");
						for (int j = 0; j < items.length; j++) {
							Log.d("", TAG + "items splig2  = " + items[j]);
							db.updateBBBsCode(items[j], "Y");
						}
					}
				}
			}
		}
	}
}
