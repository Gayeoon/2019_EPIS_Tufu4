package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HospitalActivity extends BaseActivity {
    public static final String TAG = "HospitalActivity";
    public String url = "http://192.168.0.56:3000";

    String id;
    String hos_name;

    int count = 0;

    TextView name, tcount, new_count, wait_count, finish_count;
    ImageButton status, community, alarm, confirm_new, confirm_wait, confirm_finish;

    ImageView imageView;

    Bitmap bm;
    byte[] profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

//        profile = intent.getByteArrayExtra("profile");
//
//        bm = BitmapFactory.decodeByteArray(profile, 0, profile.length);
//
//        Log.e(TAG, "byte[] : "+profile.toString());
//        Log.e(TAG, "Bitmap : "+bm.toString());


        name = (TextView) findViewById(R.id.name);
        tcount = (TextView) findViewById(R.id.count);
        new_count = (TextView) findViewById(R.id.new_count);
        wait_count = (TextView) findViewById(R.id.wait_count);
        finish_count = (TextView) findViewById(R.id.finish_count);

        status = (ImageButton) findViewById(R.id.status);
        community = (ImageButton) findViewById(R.id.community);
        alarm = (ImageButton) findViewById(R.id.alarm);
        confirm_new = (ImageButton) findViewById(R.id.confirm_new);
        confirm_wait = (ImageButton) findViewById(R.id.confirm_wait);
        confirm_finish = (ImageButton) findViewById(R.id.confirm_finish);

        imageView = (ImageView)findViewById(R.id.profile);

        new HospitalData().execute(url + "/getHospitalData");


        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), StatusActivity.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
            }
        });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //profile = Base64.encodeToString(profile, Base64.DEFAULT);

                Intent intent2 = new Intent(getApplicationContext(), CommunityActivity.class);
                //intent2.putExtra("profile", profile);
                startActivity(intent2);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent2 = new Intent(getApplicationContext(), AlarmActivity.class);
//                intent2.putExtra("id", id);
//                startActivity(intent2);
            }
        });

        confirm_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), NewReservationActivity.class);
                intent2.putExtra("id", id);
                startActivityForResult(intent2, 1111);
            }
        });

        confirm_wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), WaitReservationActivity.class);
                intent2.putExtra("id", id);
                startActivityForResult(intent2, 1111);
            }
        });

        confirm_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), FinishReservationActivity.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
            }
        });
    }



    /* HospitalData : ID 값을 통해 해당되는 병원 메인 페이지에 필요한 모든 데이터 요청
     *
     * 필요 데이터 : 병원 이름, 신규 예약 건수, 등록 대기 건수, 등록 완료 건수
     *
     * Uri  --->   /getHospitalData
     * Parm  --->   {"user":{"id":"test"}} 전송
     * Result  --->   {"result":{"name":"병원이름","new":3,"wait1":12,"wait2":12,"finish":10,"profile":"akflsjekjflsjf"}} 결과 값 */

    public class HospitalData extends AsyncTask<String, String, String> {

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
            int temp_new, temp_wait, temp_finish;
            String temp;
            byte[] tempByte[];

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new HospitalData().execute(url + "/getHospitalData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    hos_name = jsonObject.getString("name");
                    temp_new = jsonObject.getInt("new");
                    temp_wait = jsonObject.getInt("wait1") + jsonObject.getInt("wait2");
                    temp_finish = jsonObject.getInt("finish");
                    temp = jsonObject.getString("profile");

                    //  profile = Encoding.UTF8.GetBytes(str);

                    profile = Base64.decode(temp, Base64.DEFAULT);
                    bm = BitmapFactory.decodeByteArray(profile, 0, profile.length);
                    imageView.setImageBitmap(bm);

                    name.setText(hos_name);
                    new_count.setText(temp_new + "건");
                    wait_count.setText(temp_wait + "건");
                    finish_count.setText(temp_finish + "건");


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}

