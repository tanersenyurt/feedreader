package com.tsenyurt.csdm.service.data.impl.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FeedReadException extends Exception{

  public FeedReadException(String message) {
    super(message);
  }
}
