package me.soo.helloworld.model.recommendation;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RecommendationForProfile {

    private String from;

    private String content;

    private LocalDate writtenAt;
}
