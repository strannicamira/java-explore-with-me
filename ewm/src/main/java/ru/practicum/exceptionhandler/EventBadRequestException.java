package ru.practicum.exceptionhandler;

public class EventBadRequestException extends RuntimeException {
    public EventBadRequestException(String s) {
        super(s);
    }
}
