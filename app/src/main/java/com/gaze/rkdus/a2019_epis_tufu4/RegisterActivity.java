package com.gaze.rkdus.a2019_epis_tufu4;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;

import org.json.JSONException;
import org.json.JSONObject;
import pl.polidea.view.ZoomView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    TextView owner, resident, phone, resAddr, nowAddr, animal, variety, furColor, gender, neutralization, birthday, acqDate, special;
    TextView year, month, date;

    String id, name;

    String pdf_name;

    int type = 0;
    // 1: 내장형 / 2 : 외장형 / 3 : 등록인식표

    private ImageButton pdf;
    FrameLayout frameLayout;
    String dirpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoom_item, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        long now = System.currentTimeMillis();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat nameFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        String str_year = yearFormat.format(currentTime);
        String str_month = monthFormat.format(currentTime);
        String str_date = dayFormat.format(currentTime);
        pdf_name = nameFormat.format(currentTime);

        ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(false); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(2f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.

        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.addView(zoomView);

//        Intent intent = getIntent();
//        type = intent.getIntExtra("type", 0);
//
//        id = intent.getStringExtra("id");
//        name = intent.getStringExtra("name");

        pdf_name = pdf_name +"_"+ name;

                owner = (TextView) findViewById(R.id.owner);
        resident = (TextView) findViewById(R.id.resident);
        phone = (TextView) findViewById(R.id.phone);
        resAddr = (TextView) findViewById(R.id.resAddr);
        nowAddr = (TextView) findViewById(R.id.nowAddr);
        animal = (TextView) findViewById(R.id.animal);
        variety = (TextView) findViewById(R.id.variety);
        furColor = (TextView) findViewById(R.id.furColor);
        gender = (TextView) findViewById(R.id.gender);
        neutralization = (TextView) findViewById(R.id.neutralization);
        birthday = (TextView) findViewById(R.id.birthday);
        acqDate = (TextView) findViewById(R.id.acqDate);
        special = (TextView) findViewById(R.id.special);
        year = (TextView) findViewById(R.id.year);
        month = (TextView) findViewById(R.id.month);
        date = (TextView) findViewById(R.id.day);

        year.setText(str_year);
        month.setText(str_month);
        date.setText(str_date);

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        pdf = (ImageButton) findViewById(R.id.pdf);

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdf.setVisibility(View.INVISIBLE);
                layoutToImage();
            }
        });

//        new ReservationData().execute(getResources().getString(R.string.url) + "/getReservationData");

//        check = (ImageButton) findViewById(R.id.check);
//
//        check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ReservationCheck().execute(getResources().getString(R.string.url) + "/putChangeState");
//                finish();
//            }
//        });

    }

/*    ReservationData : ID, 주인 이름, type 값을 통해 예약 데이터 요청

    필요 데이터 : 병원 이름, 신규 예약 건수, 등록 대기 건수, 등록 완료 건수

    type -> 1 : 내장형 / 2 : 외장형 / 3 : 등록인식표

    Uri  --->   /getReservationData
    Parm  --->   {"user":{"id":"test","owner_name":"김가연","type":1}} 전송
    Result  --->  {"result":{"OWNER_NAME":"김가연“,"OWNER_RESIDENT":
        "960708-0000000","OWNER_PHONE_NUMBER":"010-4491-0778“,"OWNER_ADDRESS1":"대전","OWNER_ADDRESS2":"궁동","PET_NAME":"뿡이“
                ,"PET_VARIETY":"시츄","PET_COLOR":"흰색+갈색","PET_GENDER":"남“,"PET_NEUTRALIZATION":"했움","PET_BIRTH":"2008-04-04",
                "REGIST_DATE":"2008-04-04","ETC":"겁이 많아요ㅠㅠ"}} 결과 값 */

    public class ReservationData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", name);
                tmp.accumulate("type", type);

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
                    new ReservationData().execute(getResources().getString(R.string.url) + "/getReservationData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    owner.setText(jsonObject.getString("OWNER_NAME"));
                    resident.setText(jsonObject.getString("OWNER_RESIDENT"));
                    phone.setText(jsonObject.getString("OWNER_PHONE_NUMBER"));
                    resAddr.setText(jsonObject.getString("OWNER_ADDRESS1"));
                    nowAddr.setText(jsonObject.getString("OWNER_ADDRESS2"));
                    animal.setText(jsonObject.getString("PET_NAME"));
                    variety.setText(jsonObject.getString("PET_VARIETY"));
                    furColor.setText(jsonObject.getString("PET_COLOR"));

                    if (jsonObject.getString("PET_GENDER") == "1") {
                        gender.setText("남");
                    } else {
                        gender.setText("여");
                    }

                    if (jsonObject.getString("PET_NEUTRALIZATION") == "1") {
                        neutralization.setText("0");
                    } else {
                        neutralization.setText("X");
                    }


                    birthday.setText(jsonObject.getString("PET_BIRTH"));
                    acqDate.setText(jsonObject.getString("REGIST_DATE"));
                    special.setText(jsonObject.getString("ETC"));

                    Log.e(TAG, jsonObject.getString("OWNER_NAME"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }

    /* ReservationCheck : ID, 주인 이름, type 값을 통해 해당 데이터 상태를 등록 대기 상태로 변경
     *
     * type -> 1 : 내장형 / 2 : 외장형 / 3 : 등록인식표
     *
     * 변경 성공하면 -> int 1
     * 변경 실패하면 -> int 0
     *
     *  id/name/type이 일치하는 데이터의 state를 1에서 2로 변경!
     *
     * Uri  --->   /putChangeState
     * Parm  --->   {"user":{"id":"test","name":"김가연","type":1}} 전송
     * Result  --->   {"result":1} 결과 값 */

    public class ReservationCheck extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", name);
                tmp.accumulate("type", type);

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
            int success = 0;

            try {
                json = new JSONObject(result);

                if (json.get("result") == null) {
                    new ReservationCheck().execute(getResources().getString(R.string.url) + "/putChangeState");
                } else {
                    success = (int) json.get("result");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), "예약확인!!", Toast.LENGTH_LONG).show();

                        String tel = "tel:" + phone.getText().toString();
                        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }

    public void layoutToImage() {

        Bitmap bm = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Drawable bgDrawable = frameLayout.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        frameLayout.draw(canvas);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator +pdf_name+".jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            Document document = new Document();
            dirpath = Environment.getExternalStorageDirectory().toString();

            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/"+pdf_name+".pdf"));
            document.open();

            Image image = Image.getInstance(f.toString());
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100;
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(image);
            document.close();
            Toast.makeText(this, dirpath + "/"+pdf_name+".pdf", Toast.LENGTH_SHORT).show();

            f.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
