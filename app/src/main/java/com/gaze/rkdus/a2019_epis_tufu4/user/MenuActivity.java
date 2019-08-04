package com.gaze.rkdus.a2019_epis_tufu4.user;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.CommunityActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.popup.NicnamePopupActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * 사용자
 * 메인 메뉴 액티비티
 * - 이해원
 */
public class MenuActivity extends BaseActivity {
    private final int SELECT_NICNAME = 10;

    Intent switchActvityIntent;
    ImageView whatIsRegImg;
    ImageView howToPetRegImg;
    ImageView searchHospitalImg;
    ImageView myPageImg;
    ImageView regReviseGuideImg;
    ImageView communityImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 뷰 정의
        whatIsRegImg = (ImageView) findViewById(R.id.whatIsPetRegImg);
        howToPetRegImg = (ImageView) findViewById(R.id.howToPetRegImg);
        searchHospitalImg = (ImageView) findViewById(R.id.searchHospitalImg);
        myPageImg = (ImageView) findViewById(R.id.myPageImg);
        regReviseGuideImg = (ImageView) findViewById(R.id.regReviseGuideImg);
        communityImg = (ImageView) findViewById(R.id.communityImg);

        // 권한 확인 및 요청
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission();
            checkLocationPermission();
        }

        // 뷰 클릭 이벤트
        whatIsRegImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        switchActvityIntent = new Intent(getApplicationContext(), WhatIsRegActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        howToPetRegImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        switchActvityIntent = new Intent(getApplicationContext(), TypeActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        searchHospitalImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        switchActvityIntent = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        myPageImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        switchActvityIntent = new Intent(getApplicationContext(), MyPageActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        regReviseGuideImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        switchActvityIntent = new Intent(getApplicationContext(), ReviseActivity.class);
                        startActivity(switchActvityIntent);
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });
        communityImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        // TODO : 저장된 파일에서 불러오기
                        if(TextUtils.isEmpty(getNicname())) { // 닉네임이 스마트폰 안에 내장되어있지 않은 경우
                            switchActvityIntent = new Intent(getApplicationContext(), NicnamePopupActivity.class);
                            startActivityForResult(switchActvityIntent, SELECT_NICNAME);
                        }
                        else {  // 닉네임이 스마트폰 안에 내장되어있는 경우
                            switchActvityIntent = new Intent(getApplicationContext(), CommunityActivity.class);
                            switchActvityIntent.putExtra("user", 1);
                            switchActvityIntent.putExtra("userName", getNicname());
                            startActivity(switchActvityIntent);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                        break;
                }
                return true;
            }
        });

    }

    /*
    닉네임이 담은 파일 폴더 경로 반환
     */
    public String getNicnameDirPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/vowow_nicname"; // 폴더 저장 경로
    }

    /*
    닉네임이 저장된 파일에서 닉네임 불러오는 함수
     */
    public String getNicname() {
        if(existNicname()) {
            String line = null;
            try {
                Log.d(TAG, Environment.getExternalStorageDirectory().getAbsolutePath() + "/vowow_nicname");
                BufferedReader buf = new BufferedReader(new FileReader(getNicnameDirPath() + "/nicname.txt"));
                if((line = buf.readLine()) != null) {   // 파일에서 읽은 값이 null이 아닌 경우
                    return line;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else
            return null;
    }

    /*
    닉네임을 저장하는 함수
     */
    public boolean setNicname(String nicname) {
        if(!existNicname()) {   // 기존에 설정한 닉네임이 존재하지 않는 경우(= 폴더가 존재하지 않는 경우)
            File file = new File(getNicnameDirPath());
            file.mkdir();
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(getNicnameDirPath() + "/nicname.txt", false));
            buf.append(nicname);
            buf.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    닉네임이 저장된 파일이 존재하는지 체크
    */
    public boolean existNicname() {
        File nicnameFIle = new File(getNicnameDirPath()); // 폴더 저장 경로
        if(!nicnameFIle.exists()) { // 폴더가 존재하지 않으면
            return false;
        }
        else
            return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SELECT_NICNAME:
                if(resultCode == RESULT_OK) {
                    String nicname = intent.getStringExtra("nicname");
                    if(setNicname(nicname)) {
                        switchActvityIntent = new Intent(getApplicationContext(), CommunityActivity.class);
                        switchActvityIntent.putExtra("user", 1);
                        switchActvityIntent.putExtra("userName", getNicname());
                        startActivity(switchActvityIntent);
                    }
                    else
                        Log.d(TAG, "내장 파일 저장 실패");
                }
                break;
            default:
                break;
        }
    }

    /*
    Camera 관련 권한 체크 상태 확인 함수
    @return : boolean(true : 체크한 경우, false : 체크 안한 경우)
     */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        Log.d(TAG, "checkLocationPermission start ");
        if ( checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            // Should we show an explanation?
            // 권한 팝업에서 한번이라도 거부한 경우 true 리턴.
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
            {
                // ...
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    /*
   Location 관련 권한 체크 상태 확인 함수
   @return : boolean(true : 체크한 경우, false : 체크 안한 경우)
    */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission start ");
        if ( checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // Should we show an explanation?
            // 권한 팝업에서 한번이라도 거부한 경우 true 리턴.
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // ...
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult start ");
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int i=0; i<grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // 하나라도 거부한다면.
                        new AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 해당 서비스를 이용하실 수 있습니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();
                        return;
                    }
                }
            }
        }
    }
}