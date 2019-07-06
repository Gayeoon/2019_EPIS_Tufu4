package com.gaze.rkdus.a2019_epis_tufu4;

/*
 *  StatusItem
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class StatusItem {
    String owner, animal;
    int state;

    public StatusItem(String owner, String animal, int state) {
        this.owner = owner;
        this.animal = animal;
        this.state = state;
    }

    public void setowner(String owner) {
        this.owner = owner;
    }

    public String getowner() {
        return owner;
    }

    public void setanimal(String animal) {
        this.animal = animal;
    }

    public String getanimal() {
        return animal;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
