package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HospitalActivity extends AppCompatActivity {
    String id;
    String hos_name;

    TextView name, count;
    ImageButton status, community, alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        name = (TextView) findViewById(R.id.name);
        count = (TextView) findViewById(R.id.count);

        status = (ImageButton) findViewById(R.id.status);
        community = (ImageButton) findViewById(R.id.community);
        alarm = (ImageButton) findViewById(R.id.alarm);

        hos_name = HospitalName(id);

        name.setText(hos_name);
        count.setText(Newmessage(id));

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), StatusActivity.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
            }
        });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent2);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), AlarmActivity.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
            }
        });
    }

    private int Newmessage(String id) {
        // To 지원
        // id 가지고 메세지 몇개 왔는지 검색해주세여!
        // 혹시 새로운 메세지 중간에 도착하면
        // count.setText("여기에"); -> 입력하시면 됩니다!

        return 0;
    }

    private String HospitalName(String id) {
        // To 지원
        // id 가지고 hospital_name 좀 검색해주세여!

        return "두부는네모병원";
    }
}

