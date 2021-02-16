package me.soo.helloworld.util;

import me.soo.helloworld.model.email.EmailBase;

public class EmailBuilder {

    public static EmailBase build(String to, String title, String content) {

        return EmailBase.builder()
                .to(to)
                .title(title)
                .body(content)
                .build();
    }

}
