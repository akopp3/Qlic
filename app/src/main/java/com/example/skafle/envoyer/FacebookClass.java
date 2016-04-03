package com.example.skafle.envoyer;

/**
 * Created by Abhinav on 4/2/2016.
 */
public class FacebookClass extends ActivateHandler {
    private String username;

    @Override
    public void setKeyInfo(String info) {
        username = info;
    }

    @Override
    public String type() {
        return MainActivity.types[0];
    }

    @Override
    public String keyInfo() {
        return "https://www.facebook.com/" + username;
    }

    public String stringRep() {
        return username;
    }
}
