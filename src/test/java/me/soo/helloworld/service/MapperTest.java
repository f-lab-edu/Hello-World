package me.soo.helloworld.service;

import lombok.extern.slf4j.Slf4j;
import me.soo.helloworld.model.recommendation.RecommendationDataForProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@SpringBootTest
public class MapperTest {

    @Autowired
    RecommendationService recommendationService;

    @Test
    public void test() {
        List<RecommendationDataForProfile> recommendations = recommendationService.getRecommendationsForProfile("se");
        log.warn(String.valueOf(recommendations));
        assertNull(recommendations);
    }
}
