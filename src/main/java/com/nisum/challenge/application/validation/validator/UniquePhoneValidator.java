package com.nisum.challenge.application.validation.validator;

import com.nisum.challenge.application.dto.PhoneDTO;
import com.nisum.challenge.application.validation.annotation.UniquePhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, List<PhoneDTO>> {

    @Override
    public boolean isValid(List<PhoneDTO> phones, ConstraintValidatorContext context) {
        if (phones == null || phones.isEmpty()) {
            return true;
        }

        Set<String> uniquePhones = new HashSet<>();
        for (PhoneDTO phone : phones) {
            String fullNumber = phone.getCountryCode() + phone.getCityCode() + phone.getNumber();
            if (!uniquePhones.add(fullNumber)) {
                return false;
            }
        }

        return true;
    }
}