package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;

/*
 * 사용자
 * 예약에 필요한 정보 작성하고 예약하는 액티비티
 * * 내장형과 외장형을 따로 저장해놓고 진행
 * - 이해원
 */
public class MessageActivity extends AppCompatActivity {
    String type, ownerName, address, hp, petName, race, petColor, petBirth, neutralization, petGender;
    EditText eOwnerName, eAddress, eHP, ePetName, eRace, ePetColor, ePetBirth;
    Spinner sNeutralization, sPetGender;
    ArrayAdapter aaNeutralization, aaPetGender;
    Button btnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // MessageTypeActivity에서 정한 타입 값 불러오기
        Intent typeIntent = getIntent();
        if(typeIntent != null) {    // 인텐트 null 체크
            if(typeIntent.hasExtra("type")) {   // 값이 담겨온 경우
                type = typeIntent.getExtras().toString(); // 타입 값 String에 저장
            }
            else {
                Toast.makeText(getApplicationContext(), "타입이 선택되지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "타입이 선택되지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        // 뷰 정의
        eOwnerName = (EditText) findViewById(R.id.ownerNameText);
        eAddress = (EditText) findViewById(R.id.addressText);
        eHP = (EditText) findViewById(R.id.hpText);
        ePetName = (EditText) findViewById(R.id.petNameText);
        ePetBirth = (EditText) findViewById(R.id.petBirthText);
        eRace = (EditText) findViewById(R.id.raceText);
        ePetColor = (EditText) findViewById(R.id.petColorText);
        ePetBirth = (EditText) findViewById(R.id.petBirthText);
        btnMessage = (Button) findViewById(R.id.messageButton);

        // 성별 Spinner Array 생성
        sPetGender = (Spinner) findViewById(R.id.petGenderSpinner); //butterknife 없을경우
        aaPetGender = ArrayAdapter.createFromResource(this, R.array.petGenderArray, android.R.layout.simple_spinner_dropdown_item);

        //  성별 선택 값 가져오기
        sPetGender.setAdapter(aaPetGender);
        sPetGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 성별 여부 선택 리스너
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                petGender = aaPetGender.getItem(position).toString();
                Toast.makeText(getApplicationContext(), petGender, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 중성화 Spinner Array 생성
        sNeutralization = (Spinner) findViewById(R.id.neutralizationSpinner); //butterknife 없을경우
        aaNeutralization = ArrayAdapter.createFromResource(this, R.array.neutralizationArray, android.R.layout.simple_spinner_dropdown_item);

        //  중성화 선택 값 가져오기
        sNeutralization.setAdapter(aaNeutralization);
        sNeutralization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 중성화 여부 선택 리스너
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                neutralization = aaNeutralization.getItem(position).toString();
                Toast.makeText(getApplicationContext(), neutralization, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 예약하기 버튼 클릭 시
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setOwnerInfo() && setPetInfo()) {    // 입력한 모든 정보가 올바르게 변수에 저장한 경우

                }
                else {  // 하나라도 값이 잘못된 경우
                    //clearEditText();
                }
            }
        });
    }

    /*
    사용자가 적은 EditText 값 제거시키기
    사용은 모르겠음
     */
    private void clearEditText() {
    }

    /*
    소유자 정보를 가져와서 변수에 저장하기 + 공백 처리
    올바르지 않는 값의 정보를 토스트로 출력
    @return : boolean(true : 전부 저장 완료. false : 하나라도 저장 실패)
    */
    private boolean setOwnerInfo() {
        ownerName = eOwnerName.getText().toString().trim(); // 입력한 소유자 정보 가져오기, 스페이스바 공백 처리
        if(ownerName.getBytes().length <= 0) { // null(빈 값) 처리
            Toast.makeText(getApplicationContext(), "소유자의 이름이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /*
    반려동물 정보를 가져와서 변수에 저장하기 + 공백 처리
    올바르지 않는 값의 정보를 토스트로 출력
    @return : boolean(true : 전부 저장 완료. false : 하나라도 저장 실패)
     */
    private boolean setPetInfo() {
        petName = ePetName.getText().toString().trim(); // EditText 정보 가져오기, 스페이스바 공백 처리
        if(petName.getBytes().length <= 0) { // null(빈 값) 처리
            Toast.makeText(getApplicationContext(), "애완동물 이름이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

