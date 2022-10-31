package com.dkwasniak.slr_spot_backend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dkwasniak.slr_spot_backend.role.Role;
import com.dkwasniak.slr_spot_backend.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JwtUtils {

    private final static String SECRET = "secret";
    private final static String ROLES_CLAIM = "roles";
    private final static long JWT_EXPIRATION= 1 * 60 * 1000;
    private final static long REFRESH_TOKEN_EXPIRATION = 2 * 60 * 1000;
    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    public static DecodedJWT validateJwt(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwt);
    }

    public static String getUsername(DecodedJWT jwt) {
        return jwt.getSubject();
    }

    public static Collection<SimpleGrantedAuthority> getAuthorities(DecodedJWT jwt) {
        String[] roles = jwt.getClaim(ROLES_CLAIM).asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }

    public static String generateJwt(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLES_CLAIM, user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static String generateRefreshToken(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
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
}
