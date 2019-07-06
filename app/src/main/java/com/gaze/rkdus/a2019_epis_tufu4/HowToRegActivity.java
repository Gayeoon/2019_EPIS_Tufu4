package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * 사용자
 * 반려동물 등록방법 태그 방식 별 정보 제공 액티비티
 * - 이해원
 */
public class HowToRegActivity extends AppCompatActivity {
    ImageView ivHowToReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_reg);

        ivHowToReg = (ImageView) findViewById(R.id.howToRegImg);

        Intent typeIntent = getIntent();
        if(typeIntent != null) {    // 인텐트 null 체크
            // SearchActivity에서 정한 타입 값 불러오기
            if(typeIntent.hasExtra("type")) {
                String type = typeIntent.getStringExtra("type");   // 병원 type값 int에 저장
                if(type.equals("inner"))
                    ivHowToReg.setImageResource(R.drawable.howto_innerimg);
                if(type.equals("outer"))
                    ivHowToReg.setImageResource(R.drawable.howto_outerimg);
                if(type.equals("badge"))
                    ivHowToReg.setImageResource(R.drawable.howto_badgeimg);
            }
            else {
                Toast.makeText(getApplicationContext(), "필수 값을 불러올 수 없습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "타입이 선택되지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
