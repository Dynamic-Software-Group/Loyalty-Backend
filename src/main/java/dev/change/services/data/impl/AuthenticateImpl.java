package dev.change.services.data.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.change.services.data.Authenticate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AuthenticateImpl<ID> implements Authenticate<String> {

    @Override
    public abstract String getJwt();

    @Override
    public abstract void setJwt(String jwt);

    @Override
    public String genJwt(String email, List<String> authorities) {
        return JWT.create()
            .withSubject(email)
            .withClaim("authorities", authorities)
            .sign(Algorithm.HMAC256(System.getenv("JWT_SECRET"))); //TODO: set this in the environment
    }
}
