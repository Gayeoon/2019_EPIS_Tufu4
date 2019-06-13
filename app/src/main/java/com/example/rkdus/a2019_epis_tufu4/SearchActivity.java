package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/*
 * 사용자
 * 반려동물 대행 등록업체를 리스트로 출력하고, 검색 기능 추가
 * 현재 위치를 받아서 현재 위치와 가까운 병원 순으로 나열하는 액티비티
 * - 이해원
 */
public class SearchActivity extends AppCompatActivity {
    public static final String SERVER_URL = "http://192.168.0.39:3000";

    /*
    반려동물 등록대행업체 조회 API DATA 속성정보

    1   주소           ADDR           반려동물 등록대행업체의 사무실주소
    2   상세주소       DETAIL_ADDR       반려동물 등록대행업체의 사무실의 상세주소
    3   대표자명       RPRSNTV_NM       반려동물 등록대행업체의 대표자명
    4   업체명           ENTRPS_NM       반려동물 등록대행업체명
    5   업체전화번호   ENTRPS_TELNO   반려동물 등록대행업체 전화번호

    API_KEY       STRING(기본)   sample           발급받은 API_KEY
    TYPE       STRING(기본)   xml               요청파일 타입 xml, json
    API_URL       STRING(기본)   Grid_000001       OpenAPI 서비스 URL
    START_INDEX   INTEGER(기본)   1               요청시작위치
    END_INDEX   INTEGER(기본)   5               요청종료위치
    RPRSNTV_NM   STRING(필수)   죽전동물병원   업체 명

    디비 테이블 저장 정보
    테이블 이름 : AGENCY_TB
    AGENCY_TB_PK    |   ADDRESS1    |   ADDRESS2    |   CEO_NAME    |   AGENCY_NAME |   PHONE_NUMBER
    기본키                주소           상세주소        대표자명        병원명             전화번호
    AGENCY_TB_PK: int(11),
    ADDRESS1: varchar(80),
    ADDRESS2: varchar(50),
    CEO_NAME: varchar(10),
    AGENCY_NAME: varchar(20),
    PHONE_NUMBER: varchar(15)
     */

    boolean isSearchCurrentLocation, isSignUpApp;
    final String switchOnColor = "#0067A3";
    final String switchOffColor = "#000000";
    String searchResult;
    ArrayList<SearchItemData> searchList = new ArrayList<>();
    SearchAsyncTask searchAsyncTask;

    ImageView ivSearchBtn;
    EditText eSearch;
    TextView searchCurrentLocationSwitch;
    ListView lvSearchList;
    CheckedTextView ctvIsSignUpApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 변수 값 초기화
        isSearchCurrentLocation = false;
        isSignUpApp = false;
        searchAsyncTask = new SearchAsyncTask();

        // 레이아웃 뷰 정의
        ivSearchBtn = (ImageView) findViewById(R.id.searchBtn);
        eSearch = (EditText) findViewById(R.id.searchEditText);
        searchCurrentLocationSwitch = (TextView) findViewById(R.id.isSearchCurrentLocation);
        ctvIsSignUpApp = (CheckedTextView) findViewById(R.id.isSignUpApp);
        lvSearchList = (ListView) findViewById(R.id.searchListView);

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
                        searchResult = eSearch.getText().toString().trim();    // 검색어 별도의 변수에 저장
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
                        searchResult = eSearch.getText().toString().trim();    // 검색어 별도의 변수에 저장. trim으로 공백 제거
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
        if(TextUtils.isEmpty(searchResult)) { // 공백처리
            Toast.makeText(getApplicationContext(), "값을 입력해주세요.", Toast.LENGTH_LONG).show();
        }
        else {
            searchAsyncTask.execute();
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

    /*
    검색을 위한 백그라운드 작업 + UI Thread 활용하는 AsyncTask
     */
    private class SearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String search_url = SERVER_URL + "/getData";
            try {
                URL url = new URL(SERVER_URL);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                // 각각 값을 제대로 받았는지 체크하기 위한 boolean
                boolean isGetHospitalName = false;
                boolean isGetCeoName = false;
                boolean isGetPhoneNum = false;
                int eventType = parser.getEventType();

                // 파싱 시작
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT: // xml의 시작순간
                            break;
                        case XmlPullParser.END_DOCUMENT: // xml의 끝순간
                            break;
                        case XmlPullParser.START_TAG:
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    /*
    어플등록 따지는 함수
    DB들어가서 체크하기
     */

    /*
    어플등록 필터 여부에 따른 Array값 정리
     */
}