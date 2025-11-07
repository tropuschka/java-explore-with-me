package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionMainService {
    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse throwBadRequest(final BadRequestException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse throwNotFound(final NotFoundException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse throwConflict(final ConflictException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse throwForbidden(final ForbiddenException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.FORBIDDEN, e.getMessage());
    }
}
