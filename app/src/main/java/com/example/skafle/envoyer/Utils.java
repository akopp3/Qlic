package com.example.skafle.envoyer;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Sirjan Kafle on 4/23/16.
 */

public class Utils {
    public static byte[] salt;
    public static String iv;

    public static void initialize() {
        /* final Random r = new SecureRandom();
        salt = new byte[32];
        r.nextBytes(salt); */

        salt = new byte[3];
        salt[0] = 3;
        salt[1] = 4;
        salt[2] = 6;

        byte[] bt = new byte[16];
        for (byte i = 0; i < 16; i++) {
            bt[i] = i;
        }

        iv = new String(bt);
    }

    public static String getInitial(String name) {
        String[] individualNames = name.split(" ");
        String initial = "";

        for (String virindh : individualNames) {
            initial += virindh.charAt(0);
        }

        return initial;
    }

    public static String generateKey(byte[] salt, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterationCount = 1000;
        int keyLength = 256;
        // int saltLength = keyLength / 8; // same size as key output

        SecureRandom random = new SecureRandom();
        //byte[] salt = new byte[saltLength];
        //random.nextBytes(salt);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (javax.crypto.NoSuchPaddingException e) {
            Log.i("Error", e.toString());
            return "";
        }

        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        } catch (java.security.InvalidAlgorithmParameterException|java.security.InvalidKeyException e) {
            Log.i("Error", e.toString());
            return "";
        }

        try {
            byte[] ciphertext = cipher.doFinal();
            String encoded = new String(ciphertext);
            return encoded;
        } catch (javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException e) {
            Log.i("Error", e.toString());
            return "";
        }
    }

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeToString(encrypted, Base64.NO_WRAP));

            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) throws IOException {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));

            return new String(original);
        } catch (Exception ex) {
            throw new IOException("Password does not match");
        }
    }
}
