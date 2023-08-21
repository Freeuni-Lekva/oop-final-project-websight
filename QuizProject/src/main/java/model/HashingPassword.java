package model;


import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;

public class HashingPassword {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int SALT = 32;
    public static final int HASH = 64;
    public static final int COOKIE_BYTE_SIZE = 64;
    public static final int PBKDF2IT = 1000;
    public static final int INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
    
    public static String createHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    public static String createHash(char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[SALT];
        r.nextBytes(salt);
        byte[] hash = pbkdf2(password, salt, PBKDF2IT, HASH);
        return PBKDF2IT + ":" + toHex(salt) + ":" +  toHex(hash);
    }
    
    public static boolean PasswordValidation(String password, String correctHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return PasswordValidation(password.toCharArray(), correctHash);
    }
    
    public static boolean PasswordValidation(char[] password, String correctHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] params = correctHash.split(":");
        int iterations = Integer.parseInt(params[INDEX]);
        byte[] salt = fromHex(params[SALT_INDEX]);
        byte[] hash = fromHex(params[PBKDF2_INDEX]);
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        return slowEquals(hash, testHash);
    }
    
    private static boolean slowEquals(byte[] a, byte[] b) {
        int d = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            d |= a[i] ^ b[i];
        return d == 0;
    }
    
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }
    
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    public static String createCookie() {
        SecureRandom random = new SecureRandom();
        byte[] cookie = new byte[SALT];
        random.nextBytes(cookie);

        return toHex(cookie);
    } 
    
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int pad = (array.length * 2) - hex.length();
        if(pad > 0) return String.format("%0" + pad + "d", 0) + hex;
        else return hex;
    }
    
    public static void main(String[] args) {
        try {
            for(int i = 0; i < 10; i++)
                System.out.println(HashingPassword.createHash("p\r\nassw0Rd!"));

            boolean mistake = false;
            System.out.println("Running tests...");
            for(int i = 0; i < 100; i++) {
                String password = ""+i;
                String hash = createHash(password);
                String secondHash = createHash(password);
                if(hash.equals(secondHash)) {
                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
                    mistake = true;
                }
                String wrongPassword = ""+(i+1);
                if(PasswordValidation(wrongPassword, hash)) {
                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
                    mistake = true;
                }
                if(!PasswordValidation(password, hash)) {
                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                    mistake = true;
                }
            }
            if(mistake) System.out.println("TESTS FAILED!");
            else System.out.println("TESTS PASSED!");
        }
        catch(Exception ex) {
            System.out.println("ERROR: " + ex);
        }
    }
}
