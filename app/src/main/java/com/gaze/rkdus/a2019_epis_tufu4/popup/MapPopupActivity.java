package com.gaze.rkdus.a2019_epis_tufu4.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity;
import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.item.SearchResultData;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

/*
해당 병원의 위치를 카카오 지도로 보여주기 위함
 */
public class MapPopupActivity extends BaseActivity {
    private SearchResultData hospitalData;
    LocationManager locationManager;
    Location myLocation;
    ViewGroup mapViewContainer;
    Button routeBtn;
    LinearLayout routeLayout;
    MapView mapView;

    private final int GPS_CHECK = 10;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;
    // 네트워크 사용유무
    boolean isNetworkEnabled = false;
    // GPS 상태값
    boolean isGetLocation = false;
    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // 최소 GPS 정보 업데이트 시간 밀리세컨
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;
    // 네이버 앱 사용유뮤
    boolean isNaverAppInstalled = false;
    // 카카오 앱 사용유무
    boolean isKakaoAppInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_popup);
        routeBtn = (Button) findViewById(R.id.routeBtn);

        routeLayout = (LinearLayout) findViewById(R.id.routeLayout);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);   // 현재 위치의 위도 경도를 가져오기 위함
        Intent intent = getIntent();
        if(intent == null) {
            Toast.makeText(getApplicationContext(), "병원 정보를 불러올 수 없어 이전 화면으로 돌아갑니다. \n 다시 시도해주세요!", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            if(intent.hasExtra("data")) {
                hospitalData = (SearchResultData) intent.getSerializableExtra("data");
                if(hospitalData.getAddress1().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "지도에 표시할 수 없는 위치에 있습니다. \n 직접 검색해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }

                Location location = addressToLatLon(getApplicationContext(), hospitalData.getAddress1());   // 위도, 경도 저장

                if(location == null) {
                    Toast.makeText(getApplicationContext(), "지도에 표시할 수 없는 위치에 있습니다. \n 직접 검색해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {  // null이 아닌 경우 위도 경도 설정.
                    hospitalData.setLat(location.getLatitude());
                    hospitalData.setLon(location.getLongitude());
                }
            }
        }

        // 현재 사용자의 위도 경도 획득
        final Location location = getLatLon();
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        // 카카오 지도 출력을 위한 초기 선언
        mapView = new MapView(this);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(hospitalData.getLat(), hospitalData.getLon());    // 위도, 경도에 해당하는 위치
        mapView.setMapCenterPointAndZoomLevel(mapPoint, 6, true);   // 중심점과 줌레벨 설정
        mapViewContainer.addView(mapView);
        addMarker(mapView, hospitalData.getHospital_name(), mapPoint, 1);    // 병원 위치 마커 추가

        // 내 위치를 받아올 수 있는 경우
        // TODO: 내 위치 값 가져오게 하기
//        final Location tempLocation = new Location("");
//        tempLocation.setLatitude(36.369232);
//        tempLocation.setLongitude(127.347739);
        if(location != null) {
            Log.d(TAG, "location != null");
            MapPoint myLocationMapPoint = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());    // 위도, 경도에 해당하는 위치
            addMarker(mapView, "현 위치", myLocationMapPoint, 2);
            // 길찾기 버튼 활성화
            routeBtn.isClickable();
            routeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onTouch");
                    findRoute(location);
                }    // 길찾기 버튼 클릭 이벤트
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "일시적으로 내 위치를 확인할 수 없습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
        }

                // 맵 뷰 내의 마커 클릭 이벤트
        mapViewContainer.setClickable(true);
        mapView.setClickable(true);

        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(final MapView mapView, MapPOIItem mapPOIItem) {
                Log.d(TAG, "onPOIItemSelected 인지 아닌지");
                mapView.setBackgroundResource(R.color.colorDimGray);    // 뒷 배경 색
                routeLayout.setVisibility(View.VISIBLE);

                Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
                TextView tvCar = (TextView) findViewById(R.id.car);
                TextView tvPublicTraffic = (TextView) findViewById(R.id.publicTraffic);
                TextView tvWalk = (TextView) findViewById(R.id.walk);

                // 세 가지 방법 중 원하는 방법을 클릭한 경우의 이벤트 정리 => 길찾기 실행
                TextView.OnClickListener onClickListener = new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String method = null;
                        switch (v.getId()) {
                            case R.id.car:
                                method = "CAR";
                                break;
                            case R.id.publicTraffic:
                                method = "PUBLICTRANSIT";
                                break;
                            case R.id.walk:
                                method = "FOOT";
                                break;
                                default:
                                    return;
                        }
                        getRoute(location.getLatitude(),  location.getLongitude(), hospitalData.getLat(), hospitalData.getLon(), method);   // 길찾기 Uri 실행.
                    }
                };

                tvCar.setOnClickListener(onClickListener);
                tvPublicTraffic.setOnClickListener(onClickListener);
                tvWalk.setOnClickListener(onClickListener);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        routeLayout.setVisibility(View.GONE);
                        routeBtn.setVisibility(View.VISIBLE);
                        mapView.setBackgroundResource(R.color.colorWhite);    // 뒷 배경 색
                    }
                });
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        });
    }

    /*

     */
    private void findRoute(final Location myCurrentLocation) {
        Log.d(TAG, "findRoute");
        mapView.setBackgroundResource(R.color.colorDimGray);    // 뒷 배경 색
        routeLayout.setVisibility(View.VISIBLE);
        routeBtn.setVisibility(View.GONE);

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        TextView tvCar = (TextView) findViewById(R.id.car);
        TextView tvPublicTraffic = (TextView) findViewById(R.id.publicTraffic);
        TextView tvWalk = (TextView) findViewById(R.id.walk);

        // 세 가지 방법 중 원하는 방법을 클릭한 경우의 이벤트 정리 => 길찾기 실행
        TextView.OnClickListener onClickListener = new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                String method = null;
                switch (v.getId()) {
                    case R.id.car:
                        method = "CAR";
                        break;
                    case R.id.publicTraffic:
                        method = "PUBLICTRANSIT";
                        break;
                    case R.id.walk:
                        method = "FOOT";
                        break;
                    default:
                        return;
                }
                getRoute(myCurrentLocation.getLatitude(),  myCurrentLocation.getLongitude(), hospitalData.getLat(), hospitalData.getLon(), method);   // 길찾기 Uri 실행.
            }
        };

        tvCar.setOnClickListener(onClickListener);
        tvPublicTraffic.setOnClickListener(onClickListener);
        tvWalk.setOnClickListener(onClickListener);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeLayout.setVisibility(View.GONE);
                mapView.setBackgroundResource(R.color.colorWhite);    // 뒷 배경 색
            }
        });
    }
    /*
    내 위치 on/off 체크.
    꺼져있으면 위치 설정 으로 이동
     */
    private void getGPSCheck() {
        Log.d(TAG, "getGPSCheck");
        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getApplicationContext(), "GPS 설정에서 GPS 서비스를 사용함으로 변경해주세요!", Toast.LENGTH_LONG).show();
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            finish();
            startActivity(intent);
//            startActivityForResult(intent, GPS_CHECK);
        }
    }

    private void addMarker(MapView mapView, String itemName, MapPoint mapPoint, int tag) {
        MapPOIItem customMarker = new MapPOIItem();
        customMarker.setItemName(itemName);
        customMarker.setTag(tag);
        customMarker.setMapPoint(mapPoint);
        customMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
//        customMarker.setCustomImageResourceId(R.drawable.); // 마커 이미지.
//        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
//        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        mapView.addPOIItem(customMarker);
    }
    /*
    주소로 위도 경도 값 구하기(ReverseGeoCoding)
    @param address(String) - 주소
    @return latlon(double[2]) - 위도, 경도
     */
    private Location addressToLatLon(Context context, String address) {
        List<Address> addressList = null;
        Geocoder coder = new Geocoder(context);
        try {
            addressList = coder.getFromLocationName(address, 1);  // 1개의 주소만 가져옴
            Log.d(TAG, "addressList : " + addressList.size());
            if(addressList.isEmpty())
                return null;
            else {
                Location location = new Location("");
                double lat = addressList.get(0).getLatitude();
                double lon = addressList.get(0).getLongitude();
                location.setLatitude(lat);
                location.setLongitude(lon);
                return location;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "주소 변환 실패");
        }
        return null;
    }


    /*
    현재 위치의 위도 경도 값 가져오는 함수
    @return : Location(위도와 경도를 담음)
     */
    @SuppressLint("MissingPermission")
    private Location getLatLon() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                        getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            getGPSCheck();
            // GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS 와 네트워크사용이 가능하지 않을때
                Log.d(TAG, "GPS 와 네트워크사용이 가능하지 않을때");
            } else {
                this.isGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    Log.d(TAG, "네트워크");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
                        if (myLocation != null)
                            // 위도 경도 저장
                            Log.d(TAG, "WiFi를 통해 현재 위치 위도 경도 값 찾음! 위도 : " + myLocation.getLatitude() + ", 경도 : " + myLocation.getLongitude());
                    }
                }

                if (isGPSEnabled) {
                    Log.d(TAG, "isGPSEnabled");
                    if (myLocation != null) {
                        Log.d(TAG, "myLocation != null");
                        Log.d(TAG, "myLocation : " + myLocation.getLongitude());
                    }
                    if (myLocation == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener() {
                        });
                        if (locationManager != null) {
                            List<String> providers = locationManager.getProviders(true);
                            // GPS 좌표 값을 불러올 수 있을 때까지 반복
                            for (String provider : providers) {
                                Log.d(TAG, "GPS 좌표 값을 불러올 수 있을 때까지 반복");
                                Location l = locationManager.getLastKnownLocation(provider);
                                if (l == null)
                                    continue;
                                if (myLocation == null || l.getAccuracy() < myLocation.getAccuracy()) {
                                    // Found best last known location: %s", l);
                                    Log.d(TAG, "zzzz");
                                    Log.d(TAG, "GPS를 통해 현재 위치 위도 경도 값 찾음! 위도 : " + l.getAccuracy() + ", 경도 : " + myLocation.getAccuracy());
                                    myLocation = l;
                                    Log.d(TAG, "GPS를 통해 현재 위치 위도 경도 값 찾음! 위도 : " + myLocation.getLatitude() + ", 경도 : " + myLocation.getLongitude());
                                }
                            }
                        }
                    }
                }


        } catch (Exception e) {
            Log.d(TAG, String.valueOf(e));
            e.printStackTrace();
        }
        return myLocation;
    }

    void getRoute(double mylat, double mylon, double arriveLat, double arriveLon, String method) {
        String url = "daummaps://route?sp="+ mylat + "," + mylon + "&ep="+ arriveLat +"," +  arriveLon + "&by=" + method;

        if(isInstallApp("net.daum.android.map")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "길찾기를 위해선 카카오 맵을 설치해야 합니다. 앱 스토어로 이동합니다.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map")));
        }
    }

    /*
    앱 설치 확인
     */
    private boolean isInstallApp(String pakageName){
        Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(pakageName);
        if(intent==null){
            //미설치
            return false;
        }else{
            //설치
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case GPS_CHECK:
                Log.d(TAG, "resultCode : " + String.valueOf(resultCode));
                finish();
                // 새로고침
//                activityRefresh();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mapViewContainer.removeAllViews();
        mapViewContainer = null;
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


    private class MyLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
