package me.soo.helloworld.model.recommendation;

import lombok.Value;

import java.time.LocalDate;

@Value
public class Recommendations {

    int id;

    String from;

    String content;

    LocalDate writtenAt;
}
