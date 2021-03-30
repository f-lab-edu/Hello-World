package me.soo.helloworld.model.recommendation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class Recommendations {

    int id;

    String from;

    String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd")
    Timestamp writtenAt;
}
