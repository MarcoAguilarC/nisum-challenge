package com.nisum.challenge.application.port.out;

public interface PhoneRepositoryPort {
    boolean existsByCountryCodeAndCityCodeAndNumber(String countryCode, String cityCode, String number);
}