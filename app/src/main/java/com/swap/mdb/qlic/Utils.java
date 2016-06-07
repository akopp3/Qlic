package com.swap.mdb.qlic;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Sirjan Kafle on 4/23/16.
 */

public class Utils {
    public static final int SALTSIZE = 16;
    public static final int IVSIZE = 16;
    public static byte[] salt;
    public static String iv;

    public static void initialize(Context ctx) {
        salt = readInValues("salt.txt", ctx, SALTSIZE);
        byte[] bt = readInValues("bt.txt", ctx, IVSIZE);

        if (salt == null || bt == null) {
            Log.i("FILEERROR", "Security files not found");
            return;
        }

        iv = new String(bt);
    }

    public static String getInitial(String name) {
        String[] individualNames = name.split(" ");
        String initial = "";

        for (String individualName : individualNames) {
            initial += individualName.charAt(0);
        }

        return initial;
    }

    public static String generateKey(byte[] salt, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterationCount = 1000;
        int keyLength = 256;

        SecureRandom random = new SecureRandom();
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        return Base64.encodeToString(keyBytes, Base64.NO_WRAP);
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

    private static byte[] readInValues(String filename, Context ctx, int size) {
        try {
            InputStream stream = ctx.getAssets().open(filename);
            byte[] securityArray = new byte[size];

            int length = stream.read(securityArray);
            if (length != size) {
                Log.i("SECURITYERROR", "security files are whack");
                return null;
            } else {
                return securityArray;
            }
        } catch (IOException e) {
            Log.i("SECURITYERROR", "security files not found");
            return null;
        }
    }
}
