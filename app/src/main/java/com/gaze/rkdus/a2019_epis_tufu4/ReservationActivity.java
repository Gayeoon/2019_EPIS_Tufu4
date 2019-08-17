package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

/*
 *  ReservationActivity
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class ReservationActivity extends BaseActivity {

    ImageButton check;
    TextView owner, resident, phone, resAddr, nowAddr, animal, variety, furColor, gender, neutralization, birthday, acqDate, special;

    TableRow sametime;
    String id, name;
    String TAG = "ResrvationActivity";

    ImageView self_buy;

    int type = 0;
    // 1: 내장형 / 2 : 외장형 / 3 : 등록인식표

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (height > 2000) {
            setContentView(R.layout.activity_reservation);

        } else {
            setContentView(R.layout.activity_reservation_small);

        }

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

        sametime = (TableRow) findViewById(R.id.sametime);

        self_buy = (ImageView)findViewById(R.id.self_buy);

        new ReservationData().execute(getResources().getString(R.string.url) + "/getReservationData");

        check = (ImageButton) findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReservationCheck().execute(getResources().getString(R.string.url) + "/changeState2");
                finish();
            }
        });

    }

/*    ReservationData : ID, 주인 이름, type 값을 통해 예약 데이터 요청

    필요 데이터 : 병원 이름, 신규 예약 건수, 등록 대기 건수, 등록 완료 건수

    type -> 1 : 내장형 / 2 : 외장형 / 3 : 등록인식표

    Url  --->   /getReservationData
    Parm  --->   {"user":{"id":"test","owner_name":"김가연","type":1}} 전송
    Result  --->  {"result":{"OWNER_NAME":"김가연“,"OWNER_RESIDENT":
        "960708-0000000","OWNER_PHONE_NUMBER":"010-4491-0778“,"OWNER_ADDRESS1":"대전","OWNER_ADDRESS2":"궁동","PET_NAME":"뿡이“
                ,"PET_VARIETY":"시츄","PET_COLOR":"흰색+갈색","PET_GENDER":"남“,"PET_NEUTRALIZATION":"했움", "SAME_TIME":"1", "PET_BIRTH":"2008-04-04",
                "REGIST_DATE":"2008-04-04","ETC":"겁이 많아요ㅠㅠ"}} 결과 값 */

    public class ReservationData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", name);
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

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new ReservationData().execute(getResources().getString(R.string.url) + "/getReservationData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    owner.setText(jsonObject.getString("owner_name"));
                    resident.setText(jsonObject.getString("owner_resident"));
                    phone.setText(jsonObject.getString("owner_phone"));
                    resAddr.setText(jsonObject.getString("address1"));
                    nowAddr.setText(jsonObject.getString("address2"));
                    animal.setText(jsonObject.getString("pet_name"));
                    variety.setText(jsonObject.getString("pet_variety"));
                    furColor.setText(jsonObject.getString("pet_color"));

                    if (jsonObject.getInt("pet_gender") == 2) {
                        gender.setText("남성");
                    } else {
                        gender.setText("여성");
                    }

                    if (jsonObject.getInt("pet_neutralization") == 1) {
                            neutralization.setText("했음");
                    } else {
                        neutralization.setText("안했음");
                    }

                    if (jsonObject.getInt("sametime") == 1) {
                        sametime.setVisibility(View.VISIBLE);
                    } else {
                        sametime.setVisibility(View.GONE);
                    }


                    birthday.setText(jsonObject.getString("pet_birth"));
                    acqDate.setText(jsonObject.getString("regist_date"));
                    special.setText(jsonObject.getString("etc"));

                    if (jsonObject.getInt("self_buy") == 1) {
                        self_buy.setBackgroundResource(R.drawable.checked);
                    } else {
                        self_buy.setBackgroundResource(R.drawable.check);
                    }

                    Log.e(TAG, jsonObject.getString("owner_name"));
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
     *  id/name/type이 일치하는 데이터의 state를 1에서 2로 변경!
     *
     * Url  --->   /changeState2

     * Parm  --->   {"user":{"id":"test","name":"김가연","type":1}} 전송
     * Result  --->   {"result":1} 결과 값 */

    public class ReservationCheck extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", name);
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
                    new ReservationCheck().execute(getResources().getString(R.string.url) + "/changeState2");
                } else {
                    success = (int) json.get("result");

                    if (success == 1) {
                        //Toast.makeText(getApplicationContext(), "예약확인!!", Toast.LENGTH_LONG).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }
}

