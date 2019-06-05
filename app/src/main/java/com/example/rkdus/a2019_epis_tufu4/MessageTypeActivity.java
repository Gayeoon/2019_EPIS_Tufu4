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
 * 예약하기 전 내장형 또는 외장형 선택하는 액티비티
 * - 이해원
 */
public class MessageTypeActivity extends AppCompatActivity {
    Intent switchActvityIntent;
    ImageView innerTypeImg;
    ImageView outerTypeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_type);

        // 뷰 정의
        innerTypeImg = (ImageView) findViewById(R.id.innerTypeImg);
        outerTypeImg = (ImageView) findViewById(R.id.outerTypeImg);

        // 뷰 클릭 이벤트
        innerTypeImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "innerTypeImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), MessageActivity.class);
                        switchActvityIntent.putExtra("type", "inner");  // 내장형이라는 데이터 값 넣기
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        outerTypeImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "outerTypeImg click", Toast.LENGTH_LONG).show();
                        switchActvityIntent = new Intent(getApplicationContext(), MessageActivity.class);
                        switchActvityIntent.putExtra("type", "outer");  // 외장형이라는 데이터 값 넣기
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
