package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyPageCameraActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    ImageView imageView;
    SurfaceHolder holder;
    Button button;
    MyPageScanCamera surfaceView;
    MyPageCameraActivity getInstance;
    TextView textView;

    private static Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success){
                            Toast.makeText(getApplicationContext(),"Auto Focus Success",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Auto Focus Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "camera button click start ");
                capture();
            }
        });
        // 뷰 정의
        // ivCaptureCard = (ImageView) findViewById(R.id.captureCardImg);

//        Intent intent = getIntent();
//        if(intent != null) {    // 인텐트 null 체크
//            if(intent.hasExtra("image")) {   // 값이 담겨온 경우
//                // Byte Array -> 비트맵 이미지
//                byte[] arr = getIntent().getByteArrayExtra("image");
//                captureCardImg = BitmapFactory.decodeByteArray(arr, 0, arr.length);
//                ivCaptureCard.setImageBitmap(captureCardImg);
//            }
//            else {
//                sendErrorActivityResult("300");
//                finish();
//            }
//        }
//        else {
//            sendErrorActivityResult("301");
//            finish();
//        }

    }

    /*
    사진 촬영 함수
     */
    private void capture() {
        surfaceView.capture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                surfaceView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
                // surfaceView.set(bitmap);
                // 사진을 찍게 되면 미리보기가 중지된다. 다시 미리보기를 시작하려면...
                // camera.startPreview();
            }
        });
    }

    /*
    SurfaceView에 카메라를 preview하기 위한 시작 시 설정하는 함수
     */
    private void init() {
        Log.d(TAG, "init start ");
        getInstance = this;

        // 카메라 객체를 R.layout.activity_my_page_image 레이아웃에 선언한 SurfaceView에서 먼저 정의해야 함으로 setContentView 보다 먼저 정의한다.
        camera = Camera.open();

        setContentView(R.layout.activity_my_page_image);

        // SurfaceView를 상속받은 레이아웃을 정의한다.
        surfaceView = (MyPageScanCamera) findViewById(R.id.surfaceView);

        // SurfaceView 정의 - holder와 Callback을 정의한다.
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // 그 외 뷰 정의
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);

        // 화면 켜진 상태 유지
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /*
    ActivityResult를 진행하는 함수.
    @param : errorCode(String) - 해당하는 코드를 인텐트에 담아 전송
    -> 300 : Intent에 이미지 값이 담겨오지 않은 경우
    -> 301 : Intent가 null인 경우
     */
    private void sendErrorActivityResult(String errorCode) {
        Log.d(TAG, "errorCode : " + errorCode);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result",errorCode);
        setResult(RESULT_CANCELED, resultIntent);
    }

    public static Camera getCamera(){
        return camera;
    }
}
