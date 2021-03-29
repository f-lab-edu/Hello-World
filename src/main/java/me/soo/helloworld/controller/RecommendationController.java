package me.soo.helloworld.controller;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.model.recommendation.RecommendationRequest;
import me.soo.helloworld.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @ResponseStatus(HttpStatus.CREATED)
    @LoginRequired
    @PostMapping("/to/{targetId}")
    public void leaveReview(@CurrentUser String userId,
                            @PathVariable String targetId,
                            @Valid @RequestBody RecommendationRequest recommendationRequest) {
        recommendationService.leaveRecommendation(userId, targetId, recommendationRequest.getContent());
    }

    @LoginRequired
    @PutMapping("/to/{targetId}")
    public void modifyReview(@PathVariable("targetId") String targetId,
                             @CurrentUser String userId,
                             @Valid @RequestBody RecommendationRequest modificationRequest) {
        recommendationService.modifyRecommendation(targetId, userId, modificationRequest.getContent());
    }
}
