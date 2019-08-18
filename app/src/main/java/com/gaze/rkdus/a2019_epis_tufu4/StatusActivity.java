package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;

/*
 *  StatusActivity
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class StatusActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "StatusActivity";

    ListView listView;
    MyAdapter myAdapter;
    MySmallAdapter mySmallAdapter;

    ImageButton search;
    TextView search_input, regist, vaccine, health;

    String id, owner, animal, search_txt;

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
        ArrayList<StatusItem> items = new ArrayList<StatusItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(StatusItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            StatusView view = new StatusView(getApplicationContext());

            StatusItem item = items.get(i);
            view.setowner(item.getowner());
            view.setanimal(item.getanimal());
            view.setState(item.getState());


            return view;
        }
    }

    class MySmallAdapter extends BaseAdapter {
        ArrayList<StatusItem> items = new ArrayList<StatusItem>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        public void addItem(StatusItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup viewGroup) {
            statusView_small view = new statusView_small(getApplicationContext());

            StatusItem item = items.get(i);
            view.setowner(item.getowner());
            view.setanimal(item.getanimal());
            view.setState(item.getState());


            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        int height = dm.heightPixels;

        if (height < 2000) {
            small = true;
        }

        Intent getintet = getIntent();

        id = getintet.getStringExtra("id");

        TextView title = (TextView) findViewById(R.id.title);
        regist = (TextView) findViewById(R.id.regist);
        vaccine = (TextView) findViewById(R.id.vaccine);
        health = (TextView) findViewById(R.id.health);

        search = (ImageButton) findViewById(R.id.search);
        search_input = (TextView) findViewById(R.id.search_input);
        final LinearLayout searchBar = (LinearLayout) findViewById(R.id.searchbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_txt = search_input.getText().toString();

               new SearchListData().execute("http://vowow.cafe24app.com" + "/user/getReservation");


            }
        });

        listView = (ListView) findViewById(R.id.stateList);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist.setTextColor(Color.parseColor("#2eb8ff"));
                vaccine.setTextColor(Color.parseColor("#000000"));
                health.setTextColor(Color.parseColor("#000000"));
                searchBar.setVisibility(View.VISIBLE);

                new StateListData().execute(getResources().getString(R.string.url) + "/getStateListData");

            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist.setTextColor(Color.parseColor("#000000"));
                vaccine.setTextColor(Color.parseColor("#000000"));
                health.setTextColor(Color.parseColor("#2eb8ff"));
                searchBar.setVisibility(View.GONE);

                new HealthScreeningListData().execute(getResources().getString(R.string.url) + "/healthScreen/getHealthScreeningListData");

            }
        });

        vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist.setTextColor(Color.parseColor("#000000"));
                vaccine.setTextColor(Color.parseColor("#2eb8ff"));
                health.setTextColor(Color.parseColor("#000000"));
                searchBar.setVisibility(View.GONE);

                new VaccineListData().execute(getResources().getString(R.string.url) + "/vaccine/getVaccinationListData");

            }
        });

//        myAdapter = new MyAdapter();
//
//        myAdapter.addItem(new StatusItem("김가연", "뿡이", 1));
//        myAdapter.addItem(new StatusItem("정지원", "맥북", 2));
//        myAdapter.addItem(new StatusItem("이해원", "허뻥", 3));
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

        new StateListData().execute(getResources().getString(R.string.url) + "/getStateListData");

    }

    /* HealthScreeningListData : ID값을 통해 건강검진 데이터 리스트를 출력
     *
     * 건강검진 데이터 출력
     *
     * Uri  --->   /healthScreen/getHealthScreeningListData
     * Parm  --->   {"user":{"id":"test"}} 전송
     * Result  --->   {"result":{"health":[{"OWNER_NAME":"김가연","PET_NAME":"뿡이"},{"OWNER_NAME":"정지원","PET_NAME":"맥북"}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : health 넣어서!!  */

    public class HealthScreeningListData extends AsyncTask<String, String, String> {

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
            JSONArray state = null;

            if (small) {
                mySmallAdapter = new MySmallAdapter();
            } else {
                myAdapter = new MyAdapter();
            }

            JSONArray internal = null, external = null, dogtag = null;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new HealthScreeningListData().execute(getResources().getString(R.string.url) + "/healthScreen/getHealthScreeningListData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    state = jsonObject.getJSONArray("health");

                    Log.e(TAG, state.length() + "");

                    for (int i = 0; i < state.length(); i++) {
                        JSONObject jsonTemp = state.getJSONObject(i);

                        if (small) {
                            mySmallAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), 0));

                        } else {
                            myAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), 0));
                        }
                    }

                    if (small) {
                        listView.setAdapter(mySmallAdapter);
                    } else {
                        listView.setAdapter(myAdapter);
                    }

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView oTextOwner = (TextView) view.findViewById(R.id.owner);
                            TextView oTextAnimal = (TextView) view.findViewById(R.id.animal);

                            owner = oTextOwner.getText().toString();
                            animal = oTextAnimal.getText().toString();

                            Intent intent = new Intent(getApplicationContext(), HealthScreeningActivity.class);
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

    /* VaccineListData : ID값을 통해 건강검진 데이터 리스트를 출력
     *
     * 건강검진 데이터 출력
     *
     * Uri  --->   /vaccine/getVaccinationListData
     * Parm  --->   {"user":{"id":"test"}} 전송
     * Result  --->   {"result":{"health":[{"OWNER_NAME":"김가연","PET_NAME":"뿡이"},{"OWNER_NAME":"정지원","PET_NAME":"맥북"}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : health 넣어서!!  */

    public class VaccineListData extends AsyncTask<String, String, String> {

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
            JSONArray state = null;

            if (small) {
                mySmallAdapter = new MySmallAdapter();
            } else {
                myAdapter = new MyAdapter();
            }

            JSONArray internal = null, external = null, dogtag = null;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new VaccineListData().execute(getResources().getString(R.string.url) + "/vaccine/getVaccinationListData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    state = jsonObject.getJSONArray("vaccination");

                    Log.e(TAG, state.length() + "");

                    for (int i = 0; i < state.length(); i++) {
                        JSONObject jsonTemp = state.getJSONObject(i);

                        if (small) {
                            mySmallAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), 0));

                        } else {
                            myAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), 0));
                        }
                    }

                    if (small) {
                        listView.setAdapter(mySmallAdapter);
                    } else {
                        listView.setAdapter(myAdapter);
                    }

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView oTextOwner = (TextView) view.findViewById(R.id.owner);
                            TextView oTextAnimal = (TextView) view.findViewById(R.id.animal);

                            owner = oTextOwner.getText().toString();
                            animal = oTextAnimal.getText().toString();

                            Intent intent = new Intent(getApplicationContext(), VaccinationActivity.class);
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

    /* StateListData : ID값을 통해 전체 리스트를 출력
     *
     * 모든 상태의 리스트 요청
     *
     * Uri  --->   /getStateListData
     * Parm  --->   {"user":{"id":"test"}} 전송
     * Result  --->   {"result":{"state":[{"OWNER_NAME":"김가연","PET_NAME":"뿡이","REGIST_STATE":1},{"OWNER_NAME":"정지원","PET_NAME":"맥북","REGIST_STATE":2}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : state 넣어서!!  */

    public class StateListData extends AsyncTask<String, String, String> {

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
            JSONArray state = null;

            if (small) {
                mySmallAdapter = new MySmallAdapter();
            } else {
                myAdapter = new MyAdapter();
            }

            JSONArray internal = null, external = null, dogtag = null;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new StateListData().execute(getResources().getString(R.string.url) + "/getStateListData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    state = jsonObject.getJSONArray("state");

                    Log.e(TAG, state.length() + "");

                    for (int i = 0; i < state.length(); i++) {
                        JSONObject jsonTemp = state.getJSONObject(i);

                        if (small) {
                            mySmallAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), jsonTemp.getInt("regist_state")));

                        } else {
                            myAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), jsonTemp.getInt("regist_state")));
                        }
                    }

                    if (small) {
                        listView.setAdapter(mySmallAdapter);
                    } else {
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

    /* SearchListData : ID값과 key 값을 통해 값이 포함된 리스트 출력
     *
     *
     * Uri  --->   /getSearchListData
     * Parm  --->   {"user":{"id":"test","key":"뿡이"}} 전송
     * Result  --->   {"result":{"state":[{"owner_name":"김가연","pet_name":"뿡이","REGIST_STATE":1},{"owner_name":"정지원","PET_NAME":"뿡이","REGIST_STATE":2}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : state 넣어서!!  */

    public class SearchListData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("key", search_txt);

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
            JSONArray state = null;

            if (small) {
                mySmallAdapter = new MySmallAdapter();
            } else {
                myAdapter = new MyAdapter();
            }

            JSONArray internal = null, external = null, dogtag = null;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                   new SearchListData().execute("http://vowow.cafe24app.com" + "/user/getReservation");
              } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    state = jsonObject.getJSONArray("state");

                    Log.e(TAG, state.length() + "");

                    for (int i = 0; i < state.length(); i++) {
                        JSONObject jsonTemp = state.getJSONObject(i);
                        if (small) {
                            mySmallAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), jsonTemp.getInt("regist_state")));

                        } else {
                            myAdapter.addItem(new StatusItem(jsonTemp.getString("owner_name"), jsonTemp.getString("pet_name"), jsonTemp.getInt("regist_state")));
                        }
                    }

                    if (small) {
                        listView.setAdapter(mySmallAdapter);
                    } else {
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

                    if (small) {
                        mySmallAdapter.notifyDataSetChanged();
                        mySmallAdapter.notifyDataSetInvalidated();
                    } else {
                        myAdapter.notifyDataSetChanged();
                        myAdapter.notifyDataSetInvalidated();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e(TAG, result);

        }
    }

}

