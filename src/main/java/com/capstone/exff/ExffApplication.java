package com.capstone.exff;

import com.capstone.exff.filters.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


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
        registrationBean.addUrlPatterns("/user/*", "/relationship/*");
        registrationBean.addUrlPatterns("/item/*");
        registrationBean.addUrlPatterns("/image/*");
        registrationBean.addUrlPatterns("/transaction/*");
        registrationBean.addUrlPatterns("/donationPost/*");

        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT", "OPTION"));
        config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
//    @Bean
//    public FilterRegistrationBean<JwtFilter> corsFilter(){
//        FilterRegistrationBean registrationBean
//                = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(new JwtFilter());
//        registrationBean.addUrlPatterns("/**");
//
//        return registrationBean;
//    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

