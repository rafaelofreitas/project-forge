package com.projectforge.template.infrastructure.api.exceptionhandler;

import com.projectforge.template.application.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponseDTO> handleBaseException(BaseException ex) {
    final ErrorResponseDTO errorResponse =
        ErrorResponseDTO.builder()
            .status(ex.getStatus())
            .message(ex.getMessage())
            .errors(List.of(new ErrorResponseDTO.ErrorDTO(ex.getDetail())))
            .build();

    return ResponseEntity.status(HttpStatus.valueOf(errorResponse.status())).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
      final MethodArgumentNotValidException ex) {
    final List<ErrorResponseDTO.ErrorDTO> errors =
        ex.getBindingResult().getAllErrors().stream()
            .map(error -> new ErrorResponseDTO.ErrorDTO(getErrorMessage(error)))
            .sorted(Comparator.comparing(ErrorResponseDTO.ErrorDTO::message))
            .toList();

    final ErrorResponseDTO errorResponse =
        ErrorResponseDTO.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("Validation Failed")
            .errors(errors)
            .build();

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex) {
    final ErrorResponseDTO errorResponse =
        ErrorResponseDTO.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Internal Server Error")
            .errors(List.of(new ErrorResponseDTO.ErrorDTO(ex.getMessage())))
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private String getErrorMessage(final ObjectError error) {
    return error.getDefaultMessage() != null
        ? error.getDefaultMessage()
        : "Unknown validation error";
  }
}
