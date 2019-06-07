package com.example.rkdus.a2019_epis_tufu4;

import java.util.LinkedList;

public class Hospital {
    private String ceo_name;
    private String hospital_name;
    private String phone;
    private String pw;
    private Notification notification;

    // 내장칩 등록자
    public class Inner {
        private String id;
        private String isRegist;
        Inner(String id, String isRegist) {
            this.id = id;
            this.isRegist = isRegist;
        }
    }

    // 외장칩 등록자
    public class Outer {
        private String id;
        private String isRegist;
        Outer(String id, String isRegist) {
            this.id = id;
            this.isRegist = isRegist;
        }
    }

    // 알림
    public class Notification {
        public  Reservation reservation;

        Notification() {
            this.reservation = new Reservation();
        }

        // 등록신청
        public class Reservation {
            public LinkedList<Inner> inners;
            public LinkedList<Outer> outers;

            Reservation(){
                inners = new LinkedList<>();
                outers = new LinkedList<>();
            }
        }

        public Reservation getReservation() { return reservation; }
    }

    Hospital() {

    }

    Hospital(String ceo_name, String hospital_name, String phone, String pw, Notification notification) {
        this.ceo_name = ceo_name;
        this.hospital_name = hospital_name;
        this.phone = phone;
        this.pw = pw;
        this.notification = notification;
    }

    public String getCeo_name() { return ceo_name; }
    public String getHospital_name() { return hospital_name; }
    public String getPhone() { return phone; }
    public String getPw() { return pw; }
    public Notification getNotification() { return notification; }
    public void setCeo_name(String ceo_name) { this.ceo_name = ceo_name; }
    public void setHospital_name(String hospital_name) { this.hospital_name = hospital_name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPw(String pw) { this.pw = pw; }
    public void setNotification(Notification notification) { this.notification = notification; }
}
