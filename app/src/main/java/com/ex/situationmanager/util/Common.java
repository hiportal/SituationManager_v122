package com.ex.situationmanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

import com.ex.situationmanager.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/** 파일, SharePreference, String 관련
 * @author JSJ
 *
 */
public class Common {

	public final static String					PREF_SITUATIONMANAGER  				= "com.ex.situationmanager";
	
	public	static String FILE_DIR	=  Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/situationmanager/";
	public	static String FILE_DIR_TAG	=  Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/situationmanager_tag/";
	public static String FILE_TAG = "";
    //2015-09-08 db파일 버전 관리
    public final String DB_VERSION = "5.0.7";//
    static SharedPreferences pref ;//DB용
    
    Editor ed;

    public final String UPPER_BANGHYANG = "구리";
	public final String MIDDLE_BANGHYANG = "일산";
	public final String DOWN_BANGHYANG = "판교";
	
    Context context;
    
    
	public Common(){
    }

    public Common(Context context){
        this.context = context;
    }


    public void copyFile(String fileName)
    {
    	File file = new File("/data/data/"+PREF_SITUATIONMANAGER+"/databases/exApp.db");
		pref = context.getSharedPreferences(""+PREF_SITUATIONMANAGER+"", context.MODE_PRIVATE);
        ed = pref.edit();
		if (!file.exists()){
			
			InputStream is = null;
			FileOutputStream fos = null;
			File outDir = new File("/data/data/"+PREF_SITUATIONMANAGER+"/databases/");
			outDir.mkdirs();
				 
			try {
				is = context.getResources().getAssets().open("exApp.mp4");
//				int size = is.available();
				int size = 2048;
				byte[] buffer = new byte[size];
				File outfile = new File(outDir + "/" + "exApp.db");
				//File outfile = new File(DB_FILE);
				fos = new FileOutputStream(outfile);
				
				for (int c = is.read(buffer); c != -1; c = is.read(buffer)){
					fos.write(buffer, 0, c);
				}
				
				is.close();
				fos.close();
				Log.i("","copyFile file copy complete");
			} catch (IOException e) {
				//TODO Auto-generated catch block
				Log.e("예외","예외발생");
			}finally{
				//2014-01-13
				ed.putString("DB_VERSION", DB_VERSION);
				ed.commit();
			}
			//2014-01-13 db파일이 존재할때
		}else{
			Log.i("","DbFile exist update status check start ##########################");
			boolean isUpdated = false;
			
	        
			if(pref.contains("DB_VERSION") && pref.getString("DB_VERSION", "").equals(DB_VERSION)){
				isUpdated = false;
				Log.i("dbfile","DbFile exist update status check DBFILE not update");
				Log.i("dbfile","DbFile exist update status check DBFILE not update" + pref.getString("DB_VERSION", ""));
			}else{
				isUpdated = true;
				Log.i("dbfile","DbFile exist update status check DBFILE update");
				Log.i("dbfile","DbFile exist update status check DBFILE update" + pref.getString("DB_VERSION", ""));
			}
			
			if(isUpdated){
				File uptDbFile  = new File("/data/data/"+PREF_SITUATIONMANAGER+"/databases/exApp.db");
				
				//파일삭제후 Asset 폴더의 DB-> database 에 밀어넣기
				uptDbFile.delete();
				InputStream is = null;
				FileOutputStream fos = null;
				File outDir = new File("/data/data/"+PREF_SITUATIONMANAGER+"/databases/");
				outDir.mkdirs();
				
				try {
					is = context.getResources().getAssets().open("exApp.mp4");
					int size = is.available();
					byte[] buffer = new byte[size];
					File outfile = new File(outDir + "/" + "exApp.db");
					//File outfile = new File(DB_FILE);
					fos = new FileOutputStream(outfile);
					
					for (int c = is.read(buffer); c != -1; c = is.read(buffer)){
						fos.write(buffer, 0, c);
					}
					
					is.close();
					fos.close();

				} catch (IOException e) {
					//TODO Auto-generated catch block
					Log.e("예외","예외발생");
				}finally{
					//2014-01-13
					ed.putString("DB_VERSION", DB_VERSION);
					ed.commit();
				}
				
				ed.putString("DB_VERSION", DB_VERSION);
				ed.commit();	
            }
        }
    }
    
    
    public static final String nullZeroCheck(String nullStr){
    	if (nullStr != null && !nullStr.equals("")) {
			if("null".equals(nullStr)){
				return "";
			}else if("0".equals(nullStr)){
				return "";
			}
			return nullStr;
		}
		return "";
    }
    //String 값 null값 처리.
    public static final String nullCheck(String nullStr) {
		if (nullStr != null && !nullStr.equals("")) {
			if("null".equals(nullStr)){
				return "";
			}
			return nullStr;
		}
		return "";
	}
    //String to Double + null 체크
    public static final double nullCheckDouble(String nullStr) {
    	if (nullStr != null && !nullStr.equals("")) {
    		try {
				
    			return Double.parseDouble(nullStr);
			} catch (NumberFormatException | NullPointerException e) {
				return 0.0;
			}
    	}
    	return 0.0;
    }
    
    public static final int nullCheckDelayY(String nullStr) {
    	if (nullStr != null && !nullStr.equals("")) {
    		try {
				
    			return Integer.parseInt(nullStr);
			} catch (NumberFormatException | NullPointerException e) {
				return 4000;
			}
    	}
    	return 4000;
    }
    
    public static final double nullCheckDelayN(String nullStr) {
    	if (nullStr != null && !nullStr.equals("")) {
    		try {
				
    			return Integer.parseInt(nullStr);
			} catch (NumberFormatException | NullPointerException e) {
				return 60000;
			}
    	}
    	return 60000;
    }
    
    public static final String nullTrim(String nullStr){
    	if (nullStr != null && !nullStr.equals("")) {
			if("null".equals(nullStr)){
				return "";
			}
			return nullStr.replace(" ","");
		}
		return "";
    }
    
    
  //---------------------------------------------------------------------4.SharedPreference---------------------------------------------------------------------	
    public final static String					PREF_SITUATIONMANAGER_UTIL  				= "com.ex.situationmanager.util";
    private static SharedPreferences mPref ;//UTIL용
    
  		/**SharedPreference 생성
  		 * @param context
  		 */
  		public static void createPreferences(Context context) {
  			if(mPref == null) {
  				
  				File f = new File("/data/data/" + context.getPackageName() + "/shared_prefs/");
  				if(f!=null && f.exists()) {
  					File[] ff = f.listFiles();
  				}
  			}
  		}
  		/** SharedPreference 작성
  		 * @param context
  		 * @param value
  		 */
  		public static void setPrefString(Context context, String key, String value) {
  			if(mPref == null) {
  				mPref = PreferenceManager.getDefaultSharedPreferences(context);
  			}
  			SharedPreferences.Editor editor = mPref.edit();
  			editor.putString(key, value);
  			editor.commit();
  		}
  		
  		/** SharedPreference 값 가져오기
  		 * @param context
  		 * @param key
  		 * @return
  		 */
  		public static String getPrefString(Context context,String key) {
  			if(mPref == null) {
  				mPref = PreferenceManager.getDefaultSharedPreferences(context);
  			}
  			return mPref.getString(key, "");
  		}
  		

  		 public static String[] split(String sString, String sDelim) {

  	        if (isEmpty(sString) || sString.indexOf(sDelim) < -1) {
  	            return null;
  	        }
  	        
  	        StringTokenizer stringtokenizer = new StringTokenizer(sString, sDelim);
  	        int iTokenCount = stringtokenizer.countTokens();

  	        if (iTokenCount <= 0) {
  	            return null;
  	        }

  	        String sResults[] = new String[iTokenCount];
  	        int i = 0;

  	        while (stringtokenizer.hasMoreTokens()) {
  	        	sResults[i++] = stringtokenizer.nextToken();
  	        }

  		        return sResults;
  		 }
  		 
  		/**
  		 * 해당 문자열에 값이 있는지 확인한다.
  		 * @return
  		 */
  		public static boolean isEmpty(String sStr) {
  			boolean bResult = false;
  			
  			if (sStr == null || sStr.trim().equals("")) {
  				bResult = true;
  			}
  			
  			return bResult;
  		}
  		
  		/** 하위파일 삭제
  		 * @param path
  		 * @return
  		 */
  		public static boolean DeleteDir(String path) {

  			boolean rtn = false;
  			
  			File file = new File(path);
  			if(file.exists()){
  			
  				File[] childFileList = file.listFiles();
  				
  				for (File childFile : childFileList) {
  					
  					if (childFile.isDirectory()) {
  						DeleteDir(childFile.getAbsolutePath()); // 하위 디렉토리 루프
  					} else {
  						childFile.delete(); // 하위 파일삭제
  						
  					}
  				}
  				
//  				if(file.delete()){ // root 삭제
  					rtn = true;
//  				}
  			}
  			return rtn;
  		}
  		
  		
  		/**
  		 * 다이얼 로그 박스
  		 * 
  		 * @param message
  		 */
  		public static void openWarnDialog(Context ctx, String message) {
  			final Dialog dialogWarn = new Dialog(ctx,  R.style.FullHeightDialog);
  	        
  			dialogWarn.requestWindowFeature(Window.FEATURE_NO_TITLE);
  			dialogWarn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));	                
  	        
  			dialogWarn.setContentView(R.layout.cust_dialog_warn);
  			dialogWarn.setCancelable(false);

//  	        RelativeLayout layout = (RelativeLayout) dialogWarn.findViewById(R.id.RelativeLayout01);
  	        ImageButton button = (ImageButton) dialogWarn.findViewById(R.id.ibtnMovie);
  	        
  	        TextView tvPrompt = (TextView) dialogWarn.findViewById(R.id.tvPrompt);
//  	        if(message.length() > 45){
//  	        	tvPrompt.setTextSize(14);
//  	        }
  	        tvPrompt.setText(message);
  	                    
  	        button.setOnClickListener(new OnClickListener() {
  	        @Override
  	            public void onClick(View v) {
  	        		dialogWarn.dismiss();
  	            }
  	        });				                

  	        dialogWarn.show();	
  		}	

  		
  		/**사진 - 앨범에서 선택*/
  		public void cameraPicRequestSelect(Activity activity, Intent data){
  			String orgFile	= getFilePath(data.getData(), activity);
  			File temp = new File(orgFile);
  			String tempName = temp.getName();
  			String ext = tempName.substring(tempName.indexOf("."));
  			
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
			int rnd =Math.abs((int) (r.nextInt() %99)+1);
  			String result = Common.FILE_TAG+decimalFormat.format(year)
  					+ decimalFormat.format(month) + decimalFormat.format(date)
  					+ decimalFormat.format(hour) + decimalFormat.format(minute)
  					+ decimalFormat.format(second) + decimalFormat.format(rnd);
  			tempName = result+ext;
  			Log.d("","cameraPicRequestSelect = 5");
  			fileCopy2(orgFile.toString(), tempName ,"N");
  			fileCopyToGallery(orgFile.toString(), tempName ,"N");
  			
//  			StatFs staFs = new StatFs(System.getenv("SECONDARY_STORAGE"));
//   	        long totalSize = (long)staFs.getBlockSizeLong() * staFs.getBlockCountLong();
//   	        System.out.println("secondary_storage path size = " + totalSize);
//   	        if(totalSize > 1024){
//   	        	fileCopyToSdcard(orgFile.toString(), tempName ,"N");
//   	        }
   	        
  			
  			

  			// MediaStore 미디어 삽입
  			ContentValues newValues = new ContentValues(2);
  			newValues.put(MediaColumns.MIME_TYPE, "image/jpeg");
  			newValues.put(MediaColumns.DATA, Configuration.directoryName+"/"+FILE_TAG+tempName);
  			ContentResolver contentResolver = activity.getContentResolver();
  			Uri newUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newValues);
  			//최창유 LocalBroadCastManager 적용
			LocalBroadCastManager.getInstance(context).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

			//기존
  			//activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
  		}
  		
  		/**동영상 - 앨범에서 선택*/
  		public void cameraVicRequestSelect(Activity activity, Intent data){
  			
  			String orgFile	= getFilePath(data.getData(), activity);
  			
  			File temp = new File(orgFile);
  			fileCopy2(orgFile, FILE_TAG+temp.getName(), "N");

  			// MediaStore 미디어 삽입
  			ContentValues newValues = new ContentValues(5);
  			newValues.put(MediaStore.MediaColumns.TITLE, "RecordedVideo");
  			newValues.put(MediaStore.Video.Media.DISPLAY_NAME, FILE_TAG+temp.getName());
  			newValues.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
  			newValues.put(MediaColumns.MIME_TYPE, "video/mp4");
  			newValues.put(MediaColumns.DATA, Configuration.directoryName+"/"+FILE_TAG+temp.getName());
  			
  			ContentResolver contentResolver = activity.getContentResolver();
  			Uri newUri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, newValues);

			Log.i("123123",41+"");
			LocalBroadCastManager.getInstance(context).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

  			//최창유 주석
  			//activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
  		}
  		
  		/**사진 - 촬영*/
  		public void cameraPicRequest(Activity activity, Intent data){
  			
  			String saveFile = data.getExtras().getString("imgName");
  			
//  			File temp = new File(saveFile);
//  			fileCopy2(temp.getName(),temp.getName(),"Y");
  			
  			// MediaStore 미디어 삽입
  			ContentValues newValues = new ContentValues(2);
  			newValues.put(MediaColumns.MIME_TYPE, "image/jpeg");
  			newValues.put(MediaColumns.DATA, "/mnt"+saveFile);
  			
  			ContentResolver contentResolver = activity.getContentResolver();
  			Uri newUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newValues);

  			//activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
			Log.i("123123",3+"");
			//신규 localBroadCast
			LocalBroadCastManager.getInstance(context).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,  Uri.parse("file://"+Configuration.directoryName)));

			//기존 사용
  			//activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+Configuration.directoryName)));
  		}
  		
  		/**동영상 - 촬영*/
  		public void cameraVicRequest(Activity activity, Intent data){
  			
  			Image image	= Common.getVideoInfo(data.getData(), activity, "");
  			
  			fileCopy2(""+image.getFilePath(), FILE_TAG+image.getFileName().toString(), "Y");
  			
  			// MediaStore 미디어 삽입
  			ContentValues newValues = new ContentValues(5);
  			newValues.put(MediaStore.MediaColumns.TITLE, "RecordedVideo");
  			newValues.put(MediaStore.Video.Media.DISPLAY_NAME, FILE_TAG+image.getFileName().toString());
  			newValues.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
  			newValues.put(MediaColumns.MIME_TYPE, "video/mp4");
  			newValues.put(MediaColumns.DATA, Configuration.directoryName+"/"+FILE_TAG+image.getFileName().toString());
  			
  			ContentResolver contentResolver = activity.getContentResolver();
  			Uri newUri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, newValues);

			Log.i("123123",2+"");
			LocalBroadCastManager.getInstance(context).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

  			//기존 사용
  			//activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
  		}
  		
  		public  String getFilePath(Uri contentUri, Context context){
  			// can post image
  			String[]  proj = {	MediaStore.Images.Media.DATA,
  								MediaStore.Video.Media.DATA };
  			
  			Cursor cursor = ((Activity) context).managedQuery(contentUri, proj,null,null,null);
  			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
  			cursor.moveToFirst();
  			
  			String fileName = cursor.getString(column_index);
  			//ManagedQuery Close 제외
//  			cursor.close();

  			return fileName;
  		}
  		
  		
  		/** 파일 복사2
  		 * @param realFile(실제 파일명)-경로포함
  		 * @param saveFile(복사될 파일명)
  		 * @param delete(실제파일 삭제여부)
  		 */
  		public void fileCopy2(String realFile, String saveFile, String delete){

  			File file = new File(realFile);
  			File saveFullPath =  new File(Configuration.directoryName +"/"+saveFile);

  			Log.d("","filecopy2 = "+ realFile);
  			Log.d("","filecopy2 = "+ saveFile);
  			
  			String filePath = Configuration.directoryName +"/"+saveFile;
  			try {
  				File fileDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/") + 1));
  				if(!fileDirectory.exists()){
  					fileDirectory.mkdirs();
  				}

  				FileInputStream inputStream = new FileInputStream(file);
  				FileOutputStream outputStream = new FileOutputStream(saveFullPath);
  				
  				FileChannel fcin = inputStream.getChannel();
  				FileChannel fcout = outputStream.getChannel();
  				
  				long size = fcin.size();
  				fcin.transferTo(0, size, fcout);
  				
  				fcout.close();
  				fcin.close();
  				outputStream.close();
  				inputStream.close();
  				
  				if(delete=="Y"){
  					file.delete();
  				}
  				
  			} catch (IOException e) {
  				// TODO Auto-generated catch block

  				Log.e("","filecopy2 = "+e.toString());
  			}
  		}
  		
  		/** 파일 복사 촬영된 사진을 갤러리로 저장.
  		 * @param realFile(실제 파일명)-경로포함
  		 * @param saveFile(복사될 파일명)
  		 * @param delete(실제파일 삭제여부)
  		 */
  		public void fileCopyToGallery(String realFile, String saveFile, String delete){
  			
  			File file = new File(realFile);
  			File saveFullPath =  new File(Configuration.directoryName_Camera +"/"+saveFile);
  			
  			Log.d("","fileCopyToGallery = "+ realFile);
  			Log.d("","fileCopyToGallery = "+ saveFile);
  			
  			String filePath = Configuration.directoryName_Camera +"/"+saveFile;
  			try {
  				File fileDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/") + 1));
  				if(!fileDirectory.exists()){
  					fileDirectory.mkdirs();
  				}
  				
  				FileInputStream inputStream = new FileInputStream(file);
  				FileOutputStream outputStream = new FileOutputStream(saveFullPath);
  				
  				FileChannel fcin = inputStream.getChannel();
  				FileChannel fcout = outputStream.getChannel();
  				
  				long size = fcin.size();
  				fcin.transferTo(0, size, fcout);
  				
  				fcout.close();
  				fcin.close();
  				outputStream.close();
  				inputStream.close();
  				
  				if(delete=="Y"){
  					file.delete();
  				}
  				
  			} catch (IOException e) {
  				// TODO Auto-generated catch block
  				Log.e("","fileCopyToGallery = 예외");
  			}
  		}
  		
  		/** 파일 복사 촬영된 사진을 원클릭 폴더로 저장.
  		 * @param realFile(실제 파일명)-경로포함
  		 * @param delete(실제파일 삭제여부)
  		 */
  		public void fileCopyToSituation(String realFile, String saveName, String delete){
  			
  			File file = new File(realFile);
  			File saveFullPath =  new File(Configuration.directoryName +"/"+saveName);
  			
  			Log.d("","fileCopyToSituation = "+ realFile);
  			Log.d("","fileCopyToSituation = "+ saveName);
  			
  			String filePath = Configuration.directoryName +"/"+saveName;
  			try {
  				File fileDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/") + 1));
  				if(!fileDirectory.exists()){
  					fileDirectory.mkdirs();
  				}
  				
  				FileInputStream inputStream = new FileInputStream(file);
  				FileOutputStream outputStream = new FileOutputStream(saveFullPath);
  				
  				FileChannel fcin = inputStream.getChannel();
  				FileChannel fcout = outputStream.getChannel();
  				
  				long size = fcin.size();
  				fcin.transferTo(0, size, fcout);
  				
  				fcout.close();
  				fcin.close();
  				outputStream.close();
  				inputStream.close();
  				
  				if(delete=="Y"){
  					file.delete();
  				}
  				
  			} catch (IOException e) {
  				// TODO Auto-generated catch block
  				Log.e("","fileCopyToGallery = "+"예외");
  			}
  		}
  		
  		
  		//외장 sd카드에 저장.
  		public void fileCopyToSdcard(String realFile, String saveFile, String delete){
  			
  			File file = new File(realFile);
  			File saveFullPath =  new File( System.getenv("SECONDARY_STORAGE") +"/"+saveFile );
  			
  			Log.d("","fileCopyToGallery = "+ realFile);
  			Log.d("","fileCopyToGallery = "+ saveFile);
  			
  			String filePath = Configuration.directoryName_Camera +"/"+saveFile;
  			try {
  				File fileDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/") + 1));
  				if(!fileDirectory.exists()){
  					fileDirectory.mkdirs();
  				}
  				
  				FileInputStream inputStream = new FileInputStream(file);
  				FileOutputStream outputStream = new FileOutputStream(saveFullPath);
  				
  				FileChannel fcin = inputStream.getChannel();
  				FileChannel fcout = outputStream.getChannel();
  				
  				long size = fcin.size();
  				fcin.transferTo(0, size, fcout);
  				
  				fcout.close();
  				fcin.close();
  				outputStream.close();
  				inputStream.close();
  				
  				if(delete=="Y"){
  					file.delete();
  				}
  				
  			} catch (IOException e) {
  				// TODO Auto-generated catch block
  				Log.e("","fileCopyToGallery = "+"예외");
  			}
  			
  			
  			
  		}
  		
  		public static Image getVideoInfo(Uri uri, Context context, String query) {
  			
  			Image image = null;
  			Cursor thumbCursor = null;
  			Cursor c = null;
  			
  			try{
  			
  				// Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
  				image = new Image();
  		
  				String[] proj = { MediaStore.Video.VideoColumns._ID,
  						MediaStore.Video.VideoColumns.DATA,
  						MediaStore.Video.VideoColumns.DISPLAY_NAME };
  		
  				long videoId = 0;
  				String videoName = "";
  				String videoPath = "";
  		
  				
  		
  				if (query.equals("")) {
  					thumbCursor = ((Activity) context).managedQuery(uri, proj, null, null, null);
  				} else {
  					String selection = MediaStore.Video.VideoColumns.DISPLAY_NAME+ "='" + query + "'";
  					thumbCursor = ((Activity) context).managedQuery(uri, proj, selection, null, null);
  					
  				}
  		
  				if (thumbCursor != null && thumbCursor.moveToFirst()) { // 처음부터
  					// 저장된.
  					int id = thumbCursor
  							.getColumnIndex(MediaStore.Video.VideoColumns._ID);
  					int pathId = thumbCursor
  							.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
  					int nameId = thumbCursor
  							.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME);
  		
  					videoId = thumbCursor.getLong(id);
  					videoPath = thumbCursor.getString(pathId);
  					videoName = thumbCursor.getString(nameId);
  		
  					image.setFilePath(videoPath);
  					image.setFileName(videoName);
  		
  				}
  				//thumbCursor.close();
  		
  				if (videoName != "" && videoPath != "") {
  					// TODO
  				}
  		
  				String[] THUMB_PROJECTION = new String[] {MediaStore.Video.Thumbnails._ID,MediaStore.Video.Thumbnails.DATA };
  		
  				Uri thumbUri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
  				ContentResolver cr = context.getContentResolver();
  				c = cr.query(thumbUri, THUMB_PROJECTION,MediaStore.Video.Thumbnails.VIDEO_ID + "=?",new String[] { String.valueOf(videoId) }, null);

  				BitmapFactory.Options options1 = new BitmapFactory.Options();
  				options1.inSampleSize = 6;
  				
  				Bitmap bmp1 = MediaStore.Video.Thumbnails.getThumbnail(cr, videoId, MediaStore.Images.Thumbnails.MICRO_KIND, options1);
  				
  				Log.d("timer", "bmp1"+bmp1.getHeight());				
  				
  				if (c.moveToNext()) {
  					long id = c.getLong(0);

  					int dataId = c.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
  					String strThumPath = c.getString(dataId);

  					ContentResolver crThumb = context.getContentResolver();
  					BitmapFactory.Options options = new BitmapFactory.Options();
  					options.inSampleSize = 6;
  					Bitmap bmp = MediaStore.Video.Thumbnails.getThumbnail(crThumb,
  							videoId, MediaStore.Video.Thumbnails.MICRO_KIND,
  							options);
  					image.setThumbnail(bmp);
  					
  					//Log.d("KIM", "videoThumId = " + id );
  					//Log.d("KIM", "videoThumPath = " + strThumPath );
  					//Log.d("KIM", "bmp = " + bmp );
  				}
  			} catch (Exception e) {
  				Log.d("KIM", "Exception : "+e.toString());			
  			} finally {
  				if (c != null){
  					c.close();
  				}
//  				if (thumbCursor != null){
//  					thumbCursor.close();
//  				}			
  			}

  			//Log.d("KIM", "Ret image = " + image.toString());
  			return image;
  		}
  		
  		/** 전화번호 가공
		 * @param phoneNum
		 * @return
		 */
		public static final String[] phoneNumCut(String phoneNum) {

			String mPhoneNo = nullCheck(phoneNum);
			String[] phoneNo = new String[3];
			if(!"".equals(mPhoneNo)){
				if (mPhoneNo.startsWith("+")) {

					if (mPhoneNo.length() > 12) {
						phoneNo[0] = "0" + mPhoneNo.substring(3, 5);
						phoneNo[1] = mPhoneNo.substring(5, 9);
						phoneNo[2] = mPhoneNo.substring(9);
					} else {
						phoneNo[0] = "0" + mPhoneNo.substring(3, 5);
						phoneNo[1] = mPhoneNo.substring(5, 8);
						phoneNo[2] = mPhoneNo.substring(8);
					}
				} else {

					if (mPhoneNo.length() > 10) {
						phoneNo[0] = mPhoneNo.substring(0, 3);
						phoneNo[1] = mPhoneNo.substring(3, 7);
						phoneNo[2] = mPhoneNo.substring(7);
					} else {
						phoneNo[0] = mPhoneNo.substring(0, 3);
						phoneNo[1]= mPhoneNo.substring(3, 6);
						phoneNo[2] = mPhoneNo.substring(6);
					}
				}

				return phoneNo;
			}else{
				return phoneNo;
			}
			
		}
		
		public static Double  doubleCutToString(double gpsxy){
			String rtnStr = "0";
			double rtnDouble = 0.0;
			if(gpsxy > 0){
				rtnStr = Double.toString(gpsxy);
				if(rtnStr.length() > 12){
					rtnStr = rtnStr.substring(0, 12);
					rtnDouble = Double.valueOf(rtnStr).doubleValue();
					return rtnDouble;
				}else{
					return gpsxy;		
				}
			}
			return rtnDouble;
		}
		
		/** 앱 버전 코드
		 * @param context
		 * @return
		 */
		public static int getAppVersionCode(Context context) {
			try {
				PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				return packageInfo.versionCode;
			} catch (NameNotFoundException e) {
				
				throw new RuntimeException("패키지 이름을 알 수 없습니다. " + e);
			}
		}
	
		/** 앱 버전 이름
		 * @param context
		 * @return
		 */
		public static String getAppVersionName(Context context) {
			try {
				PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				return packageInfo.versionName;
			} catch (NameNotFoundException e) {
				
				throw new RuntimeException("패키지 이름을 알 수 없습니다. " + e);
			}
		}
		
		/**SDCard Refresh file list
		 * @param activity
		 */
		public void refreshSD(Activity activity) {
			Log.i("123123",1+"");
			LocalBroadCastManager.getInstance(context).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

			//기존 사용 최창유 주석
			/*activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			*/



//			activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, 
//					Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		}
		
		public static String getCurrentYMD(){
			DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat
  			Calendar rightNow = Calendar.getInstance();// 
  			int year = rightNow.get(Calendar.YEAR);
  			int month = rightNow.get(Calendar.MONTH) + 1;
  			int date = rightNow.get(Calendar.DATE);//
  			String result = decimalFormat.format(year)+"/"+decimalFormat.format(month) +"/"+ decimalFormat.format(date);
  			
			return result;
		}
		
		public static String getCurrentHMS(){
			DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat
  			Calendar rightNow = Calendar.getInstance();// 
  			int hour = rightNow.get(Calendar.HOUR_OF_DAY);
  			int minute = rightNow.get(Calendar.MINUTE);
  			int second = rightNow.get(Calendar.SECOND);
  			String result = decimalFormat.format(hour) +":"+ decimalFormat.format(minute);
  			
			return result;
		}
			
}


