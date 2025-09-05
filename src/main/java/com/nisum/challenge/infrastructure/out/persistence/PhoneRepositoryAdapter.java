package com.nisum.challenge.infrastructure.out.persistence;

import com.nisum.challenge.application.port.out.PhoneRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class PhoneRepositoryAdapter implements PhoneRepositoryPort {

    private final PhoneJpaRepository jpaRepository;

    public PhoneRepositoryAdapter(PhoneJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByCountryCodeAndCityCodeAndNumber(String countryCode, String cityCode, String number) {
        return jpaRepository.existsByCountryCodeAndCityCodeAndNumber(countryCode, cityCode, number);
    }
}