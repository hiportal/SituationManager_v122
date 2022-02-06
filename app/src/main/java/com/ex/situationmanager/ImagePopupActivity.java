package com.ex.situationmanager;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ImagePopupActivity extends Activity implements OnClickListener{
	private Context mContext = null;
	String imgPath; 
	boolean deleted = false;
	
	ImageButton ibtnEnd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_popup);
		
		mContext = this;
		
		/** 전송메시지 */
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		
		imgPath = extras.getString("filename");
		Log.d("","이미지 팝업 ===== " + imgPath);
		/** 완성된 이미지 보여주기  */
		
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inSampleSize = 2;
		ImageView iv = (ImageView)findViewById(R.id.imageView);
		Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);		
		
		Matrix matrix = new Matrix();

		int width = bm.getWidth(); 
		int height = bm.getHeight();

//		if(bm.getWidth() < bm.getHeight()){

//			matrix.postScale(1, 1);
//			matrix.postRotate(90);
			
			bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);	    						
//		}		
			
		iv.setImageBitmap(bm);
		
		//shlee 2013-11-07 멀치터치
		/*WebView webview01;
		webview01  = (WebView) findViewById(R.id.webview01);

	  	webview01.setVerticalScrollBarEnabled(false);
	  	webview01.setVerticalScrollbarOverlay(false);
	  	webview01.setHorizontalScrollBarEnabled(false);
	  	webview01.setHorizontalScrollbarOverlay(false);
//	  	webview01.setInitialScale(100);
	  	webview01.getSettings().setBuiltInZoomControls(true);
	  	//webview01.getSettings().setDefaultZoom(ZoomDensity.FAR);
	    webview01.loadDataWithBaseURL(null,creHtmlBody("file://" + imgPath), "text/html", "utf-8", null);*/
		
		/** 확인 버튼 */
		ImageButton btn = (ImageButton)findViewById(R.id.btn_back);
		btn.setOnClickListener(this);
		
		/** 삭제 버튼 */
		ImageButton ibDel = (ImageButton)findViewById(R.id.btn_del);
		ibDel.setOnClickListener(this);		
	}
	
	public  String creHtmlBody(String imagUrl){
		   StringBuffer sb = new StringBuffer("<HTML>");
		   sb.append("<HEAD>");
		   sb.append("</HEAD>");
		   sb.append("<BODY style='margin:10; padding:0; text-align:center;'>");    //중앙정렬
//		   sb.append("<img src=\"" + imagUrl+"\">");    //원래 비율에 맞게 나옴

		   sb.append("<img width='100%' height='100%' src=\""+imagUrl+"\">"); //가득차게 나옴

		   sb.append("</BODY>");
		   sb.append("</HTML>");
		   return sb.toString();
		 }

	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {

		switch(v.getId()){
			case R.id.btn_back:
				intExt(deleted);
				break;
			case R.id.btn_del:
				
				openConfirmDialog("파일을 삭제하시겠습니까?");
				
				break;
		} 
	}
	
	protected void LaunchMainMenu(){
		finish();
	}  		
	
	/**
	 * 다이얼 로그 박스
	 * 
	 * @param message
	 */
	private void openConfirmDialog(String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("알림");

		alert.setMessage(message);
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try{
					
					File file = new File(imgPath); 
					
					if(imgPath.contains("situationmanager")) deleted = file.delete(); 			
						else intExt(true);
					
					
				}catch(NullPointerException e){
					Log.e("에러","예외");
				}
				intExt(deleted);
			}
		});
		
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
			}
		});		 
		
		alert.show();
	}		
	
	public void intExt(boolean isDeleted){
		Intent i = new Intent();
		setResult(RESULT_OK, i);
		finish();		
	}
	
	public void onBackPressed(){
		intExt(false);
	}  	
	
}