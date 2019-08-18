package com.gaze.rkdus.a2019_epis_tufu4.user;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.adapter.SearchListAdapter;
import com.gaze.rkdus.a2019_epis_tufu4.item.SearchResultData;
import com.gaze.rkdus.a2019_epis_tufu4.popup.FilterPopupActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/*
 * 사용자
 * 반려동물 대행 등록업체를 리스트로 출력하고, 검색 기능 추가
 * 현재 위치를 받아서 현재 위치와 가까운 병원 순으로 나열하는 액티비티
 * 앱 등록 필터 사용
 * 출력된 리스트에서 앱 등록이 된 병원업체 클릭하면 예약 화면으로 진행
 * - 이해원
 */
public class SearchActivity extends BaseActivity {

    /*
    디비 테이블 저장 정보
    - HospitalInfo_TB

        HOSPITAL_KEY: int   -> PK
        CEO_NAME: char(30) NOT NULL
        HOSPITAL_NAME: char(50) NOT NULL
        PHONE_NUMBER: char(15) DEFAULT NULL
        ADDRESS1: char(80) DEFAULT NULL
        ADDRESS2: char(50) DEFAULT NULL
        SIGNUP_APP: tinyint(1) NOT NULL DEFAULT 0
     */

    static final int GET_FILTER = 100;
    boolean isSearchCurrentLocation, isSignUpApp, locationFIlter;
    boolean isPageRefresh = false;

    int indexStartNum;
    LocationManager locationManager;
    String searchWord;
    String location;
    int filter = 0; // 현재 입힌 필터 종류.     0 : 전체(필터 X)    1 : 최다예약순   2 : 거리순
    int searchResultCount;

    private GestureDetector gestureDetector;
    ArrayList<SearchResultData> searchResultList = new ArrayList<>();
    ArrayList<SearchResultData> signUpAppList = new ArrayList<>();
    ArrayList<SearchResultData> locationSearchList = new ArrayList<>(); // 지역 별 필터 중 검색어에 따른 조건을 갖춘 데이터
    ArrayList<SearchResultData> locationSearchResultList = new ArrayList<>();   // 지역 별 필터 입힌 모든 데이터
    SearchAsyncTask searchAsyncTask;
    SearchListAdapter adapter;

    RecyclerView searchRecyclerView;
    TabLayout listTabLayout;
    ImageView ivSearchBtn, ivFilterBtn;
    EditText eSearch;
    TextView tvSignUpApp;
    CheckBox cbSignUpApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 변수 값 초기화
        isSearchCurrentLocation = false;
        isSignUpApp = false;
        locationFIlter = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);   // 현재 위치의 위도 경도를 가져오기 위함
        indexStartNum = 1; // 현재 1페이지부터 5페이지까지.
        searchResultCount = getResources().getInteger(R.integer.searchResultCount);

        // 레이아웃 뷰 정의
        ivSearchBtn = (ImageView) findViewById(R.id.searchBtn);
        ivFilterBtn = (ImageView) findViewById(R.id.filterBtn);
        eSearch = (EditText) findViewById(R.id.searchEditText);
        // ctvIsSignUpApp = (CheckedTextView) findViewById(R.id.isSignUpApp);
        cbSignUpApp = (CheckBox) findViewById(R.id.isSignUpApp);
        // lvSearchList = (ListView) findViewById(R.id.searchListView);
        searchRecyclerView = (RecyclerView) findViewById(R.id.searchListViewPage);
        listTabLayout = (TabLayout) findViewById(R.id.listTablayout);
        tvSignUpApp = (TextView) findViewById(R.id.signUpAppText);

        // 시작 시 모든 리스트 가져오기
        searchAsyncTask = new SearchAsyncTask();
        searchAsyncTask.execute("/user/getHospitalData", "all"); // 모든 데이터 가져오기

        // EditText 자판 리스너 이벤트
        eSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH: // 자판에서 검색 모양 아이콘을 누르면
                        Toast.makeText(getApplicationContext(), "검색을 시작합니다.", Toast.LENGTH_SHORT).show();
                        if(setSearchWord())
                            SearchHospitalData();
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });

        // 검색 이미지 클릭 이벤트
        ivSearchBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Toast.makeText(getApplicationContext(), "검색을 시작합니다.", Toast.LENGTH_SHORT).show();
                        if(setSearchWord())
                            SearchHospitalData();
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

        // 필터 이미지 클릭 이벤트
        ivFilterBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Intent intent = new Intent(getApplicationContext(), FilterPopupActivity.class);
                        intent.putExtra("filter", filter);
                        if(locationFIlter) {
                            intent.putExtra("locationFilter", locationFIlter);
                            intent.putExtra("location", location);
                        }
                        startActivityForResult(intent, GET_FILTER);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

        // 체크박스 클릭 이벤트와 동일한 텍스트 클릭 이벤트
        tvSignUpApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbSignUpApp.isChecked()) {
                    cbSignUpApp.setChecked(false);
                    isSignUpApp = false;
                }
                else {  // 초기화
                    cbSignUpApp.setChecked(true);
                    isSignUpApp = true;
                }
                showListView();
            }
        });
        // 체크박스 클릭 이벤트
        cbSignUpApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbSignUpApp.isChecked()) {
                    isSignUpApp = true;
                }
                else {  // 초기화
                    isSignUpApp = false;
                }
                showListView();
            }
        });

//        // 현재위치로 찾기 클릭 시
//        searchCurrentLocationSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isSearchCurrentLocation) {   // 만약 현재 위치로 찾기로 리스트를 보여주고 있는 경우
//                    searchCurrentLocationSwitch.setTextColor(Color.parseColor(switchOffColor));  // 색상변경
//
//                    Toast.makeText(getApplicationContext(), "현재 위치로 찾기 종료합니다.", Toast.LENGTH_LONG).show();
//                    isSearchCurrentLocation = false;
//                }
//                else {  // 현재 위치로 찾고 싶은 경우
//                    searchCurrentLocationSwitch.setTextColor(Color.parseColor(switchOnColor));  // 색상변경
//                    Toast.makeText(getApplicationContext(), "현재 위치로 찾기 시작합니다.", Toast.LENGTH_LONG).show();
//                    isSearchCurrentLocation = true;
//                }
//                showListView();  // 현재 상태에 따라 on / off 변경
//            }
//        });

        // 탭 레이아웃 클릭 시
        listTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(isPageRefresh)  // 페이지 갱신 시
                    isPageRefresh = false;
                else    // 페이지 갱신이 아닐 시
                    changeTabView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 누를 때 말고 뗄 때 클릭으로 인식되게 하는 변수
        gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener() {

            //누르고 뗄 때 한번만 인식하도록 하기위해서
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

    }

    /*
    사용자가 탭을 눌렀을 때 호출
     */
    private void changeTabView(int index) {
        int tabIndexNum = 0;
        String page = null;
        if(index == 0) {
            tabIndexNum = -2;
        }
        else if(index == listTabLayout.getTabCount() - 1) {
            tabIndexNum = -1;
        }
        else {
            page = listTabLayout.getTabAt(index).getText().toString();   // Tab Page Index로 불러오기
        }

        if((tabIndexNum != -2) && (tabIndexNum != -1)) {
            tabIndexNum = Integer.parseInt(page); // 해당 페이지 번호 가져오기
        }
        ArrayList<?> arrayList = getArrayListFromFilter();
        switch (tabIndexNum) {
            case -2: // ◀ 버튼 클릭 시
                showNextPage(arrayList, false);
                break;
            case -1: // ▶ 버튼 클릭 시
                showNextPage(arrayList, true);
                break;
            default:
                setSearchListView((ArrayList<SearchResultData>) arrayList, tabIndexNum);
                break;
        }
    }

    /*
    해당 arraylist의 크기를 보고 전체 페이지 크기를 반환.
    해상도마다 view하는 크기가 다르기 때문에 searchResultCount로 정함.
     */
    private int getMaxTabSize(ArrayList<?> arrayList) {
        int tabSize = arrayList.size();
        int maxTabSize = 0;
        if(tabSize % searchResultCount == 0)    // ex. 40개면 5페이지.
            maxTabSize = tabSize / searchResultCount;
        else    // 39개여도 5페이지.
            maxTabSize = (tabSize / searchResultCount) + 1;
        return maxTabSize;
    }

    /*
    이전 또는 다음 페이지를 선택하여 새로운 페이지를 출력해야 하는 경우
     */
    private void showNextPage(ArrayList<?> arrayList, boolean isUpPage) {
        int maxSize = getMaxTabSize(arrayList);
        if(isUpPage) {  // 다음 5개의 페이지를 넘기라고 한다면
            if(indexStartNum + 5 > maxSize) { // 전체가 12페이지인데 11페이지에서 다음 페이지를 선택한 경우.
            Log.d(TAG, "Tab 페이지 설정하는데 다음 페이지가 없는 경우. ex) 최대 14페이지인데 11페이지에서 다음 페이지를 클릭하는 경우.");
            Toast.makeText(getApplicationContext(), "해당 탭 내 최대 페이지입니다.", Toast.LENGTH_LONG).show();
            listTabLayout.getTabAt(listTabLayout.getTabCount() - 2).select();
            return;
            }
            else {
                indexStartNum += 5;
                setTabIndex(arrayList); // Tab Page 변경
            }
        }
        else {  // 이전 5개의 페이지를 넘기라고 한다면
            if(indexStartNum - 5 < 0) { // 전체가 12페이지인데 1페이지에서 이전 페이지를 선택한 경우.
                Log.d(TAG, "이전 페이지가 존재하지 않습니다. 최소 페이지입니다.");
                Toast.makeText(getApplicationContext(), "해당 탭 내 최소 페이지입니다.", Toast.LENGTH_LONG).show();
                listTabLayout.getTabAt(1).select();
                return;
            }
            else {
                indexStartNum -= 5;
                setTabIndex(arrayList); // Tab Page 변경
            }
        }
    }

    /*
    Tab Layout에 Tab 생성하는 함수
     */
    private void setTabIndex(ArrayList<?> arrayList) {
        int maxTabSize = getMaxTabSize(arrayList);
        if(listTabLayout.getTabCount() != 0)
            listTabLayout.removeAllTabs(); // tab item 제거

        isPageRefresh = true;   // 페이지 갱신
        if(maxTabSize == 0) {
            listTabLayout.addTab(listTabLayout.newTab().setIcon(R.drawable.search_lefttab));
            listTabLayout.addTab(listTabLayout.newTab().setText("1"));
            listTabLayout.addTab(listTabLayout.newTab().setIcon(R.drawable.search_righttab));
            listTabLayout.getTabAt(1).select();
            return;
        }

        listTabLayout.addTab(listTabLayout.newTab().setIcon(R.drawable.search_lefttab));
        if(indexStartNum + 5 > maxTabSize) { // 전체가 12페이지인데 11페이지부터 시작하는 경우.
            for(int i = indexStartNum; i <= maxTabSize; i++) {
                listTabLayout.addTab(listTabLayout.newTab().setText(String.valueOf(i)));
//              Log.d(TAG, String.valueOf(i) + " 페이지 생성!");
            }
    }
        else {
            int i = 0;
            while(i < 5) {
                listTabLayout.addTab(listTabLayout.newTab().setText(String.valueOf(indexStartNum + i)));
//                Log.d(TAG, String.valueOf(indexStartNum + i) + " 페이지 생성!");
                i++;
            }
        }
        listTabLayout.addTab(listTabLayout.newTab().setIcon(R.drawable.search_righttab));
        // 한번 선택하기
        listTabLayout.getTabAt(1).select();
    }

    /*
    현재 필터와 조건에 맞는 arraylist 반환
     */
    private ArrayList<SearchResultData> getArrayListFromFilter() {
        if(isSignUpApp) {
            return signUpAppList;
        }
        else {
            if (locationFIlter)
                return locationSearchList;
            else
                return searchResultList;
        }
    }

    /*
    현재 필터와 조건에 맞는 arraylist 반환
    LocationFilter로 지역 별 필터에 따른 반환값만 제공.
     */
    private ArrayList<SearchResultData> getArrayListFromLocationFilter() {
        if (locationFIlter)
            return locationSearchList;
        else
            return searchResultList;
    }

    @Override
    protected void onDestroy() {
        // 배열과 AsyncTask 초기화
        searchResultList.clear();
        signUpAppList.clear();
        searchAsyncTask = new SearchAsyncTask();
        try {
            if(searchAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                searchAsyncTask.cancel(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /*
    거리 계산하는 함수
    @param : 내위치 lon, lat과 목적지의 lon, lat
    @return : float(두 지점 사이의 거리)
     */
    private float calculateDistance(double myLon, double myLat, double targetLon, double targetLat) {
        Log.d(TAG, "calculateDistance start");

        Location myLocation = new Location("current my place");
        myLocation.setLongitude(myLon);
        myLocation.setLatitude(myLat);

        Location targetLocation = new Location("my target place");
        targetLocation.setLongitude(targetLon);
        targetLocation.setLatitude(targetLat);
        return myLocation.distanceTo(targetLocation);
    }

    /*
    현재 위치로 찾기 클릭에 따른 찾기 온오프 기능 구현 함수
     */
    private void showListView() {
        final ArrayList<SearchResultData> arrayList = getNeedToShowListView();
        Log.d(TAG, "showListView ArrayList size : " + arrayList.size());
        indexStartNum = 1;
        setTabIndex(arrayList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setSearchListView(arrayList, indexStartNum);
                    }
                });
            }
        });
    }

    /*
    조건에 따라 리스트 보여주는 함수
     */
    private void SearchHospitalData() {
        Log.d(TAG, "SearchHospitalData start");
        searchAsyncTask = new SearchAsyncTask();
        try {   // 현재 searchAsyncTask가 작동중인지 확인. 두 번 실행은 오류남
            if(searchAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                searchAsyncTask.cancel(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // 초기화
        searchResultList.clear();
        locationSearchList.clear();
        signUpAppList.clear();

        // 서버 접속 실행
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
            searchAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "/user/getHospitalData", "searchword");
        else
            searchAsyncTask.execute("/user/getHospitalData", "searchword");
    }

    /*
    searchList ArrayList에서 어플등록 off인 item 제거 후 signUpAppList에 넣기
     */
    private void setSignUpAppList(ArrayList<SearchResultData> searchResultDataArrayList) {
        Log.d(TAG, "setSignUpAppList start");

        // 검색 결과를 담은 ArrayList의 값이 없는 경우 함수 종료
        if(searchResultDataArrayList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "검색한 결과가 없기에 필터가 불가능합니다.", Toast.LENGTH_LONG).show();
            return;
        }

        // 이미 배열 내 값이 존재한 경우 함수 종료
        if(!signUpAppList.isEmpty())
            return;

        // System.arraycopy(searchList, 0, signUpAppList, 0, searchList.size()); // deep copy
        // signUpAppList = searchList; // copy
        Iterator<SearchResultData> itemDataIterator = searchResultDataArrayList.iterator();
        int i = 0;
        while(itemDataIterator.hasNext()) {
           SearchResultData searchResultData = itemDataIterator.next();

            // 등록 여부 off인 값 추가
            if(searchResultData.getBoolSIGNUP_APP()) {
                signUpAppList.add(searchResultData);
            }
            i++;
        }
    }

    /*
    온오프에 따른 어플등록 업체만 보기 유무 설정 + 필터에 따른 ArrayList 정하고, 정렬한 뒤 ArrayList 반환하는 함수
     */
    private ArrayList<SearchResultData> getNeedToShowListView() {
        Log.d(TAG, "getNeedToShowListView start");
        if (isSignUpApp) {
            Log.d(TAG, "어플등록 ㅇ");
            setSignUpAppList(getArrayListFromLocationFilter());
        }

        switch (filter) {
            case 3:
                Log.d(TAG, "리뷰 개수 순");
                Collections.sort(getArrayListFromFilter(), new Comparator<SearchResultData>() {    // 각 data들의 최다 예약 횟수를 비교하여 내림차순 정렬하기
                    @Override
                    public int compare(SearchResultData searchResultData, SearchResultData t1) {
                        return String.valueOf(t1.getReservation_count()).compareTo(String.valueOf(searchResultData.getReservation_count()));
                    }
                });
                break;
            case 2:
                Log.d(TAG, "평점 순");
                Collections.sort(getArrayListFromFilter(), new Comparator<SearchResultData>() {    // 각 data들의 최다 예약 횟수를 비교하여 내림차순 정렬하기
                    @Override
                    public int compare(SearchResultData searchResultData, SearchResultData t1) {
                        return String.valueOf(t1.getReview_total()).compareTo(String.valueOf(searchResultData.getReview_total()));
                    }
                });
                break;
            case 1:
                Log.d(TAG, "최다 예약 순");
                Collections.sort(getArrayListFromFilter(), new Comparator<SearchResultData>() {    // 각 data들의 최다 예약 횟수를 비교하여 내림차순 정렬하기
                    @Override
                    public int compare(SearchResultData searchResultData, SearchResultData t1) {
                        return String.valueOf(t1.getReview_count()).compareTo(String.valueOf(searchResultData.getReview_count()));
                    }
                });
                break;
            default:    // 지역 별 필터만 있거나, 모든 필터를 해제한 경우.
                Log.d(TAG, "필터 X.");
                Collections.sort(getArrayListFromFilter(), new Comparator<SearchResultData>() {    // 각 data들의 최다 예약 횟수를 비교하여 내림차순 정렬하기
                    @Override
                    public int compare(SearchResultData searchResultData, SearchResultData t1) {
                        return String.valueOf(t1.getHospital_name()).compareTo(String.valueOf(searchResultData.getHospital_name()));
                    }
                });
                break;
        }
        return getArrayListFromFilter();
    }

    /*
    서버에 접근하는 백그라운드 작업 + UI Thread 활용하는 AsyncTask
     */
    private class SearchAsyncTask extends AsyncTask<String, String, String> {
        // String... [0] : url  [1] : type  [2 ...] : type마다 필요한 값들
        String type;

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "SearchAsyncTask doInBackground");

            String search_url = SERVER_URL + strings[0];    // URL
            type = strings[1];
            // POST 전송방식을 위한 설정
            HttpURLConnection con = null;
            BufferedReader reader = null;

            // 서버에 특정 키워드 디비에서 검색 요청
            try {
                JSONObject jsonObject = setJSONForSendPost(strings); // type(목적)에 맞는 JSONObject 생성하기
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
                    InputStream stream = con.getInputStream();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int count;
                    while ((count = stream.read(bytes)) > 0) {
                        baos.write(bytes, 0, count);
                    }

                    byte[] fileArray = baos.toByteArray();  // byte화
                    String allString = new String(fileArray);    // byte to string
                    return allString;
                }
                else {  // 정상 연결 아닐 시
                    printConnectionError(con);
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (JSONException e) { // JSON 객체 오류
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "result string : " + s);

            if(s.equals(null))
                return;
            setSearchResultArray(s, type);
            ArrayList<SearchResultData> arrayList = getNeedToShowListView();

            if(arrayList.isEmpty())  // null check
                Log.d(TAG, "getNeedToShowListView result is empty");

            indexStartNum = 1;
            setTabIndex(arrayList);
            setSearchListView(arrayList, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
    JSON 형식으로 저장된 String 값을 JSON Object로 변환해서 리턴
    */
    public static JSONObject StringToJSON(String JSONstr) {
        Log.d(TAG, "StringToJSON start");

//        JSONParser parser = new JSONParser();
//        Object obj = parser.parse(JSONstr);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSONstr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*
    FileOutputStream을 사용해서 서버에서 받은 데이터 전부 파일로 저장하기
     */
    private boolean saveJSONFile(ByteArrayOutputStream byteArrayOutputStream, String filename) {
        Log.d(TAG, "saveJSONFile start");

        filename += ".json";
        // 파일 생성(덮어쓰기)
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(filename, MODE_PRIVATE); // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함
            byteArrayOutputStream.writeTo(fileOutputStream);    // 파일에 작성
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
    String 파라미터를 받아 ArrayList에 저장
     */
    private void setSearchResultArray(String bufstr, String searchType) {
        Log.d(TAG, "setSearchListArray start");
        JSONObject jsonObject = StringToJSON(bufstr);   // JSON 객체 생성
        // JSONObjTofile(jsonObject, fileName);  // json파일로 저장
        putJSONInArrayList(jsonObject, searchType); // ArrayList에 넣기
    }

    /*
    RecyclerView Item 개별 클릭 리스너 설정하는 함수
     */
    private void setRecyclerViewItemClick(final ArrayList<SearchResultData> result, SearchListAdapter searchListAdapter) {
        searchListAdapter.setItemClick(new SearchListAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                //해당 위치의 Data get
                SearchResultData resultData = result.get(position);
                Intent intent = new Intent(getApplicationContext(), HospitalProfileActivity.class);
                intent.putExtra("data", (Serializable) resultData);
                startActivity(intent);  // MessageTypeActivity 실행
            }
        });
    }

    /*
    RecyclerView에 Tab, Page 별로 알맞게 결과 데이터를 넣고 출력하는 함수.
     */
    private void setSearchListView(final ArrayList<SearchResultData> arrayList, int index) {

//        String dimen = "@dimen/searchResultCount";
//        String packageName = this.getPackageName();
//        int temp = getResources().getIdentifier(dimen, "values", packageName);
        Log.d(TAG, "이건 dimens searchResultCount 입니다 : " + searchResultCount);

        int start = (index - 1) * searchResultCount;
        int end;
        if(start + searchResultCount >= arrayList.size())   // 만약 리스트로 출력하는 데이터가 dimens.xml에서 지정한 갯수보다 못채우는 경우
            end = arrayList.size();
        else
            end = start + searchResultCount;
        ArrayList<SearchResultData> result = new ArrayList<>();   // adapter에 넣기 위한 임시 arrayList 생성
        for(int i = start; i < end; i++) {
            result.add(arrayList.get(i));
        }

        CustomLinearLayoutManager customLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        searchRecyclerView.setLayoutManager(customLayoutManager);
        adapter = new SearchListAdapter(result);
        adapter.resetAll(result);
        searchRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // RecyclerView 클릭 이벤트 초기화
        setRecyclerViewItemClick(result, adapter);

    }

    /*
    RecyclerView Scroll 방지
     */
    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);

        }

        // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    /*
    Json 형식의 String 변수를 ArrayList 안에 전부 넣기
    -> Gson 라이브러리를 사용
     */
    private void putJSONInArrayList(JSONObject jsonObject, String searchType) {
        try {
            Log.d(TAG, "putJSONInArrayList start");
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            // Gson사용. JSONArray to ArrayList
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<SearchResultData>>(){}.getType();

            if(searchType.equals("all") || searchType.equals("searchword")) {
                Log.d(TAG, "arrayList에 담기 성공!");
                searchResultList = gson.fromJson(jsonArray.toString(), listType);

                if(filter == 4 || filter == 3) {    // 위치 별 선택한 경우
                    Iterator<SearchResultData> itemDataIterator = locationSearchResultList.iterator();

                    int i = 0;
                    while (itemDataIterator.hasNext()) {
                        SearchResultData searchResultData = itemDataIterator.next();

                        // 중복된 객체가 있을 경우만 삭제 ㄴㄴ
                        // locationSearchList의 값에서 삭제시키자
                        if (searchResultList.contains(searchResultData)) {
                            Log.d(TAG, "하나라도있냐?");
                            Log.d(TAG, "하나있네 : " + searchResultData.getHospital_name());
                            locationSearchList.add(searchResultData);
                            continue;
                        }
                        i++;
                    }
                }
            }

            if(searchType.equals("location")) {
                Log.d(TAG, "arrayList에 담기 성공!");
                locationSearchResultList = gson.fromJson(jsonArray.toString(), listType);

                Iterator<SearchResultData> itemDataIterator = locationSearchResultList.iterator();
                if (!searchResultList.isEmpty()) {   // 검색 결과 리스트가 비어있지 않은 경우
                    if (TextUtils.isEmpty(searchWord)) {   // 처음 전체 검색한 뒤 location filter를 적용한 경우ㄹ
                        Log.d(TAG, "처음 전체 검색한 뒤 location filter를 적용한 경우");
                        locationSearchList.addAll(locationSearchResultList);
                    } else {  // 검색한 키워드가 이미 있고, location filter를 적용한 경우
                        Log.d(TAG, "검색한 키워드가 이미 있고, location filter를 적용한 경우");
                        int i = 0;
                        while (itemDataIterator.hasNext()) {
                            SearchResultData searchResultData = itemDataIterator.next();

                            // 중복된 객체가 있을 경우만 삭제 ㄴㄴ
                            // locationSearchList의 값에서 삭제시키자
                            if (searchResultList.contains(searchResultData)) {
                                Log.d(TAG, "하나라도있냐?");
                                Log.d(TAG, "하나있네 : " + searchResultData.getHospital_name());
                                locationSearchList.add(searchResultData);
                                continue;
                            }
                            i++;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    EditText 값 유효성 체크한 뒤 리턴
     */
    private boolean setSearchWord() {
        Log.d(TAG, "setSearchWord");
        String eSearchTempText = eSearch.getText().toString();    // 검색어 임시 변수에 저장.
        if(TextUtils.isEmpty(eSearchTempText.trim())) { // 공백처리
            Toast.makeText(getApplicationContext(), "값을 입력해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!eSearchTempText.equals(eSearchTempText.trim())) { // 앞뒤 공백이 존재하는 단어 입력
            Toast.makeText(getApplicationContext(), "앞 뒤 공백이 있습니다. 삭제하고 다시 시도하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        searchWord = eSearchTempText;   // 전역 변수에 저장
        return true;
    }

    /*
    type에 따라 필요한 값을 JSONObject에 담는 함수
    @param : String...
    [0] : URL
    [1] : type
    [2] : 1) searchword : X  2) id : hospital_key
    @return : JSONObject(POST 방식으로 요청하기 위해 보내는 값을 담음) - SearchItemData
     */
    private JSONObject setJSONForSendPost(String... strings) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        String type = strings[1];
        Log.d(TAG, "setJSONForSendPost type : " + type);
        if(type.equals("searchword")) {
            jsonObject.accumulate("searchword", searchWord); // 검색어 인코딩까지 수행
        }
        else if(type.equals("all")) {
            jsonObject.accumulate("searchword", "allHospitalData"); // 검색어 인코딩까지 수행
        }
        else if(type.equals("location")) {
            jsonObject.accumulate("location", location);
        }
//        else if(type.equals("id")) {
//            jsonObject.accumulate("key", strings[2]);
//        }
        Log.d(TAG, "결과 : " + jsonObject.toString());
        return jsonObject;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            switch (requestCode) {
                case GET_FILTER:
                    if(resultCode == RESULT_OK) {
                        Log.d(TAG, "필터 팝업창에서 확인 누름!");
                        if(intent.hasExtra("result"))   // 값 있는지 체크
                            filter = intent.getIntExtra("result", 0);

                        // 초기화
                        signUpAppList.clear();

                        // 만약 지역 별 필터를 입힌 경우
                        // locationSearchList에 새로 기입해야 함
                        locationFIlter = intent.getBooleanExtra("locationFilter", false);
                            if (locationFIlter) {
                                Log.d(TAG, "locationFIlter true");
                                if(intent.hasExtra("location")) {
                                    // 초기화
                                    locationSearchResultList.clear();
                                    locationSearchList.clear();

                                    location = intent.getStringExtra("location");
                                    searchAsyncTask = new SearchAsyncTask();
                                    searchAsyncTask.execute("/user/getHospitalDataByAddress", "location");
                                    return;
                                }
                            }

                        Log.d(TAG, "locationFIlter false");
                        ArrayList<SearchResultData> arrayList = getNeedToShowListView();
                        indexStartNum = 1;

                        Log.d(TAG, "getNeedToShowListView size : " + arrayList.size());
                        setTabIndex(arrayList);
                        setSearchListView(arrayList, indexStartNum);

                    }
                    else {
                        Log.d(TAG, "팝업창에서 취소 누름!");
                    }
                    break;
                default:
                    break;
            }
    }
}