package com.example.rkdus.a2019_epis_tufu4;

public class SearchPrintItem {
    String hospitalName;
    String ownerName;
    int signUpApp;

    public SearchPrintItem(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalName() {

        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getSignUpApp() {
        return signUpApp;
    }

    public void setSignUpApp(int signUpApp) {
        this.signUpApp = signUpApp;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isSignUpApp(int signUpApp) {
        if(signUpApp == 1)
            return true;
        return false;
    }
}
