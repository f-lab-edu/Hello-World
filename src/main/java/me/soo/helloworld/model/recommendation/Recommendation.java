package me.soo.helloworld.model.recommendation;

import lombok.Builder;

@Builder
public class Recommendation {

    String from;

    String to;

    String content;

    public static Recommendation create(String userId, String targetId, String content) {

        return Recommendation.builder()
                        .from(userId)
                        .to(targetId)
                        .content(content)
                        .build();
    }
}
