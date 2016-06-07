package com.swap.mdb.qlic.transfer;

import android.util.Log;

import com.swap.mdb.qlic.Constants;
import com.swap.mdb.qlic.social.ContactSocial;
import com.swap.mdb.qlic.social.InstagramSocial;
import com.swap.mdb.qlic.social.LinkedinSocial;
import com.swap.mdb.qlic.social.PhoneNumber;
import com.swap.mdb.qlic.social.Social;
import com.swap.mdb.qlic.social.TwitterSocial;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Sirjan Kafle on 3/25/16.
 * The Receiver object receives a certain string
 * message and decodes it.
 */

public class Receiver {
    private String name;
    private Map<String, Social> socialMap;
    private String messageData;

    public Receiver(String rep) {
        messageData = rep;
        Scanner scanner = new Scanner(rep);
        Log.i("rep",rep);

        name = scanner.nextLine();
        socialMap = new HashMap<>();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] info = line.split(Constants.DELIMETER);

            if (Boolean.parseBoolean(info[0].toLowerCase())) {
                String type = info[1];
                String message = info[2];
                Social social = createSoc(type, message);

                socialMap.put(type, social);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getMessageData() {
        return messageData;
    }

    public Social getSocial(String type) {
        if (socialMap.containsKey(type)) {
            return socialMap.get(type);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        // Return a nicely formatted list of info
        String info = name + "\n";
        for (Social soc : socialMap.values()) {
            info += soc.type() + ": " + soc.stringRep() + "\n";
        }

        return info;
    }

    private Social createSoc(String type, String message) {
        Log.i("TYPE", type);
        switch(type) {
            case "Facebook":
                FacebookClass fb = new FacebookClass();
                fb.setKeyInfo(message);
                fb.activate();
                return fb;
            case "Twitter":
                TwitterSocial twit = new TwitterSocial();
                twit.setKeyInfo(message);
                twit.activate();
                return twit;
            case "Instagram":
                InstagramSocial inst = new InstagramSocial();
                inst.setKeyInfo(message);
                inst.activate();
                return inst;
            case "Phone":
                Log.i("CREATESOC", "goes here");
                PhoneNumber phone = new PhoneNumber();
                phone.setKeyInfo(message);
                phone.activate();
                return phone;
            case "Email":
                ContactSocial contact = new ContactSocial();
                contact.setKeyInfo(message);
                contact.activate();
                return contact;
            case "LinkedIn":
                LinkedinSocial link = new LinkedinSocial();
                link.setKeyInfo(message);
                link.activate();
                return link;
            default:
                return null;
        }
    }
}
