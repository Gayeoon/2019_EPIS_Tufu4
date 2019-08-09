package com.gaze.rkdus.a2019_epis_tufu4.item;

import java.io.Serializable;

public class ProductItemData implements Serializable {
    int PRODUCT_KEY;
    int PRODUCT_TYPE;   // 0 : 등록 인식표.  1 : 외장형 목걸이
    int PRODUCT_PRICE;
    String PRODUCT_NAME;
    String PRODUCT_CONTEXT;
    String PRODUCT_IMG_URL;
    String PRODUCT_TEXT;
    boolean PRODUCT_SOLDOUT;    // 재고 체크    true : 매진.      false : 재고 있음.
    boolean SHIPPING_FEE;  // 배송비 체크. true : 배송비 있음.    false : 배송비 없음.

    public ProductItemData(int PRODUCT_KEY, int PRODUCT_TYPE, int PRODUCT_PRICE, String PRODUCT_NAME, String PRODUCT_CONTEXT, boolean PRODUCT_SOLDOUT, boolean SHIPPING_FEE) {
        this.PRODUCT_KEY = PRODUCT_KEY;
        this.PRODUCT_TYPE = PRODUCT_TYPE;
        this.PRODUCT_PRICE = PRODUCT_PRICE;
        this.PRODUCT_NAME = PRODUCT_NAME;
        this.PRODUCT_CONTEXT = PRODUCT_CONTEXT;
        this.PRODUCT_SOLDOUT = PRODUCT_SOLDOUT;
        this.SHIPPING_FEE = SHIPPING_FEE;
    }

    public ProductItemData(int PRODUCT_KEY, int PRODUCT_TYPE, int PRODUCT_PRICE, String PRODUCT_NAME, String PRODUCT_CONTEXT, String PRODUCT_IMG_URL, String PRODUCT_TEXT, boolean PRODUCT_SOLDOUT, boolean SHIPPING_FEE) {
        this.PRODUCT_KEY = PRODUCT_KEY;
        this.PRODUCT_TYPE = PRODUCT_TYPE;
        this.PRODUCT_PRICE = PRODUCT_PRICE;
        this.PRODUCT_NAME = PRODUCT_NAME;
        this.PRODUCT_CONTEXT = PRODUCT_CONTEXT;
        this.PRODUCT_IMG_URL = PRODUCT_IMG_URL;
        this.PRODUCT_TEXT = PRODUCT_TEXT;
        this.PRODUCT_SOLDOUT = PRODUCT_SOLDOUT;
        this.SHIPPING_FEE = SHIPPING_FEE;
    }

    public String getPRODUCT_IMG_URL() {
        return PRODUCT_IMG_URL;
    }

    public void setPRODUCT_IMG_URL(String PRODUCT_IMG_URL) {
        this.PRODUCT_IMG_URL = PRODUCT_IMG_URL;
    }
    public String getPRODUCT_TEXT() {
        return PRODUCT_TEXT;
    }

    public void setPRODUCT_TEXT(String PRODUCT_TEXT) {
        this.PRODUCT_TEXT = PRODUCT_TEXT;
    }


    public boolean isPRODUCT_SOLDOUT() {
        return PRODUCT_SOLDOUT;
    }

    public void setPRODUCT_SOLDOUT(boolean PRODUCT_SOLDOUT) {
        this.PRODUCT_SOLDOUT = PRODUCT_SOLDOUT;
    }

    public boolean isSHIPPING_FEE() {
        return SHIPPING_FEE;
    }

    public void setSHIPPING_FEE(boolean SHIPPING_FEE) {
        this.SHIPPING_FEE = SHIPPING_FEE;
    }

    public int getPRODUCT_KEY() {
        return PRODUCT_KEY;
    }

    public void setPRODUCT_KEY(int PRODUCT_KEY) {
        this.PRODUCT_KEY = PRODUCT_KEY;
    }

    public int getPRODUCT_TYPE() {
        return PRODUCT_TYPE;
    }

    public void setPRODUCT_TYPE(int PRODUCT_TYPE) {
        this.PRODUCT_TYPE = PRODUCT_TYPE;
    }

    public int getPRODUCT_PRICE() {
        return PRODUCT_PRICE;
    }

    public void setPRODUCT_PRICE(int PRODUCT_PRICE) {
        this.PRODUCT_PRICE = PRODUCT_PRICE;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getPRODUCT_CONTEXT() {
        return PRODUCT_CONTEXT;
    }

    public void setPRODUCT_CONTEXT(String PRODUCT_CONTEXT) {
        this.PRODUCT_CONTEXT = PRODUCT_CONTEXT;
    }
}

