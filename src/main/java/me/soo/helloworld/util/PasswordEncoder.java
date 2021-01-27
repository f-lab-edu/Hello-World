package me.soo.helloworld.util;

/**
 * 차후 확장성 혹은 교체 등을 고려해 비밀번호 암호화에 추상화 계층 도입
 */
public interface PasswordEncoder {
    public String encode(String rawPassword);
    public boolean isMatch(String rawPassword, String encodedPassword);
}
