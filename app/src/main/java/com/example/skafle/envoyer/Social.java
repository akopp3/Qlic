package com.example.skafle.envoyer;

/**
 * Created by Sirjan Kafle on 3/25/16.
 * The Social Interface contains information
 * on one user.
 */

public interface Social {
    void activate();
    boolean isActivated();
    void setKeyInfo();
    String type();
    String stringRep();
}
