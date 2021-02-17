package me.soo.helloworld.util;

import me.soo.helloworld.model.email.EmailBase;

public class EmailBuilder {

    public final static String FIND_PASSWORD_TITLE = "임시 비밀번호 안내입니다.";

    public final static String FIND_PASSWORD_BODY = "회원님의 임시 비밀번호 입니다. 로그인 후 비밀번호를 변경해주세요";

    public static EmailBase build(String to, String title, String content, String body) {

        String fullBody = new StringBuilder(content).append("\n").append("\n").append(body).toString();

        return EmailBase.builder()
                .to(to)
                .title(title)
                .body(fullBody)
                .build();
    }

}
