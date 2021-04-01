package me.soo.helloworld.model.recommendation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.time.LocalDate;

@Value
public class Recommendations {

    int id;

    String from;

    String content;

    LocalDate writtenAt;
}
