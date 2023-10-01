package com.social.socialserviceapp.security;

import com.social.socialserviceapp.model.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private final String jwtSecret;
    private final long jwtExpiration;
    private static final String AUTHORITIES_CLAIM = "authorities";

    public JwtTokenProvider(
            @Value("${social.jwt.secret}") String jwtSecret, @Value("${social.jwt.expiration}") long jwtExpiration){
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    public String createToken(CustomUserDetails customUserDetails){
        String authorities = this.getUserAuthorities(customUserDetails);
        return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .claim(AUTHORITIES_CLAIM, authorities)
                .setHeaderParam("typ", Header.JWT_TYPE)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setIssuedAt(new Date())
                .compact();
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    public List<GrantedAuthority> getAuthoritiesFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Arrays.stream(claims.get(AUTHORITIES_CLAIM)
                        .toString()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private String getUserAuthorities(CustomUserDetails customUserDetails){
        return customUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

}
