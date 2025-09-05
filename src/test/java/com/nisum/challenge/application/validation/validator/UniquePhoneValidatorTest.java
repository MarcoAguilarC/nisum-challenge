package com.nisum.challenge.application.validation.validator;

import com.nisum.challenge.application.dto.PhoneDTO;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UniquePhoneValidatorTest {

    private UniquePhoneValidator validator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        validator = new UniquePhoneValidator();
    }

    @Test
    void isValid_nullPhones_returnsTrue() {
        assertTrue(validator.isValid(null, constraintValidatorContext));
    }

    @Test
    void isValid_emptyPhones_returnsTrue() {
        assertTrue(validator.isValid(Collections.emptyList(), constraintValidatorContext));
    }

    @Test
    void isValid_uniquePhones_returnsTrue() {
        PhoneDTO phone1 = new PhoneDTO("12345678", "9", "56");
        PhoneDTO phone2 = new PhoneDTO("23456789", "9", "56");
        List<PhoneDTO> phones = Arrays.asList(phone1, phone2);

        assertTrue(validator.isValid(phones, constraintValidatorContext));
    }

    @Test
    void isValid_duplicatePhones_returnsFalse() {
        PhoneDTO phone1 = new PhoneDTO("12345678", "9", "56");
        PhoneDTO phone2 = new PhoneDTO("12345678", "9", "56");
        List<PhoneDTO> phones = Arrays.asList(phone1, phone2);

        assertFalse(validator.isValid(phones, constraintValidatorContext));

        List<PhoneDTO> phones2 = Arrays.asList(phone2, phone1);

        assertFalse(validator.isValid(phones2, constraintValidatorContext));
    }

    @Test
    void isValid_phonesWithSomeDuplicates_returnsFalse() {
        PhoneDTO phone1 = new PhoneDTO("111", "222", "333");
        PhoneDTO phone2 = new PhoneDTO("444", "555", "666");
        PhoneDTO phone3 = new PhoneDTO("111", "222", "333");
        List<PhoneDTO> phones = Arrays.asList(phone1, phone2, phone3);

        assertFalse(validator.isValid(phones, constraintValidatorContext));
    }
}
