package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.SERVER_URL;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.printConnectionError;

/*
 * 사용자
 * 예약에 필요한 정보 작성하고 예약하는 액티비티
 * * 내장형과 외장형을 따로 저장해놓고 진행
 * - 이해원
 */
public class MessageActivity extends AppCompatActivity {
    String type, ownerName, address, hp, petName, race, petColor, petBirth, neutralization, petGender;
    String hospitalKey;
    EditText eOwnerName, eAddress, eHP, ePetName, eRace, ePetColor, ePetBirth;
    Spinner sNeutralization, sPetGender;
    ArrayAdapter aaNeutralization, aaPetGender;
    Button btnMessage;
    MessageAsyncTask messageAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // MessageTypeActivity에서 정한 타입 값 불러오기
        Intent typeIntent = getIntent();
        if(typeIntent != null) {    // 인텐트 null 체크
            if(typeIntent.hasExtra("id") && typeIntent.hasExtra("type")) {
                type = typeIntent.getStringExtra("type");   // 병원 type값 String에 저장
                hospitalKey = typeIntent.getStringExtra("id");   // 병원 id값 String에 저장
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


        // 객체 정의
        messageAsyncTask = new MessageAsyncTask();

        // 뷰 정의
        eOwnerName = (EditText) findViewById(R.id.ownerNameText);
        eAddress = (EditText) findViewById(R.id.addressText);
        eHP = (EditText) findViewById(R.id.hpText);
        ePetName = (EditText) findViewById(R.id.petNameText);
        ePetBirth = (EditText) findViewById(R.id.petBirthText);
        eRace = (EditText) findViewById(R.id.raceText);
        ePetColor = (EditText) findViewById(R.id.petColorText);
        ePetBirth = (EditText) findViewById(R.id.petBirthText);
        btnMessage = (Button) findViewById(R.id.messageButton);

        // 성별 Spinner Array 생성
        sPetGender = (Spinner) findViewById(R.id.petGenderSpinner); //butterknife 없을경우
        aaPetGender = ArrayAdapter.createFromResource(this, R.array.petGenderArray, android.R.layout.simple_spinner_dropdown_item);

        //  성별 선택 값 가져오기
        sPetGender.setAdapter(aaPetGender);
        sPetGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 성별 여부 선택 리스너
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                petGender = aaPetGender.getItem(position).toString();
                Toast.makeText(getApplicationContext(), petGender, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 중성화 Spinner Array 생성
        sNeutralization = (Spinner) findViewById(R.id.neutralizationSpinner); //butterknife 없을경우
        aaNeutralization = ArrayAdapter.createFromResource(this, R.array.neutralizationArray, android.R.layout.simple_spinner_dropdown_item);

        //  중성화 선택 값 가져오기
        sNeutralization.setAdapter(aaNeutralization);
        sNeutralization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 중성화 여부 선택 리스너
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                neutralization = aaNeutralization.getItem(position).toString();
                Toast.makeText(getApplicationContext(), neutralization, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 예약하기 버튼 클릭 시
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setOwnerInfo() && setPetInfo()) {    // 입력한 모든 정보가 올바르게 변수에 저장한 경우
                    // 서버에 해당 id에 전송
                    messageAsyncTask.execute();
                    messageAsyncTask.cancel(true);
                }
                else {  // 하나라도 값이 잘못된 경우
                    //clearEditText();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        messageAsyncTask.cancel(true); // 초기화
        super.onDestroy();
    }

    /*
    사용자가 적은 EditText 값 제거시키기
    사용은 모르겠음
     */
    private void clearEditText() {
    }

    /*
    소유자 정보를 가져와서 변수에 저장하기 + 공백 처리
    올바르지 않는 값의 정보를 토스트로 출력
    @return : boolean(true : 전부 저장 완료. false : 하나라도 저장 실패)
    */
    private boolean setOwnerInfo() {
        // 공백 체크
        if(checkEditText(eOwnerName))   ownerName = eOwnerName.getText().toString();
        if(checkEditText(eAddress))     address = eAddress.getText().toString();
        if(checkEditText(eHP))          hp = eHP.getText().toString();
        return true;
    }

    /*
    반려동물 정보를 가져와서 변수에 저장하기 + 공백 처리
    올바르지 않는 값의 정보를 토스트로 출력
    @return : boolean(true : 전부 저장 완료. false : 하나라도 저장 실패)
     */
    private boolean setPetInfo() {
        // 공백 체크
        if(checkEditText(ePetName))           petName = ePetName.getText().toString();
        if(checkEditText(eRace))              race = eRace.getText().toString();
        if(checkEditText(ePetColor))          petColor = ePetColor.getText().toString();
        if(checkEditText(ePetBirth))          petBirth = ePetBirth.getText().toString();
        return true;
    }

    /*
    EditText의 값 중 공백 확인
    @return : boolean(true : 앞 뒤 공백 또는 전체 공백이 아님. false : 둘 중 하나라도 해당하는 경우)
     */
    private boolean checkEditText(EditText editText) {
        String editStr = editText.getText().toString();    // 검색어 임시 변수에 저장.
        if(TextUtils.isEmpty(editStr.trim())) { // 공백처리
            Toast.makeText(getApplicationContext(), "아무 값도 적지 않은 항목이 있습니다. 모든 항목을 입력해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!editStr.equals(editStr.trim())) { // 앞뒤 공백이 존재하는 단어 입력
            Toast.makeText(getApplicationContext(), "앞 뒤 공백이 있습니다. 삭제하고 다시 시도하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /*
    서버에 예약 정보를 POST 방식으로 ID에 요청하도록 전송하기
    AsyncTask 사용
     */
    private class MessageAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String search_url = SERVER_URL + "/sendMessage";    // URL
            // 서버에 메세지 정보 전송
            try {
                // String type, ownerName, address, hp, petName, race, petColor, petBirth, neutralization, petGender;
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("key", hospitalKey); // key JSONObject에 담기
                jsonObject.accumulate("type", type); // type JSONObject에 담기
                // Message에 담은 모든 정보 JSONObject에 담기
                jsonObject.accumulate("ownerName", ownerName);
                jsonObject.accumulate("address", address);
                jsonObject.accumulate("hp", hp);
                jsonObject.accumulate("petName", petName);
                jsonObject.accumulate("race", race);
                jsonObject.accumulate("petColor", petColor);
                jsonObject.accumulate("petBirth", petBirth);
                jsonObject.accumulate("neutralization", neutralization);
                jsonObject.accumulate("petGender", petGender);

                // POST 전송방식을 위한 설정
                HttpURLConnection con = null;
                BufferedReader reader = null;
                URL url = new URL(search_url);  // URL 객체 생성

                con = (HttpURLConnection) url.openConnection();
                int responseCode = con.getResponseCode();   // 응답 코드 설정

                // 응답 코드 구분
                if(responseCode == HttpURLConnection.HTTP_OK) { // 200 정상 연결
                    con.setRequestMethod("POST"); // POST 방식 설정
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
                    writer.write(jsonObject.toString());    // JSONObject 객체 내 넣은 값들 전부 담아 전송
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
            } catch (JSONException e) { // JSON 객체 오류
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("OK")) {
                Toast.makeText(getApplicationContext(), "예약 성공!", Toast.LENGTH_LONG).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "예약 실패!  사유 : " + result, Toast.LENGTH_LONG).show();
        }
    }
}