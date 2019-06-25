package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class FilterPopupActivity extends AppCompatActivity {
    Button okBtn, cancelBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        intent = new Intent();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                intent.putExtra("result", "");
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
