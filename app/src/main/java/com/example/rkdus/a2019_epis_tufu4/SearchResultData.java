package com.example.rkdus.a2019_epis_tufu4;

public class SearchResultData {
    private int hospitalKey;
    private String ceoName;
    private String hospitalName;
    private String phoneNum;
    private String address1;
    private String address2;
    private int signUpApp;

    public int getHospitalKey() {
        return hospitalKey;
    }

    public void setHospitalKey(int hospitalKey) {
        this.hospitalKey = hospitalKey;
    }

    public String getCeoName() {
        return ceoName;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getPhoneNum() {
        return phoneNum;
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

    public int isSignUpApp() {
        return signUpApp;
    }

    public void setSignUpApp(int signUpApp) {
        this.signUpApp = signUpApp;
    }
}
