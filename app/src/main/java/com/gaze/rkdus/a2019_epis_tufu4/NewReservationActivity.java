package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 *  NewReservationActivity
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class NewReservationActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = "NewReservationActivity";

    ListView internalList, externalList, dogtagList;
    MyAdapter internalAdapter, externalAdapter, dogtagAdapter;

    ImageButton internalBtn, externalBtn, dogtagBtn, back;
    TextView internalCount, externalCount, dogtagCount;

    FrameLayout internalBack, externalBack, dogtagBack;

    boolean internalopen, externalopen, dogtagopen = false;

    String id;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onRefresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<NewReservationItem> items = new ArrayList<NewReservationItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(NewReservationItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            NewReservationView view = new NewReservationView(getApplicationContext());

            NewReservationItem item = items.get(i);
            view.setName(item.getName());
            view.setTime(item.getTime());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (height > 2000){
            setContentView(R.layout.activity_new_reservation);
        } else {
            setContentView(R.layout.activity_new_reservation_small);
        }

        Intent getintet = getIntent();
        id = getintet.getStringExtra("id");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        internalBtn = (ImageButton) findViewById(R.id.internal);
        externalBtn = (ImageButton) findViewById(R.id.external);
        dogtagBtn = (ImageButton) findViewById(R.id.dogtag);

        internalList = (ListView) findViewById(R.id.internalList);
        externalList = (ListView) findViewById(R.id.externalList);
        dogtagList = (ListView) findViewById(R.id.dogtagList);

        internalCount = (TextView) findViewById(R.id.internal_count);
        externalCount = (TextView) findViewById(R.id.external_count);
        dogtagCount = (TextView) findViewById(R.id.dogtag_count);

        internalBack = (FrameLayout) findViewById(R.id.internalBack);
        externalBack = (FrameLayout) findViewById(R.id.externalBack);
        dogtagBack = (FrameLayout) findViewById(R.id.dogtagBack);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        internalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internalopen) {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    internalopen = false;
                } else {
                    internalList.setVisibility(View.VISIBLE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.VISIBLE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    internalopen = true;
                    externalopen = false;
                    dogtagopen = false;
                }
            }
        });

        externalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalopen) {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    externalopen = false;
                } else {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.VISIBLE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.VISIBLE);
                    dogtagBack.setVisibility(View.GONE);

                    internalopen = false;
                    externalopen = true;
                    dogtagopen = false;
                }
            }
        });

        dogtagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dogtagopen) {
                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.GONE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.GONE);

                    dogtagopen = false;
                } else {

                    internalList.setVisibility(View.GONE);
                    externalList.setVisibility(View.GONE);
                    dogtagList.setVisibility(View.VISIBLE);

                    internalBack.setVisibility(View.GONE);
                    externalBack.setVisibility(View.GONE);
                    dogtagBack.setVisibility(View.VISIBLE);

                    internalopen = false;
                    externalopen = false;
                    dogtagopen = true;
                }
            }
        });

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String nowTime = sdf.format(date);

//        JSONObject tmp = new JSONObject();
//        JSONObject jsonObject = new JSONObject();
//
//        //여기
//        try {
//
//            JSONArray internalArray = new JSONArray("[{'name':'김가연','time':'2019-06-24'},{'name':'정지원','time':'2019-05-22'},{'name':'박지민','time':'2017-08-03'}]");
//
//            tmp.accumulate("internal", internalArray);
//
//            JSONArray externalArray = new JSONArray("[{'name':'김가연','time':'2019-06-24'},{'name':'정지원','time':'2019-05-22'},{'name':'박지민','time':'2017-08-03'}]");
//
//            tmp.accumulate("external", externalArray);
//
//            JSONArray dogtagArray = new JSONArray("[{'name':'김가연','time':'2019-06-24'},{'name':'정지원','time':'2019-05-22'},{'name':'박지민','time':'2017-08-03'}]");
//
//            tmp.accumulate("dogtag", dogtagArray);
//
//
//            jsonObject.accumulate("result", tmp);
//
//            Log.e(TAG, jsonObject.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


//
//        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        internalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        internalList.setAdapter(internalAdapter);
//


//        ArrayList<String> extitles = new ArrayList<>();
//
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        externalList.setAdapter(externalAdapter);
//



//        ArrayList<String> titles = new ArrayList<>();
//
//        dogtagAdapter.addItem(new NewReservationItem("김가연", nowTime));
//        dogtagAdapter.addItem(new NewReservationItem("김가연", nowTime));
//
//        dogtagList.setAdapter(dogtagAdapter);
//


        new NewReservationListData().execute(getResources().getString(R.string.url) + "/getNewReservationListData");

    }

    /* NewReservationListData : ID값을 통해 신규 예약한 명단 리스트를 출력
     *
     *
     * Uri  --->   /getNewReservationListData
     * Parm  --->   {"user":{"id":"test","state":1}} 전송
     * Result  --->   {"result":{"internal":[{"name":"김가연","time":"2019-06-24"},{"name":"정지원","time":"2019-05-22"}],"external":[{"name":"김가연","time":"2019-06-24"},{"name":"정지원","time":"2019-05-22"}]
     *                                          ,"dogtag":[{"name":"김가연","time":"2019-06-24"},{"name":"정지원","time":"2019-05-22"}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : internal, external, dogtag  세개 넣어서!!  */

    public class NewReservationListData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("state", 1);

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

            internalAdapter = new MyAdapter();
            externalAdapter = new MyAdapter();
            dogtagAdapter = new MyAdapter();

            JSONArray internal = null, external = null, dogtag = null;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new NewReservationListData().execute(getResources().getString(R.string.url) + "/getNewReservationListData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    internal = jsonObject.getJSONArray("internal");
                    external = jsonObject.getJSONArray("external");
                    dogtag = jsonObject.getJSONArray("dogtag");

                    Log.e(TAG, internal.length() + "");
                    for (int i = 0; i < internal.length(); i++) {
                        JSONObject jsonTemp = internal.getJSONObject(i);
                        internalAdapter.addItem(new NewReservationItem(jsonTemp.getString("OWNER_NAME"), jsonTemp.getString("ASK_DATE")));
                    }
                    internalList.setAdapter(internalAdapter);
                    internalCount.setText(internal.length() + "");

                    internalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final NewReservationItem item = (NewReservationItem) internalAdapter.getItem(i);
                            final String name = item.getName();

                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("type", 1);
                            startActivityForResult(intent, 1111);
                        }
                    });

                    for (int i = 0; i < external.length(); i++) {
                        JSONObject jsonTemp = external.getJSONObject(i);
                        externalAdapter.addItem(new NewReservationItem(jsonTemp.getString("OWNER_NAME"), jsonTemp.getString("ASK_DATE")));
                    }
                    externalList.setAdapter(externalAdapter);
                    externalCount.setText(external.length() + "");

                    externalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final NewReservationItem item = (NewReservationItem) externalAdapter.getItem(i);
                            final String name = item.getName();

                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("type", 2);
                            startActivityForResult(intent, 2222);
                        }
                    });

                    for (int i = 0; i < dogtag.length(); i++) {
                        JSONObject jsonTemp = dogtag.getJSONObject(i);
                        Log.e(TAG, "name : "+jsonTemp.getString("OWNER_NAME"));
                        dogtagAdapter.addItem(new NewReservationItem(jsonTemp.getString("OWNER_NAME"), jsonTemp.getString("ASK_DATE")));
                    }
                    dogtagList.setAdapter(dogtagAdapter);
                    dogtagCount.setText(dogtag.length() + "");

                    dogtagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final NewReservationItem item = (NewReservationItem) dogtagAdapter.getItem(i);
                            final String name = item.getName();

                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("type", 3);
                            startActivityForResult(intent, 3333);
                        }
                    });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
        finish();
        Log.d("onPostCreate", "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("please", "다시");
    }
}
