package com.ex.situationmanager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Phaser;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.xmlpull.v1.XmlPullParserException;

import com.ex.situationmanager.dto.Userinfo;
import com.ex.situationmanager.encrypt.SeedCipher;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.DBAdapter;
import com.ex.situationmanager.util.CustomMultiPartEntity.ProgressListener;
import com.ex.situationmanager.util.Image;

import android.Manifest;
import android.R.xml;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

@SuppressLint("NewApi")
public abstract class BaseActivity extends Activity implements OnClickListener {

	private String TAG = "BaseActivity";
	public static String VIEW_TAG = "";
	public static String VIEW_PATROL = "PatrolMainActivity";
	public static String VIEW_TOW = "TowMainActivity";
    public static String VIEW_INNEREMPLOYEE ="InnerEmployActivity";
	public static boolean gpsDialogFlag = true;
	SeedCipher seed = new SeedCipher();
	public static byte[] szKey = {(byte) 0x88, (byte) 0xE3, (byte) 0x44,
			(byte) 0x8F, (byte) 0x28, (byte) 0x32, (byte) 0xFE, (byte) 0xF1,
			(byte) 0xF9, (byte) 0xF3, (byte) 0xF1, (byte) 0x37, (byte) 0xFF,
			(byte) 0xA4, (byte) 0x05, (byte) 0x29};

	// ????????????
	public static String ONECLICK_NOTICE_SELECT = "ONECLICK_NOTICE_SELECT";

	// ?????????????????????
	public static String ONECLICK_GETUSERINFO_SELECT = "ONECLICK_GETUSERINFO_SELECT";

	public static String ONECLICK_GET_USERINFO_SELECT2="ONECLICK_GET_USERINFO_SELECT2";
	// gps??????+???????????? ??????.
	public static String ONECLICK_CARGPS_INSERT = "ONECLICK_CARGPS_INSERT";
	// ??????,?????? ????????????.
	public static String ONECLICK_WORKCOMPLETE_UPDATE = "ONECLICK_WORKCOMPLETE_UPDATE";
	// ?????? ?????? ?????? ??????
	public static String ONECLICK_PATROLRCEPTINFO_SELECT = "ONECLICK_PATROLRCEPTINFO_SELECT";
	// ?????? ?????? ?????? ??????
	public static String ONECLICK_ACDNTACT_INSERT = "ONECLICK_ACDNTACT_INSERT";
	// ?????? ???????????? ?????? ??????.
	public static String ONECLICK_GETACDNTACTINFO_SELECT = "ONECLICK_GETACDNTACTINFO_SELECT";
	// ?????? ?????? ??????
	public static String ONECLICK_TOWRCEPTINFO_SELECT = "ONECLICK_TOWRCEPTINFO_SELECT";
	// ????????????.
	public static String ONECLICK_FILE_SEND = "ONECLICK_FILE_SEND";

	public static String GET_JISA_LIST = "GET_JISA_LIST";

	public static String GET_CCTV_URL_LIST = "GET_CCTV_URL_LIST";

	public static String  UPDATE_JUBBO_CONTENT_INNEREMPLELOY="UPDATE_JUBBO_CONTENT_INNEREMPLELOY";

	// ??? ???????????? ????????????.
	public static String ONECLICK_MAINRCVBS_UPDATE = "ONECLICK_MAINRCVBS_UPDATE";
	// ???????????? ?????? ?????? ????????????.
	public static String ONECLICK_RCVJISALIST_UPDATE = "ONECLICK_RCVJISALIST_UPDATE";
	// ????????? ?????? ??????.
	public static String ONECLICK_CITIZENRCEPIT_INSERT = "ONECLICK_CITIZENRCEPIT_INSERT";
	// ????????? ???????????? ??????.
	public static String ONECLICK_GETPATROLTELNO_SELECT = "ONECLICK_GETPATROLTELNO_SELECT";

	//???????????? ????????? ??????????????? primitive
	public static String ONECLICK_PATROLCONFRIM_UPDATE = "ONECLICK_PATROLCONFRIM_UPDATE";

	//?????? or ?????? ????????? ???????????? ???????????? ?????? ?????? ?????? ????????????  ??????DB
	public static String ONECLICK_MOVE_DIRECTION_SELECT = "ONECLICK_MOVE_DIRECTION_SELECT";

	//?????? or ?????? ????????? ???????????? ???????????? ?????? ?????? ?????? ?????? ?????? ??????DB
	public static String ONECLICK_MOVE_DIRECTION_INSERT = "ONECLICK_MOVE_DIRECTION_INSERT";

	//?????? or ?????? ???????????? ???????????? ?????? /??????DB
	public static String ONECLICK_TOW_ACTION_SELECT = "ONECLICK_TOW_ACTION_SELECT";

	//?????? or ?????? ???????????? ???????????? ?????? /??????DB
	public static String ONECLICK_TOW_ACTION_INSERT = "ONECLICK_TOW_ACTION_INSERT";

	//???????????? ???????????? ?????? ???????????? ?????? ????????????
	public static String ONECLICK_EMPLOYEE_RECEPTINFO_SELECT ="ONECLICK_EMPLOYEE_RECEPTINFO_SELECT";

	//???????????? ???????????? ?????? setting ???
	public static String ONECLICK_EMPLOYEE_GET_INITDATA_SELECT="ONECLICK_EMPLOYEE_GET_INITDATA_SELECT";

	public static String GET_JUBBO_LIST_SELECT = "GET_JUBBO_LIST_SELECT";

	public static String INSERT_JUBBO_CONTENT="INSERT_JUBBO_CONTENT";

	// 2020.12 ?????????????????????
	public static String ONECLICK_GET_TUNNELNAME = "ONECLICK_GET_TUNNELNAME";

	// 2020.12 ??????pdf??????URL ????????????
	public static String ONECLICK_TUNNEL_DOWNLOAD = "ONECLICK_TUNNEL_DOWNLOAD";

	//public static String URL_NOTICE = "http://192.168.1.24:8080/notice.jsp";	//local ????????? ??????//192.168.1.5 8080
	//public static String URL_NOTICE = "http://192.168.10.3:8080/notice.jsp";	//local ????????? ??????//????????? IP
	//--public static String URL_NOTICE = "http://oneclickapp.ex.co.kr:5000/notice.jsp";    //?????? DB ??????//?????? ?????? ????????????(?????????)
//	public static String URL_NOTICE = "http://oneclickmobile.ex.co.kr:8080/notice.jsp";	//??????
	public static String URL_NOTICE = "https://oneclickapp.ex.co.kr:9443/notice.jsp";
	//public static String URL_NOTICE ="https://mvote.ex.co.kr:4430/mvote/index_action_ar.jsp?encId=123&encPwd=123&key=ar";
	//public static String URL_SENDGPS = "http://192.168.1.24:8080/proxy.jsp";	// local ?????? ????????? ??????
	//public static String URL_SENDGPS = "http://192.168.10.3:8080/proxy.jsp";	// ????????? IP
	//--public static String URL_SENDGPS = "http://oneclickapp.ex.co.kr:5000/proxy.jsp";    // ?????? DB ??????//?????? ?????? ????????????(?????????)
	public static String URL_SENDGPS = "https://oneclickapp.ex.co.kr:9443/proxy.jsp";    // SSL ??????
	//ssl ?????? ??????

//	public static String URL_SENDGPS = "http://oneclickmobile.ex.co.kr:8080/proxy.jsp";	// ????????????

	//???????????? ????????????
	//public static String URL_TEST = "http://oneclickapp.ex.co.kr:5000/proxy.jsp";//???????????????


	public Uri mUriSet;
	Handler fileHandler;

	final String TRAINSTR = "??????????????????";
	final String TRAIN = "train";//??????(practice), ????????????(train) ,????????????(real) real

	final String PRACTICESTR = "????????????";
	final String PRACTICE = "practice";//??????(practice), ????????????(train) ,????????????(real) real

	Common common = null;
	DBAdapter db = new DBAdapter();

	private static final int GPS_INTENT = 6;
	// ????????? ??????
	public static String USER_TYPE_PATROL = "0001";// ?????????
	public static String USER_TYPE_TOW = "0002";// ?????????
	public static String USER_TYPE_CITIZEN = "0003";// ?????????
	public static String USER_TYPE_INNEREMPLOYEE = "0004";// ????????? ????????????
	public static String USER_TYPE = USER_TYPE_PATROL;

	// ??? ??????????????????
	public static String TowJeopBoJisaCode = "";
	public static String TowJeppBoJisaName = "";

	// ????????? ???????????? ??????.
// public static String selectedRpt_id = "";
//	public static String rpt_local_way = "";
//	public static String rpt_start_km = "";
	public static String rpt_bscode = "";
	public static String rpt_bhPkCode = "";
	public static String rpt_reg_type = "";
	public static String rpt_latitude = "";
	public static String rpt_longitude = "";

	public static Activity contextActivity;

	String nowprovider = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.println(Log.ASSERT,TAG,"BASEACTIVITY ????????? ??????:"+ TAG);
	}


	@Override
	protected void onResume() {
		super.onResume();
		gpsDialogFlag = true;
	}

	public static String baseResponse = "";
	// ?????? ??????
	public class Action extends AsyncTask<String, Void, XMLData> {
		// --------------------------------------------------------------------------------------------
		// #region ???????????? ?????? ??????
		// ?????? ?????? Progressbar
		ProgressDialog progressDialog;

		String primitive = "";
		Parameters params = null;

		XMLData returnData = null;


		@Override
		protected void onPreExecute() {
		    try{
                if (primitive.equals(ONECLICK_GETUSERINFO_SELECT)) {
                    progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);
                } else if (primitive.equals(ONECLICK_CITIZENRCEPIT_INSERT)) {
                    progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);
                } else if (primitive.equals(ONECLICK_GETPATROLTELNO_SELECT)) {
//				progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);
                } else if (primitive.equals(ONECLICK_NOTICE_SELECT)) {
                    progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);
                } else if (primitive.equals(ONECLICK_TOW_ACTION_INSERT)) {
                    progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);
                } else if (primitive.equals(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT)) {
                    /*		progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);//GET_CCTV_URL_LIST*/
					try{
						progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);//GET_CCTV_URL_LIST
					}catch (Exception e){
						Log.e("??????","????????????");
						if(progressDialog!=null){
							progressDialog.dismiss();
						}
					}

                } else if (primitive.equals(GET_CCTV_URL_LIST)) {//GET_JISA_LIST
                    progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);//GET_CCTV_URL_LIST
                } else if (primitive.equals(GET_JISA_LIST)) {//GET_JISA_LIST
                    progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);//GET_CCTV_URL_LIST
                } else if (primitive.equals(GET_JUBBO_LIST_SELECT)) {//GET_JISA_LIST
                    progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);//GET_CCTV_URL_LIST
                } else if (primitive.equals(ONECLICK_GET_TUNNELNAME)) { // 2020.12 ??????
					progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);//ONECLICK_GET_TUNNELNAME)
				} else if (primitive.equals(ONECLICK_TUNNEL_DOWNLOAD)) { // 2020.12 ??????
					progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);//ONECLICK_TUNNEL_DOWNLOAD
				} else if (primitive.equals(ONECLICK_GET_USERINFO_SELECT2)) { // ?????????
					progressDialog = ProgressDialog.show(BaseActivity.this, "", "?????????...", true);
				}
            }catch (NullPointerException e){
		        Log.e("??????","??????");
            }finally {
				;;
			}



			super.onPreExecute();
		}

		// primitive ??? ?????? URL??? ???????????????.
		public Action(String primitive, Parameters params) {
			Log.println(Log.ASSERT,TAG,"?????????:"+primitive);
			this.primitive = primitive;
			this.params = params;

		}

		@Override
		protected XMLData doInBackground(String... arg0) {
			Log.println(Log.ASSERT,TAG,"doInBackground primitive ??????"+primitive);
			Log.println(Log.ASSERT,TAG,"doInBackground primitive ??????"+primitive);
			HttpURLConnection conn = null;
			XMLData xmlData = null;

			OutputStream os = null;
			InputStream is = null;
			ByteArrayOutputStream baos = null;
			try {
				StringBuffer body = new StringBuffer();


			//	URL_SENDGPS=URLEncoder.encode(URL_SENDGPS,"UTF-8");//????????? ?????? ???????????? ?????? ????????? ??????


				//????????? ???????????? ??????
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
				} else if (ONECLICK_CITIZENRCEPIT_INSERT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_PATROLCONFRIM_UPDATE.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_NOTICE_SELECT.equals(primitive)) {
					body.append(URL_NOTICE);
				} else if (ONECLICK_MOVE_DIRECTION_SELECT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_MOVE_DIRECTION_INSERT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_TOW_ACTION_INSERT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				} else if (ONECLICK_TOW_ACTION_SELECT.equals(primitive)) {
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				}else if (ONECLICK_EMPLOYEE_RECEPTINFO_SELECT.equals(primitive)) {
					Log.println(Log.ASSERT,"ActionInnerList","3");
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
					//listFlag = false;
				} else if(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT.equals(primitive)){//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT
					Log.println(Log.ASSERT,"ActionInnerList","1001??????????????? ????????? primitive:"+primitive);
					Log.println(Log.ASSERT,"ActionInnerList","4");
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
					//listFlag = false;
				} else if(GET_JISA_LIST.equals(primitive)){
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				//	listFlag = false;
				} else if(GET_JUBBO_LIST_SELECT.equals(primitive)){
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				//	listFlag = false;//GET_CCTV_URL_LIST
				} else if(GET_CCTV_URL_LIST.equals(primitive)){//GET_CCTV_URL_LIST
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				//	listFlag = false;//GET_CCTV_URL_LIST
				}else if(ONECLICK_GET_USERINFO_SELECT2.equals(primitive)){//GET_CCTV_URL_LIST
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
					//	listFlag = false;//GET_CCTV_URL_LIST
				}else if(ONECLICK_GET_TUNNELNAME.equals(primitive)) {// 2020.12 ?????????
					Log.i("ONECLICK_GET_TUNNELNAME", "base_doInBackground");
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				}else if(ONECLICK_TUNNEL_DOWNLOAD.equals(primitive)){// 2020.12 ??????pdf????????????
					Log.i("TUNNEL_DOWNLOAD", "base_doInBackground");
					body.append(URL_SENDGPS);
					body.append("?");
					body.append(params.toString());
				}
				Log.println(Log.ASSERT,TAG,"doInBackground ??????123 ??????:"+body.toString());

				//???????????? ???????????? ??????

//				String parara = "";
//				parara = body.toString();
//				String hp_no = params.get("hp_no");
//				String app_name = params.get("app_name");
//				String hash_code = params.get("hash_code");
//
//				parara += "hp_no="+URLEncoder.encode(hp_no, "UTF-8")+"&app_name="+URLEncoder.encode(app_name,"UTF-8")+"&hash_code="+URLEncoder.encode(hash_code,"UTF-8");

//				String deUrl = body.toString();
//				String encodeurl = URLEncoder.encode(deUrl, "UTF-8");
//				deUrl = encodeurl;
//				Log.i(TAG, "deUrl : = " + deUrl);
//
//				URL url = new URL(
//					new String(
//							"http://192.168.0.38:8080/proxy.jsp?primitive="+URLEncoder.encode(ONECLICK_GETUSERINFO_SELECT,"UTF-8")+"hp_no="+URLEncoder.encode(hp_no, "UTF-8")+"&app_name="+URLEncoder.encode(app_name,"UTF-8")+"&hash_code="+URLEncoder.encode(hash_code,"UTF-8")
//					)
//				);

				//URL url = new URL(new String(Common.nullTrim(body.toString()).getBytes("UTF-8"), "UTF-8"));//?????? ????????????

				//202009_????????????
//				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//					public X509Certificate[] getAcceptedIssuers() {
//						return null;
//					}
//					public void checkClientTrusted(X509Certificate[] certs, String authType) {
//					public void checkClientTrusted(X509Certificate[] certs, String authType) {
//					}
//					public void checkServerTrusted(X509Certificate[] certs, String authType) {
//					}
//				} };

				String certificateString = "-----BEGIN CERTIFICATE-----\n" +
						"MIIGczCCBVugAwIBAgIQCkXGPBtsAP24oUKfP+JruTANBgkqhkiG9w0BAQsFADBc\n" +
						"MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
						"d3cuZGlnaWNlcnQuY29tMRswGQYDVQQDExJUaGF3dGUgUlNBIENBIDIwMTgwHhcN\n" +
						"MjExMjAxMDAwMDAwWhcNMjIxMjMwMjM1OTU5WjB6MQswCQYDVQQGEwJLUjEZMBcG\n" +
						"A1UECBMQR3llb25nc2FuZ2J1ay1kbzEUMBIGA1UEBxMLR2ltY2hlb24tc2kxJTAj\n" +
						"BgNVBAoTHEtvcmVhIEV4cHJlc3N3YXkgQ29ycG9yYXRpb24xEzARBgNVBAMMCiou\n" +
						"ZXguY28ua3IwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDmTJ7/+vgQ\n" +
						"WgJO+Gf7CYlw9zDsScV2d+jsYbsIjf5X+hoNERlJjpIsUHo+LAvKE3lXEDlfvZ+e\n" +
						"k0mYBkrvRsFyng7z2siAs6b6IFdBskTl4kaSa9epS4iPPJefY6fw8XbqWAxqN2/Z\n" +
						"PeTv5/FT2o5Xki7JD6gAHfLwwczfF+IJ5y6pB+7QBgWHTlAzep7pWY+TuI7XInhr\n" +
						"wSaFk38xdHg7KKHSRIPBFZJ8m1KzkU1M1/nWsFo4jqr6MEjB7NQ5qvUWDArgzXeS\n" +
						"rURqDr7xHAXecz9Z9Fw79GX/d5l4scdmhGUu/7i9mX66pE95Hiv42jW75Tef0oJZ\n" +
						"B/45MRffs49BAgMBAAGjggMRMIIDDTAfBgNVHSMEGDAWgBSjyF5lVOUweMEF6gcK\n" +
						"alnMuf7eWjAdBgNVHQ4EFgQUFjvbBdz/p3nv1Q80e/e79+N5msowHwYDVR0RBBgw\n" +
						"FoIKKi5leC5jby5rcoIIZXguY28ua3IwDgYDVR0PAQH/BAQDAgWgMB0GA1UdJQQW\n" +
						"MBQGCCsGAQUFBwMBBggrBgEFBQcDAjA6BgNVHR8EMzAxMC+gLaArhilodHRwOi8v\n" +
						"Y2RwLnRoYXd0ZS5jb20vVGhhd3RlUlNBQ0EyMDE4LmNybDA+BgNVHSAENzA1MDMG\n" +
						"BmeBDAECAjApMCcGCCsGAQUFBwIBFhtodHRwOi8vd3d3LmRpZ2ljZXJ0LmNvbS9D\n" +
						"UFMwbwYIKwYBBQUHAQEEYzBhMCQGCCsGAQUFBzABhhhodHRwOi8vc3RhdHVzLnRo\n" +
						"YXd0ZS5jb20wOQYIKwYBBQUHMAKGLWh0dHA6Ly9jYWNlcnRzLnRoYXd0ZS5jb20v\n" +
						"VGhhd3RlUlNBQ0EyMDE4LmNydDAMBgNVHRMBAf8EAjAAMIIBfgYKKwYBBAHWeQIE\n" +
						"AgSCAW4EggFqAWgAdQApeb7wnjk5IfBWc59jpXflvld9nGAK+PlNXSZcJV3HhAAA\n" +
						"AX1zinDjAAAEAwBGMEQCIEbQTcwgYYSMKrcPF7TajkKYMca2RSbC6X6USgU/6Lo8\n" +
						"AiA6E/fOgkW9CWqXrsiF0wgZdUq/vHfC3zPyo9tVfLdGKwB2AEHIyrHfIkZKEMah\n" +
						"OglCh15OMYsbA+vrS8do8JBilgb2AAABfXOKcOsAAAQDAEcwRQIhAI6Vl0NhDdY9\n" +
						"2VGstJIhOSIGZPk8tFKWyIGunnaFpxH/AiBOw9bgt+z6jmLeHYYQhj365+rJYTWJ\n" +
						"rqugE0kTfhTVdQB3AN+lXqtogk8fbK3uuF9OPlrqzaISpGpejjsSwCBEXCpzAAAB\n" +
						"fXOKcTEAAAQDAEgwRgIhAIZUP1DXclUZxI3Zx+dRyi4MTSw96kzAeTRv61AirR1Z\n" +
						"AiEA9b5/+CW47kZgjccYHvf1JZleclwUmuOgf9boRe/Bn+kwDQYJKoZIhvcNAQEL\n" +
						"BQADggEBAMkpM9vpP+rqK4FdT8q5Wul1/VPZIhYYcDNixmquLqhHUSx6iYY/sqh2\n" +
						"t5dpIQfrkPiU/VkdbzKacF5Y/kJPTRVMYktxdF4UOZWmGBwDYOKsZcyzPf+TIgef\n" +
						"KLU+MZQmDlcZp118NEfkyjhC3SA+LW33rxcp7kXMxYkph9Xi1FCjvVRcOTWtC5MF\n" +
						"RWMk2KtcBc9cWfX0umTmoFqrTxH3MJkZM7Rskr60s3EDGZaTcLW3aL/hv92+SZc7\n" +
						"gQnCYoKn0Nb1ysOUWZXOJ/BTLGOD69wDuGLxIlFVqCGZBoN2eTTQwwdtOpON7tzm\n" +
						"h0yrP24SpIrLvhfwuAomS1AoHstK0eA=\n" +
						"-----END CERTIFICATE-----";

				CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
				ByteArrayInputStream derInputStream = new ByteArrayInputStream(certificateString.getBytes());
				X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
				String alias = cert.getSubjectX500Principal().getName();
				// KeyStore.setCertificateEntry()??? setting ????????? ?????? ?????? ????????????.

				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				trustStore.setCertificateEntry(alias, cert);

				String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
				tmf.init(trustStore);

				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, tmf.getTrustManagers(), new SecureRandom());

				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				Log.i("body.toString()",body.toString());


//				URL url;
//				if(ONECLICK_TUNNEL_DOWNLOAD.equals(primitive)){
//					 url = new URL(new String(Common.nullTrim(body.toString()).getBytes("utf-8"), "Cp1252"));//????????? test
//				} else {
//					url = new URL(new String(Common.nullTrim(body.toString()).getBytes("EUC-KR"), "Cp1252"));//????????? test
//				}

				URL url = new URL(new String(Common.nullTrim(body.toString()).getBytes("EUC-KR"), "Cp1252"));//?????????
//				URL url = new URL(Common.nullTrim(body.toString()));//?????????
//				url = new URL(new String(Common.nullTrim(body.toString()).getBytes("utf-8"), "Cp1252"));//????????? test

				//copyInputStreamToOutputStream(in, System.out);
				Log.i(TAG, "URL : = " + body.toString());
				System.out.println("EUC-KR = " + url.toString());

				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setRequestProperty("Cache-Control", "no-cache");
				// conn.setDoOutput(true);
				conn.setDoInput(true);

				int responseCode = conn.getResponseCode();
				Log.d(TAG, TAG + " ACTION responsecode  " + responseCode + "----" + conn.getResponseMessage());
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
					//???????????? ???????????? ?????????
					if (primitive.equals(ONECLICK_NOTICE_SELECT)) {
						response = new String(byteData, "utf-8").toString();
					}

					Log.d("", "responseData  = " + response);
					if (response == null || response.equals("")) {
						Log.e(TAG + "Response is NULL!! ", TAG + "Response is NULL!!");
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
						Log.println(Log.ASSERT,TAG,"doInBackground::: Response ??????:"+response);

						if(ONECLICK_TUNNEL_DOWNLOAD.equals(primitive)){
							baseResponse = response;
							System.out.println("pdfdownload url = " + response);
//
						}else{
							xmlData = new XMLData(response);
							returnData = xmlData;
							String ret = xmlData.get("result");
							Log.d("", TAG + " ret check = " + ret);

							if ("1000".equals(ret)) {
								if (ret == null) {
									// throw new IOException();
								} else {
									String retMsg = xmlData.get("resultMessage");
									// throw new IOException();
								}
							} else if (primitive.equals(ONECLICK_CARGPS_INSERT)) {
								if ("START".equals(Common.nullCheck(params.get("STOP")))) {
									Log.d("", TAG + "9999 onActionPost  1 primitive = " + primitive);
//								onActionPost(primitive, xmlData, null);
									Log.d("", TAG + " setActivityViewEdit 1");
									setActivityViewEdit(contextActivity);
								}
							}
						}


					} catch (XmlPullParserException e) {
						Log.e("??????","??????");
						e.printStackTrace();
					}
				} else {
					Toast.makeText(contextActivity, "?????? ??????1", Toast.LENGTH_SHORT).show();
				}

			} catch (IOException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException e) {
				Log.e("??????","??????");
				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				Log.e("??????","??????");
//			} catch (KeyManagementException e) {
//				Log.e("??????","??????");
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						Log.e("??????","??????");
					}
				}

				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						Log.e("??????","??????");
					}
				}

				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
						Log.e("??????","??????");
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
			Log.println(Log.ASSERT,TAG,"onPostExecute");//ONECLICK_EMPLOYEE_GET_INITDATA_SELECT

			try {
				if("ONECLICK_TUNNEL_DOWNLOAD".equals(primitive)){
					Log.println(Log.ASSERT,TAG,"++++++++"+primitive);
					if(progressDialog !=null){
						try{
							progressDialog.dismiss();
						}catch (NullPointerException e){
							Log.e("??????","??????");
						}

					}
					onActionPost(primitive, returnData, null);
				}else{
					if (primitive.equals(ONECLICK_GETUSERINFO_SELECT) ||
							primitive.equals(ONECLICK_CITIZENRCEPIT_INSERT) ||
							primitive.equals(ONECLICK_NOTICE_SELECT) ||
							primitive.equals(ONECLICK_TOW_ACTION_INSERT) ||
							primitive.equals(ONECLICK_EMPLOYEE_GET_INITDATA_SELECT) ||
							primitive.equals(GET_JISA_LIST)||
							primitive.equals(GET_JUBBO_LIST_SELECT)||
							primitive.equals(GET_CCTV_URL_LIST)||
							primitive.equals(ONECLICK_GET_TUNNELNAME) ||
							primitive.equals(ONECLICK_GET_USERINFO_SELECT2)
					) {//GET_CCTV_URL_LIST
						Log.println(Log.ASSERT,TAG,"++++++++"+primitive);
						if(progressDialog !=null){
							try{
								progressDialog.dismiss();
							}catch (NullPointerException e){
								Log.e("??????","??????");
							}

						}

					}

					onActionPost(primitive, returnData, null);
				}


			} catch (IOException e) {
				Log.e("??????","??????");
				try {
					if (!ONECLICK_CARGPS_INSERT.equals(primitive)) {
						onActionPost(primitive, returnData, e);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case GPS_INTENT:
				Log.d("", "GPS_INTENTGPS_INTENTGPS_INTENT");
				stopGPS();
				startMyGps();
//			stopTimer(contextActivity);
//			startTimer();
				break;

			default:
				break;
		}


	}

	protected abstract void setActivityViewEdit(Activity activity) throws IOException;

	protected abstract void onActionPost(String primitive, XMLData result, Exception e) throws IOException;



	/************************************************************************
	 * ?????? ?????? ?????? - Progressbar, AsyncTask??? ???????????? ?????? ?????? - ???????????? ??? ???????????? ?????? ??????
	 ************************************************************************/
	String sendCheckFile;
	String transData = "Y";
	com.ex.situationmanager.util.CustomMultiPartEntity multipartContent;

	public int iPercent;

	public class DoComplecatedJob extends AsyncTask<String, Integer, Long> {
		long totalSize;
		long totalSizeKB;
		int fileCount = 0;
		// ?????? ?????? Progressbar
		ProgressDialog progressDialog;
		Dialog mDialog = null;
		ProgressBar pb = null;
		ImageButton ibtnProgressCancel;

		public TextView tv;
		public TextView tvTrans;
		public TextView tvTot;
		public TextView tvPromptProgress;
		public int iProgress;
		public List<Image> mFileList;
		Parameters params;
		Activity mActivity;

		int kbSize = 0;

		public DoComplecatedJob() {
			super();
		}

		public DoComplecatedJob(List<Image> mList, Parameters params, Activity mActivity) {
			super();
			mFileList = mList;
			this.params = params;
			this.mActivity = mActivity;
			fileCount = mList.size();
		}

		@Override
		protected void onPreExecute() {
			// --------------------------------------------------------------------------------------------
			// #region ???????????? ??????

			progressDialog = new ProgressDialog(mActivity);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("?????? ?????? ???...");
			progressDialog.setCancelable(true);

			progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????",
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
			mDialog.getWindow().setGravity(Gravity.TOP | Gravity.CENTER);

			mDialog.setContentView(R.layout.cust_progressbar);
			mDialog.setCancelable(false);

//			for (int i = 0; i < ReadSdCardSize(); i++) {
//				long aa = mFileList.get(i).getFileSize();
//			}

			pb = (ProgressBar) mDialog.findViewById(R.id.pbProgress);
			tv = (TextView) mDialog.findViewById(R.id.tvPercentProgress);
			tvTrans = (TextView) mDialog.findViewById(R.id.tvProgressTrans);
			tvTot = (TextView) mDialog.findViewById(R.id.tvProgressTot);
			tvPromptProgress = (TextView) mDialog.findViewById(R.id.tvPromptProgress);
			if (!Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
				tvPromptProgress.setText("????????? ????????? ?????????.");
			}
			ibtnProgressCancel = (ImageButton) mDialog.findViewById(R.id.ibtnProgressCancel);

			ibtnProgressCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					transData = "N";
				}
			});

			// ?????? ?????? ?????????
			tv.setText("0 %");
			tvTrans.setText("0 KB");
			tvTot.setText("0 KB ");

			// ?????? ?????? ?????????
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
			// #region ??? ???????????? ??????, ???????????? ??????????????? ??? ???????????? ?????? ??????

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost;

			Log.d("", "################### DoComplecatedJob doInBackground start");
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(Configuration.FILE_UPLOAD_PATH);
				sb.append("?");
				sb.append(params.toString());
				Log.i("FILE_UPLOAD_PATH", "URL = 1" + sb.toString());
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

				// ?????? ?????? ??? ??????
				//Log.d("", "before send ReadSdCardSize() size = "+ ReadSdCardSize());
				for (int i = 0; i < ReadSdCardSize(); i++) {
					Image image = mFileList.get(i);
					absolutePath = Configuration.directoryName + "/" + image.getFileName().toString();
					Log.d("", "multipartContent FILENAME = " + absolutePath);
					multipartContent.addPart("uploaded_file" + i, new FileBody(new File(absolutePath)));
				}

				totalSize = multipartContent.getContentLength();
				totalSizeKB = totalSize / 1024;
				// pb.setMax(Integer.parseInt(totalSize + ""));

				// Send it
				multipartContent.addPart("renamePrefix", new StringBody(""));
				//multipartContent.addPart("isTest", new StringBody("true"));
				if (!Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
					multipartContent.addPart("rpt_id", new StringBody(params.get("rpt_id"), Charset.forName("UTF-8")));
					// bscode; // ??????, ??????
					multipartContent.addPart("bscode", new StringBody(params.get("bscode"), Charset.forName("UTF-8")));
					// patcar_id; // ???????????????
					if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
						multipartContent.addPart("patcar_id", new StringBody(params.get("patcar_id"), Charset.forName("UTF-8")));
					}
				} else {
					multipartContent.addPart("rpt_id", new StringBody(params.get("rpt_id"), Charset.forName("UTF-8")));
					multipartContent.addPart("bscode", new StringBody("", Charset.forName("UTF-8")));
					multipartContent.addPart("patcar_id", new StringBody(Configuration.User.getPatcar_id(), Charset.forName("UTF-8")));
				}

//				10066370
//				10691991
				//????????? test
//				multipartContent.addPart("rpt_id",new StringBody("10691991", Charset.forName("UTF-8")));
				//

				multipartContent.addPart("nscode", new StringBody(params.get("nscode"), Charset.forName("UTF-8")));
				multipartContent.addPart("bhcode", new StringBody(params.get("bhcode"), Charset.forName("UTF-8")));
				multipartContent.addPart("ijung", new StringBody(params.get("ijung"), Charset.forName("UTF-8")));
				// user_type; // ?????? ??????
				multipartContent.addPart("user_type", new StringBody(Configuration.User.getUser_type(), Charset.forName("UTF-8")));

				httpPost.setEntity(multipartContent);

				// multipartContent.addPart("renamePrefix", new

				// ????????? ?????? ??????
				HttpResponse response = httpClient.execute(httpPost, httpContext);
				String serverResponse = EntityUtils.toString(response.getEntity());

				sendCheckFile = serverResponse;
				Log.d("", "*****************************************************");
				Log.d("", "" + sendCheckFile);
				Log.d("", "" + sendCheckFile.trim());
				Log.d("", "*****************************************************");

			} catch (ClientProtocolException e) {
				Log.e("??????","??????");
			}catch (IOException e) {
				Log.e("??????","??????");
			}// end try~catch
			Log.d("", "################### DoComplecatedJob doInBackground end");
			return null;

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected void onCancelled() {
			// --------------------------------------------------------------------------------------------
			// #region ?????????????????? ???????????? ??????

			// ?????? flag??? ????????? ?????? ?????? upload ??????
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
			// #region ???????????? ?????????????????? ????????????

			pb.setProgress((int) (progress[0]));

			// thread ??????
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
				int now = (int) (iProgress * totalSizeKB / 100);

				int cutlineUp = 0;
				int cutLineDown = 0;
				for (int j = 0; j < mFileList.size(); j++) {
					cutLineDown = cutlineUp;
					cutlineUp = cutlineUp + (int) mFileList.get(j).getFileSize() / 1000;
					int progcnt = j + 1;
					if (now > cutLineDown && now < cutlineUp && j == 0) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 1) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 2) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 3) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 4) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					}
				}

				tvTrans.setText(df.format(iProgress * totalSizeKB / 100) + " KB");
//				tvTot.setText(df.format(totalSizeKB) + " KB"+"("+fileCount+"???)");
			}// end if

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected void onPostExecute(Long result) {
			// --------------------------------------------------------------------------------------------
			// #region ???????????? ?????? ?????? ??? ???????????? ?????? ?????? ??????

			progressDialog.dismiss();
			multipartContent = null;
			mDialog.dismiss();
			Log.d("", "");
			Log.d("ProgressUpdate", "transData : " + transData);
			Log.d("ProgressUpdate", "transData : " + result);

			if (multipartContent != null || (iPercent < 100 && iPercent > 0)) {

				openWarnDialog(mActivity, "?????? ????????? ??????????????????.");
			} else {
				if (USER_TYPE_TOW.equals(Configuration.User.getUser_type()) || USER_TYPE_PATROL.equals(Configuration.User.getUser_type())) {
					openWarnDialog(mActivity, "?????? ?????? ??????");
				} else {
					openWarnDialog(mActivity, "?????? ?????? ??????\n????????? ?????? ???????????????.");
				}
			}
			Common.DeleteDir(Common.FILE_DIR);

			if (mActivity instanceof DialogJochi) {
				tempAction.execute("");
			}

			/*
			 * else{
			 *
			 * //?????? ????????? ??????????????? ????????? ??????
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

	Action tempAction;

	public void sendValueParent(Action item) {
		tempAction = item;
	}

	public class DoComplecatedJob_new extends AsyncTask<String, Integer, Long> {
		long totalSize;
		long totalSizeKB;
		int fileCount = 0;
		// ?????? ?????? Progressbar
		ProgressDialog progressDialog;
		Dialog mDialog = null;
		ProgressBar pb = null;
		ImageButton ibtnProgressCancel;

		public TextView tv;
		public TextView tvTrans;
		public TextView tvTot;
		public TextView tvPromptProgress;
		public int iProgress;
		public List<Image> mFileList;
		Parameters params;
		Activity mActivity;

		int kbSize = 0;

		public DoComplecatedJob_new() {
			super();
		}

		public DoComplecatedJob_new(List<Image> mList, Parameters params, Activity mActivity) {
			super();
			mFileList = mList;
			this.params = params;
			this.mActivity = mActivity;
			fileCount = mList.size();
		}

		@Override
		protected void onPreExecute() {
			// --------------------------------------------------------------------------------------------
			// #region ???????????? ??????

			progressDialog = new ProgressDialog(mActivity);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("?????? ?????? ???...");
			progressDialog.setCancelable(true);

			progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????",
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
			mDialog.getWindow().setGravity(Gravity.TOP | Gravity.CENTER);

			mDialog.setContentView(R.layout.cust_progressbar);
			mDialog.setCancelable(false);

			for (int i = 0; i < ReadSdCardSize(); i++) {
				long aa = mFileList.get(i).getFileSize();
			}

			pb = (ProgressBar) mDialog.findViewById(R.id.pbProgress);
			tv = (TextView) mDialog.findViewById(R.id.tvPercentProgress);
			tvTrans = (TextView) mDialog.findViewById(R.id.tvProgressTrans);
			tvTot = (TextView) mDialog.findViewById(R.id.tvProgressTot);
			tvPromptProgress = (TextView) mDialog.findViewById(R.id.tvPromptProgress);
			if (!Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
				tvPromptProgress.setText("????????? ????????? ?????????.");
			}
			ibtnProgressCancel = (ImageButton) mDialog.findViewById(R.id.ibtnProgressCancel);

			ibtnProgressCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					transData = "N";
				}
			});

			// ?????? ?????? ?????????
			tv.setText("0 %");
			tvTrans.setText("0 KB");
			tvTot.setText("0 KB ");

			// ?????? ?????? ?????????
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
			try {
				// --------------------------------------------------------------------------------------------
				// #region ??? ???????????? ??????, ???????????? ??????????????? ??? ???????????? ?????? ??????
				HttpContext httpContext = new BasicHttpContext();
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				String sysCd = "OCK";
				String storgSeq = "29";
				String drtySeq = "1";

				builder.addTextBody("SS_USER_ID", SituationService.conf.User.getPatcar_id(), ContentType.create("Multipart/related", "UTF-8"));
				builder.addTextBody("sysCd ", sysCd, ContentType.create("Multipart/related", "UTF-8"));
				builder.addTextBody("storgSeq ", storgSeq, ContentType.create("Multipart/related", "UTF-8"));
				builder.addTextBody("drtySeq ", drtySeq, ContentType.create("Multipart/related", "UTF-8"));

				List<Image> list = ReadSDCard();

				String[] files = new String[list.size()];
				Log.d("", TAG + " file full path = " + list.size());
				for (int i = 0; i < list.size(); i++) {
					files[i] = list.get(i).getFilePath() + "/" + list.get(i).getFileName();
					Log.d("", TAG + " file full path = " + files[i]);
				}
				Log.d("", TAG + " file full path = ");


				for (int i = 0; i < list.size(); i++) {
					builder.addPart("upload_" + i, new FileBody(new File(files[i])));
				}


				InputStream inputStream = null;
				// HttpClient httpClient = AndroidHttpClient.newInstance("Android");
				HttpClient httpClient = new DefaultHttpClient();

//				HttpPost httpPost = new HttpPost("http://ock.ex.co.kr:5003/open/upload.do");
				HttpPost httpPost = new HttpPost("http://112.220.106.20:8080/open/upload.do");


				multipartContent = new com.ex.situationmanager.util.CustomMultiPartEntity(
						new ProgressListener() {

							int uploadPercent = 0;

							@Override
							public void transferred(long num) {
								// TODO Auto-generated method stub
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});

				httpPost.setEntity(multipartContent);//progresListener ??????
				httpPost.setEntity(builder.build());

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					Log.d("??????", stringBuilder.toString());
					stringBuilder.append(line + "\n");
				}
				inputStream.close();

				// ????????????
				String result = stringBuilder.toString();
				Log.d("??????", result);
				//			String fileseq = result.replaceAll("[^0-9]", "");

				totalSize = multipartContent.getContentLength();
				totalSizeKB = totalSize / 1024;
				// pb.setMax(Integer.parseInt(totalSize + ""));

				// Send it
				multipartContent.addPart("renamePrefix", new StringBody(""));
				if (!Configuration.User.getUser_type().equals(USER_TYPE_CITIZEN)) {
					multipartContent.addPart("rpt_id", new StringBody(params.get("rpt_id"), Charset.forName("UTF-8")));
					// bscode; // ??????, ??????
					multipartContent.addPart("bscode", new StringBody(params.get("bscode"), Charset.forName("UTF-8")));
					// patcar_id; // ???????????????
					if (Configuration.User.getUser_type().equals(USER_TYPE_PATROL)) {
						multipartContent.addPart("patcar_id", new StringBody(params.get("patcar_id"), Charset.forName("UTF-8")));
					}
				} else {
					multipartContent.addPart("rpt_id", new StringBody(params.get("rpt_id"), Charset.forName("UTF-8")));
					multipartContent.addPart("bscode", new StringBody("", Charset.forName("UTF-8")));
					multipartContent.addPart("patcar_id", new StringBody(Configuration.User.getPatcar_id(), Charset.forName("UTF-8")));
				}
				multipartContent.addPart("nscode", new StringBody(params.get("nscode"), Charset.forName("UTF-8")));
				multipartContent.addPart("bhcode", new StringBody(params.get("bhcode"), Charset.forName("UTF-8")));
				multipartContent.addPart("ijung", new StringBody(params.get("ijung"), Charset.forName("UTF-8")));
				// user_type; // ?????? ??????
				multipartContent.addPart("user_type", new StringBody(Configuration.User.getUser_type(), Charset.forName("UTF-8")));


				httpPost.setEntity(multipartContent);

				HttpResponse response = httpClient.execute(httpPost, httpContext);
				String serverResponse = EntityUtils.toString(response.getEntity());

				sendCheckFile = serverResponse;
				Log.d("", "*****************************************************");
				Log.d("", "" + sendCheckFile);
				Log.d("", "" + sendCheckFile.trim());
				Log.d("", "*****************************************************");

				//
				//
				//
				//
				//
				//
				//


			} catch (ClientProtocolException e) {
				Log.e("??????", "??????");
			}catch (IOException e){
				// end try~catch
				Log.e("??????","??????");
			}
			Log.d("", "################### DoComplecatedJob doInBackground end");
			return null;

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected void onCancelled() {
			// --------------------------------------------------------------------------------------------
			// #region ?????????????????? ???????????? ??????

			// ?????? flag??? ????????? ?????? ?????? upload ??????
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
			// #region ???????????? ?????????????????? ????????????

			pb.setProgress((int) (progress[0]));

			// thread ??????
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
				int now = (int) (iProgress * totalSizeKB / 100);

				int cutlineUp = 0;
				int cutLineDown = 0;
				for (int j = 0; j < mFileList.size(); j++) {
					cutLineDown = cutlineUp;
					cutlineUp = cutlineUp + (int) mFileList.get(j).getFileSize() / 1000;
					int progcnt = j + 1;
					if (now > cutLineDown && now < cutlineUp && j == 0) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 1) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 2) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 3) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					} else if (now > cutLineDown && now < cutlineUp && j == 4) {
						tvTot.setText(df.format(totalSizeKB) + " KB" + "(" + progcnt + "/" + fileCount + "???)");
					}
				}

				tvTrans.setText(df.format(iProgress * totalSizeKB / 100) + " KB");
//				tvTot.setText(df.format(totalSizeKB) + " KB"+"("+fileCount+"???)");
			}// end if

			// #endregion
			// --------------------------------------------------------------------------------------------
		}

		@Override
		protected void onPostExecute(Long result) {
			// --------------------------------------------------------------------------------------------
			// #region ???????????? ?????? ?????? ??? ???????????? ?????? ?????? ??????

			progressDialog.dismiss();
			multipartContent = null;
			mDialog.dismiss();

			Log.d("ProgressUpdate", "transData : " + transData);
			Log.d("ProgressUpdate", "transData : " + result);

			if (multipartContent != null || (iPercent < 100 && iPercent > 0)) {

				openWarnDialog(mActivity, "?????? ????????? ??????????????????.");
			} else {
				if (USER_TYPE_TOW.equals(Configuration.User.getUser_type()) || USER_TYPE_PATROL.equals(Configuration.User.getUser_type())) {
					openWarnDialog(mActivity, "?????? ?????? ??????");
				} else {
					openWarnDialog(mActivity, "?????? ?????? ??????\n????????? ?????? ???????????????.");
				}
			}
			Common.DeleteDir(Common.FILE_DIR);

			/*
			 * else{
			 *
			 * //?????? ????????? ??????????????? ????????? ??????
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
		Log.d("camera", TAG + "executeJob user type = " + Configuration.User.getUser_type());
		if (ReadSdCardSize() > 0) {

			Log.i("camera", "camera correct");
//			final DoComplecatedJob_new task = new DoComplecatedJob_new(ReadSDCard(), params, mActivity);
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

	// SituationManager ???????????? ?????? ????????????
	public static int ReadSdCardSize() {

		File fileDir = new File(Configuration.directoryName);
		if (!fileDir.exists())
			fileDir.mkdir();
		File[] files = fileDir.listFiles();

		return files.length;
	}

	// SituationManager ???????????? ?????? ?????? ????????????
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

	// 1?????? ???????????? MB ??? ??????
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
	 * ????????? ?????? ??????
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

	private double START_LAT = 37.200349;
	private double START_LNG = 127.094467;

	// GPS ??????
	static String ns_code = ""; // ????????????
	static String ns_name = ""; // ?????????
	static String banghyang = ""; // ??????
	static String bhCode = "S"; // ????????????(S,E)
	String sisul; // ????????????(IC/JCT, ?????????, ????????????)
	String siseolDaepyo;// ICJCT, ?????????, ???????????? ???????????? ?????????
	String ppastIjung = "000";
	String pastIjung = "111";
	static String currentIjung = ""; // ??????
	String rampid = "";

	String gonggu = "";
	boolean isInNoseon = true;

	private int myCnt = 0;
	public static LocationManager RelimyLocMgr;
	public static LocationListener RelimyListener;
	private int maxNsEmptyTime = 30;
	private int moveNsEmptyTime = 4;
	private int emptyCnt = 0;// ?????????????????? ????????? ?????????
	private boolean gpsStatusYN = false;
	public int locationChangedCnt = 0;
	static LocationManager mLocMgr;
	static LocationListener mLocListener;

	public void startMyGps() {
		//--------------------------------------------------------------------------------------------
		// #region startGPS, ???????????? ?????? [ setCurrentPosition(getDBSearch(latitude, longitude)); ]

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Log.e("??????","??????");
				}

				mHandler.post(new Runnable() {
					public void run() {
						mLocMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
						mLocListener = new LocationListener() {
							public void onProviderDisabled(String provider) {

							}

							public void onStatusChanged(String provider, int status, Bundle extras) {

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

							public void onProviderEnabled(String provider) {
							}

							public void onLocationChanged(Location location) {

								Log.d("", "locationchanged in send");
								locationChangedCnt = 0;
								dist = 0.0;
//								pastLatitude = latitude;
//								pastLongitude = longitude;
								latitude = location.getLatitude();// ??????
								longitude = location.getLongitude();// ??????
								Log.d("", "locationChanged in=" + latitude + " : " + longitude);
								//????????? ????????? 8??? ????????????
								latitude = Common.doubleCutToString(latitude);
								longitude = Common.doubleCutToString(longitude);

								altitude = location.getAltitude();// ??????
								speed = Math.round((location.getSpeed() * 3600) / 1000);// ??????


							}
						};

						Criteria criteria = new Criteria();
						criteria.setAccuracy(Criteria.ACCURACY_FINE);        // ?????????
						criteria.setPowerRequirement(Criteria.POWER_LOW);    // ?????? ?????????
						criteria.setAltitudeRequired(true);                // ??????, ?????? ?????? ?????? ????????? ??????
						criteria.setBearingRequired(true);
						criteria.setSpeedRequired(true);                    //??????
						criteria.setCostAllowed(false);                    //?????? ????????? ?????? ????????? ???????????? ????????? ??????

						mLocMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//				        mLocMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);
						if (PermissionChecker.checkSelfPermission(contextActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(contextActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
							// TODO: Consider calling
							//    ActivityCompat#requestPermissions
							// here to request the missing permissions, and then overriding

							// to handle the case where the user grants the permission. See the documentation
							// for ActivityCompat#requestPermissions for more details.
							return;
						}
						mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);

						GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
							public void onGpsStatusChanged(int event) {
								if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
									if (PermissionChecker.checkSelfPermission(contextActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
										// TODO: Consider calling
										//    ActivityCompat#requestPermissions
										// here to request the missing permissions, and then overriding
										//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
										//                                          int[] grantResults)
										// to handle the case where the user grants the permission. See the documentation
										// for ActivityCompat#requestPermissions for more details.
										return;
									}
									GpsStatus status = mLocMgr.getGpsStatus(null);
									Iterable<GpsSatellite> sats = status.getSatellites();
									Iterator<GpsSatellite> iter = sats.iterator();

									GpsSatellite gpsS = null;

									boolean checkStat = false;
									int checkStatCnt = 0;
									while (iter.hasNext()) {
										gpsS = iter.next();

										if (gpsS.usedInFix()) {
											Log.d("GPSInfo", "Almanac : " + gpsS.hasAlmanac());
											Log.d("GPSInfo", "Ephemeris : " + gpsS.hasEphemeris());
											checkStatCnt++;
										}
									}
//				                    Log.d("GPSInfo", "checkStatCnt"+checkStatCnt);

									if (checkStatCnt > 3) {
										checkStat = true;
									} else {
										checkStat = false;
									}

									if (mGpsSatStat != checkStat) {
										mGpsSatStat = checkStat;

										try {

											if (mLocMgr != null) {
												if (PermissionChecker.checkSelfPermission(contextActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(contextActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
													// TODO: Consider calling
													//    ActivityCompat#requestPermissions


													return;
												}
												Location l = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

												if (l != null) {

//						                    		locationChangedCnt = 0;
//						            				dist = 0.0;
//						            				firstFlag = true;
//						            				pastLatitude = latitude;
//						            				pastLongitude = longitude;
//						            				latitude = location.getLatitude();// ??????
//						            				longitude = location.getLongitude();// ??????
													//????????? ????????? 8??? ????????????
//						            				latitude = Common.doubleCutToString(latitude);
//						            				longitude = Common.doubleCutToString(longitude);
//						            				angle = Math.atan2(longitude - pastLongitude, latitude- pastLatitude);
//						            				altitude = location.getAltitude();// ??????
//						            				speed = Math.round((location.getSpeed() * 3600) / 1000);// ??????
												}
											}

										} catch (NullPointerException ex) {
											Log.e("??????","??????");
										}

									}

								}
							}
						};

						mLocMgr.addGpsStatusListener(gpsStatusListener);
						String provider = mLocMgr.getBestProvider(criteria, true);

						Log.d("GPSInfo", "bestProvider : " + provider);
						nowprovider = provider;

//				        if(provider != null){
//				        	mLocMgr.requestLocationUpdates(provider, 0, 0, mLocListener);
//				        }else{
						mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
//				        }


						if (!mLocMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
							try {

								if (contextActivity.isFinishing() == false) {
									if (gpsDialogFlag == true) {
										AlertDialog.Builder adb = new AlertDialog.Builder(contextActivity);
										adb.setCancelable(false);
										adb.setTitle("???????????? ????????????");
										adb.setMessage("GPS??? ???????????? ????????????.\n?????? ???????????????????");
										adb.setPositiveButton("???", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
												startActivityForResult(i, GPS_INTENT);
											}
										});
										adb.setNegativeButton("?????????", null);
										adb.show();
									}

								}
							} catch (NullPointerException e) {
								Log.e("??????","NullPointer");
							}
						}
					}
				});
			}
		}).start();

		// #endregion
		//--------------------------------------------------------------------------------------------
	}

	/************************************************************************
	 * GPS ??????
	 ************************************************************************/
	public static void stopGPS() {
		if (mLocMgr != null)
			mLocMgr.removeUpdates(mLocListener);
	}

	public boolean chkGpsService(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	Cursor dbCursor;

	/************************************************************************
	 * GPS ????????? ?????? ??????????????? DB?????? ?????? ??? ?????? - ?????? ??????, ?????? ?????? ?????? : ?????? ????????? ????????? ?????? ????????? ??????
	 * ???????????? > ?????? ?????? = ?????? (?????????: ??????) ???????????? < ?????? ?????? = ?????? (?????????: ??????) : ?????????????????? GPS ????????????
	 * ??????????????? ?????? ?????? ?????? ?????? ???????????? ?????? ???????????? ????????? ?????? ???, ?????????????????? ????????? ????????? ?????? ???????????? ????????? ????????????
	 * ????????? ???.
	 *
	 * @param latitude
	 * @param longitude
	 * @return
	 ************************************************************************/
	public String getDBSearch(double latitude, double longitude) {
		// --------------------------------------------------------------------------------------------
		// #region GPS ????????? ????????? ?????? ??????(??????, ??????, ??????, ?????? IC/JCT) ????????? ????????????.

		Log.v("GPSInfo", "getDBSearch");

		String rtnStr = "";
		// -> ?????? ????????? ????????? ?????? ????????? ?????? ??????????????? ??????
		// -> ?????? ????????? ????????? ?????? ????????? ?????? ??????????????? ??????
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
			// private int emptyCnt = 0;//?????????????????? ????????? ?????????

			dbCursor = db.fetchRange(latitude, longitude, ns_code);

			Log.d("GPSInfo", "getDBSearchCompleted...");
			if (dbCursor.getCount() > 0) {

				// cursor.getCount(); // ??? ROW ???
				// cursor.getColumnCount() // ?????? ??????

				// ?????? GPS????????? ????????? 2??? ????????? ?????? ?????? ?????? ????????? ????????????.
				String rowResults = "";

				if (dbCursor.getCount() > 1) {
					if (ns_code != null && ns_code.trim().length() > 0) {

						Log.d("GPSInfo", "GPSInfo ????????? 2????????? ?????? ???????????? ??????");
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

						Log.d("GPSInfo", "GPSInfo ????????? 2????????? ?????? ???????????? ??????");
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
							"GPSInfo ????????? 1??? | count : " + dbCursor.getCount());

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

				// ?????? ??????
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
				// ?????????????????????
				// <down>??????</down>
				// <upper>??????</upper>
				// <middle>??????</middle>
				if (ns_code.equals("1000")) {
					if (temp2 <= 33) {
						if (temp2 > temp1) {// ????????????(currentIjung)???
							// ????????????(pastIjung)??????
							// ??????
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
						if (temp2 > temp1) {// ????????????(currentIjung)???
							// ????????????(pastIjung)??????
							// ??????
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

						if (temp2 > temp1) {// ????????????(currentIjung)???
							// ????????????(pastIjung)??????
							// ??????
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
					if (temp2 > temp1) {// ????????????(currentIjung)??? ????????????(pastIjung)??????
						// ??????
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
				rtnStr = ns_name + "|" + banghyang + "|" + currentIjung + "|" + ns_code + "|0|" + siseolDaepyo;

				Log.d("GPSInfo", "Row : " + dbCursor.getPosition() + " => " + rowResults);
				Log.d("GPSInfo", "rtnStr :  " + rtnStr);

				isInNoseon = true;
			} else {
				// ?????? ?????? ???????????? ?????? ??????
				// ????????? ??? ?????? ??????
				if (isInNoseon) {
					Toast.makeText(getApplicationContext(), "?????? ????????? ???????????? ??????????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
					if ("".equals(Common.nullCheck(ns_code))) {
						setActivityViewEdit(contextActivity);
						Log.d("", TAG + " setActivityViewEdit 2");
					}
				}// end if

				Log.d("GPSInfo", "mGpsSatStat2:" + mGpsSatStat);

				if (mGpsSatStat) {
					rtnStr = "?????????|_|-999|_|0|0";
				} else {
					rtnStr = "?????????(??????GPS??? ??????????????? \n?????? ??????????????? ??????????????????)|_|-999|_|0|0";
				}// end if

				isInNoseon = false;
			}// end if (dbCursor.getCount() > 0) {

			// ??????????????????
			// bhCode = db.getBanghyangCode(ns_code, banghyang);

			// ???????????? ??? code ?????? ???.
			rpt_bhPkCode = db.fetchBanghyang_sub(ns_code, bhCode, rpt_bscode, currentIjung);

		} catch (IOException e) {
			Log.e("??????","??????");
		} catch (NullPointerException e) {
			Log.e("??????","??????");
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

	// ???????????? ????????? ??????.
	static double dist = 0.0;

	public void noLocationInfo() {

		if (gpsStatusYN == false) {
			emptyCnt++;
			emptyCnt++;
			if (moveNsEmptyTime % moveNsEmptyTime == 0) {
				if (moveNsEmptyTime <= maxNsEmptyTime) {

					Cursor cursor = db.fetchNextRange(ns_code, currentIjung, bhCode);
					if (null != cursor) {
						while (cursor.moveToNext()) {
							ns_code = Common.nullCheck(cursor.getString(0));
							ns_name = Common.nullCheck(cursor.getString(1));
							currentIjung = Common.nullCheck(cursor.getString(2));
							sisul = Common.nullCheck(cursor.getString(4));
							rampid = Common.nullCheck(cursor.getString(5));
						}
					} else {
						return;
					}

					sisul = db.fetchSisulAll(ns_code, latitude, longitude, bhCode);// ?????????????????????
//					System.out.println("##############################################");
//					System.out.println("Tunnel Update Locatoin cursor latitude = "+ latitude);
//					System.out.println("Tunnel Update Locatoin cursor longitude = "+ longitude);

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

					System.out.println("Tunnel Update Locatoin cursor count = " + cursor.getCount());
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

					double dist_curr_beside = Math.sqrt(Math.pow((currlo - besidelo), 2) + Math.pow((currla - besidela), 2));
					double anglea = Math.atan2(currla - besidela, currlo - besidelo);
					double angleb = Math.atan2(nextla - besidela, nextlo - besidelo);
					double nextangleb = Math.atan2(nnextla - nextla, nnextlo - nextlo);
					double realangle = anglea - angleb;

					if (dist == 0.0) {
						dist = Math.sin(realangle) * dist_curr_beside;
					}
//					System.out.println("Tunnel Update Locatoin dist 2 = "+ dist);

					if (dist > 0) {
						System.out.println("Tunnel Update Locatoin dist edit = " + dist);
						resultlo = nextlo + (Math.cos(nextangleb + Math.atan2(1, 0)) * Math.abs(dist));
						resultla = nextla + (Math.sin(nextangleb + Math.atan2(1, 0)) * Math.abs(dist));
					} else {
						System.out.println("Tunnel Update Locatoin dist edit = " + dist);
						resultlo = nextlo + (Math.cos(nextangleb - Math.atan2(1, 0)) * Math.abs(dist));
						resultla = nextla + (Math.sin(nextangleb - Math.atan2(1, 0)) * Math.abs(dist));
					}

					if (34 < latitude && latitude < 38.4) {
						if (126 < longitude && longitude < 129.6) {
							longitude = resultlo;
							latitude = resultla;
							//????????? ????????? 8??? ????????????
							longitude = Common.doubleCutToString(longitude);
							latitude = Common.doubleCutToString(latitude);
						}
					}
//					System.out.println("Tunnel Update Locatoin resultlo >> "+ resultlo);
//					System.out.println("Tunnel Update Locatoin resultla >> "+ resultla);
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
	 * GPS ????????? ?????? DB?????? ?????? ?????? ??? ?????? ???????????? ????????? ????????????.
	 ************************************************************************/
	private void setCurrentPosition(String dbSearch) {
		// --------------------------------------------------------------------------------------------
		// #region GPS ????????? ?????? DB?????? ?????? ?????? ??? ?????? ???????????? ????????? ????????????.
		Log.d("GPSInfo", "setCurrentPosition");

		Log.d("GPSInfo", dbSearch);

		String[] GPSInfo = dbSearch.split("[|]");
		Log.d("GPSInfo", "setCurrentPosition" + GPSInfo.length);
		// GPS ????????? ????????? ??????
		if (GPSInfo.length >= 5) {

			// etLocation.setTextSize(20);

			Log.d("GPSInfo", "GPSInfo[0]" + GPSInfo[0]);
			Log.d("GPSInfo", "GPSInfo[1]" + GPSInfo[1]);
			Log.d("GPSInfo", "GPSInfo[2]" + GPSInfo[2]);

			// ?????? ????????? ??????
			if (GPSInfo[0] != null && !GPSInfo[0].equals("")
					&& !GPSInfo[0].startsWith("?????????") && GPSInfo[2] != null) {

				// ????????? ???????????? ????????? .0 ??????
				if (GPSInfo[2].indexOf(".") <= -1) {
					GPSInfo[2] = GPSInfo[2] + ".0";
				} else {
					if (GPSInfo[2].indexOf(".") == GPSInfo[2].length() - 1) {
						GPSInfo[2] = GPSInfo[2] + "0";
					}// end if
				}// end if

				// ??????????????? update ??? ????????? ??? ????????? ???????????? ?????????
				ns_name = GPSInfo[0]; // ?????????
				banghyang = GPSInfo[1]; // ??????
				currentIjung = GPSInfo[2]; // ??????
				ns_code = GPSInfo[3]; // ????????????
			} else {
				ns_name = ""; // ?????????
				banghyang = ""; // ??????
				// currentIjung = "-999"; // ??????
				currentIjung = currentIjung;
				ns_code = ""; // ????????????
				sisul = "";
			}// end if
		}// end if

		sisul = db.fetchSisulAll(ns_code, latitude, longitude, bhCode);// ?????????????????????
		// ????????????
		if (!"".equals(Common.nullCheck(ns_code))) {
			gonggu = db.fetchGonggu(ns_code, currentIjung);// ????????????
		} else {// ?????? OUT
			gonggu = "";

		}

		// #endregion
		// --------------------------------------------------------------------------------------------
	}


	private int timerDelay = 1;// timerDelay ????????? ??????
	private int timerCnt = 0;// ????????? ??????. ?????? timerDelay
	static Timer timer = new Timer();
	Handler handler = new Handler();

	//???????????? ???????????? ?????? ???????????? ????????? ????????? 1?????? ??????.
	boolean firstFlag = true;

	public void startTimer() {
		TimerTask timertask = new TimerTask() {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// GPS??????
					Log.d("", TAG + " Time Check STA = " + Calendar.getInstance().getTimeInMillis());

					if (timerDelay <= timerCnt) {
						timerCnt = 0;
						locationChangedCnt++;

						if (locationChangedCnt > 1) {
							gpsStatusYN = false;
						} else {
							gpsStatusYN = true;
						}
						Log.d("", "Tunnel Update Locatoin locationChangedCnt= " + locationChangedCnt);

						if ((locationChangedCnt > 1 && locationChangedCnt <= 30)) {
							if (locationChangedCnt % 2 == 0) {
								noLocationInfo();
							}
						} else {
							if (locationChangedCnt < 30) {
								setCurrentPosition(getDBSearch(latitude, longitude));
							}
						}

						// ***************************************************************

						Log.d("", TAG + " startTimer start_yn = " + Common.getPrefString(contextActivity, "start_yn"));
						Parameters params = new Parameters(ONECLICK_CARGPS_INSERT);

						params.put("user_type", Configuration.User.getUser_type());// ??????????????????
//						params.put("car_id", Configuration.User.getPatcar_id());// ???????????? ????????????.
						params.put("car_id", Common.getPrefString(BaseActivity.this, "patcar_id"));// ???????????? ????????????.
						if ("Y".equals(Common.getPrefString(contextActivity, "start_yn"))) {
							params.put("rpt_id", Common.getPrefString(contextActivity, "selectedRpt_id"));// ???????????? ????????????
						}

						//?????????
						byte[] encryptBytes_lo = seed.encrypt(longitude + "", szKey);
						String encString_lo = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo)));

						byte[] encryptBytes_la = seed.encrypt(latitude + "", szKey);
						String encString_la = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la)));


						byte[] encryptBytes_sp = seed.encrypt(speed + "", szKey);
						String encString_sp = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_sp)));

//						params.put("car_gps_lo", longitude + "");// ??????
//						params.put("car_gps_la", latitude + "");// ??????
						params.put("car_gps_lo", encString_lo + "");// ??????
						params.put("car_gps_la", encString_la + "");// ??????
						params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0", szKey)))));// ????????????


						try {
							altitude = Math.round(altitude * 100f) / 100f;
						} catch (NumberFormatException | NullPointerException e) {
							altitude = 0.0;
							Log.e("??????","NumberFormatException | NullPointerExceptio");
						} finally {
//							params.put("car_gps_hg", altitude + "");// ??????
							byte[] encryptBytes_hg = seed.encrypt(altitude + "", szKey);
							String encString_hg = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_hg)));
							params.put("car_gps_hg", encString_hg + "");// ??????
						}
//						params.put("car_gps_sp", speed + "");// ??????
						params.put("car_gps_sp", encString_sp + "");// ??????


						params.put("start_yn", Common.getPrefString(contextActivity, "start_yn") + "");// ???????????? (y or n)
						params.put("car_way", "" + ns_code);// ?????? ?????? ??????
						if (!"".equals(Common.nullCheck(rampid))) {
//							params.put("car_way_ramp", ("" + ns_name + "_"+ rampid + "Ramp_" + currentIjung).replace(" ", ""));// ?????? ?????? ??????
							params.put("car_way_ramp", (Common.nullCheck(rampid) + "").replace(" ", "").trim());// ?????? ?????? ??????
						} else {
						}

						params.put("car_direct", rpt_bhPkCode);// ?????? ?????? pk??????
						if (null != gonggu && !gonggu.isEmpty()) {
							params.put("car_gonggu", gonggu.substring(0, 1) + "");// ?????????
						} else {
							params.put("car_gonggu", gonggu + "");// ?????????
						}

						params.put("car_sisul", sisul + "");// IC,JCT
						params.put("car_ijung", currentIjung + "");// IC,JCT,?????????,..???
						// ?????????
						params.put("ok", Common.getPrefString(BaseActivity.this, "patrolOKFlag") + "");// ???????????? ??????
						params.put("nowprovider", nowprovider + "");// best provider ??????
						params.put("STOP", "START");


						try {
							setActivityViewEdit(contextActivity);
							Log.d("", TAG + " setActivityViewEdit 3");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// USER_TYPE_CITIZEN ???????????? ?????? ??????.0003
						Log.d("", "Configuration.User.getUser_type() = " + Configuration.User.getUser_type());
						if (USER_TYPE_TOW.equals(Configuration.User.getUser_type()) || USER_TYPE_PATROL.equals(Configuration.User.getUser_type())) {
							if (34 < latitude && latitude < 38.4) {
								if (126 < longitude && longitude < 129.6) {
									Log.d("", "locationchanged in send");

									try {
										angle = Math.atan2(longitude - pastLongitude, latitude - pastLatitude);
										angle = Math.round(angle * 1000000000d) / 1000000000d;
									} catch (NumberFormatException | NullPointerException e) {
										angle = 0.0;
										Log.e("??????"," ??????");
									} finally {
										params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt(angle + "", szKey)))));// ????????????
									}

									//Angle ????????? ??????????????? ??????.
									pastLatitude = latitude;
									pastLongitude = longitude;
									//????????? ???????????? ???????????? ??????. 0.0??? ?????? ???????????? ???????????? ??????????????? ?????? ?????????.
									Common.setPrefString(contextActivity, "lastla", "" + latitude);
									Common.setPrefString(contextActivity, "lastlo", "" + longitude);
									new Action(ONECLICK_CARGPS_INSERT, params).execute("");
								}
							} else if (latitude == 0 || longitude == 0) {

								if (Common.getPrefString(contextActivity, "lastla").equals("")) {//????????? ?????? ????????? ?????? ??????
									if (mLocMgr != null) {
										if (PermissionChecker.checkSelfPermission(contextActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(contextActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
											// TODO: Consider calling
											//    ActivityCompat#requestPermissions
											// here to request the missing permissions, and then overriding
											//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
											//                                          int[] grantResults)
											// to handle the case where the user grants the permission. See the documentation
											// for ActivityCompat#requestPermissions for more details.
											return;
										}
										Location l = mLocMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				                    	if(mLocMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				                    		if(null != l){
				                    			byte[] encryptBytes_lo3 = seed.encrypt(l.getLongitude()+"",szKey);
												String encString_lo3 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo3)));
												byte[] encryptBytes_la3 = seed.encrypt(l.getLatitude()+"",szKey);
												String encString_la3 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la3)));

				                    			params.put("car_gps_lo", encString_lo3 + "");// ??????
					                    		params.put("car_gps_la", encString_la3 + "");// ??????
					                    		params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0",szKey)) )));// ????????????
					                    	}
				                    	}else{
				                    		params.put("car_gps_lo", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0",szKey)) )));// ??????
				                    		params.put("car_gps_la", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0",szKey)) )));// ??????
				                    		params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0",szKey)) )));// ????????????
				                    	}
									}
								}else{//????????? ?????? ????????? ?????? ??????.
									byte[] encryptBytes_lo2 = seed.encrypt(Common.getPrefString(contextActivity, "lastlo")+"",szKey);
									String encString_lo2 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo2)));
									byte[] encryptBytes_la2 = seed.encrypt(Common.getPrefString(contextActivity, "lastla")+"",szKey);
									String encString_la2 = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la2)));

									params.put("car_gps_lo", encString_lo2+"" );// ??????
									params.put("car_gps_la", encString_la2+"" );// ??????
									params.put("car_gps_ag", seed.renameSpecificChar(new String(Base64.encodeBase64(seed.encrypt("0.0",szKey)) )));// ????????????
								}

								//?????????.##################################################
								//?????? ,?????? ???????????????
//								params.put("car_gps_lo", "128.164027");// ??????
//								params.put("car_gps_la", "36.129331");// ??????

								//?????? ????????? ->?????????????????? ???????????? 2Km ???
//								params.put("car_gps_lo", "128.182176");// ??????
//								params.put("car_gps_la", "36.140377");// ??????
								//?????????.##################################################

								new Action(ONECLICK_CARGPS_INSERT, params).execute("");

							}
						}
					}
					timerCnt++;
					// HTTP ??????
					Log.d("",TAG+" Time Check END = "+Calendar.getInstance().getTimeInMillis());
				}
			};

			@Override
			public void run() {
				handler.post(runnable);
			}
		};
		timer = new Timer();
		timer.schedule(timertask, 1000, 15000);// 100MS ?????????, 1??? ???????????? ??????.
	}

	public void stopTimer(Context context) {
		if (null != timer) {
			timer.cancel();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		gpsDialogFlag = false;
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

//		Toast.makeText(this,"??????",Toast.LENGTH_SHORT).show();
			Intent userIntent = new Intent(getApplicationContext(), DialogUserInfoActivity.class);
			userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, userIntent, PendingIntent.FLAG_ONE_SHOT);
			try {
				pi.send();
			} catch (PendingIntent.CanceledException e) {
				Log.e("??????","??????");
			}
			break;

		default:
			break;
		}
	}

	public void sendStart() {
		Common.setPrefString(contextActivity, "start_yn", "Y");

	}


	/**???????????? ????????? ?????? ????????? ?????? ??????.
	 *
	 */
	public void sendConfirm(){
		Parameters params = new Parameters(ONECLICK_PATROLCONFRIM_UPDATE);
		params.put("user_type", Configuration.User.getUser_type());// ??????????????????
		params.put("car_id", Common.getPrefString(BaseActivity.this, "patcar_id"));// ???????????? ????????????.
		params.put("rpt_id", Common.getPrefString(contextActivity, "selectedRpt_id"));// ????????????
		params.put("start_yn", "N");// ???????????? (y or n)
		params.put("STOP", "CONFIRM");

		new Action(ONECLICK_CARGPS_INSERT, params).execute("");
	}


	/**????????????, ??????????????????, ??????????????? ??????.
	 * @param canceled ?????? ????????? ??????  true ????????? else
	 */
	public void sendStop(boolean canceled) {
		Common.setPrefString(contextActivity, "start_yn", "N");
//		firstFlag = false;
		timerCnt = 0;
		Log.i("", TAG + "timerCalled sendStop !!!!" + canceled);
		Parameters params = new Parameters(ONECLICK_CARGPS_INSERT);

		params.put("user_type", Configuration.User.getUser_type());// ??????????????????

		params.put("car_id", SituationService.conf.User.getPatcar_id());// ???????????? ????????????.
		params.put("rpt_id",SituationService.selectedRpt_id);// ????????????

		//?????????
		byte[] encryptBytes_lo = seed.encrypt(longitude+"",szKey);
		String encString_lo = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_lo)));
		byte[] encryptBytes_la = seed.encrypt(latitude+"",szKey);
		String encString_la = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_la)));


		byte[] encryptBytes_sp = seed.encrypt(speed+"",szKey);
		String encString_sp = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_sp)));

		params.put("car_gps_lo", encString_lo + "");// ??????
		params.put("car_gps_la", encString_la + "");// ??????
		try {
			altitude = Math.round(altitude * 100f) / 100f;
		} catch (NullPointerException | NumberFormatException e) {
			altitude = 0.0;
			Log.e("??????"," | ??????");
		} finally {
			byte[] encryptBytes_hg = seed.encrypt(altitude+"",szKey);
			String encString_hg = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_hg)));
			params.put("car_gps_hg", encString_hg + "");// ??????
		}
		params.put("car_gps_sp", encString_sp + "");// ??????
		try {
			angle = Math.round(angle * 1000000000d) / 1000000000d;
		} catch (NullPointerException | NumberFormatException e) {
			Log.e("??????","?????? ");
		} finally {
			byte[] encryptBytes_ag = seed.encrypt(angle+"",szKey);
			String encString_ag = seed.renameSpecificChar(new String(Base64.encodeBase64(encryptBytes_ag)));
			params.put("car_gps_ag", encString_ag + "");// ????????????
		}
		params.put("start_yn", Common.getPrefString(contextActivity, "start_yn"));// ???????????? (y or n)
		params.put("car_way", "" + ns_code);// ?????? ?????? ??????
		params.put("car_direct", "");// ?????? ?????? pk??????
		if (null != gonggu && !gonggu.isEmpty()) {
			params.put("car_gonggu", gonggu.substring(0, 1) + "");// ?????????
		} else {
			params.put("car_gonggu", gonggu + "");// ?????????
		}
		params.put("car_sisul", sisul + "");// IC,JCT
		params.put("car_ijung", currentIjung + "");// IC,JCT,?????????,..??? ?????????
		params.put("ok", Common.getPrefString(BaseActivity.this, "patrolOKFlag") + "");// ???????????? ??????
		params.put("STOP", "STOP");//sendstop
		if(canceled){
			params.put("canceled", "Y");
		}

		new Action(ONECLICK_CARGPS_INSERT, params).execute("");

		if(!canceled){
			Parameters params_end = new Parameters(ONECLICK_WORKCOMPLETE_UPDATE);
			params_end.put("car_id", SituationService.conf.User.getPatcar_id());// ???????????? ????????????.
			params_end.put("user_type", SituationService.conf.User.getUser_type());
			params_end.put("rpt_id",SituationService.selectedRpt_id);
			params_end.put("reg_type", SituationService.rpt_reg_type);

			new Action(ONECLICK_WORKCOMPLETE_UPDATE, params_end).execute("");
			try {
				setActivityViewEdit(contextActivity);
				Log.d("",TAG+ " setActivityViewEdit 4");
			} catch (IOException e) {
				Log.e("??????","??????");
			}
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
			currentItem.setFileSize(file.length());

			Log.d("saveFile", "fileName : " + fileName[fileName.length - 1]);

			// ??????????????? ??? ?????? ???????????? ????????? 1??????, ?????????, MB ??????

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

		Log.d("", "ReadSdCard list size = " + trnImageList.size());
		return trnImageList;
	}

	/**
	 * ???????????? ?????????+???????????? ??? ??????.
	 */
	public void settingJeopboJisa() {
		if (null != Configuration.User.getRcv_yn_list()) {
			// ?????????
			db.updateBBBsCodeInit();
			for (int i = 0; i < Configuration.User.getRcv_yn_list().size(); i++) {
				if (Configuration.User.getRcv_yn_list().get(i).equals("Y")) {
					Log.d("", TAG + "items splig1  = "
							+ Configuration.User.getRcv_bs_list().get(i));
					if (Configuration.User.getRcv_bs_list() != null) {
						String[] items = Configuration.User.getRcv_bs_list().get(i)
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
