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
 * 반려동물 등록방법 중 내,외장형, 등록 인식표 부착에 대한 설명을 보기 위한
 * 메뉴 액티비티
 * 원하는 정보를 보기 위해 이미지를 클릭하면 정보 표시하는 액티비티로 이동함
 * - 이해원
 */
public class TypeActivity extends AppCompatActivity {
    Intent gotoRegiInfoIntent;
    ImageView idTagTypeImg;
    ImageView innerTypeImg;
    ImageView outerTypeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        // 레이아웃 뷰 선언 및 정의
        idTagTypeImg = (ImageView) findViewById(R.id.idTagTypeImg);
        innerTypeImg = (ImageView) findViewById(R.id.innerTypeImg);
        outerTypeImg = (ImageView) findViewById(R.id.outerTypeImg);

        // 뷰 클릭 이벤트
        idTagTypeImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        gotoRegiInfoIntent = new Intent(getApplicationContext(), HowToRegActivity.class);
                        gotoRegiInfoIntent.putExtra("type", "badge"); // 인텐트에 타입 값 등록
                        startActivity(gotoRegiInfoIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        innerTypeImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        gotoRegiInfoIntent = new Intent(getApplicationContext(), HowToRegActivity.class);
                        gotoRegiInfoIntent.putExtra("type", "inner"); // 인텐트에 타입 값 등록
                        startActivity(gotoRegiInfoIntent);
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
                        gotoRegiInfoIntent = new Intent(getApplicationContext(), HowToRegActivity.class);
                        gotoRegiInfoIntent.putExtra("type", "outer"); // 인텐트에 타입 값 등록
                        startActivity(gotoRegiInfoIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

    }
}
