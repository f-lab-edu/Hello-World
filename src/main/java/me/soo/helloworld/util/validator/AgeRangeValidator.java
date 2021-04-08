package me.soo.helloworld.util.validator;

import me.soo.helloworld.annotation.AgeRange;
import me.soo.helloworld.model.condition.SearchConditionsRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class AgeRangeValidator implements ConstraintValidator<AgeRange, SearchConditionsRequest> {

    public static final int MAX_AGE_BOUND = 99;

    public static final int MIN_AGE_BOUND = 10;

    @Override
    public boolean isValid(SearchConditionsRequest condition, ConstraintValidatorContext context) {
        Integer minAge = condition.getMinAge();
        Integer maxAge = condition.getMaxAge();

        if (isBothAgesNull(minAge, maxAge)) return true;
        else if (minAge == null) return isTheOtherAgeValid(maxAge);
        else if (maxAge == null) return isTheOtherAgeValid(minAge);
        else return isBothAgesValid(minAge, maxAge);
    }

    private boolean isBothAgesNull(Integer minAge, Integer maxAge) {
        return minAge == null && maxAge == null;
    }

    private boolean isTheOtherAgeValid(Integer age) {
        return MIN_AGE_BOUND <= age && age <= MAX_AGE_BOUND;
    }

    private boolean isBothAgesValid(Integer minAge, Integer maxAge) {
        return MIN_AGE_BOUND <= minAge && minAge <= maxAge && maxAge <= MAX_AGE_BOUND;
    }
}
