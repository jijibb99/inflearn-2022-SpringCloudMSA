package com.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;

@RestController
@RequestMapping("/first-service")
@Slf4j
public class FirstServiceController {
//    Environment env;
//
//    @Autowired
//    public FirstServiceController(Environment env) {
//        this.env = env;
//    }

    @GetMapping("/welcome")
    public String welcome() {
        log.info("welcome()");
        return "Welcome to the First service.";
    }

    /**
     *  Spring Cloud Gateway – Filter
     *  - Gateway 필터에서 해더변경한 내역 확인용도
     *  - com.example.apigatewayservice.config.FilterConfig 확인
     */
    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header) {
        log.info("==@RequestHeader(\"first-request\") ==> {}" , header);
        return "Hello World in First Service.";
    }

    @GetMapping("/check")
    public String check() {
        log.info("==check ...");
        return "Hello World in First Service Check.";
    }


}
