package com.rey.userService.config;

import com.rey.userService.serviceImpl.JWTService;
import com.rey.userService.serviceImpl.UserServiceDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService service;
    private final ApplicationContext context;


    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/users/register",
            "/api/v1/users/login",
            "/api/v1/users/confirmAccount"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // skip JWT validation for registration and other public endpoints
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(p -> path.equals(p) || path.startsWith(p + "/"));
        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            username = service.extractUserName(token);
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = context.getBean(UserServiceDetails.class).loadUserByUsername(username);

            Claims claims = service.extractAllClaims(token);

            List<GrantedAuthority> authorities = ((List<String>) claims.get("authorities"))
                    .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());


            if (service.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);
                token1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token1);
            }
        }
        filterChain.doFilter(request, response);

    }
    }

