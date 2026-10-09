package com.nikita.cosmocats.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {
    private static final List<String> COSMIC_WORDS = List.of("star", "galaxy", "comet", "space");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return COSMIC_WORDS.stream().anyMatch(word -> value.toLowerCase().contains(word));
    }
}