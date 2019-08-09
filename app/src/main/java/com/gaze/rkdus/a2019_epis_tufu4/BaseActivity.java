package com.gaze.rkdus.a2019_epis_tufu4;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    public Typeface mTypeface = null;
    public final static String TAG = "LogGoGo";
    public static final String SERVER_URL = "http://vowow.cafe24app.com";
    public static long KAKAO_ID = 0;    // 0 : null
    public static String NICKNAME = null;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(this.getAssets(), "font/notosansregular.ttf");
        }

        setGlobalFont(getWindow().getDecorView());
    }

    private void setGlobalFont(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int vgCnt = vg.getChildCount();
                for (int i = 0; i < vgCnt; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(mTypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }

    public void finishPopup() {
        Toast.makeText(getApplicationContext(), "필수 값이 들어가지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
        finish();
    }
}
