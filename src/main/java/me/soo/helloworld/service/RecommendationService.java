package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.AlarmTypes;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.RecommendationMapper;
import me.soo.helloworld.model.recommendation.Recommendation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    public static final int MINIMUM_DURATION_BOUNDARY = 16;

    private final FriendService friendService;

    private final RecommendationMapper recommendationMapper;

    private final AlarmService alarmService;

    @Transactional
    public void leaveRecommendation(String from, String to, String content) {
        int friendshipDuration = friendService.getFriendshipDuration(from, to);

        validateOverMinimumDuration(friendshipDuration);
        checkDuplicateRecommendation(from, to);

        recommendationMapper.insertRecommendation(Recommendation.create(from, to, content));
        alarmService.dispatchAlarm(to, from, AlarmTypes.RECOMMENDATION_LEFT);
    }

    private void validateOverMinimumDuration(int currentDuration) {
        if (!(currentDuration >= MINIMUM_DURATION_BOUNDARY)) {
            throw new InvalidRequestException("추천글 작성을 위한 기본 조건을 충족하지 못하셨습니다. 추천글은 친구로 맺어진 날부터 16일 째 되는날 작성이 가능해집니다.");
        }
    }

    private void checkDuplicateRecommendation(String from, String to) {
        if (recommendationMapper.isRecommendationExist(from, to)) {
            throw new DuplicateRequestException("이미 추천글 작성을 마치셨습니다. 각 개별 사용자에 대한 추천글 작성은 단 한번만 가능합니다.");
        }
    }

}
