package com.example.skafle.envoyer;

/**
 * Created by Sirjan Kafle on 3/25/16.
 * ActivateHandler handles Social objects properties
 * dealing with being activated or not.
 */

public abstract class ActivateHandler implements Social {
    private boolean activated = false;

    @Override
    public void activate() {
        activated = true;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }
}
