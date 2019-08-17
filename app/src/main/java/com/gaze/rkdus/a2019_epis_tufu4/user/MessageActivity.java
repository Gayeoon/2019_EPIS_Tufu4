package com.gaze.rkdus.a2019_epis_tufu4.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static com.gaze.rkdus.a2019_epis_tufu4.user.SearchActivity.StringToJSON;
import static com.gaze.rkdus.a2019_epis_tufu4.user.SearchActivity.printConnectionError;

import com.airbnb.lottie.L;
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.MessageSpinnerAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationData;
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationListData;
import com.gaze.rkdus.a2019_epis_tufu4.item.PostCodeItem;
import com.gaze.rkdus.a2019_epis_tufu4.popup.ImageTextPopupActivity;
import com.gaze.rkdus.a2019_epis_tufu4.popup.IndividualInfoPopupActivity;
import com.gaze.rkdus.a2019_epis_tufu4.popup.MessagePopupActivity;
import com.gaze.rkdus.a2019_epis_tufu4.popup.PostCodePopupActivity;
import com.gaze.rkdus.a2019_epis_tufu4.popup.ProductPopupActivity;
import com.gaze.rkdus.a2019_epis_tufu4.popup.ProxySignPopupActivity;
import com.google.gson.Gson;

/*
 * 사용자
 * 예약에 필요한 정보 작성하고 예약하는 액티비티
 * * 내장형과 외장형을 따로 저장해놓고 진행
 * - 이해원
 */
public class MessageActivity extends BaseActivity {

    private static final int SELECT_RESERVATION = 10;
    private static final int CHECK_INDIVIDUALINFO = 111;
    private static final int SEARCH_POSTCODE = 100;
    private static final int SEARCH_REALPOSTCODE = 101;
    private static final int CHECK_PROXYSIGN = 120;
    private static final int CHECK_IMMEDIATELYBUY = 130;
    private static final int CHECK_NEUTRALIZATIONSURGERY = 140;
    private final int SPINNER_HEIGHT = 350;

    MyReservationListData reservationListData;
    int key;
    String hospitalName;
    String ownerName, ownerRRN, ownerHP, ownerPostCode, ownerPost, ownerDetailPostCode, ownerRealPostCode, ownerRealPost, ownerRealDetailPostCode;    // 소유주
    String ownerAddress, ownerRealAddress;
    String petName, petRace, petColor, petBirth, petGetDate, petSpecialProblem;
    String petYear, petMonth, petDay, petGetYear, petGetMonth, petGetDay;
    String askDateOld;
    int petGender;  // 0: default,  1: female,  2: male
    int petNeutralization;  // 0: default,  1: neutralization,  2: not neutralization
    int type;   // 0: default,  1: inner,  2: outer,  3: badge
    boolean checkReservation = false;
    int neutralizationSurgery = 0;  // 0 : 안함,  1 : 함.

    TextView individualInfoText, proxySignText, immediatelyBuyText, tvOwnerPostCode, tvOwnerRealPostCode, tvOwnerPost, tvOwnerRealPost;
    EditText eOwnerName, eOwnerRRNBefore, eOwnerRRNAfter; // 이름 및 주민등록번호
    EditText eOwnerHP1, eOwnerHP2, eOwnerHP3;   // 전화번호
    EditText eOwnerDetailPostCode, eOwnerRealDetailPostCode; // 전화번호, 우편번호, 상세주소, 실제주소
    EditText ePetName, ePetRace, ePetColor, ePetSpecialProblem; // 애완동물 이름, 인종, 색깔, 특이사항
    CheckBox cbMatchedPostCode, cbCheckIndividualInfo, cbProxySign, cbImmediatelyBuy, cbNeutralizationSurgery;

    LinearLayout neutralizationLayout;
    ImageView searchPostCodeBtn, searchRealPostCodeBtn, ivPetFemale, ivPetMale, ivPetNeutalization, ivPetNotNeutralization;
    ImageView innerBtn, outerBtn, badgeBtn, reservationBtn, rewriteBtn; // 등록방법, 예약버튼
    Spinner sPetBirthYear, sPetBirthMonth, sPetBirthDay, sPetGetYear, sPetGetMonth, sPetGetDay;

    ArrayList<String> yearArray = new ArrayList<>();
    ArrayList<String> monthArray = new ArrayList<>();
    ArrayList<String> dayArray = new ArrayList<>();
    MessageSpinnerAdapter aaYear, aaMonth, aaDay, aaGetYear, aaGetMonth, aaGetDay;
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
        tvOwnerPostCode = (TextView) findViewById(R.id.ownerPostCode);
        tvOwnerPost = (TextView) findViewById(R.id.ownerPost);
        eOwnerDetailPostCode = (EditText) findViewById(R.id.ownerDetailPostCode);
        tvOwnerRealPostCode = (TextView) findViewById(R.id.ownerRealPostCode);
        tvOwnerRealPost = (TextView) findViewById(R.id.ownerRealPost);
        eOwnerRealDetailPostCode = (EditText) findViewById(R.id.ownerRealDetailPostCode);
        cbMatchedPostCode = (CheckBox) findViewById(R.id.matchedPostCodeBox);
        cbCheckIndividualInfo = (CheckBox) findViewById(R.id.checkIndividualInfo);
        cbProxySign = (CheckBox) findViewById(R.id.checkProxySign);
        cbImmediatelyBuy = (CheckBox) findViewById(R.id.checkImmediatelyBuy);
        cbNeutralizationSurgery = (CheckBox) findViewById(R.id.checkNeutralizationCheckBox);

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
        rewriteBtn = (ImageView) findViewById(R.id.messageRewriteBtn);
        sPetBirthYear = (Spinner) findViewById(R.id.petBirthYearSpinner);
        sPetBirthMonth = (Spinner) findViewById(R.id.petBirthMonthSpinner);
        sPetBirthDay = (Spinner) findViewById(R.id.petBirthDaySpinner);
        sPetGetYear = (Spinner) findViewById(R.id.petGetYearSpinner);
        sPetGetMonth = (Spinner) findViewById(R.id.petGetMonthSpinner);
        sPetGetDay = (Spinner) findViewById(R.id.petGetDaySpinner);
        individualInfoText = (TextView) findViewById(R.id.individualInfoText);
        proxySignText = (TextView) findViewById(R.id.proxySignText);
        immediatelyBuyText = (TextView) findViewById(R.id.immediatelyBuyText);

        neutralizationLayout = (LinearLayout) findViewById(R.id.neutralizationLayout);
        setDateArrayForSpinner();

        // ArrayAdapter 정의
        aaYear = new MessageSpinnerAdapter(this, R.layout.message_spinner, yearArray);
        aaGetYear = new MessageSpinnerAdapter(this, R.layout.message_spinner, yearArray);
        aaMonth = new MessageSpinnerAdapter(this, R.layout.message_spinner, monthArray);
        aaGetMonth = new MessageSpinnerAdapter(this, R.layout.message_spinner, monthArray);
        aaDay = new MessageSpinnerAdapter(this, R.layout.message_spinner, dayArray);
        aaGetDay = new MessageSpinnerAdapter(this, R.layout.message_spinner, dayArray);

        // ArrayAdapter 설정.
        aaYear.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item);
        aaGetYear.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item);
        aaMonth.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item);
        aaGetMonth.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item);
        aaDay.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item);
        aaGetDay.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item);

        // 스피너 기본 설정 값
        sPetBirthYear.setAdapter(aaYear);
        sPetBirthYear.setSelection(0);
        sPetGetYear.setAdapter(aaGetYear);
        sPetGetYear.setSelection(0);
        sPetBirthMonth.setAdapter(aaMonth);
        sPetBirthMonth.setSelection(0);
        sPetGetMonth.setAdapter(aaGetMonth);
        sPetGetMonth.setSelection(0);
        sPetBirthDay.setAdapter(aaDay);
        sPetBirthDay.setSelection(0);
        sPetGetDay.setAdapter(aaGetDay);
        sPetGetDay.setSelection(0);

        setSpinnerMaxHeight(sPetBirthYear, SPINNER_HEIGHT);
        setSpinnerMaxHeight(sPetGetYear, SPINNER_HEIGHT);
        setSpinnerMaxHeight(sPetBirthMonth, SPINNER_HEIGHT);
        setSpinnerMaxHeight(sPetGetMonth, SPINNER_HEIGHT);
        setSpinnerMaxHeight(sPetBirthDay, SPINNER_HEIGHT);
        setSpinnerMaxHeight(sPetGetDay, SPINNER_HEIGHT);

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
        rewriteBtn.setOnTouchListener(imageViewClickListener);

        // 주소 동일 체크박스 클릭 이벤트
        cbMatchedPostCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbMatchedPostCode.isChecked()) {
                    // 모든 주소 입력 칸에 공백이 없어야 함
                    if(checkStringWS(tvOwnerPostCode.getText().toString()) && checkStringWS(tvOwnerPost.getText().toString()) && checkEditText(eOwnerDetailPostCode)) {
                        // 값 설정
                        tvOwnerRealPost.setText(tvOwnerPost.getText().toString());
                        tvOwnerRealPostCode.setText(tvOwnerPostCode.getText().toString());
                        eOwnerRealDetailPostCode.setText(eOwnerDetailPostCode.getText().toString());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "공백이 있어서 체크 실패했습니다.", Toast.LENGTH_LONG).show();
                        cbMatchedPostCode.setChecked(false); // 체크 해제
                    }
                }
                else {  // 초기화
                    tvOwnerRealPost.setText("");
                    tvOwnerRealPostCode.setText("");
                    eOwnerRealDetailPostCode.setText("");
                }
            }
        });

        // 중성화 수술 체크박스 클릭 이벤트
        cbNeutralizationSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbNeutralizationSurgery.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), ImageTextPopupActivity.class);
                    intent.putExtra("popupType", 1);
                    startActivityForResult(intent, CHECK_NEUTRALIZATIONSURGERY);
                }
            }
        });

        // 직접구매 텍스트 및 체크박스 클릭 이벤트
        immediatelyBuyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cbImmediatelyBuy.isChecked())
                    cbImmediatelyBuy.setChecked(true);
            }
        });
        cbImmediatelyBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbImmediatelyBuy.isChecked()) {
                    if (type == 2 || type == 3) {
                        Intent intent = new Intent(getApplicationContext(), ProductPopupActivity.class);
                        intent.putExtra("type", type);
                        startActivityForResult(intent, CHECK_IMMEDIATELYBUY);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "외장형 또는 인식표를 선택해야 직접 구매가 가능합니다.", Toast.LENGTH_SHORT).show();
                        cbImmediatelyBuy.setChecked(false);
                    }
                }
                else {
                    cbImmediatelyBuy.setChecked(false);
                }
            }
        });

        // 개인정보 수집 동의 텍스트 및 체크박스 클릭 이벤트
        individualInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cbCheckIndividualInfo.isChecked())
                    cbCheckIndividualInfo.setChecked(true);
                else
                    cbCheckIndividualInfo.setChecked(false);
            }
        });
        cbCheckIndividualInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbCheckIndividualInfo.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), IndividualInfoPopupActivity.class);
                    startActivityForResult(intent, CHECK_INDIVIDUALINFO);
                    }
                }
        });

        // 대리서명 동의 텍스트 및 체크박스 클릭 이벤트
        proxySignText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cbProxySign.isChecked())
                    cbProxySign.setChecked(true);
                else
                    cbProxySign.setChecked(false);
            }
        });
        cbProxySign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbProxySign.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), ProxySignPopupActivity.class);
                    startActivityForResult(intent, CHECK_PROXYSIGN);
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
            if(typeIntent.hasExtra("data")) {
                checkReservation = true;
                // 예약 변경인 경우의 설정
                reservationBtn.setImageResource(R.drawable.message_checkreservationbtn);
                rewriteBtn = (ImageView) findViewById(R.id.messageRewriteBtn);
                rewriteBtn.setVisibility(View.VISIBLE);
                rewriteBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN) {
                            if(checkReservation) {
                                if(setOwnerInfo() && setPetInfo()) {
                                    messageAsyncTask = new MessageAsyncTask();
                                    messageAsyncTask.execute("/user/changeReservation", "rewrite");
                                }
                            }
                        }
                        return true;
                    }
                });
//                reservationListData = (MyReservationListData) typeIntent.getSerializableExtra("data");
                myReservationData = (MyReservationData) typeIntent.getSerializableExtra("data");
                printReservationData(myReservationData);
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
        ownerName = data.getOwner_name();
        eOwnerName.setText(ownerName);

        hospitalName = data.getHospital_name();

        // ex) 123456-1234567
        ownerRRN = data.getOwner_resident();
        String[] splitRRN = ownerRRN.split("-");
        eOwnerRRNBefore.setText(splitRRN[0]);
        eOwnerRRNAfter.setText(splitRRN[1]);

        // ex) 010-1234-5678
        ownerHP = data.getOwner_phone_number();
        Log.d(TAG, "폰번 : " + data.getOwner_phone_number());
        String[] splitHP = ownerHP.split("-");
        eOwnerHP1.setText(splitHP[0]);
        eOwnerHP2.setText(splitHP[1]);
        eOwnerHP3.setText(splitHP[2]);

        // ex) 30265_대전시 유성구 궁동_충남대학교 4층
        String[] addr1 = data.getAddress1().split("_");
        ownerPostCode = addr1[0];
        ownerPost = addr1[1];
        ownerDetailPostCode = addr1[2];

        // String[] splitPostCode = ownerPostCode.split("-");
        tvOwnerPostCode.setText(ownerPostCode);
        tvOwnerPost.setText(ownerPost);
        eOwnerDetailPostCode.setText(ownerDetailPostCode);

        // ex) 30265_대전시 유성구 궁동_충남대학교 4층
        String[] addr2 = data.getAddress2().split("_");
        ownerRealPostCode = addr2[0];
        ownerRealPost = addr2[1];
        ownerRealDetailPostCode = addr2[2];
        // String[] splitRealPostCode = ownerRealPostCode.split("-");
        tvOwnerRealPostCode.setText(ownerRealPostCode);
        tvOwnerRealPost.setText(ownerRealPost);
        eOwnerRealDetailPostCode.setText(ownerRealDetailPostCode);

        petName = data.getPet_name();
        ePetName.setText(petName);
        petRace = data.getPet_variety();
        ePetRace.setText(petRace);
        petColor = data.getPet_color();
        ePetColor.setText(petColor);

        // ex) 1996.01.30
        String[] birth = data.getPet_birth().split("\\.");
        petYear = birth[0];
        petMonth = birth[1];
        petDay = birth[2];
        sPetBirthYear.setSelection(getIndexOfSpinner(petYear, yearArray));
        sPetBirthMonth.setSelection(getIndexOfSpinner(petMonth, monthArray));
        sPetBirthDay.setSelection(getIndexOfSpinner(petDay, dayArray));

        // ex) 1996.01.30
        String[] getDate = data.getRegist_date().split("\\.");
        petGetYear = getDate[0];
        petGetMonth = getDate[1];
        petGetDay = getDate[2];
        sPetGetYear.setSelection(getIndexOfSpinner(petGetYear, yearArray));
        sPetGetMonth.setSelection(getIndexOfSpinner(petGetMonth, monthArray));
        sPetGetDay.setSelection(getIndexOfSpinner(petGetDay, dayArray));

        key = data.getHospital_key();

        // 0: default, 1: inner, 2: outer, 3: badge
        type = data.getType();
        if(type == 1)
            innerBtn.setImageResource(R.drawable.message_innerclick);
        else if(type == 2)
            outerBtn.setImageResource(R.drawable.message_outerclick);
        else if(type == 3)
            badgeBtn.setImageResource(R.drawable.message_badgeclick);

        // 0: default, 1: female, 2: male
        petGender = data.getPet_gender();
        if(petGender == 1)
            ivPetFemale.setImageResource(R.drawable.message_petfemaleclick);
        else if(petGender == 2)
            ivPetMale.setImageResource(R.drawable.message_petmaleclick);

        // 0: default, 1: neutralization, 2: not neutralization
        petNeutralization = data.getPet_neutralization();
        if(petNeutralization == 1)
            ivPetNeutalization.setImageResource(R.drawable.message_petneutralizationclick);
        else if(petNeutralization == 2)
            ivPetNotNeutralization.setImageResource(R.drawable.message_petnotneutralizationclick);

        petSpecialProblem = data.getEtc();
        ePetSpecialProblem.setText(petSpecialProblem);

        askDateOld = data.getAsk_date();
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

        yearArray.add("년");
        monthArray.add("월");
        dayArray.add("일");

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
                || !checkStringWS(tvOwnerPostCode.getText().toString()) || !checkStringWS(tvOwnerPost.getText().toString()) || !checkEditText(eOwnerDetailPostCode)
                || !checkStringWS(tvOwnerRealPostCode.getText().toString()) || !checkStringWS(tvOwnerRealPost.getText().toString()) || !checkEditText(eOwnerRealDetailPostCode)) {
            Toast.makeText(getApplicationContext(), "모든 소유주 정보 입력 칸을 채우세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            ownerRealDetailPostCode = eOwnerRealDetailPostCode.getText().toString();
            ownerRealPostCode = tvOwnerRealPostCode.getText().toString();
            ownerRealPost = tvOwnerRealPost.getText().toString();
            ownerDetailPostCode = eOwnerDetailPostCode.getText().toString();
            ownerPostCode = tvOwnerPostCode.getText().toString();
            ownerPost = tvOwnerPost.getText().toString();
            ownerAddress = ownerPostCode + "_" + ownerPost + "_" + ownerDetailPostCode;
            ownerRealAddress = ownerRealPostCode + "_" + ownerRealPost + "_" + ownerRealDetailPostCode;
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
    public static boolean checkEditText(EditText editText) {
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
        if(TextUtils.isEmpty(string)) {
            return false;
        }
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
    타입에 따른 ASK_DATE 처리
     */
    private boolean saveMyReservationFile(JSONObject reservationObject, String type) {
        String filename = "myReservation.json";
        final String fileText = loadJSONFile(filename);
        FileOutputStream fileOutputStream = null;

        MyReservationListData myReservationListData;
        try {
            fileOutputStream = openFileOutput(filename, MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
            // ASK_DATE 수정하는 작업
            if(type.equals("rewrite")) {    // ASK_DATE_NEW -> ASK_DATE, ASK_DATE_OLD remove
                reservationObject.remove("ask_date_old");
                reservationObject.accumulate("ask_date", reservationObject.get("ask_date_new"));
                reservationObject.remove("ask_date_new");
            }

            Gson gson = new Gson();
            myReservationListData = new MyReservationListData("WAIT", 1, key,
                    hospitalName, reservationObject.get("ask_date").toString());
            JSONObject temp = new JSONObject();

            if(TextUtils.isEmpty(fileText)) { // 파일이 존재하지 않은 경우
                Log.d(TAG, "기존에 저장된 파일 존재하지 않은 경우");
                myReservationListData = new MyReservationListData("WAIT", 1, key,
                        hospitalName, reservationObject.get("ask_date").toString());
                temp.accumulate("listData", myReservationListData.getJSONObj());
                temp.accumulate("reservationData", reservationObject);

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(temp);
                fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
            }
            else {  // 기존에 저장된 파일 존재
                JSONArray jsonArray = new JSONArray(fileText);
                Log.d(TAG, "기존에 저장된 파일 존재");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i); // {listData : ~, reservationData : ~}
                    Log.d(TAG, "기존에 저장된 파일 존재2");
                    if (jsonObject.has("reservationData")) {
                        JSONObject listData = (JSONObject) jsonObject.get("listData");
                        JSONObject reservationData = (JSONObject) jsonObject.get("reservationData");
                        Log.d(TAG, "기존에 저장된 파일 존재3");
                        if(reservationData.getString("reservation_date").equals(askDateOld)) {   // 예약 날짜 동일 체크
                            if(listData.getInt("hospital_key") == myReservationData.getHospital_key() && reservationData.getInt("hospital_key") == myReservationData.getHospital_key()) { // 키 동일 체크
                                // jsonArray.remove(i);
                                Log.d(TAG, "기존에 저장된 파일 존재4");
                                // ListData 날짜 수정
                                listData.remove("reservation_date");
                                listData.accumulate("reservation_date", reservationObject.get("ask_date"));
                                // 다시 집어넣기
                                temp.accumulate("listData", listData);
                                temp.accumulate("reservationData", reservationObject);
                                Log.d(TAG, "기존에 저장된 파일 존재5");
                                jsonArray.put(i, temp); // 덮어씌우기
                                fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
                                Log.d(TAG, "myReservationListData : " + jsonArray.toString());
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                return true;
                            }
                        }
                    }

                }
                jsonArray.put(reservationObject);
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
    파일 저장하는 함수
    타입에 따른 ASK_DATE 처리
     */
//    private boolean saveMyReservationFile(JSONObject reservationObject, String type) {
//        String filename = "reservation.json";
//        final String fileText = loadJSONFile(filename);
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = openFileOutput(filename, MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
//            // ASK_DATE 수정하는 작업
//            if(type.equals("rewrite")) {    // ASK_DATE_NEW -> ASK_DATE, ASK_DATE_OLD remove
//                reservationObject.remove("ASK_DATE_OLD");
//                reservationObject.accumulate("ASK_DATE", reservationObject.get("ASK_DATE_NEW"));
//                reservationObject.remove("ASK_DATE_NEW");
//            }
//
//            reservationObject.accumulate("HOSPITAL_NAME", hospitalName);
//            reservationObject.accumulate("RESERVATION_STATE", "WAIT");    // 현재 예약 진행 상태를 나타내는 값 넣기
//
//            if(TextUtils.isEmpty(fileText)) { // 파일이 존재하지 않은 경우
//                Log.d(TAG, "기존에 저장된 파일 존재하지 않은 경우");
//                JSONArray jsonArray = new JSONArray();
//                jsonArray.put(reservationObject);
//                fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
//            }
//            else {  // 기존에 저장된 파일 존재
//                JSONArray jsonArray = new JSONArray(fileText);
//                Log.d(TAG, "기존에 저장된 파일 존재");
//                for(int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        // Todo : 체크할 때 Key로만 판단하기에는 정보가 부족하다. 모든 값을 수정해서 예약보낼수 있기 때문에. 해결법 찾기
//                        // Todo : 테스트 진행해보기. put이 덮어씌워지는지.
//                        if(jsonObject.getString("ASK_DATE").equals(askDateOld)) {   // 예약 날짜 동일 체크
//                            if(jsonObject.getInt("HOSPITAL_KEY") == myReservationData.getHOSPITAL_KEY()) { // 키 동일 체크
//                            // jsonArray.remove(i);
//                            jsonArray.put(i,reservationObject); // 덮어씌우기
//                            fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
//                            fileOutputStream.flush();
//                            fileOutputStream.close();
//                            return true;
//                        }
//                    }
//                }
//                jsonArray.put(reservationObject);
//                fileOutputStream.write(jsonArray.toString().getBytes());   // Json 쓰기
//            }
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            return true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                fileOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

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
        String type;

        @Override
        protected String doInBackground(String... strings) {
            String search_url = SERVER_URL + strings[0];    // URL
            // 서버에 메세지 정보 전송
            try {
                type = strings[1];
                jsonObject = getJsonObjOfType(type);

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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject resultObject = StringToJSON(result);
            Log.d(TAG, "result : " + result);
            try {
                if(TextUtils.isEmpty(resultObject.getString("result"))) {
                    Toast.makeText(getApplicationContext(), "예약 실패!", Toast.LENGTH_LONG).show();
                }

                if(resultObject.getInt("result") == 1) {
                    if(saveMyReservationFile(jsonObject, type)) {
                        Toast.makeText(getApplicationContext(), "예약 성공! 저장 성공!", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else {
                        Log.d(TAG, "예약 성공! 저장 실패!");
                        Toast.makeText(getApplicationContext(), "예약 성공! 저장 실패!", Toast.LENGTH_LONG).show();
                        setResult(RESULT_CANCELED);
                        finish();
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

    /*
    서버에 전송할 데이터를 타입에 따라 전송하는 타입
     */
    private JSONObject getJsonObjOfType(String typeString) {
        JSONObject tempObject = new JSONObject();
            try {
                // 현재 날짜와 시간 구하기
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String nowDate = simpleDateFormat.format(date);
                Log.d(TAG, "date : " + nowDate);

                // Message에 담은 모든 정보 JSONObject에 담기
                tempObject.accumulate("hospital_key", key); // key JSONObject에 담기
                tempObject.accumulate("type", type); // type JSONObject에 담기
                tempObject.accumulate("owner_resident", ownerRRN);
                tempObject.accumulate("owner_name", ownerName);
                tempObject.accumulate("owner_phone", ownerHP);
                tempObject.accumulate("address1", ownerAddress);
                tempObject.accumulate("address2", ownerRealAddress);
                tempObject.accumulate("pet_name", petName);
                tempObject.accumulate("pet_variety", petRace);
                tempObject.accumulate("pet_color", petColor);
                tempObject.accumulate("pet_gender", petGender);
                tempObject.accumulate("pet_neutralization", petNeutralization);
                tempObject.accumulate("pet_birth", petBirth);
                tempObject.accumulate("regist_date", petGetDate);
                tempObject.accumulate("etc", petSpecialProblem);
                tempObject.accumulate("sametime", neutralizationSurgery);
                tempObject.accumulate("user_id", KAKAO_ID);

                // ASK_DATE(등록날짜)에 대한 예외 상황처리
                if(typeString.equals("send")) {
                    tempObject.accumulate("ask_date", nowDate);
                }
                if(typeString.equals("rewrite")) {
                    tempObject.accumulate("ask_date_old", askDateOld);
                    tempObject.accumulate("ask_date_new", nowDate);
                }
                return tempObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SELECT_RESERVATION:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "팝업창에서 확인 누름!");
                    messageAsyncTask = new MessageAsyncTask();
                    messageAsyncTask.execute("/user/putReservation", "send");
                }
                else {
                    Log.d(TAG, "팝업창에서 취소 누름!");
                }
                break;
            case CHECK_INDIVIDUALINFO:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "정보제공동의 팝업창에서 확인 누름!");
                    Toast.makeText(getApplicationContext(), "정보제공에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "정보제공동의 팝업창에서 취소 누름!");
                    Toast.makeText(getApplicationContext(), "정보제공에 동의하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                    cbCheckIndividualInfo.setChecked(false);
                }
                break;
            case CHECK_IMMEDIATELYBUY:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "직접 구매 완료!");
                    Toast.makeText(getApplicationContext(), "직접 구매 확인되었습니다.", Toast.LENGTH_SHORT).show();
                    cbImmediatelyBuy.setActivated(false);
                }
                else {
                    Log.d(TAG, "직접 구매 취소!");
                    Toast.makeText(getApplicationContext(), "직접 구매를 취소합니다.", Toast.LENGTH_SHORT).show();
                    cbImmediatelyBuy.setChecked(false);
                }
                break;
            case CHECK_NEUTRALIZATIONSURGERY:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "중성화 수술 할래요!");
                    Toast.makeText(getApplicationContext(), "중성화 수술도 함께 예약합니다.", Toast.LENGTH_SHORT).show();
                    neutralizationSurgery = 1;
                }
                else {
                    Log.d(TAG, "중성화 수술 안할래요!");
                    Toast.makeText(getApplicationContext(), "중성화 수술을 하지 않습니다.", Toast.LENGTH_SHORT).show();
                    cbNeutralizationSurgery.setChecked(false);
                    neutralizationSurgery = 0;
                }
                break;
            case SEARCH_POSTCODE:
                if(resultCode == RESULT_OK) {
                    PostCodeItem postCodeItem = (PostCodeItem) intent.getSerializableExtra("data");
                    tvOwnerPostCode.setText(postCodeItem.getPostcd());
                    ownerPostCode = postCodeItem.getPostcd();
                    tvOwnerPost.setText(postCodeItem.getAddress());
                    ownerPost = postCodeItem.getAddress();
                }
                break;
            case SEARCH_REALPOSTCODE:
                if(resultCode == RESULT_OK) {
                    PostCodeItem postCodeItem = (PostCodeItem) intent.getSerializableExtra("data");
                    tvOwnerRealPostCode.setText(postCodeItem.getPostcd());
                    ownerRealPostCode = postCodeItem.getPostcd();
                    tvOwnerRealPost.setText(postCodeItem.getAddress());
                    ownerRealPost = postCodeItem.getAddress();
                }
                break;
            case CHECK_PROXYSIGN:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "대리서명동의 팝업창에서 확인 누름!");
                    Toast.makeText(getApplicationContext(), "대리서명에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "대리서명동의 팝업창에서 취소 누름!");
                    Toast.makeText(getApplicationContext(), "대리서명에 동의하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                    cbProxySign.setChecked(false);
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
                       Intent intent2 = new Intent(getApplicationContext(), PostCodePopupActivity.class);
                       startActivityForResult(intent2, SEARCH_POSTCODE);
                       break;
                   case R.id.searchRealPostCodeBtn:
                       // 우편번호 API 실행
                       Intent intent3 = new Intent(getApplicationContext(), PostCodePopupActivity.class);
                       startActivityForResult(intent3, SEARCH_REALPOSTCODE);
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
                           neutralizationLayout.setVisibility(View.GONE);
                           neutralizationSurgery = 0;
                       }
                       break;
                   case R.id.petNotNeutralization:
                       if(petNeutralization == 2) { // neutralization 선택 중인 경우
                           ivPetNotNeutralization.setImageResource(R.drawable.message_petnotneutralization);
                           petNeutralization = 0;
                           neutralizationLayout.setVisibility(View.GONE);
                           neutralizationSurgery = 0;
                       }
                       else {
                           ivPetNeutalization.setImageResource(R.drawable.message_petneutralization);
                           ivPetNotNeutralization.setImageResource(R.drawable.message_petnotneutralizationclick);
                           petNeutralization = 2;
                           neutralizationLayout.setVisibility(View.VISIBLE);
                           cbNeutralizationSurgery.setChecked(false);
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
                           badgeBtn.setImageResource(R.drawable.message_badge);
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
                       if(checkReservation) {
                           setResult(RESULT_CANCELED);
                           finish();
                       }
                       else {
                           if(setOwnerInfo() && setPetInfo() && cbCheckIndividualInfo.isChecked() && cbProxySign.isChecked()) {    // 정보제공 동의 체크 여부까지 판단
                               Intent intent = new Intent(getApplicationContext(), MessagePopupActivity.class);
                               intent.putExtra("messageType", "reservation");
                               startActivityForResult(intent, SELECT_RESERVATION);
                           }
                       }
                       break;
               }
           }
           return true;
        }
    }

    /*
    존재하는 년,월,일 맞는지 따지기
    @return boolean(true : 존재함.  false : 존재하지 않음.)
     */
    private boolean checkProperDate(int year, int month, int day) {

        return false;
    }

    /*
    스피너의 보여줄 수 있는 최대 크기 설정하기.
     */
    private void setSpinnerMaxHeight(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
            popupWindow.setHeight(height);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}