package com.projectforge.template.application.exception;

public class UserNotFoundException extends BaseException {

  public UserNotFoundException(final Long userId) {
    super(404, String.format("User with ID %d not found.", userId));
  }
}
