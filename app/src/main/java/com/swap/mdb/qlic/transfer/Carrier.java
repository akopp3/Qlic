package com.swap.mdb.qlic.transfer;

import com.swap.mdb.qlic.Constants;
import com.swap.mdb.qlic.social.Social;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sirjan Kafle on 3/25/16.
 * The Carrier class will carry messages
 * in a channel.
 */

public class Carrier {
    private String name;
    private Map<String, Social> socialMap;

    public Carrier(String name) {
        this.name = name;
        socialMap = new HashMap<>();
    }

    public void addSocial(Social soc) {
        socialMap.put(soc.type(), soc);
    }

    public Social getSocial(String key) {
        return socialMap.get(key);
    }

    public void removeAll() {
        socialMap.clear();
    }

    @Override
    public String toString() {
        String repStr = name + "\n";

        for (Social soc : socialMap.values()) {
            boolean activated = soc.isActivated();
            String type = soc.type();
            String rep = soc.stringRep();

            repStr += activated + Constants.DELIMETER + type + Constants.DELIMETER + rep + "\n";
        }

        return repStr;
    }
}
