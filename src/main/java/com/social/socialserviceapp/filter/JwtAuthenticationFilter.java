package com.social.socialserviceapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.socialserviceapp.exception.SocialAppException;
import com.social.socialserviceapp.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null) {
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                objectMapper.writeValue(response.getWriter(),
//                        Response.error(Constants.RESPONSE_CODE.FORBIDDEN, "Please login first!"));
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println(">>>Token: " + accessToken);
            Claims claims = jwtUtil.resolveClaims(request);
            if (claims != null && jwtUtil.validateClaims(claims)) {
                String username = claims.getSubject();
                System.out.println(">>>Username: " + username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, "",
                        new ArrayList<>());
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (IOException | AuthenticationException e) {
            throw new SocialAppException(HttpStatus.UNAUTHORIZED.name(), "Authentication Error");
//
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            objectMapper.writeValue(response.getWriter(),
//                    Response.error(Constants.RESPONSE_CODE.FORBIDDEN, "Authentication Error")
//                            .withData(e.getMessage()));
        }
        filterChain.doFilter(request, response);
    }
}
