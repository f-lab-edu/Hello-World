package me.soo.helloworld.util.encoder;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderBcrypt implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(0));
    }

    @Override
    public boolean isMatch(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
