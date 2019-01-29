package com.capstone.exff;

import com.capstone.exff.filters.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


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
        registrationBean.addUrlPatterns("/user/*");
        registrationBean.addUrlPatterns("/item/*");

        return registrationBean;
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**");
//            }
//        };
//    }
}

