package com.nikita.cosmocats.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmicWordValidator.class)
public @interface CosmicWordCheck {
    String message() default "Product name must contain cosmic terms (star, galaxy, comet, space)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}