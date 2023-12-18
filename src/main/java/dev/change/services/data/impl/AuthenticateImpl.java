package dev.change.services.data.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import dev.change.services.data.Authenticate;

import java.util.List;

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
