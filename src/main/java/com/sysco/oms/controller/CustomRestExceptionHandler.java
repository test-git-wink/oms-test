package com.sysco.oms.controller;

import com.sysco.oms.model.responses.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler  extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> build(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        List<String> details = new ArrayList<>();
        details.add(ex.getParameterName() + " parameter is missing");

        ApiError err = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, "Missing Parameters" ,details);

        return build(err);
    }
}
