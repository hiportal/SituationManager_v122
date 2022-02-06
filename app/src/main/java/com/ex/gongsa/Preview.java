package com.ex.gongsa;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ex.situationmanager.service.Parameters;

import java.io.IOException;
import java.util.List;


public class Preview extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;
    public int maxZoom = 0;
    public Camera mCamera = null;


    public void close() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public Preview(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            if (mCamera == null) {

                mCamera = Camera.open();


                Camera.Parameters params = mCamera.getParameters();

                maxZoom = params.getMaxZoom();

                List<Size> list = params.getSupportedPreviewSizes();
                Log.i("해상도리스트시작", "----------------");
                for (int i = 0; i < list.size(); i++) {
                    Log.i("해상도 리스트", "높이:" + Integer.toString(list.get(i).height) + ", 너비:" + Integer.toString(list.get(i).width));
                }
                Log.i("해상도리스트종료", "----------------");
                int m_resWidth = 640;
                int m_resHeight = 480;

                if (list == null) {//미리보기 지원목록이 없으면
                    //표면의 크기로 미리보기 사이즈 지정
                    m_resWidth = mCamera.getParameters().getPictureSize().width;
                    m_resHeight = mCamera.getParameters().getPictureSize().height;
                    /*m_resWidth = mCamera.getParameters().getPictureSize().width;
                    m_resHeight = mCamera.getParameters().getPictureSize().height;*/

                    params.setPictureSize(m_resWidth, m_resHeight);
                } else {//표면의 실제크기와 가장 근접한 사이즈 구하기
                    int diff = 10000;
                    Size size = null;

                    for (Size s : list) {
                        if (Math.abs(s.width - m_resWidth) < diff) {
                            diff = Math.abs(s.width - m_resWidth);
                            size = s;
                        }
                    }


                    List<Size> piclist = params.getSupportedPictureSizes();
                    int width = 0;
                    int height = 0;
                    int nexWidth = 0;
                    int nexheight = 0;

                    int m = 0;
                    for (int i = 0; i < piclist.size(); i++) {

                        if (piclist.get(i).height == 480 && piclist.get(i).width == 640) {
                            width = piclist.get(i).width;
                            height = piclist.get(i).height;
                        }
                        Log.i("ddd", "높이:" + piclist.get(i).height + ", " + piclist.get(i).width);

                    }

                    Log.d("Camera width,height", " width:" + width + "; height:" + height);
                    //  params.setPictureSize(480, 640);
                    params.setPictureSize(width, height);
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    //실험

                    //실험
                    /* params.setPictureSize(480, 680);*/

                    Log.d("Camera", "width:" + width + "; height:" + height);
                    Log.d("", "device Model info = " + Build.MODEL);
                }
                mCamera.setParameters(params);
            }
            mCamera.setDisplayOrientation(90);
            //int rotation = getResources().getConfiguration().orientation;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        } catch (NullPointerException e) {
            Log.e("에러", "예외");
        } catch (Exception e) {
            Log.e("에러", "예외");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera == null) {
                mCamera = Camera.open();
            } else {
                try {
                    mCamera.setPreviewDisplay(holder);

                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                Log.i("카메라", "초점 맞추기 성공");
                            } else {
                                Log.i("카메라", "초점 맞추기 실패");
                            }
                        }
                    });
                    mCamera.startPreview();
                } catch (IOException exception) {
                    Log.e("Error", "exception:surfaceCreated Camera Open ");
                    mCamera.release();
                    mCamera = null;
                    // TODO: add more exception handling logic here
                } catch (Exception e) {
                    Log.e("에러", "예외");
                    mCamera.release();
                    mCamera = null;
                }
            }
        } catch (NullPointerException e) {
            Log.e("에러", "익셉션");
        } catch (Exception e) {
            Log.e("에러", "예외");

        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }//end surfaceDestroyed

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        ;;
    }//end surfaceChanged
}
