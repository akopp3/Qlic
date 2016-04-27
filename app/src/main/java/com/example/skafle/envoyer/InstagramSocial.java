package com.example.skafle.envoyer;

/**
 * Created by Abhinav on 4/2/2016.
 */
public class InstagramSocial extends ActivateHandler {
    private String username;

    @Override
    public void setKeyInfo(String info) {
            username = info;
        }

    @Override
    public String type() {
            return SendActivity.types[1];
        }

    @Override
    public String keyInfo() {
            return username;
        }

    public String stringRep() {
            return username;
        }
}
