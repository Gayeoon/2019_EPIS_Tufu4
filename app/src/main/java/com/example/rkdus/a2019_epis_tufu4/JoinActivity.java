package com.example.rkdus.a2019_epis_tufu4;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class JoinActivity extends AppCompatActivity {

    EditText ehospital, ename, enumber;
    ImageButton next_one, next_two;
    LinearLayout idpw;

    String hospital = null, name = null, number = null;
    boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        StrictMode.enableDefaults();

        success = false;

        ehospital = (EditText) findViewById(R.id.hospital);
        ename = (EditText) findViewById(R.id.name);
        enumber = (EditText) findViewById(R.id.number);

        next_one = (ImageButton) findViewById(R.id.next_one);
        next_two = (ImageButton) findViewById(R.id.next_two);

        idpw = (LinearLayout)findViewById(R.id.idpw);


        next_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital = ehospital.getText().toString();
                name = ename.getText().toString();
                number = enumber.getText().toString();

                success = ThreeCheck(hospital, name, number);

                if (success){
                    idpw.setVisibility(View.VISIBLE);
                    next_two.setVisibility(View.VISIBLE);
                    ehospital.setEnabled(false);
                    ename.setEnabled(false);
                    enumber.setEnabled(false);

                }else {
                    // 존재하지 않는 병원 ImageView 띄우기
                }
            }
        });


    }

    boolean ThreeCheck(String hospital, String name, String number) {

        boolean inHospital = false, inNumber = false;
        String tHospital = null, tNumber = null;

        try {
            URL url = new URL("http://211.237.50.150:7080/openapi/sample/xml/Grid_20141225000000000161_1/1/5?"
                    + "API_KEY=9e419c9fb4293de4b448d0f553b753baddda8bc74c07e14f64fcb69ae1cbde4e&"
                    + "RPRSNTV_NM=" + name
            );

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("ENTRPS_NM")) {
                            inHospital = true;
                        }
                        if (parser.getName().equals("ENTRPS_TELNO")) {
                            inNumber = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (inHospital) {
                            tHospital = parser.getText();
                            inHospital = false;
                        }
                        if (inNumber) {
                            tNumber = parser.getText();
                            inNumber = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            Toast.makeText(this, "대표자 이름이 틀렸다!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (hospital.equals(tHospital)) {
            if (number.equals(tNumber)) {
                return true;
            } else {
                Toast.makeText(this, "번호가 틀렸다!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "병원명이 틀렸다!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}