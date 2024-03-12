package com.example.api.owner.dto.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusinessRegistrationNumberCheckValidator.class)
public @interface BusinessRegistrationNumberCheck {

    String message() default "잘못된 사업장등록번호입니다.";

    String regexp() default "^\\d{3}-\\d{2}-\\d{5}$";

    Class[] groups() default {};

    Class[] payload() default {};
}
