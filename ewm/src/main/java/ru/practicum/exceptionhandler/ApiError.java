package ru.practicum.exceptionhandler;

import lombok.Value;

@Value
public class ApiError {
    String errors;
    String message;
    String reason;
    String status;
    String timestamp;
}