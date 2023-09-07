package ru.practicum.exceptionhandler;

public class EventPublishedException extends RuntimeException {
    public EventPublishedException(String s) {
        super(s);
    }
}
