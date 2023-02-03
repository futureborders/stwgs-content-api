package uk.gov.cabinetoffice.bpdg.stwgs.cms.controller;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.NoHandlerFoundException;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.exception.ResourceNotFoundException;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalErrorController {

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
    log.info("Handling ConstraintViolationException", ex);
    final Set<String> invalidProperties =
        ex.getConstraintViolations().stream()
            .map(c -> "Invalid " + c.getPropertyPath().toString().split("\\.")[1])
            .collect(Collectors.toSet());
    final String message = String.join(", ", invalidProperties);
    return ErrorResponse.builder()
        .httpStatusCode(HttpStatus.BAD_REQUEST.value())
        .httpStatusMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .description(message)
        .build();
  }

  @ExceptionHandler({ServerWebInputException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleServerWebInputException(ServerWebInputException ex) {
    log.error("Handling ServerWebInputException", ex);
    return ErrorResponse.builder()
        .httpStatusCode(HttpStatus.BAD_REQUEST.value())
        .httpStatusMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .description("Invalid request")
        .build();
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.error("Handling MethodArgumentTypeMismatchException", ex);
    return ErrorResponse.builder()
        .httpStatusCode(HttpStatus.BAD_REQUEST.value())
        .httpStatusMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .description("Invalid request")
        .build();
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
    log.info("Handling ResourceNotFoundException", ex);
    return ErrorResponse.builder()
        .httpStatusCode(HttpStatus.NOT_FOUND.value())
        .httpStatusMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
        .description(ex.getMessage())
        .build();
  }

  @ExceptionHandler({NoHandlerFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException ex) {
    log.info("Handling NoHandlerFoundException", ex);
    return ErrorResponse.builder()
        .httpStatusCode(HttpStatus.NOT_FOUND.value())
        .httpStatusMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
        .description("No matching handler")
        .build();
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorResponse handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    log.info("Handling HttpRequestMethodNotSupportedException");
    return ErrorResponse.builder()
        .httpStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
        .httpStatusMessage(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
        .description(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
        .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public final ErrorResponse handleAllExceptions(Exception ex) {
    log.error("Handling Exception", ex);
    return ErrorResponse.builder()
        .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .httpStatusMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .description("Unexpected error")
        .build();
  }
}
