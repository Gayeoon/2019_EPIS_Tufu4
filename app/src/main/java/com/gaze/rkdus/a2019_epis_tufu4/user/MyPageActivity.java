package com.gaze.rkdus.a2019_epis_tufu4.user;

import android.content.Intent;
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

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.MyReservationListAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationAllData;
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationData;
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationListData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.gaze.rkdus.a2019_epis_tufu4.user.SearchActivity.StringToJSON;

/*
마이페이지 액티비티
- 이해원
 */
public class MyPageActivity extends BaseActivity {
    public static final int CHECK_RESERVATION = 1000;
    public static final int CHECK_REGISTCONFIRM = 1001;
    public static final int CHECK_ADDREVIEW = 2000;

    EditText eRegistNum, eOwnerName, eOwnerHP, eOwnerAddress, ePetName, ePetRace, ePetColor, ePetBirth, ePetGender, ePetNeut;
    TextView tvRegistNum, tvOwnerName, tvOwnerHP, tvOwnerAddress, tvPetName, tvPetRace, tvPetColor, tvPetBirth, tvPetGender, tvPetNeut;
    TextView tvYear, tvMonth, tvDay, tvCenterName;
    EditText eYear, eMonth, eDay, eCenterName;

    ImageView rewriteBtn, ivCard;
    RecyclerView myReservationRecycler;

    // 두 배열의 크기와 순서쌍은 같다고 정의.
    EditText[] editTexts;
    TextView[] textViews;
    ArrayList<MyReservationAllData> reservationList = new ArrayList<>();
    MyReservationListAdapter adapter;
    boolean isRewrite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

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
        tvOwnerHP = (TextView) findViewById(R.id.ownerHPText);
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
        tvCenterName = (TextView) findViewById(R.id.centerNameText);

        eYear = (EditText) findViewById(R.id.registYearEditText);
        eMonth = (EditText) findViewById(R.id.registMonthEditText);
        eDay = (EditText) findViewById(R.id.registDayEditText);
        eCenterName = (EditText) findViewById(R.id.centerNameEditText);

        rewriteBtn = (ImageView) findViewById(R.id.rewriteBtn);
        ivCard = (ImageView) findViewById(R.id.registrationCardImage);

        myReservationRecycler = (RecyclerView) findViewById(R.id.myReservationRecyclerView);

        editTexts = new EditText[]{eRegistNum, eOwnerName, eOwnerHP, eOwnerAddress, ePetName,
                ePetRace, ePetColor, ePetBirth, ePetGender, ePetNeut, eYear, eMonth, eDay, eCenterName};

        textViews = new TextView[]{tvRegistNum, tvOwnerName, tvOwnerHP, tvOwnerAddress, tvPetName,
                tvPetRace, tvPetColor, tvPetBirth, tvPetGender, tvPetNeut, tvYear, tvMonth, tvDay, tvCenterName};

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
                            Toast.makeText(getApplicationContext(), "편집을 완료합니다.", Toast.LENGTH_SHORT).show();
                            rewriteBtn.setImageResource(R.drawable.mypage_startrewritebtn);  // 이미지 변경
                            ivCard.setImageResource(R.drawable.mypage_cardsuccess);
                            setNewTextInTextView();
                            saveFileToMyRegistration();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "수정을 시작합니다.", Toast.LENGTH_SHORT).show();
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

//    /*
//    MyReservation RecyclerView 출력 또는 갱신.
//     */
//    public void refreshMyReservation() {
//        String myReservation = loadJSONFile("reservation");
//        if(!TextUtils.isEmpty(myReservation)) { // 지금까지 예약한 정보가 담겨진 파일 불러오기
//            Log.d(TAG, "string :  " + myReservation);
//            setReservationListFromStr(myReservation);
//            showRecyclerView(reservationList);
//        }
//    }
//    /*
//    JSONArray 방식의 String을 ArrayList로 변환.
//     */
//    private void setReservationListFromStr(String myReservation) {
//        try {
//            JSONArray reservationArray = new JSONArray(myReservation);
//            // Gson사용. JSONArray to ArrayList
//            Log.d(TAG, "GSON사용전 :  " + myReservation);
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<MyReservationData>>(){}.getType();
//            reservationList = gson.fromJson(reservationArray.toString(), listType);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /*
    MyReservation RecyclerView 출력 또는 갱신.
     */
    public void refreshMyReservation() {
        String myReservation = loadJSONFile("myReservation");
        if(!TextUtils.isEmpty(myReservation)) { // 지금까지 예약한 정보가 담겨진 파일 불러오기
            Log.d(TAG, "string :  " + myReservation);
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
            Log.d(TAG, "GSON사용전 :  " + myReservation);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<MyReservationAllData>>(){}.getType();
            reservationList = gson.fromJson(reservationArray.toString(), listType);
            Log.d(TAG, "GSON사용후 :  " + reservationArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    RecyclerView setting and print 함수
     */
    private void showRecyclerView(ArrayList<MyReservationAllData> arrayList) {

        final ArrayList<MyReservationAllData> result = arrayList;
        if(!result.isEmpty()) {
            Log.d(TAG, "비지 않음");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        myReservationRecycler.setLayoutManager(linearLayoutManager);
        adapter = new MyReservationListAdapter(result, getApplicationContext());
        adapter.resetAll(result);
        myReservationRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // RecyclerView 클릭 이벤트 초기화
        setRecyclerViewItemClick(result, adapter);

        // UI 작업을 위한 쓰레드 실행
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(!result.isEmpty()) {
//                            Log.d(TAG, "이거예약함 : " + result.get(0).getHOSPITAL_NAME());
//                        }
//                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                        myReservationRecycler.setLayoutManager(linearLayoutManager);
//                        adapter = new MyReservationListAdapter(result, getApplicationContext());
//                        adapter.resetAll(result);
//                        myReservationRecycler.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//
//                        // RecyclerView 클릭 이벤트 초기화
//                        setRecyclerViewItemClick(result, adapter);
//                    }
//                });
//            }
//        });
    }

    /*
    RecyclerView Item 개별 클릭 리스너 설정하는 함수
     */
    private void setRecyclerViewItemClick(final ArrayList<MyReservationAllData> result, MyReservationListAdapter myListAdapter) {
        myListAdapter.setItemClick(new MyReservationListAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                //해당 위치의 Data get
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
            Log.d(TAG, "result : " + getRemoveWSTextOfEditText(editTexts[i]));
            textViews[i].setText(getRemoveWSTextOfEditText(editTexts[i]));
        }
    }

    /*
    수정이 완료되었을 때 EditText에 옮겨담는 함수
     */
    private void setCurrentTextInEditText() {
        for (int i = 0; i < textViews.length; i++) {
            String text = textViews[i].getText().toString();
            if(text.equals("-"))
                editTexts[i].setText("");
            else
                editTexts[i].setText(text);
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
            tvYear.setText(jsonObject.getString("year"));
            tvMonth.setText(jsonObject.getString("month"));
            tvDay.setText(jsonObject.getString("day"));
            tvCenterName.setText(jsonObject.getString("centerName"));

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
            Log.d(TAG, "loadJSONFile 결과 " + result);
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
            jsonObject.accumulate("year", tvYear.getText().toString());
            jsonObject.accumulate("month", tvMonth.getText().toString());
            jsonObject.accumulate("day", tvDay.getText().toString());
            jsonObject.accumulate("centerName", tvCenterName.getText().toString());

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

    /*
    파일 수정하는 함수
    여기서는 RESERVATION_STATE 수정
    isDelete : true(삭제), false(수정)
     */
    public boolean rewriteMyReservationFile(MyReservationData rewriteData, boolean isDelete) {
        String filename = "reservation";
        final String fileText = loadJSONFile(filename);
        if(TextUtils.isEmpty(fileText)) { // 파일이 존재하지 않은 경우
            Log.d(TAG, "수정하려 하는데 파일이 없는 경우");
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(filename + ".json", MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
            // 기존에 저장된 파일 존재
            JSONArray jsonArray = new JSONArray(fileText);
            Log.d(TAG, "기존에 저장된 파일 존재");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("ASK_DATE").equals(rewriteData.getAsk_date())) {   // 예약 날짜 동일 체크
                    if(jsonObject.getInt("HOSPITAL_KEY") == rewriteData.getHospital_key()) { // 키 동일 체크
                        Log.d(TAG, "같은 객체 찾음!");
                        if (isDelete)
                            jsonArray.remove(i); // 삭제
                        else {
                            Gson gson = new Gson();
                            String rewriteJSONStr = gson.toJson(rewriteData);
                            JSONObject rewriteJSONObj = StringToJSON(rewriteJSONStr);
                            Log.d(TAG, "result ::: " + rewriteJSONObj);
                            jsonArray.put(i, rewriteJSONObj); // 덮어씌우기
                        }
                        fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
                        fileOutputStream.flush();
                        return true;
                    }
                }
            }
            fileOutputStream.flush();
            return false;
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
    파일 수정하는 함수
    여기서는 RESERVATION_STATE 수정
    isDelete : true(삭제), false(수정)
     */
    public boolean rewriteMyReservationFile(MyReservationListData rewriteData, boolean isDelete) {
        String filename = "myReservation";
        final String fileText = loadJSONFile(filename);
        if(TextUtils.isEmpty(fileText)) { // 파일이 존재하지 않은 경우
            Log.d(TAG, "수정하려 하는데 파일이 없는 경우");
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(filename + ".json", MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
            // 기존에 저장된 파일 존재
            JSONArray jsonArray = new JSONArray(fileText);
            Log.d(TAG, "기존에 저장된 파일 존재");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i); // {listData : ~, reservationData : ~}
                JSONObject listData = (JSONObject) jsonObject.get("listData");
                if(listData.getString("reservation_date").equals(rewriteData.getReservation_date())) {   // 예약 날짜 동일 체크
                    if(listData.getInt("hospital_key") == rewriteData.getHospital_key()) { // 키 동일 체크
                        Log.d(TAG, "같은 객체 찾음!");
                        if (isDelete)
                            jsonArray.remove(i); // 삭제
                        else {
//                            Gson gson = new Gson();
//                            String rewriteJSONStr = gson.toJson(rewriteData);
//                            JSONObject rewriteJSONObj = StringToJSON(rewriteJSONStr);
//                            Log.d(TAG, "result ::: " + rewriteJSONObj);
//                            jsonArray.put(i, rewriteJSONObj); // 덮어씌우기
                                JSONObject newListData = rewriteData.getJSONObj();
                                jsonObject.remove("listData");
                                jsonObject.accumulate("listData", newListData);
                                jsonArray.put(i, jsonObject);
                        }
                        fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
                        fileOutputStream.flush();
                        return true;
                    }
                }
            }
            fileOutputStream.flush();
            return false;
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
            case CHECK_REGISTCONFIRM:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "등록 확정 OK");
                    if(intent.hasExtra("data")) {
                        if(rewriteMyReservationFile((MyReservationData) intent.getSerializableExtra("data"), false))
                            refreshMyReservation();
                        else
                            Toast.makeText(getApplicationContext(), "등록 상태 변경 중 오류 발생! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "등록 확정 실패! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "등록 확정 실패! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "등록 확정 CANCEL");
                }
                break;
            case CHECK_ADDREVIEW:
                if (resultCode == RESULT_OK) {
                    if(intent.hasExtra("data")) {
                        if(rewriteMyReservationFile((MyReservationData) intent.getSerializableExtra("data"), true))
                            refreshMyReservation();
                        else
                            Toast.makeText(getApplicationContext(), "삭제 중 오류 발생! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
}
