package com.example.rkdus.a2019_epis_tufu4;

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
import android.widget.ListView;
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
import java.util.ArrayList;

public class WaitReservationActivity extends BaseActivity {

    String TAG = "ResrvationActivity";
    public String url = "http://192.168.0.39:3000";

    ListView listView;
    MyAdapter myAdapter;
    String owner, animal, id;

    class MyAdapter extends BaseAdapter {
        ArrayList<WaitReservationItem> items = new ArrayList<WaitReservationItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(WaitReservationItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            WaitReservationView view = new WaitReservationView(getApplicationContext());

            WaitReservationItem item = items.get(i);
            view.setowner(item.getowner());
            view.setanimal(item.getanimal());
            view.setCall(item.getCall());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_reservation);

        //        Intent getintet = getIntent();
//        id = getintet.getStringExtra("id");

        listView = (ListView) findViewById(R.id.waitList);

        myAdapter = new MyAdapter();

        ArrayList<String> titles = new ArrayList<>();

        myAdapter.addItem(new WaitReservationItem("김가연", "뿡이", false));
        myAdapter.addItem(new WaitReservationItem("이해원", "허뻥", false));
        myAdapter.addItem(new WaitReservationItem("정지원", "맥북", true));
        myAdapter.addItem(new WaitReservationItem("김가연", "뿡이", false));
        myAdapter.addItem(new WaitReservationItem("이해원", "허뻥", true));
        myAdapter.addItem(new WaitReservationItem("정지원", "맥북", true));


        listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WaitReservationItem item = (WaitReservationItem) myAdapter.getItem(i);

                item.setCall(true);

                myAdapter.notifyDataSetChanged();
                listView.invalidate();

                String tel = "tel:01044910778";
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });

    }

    /* ReservationCheck : 병원 ID, 주인 이름, 강아지 이름 값을 통해 주인 전화번호 요청 $ 예약상태 3으로 변경 (현재 : 2)
     *
     *     *
     * Uri  --->   //putChangeWait
     * Parm  --->   {"user":{"id":"test","owner":"김가연","animal":"뿡이"}} 전송
     * Result  --->   {"result":"010-4491-0778"} 결과 값 */

    public class ReservationCheck extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner", owner);
                tmp.accumulate("animal", animal);

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
                    new ReservationCheck().execute(url + "/putChangeWait ");

                } else {
                    String tel = "tel:"+json.get("result").toString();
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }
}
