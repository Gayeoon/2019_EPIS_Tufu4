package com.gaze.rkdus.a2019_epis_tufu4.popup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationData;

import java.io.Serializable;

/*
예약하기 버튼 클릭 시 나타나는 팝업창
 */
public class MessagePopupActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    Button okBtn, cancelBtn;
    TextView tvMessagePopup;
    String messageText;
    Intent intent, resultIntent;
    MyReservationData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        tvMessagePopup = (TextView) findViewById(R.id.messagePopupText);

        resultIntent = new Intent();    // 결과로 전송할 인텐트
        intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra("messageType")) {
                messageText = intent.getStringExtra("messageType");
            }
            else
                finishPopup();
        }
        else
            finishPopup();

        switch (messageText) {
            case "reservation":
                tvMessagePopup.setText(R.string.reservationPopupMsg);
                break;
            case "registConfirm":
                tvMessagePopup.setText(R.string.registConfirmPopupMsg);
                if(intent.hasExtra("data"))
                    data = (MyReservationData) intent.getSerializableExtra("data");
                else
                    finishPopup();
                break;
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageText.equals("registConfirm")) {
                    data.setRESERVATION_STATE("CONFIRM");
                    resultIntent.putExtra("data", (Serializable) data);
                }
                //데이터 전달하기
                setResult(RESULT_OK, resultIntent);
                //액티비티(팝업) 닫기
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                setResult(RESULT_CANCELED, resultIntent);
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

    private void finishPopup() {
        Toast.makeText(getApplicationContext(), "필수 값이 들어가지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
        finish();
    }
}
