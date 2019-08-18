package com.gaze.rkdus.a2019_epis_tufu4;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    TextView owner, resident, phone, resAddr, nowAddr, animal, variety, furColor, gender, neutralization, birthday, acqDate, special, owner_2;
    TextView year, month, date, name;

    String id, str_owner, str_animal;
    public MyProgressDialog2 progressDialog;

    String pdf_name;

    ImageButton share, finish;

    int type = 0;
    // 1: 내장형 / 2 : 외장형 / 3 : 등록인식표

    final int MY_PERMISSION_REQUEST_STORAGE = 1234;
    ConstraintLayout constraintLayout;
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

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        id = intent.getStringExtra("id");
        str_owner = intent.getStringExtra("owner");
        str_animal = intent.getStringExtra("animal");

        pdf_name = pdf_name + "_" + str_animal;

        owner = (TextView) findViewById(R.id.owner);
        owner_2 = (TextView) findViewById(R.id.owner_2);
        resident = (TextView) findViewById(R.id.resident);
        phone = (TextView) findViewById(R.id.phone);
        resAddr = (TextView) findViewById(R.id.resAddr);
        nowAddr = (TextView) findViewById(R.id.nowAddr);
        animal = (TextView) findViewById(R.id.animal);
        variety = (TextView) findViewById(R.id.variety);
        furColor = (TextView) findViewById(R.id.furColor);
        gender = (TextView) findViewById(R.id.gender);
        neutralization = (TextView) findViewById(R.id.neutralization_o);
        birthday = (TextView) findViewById(R.id.birthday);
        acqDate = (TextView) findViewById(R.id.acqDate);
        special = (TextView) findViewById(R.id.special);
        year = (TextView) findViewById(R.id.year);
        month = (TextView) findViewById(R.id.month);
        date = (TextView) findViewById(R.id.day);
        name = (TextView) findViewById(R.id.name);

        share = (ImageButton)findViewById(R.id.share);
        finish = (ImageButton)findViewById(R.id.finish);

        owner.setText(str_owner);
        owner_2.setText(str_owner);
        animal.setText(str_animal);

        name.setText(str_owner);
        year.setText(str_year);
        month.setText(str_month);
        date.setText(str_date);

        constraintLayout = (ConstraintLayout) findViewById(R.id.framelayout);

        share.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //pdf.setVisibility(View.INVISIBLE);
                try {
                    checkPermission();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new CancelReservation().execute(getResources().getString(R.string.url) + "/changeState4");
            }
        });

        new ReservationInfoData().execute(getResources().getString(R.string.url) + "/getReservationInfoData");

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

    /* ReservationInfoData : ID, 주인 이름, 강아지 이름 값을 통해 예약 데이터 요청
     *
     *
     * Uri  --->   /getReservationInfoData
     * Parm  --->   {"user":{"id":"test","owner":"김가연","animal":"뿡이"}} 전송
     * Result  --->   {"result":{"owner":"김가연","resident":"960708-2","phone":"010-4491-0778","resAddr":"대전","nowAddr":"궁동",
     * "animal":"뿡이","variety":"시츄","furColor":"흰색+갈색","gender":"남","neutralization":"했움","birthday":"2008-04-04","acqDate":"2008-04-04","special":"겁이 많아요ㅠㅠ"}} 결과 값 */

    public class ReservationInfoData extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", str_owner);
                tmp.accumulate("pet_name", str_animal);

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
                    new ReservationInfoData().execute(getResources().getString(R.string.url) + "/getReservationInfoData");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = json.getJSONObject("result");

                    owner.setText(jsonObject.getString("owner_name"));
                    resident.setText(jsonObject.getString("owner_resident"));
                    phone.setText(jsonObject.getString("owner_phone"));
                    resAddr.setText(jsonObject.getString("address1"));
                    nowAddr.setText(jsonObject.getString("address2"));
                    animal.setText(jsonObject.getString("pet_name"));
                    variety.setText(jsonObject.getString("pet_variety"));
                    furColor.setText(jsonObject.getString("pet_color"));

                    if (jsonObject.getInt("pet_gender") == 2) {
                        gender.setText("수");
                    } else {
                        gender.setText("암");
                    }

                    if (jsonObject.getInt("pet_neutralization") == 1) {
                        neutralization.setText("O");
                    } else {
                        neutralization.setText("X");
                    }

                    birthday.setText(jsonObject.getString("pet_birth"));
                    acqDate.setText(jsonObject.getString("regist_date"));
                    special.setText(jsonObject.getString("etc"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, result);

        }
    }

    public void layoutToImage() throws IOException {


        Bitmap bm = Bitmap.createBitmap(constraintLayout.getWidth(), constraintLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Drawable bgDrawable = constraintLayout.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        constraintLayout.draw(canvas);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + pdf_name + ".jpg");
        try {

            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            Document document = new Document();
            dirpath = Environment.getExternalStorageDirectory().toString();

            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/" + pdf_name + ".pdf"));
//            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/abc.pdf"));
            document.open();

            Image image = Image.getInstance(f.toString());
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100;
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(image);
            document.close();

            f.delete();

            FileOutputStream os = openFileOutput(dirpath + "/" + pdf_name + ".pdf", MODE_WORLD_READABLE);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();

        dirpath = Environment.getExternalStorageDirectory().toString();
        String text = dirpath + "/" + pdf_name + ".pdf";


        File file = new File(text);

        intent.setAction(Intent.ACTION_SEND);
        intent.setType("application/*");
        //intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(new File(text).toString()));
        Uri uri = FileProvider.getUriForFile(RegisterActivity.this, "com.bignerdranch.android.test.fileprovider", new File(text));
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        intent.putExtra(Intent.EXTRA_SUBJECT, "동물등록증 신청합니다");
        String[] mailto = { "chy0209@korea.kr" };
        intent.putExtra(Intent.EXTRA_EMAIL, mailto);
        intent.putExtra(Intent.EXTRA_TEXT, "동물등록증 신청합니다");

        Intent chooser = Intent.createChooser(intent, "공유하기");
        startActivity(chooser);

        progressDialog = MyProgressDialog2.show(RegisterActivity.this, "", "", true, false, null);

        share.setEnabled(false);

        try {
            sleep(1500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        share.setEnabled(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() throws IOException {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            layoutToImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        layoutToImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    Log.d(TAG, "Permission always deny");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }


    /* CancelReservation : 병원 ID, 주인 이름, 강아지 이름값을 통해 예약 상태 변경

    예약상태 4로 변경 (현재 : 2)

    성공 1 실패 0

    Uri  --->   /hospital/changeState4
    Parm  --->   "user":{"id":"test","owner_name":"김가연","pet_name":"뿡이"}} 전송
        Result  --->   {"result":1} 결과 값 */

    public class CancelReservation extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                JSONObject jsonObject = new JSONObject();
                JSONObject tmp = new JSONObject();

                tmp.accumulate("id", id);
                tmp.accumulate("owner_name", str_owner);
                tmp.accumulate("pet_name", str_animal);

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
                    new CancelReservation().execute(getResources().getString(R.string.url) + "/changeState4");
                } else {
                    succes = (int) json.get("result");

                    if (succes == 1) {
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), "버튼을 다시 눌러주세요.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("CancelReservation", result);

        }
    }
}
