package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 권한 작업용 : 인증이 통과이후 할 수 있는 작업 정의
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("WebSecurity.configure()");
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();     // "/users/**"는 모든 사용자에게 권한 부여
        // 이 부분이 없으면 h2-conolse과 같이 frame기반은 서비스 되지 않음

        http.authorizeRequests().antMatchers("/**")
//                .hasIpAddress(env.getProperty("gateway.ip")) // <- IP 변경
//                .hasIpAddress("127.0.0.1") // <- IP 변경  (직접은 되는데, Eureka를 경유하면 오류)
//                .hasIpAddress("10.200.0.1")
                .hasIpAddress("192.168.0.15")
                .and()
                .addFilter(getAuthenticationFilter());   //아래 메소드
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        log.info("WebSecurity.AuthenticationFilter()");
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(), userService, env);
//        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }

    // select pwd from users where email=?
    // db_pwd(encrypted) == input_pwd(encrypted)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("WebSecurity.configure()");
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);  
        //userService에서  "UserDetailsService"을 상속 받아야 함
    }
}
