package ru.practicum.exceptionhandler;

public class RequestException extends RuntimeException {
    public RequestException(String s) {
        super(s);
    }
}
