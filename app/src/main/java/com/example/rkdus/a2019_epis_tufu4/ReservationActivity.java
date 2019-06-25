package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReservationActivity extends AppCompatActivity {

    ImageButton check;
    TextView owner, resident, phone, resAddr, nowAddr, animal, variety, furColor, gender, neutralization, birthday, acqDate, special;

    String id, name;
    String TAG = "ResrvationActivity";
    public String url = "http://192.168.0.39:3000";

    int type = 0;
    // 1: 내장형 / 2 : 외장형 / 3 : 등록인식표

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");

        owner = (TextView) findViewById(R.id.owner);
        resident = (TextView) findViewById(R.id.resident);
        phone = (TextView) findViewById(R.id.phone);
        resAddr = (TextView) findViewById(R.id.resAddr);
        nowAddr = (TextView) findViewById(R.id.nowAddr);
        animal = (TextView) findViewById(R.id.animal);
        variety = (TextView) findViewById(R.id.variety);
        furColor = (TextView) findViewById(R.id.furColor);
        gender = (TextView) findViewById(R.id.gender);
        neutralization = (TextView) findViewById(R.id.neutralization);
        birthday = (TextView) findViewById(R.id.birthday);
        acqDate = (TextView) findViewById(R.id.acqDate);
        special = (TextView) findViewById(R.id.special);

        //new ReservationData().execute(url + "/getReservationData");

        check = (ImageButton)findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ReservationData().execute(url + "/putChangeState");
                finish();
            }
        });
    }

    /* ReservationData : ID 와 주인 이름값을 통해 예약 데이터 요청
     *
     * 필요 데이터 : 병원 이름, 신규 예약 건수, 등록 대기 건수, 등록 완료 건수
     *
     * type -> 1 : 내장형 / 2 : 외장형 / 3 : 등록인식표
     *
     * Uri  --->   /getReservationData
     * Parm  --->   {"user":{"id":"test","name":"김가연","type":1}} 전송
     * Result  --->   {"result":{"owner":"김가연","resident":"960708-2","phone":"010-4491-0778","resAddr":"대전","nowAddr":"궁동",
     * "animal":"뿡이","variety":"시츄","furColor":"흰색+갈색","gender":"남","neutralization":"했움","birthday":"2008-04-04","acqDate":"2008-04-04","special":"겁이 많아요ㅠㅠ"}} 결과 값 */

    public class ReservationData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("name", name);
                tmp.accumulate("type", type);

                jsonObject.accumulate("user", tmp);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {

                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                    Log.e(TAG, jsonObject.toString());
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject json = null;
            String temp;
            byte[] tempByte[];

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new ReservationData().execute(url + "/getReservationData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    owner.setText(jsonObject.getString("owner"));
                    resident.setText(jsonObject.getString("resident"));
                    phone.setText(jsonObject.getString("phone"));
                    resAddr.setText(jsonObject.getString("resAddr"));
                    nowAddr.setText(jsonObject.getString("nowAddr"));
                    animal.setText(jsonObject.getString("animal"));
                    variety.setText(jsonObject.getString("variety"));
                    furColor.setText(jsonObject.getString("furColor"));
                    gender.setText(jsonObject.getString("gender"));
                    neutralization.setText(jsonObject.getString("neutralization"));
                    birthday.setText(jsonObject.getString("birthday"));
                    acqDate.setText(jsonObject.getString("acqDate"));
                    special.setText(jsonObject.getString("special"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }

    /* ReservationCheck : ID, 주인 이름, type 값을 통해 해당 데이터 상태를 등록 대기 상태로 변경
     *
     * type -> 1 : 내장형 / 2 : 외장형 / 3 : 등록인식표
     *
     * 변경 성공하면 -> int 1
     * 변경 실패하면 -> int 0
     *
     * Uri  --->   /putChangeState
     * Parm  --->   {"user":{"id":"test","name":"김가연","type":1}} 전송
     * Result  --->   {"result":1} 결과 값 */

    public class ReservationCheck extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("name", name);
                tmp.accumulate("type", type);

                jsonObject.accumulate("user", tmp);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {

                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                    Log.e(TAG, jsonObject.toString());
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject json = null;
            int success = 0;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new ReservationCheck().execute(url + "/putChangeState");
                } else {
                    success = (int) json.get("result");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "예약확인!!", Toast.LENGTH_LONG).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }
}

