package me.soo.helloworld.service;

import me.soo.helloworld.enumeration.AlarmTypes;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.RecommendationMapper;
import me.soo.helloworld.model.recommendation.Recommendation;
import me.soo.helloworld.model.recommendation.RecommendationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    private final String userId = "msugo1";

    private final String friendId = "notMsugo1";

    private final int boundaryDuration = 16;

    Validator validator;

    @InjectMocks
    RecommendationService recommendationService;

    @Mock
    FriendService friendService;

    @Mock
    RecommendationMapper recommendationMapper;

    @Mock
    AlarmService alarmService;

    String recommendationContent;

    @BeforeEach
    public void writeRecommendationContent() {
        recommendationContent = "He is such an amazing partner, who has your back at all times. He's a good command of his mother tongue." +
                "So, you won't be disappointed with what he will teach you.";
    }

    @BeforeEach
    public void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /*
        테스트 for 리뷰 등록 (leaveReview)

        1. 친구여부 검사
        2. 리뷰등록 조건 만족 여부 검사
        3. 이미 남겨진 리뷰가 있는지 검사
        4. 모든 조건 통과 후에 리뷰 등록 가능
     */
    @Test
    @DisplayName("추천 글 요청이 지정된 사이즈(2자 이상 ~ 255자 이하)를 지키지 않는 빈칸과 함께 들어온 경우 validation 위반이 발생합니다.")
    public void recommendationRequestMappingFailWithEmptyRecommendationContent() {
        String emptyRecommendationContent = "";
        Set<ConstraintViolation<RecommendationRequest>> violations = validator.validate(new RecommendationRequest(emptyRecommendationContent));
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsOnly("추천 글은 2자 이상 255자 미만으로 작성해주세요.");
    }

    @Test
    @DisplayName("추천 글 요청이 지정된 사이즈(2자 이상 ~ 255자 이하)를 지키지 않는 크기인 1글자로 들어온 경우 validation 위반이 발생합니다.")
    public void recommendationRequestMappingFailWithRightBelowMinimumBoundarySizeRecommendationContent() {
        String emptyRecommendationContent = "한";
        Set<ConstraintViolation<RecommendationRequest>> violations = validator.validate(new RecommendationRequest(emptyRecommendationContent));
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsOnly("추천 글은 2자 이상 255자 미만으로 작성해주세요.");
    }

    @Test
    @DisplayName("추천 글 요청이 지정된 사이즈(2자 이상 ~ 255자 이하)를 지키는 최소값인 2글자로 들어온 경우 요청 매핑에 성공합니다.")
    public void recommendationRequestMappingSuccessWithRightAboveMinimumBoundarySizeRecommendationContent() {
        String recommendationCommentJustUnderBoundary = "감사";

        Set<ConstraintViolation<RecommendationRequest>> violations = validator.validate(new RecommendationRequest(recommendationCommentJustUnderBoundary));
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("추천 글 요청이 지정된 사이즈(2자 이상 ~ 255자 이하)를 지키는 값인 255글자로 들어온 경우 요청 매핑에 성공합니다.")
    public void recommendationRequestMappingSuccessWithRightBelowMaximumBoundarySizeRecommendationContent() {
        // 255자 리뷰
        String recommendationCommentJustUnderBoundary = "이 사이트를 통해서 ~를 만나기 전까지 저는 항상 연습 상대를 찾는데 목말라 있었습니다. 특히 원어민을 상대로" +
                "더 자연스러운 표현을 배우기를 항상 고대하고 있었는데 마침내 필요한 부분이 충족되서 너무 만족합니다. ~는 바쁜 삶을 살고 있지만 자신이 시간이 날 때마다" +
                "꼼꼼한 답변을 주는데 소홀하지 않으며, 제가 실수를 할 때마다 제대로 바로잡고 넘어갈 수 있도록 도와줍니다. 앞으로도 지속적으로 연락을 주고받으면서 서로 목표하는" +
                "유창성에 다가갔으면 좋겠습니다..";

        Set<ConstraintViolation<RecommendationRequest>> violations = validator.validate(new RecommendationRequest(recommendationCommentJustUnderBoundary));
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("추천 글 요청이 지정된 사이즈(2자 이상 ~ 255자 미만)를 넘는 값인 256글자로 들어온 경우 validation 위반이 발생합니다.")
    public void recommendationRequestMappingFailWithOverBoundarySizeRecommendationContent() {
        // 256자 리뷰
        String recommendationCommentJustOverBoundary = "이 사이트를 통해서 ~를 만나기 전까지 저는 항상 연습 상대를 찾는데 목말라 있었습니다. 특히 원어민을 상대로" +
                "더 자연스러운 표현을 배우기를 항상 고대하고 있었는데 마침내 필요한 부분이 충족되서 너무 만족합니다. ~는 바쁜 삶을 살고 있지만 자신이 시간이 날 때마다" +
                "꼼꼼한 답변을 주는데 소홀하지 않으며, 제가 실수를 할 때마다 제대로 바로잡고 넘어갈 수 있도록 도와줍니다. 앞으로도 지속적으로 연락을 주고받으면서 서로 목표하는" +
                "유창성에 다가갔으면 좋겠습니다...";

        Set<ConstraintViolation<RecommendationRequest>> violations = validator.validate(new RecommendationRequest(recommendationCommentJustOverBoundary));
        assertThat(violations).extracting(ConstraintViolation::getMessage).containsOnly("추천 글은 2자 이상 255자 미만으로 작성해주세요.");
    }

    @Test
    @DisplayName("친구가 아닌 대상에게 추천글을 남기는 요청을 보낼 경우 처리에 실패하며 InvalidRequestException 이 발생합니다.")
    public void leaveRecommendationFailToNonFriendUser() {
        doThrow(InvalidRequestException.class).when(friendService).getFriendshipDuration(userId, friendId);

        assertThrows(InvalidRequestException.class, () -> {
            recommendationService.leaveRecommendation(userId, friendId, recommendationContent);
        });

        verify(friendService, times(1)).getFriendshipDuration(userId, friendId);
        verify(recommendationMapper, never()).isRecommendationExist(userId, friendId);
        verify(recommendationMapper, never()).insertRecommendation(Recommendation.create(userId, friendId, recommendationContent));
        verify(alarmService, never()).dispatchAlarm(friendId, userId, AlarmTypes.RECOMMENDATION_LEFT);
    }


    @Test
    @DisplayName("친구 상태가 기준일(16일) 넘게 지속되지 않은 대상에게 추천글을 남기는 요청이 들어오면 처리에 실패하며 InvalidRequestException 이 발생합니다.")
    public void leaveRecommendationFailToFriendBeforeReviewConditionAchieved() {
        int justUnderBoundaryDuration = 15;
        when(friendService.getFriendshipDuration(userId, friendId)).thenReturn(justUnderBoundaryDuration);

        assertThrows(InvalidRequestException.class, () -> {
            recommendationService.leaveRecommendation(userId, friendId, recommendationContent);
        });

        verify(friendService, times(1)).getFriendshipDuration(userId, friendId);
        verify(recommendationMapper, never()).isRecommendationExist(userId, friendId);
        verify(recommendationMapper, never()).insertRecommendation(Recommendation.create(userId, friendId, recommendationContent));
        verify(alarmService, never()).dispatchAlarm(friendId, userId, AlarmTypes.RECOMMENDATION_LEFT);
    }

    @Test
    @DisplayName("추천글을 남기기 위한 친구 기간 기준을 만족했더라도 이미 남겨진 추천글이 있으면 추천글을 남기는데 실패하며 DuplicateRequestException 이 발생합니다.")
    public void leaveRecommendationFailToFriendToWhomReviewHasAlreadyBeenLeft() {
        when(friendService.getFriendshipDuration(userId, friendId)).thenReturn(boundaryDuration);
        when(recommendationMapper.isRecommendationExist(userId, friendId)).thenReturn(true);

        assertThrows(DuplicateRequestException.class, () -> {
            recommendationService.leaveRecommendation(userId, friendId, recommendationContent);
        });

        verify(friendService, times(1)).getFriendshipDuration(userId, friendId);
        verify(recommendationMapper, times(1)).isRecommendationExist(userId, friendId);
        verify(recommendationMapper, never()).insertRecommendation(Recommendation.create(userId, friendId, recommendationContent));
        verify(alarmService, never()).dispatchAlarm(friendId, userId, AlarmTypes.RECOMMENDATION_LEFT);
    }

    @Test
    @DisplayName("추천글을 남기기 위한 기준 - 16일 이상 친구로서 기존에 남긴 추천글이 없을 것 - 을 모두 만족하는 경우 추천글을 남기는데 성공하며 대상에게는 추천글 등록 알림을 보냅니다.")
    public void leaveRecommendationSuccessWithAllConditionsAreMet() {
        when(friendService.getFriendshipDuration(userId, friendId)).thenReturn(boundaryDuration);
        when(recommendationMapper.isRecommendationExist(userId, friendId)).thenReturn(false);

        recommendationService.leaveRecommendation(userId, friendId, recommendationContent);

        verify(friendService, times(1)).getFriendshipDuration(userId, friendId);
        verify(recommendationMapper, times(1)).isRecommendationExist(userId, friendId);
        verify(recommendationMapper, times(1)).insertRecommendation(refEq(Recommendation.create(userId, friendId, recommendationContent)));
        verify(alarmService, times(1)).dispatchAlarm(friendId, userId, AlarmTypes.RECOMMENDATION_LEFT);
    }
}