package com.example.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service")
@Slf4j
public class SecondServiceController {
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Second service.";
    }


    /**
     *  Spring Cloud Gateway – Filter
     *  - Gateway 필터에서 해더변경한 내역 확인용도
     *  - com.example.apigatewayservice.config.FilterConfig 확인
     */
    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header) {
        log.info("==@RequestHeader(\"second-request\") ==> {}" , header);
        return "Hello World in Second Service.";
    }


    @GetMapping("/check")
    public String check() {
        log.info("==check ...");
        return "Hello World in Second Service Check.";
    }

}
