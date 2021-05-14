package me.soo.helloworld.util.encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoderSHA256 {

    public static final String ENCRYPTION_ALGORITHM = "SHA-256";

    public static String encode(String rawPassword) {
        String sha = "";

        try {
            MessageDigest sh = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            sh.update(rawPassword.getBytes());
            byte[] byteData = sh.digest();
            StringBuilder sb = new StringBuilder();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            sha = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("적용하신 암호화 알고리즘이 존재하지 않습니다.", e);
        }

        return sha;
    }
}
