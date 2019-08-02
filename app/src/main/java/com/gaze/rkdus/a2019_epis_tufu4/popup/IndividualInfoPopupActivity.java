package com.gaze.rkdus.a2019_epis_tufu4.popup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.gaze.rkdus.a2019_epis_tufu4.R;

public class IndividualInfoPopupActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    WebView infoDocumentWebView;

    Button okBtn, cancelBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_info_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        infoDocumentWebView = (WebView) findViewById(R.id.infoDocumentWebView);

        infoDocumentWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        infoDocumentWebView.setVerticalScrollBarEnabled(true);
        infoDocumentWebView.loadUrl("file:///android_asset/reservation_infodocument.html");
        intent = new Intent();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                setResult(RESULT_OK);
                //액티비티(팝업) 닫기
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                setResult(RESULT_CANCELED);
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
