package me.soo.helloworld.model.email;

public class FindPasswordEmail {

    public final static String FIND_PASSWORD_TITLE = "임시 비밀번호 안내입니다.";

    public final static String FIND_PASSWORD_BODY = "회원님의 임시 비밀번호 입니다. 로그인 후 비밀번호를 변경해주세요 \n \n";

    static public EmailBase create(String to, String temporaryPassword) {

        return EmailBase.builder()
                .to(to)
                .title(FIND_PASSWORD_TITLE)
                .body(FIND_PASSWORD_BODY + temporaryPassword)
                .build();
    }
}
