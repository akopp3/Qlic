package com.swap.mdb.qlic;

/**
 * Created by Abhinav on 4/2/2016.
 */
public class ContactSocial extends ActivateHandler {
    private String email;

    @Override
    public void setKeyInfo(String info) {
        email = info;
    }

    @Override
    public String type() {
        return SendActivity.types[4];
    }

    @Override
    public String keyInfo() {
        return email;
    }

    public String stringRep() {
        return email;
    }
}
