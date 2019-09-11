package com.gaze.rkdus.a2019_epis_tufu4;

/*
 *  VaccinationItem class
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class VaccinationItem {

    String vaccine_name, date, time;

    public VaccinationItem(String vaccine_name, String date, String time) {
        this.vaccine_name = vaccine_name;
        this.date = date;
        this.time = time;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}