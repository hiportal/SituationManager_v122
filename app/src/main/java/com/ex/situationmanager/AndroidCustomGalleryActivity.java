package com.ex.situationmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.ex.situationmanager.multiphoto.DrawView;
import com.ex.situationmanager.multiphoto.MyGalleryPicker;
import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.service.XMLData;
import com.ex.situationmanager.util.Common;
import com.ex.situationmanager.util.Configuration;
import com.ex.situationmanager.util.Image;
import com.ex.situationmanager.util.LocalBroadCastManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class AndroidCustomGalleryActivity extends Activity {
	
	private String mFileimageRoute = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/situationmanager/";
	private String mFileimageRouteTag = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/situationmanager_tag/";
	private int count;
	private Bitmap[] thumbnails;
	private boolean[] thumbnailsselection;
	private String[] arrPath;
	private ImageAdapter imageAdapter;
	private Set<String> fileNames = new HashSet<String>();
	final String TAG = "AndroidCustomGalleryActivity ";
	
	int imgCntMax = 5;
	int imgCnt = 0;
	
	View adview;
	/** Called when the activity is first created. */
	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(SituationService.conf.USER_PHONE_NUMBER.startsWith(Configuration.NAVIGATION_START_NUMBER)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로전환
		}
		setContentView(R.layout.customgallery);
		//태그
		adview = new DrawView(AndroidCustomGalleryActivity.this);
		adview.setDrawingCacheEnabled(true);
		addContentView(adview, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		adview.layout(0, 0, adview.getWidth(), adview.getHeight());
		adview.setBackgroundColor(android.R.color.transparent);
		adview.setVisibility(View.INVISIBLE);
		
		Log.d("","AndroidCustomGalleryActivity Step=1");
		try {
			final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
//			final String orderBy = MediaStore.Images.Media._ID;
			final String orderBy = MediaStore.Images.Media.DATE_ADDED + " desc ";
			imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
					null, orderBy);
			//int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
			count = imagecursor.getCount();
			thumbnails = new Bitmap[count];
			arrPath = new String[count];
			thumbnailsselection = new boolean[count];
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.e("에러","에러");
//			Toast.makeText(AndroidCustomGalleryActivity.this, ""+e.toString(), 5000).show();
		}
		new ImageLoader().execute("");
		final Button selectBtn = (Button) findViewById(R.id.selectBtn);
		selectBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int len = thumbnailsselection.length;
				int cnt = 0;
				String selectImages = "";
				for (int i =0; i<len; i++)
				{
					if (thumbnailsselection[i]){
						cnt++;
						selectImages = selectImages + arrPath[i] + "|";
						fileNames.add(arrPath[i]);
					}
				}
//				saveViewToImage(adview);
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
				String result = Common.FILE_TAG+decimalFormat.format(year)
						+ decimalFormat.format(month) + decimalFormat.format(date)
						+ decimalFormat.format(hour) + decimalFormat.format(minute)
						+ decimalFormat.format(second) + decimalFormat.format(rnd);
				
				ArrayList<String> al = new ArrayList<String>();
				al.addAll(fileNames);
				Common common = new Common();
				
				for (int i = 0; i < al.size(); i++) {
					File file = new File(al.get(i));
					String tempName = file.getName();
					String ext = tempName.substring(tempName.indexOf("."));
					tempName = result+"_"+i+".jpg";
//					
					Log.d("", "fileNames = " + al.get(i));
					Log.d("", "fileNames = " + file.getName());
					
//					common.fileCopyToSituation(al.get(i), tempName, "N");
					
//					file = null;
					
//					Bitmap originFile = BitmapFactory.decodeFile(al.get(i));
//					Bitmap tagFile = BitmapFactory.decodeFile(mFileimageRouteTag+"tag.jpg");
//					Bitmap resized = Bitmap.createScaledBitmap(tagFile, 380, 620, true);
//					int m_resWidth = 860;
//					int m_resHeight = 480;
					
					
//					overlayMark(originFile, resized,  mFileimageRoute + tempName+".jpg");
				}
				new ImgSaveLowPixel("", result, al).execute("");
//				refreshSD();
//				ReadSDCard();
//				Intent data = getIntent();
//				data.putStringArrayListExtra("imagelist", al);
//				AndroidCustomGalleryActivity.this.setResult(RESULT_OK, data);
//				finish();
				
			}
		});
	}
	private class ImgSaveLowPixel extends AsyncTask<String, String, String>{
		String path = "";
		String result = "";
		ArrayList<String> al;
		ProgressDialog progressDialog;
		
		public ImgSaveLowPixel(String path, String result, ArrayList<String> al){
			this.path = path;
			this.result = result;
			this.al = al;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressDialog = ProgressDialog.show(AndroidCustomGalleryActivity.this, "", "이미지 저장중...", true);
		}
		
		@Override
		protected String doInBackground(String... params) {
			for (int i = 0; i < al.size(); i++) {
				File file = new File(al.get(i));
				String tempName = file.getName();
				String ext = tempName.substring(tempName.indexOf("."));
				tempName = result+"_"+i+".jpg";
				Log.d("","ImgSaveLowPixel = " +path);
				
				BitmapFactory.Options opt = new BitmapFactory.Options();
				long megaByte = Math.round((Math.floor(file.length() * 100 / (1024 * 1024)))/100);

				//?
				if(megaByte> 2 ){
				/*if(megaByte> 1 ){*/
					opt.inSampleSize = (int)megaByte;
				}else{
					opt.inSampleSize = 1;
					//opt.inSampleSize = 4;
				}
				Log.d( "" ,"megaByte = " + megaByte + ":" + (int)megaByte );
				
				Bitmap originFile = BitmapFactory.decodeFile(al.get(i), opt);
				
				double height = originFile.getHeight();
				double width = originFile.getWidth();
				double calcu_height = height*(1024/width);
				Log.d("","originFile = " +height);
				Log.d("","originFile = " +width);
				
				Log.d("","originFile = " + (int)calcu_height);
				
				Bitmap resized = Bitmap.createScaledBitmap(originFile, 1024, (int)calcu_height, true);
				try {
			    	FileOutputStream fos = new FileOutputStream(Configuration.directoryName +"/"+tempName);

			    	resized.compress(CompressFormat.JPEG, 50, fos);
			    	fos.close();

			    	resized.recycle();
				} catch ( IOException e) {
					Log.e("에러","예외");
				}
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			refreshSD();

			ReadSDCard();
//			progressDialog.dismiss();
			Intent data = getIntent();
			data.putStringArrayListExtra("imagelist", al);
			AndroidCustomGalleryActivity.this.setResult(RESULT_OK, data);
			finish();
		}
	}
	

	Cursor imagecursor;
	public class ImageLoader extends AsyncTask<String, Void, XMLData> {
		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(AndroidCustomGalleryActivity.this, "", "이미지 로딩중...", true);
			super.onPreExecute();
		}
		
		@Override
		protected XMLData doInBackground(String... params) {
				
			/*final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
//			final String orderBy = MediaStore.Images.Media._ID;
			final String orderBy = MediaStore.Images.Media.DATE_ADDED + " desc ";
			imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
					null, orderBy);
			//int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
			count = imagecursor.getCount();
			thumbnails = new Bitmap[count];
			arrPath = new String[count];
			thumbnailsselection = new boolean[count];*/
			
//			for (int i = 0; i < count; i++) {
//				imagecursor.moveToPosition(i);
//				int id = imagecursor.getInt(image_column_index);
//				int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
//				thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
//						getApplicationContext().getContentResolver(), id,
//						MediaStore.Images.Thumbnails.MICRO_KIND, null);
//				arrPath[i]= imagecursor.getString(dataColumnIndex);
//			}
			
			
			return null;
		}
		@Override
		protected void onPostExecute(XMLData result) {
			super.onPostExecute(result);
			
			GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
			imageAdapter = new ImageAdapter();
			imagegrid.setAdapter(imageAdapter);
			progressDialog.dismiss();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	double totalFileSize =0;
	public List<Image> ReadSDCard() {
		Log.i("123123",2+"+");
		Log.d("","AndroidCustomGalleryActivity Step=4");
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

		Log.d("", "ReadSdCard list size = " + trnImageList.size());
		return trnImageList;
	}
	public void refreshSD() {
		Log.d("","AndroidCustomGalleryActivity Step=5");
	/*	sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://"+ Environment.getExternalStorageDirectory())));*/

		Log.i("123123","2");
		LocalBroadCastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	}
	

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("","AndroidCustomGalleryActivity Step=6");
			imagecursor.moveToPosition(position);
			int id = imagecursor.getInt(imagecursor.getColumnIndex(MediaStore.Images.Media._ID));
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
			thumbnails[position] = MediaStore.Images.Thumbnails.getThumbnail(
					getApplicationContext().getContentResolver(), id,
					MediaStore.Images.Thumbnails.MICRO_KIND, null);
			arrPath[position]= imagecursor.getString(dataColumnIndex);
			
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.custom_galleryitem, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setId(position);
			holder.imageview.setId(position);
			holder.checkbox.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					int id = cb.getId();
					if (thumbnailsselection[id]){
						imgCnt--;
						cb.setChecked(false);
						thumbnailsselection[id] = false;
					} else {
						//202007_사진최대갯수수정
						if(imgCnt >= 7){
							makeDialog();
							cb.setChecked(false);
							thumbnailsselection[id] = false;
						}else{
							cb.setChecked(true);
							thumbnailsselection[id] = true;
							imgCnt ++;
						}
						
					}
				}
			});
			
			holder.imageview.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = v.getId();
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
				//	intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
					intent.setDataAndType(Uri.parse("content://" + arrPath[id]), "image/*");
					startActivity(intent);
				}
			});
			holder.imageview.setImageBitmap(thumbnails[position]);
			holder.checkbox.setChecked(thumbnailsselection[position]);
			holder.id = position;
			return convertView;
		}
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
	    //태스 이미지 그리지 않는다. 나중에 그리려면 이걸 사용해라.(주석만풀면됨)
//	    canvas.drawBitmap(bmp2, 0, 0, null);
	    try {
	    	FileOutputStream fos = new FileOutputStream(savePath);
	    	bmOverlay.compress(CompressFormat.JPEG, 50, fos);
	    	fos.close();
	    	bmOverlay.recycle();
		} catch (IOException e) {
			Log.e("에러","예외");
		}
	    return bmOverlay; 
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
	       
	       } catch( IOException e ){
	                Log.e("testSaveView", "Exception: " + "예외");
	       } 
       }
    }  
	
	class ViewHolder {
		ImageView imageview;
		CheckBox checkbox;
		int id;
	}
	
	private void makeDialog(){
		AlertDialog.Builder ad = new AlertDialog.Builder(AndroidCustomGalleryActivity.this);
		ad.setMessage("")
				.setTitle("확인")
				//202007_최대갯수 수정후 멘트 수정
				.setMessage("* 이미지는 최대 7개까지 선택 가능합니다.")
				.setCancelable(false)
				.setPositiveButton("확인",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
							}
						});
		ad.show();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		imagecursor.close();
		imagecursor = null;
	}
}