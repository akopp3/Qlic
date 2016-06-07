package com.swap.mdb.qlic;

import android.util.Base64;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
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
        byte[] saltInit = {95, 105, 6, 70, 107, 26, 46, 117, 7, 82, 20, 69, 88, 123, 20, 40};
        salt = saltInit;

        byte[] bt = {114, 27, 26, 26, 64, 96, 81, 63, 58, 120, 91, 122, 48, 126, 124, 67};

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
        return Base64.encodeToString(keyBytes, Base64.NO_WRAP);
//        SecretKey key = new SecretKeySpec(keyBytes, "AES");
//        Cipher cipher;
//
//        try {
//            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        } catch (javax.crypto.NoSuchPaddingException e) {
//            Log.i("Error", e.toString());
//            return "";
//        }
//
//        byte[] iv = new byte[cipher.getBlockSize()];
//        random.nextBytes(iv);
//        IvParameterSpec ivParams = new IvParameterSpec(iv);
//        try {
//            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
//        } catch (java.security.InvalidAlgorithmParameterException|java.security.InvalidKeyException e) {
//            Log.i("Error", e.toString());
//            return "";
//        }
//
//        try {
//            byte[] ciphertext = cipher.doFinal();
//            String encoded = new String(ciphertext);
//            return encoded;
//        } catch (javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException e) {
//            Log.i("Error", e.toString());
//            return "";
//        }
    }

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(key, Base64.NO_WRAP), "AES");

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
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(key, Base64.NO_WRAP), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));

            return new String(original);
        } catch (Exception ex) {
            throw new IOException("Password does not match");
        }
    }

    public static String dateString(Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        return sdf.format(date);
    }
}
