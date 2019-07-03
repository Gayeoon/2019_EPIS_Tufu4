package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FilterPopupActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    Button okBtn, cancelBtn;
    TextView tvDistance, tvBestReservation, tvNothing;
    Intent intent;
    int type = 0;   // 1 : 최다 예약 순.     2 : 거리 순.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        tvBestReservation = (TextView) findViewById(R.id.reservationCount);
        tvDistance = (TextView) findViewById(R.id.distanceCount);
        tvNothing = (TextView) findViewById(R.id.nothing);

        intent = new Intent();

        tvBestReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1) {
                    type = 0;
                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvDistance.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else {
                    type = 1;
                    tvDistance.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });

        tvDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 2) {
                    type = 0;
                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvDistance.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else {
                    type = 2;
                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
                    tvDistance.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });

        tvNothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                tvNothing.setTextColor(getResources().getColor(R.color.colorBlue));
                tvDistance.setTextColor(getResources().getColor(R.color.colorBlack));
                tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                intent.putExtra("result", type);
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
