package com.example.skafle.envoyer;

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

    public Receiver(String rep) {
        Scanner scanner = new Scanner(rep);

        name = scanner.nextLine();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] info = line.split("|");

            if (Boolean.parseBoolean(info[0])) {
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
        // TODO: Update this with all the different types of Social Media
        switch(type) {
            case "phone":
                PhoneNumber phone = new PhoneNumber();
                phone.setKeyInfo(message);
                return phone;
            default:
                return null;
        }
    }
}
