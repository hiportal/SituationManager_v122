package com.ex.gongsa.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.gongsa.Preview;
import com.ex.situationmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class CameraActivity extends BaseActivity implements View.OnClickListener {


    private String TAG = "CameraActivity";
    Preview preview;
    //Camera camera=null;
    /*SurfaceHolder holder;*/
    /*TableLayout tl = null;*/
    /*final static int TAKE_PICTURE = 1;*/
    ViewGroup ibtnSave = null;
    ViewGroup ibtnCancel = null;

    /*ImageView imageView;*/
    /*String ffffffffffffff;*/
    /*private byte[][] mImageData;*/
    Intent intent;
    TextView bonbuTextView;
    TextView jisaTextView;
    TextView nosunTextView;
    TextView gamdokTextView;
    //  TextView guganTextView;

    JSONObject jsonObject = null;

   // FrameLayout camera_frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_camera);
        Log.i("CameraActivity", "onCreate");
        intent = getIntent();

        bonbuTextView = (TextView) findViewById(R.id.bonbuTextView);
        jisaTextView = (TextView) findViewById(R.id.jisaTextView);
        nosunTextView = (TextView) findViewById(R.id.nosunTextView);
        gamdokTextView = (TextView) findViewById(R.id.gamdokTextView);

        //camera_frameLayout =(FrameLayout)findViewById(R.id.camera_frameLayout);
        // guganTextView=(TextView)findViewById(R.id.guganTextView);

        preview = findViewById(R.id.cameraPreview);


        ibtnSave = (ViewGroup) findViewById(R.id.ibtnSave);
        ibtnSave.setOnClickListener(this);
        ibtnCancel = (ViewGroup) findViewById(R.id.ibtnCancel);
        ibtnCancel.setOnClickListener(this);

        try {
            jsonObject = new JSONObject(intent.getStringExtra("TodayWorkListJsonValue"));
            bonbuTextView.setText(jsonObject.get("hdqrNm").toString());
            jisaTextView.setText(jsonObject.get("mtnofNm").toString());
            nosunTextView.setText(jsonObject.get("routeNm").toString());
            gamdokTextView.setText(jsonObject.get("gamdokNameEMNM").toString());
            String[] lineArray = jsonObject.get("acdtLoctCtnt").toString().split("km");

            //    guganTextView.setText(lineArray[0]+"km"+lineArray[1]+"km");



            //camera_frameLayout.setOnClickListener(this);
        } catch (JSONException e) {
            Log.e("에러","에러발생");
        }
    }

    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.i(getClass().getSimpleName(), "SHUTTER CALLBACK");
        }
    };


    Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera c) {


            FileOutputStream fos = null;
            try {
            } catch (NullPointerException e) {
                Log.e("에러","에러발생");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }//inner try-catch-finally
                }//if
            }//outter try-catch-finally
            intent = new Intent();
            intent.putExtra("filename", data);
            setResult(RESULT_OK, intent);
            finish();

        }//onPictureTaken

    };//mPictureCallbackJpeg

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ibtnSave:

                preview.mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            if (preview.mCamera != null) {
                                preview.mCamera.takePicture(mShutterCallback, null, mPictureCallbackJpeg);
                            }//if
                        }else{
                            Log.i("카메라","초점 맞추기 실패");
                        }
                    }
                });


                break;
            case R.id.ibtnCancel:
                finish();
                break;
   /*         case R.id.camera_frameLayout:
                *//*Toast.makeText(this,"프레임레이아웃 클릭됨",Toast.LENGTH_SHORT).show();*//*
                preview.mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            Log.i("카메라","초점 맞추기 성공");
                        }else{
                            Log.i("카메라","초점 맞추기 실패");
                        }
                    }
                });
                break;*/
        }//switch
    }//onClickna

    @Override
    protected void onResume() {
        super.onResume();
        if (null != preview) {
            if (null != preview.mCamera) {
                preview.mCamera.startPreview();
            }
        }
    }

    @Override
    public String onActionPost(String primitive, String date) {
        return null;
    }
}
