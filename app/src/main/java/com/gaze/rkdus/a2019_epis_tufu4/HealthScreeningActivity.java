package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HealthScreeningActivity extends AppCompatActivity {

    class MyAdapter extends BaseAdapter {
        ArrayList<HealthScreeningItem> items = new ArrayList<HealthScreeningItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(HealthScreeningItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            HealthScreeningView view = new HealthScreeningView(getApplicationContext());

            HealthScreeningItem item = items.get(i);
            view.setDate(item.getDate());
            view.setTime(item.getTime());

            return view;
        }
    }

    ListView listView;
    MyAdapter myAdapter;

    TextView date, time;
    TextView owner, animal, weight, age, gender, text1, text2;
    ImageButton call;

    LinearLayout bar;

    String id, strOwner, strAnimal;

    String TAG = "HealthScreeningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_screening);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        strOwner = intent.getStringExtra("owner");
        strAnimal = intent.getStringExtra("animal");

        date = (TextView) findViewById(R.id.reservation_date);
        time = (TextView) findViewById(R.id.reservation_time);
        owner = (TextView) findViewById(R.id.owner);
        animal = (TextView) findViewById(R.id.animal);
        weight = (TextView) findViewById(R.id.weight);
        age = (TextView) findViewById(R.id.age);
        gender = (TextView) findViewById(R.id.gender);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

        bar = (LinearLayout)findViewById(R.id.bar);

        owner.setText(strOwner);
        animal.setText(strAnimal);

        call = (ImageButton) findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new CallReservation().execute(getResources().getString(R.string.url) + "/healthScreen/getPhone");

            }
        });
        listView = (ListView) findViewById(R.id.healthList);

        new HealthScreeningData().execute(getResources().getString(R.string.url) + "/healthScreen/getHealthScreeningData");

    }

    /* getHealthScreeningData : ID값을 통해 신규 예약한 명단 리스트를 출력
     *
     *
     * Uri  --->   /healthScreen/getHealthScreeningData
     * Parm  --->   {"user":{"id":"test","owner":"김가연","animal":"뿡이"}} 전송
     * Result  --->   {"result":{"internal":[{"name":"김가연","time":"2019-06-24"},{"name":"정지원","time":"2019-05-22"}],"external":[{"name":"김가연","time":"2019-06-24"},{"name":"정지원","time":"2019-05-22"}]
     *                                          ,"dogtag":[{"name":"김가연","time":"2019-06-24"},{"name":"정지원","time":"2019-05-22"}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : internal, external, dogtag  세개 넣어서!!  */

    public class HealthScreeningData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", strOwner);
                tmp.accumulate("pet_name", strAnimal);

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

                    Log.e("HealthScreeningActivity", jsonObject.toString());
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

            myAdapter = new MyAdapter();

            JSONArray reservation = null;

            JSONObject info = null;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new HealthScreeningData().execute(getResources().getString(R.string.url) + "/healthScreen/getHealthScreeningData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    info = jsonObject.getJSONObject("animal_info");
                    reservation = jsonObject.getJSONArray("reservation_data");

                    animal.setText(info.getString("animal_name"));

                    if (info.getInt("gender") == 2) {
                        gender.setText("수컷");
                    } else {
                        gender.setText("암컷");
                    }

                    age.setText(info.getInt("age") + "살");

                    weight.setText(info.getString("weight"));

                    if (reservation.length() != 0) {


                        for (int i = 0; i < reservation.length() - 1; i++) {
                            JSONObject temp = reservation.getJSONObject(i);
                            myAdapter.addItem(new HealthScreeningItem(temp.getString("date"), temp.getString("time")));
                        }

                        listView.setAdapter(myAdapter);

                        JSONObject reservationTemp = reservation.getJSONObject(reservation.length() - 1);

                        String timeTemp = reservationTemp.getString("date") + " " + reservationTemp.getString("time");

                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH시 mm분", Locale.getDefault());

                        String now = dateFormat.format(currentTime);

                        Date date1 = null;
                        Date date2 = null;

                        try {
                            date1 = dateFormat.parse(now);
                            date2 = dateFormat.parse(timeTemp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (date1.after(date2)) {
                            myAdapter.addItem(new HealthScreeningItem(reservationTemp.getString("date"), reservationTemp.getString("time")));
                            text1.setText("예약된 내역이 없습니다.");
                            text2.setVisibility(View.GONE);
                            bar.setVisibility(View.GONE);
                        } else {
                            time.setText(reservationTemp.getString("time"));
                            date.setText(reservationTemp.getString("date"));
                        }

                    }

                    listView.setAdapter(myAdapter);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e("HealthScreeningActivity", result);

        }
    }

        /* CallReservation : 병원 ID, 주인 이름, 강아지 이름값을 통해 주인 전화번호 요청

    Uri  --->   /health/getPhone
    Parm  --->   {"user":{"id":"test","owner_name":"김가연","pet_name":"뿡이"}} 전송
    Result  --->   {"result":{OWNER_PHONE_NUMBER:"010-4491-0778"}} 결과 값 */

    public class CallReservation extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", strOwner);
                tmp.accumulate("pet_name", strAnimal);

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
                    new CallReservation().execute(getResources().getString(R.string.url) + "/healthScreen/getPhone");

                } else {
                    JSONObject temp = json.getJSONObject("result");

                    String tel = "tel:" + temp.getString("owner_phone_number");
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("getPhone", result);

        }
    }
}