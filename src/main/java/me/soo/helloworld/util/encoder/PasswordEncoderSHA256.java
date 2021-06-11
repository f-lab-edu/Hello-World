package me.soo.helloworld.util.encoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Primary
@Component
public class PasswordEncoderSHA256 implements PasswordEncoder {

    public static final String ENCRYPTION_ALGORITHM = "SHA-256";

    public String encode(String rawPassword) {
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

    @Override
    public boolean isMatch(String rawPassword, String encodedPassword) {
        return StringUtils.equals(encode(rawPassword), encodedPassword);
    }
}
