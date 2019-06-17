package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HospitalActivity extends AppCompatActivity {
    public static final String SERVER_URL = "https://201502119.iptime.org:3306";
    public static final String TAG = "HospitalActivity";
    SearchAsyncTask searchAsyncTask;

    String id;
    String hos_name;

    TextView name, count;
    ImageButton status, community, alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        searchAsyncTask = new SearchAsyncTask();

        Intent intent = getIntent();
        //id = intent.getStringExtra("id");
        id = "hi";

        name = (TextView) findViewById(R.id.name);
        count = (TextView) findViewById(R.id.count);

        status = (ImageButton) findViewById(R.id.status);
        community = (ImageButton) findViewById(R.id.community);
        alarm = (ImageButton) findViewById(R.id.alarm);

        count.setText(String.valueOf(Newmessage(id)));

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hos_name = HospitalName(id);

                name.setText(hos_name);
//                Intent intent2 = new Intent(getApplicationContext(), StatusActivity.class);
//                intent2.putExtra("id", id);
//                startActivity(intent2);
            }
        });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent2);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), AlarmActivity.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
            }
        });
    }

    private int Newmessage(String id) {
        // To 지원
        // id 가지고 메세지 몇개 왔는지 검색해주세여!
        // 혹시 새로운 메세지 중간에 도착하면
        // count.setText("여기에"); -> 입력하시면 됩니다!

        return 0;
    }

    private String HospitalName(String id){
        // To 지원
        // id 가지고 hospital_name 좀 검색해주세여!
        String name = String.valueOf(searchAsyncTask.execute());
        return name;
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String search_url = SERVER_URL + "/getTest";    // URL
            // 서버에 특정 키워드 디비에서 검색 요청

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", id); // id 줄게 병원 이름 돌려줭

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
                    writer.write(jsonObject.toString());
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
    HttpURLConnection 연결 잘 안되는 경우 원인 내용 Log 출력
     */
    public static void printConnectionError(HttpURLConnection con) throws IOException {
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
}

