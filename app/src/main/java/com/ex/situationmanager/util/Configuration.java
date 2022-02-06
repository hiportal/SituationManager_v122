package com.ex.situationmanager.util;

import com.ex.situationmanager.dto.Userinfo;

import android.net.Uri;
import android.os.Environment;

public class Configuration {
	//local 테스트
	//public static String FILE_UPLOAD_PATH = "http://175.214.44.30:8080/uploadproc.jsp";
	//운영//사진전송
	//--public static String FILE_UPLOAD_PATH = "http://oneclickapp.ex.co.kr:5000/uploadproc.jsp";//통합 DB 신규 테스트 test

    public static String FILE_UPLOAD_PATH = "https://oneclickapp.ex.co.kr:9443/uploadproc.jsp";//통합 DB 신규 운영 *2021운영

    //public static String FILE_UPLOAD_PATH = "https://oneclickapp.ex.co.kr:9443/uploadproc_test.jsp"; // 2021.06 TEST
    //public static String FILE_UPLOAD_PATH = "https://oneclickapp.ex.co.kr:9443/uploadproc_test.jsp";//통합 DB 신규 운영

    //public static String FILE_UPLOAD_PATH = "https://oneclickapp.ex.co.kr:9443/insertSendFile23";


   // public static String FILE_UPLOAD_PATH = "http://121.190.60.214:8080/uploadproc.jsp";//통합 DB 신규
	//public static String FILE_UPLOAD_PATH = "http://oneclickmobile.ex.co.kr:8080/uploadproc.jsp";
	
	
	public static String directoryName = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/situationmanager";
	
	public static String directoryName_Camera = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
	// 사용자 정보.
	public static Userinfo User = new Userinfo();
		
	//환경설정 전송주기 최소 5
    public static int sendDelay = 1;
    
    public static boolean DATA_SEND_STAT = true;
    
    public static String USER_PHONE_NUMBER = "010-1234-5678";
    
    public static String NAVIGATION_START_NUMBER = "012";
    //사진.
    public final static int LOC_CHANGE = 0;
    public final static int IMAGE_CAPTURE = 1;
    public final static int VIDEO_CAPTURE = 2;
    public final static int SELECT_JISA = 3;
    public final static int FILE_TYPE_IMAGE = 8;
    public final static int FILE_TYPE_VIDEO = 9;
    //조치완료 등록.
    public final static int PATROL_REG_RETURN = 11;
    
    public static Uri	mUriSet;
    public static Uri 	mPhotoUri;
    
    
}
