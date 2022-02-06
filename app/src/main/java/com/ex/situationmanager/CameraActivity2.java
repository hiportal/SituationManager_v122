package com.ex.situationmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ex.situationmanager.BaseActivity.DoComplecatedJob;
import com.ex.situationmanager.multiphoto.DrawView;
import com.ex.situationmanager.service.Parameters;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.MediaScanner;
import com.ex.situationmanager.util.Preview;
import com.ex.situationmanager.util.Preview2;
import com.ex.situationmanager.util.VerticalSeekBar;


@SuppressLint("ResourceAsColor")
public class CameraActivity2 extends BaseActivity implements OnClickListener, SensorEventListener {
	
	private Preview2 mPreview;
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
	private String mFileimageRouteTag = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/situationmanager_tag/";
	
	private Camera camera;
	private int m_resWidth;
	private int m_resHeight;
	
	ImageButton ibtnRunShutter;
	ImageButton ibtnSave;
	ImageButton ibtnCancel;
	ImageView flashOnOff;
	
	boolean directSendYN = true;
	
	boolean canShutter = true;
	
	String resultFileName;
	
	VerticalSeekBar seekbar;
	private static String TAG = "CameraActivity2";;
	View adview;
	
	//센서
	public int SCREEN_ORIENT;

	//현재 사용중인 CameraActivity!
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera2);
		File dir = new File(mFileimageRoute);
		if(!dir.isDirectory()){
			dir.mkdirs();
			Log.d("","Camera File not Exist!"+mFileimageRoute);
		}else{
			Log.d("","Camera File Exist!");
		}
		
		File dir2 = new File(mFileimageRouteTag);
		if(!dir2.isDirectory()){
			dir2.mkdirs();
			Log.d("","Camera File not Exist!"+mFileimageRouteTag);
		}else{
			Log.d("","Camera File Exist!");
		}
		
//		adview = new DrawView(CameraActivity2.this);
//		adview.setDrawingCacheEnabled(true);
//		addContentView(adview, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//		adview.layout(0, 0, adview.getWidth(), adview.getHeight());
//		adview.setBackgroundColor(android.R.color.transparent);
		
		Resources resource = Resources.getSystem();
        Configuration config = resource.getConfiguration();
        SCREEN_ORIENT = config.orientation;

        onConfigurationChanged(config);
		
		Log.d("",TAG+" onCreate 1");

		mPreview = (Preview2) findViewById(R.id.camera_preview);
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
//		for (int i = 0; i < sizes.size(); i++) {
//			Log.d("",TAG+" onCreate 4 for " + i);
//			int width = sizes.get(i).width;
//			int height = sizes.get(i).height;
//			if(preWidth == 0){
//				preWidth = width;
//				preHeight = height;
//			}
//			if(preWidth >= width){
//				Log.d("","CameraActivity2 width height result = " + width +" : "+height);	
//				preWidth = width;
//				preHeight = height;
//				parameters.setPictureSize(width, height);
//			}
//			
//		}
		for (int i = 0; i < sizes.size(); i++) {
			int width = sizes.get(i).width;
			int height = sizes.get(i).height;
			
			Log.d("","Preview2 width height result = " + width +" : "+height);
			
			if(width > 1000 && width < 2000){
				if(preWidth == 0){
					preWidth = width;
					preHeight = height;
				}
				if(preWidth >= width){
					parameters.setPictureSize(width, height);
					Log.d("","picturesize result  = " + width +" : "+height);
				}
				preWidth = width;
				preHeight = height;
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
		
		flashOnOff = (ImageView) findViewById(R.id.flashOnOff);
		flashOnOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPreview.mCamera.getParameters().getFlashMode().equals(android.hardware.Camera.Parameters.FLASH_MODE_TORCH)){
					mPreview.FlashOff();
				}else{
					mPreview.FlashOn();
				}
				
			}
		});
		
		Intent i = getIntent();
		directSendYN = i.getBooleanExtra("directSendYN", true);
		
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
//						Log.d("","CameraActivity2 width height = " + width +" : "+height);
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
							//e.printStackTrace();
							Log.e("에러","예외 발생");
							
						}
					}
				}				
				
			break;
				
			case R.id.ibtnSave:
				//태그파일 정보 날짜 시간 위치
//				saveViewToImage(adview);
				SaveImage(resultFileName);
//				Bitmap originFile = BitmapFactory.decodeFile(mFileimageRoute + resultFileName + ".jpg");
//				Bitmap tagFile = BitmapFactory.decodeFile(mFileimageRouteTag+"tag.jpg");
//				Bitmap resized = Bitmap.createScaledBitmap(tagFile, 380, 620, true);
//				int m_resWidth = 860;
//				int m_resHeight = 480;
				
				
//				overlayMark(originFile, resized,  mFileimageRoute + resultFileName+".jpg");
				
//				combineImage(originFile, tagFile, true, mFileimageRouteTag + resultFileName+"tag.jpg");
				
				Parameters params = new Parameters(ONECLICK_FILE_SEND);
				params.put("rpt_id", SituationService.selectedRpt_id);
				params.put("nscode", ns_code);
				params.put("nsname", ns_name);
				params.put("bhcode", bhCode);
				params.put("bhname", banghyang);
				params.put("ijung", currentIjung);

				//파람 정보에 위도경도값 넣어준다.
				/*
				params.put("latitude",new Double(latitude).toString());
				params.put("longitude",new Double(longitude).toString());
				*/


				if (com.ex.situationmanager.util.Configuration.User.getBscode_list() != null) {
					if (com.ex.situationmanager.util.Configuration.User.getBscode_list().size() > 0) {
						params.put("bscode", com.ex.situationmanager.util.Configuration.User.getBscode_list().get(0));
					}
				}
				params.put("patcar_id", com.ex.situationmanager.util.Configuration.User.getPatcar_id());
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//파일전송 주석 풀기
				if(true == directSendYN){
					executeJob(params, CameraActivity2.this);
				}else{
					Intent i = getIntent();
					i.putExtra("imgName", filefullpath);
					setResult(RESULT_OK, i);
					finish();					
//					finish();
				}
				
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				//**************************************************************************************************************
				
//				Intent i = getIntent();
//				i.putExtra("imgName", mFileimageRoute + f + ".jpg");
//				setResult(RESULT_OK, i);
//				finish();			
				
				ibtnRunShutter.setVisibility(ImageButton.VISIBLE);
				ibtnSave.setVisibility(ImageButton.GONE);
				ibtnCancel.setVisibility(ImageButton.GONE);
				canShutter = true;
				mPreview.mCamera.startPreview();
				
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
	
	public void refreshSD() {
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
				Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	}
	
	private void saveViewToImage( View view ) 
    { 
       
		Log.d("", TAG+ "saveViewToImage width,height = " +view.getWidth()+":"+view.getHeight());
       Bitmap  b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
       if(b!=null){
       try { 
	       File f = new File(mFileimageRouteTag);//디렉토리경로
	       f.mkdir();
	       File f2 = new File(mFileimageRouteTag+"tag.jpg");//디렉토리경로+파일명
	       
	       Canvas c = new Canvas( b ); 
	       view.draw( c ); 
	       FileOutputStream fos = new FileOutputStream(f2);
	
	                if ( fos != null ) 
	                { 
	                        b.compress(Bitmap.CompressFormat.PNG, 100, fos );

	                        fos.close(); 
	                } 
	                //setWallpaper( b ); 
	       
	       } catch( Exception e ){ 
	                Log.e("testSaveView", "Exception: " + e.toString() ); 
	       } 
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

	String filefullpath = "";
	public int SaveImage(String sFilename) {
		int ret = 0;
		try {
			Log.d("camera","SaveImage filepath  === "+ mFileimageRoute);
			Log.d("camera","SaveImage filename=== "+  sFilename + ".jpg");
			Log.d("camera","SaveImage filepath+filename === "+ mFileimageRoute + sFilename + ".jpg");
			filefullpath = mFileimageRoute + sFilename + ".jpg";
			
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
			if(folder.exists()){
				Log.d("","folder is exist");
			}else{
				Log.d("","folder is not exist");
			}
			
			File[] files = new File(mFileimageRoute).listFiles();
			if(null == files){
				Log.d("","filelist   is null");
			}else{
				for (int i = 0; i < files.length; i++) {
					Log.d("", "filelist = " + files[i].getName());
				}
			}
			
			// 파일로 저장
			OutputStream fileoutstream = new FileOutputStream( mFileimageRoute + sFilename + ".jpg");
			if(rotation == 1){//Landscape left
				fileoutstream.write(mImageData[0]);
				fileoutstream.flush();
			}


			//여기서 퀄리티를 낮춘다(파일 용량을 줄이기위해서)
			if(rotation == 0 || rotation == 3){//portrait || landscape right
				rotateBitmap.compress(CompressFormat.JPEG, 100, fileoutstream);
			}
			fileoutstream.close();
			
			System.gc();
			Common com = new Common();
			com.fileCopyToGallery(mFileimageRoute+sFilename + ".jpg", sFilename + ".jpg" ,"N");
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+Environment.getExternalStorageDirectory())));
			MediaScanner scanner = MediaScanner.newInstance(CameraActivity2.this);
			scanner.mediaScanning(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera");
			File file = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera");
			CameraActivity2.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)) );
			// MediaStore 미디어 삽입
			ContentValues newValues = new ContentValues(2);
			//newValues.put(MediaColumns.MIME_TYPE, "image/jpeg");
			newValues.put(MediaColumns.MIME_TYPE, "image/jpg");
			newValues.put(MediaColumns.DATA, Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera"+"/"+sFilename+".jpg");
			
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
	
	/** 이미지 겹치기
	 * @param bmp1
	 * @param bmp2
	 * @param savePath
	 * @return
	 */
	private Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2, String savePath){ 
	    Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig()); 
	    Canvas canvas = new Canvas(bmOverlay); 
	    canvas.drawBitmap(bmp1, 0, 0, null);
	    canvas.drawBitmap(bmp2, 0, 0, null);
	    try {
	    	FileOutputStream fos = new FileOutputStream(savePath);
	    	bmOverlay.compress(CompressFormat.JPEG, 50, fos);
	    	fos.close();
	    	bmOverlay.recycle();
		} catch (FileNotFoundException e) {
			Log.e("에러","예외");
		} catch (IOException e) {
			Log.e("에러","예외");
		}
	    return bmOverlay; 
	} 
	
	/**이미지 붙이기
	 * @param first
	 * @param second
	 * @param isVerticalMode
	 * @param savePath
	 */
	private void combineImage(Bitmap first, Bitmap second, boolean isVerticalMode, String savePath){
		Options option = new Options();
		option.inDither = true;
		option.inPurgeable = true;
		Bitmap bitmap = null;
		if(isVerticalMode)
			bitmap = Bitmap.createScaledBitmap(first, first.getWidth(), first.getHeight()+second.getHeight(), true);
		else
			bitmap = Bitmap.createScaledBitmap(first, first.getWidth()+second.getWidth(), first.getHeight(), true);
		 
		Paint p = new Paint();
		p.setDither(true);
		p.setFlags(Paint.ANTI_ALIAS_FLAG);
		Canvas mCanvas = new Canvas(bitmap);
		mCanvas.drawBitmap(first, 0, 0, p);
		if(isVerticalMode)
			mCanvas.drawBitmap(second, 0, first.getHeight(), p);
		else
			mCanvas.drawBitmap(second, first.getWidth(), 0, p);
		 
		first.recycle();
		second.recycle();
		try{
			FileOutputStream fos = new FileOutputStream(savePath);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			 
			fos.close();
			bitmap.recycle();
		}catch(IOException e){
			Log.e("에러","IOException ");
		}
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

			resultFileName = getRealFileName();
			//openDialog(f);
			
			//ibtnRunShutter.setAnimation(AnimationUtils.loadAnimation(CameraActivity2.this, R.anim.push_up_out));
			ibtnSave.setAnimation(AnimationUtils.loadAnimation(CameraActivity2.this, R.anim.fade));
			ibtnCancel.setAnimation(AnimationUtils.loadAnimation(CameraActivity2.this, R.anim.fade));
			
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
					Log.e("에러","예외");
					//Log.d("Camera", e.toString());
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
					Log.e("에러","예외");
					//Log.d("Camera", e.toString());
				}finally{
					canShutter = true;
				}
			}

		}
	};

	protected void onResume() {

		super.onResume();
		mPreview.mCamera.startPreview();
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