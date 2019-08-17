package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gaze.rkdus.a2019_epis_tufu4.popup.HospitalInfoPopupActivity;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

/*
 *  JoinActivity
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class JoinActivity extends BaseActivity {
    private static final int CHECK_INDIVIDUALINFO = 4491;

    EditText ehospital, ename, num1, num2, num3, eid, epw, epwCheck, eaccount, eaccountName;
    ImageButton next_one, next_two;
    LinearLayout idpw;
    LinearLayout no, vowow;

    ImageView check, overlap;

    String hospital = null, name = null, number = null, id = "", pw = "", comfirm = "", account = "", accountName = "";
    boolean success;

    boolean id_check = false, pw_check = false, pwcheck_check = false, accountName_check = false, bank_check = false, account_check = false, ischeck = false;
    InputMethodManager imm;

    Animation slowly_appear, slowlyDisappear;

    CompoundButton cbCheckIndividualInfo;
    boolean isSignUpApp = false;

    String select = "은행";
    TextView bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        StrictMode.enableDefaults();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        success = false;

        ehospital = (EditText) findViewById(R.id.hospital);
        ename = (EditText) findViewById(R.id.name);
        num1 = (EditText) findViewById(R.id.number1);
        num2 = (EditText) findViewById(R.id.number2);
        num3 = (EditText) findViewById(R.id.number3);
        eid = (EditText) findViewById(R.id.id);
        epw = (EditText) findViewById(R.id.pw);
        epwCheck = (EditText) findViewById(R.id.pwCheck);
        eaccount = (EditText) findViewById(R.id.account);
        eaccountName = (EditText) findViewById(R.id.account_name);

        next_one = (ImageButton) findViewById(R.id.next_one);
        next_two = (ImageButton) findViewById(R.id.next_two);

        next_one.setEnabled(false);
        next_two.setEnabled(false);

        check = (ImageView) findViewById(R.id.check);
        overlap = (ImageView) findViewById(R.id.overlap);

        idpw = (LinearLayout) findViewById(R.id.idpw);
        vowow = (LinearLayout) findViewById(R.id.vowow);

        no = (LinearLayout) findViewById(R.id.no);

        cbCheckIndividualInfo = (CheckBox) findViewById(R.id.checkIndividualInfo);

        bank = (TextView) findViewById(R.id.bank);

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, PopupActivity.class);
                intent.putExtra("hospital", ehospital.getText().toString());
                intent.putExtra("num1", num1.getText().toString());
                intent.putExtra("name", ename.getText().toString());
                intent.putExtra("num2", num2.getText().toString());
                intent.putExtra("num3", num3.getText().toString());

                if (eaccount.getText() != null) {
                    intent.putExtra("account", eaccount.getText().toString());
                }
                if (eid.getText() != null) {
                    intent.putExtra("id", eid.getText().toString());
                }
                if (epw.getText() != null) {
                    intent.putExtra("pw", epw.getText().toString());
                }
                if (epwCheck.getText() != null) {
                    intent.putExtra("pwcheck", epwCheck.getText().toString());
                }
                if (eaccountName.getText() != null) {
                    intent.putExtra("accountName", eaccountName.getText().toString());
                }
                startActivityForResult(intent, 1);
            }
        });

//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//        if (height > 2000) {
//            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.select, R.layout.spinner_item);
//            spinner.setAdapter(adapter);
//        } else {
//            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.select, R.layout.spinner_item_small);
//            spinner.setAdapter(adapter);
//        }
//

        epwCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pw = epw.getText().toString();
                comfirm = epwCheck.getText().toString();

                if (pw.equals(comfirm)) {
                    pwcheck_check = true;
                    check.setBackgroundResource(R.drawable.join_check);
                    if (id_check != false && pw_check != false && pwcheck_check != false && accountName_check != false && account_check != false && ischeck != false) {
                        next_two.setEnabled(true);
                        next_two.setBackgroundResource(R.drawable.join_finishon);

                    }

                } else {
                    pwcheck_check = false;
                    check.setBackgroundResource(R.drawable.join_fail);

                    next_two.setEnabled(false);
                    next_two.setBackgroundResource(R.drawable.join_finishoff);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ehospital.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ehospital.getText().toString().equals("")) {
                    ename.setEnabled(false);
                } else {
                    ename.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ename.getText().toString().equals("")) {
                    num1.setEnabled(false);
                } else {
                    num1.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (num1.getText().toString().equals("")) {
                    num2.setEnabled(false);
                } else {
                    num2.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (num2.getText().toString().equals("")) {
                    num3.setEnabled(false);
                } else {
                    num3.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (num3.getText().toString().equals("")) {
                    next_one.setEnabled(false);
                    next_one.setBackgroundResource(R.drawable.join_nextoff);
                } else {
                    next_one.setEnabled(true);
                    next_one.setBackgroundResource(R.drawable.join_nexton);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eaccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (eaccount.getText().toString().equals("")) {
                    account_check = false;

                    next_two.setEnabled(false);
                    next_two.setBackgroundResource(R.drawable.join_finishoff);
                } else {
                    account_check = true;
                    if (id_check != false && pw_check != false && pwcheck_check != false && accountName_check != false && account_check != false && ischeck != false) {
                        next_two.setEnabled(true);
                        next_two.setBackgroundResource(R.drawable.join_finishon);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eaccountName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (eaccountName.getText().toString().equals("")) {
                    accountName_check = false;

                    next_two.setEnabled(false);
                    next_two.setBackgroundResource(R.drawable.join_finishoff);
                } else {
                    accountName_check = true;
                    if (id_check != false && pw_check != false && pwcheck_check != false && accountName_check != false && account_check != false && ischeck != false) {
                        next_two.setEnabled(true);
                        next_two.setBackgroundResource(R.drawable.join_finishon);
                    }
                  //  Log.d("Join", ""+id_check+""+pw_check+""+pwcheck_check+""+accountName_check+""+account_check+""+ischeck);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        epw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (epw.getText().toString().equals("")) {
                    pw_check = false;

                    next_two.setEnabled(false);
                    next_two.setBackgroundResource(R.drawable.join_finishoff);
                } else {
                    pw_check = true;
                    if (id_check != false && pw_check != false && pwcheck_check != false && accountName_check != false && account_check != false && ischeck != false) {
                        next_two.setEnabled(true);
                        next_two.setBackgroundResource(R.drawable.join_finishon);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        next_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital = ehospital.getText().toString();
                name = ename.getText().toString();
                number = num1.getText().toString() + "-" + num2.getText().toString() + "-" + num3.getText().toString();

                new ThreeCheck().execute(getResources().getString(R.string.url) + "/isHospital");
            }
        });

        next_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (select.equals("은행")) {
                    Toast.makeText(JoinActivity.this, "은행을 선택해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    account = eaccount.getText().toString();
                    id = eid.getText().toString();
                    pw = epw.getText().toString();

                    new JoinDB().execute(getResources().getString(R.string.url) + "/putHospital");
                }
            }
        });

        overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = eid.getText().toString();

                if (id.equals("")) {
                    Toast.makeText(getApplicationContext(), "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new IDCheck().execute(getResources().getString(R.string.url) + "/idCheck");
                }
            }
        });

        // 개인정보 수집 동의 텍스트 및 체크박스 클릭 이벤트

        cbCheckIndividualInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbCheckIndividualInfo.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), HospitalInfoPopupActivity.class);
                    startActivityForResult(intent, CHECK_INDIVIDUALINFO);
                }
            }
        });
    }

    /* IDCheck : 중복된 ID 값이 있는지 체크
     * ID 중복이 있으면 -> int 1
     * ID 중복이 없으면 -> int 2
     * Default(에러 체크 하려고 만들었음) -> int 0
     *
     * Uri  --->   /idCheck
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
                    new IDCheck().execute(getResources().getString(R.string.url) + "/idCheck");
                } else {
                    success = (int) json.get("result");

                    if (success == 2) {
                        overlap.setBackgroundResource(R.drawable.join_overlapon);
                        epw.setEnabled(true);
                        eid.setEnabled(false);
                        overlap.setEnabled(false);
                        id_check = true;
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
     * Uri  --->   /hospital/putHospital
     * Parm  --->   {"user":{"hospital":"병원이름", "name":"김가연", "number":"010-4491-0778", "bank":"국민", "accountHolder":"김가연", account":"70940200283092", "id":"test", "pw":"1234"}} 전송
     * Result  --->   {"result":1} 결과 값*/

    public class JoinDB extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("hospital", hospital);
                tmp.accumulate("name", name);
                tmp.accumulate("number", number);
                tmp.accumulate("bank", select);
                tmp.accumulate("accountHolder", accountName);
                tmp.accumulate("account", account);
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
                    new JoinDB().execute(getResources().getString(R.string.url) + "/putHospital");
                } else {
                    success = (int) json.get("result");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "회원가입 성공!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SelectPicActivity.class);
                        intent.putExtra("id", id);
                        startActivityForResult(intent, 4491);
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

    /* ThreeCheck : 대표자 명, 전화번호가 같은지 확인
     * 파라미터와 DB 내용이 모두 같으면 -> int 1
     * 파라미터와 DB 내용이 다르면 -> int 2
     * Default(에러 체크 하려고 만들었음) -> int 0
     *
     * Uri  --->   /isHospital
     * Parm  --->   {"user":{"name":"김가연","number":"031-574-7580"}} 전송
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

                    Log.e("ThreeCheck : ", jsonObject.toString());
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
                    new ThreeCheck().execute(getResources().getString(R.string.url) + "/isHospital");
                } else {

                    success = (int) json.get("result");

                    if (success == 1) {
                        idpw.setVisibility(View.VISIBLE);
                        next_two.setVisibility(View.VISIBLE);
                        vowow.setVisibility(View.VISIBLE);
                        ehospital.setEnabled(false);
                        ename.setEnabled(false);
                        num1.setEnabled(false);
                        num2.setEnabled(false);
                        num3.setEnabled(false);

                    } else if (success == 2) {
                        imm.hideSoftInputFromWindow(num3.getWindowToken(), 0);

                        next_one.setEnabled(false);

                        no.setVisibility(View.VISIBLE);

                        slowly_appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowly_appear);
                        slowlyDisappear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slowly_disappear);

                        slowly_appear.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                try {
                                    sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                no.setAnimation(slowlyDisappear);
                            }

                            public void onAnimationStart(Animation animation) {
                                ;
                            }

                            public void onAnimationRepeat(Animation animation) {
                                ;
                            }
                        });

                        slowlyDisappear.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                no.setVisibility(View.GONE);

                                next_one.setEnabled(true);
                                imm.showSoftInput(num3, 0);
                            }

                            public void onAnimationStart(Animation animation) {
                                ;
                            }

                            public void onAnimationRepeat(Animation animation) {
                                ;
                            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                //데이터 받기
                ehospital.setText(data.getStringExtra("hospital"));
                ehospital.setEnabled(false);
                num1.setText(data.getStringExtra("num1"));
                num1.setEnabled(false);
                num2.setText(data.getStringExtra("num2"));
                num2.setEnabled(false);
                num3.setText(data.getStringExtra("num3"));
                num3.setEnabled(false);
                ename.setText(data.getStringExtra("name"));
                ename.setEnabled(false);
                next_one.setEnabled(false);
                next_two.setVisibility(View.VISIBLE);
                vowow.setVisibility(View.VISIBLE);
                idpw.setVisibility(View.VISIBLE);
                bank.setText(data.getStringExtra("bank"));
                select = data.getStringExtra("bank");
                hospital = ehospital.getText().toString();
                name = ename.getText().toString();
                number = num1.getText().toString() + "-" + num2.getText().toString() + "-" + num3.getText().toString();

                if (data.hasExtra("account")) {
                    eaccount.setText(data.getStringExtra("account"));
                }
                if (data.hasExtra("id")) {
                    eid.setText(data.getStringExtra("id"));
                }
                if (data.hasExtra("pw")) {
                    epw.setText(data.getStringExtra("pw"));
                }
                if (data.hasExtra("pwcheck")) {
                    epwCheck.setText(data.getStringExtra("pwcheck"));
                }
                if (data.hasExtra("accountName")) {
                    eaccountName.setText(data.getStringExtra("accountName"));
                }
            }

        } else if (requestCode == CHECK_INDIVIDUALINFO) {
            if (resultCode == RESULT_OK) {
                Log.d("Join", "정보제공동의 팝업창에서 확인 누름!");
                ischeck = true;
                Toast.makeText(getApplicationContext(), "정보제공에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("Join", "정보제공동의 팝업창에서 취소 누름!");
                Toast.makeText(getApplicationContext(), "정보제공에 동의하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                cbCheckIndividualInfo.setChecked(false);
            }
        } else {
            if (resultCode != RESULT_OK) {
                finish();
            }
        }
    }
}
