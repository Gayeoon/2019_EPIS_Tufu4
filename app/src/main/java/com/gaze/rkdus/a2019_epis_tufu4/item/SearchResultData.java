package com.gaze.rkdus.a2019_epis_tufu4.item;

import java.io.Serializable;

/*
SearchActivity에 사용하는 객체
검색 시 반환받는 값들을 저장
 */
public class SearchResultData implements Serializable {
    private int hospital_key;
    private String ceo_name;
    private String hospital_name;
    private String phone;
    private String address1;
    private String address2;
    private int signup_app;
    private int reservation_count;
    private int review_count;
    private String review_total;

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public String getReview_total() {
        return review_total;
    }

    public void setReview_total(String review_total) {
        this.review_total = review_total;
    }

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

    public int getHospital_key() {
        return hospital_key;
    }

    public void setHospital_key(int hospital_key) {
        this.hospital_key = hospital_key;
    }

    public String getCeo_name() {
        return ceo_name;
    }

    public void setCeo_name(String ceo_name) {
        this.ceo_name = ceo_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getSignup_app() {
        return signup_app;
    }

    public void setSignup_app(int signup_app) {
        this.signup_app = signup_app;
    }

    public int getReservation_count() {
        return reservation_count;
    }

    public void setReservation_count(int reservation_count) {
        this.reservation_count = reservation_count;
    }

    public boolean getBoolSIGNUP_APP() {
        if(this.signup_app == 1)
            return true;
        return false;
    }

    // ArrayList 내 객체의 값을 contains하기 위한 equals 메소드
    @Override
    public boolean equals(Object object) {
        boolean same = false;
        if(object != null && object instanceof SearchResultData) {
            same = this.hospital_key == ((SearchResultData) object).getHospital_key();
        }

        return same;
    }
}
