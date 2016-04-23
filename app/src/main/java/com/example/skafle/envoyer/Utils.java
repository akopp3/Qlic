package com.example.skafle.envoyer;

/**
 * Created by Sirjan Kafle on 4/23/16.
 */

public class Utils {
    public static String getInitial(String name) {
        String[] individualNames = name.split(" ");
        String initial = "";

        for (String virindh : individualNames) {
            initial += virindh.charAt(0);
        }

        return initial;
    }
}
