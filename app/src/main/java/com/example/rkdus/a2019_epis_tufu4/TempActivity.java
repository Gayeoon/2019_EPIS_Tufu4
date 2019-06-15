package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class TempActivity extends AppCompatActivity {

    DatabaseCls db;
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        Button gotouser = (Button) findViewById(R.id.gotouser);
        Button gotojoin = (Button) findViewById(R.id.gotojoin);
        gotojoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        gotouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });


//        /*
//        * db 테스트 코드
//        */
//        db = new DatabaseCls();
//
//        // ##### 임시 ##### //
//        // 나중에 클라이언트랑 병원 받아오면 매개변수로
//        Client.Pet tmpPet = new Client().new Pet();
//        tmpPet.setPet("펫이름", "19961217", "black", "false", "true", "치와아치와와", "남견", "96121712003993");
//        Client tmpCl = new Client("주인이름", "99스트릿", "0428221551", "내장형", tmpPet);
//        db.addClient(tmpCl);
//
//
//        /*
//        * 여기 notification 안 들어감
//        */
//        Hospital.Notification tmpNoti = new Hospital().new Notification();
//        Hospital.Notification.Reservation tmpReserve = tmpNoti.getReservation();
//        Hospital.Inner tmpIn = new Hospital().new Inner("IDisIN0ANSKD13BPK3523PKN3", "true");
//        Hospital.Outer tmpOut = new Hospital().new Outer("IDisIN0ANSKD13BPK3523PKN3", "false");
//        tmpReserve.inners.add(tmpIn);
//        tmpReserve.outers.add(tmpOut);
//        Hospital tmpHos = new Hospital("이거내병원", "이거쟤병원", "0402039423", "480480", tmpNoti);
////        db.addHospital(tmpHos);


        // server test //

        tvData = (TextView) findViewById(R.id.dbContents);
        Button readDB = (Button) findViewById(R.id.readDB);

        readDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new JSONTask().execute("http://192.168.0.39:3000/getHospital");
                new JSONTask().execute("http://201502119.iptime.org/getHospital");
            }
        });
    }


        public class JSONTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... urls) {
                try {
                    // JSONObject를 만들고 형식 맞춰준다
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();

                    // READ 해주세요 : 라이브러리 추가하고싶어어요ㅛㅇ,,ㅠㅠㅠㅠㅠㅠㅠㅠㅠ
//                    JSONParser parser = new JSONParser();
                    Object parseObj;

                    /*
                        AGENCY_TB_PK: int(11),
                        ADDRESS1: varchar(80),
                        ADDRESS2: varchar(50),
                        CEO_NAME: varchar(10),
                        AGENCY_NAME: varchar(20),
                        PHONE_NUMBER: varchar(15)
                    */

//                    jsonObject.accumulate("AGENCY_TB_PK", "1");
//                    jsonObject.accumulate("ADDRESS1", "test");
//                    jsonObject.accumulate("ADDRESS2", "test");
//                    jsonObject.accumulate("CEO_NAME", "test");
//                    jsonObject.accumulate("AGENCY_NAME", "test");
//                    jsonObject.accumulate("PHONE_NUMBER", "test");

                    HttpURLConnection con = null;
                    BufferedReader reader = null;

                    try{
                        //URL url = new URL("http://192.168.0.39:3000/getData");
                        URL url = new URL(urls[0]); // url 가져오기
                        con = (HttpURLConnection) url.openConnection();
                        con.connect();//연결 수행

                        InputStream stream = con.getInputStream(); // 입력 스트림 생성
                        reader = new BufferedReader(new InputStreamReader(stream)); // 버퍼 선언
                        StringBuffer buffer = new StringBuffer(); //실제 데이터를 받는곳
                        String line = ""; //line별 스트링을 받기 위한 temp 변수

                        // 서버에서 보낸 데이터 읽기
                        while((line = reader.readLine()) != null){
                            buffer.append(line);
                        }

//                        parseObj = parser.parse(buffer);


                        //다 읽은 후 String 형변환
                        return buffer.toString();

                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        // 종료할 때 연결 해제
                        if(con != null){
                            con.disconnect();
                        }
                        try {
                            // 버퍼 닫기
                            if(reader != null){
                                reader.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } // finally
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            // 텍스트뷰
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                tvData.setText(result);
            }

    }
}
