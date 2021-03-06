package com.swap.mdb.qlic.social;

import com.swap.mdb.qlic.activities.SendActivity;

/**
 * Created by Abhinav on 4/2/2016.
 */
public class FacebookSocial extends ActivateHandler {
    private String id;

    @Override
    public void setKeyInfo(String info) {
        id = info;
    }

    @Override
    public String type() {
        return SendActivity.types[0];
    }

    @Override
    public String keyInfo() {
        return "https://www.facebook.com/app_scoped_user_id/" + id;
    }

    public String stringRep() {
        return id;
    }
}
