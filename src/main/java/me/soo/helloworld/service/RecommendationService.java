package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.AlarmTypes;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.RecommendationMapper;
import me.soo.helloworld.model.recommendation.RecommendationDataForProfile;
import me.soo.helloworld.model.recommendation.Recommendation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    public static final int MINIMUM_DURATION_BOUNDARY = 16;

    public static final int MAXIMUM_HOW_LONG_BOUNDARY = 2;

    private final FriendService friendService;

    private final RecommendationMapper recommendationMapper;

    private final AlarmService alarmService;

    @Transactional
    public void leaveRecommendation(String from, String to, String content) {
        int friendshipDuration = friendService.getFriendshipDuration(from, to);

        validateLeaveRecommendationAvailability(friendshipDuration);
        checkDuplicateRecommendation(from, to);

        recommendationMapper.insertRecommendation(Recommendation.create(from, to, content));
        alarmService.dispatchAlarm(to, from, AlarmTypes.RECOMMENDATION_LEFT);
    }

    public void modifyRecommendation(int id, String from, String modifiedContent) {
        int howLong = howLongSinceWrittenAt(id, from);
        validateModificationAvailability(howLong);
        recommendationMapper.updateRecommendation(id, from, modifiedContent);
    }

    public List<RecommendationDataForProfile> getRecommendationsByUserId(String userId) {
        return recommendationMapper.getRecommendationsByUserId(userId);
    }

    public int howLongSinceWrittenAt(int id, String from) {
        return recommendationMapper.getHowLongSinceWrittenAt(id, from)
                                    .orElseThrow(() -> new InvalidRequestException("해당 추천글이 존재하지 않습니다. 존재하지 않는 추천글은 수정할 수 없습니다."));
    }

    private void validateLeaveRecommendationAvailability(int currentDuration) {
        if (!(currentDuration >= MINIMUM_DURATION_BOUNDARY)) {
            throw new InvalidRequestException("추천글 작성을 위한 기본 조건을 충족하지 못하셨습니다. 추천글은 친구로 맺어진 날부터 16일 째 되는날 작성이 가능해집니다.");
        }
    }

    private void validateModificationAvailability(int currentHowLong) {
        if (!(currentHowLong <= MAXIMUM_HOW_LONG_BOUNDARY)) {
            throw new InvalidRequestException("추천글 수정은 오직 48시간(2일) 내에만 가능합니다. 해당 추천글은 이미 수정가능 기간을 초과하였습니다.");
        }
    }

    private void checkDuplicateRecommendation(String from, String to) {
        if (recommendationMapper.isRecommendationExist(from, to)) {
            throw new DuplicateRequestException("이미 추천글 작성을 마치셨습니다. 각 개별 사용자에 대한 추천글 작성은 단 한번만 가능합니다.");
        }
    }

}
