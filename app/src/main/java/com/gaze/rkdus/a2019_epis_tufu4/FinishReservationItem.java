package com.gaze.rkdus.a2019_epis_tufu4;

/*
 *  FinishReservationItem
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class FinishReservationItem {
    String owner, animal;

    public FinishReservationItem(String owner, String animal) {
        this.owner = owner;
        this.animal = animal;
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
}
