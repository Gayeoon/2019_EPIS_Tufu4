package com.gaze.rkdus.a2019_epis_tufu4;

/*
 *  NewReservationItem
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class NewReservationItem {
    String name, time;

    public NewReservationItem(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}