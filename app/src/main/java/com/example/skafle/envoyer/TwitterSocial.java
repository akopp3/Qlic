package com.example.skafle.envoyer;

/**
 * Created by Abhinav on 4/2/2016.
 */
public class TwitterSocial extends ActivateHandler {
    private String username;

    @Override
    public void setKeyInfo(String info) {
        username = info;
    }

    @Override
    public String type() {
        return MainActivity.types[2];
    }

    @Override
    public String keyInfo() {
        return "https://www.twitter.com/" + username;
    }

    public String stringRep() {
        return username;
    }
}
