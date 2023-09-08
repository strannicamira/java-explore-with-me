package ru.practicum.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleNotAvailableException(final NotAvailableException e) {
        return new ErrorResponse(e.getMessage());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ErrorResponse handleIllegalStateException(final IllegalArgumentException e) {
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleIllegalStateException(final IllegalStateException e) {
        return new ErrorResponse(e.getMessage());
    }

    //TODO: add handle NumberFormatException for
    // GET /categories
    // GET /categories/{catId}
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ErrorResponse handleNumberFormatException(final NumberFormatException e) {
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleEventBadRequestException(final EventBadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleNotOwnerException(final NotOwnerException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleEventNotPublishedExceptionHelper(final EventNotPublishedException e) {
        return new ErrorResponse(e.getMessage());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT) // 409
//    public ErrorResponse handleDuplicateEmailFoundException(final DuplicateEmailFoundException e) {
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ErrorResponse handleSqlExceptionHelper(final DataIntegrityViolationException e) {
        //instead DuplicateEmailFoundException
        return new ErrorResponse(e.getMessage());
    }

//TODO: add handle ConstraintViolationException for
// POST /admin/categories
// PATCH /admin/categories/{catId}

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT) // 409
//    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ErrorResponse handleRequestExceptionHelper(final RequestConflictException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ErrorResponse handleEventPublishedExceptionHelper(final EventConflictException e) {
        return new ErrorResponse(e.getMessage());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
//    public ErrorResponse handleThrowable(final Throwable e) {
//        return new ErrorResponse("Произошла непредвиденная ошибка." + e.toString() + "\nmessage: " + e.getMessage() + "\ncouse:" + e.getCause());
//    }
}
