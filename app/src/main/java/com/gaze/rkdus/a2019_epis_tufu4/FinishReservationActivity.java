package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import java.util.ArrayList;

/*
 *  FinishReservationActivity
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class FinishReservationActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = "FinishReservation";

    ListView listView;
    MyAdapter myAdapter;
    MySmallAdapter mySmallAdapter;

    ImageButton back;

    String id, owner, animal;

    boolean small = false;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onRefresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<FinishReservationItem> items = new ArrayList<FinishReservationItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(FinishReservationItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            FinishReservationView view = new FinishReservationView(getApplicationContext());

            FinishReservationItem item = items.get(i);
            view.setowner(item.getowner());
            view.setanimal(item.getanimal());
            return view;
        }
    }

    class MySmallAdapter extends BaseAdapter {
        ArrayList<FinishReservationItem> items = new ArrayList<FinishReservationItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(FinishReservationItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            FinishReservationView_small view = new FinishReservationView_small(getApplicationContext());

            FinishReservationItem item = items.get(i);
            view.setowner(item.getowner());
            view.setanimal(item.getanimal());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_reservation);

        Intent getintet = getIntent();
        id = getintet.getStringExtra("id");

        TextView title = (TextView)findViewById(R.id.title) ;

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (height < 2000){
            title.setTextSize(16);
            small = true;
        }

        listView = (ListView) findViewById(R.id.finishList);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

//
//        myAdapter = new MyAdapter();
//
//        myAdapter.addItem(new FinishReservationItem("김가연", "뿡이"));
//        myAdapter.addItem(new FinishReservationItem("정지원", "맥북"));
//        myAdapter.addItem(new FinishReservationItem("이해원", "허뻥"));
//
//        listView.setAdapter(myAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView oTextOwner = (TextView) view.findViewById(R.id.owner);
//                TextView oTextAnimal = (TextView) view.findViewById(R.id.animal);
//
//                owner = oTextOwner.getText().toString();
//                animal = oTextAnimal.getText().toString();
//
//                Intent intent = new Intent(getApplicationContext(), Reservation_v2_Activity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("owner", owner);
//                intent.putExtra("animal", animal);
//                startActivity(intent);
//            }
//        });

        new FinishReservationListData().execute(getResources().getString(R.string.url) + "/getFinishReservationListData");
    }

    /* FinishReservationListData : ID값을 통해 등록 대기 명단 리스트를 출력
     *
     * state 3 상태의 리스트 요청
     *
     * Uri  --->   /getFinishReservationListData
     * Parm  --->   {"user":{"id":"test"}} 전송
     * Result  --->   {"result":{"wait":[{"owner":"김가연","animal":"뿡이"},{"owner":"정지원","animal":"맥북"}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : wait 넣어서!!  */

    public class FinishReservationListData extends AsyncTask<String, String, String> {

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
            JSONArray wait = null;

            if (small){
                mySmallAdapter = new MySmallAdapter();
            }else {
                myAdapter = new MyAdapter();
            }

            JSONArray internal = null, external = null, dogtag = null;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new FinishReservationListData().execute(getResources().getString(R.string.url) + "/getFinishReservationListData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    wait = jsonObject.getJSONArray("wait");

                    Log.e(TAG, wait.length() + "");

                    for (int i = 0; i < wait.length(); i++) {
                        JSONObject jsonTemp = wait.getJSONObject(i);
                        if (small){
                            mySmallAdapter.addItem(new FinishReservationItem(jsonTemp.getString("OWNER_NAME"), jsonTemp.getString("PET_NAME")));
                        }else {
                            myAdapter.addItem(new FinishReservationItem(jsonTemp.getString("OWNER_NAME"), jsonTemp.getString("PET_NAME")));
                        }
                    }

                    if (small){
                        listView.setAdapter(mySmallAdapter);
                    }else {
                        listView.setAdapter(myAdapter);
                    }

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView oTextOwner = (TextView) view.findViewById(R.id.owner);
                            TextView oTextAnimal = (TextView) view.findViewById(R.id.animal);

                            owner = oTextOwner.getText().toString();
                            animal = oTextAnimal.getText().toString();

                            Intent intent = new Intent(getApplicationContext(), Reservation_v2_Activity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("owner", owner);
                            intent.putExtra("animal", animal);
                            startActivity(intent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e(TAG, result);

        }
    }
}
