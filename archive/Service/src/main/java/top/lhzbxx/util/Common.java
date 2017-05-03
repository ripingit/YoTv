package top.lhzbxx.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

/**
 * Author: LuHao
 * Date: 16/5/27
 * Mail: lhzbxx@gmail.com
 */

public class Common {

    public static String generateSalt(int length) {
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = 32 + (int) (new Random().nextFloat() * 94);
            buffer.append((char) c);
        }
        return new String(buffer);
    }

    public static String generateSerial(int length) {
        Random rand = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(rand.nextInt(10));
        }
        return result.toString();
    }

    public static String encryptSHA1(String str) {
        try {
            MessageDigest encrypt = MessageDigest.getInstance("SHA-1");
            encrypt.update(str.getBytes());
            return byteToHex(encrypt.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
