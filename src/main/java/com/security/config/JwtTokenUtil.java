package com.security.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.DatatypeConverter;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@PropertySource(value={"classpath:application.properties"})
public class JwtTokenUtil {

    @Value("${jwt.token.signature:default}")
    private String key;

    @Value("${jwt.token.expiration:default}")
    private int expirationTime;

    //convert string key to array of bytes
    private byte[] getConvertedBinaryKey(String key) {
        String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
        return DatatypeConverter.parseBase64Binary(base64Key);
    }

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public List<GrantedAuthority> getGrantedAuthoritiesFromToken(String token) {
        var userRoles = ((List<?>) getClaimFromToken(token, claims -> claims.get("roles")))
                .stream()
                .map(role -> Role.valueOf((String) role))
                .toList();

        var userStatus = AccountStatus.valueOf((String) getClaimFromToken(token, claims -> claims.get("status")));
        var userGrantedAuthorities = new ArrayList<GrantedAuthority>(userRoles);
        userGrantedAuthorities.add(userStatus);

        return userGrantedAuthorities;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(getConvertedBinaryKey(key)).parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException jwtException) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token signature or token is expired! In result this token cannot be trusted.");
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .filter(authority -> authority instanceof Role)
                .map(authority -> ((Role) authority).name())
                .toList();

        String status = userDetails.getAuthorities()
                .stream()
                .filter(authority -> authority instanceof AccountStatus)
                .map(authority -> ((AccountStatus) authority).getAuthority())
                .findFirst().orElse("CREATED");

        claims.put("roles", roles);
        claims.put("status", status);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        var now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expirationTime, MINUTES)))
                .signWith(SignatureAlgorithm.HS512, getConvertedBinaryKey(key))
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}