package me.soo.helloworld.annotation;

import me.soo.helloworld.util.validator.AgeRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AgeRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeRange {

    String message() default "나이를 검색조건으로 지정할 때, 최대나이는 100살 이상, 최소나이는 9살 이하로 내려갈 수 없으며, " +
            "최소 나이는 항상 최대 나이보다 작은 값을 유지해야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
