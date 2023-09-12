package ru.practicum.exceptionhandler;

public class EventConflictException extends RuntimeException {
    public EventConflictException(String s) {
        super(s);
    }
}
