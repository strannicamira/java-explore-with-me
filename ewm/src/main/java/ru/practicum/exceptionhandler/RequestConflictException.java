package ru.practicum.exceptionhandler;

public class RequestConflictException extends RuntimeException {
    public RequestConflictException(String s) {
        super(s);
    }
}
