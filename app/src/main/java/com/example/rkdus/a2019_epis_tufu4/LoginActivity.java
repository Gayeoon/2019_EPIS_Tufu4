package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText id, pw;
    ImageButton login, join, find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);

        login = (ImageButton) findViewById(R.id.login);
        join = (ImageButton) findViewById(R.id.join);
        find = (ImageButton) findViewById(R.id.find);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Check(id.getText().toString(), pw.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), HospitalActivity.class);
                    intent.putExtra("id", id.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean Check(String id, String pw) {
        // To 지원
        // DB에 ID/PW 있는지 확인해죠!
        return false;
    }
}

