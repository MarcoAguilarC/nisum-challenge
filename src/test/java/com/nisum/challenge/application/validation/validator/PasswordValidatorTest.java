package com.nisum.challenge.application.validation.validator;

import com.nisum.challenge.application.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "passwordRegex", "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
    }

    @Test
    void isValid_validPassword_returnsTrue() {
        assertTrue(passwordValidator.isValid("Password123@", constraintValidatorContext));
    }

    @Test
    void isValid_passwordTooShort_returnsFalse() {
        assertFalse(passwordValidator.isValid("Pass1@", constraintValidatorContext));
    }

    @Test
    void isValid_passwordTooLong_returnsFalse() {
        assertFalse(passwordValidator.isValid("Password123456789012345@", constraintValidatorContext));
    }

    @Test
    void isValid_noDigit_returnsFalse() {
        assertFalse(passwordValidator.isValid("Password@", constraintValidatorContext));
    }

    @Test
    void isValid_noLowercase_returnsFalse() {
        assertFalse(passwordValidator.isValid("PASSWORD123@", constraintValidatorContext));
    }

    @Test
    void isValid_noUppercase_returnsFalse() {
        assertFalse(passwordValidator.isValid("password123@", constraintValidatorContext));
    }

    @Test
    void isValid_noSpecialChar_returnsFalse() {
        assertFalse(passwordValidator.isValid("Password123", constraintValidatorContext));
    }

    @Test
    void isValid_containsWhitespace_returnsFalse() {
        assertFalse(passwordValidator.isValid("Pass word123@", constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_returnsFalse() {
        assertFalse(passwordValidator.isValid(null, constraintValidatorContext));
    }
}
