package graph_encryption.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class CipherUtils {
    public static String MD5(String content) {
        byte[] result = new byte[]{};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(content.getBytes("UTF-8"));
            result = md.digest();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        BigInteger bigInteger = new BigInteger(1, result);
        return bigInteger.toString(16);
    }
}