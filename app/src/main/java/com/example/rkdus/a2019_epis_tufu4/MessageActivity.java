package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.SERVER_URL;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.StringToJSON;
import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.printConnectionError;

/*
 * 사용자
 * 예약에 필요한 정보 작성하고 예약하는 액티비티
 * * 내장형과 외장형을 따로 저장해놓고 진행
 * - 이해원
 */
public class MessageActivity extends AppCompatActivity {
    public static final String TAG = "LogGoGo";
    private static final int SELECT_RESERVATION = 10;

    int key;
    String hospitalName;
    String ownerName, ownerRRN, ownerHP, ownerPostCode, ownerDetailPostCode, ownerRealPostCode, ownerRealDetailPostCode;    // 소유주
    String ownerAddress, ownerRealAddress;
    String petName, petRace, petColor, petBirth, petGetDate, petSpecialProblem;
    String petYear, petMonth, petDay, petGetYear, petGetMonth, petGetDay;
    int petGender;  // 0: default,  1: female,  2: male
    int petNeutralization;  // 0: default,  1: neutralization,  2: not neutralization
    int type;   // 0: default,  1: inner,  2: outer,  3: badge

    EditText eOwnerName, eOwnerRRNBefore, eOwnerRRNAfter; // 이름 및 주민등록번호
    EditText eOwnerHP1, eOwnerHP2, eOwnerHP3;   // 전화번호
    EditText eOwnerPostCode1, eOwnerPostCode2, eOwnerDetailPostCode, eOwnerRealPostCode1, eOwnerRealPostCode2, eOwnerRealDetailPostCode; // 전화번호, 우편번호, 상세주소, 실제주소
    EditText ePetName, ePetRace, ePetColor, ePetSpecialProblem; // 애완동물 이름, 인종, 색깔, 특이사항
    CheckBox cbMatchedPostCode; // 주민등록주소와 동일 체크박스

    ImageView searchPostCodeBtn, searchRealPostCodeBtn, ivPetFemale, ivPetMale, ivPetNeutalization, ivPetNotNeutralization;
    ImageView innerBtn, outerBtn, badgeBtn, reservationBtn; // 등록방법, 예약버튼
    Spinner sPetBirthYear, sPetBirthMonth, sPetBirthDay, sPetGetYear, sPetGetMonth, sPetGetDay;

    ArrayList<String> yearArray = new ArrayList<>();
    ArrayList<String> monthArray = new ArrayList<>();
    ArrayList<String> dayArray = new ArrayList<>();
    ArrayAdapter aaYear, aaMonth, aaDay, aaGetYear, aaGetMonth, aaGetDay;
    MessageAsyncTask messageAsyncTask;
    MyReservationData myReservationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // 뷰 정의 - 소유주
        eOwnerName = (EditText) findViewById(R.id.ownerNameText);
        eOwnerRRNBefore = (EditText) findViewById(R.id.ownerRRNBefore);
        eOwnerRRNAfter = (EditText) findViewById(R.id.ownerRRNAfter);
        eOwnerHP1 = (EditText) findViewById(R.id.ownerHPFirst);
        eOwnerHP2 = (EditText) findViewById(R.id.ownerHPSecond);
        eOwnerHP3 = (EditText) findViewById(R.id.ownerHPThird);
        eOwnerPostCode1 = (EditText) findViewById(R.id.ownerPostCodeFirst);
        eOwnerPostCode2 = (EditText) findViewById(R.id.ownerPostCodeSecond);
        eOwnerDetailPostCode = (EditText) findViewById(R.id.ownerDetailPostCode);
        eOwnerRealPostCode1 = (EditText) findViewById(R.id.ownerRealPostCodeFirst);
        eOwnerRealPostCode2 = (EditText) findViewById(R.id.ownerRealPostCodeSecond);
        eOwnerRealDetailPostCode = (EditText) findViewById(R.id.ownerRealDetailPostCode);
        cbMatchedPostCode = (CheckBox) findViewById(R.id.matchedPostCodeBox);

        // 뷰 정의 - 반려동물
        ePetName = (EditText) findViewById(R.id.petNameText);
        ePetRace = (EditText) findViewById(R.id.petRaceText);
        ePetColor = (EditText) findViewById(R.id.petColorText);
        ePetSpecialProblem = (EditText) findViewById(R.id.petSpecialProblem);
        searchPostCodeBtn = (ImageView) findViewById(R.id.searchPostCodeBtn);
        searchRealPostCodeBtn = (ImageView) findViewById(R.id.searchRealPostCodeBtn);
        ivPetFemale = (ImageView) findViewById(R.id.petFemale);
        ivPetMale = (ImageView) findViewById(R.id.petMale);
        ivPetNeutalization = (ImageView) findViewById(R.id.petNeutralization);
        ivPetNotNeutralization = (ImageView) findViewById(R.id.petNotNeutralization);
        innerBtn = (ImageView) findViewById(R.id.innerBtn);
        outerBtn = (ImageView) findViewById(R.id.outerBtn);
        badgeBtn = (ImageView) findViewById(R.id.badgeBtn);
        reservationBtn = (ImageView) findViewById(R.id.reservationBtn);
        sPetBirthYear = (Spinner) findViewById(R.id.petBirthYearSpinner);
        sPetBirthMonth = (Spinner) findViewById(R.id.petBirthMonthSpinner);
        sPetBirthDay = (Spinner) findViewById(R.id.petBirthDaySpinner);
        sPetGetYear = (Spinner) findViewById(R.id.petGetYearSpinner);
        sPetGetMonth = (Spinner) findViewById(R.id.petGetMonthSpinner);
        sPetGetDay = (Spinner) findViewById(R.id.petGetDaySpinner);

        setDateArrayForSpinner();

        // ArrayAdapter 정의
        aaYear = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, yearArray);
        aaGetYear = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, yearArray);
        aaMonth = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, monthArray);
        aaGetMonth = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, monthArray);
        aaDay = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, dayArray);
        aaGetDay = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, dayArray);

        // ArrayAdapter 설정.
        sPetBirthYear.setAdapter(aaYear);
        sPetBirthYear.setSelection(0, false);
        sPetGetYear.setAdapter(aaGetYear);
        sPetGetYear.setSelection(0, false);
        sPetBirthMonth.setAdapter(aaMonth);
        sPetBirthMonth.setSelection(0, false);
        sPetGetMonth.setAdapter(aaGetMonth);
        sPetGetMonth.setSelection(0, false);
        sPetBirthDay.setAdapter(aaDay);
        sPetBirthDay.setSelection(0, false);
        sPetGetDay.setAdapter(aaGetDay);
        sPetGetDay.setSelection(0, false);

        // Spinner Select Listener 정의
        SpinnerItemSelectedListener spinnerItemSelectedListener = new SpinnerItemSelectedListener();
        sPetBirthYear.setOnItemSelectedListener(spinnerItemSelectedListener);
        sPetBirthMonth.setOnItemSelectedListener(spinnerItemSelectedListener);
        sPetBirthDay.setOnItemSelectedListener(spinnerItemSelectedListener);
        sPetGetYear.setOnItemSelectedListener(spinnerItemSelectedListener);
        sPetGetMonth.setOnItemSelectedListener(spinnerItemSelectedListener);
        sPetGetDay.setOnItemSelectedListener(spinnerItemSelectedListener);

        // ImageView Click Listener 선언
        ImageViewClickListener imageViewClickListener = new ImageViewClickListener();
        searchRealPostCodeBtn.setOnTouchListener(imageViewClickListener);
        searchPostCodeBtn.setOnTouchListener(imageViewClickListener);
        ivPetFemale.setOnTouchListener(imageViewClickListener);
        ivPetMale.setOnTouchListener(imageViewClickListener);
        ivPetNeutalization.setOnTouchListener(imageViewClickListener);
        ivPetNotNeutralization.setOnTouchListener(imageViewClickListener);
        innerBtn.setOnTouchListener(imageViewClickListener);
        outerBtn.setOnTouchListener(imageViewClickListener);
        badgeBtn.setOnTouchListener(imageViewClickListener);
        reservationBtn.setOnTouchListener(imageViewClickListener);

        // 체크박스 클릭 이벤트
        cbMatchedPostCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbMatchedPostCode.isChecked()) {
                    if(checkEditText(eOwnerPostCode1) && checkEditText(eOwnerPostCode2) && checkEditText(eOwnerDetailPostCode)) {
                        // editText 설정
                        eOwnerRealPostCode1.setText(eOwnerPostCode1.getText().toString());
                        eOwnerRealPostCode2.setText(eOwnerPostCode2.getText().toString());
                        eOwnerRealDetailPostCode.setText(eOwnerDetailPostCode.getText().toString());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "공백이 있어서 체크 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                }
                else {  // 초기화
                    eOwnerRealPostCode1.setText("");
                    eOwnerRealPostCode2.setText("");
                    eOwnerRealDetailPostCode.setText("");
                }
            }
        });

        Intent typeIntent = getIntent();
        if(typeIntent != null) {    // 인텐트 null 체크
            // SearchActivity에서 정한 타입 값 불러오기
            if(typeIntent.hasExtra("key") && typeIntent.hasExtra("hospitalName")) {
                key = typeIntent.getIntExtra("key", 0);   // 병원 type값 int에 저장
                hospitalName = typeIntent.getStringExtra("hospitalName");   // 병원 이름 String에 저장
            }
            // MyPageActivity에서 수정을 통해 받은 MyReservationData 가져오기.
            else if(typeIntent.hasExtra("data")) {
                myReservationData = (MyReservationData) typeIntent.getSerializableExtra("data");
                printReservationData(myReservationData);
            }
            else {
                Toast.makeText(getApplicationContext(), "필수 값을 불러올 수 없습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "타입이 선택되지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /*
    예약 수정 시 EditText에 정보 미리 입력시키기.
     */
    private void printReservationData(MyReservationData data) {
        ownerName = data.getOwnerName();
        eOwnerName.setText(ownerName);

        // ex) 123456-1234567
        ownerRRN = data.getOwnerRRN();
        String[] splitRRN = ownerRRN.split("-");
        eOwnerRRNBefore.setText(splitRRN[0]);
        eOwnerRRNAfter.setText(splitRRN[1]);

        // ex) 010-1234-5678
        ownerHP = data.getOwnerHP();
        String[] splitHP = ownerHP.split("-");
        eOwnerHP1.setText(splitHP[0]);
        eOwnerHP2.setText(splitHP[1]);
        eOwnerHP3.setText(splitHP[2]);

        // ex) 302-765_대전시 유성구 궁동
        String[] addr1 = data.getOwnerAddress1().split("_");
        ownerPostCode = addr1[0];
        ownerDetailPostCode = addr1[1];
        String[] splitPostCode = ownerPostCode.split("-");
        eOwnerPostCode1.setText(splitPostCode[0]);
        eOwnerPostCode2.setText(splitPostCode[1]);
        eOwnerDetailPostCode.setText(ownerDetailPostCode);

        // ex) 302-765_대전시 유성구 궁동
        String[] addr2 = data.getOwnerAddress2().split("_");
        ownerRealPostCode = addr2[0];
        ownerRealDetailPostCode = addr2[1];
        String[] splitRealPostCode = ownerRealPostCode.split("-");
        eOwnerRealPostCode1.setText(splitRealPostCode[0]);
        eOwnerRealPostCode2.setText(splitRealPostCode[1]);
        eOwnerRealDetailPostCode.setText(ownerRealDetailPostCode);

        petName = data.getPetName();
        ePetName.setText(petName);
        petRace = data.getPetRace();
        ePetRace.setText(petRace);
        petColor = data.getPetColor();
        ePetColor.setText(petColor);

        // ex) 1996.01.30
        String[] birth = data.getPetBirth().split(".");
        petYear = birth[0];
        petMonth = birth[1];
        petDay = birth[2];
        sPetBirthYear.setSelection(getIndexOfSpinner(petYear, yearArray));
        sPetBirthMonth.setSelection(getIndexOfSpinner(petMonth, monthArray));
        sPetBirthDay.setSelection(getIndexOfSpinner(petDay, dayArray));

        // ex) 1996.01.30
        String[] getDate = data.getDate().split(".");
        petGetYear = getDate[0];
        petGetMonth = getDate[1];
        petGetDay = getDate[2];
        sPetGetYear.setSelection(getIndexOfSpinner(petGetYear, yearArray));
        sPetGetMonth.setSelection(getIndexOfSpinner(petGetMonth, monthArray));
        sPetGetDay.setSelection(getIndexOfSpinner(petGetDay, dayArray));

        key = data.getKey();

        // 0: default, 1: inner, 2: outer, 3: badge
        type = data.getType();
        if(type == 1)
            innerBtn.setImageResource(R.drawable.message_innerclick);
        else if(type == 2)
            outerBtn.setImageResource(R.drawable.message_outerclick);
        else if(type == 3)
            badgeBtn.setImageResource(R.drawable.message_badgeclick);

        // 0: default, 1: female, 2: male
        petGender = data.getPetGender();
        if(petGender == 1)
            ivPetFemale.setImageResource(R.drawable.message_petfemaleclick);
        else if(petGender == 2)
            ivPetMale.setImageResource(R.drawable.message_petmaleclick);

        // 0: default, 1: neutralization, 2: not neutralization
        petNeutralization = data.getPetNeut();
        if(petNeutralization == 1)
            ivPetNeutalization.setImageResource(R.drawable.message_petneutralizationclick);
        else if(petNeutralization == 2)
            ivPetNotNeutralization.setImageResource(R.drawable.message_petnotneutralizationclick);

    }

    /*
    Spinner에서 원하는 값의 index 반환
     */
    private int getIndexOfSpinner(String value, ArrayList<String> arrayList) {
        for(int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).equals(value))
                return i;
        }
        return 0;
    }

    /*
    Spinner 년, 월, 일 ArrayList 초기화
     */
    private void setDateArrayForSpinner() {
        // year
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearArray.add(" ");
        monthArray.add(" ");
        dayArray.add(" ");
        for(int i = year - 20; i <= year; i++) {
            yearArray.add(String.valueOf(i));
        }

        // month
        for(int i = 1; i <= 12; i++) {
            monthArray.add(String.valueOf(i));
        }

        // day
        for(int i = 1; i <= 31; i++) {
            dayArray.add(String.valueOf(i));
        }
    }

    @Override
    protected void onDestroy() {
        // messageAsyncTask.cancel(true); // 초기화
        super.onDestroy();
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
        // 공백 체크
        // 이 모든 editText중 하나라도 공백이 있는 경우 false
        if(!checkEditText(eOwnerName) || !checkEditText(eOwnerRRNBefore) || !checkEditText(eOwnerRRNAfter)
                || !checkEditText(eOwnerHP1) || !checkEditText(eOwnerHP2) || !checkEditText(eOwnerHP3)
                || !checkEditText(eOwnerPostCode1) || !checkEditText(eOwnerPostCode2) || !checkEditText(eOwnerDetailPostCode)
                || !checkEditText(eOwnerRealPostCode1) || !checkEditText(eOwnerRealPostCode2) || !checkEditText(eOwnerRealDetailPostCode)) {
            Toast.makeText(getApplicationContext(), "모든 소유주 정보 입력 칸을 채우세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            ownerRealDetailPostCode = eOwnerRealDetailPostCode.getText().toString();
            ownerRealPostCode = eOwnerRealPostCode1.getText().toString() + "-" + eOwnerRealPostCode2.getText().toString();
            ownerDetailPostCode = eOwnerDetailPostCode.getText().toString();
            ownerPostCode = eOwnerPostCode1.getText().toString() + "-" + eOwnerPostCode2.getText().toString();
            ownerAddress = ownerPostCode + "_" + ownerRealPostCode;
            ownerRealAddress = ownerRealPostCode + "_" + ownerRealDetailPostCode;
            ownerHP = eOwnerHP1.getText().toString() + "-" + eOwnerHP2.getText().toString() + "-" + eOwnerHP3.getText().toString();
            ownerRRN = eOwnerRRNBefore.getText().toString() + "-" + eOwnerRRNAfter.getText().toString();
            ownerName = eOwnerName.getText().toString();
            return true;
        }
    }

    /*
    반려동물 정보를 가져와서 변수에 저장하기 + 공백 처리
    올바르지 않는 값의 정보를 토스트로 출력
    @return : boolean(true : 전부 저장 완료. false : 하나라도 저장 실패)
     */
    private boolean setPetInfo() {
        // 공백 체크
        // 이 모든 editText중 하나라도 공백이 있는 경우 false
        if(!checkEditText(ePetName) || !checkEditText(ePetRace) || !checkEditText(ePetColor) || !checkEditText(ePetSpecialProblem)) {   // editText check
            Toast.makeText(getApplicationContext(), "반려동물 정보 입력 칸 중 공백이 있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!checkStringWS(petYear) || !checkStringWS(petMonth) || !checkStringWS(petDay) ||
                !checkStringWS(petGetYear) || !checkStringWS(petGetMonth) || !checkStringWS(petGetDay)) {  // spinner check
            Toast.makeText(getApplicationContext(), "반려동물 정보 입력 칸 중 선택하지 않은 날짜가 있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(petGender == 0 || petNeutralization == 0 || type == 0) { // imageView check
            Toast.makeText(getApplicationContext(), "반려동물 정보 입력 칸 중 선택하지 않은 이미지가 있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }

        // 값 설정 후 true 리턴
        petName = ePetName.getText().toString();
        petRace = ePetRace.getText().toString();
        petColor = ePetColor.getText().toString();
        petSpecialProblem = ePetSpecialProblem.getText().toString();
        petBirth = petYear + "." + petMonth + "." + petDay;
        petGetDate = petGetYear + "." + petGetMonth + "." + petGetDay;
        return true;
    }

    /*
    EditText의 값 중 공백 확인
    @return : boolean(true : 앞 뒤 공백 또는 전체 공백이 아님. false : 둘 중 하나라도 해당하는 경우)
     */
    private boolean checkEditText(EditText editText) {
        String editStr = editText.getText().toString();    // 검색어 임시 변수에 저장.
        if(TextUtils.isEmpty(editStr.trim())) { // 공백처리
            return false;
        }
        if(!editStr.equals(editStr.trim())) { // 앞뒤 공백이 존재하는 단어 입력
            return false;
        }
        return true;
    }

    /*
    String의 값 중 공백 확인
    @return : boolean(true : 앞 뒤 공백 또는 전체 공백이 아님. false : 둘 중 하나라도 해당하는 경우)
     */
    public static boolean checkStringWS(String string) {
        if(TextUtils.isEmpty(string.trim())) { // 공백처리
            return false;
        }
        if(!string.equals(string.trim())) { // 앞뒤 공백이 존재하는 단어 입력
            return false;
        }
        return true;
    }

    /*
    파일 저장하는 함수
     */
    private boolean saveMyReservationFile(JSONObject reservationObject) {
        String filename = "reservation.json";
        final String fileText = loadJSONFile(filename);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(filename, MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
            if(TextUtils.isEmpty(fileText)) { // 파일이 존재하지 않은 경우
                fileOutputStream.write(reservationObject.toString().getBytes());   // Json 쓰기
            }
            else {  // 기존에 저장된 파일 존재
                JSONArray jsonArray = new JSONArray(fileText);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getInt("HOSPITAL_KEY") == myReservationData.getKey()) { // 키 동일 체크
                        // Todo : 체크할 때 Key로만 판단하기에는 정보가 부족하다. 모든 값을 수정해서 예약보낼수 있기 때문에. 해결법 찾기
                        // jsonArray.remove(i);
                        jsonArray.put(i, jsonObject);   // 덮어씌우기
                        break;
                    }
                }
                fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    json 파일 불러와서 String으로 리턴하기
     */
    private String loadJSONFile(String filename) {
        Log.d(TAG, "loadJSONFIle start");
        String result = null;

        FileInputStream fileinputStream = null;
        try {
            fileinputStream = openFileInput(filename);
            int size = fileinputStream.available();
            byte[] buffer = new byte[size];
            fileinputStream.read(buffer);
            fileinputStream.close();
            result = new String(buffer, "UTF-8");
        } catch (FileNotFoundException e) {
            Log.d(TAG ,"사전에 등록증을 저장한 내역이 없습니다.");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    서버에 예약 정보를 POST 방식으로 ID에 요청하도록 전송하기
    AsyncTask 사용
     */
    private class MessageAsyncTask extends AsyncTask<String, Void, String> {
        JSONObject jsonObject = new JSONObject();
        @Override
        protected String doInBackground(String... strings) {
            String search_url = SERVER_URL + strings[0];    // URL
            // 서버에 메세지 정보 전송
            try {
                // String type, ownerName, address, hp, petName, race, petColor, petBirth, neutralization, petGender;
                // Message에 담은 모든 정보 JSONObject에 담기
                jsonObject.accumulate("HOSPITAL_KEY", key); // key JSONObject에 담기
                jsonObject.accumulate("TYPE", type); // type JSONObject에 담기
                jsonObject.accumulate("OWNER_NAME", ownerName);
                jsonObject.accumulate("OWNER_RESIDENT", ownerRRN);
                jsonObject.accumulate("OWNER_PHONE_NUMBER", ownerHP);
                jsonObject.accumulate("OWNER_ADDRESS1", ownerAddress);
                jsonObject.accumulate("OWNER_ADDRESS2", ownerRealAddress);
                jsonObject.accumulate("PET_NAME", petName);
                jsonObject.accumulate("PET_VARIETY", petRace);
                jsonObject.accumulate("PET_COLOR", petColor);
                jsonObject.accumulate("PET_GENDER", petGender);
                jsonObject.accumulate("PET_NEUTRALIZATION", petNeutralization);
                jsonObject.accumulate("PET_BIRTH", petBirth);
                jsonObject.accumulate("ASK_DATE", petGetDate);
                jsonObject.accumulate("ETC", petSpecialProblem);

                // POST 전송방식을 위한 설정
                HttpURLConnection con = null;
                BufferedReader reader = null;

                Log.d(TAG, "url : " + search_url);
                Log.d(TAG, "type : " + type);
                URL url = new URL(search_url);  // URL 객체 생성
                Log.d(TAG, "jsonObject String : " + jsonObject.toString());
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10 * 1000);   // 서버 접속 시 연결 시간
                con.setReadTimeout(10 * 1000);   // Read시 연결 시간
                con.setRequestMethod("POST"); // POST방식 설정
                con.setRequestProperty("Content-Type", "application/json"); // application JSON 형식으로 전송
                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                con.setRequestProperty("Accept", "text/json"); // 서버에 response 데이터를 html로 받음 -> JSON 또는 xml
                con.setDoOutput(true); // Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true); // Inputstream으로 서버로부터 응답을 받겠다는 의미

                // 서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                // 버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());    // searchword : 검색키워드 식으로 전송
                writer.flush();
                writer.close(); // 버퍼를 받아줌

                con.connect();

                // 응답 코드 구분
                int responseCode = con.getResponseCode();   // 응답 코드 설정
                if(responseCode == HttpURLConnection.HTTP_OK)  // 200 정상 연결
                //서버로 부터 데이터를 받음
                {
                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line;    // 한 줄씩 읽어오기 위한 임시 String 변수
                    while((line = reader.readLine()) != null){
                        buffer.append(line); // buffer에 데이터 저장
                    }
                    return buffer.toString(); //서버로 부터 받은 값을 리턴해줌
                }
                else {  // 연결이 잘 안됨
                    printConnectionError(con);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject = StringToJSON(result);
            try {
                if(jsonObject.getInt("result") == 1) {
                    if(saveMyReservationFile(jsonObject)) {
                        Toast.makeText(getApplicationContext(), "예약 성공! 저장 성공!", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "예약 성공! 저장 실패!", Toast.LENGTH_LONG).show();
                        setResult(RESULT_CANCELED);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "예약 실패! 잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SELECT_RESERVATION:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "팝업창에서 확인 누름!");
                    messageAsyncTask = new MessageAsyncTask();
                    messageAsyncTask.execute("/sendMessage");
                }
                else {
                    Log.d(TAG, "팝업창에서 취소 누름!");
                }
                break;
            default:
                break;
        }
    }

    /*
    Spinner Item Select Listener 처리하는 클래스
     */
    class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner) parent;
            switch (spinner.getId()) {
                case R.id.petBirthYearSpinner:
                    petYear = parent.getItemAtPosition(position).toString();
                    break;
                case R.id.petBirthMonthSpinner:
                    petMonth = aaMonth.getItem(position).toString();
                    break;
                case R.id.petBirthDaySpinner:
                    petDay = aaDay.getItem(position).toString();
                    break;
                case R.id.petGetYearSpinner:
                    petGetYear = aaGetYear.getItem(position).toString();
                    break;
                case R.id.petGetMonthSpinner:
                    petGetMonth = aaGetMonth.getItem(position).toString();
                    break;
                case R.id.petGetDaySpinner:
                    petGetDay = aaGetDay.getItem(position).toString();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /*
    ImageView Click Listener 처리하는 클래스
     */
    class ImageViewClickListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
           if(event.getAction() == MotionEvent.ACTION_DOWN) {
               switch(v.getId()) {
                   case R.id.searchPostCodeBtn:
                       // 우편번호 API 실행
                       break;
                   case R.id.searchRealPostCodeBtn:
                       // 우편번호 API 실행
                       break;
                   case R.id.petFemale:
                       if(petGender == 1) { // female 선택 중인 경우
                           ivPetFemale.setImageResource(R.drawable.message_petfemale);
                           petGender = 0;
                       }
                       else {
                           ivPetFemale.setImageResource(R.drawable.message_petfemaleclick);
                           ivPetMale.setImageResource(R.drawable.message_petmale);
                           petGender = 1;
                       }
                       break;
                   case R.id.petMale:
                       if(petGender == 2) { // male 선택 중인 경우
                           ivPetMale.setImageResource(R.drawable.message_petmale);
                           petGender = 0;
                       }
                       else {
                           ivPetFemale.setImageResource(R.drawable.message_petfemale);
                           ivPetMale.setImageResource(R.drawable.message_petmaleclick);
                           petGender = 2;
                       }
                       break;
                   case R.id.petNeutralization:
                       if(petNeutralization == 1) { // neutralization 선택 중인 경우
                           ivPetNeutalization.setImageResource(R.drawable.message_petneutralization);
                           petNeutralization = 0;
                       }
                       else {
                           ivPetNeutalization.setImageResource(R.drawable.message_petneutralizationclick);
                           ivPetNotNeutralization.setImageResource(R.drawable.message_petnotneutralization);
                           petNeutralization = 1;
                       }
                       break;
                   case R.id.petNotNeutralization:
                       if(petNeutralization == 2) { // neutralization 선택 중인 경우
                           ivPetNotNeutralization.setImageResource(R.drawable.message_petnotneutralization);
                           petNeutralization = 0;
                       }
                       else {
                           ivPetNeutalization.setImageResource(R.drawable.message_petneutralization);
                           ivPetNotNeutralization.setImageResource(R.drawable.message_petnotneutralizationclick);
                           petNeutralization = 2;
                       }
                       break;
                   case R.id.innerBtn:
                       if(type == 1) { // neutralization 선택 중인 경우
                           innerBtn.setImageResource(R.drawable.message_inner);
                           type = 0;
                       }
                       else {
                           innerBtn.setImageResource(R.drawable.message_innerclick);
                           outerBtn.setImageResource(R.drawable.message_outer);
                           badgeBtn.setImageResource(R.drawable.message_badge);
                           type = 1;
                       }
                       break;
                   case R.id.outerBtn:
                       if(type == 2) { // neutralization 선택 중인 경우
                           outerBtn.setImageResource(R.drawable.message_outer);
                           type = 0;
                       }
                       else {
                           outerBtn.setImageResource(R.drawable.message_outerclick);
                           innerBtn.setImageResource(R.drawable.message_inner);
                           badgeBtn.setImageResource(R.drawable.message_badge);
                           type = 2;
                       }
                       break;
                   case R.id.badgeBtn:
                       if(type == 3) { // neutralization 선택 중인 경우
                           badgeBtn.setImageResource(R.drawable.message_outer);
                           type = 0;
                       }
                       else {
                           badgeBtn.setImageResource(R.drawable.message_badgeclick);
                           innerBtn.setImageResource(R.drawable.message_inner);
                           outerBtn.setImageResource(R.drawable.message_outer);
                           type = 3;
                       }
                       break;
                   case R.id.reservationBtn:
                       if(setOwnerInfo() && setPetInfo()) {
                           Intent intent = new Intent(getApplicationContext(), MessagePopupActivity.class);
                           startActivityForResult(intent, SELECT_RESERVATION);
                       }
                       break;
               }
           }
           return true;
        }
    }
}