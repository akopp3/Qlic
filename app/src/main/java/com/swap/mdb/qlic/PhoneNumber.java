package com.swap.mdb.qlic;

/**
 * Created by Sirjan Kafle on 3/25/16.
 */

public class PhoneNumber extends ActivateHandler {
    private String phoneNumber;

    @Override
    public void setKeyInfo(String info) {
        phoneNumber = info;
    }

    @Override
    public String type() {
        return SendActivity.types[3];
    }

    @Override
    public String keyInfo() {
        return phoneNumber;
    }

    public String stringRep() {
        return phoneNumber;
    }
}
