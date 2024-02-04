package com.tallink.trainings.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CourseException extends RuntimeException {
    public CourseException(String message) {
        super(message);
    }
}
