package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TempActivity extends AppCompatActivity {

    DatabaseCls db;

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


        /*
        * db 테스트 코드
        */
        db = new DatabaseCls();

        // ##### 임시 ##### //
        // 나중에 클라이언트랑 병원 받아오면 매개변수로
        Client.Pet tmpPet = new Client().new Pet();
        tmpPet.setPet("펫이름", "19961217", "black", "false", "true", "치와아치와와", "남견", "96121712003993");
        Client tmpCl = new Client("주인이름", "99스트릿", "0428221551", "내장형", tmpPet);
        db.addClient(tmpCl);


        /*
        * 여기 notification 안 들어감
        */
        Hospital.Notification tmpNoti = new Hospital().new Notification();
        Hospital.Notification.Reservation tmpReserve = tmpNoti.getReservation();
        Hospital.Inner tmpIn = new Hospital().new Inner("IDisIN0ANSKD13BPK3523PKN3", "true");
        Hospital.Outer tmpOut = new Hospital().new Outer("IDisIN0ANSKD13BPK3523PKN3", "false");
        tmpReserve.inners.add(tmpIn);
        tmpReserve.outers.add(tmpOut);
        Hospital tmpHos = new Hospital("이거내병원", "이거쟤병원", "0402039423", "480480", tmpNoti);
//        db.addHospital(tmpHos);
    }
}
