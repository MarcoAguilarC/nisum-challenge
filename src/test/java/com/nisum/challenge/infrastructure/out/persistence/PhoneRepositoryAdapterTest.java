package com.nisum.challenge.infrastructure.out.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneRepositoryAdapterTest {

    @Mock
    private PhoneJpaRepository phoneJpaRepository;

    @InjectMocks
    private PhoneRepositoryAdapter phoneRepositoryAdapter;

    @Test
    void existsByCountryCodeAndCityCodeAndNumber_delegatesToJpaRepository() {
        String countryCode = "56";
        String cityCode = "9";
        String number = "12345678";

        when(phoneJpaRepository.existsByCountryCodeAndCityCodeAndNumber(countryCode, cityCode, number)).thenReturn(true);

        boolean exists = phoneRepositoryAdapter.existsByCountryCodeAndCityCodeAndNumber(countryCode, cityCode, number);

        assertTrue(exists);
    }
}
