package com.example.rkdus.a2019_epis_tufu4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

/*
MyPageActivity의 Registration RecyclerView에 담을 객체
 */
public class MyReservationData implements Serializable {
    int key;
    String ownerName;
    String ownerRRN;
    String ownerHP;
    String ownerAddress1;
    String ownerAddress2;
    String petName;
    String petRace;
    String petColor;
    int petGender;
    int petNeut;
    String petBirth;
    String petGetDate;
    String petSpecialProblem;

    int type;
    String hospitalName;
    String date;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerRRN() {
        return ownerRRN;
    }

    public void setOwnerRRN(String ownerRRN) {
        this.ownerRRN = ownerRRN;
    }

    public String getOwnerHP() {
        return ownerHP;
    }

    public void setOwnerHP(String ownerHP) {
        this.ownerHP = ownerHP;
    }

    public String getOwnerAddress1() {
        return ownerAddress1;
    }

    public void setOwnerAddress1(String ownerAddress1) {
        this.ownerAddress1 = ownerAddress1;
    }

    public String getOwnerAddress2() {
        return ownerAddress2;
    }

    public void setOwnerAddress2(String ownerAddress2) {
        this.ownerAddress2 = ownerAddress2;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetRace() {
        return petRace;
    }

    public void setPetRace(String petRace) {
        this.petRace = petRace;
    }

    public String getPetColor() {
        return petColor;
    }

    public void setPetColor(String petColor) {
        this.petColor = petColor;
    }

    public int getPetGender() {
        return petGender;
    }

    public void setPetGender(int petGender) {
        this.petGender = petGender;
    }

    public int getPetNeut() {
        return petNeut;
    }

    public void setPetNeut(int petNeut) {
        this.petNeut = petNeut;
    }

    public String getPetBirth() {
        return petBirth;
    }

    public void setPetBirth(String petBirth) {
        this.petBirth = petBirth;
    }

    public String getPetGetDate() {
        return petGetDate;
    }

    public void setPetGetDate(String petGetDate) {
        this.petGetDate = petGetDate;
    }

    public String getPetSpecialProblem() {
        return petSpecialProblem;
    }

    public void setPetSpecialProblem(String petSpecialProblem) {
        this.petSpecialProblem = petSpecialProblem;
    }


    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
