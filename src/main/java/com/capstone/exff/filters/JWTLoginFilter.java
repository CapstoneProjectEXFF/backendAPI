package com.capstone.exff.filters;

import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.TokenAuthenticationService;
import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    public JWTLoginFilter(String url, AuthenticationManager antManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(antManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        UserEntity userEntity = getUserData(request);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        userEntity.getPhoneNumber(),
                        userEntity.getPassword(),
                        Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        TokenAuthenticationService.addAuthentication(response, authResult.getName());
    }

    private UserEntity getUserData(HttpServletRequest request) {
        UserEntity userEntity = new UserEntity();
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            try(BufferedReader bufferedReader = request.getReader()) {
                Gson gson = new Gson();
                userEntity = gson.fromJson(bufferedReader, UserEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            userEntity.setPhoneNumber(request.getParameter("phoneNumber"));
            userEntity.setPassword(request.getParameter("password"));
        }
        return userEntity;
    }
}
