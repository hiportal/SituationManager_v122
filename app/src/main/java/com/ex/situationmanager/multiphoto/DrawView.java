package com.ex.situationmanager.multiphoto;

import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.util.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class DrawView extends View{

	Bitmap canvasBitmap;
		
	Paint paint;
	Canvas canvas;
	String location = "";
	
	
	@SuppressLint("NewApi")
	public DrawView(Context context) {
		super(context);
//			Bitmap bi = Bitmap.createBitmap(adview.getWidth(), adview.getHeight(),  Bitmap.Config.ARGB_8888);
		location = SituationService.ns_name+":"+SituationService.currentIjung;
		if(location.equals(":")){
			location = "노선외";
		}
	}
	
	public DrawView(Context context, String nsname, String ijung){
		super(context);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d("", ""+"DrawView onMeasure " + widthMeasureSpec+":"+heightMeasureSpec );
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(40);
		paint.setAntiAlias(true);
//			paint.setStyle(Paint.Style.STROKE);
		Rect rect = new Rect(80, 60, 420, 210);
		canvas.drawRect(rect, paint);
		paint.setColor(Color.BLUE);
		canvas.drawRect(100, 200, 100, 200, paint);
		canvas.drawText("날짜 : "+Common.getCurrentYMD(), 100, 100, paint);
		canvas.drawText("시간 : "+Common.getCurrentHMS(), 100, 150, paint);
		canvas.drawText("위치 : "+location , 100, 200, paint);
		
		this.paint = paint;
		this.canvas = canvas;
		
	}
		
}
