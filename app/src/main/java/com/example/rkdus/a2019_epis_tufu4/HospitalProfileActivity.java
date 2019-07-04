package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import java.util.ArrayList;

import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.SERVER_URL;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.StringToJSON;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.printConnectionError;
import static java.lang.Thread.sleep;

/*
SearchActivity에서 검색한 결과 중 특정 병원을 클릭한 경우
나오는 병원의 프로필 화면
- 이해원
 */
public class HospitalProfileActivity extends BaseActivity {
    public static final String TAG = "LogGoGo";
    private static final int START_RESERVATION = 10;
    SearchResultData hospitalData;
    int key;

    TextView tvHospitalName, tvOwnerName, tvReservationCount, tvHospitalHP, tvHospitalAddress;
    ImageView ivHospitalImage, ivReservation;
    ProfileAsyncTask profileAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile);

        // SearchActivity에서 SearchResultData 불러오기
        Intent intent = getIntent();
        if(intent != null) {    // 인텐트 null 체크
            if(intent.hasExtra("data")) {
                hospitalData = (SearchResultData) intent.getSerializableExtra("data");
                key = hospitalData.getHOSPITAL_KEY();
            }
            else {
                Toast.makeText(getApplicationContext(), "필수 값을 불러올 수 없습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "타입이 선택되지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        // 뷰 정의
        tvHospitalName = (TextView) findViewById(R.id.hosProfHospitalName);
        tvOwnerName = (TextView) findViewById(R.id.hosProfOwnerName);
        tvReservationCount = (TextView) findViewById(R.id.hosProfReservationCount);
        tvHospitalHP = (TextView) findViewById(R.id.hosProfHospitalHP);
        tvHospitalAddress = (TextView) findViewById(R.id.hosProfHospitalAddress);
        ivHospitalImage = (ImageView) findViewById(R.id.hosProfImage);
        ivReservation = (ImageView) findViewById(R.id.hosProfReservationBtn);

        // 병원 정보 객체가 잘 들어왔는지 체크
        if(checkHospitalInfo()) {
            setHospitalInfo();
        }
        else{
            Toast.makeText(getApplicationContext(), "해당 병원에 대한 정보가 일부 누락되었습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        // 예약 이미지 클릭 이벤트
        ivReservation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hosinfo_translate);
                        v.startAnimation(animation);
                        if(hospitalData.getBoolSIGNUP_APP()) {  // 어플 등록 여부 체크
                            Log.d(TAG, "key : " + hospitalData.getHOSPITAL_KEY() + ", name : " + hospitalData.getHOSPITAL_NAME());
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //지연시키길 원하는 밀리초 뒤에 동작
                                    Intent reservationIntent = new Intent(getApplicationContext(), MessageActivity.class);
                                    reservationIntent.putExtra("key", hospitalData.getHOSPITAL_KEY());
                                    reservationIntent.putExtra("hospitalName", hospitalData.getHOSPITAL_NAME());
                                    startActivityForResult(reservationIntent, START_RESERVATION);
                                }
                            }, 450);

                        }
                        else
                            Toast.makeText(getApplicationContext(), "현재 해당 병원은 어플 등록이 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
    }

    /*
    병원 정보를 화면에 세팅하는 함수
     */
    private void setHospitalInfo() {
        tvHospitalName.setText(hospitalData.getHOSPITAL_NAME());
        tvOwnerName.setText(hospitalData.getCEO_NAME());
        tvReservationCount.setText(String.valueOf(hospitalData.getRESERVATION_COUNT()) + "건");
        tvHospitalHP.setText(hospitalData.getPHONE_NUMBER());

        if(TextUtils.isEmpty(hospitalData.getADDRESS2()))   // Address1만 존재
            tvHospitalAddress.setText(hospitalData.getADDRESS1());
        else                                                // Address1, 2 둘 다 존재
            tvHospitalAddress.setText(hospitalData.getADDRESS1() + " " + hospitalData.getADDRESS2());

        tvHospitalHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvHospitalHP.getText().toString()));
                startActivity(intent);
            }
        });
    }

    /*
    병원 정보가 맞게 들어왔는지 확인
     */
    private boolean checkHospitalInfo() {
        if(hospitalData == null)   // 객체 null 체크
            return false;
        else {
            // 앱 등록 안되있어도 들어오는지? 그럴거같다.
            if (TextUtils.isEmpty(hospitalData.getHOSPITAL_NAME()))
                return false;    // 병원 이름 null 체크
            if (TextUtils.isEmpty(hospitalData.getCEO_NAME()))
                return false;    // 병원 대표자명 null 체크
            if (TextUtils.isEmpty(hospitalData.getPHONE_NUMBER()))
                return false;    // 병원 전화번호 null 체크
            if (TextUtils.isEmpty(hospitalData.getADDRESS1()))
                return false;    // 병원 주소 null 체크(필수 주소)

            return true;
        }
    }

    /*
    서버에 예약 정보를 POST 방식으로 ID에 요청하도록 전송하기
    AsyncTask 사용
     */
    private class ProfileAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String search_url = SERVER_URL + strings[0];    // URL
            // 서버에 메세지 정보 전송
            try {
                // String type, ownerName, address, hp, petName, race, petColor, petBirth, neutralization, petGender;
                JSONObject jsonObject = new JSONObject();
                // Message에 담은 모든 정보 JSONObject에 담기
                jsonObject.accumulate("HOSPITAL_KEY", key); // key JSONObject에 담기

                // POST 전송방식을 위한 설정
                HttpURLConnection con = null;
                BufferedReader reader = null;

                Log.d(TAG, "url : " + search_url);
                URL url = new URL(search_url);  // URL 객체 생성
                Log.d(TAG, "jsonObject String : " + jsonObject.toString());
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10 * 1000);   // 서버 접속 시 연결 시간
                con.setReadTimeout(10 * 1000);   // Read시 연결 시간
                con.setRequestMethod("POST"); // POST방식 설정
                con.setRequestProperty("Content-Type", "application/json"); // application JSON 형식으로 전송
                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                con.setRequestProperty("Accept", "text/json"); // 서버에 response 데이터를 html로 받음 -> JSON 또는 xml
                con.setDoOutput(true); // Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true); // Inputstream으로 서버로부터 응답을 받겠다는 의미

                // 서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                // 버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());    // searchword : 검색키워드 식으로 전송
                writer.flush();
                writer.close(); // 버퍼를 받아줌

                con.connect();

                // 응답 코드 구분
                int responseCode = con.getResponseCode();   // 응답 코드 설정
                if(responseCode == HttpURLConnection.HTTP_OK) { // 200 정상 연결
                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line;    // 한 줄씩 읽어오기 위한 임시 String 변수
                    while((line = reader.readLine()) != null){
                        buffer.append(line); // buffer에 데이터 저장
                    }
                    return buffer.toString(); //서버로 부터 받은 값을 리턴해줌
                }
                else {  // 연결이 잘 안됨
                    printConnectionError(con);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject = StringToJSON(result);
            try {
                int count = jsonObject.getInt("RESERVATION_COUNT");
                if(count != 0) {
                    Log.d(TAG, "예약횟수 refresh");
                    Log.d(TAG, "예약횟수 : " + count);
                    tvReservationCount.setText(String.valueOf(count) + "건");  // refresh
                }
                else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case START_RESERVATION:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "예약 완료.");
                    // 해당 병원의 예약 횟수 불러오기?
                    profileAsyncTask = new ProfileAsyncTask();
                    profileAsyncTask.execute("/getReservationCount");
                }
                else {
                    Log.d(TAG, "예약 실패.");
                }
                break;
            default:
                break;
        }
    }
}
