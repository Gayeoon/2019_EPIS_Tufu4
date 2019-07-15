package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.gaze.rkdus.a2019_epis_tufu4.MessageActivity.checkEditText;
import static com.gaze.rkdus.a2019_epis_tufu4.MessageActivity.checkStringWS;

public class FilterPopupActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    Button okBtn, cancelBtn;
    TextView tvDistance, tvBestReservation, tvNothing, tvLocation;
    EditText eMegalopolis, eCity, eDong;
    LinearLayout selectFilterLayout, locationFilterLayout;

    Button okLocationBtn, cancelLocationBtn;
    Intent intent;

    String megalopolis, city, dong, location;
    int type = 0;   // 1 : 최다 예약 순.     2 : 거리 순.   3 : 지역 별    4 : 지역 별 + 최다 예약 순

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_popup);

        // Filter Popup Layout
        selectFilterLayout = (LinearLayout) findViewById(R.id.selectFilterLayout);
        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        tvBestReservation = (TextView) findViewById(R.id.reservationCount);
        tvDistance = (TextView) findViewById(R.id.distanceCount);
        tvNothing = (TextView) findViewById(R.id.nothing);
        tvLocation = (TextView) findViewById(R.id.filterLocation);

        // Location Filter Popup Layout
        locationFilterLayout = (LinearLayout) findViewById(R.id.locationFilterLayout);
        eMegalopolis = (EditText) findViewById(R.id.megalopolisEditText);
        eCity = (EditText) findViewById(R.id.cityEditText);
        eDong = (EditText) findViewById(R.id.dongEditText);
        okLocationBtn = (Button) findViewById(R.id.okLocationBtn);
        cancelLocationBtn = (Button) findViewById(R.id.cancelLocationBtn);

        Intent getIntent = getIntent();

        if(getIntent != null) {    // 인텐트 null 체크

           if(getIntent.hasExtra("filter"))
               type = getIntent.getIntExtra("filter", 0);
               switch (type) {
                   case 4:
                       location = getIntent.getStringExtra("location");
                       tvLocation.setTextColor(getResources().getColor(R.color.colorBlue));
                       tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlue));
                       break;
                   case 3:
                       location = getIntent.getStringExtra("location");
                       tvLocation.setTextColor(getResources().getColor(R.color.colorBlue));
                       break;
                   case 1:
                       tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlue));
                       break;
                       default:
                           break;
               }
        }

        intent = new Intent();

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 3) {     // 지역 별
                    type = 0;
                    tvLocation.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else if(type == 4) {    // 지역 별 + 최다 예약 순
                    type = 1;
                    tvLocation.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else {  // 지역 별 필터가 체크되어있지 않은 경우
                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
                    startLocationFilterLayout();    // Location Layout 시작
                }
            }
        });

        tvBestReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1) { // 최다 예약 순
                    type = 0;
                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else if(type == 4) {    // 지역 별 + 최다 예약 순
                    type = 3;
                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else {  // 최다 예약 순 필터가 체크되어있지 않은 경우
                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlue));
                    if(type == 3)
                        type = 4;
                    else
                        type = 1;
                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });

        tvDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(type == 2) {
//                    type = 0;
//                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
//                    tvDistance.setTextColor(getResources().getColor(R.color.colorBlack));
//                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
//                }
//                else {
//                    type = 2;
//                    tvNothing.setTextColor(getResources().getColor(R.color.colorBlack));
//                    tvDistance.setTextColor(getResources().getColor(R.color.colorBlue));
//                    tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
//                }
                Toast.makeText(getApplicationContext(), "해당 기능은 준비중입니다. \n다음 업데이트를 기대하세요!(씽긋)", Toast.LENGTH_LONG).show();
            }
        });

        tvNothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                tvNothing.setTextColor(getResources().getColor(R.color.colorBlue));
                tvDistance.setTextColor(getResources().getColor(R.color.colorBlack));
                tvBestReservation.setTextColor(getResources().getColor(R.color.colorBlack));
                tvLocation.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                intent.putExtra("result", type);
                if(type == 4 || type == 3)  // 지역 별을 선택한 경우
                    intent.putExtra("location", location);
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

    /*
    Location Filter Layout 시작
     */
    private void startLocationFilterLayout() {
        locationFilterLayout.setVisibility(View.VISIBLE);
        selectFilterLayout.setVisibility(View.GONE);

        Log.d(TAG, "oKLocationBtn click");
        okLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "oKLocationBtn click");
                // editText 값 Null 체크
                if(checkEditText(eMegalopolis)) {
                    megalopolis = eMegalopolis.getText().toString();
                    location = megalopolis;
                }
                else {
                   Toast.makeText(getApplicationContext(), "최소 시/도는 입력해야 합니다.", Toast.LENGTH_LONG).show();
                   return;
                }

                if(checkEditText(eCity)) {
                    city = eCity.getText().toString();
                    location += " " + city;
                }
                if(checkEditText(eDong)) {
                    dong = eDong.getText().toString();
                    location += " " + dong;
                }

                // 레이아웃, type 설정
                locationFilterLayout.setVisibility(View.GONE);
                selectFilterLayout.setVisibility(View.VISIBLE);
                tvLocation.setTextColor(getResources().getColor(R.color.colorBlue));
                if(type == 1)
                    type = 4;
                else
                    type = 3;

                Log.d(TAG, "location : " + location);
            }
        });

        cancelLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "cancelLocationBtn click");
                // Filter Popup Layout으로 돌아가기
                // editText 초기화
                eMegalopolis.setText("");
                eCity.setText("");
                eDong.setText("");

                locationFilterLayout.setVisibility(View.GONE);
                selectFilterLayout.setVisibility(View.VISIBLE);
                tvLocation.setTextColor(getResources().getColor(R.color.colorBlack));
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
