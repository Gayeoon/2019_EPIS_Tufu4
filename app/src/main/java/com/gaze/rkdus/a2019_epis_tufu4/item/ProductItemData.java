package com.gaze.rkdus.a2019_epis_tufu4.item;

import java.io.Serializable;

public class ProductItemData implements Serializable {
    String product_key;
    int type;   // 0 : 등록 인식표.  1 : 외장형 목걸이
    int price;
    String name;
    String text;
    String img_detail;
    String img_list;
    int shipping;

    public String getImg_detail() {
        return img_detail;
    }

    public void setImg_detail(String img_detail) {
        this.img_detail = img_detail;
    }

    public String getImg_list() {
        return img_list;
    }

    public void setImg_list(String img_list) {
        this.img_list = img_list;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }


}

