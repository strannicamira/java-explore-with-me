package ru.practicum.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
//        return new ErrorResponse(e.getMessage());
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ErrorResponse handleValidationException(final ValidationException e) {
//        return new ErrorResponse(e.getMessage());
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
//        return new ErrorResponse(e.getMessage());
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ErrorResponse handleIllegalStateException(final IllegalStateException e) {
//        return new ErrorResponse(e.getMessage());
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT) // 409
//    public ErrorResponse handleSqlExceptionHelper(final DataIntegrityViolationException e) {
//        //instead DuplicateEmailFoundException
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleIllegalDataException(final IllegalDataException e) {
        return new ErrorResponse(e.getMessage());
    }

    //TODO: handle MissingServletRequestParameterException in case of /stats requests without start or end
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
//    public ErrorResponse handleThrowable(final Throwable e) {
//        return new ErrorResponse("Произошла непредвиденная ошибка." + e.toString() + "\nmessage: " + e.getMessage() + "\ncouse:" + e.getCause());
//    }
}