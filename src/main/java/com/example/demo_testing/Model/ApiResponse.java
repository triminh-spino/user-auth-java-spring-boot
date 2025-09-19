package com.example.demo_testing.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponse<T> {

    private int status;

    private String message;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private T data;
    
}
