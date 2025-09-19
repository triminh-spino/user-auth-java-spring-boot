package com.example.demo_testing.Exception;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo_testing.Model.ApiResponse;
import com.example.demo_testing.Util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
        HttpServletRequest request, 
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        ApiResponse apiResponse = Response.createResponse(HttpStatus.FORBIDDEN, Collections.singletonList(accessDeniedException.getMessage()));
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (ServletOutputStream out = response.getOutputStream()) {
            new ObjectMapper().writeValue(out, apiResponse);
        }
    }
}
