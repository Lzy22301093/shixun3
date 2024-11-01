package com.icplatform.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Calendar;

import java.util.Calendar;

public class JWTUtil {
    private static final String SECRET = "!DAR$";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public static String generateToken(int userType, String username) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 60000);

        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("usertype", userType);
        builder.withClaim("username", username);
        String token = builder.withExpiresAt(calendar.getTime()).sign(algorithm);
        System.out.println(token);
        return token;
    }

    public static DecodedJWT verifyToken(String token) throws TokenExpiredException {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            System.out.println(decodedJWT.getClaim("usertype").asInt());
            System.out.println(decodedJWT.getClaim("username").asString());
            System.out.println(decodedJWT.getExpiresAt());

            return decodedJWT;
        }
        catch(TokenExpiredException e) {
            throw e;
        }
        catch(Exception e) {
            return null;
        }
    }
}

