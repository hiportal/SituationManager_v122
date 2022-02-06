package com.ex.situationmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Image;
import com.ex.situationmanager.util.LocalBroadCastManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageListActivity extends Activity {
	
	private final String TAG = "ImageListActivity";
	
	private List<Image> tImageList;
	
	String filePath;
	
	Intent mIntent;
	
	LinearLayout llTitle;
	ListView lvList;

	double totalFileSize = 0;
	int totalFileCount = 0;
	
	TextView totalFileListSize, totalFileListCount;
	ImageView btnBack;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
				
		setContentView(R.layout.image_list);		
	
		mIntent = getIntent();
		
		btnBack = (ImageView) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		totalFileListCount = (TextView) findViewById(R.id.totalFileListCount);
		totalFileListSize = (TextView) findViewById(R.id.totalFileListSize);
		
        executeSearch();
        
	}
	
	protected void executeSearch(){
        tImageList = ReadSDCard();
        for (int i = 0; i < tImageList.size(); i++) {
        	Log.d("","ImageListActivity imageList fileName = "+tImageList.get(i).getFileName());
		}
        totalFileListCount.setText(tImageList.size()+"개");
        
        lvList = (ListView) findViewById(R.id.lvList);
        ImagesAdapter adapter= new ImagesAdapter(this, tImageList);
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				filePath = Common.FILE_DIR +tImageList.get(position).getFileName();
				RunGallery();
			}
		});		
	}
	
	public class ImagesAdapter extends ArrayAdapter<Image>{
		private Context mContext;
		private LayoutInflater mInflater;
		private List<Image> items;
		Bitmap thumbnail = null;
		public ImagesAdapter(Context context,  List<Image> list) {
			super(context, 0, list);
			items = list;
			mContext = context;
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view = null;
			view = convertView;
			Uri mUriSet;
			
			if(convertView	== null){
				view = mInflater.inflate(R.layout.image_list_row, null);
				File file = new File(Common.FILE_DIR+(String) items.get(position).getFileName());
				ImageView itemImage = (ImageView) view.findViewById(R.id.itemImage);
				ImageView btn_delete = (ImageView) view.findViewById(R.id.btn_delete);
				TextView fileName = (TextView) view.findViewById(R.id.fileName);
		        TextView fileSize = (TextView) view.findViewById(R.id.fileSize);
				
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inSampleSize = 6;
		        options.inPurgeable = true; 
		        options.inDither = true;
		        
		        //동영상과 사진 구분
		        if(items.get(position).getFileType().toString().trim().equals("mp4")){
			        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(Common.FILE_DIR+items.get(position).getFileName()
			        		, MediaStore.Video.Thumbnails.MICRO_KIND);
	//			    Bitmap bit = BitmapFactory.decodeFile(FILE_DIR+items.get(position).getFileName(), options);
				    Drawable  drawable = new BitmapDrawable(bMap);
				    itemImage.setImageBitmap(null);
				    itemImage.setBackgroundDrawable(drawable);
				    itemImage.setImageResource(R.drawable.img_movie01);
		        }else{
		    	   Bitmap bit = BitmapFactory.decodeFile(Common.FILE_DIR+items.get(position).getFileName(), options);
				   Drawable  drawable = new BitmapDrawable(bit);
				   itemImage.setImageBitmap(null);
				   itemImage.setBackgroundDrawable(drawable);
		        }
		       
		        //--------------------------
		        fileName.setText(items.get(position).getFileName().toString());
		        
		        
		        //파일사이즈 소수점 1자리, 반올림, MB 표현
		        double nByte = (double)file.length();
				double mByte = 0;
				double tvMByte = 0;
				String tvTextSize = "";
				
				mByte = Math.floor(nByte*100/(1024*1024));
		        tvTextSize = String.valueOf(mByte/100);

		        tvMByte = Math.round(mByte/10);
		        tvMByte = tvMByte/10;
		        fileSize.setText(""+tvMByte +"MB");
		        
		        btn_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						openConfirmDialog("파일을 삭제하시겠습니까?", Common.FILE_DIR+items.get(position).getFileName());
					}
				});
			}
			return view;
		}
	}
	
	public List<Image> ReadSDCard() {
		Image		currentItem	= null;
		List<Image> trnImageList= null;
		
		trnImageList= new ArrayList<Image>();
		
		File		fileDir		= new File(Common.FILE_DIR);
		
		if(!fileDir.exists()) fileDir.mkdir();
		
		File[]		files		= fileDir.listFiles();
		totalFileSize = 0;
		for (int i = 0; i < files.length; i++) {
			
			currentItem			= new Image();
			File file			= files[i];
	
			String fileName[]	= Common.split( file.getPath().toString(), "/");
			String fileType[]	= Common.split( file.getPath().toString(), ".");
			
			currentItem.setFilePath(file.getPath().toString());
			currentItem.setFileName(fileName[fileName.length-1]);
			currentItem.setFileType(fileType[fileType.length-1]);
//			currentItem.setFileSize(file.length());
			
			Log.d("saveFile", "fileName : "+fileName[fileName.length-1]);
			
			//파일리스트 총 용량 받아오기 소수점 1자리, 반올림, MB 표현
	        
	        
        	double nByte = file.length();
        	double mByte = 0;
        	double tvMByte = 0;
        	String tvTextSize = "";
        	
        	mByte = Math.floor(nByte*100/(1024*1024));
        	tvTextSize = String.valueOf(mByte/100);
        	
        	tvMByte = Math.round(mByte/10);
        	tvMByte = tvMByte/10;
        	totalFileSize = totalFileSize + tvMByte;
        	
//        	totalFileListSize.setText(totalFileSize+"MB");
        	
			trnImageList.add(currentItem);
			
		}//end for
		
		//shlee 2013-11-20 추가
		String num = String.format("%.1f" , totalFileSize);
		totalFileListSize.setText(num+"MB");
		
//		Log.d("", "totalFileSize = " + totalFileSize);
//		Log.d("", "totalFileSize num = " + num);
		
		Log.d("","ReadSdCard list size = "+trnImageList.size());
		return trnImageList;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	// 사진	: ImagePopupActivity 화면 호출
	// 동영상	: VideoPopupActivity 화면 호출
	protected void RunGallery(){
		
		
		if(filePath != null && !filePath.equals("")){
		
			String uriSet = filePath;
			String absolutePath = "";
			
			Log.d("Gallery", "uriSet : "+uriSet);
			
			if(uriSet.contains("file")){
				absolutePath = uriSet.replace("file://", "");
			}else{
//					Cursor c = getContentResolver().query(Uri.parse(filePath), null,null,null,null);
//					c.moveToNext();
//					absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
				absolutePath = uriSet;
			}//end if						
			

			
			if(absolutePath.toLowerCase().lastIndexOf(".mp4") > 0 || absolutePath.toLowerCase().lastIndexOf(".3gp") > 0 
					|| absolutePath.toLowerCase().lastIndexOf(".jpg") > 0 || absolutePath.toLowerCase().lastIndexOf(".png") > 0){
			
//				if(absolutePath.toLowerCase().lastIndexOf(".mp4") > 0 || absolutePath.toLowerCase().lastIndexOf(".3gp") > 0){
//
//					File temp = new File(absolutePath);
//					Intent i = new Intent(this, VideoPopupActivity.class);
//					i.putExtra("filename", Environment.getExternalStorageDirectory()+"/DCIM/Camera/customReport/"+temp.getName());
//					i.putExtra("filetype", "1");
//					startActivityForResult(i, 7);			
//				}else{
					if(absolutePath.length() > 0){
						File temp = new File(absolutePath);

						Intent i = new Intent(this, ImagePopupActivity.class);
						i.putExtra("filename", Environment.getExternalStorageDirectory()+"/DCIM/Camera/situationmanager/"+temp.getName());
						i.putExtra("fileType", "1");
						startActivityForResult(i, 7);
					}//end if
//				}//end if
			}else{
				Toast.makeText(this, "해당 비디오 포멧을 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
			}//end if
		}//end if
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("",TAG+ "requestcode " + requestCode);
		Log.d("",TAG+ "resultCode " + resultCode+":"+RESULT_OK);
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			
			//shlee 2013-11-12 추가
			String isDeleted = data.getStringExtra("isDeleted");
			Log.d("","ImageListActivity isDeleted " + isDeleted);
			if(isDeleted != null && isDeleted.equals("true")){
				//this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
				Log.i("123123123dsljd","므야 ㅡㅅ ㅡ");
				LocalBroadCastManager.getInstance(this).sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,  Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			}//end if
			
			executeSearch();
		}
		
	} 	
	
	
	/**
	 * 다이얼 로그 박스
	 * 파일 삭제 용
	 * @param message
	 */
	private void openConfirmDialog(String message, final String imgPath) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("알림");

		alert.setMessage(message);
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try{
					
					File file = new File(imgPath); 
					file.delete(); 			
					executeSearch();
					
				}catch(NullPointerException e){
					Log.e("예외","예외발생");
				}catch(Exception e){
					Log.e("예외","예외발생");
				}
			}
		});
		
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
			}
		});		 
		
		alert.show();
	}		
		
}