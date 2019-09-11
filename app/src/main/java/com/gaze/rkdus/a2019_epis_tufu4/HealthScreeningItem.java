package com.gaze.rkdus.a2019_epis_tufu4;

/*
 *  HealthScreeningItem class
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class HealthScreeningItem {
    String date, time;

    public HealthScreeningItem(String date, String time) {
        this.date = date;
        this.time = time;
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