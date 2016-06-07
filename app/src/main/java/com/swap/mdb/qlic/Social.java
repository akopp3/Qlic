package com.swap.mdb.qlic;

/**
 * Created by Sirjan Kafle on 3/25/16.
 * The Social Interface contains information
 * on one user.
 */

public interface Social {
    void activate();

    boolean isActivated();

    void setKeyInfo(String info);

    String keyInfo();

    String type();

    String stringRep();
}
