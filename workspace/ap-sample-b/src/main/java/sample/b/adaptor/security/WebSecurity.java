package sample.b.adaptor.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/api/v2/**", "/health", "swagger-ui.html", "/swagger/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable()
//                .formLogin();
//    }

    /**
     * 권한 작업용 : 인증이 통과이후 할 수 있는 작업 정의
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("WebSecurity.configure()");
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();

        http.authorizeRequests().antMatchers("/api/v2/**", "/health", "swagger-ui.html",
                "/swagger/**","swagger-resources/**", "webjars/**", "v2/api-docs").permitAll();
        //임시로 허용
        http.authorizeRequests().antMatchers("/sampleb/**").permitAll();


       http.headers().frameOptions().disable();
    }

}
