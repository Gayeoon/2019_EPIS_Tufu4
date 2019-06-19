package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class TempActivity extends AppCompatActivity {

    DatabaseCls db;
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        Button gotouser = (Button) findViewById(R.id.gotouser);
        Button gotojoin = (Button) findViewById(R.id.gotojoin);
        gotojoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        gotouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        // server test //

        tvData = (TextView) findViewById(R.id.dbContents);
        Button readDB = (Button) findViewById(R.id.readDB);

        readDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new JSONTask().execute("http://192.168.0.39:3000/getHospital");
                new JSONTask().execute("http://201502119.iptime.org/getHospital");
            }
        });
    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                // JSONObject를 만들고 형식 맞춰준다
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();

//                    JSONParser parser = new JSONParser();
                Object parseObj;


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.0.39:3000/getData");
                    URL url = new URL(urls[0]); // url 가져오기
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    InputStream stream = con.getInputStream(); // 입력 스트림 생성
                    reader = new BufferedReader(new InputStreamReader(stream)); // 버퍼 선언
                    StringBuffer buffer = new StringBuffer(); //실제 데이터를 받는곳
                    String line = ""; //line별 스트링을 받기 위한 temp 변수

                    // 서버에서 보낸 데이터 읽기
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

//                        parseObj = parser.parse(buffer);


                    //다 읽은 후 String 형변환
                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 종료할 때 연결 해제
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        // 버퍼 닫기
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } // finally
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // 텍스트뷰
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }

    }
}
