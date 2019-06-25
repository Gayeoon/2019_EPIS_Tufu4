package com.example.rkdus.a2019_epis_tufu4;

public class WaitReservationItem {
    String owner, animal;
    boolean call;

    public WaitReservationItem(String owner, String animal, boolean call) {
        this.owner = owner;
        this.animal = animal;
        this.call = call;
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

    public void setCall(boolean call) {
        this.call = call;
    }

    public boolean getCall() {
        return call;
    }

}

