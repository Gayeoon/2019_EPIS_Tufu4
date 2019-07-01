package com.example.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;

public class WaitReservationActivity extends BaseActivity implements View.OnClickListener {

    String TAG = "ResrvationActivity";
    public String url = "http://192.168.0.56:3000";

    String owner, animal, id;

    View.OnClickListener context = this;
    private ListView m_oListView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_reservation);

        Intent getintet = getIntent();
        id = getintet.getStringExtra("id");

        new WaitReservationListData().execute(url + "/getWaitReservationListData");

//        ArrayList<waitItemData> oData = new ArrayList<>();
//
//        waitItemData oItem = new waitItemData();
//        oItem.strOwner = "김가연";
//        oItem.strAnimal = "뿡이";
//        oItem.onClickListener = this;
//        oData.add(oItem);
//
//        waitItemData oItem2 = new waitItemData();
//        oItem2.strOwner = "정지원";
//        oItem2.strAnimal = "맥북";
//        oItem2.onClickListener = this;
//        oData.add(oItem2);
//
//        waitItemData oItem3 = new waitItemData();
//        oItem3.strOwner = "이해원";
//        oItem3.strAnimal = "허뻥";
//        oItem3.onClickListener = this;
//        oData.add(oItem3);
//
//        m_oListView = (ListView) findViewById(R.id.waitList);
//        waitListAdapter oAdapter = new waitListAdapter(oData);
//        m_oListView.setAdapter(oAdapter);
//
//        m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call:
                View oParentView = (View) v.getParent();
                TextView oTextOwner = (TextView) oParentView.findViewById(R.id.owner);
                TextView oTextAnimal = (TextView) oParentView.findViewById(R.id.animal);
                ImageView btn = (ImageView) oParentView.findViewById(R.id.call);

                owner = oTextOwner.getText().toString();
                animal = oTextAnimal.getText().toString();

                Log.e(TAG, "owner : " + owner + " animal : " + animal);

                new CallReservation().execute(url + "/putChangeWait ");
                break;

            case R.id.cancel:
                View cParentView = (View) v.getParent();
                TextView cTextOwner = (TextView) cParentView.findViewById(R.id.owner);
                TextView cTextAnimal = (TextView) cParentView.findViewById(R.id.animal);

                owner = cTextOwner.getText().toString();
                animal = cTextAnimal.getText().toString();

                new CancelReservation().execute(url+"/putCancelReservation");
                break;
        }

//        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
//                android.R.style.Theme_DeviceDefault_Light_Dialog);
//
//        String strMsg = "선택한 아이템의 position 은 " + position + " 입니다.\n주인 이름 :" + oTextOwner.getText();
//
//        oDialog.setMessage(strMsg)
//                .setPositiveButton("확인", null)
//                .setCancelable(false)
//                .show();

    }

    /* WaitReservationListData : ID값을 통해 등록 대기 명단 리스트를 출력
     *
     * state 2와 state 3 상태의 리스트 요청
     *
     * Uri  --->   /getWaitReservationListData
     * Parm  --->   {"user":{"id":"test"}} 전송
     * Result  --->   {"result":{"wait":[{"owner":"김가연","animal":"뿡이","state":2},{"owner":"정지원","animal":"맥북"}]}} 결과 값
     *
     * ps. 결과값 : result Object 안에 JSONArray : wait 넣어서!!  */

    public class WaitReservationListData extends AsyncTask<String, String, String> {

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

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new WaitReservationListData().execute(url + "/getWaitReservationListData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    wait = jsonObject.getJSONArray("wait");

                    Log.e(TAG, wait.length() + "");

                    ArrayList<waitItemData> oData = new ArrayList<>();

                    for (int i = 0; i < wait.length(); i++) {
                        JSONObject jsonTemp = wait.getJSONObject(i);
                        Log.e(TAG, jsonTemp.toString());

                        waitItemData oItem = new waitItemData();

                        oItem.strOwner = jsonTemp.getString("OWNER_NAME");
                        oItem.strAnimal = jsonTemp.getString("PET_NAME");

                        if (jsonTemp.getInt("REGIST_STATE") == 2) {
                            oItem.bolCal = true;
                            oItem.state = 2;
                        } else {
                            oItem.bolCal = false;
                            oItem.state = 3;
                        }

                        oItem.onClickListener = context;
                        oData.add(oItem);

                    }

                    m_oListView = (ListView) findViewById(R.id.waitList);
                    waitListAdapter oAdapter = new waitListAdapter(oData);
                    m_oListView.setAdapter(oAdapter);

                    m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

                    oAdapter.notifyDataSetChanged();
                    oAdapter.notifyDataSetInvalidated();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e(TAG, result);

        }
    }
    /* CallReservation : 병원 ID, 주인 이름, 강아지 이름 값을 통해 주인 전화번호 요청
     *
     * 예약상태 3으로 변경 (현재 : 2)
     *
     * Uri  --->   /putChangeWait
     * Parm  --->   {"user":{"id":"test","owner":"김가연","animal":"뿡이"}} 전송
     * Result  --->   {"result":"010-4491-0778"} 결과 값 */

    public class CallReservation extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", owner);
                tmp.accumulate("pet_name", animal);

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
                    new CallReservation().execute(url + "/putChangeWait ");

                } else {
                    JSONObject temp = json.getJSONObject("result");
                    String tel = "tel:" + temp.get("OWNER_PHONE_NUMBER").toString();
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

                    new WaitReservationListData().execute(url + "/getWaitReservationListData");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }

    /* CancelReservation : 병원 ID, 주인 이름, 강아지 이름 값을 통해 예약 상태 변경
     *
     * 예약상태 4로 변경 (현재 : 3)
     *
     * 성공 1 실패 0
     *
     * Uri  --->   /putCancelReservation
     * Parm  --->   {"user":{"id":"test","owner":"김가연","animal":"뿡이"}} 전송
     * Result  --->   {"result":1} 결과 값 */

    public class CancelReservation extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", owner);
                tmp.accumulate("pet_name", animal);

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
            int succes = 0;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new CancelReservation().execute(url+"/putCancelReservation");
                } else {
                    succes = (int) json.get("result");

                    if (succes == 1) {
                        new WaitReservationListData().execute(url + "/getWaitReservationListData");
                    } else {
                        Toast.makeText(getApplicationContext(), "삭제 실패!!", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("CancelReservation", result);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
        finish();
        Log.d("onPostCreate", "onDestroy");
    }
}
