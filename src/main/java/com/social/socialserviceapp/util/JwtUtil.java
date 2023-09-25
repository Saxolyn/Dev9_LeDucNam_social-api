package com.social.socialserviceapp.util;

import com.social.socialserviceapp.model.entities.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final String secret_key = "hellojwt";
    private long accessTokenValidity = 60 * 60 * 1000;
    private final JwtParser jwtParser;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(){
        this.jwtParser = Jwts.parser()
                .setSigningKey(secret_key);
    }

    public String createToken(User user){
        Claims claims = Jwts.claims()
                .setSubject(user.getUsername());
        claims.put("email", user.getEmail());
        Date tokenValidity = new Date(System.currentTimeMillis() + accessTokenValidity);
        return Jwts.builder()
                .setHeaderParam("typ", Header.JWT_TYPE)
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .setIssuedAt(new Date())
                .compact();
    }

    private Claims parseJwtClaims(String token){
        return jwtParser.parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public Claims resolveClaims(HttpServletRequest request){
        try {
            String token = resolveToken(request);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException e) {
            request.setAttribute("expired", e.getMessage());
            throw e;
        } catch (Exception e) {
            request.setAttribute("invalid", e.getMessage());
            throw e;
        }
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException{
        try {
            return claims.getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getUsername(Claims claims){
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims){
        return (List<String>) claims.get("roles");
    }

}
