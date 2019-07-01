package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteCommunityActivity extends BaseActivity {
    public static final String TAG = "NewReservationActivity";
    public String url = "http://192.168.1.11:3000";

    ImageButton pic1, pic2, pic3;
    ImageView btnAddImg, btnWrite;
    EditText txtShow, title;
    byte[] byteArray, picbyte1 = null, picbyte2 = null, picbyte3 = null;
    String strpic1 = null, strpic2 = null, strpic3 = null;

    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_community);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");


        pic1 = (ImageButton) findViewById(R.id.pic1);
        pic2 = (ImageButton) findViewById(R.id.pic2);
        pic3 = (ImageButton) findViewById(R.id.pic3);
        btnWrite = (ImageView) findViewById(R.id.btnWrite);
        txtShow = (EditText) findViewById(R.id.txtShow);
        title = (EditText) findViewById(R.id.title);

        // 버튼 클릭 시 카메라 앨범으로 이동
        pic1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra("outputX", 400);
                        intent.putExtra("outputY", 1000);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                        break;
                }
                return true;
            }
        });

        pic2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra("outputX", 500);
                        intent.putExtra("outputY", 500);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 2);
                        break;
                }
                return true;
            }
        });

        pic3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra("outputX", 300);
                        intent.putExtra("outputY", 300);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 3);
                        break;
                }
                return true;
            }
        });

        btnWrite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        // TODO: 입력 정보 (인증 사진 및 코멘트) 저장하여 표시
                        new CoummunityDB().execute(url + "/putCoummunity");

                        break;
                }
                return true;
            }
        });


//        // 원 모양으로 이미지 버튼에 그림자 넣기
//        Outline circularOutline = new Outline();
//        circularOutline.setOval(0, 0, 4, 4);
//        btnAddImg.setOutline(circularOutline);
    }

    // 이미지를 비트맵 형식으로 받아와 이미지뷰에 표시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    pic1.setImageBitmap(img);

                    // 비트맵 형식의 이미지를 Byte Array로 변경하여 인텐트 담아 전송
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    strpic1 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    pic2.setImageBitmap(img);

                    // 비트맵 형식의 이미지를 Byte Array로 변경하여 인텐트 담아 전송
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    strpic2 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 3) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    pic3.setImageBitmap(img);

                    // 비트맵 형식의 이미지를 Byte Array로 변경하여 인텐트 담아 전송
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    strpic3 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* CoummunityDB : 프로필 사진 db에 저장
     * 저장 성공 -> int 1
     * 저장 실패 -> int 0
     *
     * Uri  --->   /putCoummunity
     * Parm  --->   {"user":{"title":"집에 보내줘","article_author":"김가연","article_date":"2019-07-02","article_content":"집에 가고싶어요..","img_url_1":"null","img_url_2":"null","img_url_3":"null"}} 전송
     * Result  --->   {"result":1} 결과 값 */

    public class CoummunityDB extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String nowTime = sdf.format(date);

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("title", title.getText().toString());
                tmp.accumulate("article_author", user);
                tmp.accumulate("article_date", nowTime);
                tmp.accumulate("article_content", txtShow.getText().toString());
                tmp.accumulate("img_url_1", strpic1);
                tmp.accumulate("img_url_2", strpic2);
                tmp.accumulate("img_url_3", strpic3);

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

                    Log.e("Write", jsonObject.toString());
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
                    new CoummunityDB().execute(url + "/putCoummunity");
                } else {
                    succes = (int) json.get("result");

                    if (succes == 1) {
                        Toast.makeText(getApplicationContext(), "저장 성공!!", Toast.LENGTH_LONG).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "저장 실패!!", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("LoginActivity", result);

        }
    }

}
