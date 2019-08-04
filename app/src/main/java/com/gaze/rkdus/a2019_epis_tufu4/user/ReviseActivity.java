package com.gaze.rkdus.a2019_epis_tufu4.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;

/*
 * 사용자
 * 동물 등록증 수정 가이드 정보를 표시하는 액티비티
 * - 이해원
 */
public class ReviseActivity extends BaseActivity {

    ImageView ivRevise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);

        ivRevise = (ImageView) findViewById(R.id.reviseImg);
        ivRevise.setImageResource(R.drawable.revise_info_img);
    }
}
