package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.recommendation.RecommendationRequest;
import me.soo.helloworld.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @LoginRequired
    @PostMapping("/to/{targetId}")
    public void leaveReview(@CurrentUser String userId,
                            @PathVariable String targetId,
                            @Valid @RequestBody RecommendationRequest recommendationRequest) {
        recommendationService.leaveRecommendation(userId, targetId, recommendationRequest.getContent());
    }
}
