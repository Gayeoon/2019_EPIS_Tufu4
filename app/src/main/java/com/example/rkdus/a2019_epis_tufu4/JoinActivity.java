package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import static java.lang.Thread.sleep;

public class JoinActivity extends BaseActivity {

    public String url = "http://192.168.0.39:3000";

    EditText ehospital, ename, num1, num2, num3, eid, epw;
    ImageButton next_one, next_two;
    LinearLayout idpw;
    ImageView no;

    String hospital = null, name = null, number = null, id = "", pw = "";
    boolean success;

    Animation slowly_appear, slowlyDisappear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        StrictMode.enableDefaults();

        success = false;

        ehospital = (EditText) findViewById(R.id.hospital);
        ename = (EditText) findViewById(R.id.name);
        num1 = (EditText) findViewById(R.id.number1);
        num2 = (EditText) findViewById(R.id.number2);
        num3 = (EditText) findViewById(R.id.number3);
        eid = (EditText) findViewById(R.id.id);
        epw = (EditText) findViewById(R.id.pw);

        next_one = (ImageButton) findViewById(R.id.next_one);
        next_two = (ImageButton) findViewById(R.id.next_two);

        idpw = (LinearLayout) findViewById(R.id.idpw);

        no = (ImageView) findViewById(R.id.no);

        next_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital = ehospital.getText().toString();
                name = ename.getText().toString();
                number = num1.getText().toString() +"-"+num2.getText().toString()+"-"+num3.getText().toString();

                //new ThreeCheck().execute(url+"/getThreeCheck");

                idpw.setVisibility(View.VISIBLE);
                next_two.setVisibility(View.VISIBLE);
                ehospital.setEnabled(false);
                ename.setEnabled(false);
                num1.setEnabled(false);
                num2.setEnabled(false);
                num3.setEnabled(false);

            }
        });

        next_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = eid.getText().toString();
                pw = epw.getText().toString();

                new IDCheck().execute(url+"/getIdCheck");
            }
        });

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
                    new IDCheck().execute(url+"/getIdCheck");
                } else {
                    success = (int) json.get("result");

                    if (success == 2) {

                        new JoinDB().execute(url+"/getJoin");

                    } else if (success == 1) {
                        Toast.makeText(getApplicationContext(), "이미 존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                        eid.setText("");
                    } else {
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
                    new JoinDB().execute(url+"/getJoin");
                } else {
                    success = (int) json.get("result");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "회원가입 성공!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SelectPicActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("JoinActivity", result);

        }
    }

    /* ThreeCheck : 병원 명, 대표자 명, 전화번호가 같은지 확인
     * 파라미터와 DB 내용이 모두 같으면 -> int 1
     * 파라미터와 DB 내용이 다르면 -> int 2
     * Default(에러 체크 하려고 만들었음) -> int 0
     *
     * Uri  --->   /getThreeCheck
     * Parm  --->   {"user":{"hospital":"미리내동물병원","name":"김가연","number":"031-574-7580"}} 전송
     * Result  --->   {"result":1} 결과 값 */

    public class ThreeCheck extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

               // tmp.accumulate("hospital", hospital);
                tmp.accumulate("name", name);
                tmp.accumulate("number", number);

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
                    new ThreeCheck().execute(url+"/getThreeCheck");
                } else {

                    success = (int) json.get("result");

                    if (success == 1) {

                        idpw.setVisibility(View.VISIBLE);
                        next_two.setVisibility(View.VISIBLE);
                        ehospital.setEnabled(false);
                        ename.setEnabled(false);
                        num1.setEnabled(false);
                        num2.setEnabled(false);
                        num3.setEnabled(false);

                    } else if (success == 2) {

                        next_one.setEnabled(false);

                        no.setVisibility(View.VISIBLE);

                        slowly_appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowly_appear);
                        slowlyDisappear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowly_disappear);

                        slowly_appear.setAnimationListener(new Animation.AnimationListener(){
                            public void onAnimationEnd(Animation animation){
                                try {
                                    sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                no.setAnimation(slowlyDisappear);
                            }
                            public void onAnimationStart(Animation animation){;}
                            public void onAnimationRepeat(Animation animation){;}
                        });

                        slowlyDisappear.setAnimationListener(new Animation.AnimationListener(){
                            public void onAnimationEnd(Animation animation){
                                no.setVisibility(View.GONE);

                                next_one.setEnabled(true);
                            }
                            public void onAnimationStart(Animation animation){;}
                            public void onAnimationRepeat(Animation animation){;}
                        });

                        no.setAnimation(slowly_appear);

                    } else {
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