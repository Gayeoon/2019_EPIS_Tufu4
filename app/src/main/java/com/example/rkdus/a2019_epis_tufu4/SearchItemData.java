package com.example.rkdus.a2019_epis_tufu4;

public class SearchItemData {
    private String ceoName;
    private String hospitalName;

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    private String phoneNum;
    private String address1;
    private String address2;
    private boolean signUpApp;

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

    public SearchItemData(String ceoname, String hospitalname, String phonenum, String address1, String address2, boolean signupapp) {
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
