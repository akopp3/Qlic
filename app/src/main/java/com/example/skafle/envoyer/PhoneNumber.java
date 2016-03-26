package com.example.skafle.envoyer;

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
        return "phone";
    }

    public String stringRep() {
        return phoneNumber;
    }
}
