package com.example.skafle.envoyer;

/**
 * Created by Abhinav on 4/2/2016.
 */
public class LinkedinSocial extends ActivateHandler {
    private String username;

    @Override
    public void setKeyInfo(String info) {
        username = info;
    }

    @Override
    public String type() {
        return MainActivity.types[5];
    }

    @Override
    public String keyInfo() {
        return "https://www.linkedin.com/" + username;
    }

    public String stringRep() {
        return username;
    }
}
