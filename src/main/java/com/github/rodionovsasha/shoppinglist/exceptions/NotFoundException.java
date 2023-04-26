package com.github.rodionovsasha.shoppinglist.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public final class NotFoundException extends RuntimeException {
    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException forId(long id) {
        return new NotFoundException(String.format("The entity with id '%d' could not be found", id));
    }
}
