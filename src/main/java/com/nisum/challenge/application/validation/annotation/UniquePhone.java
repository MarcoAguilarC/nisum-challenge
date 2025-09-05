package com.nisum.challenge.application.validation.annotation;

import com.nisum.challenge.application.validation.validator.UniquePhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePhoneValidator.class)
public @interface UniquePhone {
    String message() default "The request contains duplicate phone numbers.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
