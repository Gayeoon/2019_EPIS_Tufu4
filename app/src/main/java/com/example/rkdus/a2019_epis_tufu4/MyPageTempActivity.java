package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.rkdus.a2019_epis_tufu4.SearchActivity.StringToJSON;

/*
마이페이지 액티비티
- 이해원
 */
public class MyPageTempActivity extends BaseActivity {
    private static final String TAG = "LogGoGo";
    public static final int CHECK_RESERVATION = 1000;

    EditText eRegistNum, eOwnerName, eOwnerHP, eOwnerAddress, ePetName, ePetRace, ePetColor, ePetBirth, ePetGender, ePetNeut;
    TextView tvRegistNum, tvOwnerName, tvOwnerHP, tvOwnerAddress, tvPetName, tvPetRace, tvPetColor, tvPetBirth, tvPetGender, tvPetNeut;
    TextView tvYear, tvMonth, tvDay;
    ImageView rewriteBtn, ivCard;
    RecyclerView myReservationRecycler;

    // 두 배열의 크기와 순서쌍은 같다고 정의.
    EditText[] editTexts;
    TextView[] textViews;
    ArrayList<MyReservationData> reservationList = new ArrayList<>();
    MyReservationListAdapter adapter;
    boolean isRewrite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_temp);

        // 뷰 정의
        eRegistNum = (EditText) findViewById(R.id.registNumEditText);
        eOwnerName = (EditText) findViewById(R.id.ownerNameEditText);
        eOwnerHP = (EditText) findViewById(R.id.ownerHPEditText);
        eOwnerAddress = (EditText) findViewById(R.id.ownerAddressEditText);
        ePetName = (EditText) findViewById(R.id.petNameEditText);
        ePetRace = (EditText) findViewById(R.id.petRaceEditText);
        ePetColor = (EditText) findViewById(R.id.petColorEditText);
        ePetBirth = (EditText) findViewById(R.id.petBirthEditText);
        ePetGender = (EditText) findViewById(R.id.petGenderEditText);
        ePetNeut = (EditText) findViewById(R.id.petNeutralizationEditText);

        tvRegistNum = (TextView) findViewById(R.id.registNumText);
        tvOwnerName = (TextView) findViewById(R.id.ownerNameText);
        tvOwnerHP = (TextView) findViewById(R.id.ownerAddressText);
        tvOwnerAddress = (TextView) findViewById(R.id.ownerAddressText);
        tvPetName = (TextView) findViewById(R.id.petNameText);
        tvPetRace = (TextView) findViewById(R.id.petRaceText);
        tvPetColor = (TextView) findViewById(R.id.petColorText);
        tvPetBirth = (TextView) findViewById(R.id.petBirthText);
        tvPetGender = (TextView) findViewById(R.id.petGenderText);
        tvPetNeut = (TextView) findViewById(R.id.petNeutralizationText);

        tvYear = (TextView) findViewById(R.id.registYearText);
        tvMonth = (TextView) findViewById(R.id.registMonthText);
        tvDay = (TextView) findViewById(R.id.registDayText);

        rewriteBtn = (ImageView) findViewById(R.id.rewriteBtn);
        ivCard = (ImageView) findViewById(R.id.registrationCardImage);
        myReservationRecycler = (RecyclerView) findViewById(R.id.myReservationRecyclerView);

        editTexts = new EditText[]{eRegistNum, eOwnerName, eOwnerHP, eOwnerAddress, ePetName,
                ePetRace, ePetColor, ePetBirth, ePetGender, ePetNeut};

        textViews = new TextView[]{tvRegistNum, tvOwnerName, tvOwnerHP, tvOwnerAddress, tvPetName,
                tvPetRace, tvPetColor, tvPetBirth, tvPetGender, tvPetNeut};

        String registrationInfo = loadJSONFile("registration");
        if(!TextUtils.isEmpty(registrationInfo)) { // 이전에 동물등록증을 저장해놨던 경우
            JSONObject registrationObject = StringToJSON(registrationInfo);
            printSavedTextInTextView(registrationObject);
        }

        refreshMyReservation();

        // 수정 버튼 클릭 이벤트
        rewriteBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        if(isRewrite) {
                            isRewrite = false;
                            Toast.makeText(getApplicationContext(), "편집을 완료합니다.", Toast.LENGTH_LONG).show();
                            rewriteBtn.setImageResource(R.drawable.mypage_startrewritebtn);  // 이미지 변경
                            ivCard.setImageResource(R.drawable.mypage_cardsuccess);
                            setNewTextInTextView();
                            saveFileToMyRegistration();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "수정을 시작합니다.", Toast.LENGTH_LONG).show();
                            rewriteBtn.setImageResource(R.drawable.mypage_startsavebtn);  // 이미지 변경
                            ivCard.setImageResource(R.drawable.mypage_cardrewrite);
                            isRewrite = true;
                            setCurrentTextInEditText();
                        }
                        setVisibleView(isRewrite);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
    }

    /*
    MyReservation RecyclerView 출력 또는 갱신.
     */
    private void refreshMyReservation() {
        String myReservation = loadJSONFile("reservation");
        if(!TextUtils.isEmpty(myReservation)) { // 지금까지 예약한 정보가 담겨진 파일 불러오기
            setReservationListFromStr(myReservation);
            showRecyclerView(reservationList);
        }
    }
    /*
    JSONArray 방식의 String을 ArrayList로 변환.
     */
    private void setReservationListFromStr(String myReservation) {
        try {
            JSONArray reservationArray = new JSONArray(myReservation);
            // Gson사용. JSONArray to ArrayList
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<MyReservationData>>(){}.getType();
            reservationList = gson.fromJson(reservationArray.toString(), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    RecyclerView setting and print 함수
     */
    private void showRecyclerView(ArrayList<MyReservationData> arrayList) {

        final ArrayList<MyReservationData> result = arrayList;
        Log.d(TAG, "reservation ArrayList size : " + result.size());
        // UI 작업을 위한 쓰레드 실행
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        myReservationRecycler.setLayoutManager(linearLayoutManager);
                        adapter = new MyReservationListAdapter(result, getApplicationContext());
                        adapter.resetAll(result);
                        myReservationRecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        // RecyclerView 클릭 이벤트 초기화
                        setRecyclerViewItemClick(result, adapter);
                    }
                });
            }
        });
    }

    /*
    RecyclerView Item 개별 클릭 리스너 설정하는 함수
     */
    private void setRecyclerViewItemClick(final ArrayList<MyReservationData> result, MyReservationListAdapter myListAdapter) {
        myListAdapter.setItemClick(new MyReservationListAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                //해당 위치의 Data get
                MyReservationData resultData = result.get(position);
                Toast.makeText(getApplicationContext(),
                        "병원 이름, 병원 타입 :  (" + resultData.getHospitalName() + ", " + resultData.getTypeToStr(resultData.getType()) + ")",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
    EditText의 값 중 공백 확인해서
    공백이면 "-"를,
    앞 뒤 공백이 있으면 공백 제거를,
    공백이 없으면 입력한 문자열을 리턴
    @return : String(WS인 경우 : "-", else : 공백 제거한 문자열)
     */
    private String getRemoveWSTextOfEditText(EditText editText) {
        String editStr = editText.getText().toString();    // 검색어 임시 변수에 저장.
        if(TextUtils.isEmpty(editStr.trim())) { // 공백처리
            return "-";
        }
        else
            return editStr.trim();
    }

    /*
    수정이 완료되었을 때 TextView에 옮겨담는 함수
     */
    private void setNewTextInTextView() {
        for (int i = 0; i < editTexts.length; i++) {
            textViews[i].setText(getRemoveWSTextOfEditText(editTexts[i]));
        }
    }

    /*
    수정이 완료되었을 때 EditText에 옮겨담는 함수
     */
    private void setCurrentTextInEditText() {
        for (int i = 0; i < textViews.length; i++) {
            editTexts[i].setText(textViews[i].getText().toString());
        }
    }

    /*
    저장했던 파일을 불러온 Json 객체를 가지고 TextView에 옮겨담는 함수
     */
    private void printSavedTextInTextView(JSONObject jsonObject) {
        try {
            tvRegistNum.setText(jsonObject.getString("registNum"));
            tvOwnerName.setText(jsonObject.getString("ownerName"));
            tvOwnerHP.setText(jsonObject.getString("ownerHP"));
            tvOwnerAddress.setText(jsonObject.getString("ownerAddress"));
            tvPetName.setText(jsonObject.getString("petName"));
            tvPetRace.setText(jsonObject.getString("petRace"));
            tvPetColor.setText(jsonObject.getString("petColor"));
            tvPetBirth.setText(jsonObject.getString("petBirth"));
            tvPetGender.setText(jsonObject.getString("petGender"));
            tvPetNeut.setText(jsonObject.getString("petNeut"));
        } catch (JSONException e) {
            Log.d(TAG, "JSONObject 내용물 중 null이 있네.");
            e.printStackTrace();
        }
    }

    /*
    json 파일 불러와서 String으로 리턴하기
     */
    private String loadJSONFile(String filename) {
        Log.d(TAG, "loadJSONFIle start");

        String result = null;
        filename += ".json";

        FileInputStream fileinputStream = null;
        try {
            Log.d(TAG, "file name : " + filename);
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
    받아온 String 값 Json 파일로 임시 저장
     */
    private boolean JSONObjTofile(JSONObject jsonObject, String filename) {
        Log.d(TAG, "JSONObjTofile start");

        filename += ".json";
        // 파일 생성(덮어쓰기)
        FileOutputStream fileOutputStream = null;
        try {
            Log.d(TAG, "file name : " + filename);
            fileOutputStream = openFileOutput(filename, MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
            fileOutputStream.write(jsonObject.toString().getBytes());   // Json 쓰기
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    기록한 동물 등록증을 파일에 저장
     */
    private void saveFileToMyRegistration() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("registNum", tvRegistNum.getText().toString());
            jsonObject.accumulate("ownerName", tvOwnerName.getText().toString());
            jsonObject.accumulate("ownerHP", tvOwnerHP.getText().toString());
            jsonObject.accumulate("ownerAddress", tvOwnerAddress.getText().toString());
            jsonObject.accumulate("petName", tvPetName.getText().toString());
            jsonObject.accumulate("petRace", tvPetRace.getText().toString());
            jsonObject.accumulate("petColor", tvPetColor.getText().toString());
            jsonObject.accumulate("petBirth", tvPetBirth.getText().toString());
            jsonObject.accumulate("petGender", tvPetGender.getText().toString());
            jsonObject.accumulate("petNeut", tvPetNeut.getText().toString());

            if(JSONObjTofile(jsonObject, "registration")) {
                Log.d(TAG, "JSONObjTofile save success");
            }
            else
                Log.d(TAG, "JSONObjTofile failed");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
    EditText 또는 TextView 전체를 visible, gone 시키는 함수
    수정 버튼을 눌렀을 경우에 호출됨
    @param : boolean(true: EditText visible, TextView gone.    false: EditText gone, TextView visible)
     */
    private void setVisibleView(boolean visibleEditText) {
        // EditText Setting
        for(int i = 0; i < editTexts.length; i++) {
            editTexts[i].setVisibility(getVisibleResource(visibleEditText));
        }

        // TextView Setting
        for(int i = 0; i < textViews.length; i++) {
            textViews[i].setVisibility(getVisibleResource(!visibleEditText));
        }
    }

    private int getVisibleResource(boolean visible) {
        if(visible)
            return View.VISIBLE;
        else
            return View.GONE;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CHECK_RESERVATION:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "예약 수정 성공!");
                    refreshMyReservation();
                }
                else {
                    Log.d(TAG, "예약 수정하고 파일 저장 실패!");
                }
                break;
            default:
                break;
        }
    }
}
