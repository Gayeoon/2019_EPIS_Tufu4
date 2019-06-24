package com.example.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

public class MyPageScanCamera extends SurfaceView implements SurfaceHolder.Callback {
    public static final String TAG = "LogGoGo";
    SurfaceHolder surfaceHolder;
    Camera camera = null;

    Context context;

    public MyPageScanCamera(Context context) {
        super(context);
        init(context);
    }

    public MyPageScanCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Log.d(TAG, "MyPageScanCamera init start");
        this.context = context;
         camera = MyPageCameraActivity.getCamera();
        if(camera == null){
            camera = Camera.open();
        }
        // listPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setFocusableInTouchMode(true);
        setFocusable(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        try{
            Log.d(TAG, "MyPageScanCamera surfaceCreated start");
            // 카메라 객체를 사용할 수 있게 연결
            if(camera == null)
                camera = Camera.open();

            Camera.Parameters parameters = camera.getParameters();  // 카메라 설정
            List<String> focusModes = parameters.getSupportedFocusModes();

            if(focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                //Phone supports autofocus!
                Log.d(TAG, "autofocus ok");
            }
            else {
                //Phone does not support autofocus!
                Log.d(TAG, "autofocus not ok");
            }

            // 카메라의 회전이 가로 / 세로일때 화면을 설정한다.
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) { // 세로인 경우
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else { // 가로인 경우
                parameters.set("orientation", "landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            camera.setParameters(parameters);
            camera.setPreviewDisplay(surfaceHolder);

            // Preview callback 가능 - 프레임 설정 가능
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                }
            });
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    SurfaceView의 크기가 바뀌면 호출
    화면에 보여지기 전 크기가 결정되는 시점
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 카메라 미리보기를 시작한다.
        camera.startPreview();
    }

    /*
    카메라 종료 시
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(camera != null){
            // 카메라 미리보기를 종료한다.
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public boolean capture(Camera.PictureCallback callback)
    {
        Log.d(TAG, "MyPageScanCamera capture start");
        if (camera != null)
        {
            camera.takePicture(null, null, callback);
            return true;
        }
        else
            return false;
    }
}
