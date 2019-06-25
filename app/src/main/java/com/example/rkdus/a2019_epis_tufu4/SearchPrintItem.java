package com.example.rkdus.a2019_epis_tufu4;

public class SearchPrintItem {
    String hospitalName;
    String ownerName;

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

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
