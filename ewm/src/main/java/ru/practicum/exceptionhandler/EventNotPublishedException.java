package ru.practicum.exceptionhandler;

public class EventNotPublishedException extends RuntimeException {
    public EventNotPublishedException(String s) {
        super(s);
    }
}
