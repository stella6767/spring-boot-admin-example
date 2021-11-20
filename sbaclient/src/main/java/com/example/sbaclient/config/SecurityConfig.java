package com.example.sbaclient.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity //시큐리티 활성화
@Configuration // ioc 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final HttpSession session;


    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(EndpointRequest.toAnyEndpoint());
                //.mvcMatchers("**/*/js")
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())//@CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                .and()
                .csrf().disable() //rest api이므로 csrf 보안이 필요없으므로 disable처리.

                .formLogin()
                .disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// STATELESS: session을 사용하지 않겠다는 의미

                .and()


                .exceptionHandling()

                .and()
                .authorizeRequests()
                .antMatchers("/user/**").access("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .logout()
                .logoutUrl("/logout.do")
        ;
        //xss csrx


    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //내서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.addAllowedOriginPattern("*"); //모든 ip에 응답을 허용하겠다.
        config.addAllowedMethod("*"); // 모든 post,get,put,delete, patch ..요청을 허용하겠다.
        config.addAllowedHeader("*"); //모든 header에 응답을 허용하겠다.
        //config.addAllowedOrigin("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
