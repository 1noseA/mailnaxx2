package com.mailnaxx2.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
//log出力用
@Slf4j
public class ErrorController {
    @ExceptionHandler
    public String handleException(Throwable e) {
        log.error("System Error occurred.", e);
        e.printStackTrace();
        return "error/error";
    }
}
