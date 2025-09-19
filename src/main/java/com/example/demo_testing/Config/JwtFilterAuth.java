package com.example.demo_testing.Config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo_testing.Enum.TokenType;
import com.example.demo_testing.Exception.BadRequestException;
import com.example.demo_testing.Exception.ForbiddenException;
import com.example.demo_testing.Exception.NotFoundException;
import com.example.demo_testing.Exception.UnauthorizedException;
import com.example.demo_testing.Service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilterAuth extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
 
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;
 
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
 
            TokenType tokenType = TokenType.BEARER;
 
            jwt = authHeader.substring(7);
 
            try {

                if (jwtService.checkTokenRevoked(jwt, tokenType)) {
               
                    userEmail = jwtService.extractUserName(jwt, tokenType);
   
                    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
       
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
       
                        if (jwtService.isTokenValid(jwt, userDetails, tokenType)) {
       
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
       
                            authenticationToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );
       
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
                
            } catch (UnauthorizedException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            } catch (ForbiddenException | BadRequestException | NotFoundException e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                return;
            }
        }
       
        filterChain.doFilter(request, response);
    }
}