package com.example.skafle.envoyer;

/**
 * Created by Abhinav on 4/2/2016.
 */
public class FacebookClass extends ActivateHandler {
    private String id;

    @Override
    public void setKeyInfo(String info) {
        id = info;
    }

    @Override
    public String type() {
        return MainActivity.types[0];
    }

    @Override
    public String keyInfo() {
        return "https://www.facebook.com/app_scoped_user_id/" + id;
    }

    public String stringRep() {
        return id;
    }
}
