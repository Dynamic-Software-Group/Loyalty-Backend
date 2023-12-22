package dev.change.services.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.change.beans.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class JwtService {
    public static String generateJwt(String id) throws IOException {
//        String secret = Runtime.getRuntime().exec("doppler secrets get JWT_SECRET --plain").toString();
        String secret = "test"; //TODO: TEMP
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("id", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .sign(algorithm);
    }
}
