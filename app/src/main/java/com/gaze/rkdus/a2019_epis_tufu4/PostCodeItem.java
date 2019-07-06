package com.gaze.rkdus.a2019_epis_tufu4;

import java.io.Serializable;

public class PostCodeItem implements Serializable {
    String postcd;
    String address;
    String addrjibun;

    public String getPostcd() {
        return postcd;
    }

    public void setPostcd(String postcd) {
        this.postcd = postcd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddrjibun() {
        return addrjibun;
    }

    public void setAddrjibun(String addrjibun) {
        this.addrjibun = addrjibun;
    }
}
