package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 사용자
 * 반려동물 대행 등록업체를 리스트로 출력하고, 검색 기능 추가
 * 현재 위치를 받아서 현재 위치와 가까운 병원 순으로 나열하는 액티비티
 * - 이해원
 */
public class SearchActivity extends AppCompatActivity {
    boolean isSearchCurrentLocation, isSignUpApp;
    final String switchOnColor = "#0067A3";
    final String switchOffColor = "#000000";
    String searchResult;

    ImageView ivSearchBtn;
    EditText eSearch;
    TextView searchCurrentLocationSwitch;
    CheckedTextView ctvIsSignUpApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 변수 값 초기화
        isSearchCurrentLocation = false;
        isSignUpApp = false;

        // 레이아웃 뷰 정의
        ivSearchBtn = (ImageView) findViewById(R.id.searchBtn);
        eSearch = (EditText) findViewById(R.id.searchEditText);
        searchCurrentLocationSwitch = (TextView) findViewById(R.id.isSearchCurrentLocation);
        ctvIsSignUpApp = (CheckedTextView) findViewById(R.id.isSignUpApp);

        Button button = (Button) findViewById(R.id.temp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageTypeActivity.class);
                startActivity(intent);
            }
        });

        // EditText 자판 리스너 이벤트
        eSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH: // 자판에서 검색 모양 아이콘을 누르면
                        Toast.makeText(getApplicationContext(), "검색을 시작합니다.", Toast.LENGTH_LONG).show();
                        searchResult = eSearch.getText().toString();    // 검색어 별도의 변수에 저장
                        showSearchList();
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });

        // 검색 이미지 클릭 이벤트
        ivSearchBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "검색 이미지 click", Toast.LENGTH_LONG).show();
                        searchResult = eSearch.getText().toString();    // 검색어 별도의 변수에 저장
                        showSearchList();
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

        // 어플등록 체크박스 체크 이벤트
        ctvIsSignUpApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckedTextView view = (CheckedTextView) v;
                view.toggle();

                if(view.isChecked()) {
                    isSignUpApp = true;
                    Toast.makeText(getApplicationContext(), "어플등록업체 보기 체크", Toast.LENGTH_LONG).show();
                }
                else {
                    isSignUpApp = false;
                    Toast.makeText(getApplicationContext(), "어플등록업체 보기 체크 X", Toast.LENGTH_LONG).show();
                }
                showSignUpApp();
            }
        });
        // 현재위치로 찾기 클릭 시
        searchCurrentLocationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchOnOff();  // 현재 상태에 따라 on / off 변경
            }
        });
    }

    /*
    현재 위치로 찾기 클릭에 따른 찾기 온오프 기능 구현 함수
     */
    private void switchOnOff() {
        if(isSearchCurrentLocation) {   // 만약 현재 위치로 찾기로 리스트를 보여주고 있는 경우
            searchCurrentLocationSwitch.setTextColor(Color.parseColor(switchOffColor));  // 색상변경
            Toast.makeText(getApplicationContext(), "현재 위치로 찾기 종료합니다.", Toast.LENGTH_LONG).show();
            isSearchCurrentLocation = false;
        }
        else {  // 현재 위치로 찾고 싶은 경우
            searchCurrentLocationSwitch.setTextColor(Color.parseColor(switchOnColor));  // 색상변경
            Toast.makeText(getApplicationContext(), "현재 위치로 찾기 시작합니다.", Toast.LENGTH_LONG).show();
            isSearchCurrentLocation = true;
        }
        showSearchList();
    }

    /*
    조건에 따라 리스트 보여주는 함수
     */
    private void showSearchList() {
        searchResult = searchResult.trim();
        if(searchResult.getBytes().length <= 0) { // null(빈 값) 처리


            Toast.makeText(getApplicationContext(), "값을 입력해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    /*
    온오프에 따른 어플등록 업체만 보기 유무 설정해서 출력하는 함수
     */
    private void showSignUpApp() {
        if(isSignUpApp) {

        }
        else {

        }
    }
}
