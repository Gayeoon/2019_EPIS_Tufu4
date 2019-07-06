package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.gaze.rkdus.a2019_epis_tufu4.SearchActivity.SERVER_URL;
import static com.gaze.rkdus.a2019_epis_tufu4.SearchActivity.StringToJSON;
import static com.gaze.rkdus.a2019_epis_tufu4.SearchActivity.printConnectionError;

/*
예약하기 버튼 클릭 시 나타나는 팝업창
 */
public class MessagePopupActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    Button okBtn, cancelBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        intent = new Intent();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                setResult(RESULT_OK, intent);
                //액티비티(팝업) 닫기
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                setResult(RESULT_CANCELED, intent);
                //액티비티(팝업) 닫기
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
