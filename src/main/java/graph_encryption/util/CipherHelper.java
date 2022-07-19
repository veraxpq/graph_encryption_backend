package graph_encryption.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class helps encrypt strings.
 */
public final class CipherHelper {
    private static MessageDigest md5Instance;
    private static MessageDigest sha256Instance;
    private static final Logger logger = LogManager.getLogger(CipherHelper.class);

    static {
        try {
//            md5Instance = MessageDigest.getInstance("MD5");
//            sha256Instance = MessageDigest.getInstance("SHA-256");
            sha256Instance = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getStackTrace());
        }
    }

    /**
     * This method encrypts string with input algorithm modes, and formats.
     *
     * @param data input data
     * @param key input key to encrypt the data
     * @param algorithm given algorithm to encrypt data
     * @param mode given mode of encryption
     * @param padding given format of padding
     * @return encrypted data
     */
    public static String encrypt(String data, String key, String algorithm, String mode, String padding) {
        try {
            String encType = algorithm + "/" + mode + "/" + padding;
            Cipher cipher = Cipher.getInstance(encType);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), algorithm));
            byte[] bytes = data.getBytes("UTF-8");
            byte[] encBytes = cipher.doFinal(bytes);
            String hexString = bytesToHexString(encBytes);
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getStackTrace());
            return null;
        } catch (InvalidKeyException e) {
            logger.error(e.getStackTrace());
            return null;
        } catch (NoSuchPaddingException e) {
            logger.error(e.getStackTrace());
            return null;
        } catch (BadPaddingException e) {
            logger.error(e.getStackTrace());
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getStackTrace());
            return null;
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getStackTrace());
            return null;
        }
    }

    /**
     * This method use SHA256 to encrypt a string.
     *
     * @param val input string
     * @return encrypted string
     */
    public static String getSHA256(String val) {
        String encodestr = "";
        try {
            sha256Instance.update(val.getBytes("UTF-8"));
            encodestr = bytesToHexString(sha256Instance.digest());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
        return encodestr;
    }

    /**
     * This method decrypts the data with given key, algorithm, mode and padding.
     *
     * @param data input data to decrypt
     * @param key give key to decrypt the data
     * @param algorithm given algorithm to decrypt
     * @param mode given mode
     * @param padding given format of padding
     * @return decrpted data
     */
    public static String decrypt(String data, String key, String algorithm, String mode, String padding) {
        try {
            byte[] bytes = hexStringToBytes(data);
            String encType = algorithm + "/" + mode + "/" + padding;
            Cipher cipher = Cipher.getInstance(encType);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), algorithm));
            byte[] result = cipher.doFinal(bytes);

            return new String(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method turns byte array to string.
     *
     * @param bytes input byte array
     * @return transformed string
     */
    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; ++i) {
            int value = bytes[i] & 0xff;
            String hexValue = Integer.toHexString(value);
            if (hexValue.length() < 2) {
                result += "0";
            }
            result += hexValue;
        }
        return result;
    }

    /**
     * This method turns a hex string to byte array
     *
     * @param hexString input hex string
     * @return transformed byte array
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return new byte[0];
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            bytes[i] = (byte) (((charToByte(hexChars[pos]) << 4)) & 0xff | (charToByte(hexChars[pos + 1]) & 0xff));
        }
        return bytes;
    }

    /**
     * This method turn char to byte.
     *
     * @param c input character
     * @return transformed byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
