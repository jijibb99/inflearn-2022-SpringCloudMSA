package com.example.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
//    private UserService userService;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    private Environment env;
//
//    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.env = env;
//        this.userService = userService;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

    /**
     * 권한 작업용 : 인증이 통과이후 할 수 있는 작업 정의
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users/**").permitAll();     // "/users/**"는 모든 사용자에게 권한 부여
        // 이 부분이 없으면 h2-conolse과 같이 frame기반은 서비스 되지 않음
        http.headers().frameOptions().disable();
    }


}
