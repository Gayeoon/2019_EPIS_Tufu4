package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * 사용자
 * 메인 메뉴 액티비티
 * - 이해원
 */
public class MenuActivity extends AppCompatActivity {
    Intent switchActvityIntent;
    ImageView whatIsRegImg;
    ImageView howToPetRegImg;
    ImageView searchPetRegPlaceImg;
    ImageView myPageImg;
    ImageView regReviseGuideImg;
    ImageView communityImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 뷰 정의
        whatIsRegImg = (ImageView) findViewById(R.id.whatIsPetRegImg);
        howToPetRegImg = (ImageView) findViewById(R.id.howToPetRegImg);
        searchPetRegPlaceImg = (ImageView) findViewById(R.id.searchPetRegPlaceImg);
        myPageImg = (ImageView) findViewById(R.id.myPageImg);
        regReviseGuideImg = (ImageView) findViewById(R.id.regReviseGuideImg);
        communityImg = (ImageView) findViewById(R.id.communityImg);

        // 뷰 클릭 이벤트
        whatIsRegImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "WhatIsRegImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        howToPetRegImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "howToPetRegImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), TypeActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        searchPetRegPlaceImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "findPetRegPlaceImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        myPageImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "myPageImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        regReviseGuideImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "regReviseGuideImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), ReviseActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        communityImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "communityImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), CommunityActivity.class);
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
