package ru.practicum.exceptionhandler;

public class CommentConflictException extends RuntimeException {
    public CommentConflictException(String s) {
        super(s);
    }
}
