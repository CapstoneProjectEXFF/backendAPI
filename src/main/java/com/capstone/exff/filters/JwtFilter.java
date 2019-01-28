package com.capstone.exff.filters;

import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.TokenAuthenticationService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class JwtFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = (String) request.getHeader("Authorization");
        if (token != null) {
            UserEntity userEntity = TokenAuthenticationService.getUserFromToken(token);
            request.setAttribute("USER_INFO", userEntity);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("{\"message\":\"Access denied\"}");
        }
    }

    @Override
    public void destroy() {

    }

}
