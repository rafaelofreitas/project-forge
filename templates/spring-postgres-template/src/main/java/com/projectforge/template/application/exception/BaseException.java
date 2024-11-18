package com.projectforge.template.application.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

  private final int status;
  private final String message;
  private final String detail;

  protected BaseException(int status, String message, String detail) {
    super(message);
    this.status = status;
    this.message = message;
    this.detail = detail;
  }

  protected BaseException(int status, String message) {
    super(message);
    this.status = status;
    this.message = message;
    this.detail = "No additional details available.";
  }
}
