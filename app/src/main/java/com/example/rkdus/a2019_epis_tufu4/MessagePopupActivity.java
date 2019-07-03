package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.SERVER_URL;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.StringToJSON;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.printConnectionError;

/*
예약하기 버튼 클릭 시 나타나는 팝업창
 */
public class MessagePopupActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    Button okBtn, cancelBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_popup);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        intent = new Intent();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
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
    서버에 닉네임 중복있는지 검사
     */
//    private class CheckNicnameAsyncTask extends AsyncTask<String, Void, String> {
//        JSONObject jsonObject = new JSONObject();
//        @Override
//        protected String doInBackground(String... strings) {
//            String search_url = SERVER_URL + strings[0];    // URL
//            // 서버에 메세지 정보 전송
//            try {
//                // String type, ownerName, address, hp, petName, race, petColor, petBirth, neutralization, petGender;
//                // Message에 담은 모든 정보 JSONObject에 담기
//                jsonObject.accumulate("HOSPITAL_KEY", key); // key JSONObject에 담기
//                jsonObject.accumulate("TYPE", type); // type JSONObject에 담기
//                jsonObject.accumulate("OWNER_NAME", ownerName);
//                jsonObject.accumulate("OWNER_RESIDENT", ownerRRN);
//                jsonObject.accumulate("OWNER_PHONE_NUMBER", ownerHP);
//                jsonObject.accumulate("OWNER_ADDRESS1", ownerAddress);
//                jsonObject.accumulate("OWNER_ADDRESS2", ownerRealAddress);
//                jsonObject.accumulate("PET_NAME", petName);
//                jsonObject.accumulate("PET_VARIETY", petRace);
//                jsonObject.accumulate("PET_COLOR", petColor);
//                jsonObject.accumulate("PET_GENDER", petGender);
//                jsonObject.accumulate("PET_NEUTRALIZATION", petNeutralization);
//                jsonObject.accumulate("PET_BIRTH", petBirth);
//                jsonObject.accumulate("ASK_DATE", petGetDate);
//                jsonObject.accumulate("ETC", petSpecialProblem);
//
//                // POST 전송방식을 위한 설정
//                HttpURLConnection con = null;
//                BufferedReader reader = null;
//
//                Log.d(TAG, "url : " + search_url);
//                Log.d(TAG, "type : " + type);
//                URL url = new URL(search_url);  // URL 객체 생성
//                Log.d(TAG, "jsonObject String : " + jsonObject.toString());
//                con = (HttpURLConnection) url.openConnection();
//                con.setConnectTimeout(10 * 1000);   // 서버 접속 시 연결 시간
//                con.setReadTimeout(10 * 1000);   // Read시 연결 시간
//                con.setRequestMethod("POST"); // POST방식 설정
//                con.setRequestProperty("Content-Type", "application/json"); // application JSON 형식으로 전송
//                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
//                con.setRequestProperty("Accept", "text/json"); // 서버에 response 데이터를 html로 받음 -> JSON 또는 xml
//                con.setDoOutput(true); // Outstream으로 post 데이터를 넘겨주겠다는 의미
//                con.setDoInput(true); // Inputstream으로 서버로부터 응답을 받겠다는 의미
//
//                // 서버로 보내기위해서 스트림 만듬
//                OutputStream outStream = con.getOutputStream();
//                // 버퍼를 생성하고 넣음
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
//                writer.write(jsonObject.toString());    // searchword : 검색키워드 식으로 전송
//                writer.flush();
//                writer.close(); // 버퍼를 받아줌
//
//                con.connect();
//
//                // 응답 코드 구분
//                int responseCode = con.getResponseCode();   // 응답 코드 설정
//                if(responseCode == HttpURLConnection.HTTP_OK)  // 200 정상 연결
//                //서버로 부터 데이터를 받음
//                {
//                    //서버로 부터 데이터를 받음
//                    InputStream stream = con.getInputStream();
//                    reader = new BufferedReader(new InputStreamReader(stream));
//                    StringBuffer buffer = new StringBuffer();
//                    String line;    // 한 줄씩 읽어오기 위한 임시 String 변수
//                    while((line = reader.readLine()) != null){
//                        buffer.append(line); // buffer에 데이터 저장
//                    }
//                    return buffer.toString(); //서버로 부터 받은 값을 리턴해줌
//                }
//                else {  // 연결이 잘 안됨
//                    printConnectionError(con);
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            JSONObject jsonObject = StringToJSON(result);
//            try {
//                if(jsonObject.getInt("result") == 1) {
//                    if(saveMyReservationFile(jsonObject)) {
//                        Toast.makeText(getApplicationContext(), "예약 성공! 저장 성공!", Toast.LENGTH_LONG).show();
//                        setResult(RESULT_OK);
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "예약 성공! 저장 실패!", Toast.LENGTH_LONG).show();
//                        setResult(RESULT_CANCELED);
//                    }
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "예약 실패! 잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
