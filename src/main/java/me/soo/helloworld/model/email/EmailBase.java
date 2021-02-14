package me.soo.helloworld.model.email;

import lombok.Getter;

@Getter
public abstract class EmailBase {

    String title;

    String body;

    abstract String writeBody(String content);
}
