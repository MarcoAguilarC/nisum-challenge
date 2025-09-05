package com.nisum.challenge.infrastructure.out.persistence;

import com.nisum.challenge.domain.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneJpaRepository extends JpaRepository<Phone, Long> {
    boolean existsByCountryCodeAndCityCodeAndNumber(String countryCode, String cityCode, String number);
}