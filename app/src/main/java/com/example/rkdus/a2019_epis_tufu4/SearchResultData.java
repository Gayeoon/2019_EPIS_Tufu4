package com.example.rkdus.a2019_epis_tufu4;

public class SearchResultData {
    private int HOSPITAL_KEY;
    private String CEO_NAME;
    private String HOSPITAL_NAME;
    private String PHONE_NUMBER;
    private String ADDRESS1;
    private String ADDRESS2;
    private int SIGNUP_APP;

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
}
