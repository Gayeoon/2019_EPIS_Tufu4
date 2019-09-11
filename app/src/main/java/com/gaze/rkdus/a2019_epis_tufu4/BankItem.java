package com.gaze.rkdus.a2019_epis_tufu4;

/*
 *  BankItem class
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class BankItem {
    String bank_name;
    int resId;

    public BankItem(String bank_name, int resId) {
        this.bank_name = bank_name;
        this.resId = resId;
    }

    public void setBankName(String n) {
        this.bank_name = n;
    }

    public String getBankName() {
        return bank_name;
    }

    public void setImage(int resId) {
        this.resId = resId;
    }

    public int getImage() {
        return resId;
    }

}
