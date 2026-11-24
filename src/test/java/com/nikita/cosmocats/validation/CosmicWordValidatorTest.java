package com.nikita.cosmocats.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CosmicWordValidatorTest {

    private final CosmicWordValidator validator = new CosmicWordValidator();

    @ParameterizedTest
    @ValueSource(strings = {"Star Catnip", "Galaxy Toy", "Comet Laser", "space helmet"})
    void isValidShouldReturnTrueForCosmicWords(String value) {
        assertTrue(validator.isValid(value, null));
    }

    @Test
    void isValidShouldReturnFalseWhenCosmicWordMissing() {
        assertFalse(validator.isValid("Regular Milk", null));
    }

    @Test
    void isValidShouldReturnFalseWhenValueIsNullOrBlank() {
        assertFalse(validator.isValid(null, null));
        assertFalse(validator.isValid("   ", null));
    }
}