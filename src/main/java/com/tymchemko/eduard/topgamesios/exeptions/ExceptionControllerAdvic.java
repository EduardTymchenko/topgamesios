package com.tymchemko.eduard.topgamesios.exeptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvic extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvic.class);

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<String> handleRedisConnectionFailureException() {
        String logErrMess = "**!!!** Database connection error! Request from client.";
        LOG.error(logErrMess);
        return new ResponseEntity<>("Database connection error! Try later, please.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
