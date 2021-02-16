package me.soo.helloworld.model.email;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailBase {

    private final String to;

    private final String title;

    private final String body;

}
