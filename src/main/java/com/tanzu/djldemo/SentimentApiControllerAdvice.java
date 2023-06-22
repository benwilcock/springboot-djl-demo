package com.tanzu.djldemo;

import java.io.IOException;

import javax.naming.ServiceUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;

@ControllerAdvice
public class SentimentApiControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(SentimentApiControllerAdvice.class);


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Model is malformed")
    @ExceptionHandler(MalformedModelException.class)
    public void handleException(MalformedModelException e) {
        LOG.error("There was a problem performing the sentiment analysis.", e);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Model was not found")
    @ExceptionHandler(ModelNotFoundException.class)
    public void handleException(ModelNotFoundException e) {
        LOG.error("There was a problem performing the sentiment analysis.", e);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Could not analyze the sentence.")
    @ExceptionHandler(TranslateException.class)
    public void handleException(TranslateException e) {
        LOG.error("There was a problem performing the sentiment analysis.", e);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was an IO problem.")
    @ExceptionHandler(IOException.class)
    public void handleException(IOException e) {
        LOG.error("There was a problem performing the sentiment analysis.", e);
    }
}
