package com.dkwasniak.slr_spot_backend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkwasniak.slr_spot_backend.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JwtUtils {

    private final static long JWT_EXPIRATION = TimeUnit.DAYS.toMillis(7L);
    private final static long REFRESH_TOKEN_EXPIRATION = TimeUnit.DAYS.toMillis(31L);
    private static final String AUTHORIZATION_PREFIX = "Bearer ";
    private static String SECRET_STATIC;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public static DecodedJWT validateJwt(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_STATIC.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwt);
    }

    public static String getUsername(DecodedJWT jwt) {
        return jwt.getSubject();
    }

    public static String generateJwt(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_STATIC.getBytes());
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public static String generateRefreshToken(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_STATIC.getBytes());
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public static boolean validateHeader(String authorizationHeader) {
        return !StringUtils.isEmpty(authorizationHeader)
                && authorizationHeader.startsWith(AUTHORIZATION_PREFIX);
    }

    public static String extractJwtFromHeader(String authorizationHeader) {
        return authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
    }

    @Value("${jwt.secret}")
    public void setSecretStatic(String jwtSecret){
        JwtUtils.SECRET_STATIC = jwtSecret;
    }
}
