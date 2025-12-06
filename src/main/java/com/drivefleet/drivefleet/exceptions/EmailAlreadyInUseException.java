package com.drivefleet.drivefleet.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyInUseException extends DomainException{

    public EmailAlreadyInUseException(String email) {
        super(
                "PARAM-CONFLICT-" + HttpStatus.CONFLICT.value(),
                "User with email " + email + " already exists",
                HttpStatus.CONFLICT
        );
    }
}
