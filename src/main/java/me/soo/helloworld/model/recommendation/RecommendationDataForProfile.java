package me.soo.helloworld.model.recommendation;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RecommendationDataForProfile {

    private String from;

    private String content;
}
