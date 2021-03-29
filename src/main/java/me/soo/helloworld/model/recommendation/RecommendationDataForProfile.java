package me.soo.helloworld.model.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecommendationDataForProfile {

    private final String from;

    private final String content;
}
