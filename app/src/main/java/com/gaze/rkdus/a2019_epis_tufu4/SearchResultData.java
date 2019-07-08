package com.gaze.rkdus.a2019_epis_tufu4;

import java.io.Serializable;

/*
SearchActivity에 사용하는 객체
검색 시 반환받는 값들을 저장
 */
public class SearchResultData implements Serializable {
    private int HOSPITAL_KEY;
    private String CEO_NAME;
    private String HOSPITAL_NAME;
    private String PHONE_NUMBER;
    private String ADDRESS1;
    private String ADDRESS2;
    private int SIGNUP_APP;
    private int RESERVATION_COUNT;

    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getRESERVATION_COUNT() {
        return RESERVATION_COUNT;
    }

    public void setRESERVATION_COUNT(int RESERVATION_COUNT) {
        this.RESERVATION_COUNT = RESERVATION_COUNT;
    }
    public int getHOSPITAL_KEY() {
        return HOSPITAL_KEY;
    }

    public void setHOSPITAL_KEY(int HOSPITAL_KEY) {
        this.HOSPITAL_KEY = HOSPITAL_KEY;
    }

    public String getCEO_NAME() {
        return CEO_NAME;
    }

    public void setCEO_NAME(String CEO_NAME) {
        this.CEO_NAME = CEO_NAME;
    }

    public String getHOSPITAL_NAME() {
        return HOSPITAL_NAME;
    }

    public void setHOSPITAL_NAME(String HOSPITAL_NAME) {
        this.HOSPITAL_NAME = HOSPITAL_NAME;
    }

    public String getPHONE_NUMBER() {
        return PHONE_NUMBER;
    }

    public void setPHONE_NUMBER(String PHONE_NUMBER) {
        this.PHONE_NUMBER = PHONE_NUMBER;
    }

    public String getADDRESS1() {
        return ADDRESS1;
    }

    public void setADDRESS1(String ADDRESS1) {
        this.ADDRESS1 = ADDRESS1;
    }

    public String getADDRESS2() {
        return ADDRESS2;
    }

    public void setADDRESS2(String ADDRESS2) {
        this.ADDRESS2 = ADDRESS2;
    }

    public int getSIGNUP_APP() {
        return SIGNUP_APP;
    }

    public void setSIGNUP_APP(int SIGNUP_APP) {
        this.SIGNUP_APP = SIGNUP_APP;
    }

    public boolean getBoolSIGNUP_APP() {
        if(this.SIGNUP_APP == 1)
            return true;
        return false;
    }

    // ArrayList 내 객체의 값을 contains하기 위한 equals 메소드
    @Override
    public boolean equals(Object object) {
        boolean same = false;
        if(object != null && object instanceof SearchResultData) {
            same = this.HOSPITAL_KEY == ((SearchResultData) object).getHOSPITAL_KEY();
        }

        return same;
    }
}
