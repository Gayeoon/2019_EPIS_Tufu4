package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
public class MyPageImageActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    ImageView ivCaptureCard;
    Bitmap captureCardImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_image);

        // 뷰 정의
        ivCaptureCard = (ImageView) findViewById(R.id.captureCardImg);

        Intent intent = getIntent();
        if(intent != null) {    // 인텐트 null 체크
            if(intent.hasExtra("image")) {   // 값이 담겨온 경우
                // Byte Array -> 비트맵 이미지
                byte[] arr = getIntent().getByteArrayExtra("image");
                captureCardImg = BitmapFactory.decodeByteArray(arr, 0, arr.length);
                ivCaptureCard.setImageBitmap(captureCardImg);
            }
            else {
                sendErrorActivityResult("300");
                finish();
            }
        }
        else {
            sendErrorActivityResult("301");
            finish();
        }

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
}
