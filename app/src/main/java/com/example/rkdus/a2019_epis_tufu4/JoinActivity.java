package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

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

public class JoinActivity extends AppCompatActivity {

    EditText ehospital, ename, enumber, eid, epw;
    ImageButton next_one, next_two;
    LinearLayout idpw;

    String hospital = null, name = null, number = null, id="", pw="";
    boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        StrictMode.enableDefaults();

        success = false;

        ehospital = (EditText) findViewById(R.id.hospital);
        ename = (EditText) findViewById(R.id.name);
        enumber = (EditText) findViewById(R.id.number);
        eid = (EditText) findViewById(R.id.id);
        epw = (EditText) findViewById(R.id.pw);

        next_one = (ImageButton) findViewById(R.id.next_one);
        next_two = (ImageButton) findViewById(R.id.next_two);

        idpw = (LinearLayout)findViewById(R.id.idpw);


        next_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital = ehospital.getText().toString();
                name = ename.getText().toString();
                number = enumber.getText().toString();

                success = ThreeCheck(hospital, name, number);

                if (success){
                    idpw.setVisibility(View.VISIBLE);
                    next_two.setVisibility(View.VISIBLE);
                    ehospital.setEnabled(false);
                    ename.setEnabled(false);
                    enumber.setEnabled(false);

                }else {
                    // 존재하지 않는 병원 ImageView 띄우기
                }
            }
        });

        next_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = eid.getText().toString();
                pw = epw.getText().toString();

                new IDCheck().execute("http://192.168.0.39:3000/getIdCheck");
            }
        });

    }

    boolean ThreeCheck(String hospital, String name, String number) {

        boolean inHospital = false, inNumber = false;
        String tHospital = null, tNumber = null;

        try {
            URL url = new URL("http://211.237.50.150:7080/openapi/sample/xml/Grid_20141225000000000161_1/1/5?"
                    + "API_KEY=9e419c9fb4293de4b448d0f553b753baddda8bc74c07e14f64fcb69ae1cbde4e&"
                    + "RPRSNTV_NM=" + name
            );

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("ENTRPS_NM")) {
                            inHospital = true;
                        }
                        if (parser.getName().equals("ENTRPS_TELNO")) {
                            inNumber = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (inHospital) {
                            tHospital = parser.getText();
                            inHospital = false;
                        }
                        if (inNumber) {
                            tNumber = parser.getText();
                            inNumber = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            Toast.makeText(this, "대표자 이름이 틀렸다!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (hospital.equals(tHospital)) {
            if (number.equals(tNumber)) {
                return true;
            } else {
                Toast.makeText(this, "번호가 틀렸다!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "병원명이 틀렸다!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    /* IDCheck : 중복된 ID 값이 있는지 체크
    * ID 중복이 있으면 -> int 1
    * ID 중복이 없으면 -> int 2
    * Default(에러 체크 하려고 만들었음) -> int 0
    *
    * Uri  --->   /getIdCheck
    * Parm  --->   {"user":{"id":"test"}} 전송
    * Result  --->   {"result":1} 결과 값*/

    public class IDCheck extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);

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

                    Log.e("JoinActivity", jsonObject.toString());
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
                    new IDCheck().execute("http://192.168.0.39:3000/getIdCheck");
                } else {
                    success = (int) json.get("result");

                    if (success == 1) {

                        new JoinDB().execute("http://192.168.0.39:3000/getJoin");

                    } else if (success == 2){
                        Toast.makeText(getApplicationContext(), "이미 존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                        eid.setText("");
                    }else{
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("JoinActivity", result);

        }
    }

    /* JoinDB : 회원가입
     * 회원가입 성공하면 -> int 1
     * 회원가입 실패하면 -> int 0
     *
     * Uri  --->   /getJoin
     * Parm  --->   {"user":{"hospital":"병원이름", "name":"김가연", "number":"010-4491-0778", "id":"test", "pw":"1234"}} 전송
     * Result  --->   {"result":1} 결과 값*/

    public class JoinDB extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);

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

                    Log.e("JoinActivity", jsonObject.toString());
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
                    new JoinDB().execute("http://192.168.0.39:3000/getJoin");
                } else {
                    success = (int) json.get("result");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "회원가입 성공!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SelectPicActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("JoinActivity", result);

        }
    }

}