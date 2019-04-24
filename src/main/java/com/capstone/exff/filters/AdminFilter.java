package com.capstone.exff.filters;

import com.capstone.exff.constants.ExffRole;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.UserRepository;
import com.capstone.exff.services.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = (String) request.getHeader("Authorization");
        try {
            if (token != null && !token.equals("null")) {
                UserEntity userEntity = TokenAuthenticationService.getUserFromToken(token);
                if (userEntity != null) {
                    if (userEntity.getRoleByRoleId().getName().equals(ExffRole.ROLE_ADMIN)) {
                        request.setAttribute("USER_INFO", userEntity);
                        filterChain.doFilter(request, response);
                    } else {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.getWriter().write("{\"message\":\"Access denied\"}");
                    }
                } else {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("{\"message\":\"Access denied\"}");
                }
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("{\"message\":\"Access denied\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("{\"message\":\"Access denied\"}");
        }

    }

    @Override
    public void destroy() {

    }
}
