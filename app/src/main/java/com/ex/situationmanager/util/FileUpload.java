package com.ex.situationmanager.util;

//package com.ex.moffice;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
//import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ex.situationmanager.service.SituationService;
import com.ex.situationmanager.util.CustomMultiPartEntity.ProgressListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hasomin on 2017-07-17.
 */

//@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FileUpload {
	public static FileUpload getContext;
	static String fileseq;

//	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void main(String[] filePath, String storgSeq, String drtySeq, String sysCd) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		sysCd = "OCK";
		storgSeq = "29";
		drtySeq = "1";

		getContext = this;
		try {
			upload(filePath, storgSeq, drtySeq, sysCd);
		} catch (IOException e) {
			Log.e("에러","예외");
		}
	}

	public void upload(String[] filePath, String storgSeq, String drtySeq, String sysCd) throws IOException {
		try {
			String responseStr = "";

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			// 파라미터
//			builder.addTextBody("SS_USER_ID", LoginActivity.userid, ContentType.create("Multipart/related", "UTF-8"));
			builder.addTextBody("SS_USER_ID", SituationService.conf.User.getPatcar_id(), ContentType.create("Multipart/related", "UTF-8"));
			builder.addTextBody("sysCd ", sysCd, ContentType.create("Multipart/related", "UTF-8"));
			builder.addTextBody("storgSeq ", storgSeq, ContentType.create("Multipart/related", "UTF-8"));
			builder.addTextBody("drtySeq ", drtySeq, ContentType.create("Multipart/related", "UTF-8"));
			// 파일 첨부
			// builder.addPart("upload_0", new FileBody(new File(filePath)));
			for (int i = 0; i < filePath.length; i++) {
				builder.addPart("upload_" + i, new FileBody(new File(filePath[i])));
				Log.d("파일경로", filePath[i]);
			}

			// 전송
			InputStream inputStream = null;
			// HttpClient httpClient = AndroidHttpClient.newInstance("Android");
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://ock.ex.co.kr:5003/open/upload.do");
//			multipartContent = new com.ex.situationmanager.util.CustomMultiPartEntity(
//				new ProgressListener() {
//
//					int uploadPercent = 0;
//
//					@Override
//					public void transferred(long num) {
//						// TODO Auto-generated method stub
//						publishProgress((int) ((num / (float) totalSize) * 100));
//					}
//				});
			httpPost.setEntity(builder.build());
			
			
			HttpResponse httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();

			// 응답
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				Log.d("결과", stringBuilder.toString());
				stringBuilder.append(line + "\n");
			}
			inputStream.close();

			// 응답결과
			String result = stringBuilder.toString();
			Log.d("결과", result);
			fileseq = result.replaceAll("[^0-9]", "");
		} catch (IOException e) {
			Log.e("파일업로드 에러", "예외");
		}
	}
}
