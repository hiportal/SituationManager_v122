package com.ex.situationmanager.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

public class MediaScanner {

	private Context mContext;
	
	private String mPath;
	
	private MediaScannerConnection mMediaScanner;
	private MediaScannerConnectionClient mMediaScannerClient;
	
	public static MediaScanner newInstance(Context context){
		return new MediaScanner(context);
	}
	
	private MediaScanner(Context context){
		mContext = context;
	}
	
	public void mediaScanning(final String path){
		try{
			if(mMediaScanner == null){
				mMediaScannerClient = new MediaScannerConnectionClient() {

					@Override
					public void onScanCompleted(String path, Uri uri) {
						//

					}

					@Override
					public void onMediaScannerConnected() {
						mMediaScanner.scanFile(mPath, null);
					}
				};
				mMediaScanner = new MediaScannerConnection(mContext, mMediaScannerClient);
			}
			mPath = path;
			mMediaScanner.connect();
		}catch (NullPointerException e){
			Log.e("에러","NullPointerException");
		}
	}
}
