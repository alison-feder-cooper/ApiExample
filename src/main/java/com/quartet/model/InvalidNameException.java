package com.quartet.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Name must not be blank")
public class InvalidNameException extends RuntimeException {

    private static final long serialVersionUID = -6791497609517443019L;

    public InvalidNameException() {
        super("Name must not be blank");
    }
}
