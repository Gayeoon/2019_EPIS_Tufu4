package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/*
시작 액티비티
병원과 개인 선택할 수 있는 메인 페이지
*** layout 완료.

- 이해원
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    ImageView ivStartHospital, ivStartIndividual;
    Intent switchActvityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivStartHospital = (ImageView) findViewById(R.id.startHospital);
        ivStartIndividual = (ImageView) findViewById(R.id.startIndividual);

        // 뷰 클릭 이벤트
        ivStartHospital.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { // 병원용
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        switchActvityIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

        ivStartIndividual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { // 사용자용
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        switchActvityIntent = new Intent(getApplicationContext(), UserLoginActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
    }
}
