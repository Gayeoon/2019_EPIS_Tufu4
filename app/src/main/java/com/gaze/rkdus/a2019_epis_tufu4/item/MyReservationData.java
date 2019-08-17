package com.gaze.rkdus.a2019_epis_tufu4.item;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

/*
MyPageActivity의 Registration RecyclerView에 담을 객체
 */
public class MyReservationData implements Serializable {
    int hospital_key;
    int type;
    String owner_name;
    String owner_resident;
    String owner_phone_number;
    String address1;
    String address2;
    String pet_name;
    String pet_variety;
    String pet_color;
    int pet_gender;
    int pet_neutralization;
    String pet_birth;
    String regist_date;
    String etc;
    String ask_date;

    String hospital_name;


    public int getHospital_key() {
        return hospital_key;
    }

    public void setHospital_key(int hospital_key) {
        this.hospital_key = hospital_key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_resident() {
        return owner_resident;
    }

    public void setOwner_resident(String owner_resident) {
        this.owner_resident = owner_resident;
    }

    public String getOwner_phone_number() {
        return owner_phone_number;
    }

    public void setOwner_phone_number(String owner_phone_number) {
        this.owner_phone_number = owner_phone_number;
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

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_variety() {
        return pet_variety;
    }

    public void setPet_variety(String pet_variety) {
        this.pet_variety = pet_variety;
    }

    public String getPet_color() {
        return pet_color;
    }

    public void setPet_color(String pet_color) {
        this.pet_color = pet_color;
    }

    public int getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(int pet_gender) {
        this.pet_gender = pet_gender;
    }

    public int getPet_neutralization() {
        return pet_neutralization;
    }

    public void setPet_neutralization(int pet_neutralization) {
        this.pet_neutralization = pet_neutralization;
    }

    public String getPet_birth() {
        return pet_birth;
    }

    public void setPet_birth(String pet_birth) {
        this.pet_birth = pet_birth;
    }

    public String getRegist_date() {
        return regist_date;
    }

    public void setRegist_date(String regist_date) {
        this.regist_date = regist_date;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getAsk_date() {
        return ask_date;
    }

    public void setAsk_date(String ask_date) {
        this.ask_date = ask_date;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
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
