package me.soo.helloworld.util.validator;

import me.soo.helloworld.annotation.AgeRange;
import me.soo.helloworld.model.condition.SearchConditionsRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class AgeRangeValidator implements ConstraintValidator<AgeRange, SearchConditionsRequest> {

    public static final int MAX_AGE_RANGE = 99;

    public static final int MIN_AGE_RANGE = 10;

    @Override
    public boolean isValid(SearchConditionsRequest condition, ConstraintValidatorContext context) {
        if (condition.getMinAge() == null && condition.getMaxAge() == null) {
            return true;
        } else if (condition.getMaxAge() == null) {
            return MIN_AGE_RANGE <= condition.getMinAge() && condition.getMinAge() <= MAX_AGE_RANGE;
        } else if (condition.getMinAge() == null) {
            return  MIN_AGE_RANGE <= condition.getMaxAge() && condition.getMaxAge() <= MAX_AGE_RANGE;
        } else {
            return MIN_AGE_RANGE <= condition.getMinAge()
                    && condition.getMinAge() <= condition.getMaxAge()
                    && condition.getMaxAge() <= MAX_AGE_RANGE;
        }
    }
}
