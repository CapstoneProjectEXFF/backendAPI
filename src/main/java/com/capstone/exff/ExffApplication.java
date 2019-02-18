package com.capstone.exff;

import com.capstone.exff.filters.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ExffApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExffApplication.class, args);
    }
    @Bean
    public FilterRegistrationBean<JwtFilter> loggingFilter(){
        FilterRegistrationBean registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/user/*", "/relationship");
        registrationBean.addUrlPatterns("/item/*");

        return registrationBean;
    }
}

