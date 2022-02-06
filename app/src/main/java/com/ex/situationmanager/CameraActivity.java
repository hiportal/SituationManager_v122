package com.ex.situationmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ex.situationmanager.BaseActivity.DoComplecatedJob;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.MediaScanner;
import com.ex.situationmanager.util.Preview;
import com.ex.situationmanager.util.VerticalSeekBar;


public class CameraActivity extends BaseActivity implements OnClickListener, SensorEventListener {
	
	private Preview mPreview;
	private byte[][] mImageData;
	private boolean gFocussed = false;
	private boolean gCameraPressed = false;
	// Camera mCamera = null;
	private static SharedPreferences sPrefs = null;
	public static final String KEY_POPUP_ENV = "key_env";
	public static final String KEY_POPUP_ENV_RUN_MODE = "key_env_run";

	public String mFilename;
	private int mFileNameYear;
	private int mFileNameMonth;
	private int mFileNameDay;
	private int mFileNameCount;

	public static final String SAVE_FILE_YEAR = "sava_file_year";
	public static final String SAVE_FILE_MONTH = "sava_file_month";
	public static final String SAVE_FILE_DATE = "sava_file_date";
	public static final String SAVE_FILE_COUNT = "sava_file_count";

	
	private String mFileimageRoute = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/situationmanager/";
	private Camera camera;
	private int m_resWidth;
	private int m_resHeight;
	
	ImageButton ibtnRunShutter;	
	ImageButton ibtnSave;
	ImageButton ibtnCancel;
	
	boolean canShutter = true;
	
	String ffffffffffffff;
	
	VerticalSeekBar seekbar;
	private static String TAG = "CameraActivity";;
	
	//센서
	public int SCREEN_ORIENT;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(SituationService.conf.USER_PHONE_NUMBER.startsWith(com.ex.situationmanager.util.Configuration.NAVIGATION_START_NUMBER)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
		}
		setContentView(R.layout.camera);
		
		Resources resource = Resources.getSystem();
        Configuration config = resource.getConfiguration();
        SCREEN_ORIENT = config.orientation;

        onConfigurationChanged(config);
        
		File dir = new File(mFileimageRoute);
		if(!dir.isDirectory()){
			dir.mkdirs();
			Log.d("","Camera File not Exist!"+mFileimageRoute);
		}else{
			Log.d("","Camera File Exist!");
		}
		Log.d("",TAG+" onCreate 1");

		mPreview = (Preview) findViewById(R.id.camera_preview);
		Log.d("",TAG+" onCreate 2");
		//카메라 최저 해상도 설정.
		Camera.Parameters parameters = mPreview.mCamera.getParameters();
		if(rotation == 0){//portrait
			mPreview.mCamera.setDisplayOrientation(90);
		}else if(rotation == 3){//landScape right
			mPreview.mCamera.setDisplayOrientation(180);
		}
		
		Log.d("",TAG+" onCreate 3");
		int preWidth = 0;
		int preHeight = 0;
		List<Size> sizes = parameters.getSupportedPictureSizes();
		Log.d("",TAG+" onCreate 4");
		for (int i = 0; i < sizes.size(); i++) {
			Log.d("",TAG+" onCreate 4 for " + i);
			int width = sizes.get(i).width;
			int height = sizes.get(i).height;
//			Log.d("","CameraActivity width height = " + width +" : "+height);	
			if(preWidth == 0){
				preWidth = width;
				preHeight = height;
			}
			if(preWidth >= width){
				Log.d("","CameraActivity width height result = " + width +" : "+height);	
				preWidth = width;
				preHeight = height;
				parameters.setPictureSize(width, height);
			}
		}		
		Log.d("",TAG+" onCreate 5");
		
		mImageData = new byte[6][];
		Log.d("",TAG+" onCreate 6");
		ibtnRunShutter = (ImageButton) findViewById(R.id.ibtnRunShutter);
		ibtnRunShutter.setOnClickListener(this);		
		ibtnSave = (ImageButton) findViewById(R.id.ibtnSave);
		ibtnSave.setOnClickListener(this);
		ibtnCancel = (ImageButton) findViewById(R.id.ibtnCancel);
		ibtnCancel.setOnClickListener(this);		
		Log.d("",TAG+" onCreate 7");
		ibtnSave.setVisibility(ImageButton.GONE);
		ibtnCancel.setVisibility(ImageButton.GONE);
		Log.d("",TAG+" onCreate 8");
		setSeekBar();
		setSensor();
		
		Log.d("",TAG+" onCreate 9");
		LinearLayout previewLayout = (LinearLayout) findViewById(R.id.previewLayout);
		previewLayout.addView(new DrawView(CameraActivity.this));
	}
	
	class DrawView extends View{

		public DrawView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Paint paint = new Paint();
			paint.setColor(Color.BLUE);
			paint.setTextSize(22);
			paint.setAntiAlias(true);
			
			canvas.drawRect(100, 100, 100, 100, paint);
			canvas.drawText("한글테스트", 100, 100, paint);
		}
		
	}
	
	
	
	
	
	
	int rotation = 0;
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		rotation = this.getWindowManager().getDefaultDisplay().getRotation();
		Log.d("","onConfigurationChanged rotation = " + rotation);
		if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {
			Log.d("","onConfigurationChanged = portrait");
			
			
        }else {
        	Log.d("","onConfigurationChanged = landscape");
        }
	}

	
	/** 移대???以? ProgressBar
	 * 
	 */
	private void setSeekBar(){
//		Camera.Parameters params = camera.getParameters();
		seekbar = (VerticalSeekBar)findViewById(R.id.SeekBar01);
//		if (SCREEN_ORIENT == Configuration.ORIENTATION_LANDSCAPE) {
//			View seekView = seekbar;
//		}
		
		seekbar.setMax(mPreview.maxZoom);
		seekbar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(VerticalSeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(VerticalSeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(VerticalSeekBar seekBar, int progress, boolean fromUser) {
				Log.d("[SeekBar]","onProgressChanged :zoomLevel= " + progress);
			    if( mPreview.mCamera != null){
					Camera.Parameters parameters = mPreview.mCamera.getParameters();
					parameters.set("zoom", progress);
					
//					int preWidth = 0;
//					int preHeight = 0;
//					List<Size> sizes = parameters.getSupportedPictureSizes();
//					for (int i = 0; i < sizes.size(); i++) {
//						int width = sizes.get(i).width;
//						int height = sizes.get(i).height;
//						Log.d("","CameraActivity width height = " + width +" : "+height);
//						if(preWidth == 0){
//							preWidth = width;
//							preHeight = height;
//						}else if(preWidth >= width){
//							
//							parameters.setPictureSize(width, height);
//						}
//					}
					
					mPreview.mCamera.setParameters(parameters);
			    } else {
			    	Log.v("Preview", "Camera is null ");
			    }
			}
		});
		
//		SeekBarListener seekListener = new SeekBarListener(seekbar);
//		seekbar.setMax(max);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.ibtnRunShutter: 
				
				if (mPreview.mCamera != null) {
					
					if(canShutter){
						canShutter = false;
						try {
							mPreview.mCamera.autoFocus(cb);
						} catch (Exception e) {
							Log.e("예외","예외발생");
						}
					}
				}				
			break;
				
			case R.id.ibtnSave:
				
				int result =SaveImage(ffffffffffffff);
				Log.i("파일 저장 결과",result+"");
				Intent past = getIntent();
				if("0003".equals(past.getStringExtra("MainType"))){
					Intent i = getIntent();
					i.putExtra("imgName", mFileimageRoute + ffffffffffffff + ".jpg");
					setResult(RESULT_OK, i);
					finish();			
					return;
				}else{
					Parameters params = new Parameters(ONECLICK_FILE_SEND);
					params.put("rpt_id", SituationService.selectedRpt_id);
					params.put("nscode", ns_code);
					params.put("nsname", ns_name);
					params.put("bhcode", bhCode);
					params.put("bhname", banghyang);
					params.put("ijung", currentIjung);
					
					if (com.ex.situationmanager.util.Configuration.User.getBscode_list() != null) {
						if (com.ex.situationmanager.util.Configuration.User.getBscode_list().size() > 0) {
							params.put("bscode", com.ex.situationmanager.util.Configuration.User.getBscode_list().get(0));
						}
					}
					params.put("patcar_id", com.ex.situationmanager.util.Configuration.User.getPatcar_id());
					
					executeJob(params, CameraActivity.this);
					
					ibtnRunShutter.setVisibility(ImageButton.VISIBLE);
					ibtnSave.setVisibility(ImageButton.GONE);
					ibtnCancel.setVisibility(ImageButton.GONE);
					canShutter = true;
					mPreview.mCamera.startPreview();
				}
				
				
			break;
			
			case R.id.ibtnCancel:
				ibtnRunShutter.setVisibility(ImageButton.VISIBLE);
				ibtnSave.setVisibility(ImageButton.GONE);
				ibtnCancel.setVisibility(ImageButton.GONE);	
				
				canShutter = true;
				
				onResume();
			break;
			
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		return true;
	}

	private int getPreFileName() {
		if (sPrefs == null) {
			sPrefs = getSharedPreferences(KEY_POPUP_ENV, Context.MODE_PRIVATE);
		}
		
		mFileNameYear = sPrefs.getInt(SAVE_FILE_YEAR, 0);
		mFileNameMonth = sPrefs.getInt(SAVE_FILE_MONTH, 0);
		mFileNameDay = sPrefs.getInt(SAVE_FILE_DATE, 0);
		mFileNameCount = sPrefs.getInt(SAVE_FILE_COUNT, 0);
		return mFileNameCount;
	}

	private String getRealFileName() {
		MakeFileName();
		return mFilename;
	}

	private void MakeFileName() {
		DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat
		DecimalFormat NumFormat = new DecimalFormat("0000");// 4
		Calendar rightNow = Calendar.getInstance();// 
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH) + 1;
		int date = rightNow.get(Calendar.DATE);//
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minute = rightNow.get(Calendar.MINUTE);
		int second = rightNow.get(Calendar.SECOND);
		//int rnd = (int) (Math.random() * 99);
		Random r = new Random();
		r.setSeed(new Date().getTime());
		int rnd = Math.abs((int) (r.nextInt() %99)+1);

		int num = getPreFileName();
		String result = Common.FILE_TAG+decimalFormat.format(year)
				+ decimalFormat.format(month) + decimalFormat.format(date)
				+ decimalFormat.format(hour) + decimalFormat.format(minute)
				+ decimalFormat.format(second) + decimalFormat.format(rnd);
		String FormatNum = NumFormat.format(num);
		mFilename = result+"_";

		File[] files = new File(mFileimageRoute).listFiles();
		if(null == files){
			Log.d("","Files null");
		}else{
			if (files.length == 0) {
				num++;
			} else if (files.length > 0) {
				
				if (CompareDate(year, month, date) == true) {
					num++;
				} else if (CompareDate(year, month, date) == false) {
					
					num = 0;
				}
			}
		}

		SaveFileName(year, month, date, num);
	}

	private boolean CompareDate(int year, int month, int date) {
		boolean ret = false;

		if (year == getFileNameYear()) {
			if (month == getFileNameMonth()) {
				if (date == getFileNameDay()) {
					ret = true;
				}
			}
		}

		return ret;
	}

	private int getFileNameYear() {
		return mFileNameYear;
	}

	private int getFileNameMonth() {
		return mFileNameMonth;
	}

	private int getFileNameDay() {
		return mFileNameDay;
	}

	private void SaveFileName(int year, int month, int date, int num) {

		SharedPreferences.Editor editor = sPrefs.edit();
		editor.putInt(SAVE_FILE_YEAR, year);
		editor.putInt(SAVE_FILE_MONTH, month);
		editor.putInt(SAVE_FILE_DATE, date);
		editor.putInt(SAVE_FILE_COUNT, num);
		editor.commit();

	}

	public int SaveImage(String f) {

		int ret = 0;
		try {
			Log.d("","SaveImage filepath  === "+ mFileimageRoute);
			Log.d("","SaveImage filename=== "+  f + ".jpg");
			Log.d("","SaveImage filepath+filename === "+ mFileimageRoute + f + ".jpg");
			
			// data[] 로 넘어온 데이터를 bitmap으로 변환 
			Bitmap bmp = BitmapFactory.decodeByteArray(mImageData[0], 0, mImageData[0].length);
			Bitmap rotateBitmap = null;
			if(rotation == 0){//portrait
				// 화면 회전을 위한 matrix객체 생성
				Matrix m = new Matrix();
				// matrix객체에 회전될 정보 입력
				m.setRotate(90, (float) bmp.getWidth(), (float) bmp.getHeight()); 
				// 기존에 저장했던 bmp를 Matrix를 적용하여 다시 생성
				rotateBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false); 
				// 기존에 생성했던 bmp 자원해제
				bmp.recycle();
			}else if(rotation == 3){
				// 화면 회전을 위한 matrix객체 생성
				Matrix m = new Matrix();
				// matrix객체에 회전될 정보 입력
				m.setRotate(180, (float) bmp.getWidth(), (float) bmp.getHeight()); 
				// 기존에 저장했던 bmp를 Matrix를 적용하여 다시 생성
				rotateBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false); 
				// 기존에 생성했던 bmp 자원해제
				bmp.recycle();
			}


			File folder = new File(mFileimageRoute);
			/*if(folder.exists()){
				Log.d("","folder is exist");
			}else{
				Log.d("","folder is not exist");
			}*/
			
			File[] files = new File(mFileimageRoute).listFiles();
			if(null == files){
				Log.d("","filelist   is null");
			}else{
				for (int i = 0; i < files.length; i++) {
					Log.d("", "filelist = " + files[i].getName());
				}
			}
			
			// 파일로 저장
			OutputStream fileoutstream = new FileOutputStream( mFileimageRoute + f + ".jpg");
			if(rotation == 1){//Landscape left
				fileoutstream.write(mImageData[0]);
				fileoutstream.flush();
			}
			
			if(rotation == 0 || rotation == 3){//portrait || landscape right
				rotateBitmap.compress(CompressFormat.JPEG, 100, fileoutstream);
			}
			fileoutstream.close();
			
			System.gc();
			Common com = new Common();
			com.fileCopyToGallery(mFileimageRoute+f + ".jpg", f + ".jpg" ,"N");
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+Environment.getExternalStorageDirectory())));
			MediaScanner scanner = MediaScanner.newInstance(CameraActivity.this);
			scanner.mediaScanning(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera");
			File file = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera");
			CameraActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)) );
			// MediaStore 미디어 삽입
			ContentValues newValues = new ContentValues(2);
			newValues.put(MediaColumns.MIME_TYPE, "image/jpeg");
			newValues.put(MediaColumns.DATA, Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera"+"/"+f+".jpg");
			
			ContentResolver contentResolver = this.getContentResolver();
			Uri newUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newValues);
			
			this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
			
			
		} catch (FileNotFoundException fne) {
			//Log.e("writing and scanning image ", fne.toString());
			ret = -1;
		} catch (IOException ioe) {
			//Log.e("writing and scanning image ", ioe.toString());
			ret = -1;
		} catch (NullPointerException e) {
			ret = -1;
		}

		return ret;

	}

	Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera c) {
			
		}
	};

	Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera c) {
			
//			m_resWidth = c.getParameters().getPictureSize().width;
//			m_resHeight = c.getParameters().getPictureSize().height;
			
//			String dummy = "dummy";
//			FileOutputStream fos = null;
//			try {
//				fos = new FileOutputStream("/sdcard/DCIM/Camera/" + dummy
//						+ ".jpg");
//				fos.write(data);
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (fos != null) {
//					try {
//						fos.close();
//					} catch (IOException e) {
//					}
//				}
//			}
			

			// 파일로 저장
			mImageData[0] = data;

			ffffffffffffff = getRealFileName();
			//openDialog(f);
			
			//ibtnRunShutter.setAnimation(AnimationUtils.loadAnimation(CameraActivity.this, R.anim.push_up_out));
			ibtnSave.setAnimation(AnimationUtils.loadAnimation(CameraActivity.this, R.anim.fade));
			ibtnCancel.setAnimation(AnimationUtils.loadAnimation(CameraActivity.this, R.anim.fade));
			
			ibtnRunShutter.setVisibility(ImageButton.GONE);
			ibtnSave.setVisibility(ImageButton.VISIBLE);
			ibtnCancel.setVisibility(ImageButton.VISIBLE);	
			mPreview.mCamera.stopPreview();
		}

	};

	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			Log.i(getClass().getSimpleName(), "SHUTTER CALLBACK");
		}
	};
	Camera.AutoFocusCallback cb = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera c) {

			if (success) {

//				ToneGenerator tg = new ToneGenerator(
//						AudioManager.STREAM_SYSTEM, 100);
//				if (tg != null)
//					tg.startTone(ToneGenerator.TONE_PROP_BEEP);
//				gFocussed = true;
				try {
					
					if (mPreview.mCamera != null) {

						mPreview.mCamera.takePicture(mShutterCallback,
								mPictureCallbackRaw, mPictureCallbackJpeg);
					}
				} catch (NullPointerException e) {
					Log.e("에러", "NullPointerException");
				}finally{
					canShutter = true;
				}
			} else {
//				ToneGenerator tg = new ToneGenerator(
//						AudioManager.STREAM_SYSTEM, 100);
//				if (tg != null)
//					tg.startTone(ToneGenerator.TONE_PROP_BEEP);

				try {
					if (mPreview.mCamera != null) {
						mPreview.mCamera.takePicture(mShutterCallback,
								mPictureCallbackRaw, mPictureCallbackJpeg);
					}
				} catch (NullPointerException e) {
					Log.e("에러", "NullPointerException");
				}finally{
					
					canShutter = true;
				}
			}
		}
	};

	protected void onResume() {

		super.onResume();
		if(null != mPreview){
			if(null != mPreview.mCamera){
				mPreview.mCamera.startPreview();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mPreview != null) {
			Log.e("onDestroy", "onDestroy...");
			if (mPreview.mCamera != null) {
				mPreview.mCamera.stopPreview();
				mPreview.mCamera.release();
				mPreview.mCamera = null; // <- nullpointer exception
			}
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		setResult(RESULT_CANCELED, getIntent());
		return super.onKeyDown(keyCode, event);
	}


	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;
	private float[] mGravity = null;
	private float[] mGeomagnetic = null;
	private float RR[] = new float[9];
	private float II[] = new float[9];
	
	public void setSensor(){
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		
//		accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//
//		magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//		mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		
//		List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//		for (int i = 0; i < sensorList.size(); i++) {
//			Sensor s = sensorList.get(i);
//			Log.d("","sensorList name      = " + s.getName());
//			Log.d("","sensorList power     = " + s.getName());
//			Log.d("","sensorList resolution= " + s.getResolution());
//			Log.d("","sensorList range     = " + s.getMaximumRange());
//		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		Log.d(TAG,TAG+" SensorManager onSensorChanged" );
		
		/*if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			mGravity = event.values;
			Log.d(TAG,TAG+" SensorManager onSensorChanged ACC" );
		}
		
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			mGeomagnetic = event.values;
			Log.d(TAG,TAG+" SensorManager onSensorChanged magnetic" );
		}
		
		if (mGravity != null && mGeomagnetic != null) {
			boolean success = SensorManager.getRotationMatrix(RR, II, mGravity, mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(RR, orientation);
				double degreez = Math.toDegrees(orientation[0]);
				double degreeX = Math.toDegrees(orientation[1]);
				double degreeY =  Math.toDegrees(orientation[2]);
				Log.d(TAG,TAG+" SensorManager degreeY = " + degreez+" : "+ degreeY+" : "+ degreeY);
			}
		}*/
		
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}


	@Override
	protected void setActivityViewEdit(Activity activity) throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onActionPost(String primitive, XMLData result, Exception e) throws IOException {
		// TODO Auto-generated method stub
		
	}
	

}