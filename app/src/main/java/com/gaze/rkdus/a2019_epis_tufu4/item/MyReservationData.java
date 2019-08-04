package com.gaze.rkdus.a2019_epis_tufu4.item;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

/*
MyPageActivity의 Registration RecyclerView에 담을 객체
 */
public class MyReservationData implements Serializable {
    int HOSPITAL_KEY;
    int TYPE;
    String OWNER_NAME;
    String OWNER_RESIDENT;
    String OWNER_PHONE_NUMBER;
    String OWNER_ADDRESS1;
    String OWNER_ADDRESS2;
    String PET_NAME;
    String PET_VARIETY;
    String PET_COLOR;
    int PET_GENDER;
    int PET_NEUTRALIZATION;
    String PET_BIRTH;
    String REGIST_DATE;
    String ETC;
    String ASK_DATE;

    String HOSPITAL_NAME;

    public String getRESERVATION_STATE() {
        return RESERVATION_STATE;
    }

    public void setRESERVATION_STATE(String RESERVATION_STATE) {
        this.RESERVATION_STATE = RESERVATION_STATE;
    }

    String RESERVATION_STATE;

    public String getREGIST_DATE() {
        return REGIST_DATE;
    }

    public void setREGIST_DATE(String REGIST_DATE) {
        this.REGIST_DATE = REGIST_DATE;
    }

    public int getHOSPITAL_KEY() {
        return HOSPITAL_KEY;
    }

    public void setHOSPITAL_KEY(int HOSPITAL_KEY) {
        this.HOSPITAL_KEY = HOSPITAL_KEY;
    }

    public String getOWNER_NAME() {
        return OWNER_NAME;
    }

    public void setOWNER_NAME(String OWNER_NAME) {
        this.OWNER_NAME = OWNER_NAME;
    }

    public String getOWNER_RESIDENT() {
        return OWNER_RESIDENT;
    }

    public void setOWNER_RESIDENT(String OWNER_RESIDENT) {
        this.OWNER_RESIDENT = OWNER_RESIDENT;
    }

    public String getOWNER_PHONE_NUMBER() {
        return OWNER_PHONE_NUMBER;
    }

    public void setOWNER_PHONE_NUMBER(String OWNER_PHONE_NUMBER) {
        this.OWNER_PHONE_NUMBER = OWNER_PHONE_NUMBER;
    }

    public String getOWNER_ADDRESS1() {
        return OWNER_ADDRESS1;
    }

    public void setOWNER_ADDRESS1(String OWNER_ADDRESS1) {
        this.OWNER_ADDRESS1 = OWNER_ADDRESS1;
    }

    public String getOWNER_ADDRESS2() {
        return OWNER_ADDRESS2;
    }

    public void setOWNER_ADDRESS2(String OWNER_ADDRESS2) {
        this.OWNER_ADDRESS2 = OWNER_ADDRESS2;
    }

    public String getPET_NAME() {
        return PET_NAME;
    }

    public void setPET_NAME(String PET_NAME) {
        this.PET_NAME = PET_NAME;
    }

    public String getPET_VARIETY() {
        return PET_VARIETY;
    }

    public void setPET_VARIETY(String PET_VARIETY) {
        this.PET_VARIETY = PET_VARIETY;
    }

    public String getPET_COLOR() {
        return PET_COLOR;
    }

    public void setPET_COLOR(String PET_COLOR) {
        this.PET_COLOR = PET_COLOR;
    }

    public int getPET_GENDER() {
        return PET_GENDER;
    }

    public void setPET_GENDER(int PET_GENDER) {
        this.PET_GENDER = PET_GENDER;
    }

    public int getPET_NEUTRALIZATION() {
        return PET_NEUTRALIZATION;
    }

    public void setPET_NEUTRALIZATION(int PET_NEUTRALIZATION) {
        this.PET_NEUTRALIZATION = PET_NEUTRALIZATION;
    }

    public String getPET_BIRTH() {
        return PET_BIRTH;
    }

    public void setPET_BIRTH(String PET_BIRTH) {
        this.PET_BIRTH = PET_BIRTH;
    }

    public String getASK_DATE() {
        return ASK_DATE;
    }

    public void setASK_DATE(String ASK_DATE) {
        this.ASK_DATE = ASK_DATE;
    }

    public String getETC() {
        return ETC;
    }

    public void setETC(String ETC) {
        this.ETC = ETC;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public String getHOSPITAL_NAME() {
        return HOSPITAL_NAME;
    }

    public void setHOSPITAL_NAME(String HOSPITAL_NAME) {
        this.HOSPITAL_NAME = HOSPITAL_NAME;
    }

    public String getTypeToStr(int type) {   // 1: inner,  2: outer,  3: badge
        if(type == 1)
            return "내장형";
        if(type == 2)
            return "외장형";
        if(type == 3)
            return "등록증";

        return null;
    }
}
