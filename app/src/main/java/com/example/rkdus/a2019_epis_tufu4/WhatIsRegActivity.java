package com.example.rkdus.a2019_epis_tufu4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

/*
 * 사용자
 * 반려동물 등록제란?
 * 정보 출력 액티비티
 * - 이해원
 */
public class WhatIsRegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_reg);

        ImageView ivRegInfo = (ImageView) findViewById(R.id.regInfoImg);
        ivRegInfo.setImageResource(R.drawable.what_is_reg_info);


    }
}
