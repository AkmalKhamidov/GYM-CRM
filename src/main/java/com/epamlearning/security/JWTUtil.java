package com.epamlearning.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Scope(scopeName = "singleton")
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(30).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("epamlearning.com")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public Date getExpirationDateFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getExpiresAt();
    }

    public Date getIssuedAtDateFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getIssuedAt();
    }

    public String validateTokenRetrieveUsername(String token) {
        return JWT.require(Algorithm.HMAC256("NOT SECRET"))
                .build()
                .verify(token)
                .getClaim("username")
                .asString();
    }

}
