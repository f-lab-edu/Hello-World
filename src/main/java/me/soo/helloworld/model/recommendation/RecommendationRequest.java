package me.soo.helloworld.model.recommendation;

import lombok.*;

import javax.validation.constraints.Size;

// 빈 객체만 만들어지는 불상사를 막기 위해 NoArgs 생성자의 AccessLevel 을 PRIVATE 로 설정
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RecommendationRequest {

    @Size(min = 2, max = 255, message = "추천 글은 2자 이상 255자 미만으로 작성해주세요.")
    private String content;
}
