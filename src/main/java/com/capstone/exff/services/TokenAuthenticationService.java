package com.capstone.exff.services;

import com.capstone.exff.entities.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

public class TokenAuthenticationService {
    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "ExffSecrect";
    static final String TOKEN_PREFIX = "Bearer";
    static public final String HEADER_STRING = "Authorization";

    public static String createToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + token;
    }

    public static String createToken(UserEntity userEntity) {
        ObjectMapper mapper = new ObjectMapper();
        UserEntity userAuthenInfo = new UserEntity();
        userAuthenInfo.setId(userEntity.getId());
        userAuthenInfo.setPhoneNumber(userEntity.getPhoneNumber());
        userAuthenInfo.setRoleByRoleId(userEntity.getRoleByRoleId());
        String json;
        try {
            json = mapper.writeValueAsString(userAuthenInfo);
        } catch (JsonProcessingException e) {
            json = userAuthenInfo.getId() + " " + userAuthenInfo.getPhoneNumber();
        }
        String token = Jwts.builder()
                .setSubject(json)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + token;
    }

    public static boolean checkToken(String username, String token) {
        boolean res = false;
        try {
            String user = getInfoFromToken(token);
            res = user.equals(username);
        } catch (Exception e) {
        }
        return res;
    }

    public static String getInfoFromToken(String token) {
        String res = null;
        try {
            res = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
        }
        return res;
    }

    public static UserEntity getUserFromToken(String token) {
        UserEntity userEntity = null;

        String res = getInfoFromToken(token);
        ObjectMapper mapper = new ObjectMapper();
        try {
            userEntity = mapper.readValue(res, UserEntity.class);
        } catch (IOException e) {
            StringTokenizer stringTokenizer = new StringTokenizer(res, " ");
            userEntity = new UserEntity();
            String id = stringTokenizer.nextToken();
            try {
                userEntity.setId(Integer.parseInt(id));
            } catch (Exception ex){
            }
            userEntity.setPhoneNumber(stringTokenizer.nextToken());
        }
        return userEntity;
    }

}
