package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.rkdus.a2019_epis_tufu4.MessageActivity.checkStringWS;

/*
 사용자가 커뮤니티 들어갈 때 닉네임 설정하는 팝업창
 */
public class NicnamePopupActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    Button okBtn, cancelBtn;
    ImageView checkNicnameBtn;
    EditText eNicname;
    Intent intent;

    boolean checkNicname = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nicname_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        checkNicnameBtn = (ImageView) findViewById(R.id.checkNicnameImage);
        eNicname = (EditText) findViewById(R.id.nicnameEditText);

        intent = new Intent();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                if(checkNicname) { // 중복체크 여부
                    if (checkStringWS(eNicname.getText().toString())) { // 공백 체크
                        intent.putExtra("username", eNicname.getText().toString());
                        setResult(RESULT_OK, intent);
                        //액티비티(팝업) 닫기
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "입력 칸에 공백이 존재합니다. 다시 작성하여 중복체크하세요.", Toast.LENGTH_SHORT).show();
                        checkNicname = false;
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "중복 체크를 먼저 해야 합니다.", Toast.LENGTH_SHORT).show();
                }
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

        checkNicnameBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Log.d(TAG, "닉네임 중복체크");

                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
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
