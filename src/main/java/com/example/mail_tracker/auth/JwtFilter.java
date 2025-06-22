package com.example.mail_tracker.auth;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String path = request.getRequestURI();


        // ✅ Skip JWT filter for public routes
        if (path.equals("/track") ||
                path.startsWith("/connect") ||
                path.startsWith("/oauth") ||
                path.equals("/login") ||
                path.equals("/register")) {
            System.out.println("🔍 JWT Filter: Skipping authentication for path: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 JWT Filter: Processing request for path: " + authHeader);
        String token = null;
        String email = null;

        System.out.println("Incoming request: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtService.getEmailFromToken(token);
                System.out.println("Extracted email: " + email);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                System.out.println("⚠️ Token expired: " + e.getMessage());
                // Optional: You can choose to return a 401 or just proceed unauthenticated
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(email);

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("User found: " + userDetails);

            }
        }

        filterChain.doFilter(request, response);
        System.out.println("Response status: " + response.getStatus());
    }

}
