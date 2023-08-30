package ru.practicum.exceptionhandler;

public class IllegalDataException extends RuntimeException {
    public IllegalDataException(String s) {
        super(s);
    }
}
