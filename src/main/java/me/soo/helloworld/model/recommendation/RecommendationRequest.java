package me.soo.helloworld.model.recommendation;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {

    @Size(min = 2, max = 255, message = "추천 글은 2자 이상 255자 미만으로 작성해주세요.")
    private String content;
}
