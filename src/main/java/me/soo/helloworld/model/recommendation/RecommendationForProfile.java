package me.soo.helloworld.model.recommendation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RecommendationForProfile {

    private String from;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd")
    private LocalDate writtenAt;
}
