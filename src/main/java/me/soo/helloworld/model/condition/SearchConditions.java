package me.soo.helloworld.model.condition;

import lombok.Builder;
import lombok.Getter;
import me.soo.helloworld.util.Pagination;

import static me.soo.helloworld.util.validator.AgeRangeValidator.MAX_AGE_RANGE;
import static me.soo.helloworld.util.validator.AgeRangeValidator.MIN_AGE_RANGE;

@Getter
@Builder
public class SearchConditions {

    SearchConditionsRequest conditions;

    String currentUser;

    Pagination pagination;

    int defaultMaxAge;

    int defaultMinAge;

    public static SearchConditions create(SearchConditionsRequest conditionsRequest, String userId, Pagination pagination) {

        return SearchConditions.builder()
                                .conditions(conditionsRequest)
                                .currentUser(userId)
                                .pagination(pagination)
                                .defaultMaxAge(MAX_AGE_RANGE)
                                .defaultMinAge(MIN_AGE_RANGE)
                                .build();
    }
}
