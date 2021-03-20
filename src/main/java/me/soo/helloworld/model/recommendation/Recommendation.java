package me.soo.helloworld.model.recommendation;

import lombok.Builder;

@Builder
public class Recommendation {

    private final String from;

    private final String to;

    private final String content;

    public static Recommendation create(String userId, String targetId, String content) {

        return Recommendation.builder()
                        .from(userId)
                        .to(targetId)
                        .content(content)
                        .build();
    }
}
