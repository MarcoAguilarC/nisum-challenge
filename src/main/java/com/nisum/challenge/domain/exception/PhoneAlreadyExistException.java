package com.nisum.challenge.domain.exception;

import lombok.Getter;

@Getter
public class PhoneAlreadyExistException extends RuntimeException {

    private final transient Object[] args;

    public PhoneAlreadyExistException(String message, Object... args) {
        super(message);
        this.args = args;
    }
}
