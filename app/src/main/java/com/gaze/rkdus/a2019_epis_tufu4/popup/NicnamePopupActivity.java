package com.gaze.rkdus.a2019_epis_tufu4.popup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.user.MenuActivity;

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

import static com.gaze.rkdus.a2019_epis_tufu4.user.MessageActivity.checkStringWS;
import static com.gaze.rkdus.a2019_epis_tufu4.user.SearchActivity.StringToJSON;
import static com.gaze.rkdus.a2019_epis_tufu4.user.SearchActivity.printConnectionError;

/*
 사용자가 커뮤니티 들어갈 때 닉네임 설정하는 팝업창
 */
public class NicnamePopupActivity extends BaseActivity {
    public static final String TAG = "LogGoGo";
    ImageView checkNicnameBtn, okBtn;
    EditText eNicname;
    Intent intent;

    String nickname;
    boolean checkNickname = false;
    NicnameAsyncTask nicnameAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nicname_popup);

        okBtn = (ImageView) findViewById(R.id.okBtn);
        checkNicnameBtn = (ImageView) findViewById(R.id.checkNicnameImage);
        eNicname = (EditText) findViewById(R.id.nicnameEditText);

        intent = new Intent();

        okBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Log.d(TAG, "닉네임 설정");
                        //데이터 전달하기
                        if(checkNickname) { // 중복체크 여부
                            if (checkStringWS(eNicname.getText().toString())) { // 공백 체크
                                if(nickname.equals(eNicname.getText().toString())) { // 중복 체크 값과 입력 값이 동일한 경우
                                    nicnameAsyncTask = new NicnameAsyncTask();
                                    nicnameAsyncTask.execute("/user/join", "add");
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "중복검사한 값과 입력한 값이 다릅니다. 다시 작성하여 중복체크하세요.", Toast.LENGTH_SHORT).show();
                                    checkNickname = false;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "입력 칸에 공백이 존재합니다. 다시 작성하여 중복체크하세요.", Toast.LENGTH_SHORT).show();
                                checkNickname = false;
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(), "중복 체크를 먼저 해야 합니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

        checkNicnameBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Log.d(TAG, "닉네임 중복체크");
                        if (checkStringWS(eNicname.getText().toString())) { // 공백 체크
                            nickname = eNicname.getText().toString();
                            nicnameAsyncTask = new NicnameAsyncTask();
                            nicnameAsyncTask.execute("/user/nameCheck", "check");
                        }
                        else
                            Toast.makeText(getApplicationContext(), "입력 칸에 공백이 존재합니다. 다시 작성하여 중복체크하세요.", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

    }

    /*
    서버에 닉네임 중복 체크 및 DB에 닉네임 등록하는 AsyncTask
     */
    private class NicnameAsyncTask extends AsyncTask<String, Void, String> {
        String type;

        @Override
        protected String doInBackground(String... strings) {
            String search_url = SERVER_URL + strings[0];    // URL
            type = strings[1];
            // 서버에 메세지 정보 전송
            try {
                // String type, ownerName, address, hp, petName, race, petColor, petBirth, neutralization, petGender;
                JSONObject jsonObject = new JSONObject();
                // Message에 담은 모든 정보 JSONObject에 담기
                jsonObject.accumulate("NICKNAME", nickname); // key JSONObject에 담기

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
                int getResult = jsonObject.getInt("result");
                Log.d(TAG, "result : " + getResult);
                if(type.equals("check")) {
                    if(getResult == 1) { // 중복체크 통과
                        Toast.makeText(getApplicationContext(), "중복된 닉네임이 없습니다.", Toast.LENGTH_LONG).show();
                        checkNickname = true;
                    }
                    else
                        Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_LONG).show();
                }
                else if(type.equals("add")) {
                    if(getResult == 1) {    // DB에 생성 완료
                        Toast.makeText(getApplicationContext(), "닉네임 설정 완료", Toast.LENGTH_LONG).show();
                        NICKNAME = nickname;
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "서버 오류입니다. 잠시 후 다시 실행해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "서버 오류입니다. 잠시 후 다시 실행해주세요.", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
