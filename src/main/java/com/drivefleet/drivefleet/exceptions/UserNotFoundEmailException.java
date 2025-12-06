package com.drivefleet.drivefleet.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundEmailException extends DomainException {

    public UserNotFoundEmailException(String email) {
        super(
                "USER-NOT-FOUND-" + HttpStatus.NOT_FOUND.value(),
                "User with email " + email + " not found",
                HttpStatus.NOT_FOUND
        );
    }
}
