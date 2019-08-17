package com.gaze.rkdus.a2019_epis_tufu4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

/*
 *  LoginActivity
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class LoginActivity extends BaseActivity {
    public static final String TAG = "LoginActivity";

    EditText eid, epw;
    ImageButton login;
    TextView join, find;
    public String id = "", pw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (height > 2000){
            setContentView(R.layout.activity_login);
            Log.e("Tag", height +"   "+ width);
        } else {
            setContentView(R.layout.activity_login_small);
            Log.e("Tag", height +"   "+ width);
        }


        eid = (EditText) findViewById(R.id.id);
        epw = (EditText) findViewById(R.id.pw);

        login = (ImageButton) findViewById(R.id.login);
        join = (TextView) findViewById(R.id.join);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = eid.getText().toString();
                pw = epw.getText().toString();

                Log.e(TAG, getResources().getString(R.string.url) +"/login");
                new loginDB().execute(getResources().getString(R.string.url) + "/login");
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    /* loginDB : 로그인
     * 로그인 성공 -> int 1
     * 로그인 실패 -> int 0
     *
     * Uri  --->   /login
     * Parm  --->   {"user":{"id":"test", "pw":"0000"}} 전송
     * Result  --->   {"result":1} 결과 값 */

    public class loginDB extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("pw", pw);

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

                    Log.e("LoginActivity", jsonObject.toString());
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

                    Log.e("LoginActivity!!!! : ", buffer.toString());
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
            int succes = 0;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new loginDB().execute(getResources().getString(R.string.url) + "/login");
                } else {
                    succes = (int) json.get("result");

                    if (succes == 1) {
                       // Toast.makeText(getApplicationContext(), "로그인 성공!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HospitalActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 실패!!", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("LoginActivity", result);

        }
    }
}

