package com.ex.situationmanager.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.ex.gongsa.view.WorkPlanDetailActivity;

public class SituationBroadcast extends BroadcastReceiver{

	
	static String msg;
	@Override
	public void onReceive(Context context, Intent intent) {
		String message = intent.getStringExtra("message");
		Log.d("","PopBroadCastReceiver in = "+message);
	/*	Log.d("123","PopBroadCastReceiver in = "+message);

		Log.i("확인","1");
	//	Bundle bundle = intent.getExtras();
		Log.println(Log.ASSERT,"","브로드 캐스트");
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Log.println(Log.ASSERT,"","브로드 캐스트 호출");
			Log.i("확인","2");
			Bundle bundle = intent.getExtras();
			Toast.makeText(context,"잉여",Toast.LENGTH_LONG).show();

		}

		Log.i("확인","3");*/
	}

	static public String getMsg(){
		return msg;
	}
}
