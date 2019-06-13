package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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
    public static final String TAG = "SearchActivity";
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
    String searchWord;
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
                        if(setSearchWord())
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
                        if(setSearchWord())
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
            searchAsyncTask.execute();
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
            String search_url = SERVER_URL + "/getHospitalData";    // URL
            // 서버에 특정 키워드 디비에서 검색 요청
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("searchword", searchWord); // 키워드 JSONObject에 담기

                // POST 전송방식을 위한 설정
                HttpURLConnection con = null;
                BufferedReader reader = null;
                URL url = new URL(search_url);  // URL 객체 생성

                con = (HttpURLConnection) url.openConnection();
                int responseCode = con.getResponseCode();   // 응답 코드 설정

                // 응답 코드 구분
                if(responseCode == HttpURLConnection.HTTP_OK) { // 200 정상 연결
                    con.setRequestMethod("POST"); // POST방식 설정
                    con.setRequestProperty("Cache-Control", "no-cache"); // 캐시 설정
                    con.setRequestProperty("Content-Type", "application/json"); // application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/xml"); // 서버에 response 데이터를 html로 받음 -> JSON 또는 xml
                    con.setDoOutput(true); // Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true); // Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();  // URL 접속 시작

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());    // searchword : 검색키워드 식으로 전송
                    writer.flush();
                    writer.close(); // 버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line;    // 한 줄씩 읽어오기 위한 임시 String 변수
                    while((line = reader.readLine()) != null){
                        buffer.append(line); // buffer에 데이터 저장
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
                }
                else {  // 연결이 잘 안됨
                    printConnectionError(con);
                }

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
            } catch (JSONException e) { // JSON 객체 오류
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String bufstr) {

        }
    }

    /*
    JSON 형식으로 저장된 String 값을 JSON Object로 변환해서 리턴
     */
    private JSONObject StringToJSON(String JSONstr) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(JSONstr);
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject;
    }

    /*
    받아온 String 값 Json 파일로 임시 저장
     */
    private boolean JSONObjTofile(JSONObject jsonObject, String filename) {
        filename += ".json";
        // 파일 생성(덮어쓰기)
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(filename, MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
            fileOutputStream.write(jsonObject.toString().getBytes());   // Json 쓰기
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /*
    어플등록 필터 여부에 따른 Array값 정리
     */

    /*
    Json 형식의 String 변수를 ArrayList 안에 전부 넣기
     */
    private void putStrInArrayList(String bufstr) {
        try {
            JSONObject jsonObject = StringToJSON(bufstr);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for(int i = 0; i<jsonArray.length(); i++) { // jsonArray에 담긴 jsonObject를 하나씩 꺼낸다.
                jsonObject = jsonArray.getJSONObject(i);
                
                // list.add(jsonObject.getInt("학번") +" "+ jsonObject.getString("이름") +" "+ jsonObject.getString("학과"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    HttpURLConnection 연결 잘 안되는 경우 원인 내용 Log 출력
     */
    private void printConnectionError(HttpURLConnection con) throws IOException {
        InputStream is = con.getErrorStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] byteBuffer = new byte[1024];
        byte[] byteData = null;
        int nLength = 0;
        while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
            baos.write(byteBuffer, 0, nLength);
        }
        byteData = baos.toByteArray();
        String response = new String(byteData);
        Log.d(TAG, "응답 코드 발생! 오류 내용 = " + response);
    }

    /*
    EditText 값 유효성 체크한 뒤 리턴
     */
    private boolean setSearchWord() {
        String eSearchTempText = eSearch.getText().toString();    // 검색어 임시 변수에 저장.
        if(TextUtils.isEmpty(eSearchTempText.trim())) { // 공백처리
            Toast.makeText(getApplicationContext(), "값을 입력해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!eSearchTempText.equals(eSearchTempText.trim())) { // 앞뒤 공백이 존재하는 단어 입력
            Toast.makeText(getApplicationContext(), "앞 뒤 공백이 있습니다. 삭제하고 다시 시도하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        searchWord = eSearchTempText;   // 전역 변수에 저장
        return true;
    }
}