package com.drivefleet.drivefleet.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundIdException extends DomainException {

    public UserNotFoundIdException(String id) {
        super(
                "USER-NOT-FOUND-" + HttpStatus.NOT_FOUND.value(),
                "User with id " + id + " not found",
                HttpStatus.NOT_FOUND
        );
    }
}
