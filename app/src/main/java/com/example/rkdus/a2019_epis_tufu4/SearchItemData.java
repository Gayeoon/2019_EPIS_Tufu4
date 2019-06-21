package com.example.rkdus.a2019_epis_tufu4;

/*
반려동물 등록 대행업체 검색 후 결과를 담은 객체
+ 위치 정보, 거리 변수 추가
 */
public class SearchItemData {
    public int getHospitalKey() {
        return hospitalKey;
    }

    public void setHospitalKey(int hospitalKey) {
        this.hospitalKey = hospitalKey;
    }

    private int hospitalKey;
    private String ceoName;
    private String hospitalName;
    private String phoneNum;
    private String address1;
    private String address2;
    private boolean signUpApp;

    private double longitude;
    private double latitude;
    private float distance;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setSignUpApp(boolean signUpApp) {
        this.signUpApp = signUpApp;
    }

    public SearchItemData() {}

    public SearchItemData(int hospitalkey, String ceoname, String hospitalname, String phonenum, String address1, String address2, boolean signupapp) {
        this.hospitalKey = hospitalkey;
        this.ceoName = ceoname;
        this.hospitalName = hospitalname;
        this.phoneNum = phonenum;
        this.address1 = address1;
        this.address2 = address2;
        this.signUpApp = signupapp;
    }

    public String getCeoName() {
        return ceoName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public boolean getSignUpApp() {
        return signUpApp;
    }

    public String getSignUpAppSymbol() {
        if(getSignUpApp())   // boolean T/F에 따라 결과 다르게 출력(이미지로 변환 가능)
            return "●";
        else
            return "○";
    }
}