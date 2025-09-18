package com.example.demo_testing.Util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.example.demo_testing.Model.ApiResponse;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Response {
    public static ApiResponse createResponse(HttpStatus status, Object data) {
        return ApiResponse.builder()
            .status(status.value())
            .message(status.getReasonPhrase())
            .timestamp(LocalDateTime.now())
            .data(data)
            .build();
    }
}
